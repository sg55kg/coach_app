CREATE TABLE IF NOT EXISTS user_preference (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid REFERENCES user_data ON DELETE CASCADE,
    weight TEXT CHECK (weight IN ('kg', 'lb')) DEFAULT 'kg',
    mode TEXT CHECK (mode IN ('light', 'dark')) DEFAULT 'dark',
    PRIMARY KEY (id)
);

INSERT INTO user_preference (user_id, weight, mode)
SELECT id, 'kg', 'dark'
FROM user_data;