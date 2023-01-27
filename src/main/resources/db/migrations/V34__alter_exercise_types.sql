ALTER TABLE cardio_exercise RENAME TO duration_exercise;

CREATE TABLE complex_exercise (
    id uuid DEFAULT gen_random_uuid(),
    rep_arr int [],
    name_arr text [],
    PRIMARY KEY (id)
);

ALTER TABLE exercise ADD COLUMN top_set uuid REFERENCES exercise(id) ON DELETE SET NULL;