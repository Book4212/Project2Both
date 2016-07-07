package com.example.project.studentnamereader;


public class CourseDB {
    // Table name
    public static final String table_name = "Course";
    // column name
    public static final String[] column = {"ID", "course_id", "course_name", "section", "teacher_id", "teacher_name"};
    // value of column
    private int ID;
    private String course_id;
    private String course_name;
    private String section;
    private String teacher_id;
    private String teacher_name;

    public CourseDB(int ID, String course_id, String course_name, String section, String teacher_id, String teacher_name) {
        this.ID = ID;
        this.course_id = course_id;
        this.course_name = course_name;
        this.section = section;
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
    }

    public int getID() {
        return this.ID;
    }

    public String getCourseID(){
        return this.course_id;
    }

    public String getCourseName(){
        return this.course_name;
    }

    public String getSection(){
        return this.section;
    }

    public String getTeacherID() { return this.teacher_id; }

    public String getTeacherName() { return this.teacher_name; }
}
