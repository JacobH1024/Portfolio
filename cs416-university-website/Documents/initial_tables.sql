CREATE TABLE Semesters
(
  semester_id INTEGER     NOT NULL AUTO_INCREMENT,
  term        VARCHAR(10) NOT NULL,
  year        INTEGER     NOT NULL,
  PRIMARY KEY (semester_id)
);

CREATE TABLE Professors
(
  professor_id  INTEGER      NOT NULL,
  first_name    VARCHAR(50)  NOT NULL,
  last_name     VARCHAR(50)  NOT NULL,
  username      VARCHAR(10)  NOT NULL,
  password      VARCHAR(100) NOT NULL,
  active        BOOLEAN      NOT NULL,
  salary        INTEGER      NOT NULL,
  email_address VARCHAR(100) NOT NULL,
  phone_number  VARCHAR(25)  NOT NULL,
  PRIMARY KEY (professor_id)
);

CREATE TABLE Departments
(
  department_id INTEGER     NOT NULL AUTO_INCREMENT,
  name          VARCHAR(50) NOT NULL,
  PRIMARY KEY (department_id)
);

CREATE TABLE Events
(
  event_id         INTEGER      NOT NULL AUTO_INCREMENT,
  title            VARCHAR(50)  NOT NULL,
  description      VARCHAR(300) NOT NULL,
  begin_date       DATETIME     NOT NULL,
  end_date         DATETIME     NOT NULL,
  department_id    INTEGER      NOT NULL,
  creator_id       INTEGER      NOT NULL,
  student_capacity INTEGER      NOT NULL,
  prof_capacity    INTEGER      NOT NULL,
  PRIMARY KEY (event_id),
  FOREIGN KEY (creator_id) REFERENCES Professors (professor_id)
    ON DELETE CASCADE
);

CREATE TABLE Professor_Employment
(
  department_id      INTEGER NOT NULL,
  professor_id       INTEGER NOT NULL,
  is_department_head BOOLEAN NOT NULL,
  FOREIGN KEY (department_id) REFERENCES Departments (department_id)
    ON DELETE CASCADE,
  FOREIGN KEY (professor_id) REFERENCES Professors (professor_id)
    ON DELETE CASCADE,
  UNIQUE KEY prof_department (department_id, professor_id)
);

CREATE TABLE Degrees
(
  degree_id     INTEGER     NOT NULL AUTO_INCREMENT,
  name          VARCHAR(50) NOT NULL,
  department_id INTEGER     NOT NULL,
  PRIMARY KEY (degree_id),
  FOREIGN KEY (department_id) REFERENCES Departments (department_id)
    ON DELETE CASCADE
);

CREATE TABLE Students
(
  student_id INTEGER      NOT NULL,
  first_name VARCHAR(50)  NOT NULL,
  last_name  VARCHAR(50)  NOT NULL,
  username   VARCHAR(10)  NOT NULL,
  password   VARCHAR(100) NOT NULL,
  in_state   BOOLEAN      NOT NULL,
  graduate   BOOLEAN      NOT NULL,
  major      INTEGER,
  minor      INTEGER,
  PRIMARY KEY (student_id),
  FOREIGN KEY (major) REFERENCES Degrees (degree_id)
    ON DELETE CASCADE,
  FOREIGN KEY (minor) REFERENCES Degrees (degree_id)
    ON DELETE CASCADE
);

CREATE TABLE Professor_Event_Registrations
(
  event_id     INTEGER NOT NULL,
  professor_id INTEGER NOT NULL,
  PRIMARY KEY (event_id, professor_id),
  FOREIGN KEY (event_id) REFERENCES Events (event_id),
  FOREIGN KEY (professor_id) REFERENCES Professors (professor_id)
);

CREATE TABLE Student_Event_Registrations
(
  event_id   INTEGER NOT NULL,
  student_id INTEGER NOT NULL,
  PRIMARY KEY (event_id, student_id),
  FOREIGN KEY (event_id) REFERENCES Events (event_id),
  FOREIGN KEY (student_id) REFERENCES Students (student_id)
);

