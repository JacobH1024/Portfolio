package repos;

import models.Event;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.time.LocalDate;
import java.util.List;

public class EventRepository {
    private Sql2o sql2o;

    public EventRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<Event> getEventsUpcomingWeek() {
        return getEventsWeekAfterDate(LocalDate.now());
    }

    public List<Event> getEventsWeekAfterDate(LocalDate date) {
        // Set to 8 days because we use a "less than" comparison
        LocalDate weekAfterDate = date.plusDays(8);
        String sql = "SELECT e.event_id, " +
                "e.title, " +
                "e.description, " +
                "e.begin_date, " +
                "e.end_date, " +
                "e.student_capacity, " +
                "e.prof_capacity, " +
                "d.name, " +
                "h.first_name, " +
                "h.last_name, " +
                "(SELECT COUNT(*) FROM Student_Event_Registrations s WHERE s.event_id = e.event_id) AS students_attending, " +
                "(SELECT COUNT(*) FROM Professor_Event_Registrations p WHERE p.event_id = e.event_id) AS professors_attending " +
                "FROM Events e, Departments d, Professors h " +
                "WHERE e.begin_date >= :date " +
                "AND e.begin_date < :weekAfterDate " +
                "AND d.department_id = e.department_id " +
                "AND h.professor_id = e.creator_id " +
                "ORDER BY e.begin_date";

        try (Connection con = sql2o.open()) {

            List<Event> eventList = con.createQuery(sql)
                    .addParameter("date", date.toString())
                    .addParameter("weekAfterDate", weekAfterDate.toString())
                    .addColumnMapping("event_id", "eventID")
                    .addColumnMapping("title", "title")
                    .addColumnMapping("description", "description")
                    .addColumnMapping("begin_date", "beginDate")
                    .addColumnMapping("end_date", "endDate")
                    .addColumnMapping("name", "departmentName")
                    .addColumnMapping("first_name", "creatorFirstName")
                    .addColumnMapping("last_name", "creatorLastName")
                    .addColumnMapping("student_capacity", "studentCapacity")
                    .addColumnMapping("prof_capacity", "professorCapacity")
                    .addColumnMapping("students_attending", "studentsAttending")
                    .addColumnMapping("professors_attending", "professorsAttending")
                    .executeAndFetch(Event.class);

            return eventList;
        }
    }

