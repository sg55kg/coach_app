ALTER TABLE team ADD COLUMN team_logo TEXT DEFAULT '';

ALTER TABLE athlete_record ADD COLUMN muscle_snatch INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN hang_muscle_snatch INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN deficit_snatch INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN deficit_clean INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN romanian_deadlift INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN snatch_romanian_deadlift INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN deficit_deadlift INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN block_deadlift INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN pause_back_squat INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN pause_front_squat INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN pause_jerk_dip INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN pause_jerk_catch INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN low_hang_snatch INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN low_hang_clean INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN high_hang_snatch INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN high_hang_clean INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN sumo_deadlift INT DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN pause_deadlift INT DEFAULT 0;

