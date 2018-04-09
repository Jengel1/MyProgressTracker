package com.jdevelopment.myprogresstracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class TermCourseArrayAdapter extends ArrayAdapter<Course> {

    private Context context;
    private ArrayList<Course> termCourses;
    private static LayoutInflater inflater = null;

    //constructor
    public TermCourseArrayAdapter(@NonNull Context context, int resource, ArrayList<Course> _termCourses) {
        super(context, resource, _termCourses);
        try {
            this.context = context;
            this.termCourses = _termCourses;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e){
            Log.d("adapter", "constructor exception");
        }
    }

    public int getCount(){return termCourses.size();}

    public Course getItem(Course position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder {
        public TextView adapterTermCourseName;
        public TextView adapterTermCourseStart;
        public TextView adapterTermCourseEnd;
        public TextView adapterTermCourseStatus;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;
        final TermCourseArrayAdapter.ViewHolder holder;

        final Course course = getItem(position);

        try {
            if (convertView == null){
                view = inflater.inflate(R.layout.course_list_item, null);
                holder = new TermCourseArrayAdapter.ViewHolder();

                holder.adapterTermCourseName = (TextView) view.findViewById(R.id.tvCourseName);
                holder.adapterTermCourseStart = (TextView) view.findViewById(R.id.tvCStartDate);
                holder.adapterTermCourseEnd = (TextView) view.findViewById(R.id.tvCEndDate);
                holder.adapterTermCourseStatus = (TextView) view.findViewById(R.id.tvCourseListItemStatus);

                view.setTag(holder);
            }
            else {
                holder = (TermCourseArrayAdapter.ViewHolder) view.getTag();
            }
            holder.adapterTermCourseName.setText(termCourses.get(position).getCourseName());
            holder.adapterTermCourseStart.setText(termCourses.get(position).getCourseStart());
            holder.adapterTermCourseEnd.setText(termCourses.get(position).getCourseEnd());
            holder.adapterTermCourseStatus.setText(termCourses.get(position).getCourseStatus());
        }
        catch (Exception e){
            Log.d("adapter", "getView exception");
        }
        return view;
    }

}
