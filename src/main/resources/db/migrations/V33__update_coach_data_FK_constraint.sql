ALTER TABLE user_data DROP CONSTRAINT user_data_coach_id_fkey;
ALTER TABLE user_data ADD CONSTRAINT user_data_coach_id_fkey FOREIGN KEY (coach_id) REFERENCES coach_data(id) ON DELETE SET NULL;