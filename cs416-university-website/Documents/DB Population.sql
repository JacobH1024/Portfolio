INSERT INTO Semesters
VALUES
  (1, 'Fall', 2014),
  (2, 'Spring', 2015),
  (3, 'Summer', 2015),
  (4, 'Fall', 2015),
  (5, 'Spring', 2016),
  (6, 'Summer', 2016),
  (7, 'Fall', 2016),
  (8, 'Spring', 2017),
  (9, 'Summer', 2017),
  (10, 'Fall', 2017);

INSERT INTO Professors
VALUES
  (1, 'Catherine', 'Murphy', 'cmurphy', 'pass', TRUE, 200000, 'cmurphy@aau.edu', '111-111-1111 ext 1'),
  (2, 'Hairong', 'Zhao', 'hzhao', 'pass', TRUE, 100000, 'hzhao@aau.edu', '111-111-1111 ext 2'),
  (3, 'Roger', 'Kraft', 'rkraft', 'pass', TRUE, 100000, 'rkraft@aau.edu', '111-111-1111 ext 3'),
  (4, 'Shuhui', 'Yang', 'syang', 'pass', TRUE, 100000, 'syang@aau.edu', '111-111-1111 ext 4'),
  (5, 'Ruijian', 'Zhang', 'rzhang', 'pass', TRUE, 100000, 'rzhang@aau.edu', '111-111-1111 ext 5'),
  (6, 'Raida', 'Abuizam', 'rabuizam', 'pass', TRUE, 200000, 'rabuizam@aau.edu', '111-111-1111 ext 6'),
  (7, 'Kuan-Chou', 'Chen', 'kchen', 'pass', TRUE, 100000, 'kchen@aau.edu', '111-111-1111 ext 7'),
  (8, 'Lori', 'Feldman', 'lfeldman', 'pass', TRUE, 100000, 'lfeldman@aau.edu', '111-111-1111 ext 8'),
  (9, 'Karen', 'Bishop-Morris', 'kbmorris', 'pass', TRUE, 200000, 'kbmorris@aau.edu', '111-111-1111 ext 9'),
  (10, 'Jane', 'Rose', 'jrose', 'pass', TRUE, 100000, 'jrose@aau.edu', '111-111-1111 ext 10');

INSERT INTO Departments
VALUES
  (1, 'Computer Science'),
  (2, 'Business'),
  (3, 'English'),
  (4, 'General Studies');

INSERT INTO Professor_Employment
VALUES
  (1, 1, TRUE),
  (1, 2, FALSE),
  (1, 3, FALSE),
  (1, 4, FALSE),
  (1, 5, FALSE),
  (2, 6, TRUE),
  (2, 7, FALSE),
  (2, 8, FALSE),
  (3, 9, TRUE),
  (3, 10, FALSE);

INSERT INTO Degrees
VALUES
  (1, 'Computer Science BS', 1),
  (2, 'Computer Science Masters', 1),
  (3, 'Business BS', 2),
  (4, 'Business MBA', 2),
  (5, 'English BA', 3),
  (6, 'English Masters', 3),
  (7, 'Computer Science Minor', 1),
  (8, 'Business Minor', 2),
  (9, 'English Minor', 3),
  (10, 'Undeclared', 4);

INSERT INTO Students
VALUES
  (1, 'John', 'Smith', 'jsmith', 'pass', TRUE, FALSE, 1, 9),
  (2, 'Jane', 'Doe', 'jdoe', 'pass', TRUE, FALSE, 1, 8),
  (3, 'Steve', 'Jobs', 'sjobs', 'pass', TRUE, FALSE, 1, 8),
  (4, 'Jose', 'Cruz', 'jcruz', 'pass', FALSE, TRUE, 1, NULL),
  (5, 'Sharon', 'Osborn', 'sosborn', 'pass', TRUE, FALSE, 1, 9),
  (6, 'Michael', 'Tabor', 'mtabor', 'pass', TRUE, TRUE, 1, NULL),
  (7, 'Melissa', 'Bone', 'mbone', 'pass', TRUE, FALSE, 2, 7),
  (8, 'Lisa', 'Simpson', 'lsimpson', 'pass', TRUE, TRUE, 2, NULL),
  (9, 'Jack', 'Smith', 'jsmith2', 'pass', TRUE, FALSE, 2, NULL),
  (10, 'Harold', 'Jackson', 'hjackson', 'pass', TRUE, FALSE, 2, 9),
  (11, 'Lenny', 'Jetson', 'ljetson', 'pass', TRUE, TRUE, 2, NULL),
  (12, 'Lily', 'Allen', 'lallen', 'pass', FALSE, FALSE, 2, 7),
  (13, 'Jacob', 'Hader', 'jhader', 'pass', TRUE, FALSE, 3, NULL),
  (14, 'Betty', 'White', 'bwhite', 'pass', TRUE, TRUE, 3, NULL),
  (15, 'Bernie', 'Sanders', 'bsanders', 'pass', FALSE, TRUE, 3, 8);

