package com.example.project.studentnamereader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddStudentActivity extends Activity {

    //ตัวแปรของ View
    private EditText name, lastName, SID;
    private TextView addUID;

    private SQLiteDatabase database;

    // ตัวแปรภายนอกสำหรับ class นี้
    private MediaPlayer mp;
    private AlertDialog mEnableNfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Check if there is an NFC hardware component.
        MifareReader.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        if (MifareReader.getNfcAdapter() == null) {
            createNfcEnableDialog();
        }

        // Create a new MediaPlayer to play this sound
        mp = MediaPlayer.create(this, R.raw.sound_alert);

        //เชื่อม View
        name = (EditText) findViewById(R.id.addName);
        lastName = (EditText) findViewById(R.id.addLastName);
        SID = (EditText) findViewById(R.id.addSID);
        addUID = (TextView) findViewById(R.id.addUID);

        //สร้างตัวจัดการฐานข้อมูล
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //นำตัวจัดการฐานข้อมูลมาใช้งาน
        database = dbHelper.getWritableDatabase();
    }

    //Method เพิ่มข้อมูลใน SQLite
    public void addStudent(View view) {
        // TODO Auto-generated method stub
        if(name.length() > 0 && lastName.length() > 0 && SID.length() > 0 && addUID.length() > 0) {    // ใส่ข้อมูลครบ

            String card_id = addUID.getText().toString();

            //ทำการ Query ข้อมูลจากตาราง CourseDB ใส่ใน Cursor เพื่อดูว่าข้อมูลนี้มีอยู่หรือไม่
            String command = "SELECT * FROM " + StudentDB.table_name +
                    " WHERE " + StudentDB.column[2] + " = '" + card_id + "'";
            Cursor mCursor = database.rawQuery(command, null);
            mCursor.moveToFirst();

            if(mCursor.getCount() == 0) {   // ไม่มีข้อมูลเดิมอยู่
                //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
                String nameStudent = name.getText().toString() + " " + lastName.getText().toString();
                ContentValues values = new ContentValues();
                values.put(StudentDB.column[1], SID.getText().toString());
                values.put(StudentDB.column[2], card_id);
                values.put(StudentDB.column[3], nameStudent);

                //ทำการเพิ่มข้อมูลลงไปในตาราง CourseDB
                database.insert(StudentDB.table_name, null, values);

                Toast.makeText(this, "เพิ่มข้อมูลนักศึกษาเรียบร้อย", Toast.LENGTH_SHORT).show();
            } else {    // มีข้อมูลเดิมอยู่
                Toast.makeText(this, "เพิ่มข้อมูลนักศึกษานี้ไม่ได้ เพราะมีข้อมูลนี้อยู่แล้ว", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent();

            //ตั้งค่าผลลัพธ์การทำงานว่า RESULT_OK
            setResult(RESULT_OK, intent);

        } else {    // ใส่ข้อมูลไม่ครบ
            Toast.makeText(this, "โปรดใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }
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
        if (temp != "0x") {
            //Log.d("print", uid);
            mp.start(); // play sound
            showUIDToScreen(temp);
        }
    }

    //--------------------> Part of other method for display to screen

    private void showUIDToScreen(String data) {
        //setContentView(R.layout.activity_add_student);
        //TextView textView = (TextView) findViewById(R.id.addUID);
        addUID.setTextSize(20);
        addUID.setText(data);
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
                .setNeutralButton("ออกจากหน้านี้",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Exit AddStudentActivity
                                finish();
                            }
                        }).show();
    }

    public void goShowStudent(View view) {
        Intent intent = new Intent(this, ShowStudentActivity.class);
        startActivity(intent);
    }
}
