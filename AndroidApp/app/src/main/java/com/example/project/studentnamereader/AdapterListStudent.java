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

public class AdapterListStudent extends BaseAdapter {

    private LayoutInflater mInflater;
    private ShowStudentActivity control;

    //list ในการเก็บข้อมูลของตาราง Student
    private ArrayList<StudentDB> listData = new ArrayList<StudentDB>();

    public AdapterListStudent(ShowStudentActivity control, ArrayList<StudentDB> listData) {
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
            //ใช้ Layout ของ List เราที่สร้างขึ้นเอง (adapter_list_course.xml)
            convertView = mInflater.inflate(R.layout.adapter_list_student, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapter = new HolderListAdapter();

            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View ใน adapter_list_student.xml
            holderListAdapter.data1 = (TextView) convertView.findViewById(R.id.courseData);
            holderListAdapter.data2 = (TextView) convertView.findViewById(R.id.student_name);
            holderListAdapter.editB = (ImageButton) convertView.findViewById(R.id.editButton);
            holderListAdapter.deleteB = (ImageButton) convertView.findViewById(R.id.deleteButton);

            convertView.setTag(holderListAdapter);
        } else {
            holderListAdapter = (HolderListAdapter) convertView.getTag();
        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int ID = listData.get(position).getID();
        final String student_id = listData.get(position).getStudentID();
        final String card_id = listData.get(position).getCardID();
        final String name = listData.get(position).getName();

        String dataText = "รหัสนักศึกษา " + student_id + "\nรหัส UID " + card_id;
        holderListAdapter.data1.setText(dataText);
        holderListAdapter.data2.setText(name);

        //สร้าง Event ให้ปุ่มแก้ไขข้อมูล
        holderListAdapter.editB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String[] nameStudent = name.split(" ");
                control.showEditStudent(ID, student_id, card_id, nameStudent[0], nameStudent[1]);
            }
        });

        //สร้าง Event ให้ปุ่มลบข้อมูล
        holderListAdapter.deleteB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                control.showConfirmDeleteDialog(ID, student_id);
            }
        });

        return convertView;
    }
}