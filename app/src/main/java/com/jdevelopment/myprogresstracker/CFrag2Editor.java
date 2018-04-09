package com.jdevelopment.myprogresstracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class CFrag2Editor extends Fragment {

    Intent intent;

    String alertStatusTag = "alertOff";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.cfrag2_editor, container, false);
        final View examView = inflater.inflate(R.layout.cfrag2_exams, container, false);

        MainActivity.switchId = "courseBtn";


        RadioGroup rgType = rootView.findViewById(R.id.examRadioGroup);
        TextView tvDate = rootView.findViewById(R.id.examDueDateResult);
        RadioGroup rgStatus = rootView.findViewById(R.id.examStatusRadioGroup);
        String type = null;
        String status = null;
        Switch alertSwitch = rootView.findViewById(R.id.setAlarmBtn);

        intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            type = (String) b.getCharSequence("existingExamType");
            status = (String) b.getCharSequence("existingExamStatus");
            //set Exam type
            for (int i = 0; i < rgType.getChildCount(); i++){
                RadioButton button = (RadioButton) rgType.getChildAt(i);
                String btnTextType = button.getText().toString();
                if (btnTextType.equals(type)){
                    button.toggle();
                    break;
                }
            }
            //set Exam Date
            tvDate.setText(b.getCharSequence("existingExamDate"));
            //set Exam alert status
            if (b.getBoolean("existingExamAlertStatus")){
                alertSwitch.toggle();
            }
            //set Exam Status
            for (int i = 0; i < rgStatus.getChildCount(); i++){
                RadioButton button = (RadioButton) rgStatus.getChildAt(i);
                String btnTextStatus = button.getText().toString();
                if (btnTextStatus.equals(status)){
                    button.toggle();
                    break;
                }
            }
        }

        rootView.findViewById(R.id.setAlarmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch alertSwitch = rootView.findViewById(R.id.setAlarmBtn);
                if(alertSwitch.isChecked()){
                    //create String to hold reference to to dueDate textview
                    TextView examDateTV = rootView.findViewById(R.id.examDueDateResult);
                    String examDateString = examDateTV.getText().toString();  //format M/d/yyyy or MM/dd/yyyy
                    //find indexes of slashes
                    int slashIndex1 = examDateString.indexOf("/");
                    int slashIndex2 = examDateString.lastIndexOf("/");
                    //parse out year, month, day
                    int year = Integer.parseInt(examDateString.substring(slashIndex2 + 1));
                    int month = Integer.parseInt(examDateString.substring(0, slashIndex1));
                    int day = Integer.parseInt(examDateString.substring(slashIndex1 + 1, slashIndex2));
                    //get Calendar instance
                    Calendar calendar = Calendar.getInstance();
//                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
//                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
                    calendar.set(year + 0, month - 1, day + 0, 16 - 6, 40, 0);
                    Log.d("calender", "time = " + calendar.getTimeInMillis());
                    Log.d("calender", "time = " + System.currentTimeMillis());
                    setAlarm(calendar.getTimeInMillis());

                    alertStatusTag = "alertOn";
//                    intent.putExtra("alertStatusTag", "alertOn");
                }
                else {
                    Toast.makeText(getContext(), "Alert canceled", Toast.LENGTH_SHORT).show();
                    //turn alert off
                    alertStatusTag = "alertOff";
//                    intent.putExtra("alertStatusTag", "alertOff");
                }
            }
        });

        rootView.findViewById(R.id.saveExam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set exam type
                RadioGroup rg = rootView.findViewById(R.id.examRadioGroup);  //in editor
                int id = rg.getCheckedRadioButtonId();
                View radioView = rg.findViewById(id);
                int index = rg.indexOfChild(radioView);
                RadioButton checkedBtn = (RadioButton) rg.getChildAt(index);
                String newExamType = checkedBtn.getText().toString();
                intent.putExtra("examType", newExamType);

                //set exam date
                TextView examDateEditor = rootView.findViewById(R.id.examDueDateResult);  //in editor
                String newExamDate = examDateEditor.getText().toString();
                intent.putExtra("examDate", newExamDate);

                //set exam status
                RadioGroup rg2 = rootView.findViewById(R.id.examStatusRadioGroup);  //in editor
                int id2 = rg2.getCheckedRadioButtonId();
                View radioView2 = rg2.findViewById(id2);
                int index2 = rg2.indexOfChild(radioView2);
                RadioButton checkedBtn2 = (RadioButton) rg2.getChildAt(index2);
                String newExamStatus = checkedBtn2.getText().toString();
                intent.putExtra("examStatus", newExamStatus);

                //set alert status
                intent.putExtra("alertStatus", alertStatusTag);

//                intent.putExtra("switchObject", "afterSave");
//
//                CFrag1CourseInfo.updateFragment();
                CFrag2Exams.addOrUpdateExam();
                changeFragment();

                View button = examView.findViewById(R.id.addExamBtn);
                button.setVisibility(View.VISIBLE);
                button.bringToFront();
            }
        });

        rootView.findViewById(R.id.deleteExam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CFrag2Exams.deleteExam();
                changeFragment();

                View button = examView.findViewById(R.id.addExamBtn);
                button.setVisibility(View.VISIBLE);
                button.bringToFront();

            }
        });


        final Button addButton = new Button(getActivity());
        final RelativeLayout examLayout = (RelativeLayout) View.inflate(getActivity(), R.layout.cfrag2_exams, null);
        rootView.findViewById(R.id.cancelAndReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment();

//                View button = examView.findViewById(R.id.addExamBtn);
//                button.requestFocus();
//                button.setVisibility(View.VISIBLE);
//                button.bringToFront();
//                examView.findViewById(R.id.addExamBtn).bringToFront();
//                examView.invalidate();

                //add button dynamic buttons in course tips
                RelativeLayout.LayoutParams btnLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                btnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                addButton.setLayoutParams(btnLayoutParams);
                addButton.setText("Add Exam");
                addButton.bringToFront();
                addButton.setVisibility(View.VISIBLE);

                examLayout.addView(addButton);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment newFragment = new CFrag2Editor();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.cFrag2BaseLayout, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        intent.putExtra("newOrExisting", "newExam");
                        View button = view.findViewById(R.id.addExamBtn);
                        button.setVisibility(View.INVISIBLE);

                    }
                });

            }
        });

        return rootView;

    }

    /*
    //Handle setting exam alarms
    // */
    public void setAlarm(long time){
        //create alarm manager
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //create intent to specify broadcast reciever
        Intent i = new Intent(getActivity(), Alerts.class);

        //put in extras for alarm message
        String title = "Exam Alert!";
        String message = "You have an exam due today.";

        i.putExtra("title", title);
        i.putExtra("message", message);
        i.putExtra("time", time);

        //create a pending intent using above intent
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, 0);
        //set alarm
        alarmManager.set(AlarmManager.RTC, time, pi);
        Toast.makeText(getContext(), "Alarm is set", Toast.LENGTH_SHORT).show();

    }

    public void changeFragment(){
        getFragmentManager().popBackStackImmediate();
    }

}