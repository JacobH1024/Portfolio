package repos;

import models.Announcement;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.List;

public class AnnouncementRepository
{
    private Sql2o sql2o;

    public AnnouncementRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    // Retrieve all announcements to display in Faculty Announcements table (html)
    public List<Announcement> getAllAnnouncements() {
        String announcementsSQL = "SELECT a.announcement_id, a.subject, a.message, a.date, a.author, a.professor_id " +
                                  "FROM Faculty_Announcements a, Professors p " +
                                  "WHERE a.professor_id = p.professor_id " +
                                  "ORDER BY a.announcement_id DESC ";

        try (Connection con = sql2o.open()) {
            return con.createQuery(announcementsSQL)
                    .addColumnMapping("announcement_id", "announcementID")
                    .addColumnMapping("subject", "subject")
                    .addColumnMapping("message", "message")
                    .addColumnMapping("date", "date")
                    .addColumnMapping("author", "author")
                    .addColumnMapping("professor_id", "authorID")
                    .executeAndFetch(Announcement.class);
        }
    }
    public void postAnnouncement(String announcement, String subject, String date, String author, int id) {
        String insertAnnouncementSQL = "INSERT INTO Faculty_Announcements (subject, message, date, author, professor_id) " +
                                        "VALUES (:subject, :message, :date, :author, :professor_id)";

        try (Connection con = sql2o.open()) {
            con.createQuery(insertAnnouncementSQL)
                    .addParameter("subject", subject)
                    .addParameter("message", announcement)
                    .addParameter("date", date)
                    .addParameter("author", author)
                    .addParameter("professor_id", id)
                    .executeUpdate();
        }
    }
}

