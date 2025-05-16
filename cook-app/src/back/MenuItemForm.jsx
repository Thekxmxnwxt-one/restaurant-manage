import React, { useState } from 'react';

const MenuItemForm = ({ onUpdate }) => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const trimmedName = name.trim();
    const priceNumber = Number(price);

    if (!trimmedName || !price || isNaN(priceNumber) || priceNumber <= 0) {
      alert('กรุณากรอกชื่อเมนูและราคาที่ถูกต้อง');
      return;
    }

    setLoading(true);

    try {
      const res = await fetch('/api/menu', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: trimmedName, price: priceNumber }),
      });

      if (res.ok) {
        alert('เพิ่มเมนูอาหารสำเร็จ');
        setName('');
        setPrice('');
        if (onUpdate) onUpdate();
      } else {
        alert('เพิ่มเมนูอาหารไม่สำเร็จ');
      }
    } catch (error) {
      console.error('Error adding menu item:', error);
      alert('เกิดข้อผิดพลาดขณะเพิ่มเมนูอาหาร');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="menu-item-form">
      <input
        type="text"
        placeholder="ชื่อเมนูอาหาร"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <input
        type="number"
        placeholder="ราคา"
        value={price}
        min="1"
        onChange={(e) => setPrice(e.target.value)}
      />
      <button type="submit" disabled={loading}>
        {loading ? 'กำลังเพิ่ม...' : 'เพิ่มเมนูอาหาร'}
      </button>
    </form>
  );
};

export default MenuItemForm;
