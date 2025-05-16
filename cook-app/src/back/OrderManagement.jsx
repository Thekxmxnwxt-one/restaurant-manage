import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/OrderManagement.css';

const OrderManagement = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/orders');
      setOrders(response.data.data); // Adjust if your API wraps in .data or .result
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  const updateOrderStatus = async (orderId, newStatus) => {
    try {
      await axios.put(`http://localhost:8080/api/orders/status`, {
        status: newStatus
      });
      fetchOrders(); // refresh data
    } catch (error) {
      console.error('Error updating order status:', error);
    }
  };

  return (
    <div className="order-management">
      <h2>จัดการออเดอร์ลูกค้า</h2>
      <div className="order-grid">
        {orders.map(order => (
          <div key={order.id} className={`order-card status-${order.status.toLowerCase()}`}>
            <h3>โต๊ะ: {order.tableNumber}</h3>
            <p><strong>ลูกค้า:</strong> {order.customerName}</p>
            <ul>
              {order.items.map((item, index) => (
                <li key={index}>{item.menuName} x {item.quantity}</li>
              ))}
            </ul>
            <p><strong>สถานะ:</strong> {order.status}</p>
            {order.status === 'READY' && (
              <button
                className="btn-serve"
                onClick={() => updateOrderStatus(order.id, 'SERVED')}
              >
                เสิร์ฟเรียบร้อย
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default OrderManagement;
