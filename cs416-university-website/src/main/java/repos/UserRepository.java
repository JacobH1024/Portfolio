package repos;

import models.Professor;
import models.Student;
import models.User;
import models.Visitor;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class UserRepository {
    private Sql2o sql2o;
    private Visitor visitor;

    public UserRepository(Sql2o sql2o, Visitor visitor) {
        this.sql2o = sql2o;
        this.visitor = visitor;
    }

    public User attemptLogin(String username, String password) {
        // The words that begin with ':' are parameters that are filled in using .addParameter(name, value)
        String student_sql = "SELECT student_id, first_name, last_name, username, graduate " +
                "FROM Students " +
                "WHERE username = :username AND password = :password";
        String prof_sql = "SELECT professor_id, first_name, last_name, username " +
                "FROM Professors " +
                "WHERE username = :username AND password = :password";

        try(Connection con = sql2o.open()) {
            if (this.studentExists(username)) {
                List<Student> studentList = con.createQuery(student_sql)
                        .addParameter("username", username).addParameter("password", password)
                        .addColumnMapping("student_id", "userID")
                        .addColumnMapping("first_name", "firstName")
                        .addColumnMapping("last_name", "lastName")
                        .addColumnMapping("username", "userName")
                        .addColumnMapping("graduate", "isGraduate")
                        .executeAndFetch(Student.class);
                if (studentList.isEmpty()) {
                    return visitor;
                }
                return studentList.get(0);

            } else if (this.professorExists(username)) {
                List<Professor> professorList = con.createQuery(prof_sql)
                        .addParameter("username", username).addParameter("password", password)
                        .addColumnMapping("professor_id", "userID")
                        .addColumnMapping("first_name", "firstName")
                        .addColumnMapping("last_name", "lastName")
                        .addColumnMapping("username", "userName")
                        .executeAndFetch(Professor.class);
                if (professorList.isEmpty()) {
                    return visitor;
                }
                return professorList.get(0);
            } else {
                return visitor;
            }
        }
    }

    public User sessionLogin(String username) {
        String student_sql = "SELECT student_id, first_name, last_name, username, graduate " +
                "FROM Students " +
                "WHERE username = :username";
        String prof_sql = "SELECT p.professor_id, p.first_name, p.last_name, p.username, pe.is_Department_head " +
                "FROM Professors p " +
                "INNER JOIN Professor_Employment pe ON pe.professor_id = p.professor_id " +
                "INNER JOIN Departments d ON pe.department_id = d.department_id " +
                "WHERE username = :username";

        if (username == null) {
            return visitor;
        }

        try(Connection con = sql2o.open()) {
            if (this.studentExists(username)) {
                List<Student> studentList = con.createQuery(student_sql)
                        .addParameter("username", username)
                        .addColumnMapping("student_id", "userID")
                        .addColumnMapping("first_name", "firstName")
                        .addColumnMapping("last_name", "lastName")
                        .addColumnMapping("username", "userName")
                        .addColumnMapping("graduate", "isGraduate")
                        .executeAndFetch(Student.class);
                if (studentList.isEmpty()) {
                    return visitor;
                }
                return studentList.get(0);

            } else if (this.professorExists(username)) {
                List<Professor> professorList = con.createQuery(prof_sql)
                        .addParameter("username", username)
                        .addColumnMapping("professor_id", "userID")
                        .addColumnMapping("first_name", "firstName")
                        .addColumnMapping("last_name", "lastName")
                        .addColumnMapping("username", "userName")
                        .addColumnMapping("is_Department_head", "isDepartmentHead")
                        .executeAndFetch(Professor.class);
                if (professorList.isEmpty()) {
                    return visitor;
                }
                return professorList.get(0);
            } else {
                return visitor;
            }
        }
    }

    private boolean studentExists(String username) {
        String sql = "SELECT count(*) FROM Students WHERE username = :username";
        try(Connection con = sql2o.open()) {
            int count = con.createQuery(sql)
                    .addParameter("username", username)
                    .executeScalar(Integer.class);
            return count > 0;
        }
    }

    private boolean professorExists(String username) {
        String sql = "SELECT count(*) FROM Professors WHERE username = :username";
        try(Connection con = sql2o.open()) {
            int count = con.createQuery(sql)
                    .addParameter("username", username)
                    .executeScalar(Integer.class);
            return count > 0;
        }
    }
}
