ALTER TABLE exercise ADD COLUMN percent INTEGER;
ALTER TABLE exercise ADD CONSTRAINT percent_valid CHECK((percent >= 0) AND (percent < 100));