CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status VARCHAR(255) NOT NULL,
    discount INTEGER CHECK (discount >= 0),
    total DECIMAL(19, 2)
);