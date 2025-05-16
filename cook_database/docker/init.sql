-- ตารางโต๊ะ
CREATE TABLE tables (
    id SERIAL PRIMARY KEY,
    table_number INT NOT NULL UNIQUE, 
    status VARCHAR(20) CHECK (status IN ('available', 'not available')) DEFAULT 'available'
);
-- ลูกค้า
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    table_id INT REFERENCES tables(id),
    status VARCHAR(20) CHECK (status IN ('waiting', 'seated', 'done')) DEFAULT 'waiting',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- เมนูอาหาร
CREATE TABLE menu_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_url TEXT,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(20) CHECK (category IN ('เซต', 'ราเมง', 'เบนโตะ', 'เครื่องดื่ม', 'ของหวาน')),
    available BOOLEAN DEFAULT TRUE
);

-- พนักงาน
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('chef','waitress', 'manager')) NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    employee_id INT REFERENCES employees(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id INT REFERENCES customers(id),
    table_id INT REFERENCES tables(id),
    employee_id INT NULL,
    ordered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('pending', 'processing', 'served', 'cancelled')) DEFAULT 'pending',
    closed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- ใส่ FOREIGN KEY แยก เพื่อกำหนด ON DELETE SET NULL
    CONSTRAINT fk_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id)
        ON DELETE SET NULL
);


-- รายการอาหารในแต่ละ order
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id) ON DELETE CASCADE,
    unit_price DECIMAL(10,2),
    menu_item_id INT REFERENCES menu_items(id),
    quantity INT NOT NULL CHECK (quantity > 0),
    status VARCHAR(20) CHECK (status IN ('pending', 'cooking', 'ready', 'served')) DEFAULT 'pending',
    kitchen_station VARCHAR(20) CHECK (kitchen_station IN ('เซต', 'ราเมง', 'เบนโตะ', 'เครื่องดื่ม', 'ของหวาน'))
);

-- สถานีครัว (กรณีมีข้อมูลแยก)
CREATE TABLE kitchen_stations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) CHECK (name IN ('เซต', 'ราเมง', 'เบนโตะ', 'เครื่องดื่ม', 'ของหวาน')) UNIQUE NOT NULL
);

-- Log สำหรับการปรุงอาหาร (optional)
CREATE TABLE kitchen_logs (
    id SERIAL PRIMARY KEY,
    order_item_id INT REFERENCES order_items(id) ON DELETE CASCADE,
    station_id INT REFERENCES kitchen_stations(id),
    status VARCHAR(20) CHECK (status IN ('cooking', 'done')) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id),
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(20) CHECK (method IN ('cash', 'credit_card', 'mobile_payment')),
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- ตัวอย่างข้อมูลโต๊ะในร้าน
INSERT INTO tables (table_number, status) VALUES
(1, 'available'), 
(2, 'available'),  
(3, 'available'),  
(4, 'available'), 
(5, 'available'), 
(6, 'available'),
(7, 'available'),
(8, 'available'),
(9, 'available'),
(10, 'available'),
(11, 'available'),
(12, 'available'),
(13, 'available'),
(14, 'available'),
(15, 'available');

