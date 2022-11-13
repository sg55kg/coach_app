ALTER TABLE programs DROP COLUMN created_at;
ALTER TABLE programs DROP COLUMN updated_at;
ALTER TABLE programs ADD COLUMN created_at DATE DEFAULT now();
ALTER TABLE programs ADD COLUMN updated_at DATE DEFAULT now();
