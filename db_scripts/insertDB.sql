INSERT INTO "roles"(role_name)
VALUES ('ADMIN'),
       ('MANAGER'),
       ('ENGINEER');

INSERT INTO "statuses"(status_name)
VALUES ('PASSED'),
	   ('FINISHED'),
	   ('IN_PROGRESS'),
	   ('RESUMED'),
	   ('SUSPENDED'),
	   ('STOPPED'),
	   ('NOT_STARTED'),
	   ('FAILED'),
 	   ('UNKNOWN');

INSERT INTO "users"(role_id, user_username, user_password, user_email, user_active, user_first_name, user_last_name)
VALUES ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ADMIN'), 'admin', 'admin123', 'adminprotester@gmail.com', TRUE, 'Jason', 'Statham'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'MANAGER'), 'promanager', 'qwerty123', 'managerprotester@ukr.net', TRUE, 'Kevin', 'Link'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'MANAGER'), 'l_manager_l', 'qwerty123', 'managertester@ukr.net', TRUE, 'Vin', 'Diesel'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'proengineer', 'engineer', 'engineerprotester@gmail.com', TRUE, 'Vito', 'Armstrong'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'l_engineer_l', 'qwerty', 'engineertester@gmail.com', TRUE, 'Bruce', 'Lee'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'engineIS', '123qwerty', 'enginispro@gmail.com', FALSE, 'Lucid', 'Dream');
