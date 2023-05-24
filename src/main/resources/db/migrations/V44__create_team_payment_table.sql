CREATE TABLE IF NOT EXISTS team_finance (
    id UUID DEFAULT gen_random_uuid(),
    coach_id UUID REFERENCES coach_data ON DELETE CASCADE,
    team_id UUID REFERENCES team ON DELETE CASCADE,
    stripe_connect_id TEXT DEFAULT '' NOT NULL,
    stripe_status TEXT CHECK (stripe_status IN ('NEW', 'ONBOARDING', 'CREATED')) DEFAULT 'NEW',
    team_price INT DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE team ADD COLUMN IF NOT EXISTS team_finance_id UUID references team_finance DEFAULT NULL;

CREATE TABLE IF NOT EXISTS athlete_payment_record (
    id UUID DEFAULT gen_random_uuid(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    athlete_id UUID REFERENCES athlete_data ON DELETE SET NULL,
    stripe_connect_id TEXT DEFAULT '',
    team_finance_id UUID REFERENCES team_finance ON DELETE SET NULL,
    amount_paid bigint DEFAULT 0,
    payment_status TEXT CHECK (payment_status IN ('NEW', 'SUCCESS', 'FAIL')) DEFAULT 'NEW',
    PRIMARY KEY (id)
)