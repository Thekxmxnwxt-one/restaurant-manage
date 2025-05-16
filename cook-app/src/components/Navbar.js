import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';

const Navbar = () => {
  const location = useLocation();
  const activePath = location.pathname;
  const navigate = useNavigate();

  const handleLogout = () => {
    console.log('Logout clicked');
    navigate("/");
  };

  return (
    <nav className="navbar">
      <ul>
        <li className={activePath === '/customer' ? 'active' : ''}>
          <Link to="/customer">Addcustomer</Link>
        </li>
        <li className={activePath === '/serve' ? 'active' : ''}>
          <Link to="/serve">Serve</Link>
        </li>
        <li className={activePath === '/payment' ? 'active' : ''}>
          <Link to="/payment">Payment</Link>
        </li>
      </ul>
      <div className="logout-container">
          <button className="btn-logout" onClick={handleLogout}>Logout</button>
        </div>
    </nav>
  );
};

export default Navbar;
