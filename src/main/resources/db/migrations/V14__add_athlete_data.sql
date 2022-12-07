CREATE TABLE IF NOT EXISTS athlete_data (
    id uuid DEFAULT gen_random_uuid(),
    current_program_id uuid REFERENCES programs,
    coach_id uuid REFERENCES coach_data,

    PRIMARY KEY (id)
);

ALTER TABLE user_data ADD COLUMN athlete_id uuid REFERENCES athlete_data;
ALTER TABLE programs ADD COLUMN athlete_id uuid REFERENCES athlete_data;