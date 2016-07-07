package com.example.project.studentnamereader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GiveScoreActivity extends Activity {

    // ตัวแปรบอกชื่อวิชา
    String courseID_sec;

    //ตัวแปรของ View
    private EditText SID, score;
    private TextView studentData;

    private SQLiteDatabase database;

    // ตัวแปรภายนอกสำหรับ class นี้
    private MediaPlayer mp;
    private AlertDialog mEnableNfc;
    private String student_id, student_name, cTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_score);

        courseID_sec = getIntent().getExtras().getString("keyCourseID_sec");

        // Check if there is an NFC hardware component.
        MifareReader.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        if (MifareReader.getNfcAdapter() == null) {
            createNfcEnableDialog();
        }

        // Create a new MediaPlayer to play this sound
        mp = MediaPlayer.create(this, R.raw.sound_alert);

        //เชื่อม View
        SID = (EditText) findViewById(R.id.SID);
        score = (EditText) findViewById(R.id.score);
        studentData = (TextView) findViewById(R.id.studentData);

        //สร้างตัวจัดการฐานข้อมูล
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //นำตัวจัดการฐานข้อมูลมาใช้งาน
        database = dbHelper.getWritableDatabase();

    }

    @Override
    public void onResume() {
        //Log.d("print", "function onResume in ComeClassActivity.java");
        super.onResume();
        MifareReader.enableNfc(this);
        checkNfc();
    }

    //---------------> Part of Mifare reader

    @Override
    public void onPause() {
        //Log.d("print", "function onPause in ComeClassActivity.java");
        super.onPause();
        MifareReader.disableNfc(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        //Log.d("print", "function onNewIntent in ComeClassActivity.java");
        String temp = MifareReader.readNewTag(intent, this);
        String data;
        if (temp != "0x") {

            StudentDB dataStu = getRealStudentData(temp);
            if (dataStu == null) {
                studentData.setTextColor(Color.parseColor("#4781CC"));
                studentData.setText("ไม่มีข้อมูลนักศึกษาคนนี้ โปรดเพิ่มข้อมูลนักศึกษาคนนี้");
            } else {
                student_id = dataStu.getStudentID();
                student_name = dataStu.getName();
                cTime = getDateTime();
                data = student_id + " " + student_name + "\n" + cTime;
                studentData.setTextColor(Color.parseColor("#595353"));
                studentData.setText(data);
            }
            // Log.d("print", date);
            mp.start(); // play sound
        }
    }

    //--------------------> Part of other method for display to screen

    private String getDateTime() {
        // format วันที่ 12/02/2559 เวลา 15:30:15   dd/MM/yyyy HH:mm:ss
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("วันที่ dd/MM/yyyy เวลา HH:mm:ss");
        return sdf.format(today.getTime());
    }

    private void checkNfc() {
        // Check if the NFC hardware is enabled.
        if (MifareReader.getNfcAdapter() != null && !MifareReader.getNfcAdapter().isEnabled()) {
            // NFC is disabled.
            //  Show dialog.
            if (mEnableNfc == null) {
                createNfcEnableDialog();
            }
            mEnableNfc.show();

        } else {
            MifareReader.readNewTag(getIntent(), this);
            if (mEnableNfc == null) {
                createNfcEnableDialog();
            }
            mEnableNfc.hide();
        }
    }

    //-----------------> Part of alert dialog

    private void createNfcEnableDialog() {
        mEnableNfc = new AlertDialog.Builder(this)
                .setTitle("ไม่ได้เปิดใช้งาน NFC")
                .setMessage("อุปกรณ์ NFC ไม่ได้เปิดใช้งาน โปรดไปที่ การตั้งค่า และเปิดใช้งาน")
                .setIcon(android.R.drawable.ic_secure)
                .setPositiveButton("ไปที่ การตั้งค่า NFC",
                        new DialogInterface.OnClickListener() {
                            @Override
                            @SuppressLint("InlinedApi")
                            public void onClick(DialogInterface dialog, int which) {
                                // Goto NFC Settings.
                                if (Build.VERSION.SDK_INT >= 16) {
                                    startActivity(new Intent(
                                            Settings.ACTION_NFC_SETTINGS));
                                } else {
                                    startActivity(new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            }
                        })
                .setNeutralButton("ไม่ใช้งาน NFC",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Do not something
                            }
                        }).show();
    }

    //Method เพิ่มข้อมูลใน SQLite
    public void addTempScore(String student_id, String student_name, String time_check, float score) {
        // TODO Auto-generated method stub

        //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
        ContentValues values = new ContentValues();
        values.put(TempStudentScoreDB.column[1], student_id);
        values.put(TempStudentScoreDB.column[2], student_name);
        values.put(TempStudentScoreDB.column[3], time_check);
        values.put(TempStudentScoreDB.column[4], courseID_sec);
        values.put(TempStudentScoreDB.column[5], score);

        //ทำการเพิ่มข้อมูลลงไปในตาราง TempStudentScore
        database.insert(TempStudentScoreDB.table_name, null, values);

        Toast.makeText(this, "ให้คะแนนจิตพิสัยเรียบร้อย", Toast.LENGTH_SHORT).show();
    }

    public StudentDB getRealStudentData(String UID) {
        //ทำการ Query ข้อมูลจากตาราง Student ใส่ใน Cursor
        String command = "SELECT * FROM " + StudentDB.table_name +
                " WHERE " + StudentDB.column[2] + " = '" + UID + "'";
        Cursor mCursor = database.rawQuery(command, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            if (mCursor.getCount() > 0) {    // มีข้อมูลในตาราง
                int ID = mCursor.getInt(mCursor.getColumnIndex(StudentDB.column[0]));
                String student_id = mCursor.getString(mCursor.getColumnIndex(StudentDB.column[1]));
                String card_id = mCursor.getString(mCursor.getColumnIndex(StudentDB.column[2]));
                String name = mCursor.getString(mCursor.getColumnIndex(StudentDB.column[3]));

                return new StudentDB(ID, student_id, card_id, name);
            }
        }
        return null;
    }

    // เพิ่มคะแนนจิตพิสัยของนักศึกษา
    public void addScore(View view) {
        if (score.length() > 0) {   // ใส่คะแนน
            float scoreStu = Float.parseFloat(score.getText().toString());
            switch(view.getId()) {
                case R.id.plusScore:
                    break;
                default:
                    scoreStu *= -1;
                    break;
            }

            if (SID.length() > 0) { // กรอกรหัสนักศึกษา
                // เอารหัสนักศึกษาจาก editText
                String student_idE = SID.getText().toString();
                String cTimeE = getDateTime();
                addTempScore(student_idE, "-", cTimeE, scoreStu);
                SID.setText("");
            } else {    // สแกนบัตรนักศึกษา
                if (student_id != null) {
                    studentData.setTextColor(Color.parseColor("#595353"));
                    studentData.setText("");
                    if (student_id != "ไม่มีข้อมูลนักศึกษาคนนี้ โปรดเพิ่มข้อมูลนักศึกษาคนนี้") {
                        addTempScore(student_id, student_name, cTime, scoreStu);
                    }
                    student_id = null;
                    student_name = null;
                    cTime = null;
                } else {
                    studentData.setTextColor(Color.parseColor("#595353"));
                    studentData.setText("");
                    Toast.makeText(this, "ใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                }
            }
            score.setText("");
        } else {    // ใส่ข้อมูลไม่ครบ
            Toast.makeText(this, "ใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }
    }

    public void goShowScoreActivity(View view) {
        Intent intent = new Intent(this, ShowScoreActivity.class);
        intent.putExtra("keyCourseID_sec", courseID_sec);
        startActivity(intent);
    }
}
