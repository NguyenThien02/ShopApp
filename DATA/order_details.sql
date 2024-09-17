create table order_details(
	id int primary key auto_increment,
    order_id int,
    foreign key(order_id) references orders(id),
    product_id int,
    foreign key(product_id) references products(id),
    price float check(price >= 0),
    number_of_products int check(number_of_products > 0),
    total_money float check(total_money >= 0),
    color varchar(20) default ''
);