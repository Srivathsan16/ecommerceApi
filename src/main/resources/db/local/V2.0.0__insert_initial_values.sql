/*-- Inserting Products with UUID generated for each
INSERT INTO Products (id, name, price) VALUES
                                           (UUID_TO_BIN(UUID(), true), 'Rolex', 100),
                                           (UUID_TO_BIN(UUID(), true), 'Michael Kors', 80),
                                           (UUID_TO_BIN(UUID(), true), 'Swatch', 50),
                                           (UUID_TO_BIN(UUID(), true), 'Casio', 30);
-- Inserting Discounts with UUID generated for each
INSERT INTO Discounts (id, product_id, quantity_required, discounted_price)
SELECT UUID_TO_BIN(UUID(), true), id, 3, 200 FROM Products WHERE name = 'Rolex'
UNION ALL
SELECT UUID_TO_BIN(UUID(), true), id, 2, 120 FROM Products WHERE name = 'Michael Kors';

*/

INSERT INTO products (name, price) VALUES
      ('Rolex', 100.00),
      ('Michael Kors', 80.00),
      ('Swatch', 50.00),
      ('Casio', 30.00);

INSERT INTO discounts (product_id, quantity_required, discounted_price)
SELECT id, 3, 200.00 FROM products WHERE name = 'Rolex'
UNION ALL
SELECT id, 2, 120.00 FROM products WHERE name = 'Michael Kors';

