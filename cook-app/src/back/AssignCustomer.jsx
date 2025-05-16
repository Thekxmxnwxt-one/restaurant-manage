import React, { useEffect, useState } from "react";
import axios from "axios";
import "../styles/AssignCustomer.css";

const AssignCustomer = () => {
  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [tableId, setTableId] = useState("");
  const [tables, setTables] = useState([]);
  const [activeCustomers, setActiveCustomers] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchTables();
    fetchCustomers();
  }, []);

  const fetchTables = async () => {
    const res = await axios.get("http://localhost:8080/api/tables");
    const available = res.data.data.filter((t) => t.status === "available");
    setTables(available);
  };

  const fetchCustomers = async () => {
    const res = await axios.get("http://localhost:8080/api/customers");
    const active = res.data.data.filter((c) => c.status === "seated");
    setActiveCustomers(active);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const customerRes = await axios.post("http://localhost:8080/api/customers", {
        name,
        phone,
        tableId: parseInt(tableId),
        status: "seated",
      });


      await axios.put("http://localhost:8080/api/tables/status", {
        id: parseInt(tableId),
        status: "not available",
      });

      setMessage("เพิ่มลูกค้าและจองโต๊ะสำเร็จ");
      setName("");
      setPhone("");
      setTableId("");
      fetchTables();
      fetchCustomers();
    } catch (err) {
      setMessage("เกิดข้อผิดพลาด");
    }
  };

  return (
    <div className="assign-container">
      <div className="column form-column">
        <h2>เพิ่มลูกค้าและจองโต๊ะ</h2>
        <form onSubmit={handleSubmit} className="form-section">
          <label>ชื่อลูกค้า</label>
          <input value={name} onChange={(e) => setName(e.target.value)} required />

          <label>เบอร์โทรศัพท์</label>
          <input value={phone} onChange={(e) => setPhone(e.target.value)} required />

          <label>เลือกโต๊ะที่ว่าง</label>
          <select value={tableId} onChange={(e) => setTableId(e.target.value)} required>
            <option value="">-- เลือกโต๊ะ --</option>
            {tables.map((t) => (
              <option key={t.id} value={t.id}>
                โต๊ะ {t.tableNumber}
              </option>
            ))}
          </select>

          <button type="submit">บันทึก</button>
          {message && <p className="message">{message}</p>}
        </form>
      </div>

      <div className="column list-column">
        <h2>ลูกค้าที่กำลังนั่งโต๊ะ</h2>
        <ul className="customer-list">
          {activeCustomers.length === 0 ? (
            <li>ยังไม่มีลูกค้าที่นั่งโต๊ะ</li>
          ) : (
            activeCustomers.map((c) => (
              <li key={c.id}>
                👤 {c.name} ({c.phone}) - โต๊ะ {c.table?.tableNumber || "-"}
              </li>
            ))
          )}
        </ul>
      </div>
    </div>
  );
};

export default AssignCustomer;