package com.example.project.studentnamereader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class IndexActivity extends Activity {

    //ตัวแปรของ View
    private ListView listCourse;
    private EditText search;
    private TextView textState;

    //list ในการเก็บข้อมูล
    private ArrayList<CourseDB> listData = new ArrayList<CourseDB>();
    private ArrayList<CourseDB> listSearch = new ArrayList<CourseDB>();

    //ตัวจัดการ database
    private SQLiteDatabase database;

    private AlertDialog confirm_del;

    private String teacherID = "", courseData = "", studentData = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //เชื่อม View
        listCourse = (ListView) findViewById(R.id.listCourse);
        search = (EditText) findViewById(R.id.search);
        textState = (TextView) findViewById(R.id.textState);

        //สร้างตัวจัดการฐานข้อมูล
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //นำตัวจัดการฐานข้อมูลมาใช้งาน
        database = dbHelper.getWritableDatabase();

        teacherID = getIntent().getExtras().getString("LoginSend");

        //แสดงรายการสมาชิก
        showListCourse();
        // part of receive data from server
        String stringUrl = "http://192.168.1.35/getTeacherPrivateData";
        textState.setText("กำลังอัพเดตข้อมูลส่วนตัว");
        if(checkConnectNetwork()) {
            //Log.d("print", "continue connect network");
            new getDataFromServer().execute(stringUrl);
        }
    }

    // ------------------------> Part of Database

    public void addCourse() {
        String[] courses = courseData.split("@");
        //Log.d("print", "len courses = " + courses.length);
        for(int i = 0; i < courses.length; i++) {
            String[] course_att = courses[i].split("!");
            //Log.d("print", "len courses_att = " + course_att.length);

            //ทำการ Query ข้อมูลจากตาราง CourseDB ใส่ใน Cursor เพื่อดูว่าข้อมูลนี้มีอยู่หรือไม่
            String command = "SELECT * FROM " + CourseDB.table_name +
                    " WHERE " + CourseDB.column[1] + " = '" + course_att[0] + "' AND " +
                    CourseDB.column[3] + " = '" + course_att[2] + "'";
            Cursor mCursor = database.rawQuery(command, null);
            if (mCursor != null) {
                mCursor.moveToFirst();

                if (mCursor.getCount() == 0) {   // ไม่มีข้อมูลเดิมอยู่
                    //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
                    ContentValues values = new ContentValues();
                    values.put(CourseDB.column[1], course_att[0]);
                    values.put(CourseDB.column[2], course_att[1]);
                    values.put(CourseDB.column[3], course_att[2]);
                    values.put(CourseDB.column[4], course_att[3]);
                    values.put(CourseDB.column[5], course_att[4]);

                    //ทำการเพิ่มข้อมูลลงไปในตาราง CourseDB
                    database.insert(CourseDB.table_name, null, values);
                } else {    // มีข้อมูลเดิมอยู่
                    int ID = mCursor.getInt(mCursor.getColumnIndex(CourseDB.column[0]));
                    String course_id_sec = course_att[0] + " " + course_att[2];
                    editCourse(ID, course_att[0], course_att[1], course_att[2], course_att[3], course_id_sec, false);
                }
            }
        }
    }

    public void addStudent() {
        String[] students = studentData.split("@");
        //Log.d("print", "len students = " + students.length);
        for(int i = 0; i < students.length; i++) {
            String[] student_att = students[i].split("!");
            //Log.d("print", "len student_att = " + student_att.length);

            //ทำการ Query ข้อมูลจากตาราง CourseDB ใส่ใน Cursor เพื่อดูว่าข้อมูลนี้มีอยู่หรือไม่
            String command = "SELECT * FROM " + StudentDB.table_name +
                    " WHERE " + StudentDB.column[2] + " = '" + student_att[1] + "'";
            Cursor mCursor = database.rawQuery(command, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                if(mCursor.getCount() == 0) {   // ไม่มีข้อมูลเดิมอยู่
                    //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
                    ContentValues values = new ContentValues();
                    values.put(StudentDB.column[1], student_att[0]);
                    values.put(StudentDB.column[2], student_att[1]);
                    values.put(StudentDB.column[3], student_att[2]);

                    //ทำการเพิ่มข้อมูลลงไปในตาราง CourseDB
                    database.insert(StudentDB.table_name, null, values);
                } else {    // มีข้อมูลเดิมอยู่
                    int ID = mCursor.getInt(mCursor.getColumnIndex(StudentDB.column[0]));
                    editStudent(ID, student_att[0], student_att[1], student_att[2], false);
                }
            }
        }
    }

    //Method แก้ไขข้อมูลใน SQLite
    public void editCourse(int ID, String course_id, String course_name, String section, String teacher_id, String course_id_sec, boolean showToast) {
        //เตรียมค่าต่างๆ เพื่อทำการแก้ไข
        ContentValues values = new ContentValues();
        values.put(CourseDB.column[0], ID);
        values.put(CourseDB.column[1], course_id);
        values.put(CourseDB.column[2], course_name);
        values.put(CourseDB.column[3], section);
        values.put(CourseDB.column[4], teacher_id);

        ContentValues value2 = new ContentValues();
        value2.put(TempCheckNameDB.column[4], course_id + " " + section);

        //ให้ Database ทำการแก้ไขข้อมูลที่ id ที่กำหนด
        database.update(CourseDB.table_name, values, "ID = ?", new String[] { "" + ID });
        database.update(TempCheckNameDB.table_name, value2, "course_id_sec = '" + course_id_sec + "'", null);
        database.update(TempStudentScoreDB.table_name, value2, "course_id_sec = '" + course_id_sec + "'", null);
        database.update(TempCountCheckDB.table_name, value2, "course_id_sec = '" + course_id_sec + "'", null);

        if(showToast) {
            Toast.makeText(this, "แก้ไขข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();
        }

        //แสดงข้อมูลล่าสุด
        showListCourse();
    }

    public void editStudent(int ID, String SID, String card_id, String name, boolean showToast) {
        //เตรียมค่าต่างๆ เพื่อทำการแก้ไข
        ContentValues values = new ContentValues();
        values.put(StudentDB.column[0], ID);
        values.put(StudentDB.column[1], SID);
        values.put(StudentDB.column[2], card_id);
        values.put(StudentDB.column[3], name);

        //ให้ Database ทำการแก้ไขข้อมูลที่ id ที่กำหนด
        database.update(StudentDB.table_name, values, "ID = ?", new String[] { "" + ID });

        if(showToast) {
            Toast.makeText(this, "แก้ไขข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();
        }
    }

    //Method ลบข้อมูลใน SQLite
    public void deleteCourse(int ID, String course_id_sec) {
        database.delete(CourseDB.table_name, "ID = " + ID, null);
        database.delete(TempCheckNameDB.table_name, "course_id_sec = '" + course_id_sec + "'", null);
        database.delete(TempStudentScoreDB.table_name, "course_id_sec = '" + course_id_sec + "'", null);
        database.delete(TempCountCheckDB.table_name, "course_id_sec = '" + course_id_sec + "'", null);

        Toast.makeText(this, "ลบข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();

        //แสดงข้อมูลล่าสุด
        showListCourse();
    }

    //Method ดึงข้อมูลจาก SQLite
    private void getCourse() {
        //ทำการ Query ข้อมูลจากตาราง CourseDB ใส่ใน Cursor
        String command = "SELECT * FROM " + CourseDB.table_name +
                " WHERE " + CourseDB.column[4] + " = '" + teacherID + "'";
        Cursor mCursor = database.rawQuery(command, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            listData.clear();
            //ถ้ามีข้อมูลจะทำการเก็บข้อมูลใส่ List เพื่อนำไปแสดง
            if(mCursor.getCount() > 0) {
                do {
                    int ID = mCursor.getInt(mCursor.getColumnIndex(CourseDB.column[0]));
                    String course_id = mCursor.getString(mCursor.getColumnIndex(CourseDB.column[1]));
                    String course_name = mCursor.getString(mCursor.getColumnIndex(CourseDB.column[2]));
                    String section = mCursor.getString(mCursor.getColumnIndex(CourseDB.column[3]));
                    String teacher_id = mCursor.getString(mCursor.getColumnIndex(CourseDB.column[4]));
                    String teacher_name = mCursor.getString(mCursor.getColumnIndex(CourseDB.column[5]));

                    listData.add(new CourseDB(ID, course_id, course_name, section, teacher_id, teacher_name));
                } while (mCursor.moveToNext());
            }
        }
    }

    private void showListCourse() {
        //ดึงข้อมูลสมาชิกจาก SQLite Database
        getCourse();

        //แสดงสมาชิกใน ListView
        listCourse.setAdapter(new AdapterListCourse(this, listData));
    }

    private void showListSearch() {
        //แสดงสมาชิกใน ListView
        listCourse.setAdapter(new AdapterListCourse(this, listSearch));
    }

    public void showEditCourse(int ID, String course_id, String course_name, String sec, String teacher_id, String course_id_sec) {
        Intent intent = new Intent(this, EditCourseActivity.class);

        //ทำการส่งค่าต่าง ๆ ให้ EditCourseActivity แก้ไข
        intent.putExtra("keyID", ID);
        intent.putExtra("keyCourseID", course_id);
        intent.putExtra("keyCourseName", course_name);
        intent.putExtra("keySec", sec);
        intent.putExtra("keyTeacherID", teacher_id);
        intent.putExtra("course_id_sec", course_id_sec);
        //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น  *****//

        //ทำการเรียก EditActivity โดยให้ Request Code เป็น 1
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //ถ้ากลับมาหน้า IndexActivity แล้วผลลัพธ์การทำงานสมบูรณ์
        String course_id, course_name, sec, teacher_id, teacher_name, course_id_sec;
        if(requestCode == 1 && resultCode == RESULT_OK) {
            try {
                //เก็บค่าที่ส่งกลับมาใส่ตัวแปร
                int ID = intent.getExtras().getInt("keyID");
                course_id = intent.getExtras().getString("keyCourseID");
                course_name = intent.getExtras().getString("keyCourseName");
                sec = intent.getExtras().getString("keySec");
                teacher_id = intent.getExtras().getString("keyTeacherID");
                course_id_sec = intent.getExtras().getString("course_id_sec");
                //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

                //ให้แก้ไขข้อมูลสมาชิก
                editCourse(ID, course_id, course_name, sec, teacher_id, course_id_sec, true);
            } catch (NullPointerException except) {
                //Log.d("print", except.getMessage());
            }
        }
        if(requestCode == 2 && resultCode == RESULT_OK) {
            showListCourse();
        }
    }

    public void goMainMenuActivity(String courseID_sec) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("IndexActivitySend", courseID_sec);
        startActivity(intent);
    }

    // ------------------------> Part of event of button in IndexActivity

    public void addStudentData(View view) {
        Intent intent = new Intent(this, AddStudentActivity.class);
        startActivity(intent);
    }

    public void addCourseData(View view) {
        String teacherID = getIntent().getExtras().getString("LoginSend");
        Intent intent = new Intent(this, AddCourseActivity.class);
        intent.putExtra("keyTeacher_id", teacherID);
        //ทำการเรียก EditActivity โดยให้ Request Code เป็น 2
        startActivityForResult(intent, 2);
    }

    public void syncData(View view) {
        String stringUrl = "http://192.168.1.35/getTeacherPrivateData";
        textState.setText("กำลังอัพเดตข้อมูลส่วนตัว");
        // part of send data to server
        if(checkConnectNetwork()) {
            //Log.d("print", "continue connect network");
            new getDataFromServer().execute(stringUrl);
        }
    }

    private boolean checkConnectNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) { // can connect
            return true;
        } else {    // can not connect
            textState.setText("ไม่สามารถเชื่อมต่ออินเตอร์เน็ตได้");
            //Log.d("print", "can not connect network");
            return false;
        }
    }

    private void saveDataToDB() {
        textState.setText("กำลังบันทึกข้อมูล");
        //Log.d("print", "course = " + courseData);
        //Log.d("print", "student = " + studentData);
        addCourse();
        addStudent();
        textState.setText("บันทึกข้อมูลเสร็จเรียบร้อยแล้ว");
        showListCourse();
    }

    // inner class for connect server
    private class getDataFromServer extends AsyncTask<String, Void, String> {
        int stateConnect = 0;
        @Override
        protected String doInBackground(String... myUrl) {
            String tempData = createUrl(myUrl[0], "getCourse");
            if(stateConnect == 0) {
                courseData = tempData;
                tempData = createUrl(myUrl[0], "getStudent");
                if(stateConnect == 0) {
                    studentData = tempData;
                }
            }
            return "";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(stateConnect == 1) { // can not connect server
                textState.setText("ไม่สามารถเชื่อมต่อ Server ได้");
            } else if(stateConnect == 2) {  // can not convert data from server
                textState.setText("ไม่สามารถรับข้อมูลจาก Server ได้");
            } else if(stateConnect == 3) {  // No teacher data
                textState.setText("ไม่มีข้อมูลส่วนตัวของอาจารย์ท่านนี้");
            } else if(stateConnect == 4) {  // teacher no teach
                textState.setText("ไม่มีข้อมูลวิชาที่สอนของอาจารย์ท่านนี้");
            } else if(stateConnect == 5) {  // course no student
                textState.setText("ไม่มีนักศึกษาที่เรียนกับอาจารย์ท่านนี้");
            } else {    // get data from server complete
                textState.setText("รับข้อมูลส่วนตัวจาก Server เรียบร้อยแล้ว");
                saveDataToDB();
            }
        }

        private String createUrl(String myUrl, String var) {
            try {
                String urlTeacher_id = URLEncoder.encode(teacherID, "utf-8");
                String urlCommand = URLEncoder.encode(var, "utf-8");
                String tempData = getPrivateData(myUrl + "?teacher_id=" + urlTeacher_id + "&command=" + urlCommand);
                tempData = removeOtherChar(tempData);
                //Log.d("print", "Web send : " + tempData);
                setStateResult(tempData);
                return tempData;
            } catch (IOException e) {
                e.getMessage();
                return "";
            }
        }

        private void setStateResult(String data) {
            if(data.equals("Connect Server Error")) {
                stateConnect = 1;
                //Log.d("print", "ไม่สามารถเชื่อมต่อ Server ได้ state connect = " + stateConnect);
            } else if(data.equals("Convert text Error")) {
                stateConnect = 2;
                //Log.d("print", "ไม่สามารถรับข้อมูลจาก Server ได้ state connect = " + stateConnect);
            } else if(data.equals("1@")) {
                stateConnect = 3;
            } else if(data.equals("2@")) {
                stateConnect = 4;
            } else if(data.equals("3@")) {
                stateConnect = 5;
            }
        }

        private String removeOtherChar(String str) {
            String[] temp_Arr = str.split("@");
            if(temp_Arr.length > 1) {
                String newStr = "";
                for (int i = 0; i < temp_Arr.length - 1; i++) {
                    newStr += temp_Arr[i] + "@";
                }
                //Log.d("print", newStr);
                return newStr;
            }
            return str;
        }

        private String getPrivateData(String myurl) {
            InputStream is = null;
            int len = 50000;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                //Log.d("print", "The response is: " + response);
                is = conn.getInputStream();
                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                if (is != null) {
                    is.close();
                }
                return contentAsString;
            } catch (IOException e) {
                return "Connect Server Error";
            }
        }

        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) {
            try {
                Reader reader = null;
                reader = new InputStreamReader(stream, "UTF-8");
                char[] buffer = new char[len];
                reader.read(buffer);
                return new String(buffer);
            } catch (IOException e) {
                return "Convert text Error";
            }
        }
    }

    public void searchCourseID(View view) {
        listSearch.clear();
        String compare = "", courseID, searchID;
        int startChar = 0;
        if(search.length() > 0) {   // ใส่รหัสวิชามา
            searchID = search.getText().toString();

            for(int i = 0; i < listData.size(); i++) {  // วนทุกวิชา
                courseID = listData.get(i).getCourseID();
                if(searchID.length() > courseID.length()) {
                    continue;
                }
                for(int j = 0; j < courseID.length(); j++) {    // วนทุกคัวอักษร
                    for (int k = 0; k < searchID.length(); k++) {   // วนจนครบตามจำนวนตัวอักษรที่ใส่มาค้นหา
                        compare += courseID.charAt(k + startChar);
                    }
                    if (searchID.equals(compare)) { // เปรียบเทียบพบว่าเหมือนกัน
                        listSearch.add(listData.get(i));
                        break;
                    }
                    startChar++;
                    if ((startChar + searchID.length()) > courseID.length()) {  // ถ้าเกินจำนวนตัวอักษรในรหัสที่จะเพิ่มได้
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

    public void showConfirmDeleteDialog(final int ID, final String course_id_sec) {
        confirm_del = new AlertDialog.Builder(this)
                .setTitle("คุณต้องการลบข้อมูลนี้")
                .setMessage("คุณต้องการลบข้อมูลนี้ ใช่หรือไม่")
                .setIcon(android.R.drawable.ic_secure)
                .setPositiveButton("ใช่",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // delete data
                                deleteCourse(ID, course_id_sec);
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