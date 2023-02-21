DROP TABLE IF EXISTS chat_room CASCADE;
DROP TABLE IF EXISTS message CASCADE;

CREATE TABLE chat_room (
  id uuid DEFAULT gen_random_uuid(),
  team_id uuid REFERENCES team ON DELETE cascade,
  PRIMARY KEY (id)
);

CREATE TABLE chat_room_member (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid REFERENCES user_data ON DELETE cascade,
    chat_room_id uuid REFERENCES chat_room ON DELETE cascade,
    PRIMARY KEY (id)
);

CREATE TABLE message (
  id uuid DEFAULT gen_random_uuid(),
  sender_id uuid REFERENCES chat_room_member ON DELETE cascade,
  contents text,
  sent_at TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NULL,
  chat_id uuid REFERENCES chat_room ON DELETE cascade,
  parent_id uuid REFERENCES message ON DELETE SET NULL,
  PRIMARY KEY (id)
);

