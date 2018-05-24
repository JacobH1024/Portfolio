package models;

public interface User {
    int getUserID();

    String getFirstName();

    String getLastName();

    String getUserName();

    boolean isLoggedIn();

    boolean isStudent();

    boolean isGradStudent();

    boolean isProfessor();

    boolean isDepartmentHead();
}