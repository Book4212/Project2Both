package com.example.project.studentnamereader;


public class TempStudentScoreDB {
    // Table name
    public static final String table_name = "Temp_Student_Score";
    // column name
    public static final String[] column = { "ID", "student_id", "student_name", "time_check", "course_id_sec", "score" };
    // value of column
    private int ID;
    private String student_id;
    private String student_name;
    private String time_check;
    private String course_id_sec;
    private float score;

    public TempStudentScoreDB(int ID, String student_id, String student_name, String time_check, String course_id_sec, float score) {
        this.ID = ID;
        this.student_id = student_id;
        this.student_name = student_name;
        this.time_check = time_check;
        this.course_id_sec = course_id_sec;
        this.score = score;
    }

    public int getID() {
        return this.ID;
    }

    public String getStudentID() { return this.student_id; }

    public String getStudentName() { return this.student_name; }

    public String getTimeCheck() { return this.time_check; }

    public String getCourseIdSec() { return this.course_id_sec; }

    public float getScore() { return this.score; }
}
