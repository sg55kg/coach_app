ALTER TABLE athlete_record ADD COLUMN date_holder TIMESTAMP WITH TIME ZONE NULL;
UPDATE athlete_record SET date_holder = last_updated::TIMESTAMP WITH TIME ZONE;
ALTER TABLE athlete_record ALTER COLUMN last_updated TYPE TIMESTAMP WITH TIME ZONE USING athlete_record.date_holder;
ALTER TABLE athlete_record DROP COLUMN date_holder;