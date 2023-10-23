ALTER TABLE passwords
ALTER COLUMN "password" SET NOT NULL;

ALTER TABLE attachments
DROP COLUMN chat_id;

ALTER TABLE messages
RENAME COLUMN "text" TO text_message;

ALTER TABLE passwords
RENAME COLUMN "password" TO user_password;

ALTER TABLE profiles
RENAME COLUMN "name" TO first_name;

ALTER TABLE chats
RENAME COLUMN "type" TO chat_type;

ALTER TABLE participants
RENAME COLUMN "type" TO participant_type;

ALTER TABLE users
RENAME COLUMN "role" TO user_role;


ALTER TABLE messages
DROP CONSTRAINT fk64w44ngcpqp99ptcb9werdfmb,
ADD CONSTRAINT fk64w44ngcpqp99ptcb9werdfmb FOREIGN KEY (chat_id)
REFERENCES chats (id) ON DELETE CASCADE;

ALTER TABLE attachments
DROP CONSTRAINT fkcf4ta8qdkixetfy7wnqfv3vkv,
ADD CONSTRAINT fkcf4ta8qdkixetfy7wnqfv3vkv FOREIGN KEY (message_id)
REFERENCES messages (id) ON DELETE CASCADE;

ALTER TABLE participants
DROP CONSTRAINT fkscaw178b2h2dma5wnjrxngg5l,
ADD CONSTRAINT fkscaw178b2h2dma5wnjrxngg5l FOREIGN KEY (chat_id)
REFERENCES chats (id) ON DELETE CASCADE;

ALTER TABLE profiles
DROP CONSTRAINT fk410q61iev7klncmpqfuo85ivh,
ADD CONSTRAINT fk410q61iev7klncmpqfuo85ivh FOREIGN KEY (user_id)
REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE users
DROP CONSTRAINT fk8b9ebmptosmrxe2jbgytxbav6,
ADD CONSTRAINT fk8b9ebmptosmrxe2jbgytxbav6 FOREIGN KEY (password_id)
REFERENCES users (id) ON DELETE CASCADE;

