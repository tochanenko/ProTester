INSERT INTO "roles"(role_name)
VALUES ('ADMIN'),
       ('MANAGER'),
       ('ENGINEER');

INSERT INTO "statutes"(status_name)
VALUES ('PASSED'),
	   ('FINISHED'),
	   ('IN PROGRESS'),
	   ('RESUMED'),
	   ('SUSPENDED'),
	   ('STOPPED'),
	   ('NOT STARTED'),
	   ('FAILED'),
 	   ('UNKNOWN');

INSERT INTO "action_types"(action_type_name)
VALUES ('REST'),
	   ('UI'),
	   ('SQL'),
	   ('TECHNICAL');

INSERT INTO "projects"(project_name, project_website_link, project_active)
VALUES ('rozetka', 'https://rozetka.com.ua/', TRUE),
	   ('allo', 'https://allo.ua/', TRUE),
	   ('estore', 'https://estore.ua/', TRUE);

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

INSERT INTO "users"(role_id, user_name, user_password, user_email, user_active, user_full_name)
VALUES ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ADMIN'), 'admin', 'admin123', 'adminprotester@gmail.com', TRUE, 'Jason Statham'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'MANAGER'), 'promanager', 'qwerty123', 'managerprotester@ukr.net', TRUE, 'Kevin Link'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'MANAGER'), 'l_manager_l', 'qwerty123', 'managertester@ukr.net', TRUE, 'Vin Diesel'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'proengineer', 'engineer', 'engineerprotester@gmail.com', TRUE, 'Vito Armstrong'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'l_engineer_l', 'qwerty', 'engineertester@gmail.com', TRUE, 'Bruce Lee'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'engineIS', '123qwerty', 'enginispro@gmail.com', FALSE, 'Lucid Dream');

INSERT INTO "actions"(action_type_id, action_name, action_description)
VALUES ((SELECT action_type_id
		 FROM action_types
		 WHERE action_type_name = 'REST'), 'language', 'choose language'),
	   ((SELECT action_type_id
		 FROM action_types
		 WHERE action_type_name = 'REST'), 'filter', 'choose filter'),
	   ((SELECT action_type_id
		 FROM action_types
		 WHERE action_type_name = 'REST'), 'request', 'send get request by url"" and store result in variable "name"'),
	   ((SELECT action_type_id
		 FROM action_types
		 WHERE action_type_name = 'UI'), 'screenshot', 'make screenshot for an action'),
	   ((SELECT action_type_id
		 FROM action_types
		 WHERE action_type_name = 'TECHNICAL'), 'item click', 'opens the page of item'),
	   ((SELECT action_type_id
		 FROM action_types
		 WHERE action_type_name = 'TECHNICAL'), 'item add', 'moves the item to the cart');

INSERT INTO "libraries_actions"(library_id, action_id)
VALUES ((SELECT library_id
		 FROM libraries
		 WHERE library_name = 'rest library actions'),
	    (SELECT action_id
	     FROM actions
	     WHERE action_name = 'language')),
	   ((SELECT library_id
		 FROM libraries
		 WHERE library_name = 'rest library actions'),
	    (SELECT action_id
	     FROM actions
	     WHERE action_name = 'filter')),
	   ((SELECT library_id
		 FROM libraries
		 WHERE library_name = 'rest library actions'),
	    (SELECT action_id
	     FROM actions
	     WHERE action_name = 'request')),
	   ((SELECT library_id
		 FROM libraries
		 WHERE library_name = 'ui library actions'),
	    (SELECT action_id
	     FROM actions
	     WHERE action_name = 'screenshot'));

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
		 
INSERT INTO "compounds_actions"(action_id, compound_id)
VALUES ((SELECT action_id
		 FROM actions
		 WHERE action_name = 'item click'), 
		(SELECT compound_id
		 FROM compounds
		 WHERE compound_name = 'add item')),
	   ((SELECT action_id
		 FROM actions
		 WHERE action_name = 'item add'), 
		(SELECT compound_id
		 FROM compounds
		 WHERE compound_name = 'add item'));