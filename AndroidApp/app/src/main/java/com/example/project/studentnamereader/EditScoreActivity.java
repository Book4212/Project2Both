package com.example.project.studentnamereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditScoreActivity extends Activity {

    //ตัวแปรของ View
    private EditText editScore;

    //ตัวแปรไว้เก็บว่าข้อมูลที่จะแก้ไข id เป็นอะไร
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_score);

        //เชื่อม View
        editScore = (EditText) findViewById(R.id.editScore);
        TextView studentData = (TextView) findViewById(R.id.studentData);

        //รับค่าจาก ShowScoreActivity มาแสดงข้อมูลเพื่อทำการแก้ไข
        ID = getIntent().getExtras().getInt("keyID");
        String student_id = getIntent().getExtras().getString("keyStudentID");
        String student_name = getIntent().getExtras().getString("keyStudentName");
        String time_check = getIntent().getExtras().getString("keyTimeCheck");

        editScore.setText("" + getIntent().getExtras().getFloat("keyScore"));
        String dataText = student_id + "\n" + student_name + "\n" + time_check;
        studentData.setText(dataText);
        //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

    }

    public void editScoreData(View view) {
        Intent intent = new Intent();

        //ตั้งค่าผลลัพธ์การทำงานว่า RESULT_OK
        setResult(RESULT_OK, intent);

        if(editScore.length() > 0) {
            float scoreStu = Float.parseFloat(editScore.getText().toString());
            switch(view.getId()) {
                case R.id.plusScore:
                    break;
                default:
                    scoreStu *= -1;
                    break;
            }

            //ส่งข้อมูลกลับไปให้ ShowScoreActivity ทำการแก้ไขข้อมูล
            intent.putExtra("keyID", ID);
            intent.putExtra("keyScore", scoreStu);
            //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

            finish();

        } else {
            Toast.makeText(this, "โปรดใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }
    }

}
