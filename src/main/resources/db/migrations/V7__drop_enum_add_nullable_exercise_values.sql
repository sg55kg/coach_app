ALTER TABLE exercise ALTER COLUMN weight_intensity TYPE int USING 0;
DROP TYPE weighted_exercise_intensity;

ALTER TABLE exercise ALTER COLUMN weight_intensity SET DEFAULT 0;
ALTER TABLE exercise ALTER COLUMN is_max SET DEFAULT false;