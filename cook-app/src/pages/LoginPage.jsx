// src/components/LoginPage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
  e.preventDefault();
  setError('');
  try {
    const response = await fetch('http://localhost:8080/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    const data = await response.json();

    if (data.status === 200) {
      const employeeId = data.data.employeeId;

      // ดึงข้อมูล employee เพื่อดู role
      const empRes = await fetch(`http://localhost:8080/api/employee/${employeeId}`);
      const empData = await empRes.json();

      if (empData.status === 200) {
        const role = empData.data.role; // สมมติว่า employee มี field role
        localStorage.setItem("role", role);

        if (role === "waitress") {
          navigate("/customer");
        } else if (role === "chef") {
          navigate("/kitchen");
        } else if (role === "manager") {
          navigate("/manager");
        } else {
          alert("Unknown role");
        }
      } else {
        setError('ไม่สามารถโหลดข้อมูลพนักงานได้');
      }
    } else {
      setError('ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง');
    }
  } catch (err) {
    console.error(err);
    setError('เกิดข้อผิดพลาดในการเชื่อมต่อ');
  }
};


  return (
    <div className="login-container">
      <div className="login-box">
        <h2 className="title">Welcome back</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleLogin}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <button type="submit">Sign in</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Login;