-- ตัวอย่างข้อมูล menu_items
INSERT INTO menu_items (name, image_url, description, price, category, available) VALUES
('ไก่ทอดคาราอาเกะ', 'https://yayoirestaurants.com/productimages/thumbs/2544_Yayoi-Menubook-2024_880-x-800-px_Set-Menu_.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วเหลือง', 160.00, 'เซต', TRUE),
('ไก่ซอสสามรส', 'https://yayoirestaurants.com/productimages/thumbs/3077_Yayoi-Menubook-2024_880-x-800-px_Set-Menu_.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วเหลือง', 180.00, 'เซต', TRUE),
('ไก่เทอริยากิ', 'https://yayoirestaurants.com/productimages/thumbs/7420_Yayoi-Menubook-2024_880-x-800-px_Set-Menu_.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วเหลือง', 190.00, 'เซต', TRUE),
('หมูผัดกิมจิ', 'https://yayoirestaurants.com/productimages/thumbs/7618_Yayoi-Menubook-2024_880-x-800-px_Set-Menu_.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน หอยและหมึก ถั่วเหลือง', 200.00, 'เซต', TRUE),
('เซตกุ้งและไก่ซอสสามรส', 'https://yayoirestaurants.com/productimages/2524_Yayoi-Menubook-2024_880-x-800-px_Set-Menu_.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว สัตว์น้ำมีเปลือก ถั่วเหลือง', 200.00, 'เซต', TRUE),
('โซเม็ง', 'https://yayoirestaurants.com/productimages/thumbs/5071_Yayoi-Menubook-2024_880-x-800-px_Noodle-.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วเหลือง', 175.00, 'ราเมง', TRUE),
('อุด้งหมูสุกี้ยากี้', 'https://yayoirestaurants.com/productimages/thumbs/8231_ND003_Sukiyaki-Udon.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วลิสง ถั่วเหลือง', 185.00, 'ราเมง', TRUE),
('เทมปุระกุ้ง ต้มยำ ราเม็ง', 'https://yayoirestaurants.com/productimages/thumbs/9900_Yayoi-Menubook-2024_880-x-800-px_Noodle---.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว สัตว์น้ำมีเปลือก ถั่วเหลือง', 199.00, 'ราเมง', TRUE),
('ยากิโซบะกระทะร้อน', 'https://yayoirestaurants.com/productimages/1723_05%20-%20880%20x%20800%20Noodle%20-%20%20(Rev.).jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน ถั่วเหลือง', 149.00, 'ราเมง', TRUE),
('โซบะเย็น', 'https://yayoirestaurants.com/productimages/thumbs/3803_Yayoi-Menubook-2024_880-x-800-px_Noodle-.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วเหลือง', 149.00, 'ราเมง', TRUE),
('ชิกกี้ เบนโตะ', 'https://yayoirestaurants.com/productimages/thumbs/3799_3.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วลิสง ถั่วเหลือง', 245.00, 'เบนโตะ', TRUE),
('ซากานะ เบนโตะ', 'https://yayoirestaurants.com/productimages/thumbs/3279_1.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วลิสง ถั่วเหลือง', 255.00, 'เบนโตะ', TRUE),
('ซาชิมิ เดอลุกซ์ เบนโตะ', 'https://yayoirestaurants.com/productimages/thumbs/5104_2_0.jpg', 'เมนูนี้มีส่วนผสมของไข่ ปลา กลูเตน นมวัว ถั่วลิสง ถั่วเหลือง', 385.00, 'เบนโตะ', TRUE),
('ชาสตรอว์เบอร์รี', 'https://yayoirestaurants.com/productimages/7756_Yayoi-Menubook-2024_880-x-800-px_Drink_.jpg', 'เมนูนี้มีส่วนผสมของนมวัว', 60.00, 'เครื่องดื่ม', TRUE),
('มัทฉะ ลาเต้', 'https://yayoirestaurants.com/productimages/4490_Yayoi-Menubook-2024_880-x-800-px_Drink_-.jpg', 'เมนูนี้มีส่วนผสมของนมวัว', 70.00, 'เครื่องดื่ม', TRUE),
('สตรอว์เบอร์รี โซดา', 'https://yayoirestaurants.com/productimages/3425_Yayoi-Menubook-2024_880-x-800-px_Drink_-.jpg', 'เมนูนี้มีส่วนผสมของนมวัว', 60.00, 'เครื่องดื่ม', TRUE),
('วาราบิโมจิ', 'https://yayoirestaurants.com/productimages/8605_%20-%20Copy.jpg', 'เมนูนี้มีส่วนผสมของกลูเตน ถั่วเปลือกแข็ง ถั่วเหลือง', 79.00, 'ของหวาน', TRUE),
('ถั่วแดงร้อนญี่ปุ่นโมจิย่าง', 'https://yayoirestaurants.com/productimages/9330_%20-%20Copy.jpg', 'เมนูนี้มีส่วนผสมของกลูเตน', 65.00, 'ของหวาน', TRUE);

-- ตัวอย่างข้อมูล employees
INSERT INTO employees (name, role) VALUES
('สมชาย ภักดีไทย', 'waitress'),
('อรวรรณ ศรีสวัสดิ์', 'chef'),
('วิทยา พิทักษ์ผล', 'chef'),
('ศิริพร แซ่ลี้', 'waitress'),
('ปวีณา ทองมาก', 'manager');

-- เพิ่ม user login สำหรับแต่ละพนักงาน
INSERT INTO users (username, password, employee_id) VALUES
('somchai01', 'pass1234', 1),
('orawan02', 'pass2345', 2),
('withaya03', 'pass3456', 3),
('siriporn04', 'pass4567', 4),
('paweena05', 'pass5678', 5);

-- ตัวอย่างข้อมูล kitchen_stations
INSERT INTO kitchen_stations (name) VALUES
('เซต'),
('ราเมง'),
('เบนโตะ'),
('เครื่องดื่ม'),
('ของหวาน');
