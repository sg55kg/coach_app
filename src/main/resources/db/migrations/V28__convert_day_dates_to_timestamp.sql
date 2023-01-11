ALTER TABLE days ADD COLUMN date_holder TIMESTAMP WITH TIME ZONE NULL;
UPDATE days SET date_holder = date::TIMESTAMP WITH TIME ZONE;
ALTER TABLE days ALTER COLUMN date TYPE TIMESTAMP WITH TIME ZONE USING days.date_holder;
ALTER TABLE days DROP COLUMN date_holder;