DROP DATABASE IF EXISTS loan_management;
CREATE DATABASE loan_management;
USE loan_management;

CREATE TABLE customer (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(15),
    address TEXT,
    credit_score INT
);

CREATE TABLE loan (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    principal_amount DOUBLE,
    interest_rate DOUBLE,
    loan_term INT,
    loan_type VARCHAR(20),
    loan_status VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE homeloan (
    loan_id INT PRIMARY KEY,
    property_address VARCHAR(255),
    property_value INT,
    FOREIGN KEY (loan_id) REFERENCES loan(loan_id)
);

CREATE TABLE carloan (
    loan_id INT PRIMARY KEY,
    car_model VARCHAR(100),
    car_value INT,
    FOREIGN KEY (loan_id) REFERENCES loan(loan_id)
);
INSERT INTO loan (loan_id, customer_id, principal_amount, interest_rate, loan_term, loan_type, loan_status)
VALUES (1, 1, 20000, 5, 12, 'HomeLoan', 'Pending');