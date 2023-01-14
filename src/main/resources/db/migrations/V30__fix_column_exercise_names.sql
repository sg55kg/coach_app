ALTER TABLE cardio_exercise DROP COLUMN secondsperset;
ALTER TABLE cardio_exercise DROP COLUMN secondspersetcompleted;
ALTER TABLE cardio_exercise ADD COLUMN seconds_per_set int DEFAULT 0;
ALTER TABLE cardio_exercise ADD COLUMN seconds_per_set_completed int DEFAULT 0;