package models;

public class Announcement
{
    private int announcementID;
    private String subject;
    private String message;
    private String date;
    private int authorID;
    private String author;

    public int getAnnouncementID() {
        return announcementID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate() {
        this.date = date;
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
