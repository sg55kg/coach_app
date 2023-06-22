UPDATE TABLE exercise ADD COLUMN percent INTEGER;
UPDATE TABLE exercise ADD CONSTRAINT percent_valid CHECK((percent >= 0) AND (percent < 100));