import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/CustomerManagement.css';

const CustomerManagement = () => {
  const [customers, setCustomers] = useState([]);
  const [newCustomer, setNewCustomer] = useState({ name: '', tablesId: '' });
  const [loading, setLoading] = useState(true);

  const fetchCustomers = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/customers');
      setCustomers(res.data.data || []);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching customers:', error);
      setLoading(false);
    }
  };

  const handleCreateCustomer = async () => {
    if (!newCustomer.name || !newCustomer.tablesId) return;

    try {
      await axios.post('http://localhost:8080/api/customers', {
        name: newCustomer.name,
        tablesId: parseInt(newCustomer.tablesId),
      });
      setNewCustomer({ name: '', tablesId: '' });
      fetchCustomers();
    } catch (error) {
      console.error('Error creating customer:', error);
    }
  };

  const handleDeleteCustomer = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/customers/${id}`);
      fetchCustomers();
    } catch (error) {
      console.error('Error deleting customer:', error);
    }
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  return (
    <div className="customer-management">
      <h1>👥 จัดการลูกค้า</h1>

      <div className="create-form">
        <input
          type="text"
          placeholder="ชื่อลูกค้า"
          value={newCustomer.name}
          onChange={(e) => setNewCustomer({ ...newCustomer, name: e.target.value })}
        />
        <input
          type="number"
          placeholder="หมายเลขโต๊ะ"
          value={newCustomer.tablesId}
          onChange={(e) => setNewCustomer({ ...newCustomer, tablesId: e.target.value })}
        />
        <button onClick={handleCreateCustomer}>➕ เพิ่มลูกค้า</button>
      </div>

      {loading ? (
        <div className="loading">กำลังโหลด...</div>
      ) : (
        <div className="customer-list">
          {customers.length === 0 ? (
            <p>ยังไม่มีลูกค้าในระบบ</p>
          ) : (
            customers.map((customer) => (
              <div key={customer.id} className="customer-card">
                <div>
                  <strong>{customer.name}</strong> | โต๊ะ {customer.tablesId}
                  <p>สถานะ: {customer.status}</p>
                </div>
                <button onClick={() => handleDeleteCustomer(customer.id)}>🗑️ ลบ</button>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default CustomerManagement;
