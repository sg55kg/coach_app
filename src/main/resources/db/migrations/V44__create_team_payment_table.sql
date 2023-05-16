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