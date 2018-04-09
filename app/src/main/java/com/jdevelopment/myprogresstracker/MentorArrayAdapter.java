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


public class MentorArrayAdapter extends ArrayAdapter<Mentor> {

    private Context context;
    private ArrayList<Mentor> allMentors;
    private static LayoutInflater inflater = null;

    //constructor
    public MentorArrayAdapter(@NonNull Context context, int resource, ArrayList<Mentor> _allMentors) {
        super(context, resource, _allMentors);
        try {
            this.context = context;
            this.allMentors = _allMentors;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e){
            Log.d("adapter", "constructor exception");
        }
    }

    public int getCount(){return allMentors.size();}

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
        public CheckBox adapterCourseMentorAdd;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;
        final MentorArrayAdapter.ViewHolder holder;

        final Mentor mentor = getItem(position);

        try {
            if (convertView == null){
                view = inflater.inflate(R.layout.mentor_list_item_editor, null);
                holder = new MentorArrayAdapter.ViewHolder();

                holder.adapterCourseMentorName = (TextView) view.findViewById(R.id.tvCourseMentorName);
                holder.adapterCourseMentorPhone = (TextView) view.findViewById(R.id.tvCourseMentorPhone);
                holder.adapterCourseMentorEmail = (TextView) view.findViewById(R.id.tvCourseMentorEmail);
                holder.adapterCourseMentorAdd = (CheckBox) view.findViewById(R.id.addCourseMentorCB);

                view.setTag(holder);

                holder.adapterCourseMentorAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getItem(position);
                        mentor.toggleChecked();
                    }
                });
            }
            else {
                holder = (MentorArrayAdapter.ViewHolder) view.getTag();
            }
            holder.adapterCourseMentorName.setText(allMentors.get(position).getMentorName());
            holder.adapterCourseMentorPhone.setText(allMentors.get(position).getMentorPhone());
            holder.adapterCourseMentorEmail.setText(allMentors.get(position).getMentorEmail());
            holder.adapterCourseMentorAdd.setChecked(allMentors.get(position).isChecked());
        }
        catch (Exception e){
            Log.d("adapter", "getView exception");
        }
        return view;
    }

}