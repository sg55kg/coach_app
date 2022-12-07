CREATE TABLE IF NOT EXISTS coach_data (
    id uuid DEFAULT gen_random_uuid(),
    PRIMARY KEY (id)
);

ALTER TABLE user_data ADD COLUMN coach_id uuid REFERENCES coach_data;
ALTER TABLE programs DROP COLUMN coach_id;
ALTER TABLE programs ADD COLUMN coach_id uuid REFERENCES coach_data;