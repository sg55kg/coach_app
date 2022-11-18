CREATE TYPE weighted_exercise_intensity AS ENUM ('light', 'moderate', 'heavy', 'body_weight');

ALTER TABLE exercise ADD COLUMN weight_intensity weighted_exercise_intensity;