package com.example.project.studentnamereader;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ShowScoreActivity extends Activity {

    // ตัวแปรบอกชื่อวิชา
    String courseID_sec;

    //ตัวแปรของ View
    private ListView listScore;
    private EditText search;
    private TextView textState;

    //list ในการเก็บข้อมูลของ student score
    private ArrayList<TempStudentScoreDB> listData = new ArrayList<TempStudentScoreDB>();
    private ArrayList<TempStudentScoreDB> listSearch = new ArrayList<TempStudentScoreDB>();

    private SQLiteDatabase database;

    private AlertDialog confirm_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);

        courseID_sec = getIntent().getExtras().getString("keyCourseID_sec");

        //เชื่อม View
        listScore = (ListView) findViewById(R.id.listScore);
        search = (EditText) findViewById(R.id.search);
        textState = (TextView) findViewById(R.id.textState);

        //สร้างตัวจัดการฐานข้อมูล
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //นำตัวจัดการฐานข้อมูลมาใช้งาน
        database = dbHelper.getWritableDatabase();

        //แสดงรายการสมาชิก
        showListScore();
    }

    private void showListScore() {
        //ดึงข้อมูลสมาชิกจาก SQLite Database
        getScoreData();

        //แสดงสมาชิกใน ListView
        listScore.setAdapter(new AdapterListScore(this, listData));
    }

    private void showListSearch() {
        //แสดงสมาชิกใน ListView
        listScore.setAdapter(new AdapterListScore(this, listSearch));
    }

    //Method ดึงข้อมูลจาก SQLite
    private void getScoreData() {
        //ทำการ Query ข้อมูลจากตาราง TempCheckName ใส่ใน Cursor
        String command = "SELECT * FROM " + TempStudentScoreDB.table_name +
                " WHERE " + TempStudentScoreDB.column[4] + " = '" + courseID_sec + "'";
        Cursor mCursor = database.rawQuery(command, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            listData.clear();
            //ถ้ามีข้อมูลจะทำการเก็บข้อมูลใส่ List เพื่อนำไปแสดง
            if(mCursor.getCount() > 0) {
                do {
                    int ID = mCursor.getInt(mCursor.getColumnIndex(TempStudentScoreDB.column[0]));
                    String student_id = mCursor.getString(mCursor.getColumnIndex(TempStudentScoreDB.column[1]));
                    String student_name = mCursor.getString(mCursor.getColumnIndex(TempStudentScoreDB.column[2]));
                    String time_check = mCursor.getString(mCursor.getColumnIndex(TempStudentScoreDB.column[3]));
                    String course_id_sec = mCursor.getString(mCursor.getColumnIndex(TempStudentScoreDB.column[4]));
                    float score = mCursor.getFloat(mCursor.getColumnIndex(TempStudentScoreDB.column[5]));

                    listData.add(0, new TempStudentScoreDB(ID, student_id, student_name, time_check, course_id_sec, score));
                } while (mCursor.moveToNext());
            }
        }
    }

    //Method แก้ไขข้อมูลใน SQLite
    public void editScore(int ID, float score) {
        //เตรียมค่าต่างๆ เพื่อทำการแก้ไข
        ContentValues values = new ContentValues();
        values.put(TempStudentScoreDB.column[5], score);

        //ให้ Database ทำการแก้ไขข้อมูลที่ id ที่กำหนด
        database.update(TempStudentScoreDB.table_name, values, "ID = ?", new String[] { "" + ID });

        Toast.makeText(this, "แก้ไขคะแนนเรียบร้อย", Toast.LENGTH_SHORT).show();

        //แสดงข้อมูลล่าสุด
        showListScore();
    }

    public void showEditScore(int ID, String student_id, String student_name, String time_check, float score) {
        Intent intent = new Intent(this, EditScoreActivity.class);

        //ทำการส่งค่าต่างๆ ให้ EditScoreActivity ไปทำการแก้ไข
        intent.putExtra("keyID", ID);
        intent.putExtra("keyStudentID", student_id);
        intent.putExtra("keyStudentName", student_name);
        intent.putExtra("keyTimeCheck", time_check);
        intent.putExtra("keyScore", score);

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
                float score = intent.getExtras().getFloat("keyScore");
                //***** ในการส่งค่าและรับค่า ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//

                //ให้แก้ไขข้อมูลสมาชิก
                editScore(ID, score);
            } catch (NullPointerException except) {
                //Log.d("print", except.getMessage());
            }
        }
    }

    //Method ลบข้อมูลใน SQLite
    public void deleteScore(int ID){
        database.delete(TempStudentScoreDB.table_name, "ID = " + ID, null);
        Toast.makeText(this, "ลบข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();

        showListScore();
    }

    public void sendData() {
        String stringUrl = "http://192.168.1.35/receiveStudentScore";
        textState.setText("กำลังส่งข้อมูล");
        if(checkConnectNetwork()) {
            //Log.d("print", "continue connect network");
            new sendDataToServer().execute(stringUrl);
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

    private void deleteAllData() {
        for(int i = 0; i < listData.size(); i++) {
            database.delete(TempStudentScoreDB.table_name, "ID = " + listData.get(i).getID(), null);
        }
        showListScore();
        textState.setText("ส่งข้อมูลเรียบร้อยแล้ว");
    }

    // inner class for connect server
    private class sendDataToServer extends AsyncTask<String, Void, String> {
        int stateConnect = 0;
        @Override
        protected String doInBackground(String... myUrl) {
            try {
                for(int i = 0; i < listData.size(); i++) {
                    String urlStudent_id = URLEncoder.encode(listData.get(i).getStudentID(), "utf-8");
                    //Log.d("print", listData.get(i).getStudentID());

                    String[] times_arr = listData.get(i).getTimeCheck().split(" ");
                    String times = times_arr[1] + "-" + times_arr[3];
                    times = times.replace("/", "-");
                    times = times.replace(":", "-");
                    String urlTime_check = URLEncoder.encode(times, "utf-8");
                    //Log.d("print", times);

                    String[] courses = listData.get(i).getCourseIdSec().split(" ");
                    String urlCourse_id = URLEncoder.encode(courses[0], "utf-8");
                    //Log.d("print", courses[0]);

                    String urlSec = URLEncoder.encode(courses[1], "utf-8");
                    //Log.d("print", courses[1]);

                    String urlScore = URLEncoder.encode("" + listData.get(i).getScore(), "utf-8");
                    //Log.d("print", menu);

                    sendData(myUrl[0] + "?student_id=" + urlStudent_id + "&time_check=" + urlTime_check + "&course_id=" + urlCourse_id + "&section=" + urlSec + "&score=" + urlScore);
                }
                return "";
                //return tempData;
            } catch (IOException e) {
                e.getMessage();
                return "";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(stateConnect == 1) { // can not connect server
                textState.setText("ไม่สามารถเชื่อมต่อ Server ได้");
            } else {    // send data to server complete
                deleteAllData();
            }
        }

        private void sendData(String myurl) {
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                //Log.d("print", "The response is: " + response);
            } catch (IOException e) {   // can not connect server
                stateConnect = 1;
            }
        }
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

    public void showConfirmDeleteDialog(final int ID) {
        confirm_del = new AlertDialog.Builder(this)
                .setTitle("คุณต้องการลบข้อมูลนี้")
                .setMessage("คุณต้องการลบข้อมูลนี้ ใช่หรือไม่")
                .setIcon(android.R.drawable.ic_secure)
                .setPositiveButton("ใช่",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // delete data
                                deleteScore(ID);
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

    public void showConfirmSendData(View view) {
        confirm_del = new AlertDialog.Builder(this)
                .setTitle("คำแนะนำก่อนการส่งข้อมูล")
                .setMessage(R.string.send_score_alert)
                .setIcon(android.R.drawable.ic_secure)
                .setPositiveButton("ส่งข้อมูล",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // send data
                                sendData();
                            }
                        })
                .setNeutralButton("ไม่ส่งข้อมูล",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Do not something
                            }
                        }).show();
    }
}