## TEACHERS table schema
- id [UUID]: Unique id <- PRIMARY KEY
- name [NVARCHAR(50)]: Name
- email [NVARCHAR(50)]: Email id
- pass [NVARCHAR(50)]: Password hash
- active [boolean]: Whether the teacher is active
- created_date [timestamp]: Timestamp when the record was created
- last_modified_date [timestamp]: Timestamp when the record was last modified