DROP TABLE IF EXISTS athlete_program_stats;

ALTER TABLE accessory_exercise ADD COLUMN IF NOT EXISTS actual_intensity INT DEFAULT 0;

ALTER TABLE programs ADD COLUMN start_date_holder TIMESTAMP WITH TIME ZONE NULL;
ALTER TABLE programs ADD COLUMN end_date_holder TIMESTAMP WITH TIME ZONE NULL;
ALTER TABLE programs ADD COLUMN date_holder TIMESTAMP WITH TIME ZONE NULL;
ALTER TABLE programs ADD COLUMN create_holder TIMESTAMP WITH TIME ZONE NULL;
UPDATE programs SET start_date_holder = start_date::TIMESTAMP WITH TIME ZONE;
UPDATE programs SET end_date_holder = end_date::TIMESTAMP WITH TIME ZONE;
UPDATE programs SET date_holder = updated_at::TIMESTAMP WITH TIME ZONE;
UPDATE programs SET create_holder = created_at::TIMESTAMP WITH TIME ZONE;
ALTER TABLE programs ALTER COLUMN start_date TYPE TIMESTAMP WITH TIME ZONE USING programs.start_date_holder;
ALTER TABLE programs ALTER COLUMN end_date TYPE TIMESTAMP WITH TIME ZONE USING programs.end_date_holder;
ALTER TABLE programs ALTER COLUMN updated_at TYPE TIMESTAMP WITH TIME ZONE USING programs.date_holder;
ALTER TABLE programs ALTER COLUMN created_at TYPE TIMESTAMP WITH TIME ZONE USING programs.create_holder;
ALTER TABLE programs DROP COLUMN start_date_holder;
ALTER TABLE programs DROP COLUMN end_date_holder;
ALTER TABLE programs DROP COLUMN date_holder;
ALTER TABLE programs DROP COLUMN create_holder;