-- Usuário admin inicial
INSERT INTO users (username, password, role, enabled)
VALUES ('jv', '$2a$10$Dow1HjOTUdWu08gH3y3cDeKmbM0YlT4WxyaWQIzHfUCLXK7IBbF72', 'ADMIN', true);
-- senha = 1234 (BCrypt)

-- Produtos da padaria
INSERT INTO produtos (nome, preco, quantidade)
VALUES ('Pão Francês', 0.50, 200);

INSERT INTO produtos (nome, preco, quantidade)
VALUES ('Coxinha de Frango', 5.00, 50);

INSERT INTO produtos (nome, preco, quantidade)
VALUES ('Sonho de Creme', 6.00, 30);

INSERT INTO produtos (nome, preco, quantidade)
VALUES ('Bolo de Chocolate', 25.00, 10);

INSERT INTO produtos (nome, preco, quantidade)
VALUES ('Café Expresso', 4.00, 100);

