CREATE TABLE athlete_program_stats (
    id uuid DEFAULT gen_random_uuid(),
    athlete_id uuid REFERENCES athlete_data,
    total_weight int DEFAULT 0,
    total_reps int DEFAULT 0,
    avg_weight_per_week int DEFAULT 0,
    avg_reps_per_week int DEFAULT 0,
    percent_snatch int DEFAULT 0,
    percent_clean int DEFAULT 0,
    percent_jerk int DEFAULT 0,
    percent_overhead int DEFAULT 0,
    percent_squats int DEFAULT 0,
    percent_pulls int DEFAULT 0,
    percent_lower_body int DEFAULT 0,
    percent_upper_body int DEFAULT 0,
    percent_accessories int DEFAULT 0,


    PRIMARY KEY (id)
);