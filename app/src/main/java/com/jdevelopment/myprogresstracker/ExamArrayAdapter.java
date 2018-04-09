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


public class ExamArrayAdapter extends ArrayAdapter<Exam> {

    private Context context;
    private ArrayList<Exam> exams;
    private static LayoutInflater inflater = null;

    //constructor
    public ExamArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exam> _exams) {
        super(context, resource, _exams);
        try {
            this.context = context;
            this.exams = _exams;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e){
            Log.d("adapter", "constructor exception");
        }
    }

    public int getCount(){
        return exams.size();
    }

    public Exam getItem(Exam position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder {
        public TextView adapterExamType;
        public TextView adapterExamDate;
        public TextView adapterExamStatus;
        public CheckBox adapterExamCB;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        final ViewHolder holder;

        Exam exam = getItem(position);

        try {
            if (convertView == null){
                view = inflater.inflate(R.layout.exam_list_item, null);
                holder = new ViewHolder();

                holder.adapterExamType = (TextView) view.findViewById(R.id.listExamType);
                holder.adapterExamDate = (TextView) view.findViewById(R.id.listExamDueDate);
                holder.adapterExamStatus = (TextView) view.findViewById(R.id.listExamStatus);
                holder.adapterExamCB = (CheckBox) view.findViewById(R.id.alarmSetCB);

                view.setTag(holder);
            }
            else {
                holder = (ViewHolder) view.getTag();
            }
            holder.adapterExamType.setText(exams.get(position).getExamType());
            holder.adapterExamDate.setText(exams.get(position).getExamDate());
            holder.adapterExamStatus.setText(exams.get(position).getExamSatus());
            holder.adapterExamCB.setChecked(exams.get(position).isChecked());
        }
        catch (Exception e){
            Log.d("adapter", "getView exception");
        }
        return view;
    }


}
