package repos;

import models.EnrolledCourse;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.List;

public class EnrolledCourseRepository {
    private Sql2o sql2o;

    public EnrolledCourseRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<EnrolledCourse> getAllEnrolledCoursesByStudent(int id) {
        String sql = "SELECT c.course_id, c.name, se.enrollment_id, se.offered_id ,se.student_id, " +
                "se.status, s.term, s.year, c.credit_hours, cc.name as category, co.days, co.time " +
                "FROM Student_Enrollment se " +
                "INNER JOIN Courses_Offered co ON se.offered_id = co.offered_id " +
                "INNER JOIN Courses c ON co.course_id = c.course_id " +
                "INNER JOIN Semesters s ON co.semester_id = s.semester_id " +
                "INNER JOIN Course_Categories cc ON c.category_id = cc.course_category_id " +
                "WHERE :id = se.student_id";

        try (Connection con = sql2o.open()) {
            Query studentQuery = con.createQuery(sql)
                    .addParameter("id", id)
                    .addColumnMapping("course_id", "courseID")
                    .addColumnMapping("name", "courseName")
                    .addColumnMapping("enrollment_id", "enrollmentID")
                    .addColumnMapping("offered_id", "offeredID")
                    .addColumnMapping("student_id", "studentID")
                    .addColumnMapping("status", "status")
                    .addColumnMapping("term", "term")
                    .addColumnMapping("year", "year")
                    .addColumnMapping("credit_hours", "creditHours")
                    .addColumnMapping("category", "category")
                    .addColumnMapping("days", "days")
                    .addColumnMapping("time", "time");

            return studentQuery.executeAndFetch(EnrolledCourse.class);
        }
    }

    public List<EnrolledCourse> getAllEnrolledCoursesByStudentForSemester(int id, int semesterID) {
        String sql = "SELECT c.course_id, c.name, se.enrollment_id, se.student_id, se.offered_id, " +
                "se.status, s.term, s.year, c.credit_hours, cc.name as category, co.days, co.time " +
                "FROM Student_Enrollment se " +
                "INNER JOIN Courses_Offered co ON se.offered_id = co.offered_id " +
                "INNER JOIN Courses c ON co.course_id = c.course_id " +
                "INNER JOIN Semesters s ON co.semester_id = s.semester_id " +
                "INNER JOIN Course_Categories cc ON c.category_id = cc.course_category_id " +
                "WHERE :id = se.student_id AND :semesterID = s.semester_id ";

        try (Connection con = sql2o.open()) {
            Query studentQuery = con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("semesterID", semesterID)
                    .addColumnMapping("course_id", "courseID")
                    .addColumnMapping("name", "courseName")
                    .addColumnMapping("enrollment_id", "enrollmentID")
                    .addColumnMapping("student_id", "studentID")
                    .addColumnMapping("offered_id", "offeredID")
                    .addColumnMapping("status", "status")
                    .addColumnMapping("term", "term")
                    .addColumnMapping("year", "year")
                    .addColumnMapping("credit_hours", "creditHours")
                    .addColumnMapping("category", "category")
                    .addColumnMapping("days", "days")
                    .addColumnMapping("time", "time");

            return studentQuery.executeAndFetch(EnrolledCourse.class);
        }
    }
}
