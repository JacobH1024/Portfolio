package models;

import java.sql.Blob;

public class File {
    private int fileID;
    private int courseID;
    private String fileName;
    private Blob file;

    public Blob getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileID() {
        return fileID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }
}
