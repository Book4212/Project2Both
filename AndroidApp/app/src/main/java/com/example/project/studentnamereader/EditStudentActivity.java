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

public class EditStudentActivity extends Activity {

    String oldSID;

    //ตัวแปรของ View
    private EditText editName, editLastName, editSID;
    private TextView editUID;

    //ตัวแปรไว้เก็บว่าข้อมูลที่จะแก้ไข id เป็นอะไร
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        //เชื่อม View
        editName = (EditText) findViewById(R.id.editName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editSID = (EditText) findViewById(R.id.editSID);
        editUID = (TextView) findViewById(R.id.editUID);

        //รับค่าจาก ShowStudentActivity มาแสดงข้อมูลเพื่อทำการแก้ไข
        ID = getIntent().getExtras().getInt("keyID");
        editSID.setText(getIntent().getExtras().getString("keyStudentID"));
        editUID.setText(getIntent().getExtras().getString("keyCardID"));
        editName.setText(getIntent().getExtras().getString("keyName"));
        editLastName.setText(getIntent().getExtras().getString("keyLastName"));
        oldSID = getIntent().getExtras().getString("keyStudentID");
        //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

    }

    public void editStudentData(View view) {
        Intent intent = new Intent();

        //ตั้งค่าผลลัพธ์การทำงานว่า RESULT_OK
        setResult(RESULT_OK, intent);

        if(editSID.length() > 0 && editName.length() > 0 && editLastName.length() > 0) {

            //ส่งข้อมูลกลับไปให้ IndexActivity ทำการแก้ไขข้อมูล
            intent.putExtra("keyID", ID);
            intent.putExtra("keyStudentID", editSID.getText().toString());
            intent.putExtra("keyCardID", editUID.getText().toString());
            intent.putExtra("keyName", editName.getText().toString());
            intent.putExtra("keyLastName", editLastName.getText().toString());
            intent.putExtra("keyOldStudentID", oldSID);
            //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

            finish();

        } else {
            Toast.makeText(this, "โปรดใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }
    }

}
