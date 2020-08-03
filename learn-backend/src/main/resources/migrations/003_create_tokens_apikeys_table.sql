
CREATE TABLE TOKENS (
  id uuid PRIMARY KEY NOT NULL,
  metadata json NOT NULL
);

CREATE TABLE APIKEYS (
  id uuid PRIMARY KEY NOT NULL,
  roles varchar(100) NOT NULL
);

INSERT INTO APIKEYS (id, roles)
VALUES (uuid_generate_v4(), 'ALL')
