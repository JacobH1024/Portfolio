package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Event {
    private Timestamp beginDate;
    private String creatorFirstName;
    private String creatorLastName;
    private int departmentID;
    private String departmentName;
    private String description;
    private Timestamp endDate;
    private int eventID;
    private int professorCapacity;
    private int professorsAttending;
    private int studentCapacity;
    private int studentsAttending;
    private String title;

    public LocalDateTime getBeginDate() {
        return LocalDateTime.ofInstant(this.beginDate.toInstant(), ZoneId.systemDefault());
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public String getCreatorName() {
        return this.creatorFirstName + " " + creatorLastName;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEndDate() {
        return LocalDateTime.ofInstant(this.endDate.toInstant(), ZoneId.systemDefault());
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getProfessorCapacity() {
        return professorCapacity;
    }

    public void setProfessorCapacity(int professorCapacity) {
        this.professorCapacity = professorCapacity;
    }

    public int getProfessorsAttending() {
        return professorsAttending;
    }

    public void setProfessorsAttending(int professorsAttending) {
        this.professorsAttending = professorsAttending;
    }

    public int getStudentCapacity() {
        return studentCapacity;
    }

    public void setStudentCapacity(int studentCapacity) {
        this.studentCapacity = studentCapacity;
    }

    public int getStudentsAttending() {
        return studentsAttending;
    }

    public void setStudentsAttending(int studentsAttending) {
        this.studentsAttending = studentsAttending;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorFirstName() {
        return creatorFirstName;
    }

    public void setCreatorFirstName(String creatorFirstName) {
        this.creatorFirstName = creatorFirstName;
    }

    public String getCreatorLastName() {
        return creatorLastName;
    }

    public void setCreatorLastName(String creatorLastName) {
        this.creatorLastName = creatorLastName;
    }
}
