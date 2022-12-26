ALTER TABLE exercise ADD COLUMN weight_completed int;
ALTER TABLE exercise ADD COLUMN reps_completed int;

CREATE TABLE athlete_exercise_comment (
    id uuid DEFAULT gen_random_uuid(),
    athlete_id uuid REFERENCES athlete_data ON DELETE SET NULL,
    content TEXT,
    created_at DATE,
    updated_at DATE,

    PRIMARY KEY (id)
);

ALTER TABLE exercise ADD COLUMN athlete_comment_id uuid REFERENCES athlete_exercise_comment;