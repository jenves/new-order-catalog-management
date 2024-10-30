CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    type VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL
);