INSERT INTO Course_Categories
VALUES
  (1, 'Computer Science'),
  (2, 'Business Management'),
  (3, 'English');

INSERT INTO Courses
VALUES
  (123, 'Java', 'An intro to programming using the java language', 3, 1),
  (124, 'C++', 'An intro to programming using the C++ language', 3, 1),
  (275, 'Data Structures', 'An introduction to using data structures in programming. Uses the Java Language', 3, 1),
  (302, 'Operating Systems', 'A course on Operating Systems (Windows and Linux), using the C language', 3, 1),
  (416, 'Software Engineering', 'A study of the full software development process', 3, 1),
  (442, 'Database Systems', 'Everything database related that you needed to know', 3, 1),
  (101, 'Intro to Business', 'An intro to the business world', 3, 2),
  (225, 'Fund Managerial Stat', 'The base foundation of managerial strategies', 3, 2),
  (411, 'Entrepreneurship', 'How to be an entrepreneur', 3, 2),
  (495, 'Intern In Management', 'Internship opportunities in the real world', 3, 2),
  (104, 'English Composition I', 'English Basics', 3, 3),
  (105, 'English Composition II', 'Advanced English', 3, 3),
  (205, 'Intro Creative Writing', 'Learn how to write short stories in a creative approach', 3, 3),
  (360, 'Gender And Literature', 'Gender and how it applies to the past, present, and future of literature', 3, 3);

INSERT INTO Courses_Offered
VALUES
  (1, 1, 2, 123, 'M/W', '10:00:00'),
  (2, 1, 3, 275, 'M/W', '11:30:00'),
  (3, 1, 5, 416, 'T/TR', '11:30:00'),
  (4, 1, 8, 101, 'M/W', '16:00:00'),
  (5, 1, 7, 411, 'T/TR', '13:00:00'),
  (6, 1, 9, 104, 'T/TR', '14:30:00'),
  (7, 1, 10, 205, 'M/W', '18:00:00'),
  (8, 2, 4, 124, 'M/W', '10:00:00'),
  (9, 2, 3, 302, 'M/W', '11:30:00'),
  (10, 2, 5, 442, 'T/TR', '11:30:00'),
  (11, 2, 8, 225, 'M/W', '16:00:00'),
  (12, 2, 7, 495, 'T/TR', '13:00:00'),
  (13, 2, 9, 105, 'T/TR', '14:30:00'),
  (14, 2, 10, 360, 'M/W', '18:00:00'),
  (15, 3, 2, 123, 'M/W', '16:00:00'),
  (16, 3, 4, 123, 'T/TR', '13:00:00'),
  (17, 3, 6, 495, 'T/TR', '14:30:00'),
  (18, 3, 9, 205, 'M/W', '18:00:00'),
  (19, 4, 2, 123, 'M/W', '10:00:00'),
  (20, 4, 3, 275, 'M/W', '11:30:00'),
  (21, 4, 5, 416, 'T/TR', '11:30:00'),
  (22, 4, 8, 101, 'M/W', '16:00:00'),
  (23, 4, 7, 411, 'T/TR', '13:00:00'),
  (24, 4, 9, 104, 'T/TR', '14:30:00'),
  (25, 4, 10, 205, 'M/W', '18:00:00'),
  (26, 5, 4, 124, 'M/W', '10:00:00'),
  (27, 5, 3, 302, 'M/W', '11:30:00'),
  (28, 5, 5, 442, 'T/TR', '11:30:00'),
  (29, 5, 8, 225, 'M/W', '16:00:00'),
  (30, 5, 7, 495, 'T/TR', '13:00:00'),
  (31, 5, 9, 105, 'T/TR', '14:30:00'),
  (32, 5, 10, 360, 'M/W', '18:00:00'),
  (33, 6, 2, 123, 'M/W', '16:00:00'),
  (34, 6, 4, 123, 'T/TR', '13:00:00'),
  (35, 6, 6, 495, 'T/TR', '14:30:00'),
  (36, 6, 9, 205, 'M/W', '18:00:00'),
  (38, 7, 2, 123, 'M/W', '10:00:00'),
  (39, 7, 3, 275, 'M/W', '11:30:00'),
  (40, 7, 5, 416, 'T/TR', '11:30:00'),
  (41, 7, 8, 101, 'M/W', '16:00:00'),
  (42, 7, 7, 411, 'T/TR', '13:00:00'),
  (43, 7, 9, 104, 'T/TR', '14:30:00'),
  (44, 7, 10, 205, 'M/W', '18:00:00'),
  (45, 8, 4, 124, 'M/W', '10:00:00'),
  (46, 8, 3, 302, 'M/W', '11:30:00'),
  (47, 8, 5, 442, 'T/TR', '11:30:00'),
  (48, 8, 8, 225, 'M/W', '16:00:00'),
  (49, 8, 7, 495, 'T/TR', '13:00:00'),
  (50, 8, 9, 105, 'T/TR', '14:30:00'),
  (51, 8, 10, 360, 'M/W', '18:00:00'),
  (52, 9, 2, 123, 'M/W', '16:00:00'),
  (53, 9, 4, 123, 'T/TR', '13:00:00'),
  (54, 9, 6, 495, 'T/TR', '14:30:00'),
  (55, 9, 9, 205, 'M/W', '18:00:00'),
  (56, 10, 2, 123, 'M/W', '10:00:00'),
  (57, 10, 3, 275, 'M/W', '11:30:00'),
  (58, 10, 5, 416, 'T/TR', '11:30:00'),
  (59, 10, 8, 101, 'M/W', '16:00:00'),
  (60, 10, 7, 411, 'T/TR', '13:00:00'),
  (61, 10, 9, 104, 'T/TR', '14:30:00'),
  (62, 10, 10, 205, 'M/W', '18:00:00');

