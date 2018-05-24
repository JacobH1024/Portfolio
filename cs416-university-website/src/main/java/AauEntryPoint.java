import models.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.sql2o.Sql2o;
import repos.*;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class AauEntryPoint {

    private static Visitor visitor = new Visitor();
    private static DateTimeFormatter eventFormatter = DateTimeFormatter.ofPattern("M/d - hh:mm a");

    public static void main(String args[]) {
        // Set these environment variables to connect to your database
        String jdbcURL = System.getenv("JDBC-URL");
        String jdbcUser = System.getenv("JDBC-USER");
        String jdbcPass = System.getenv("JDBC-PASS");

        if (jdbcURL == null || jdbcURL.length() == 0) {
            jdbcURL = "jdbc:mysql://localhost:3306/test";
        }

        JSONParser parser = new JSONParser();
        Sql2o sql2o = new Sql2o(jdbcURL, jdbcUser, jdbcPass);
        UserRepository userRepo = new UserRepository(sql2o, visitor);
        FileRepository fileRepo = new FileRepository(sql2o);
        EventRepository eventRepo = new EventRepository(sql2o);
        GradeRepository gradeRepo = new GradeRepository(sql2o);
        CourseRepository courseRepo = new CourseRepository(sql2o);
        StudentRepository studentRepo = new StudentRepository(sql2o);
        AssignmentRepository assRepo = new AssignmentRepository(sql2o); // lol..assRepo
        SemesterRepository semesterRepo = new SemesterRepository(sql2o);
        ProfessorRepository professorRepo = new ProfessorRepository(sql2o);
        DepartmentRepository departmentRepo = new DepartmentRepository(sql2o);
        EnrolledCourseRepository enrolledRepo = new EnrolledCourseRepository(sql2o);

        AnnouncementRepository announcementRepo = new AnnouncementRepository(sql2o);


        // So Spark knows which folder (starting from "/resources") our static files are located in
        staticFileLocation("/public");

        // Sets the login information on each page load
        before((req, res) -> {
            String username = req.session().attribute("username");
            User userData = userRepo.sessionLogin(username);
            req.session().attribute("userData", userData);
        });

        /*
            Routing

            Each method call here is organized by the type of page it intends to render.
            For each page, different data is retrieved from the database if necessary and
            a main template file is loaded with the proper model variables using a hash map.
         */

        // API group - organizes all URLs for the API
        path("/api", () -> {
            // Course search API
            get("/courses", (req, res) -> {
                String semester = req.queryParams("semester");
                String[] categories = req.queryParamsValues("categories");
                String name = req.queryParams("name");
                List<Course> courses = courseRepo.courseSearch(semester, categories, name);

                JSONObject result = new JSONObject();
                JSONArray list = new JSONArray();
                for (Course course : courses) {
                    JSONObject obj = new JSONObject();
                    List<Professor> prof = professorRepo.getProfByID(course.getProfessorID());
                    obj.put("id", course.getCourseID());
                    obj.put("name", course.getName());
                    obj.put("description", course.getDescription());
                    obj.put("creditHours", course.getCreditHours());
                    obj.put("days", course.getDays());
                    obj.put("time", course.getTime());
                    obj.put("offeredID", course.getOfferedID());
                    obj.put("profID", prof.get(0).getFullName());
                    list.add(obj);
                }
                result.put("data", list);

                return result.toJSONString();
            });

            post("/upload", (req, res) -> {
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
                req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                Part uploadFile = req.raw().getPart("file");
                String courseID = req.queryParams("offeredID");
                String fileName = req.raw().getPart("file").getSubmittedFileName();

                try (InputStream is = uploadFile.getInputStream()) {
                    fileRepo.uploadFile(courseID, is, fileName);
                }

                return "API not yet implemented";
            });

            // Download file API
            get("/download", (req, res) -> {
                String fileID = req.queryParams("fileID");
                byte[] fileBuffer = fileRepo.download(fileID);
                String fileName = fileRepo.getFileName(fileID);
                res.raw().addHeader("Content-Disposition", "attachment; filename=" + fileName);
                OutputStream out = res.raw().getOutputStream();
                out.write(fileBuffer);
                return "";
            });

            // File removal API
            post("/delete", (req, res) -> {
                int fileID = Integer.parseInt(req.queryParams("fileID"));
                fileRepo.deleteFile(fileID);
                return "";
            });

            // Register course API
            post("/courses/register", (req, res) -> {
                User userData = req.session().attribute("userData");
                String studentID = Integer.toString(userData.getUserID());
                String offeredID = req.queryParams("offeredID");
                courseRepo.registerCourse(offeredID, studentID);
                List<Assignment> ass = assRepo.getCoursesAss(offeredID);
                if (ass != null) {
                    for (Assignment assignment : ass) {
                        gradeRepo.insertNewAssignment(Integer.parseInt(studentID), assignment.getAssID(), 0,
                                Integer.parseInt(offeredID));
                    }
                }
                JSONObject result = new JSONObject();
                JSONArray list = new JSONArray();
                List<Course> courseName = courseRepo.getCourseNameByID(offeredID);
                for (Course course: courseName) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", course.getName());
                    obj.put("offID", course.getOfferedID());
                    list.add(obj);
                }
                result.put("data", list);
                return result.toJSONString();
            });

            // Send grade list of student for Professor view
            get("/grade/list", (req, res) -> {
                int userID = Integer.parseInt(req.queryParams("userID"));
                String offeredID = req.queryParams("offeredID");
                List<Grade> gradeList = gradeRepo.getStudentsGrades(userID, offeredID);
                JSONObject result = new JSONObject();
                JSONArray list = new JSONArray();

                for (Grade grade : gradeList) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", grade.getAssignmentName());
                    obj.put("assID", grade.getAssignmentID());
                    obj.put("points", grade.getPoints());
                    obj.put("possible", grade.getPointsPossible());
                    obj.put("userID", userID);
                    list.add(obj);
                }
                result.put("data", list);
                return result.toJSONString();
            });

            // Update grade for student
            post("/grade/update", (req, res) -> {
                int newScore = Integer.parseInt(req.queryParams("newScore"));
                int assignmentID = Integer.parseInt(req.queryParams("assignID"));
                int userID = Integer.parseInt(req.queryParams("userID"));
                gradeRepo.updateAssignmentGrade(userID, assignmentID, newScore);
                return "";
            });

            post("/assignment/delete", (req, res) -> {
                int assID = Integer.parseInt(req.queryParams("assID"));
                assRepo.deleteAssignment(assID);
                return "";
            });

            post("/assignment/add", (req, res) -> {
                String name = req.queryParams("name");
                int pointPossible = Integer.parseInt(req.queryParams("pointsPossible"));
                int offeredID = Integer.parseInt(req.queryParams("offeredID"));
                int assignmentID = assRepo.addAssignment(name, pointPossible, offeredID);

                // After an assignment is added, grades should be set to 0 by default
                List<Student> studentList = studentRepo.getStudentsInCourse(Integer.toString(offeredID));
                for (Student student : studentList) {
                    gradeRepo.insertNewAssignment(student.getUserID(), assignmentID, 0, offeredID);
                }
                return "";
            });

            post("/homepage/update", (req, res) -> {
                // TODO: Send enter spaces
                String text = req.queryParams("text");
                String offeredID = req.queryParams("offeredID");
                courseRepo.updateHomePageText(offeredID, text);
                return "";
            });

            // Events API
            get("/events", (req, res) -> {
                int year = Integer.parseInt(req.queryParams("year"));
                int month = Integer.parseInt(req.queryParams("month"));
                int day = Integer.parseInt(req.queryParams("day"));
                LocalDate date = LocalDate.of(year, month, day);
                List<Event> eventList = eventRepo.getEventsWeekAfterDate(date);

                return eventListToJSON(eventList);
            });

            // Event registration API
            get("/events/register", (req, res) -> {
                User userData = req.session().attribute("userData");
                int eventID = Integer.parseInt(req.queryParams("eventID"));

                if (!userData.isLoggedIn()) {
                    return false;
                }

                if (userData.isStudent()) {
                    return eventRepo.registerForEventStudent(userData.getUserID(), eventID);
                } else {
                    return eventRepo.registerForEventProfessor(userData.getUserID(), eventID);
                }
            });

            // Events-by-department API
            get("/department/:id/events", (req, res) -> {
                int departmentID = Integer.parseInt(req.queryParams("departmentID"));
                List<Event> eventList = eventRepo.getEventsByDepartmentID(departmentID);

                return eventListToJSON(eventList);
            });

            // Edit department information API
            post("/department/:id", (req, res) -> {
                // TODO: Implement department editing JSON API
                return "API not yet implemented";
            });

            // New Event API
            post("/events/new", (req, res) -> {
                User userData = req.session().attribute("userData");
                if (userData == visitor || !userData.isDepartmentHead()) {
                    halt(401);
                    return "Not authorized";
                }

                JSONObject newEventJSON = (JSONObject) parser.parse(req.body());
                String title = (String) newEventJSON.get("title");
                String description = (String) newEventJSON.get("description");
                Timestamp beginDate = Timestamp.valueOf((String) newEventJSON.get("beginDate"));
                Timestamp endDate = Timestamp.valueOf((String) newEventJSON.get("endDate"));
                int creatorID = userData.getUserID();
                int departmentID = departmentRepo.getIdForDepartmentHead(creatorID);
                int studentCapacity = (int) newEventJSON.get("studentCapacity");
                int professorCapacity = (int) newEventJSON.get("professorCapacity");

                Event newEvent = new Event();
                newEvent.setTitle(title);
                newEvent.setDescription(description);
                newEvent.setBeginDate(beginDate);
                newEvent.setEndDate(endDate);
                newEvent.setDepartmentID(departmentID);
                newEvent.setStudentCapacity(studentCapacity);
                newEvent.setProfessorCapacity(professorCapacity);

                return eventRepo.addNewEvent(newEvent, creatorID);
            });

            // Directory search API
            get("/directory", (req, res) -> {
                String department = req.queryParams("department");
                String searchText = req.queryParams("searchText");
                List<Department> departments = departmentRepo.getAllDepartments();
                List<Professor> professors;

                if (!department.equals("-1")) {
                    String deptName = departments.get(Integer.parseInt(department) - 1).getName();
                    professors = professorRepo.getProfByDepartmentName(deptName);
                } else {
                    professors = professorRepo.getAllProfessors();
                }

                JSONObject result = new JSONObject();
                JSONArray list = new JSONArray();

                for (Professor professor : professors) {
                    if (professor.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                            professor.getLastName().toLowerCase().contains(searchText.toLowerCase()) ||
                            professor.getUserName().toLowerCase().contains(searchText.toLowerCase())) {
                        JSONObject obj = new JSONObject();
                        obj.put("department", professor.getDepartment());
                        obj.put("emailAddress", professor.getEmailAddress());
                        obj.put("firstName", professor.getFirstName());
                        obj.put("lastName", professor.getLastName());
                        obj.put("isDepartmentHead", professor.isDepartmentHead());
                        obj.put("phoneNumber", professor.getPhoneNumber());
                        list.add(obj);
                    }
                }
                result.put("data", list);

                return result.toJSONString();
            });

            post("/announcements", (req, res) -> {
                String message = req.queryParams("message");
                String subject = req.queryParams("subject");
                String date = req.queryParams("date");
                String author = req.queryParams("author");
                int facultyID = Integer.parseInt(req.queryParams("id"));

                announcementRepo.postAnnouncement(message, subject, date, author, facultyID);

                return "";
            });

            get("/professor/:id/courses", (req, res) -> {
                int semesterSelection = Integer.parseInt(req.queryParams("selection"));
                int professorID = Integer.parseInt(req.params("id"));
                List<Course> courses = courseRepo.getCoursesByProfessorSemester(professorID, semesterSelection);

                JSONObject result = new JSONObject();
                JSONArray list = new JSONArray();

                for (Course course : courses) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", course.getCourseID());
                    obj.put("offID", course.getOfferedID());
                    obj.put("title", course.getName());
                    obj.put("schedule", course.getDays() + " " + course.getTime());
                    obj.put("creditHours", course.getCreditHours());
                    list.add(obj);
                }
                result.put("data", list);
                return result.toJSONString();
            });

            get("/student/:id/courses", (req, res) -> {
                int semesterSelection = Integer.parseInt(req.queryParams("selection"));
                String studentIDStr = req.params("id");
                int studentID = Integer.parseInt(studentIDStr);
                List<EnrolledCourse> enrolled = enrolledRepo.getAllEnrolledCoursesByStudentForSemester(studentID, semesterSelection);
                JSONObject result = new JSONObject();
                JSONArray list = new JSONArray();
                for (EnrolledCourse enrolledCourse : enrolled) {
                    JSONObject obj = new JSONObject();
                    String total = gradeRepo.getTotalAchieved(studentID, Integer.toString(enrolledCourse.getOfferedID()));
                    String possible = gradeRepo.getTotalPossible(Integer.toString(enrolledCourse.getOfferedID()));
                    DecimalFormat df = new DecimalFormat("#.##"); // Format GPA
                    obj.put("id", enrolledCourse.getCourseID());
                    obj.put("offID", enrolledCourse.getOfferedID());
                    obj.put("title", enrolledCourse.getCourseName());
                    obj.put("schedule", enrolledCourse.getDays() + "/" + enrolledCourse.getTime());
                    obj.put("creditHours", enrolledCourse.getCreditHours());
                    if (possible != null && total != null) {
                        double score = ((Double.parseDouble(total) / Double.parseDouble(possible)) * 100.0);
                        String letter = "";
                        if(score > 93){ letter = "A";}
                        if(score < 93 && score >= 90) { letter = "A-";}      if(score < 90 && score >= 87) { letter = "B+";}
                        if(score < 87 && score >= 83) { letter = "B";}       if(score < 83 && score >= 80) { letter = "B-";}
                        if(score < 80 && score >= 77) { letter = "C+";}      if(score < 77 && score >= 73) { letter = "C";}
                        if(score < 73 && score >= 70) { letter = "C-";}      if(score < 70 && score >= 67) { letter = "D+";}
                        if(score < 67 && score >= 63) { letter = "D";}       if(score < 63 && score >= 60) { letter = "D-";}
                        if(score < 60) { letter = "F";}
                        obj.put("courseGrade", letter);
                    }
                    obj.put("courseStatus", enrolledCourse.getStatus());
                    list.add(obj);
                }
                result.put("data", list);
                return result.toJSONString();
            });
        });

        // Home page
        get("/", (req, res) -> {
            String loginErrorParam = req.queryParams("loginError");
            String logoutParam = req.queryParams("logout");

            boolean loginError = (loginErrorParam != null) && loginErrorParam.equals("true");
            boolean logout = (logoutParam != null) && logoutParam.equals("true");

            Map<String, Object> model = new HashMap<>();
            model.put("userData", req.session().attribute("userData"));
            model.put("loginError", loginError);
            model.put("logout", logout);
            return new FreeMarkerEngine().render(
                    new ModelAndView(model, "homePage.ftl"));
        });

        // Course search page
        get("/courses", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<CourseCategory> categories = courseRepo.getAllCategories();
            List<Semester> semesters = semesterRepo.getAllSemesters();
            User userData = req.session().attribute("userData");
            if(userData.isStudent()){
                List<EnrolledCourse> taken = enrolledRepo.getAllEnrolledCoursesByStudent(userData.getUserID());
                model.put("taken", taken);
            }

            // Send data to template
            model.put("categories", categories);
            model.put("semesters", semesters);
            model.put("userData", req.session().attribute("userData"));
            model.put("nextSemesterID", semesters.get(0).getSemesterID());
            return new FreeMarkerEngine().render(
                    new ModelAndView(model, "course-search.ftl")
            );
        });

        // Directory Search
        get("/directory", (req, res) -> {
            //take input
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = departmentRepo.getAllDepartments();


            //send data to template
            model.put("departments", departments);
            model.put("userData", req.session().attribute("userData"));
            return new FreeMarkerEngine().render(
                    new ModelAndView(model, "directorySearch.ftl")
            );
        });

        // Department page
        get("/department/:id", (req, res) -> {
            // TODO: Load department page by ID here
            return "Page not yet implemented";
        });

        // Login form result
        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            User userData = userRepo.attemptLogin(username, password);
            if (userData != visitor) {
                req.session().attribute("username", username);
                res.redirect("/portal");
            } else {
                res.redirect("/?loginError=true");
            }
            return null;
        });

        // Logout function
        get("/logout", (req, res) -> {
            req.session().attribute("username", null);
            req.session().attribute("userData", visitor);
            res.redirect("/?logout=true");
            return null;
        });

        // User Portal page
        get("/portal", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            User userData = req.session().attribute("userData");
            if (userData == visitor) {
                halt(401);
                return "Not authorized";
            }

            int userID = userData.getUserID();
            List<Announcement> announcements = announcementRepo.getAllAnnouncements();

            if (userData.isProfessor()) {

                List<Professor> professor = professorRepo.getProfByID(userData.getUserID());

                // Retrieve semesters for a particular professor to populate selection list.
                List<Semester> semesters = semesterRepo.getSemestersForProfessor(userData.getUserID());


                model.put("professor", professor);
                model.put("semesters", semesters);
                model.put("announcements", announcements);
                model.put("userData", req.session().attribute("userData"));
                return new FreeMarkerEngine().render(
                        new ModelAndView(model, "faculty.ftl")
                );
            } else {
                Student student = studentRepo.getStudentByID(userID);

                // Retrieve semesters for a particular student to populate selection list.
                List<Semester> semesters = semesterRepo.getSemestersForStudent(userID);

                model.put("student", student);
                model.put("semesters", semesters);
                model.put("announcements", announcements);
                model.put("userData", req.session().attribute("userData"));
                return new FreeMarkerEngine().render(
                        new ModelAndView(model, "studentPortal.ftl")
                );
            }
        });

        // Bill Payment page
        get("/bill", (req, res) -> {
            // TODO: Load bill payment page here (students only)
            return "Page not yet implemented";
        });

        // Course Detail page
        get("/course/:id", (req, res) -> {
            // TODO: Display grades and contact info
            Map<String, Object> model = new HashMap<>();
            boolean success = false; // Default
            String offeredID = req.params(":id");
            User userData = req.session().attribute("userData");
            if (userData != visitor) {
                List<Course> courseName = courseRepo.getCourseNameByID(offeredID);
                List<Course> homePageText = courseRepo.getHomePageText(offeredID);
                List<File> courseFiles = fileRepo.getDownloadsForCourse(offeredID);
                List<Grade> studentGrades = gradeRepo.getStudentsGrades(userData.getUserID(), offeredID);
                List<Student> studentList = studentRepo.getStudentsInCourse(offeredID);
                List<Professor> profInfo = professorRepo.getProfessorByOfferedID(Integer.parseInt(offeredID));
                model.put("profInfo", profInfo);
                model.put("courseName", courseName);
                model.put("courseFiles", courseFiles);
                model.put("offeredID", offeredID);
                model.put("userID", userData.getUserID());
                model.put("userData", req.session().attribute("userData"));
                try {
                    if (homePageText.get(0).getText() != null) {
                        success = true; // Row exists and isn't null
                    }
                } catch (Exception e) {
                    courseRepo.setHomePageText(offeredID, "Welcome"); // Row doesn't exist, create
                    homePageText = courseRepo.getHomePageText(offeredID); // Run query again
                    success = true;
                }
                if (success)
                    model.put("text", homePageText.get(0).getText()); // Display, success = true
                else
                    model.put("text", "Welcome"); // Row exists but null, send default

                if (userData.isProfessor()) {
                    if (studentList != null) {
                        model.put("studentList", studentList);
                    }
                    model.put("professor", "1");
                    model.put("student", "0");
                } else if (userData.isStudent()) {
                    String totalPossible = gradeRepo.getTotalPossible(offeredID);
                    String totalAchieved = gradeRepo.getTotalAchieved(userData.getUserID(), offeredID);
                    model.put("professor", "0");
                    model.put("student", "1");
                    model.put("gradeList", studentGrades);
                    model.put("totalPossible", totalPossible);
                    model.put("totalAchieved", totalAchieved);
                }
            } else {
                halt(401);
                return "Unauthorized. You must be logged in.";
            }
            return new FreeMarkerEngine().render(
                    new ModelAndView(model, "course-details.ftl")
            );
        });

        /*
            Old, example routes

            You can study these for how to implement the above routes as necessary.
            Each route begins with the HTTP verb (get/post/put/delete), then the relative route,
            and then it supplies request/response objects to a lambda function that returns a string,
            which is the finished webpage. You can render that string using a template engine. For
            more details, see the Spark Java documentation: http://sparkjava.com/documentation.html
         */

        // Example "hello world" route, with login functionality
        get("/hello/:username/:password", (req, res) -> {
            // Get the params from the URL
            String username = req.params(":username");
            String password = req.params(":password");

            // Try logging in
            User user = userRepo.attemptLogin(username, password);
            if (user != null) {
                // If it worked, output a custom messge
                return "Hello, " + user.getFirstName() + "!";
            } else {
                return "Hello, user who is not logged in!";
            }
        });

        // Example Freemarker template route
        get("/example", (req, res) -> {
            String headingText = "This is some example heading text";
            Map<String, Object> exampleMap = new HashMap<>();
            exampleMap.put("headingText", headingText);
            exampleMap.put("userData", req.session().attribute("userData"));
            return new FreeMarkerEngine().render(
                    new ModelAndView(exampleMap, "example.ftl")
            );
        });
    }

    private static JSONObject eventListToJSON(List<Event> eventList) {
        JSONObject result = new JSONObject();
        JSONArray list = new JSONArray();
        for (Event event : eventList) {
            JSONObject obj = new JSONObject();
            String beginDate = eventFormatter.format(event.getBeginDate());
            String endDate = eventFormatter.format(event.getEndDate());

            obj.put("eventID", event.getEventID());
            obj.put("title", event.getTitle());
            obj.put("description", event.getDescription());
            obj.put("beginDate", beginDate);
            obj.put("endDate", endDate);
            obj.put("department", event.getDepartmentName());
            obj.put("creatorName", event.getCreatorName());
            obj.put("studentCapacity", event.getStudentCapacity());
            obj.put("professorCapacity", event.getProfessorCapacity());
            obj.put("studentsAttending", event.getStudentsAttending());
            obj.put("professorsAttending", event.getProfessorsAttending());

            list.add(obj);
        }

        result.put("data", list);
        return result;
    }
}

