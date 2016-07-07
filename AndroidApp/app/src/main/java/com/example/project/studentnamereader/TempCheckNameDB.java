package com.example.project.studentnamereader;


public class TempCheckNameDB {
    // Table name
    public static final String table_name = "Temp_Check_Name";
    // column name
    public static final String[] column = { "ID", "student_id", "student_name", "time_check", "course_id_sec", "menu", "count" };
    // value of column
    private int ID;
    private String student_id;
    private String student_name;
    private String time_check;
    private String course_id_sec;
    private String menu;
    private int count;

    public TempCheckNameDB(int ID, String student_id, String student_name, String time_check, String course_id_sec, String menu, int count) {
        this.ID = ID;
        this.student_id = student_id;
        this.student_name = student_name;
        this.time_check = time_check;
        this.course_id_sec = course_id_sec;
        this.menu = menu;
        this.count = count;
    }

    public int getID() {
        return this.ID;
    }

    public String getStudentID(){
        return this.student_id;
    }

    public String getStudentName(){
        return this.student_name;
    }

    public String getTimeCheck(){
        return this.time_check;
    }

    public String getCourseIdSec(){
        return this.course_id_sec;
    }

    public String getMenu() { return this.menu; }

    public int getCountCheck() {
        return this.count;
    }
}
