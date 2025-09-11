INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-001', 'Mouse', 'Mouse de escritório', 50.0, 100, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-002', 'Teclado', 'Teclado de escritório', 150.0, 80, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-003', 'Monitor', 'Monitor LED 24 polegadas Full HD', 700.0, 50, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-004', 'Headset', 'Headset gamer com microfone', 200.0, 60, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-005', 'Webcam', 'Webcam HD para videoconferência', 120.0, 40, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-006', 'Impressora', 'Impressora multifuncional Wi-Fi', 450.0, 30, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-007', 'Cadeira Gamer', 'Cadeira ergonômica para jogos', 850.0, 2, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-008', 'Notebook', 'Notebook para trabalho de alta performance', 3200.0, 1, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES ('P-009', 'Roteador', 'Roteador dual-band com 4 antenas', 180.0, 70, true, '2025-09-10T20:30:00Z', null);
INSERT INTO tb_produto(sku, nome, descricao, preco, estoque, ativo, created_at, updated_at) VALUES (1'P-010', 'HD Externo', 'HD externo 1TB USB 3.0', 300.0, 0, false, '2025-09-10T20:30:00Z', null);

INSERT INTO tb_order(customer_id, status, total, created_at) VALUES (456, 'CREATED', 100.0, '2025-09-10T20:40:00Z');
INSERT INTO tb_order(customer_id, status, total, created_at) VALUES (456, 'PAID', 320.0, '2025-09-10T20:40:00Z');
INSERT INTO tb_order(customer_id, status, total, created_at) VALUES (590, 'CREATED', 200.0, '2025-09-10T20:40:00Z');
INSERT INTO tb_order(customer_id, status, total, created_at) VALUES (581, 'CANCELLED', 3200.0, '2025-09-10T20:40:00Z');

INSERT INTO tb_order_item(product_sku, quantity, price, order_id) VALUES ('P-001', 2, 50.0, 1);
INSERT INTO tb_order_item(product_sku, quantity, price, order_id) VALUES ('P-004', 1, 200.0, 2);
INSERT INTO tb_order_item(product_sku, quantity, price, order_id) VALUES ('P-005', 1, 120.0, 2);
INSERT INTO tb_order_item(product_sku, quantity, price, order_id) VALUES ('P-001', 1, 50.0, 3);
INSERT INTO tb_order_item(product_sku, quantity, price, order_id) VALUES ('P-002', 1, 150.0, 3);
INSERT INTO tb_order_item(product_sku, quantity, price, order_id) VALUES ('P-008', 1, 3200.0, 4);