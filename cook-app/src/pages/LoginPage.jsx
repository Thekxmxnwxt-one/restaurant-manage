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

      if (!response.ok) {
        setError('ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง');
        return;
      }

      const data = await response.json();

      if (data.status === 200) {
        const token = data.data.token;
        const user = data.data.user;
        const role = user.employeesModel.role;

        localStorage.setItem("token", token);
        localStorage.setItem("role", role);
        localStorage.setItem("username", user.username);

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

        {/* ปุ่ม Register */}
        <div className="register-section">
          <span>ยังไม่มีบัญชี? </span>
          <button
            type="button"
            onClick={() => navigate('/register')}
          >
            สมัครสมาชิก
          </button>
        </div>
      </div>
    </div>
  );
}

export default Login;
