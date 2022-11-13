CREATE TABLE IF NOT EXISTS exercise (
    id uuid,
    name TEXT,
    sets INT,
    reps_per_set INT,
    day_id uuid,
    PRIMARY KEY (id)
);

ALTER TABLE exercise
ADD CONSTRAINT EXERCISE_DAY_ID_FK
FOREIGN KEY (day_id) REFERENCES days;