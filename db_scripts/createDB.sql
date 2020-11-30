DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS tokens CASCADE;
DROP TABLE IF EXISTS action_declarations CASCADE;
DROP TABLE IF EXISTS actions CASCADE;
DROP TABLE IF EXISTS action_parameters CASCADE;
DROP TABLE IF EXISTS compounds CASCADE;
DROP TABLE IF EXISTS compounds_actions CASCADE;
DROP TABLE IF EXISTS libraries CASCADE;
DROP TABLE IF EXISTS libraries_actions CASCADE;
DROP TABLE IF EXISTS libraries_compounds CASCADE;
DROP TABLE IF EXISTS tests_scenarios CASCADE;
DROP TABLE IF EXISTS tests_scenarios_actions_compounds CASCADE;
DROP TABLE IF EXISTS data_sets CASCADE;
DROP TABLE IF EXISTS parameters CASCADE;
DROP TABLE IF EXISTS statutes CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS test_cases CASCADE;
DROP TABLE IF EXISTS test_cases_watchers CASCADE;
DROP TABLE IF EXISTS result_tests CASCADE;
​
CREATE TABLE roles (
                       role_id		SERIAL PRIMARY KEY,
                       role_name	VARCHAR(16) UNIQUE NOT NULL
);
​
CREATE TABLE users (
                       user_id			SERIAL PRIMARY KEY,
                       role_id			INTEGER			   NOT NULL,
                       user_username	VARCHAR(32) UNIQUE NOT NULL,
                       user_password	CHAR(64) 		   NOT NULL,
                       user_email 		VARCHAR(32) UNIQUE NOT NULL,
                       user_active		BOOLEAN			   NOT NULL,
                       user_first_name	VARCHAR(32) 	   NOT NULL,
                       user_last_name	VARCHAR(32) 	   NOT NULL,
                       CONSTRAINT user_role_fk FOREIGN KEY (role_id) REFERENCES roles (role_id)
);
​
CREATE TABLE tokens (
                        token_id			SERIAL PRIMARY KEY,
                        user_id				INTEGER 		   NOT NULL,
                        token_value			VARCHAR(64)	UNIQUE NOT NULL,
                        token_expiry_date	TIMESTAMPTZ,
                        CONSTRAINT token_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
​
CREATE TABLE action_declarations (
                                     action_declaration_id		SERIAL PRIMARY KEY,
                                     action_declaration_class	VARCHAR(128) NOT NULL
);
​
CREATE TABLE actions (
                         action_id			  SERIAL PRIMARY KEY,
                         action_declaration_id INTEGER NOT NULL,
                         action_description	  TEXT,
                         CONSTRAINT action_decl_fk FOREIGN KEY (action_declaration_id) REFERENCES action_declarations (action_declaration_id) ON DELETE CASCADE
);
​
CREATE TABLE action_parameters(
                                  action_parameter_id	SERIAL PRIMARY KEY,
                                  action_id 			INTEGER NOT NULL,
                                  action_key			VARCHAR(128) NOT NULL,
                                  action_value		VARCHAR(512) NOT NULL,
                                  CONSTRAINT ap_action_fk FOREIGN KEY (action_id) REFERENCES actions (action_id) ON DELETE CASCADE
);
​
CREATE TABLE compounds (
                           compound_id				SERIAL PRIMARY KEY,
                           compound_name			VARCHAR(32)	NOT NULL,
                           compound_description 	TEXT
);
​
CREATE TABLE compounds_actions (
                                   action_id	INTEGER NOT NULL,
                                   compound_id INTEGER NOT NULL,
                                   CONSTRAINT ca_action_fk   FOREIGN KEY (action_id)   REFERENCES actions (action_id)     ON DELETE CASCADE,
                                   CONSTRAINT ca_compound_fk FOREIGN KEY (compound_id) REFERENCES compounds (compound_id) ON DELETE CASCADE
);
​
CREATE TABLE libraries (
                           library_id 				SERIAL PRIMARY KEY,
                           library_name 			VARCHAR(32) NOT NULL,
                           library_description 	TEXT
);
​
CREATE TABLE libraries_actions (
                                   library_id	INTEGER NOT NULL,
                                   action_id	INTEGER	NOT NULL,
                                   CONSTRAINT la_library_fk FOREIGN KEY (library_id) REFERENCES libraries (library_id) ON DELETE CASCADE,
                                   CONSTRAINT la_action_fk  FOREIGN KEY (action_id)  REFERENCES actions (action_id)    ON DELETE CASCADE
);
​
CREATE TABLE libraries_compounds (
                                     library_id	INTEGER NOT NULL,
                                     compound_id INTEGER NOT NULL,
                                     CONSTRAINT la_library_fk  FOREIGN KEY (library_id)  REFERENCES libraries (library_id)   ON DELETE CASCADE,
                                     CONSTRAINT la_compound_fk FOREIGN KEY (compound_id) REFERENCES compounds (compound_id) ON DELETE CASCADE
);
​
​
CREATE TABLE tests_scenarios (
                                 scenario_id 			SERIAL PRIMARY KEY,
                                 scenario_name 			VARCHAR(32) NOT NULL,
                                 scenario_description 	TEXT
);
​
CREATE TABLE tests_scenarios_actions_compounds (
                                                   scenario_id		INTEGER NOT NULL,
                                                   action_id		INTEGER	NOT NULL,
                                                   compound_id		INTEGER	NOT NULL,
                                                   order_priority	INTEGER NOT NULL,
                                                   CONSTRAINT tsac_scenario_fk FOREIGN KEY (scenario_id) REFERENCES tests_scenarios (scenario_id) ON DELETE CASCADE,
                                                   CONSTRAINT tsac_action_fk   FOREIGN KEY (action_id)   REFERENCES actions         (action_id)   ON DELETE CASCADE,
                                                   CONSTRAINT tsac_compound_fk FOREIGN KEY (compound_id) REFERENCES compounds 		 (compound_id) ON DELETE CASCADE
);
​
CREATE TABLE data_sets (
                           data_set_id 		 SERIAL PRIMARY KEY,
                           data_set_name 		 VARCHAR(32) NOT NULL,
                           data_set_description TEXT
);
​
CREATE TABLE parameters (
                            parameter_id			SERIAL PRIMARY KEY,
                            data_set_id				INTEGER NOT NULL,
                            parametr_name			VARCHAR(32) NOT NULL,
                            parametr_number_value	INTEGER,
                            parametr_string_value	VARCHAR(32),
                            parametr_date_value		TIMESTAMP,
                            CONSTRAINT parametr_data_fk FOREIGN KEY (data_set_id) REFERENCES data_sets (data_set_id) ON DELETE CASCADE
);
​
CREATE TABLE projects (
                          project_id 				SERIAL PRIMARY KEY,
                          creator_id				INTEGER 		   NOT NULL,
                          project_name 			VARCHAR(32) UNIQUE NOT NULL,
                          project_website_link	VARCHAR(64) UNIQUE NOT NULL,
                          project_active			BOOLEAN 		   NOT NULL,
                          CONSTRAINT project_creator_fk  FOREIGN KEY (creator_id)  REFERENCES users (user_id) ON DELETE CASCADE
);
​
CREATE TABLE statutes (
                          status_id	SERIAL PRIMARY KEY,
                          status_name	VARCHAR(32)	UNIQUE NOT NULL
);
​
CREATE TABLE test_cases (
                            test_case_id 	 SERIAL PRIMARY KEY,
                            project_id 		 INTEGER NOT NULL,
                            author_id 		 INTEGER NOT NULL,
                            scenario_id 	 INTEGER NOT NULL,
                            data_set_id 	 INTEGER NOT NULL,
                            CONSTRAINT tc_project_fk FOREIGN KEY (project_id)       REFERENCES projects (project_id)         ON DELETE CASCADE,
                            CONSTRAINT tc_author_fk  FOREIGN KEY (author_id)        REFERENCES users (user_id)               ON DELETE CASCADE,
                            CONSTRAINT tc_ts_fk 	 FOREIGN KEY (scenario_id) 		REFERENCES tests_scenarios (scenario_id) ON DELETE CASCADE,
                            CONSTRAINT tc_ds_fk 	 FOREIGN KEY (data_set_id)      REFERENCES data_sets (data_set_id)       ON DELETE CASCADE
);
​
CREATE TABLE test_cases_watchers(
                                    test_case_id	INTEGER NOT NULL,
                                    user_id			INTEGER NOT NULL,
                                    CONSTRAINT tcw_testcase_fk	FOREIGN KEY (test_case_id)	REFERENCES test_cases (test_case_id),
                                    CONSTRAINT tcw_watcher_fk   FOREIGN KEY (user_id) 		REFERENCES users (user_id)
);
​
CREATE TABLE result_tests (
                              result_id 					SERIAL PRIMARY KEY,
                              test_case_id 				INTEGER   NOT NULL,
                              started_by_user 			INTEGER   NOT NULL,
                              status_id 					INTEGER   NOT NULL,
                              result_test_start_date 		TIMESTAMP NOT NULL,
                              result_test_end_date 		TIMESTAMP NOT NULL,
                              CONSTRAINT rt_tc_fk 	FOREIGN KEY (test_case_id) 	  REFERENCES test_cases (test_case_id) ON DELETE CASCADE,
                              CONSTRAINT rt_su_fk 	FOREIGN KEY (started_by_user) REFERENCES users (user_id) 		   ON DELETE CASCADE,
                              CONSTRAINT rt_status_fk FOREIGN KEY (status_id) 	  REFERENCES statutes (status_id) 	   ON DELETE CASCADE
);
