import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/ManagerPage.css';

export default function ManagerPage() {
  const [activeTab, setActiveTab] = useState('summary');

  const [employees, setEmployees] = useState([]);
  const [empName, setEmpName] = useState('');
  const [empPosition, setEmpPosition] = useState('');

  const [menus, setMenus] = useState([]);
  const [menuName, setMenuName] = useState('');
  const [menuImageUrl, setMenuImageUrl] = useState('');
  const [menuDescription, setMenuDescription] = useState('');
  const [menuPrice, setMenuPrice] = useState('');
  const navigate = useNavigate();

  const [kitchenStations, setKitchenStations] = useState([]);
  const [menuCategory, setMenuCategory] = useState('');

  const [tables, setTables] = useState([]);
  const [newTableName, setNewTableName] = useState('');

  // Employee editing
const [editingEmployeeId, setEditingEmployeeId] = useState(null);
const [editingEmpName, setEditingEmpName] = useState('');
const [editingEmpPosition, setEditingEmpPosition] = useState('');

// Menu editing
const [editingMenuId, setEditingMenuId] = useState(null);
const [editingMenuData, setEditingMenuData] = useState({
  name: '',
  imageUrl: '',
  description: '',
  price: '',
  category: ''
});

const [totalSales, setTotalSales] = useState(0);

const token = localStorage.getItem('token'); 


const fetchTotalSales = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/payment', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    if (res.data.status === 200 && Array.isArray(res.data.data)) {
      const total = res.data.data.reduce((sum, payment) => sum + payment.amount, 0);
      setTotalSales(total);
    }
  } catch (error) {
    console.error('Error fetching total sales', error);
  }
};



const handleEditEmployee = (emp) => {
  setEditingEmployeeId(emp.id);
  setEditingEmpName(emp.name);
  setEditingEmpPosition(emp.role);
};

