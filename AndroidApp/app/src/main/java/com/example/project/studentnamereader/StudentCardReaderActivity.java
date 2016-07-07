package com.example.project.studentnamereader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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

public class StudentCardReaderActivity extends Activity {

    // ตัวแปรบอกชื่อวิชาและเมนูการทำงาน
    String courseID_sec, menu;

    //ตัวแปรของ View
    private EditText SID, round;
    private TextView studentData, recommendCount, rec_act;

    private TempCountCheckDB lastCount = null;

    private SQLiteDatabase database;

    // ตัวแปรภายนอกสำหรับ class นี้
    private MediaPlayer mp;
    private AlertDialog mEnableNfc;
    private boolean stateRec = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_card_reader);

        courseID_sec = getIntent().getExtras().getString("keyCourseID_sec");
        menu = getIntent().getExtras().getString("keyMenu");
        //Log.d("print", courseID_sec);
        //Log.d("print", menu);

        // Check if there is an NFC hardware component.
        MifareReader.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        if (MifareReader.getNfcAdapter() == null) {
            createNfcEnableDialog();
        }

        // Create a new MediaPlayer to play this sound
        mp = MediaPlayer.create(this, R.raw.sound_alert);

        //เชื่อม View
        SID = (EditText) findViewById(R.id.SID);
        round = (EditText) findViewById(R.id.count);
        studentData = (TextView) findViewById(R.id.studentData);
        recommendCount = (TextView) findViewById(R.id.rec);
        rec_act = (TextView) findViewById(R.id.rec_action);

        //สร้างตัวจัดการฐานข้อมูล
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //นำตัวจัดการฐานข้อมูลมาใช้งาน
        database = dbHelper.getWritableDatabase();

        getLastCountCheckName();
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
        String temp = MifareReader.readNewTag(intent, this);
        String student_id, student_name, cTime, data;

        if(round.length() > 0) {    //ใส่ข้อมูลครั้งที่เช็คชื่อ
            int count = Integer.parseInt(round.getText().toString());

            if (temp != "0x") {
                cTime = getDateTime();
                StudentDB dataStu = getRealStudentData(temp);
                if (dataStu == null) {
                    studentData.setTextColor(Color.parseColor("#4781CC"));
                    studentData.setText("ไม่มีข้อมูลนักศึกษาคนนี้ โปรดเพิ่มข้อมูลนักศึกษาคนนี้");
                } else {
                    student_id = dataStu.getStudentID();
                    student_name = dataStu.getName();
                    addAutoTempData(student_id, student_name, cTime, count);
                    updateLastCountCheckName(courseID_sec, menu, count);
                    data = "รหัสนักศึกษา " + student_id + "\nชื่อ " + student_name + "\n" +
                            "เช็คชื่อแบบ " + translateMenu(menu) + "\n" + cTime + "\nครั้งที่ " + count;
                    studentData.setTextColor(Color.parseColor("#595353"));
                    studentData.setText(data);
                }
            }
        } else {    // ไม่ใส่ข้อมูลครั้งที่เช็คชื่อ
            Toast.makeText(this, "โปรดใส่ครั้งที่เช็คชื่อ", Toast.LENGTH_SHORT).show();
        }
        mp.start(); // play sound
    }

    //--------------------> Part of other method for display to screen

    private String translateMenu(String menu) {
        if(menu.equals("come class")) {
            return "มาเรียนทัน";
        } else if(menu.equals("come late")) {
            return "มาเรียนสาย";
        } else if(menu.equals("out class")) {
            return "ออกห้องเรียน";
        } else {
            return "เข้าร่วมกิจกรรม";
        }
    }

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

    public void getLastCountCheckName() {
        String mode = "";
        if(menu.equals("activity")) {
            mode = "activity";
        } else {
            mode = "check name";
        }
        //ทำการ Query ข้อมูลจากตาราง TempCountCheckDB ใส่ใน Cursor
        String command = "SELECT * FROM " + TempCountCheckDB.table_name +
                " WHERE " + TempCountCheckDB.column[1] + " = '" + courseID_sec + "'" +
                " AND " + TempCountCheckDB.column[2] + " = '" + mode + "'";
        Cursor mCursor = database.rawQuery(command, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            if(mCursor.getCount() > 0) {    // ถ้ามีข้อมูล
                int ID = mCursor.getInt(mCursor.getColumnIndex(TempCountCheckDB.column[0]));
                String course_id_sec = mCursor.getString(mCursor.getColumnIndex(TempCountCheckDB.column[1]));
                String menu = mCursor.getString(mCursor.getColumnIndex(TempCountCheckDB.column[2]));
                int count = mCursor.getInt(mCursor.getColumnIndex(TempCountCheckDB.column[3]));

                lastCount = new TempCountCheckDB(ID, course_id_sec, menu, count);
            } else {    // ถ้าไม่มีข้อมูล
                lastCount = null;
            }
        }
    }

    public void updateLastCountCheckName(String course_id_sec, String menu, int count) {
        String mode = "";
        if(menu.equals("activity")) {
            mode = "activity";
        } else {
            mode = "check name";
        }
        //ทำการ Query ข้อมูลจากตาราง TempCountCheckDB ใส่ใน Cursor
        String command = "SELECT * FROM " + TempCountCheckDB.table_name +
                " WHERE " + TempCountCheckDB.column[1] + " = '" + courseID_sec + "'" +
                " AND " + TempCountCheckDB.column[2] + " = '" + mode + "'";
        Cursor mCursor = database.rawQuery(command, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
            ContentValues values = new ContentValues();
            values.put(TempCountCheckDB.column[1], course_id_sec);
            values.put(TempCountCheckDB.column[2], mode);
            values.put(TempCountCheckDB.column[3], count);

            if (mCursor.getCount() == 0) {   // ไม่มีข้อมูลเดิมอยู่
                //ทำการเพิ่มข้อมูลลงไปในตาราง TempCountCheckDB
                database.insert(TempCountCheckDB.table_name, null, values);
            } else {    // มีข้อมูลเดิมอยู่
                int ID = mCursor.getInt(mCursor.getColumnIndex(TempCountCheckDB.column[0]));
                //ทำการแก้ไขข้อมูลในตาราง TempCountCheckDB
                database.update(TempCountCheckDB.table_name, values, "ID = ?", new String[] { "" + ID });
            }
        }
    }

    //Method เพิ่มข้อมูลใน SQLite อัตโนมัติ
    public void addAutoTempData(String student_id, String student_name, String time_check, int count) {
        //ทำการ Query ข้อมูลจากตาราง TempCheckNameDB ใส่ใน Cursor เพื่อดูว่านักศึกษาเช็คชื่อไปยัง
        String command = "SELECT * FROM " + TempCheckNameDB.table_name +
                " WHERE " + TempCheckNameDB.column[1] + " = '" + student_id + "'" +
                " AND " + TempCheckNameDB.column[4] + " = '" + courseID_sec + "'" +
                " AND " + TempCheckNameDB.column[5] + " = '" + menu + "'" +
                " AND " + TempCheckNameDB.column[6] + " = " + count;
        Cursor mCursor = database.rawQuery(command, null);
        mCursor.moveToFirst();

        if (mCursor.getCount() == 0) {   // ไม่มีข้อมูลเดิมอยู่
            //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
            ContentValues values = new ContentValues();
            values.put(TempCheckNameDB.column[1], student_id);
            values.put(TempCheckNameDB.column[2], student_name);
            values.put(TempCheckNameDB.column[3], time_check);
            values.put(TempCheckNameDB.column[4], courseID_sec);
            values.put(TempCheckNameDB.column[5], menu);
            values.put(TempCheckNameDB.column[6], count);

            //ทำการเพิ่มข้อมูลลงไปในตาราง TempCheckNameDB
            database.insert(TempCheckNameDB.table_name, null, values);

            Toast.makeText(this, "เช็คชื่อเรียบร้อย", Toast.LENGTH_SHORT).show();
        } else {    // มีข้อมูลเดิมอยู่
            Toast.makeText(this, "เช็คชื่อเพิ่มไม่ได้ เพราะเช็คชื่อไปแล้ว", Toast.LENGTH_SHORT).show();
        }
    }

    //Method เพิ่มข้อมูลใน SQLite เอามาจากการกรอกรหัสนักศึกษา
    public void addTempDataFromSID(String student_id, String time_check, int count) {
        //ทำการ Query ข้อมูลจากตาราง TempCheckNameDB ใส่ใน Cursor เพื่อดูว่านักศึกษาเช็คชื่อไปยัง
        String command = "SELECT * FROM " + TempCheckNameDB.table_name +
                " WHERE " + TempCheckNameDB.column[1] + " = '" + student_id + "'" +
                " AND " + TempCheckNameDB.column[4] + " = '" + courseID_sec + "'" +
                " AND " + TempCheckNameDB.column[5] + " = '" + menu + "'" +
                " AND " + TempCheckNameDB.column[6] + " = " + count;
        Cursor mCursor = database.rawQuery(command, null);
        mCursor.moveToFirst();

        if (mCursor.getCount() == 0) {   // ไม่มีข้อมูลเดิมอยู่
            //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
            ContentValues values = new ContentValues();
            values.put(TempCheckNameDB.column[1], student_id);
            values.put(TempCheckNameDB.column[2], "-");
            values.put(TempCheckNameDB.column[3], time_check);
            values.put(TempCheckNameDB.column[4], courseID_sec);
            values.put(TempCheckNameDB.column[5], menu);
            values.put(TempCheckNameDB.column[6], count);

            //ทำการเพิ่มข้อมูลลงไปในตาราง CourseDB
            database.insert(TempCheckNameDB.table_name, null, values);

            Toast.makeText(this, "เช็คชื่อเรียบร้อย", Toast.LENGTH_SHORT).show();
        } else {    // มีข้อมูลเดิมอยู่
            Toast.makeText(this, "เช็คชื่อเพิ่มไม่ได้ เพราะเช็คชื่อไปแล้ว", Toast.LENGTH_SHORT).show();
        }
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

    public void addSID(View view) {
        if (SID.length() > 0 && round.length() > 0) {    // ใส่ข้อมูลครบ
            // เอารหัสนักศึกษาจาก editText
            int count = Integer.parseInt(round.getText().toString());
            String student_id = SID.getText().toString();
            String cTime = getDateTime();
            String data = "รหัสนักศึกษา " + student_id + "\nเช็คชื่อแบบ "
                    + translateMenu(menu) + "\n" + cTime + "\nครั้งที่ " + count;
            studentData.setTextColor(Color.parseColor("#595353"));
            studentData.setText(data);
            addTempDataFromSID(student_id, cTime, count);
            updateLastCountCheckName(courseID_sec, menu, count);
        } else {    // ใส่ข้อมูลไม่ครบ
            Toast.makeText(this, "โปรดใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }
    }

    public void goShowCheckNameActivity(View view) {
        Intent intent = new Intent(this, ShowCheckNameActivity.class);
        intent.putExtra("keyCourseID_sec", courseID_sec);
        intent.putExtra("keyMenu", menu);
        startActivity(intent);
    }

    public void showRecommendCount(View view) {
        String rec;
        if(stateRec) {
            stateRec = false;
            rec_act.setText("[ แสดง ]");
            recommendCount.setText("");
        } else {
            stateRec = true;
            rec_act.setText("[ ซ่อน ]");
            if(menu.equals("activity")) {
                rec = "*ครั้งที่* ของกิจกรรมที่ต้องการเช็คชื่อในเว็บคือ ค่าที่ใส่ใน *ครั้งที่เช็คชื่อ*\n";
                if(lastCount == null) { // ไม่เคยเช็คชื่อมาก่อน
                    rec += "อาจารย์ไม่เคยเช็คชื่อเข้าร่วมกิจกรรมของวิชานี้มาก่อน";
                } else {    // เคยเช็คชื่อมาก่อน
                    rec += "อาจารย์เคยเช็คชื่อเข้าร่วมกิจกรรมของวิชานี้มาแล้ว " + lastCount.getCountCheck() + " ครั้ง";
                }
            } else {
                if(lastCount == null) {
                    rec = "อาจารย์ไม่เคยเช็คชื่อนักศึกษาที่เรียนวิชานี้มาก่อน";
                } else {
                    rec = "อาจารย์เคยเช็คชื่อนักศึกษาที่เรียนวิชานี้มาแล้ว " + lastCount.getCountCheck() + " ครั้ง";
                }
            }
            recommendCount.setText(rec);
        }
    }
}