    public List<Event> getEventsByDepartmentID(int id) {
        String sql = "SELECT e.event_id, " +
                "e.title, " +
                "e.description, " +
                "e.begin_date, " +
                "e.end_date, " +
                "h.first_name, " +
                "h.last_name, " +
                "(SELECT COUNT(*) FROM Student_Event_Registrations s WHERE s.event_id = e.event_id) AS students_attending, " +
                "(SELECT COUNT(*) FROM Professor_Event_Registrations p WHERE p.event_id = e.event_id) AS professors_attending " +
                "FROM Events e, Professors h " +
                "WHERE e.department_id = :id " +
                "AND h.professor_id = e.creator_id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .addColumnMapping("event_id", "eventID")
                    .addColumnMapping("title", "title")
                    .addColumnMapping("description", "description")
                    .addColumnMapping("begin_date", "beginDate")
                    .addColumnMapping("end_date", "endDate")
                    .addColumnMapping("first_name", "creatorFirstName")
                    .addColumnMapping("last_name", "creatorLastName")
                    .addColumnMapping("student_capacity", "studentCapacity")
                    .addColumnMapping("professor_capacity", "professorCapacity")
                    .addColumnMapping("students_attending", "studentsAttending")
                    .addColumnMapping("professors_attending", "professorsAttending")
                    .executeAndFetch(Event.class);
        }
    }

    public int addNewEvent(Event event, int creatorID) {
        String sql = "INSERT INTO Events " +
                "(" +
                "title, " +
                "description, " +
                "begin_date, " +
                "end_date, " +
                "department_id, " +
                "creator_id, " +
                "student_capacity, " +
                "prof_capacity" +
                ") " +
                "VALUES " +
                "(" +
                ":title, " +
                ":description, " +
                ":beginDate, " +
                ":endDate, " +
                ":departmentID, " +
                ":creatorID, " +
                ":studentCapacity, " +
                ":professorCapacity" +
                ")";

        try(Connection con = sql2o.open()) {
            return (int) con.createQuery(sql, true)
                    .addParameter("title", event.getTitle())
                    .addParameter("description", event.getDescription())
                    .addParameter("beginDate", event.getBeginDate())
                    .addParameter("endDate", event.getEndDate())
                    .addParameter("departmentID", event.getDepartmentID())
                    .addParameter("creatorID", creatorID)
                    .addParameter("studentCapacity", event.getStudentCapacity())
                    .addParameter("professorCapacity", event.getProfessorCapacity())
                    .executeUpdate()
                    .getKey();
        }
    }

    public boolean registerForEventStudent(int studentID, int eventID) {
        String checkExistsSQL = "SELECT " +
                "COUNT(*) " +
                "FROM Student_Event_Registrations s " +
                "WHERE s.event_id = :eventID AND s.student_id = :studentID";

        try (Connection con = sql2o.open()) {
            int studentExistsCount = con.createQuery(checkExistsSQL)
                    .addParameter("eventID", eventID)
                    .addParameter("studentID", studentID)
                    .executeAndFetchFirst(Integer.class);

            if (studentExistsCount > 0) {
                return false;
            }
        }

        String checkCapacitySQL = "SELECT " +
                "e.student_capacity, " +
                "(SELECT COUNT(*) FROM Student_Event_Registrations s WHERE s.event_id = e.event_id) AS students_attending " +
                "FROM Events e WHERE e.event_id = :eventID";

        try (Connection con = sql2o.open()) {
            Event eventCapacityData = con.createQuery(checkCapacitySQL)
                    .addParameter("eventID", eventID)
                    .addColumnMapping("student_capacity", "studentCapacity")
                    .addColumnMapping("students_attending", "studentsAttending")
                    .executeAndFetchFirst(Event.class);

            if (eventCapacityData.getStudentCapacity() - eventCapacityData.getStudentsAttending() > 0) {
                String registerProfSQL = "INSERT INTO Student_Event_Registrations VALUES (:eventID, :studentID)";

                try (Connection con2 = sql2o.open()) {
                    con2.createQuery(registerProfSQL)
                            .addParameter("eventID", eventID)
                            .addParameter("studentID", studentID)
                            .executeUpdate();
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public boolean registerForEventProfessor(int professorID, int eventID) {
        String checkExistsSQL = "SELECT " +
                "COUNT(*) " +
                "FROM Professor_Event_Registrations p " +
                "WHERE p.event_id = :eventID AND p.professor_id = :professorID";

        try (Connection con = sql2o.open()) {
            int professorExistsCount = con.createQuery(checkExistsSQL)
                    .addParameter("eventID", eventID)
                    .addParameter("professorID", professorID)
                    .executeAndFetchFirst(Integer.class);

            if (professorExistsCount > 0) {
                return false;
            }
        }

        String checkCapacitySQL = "SELECT " +
                "e.prof_capacity, " +
                "(SELECT COUNT(*) FROM Professor_Event_Registrations s WHERE s.event_id = e.event_id) AS profs_attending " +
                "FROM Events e WHERE e.event_id = :eventID";

        try (Connection con = sql2o.open()) {
            Event eventCapacityData = con.createQuery(checkCapacitySQL)
                    .addParameter("eventID", eventID)
                    .addColumnMapping("prof_capacity", "professorCapacity")
                    .addColumnMapping("profs_attending", "professorsAttending")
                    .executeAndFetchFirst(Event.class);

            if (eventCapacityData.getProfessorCapacity() - eventCapacityData.getProfessorsAttending() > 0) {
                String registerProfSQL = "INSERT INTO Professor_Event_Registrations VALUES (:eventID, :professorID)";

                try (Connection con2 = sql2o.open()) {
                    con2.createQuery(registerProfSQL)
                            .addParameter("eventID", eventID)
                            .addParameter("professorID", professorID)
                            .executeUpdate();
                    return true;
                }
            } else {
                return false;
            }
        }
    }
}
