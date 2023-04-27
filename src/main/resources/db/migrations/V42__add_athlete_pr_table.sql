DROP TABLE IF EXISTS athlete_record;

CREATE TABLE IF NOT EXISTS athlete_record (
  id uuid DEFAULT gen_random_uuid(),
  exercise_id uuid REFERENCES exercise,
  day_id uuid REFERENCES days,
  athlete_id uuid REFERENCES athlete_data,
  created_at timestamp with time zone DEFAULT now(),
  num_reps int DEFAULT 1,
  weight int DEFAULT 0,
  exercise_name text DEFAULT '',
  is_current bool DEFAULT true
);