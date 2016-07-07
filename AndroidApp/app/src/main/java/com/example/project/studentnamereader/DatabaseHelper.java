package com.example.project.studentnamereader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "Application_DB.db";
    private static final int DB_VERSION = 1;

    //คำสั่งในการสร้าง ตารางสำหรับเก็บข้อมูล
    private static final String CREATE_TABLE1 =
            "CREATE TABLE " + CourseDB.table_name + " (" +
                    CourseDB.column[0] + " INTEGER PRIMARY KEY, " +
                    CourseDB.column[1] + " TEXT NOT NULL, " +
                    CourseDB.column[2] + " TEXT NOT NULL, " +
                    CourseDB.column[3] + " TEXT NOT NULL, " +
                    CourseDB.column[4] + " TEXT NOT NULL, " +
                    CourseDB.column[5] + " TEXT NOT NULL)";

    private static final String CREATE_TABLE2 =
            "CREATE TABLE " + StudentDB.table_name + " (" +
                    StudentDB.column[0] + " INTEGER PRIMARY KEY, " +
                    StudentDB.column[1] + " TEXT NOT NULL, " +
                    StudentDB.column[2] + " TEXT NOT NULL, " +
                    StudentDB.column[3] + " TEXT NOT NULL)";

    private static final String CREATE_TABLE3 =
            "CREATE TABLE " + TempCheckNameDB.table_name + " (" +
                    TempCheckNameDB.column[0] + " INTEGER PRIMARY KEY, " +
                    TempCheckNameDB.column[1] + " TEXT NOT NULL, " +
                    TempCheckNameDB.column[2] + " TEXT NOT NULL, " +
                    TempCheckNameDB.column[3] + " TEXT NOT NULL, " +
                    TempCheckNameDB.column[4] + " TEXT NOT NULL, " +
                    TempCheckNameDB.column[5] + " TEXT NOT NULL, " +
                    TempCheckNameDB.column[6] + " INTEGER NOT NULL)";

    private static final String CREATE_TABLE4 =
            "CREATE TABLE " + TempStudentScoreDB.table_name + " (" +
                    TempStudentScoreDB.column[0] + " INTEGER PRIMARY KEY, " +
                    TempStudentScoreDB.column[1] + " TEXT NOT NULL, " +
                    TempStudentScoreDB.column[2] + " TEXT NOT NULL, " +
                    TempStudentScoreDB.column[3] + " TEXT NOT NULL, " +
                    TempStudentScoreDB.column[4] + " TEXT NOT NULL, " +
                    TempStudentScoreDB.column[5] + " REAL NOT NULL)";

    private static final String CREATE_TABLE5 =
            "CREATE TABLE " + TempCountCheckDB.table_name + " (" +
                    TempCountCheckDB.column[0] + " INTEGER PRIMARY KEY, " +
                    TempCountCheckDB.column[1] + " TEXT NOT NULL, " +
                    TempCountCheckDB.column[2] + " TEXT NOT NULL, " +
                    TempCountCheckDB.column[3] + " INTEGER NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        db.execSQL(CREATE_TABLE4);
        db.execSQL(CREATE_TABLE5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //พิมพ์ Log เพื่อให้เห็นว่ามีการ Upgrade Database
        Log.d("print",
                "Upgrade database version from version" + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        //ลบตาราง member ของเก่าทิ้ง
        db.execSQL("DROP TABLE IF EXISTS " + CourseDB.table_name);
        db.execSQL("DROP TABLE IF EXISTS " + StudentDB.table_name);
        db.execSQL("DROP TABLE IF EXISTS " + TempCheckNameDB.table_name);
        db.execSQL("DROP TABLE IF EXISTS " + TempStudentScoreDB.table_name);
        db.execSQL("DROP TABLE IF EXISTS " + TempCountCheckDB.table_name);
        onCreate(db);
    }
}