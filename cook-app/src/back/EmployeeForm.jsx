import React, { useEffect, useState } from 'react';
import '../styles/EmployeeAssignTable.css'

const TableOrdersStatus = () => {
  const [groupedCustomers, setGroupedCustomers] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchGroupedOrders();
  }, []);

  const fetchGroupedOrders = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/customers');
      const data = await res.json();

      if (data.status === 200) {
        const activeCustomers = data.data.filter(c => c.status !== 'done');

        const grouped = activeCustomers.map(c => {
          const allItems = c.orders?.flatMap(order => order.orderItems) || [];
          return {
            customerId: c.id,
            tableNumber: c.table.tableNumber,
            customerName: c.name,
            orderItems: allItems,
          };
        });

        // รวมกลุ่มตาม customerId + tableNumber (กรณีมีข้อมูลซ้ำ)
        const merged = [];
        grouped.forEach(g => {
          const key = `${g.customerId}-${g.tableNumber}`;
          const existing = merged.find(m => m.key === key);

          if (existing) {
            existing.orderItems.push(...g.orderItems);
          } else {
            merged.push({ ...g, key });
          }
        });

        setGroupedCustomers(merged);
      } else {
        setError('ไม่สามารถโหลดข้อมูลลูกค้าและออร์เดอร์ได้ (status not 200)');
      }
    } catch (err) {
      console.error(err);
      setError('ไม่สามารถโหลดข้อมูลลูกค้าและออร์เดอร์ได้');
    }
  };

  const handleServe = async (orderItemId) => {
    try {
      console.log('เริ่มอัปเดต order item:', orderItemId);

      // 1. อัปเดต order item เป็น served
      const res = await fetch(`http://localhost:8080/api/order_items/${orderItemId}/status`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status: 'served' }),
      });
      const data = await res.json();
      console.log('Response update order item:', data);

      if (data.status === 200) {
        const orderId = data.data.orderId;
        console.log('Order ID ที่จะตรวจสอบ:', orderId);

        // 2. ดึงข้อมูล order ล่าสุด
        const orderRes = await fetch(`http://localhost:8080/api/orders/${orderId}`);
const orderData = await orderRes.json();

console.log('ดึงข้อมูล order หลัง update:', orderData);

if (orderData.status === 200) {
  const updatedOrder = orderData.data;

  // แสดงสถานะ order item แต่ละตัว
  updatedOrder.items.forEach(item => {
    console.log(`Order item ${item.id} status:`, item.status);
  });

  const allServed = updatedOrder.items.every(
    item => item.status && item.status.toLowerCase().trim() === 'served'
  );
  console.log('ตรวจสอบ served ครบ:', allServed);

  if (allServed) {
    console.log('จะเรียก API update order status');
    const updateOrderRes = await fetch('http://localhost:8080/api/orders/status', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: orderId, status: 'served' }),
    });
    const updateOrderData = await updateOrderRes.json();
    console.log('Response update order:', updateOrderData);
  }
}
 else {
          console.error('ดึงข้อมูล order ไม่สำเร็จ', orderData);
          setError('ไม่สามารถโหลดข้อมูล order ได้');
        }

        // 4. โหลดข้อมูลใหม่เพื่ออัปเดต UI
        await fetchGroupedOrders();
      } else {
        console.error('อัปเดต order item ล้มเหลว:', data);
        setError('อัปเดตสถานะ order item ไม่สำเร็จ');
      }
    } catch (err) {
      console.error('Error in handleServe:', err);
      setError('ไม่สามารถอัปเดตสถานะเมนูได้');
    }
  };

  return (
    <div className="table-orders-container">
      <h2>รายการอาหารของลูกค้าแต่ละโต๊ะ</h2>
      {error && <p className="error">{error}</p>}
      {groupedCustomers.length === 0 ? (
        <p>ไม่มีลูกค้าที่กำลังนั่งอยู่</p>
      ) : (
        groupedCustomers.map(group => (
          <div key={group.key} className="table-order-box">
            <h3>โต๊ะ {group.tableNumber} - {group.customerName}</h3>
            <ul>
              {group.orderItems.map(item => (
                <li key={item.id}>
                  {item.menu.name} - <strong>{item.status}</strong>
                  {item.status === 'ready' && (
                    <button
                      onClick={() => handleServe(item.id)}
                      className="serve-button"
                      type="button"
                    >
                      เสิร์ฟแล้ว
                    </button>
                  )}
                </li>
              ))}
            </ul>
          </div>
        ))
      )}
    </div>
  );
};

export default TableOrdersStatus;
