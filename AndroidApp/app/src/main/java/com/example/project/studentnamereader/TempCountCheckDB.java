package com.example.project.studentnamereader;

public class TempCountCheckDB {
    // Table name
    public static final String table_name = "Temp_Count_Check";
    // column name
    public static final String[] column = { "ID", "course_id_sec", "menu", "count" };
    // value of column
    private int ID;
    private String course_id_sec;
    private String menu;
    private int count;

    public TempCountCheckDB(int ID, String course_id_sec, String menu, int count) {
        this.ID = ID;
        this.course_id_sec = course_id_sec;
        this.menu = menu;
        this.count = count;
    }

    public int getID() {
        return this.ID;
    }

    public String getCourseIdSec(){
        return this.course_id_sec;
    }

    public String getMenu() { return this.menu; }

    public int getCountCheck() {
        return this.count;
    }
}
