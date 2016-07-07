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

public class AdapterListCourse extends BaseAdapter {

    private LayoutInflater mInflater;
    private IndexActivity controller;

    //list ในการเก็บข้อมูลของตาราง Course
    private ArrayList<CourseDB> listData = new ArrayList<CourseDB>();

    public AdapterListCourse(IndexActivity control, ArrayList<CourseDB> listData) {
        this.controller = control;
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
            convertView = mInflater.inflate(R.layout.adapter_list_course, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapter = new HolderListAdapter();

            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View ใน adapter_list_course.xml
            holderListAdapter.data1 = (TextView) convertView.findViewById(R.id.courseData);
            holderListAdapter.data2 = (TextView) convertView.findViewById(R.id.teacherData);
            holderListAdapter.data3 = (TextView) convertView.findViewById(R.id.course_id);
            holderListAdapter.editB = (ImageButton) convertView.findViewById(R.id.editButton);
            holderListAdapter.deleteB = (ImageButton) convertView.findViewById(R.id.deleteButton);

            convertView.setTag(holderListAdapter);
        } else {
            holderListAdapter = (HolderListAdapter) convertView.getTag();
        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int ID = listData.get(position).getID();
        final String course_id = listData.get(position).getCourseID();
        final String course_name = listData.get(position).getCourseName();
        final String sec = listData.get(position).getSection();
        final String teacher_name = listData.get(position).getTeacherName();
        final String teacher_id = listData.get(position).getTeacherID();
        final String course_id_sec = course_id + " " + sec;

        String dataText = "รหัสวิชา " + course_id + " ตอนเรียนที่ " + sec;
        holderListAdapter.data1.setText(dataText);
        dataText = "ผู้สอน " + teacher_name;
        holderListAdapter.data2.setText(dataText);
        holderListAdapter.data3.setText(course_name);

        //สร้าง Event ให้ปุ่มแก้ไขข้อมูล
        holderListAdapter.editB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                controller.showEditCourse(ID, course_id, course_name, sec, teacher_id, course_id_sec);
            }
        });

        //สร้าง Event ให้ปุ่มลบข้อมูล
        holderListAdapter.deleteB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                controller.showConfirmDeleteDialog(ID, course_id_sec);
            }
        });

        //สร้าง Event ให้ชื่อวิชา
        holderListAdapter.data1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String courseID_sec = course_id + " " + sec;
                controller.goMainMenuActivity(courseID_sec);
            }
        });

        //สร้าง Event ให้ชื่ออาจารย์
        holderListAdapter.data2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String courseID_sec = course_id + " " + sec;
                controller.goMainMenuActivity(courseID_sec);
            }
        });

        //สร้าง Event ให้รหัสวิชา
        holderListAdapter.data3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String courseID_sec = course_id + " " + sec;
                controller.goMainMenuActivity(courseID_sec);
            }
        });

        return convertView;
    }
}