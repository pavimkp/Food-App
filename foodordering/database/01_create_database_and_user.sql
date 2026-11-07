-- Run once in MySQL Workbench as root. (You already did this if you followed Phase 3.)
CREATE DATABASE IF NOT EXISTS food_ordering_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Replace the password with your own. Use the SAME value for the DB_PASSWORD environment variable.
CREATE USER IF NOT EXISTS 'food_app'@'localhost' IDENTIFIED BY 'ChangeMe_123!';
GRANT SELECT, INSERT, UPDATE, DELETE ON food_ordering_db.* TO 'food_app'@'localhost';
FLUSH PRIVILEGES;
