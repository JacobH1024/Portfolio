package models;

public class Student implements User {
    private int userID;
    private String firstName;
    private String lastName;
    private String majorDegree;
    private String minorDegree;
    private String userName;
    private boolean isGraduate;
    private boolean isInState;

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isLoggedIn() {
        return true;
    }

    public boolean isStudent() {
        return true;
    }

    public boolean isGradStudent() {
        return this.isGraduate;
    }

    public boolean isProfessor() {
        return false;
    }

    public boolean isDepartmentHead() {
        return false;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGraduate(boolean graduate) {
        isGraduate = graduate;
    }

    public String getMajorDegree() {
        return majorDegree;
    }

    public void setMajorDegree(String majorDegree) {
        this.majorDegree = majorDegree;
    }

    public String getMinorDegree() {
        return minorDegree;
    }

    public void setMinorDegree(String minorDegree) {
        this.minorDegree = minorDegree;
    }

    public boolean isInState() {
        return isInState;
    }

    public void setInState(boolean inState) {
        isInState = inState;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
