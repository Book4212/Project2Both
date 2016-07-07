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
import android.widget.Toast;

public class LoginActivity extends Activity {

    //ตัวแปรของ View
    private EditText teacherID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //เชื่อม View
        teacherID = (EditText) findViewById(R.id.teacherID);
    }

    public void goIndexActivity(View view) {
        if(teacherID.length() > 0) {    // ใส่ข้อมูลครบ
            Intent intent = new Intent(this, IndexActivity.class);
            intent.putExtra("LoginSend", teacherID.getText().toString());
            teacherID.setText("");
            startActivity(intent);
        } else {    // ใส่ข้อมูลไม่ครบ
            Toast.makeText(this, "โปรดกรอกรหัสอาจารย์", Toast.LENGTH_SHORT).show();
        }
    }
}
