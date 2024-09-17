create table social_accounts(
	id int primary key auto_increment,
    provider varchar(20) not null,
    provider_id varchar(50) not null,
    email varchar(150) not null comment 'Email tài khoản',
    name varchar(100) not null comment 'Tên người dùng',
    user_id int,
    foreign key(user_id) references users(id)
);