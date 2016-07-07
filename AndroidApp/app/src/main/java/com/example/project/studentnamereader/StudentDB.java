package com.example.project.studentnamereader;

public class StudentDB {
    // Table name
    public static final String table_name = "Student";
    // column name
    public static final String[] column = {"ID", "student_id", "card_id", "name"};
    // value of column
    private int ID;
    private String student_id;
    private String card_id;
    private String name;

    public StudentDB(int ID, String student_id, String card_id, String name) {
        this.ID = ID;
        this.student_id = student_id;
        this.card_id = card_id;
        this.name = name;
    }

    public int getID() {
        return this.ID;
    }

    public String getStudentID(){
        return this.student_id;
    }

    public String getCardID(){
        return this.card_id;
    }

    public String getName(){
        return this.name;
    }
}
