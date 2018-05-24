package repos;

import models.Semester;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class SemesterRepository {
    private Sql2o sql2o;

    public SemesterRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<Semester> getAllSemesters(){
        String semesterSQL = "SELECT semester_id, term, year FROM Semesters GROUP BY year DESC, term";
        try (Connection con = sql2o.open()){
            return con.createQuery(semesterSQL)
                    .addColumnMapping("semester_id","semesterID")
                    .addColumnMapping("term","term")
                    .addColumnMapping("year","year")
                    .executeAndFetch(Semester.class);
        }
    }
    // Retrieve the semesters that a particular Professor has taught in.
    public List<Semester> getSemestersForProfessor(int professorID) {
        String semestersForProfSQL = "SELECT s.semester_id, s.term, s.year " +
                                     "FROM Semesters s, Courses_Offered c " +
                                     "WHERE s.semester_id = c.semester_id AND c.professor_id = :professorID " +
                                     "GROUP BY year DESC, term";
        try (Connection con = sql2o.open()){
            return con.createQuery(semestersForProfSQL)
                    .addParameter("professorID", professorID)
                    .addColumnMapping("semester_id","semesterID")
                    .addColumnMapping("term","term")
                    .addColumnMapping("year","year")
                    .executeAndFetch(Semester.class);
        }
    }

    public List<Semester> getSemestersForStudent(int studentID) {
        String semestersForProfSQL = "SELECT s.semester_id, s.term, s.year " +
                                     "FROM Semesters s, Student_Enrollment se, Courses_Offered co " +
                                     "WHERE se.student_id = :studentID AND co.semester_id = s.semester_id AND co.offered_id = se.offered_id " +
                                     "GROUP BY year DESC, term";
        try (Connection con = sql2o.open()){
            return con.createQuery(semestersForProfSQL)
                    .addParameter("studentID", studentID)
                    .addColumnMapping("semester_id","semesterID")
                    .addColumnMapping("term","term")
                    .addColumnMapping("year","year")
                    .executeAndFetch(Semester.class);
        }
    }
}
