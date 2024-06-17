/*    -- V1__Create_tables.sql
    CREATE TABLE IF NOT EXISTS Products (
        id BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID(), true)),
        name VARCHAR(255) NOT NULL,
        price DECIMAL(19,2) NOT NULL  -- Assumption is that it is Sufficient for monetary values
        );

    CREATE TABLE IF NOT EXISTS Discounts (
        id BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID(), true)),
        product_id BINARY(16) NOT NULL,
        quantity_required INT NOT NULL,
        discounted_price DECIMAL(19,2) NOT NULL,
        FOREIGN KEY (product_id) REFERENCES Products(id)
        );
*/


CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19,2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS discounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity_required INT NOT NULL,
    discounted_price DECIMAL(19,2) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
    );
