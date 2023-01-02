ALTER TABLE athlete_record ADD COLUMN created_at date DEFAULT now();
ALTER TABLE athlete_record ADD COLUMN snatch_balance int DEFAULT 0;
ALTER TABLE athlete_record ADD COLUMN last_updated text;