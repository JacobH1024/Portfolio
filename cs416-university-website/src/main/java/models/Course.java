package models;

public class Course
{
    private int courseID;
    private String name;
    private String description;
    private int creditHours;
    private String days;
    private String time;
    private int professorID;
    private int courseCategoryID;
    private String categoryName;
    private int offeredID;
    private String text;

    public int getCourseID() {
        return courseID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public String getDays() {
        return days;
    }

    public String getTime() {
        return time;
    }

    public int getProfessorID() {
        return professorID;
    }

    public int getCategoryId(){
        return courseCategoryID;
    }
    public String getCategoryName(){
        return categoryName;
    }

    public int getOfferedID() {
        return offeredID;
    }

    public String getText() {
        return text;
    }
}
