package com.example.project.studentnamereader;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.IDN;
import java.util.ArrayList;

public class ShowStudentActivity extends AppCompatActivity {

    //ตัวแปรของ View
    private ListView listStudent;
    private EditText search;

    //list ในการเก็บข้อมูลของ MemberData
    private ArrayList<StudentDB> listData = new ArrayList<StudentDB>();
    private ArrayList<StudentDB> listSearch = new ArrayList<StudentDB>();

    private SQLiteDatabase database;

    private AlertDialog confirm_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);

        //เชื่อม View
        listStudent = (ListView) findViewById(R.id.listStudent);
        search = (EditText) findViewById(R.id.search);

        //สร้างตัวจัดการฐานข้อมูล
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //นำตัวจัดการฐานข้อมูลมาใช้งาน
        database = dbHelper.getWritableDatabase();

        //แสดงรายการสมาชิก
        showListStudent();

    }

    private void showListStudent() {
        //ดึงข้อมูลสมาชิกจาก SQLite Database
        getStudent();

        //แสดงสมาชิกใน ListView
        listStudent.setAdapter(new AdapterListStudent(this, listData));
    }

    private void showListSearch() {
        //แสดงสมาชิกใน ListView
        listStudent.setAdapter(new AdapterListStudent(this, listSearch));
    }

    //Method ดึงข้อมูลจาก SQLite
    private void getStudent() {
        //ทำการ Query ข้อมูลจากตาราง Student ใส่ใน Cursor
        String command = "SELECT * FROM " + StudentDB.table_name;
        Cursor mCursor = database.rawQuery(command, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            listData.clear();
            //ถ้ามีข้อมูลจะทำการเก็บข้อมูลใส่ List เพื่อนำไปแสดง
            if(mCursor.getCount() > 0) {
                do {
                    int ID = mCursor.getInt(mCursor.getColumnIndex(StudentDB.column[0]));
                    String student_id = mCursor.getString(mCursor.getColumnIndex(StudentDB.column[1]));
                    String card_id = mCursor.getString(mCursor.getColumnIndex(StudentDB.column[2]));
                    String name = mCursor.getString(mCursor.getColumnIndex(StudentDB.column[3]));

                    listData.add(new StudentDB(ID, student_id, card_id, name));
                } while (mCursor.moveToNext());
            }
        }
    }

    //Method แก้ไขข้อมูลใน SQLite
    public void editStudent(int ID, String SID, String card_id, String name, String oldSID) {
        //เตรียมค่าต่างๆ เพื่อทำการแก้ไข
        ContentValues values = new ContentValues();
        values.put(StudentDB.column[0], ID);
        values.put(StudentDB.column[1], SID);
        values.put(StudentDB.column[2], card_id);
        values.put(StudentDB.column[3], name);

        ContentValues value2 = new ContentValues();
        value2.put(TempCheckNameDB.column[1], SID);
        value2.put(TempCheckNameDB.column[2], name);

        //ให้ Database ทำการแก้ไขข้อมูลที่ id ที่กำหนด
        database.update(StudentDB.table_name, values, "ID = ?", new String[] { "" + ID });
        database.update(TempCheckNameDB.table_name, value2, "student_id = '" + oldSID + "'", null);
        database.update(TempStudentScoreDB.table_name, value2, "student_id = '" + oldSID + "'", null);

        Toast.makeText(this, "แก้ไขข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();

        //แสดงข้อมูลล่าสุด
        showListStudent();
    }

    public void showEditStudent(int ID, String student_id, String card_id, String name, String lastName) {
        Intent intent = new Intent(this, EditStudentActivity.class);

        //ทำการส่งค่าต่างๆ ให้ EditStudentActivity ไปทำการแก้ไข
        intent.putExtra("keyID", ID);
        intent.putExtra("keyStudentID", student_id);
        intent.putExtra("keyCardID", card_id);
        intent.putExtra("keyName", name);
        intent.putExtra("keyLastName", lastName);
        //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

        //ทำการเรียก EditStudentActivity โดยให้ Request Code เป็น 1
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //ถ้ากลับมาหน้า ShowStudentActivity แล้วผลลัพธ์การทำงานสมบูรณ์
        if(requestCode == 1 && resultCode == RESULT_OK) {
            try {
                //เก็บค่าที่ส่งกลับมาใส่ตัวแปร
                int ID = intent.getExtras().getInt("keyID");
                String SID = intent.getExtras().getString("keyStudentID");
                String card_id = intent.getExtras().getString("keyCardID");
                String name = intent.getExtras().getString("keyName");
                String lastName = intent.getExtras().getString("keyLastName");
                String oldSID = intent.getExtras().getString("keyOldStudentID");
                //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

                //ให้แก้ไขข้อมูลสมาชิก
                editStudent(ID, SID, card_id, name + " " + lastName, oldSID);
            } catch (NullPointerException except) {
                //Log.d("print", except.getMessage());
            }
        }
    }

    //Method ลบข้อมูลใน SQLite
    public void deleteStudent(int ID, String SID) {
        database.delete(StudentDB.table_name, "ID = " + ID, null);
        database.delete(TempCheckNameDB.table_name, "student_id = '" + SID + "'", null);
        database.delete(TempStudentScoreDB.table_name, "student_id = '" + SID + "'", null);

        Toast.makeText(this, "ลบข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();

        showListStudent();
    }

    public void searchStudentID(View view) {
        listSearch.clear();
        String compare = "", studentID, searchID;
        int startChar = 0;
        if(search.length() > 0) {   // ใส่รหัสวิชามา
            searchID = search.getText().toString();

            for(int i = 0; i < listData.size(); i++) {  // วนทุกรหัสนักศึกษา
                studentID = listData.get(i).getStudentID();
                if(searchID.length() > studentID.length()) {
                    continue;
                }
                for(int j = 0; j < studentID.length(); j++) {    // วนทุกคัวอักษร
                    for (int k = 0; k < searchID.length(); k++) {   // วนจนครบตามจำนวนตัวอักษรที่ใส่มาค้นหา
                        compare += studentID.charAt(k + startChar);
                    }
                    if (searchID.equals(compare)) { // เปรียบเทียบพบว่าเหมือนกัน
                        listSearch.add(listData.get(i));
                        break;
                    }
                    startChar++;
                    if ((startChar + searchID.length()) > studentID.length()) {  // ถ้าเกินจำนวนตัวอักษรในรหัสที่จะเพิ่มได้
                        break;
                    }
                    compare = "";
                }
                startChar = 0;
                compare = "";
            }

            showListSearch();
        }
    }

    public void showConfirmDeleteDialog(final int ID, final String SID) {
        confirm_del = new AlertDialog.Builder(this)
                .setTitle("คุณต้องการลบข้อมูลนี้")
                .setMessage("คุณต้องการลบข้อมูลนี้ ใช่หรือไม่")
                .setIcon(android.R.drawable.ic_secure)
                .setPositiveButton("ใช่",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // delete data
                                deleteStudent(ID, SID);
                            }
                        })
                .setNeutralButton("ไม่ใช่",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Do not something
                            }
                        }).show();
    }

}
