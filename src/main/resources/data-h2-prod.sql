DROP TABLE IF EXISTS client;

CREATE TABLE client (
  id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  balance_in_pln decimal(15,2) NOT NULL
);

INSERT INTO client (id, first_name, last_name, balance_in_pln) VALUES
  (1, 'John', 'Rich', 100000.50),
  (2, 'Bob', 'Smart', 5000.99),
  (3, 'Don', 'Poor', 20.20),
  (4, 'Ben', 'Tall', 99.99),
  (5, 'Tom', 'Fat', 200);
