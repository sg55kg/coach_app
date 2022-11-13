CREATE TABLE IF NOT EXISTS programs (
    id uuid DEFAULT gen_random_uuid(),
    name TEXT,
    created_at DATE,
    updated_at DATE,
    start_date DATE,
    end_date DATE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS days (
    id uuid DEFAULT gen_random_uuid(),
    date DATE,
    program_id uuid,
    PRIMARY KEY (id)
);

ALTER TABLE days
ADD CONSTRAINT DAYS_PROGRAM_ID_FK
FOREIGN KEY (program_id) REFERENCES programs;

CREATE TABLE IF NOT EXISTS exercise (
    id uuid DEFAULT gen_random_uuid(),
    name TEXT,
    sets INT,
    reps_per_set INT,
    day_id uuid,
    PRIMARY KEY (id)
);

ALTER TABLE exercise
ADD CONSTRAINT EXERCISE_DAY_ID_FK
FOREIGN KEY (day_id) REFERENCES days;