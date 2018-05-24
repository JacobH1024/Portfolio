package models;

public class Professor implements User {
    private String department;
    private String emailAddress;
    private String firstName;
    private boolean isDepartmentHead;
    private String lastName;
    private String phoneNumber;
    private int Salary;
    private int userID;
    private String userName;

    public void setDepartmentHead(boolean departmentHead) {
        isDepartmentHead = departmentHead;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLoggedIn() {
        return true;
    }

    public boolean isStudent() {
        return false;
    }

    public boolean isGradStudent() {
        return false;
    }

    public boolean isProfessor() {
        return true;
    }

    public boolean isDepartmentHead() {
        return this.isDepartmentHead;
    }
}
