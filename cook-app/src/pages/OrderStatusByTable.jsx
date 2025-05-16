import React, { useEffect, useState } from 'react';
import '../styles/OrderStatus.css';
import Navbar from '../components/Navbar';

const OrderStatusByTable = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // โหลด order ทั้งหมดที่ยังไม่ปิด (status != served หรือ closedAt == null)
  const fetchOrders = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await fetch('http://localhost:8080/api/orders');
      const data = await res.json();
      if (data.status === 200) {
        // filter เฉพาะ order ที่ status ยังไม่เสร็จ (ตัวอย่าง: pending, processing, ready)
        const activeOrders = data.data.filter(o => o.status !== 'served');
        // เพื่อให้แสดงรายการอาหาร ต้องดึงรายละเอียด order แต่ละตัวเพิ่มเติม
        const detailedOrders = await Promise.all(
          activeOrders.map(async (order) => {
            const detailRes = await fetch(`http://localhost:8080/api/orders/${order.id}`);
            const detailData = await detailRes.json();
            return detailData.data;
          })
        );
        setOrders(detailedOrders);
      } else {
        setError('โหลดข้อมูลออร์เดอร์ล้มเหลว');
      }
    } catch (err) {
      setError('เกิดข้อผิดพลาดในการโหลดออร์เดอร์');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  // ฟังก์ชันเปลี่ยนสถานะ order-item เป็น served
  const handleMarkServed = async (orderItemId, orderId) => {
  setError('');
  setSuccess('');
  try {
    const res = await fetch('http://localhost:8080/api/orders/items', {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: orderItemId,
        status: 'served'
      })
    });
    const data = await res.json();
    if (data.status === 200) {
      // ตรวจสอบว่า order ทั้งหมดเสิร์ฟหรือยัง
      const detailRes = await fetch(`http://localhost:8080/api/orders/${orderId}`);
      const detailData = await detailRes.json();
      const allServed = detailData.data.items.every(item => item.status === 'served');
      if (allServed) {
        // ถ้าใช่ให้อัปเดตสถานะของ order เป็น served ด้วย
        await fetch(`http://localhost:8080/api/orders/status`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ id: orderId, status: 'served' })
        });
      }

      setSuccess(`อัพเดตสถานะเมนูสำเร็จ (id: ${orderItemId})`);
      fetchOrders(); // รีเฟรชข้อมูล
    } else {
      setError('อัพเดตสถานะเมนูล้มเหลว');
    }
  } catch {
    setError('เกิดข้อผิดพลาดในการอัพเดตสถานะเมนู');
  }
};


  const handleMarkOrderServed = async (orderId) => {
  setError('');
  setSuccess('');
  try {
    const res = await fetch(`http://localhost:8080/api/orders/status`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: orderId, status: 'served' })
    });
    const data = await res.json();
    if (data.status === 200) {
      setSuccess(`อัพเดตสถานะออร์เดอร์สำเร็จ (โต๊ะ ${orderId})`);
      fetchOrders();
    } else {
      setError('อัพเดตสถานะออร์เดอร์ล้มเหลว');
    }
  } catch {
    setError('เกิดข้อผิดพลาดในการอัพเดตออร์เดอร์');
  }
};


  return (
    <>
    <Navbar />
    <div className="order-status-container">
      <h1>สถานะรายการอาหารตามโต๊ะ</h1>
      {loading && <p>กำลังโหลดข้อมูล...</p>}
      {error && <p className="error">{error}</p>}
      {success && <p className="success">{success}</p>}

      {orders.length === 0 && !loading && <p>ไม่มีรายการอาหารที่ต้องแสดง</p>}

      {orders.map(order => {
  const allItemsServed = order.items.every(item => item.status === 'served');
  return (
    <div key={order.id} className="order-table">
      <div className="order-header">
        <h3>โต๊ะ {order.tableId} (สถานะออร์เดอร์: {order.status})</h3>
        {allItemsServed && order.status !== 'served' && (
          <button
            className="btn-order-serve"
            onClick={() => handleMarkOrderServed(order.id)}
          >
            ได้รับอาหารครบแล้ว
          </button>
        )}
      </div>
      <ul>
        {order.items && order.items.length > 0 ? order.items.map(item => (
          <li key={item.id} className={`order-item status-${item.status}`}>
            <strong>{item.menuItem?.name || 'ไม่ทราบชื่อเมนู'}</strong> - สถานะ: {item.status}
            {item.status === 'ready' && (
              <button
                className="btn-serve"
                onClick={() => handleMarkServed(item.id, order.id)}
              >
                เสิร์ฟ
              </button>
            )}
          </li>
        )) : <li>ไม่มีรายการอาหาร</li>}
      </ul>
    </div>
  );
})}

    </div>
    </>
  );
};

export default OrderStatusByTable;
