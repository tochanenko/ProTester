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
		 WHERE role_name = 'ADMIN'), 'adrenaline', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'j.statham@mail.com', TRUE, 'Jason', 'Statham'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ADMIN'), 'neo', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'neo@mail.com', TRUE, 'Thomas', 'Anderson'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ADMIN'), 'hal9000', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'hal9000@gmail.com', TRUE, 'HAL', '9000'),
       ((SELECT role_id
         FROM roles
         WHERE role_name = 'ADMIN'), 'niqz', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'niqz99@gmail.com', TRUE, 'Vadim', 'Dudka'),
       ((SELECT role_id
         FROM roles
         WHERE role_name = 'ADMIN'), 'Egich', '$2a$10$0Q6Ar04XcXNLiXR/OAnxT.n13QLNYi3W0vtTg.tPmiXG1iwhdayHe', 'raidervolt@gmail.com', TRUE, 'Egor', 'Shulha'),
       ((SELECT role_id
         FROM roles
         WHERE role_name = 'ADMIN'), 'vovannna', '$2a$10$eB3DxevCQojYpyOMlcfnf.ZqSUXl/2RrDgMS7YA2Jh3no9UMpCHpy', 'vovan.zhuk3.14@gmail.com', TRUE, 'Volodymyr', 'Zhuk'),
       ((SELECT role_id
         FROM roles
         WHERE role_name = 'ADMIN'), 'herman', '$2a$10$cJgpEWLNb.AZKIO15wfuCepf8qLlvGY3DU8eNk88X69F2HHEwg6nO', 'hermansmoliar23@gmail.com', TRUE, 'Herman', 'Smoliar'),
       ((SELECT role_id
         FROM roles
         WHERE role_name = 'ADMIN'), 'tochik', '$2a$10$13QDEfY2pgF.0aYcOxJ.7usUbchOn6Sa3MLTqsWsMneK4t9JcSuHi', 'itochanenko@gmail.com', TRUE, 'Vladislav', 'Tochanenko'),
       ((SELECT role_id
         FROM roles
         WHERE role_name = 'ADMIN'), 'juliaBorovets', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'juliaborovets2001@gmail.com', TRUE, 'Julia', 'Borovets'),
       ((SELECT role_id
         FROM roles
         WHERE role_name = 'MANAGER'), 'wolf', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'j.belfort@mail.com', TRUE, 'Jordan', 'Belfort'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'IsaacClarke', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'i.clarke@mail.com', FALSE, 'Isaac', 'Clarke'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'DocBrown', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'doc.brown@mail.com', TRUE, 'Emmett', 'Brown'),
	   ((SELECT role_id
		 FROM roles
		 WHERE role_name = 'ENGINEER'), 'shurik', '$2a$10$OVH2hTUh1EC8rZwp1I0b2utSptMWwWOHS.gBcW.cLG8VtnRs69CBu', 'shurik@mail.com', TRUE, 'Alexander', 'Timofeev');
