CREATE SEQUENCE IF NOT EXISTS chats_id_seq;
CREATE SEQUENCE IF NOT EXISTS messages_id_seq;
CREATE SEQUENCE IF NOT EXISTS attachments_id_seq;
CREATE SEQUENCE IF NOT EXISTS passwords_id_seq;
CREATE SEQUENCE IF NOT EXISTS users_id_seq;
CREATE SEQUENCE IF NOT EXISTS profiles_id_seq;
CREATE SEQUENCE IF NOT EXISTS settings_id_seq;

create TABLE IF NOT EXISTS passwords
(
    id bigint NOT NULL DEFAULT nextval('passwords_id_seq'::regclass),
    password character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT passwords_pkey PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    password_id bigint NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    role character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_password_id_key UNIQUE (password_id),
    CONSTRAINT fk8b9ebmptosmrxe2jbgytxbav6 FOREIGN KEY (password_id)
        REFERENCES passwords (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete NO ACTION,
    CONSTRAINT users_role_check CHECK (role::text = ANY (ARRAY['UNREGISTERED'::character varying, 'REGISTERED'::character varying, 'ADMIN'::character varying]::text[]))
);

create TABLE IF NOT EXISTS settings
(
    id bigint NOT NULL DEFAULT nextval('settings_id_seq'::regclass),
    setting character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT settings_pkey PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS profiles
(
    id bigint NOT NULL DEFAULT nextval('profiles_id_seq'::regclass),
    online_date timestamp(6) without time zone,
    setting_id bigint,
    user_id bigint,
    name character varying(255) COLLATE pg_catalog."default",
    photo character varying(255) COLLATE pg_catalog."default",
    surname character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT profiles_pkey PRIMARY KEY (id),
    CONSTRAINT profiles_user_id_key UNIQUE (user_id),
    CONSTRAINT fk410q61iev7klncmpqfuo85ivh FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete NO ACTION,
    CONSTRAINT fkk27lg0n4umrtg3bp2b3cnu428 FOREIGN KEY (setting_id)
        REFERENCES settings (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete NO ACTION
);

create TABLE IF NOT EXISTS chats
(
    id bigint NOT NULL DEFAULT nextval('chats_id_seq'::regclass),
    photo character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    type character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT chats_pkey PRIMARY KEY (id),
    CONSTRAINT chats_type_check CHECK (type::text = ANY (ARRAY['PRIVATE'::character varying, 'GROUP'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS messages
(
    chat_id bigint,
    create_date timestamp(6) without time zone,
    id bigint NOT NULL DEFAULT nextval('messages_id_seq'::regclass),
    reply_message_id bigint,
    sender_id bigint,
    state character varying(255) COLLATE pg_catalog."default",
    text text COLLATE pg_catalog."default",
    CONSTRAINT messages_pkey PRIMARY KEY (id),
    CONSTRAINT fk1yq4n2d8fwdqcr8x3u1bmiypf FOREIGN KEY (reply_message_id)
        REFERENCES messages (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk64w44ngcpqp99ptcb9werdfmb FOREIGN KEY (chat_id)
        REFERENCES chats (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk79kgt6oyju1ma9ly4qmax5933 FOREIGN KEY (sender_id)
        REFERENCES profiles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT messages_state_check CHECK (state::text = ANY (ARRAY['SEND_ERROR'::character varying, 'SENT'::character varying, 'READ'::character varying]::text[]))
);

create TABLE IF NOT EXISTS attachments
(
    message_id bigint,
    id bigint NOT NULL DEFAULT nextval('attachments_id_seq'::regclass),
    name_file character varying(255) COLLATE pg_catalog."default",
    chat_id bigint,
    CONSTRAINT attachments_pkey PRIMARY KEY (id),
    CONSTRAINT fkcf4ta8qdkixetfy7wnqfv3vkv FOREIGN KEY (message_id)
        REFERENCES messages (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete NO ACTION
);

CREATE TABLE IF NOT EXISTS forwarded_messages
(
    forwarded_from_id bigint NOT NULL,
    message_id bigint NOT NULL,
    CONSTRAINT forwarded_messages_forwarded_from_id_key UNIQUE (forwarded_from_id),
    CONSTRAINT fkbt24ddndkgpgmk2d4d1sbdv41 FOREIGN KEY (message_id)
        REFERENCES messages (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkd67dbko1fs3ls7ckjd2004kfl FOREIGN KEY (forwarded_from_id)
        REFERENCES messages (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

create TABLE IF NOT EXISTS participants
(
    chat_id bigint NOT NULL,
    profile_id bigint NOT NULL,
    type character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT participants_pkey PRIMARY KEY (chat_id, profile_id),
    CONSTRAINT fk10f9tie0lg5dbyha102xl70bn FOREIGN KEY (profile_id)
        REFERENCES profiles (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete NO ACTION,
    CONSTRAINT fkscaw178b2h2dma5wnjrxngg5l FOREIGN KEY (chat_id)
        REFERENCES chats (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete NO ACTION,
    CONSTRAINT participants_type_check CHECK (type::text = ANY (ARRAY['ADMIN'::character varying, 'REGULAR'::character varying, 'INVITED'::character varying]::text[]))
);
