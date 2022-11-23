ALTER TABLE user_data ALTER COLUMN id SET DATA TYPE uuid USING (gen_random_uuid());

CREATE TYPE e_role AS ENUM ('athlete', 'coach');

CREATE TABLE IF NOT EXISTS role (
    id uuid DEFAULT gen_random_uuid(),
    name e_role,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_role (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL REFERENCES "user_data"(id) ON DELETE CASCADE,
    role_id uuid NOT NULL REFERENCES "role"(id) ON DELETE CASCADE
);

ALTER TABLE user_data DROP COLUMN user_type;
ALTER TABLE user_data ADD COLUMN username TEXT;
