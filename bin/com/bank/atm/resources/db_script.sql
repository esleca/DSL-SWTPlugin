drop database if exists bank_db;
CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;
create table clients(
	id int primary key,
	name varchar(50),
    lastname varchar(50)
);

create table accounts(
	account_number int NOT NULL AUTO_INCREMENT primary key,
	owner_id int,
    pin int,
	balance int
);

create table transactions(
	id int NOT NULL AUTO_INCREMENT primary key,
    account_number int,
    type ENUM('depósito','retiro'),
    amount int,
    date datetime
);

CREATE USER 'atm_user'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE ON * . * TO 'atm_user'@'localhost';
FLUSH PRIVILEGES;

insert into clients (id, name, lastname) values (1234,'Luis', 'Lopez');
insert into accounts (owner_id, pin, balance) values (1234, 123, 1000);