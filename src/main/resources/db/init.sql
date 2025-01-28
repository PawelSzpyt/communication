CREATE TABLE IF NOT EXISTS communications (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    delivery_settings TEXT NOT NULL,
    size BIGINT NOT NULL,
    type VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'LOADED' -- Make status NOT NULL
);