package com.example.project.studentnamereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListScore extends BaseAdapter {

    private LayoutInflater mInflater;
    private ShowScoreActivity control;

    //list ในการเก็บข้อมูลของตาราง Student
    private ArrayList<TempStudentScoreDB> listData = new ArrayList<TempStudentScoreDB>();

    public AdapterListScore(ShowScoreActivity control, ArrayList<TempStudentScoreDB> listData) {
        this.control = control;
        Context context = control.getBaseContext();
        this.mInflater = LayoutInflater.from(context);
        this.listData = listData;
    }

    public int getCount() {
        //ส่งขนาดของ List ที่เก็บข้อมูลอยู่
        return listData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderListAdapter holderListAdapter; //เก็บส่วนประกอบของ List แต่ละอัน

        if(convertView == null) {
            //ใช้ Layout ของ List เราที่สร้างขึ้นเอง (adapter_list_check_name.xml)
            convertView = mInflater.inflate(R.layout.adapter_list_score, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapter = new HolderListAdapter();

            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View ใน adapter_list_score.xml
            holderListAdapter.data1 = (TextView) convertView.findViewById(R.id.scoreData);
            holderListAdapter.data2 = (TextView) convertView.findViewById(R.id.student_name);
            holderListAdapter.editB = (ImageButton) convertView.findViewById(R.id.editScore);
            holderListAdapter.deleteB = (ImageButton) convertView.findViewById(R.id.deleteScore);

            convertView.setTag(holderListAdapter);
        } else {
            holderListAdapter = (HolderListAdapter) convertView.getTag();
        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int ID = listData.get(position).getID();
        final String student_id = listData.get(position).getStudentID();
        final String student_name = listData.get(position).getStudentName();
        final String time_check = listData.get(position).getTimeCheck();
        final float score = listData.get(position).getScore();

        String dataText = "รหัสนักศึกษา " + student_id + "\n" +
                time_check + "\nได้คะแนนจิตพิสัย " + score;
        holderListAdapter.data1.setText(dataText);
        holderListAdapter.data2.setText(student_name);

        //สร้าง Event ให้ปุ่มแก้ไขข้อมูล
        holderListAdapter.editB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                control.showEditScore(ID, student_id, student_name, time_check, score);
            }
        });

        //สร้าง Event ให้ปุ่มลบข้อมูล
        holderListAdapter.deleteB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                control.showConfirmDeleteDialog(ID);
            }
        });

        return convertView;
    }
}