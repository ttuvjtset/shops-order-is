-- DROP SCHEMA public CASCADE;

CREATE SEQUENCE seq1
  START WITH 1;

CREATE TABLE orders (
  id          BIGINT NOT NULL PRIMARY KEY,
  orderNumber VARCHAR(255),
  orderRows   VARCHAR(255)
);

CREATE TABLE orderrow (
  orderId     BIGINT NOT NULL,
  itemName    VARCHAR(255),
  quantity    INT,
  price       INT
);