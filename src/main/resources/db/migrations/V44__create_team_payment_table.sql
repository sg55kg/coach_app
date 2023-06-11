CREATE TABLE IF NOT EXISTS team_finance (
    id UUID DEFAULT gen_random_uuid(),
    coach_id UUID REFERENCES coach_data ON DELETE CASCADE,
    team_id UUID REFERENCES team ON DELETE CASCADE,
    stripe_connect_id TEXT DEFAULT '' NOT NULL,
    stripe_status TEXT DEFAULT 'NEW',
    team_price INT DEFAULT 0,
    invite_only BOOLEAN DEFAULT FALSE,
    athlete_cap INT DEFAULT 0,
    request_required BOOLEAN DEFAULT FALSE,
    CONSTRAINT stripe_status_valid CHECK (stripe_status IN ('NEW', 'ONBOARDING', 'CREATED')),
    CONSTRAINT athlete_cap_positive CHECK (athlete_cap >= 0),
    PRIMARY KEY (id)
);

ALTER TABLE team DROP COLUMN IF EXISTS team_payment;

ALTER TABLE team ADD COLUMN IF NOT EXISTS team_finance_id UUID references team_finance DEFAULT NULL;

CREATE TABLE IF NOT EXISTS athlete_payment_record (
    id UUID DEFAULT gen_random_uuid(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    athlete_id UUID REFERENCES athlete_data ON DELETE SET NULL,
    stripe_connect_id TEXT DEFAULT '',
    team_finance_id UUID REFERENCES team_finance ON DELETE SET NULL,
    amount_paid bigint DEFAULT 0,
    payment_status TEXT DEFAULT 'NEW',
    stripe_session_id TEXT DEFAULT '',
    CONSTRAINT payment_status_valid CHECK (payment_status IN ('NEW', 'SUCCESS', 'FAIL')),
    PRIMARY KEY (id)
);

ALTER TABLE user_preference ADD COLUMN IF NOT EXISTS default_home_page TEXT CHECK (default_home_page IN ('coach', 'athlete')) DEFAULT 'athlete';