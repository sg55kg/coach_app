ALTER TABLE athlete_exercise_comment ADD COLUMN exercise_id uuid REFERENCES exercise ON DELETE CASCADE;