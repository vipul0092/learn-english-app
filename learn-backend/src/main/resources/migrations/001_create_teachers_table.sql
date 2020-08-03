
CREATE TABLE TEACHERS (
  id uuid PRIMARY KEY,
  name varchar(50),
  email varchar(50),
  pass varchar(50),
  active boolean,
  created_date timestamp,
  last_modified_date timestamp
);