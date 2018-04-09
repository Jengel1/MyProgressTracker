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


public class CourseMentorArrayAdapter extends ArrayAdapter<Mentor> {

    private Context context;
    private ArrayList<Mentor> courseMentors;
    private static LayoutInflater inflater = null;

    //constructor
    public CourseMentorArrayAdapter(@NonNull Context context, int resource, ArrayList<Mentor> _courseMentors) {
        super(context, resource, _courseMentors);
        try {
            this.context = context;
            this.courseMentors = _courseMentors;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e){
            Log.d("adapter", "constructor exception");
        }
    }

    public int getCount(){return courseMentors.size();}

    public Mentor getItem(Mentor position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder {
        public TextView adapterCourseMentorName;
        public TextView adapterCourseMentorPhone;
        public TextView adapterCourseMentorEmail;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;
        final CourseMentorArrayAdapter.ViewHolder holder;

        final Mentor mentor = getItem(position);

        try {
            if (convertView == null){
                view = inflater.inflate(R.layout.mentor_list_item, null);
                holder = new CourseMentorArrayAdapter.ViewHolder();

                holder.adapterCourseMentorName = (TextView) view.findViewById(R.id.tvMentorName);
                holder.adapterCourseMentorPhone = (TextView) view.findViewById(R.id.tvPhone);
                holder.adapterCourseMentorEmail = (TextView) view.findViewById(R.id.tvEmail);

                view.setTag(holder);
            }
            else {
                holder = (CourseMentorArrayAdapter.ViewHolder) view.getTag();
            }
            holder.adapterCourseMentorName.setText(courseMentors.get(position).getMentorName());
            holder.adapterCourseMentorPhone.setText(courseMentors.get(position).getMentorPhone());
            holder.adapterCourseMentorEmail.setText(courseMentors.get(position).getMentorEmail());
        }
        catch (Exception e){
            Log.d("adapter", "getView exception");
        }
        return view;
    }

}
