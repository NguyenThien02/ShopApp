-- CREATE TABLE orders (
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     user_id INT,
--     FOREIGN KEY (user_id) REFERENCES users (id),
--     full_name VARCHAR(100) DEFAULT '',
--     email VARCHAR(100) DEFAULT '',
--     phone_number VARCHAR(20) NOT NULL,
--     address VARCHAR(200) NOT NULL,
--     note VARCHAR(100) DEFAULT '',
--     order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
--     status VARCHAR(20),
--     total_money FLOAT CHECK (total_money >= 0)
-- );

-- alter table orders add column shipping_method varchar(100);
-- alter table orders add column shipping_address varchar(100);
-- alter table orders add column shipping_date date;
-- alter table orders add column tracking_number varchar(100);
-- alter table orders add column payment_method varchar(100);

-- Xóa một đơn hàng => xóa mềm => thêm trường active 
alter table orders add column active tinyint(1);

-- Trạng thái đơn hàng chỉ được phép nhận các giá trị cụ thế sau
alter table orders modify column status enum('pending', 'processing', 'shipped', 'delivered', 'cancelled')
comment 'Trạng thái đơn hàng';
