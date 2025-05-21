import React, { useState, useEffect } from 'react';
import '../styles/StaffPage.css';

const BASE_URL = 'http://localhost:8080/api';

function StaffPage() {
  const [tables, setTables] = useState([]);
  const [customerName, setCustomerName] = useState('');
  const [customerPhone, setCustomerPhone] = useState('');
  const [selectedTable, setSelectedTable] = useState(null);

  useEffect(() => {
    fetch(`${BASE_URL}/tables`)
      .then(res => res.json())
      .then(data => setTables(data.data));
  }, []);

  const handleAssignCustomer = () => {
    if (!customerName || !selectedTable) {
      alert('กรุณากรอกชื่อและเลือกโต๊ะ');
      return;
    }

    // สร้างลูกค้า
    fetch(`${BASE_URL}/customers`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: customerName,
        phone: customerPhone,
        tablesId: selectedTable.id,
      }),
    })
      .then(res => res.json())
      .then(customerData => {
        // อัพเดตสถานะโต๊ะเป็น occupied
        fetch(`${BASE_URL}/tables/status`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            id: selectedTable.id,
            status: 'occupied',
          }),
        }).then(() => {
          alert(`กำหนดลูกค้า ${customerName} ให้โต๊ะ ${selectedTable.tableNumber} เรียบร้อย`);
          setCustomerName('');
          setCustomerPhone('');
          setSelectedTable(null);
          // โหลดโต๊ะใหม่
          fetch(`${BASE_URL}/tables`)
            .then(res => res.json())
            .then(data => setTables(data.data));
        });
      });
  };

  return (
    <div className="staff-page">
      <h2>พนักงาน - สร้างลูกค้าและกำหนดโต๊ะ</h2>
      <input
        type="text"
        placeholder="ชื่อลูกค้า"
        value={customerName}
        onChange={e => setCustomerName(e.target.value)}
      />
      <input
        type="text"
        placeholder="เบอร์โทร"
        value={customerPhone}
        onChange={e => setCustomerPhone(e.target.value)}
      />
      <select
        value={selectedTable ? selectedTable.id : ''}
        onChange={e => {
          const table = tables.find(t => t.id === +e.target.value);
          setSelectedTable(table);
        }}
      >
        <option value="">เลือกโต๊ะที่ว่าง</option>
        {tables
          .filter(t => t.status === 'available')
          .map(t => (
            <option key={t.id} value={t.id}>
              โต๊ะ {t.tableNumber}
            </option>
          ))}
      </select>
      <button onClick={handleAssignCustomer}>กำหนดลูกค้า</button>

      <h3>สถานะโต๊ะทั้งหมด</h3>
      <ul>
        {tables.map(t => (
          <li key={t.id}>
            โต๊ะ {t.tableNumber} - สถานะ: {t.status}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default StaffPage;
