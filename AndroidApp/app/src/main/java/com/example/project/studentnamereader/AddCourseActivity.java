package com.example.project.studentnamereader;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddCourseActivity extends Activity {

    //ตัวแปรของ View
    private EditText course_id, course_name, sec;
    //ตัวแปรที่บอกรหัสอาจารย์
    private TextView teacher_id;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        //เชื่อม View
        course_id = (EditText) findViewById(R.id.course_id);
        course_name = (EditText) findViewById(R.id.course_name);
        sec = (EditText) findViewById(R.id.sec);
        teacher_id = (TextView) findViewById(R.id.teacher_id);
        teacher_id.setText(getIntent().getExtras().getString("keyTeacher_id"));

        //สร้างตัวจัดการฐานข้อมูล
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //นำตัวจัดการฐานข้อมูลมาใช้งาน
        database = dbHelper.getWritableDatabase();

    }

    //Method เพิ่มข้อมูลใน SQLite
    public void addCourse(View view) {
        // TODO Auto-generated method stub
        if(course_id.length() > 0 && course_name.length() > 0 && sec.length() > 0) {    // ใส่ข้อมูลครบ

            String course_id_input = course_id.getText().toString();
            String course_name_input = course_name.getText().toString();
            String sec_input = sec.getText().toString();

            //ทำการ Query ข้อมูลจากตาราง CourseDB ใส่ใน Cursor เพื่อดูว่าข้อมูลนี้มีอยู่หรือไม่
            String command = "SELECT * FROM " + CourseDB.table_name +
                    " WHERE " + CourseDB.column[1] + " = '" + course_id_input + "' AND " +
                    CourseDB.column[3] + " = '" + sec_input + "'";
            Cursor mCursor = database.rawQuery(command, null);
            mCursor.moveToFirst();

            if(mCursor.getCount() == 0) {   // ไม่มีข้อมูลเดิมอยู่
                //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
                ContentValues values = new ContentValues();
                values.put(CourseDB.column[1], course_id_input);
                values.put(CourseDB.column[2], course_name_input);
                values.put(CourseDB.column[3], sec_input);
                values.put(CourseDB.column[4], teacher_id.getText().toString());
                values.put(CourseDB.column[5], "");

                //ทำการเพิ่มข้อมูลลงไปในตาราง CourseDB
                database.insert(CourseDB.table_name, null, values);

                Toast.makeText(this, "เพิ่มข้อมูลวิชาที่สอนเรียบร้อย", Toast.LENGTH_SHORT).show();
            } else {    // มีข้อมูลเดิมอยู่
                Toast.makeText(this, "เพิ่มข้อมูลวิชาที่สอนนี้ไม่ได้ เพราะมีข้อมูลนี้อยู่แล้ว", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent();

            //ตั้งค่าผลลัพธ์การทำงานว่า RESULT_OK
            setResult(RESULT_OK, intent);

            finish();   // Exit AddCourseActivity

        } else {    // ใส่ข้อมูลไม่ครบ
            Toast.makeText(this, "โปรดใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }
    }
}
