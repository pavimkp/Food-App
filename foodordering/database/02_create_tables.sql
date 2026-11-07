-- Run once in MySQL Workbench. (You already did this if you followed Phase 3.)
USE food_ordering_db;

CREATE TABLE IF NOT EXISTS restaurants (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(500) NULL,
  address     VARCHAR(255) NOT NULL,
  image_url   VARCHAR(500) NULL,
  active      BOOLEAN      NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS food_items (
  id            BIGINT        NOT NULL AUTO_INCREMENT,
  restaurant_id BIGINT        NOT NULL,
  name          VARCHAR(100)  NOT NULL,
  description   VARCHAR(500)  NULL,
  price         DECIMAL(10,2) NOT NULL,
  image_url     VARCHAR(500)  NULL,
  available     BOOLEAN       NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id),
  CONSTRAINT fk_food_items_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
  CONSTRAINT chk_food_items_price CHECK (price >= 0),
  INDEX idx_food_items_restaurant (restaurant_id),
  INDEX idx_food_items_name (name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS orders (
  id               BIGINT        NOT NULL AUTO_INCREMENT,
  customer_name    VARCHAR(100)  NOT NULL,
  phone            VARCHAR(20)   NOT NULL,
  delivery_address VARCHAR(255)  NOT NULL,
  total_amount     DECIMAL(10,2) NOT NULL,
  status           VARCHAR(20)   NOT NULL DEFAULT 'PLACED',
  payment_method   VARCHAR(20)   NOT NULL DEFAULT 'COD',
  created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT chk_orders_total  CHECK (total_amount >= 0),
  CONSTRAINT chk_orders_status CHECK (status IN ('PLACED','PREPARING','DELIVERED','CANCELLED')),
  INDEX idx_orders_created_at (created_at)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS order_items (
  id           BIGINT        NOT NULL AUTO_INCREMENT,
  order_id     BIGINT        NOT NULL,
  food_item_id BIGINT        NOT NULL,
  quantity     INT           NOT NULL,
  unit_price   DECIMAL(10,2) NOT NULL,
  subtotal     DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_order_items_order     FOREIGN KEY (order_id)     REFERENCES orders (id) ON DELETE CASCADE,
  CONSTRAINT fk_order_items_food_item FOREIGN KEY (food_item_id) REFERENCES food_items (id),
  CONSTRAINT chk_order_items_qty CHECK (quantity > 0),
  INDEX idx_order_items_order (order_id),
  INDEX idx_order_items_food_item (food_item_id)
) ENGINE=InnoDB;

SHOW TABLES;
