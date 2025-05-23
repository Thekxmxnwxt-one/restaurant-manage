import React, { useState, useEffect } from 'react';
import '../styles/CustomerOrder.css';

const MenuPage = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [cart, setCart] = useState({});
  const [loading, setLoading] = useState(true);
  const [tableNumber, setTableNumber] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [orderLoading, setOrderLoading] = useState(false); // สำหรับสถานะ loading เวลาสั่งซื้อ

  useEffect(() => {
    fetch('http://localhost:8080/api/menu')
      .then(res => res.json())
      .then(data => {
        if (data.status === 200) {
          const items = data.data.map(item => ({
            id: item.id,
            name: item.name,
            price: item.price,
            image: item.imageUrl,
            description: item.description,
            category: item.category,
          }));
          setMenuItems(items);
        } else {
          console.error('Failed to load menu');
          setError('ไม่สามารถโหลดเมนูอาหารได้');
        }
      })
      .catch(err => {
        console.error('Error fetching menu:', err);
        setError('เกิดข้อผิดพลาดในการเชื่อมต่อเซิร์ฟเวอร์');
      })
      .finally(() => setLoading(false));
  }, []);

  const addToCart = (item) => {
    setCart((prev) => ({
      ...prev,
      [item.id]: {
        ...item,
        quantity: prev[item.id]?.quantity + 1 || 1,
      },
    }));
    setError('');
    setSuccess('');
  };

  const removeFromCart = (id) => {
    setCart((prev) => {
      const newCart = { ...prev };
      delete newCart[id];
      return newCart;
    });
    setError('');
    setSuccess('');
  };

  const changeQuantity = (id, amount) => {
    setCart((prev) => {
      const newQty = (prev[id]?.quantity || 0) + amount;
      if (newQty <= 0) {
        const newCart = { ...prev };
        delete newCart[id];
        return newCart;
      }
      return {
        ...prev,
        [id]: { ...prev[id], quantity: newQty },
      };
    });
    setError('');
    setSuccess('');
  };

  const totalPrice = Object.values(cart).reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  // ดึงข้อมูลลูกค้า จากเลขโต๊ะ
  const fetchCustomerByTable = async (tableNum) => {
    try {
      const res = await fetch(`http://localhost:8080/api/tables/${tableNum}`);
      const data = await res.json();
      if (data.status === 200 && data.data && data.data.customer) {
        return data.data.customer;
      } else {
        return null;
      }
    } catch (err) {
      console.error('Error fetching table/customer:', err);
      return null;
    }
  };

  // ดึง order ที่ยัง pending ของลูกค้าคนนี้ (ถ้ามี)
  const fetchPendingOrderByCustomer = async (customerId) => {
  try {
    console.log('Fetching pending orders for customerId:', customerId);
    const res = await fetch(`http://localhost:8080/api/orders?customerId=${customerId}&status=pending`);
    const data = await res.json();
    console.log('Pending orders data:', data);

    if (data.status === 200 && Array.isArray(data.data) && data.data.length > 0) {
      // ค้นหา order ที่ customerId ตรงกับตัวแปร customerId จริง ๆ
      const foundOrder = data.data.find(order => order.customerId === customerId);
      return foundOrder || null;
    } else {
      return null;
    }
  } catch (err) {
    console.error('Error fetching pending order:', err);
    return null;
  }
};

  const handleConfirmOrder = async () => {
  if (!tableNumber.trim()) {
    setError('กรุณากรอกเลขโต๊ะก่อนยืนยันการสั่งซื้อ');
    return;
  }
  if (Object.values(cart).length === 0) {
    setError('กรุณาเลือกอาหารก่อนยืนยันการสั่งซื้อ');
    return;
  }

  setError('');
  setSuccess('');
  setOrderLoading(true);

  try {
    const customer = await fetchCustomerByTable(tableNumber.trim());
    console.log('TableNumber ที่จะส่งไป:', tableNumber);
    console.log('เลขโต๊ะที่กรอก:', tableNumber);
    console.log('Customer:', customer);

    if (!customer) {
      setError('ไม่พบลูกค้าสำหรับเลขโต๊ะนี้ กรุณาตรวจสอบเลขโต๊ะอีกครั้ง');
      setOrderLoading(false);
      return;
    }

    let orderId = null;

    const existingOrder = await fetchPendingOrderByCustomer(customer.id);
    console.log('Existing pending order:', existingOrder);
    if (existingOrder) {
  orderId = existingOrder.id;
  console.log('ใช้ orderId เดิม:', orderId);

  if (existingOrder.status === 'served') {
    const updateRes = await fetch(`http://localhost:8080/api/orders/status`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({id: orderId, status: 'pending' }),
    });
    const updateData = await updateRes.json();
    if (updateData.status !== 200) {
      setError(`ไม่สามารถเปลี่ยนสถานะ order เป็น pending ได้: ${updateData.description || 'Unknown error'}`);
      setOrderLoading(false);
      return;
    }
    console.log('เปลี่ยนสถานะ order เป็น pending เรียบร้อย');
  }
}


    if (!orderId) {
      console.log('กำลังสร้าง order ใหม่...');
      const orderPayload = {
        customerId: customer.id,
        tableId: customer.tablesId,
        employeeId: 2, // แนะนำให้เปลี่ยนเป็นค่าที่กำหนดได้
        status: 'pending',
      };

      const orderRes = await fetch('http://localhost:8080/api/orders', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderPayload),
      });
      const orderData = await orderRes.json();

      console.log('Response จากการสร้าง order:', orderData);

      if (orderData.status !== 201) {
        setError(`สร้างคำสั่งซื้อไม่สำเร็จ: ไม่พบลูกค้าโต๊ะนี้ค่ะ`);
        setOrderLoading(false);
        return;
      }

      if (typeof orderData.data === 'object' && orderData.data.id) {
        orderId = orderData.data.id;
      } else {
        orderId = orderData.data;
      }
      console.log('orderId ใหม่ที่สร้าง:', orderId);
    }

    // เพิ่มรายการอาหารใน order
    for (const item of Object.values(cart)) {
      const itemPayload = {
        orderId: orderId,
        unitPrice: item.price,
        menuItemId: item.id,
        quantity: item.quantity,
        kitchenStation: item.category,
      };

      const itemRes = await fetch('http://localhost:8080/api/orders/items', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(itemPayload),
      });
      const itemData = await itemRes.json();

      if (itemData.status !== 201) {
        setError(`เพิ่มรายการอาหารสำเร็จ: ${itemData.description || 'Unknown error'}`);
        setOrderLoading(false);
        return;
      }
    }

    setSuccess('สั่งซื้อสำเร็จ! ขอบคุณที่ใช้บริการค่ะ');
    setCart({});
    setTableNumber('');
  } catch (err) {
    console.error('Order error:', err);
    setError('เกิดข้อผิดพลาดในการสั่งซื้อ กรุณาลองใหม่อีกครั้ง');
  } finally {
    setOrderLoading(false);
  }
};


  if (loading) {
    return <p>Loading menu...</p>;
  }

  return (
    <div className="menu-container">
      <div className="menu-list">
        {menuItems.map((item) => (
          <div className="menu-item" key={item.id}>
            <img src={item.image} alt={item.name} className="menu-image" />
            <h3>{item.name}</h3>
            <p className="description">{item.description}</p>
            <div className="price">฿ {item.price}</div>
            <button className="add-button" onClick={() => addToCart(item)}>
              🛒 เพิ่มลงตะกร้า
            </button>
          </div>
        ))}
      </div>

      <div className="cart">
        <h2>รายการอาหารทั้งหมด</h2>

        <label htmlFor="tableNumber" style={{ fontWeight: '600', marginBottom: '8px', display: 'block' }}>
          เลขโต๊ะ:
        </label>
        <input
          type="text"
          id="tableNumber"
          value={tableNumber}
          onChange={(e) => setTableNumber(e.target.value)}
          placeholder="กรอกเลขโต๊ะของคุณ"
          style={{
            width: '100%',
            padding: '8px 12px',
            marginBottom: '16px',
            borderRadius: '8px',
            border: '1px solid #ccc',
            fontSize: '16px',
            boxSizing: 'border-box',
            backgroundColor: '#fff',
            color: '#333',
          }}
        />

        {Object.values(cart).length === 0 && <p>ยังไม่มีรายการในตะกร้า</p>}
        {Object.values(cart).map((item) => (
          <div className="cart-item" key={item.id}>
            <div className="cart-title">{item.name}</div>
            <button onClick={() => changeQuantity(item.id, -1)}>-</button>
            <span>{item.quantity}</span>
            <button onClick={() => changeQuantity(item.id, 1)}>+</button>
            <span className="item-total">฿{item.price * item.quantity}</span>
            <button className="remove" onClick={() => removeFromCart(item.id)}>
              ✕ ลบ
            </button>
          </div>
        ))}
        <div className="total">ราคารวมทั้งหมด: ฿{totalPrice}</div>

        {error && (
          <div style={{ color: 'red', marginBottom: '12px', fontWeight: '600' }}>
            {error}
          </div>
        )}

        {success && (
          <div style={{ color: 'green', marginBottom: '12px', fontWeight: '600' }}>
            {success}
          </div>
        )}

        <button className="confirm" onClick={handleConfirmOrder} disabled={orderLoading}>
          {orderLoading ? 'กำลังสั่งซื้อ...' : 'ยืนยันการสั่งซื้อ'}
        </button>
      </div>
    </div>
  );
};

export default MenuPage;
