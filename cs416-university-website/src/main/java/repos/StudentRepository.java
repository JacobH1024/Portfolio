package repos;

import models.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import java.util.List;

public class StudentRepository {
    private Sql2o sql2o;

    public StudentRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public Student getStudentByID(int id) {
        String sql = "SELECT s.student_id, s.first_name, s.last_name, s.username, s.in_state, " +
                "s.graduate, dMajor.name as majorName, dMinor.name as minorName " +
                "FROM Students s " +
                "INNER JOIN Degrees dMajor ON dMajor.degree_id = s.major " +
                "INNER JOIN Degrees dMinor ON dMinor.degree_id = s.minor " +
                "WHERE :id = s.student_id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .addColumnMapping("student_id", "userID")
                    .addColumnMapping("first_name", "firstName")
                    .addColumnMapping("last_name", "lastName")
                    .addColumnMapping("username", "userName")
                    .addColumnMapping("in_state", "isInState")
                    .addColumnMapping("graduate", "isGraduate")
                    .addColumnMapping("majorName", "majorDegree")
                    .addColumnMapping("minorName", "minorDegree")
                    .executeAndFetchFirst(Student.class);
        }
    }

    public  List<Student> getAllStudents() {
        String sql = "SELECT s.student_id, s.first_name, s.last_name, s.username, s.in_state, " +
                "s.graduate, dmajor.name as majorName, dminor.name as minorName " +
                "FROM Students s " +
                "INNER JOIN Degrees dMajor ON dMajor.degree_id = s.major " +
                "INNER JOIN Degrees dMinor ON dMinor.degree_id = s.minor ";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addColumnMapping("student_id", "userID")
                    .addColumnMapping("first_name", "firstName")
                    .addColumnMapping("last_name", "lastName")
                    .addColumnMapping("username", "userName")
                    .addColumnMapping("in_state", "isInState")
                    .addColumnMapping("graduate", "isGraduate")
                    .addColumnMapping("majorName", "majorDegree")
                    .addColumnMapping("minorName", "minorDegree")
                    .executeAndFetch(Student.class);
        }
    }

    public List<Student> getStudentsInCourse(String offeredID){
        String studentQuery = "SELECT s.first_name, s.last_name, s.student_id " +
                              "FROM Students s, Student_Enrollment s1 " +
                              "WHERE s.student_id = s1.student_id AND s1.offered_id = :offeredID";
        try(Connection con = sql2o.open()){
           return con.createQuery(studentQuery)
                    .addParameter("offeredID", offeredID)
                    .addColumnMapping("first_name","firstName")
                    .addColumnMapping("last_name", "lastName")
                    .addColumnMapping("student_id", "userID")
                    .executeAndFetch(Student.class);
        }
    }
}
