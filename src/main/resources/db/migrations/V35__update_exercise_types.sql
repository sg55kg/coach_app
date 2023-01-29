DROP TABLE carry_exercise;
DROP TABLE accessory_exercise;
DROP TABLE duration_exercise;
DROP TABLE complex_exercise;

CREATE TABLE IF NOT EXISTS carry_exercise (
    id uuid,
    distance_meters int DEFAULT 0,
    unilateral bool DEFAULT false,
    distance_completed_meters int DEFAULT 0,
    weight_intensity int DEFAULT 0,
    effort_intensity int DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS accessory_exercise (
    id uuid,
    equipment text,
    weight_intensity int DEFAULT 0,
    effort_intensity int DEFAULT 0,
    unilateral bool DEFAULT false,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS complex_exercise (
    id uuid,
    rep_arr text,
    name_arr text,
    rep_completed_arr text,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS duration_exercise (
    id uuid,
    equipment text,
    effort_intensity int DEFAULT 0,
    actual_intensity int DEFAULT 0,
    distance_meters int DEFAULT 0,
    distance_completed_meters int DEFAULT 0,
    seconds_per_set int DEFAULT 0,
    seconds_per_set_completed int DEFAULT 0,
    PRIMARY KEY (id)
);