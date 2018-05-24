package repos;

import models.Professor;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.List;

public class ProfessorRepository {
    private Sql2o sql2o;

    public ProfessorRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<Professor> getProfByID(int id) {
        String sql = "SELECT p.professor_id, p.first_name, p.last_name, p.username, p.salary, " +
                "pe.is_Department_head, p.phone_number, p.email_address, d.name as department_name " +
                "FROM Professors p " +
                "INNER JOIN Professor_Employment pe ON pe.professor_id = p.professor_id " +
                "INNER JOIN Departments d ON pe.department_id = d.department_id " +
                "WHERE :id = p.professor_id";

        try (Connection con = sql2o.open()) {
            Query profQuery = con.createQuery(sql)
                    .addParameter("id", id)
                    .addColumnMapping("professor_id", "userID")
                    .addColumnMapping("first_name", "firstName")
                    .addColumnMapping("last_name", "lastName")
                    .addColumnMapping("username", "userName")
                    .addColumnMapping("salary", "salary")
                    .addColumnMapping("is_Department_head", "isDepartmentHead")
                    .addColumnMapping("phone_number", "phoneNumber")
                    .addColumnMapping("email_address", "emailAddress")
                    .addColumnMapping("department_name", "department");

            return profQuery.executeAndFetch(Professor.class);
        }
    }

    public List<Professor> getProfByDepartmentName(String departmentName) {
        String sql = "SELECT p.professor_id, p.first_name, p.last_name, p.username, p.salary, " +
                "pe.is_Department_head, p.phone_number, p.email_address, d.name as department_name " +
                "FROM Professors p " +
                "INNER JOIN Professor_Employment pe ON pe.professor_id = p.professor_id " +
                "INNER JOIN Departments d ON pe.department_id = d.department_id " +
                "WHERE :departmentName = d.name";

        try (Connection con = sql2o.open()) {
            Query profQuery = con.createQuery(sql)
                    .addParameter("departmentName", departmentName)
                    .addColumnMapping("professor_id", "userID")
                    .addColumnMapping("first_name", "firstName")
                    .addColumnMapping("last_name", "lastName")
                    .addColumnMapping("username", "userName")
                    .addColumnMapping("salary", "salary")
                    .addColumnMapping("is_Department_head", "isDepartmentHead")
                    .addColumnMapping("phone_number", "phoneNumber")
                    .addColumnMapping("email_address", "emailAddress")
                    .addColumnMapping("department_name", "department");

            return profQuery.executeAndFetch(Professor.class);
        }
    }

    public List<Professor> getAllProfessors() {
        String professorSQL =   "Select p.professor_id, p.first_name, p.last_name, p.username, p.password, p.salary, " +
                "pe.is_Department_head, p.phone_number, p.email_address, d.name as department_name " +
                "FROM Professors p " +
                "INNER JOIN Professor_Employment pe ON pe.professor_id = p.professor_id " +
                "INNER JOIN Departments d ON pe.department_id = d.department_id";

        try (Connection con = sql2o.open()) {
            Query profQuery = con.createQuery(professorSQL)
                    .addColumnMapping("professor_id", "userID")
                    .addColumnMapping("first_name", "firstName")
                    .addColumnMapping("last_name", "lastName")
                    .addColumnMapping("username", "userName")
                    .addColumnMapping("salary", "salary")
                    .addColumnMapping("is_Department_head", "isDepartmentHead")
                    .addColumnMapping("phone_number", "phoneNumber")
                    .addColumnMapping("email_address", "emailAddress")
                    .addColumnMapping("department_name", "department");

            return profQuery.executeAndFetch(Professor.class);
        }
    }

    public List<Professor> getProfessorByOfferedID(int ID){
        String professorSQL =   "SELECT p.professor_id, p.first_name, p.last_name, p.phone_number, p.email_address " +
                                "FROM Professors p, Courses_Offered c " +
                                "WHERE c.offered_id = :offID AND c.professor_id = p.professor_id";

        try (Connection con = sql2o.open()) {
            Query profQuery = con.createQuery(professorSQL)
                    .addParameter("offID", ID)
                    .addColumnMapping("professor_id", "userID")
                    .addColumnMapping("first_name", "firstName")
                    .addColumnMapping("last_name", "lastName")
                    .addColumnMapping("phone_number", "phoneNumber")
                    .addColumnMapping("email_address", "emailAddress");

            return profQuery.executeAndFetch(Professor.class);
        }
    }
}
