CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(500) NOT NULL,
    date_of_birth DATE NOT NULL,
    password VARCHAR(500) NOT NULL CHECK (char_length(password) >= 8)
);

CREATE TABLE IF NOT EXISTS account (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL UNIQUE,
    balance DECIMAL(19, 2) NOT NULL CHECK (balance >= 0),
    initial_balance DECIMAL(19, 2) NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS email_data (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    CONSTRAINT fk_email_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_email_data_user_id ON email_data(user_id);

CREATE TABLE IF NOT EXISTS phone_data (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    phone VARCHAR(11) NOT NULL UNIQUE CHECK (phone ~ '^\d{11}$'),
    CONSTRAINT fk_phone_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_phone_data_user_id ON phone_data(user_id);
