package repos;

import models.Department;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.List;

public class DepartmentRepository {
    private Sql2o sql2o;

    public DepartmentRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<Department> getAllDepartments() {
        String sql = "SELECT d.department_id, d.name " +
                "FROM Departments d";

        try (Connection con = sql2o.open()) {
            Query depQuery = con.createQuery(sql)
                    .addColumnMapping("department_id", "departmentID")
                    .addColumnMapping("name", "name");

            return depQuery.executeAndFetch(Department.class);
        }
    }

    public int getIdForDepartmentHead(int id) {
        String sql = "SELECT department_id, " +
                "FROM Professor_Employment " +
                "WHERE professor_id = :id AND is_department_head = true " +
                "LIMIT 1";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeScalar(Integer.class);
        }
    }
}
