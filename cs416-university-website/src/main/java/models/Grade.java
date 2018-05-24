package models;

public class Grade {
    private int studentID;
    private int assignmentID;
    private String assignmentName;
    private int points;
    private int pointsPossible;
    private int courseID;

    public int getStudentID() {
        return studentID;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public String getAssignmentName(){
        return assignmentName;
    }

    public int getPoints() {
        return points;
    }

    public int getPointsPossible(){
        return pointsPossible;
    }

    public int getCourseID(){
        return courseID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setPointsPossible(int pointsPossible) {
        this.pointsPossible = pointsPossible;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
