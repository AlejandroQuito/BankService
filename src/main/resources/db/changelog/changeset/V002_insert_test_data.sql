INSERT INTO users (name, date_of_birth, password) VALUES
('Иван Иванов', '1990-05-10', '$2a$12$hash1'),
('Мария Петрова', '1985-11-22', '$2a$12$hash2'),
('Алексей Смирнов', '1995-07-15', '$2a$12$hash3'),
('Елена Кузнецова', '1988-03-30', '$2a$12$hash4'),
('Дмитрий Волков', '1992-12-11', '$2a$12$hash5'),
('Ольга Морозова', '1983-08-19', '$2a$12$hash6'),
('Сергей Никитин', '1991-01-07', '$2a$12$hash7'),
('Татьяна Федорова', '1987-05-25', '$2a$12$hash8'),
('Михаил Васильев', '1994-09-14', '$2a$12$hash9'),
('Наталья Соколова', '1989-04-05', '$2a$12$hash10');

INSERT INTO account (user_id, balance, initial_balance) VALUES
(1, 1500.00, 1500.00),
(2, 2500.50, 2500.50),
(3, 500.00, 500.00),
(4, 1200.00, 1200.00),
(5, 750.00, 750.00),
(6, 3000.00, 3000.00),
(7, 100.00, 100.00),
(8, 1800.75, 1800.75),
(9, 950.25, 950.25),
(10, 2000.00, 2000.00);

INSERT INTO email_data (user_id, email) VALUES
(1, 'ivan.ivanov@example.com'),
(2, 'maria.petrova@example.com'),
(3, 'alexey.smirnov@example.com'),
(4, 'elena.kuznetsova@example.com'),
(5, 'dmitry.volkov@example.com'),
(6, 'olga.morozova@example.com'),
(7, 'sergey.nikitin@example.com'),
(8, 'tatiana.fedorova@example.com'),
(9, 'mikhail.vasiliev@example.com'),
(10, 'natalia.sokolova@example.com');

INSERT INTO phone_data (user_id, phone) VALUES
(1, '79001112233'),
(2, '79002223344'),
(3, '79003334455'),
(4, '79004445566'),
(5, '79005556677'),
(6, '79006667788'),
(7, '79007778899'),
(8, '79008889900'),
(9, '79009990011'),
(10, '79001001122');
