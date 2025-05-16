import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/DashboardPage.css';

const DashboardPage = () => {
  const [tables, setTables] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    try {
      const tablesRes = await axios.get('http://localhost:8080/api/tables');
      const customersRes = await axios.get('http://localhost:8080/api/customers/active');
      setTables(tablesRes.data.data || []);
      setCustomers(customersRes.data.data || []);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (loading) return <div className="loading">กำลังโหลดข้อมูล...</div>;

  return (
    <div className="dashboard">
      <header className="header">
        <h1>📊 แดชบอร์ดร้านอาหาร</h1>
        <p>ภาพรวมการใช้งานโต๊ะและลูกค้าปัจจุบัน</p>
      </header>

      <section className="section">
        <h2>🪑 สถานะโต๊ะ</h2>
        <div className="table-grid">
          {tables.map((table) => (
            <div
              key={table.id}
              className={`table-card ${table.status === 'available' ? 'available' : 'occupied'}`}
            >
              <h3>โต๊ะ {table.tableNumber}</h3>
              <p>
                {table.status === 'available' ? '✅ ว่าง' : '🍽️ มีลูกค้า'}
              </p>
            </div>
          ))}
        </div>
      </section>

      <section className="section">
        <h2>👥 ลูกค้าที่ใช้งานอยู่</h2>
        <div className="customer-list">
          {customers.length === 0 ? (
            <p>ไม่มีลูกค้าในระบบขณะนี้</p>
          ) : (
            customers.map((customer) => (
              <div key={customer.id} className="customer-card">
                <strong>{customer.name}</strong> | โต๊ะ {customer.tablesId}
                <p>สถานะ: {customer.status}</p>
              </div>
            ))
          )}
        </div>
      </section>
    </div>
  );
};

export default DashboardPage;
