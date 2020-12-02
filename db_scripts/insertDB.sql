INSERT INTO "roles"(role_name)
VALUES ('ADMIN'),
       ('MANAGER'),
       ('ENGINEER');

INSERT INTO "statuses"(status_name)
VALUES ('PASSED'),
	   ('FINISHED'),
	   ('IN PROGRESS'),
	   ('RESUMED'),
	   ('SUSPENDED'),
	   ('STOPPED'),
	   ('NOT STARTED'),
	   ('FAILED'),
 	   ('UNKNOWN');

INSERT INTO "libraries"(library_name, library_description)
VALUES ('rest library actions', 'The list of all rest actions'),
	   ('ui library actions', 'The list of all ui actions'),
	   ('sql library actions', 'The list of all sql actions'),
	   ('technical library actions', 'The list of all techical actions'),
	   ('registration library compounds', 'The list of all registations compounds');

INSERT INTO "compounds"(compound_name, compound_description)
VALUES ('fail registration', 'The system should not register a user'),
	   ('pass registration', 'The system should register a user'), 
	   ('add item', 'Add item to cart, cart must have 1 item'),
	   ('check cart', 'Add item to cart then delete it, cart must be empty');

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

INSERT INTO "projects"(creator_id, project_name, project_website_link, project_active)
VALUES ((SELECT user_id
		FROM users
		WHERE user_username = 'admin'), 'rozetka', 'https://rozetka.com.ua/', TRUE),
	   ((SELECT user_id
		FROM users
		WHERE user_username = 'promanager'), 'allo', 'https://allo.ua/', TRUE),
	   ((SELECT user_id
		FROM users
		WHERE user_username = 'l_engineer_l'), 'estore', 'https://estore.ua/', TRUE);

INSERT INTO "libraries_compounds"(library_id, compound_id)
VALUES ((SELECT library_id
		 FROM libraries
		 WHERE library_name = 'registration library compounds'),
	    (SELECT compound_id
	     FROM compounds
	     WHERE compound_name = 'fail registration')),
	   ((SELECT library_id
		 FROM libraries
		 WHERE library_name = 'registration library compounds'),
	    (SELECT compound_id
	     FROM compounds
	     WHERE compound_name = 'pass registration'));