INSERT INTO Events
VALUES
  (1, 'Bill Nye the Science Guy Lecture', 'Meet and greet Bill Nye as he answers questions about science and promotes action against climate change. In main lecture hall.', '2017-4-29 12:30:00', '2017-4-29 16:00:00', 1, 1, 30, 10),
  (2, 'Mayor Adam West Speech', 'Adam West talks about his life as literally the coolest guy ever. You will never be him.', '2017-5-03 15:30:00', '2017-5-03 17:00:00', 1, 1, 30, 10),
  (3, 'Mark Wahlberg Speech', 'Mark Wahlberg speeks at commencement ceremony in main lecture hall.', '2017-5-05 12:30:00', '2017-5-05 16:00:00', 1, 1, 30, 10),
  (4, 'Morgan Freeman Book Reading', 'Morgan Freeman soothes the soul with a powerful yet relaxing reading of Go The F*ck To Sleep in the main lecture hall.', '2017-5-06 18:30:00', '2017-5-06 19:00:00', 1, 1, 30, 10),
  (5, 'AAU President Speech', 'During finals week, the AAU President will give a speech to empower students to achieve their goals in the main lecture hall.', '2017-5-14 13:30:00', '2017-5-14 16:00:00', 1, 1, 30, 10),
  (6, 'Veteran Performance on Front Lawn', 'Local veterans will be performing a special rendition of Cats: The Musical on the front lawn to honor fallen soldiers on Memorial Day.', '2017-5-29 10:00:00', '2017-5-29 13:00:00', 1, 1, 30, 10);

INSERT INTO Student_Event_Registrations
VALUES
  (1, 1);

INSERT INTO Professor_Event_Registrations
VALUES
  (1, 1);

INSERT INTO Faculty_Announcements (announcement_id, subject, message, date, professor_id, author)
VALUES
  (1, 'New CS courses',
   'Hello, this announcement is directed towards all Computer Science faculty members.<br>In your emails, you should have received a form to request additional CS courses for the next semester. Please respond to it promptly.',
   '2017-04-21', 1, 'Catherine Murphy'),
  (2, 'Business majors internship opportunity',
   'Dear all Business faculty members,<br>please let all Business major students know of this internship opportunity with Above Average Marketing Consultants. This marketing firm is an extended branch of our university Business department with whom we work closely with to provide research opportunities for our students.',
   '2017-04-21', 6, 'Raida Abuizam'),
  (3, 'Writing center openings',
   'There are job opportunities in the Writing Center for students majoring in English. Please make sure your students are aware of this opportunity promptly because the deadline for applying is May 5th, the end of this semester.',
   '2017-04-22', 9, 'Karen Bishop-Morris');
