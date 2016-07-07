package com.example.project.studentnamereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditCourseActivity extends Activity {

    //ตัวแปรของ View
    private EditText course_id, course_name, sec;
    //ตัวแปรที่บอกรหัสอาจารย์
    private TextView teacher_id;
    private String course_id_sec;

    //ตัวแปรไว้เก็บว่าข้อมูลที่จะแก้ไข id เป็นอะไร
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        //เชื่อม View
        course_id = (EditText) findViewById(R.id.course_id);
        course_name = (EditText) findViewById(R.id.course_name);
        sec = (EditText) findViewById(R.id.sec);
        teacher_id = (TextView) findViewById(R.id.teacher_id);

        //รับค่าจาก IndexActivity มาแสดงข้อมูลเพื่อทำการแก้ไข
        ID = getIntent().getExtras().getInt("keyID");
        course_id.setText(getIntent().getExtras().getString("keyCourseID"));
        course_name.setText(getIntent().getExtras().getString("keyCourseName"));
        sec.setText(getIntent().getExtras().getString("keySec"));
        teacher_id.setText(getIntent().getExtras().getString("keyTeacherID"));
        course_id_sec = getIntent().getExtras().getString("course_id_sec");
        //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//
    }

    public void editCourseData(View view) {
        Intent intent = new Intent();

        //ตั้งค่าผลลัพธ์การทำงานว่า RESULT_OK
        setResult(RESULT_OK, intent);

        if(course_id.length() > 0 && course_name.length() > 0 && sec.length() > 0) {

            //ส่งข้อมูลกลับไปให้ IndexActivity ทำการแก้ไขข้อมูล
            intent.putExtra("keyID", ID);
            intent.putExtra("keyCourseID", course_id.getText().toString());
            intent.putExtra("keyCourseName", course_name.getText().toString());
            intent.putExtra("keySec", sec.getText().toString());
            intent.putExtra("keyTeacherID", teacher_id.getText().toString());
            intent.putExtra("course_id_sec", course_id_sec);
            //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

            finish();

        } else {
            Toast.makeText(this, "โปรดใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }
    }

}
