
CREATE TABLE STUDENTS (
  id uuid PRIMARY KEY NOT NULL,
  teacher_id uuid NOT NULL,
  name varchar(50) NOT NULL,
  email varchar(50) NOT NULL,
  pass varchar(50) NOT NULL,
  valid_until timestamp,
  created_date timestamp NOT NULL,
  last_modified_date timestamp NOT NULL,

  FOREIGN KEY (teacher_id) REFERENCES TEACHERS (id) ON DELETE CASCADE
);