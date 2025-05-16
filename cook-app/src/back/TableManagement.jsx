import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/TableManagement.css';

const TableManagement = () => {
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchTables = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/tables');
      setTables(res.data.data);
    } catch (err) {
      console.error('Error fetching tables:', err);
    } finally {
      setLoading(false);
    }
  };

  const updateStatus = async (id, newStatus) => {
    try {
      await axios.put('http://localhost:8080/api/table/status', {
        tableId: id,
        status: newStatus,
      });
      fetchTables();
    } catch (err) {
      console.error('Error updating table status:', err);
    }
  };

  useEffect(() => {
    fetchTables();
  }, []);

  if (loading) return <div className="loading">Loading tables...</div>;

  return (
    <div className="table-management">
      <h2>การจัดการโต๊ะ</h2>
      <div className="table-grid">
        {tables.map((table) => (
          <div key={table.id} className={`table-card ${table.status}`}>
            <h3>โต๊ะ {table.tableNumber}</h3>
            <p>สถานะ: <strong>{table.status}</strong></p>
            <button
              className="btn-ready"
              onClick={() => updateStatus(table.id, 'available')}
              disabled={table.status === 'available'}
            >
              ตั้งเป็นพร้อมใช้
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TableManagement;
