ALTER TABLE programs
    ADD coach_id UUID;
ALTER TABLE programs
    ADD CONSTRAINT programs_coach_fk_coach_id
        FOREIGN KEY (coach_id) REFERENCES user_data (id) ON DELETE cascade;
