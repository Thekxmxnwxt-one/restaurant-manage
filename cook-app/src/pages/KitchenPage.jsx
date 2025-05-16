import React, { useEffect, useState } from 'react';
import '../styles/KitchenPage.css';

function KitchenPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // ฟังก์ชันดึงรายละเอียด order (รวม items)
  const fetchOrderDetails = async (orderId) => {
    const res = await fetch(`http://localhost:8080/api/orders/${orderId}`);
    if (!res.ok) throw new Error('Failed to fetch order details for order ' + orderId);
    const data = await res.json();
    if (data.status !== 200) throw new Error(data.description || 'Error fetching order details');
    return data.data;
  };

  useEffect(() => {
    async function fetchOrdersWithItems() {
      try {
        const res = await fetch('http://localhost:8080/api/orders');
        if (!res.ok) throw new Error('Failed to fetch orders');
        const data = await res.json();

        if (data.status !== 200) {
          throw new Error(data.description || 'Error fetching orders');
        }

        // กรอง order ที่ต้องทำในครัว
        const kitchenOrdersBasic = data.data.filter(order =>
          order.status === 'pending' || order.status === 'cooking' || order.status === 'processing'
        );

        // ดึงรายละเอียด items ของแต่ละ order เพิ่มเติม
        const kitchenOrdersFull = await Promise.all(
          kitchenOrdersBasic.map(async (order) => {
            const fullOrder = await fetchOrderDetails(order.id);
            return {
              ...order,
              items: fullOrder.items || []
            };
          })
        );

        setOrders(kitchenOrdersFull);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    }

    fetchOrdersWithItems();
  }, []);

  const handleUpdateStatus = async (orderId, itemId, newStatus) => {
  try {
    // อัปเดตสถานะ item
    const res = await fetch('http://localhost:8080/api/orders/items', {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: itemId, status: newStatus }),
    });

    if (!res.ok) throw new Error('Failed to update item status');
    const data = await res.json();
    if (data.status !== 200) throw new Error(data.description);

    // ดึง order ใหม่จาก backend (เพื่อให้แน่ใจว่า item อัปเดตจริง)
    const orderRes = await fetch(`http://localhost:8080/api/orders/${orderId}`);
    if (!orderRes.ok) throw new Error('Failed to fetch updated order');
    const orderData = await orderRes.json();
    if (orderData.status !== 200) throw new Error(orderData.description);

    const updatedOrder = orderData.data;

    // ถ้า items ทุกตัวเป็น served → อัปเดตสถานะ order
    console.log('รายการอาหารในออเดอร์:', updatedOrder.items.map(i => ({ id: i.id, status: i.status })));
    const allServed = updatedOrder.items.every(item => item.status === 'served');
    console.log('ตรวจสอบสถานะ items ทุกตัว served:', allServed, updatedOrder.items);
    if (allServed) {
      console.log('เมนูทั้งหมดเสิร์ฟแล้ว → อัปเดต order status เป็น served');

      const orderStatusRes = await fetch('http://localhost:8080/api/orders/status', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: orderId, status: 'served' }),
      });

      if (!orderStatusRes.ok) throw new Error('Failed to update order status');
      const statusData = await orderStatusRes.json();
      if (statusData.status !== 200) throw new Error(statusData.description);

      // ลบออกจาก orders ในหน้า
      setOrders(prev => prev.filter(o => o.id !== orderId));
    } else {
      // ถ้ายังไม่ครบ served → แค่อัปเดต item ใน state
      setOrders(prevOrders =>
        prevOrders.map(order => {
          if (order.id === orderId) {
            const updatedItems = order.items.map(item =>
              item.id === itemId ? { ...item, status: newStatus } : item
            );
            return { ...order, items: updatedItems };
          }
          return order;
        })
      );
    }
  } catch (err) {
    alert('เกิดข้อผิดพลาด: ' + err.message);
  }
};




  if (loading) return <div className="kitchen-page"><p>Loading orders...</p></div>;
  if (error) return <div className="kitchen-page"><p>Error: {error}</p></div>;

  return (
    <div className="kitchen-page">
      <h2>ครัว - Order ที่ต้องทำ</h2>
      {orders.length === 0 ? (
        <p>ไม่มีคำสั่งอาหารที่ต้องทำในขณะนี้</p>
      ) : (
        orders.map(order => (
          <div key={order.id} style={{ marginBottom: '25px' }}>
            <h3>โต๊ะ: {order.table?.tableNumber || order.tableId} (Order ID: {order.id})</h3>
            <table>
              <thead>
                <tr>
                  <th>รายการอาหาร</th>
                  <th>จำนวน</th>
                  <th>สถานะ</th>
                  <th>อัปเดตสถานะ</th>
                </tr>
              </thead>
              <tbody>
                {order.items.length > 0 ? (
                  order.items.map(item => (
                    <tr key={item.id}>
                      <td>{item.menuItem?.name || 'ไม่ระบุเมนู'}</td>
                      <td>{item.quantity}</td>
                      <td>{item.status}</td>
                      <td>
  {item.status === 'served' ? (
    <span>เสร็จสิ้น</span>
  ) : item.status === 'ready' ? (
    <span>รอเสิร์ฟ</span>
  ) : (
    <>
      {item.status !== 'cooking' && (
        <button
          onClick={() => handleUpdateStatus(order.id, item.id, 'cooking')}
          style={{ marginRight: '8px' }}
        >
          กำลังทำ
        </button>
      )}
      <button onClick={() => handleUpdateStatus(order.id, item.id, 'ready')}>
        ทำเสร็จแล้ว
      </button>
    </>
  )}
</td>


                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="4" style={{ textAlign: 'center' }}>
                      ไม่มีรายการอาหาร
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        ))
      )}
    </div>
  );
}

export default KitchenPage;
