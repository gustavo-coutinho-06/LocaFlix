CREATE TABLE plans (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    allowed_rental_quantity INTEGER NOT NULL,
    CONSTRAINT uq_plans_name UNIQUE (name),
    CONSTRAINT ck_plans_allowed_rental_quantity CHECK (allowed_rental_quantity > 0)
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    age INTEGER NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    plan_id UUID NOT NULL,
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT ck_users_age CHECK (age >= 0),
    CONSTRAINT fk_users_plan FOREIGN KEY (plan_id) REFERENCES plans (id)
);

CREATE INDEX idx_users_plan_id ON users (plan_id);
