import React, { useEffect, useState } from 'react';
import '../styles/EmployeeAssignTable.css';
import Navbar from '../components/Navbar';

const AssignCustomer = () => {
  const [tables, setTables] = useState([]);
  const [activeCustomers, setActiveCustomers] = useState([]);
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [selectedTable, setSelectedTable] = useState('');
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    fetchTables();
    fetchActiveCustomers();
  }, []);

  const fetchTables = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/tables');
      const data = await res.json();
      if (data.status === 200) {
        const available = data.data.filter(t => t.status === 'available');
        setTables(available);
      }
    } catch {
      setError('โหลดข้อมูลโต๊ะล้มเหลว');
    }
  };

  const fetchActiveCustomers = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/tables');
      const data = await res.json();

      if (data.status === 200) {
        const nonAvailableTables = data.data.filter(t => t.status !== 'available');

        const detailedTables = await Promise.all(
          nonAvailableTables.map(async (t) => {
            const res = await fetch(`http://localhost:8080/api/tables/${t.id}`);
            const detail = await res.json();
            return detail.data;
          })
        );

        const active = detailedTables.filter(
          t => t.customer && t.customer.name
        );

        setActiveCustomers(active);
      }
    } catch (err) {
      console.error(err);
      setError('โหลดข้อมูลลูกค้าที่นั่งล้มเหลว');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccess('');
    setError('');

    const parsedTableId = parseInt(selectedTable);
    if (!name || !phone || isNaN(parsedTableId)) {
      setError('กรุณากรอกข้อมูลให้ครบถ้วน');
      return;
    }

    console.log("กำลังส่งข้อมูล:", {
      name,
      phone,
      tableId: parsedTableId
    });

    try {
      const res = await fetch('http://localhost:8080/api/customers', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name,
          phone,
          tablesId: parsedTableId,
          status: 'seated',
        }),
      });

      const customer = await res.json();
      if (customer.status !== 201) {
        setError('เพิ่มลูกค้าไม่สำเร็จ');
        return;
      }

      const tableRes = await fetch('http://localhost:8080/api/tables/status', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id: parsedTableId,
          status: 'not available',
        }),
      });

      const table = await tableRes.json();
      if (table.status !== 200) {
        setError('เปลี่ยนสถานะโต๊ะไม่สำเร็จ');
        return;
      }

      setSuccess('สร้างลูกค้าและจองโต๊ะสำเร็จ');
      setName('');
      setPhone('');
      setSelectedTable('');
      fetchTables();
      fetchActiveCustomers();
    } catch (err) {
      console.error(err);
      setError('เกิดข้อผิดพลาด');
    }
  };

  return (
    <>
    <Navbar />
    <div className="assign-customer-container">
      <div className="assign-customer-columns">
        <div className="form-box">
          <h1>สร้างลูกค้าและจองโต๊ะ</h1>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>ชื่อลูกค้า</label>
              <input type="text" value={name} onChange={e => setName(e.target.value)} />
            </div>

            <div className="form-group">
              <label>เบอร์โทร</label>
              <input type="text" value={phone} onChange={e => setPhone(e.target.value)} />
            </div>

            <div className="form-group">
              <label>เลือกโต๊ะที่ว่าง</label>
              <select value={selectedTable} onChange={e => setSelectedTable(e.target.value)}>
                <option value="">-- เลือกโต๊ะ --</option>
                {tables.map(t => (
                  <option key={t.id} value={String(t.id)}>โต๊ะ {t.tableNumber}</option>
                ))}
              </select>
            </div>

            <button type="submit" className="submit-button">ยืนยัน</button>

            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
          </form>
        </div>

        <div className="customer-list">
          <h2>ลูกค้าที่นั่งอยู่</h2>
          <ul>
            {activeCustomers.length > 0 ? (
              activeCustomers.map(t => (
                <li key={t.id}>
                  โต๊ะ {t.tableNumber} - <strong>{t.customer.name}</strong>
                </li>
              ))
            ) : (
              <p>ยังไม่มีลูกค้าที่นั่งอยู่</p>
            )}
          </ul>
        </div>
      </div>
    </div>
    </>
  );
};

export default AssignCustomer;
