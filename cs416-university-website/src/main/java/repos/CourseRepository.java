package repos;

import models.Course;
import models.CourseCategory;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.List;

public class CourseRepository {
    private Sql2o sql2o;

    public CourseRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    // Gets courses offered and populates list
    public List<Course> courseSearch(String semester, String[] categories, String name) {
        String coursesSQL = "Select c.course_id, c1.name, c1.description, c1.credit_hours, c.days, c.time, c.professor_id, c.offered_id " +
                "FROM Courses_Offered c, Courses c1 " +
                "WHERE c.semester_id = :semester AND c.course_id = c1.course_id";

        // Lists the conditions out for multiple category selections
        if (categories.length > 0) {
            StringBuilder categoryConditions = new StringBuilder(" AND (category_id = :category_id_0");
            for (int i = 1; i < categories.length; i++) {
                categoryConditions.append(" OR category_id = :category_id_").append(i);
            }
            categoryConditions.append(")");
            coursesSQL += categoryConditions;
        }

        // Adds a condition for a name search
        if (name != null && name.length() > 0) {
            coursesSQL += " AND c1.name LIKE :name";
        }

        try (Connection con = sql2o.open()) {
            Query courseQuery = con.createQuery(coursesSQL)
                    .addParameter("semester", semester)
                    .addColumnMapping("course_id", "courseID")
                    .addColumnMapping("name", "name")
                    .addColumnMapping("description", "description")
                    .addColumnMapping("credit_hours", "creditHours")
                    .addColumnMapping("days", "days")
                    .addColumnMapping("time", "time")
                    .addColumnMapping("professor_id", "professorID")
                    .addColumnMapping("offered_id", "offeredID");

            for (int i = 0; i < categories.length; i++) {
                courseQuery.addParameter("category_id_" + i, categories[i]);
            }

            // The name parameter is formatted with '%' to indicate a character wildcard
            if (name != null && name.length() > 0) {
                courseQuery.addParameter("name", "%" + name + "%");
            }

            return courseQuery.executeAndFetch(Course.class);
        }
    }

    // Populate list of all possible categories
    public List<CourseCategory> getAllCategories() {
        // Query and remove dupes
        String categorySQL = "Select course_category_id, name FROM Course_Categories ORDER BY name";
        try (Connection con = sql2o.open()) {
            return con.createQuery(categorySQL)
                    .addColumnMapping("course_category_id", "categoryID")
                    .addColumnMapping("name", "name")
                    .executeAndFetch(CourseCategory.class);
        }
    }

    // Get Course name by offeredID
    public List<Course> getCourseNameByID(String ID) {
        // Query
        String nameQuery = "SELECT c.name, c.course_id FROM Courses c WHERE c.course_id IN " +
                "( SELECT c1.course_id FROM Courses_Offered c1 WHERE c.course_id" +
                "= c1.course_id AND c1.offered_id = :id)";
        try (Connection con = sql2o.open()) {
            return con.createQuery(nameQuery)
                    .addParameter("id", ID)
                    .addColumnMapping("course_id", "courseID")
                    .addColumnMapping( ID, "offeredID")
                    .addColumnMapping("name", "name")
                    .executeAndFetch(Course.class);
        }
    }

    // Register course for student
    public void registerCourse(String offeredID, String studentID) {
        String registerSQL = "INSERT INTO Student_Enrollment ( student_id, offered_id, status) " +
                "VALUES ( :studentID, :offeredID, :status)";
        try (Connection con = sql2o.open()) {
            con.createQuery(registerSQL)
                    .addParameter("studentID", studentID)
                    .addParameter("offeredID", offeredID)
                    .addParameter("status", "in-progress")
                    .executeUpdate();
        }
    }

    public List<Course> getHomePageText(String offeredID) {
        String homepageQuery = "SELECT text FROM Course_Homepage WHERE offered_course_id = :offeredID";
        try (Connection con = sql2o.open()) {
            return con.createQuery(homepageQuery)
                    .addParameter("offeredID", offeredID)
                    .addColumnMapping("text", "text")
                    .executeAndFetch(Course.class);
        }
    }

    public void setHomePageText(String offeredID, String text) {
        String homePageQuery = "INSERT INTO Course_Homepage (offered_course_id ,text) VALUE (:courseID, :text)";
        try (Connection con = sql2o.open()) {
            con.createQuery(homePageQuery)
                    .addParameter("courseID", offeredID)
                    .addParameter("text", text)
                    .executeUpdate();
        }
    }

    public void updateHomePageText(String offeredID, String text) {
        String homePageQuery = "UPDATE Course_Homepage SET text = '" + text + "' " +
                "WHERE offered_course_id = :offeredID";
        try (Connection con = sql2o.open()) {
            con.createQuery(homePageQuery)
                    .addParameter("offeredID", offeredID)
                    .executeUpdate();
        }
    }

    // Retrieve the courses taught by some professor with chosen semester.
    public List<Course> getCoursesByProfessorSemester(int professorID, int semesterID) {
        String coursesByProfSemSQL = "SELECT c.course_id, c.offered_id, c1.name, c.days, c.time, c1.credit_hours " +
                                     "FROM Courses_Offered c, Courses c1 " +
                                     "WHERE c.professor_id = :professorID AND c.semester_id = :semesterID " +
                                        "AND c.course_id = c1.course_id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(coursesByProfSemSQL)
                    .addParameter("professorID", professorID)
                    .addParameter("semesterID", semesterID)
                    .addColumnMapping("course_id", "courseID")
                    .addColumnMapping("offered_id", "offeredID")
                    .addColumnMapping("name", "name")
                    .addColumnMapping("days", "days")
                    .addColumnMapping("time", "time")
                    .addColumnMapping("credit_hours", "creditHours")
                    .executeAndFetch(Course.class);
        }
    }
}
