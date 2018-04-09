package com.jdevelopment.myprogresstracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


public class CourseArrayAdaptor extends ArrayAdapter<Course> {

    private Context context;
    private ArrayList<Course> allCourses;
    private static LayoutInflater inflater = null;

    //constructor
    public CourseArrayAdaptor(@NonNull Context context, int resource, ArrayList<Course> _allCourses) {
        super(context, resource, _allCourses);
        try {
            this.context = context;
            this.allCourses = _allCourses;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e){
            Log.d("adapter", "constructor exception");
        }
    }

    public int getCount(){return allCourses.size();}

    public Mentor getItem(Mentor position){return position;}

    public long getItemId(int position){return position;}

    public static class ViewHolder {
        public TextView adapterTermCourseName;
        public TextView adapterTermCourseStart;
        public TextView adapterTermCourseEnd;
        public TextView adapterTermCourseStatus;
        public CheckBox adapterTermCourseAdd;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;
        final CourseArrayAdaptor.ViewHolder holder;

        final Course course = getItem(position);

        try {
            if (convertView == null){
                view = inflater.inflate(R.layout.course_list_item_editor, null);
                holder = new CourseArrayAdaptor.ViewHolder();

                holder.adapterTermCourseName = (TextView) view.findViewById(R.id.tvTermCourseName);
                holder.adapterTermCourseStart = (TextView) view.findViewById(R.id.tvCStartDate);
                holder.adapterTermCourseEnd = (TextView) view.findViewById(R.id.tvCEndDate);
                holder.adapterTermCourseStatus = (TextView) view.findViewById(R.id.tvTermCourseListItemStatus);
                holder.adapterTermCourseAdd = (CheckBox) view.findViewById(R.id.addTermCourseCB);

                view.setTag(holder);

                //set onClickListener
                holder.adapterTermCourseAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getItem(position);
                        course.toggleCheckedTerm();
                    }
                });
            }
            else {
                holder = (CourseArrayAdaptor.ViewHolder) view.getTag();
            }
            holder.adapterTermCourseName.setText(allCourses.get(position).getCourseName());
            holder.adapterTermCourseStart.setText(allCourses.get(position).getCourseStart());
            holder.adapterTermCourseEnd.setText(allCourses.get(position).getCourseEnd());
            holder.adapterTermCourseStatus.setText(allCourses.get(position).getCourseStatus());
            holder.adapterTermCourseAdd.setChecked(allCourses.get(position).isCheckedTerm());
        }
        catch (Exception e){
            Log.d("adapter", "getView exception");
        }
        return view;
    }

}