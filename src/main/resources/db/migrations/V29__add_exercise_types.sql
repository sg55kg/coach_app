ALTER TABLE exercise ADD COLUMN sets_completed int DEFAULT 0;
ALTER TABLE exercise DROP COLUMN weight_intensity;

CREATE TABLE carry_exercise (
    distance_meters int DEFAULT 0,
    unilateral bool DEFAULT false,
    distance_completed_meters int DEFAULT 0,
    weight_intensity int DEFAULT 0,
    effort_intensity int DEFAULT 0
) INHERITS (exercise);

CREATE TABLE accessory_exercise (
    equipment text,
    weight_intensity int DEFAULT 0,
    effort_intensity int DEFAULT 0,
    unilateral bool DEFAULT false
) INHERITS (exercise);

CREATE TABLE cardio_exercise (
    equipment text,
    effort_intensity int DEFAULT 0,
    actual_intensity int DEFAULT 0,
    distance_meters int DEFAULT 0,
    distance_completed_meters int DEFAULT 0,
    secondsPerSet int DEFAULT 0,
    secondsPerSetCompleted int DEFAULT 0
) INHERITS (exercise);

CREATE TABLE warm_up (
    id uuid DEFAULT gen_random_uuid(),
    day_id uuid REFERENCES days,
    instructions text,
    PRIMARY KEY (id)
);

ALTER TABLE days ADD COLUMN warm_up_id uuid REFERENCES warm_up;