DROP SCHEMA PUBLIC CASCADE;

CREATE SEQUENCE order_sequence AS INTEGER START WITH 1;

CREATE TABLE orders (
  id BIGINT NOT NULL PRIMARY KEY,
  order_number VARCHAR(255)
);

CREATE TABLE order_rows (
  item_name VARCHAR(255),
  price INT,
  quantity INT,
  orders_id BIGINT,
  FOREIGN KEY (orders_id)
  REFERENCES orders ON DELETE CASCADE
);