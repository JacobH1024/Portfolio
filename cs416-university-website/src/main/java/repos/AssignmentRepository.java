package repos;

import models.Assignment;
import models.Student;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class AssignmentRepository {

    private Sql2o sql2o;

    public AssignmentRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public int addAssignment(String name, int pointsPossible, int offeredID){
        String assQuery = "INSERT INTO Assignments (name, points_possible, offered_course_id) " +
                          "VALUES (:name, :pointsPossible, :offeredID)";
        String getAssID = "SELECT assignment_id FROM Assignments WHERE name = ?";
        int assignmentID = 0; // DEFAULT
        try(Connection con = sql2o.open()) {
            con.createQuery(assQuery)
                    .addParameter("name", name)
                    .addParameter("pointsPossible", pointsPossible)
                    .addParameter("offeredID", offeredID)
                    .executeUpdate();
        }
        // Ugly but works, trying to go fast
        try {
            String jdbcUser = System.getenv("JDBC-USER");
            String jdbcPass = System.getenv("JDBC-PASS");
            String jdbcURL = System.getenv("JDBC-URL");

            Class.forName("com.mysql.jdbc.Driver");

            if (jdbcURL == null) {
                jdbcURL = "jdbc:mysql://localhost:3306/test";
            }

            if (jdbcUser != null && jdbcPass != null) {
                jdbcURL += "?user=" + jdbcUser + "&password=" + jdbcPass;
            }

            java.sql.Connection conn = DriverManager.getConnection(jdbcURL);
            PreparedStatement getFiles = conn.prepareStatement(getAssID);
            getFiles.setString(1, name);
            ResultSet rs = getFiles.executeQuery();
            rs.next();
            assignmentID = rs.getInt(1);
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return assignmentID;
    }
    // lol.. assID
    public void deleteAssignment(int assID){
        String assQuery = "DELETE FROM Assignments WHERE assignment_id = :assID";
        try(Connection con = sql2o.open()){
            con.createQuery(assQuery)
                    .addParameter("assID", assID)
                    .executeUpdate();
        }
    }
    // Gimme dat booty list
    public List<Assignment> getCoursesAss(String offeredID){
        // "ay gurl mind if I query dat ass" - Ryan lol
        String assQuery = "SELECT * FROM Assignments WHERE offered_course_id = :offeredID";
        try(Connection con = sql2o.open()){
            return con.createQuery(assQuery)
                    .addParameter("offeredID", offeredID)
                    .addColumnMapping("assignment_id", "assID")
                    .addColumnMapping("name", "name")
                    .addColumnMapping("points_possible", "pointsPossible")
                    .addColumnMapping("offered_course_id", "offeredID")
                    .executeAndFetch(Assignment.class);
        }

    }

}
