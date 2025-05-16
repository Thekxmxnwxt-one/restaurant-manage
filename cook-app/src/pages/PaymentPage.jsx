import React, { useEffect, useState } from 'react';
import '../styles/PaymentPage.css';
import Navbar from '../components/Navbar';

function PaymentPage() {
  const [orders, setOrders] = useState([]);
  const [paidOrders, setPaidOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedTab, setSelectedTab] = useState('unpaid');
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [paymentMethod, setPaymentMethod] = useState('cash');
  const [payingOrderId, setPayingOrderId] = useState(null);

  useEffect(() => {
  async function fetchData() {
    try {
      // ดึง orders
      const ordersRes = await fetch('http://localhost:8080/api/orders');
      if (!ordersRes.ok) throw new Error('Failed to fetch orders');
      const ordersData = await ordersRes.json();
      if (ordersData.status !== 200) throw new Error(ordersData.description || 'Error fetching orders');
      const allOrders = ordersData.data;

      // ดึง payments
      const paymentsRes = await fetch('http://localhost:8080/api/payment');
      if (!paymentsRes.ok) throw new Error('Failed to fetch payments');
      const paymentsData = await paymentsRes.json();
      if (paymentsData.status !== 200) throw new Error(paymentsData.description || 'Error fetching payments');
      const payments = paymentsData.data;

      // สร้าง set ของ orderId ที่มีการชำระแล้ว
      const paidOrderIds = new Set(payments.map(p => p.orderId));

      // แยก orders ออกเป็น paid และ unpaid ตาม payment table
      const unpaidOrdersRaw = allOrders.filter(order => !paidOrderIds.has(order.id) && order.status !== 'closed');
      const paidOrdersRaw = allOrders.filter(order => paidOrderIds.has(order.id) || order.status === 'closed');

      // โหลด items ของ unpaid
      const unpaidWithItems = await Promise.all(unpaidOrdersRaw.map(async order => {
        const items = await fetchOrderItems(order.id);
        return { ...order, items };
      }));

      // โหลด items ของ paid
      const paidWithItems = await Promise.all(paidOrdersRaw.map(async order => {
        const items = await fetchOrderItems(order.id);
        return { ...order, items };
      }));

      setOrders(unpaidWithItems);
      setPaidOrders(paidWithItems);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  }
  fetchData();
}, []);


  const fetchOrderItems = async (orderId) => {
    try {
      const res = await fetch(`http://localhost:8080/api/orders/${orderId}`);
      const data = await res.json();
      if (data.status !== 200) throw new Error('Error fetching order items');
      return data.data.items || [];
    } catch (err) {
      console.error(err);
      return [];
    }
  };

  const calculateTotal = (order) => {
    if (!order.items) return 0;
    return order.items.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0);
  };

  const handlePay = async () => {
  if (!selectedOrder) return;
  setPayingOrderId(selectedOrder.id);
  try {
    const res = await fetch('http://localhost:8080/api/payment', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        orderId: selectedOrder.id,
        amount: calculateTotal(selectedOrder),
        method: paymentMethod,
      })
    });

    const data = await res.json();
    if (data.status !== 201) throw new Error('ชำระเงินไม่สำเร็จ');

    // ✅ เปลี่ยนสถานะโต๊ะเป็น available
    if (selectedOrder.tableId) {
      await fetch(`http://localhost:8080/api/tables/status`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({id: selectedOrder.tableId, status: 'available' })
      });
    }

    // ✅ เปลี่ยนสถานะลูกค้าเป็น done
    if (selectedOrder.customer?.id) {
      await fetch(`http://localhost:8080/api/customers/status`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: selectedOrder.customer.id, status: 'done' })
      });
    }

    setOrders(prev => prev.filter(order => order.id !== selectedOrder.id));
    setPaidOrders(prev => [...prev, { ...selectedOrder, status: 'paid' }]);
    setSelectedOrder(null);
    alert('ชำระเงินเรียบร้อยแล้ว');
  } catch (err) {
    alert('เกิดข้อผิดพลาด: ' + err.message);
  } finally {
    setPayingOrderId(null);
  }
};



  if (loading) return <div className="payment-page"><p>Loading orders...</p></div>;
  if (error) return <div className="payment-page"><p>Error: {error}</p></div>;

  return (
    <>
      <Navbar />
      <div className="payment-page">
        <h2>การชำระเงิน</h2>
        <div className="tabs">
          <button className={selectedTab === 'unpaid' ? 'active' : ''} onClick={() => setSelectedTab('unpaid')}>รอการชำระ</button>
          <button className={selectedTab === 'paid' ? 'active' : ''} onClick={() => setSelectedTab('paid')}>ประวัติการชำระ</button>
        </div>

        {selectedTab === 'unpaid' && !selectedOrder && (
          <div className="table-buttons">
            {orders.map(order => (
              <button key={order.id} className="table-btn" onClick={() => setSelectedOrder(order)}>
                โต๊ะ {order.table?.tableNumber || order.tableId}
              </button>
            ))}
            {orders.length === 0 && <p>ไม่มีคำสั่งซื้อที่รอชำระ</p>}
          </div>
        )}

        {selectedOrder && (
          <div className="receipt">
            <h3>โต๊ะ: {selectedOrder.table?.tableNumber || selectedOrder.tableId}</h3>
            <p>ลูกค้า: {selectedOrder.customer?.name || 'ไม่ระบุ'}</p>
            <table className="order-table">
              <thead>
                <tr>
                  <th>เมนู</th>
                  <th>ราคา/หน่วย</th>
                  <th>จำนวน</th>
                  <th>รวม</th>
                </tr>
              </thead>
              <tbody>
                {selectedOrder.items.map(item => (
                  <tr key={item.id}>
                    <td>{item.menuItem?.name}</td>
                    <td>{item.unitPrice.toFixed(2)}</td>
                    <td>{item.quantity}</td>
                    <td>{(item.unitPrice * item.quantity).toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <h4>รวมทั้งหมด: {calculateTotal(selectedOrder).toFixed(2)} บาท</h4>
            <div className="payment-methods">
  <input
    type="radio"
    id="cash"
    name="payment"
    value="cash"
    checked={paymentMethod === 'cash'}
    onChange={e => setPaymentMethod(e.target.value)}
  />
  <label htmlFor="cash">เงินสด</label>

  <input
    type="radio"
    id="credit_card"
    name="payment"
    value="credit_card"
    checked={paymentMethod === 'credit_card'}
    onChange={e => setPaymentMethod(e.target.value)}
  />
  <label htmlFor="credit_card">บัตรเครดิต</label>

  <input
    type="radio"
    id="mobile_payment"
    name="payment"
    value="mobile_payment"
    checked={paymentMethod === 'mobile_payment'}
    onChange={e => setPaymentMethod(e.target.value)}
  />
  <label htmlFor="mobile_payment">โอน/พร้อมเพย์</label>
</div>

            <button className="pay-btn" onClick={handlePay} disabled={payingOrderId === selectedOrder.id}>
              {payingOrderId === selectedOrder.id ? 'กำลังชำระ...' : 'ยืนยันการชำระ'}
            </button>
            <button className="back-btn" onClick={() => setSelectedOrder(null)}>กลับ</button>
          </div>
        )}

        {selectedTab === 'paid' && (
          <div className="history-list">
            {paidOrders.map(order => (
              <div key={order.id} className="history-card">
                <h4>โต๊ะ {order.table?.tableNumber || order.tableId} | รวม {calculateTotal(order).toFixed(2)} บาท</h4>
                <p>ลูกค้า: {order.customer?.name || 'ไม่ระบุ'}</p>
              </div>
            ))}
            {paidOrders.length === 0 && <p>ไม่มีประวัติการชำระ</p>}
          </div>
        )}
      </div>
    </>
  );
}

export default PaymentPage;
