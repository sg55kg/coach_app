CREATE TABLE IF NOT EXISTS user_data (
    id text,
    created_at DATE,
    updated_at DATE,
    user_type int,
    email text,

    PRIMARY KEY (id)
);