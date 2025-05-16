import React, { useEffect, useState } from "react";
import axios from "axios";
import "../styles/Menu.css";

const MenuPage = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [cart, setCart] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/menu")
      .then((res) => {
        if (res.data && res.data.data) {
          setMenuItems(res.data.data);
        }
      })
      .catch((err) => console.error(err));
  }, []);

  const addToCart = (item) => {
    setCart((prev) => {
      const exists = prev.find((i) => i.id === item.id);
      if (exists) {
        return prev.map((i) =>
          i.id === item.id ? { ...i, quantity: i.quantity + 1 } : i
        );
      }
      return [...prev, { ...item, quantity: 1 }];
    });
  };

  const handleOrder = () => {
    const orderItems = cart.map((item) => ({
      menuItemId: item.id,
      quantity: item.quantity,
      unitPrice: item.price,
    }));

    axios
      .post("http://localhost:8080/api/orders", {
        customerId: 2,
        tableId: 2,
        employeeId: 0,
      })
      .then((res) => {
        const orderId = res.data.data.id;
        return axios.post("http://localhost:8080/api/orders/items", {
          orderId,
          items: orderItems,
        });
      })
      .then(() => {
        alert("Order placed successfully!");
        setCart([]);
      })
      .catch((err) => console.error(err));
  };

  return (
    <div className="menu-page">
      <h1 className="title">เมนูอาหาร</h1>
      <div className="menu-container">
        {menuItems.map((item) => (
          <div className="menu-card" key={item.id}>
            <img src={item.imageUrl} alt={item.name} className="menu-img" />
            <div className="menu-info">
              <h3>{item.name}</h3>
              <p>{item.price.toFixed(2)} บาท</p>
              <button onClick={() => addToCart(item)}>เพิ่มลงตะกร้า</button>
            </div>
          </div>
        ))}
      </div>

      {cart.length > 0 && (
        <div className="cart-section">
          <h2>รายการที่เลือก</h2>
          <ul>
            {cart.map((item) => (
              <li key={item.id}>
                {item.name} x {item.quantity} = {(item.price * item.quantity).toFixed(2)} บาท
              </li>
            ))}
          </ul>
          <button className="order-btn" onClick={handleOrder}>
            สั่งอาหาร
          </button>
        </div>
      )}
    </div>
  );
};

export default MenuPage;