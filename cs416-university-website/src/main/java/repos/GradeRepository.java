package repos;

import models.Grade;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class GradeRepository {

    private Sql2o sql2o;

    public GradeRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<Grade> getStudentsGrades(int userID, String offeredID) {
        String gradesQuery = "SELECT a.name, a.assignment_id, g.points, a.points_possible " +
                "FROM Grades g, Assignments a " +
                "WHERE g.student_id = :userID AND g.offered_course_id = :offeredID AND g.assignment_id = a.assignment_id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(gradesQuery)
                    .addParameter("userID", userID)
                    .addParameter("offeredID", offeredID)
                    .addColumnMapping("name", "assignmentName")
                    .addColumnMapping("points", "points")
                    .addColumnMapping("points_possible", "pointsPossible")
                    .addColumnMapping("assignment_id", "assignmentID")
                    .executeAndFetch(Grade.class);
        }

    }

    public String getTotalPossible(String offeredID) {
        String totalQuery = "SELECT SUM(points_possible), COUNT(*) FROM Assignments WHERE offered_course_id = :offeredID";
        try (Connection con = sql2o.open()) {
            return con.createQuery(totalQuery)
                    .addParameter("offeredID", offeredID)
                    .executeScalar(String.class);
        }
    }

    public String getTotalAchieved(int userID, String offeredID) {
        String totalQuery = "SELECT SUM(points), COUNT(*) FROM Grades WHERE student_id = :userID AND offered_course_id = :offeredID";
        try (Connection con = sql2o.open()) {
            return con.createQuery(totalQuery)
                    .addParameter("userID", userID)
                    .addParameter("offeredID", offeredID)
                    .executeScalar(String.class);
        }
    }

    public void updateAssignmentGrade(int userID, int assignmentID, int newScore) {
        String updateQuery = "UPDATE Grades SET points = '" + newScore + "' " +
                "WHERE student_id = :userID AND assignment_id = :assignmentID";
        try (Connection con = sql2o.open()) {
            con.createQuery(updateQuery)
                    .addParameter("userID", userID)
                    .addParameter("assignmentID", assignmentID)
                    .executeUpdate();
        }
    }

    public void insertNewAssignment(int userID, int assignmentID, int newScore, int offeredID) {
        String insertQuery = "INSERT INTO Grades (student_id, assignment_id, points, offered_course_id) " +
                "VALUES (:userID, :assignmentID, :newScore, :offeredID)";
        try (Connection con = sql2o.open()) {
            con.createQuery(insertQuery)
                    .addParameter("userID", userID)
                    .addParameter("assignmentID", assignmentID)
                    .addParameter("newScore", newScore)
                    .addParameter("offeredID", offeredID)
                    .executeUpdate();
        }

    }
}
