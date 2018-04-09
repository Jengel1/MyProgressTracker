package com.jdevelopment.myprogresstracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class DataCursorAdapter extends CursorAdapter {

    public DataCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int itemLayout = 0;
        if (MainActivity.switchId.equals("termsBtn")){
            itemLayout = R.layout.term_list_item;
        }
        if (MainActivity.switchId.equals("courseBtn")){
            itemLayout = R.layout.course_list_item;
        }
        if (MainActivity.switchId.equals("mentorsBtn")){
            itemLayout = R.layout.mentor_list_item;
        }
        return LayoutInflater.from(context).inflate(itemLayout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (MainActivity.switchId.equals("mentorsBtn")){  //if the mentor btn is clicked
            //set mentor name
            String mentorName = cursor.getString(cursor.getColumnIndex(DBOpener.MENTOR_NAME));
            TextView tvMName = (TextView) view.findViewById(R.id.tvMentorName);
            tvMName.setText(mentorName);
            //set mentor phone
            String mentorPhone = cursor.getString(cursor.getColumnIndex(DBOpener.MENTOR_PHONE));
            TextView tvMPhone = (TextView) view.findViewById(R.id.tvPhone);
            tvMPhone.setText(mentorPhone);
            //set mentor email
            String mentorEmail = cursor.getString(cursor.getColumnIndex(DBOpener.MENTOR_EMAIL));
            TextView tvMEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMEmail.setText(mentorEmail);
        }
        if (MainActivity.switchId.equals("courseBtn")){  //if the course btn is clicked
            //set course name
            String courseName = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_NAME));
            TextView tvCName = view.findViewById(R.id.tvCourseName);
            tvCName.setText(courseName);
            //set course startDate
            String courseStartDateWithTag = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_START));
            TextView tvCStart = view.findViewById(R.id.tvCStartDate);
            tvCStart.setText(CFrag1CourseInfo.getDate(courseStartDateWithTag));
            //set course endDate
            String courseEndDateWithTag = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_END));
            TextView tvCEnd = view.findViewById(R.id.tvCEndDate);
            tvCEnd.setText(CFrag1CourseInfo.getDate(courseEndDateWithTag));
            //set course status
            String courseStatus = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_STATUS));
            TextView tvStatus = view.findViewById(R.id.tvCourseListItemStatus);
            tvStatus.setText(courseStatus);
        }
        if (MainActivity.switchId.equals("termsBtn")){  //if the term btn is clicked
            //set course name
            String termName = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_NAME));
            TextView tvTName = view.findViewById(R.id.tvTermName);
            tvTName.setText(termName);
            //set course startDate
            String termStartDate = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_START));
            TextView tvTStart = view.findViewById(R.id.tvTStartDate);
            tvTStart.setText(termStartDate);
            //set course endDate
            String termEndDate = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_END));
            TextView tvTEnd = view.findViewById(R.id.tvTEndDate);
            tvTEnd.setText(termEndDate);
            //set term status variable
            String termStatus = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_STATUS));
            TextView tvStatus = view.findViewById(R.id.tvTermListItemStatus);
            tvStatus.setText(termStatus);
        }
    }
}