CREATE TABLE IF NOT EXISTS team (
    id uuid DEFAULT gen_random_uuid(),
    name TEXT,
    description TEXT,
    created_at DATE,
    updated_at DATE,

    PRIMARY KEY (id)
);