CREATE TABLE Course_Categories
(
  course_category_id INTEGER     NOT NULL AUTO_INCREMENT,
  name               VARCHAR(50) NOT NULL,
  PRIMARY KEY (course_category_id)
);

CREATE TABLE Courses
(
  course_id    INTEGER      NOT NULL AUTO_INCREMENT,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL,
  credit_hours INTEGER      NOT NULL,
  category_id  INTEGER      NOT NULL,
  PRIMARY KEY (course_id),
  FOREIGN KEY (category_id) REFERENCES Course_Categories (course_category_id)
);

CREATE TABLE Courses_Offered
(
  offered_id   INTEGER    NOT NULL AUTO_INCREMENT,
  semester_id  INTEGER    NOT NULL,
  professor_id INTEGER    NOT NULL,
  course_id    INTEGER    NOT NULL,
  days         VARCHAR(5) NOT NULL,
  time         TIME       NOT NULL,
  PRIMARY KEY (offered_id),
  FOREIGN KEY (semester_id) REFERENCES Semesters (semester_id)
    ON DELETE CASCADE,
  FOREIGN KEY (professor_id) REFERENCES Professors (professor_id)
    ON DELETE CASCADE,
  FOREIGN KEY (course_id) REFERENCES Courses (course_id)
    ON DELETE CASCADE
);

CREATE TABLE Course_Files
(
  file_id   INTEGER       NOT NULL AUTO_INCREMENT,
  offered_course_id INTEGER       NOT NULL,
  file      MEDIUMBLOB    NOT NULL,
  file_name VARCHAR(100)  NOT NULL,
  PRIMARY KEY (file_id),
  FOREIGN KEY (offered_course_id) REFERENCES Courses_Offered (offered_id)
    ON DELETE CASCADE
);

CREATE TABLE Assignments
(
  assignment_id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  points_possible INTEGER NOT NULL,
  offered_course_id INTEGER NOT NULL,
  PRIMARY KEY (assignment_id),
  FOREIGN KEY (offered_course_id) REFERENCES Courses_Offered (offered_id)
    ON DELETE CASCADE
);

CREATE TABLE Grades
(
  grade_id      INTEGER    NOT NULL AUTO_INCREMENT,
  student_id    INTEGER    NOT NULL,
  assignment_id INTEGER    NOT NULL,
  points        INTEGER    NOT NULL,
  offered_course_id     INTEGER    NOT NULL,
  PRIMARY KEY (grade_id),
  FOREIGN KEY (student_id) REFERENCES Students (student_id)
    ON DELETE CASCADE,
  FOREIGN KEY (assignment_id) REFERENCES Assignments (assignment_id)
    ON DELETE CASCADE,
  FOREIGN KEY (offered_course_id) REFERENCES Courses_Offered (offered_id)
    ON DELETE CASCADE
);

CREATE TABLE Course_Homepage
(
  offered_course_id INTEGER NOT NULL,
  text      TEXT,
  PRIMARY KEY (offered_course_id),
  FOREIGN KEY (offered_course_id) REFERENCES Courses_Offered (offered_id)
    ON DELETE CASCADE
);



CREATE TABLE Student_Enrollment
(
  enrollment_id INTEGER                                                 NOT NULL AUTO_INCREMENT,
  student_id    INTEGER                                                 NOT NULL,
  offered_id    INTEGER                                                 NOT NULL,
  status        ENUM ('passed', 'incomplete', 'in-progress', 'dropped') NOT NULL,
  PRIMARY KEY (enrollment_id),
  FOREIGN KEY (student_id) REFERENCES Students (student_id)
    ON DELETE CASCADE,
  FOREIGN KEY (offered_id) REFERENCES Courses_Offered (offered_id)
    ON DELETE CASCADE
);

CREATE TABLE Faculty_Announcements
(
  announcement_id INTEGER     NOT NULL AUTO_INCREMENT,
  subject         VARCHAR(50) NOT NULL,
  message         TEXT        NOT NULL,
  date            DATE        NOT NULL,
  author          VARCHAR(50) NOT NULL,
  professor_id    INTEGER     NOT NULL,
  PRIMARY KEY (announcement_id),
  FOREIGN KEY (professor_id) REFERENCES Professors (professor_id)
    ON DELETE CASCADE
);