const handleUpdateEmployee = async (e) => {
  e.preventDefault();
  try {
    await axios.put(`http://localhost:8080/api/manager/employee`,
      {
        id: editingEmployeeId,
        name: editingEmpName,
        role: editingEmpPosition
      },
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    setEditingEmployeeId(null);
    fetchEmployees();
  } catch (error) {
    console.error("Error updating employee", error);
  }
};


  const summaryData = {
    totalEmployees: employees.length,
    totalMenus: menus.length,
    totalSales: 12450,
  };


  const fetchEmployees = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/employee", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
      console.log(res.data);
      if (res.data.status === 200 && Array.isArray(res.data.data)) {
        setEmployees(res.data.data);
      }
    } catch (error) {
      console.error("Error fetching employees", error);
    }
  };

  // เพิ่มพนักงาน
  const handleAddEmployee = async (e) => {
    e.preventDefault();
    if (!empName.trim() || !empPosition.trim()) return;

    try {
      const postData = {
        name: empName.trim(),
        role: empPosition.trim(),
      };
      const res = await axios.post("http://localhost:8080/api/manager/employee", postData, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
      if (res.data.status === 201) {
        fetchEmployees();
        setEmpName('');
        setEmpPosition('');
      }
    } catch (error) {
      console.error("Error adding employee", error);
    }
  };

  // ลบพนักงาน
  const handleDeleteEmployee = async (id) => {
    try {
      const res = await axios.delete(`http://localhost:8080/api/manager/employee/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
      if (res.data.status === 200) {
        fetchEmployees();
      }
    } catch (error) {
      console.error("Error deleting employee", error);
    }
  };

  const fetchKitchenStations = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/kitchen/stations", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
      if (res.data.status === 200 && Array.isArray(res.data.data)) {
        setKitchenStations(res.data.data);
      }
    } catch (error) {
      console.error("Error fetching kitchen stations", error);
    }
  };

  const fetchMenus = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/menu");
      setMenus(res.data.data || []);
    } catch (error) {
      console.error("Error fetching menus", error);
    }
  };

  const handleAddMenu = async (e) => {
    e.preventDefault();
    if (!menuName.trim() || !menuCategory.trim() || !menuPrice || isNaN(menuPrice)) return;

    const newMenu = {
      name: menuName.trim(),
      imageUrl: menuImageUrl.trim(),
      description: menuDescription.trim(),
      price: parseFloat(menuPrice),
      category: menuCategory,
      available: true
    };

    try {
      await axios.post("http://localhost:8080/api/manager/menu", 
        [newMenu],
      {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
      fetchMenus(); // โหลดใหม่
      setMenuName('');
      setMenuImageUrl('');
      setMenuDescription('');
      setMenuPrice('');
      setMenuCategory('');
    } catch (error) {
      console.error("Error adding menu", error);
    }
  };

  const handleDeleteMenu = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/manager/menu/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
      fetchMenus(); // โหลดใหม่
    } catch (error) {
      console.error("Error deleting menu", error);
    }
  };

  const fetchTables = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/tables');
      if (res.data.status === 200 && Array.isArray(res.data.data)) {
        setTables(res.data.data);
      }
    } catch (error) {
      console.error('Error fetching tables', error);
    }
  };

  const handleAddTable = async (e) => {
  e.preventDefault();
  if (!newTableName.trim()) return;

  try {
    const postData = [
      {
        tableNumber: parseInt(newTableName.trim(), 10),
        status: 'available'
      }
    ];
    const res = await axios.post('http://localhost:8080/api/manager/tables', postData,
      {
    headers: {
      Authorization: `Bearer ${token}`
    }
  }
    );
    if (res.data.status === 201) {
      fetchTables();
      setNewTableName('');
    }
  } catch (error) {
    console.error('Error adding table', error);
  }
};


  // ลบโต๊ะ
  const handleDeleteTable = async (tableId) => {
    try {
      const res = await axios.delete(`http://localhost:8080/api/manager/tables/${tableId}`,
        {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
      );
      if (res.status === 200) {
        fetchTables();
      }
    } catch (error) {
      console.error('Error deleting table', error);
    }
  };

  useEffect(() => {
    fetchEmployees();
    fetchMenus();
    fetchKitchenStations();
    fetchTables();
    fetchTotalSales();
  }, []);


  const handleLogout = () => {
    console.log('Logout clicked');
    navigate("/");
  };

  return (
    <div className="manager">
      <nav className="navbar">
        <ul>
          {['summary', 'employee', 'menu', 'table'].map(tab => (
            <li
              key={tab}
              className={activeTab === tab ? 'active' : ''}
              onClick={() => setActiveTab(tab)}
            >
              {tab.charAt(0).toUpperCase() + tab.slice(1)}
            </li>
          ))}
        </ul>

        <div className="logout-container">
          <button className="btn-logout" onClick={handleLogout}>Logout</button>
        </div>
      </nav>

      <main className="content">
        {activeTab === 'summary' && (
          <section className="summary">
            <h2>Summary</h2>
            <div className="summary-cards">
              <div className="card">
                <p className="card-label">พนักงานทั้งหมด</p>
                <p className="card-value">{employees.length} คน</p>
              </div>
              <div className="card">
                <p className="card-label">เมนูทั้งหมด</p>
                <p className="card-value">{menus.length} รายการ</p>
              </div>
              <div className="card">
                <p className="card-label">โต๊ะทั้งหมด</p>
                <p className="card-value">{tables.length} รายการ</p>
              </div>
              <div className="card">
                <p className="card-label">ยอดขายรวม</p>
                <p className="card-value">{totalSales} บาท</p>
              </div>
            </div>
          </section>
        )}

        {activeTab === 'employee' && (
          <section className="employee">
            <h2>Employee Management</h2>
            <form onSubmit={handleAddEmployee} className="form">
              <input
                type="text"
                placeholder="ชื่อพนักงาน"
                value={empName}
                onChange={e => setEmpName(e.target.value)}
                required
              />
              <select
                value={empPosition}
                onChange={e => setEmpPosition(e.target.value)}
                required
                >
                <option value="">-- เลือกตำแหน่ง --</option>
                <option value="waitress">Waitress</option>
                <option value="chef">Chef</option>
                <option value="manager">Manager</option>
                </select>

              <button type="submit">เพิ่มพนักงาน</button>
            </form>

            <ul className="list">
          {employees.length ? employees.map(emp => (
            <li key={emp.id} className="employee-item">
              {editingEmployeeId === emp.id ? (
            <div className="employee-box">
          <input
            value={editingEmpName}
            onChange={e => setEditingEmpName(e.target.value)}
          />
          <select
            value={editingEmpPosition}
            onChange={e => setEditingEmpPosition(e.target.value)}
          >
            <option value="waitress">Waitress</option>
            <option value="chef">Chef</option>
            <option value="manager">Manager</option>
          </select>
          <div className="employee-actions">
            <button onClick={handleUpdateEmployee}>บันทึก</button>
            <button onClick={() => setEditingEmployeeId(null)}>ยกเลิก</button>
          </div>
        </div>
      ) : (
        <div className="employee-box">
          <div className="employee-info">
            <strong>name: {emp.name}</strong>
            <em>role: {emp.role}</em>
          </div>
          <div className="employee-actions">
            <button className="btn-edit" onClick={() => handleEditEmployee(emp)}>แก้ไข</button>
            <button className="btn-delete" onClick={() => handleDeleteEmployee(emp.id)}>ลบ</button>
          </div>
        </div>
      )}
    </li>
  )) : <li className="empty">ไม่มีพนักงานในระบบ</li>}
</ul>


          </section>
        )}

        {activeTab === 'menu' && (
          <section className="menu">
            <div className='topic'><h2>Menu Management</h2></div>
            <form onSubmit={handleAddMenu} className="form">
              <input
                type="text"
                placeholder="ชื่อเมนู"
                value={menuName}
                onChange={e => setMenuName(e.target.value)}
                required
              />
              <input
                type="url"
                placeholder="URL รูปภาพ (ถ้ามี)"
                value={menuImageUrl}
                onChange={e => setMenuImageUrl(e.target.value)}
              />
              <textarea
                placeholder="คำอธิบายเมนู"
                value={menuDescription}
                onChange={e => setMenuDescription(e.target.value)}
                rows={3}
              />
              <input
                type="number"
                placeholder="ราคา"
                value={menuPrice}
                onChange={e => setMenuPrice(e.target.value)}
                required
                min="0"
              />
              <select
                value={menuCategory}
                onChange={e => setMenuCategory(e.target.value)}
                required
              >
                <option value="">-- เลือกสถานีครัว --</option>
            {kitchenStations.map(station => (
              <option key={station.id} value={station.name}>
                {station.name.charAt(0).toUpperCase() + station.name.slice(1)}
              </option>
            ))}
              </select>
              <button type="submit">เพิ่มเมนู</button>
            </form>

            <div className="menu-grid">
              {menus.length ? menus.map(menu => (
              <div key={menu.id} className="menu-row">
                {menu.imageUrl ? (
                  <img src={menu.imageUrl} alt={menu.name} className="menu-image-manager" />
                ) : (
                  <div className="menu-image placeholder">ไม่มีรูป</div>
                )}

                <div className="menu-info">
                  <div className="menu-name">{menu.name}</div>
                  <div className="menu-description">{menu.description}</div>
                </div>

                <div className="menu-actions">
                  <div className="menu-price">{menu.price} บาท</div>
                  <button className="btn-delete" onClick={() => handleDeleteMenu(menu.id)}>ลบ</button>
                </div>
              </div>
              )) : <div className="empty">ไม่มีเมนูในระบบ</div>}
            </div>
          </section>
        )}

        {activeTab === 'table' && (
          <section className="table">
            <h2>Table Management</h2>
            <form onSubmit={handleAddTable} className="form">
              <input
                type="text"
                placeholder="เลขโต๊ะใหม่"
                value={newTableName}
                onChange={e => setNewTableName(e.target.value)}
                required
              />
              <button type="submit">เพิ่มโต๊ะ</button>
            </form>

            <ul className="list">
              {tables.length ? tables.map(table => (
                <li key={table.id} className="table-item">
                  <span>โต๊ะเลขที่: {table.tableNumber}</span>
                  <button className="btn-delete" onClick={() => handleDeleteTable(table.id)}>ลบ</button>
                </li>
              )) : <li className="empty">ไม่มีโต๊ะในระบบ</li>}
            </ul>
          </section>
        )}
      </main>
    </div>
  );
}
