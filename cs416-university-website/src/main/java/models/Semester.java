package models;

import java.util.Date;

public class Semester {
    private int semesterID;
    private String term;
    private int year;

    public int getSemesterID() {
        return semesterID;
    }

    public String getTerm() {
        return term;
    }

    public int getYear() {
        return year;
    }

    public void setSemesterID(int semesterID) {
        this.semesterID = semesterID;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String toString() {
        return year + " " + term;
    }
}
