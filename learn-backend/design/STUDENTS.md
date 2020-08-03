## STUDENTS table schema
- id [UUID]: Unique id <- PRIMARY KEY
- teacher_id: [UUID]: Teacher id for the student (FK on id column in TEACHERS table)
- name [NVARCHAR(50)]: Name
- email [NVARCHAR(50)]: Email id
- pass [NVARCHAR(50)]: Password hash
- valid_until [timestamp]: Until when is the student valid
- created_date [timestamp]: Timestamp when the record was created
- last_modified_date [timestamp]: Timestamp when the record was last modified