
DROP TABLE TOKENS, APIKEYS;

CREATE TABLE TOKENS (
    id uuid PRIMARY KEY NOT NULL,
    metadata json NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_date timestamp NOT NULL
);

CREATE TABLE APIKEYS (
    id uuid PRIMARY KEY NOT NULL,
    roles varchar(100) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_date timestamp NOT NULL
);

INSERT INTO APIKEYS (id, roles, created_date, last_modified_date)
VALUES (uuid_generate_v4(), 'ADMIN', current_timestamp, current_timestamp)
