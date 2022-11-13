CREATE TABLE IF NOT EXISTS programs (
    id uuid,
    name TEXT,
    created_at DATE,
    updated_at DATE,
    start_date DATE,
    end_date DATE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS days (
    id uuid,
    date DATE,
    program_id uuid,
    PRIMARY KEY (id)
);

ALTER TABLE days
ADD CONSTRAINT DAYS_PROGRAM_ID_FK
FOREIGN KEY (program_id) REFERENCES programs;