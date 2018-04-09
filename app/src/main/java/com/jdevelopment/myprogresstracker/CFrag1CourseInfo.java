package com.jdevelopment.myprogresstracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class CFrag1CourseInfo extends android.support.v4.app.Fragment {

    static TextView courseName;
    static TextView courseStart;
    static TextView courseStatus;
    static TextView courseEnd;
    static Switch startAlertSwitch;
    static Switch endAlertSwitch;

    static Intent intent;

    //Strings to hold view text values for action_edit
    String someDataName = null;
    String someDataStart = null;
    String someDataEnd = null;
    String someDataStatus = null;

    //variable to hold start or end alert switch value
    boolean startDateAlertBooleanValue = false;

    //variable to track how many times page is loaded
    private static int fragmentLoadCount = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.cfrag1_course_info, container, false);
        View editView = inflater.inflate(R.layout.cfrag1_editor, container, false);

        MainActivity.switchId = "courseBtn";

        //reference to info views
        courseName = rootView.findViewById(R.id.tvCourseNameInfo);
        courseStart = rootView.findViewById(R.id.tvCStartDateInfo);
        courseEnd = rootView.findViewById(R.id.tvCEndDateInfo);
        courseStatus = rootView.findViewById(R.id.tvCourseStatusResult);
        startAlertSwitch = rootView.findViewById(R.id.cStartAlertSwitch);
        endAlertSwitch = rootView.findViewById(R.id.cEndAlertSwitch);

        //Strings to hold view text values for action_insert
        String noDataName = courseName.getText().toString();
        String noDataStart = courseStart.getText().toString();
        String noDataEnd = courseEnd.getText().toString();
        String noDataStatus = courseStatus.getText().toString();

        intent = getActivity().getIntent();
        final Bundle b = intent.getExtras();

        if(b.getCharSequence("action").equals(Intent.ACTION_INSERT)){
            //add text values and data tracking variableto intent object
            if (fragmentLoadCount > 0){
                updateFragment();
            }
            else {
                intent.putExtra("switchObject", "beforeSaveInsert");
                intent.putExtra("noDataName", noDataName);
                intent.putExtra("noDataStart", noDataStart);
                intent.putExtra("noDataEnd", noDataEnd);
                intent.putExtra("noDataStatus", noDataStatus);
                fragmentLoadCount++;
            }
        }

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
                    //set TextViews with existing data after popping last char for alert status
                    courseName.setText(b.getCharSequence("courseName"));
                    courseStart.setText(getDate(b.getString("courseStart")));
                    courseEnd.setText(getDate(b.getString("courseEnd")));
                    courseStatus.setText(b.getCharSequence("courseStatus"));
                    //set String variables with existing data
                    someDataName = courseName.getText().toString();
                    someDataStart = courseStart.getText().toString();
                    someDataEnd = courseEnd.getText().toString();
                    someDataStatus = courseStatus.getText().toString();
                    //put String variables in intent bundle for finish editing method
                    intent.putExtra("switchObject", "beforeSaveEdit");
                    intent.putExtra("someDataName", someDataName);
                    intent.putExtra("someDataStart", someDataStart);
                    intent.putExtra("someDataEnd", someDataEnd);
                    intent.putExtra("someDataStatus", someDataStatus);

                    if (b.getString("switchObject") != null && b.getString("switchObject").equals("afterSave")){
                        updateFragment();
                    }

                //set alert switches
                    Switch startAlertSwitch = rootView.findViewById(R.id.cStartAlertSwitch);
                    if (getAlertTag(b.getString("courseStart")).equals("T")){
                        startAlertSwitch.toggle();
                        intent.putExtra("startAlertStatus", "alertOn");
                    }
                    else {
                        intent.putExtra("startAlertStatus", "alertOff");
                    }
                    Switch endAlertSwitch = rootView.findViewById(R.id.cEndAlertSwitch);
                    if(getAlertTag(b.getString("courseEnd")).equals("T")){
                        endAlertSwitch.toggle();
                        intent.putExtra("endAlertStatus", "alertOn");
                    }
                    else {
                        intent.putExtra("endAlertStatus", "alertOff");
                    }
        }

        rootView.findViewById(R.id.editCFrag1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new CFrag1Editor();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.cFrag1BaseLayout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        rootView.findViewById(R.id.cStartAlertSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch startAlertSwitch = rootView.findViewById(R.id.cStartAlertSwitch);
                if (startAlertSwitch.isChecked()){
                    //create reference to start date and string to hold value
                    TextView startDateTV = rootView.findViewById(R.id.tvCStartDateInfo);
                    String startDateString = startDateTV.getText().toString();
                    //find indexes of slashes
                    int slashIndex1 = startDateString.indexOf("/");
                    int slashIndex2 = startDateString.lastIndexOf("/");
                    //parse out year, month, day
                    int year = Integer.parseInt(startDateString.substring(slashIndex2 + 1));
                    int month = Integer.parseInt(startDateString.substring(0, slashIndex1));
                    int day = Integer.parseInt(startDateString.substring(slashIndex1 + 1, slashIndex2));
                    //get Calendar instance
                    Calendar calendar = Calendar.getInstance();
//                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
//                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
                    calendar.set(year, month, day, 12, 25, 0);
                    setAlarm(calendar.getTimeInMillis());

                    intent.putExtra("startAlertStatus", "alertOn");

                    startDateAlertBooleanValue = true;
                }
                else {
                    Toast.makeText(getContext(), "Alert canceled", Toast.LENGTH_SHORT).show();
                    //turn alert off
                    intent.putExtra("startAlertStatus", "alertOff");
                }
            }
        });

        rootView.findViewById(R.id.cEndAlertSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch endAlertSwitch = rootView.findViewById(R.id.cEndAlertSwitch);
                if (endAlertSwitch.isChecked()){
                    //create reference to end date and string to hold value
                    TextView endDateTV = rootView.findViewById(R.id.tvCEndDateInfo);
                    String endDateString = endDateTV.getText().toString();
                    //find indexes of slashes
                    int slashIndex1 = endDateString.indexOf("/");
                    int slashIndex2 = endDateString.lastIndexOf("/");
                    //parse out year, month, day
                    int year = Integer.parseInt(endDateString.substring(slashIndex2 + 1));
                    int month = Integer.parseInt(endDateString.substring(0, slashIndex1));
                    int day = Integer.parseInt(endDateString.substring(slashIndex1 + 1, slashIndex2));
                    //get Calendar instance
                    Calendar calendar = Calendar.getInstance();
//                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
//                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
                    calendar.set(year, month, day, 12, 25, 0);
                    setAlarm(calendar.getTimeInMillis());

                    intent.putExtra("endAlertStatus", "alertOn");

                }
                else {
                    Toast.makeText(getContext(), "Alert canceled", Toast.LENGTH_SHORT).show();
                    //turn alert off
                    intent.putExtra("endAlertStatus", "alertOff");
                }

            }
        });

        return rootView;
    }

    public static String getDate(String dateWithTag){
        String dateWithNoTag = dateWithTag.substring(0, dateWithTag.length() - 1);
        return dateWithNoTag;
    }

    public static String getAlertTag(String tagWithDate){
        String tagWithNoDate = tagWithDate.substring(tagWithDate.length() - 1);
        return tagWithNoDate;
    }

    public static void updateFragment(){
        Bundle b = intent.getExtras();
        courseName.setText(b.getCharSequence("savedCourseName"));
        courseStart.setText(b.getCharSequence("savedCourseStart"));
        courseEnd.setText(b.getCharSequence("savedCourseEnd"));
        courseStatus.setText(b.getCharSequence("savedCourseStatus"));

        //strings to hold final values
        String finalCoursename = courseName.getText().toString();
        String finalCourseStart = courseStart.getText().toString();
        String finalCourseEnd = courseEnd.getText().toString();
        String finalCourseStatus = courseStatus.getText().toString();

        intent.putExtra("switchObject", "afterSave");
        intent.putExtra("finalCourseName", finalCoursename);
        intent.putExtra("finalCourseStart", finalCourseStart);
        intent.putExtra("finalCourseEnd", finalCourseEnd);
        intent.putExtra("finalCourseStatus", finalCourseStatus);
    }

    /*
    //Handle setting course alarms
    // */
    public void setAlarm(long time){
        //create alarm manager
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //create intent to specify broadcast reciever
        Intent i = new Intent(getActivity(), Alerts.class);

        //put in extras for alarm message
        String title = "Course Alert!";
        String message = null;
        if(startDateAlertBooleanValue){  //start alert
            message = "You have a course starting today.";
        }
        else {  //end alert
            message = "You have a course ending today.";
        }

        i.putExtra("title", title);
        i.putExtra("message", message);
        i.putExtra("time", time);

        //create a pending intent using above intent
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, 0);
        //set alarm
        alarmManager.set(AlarmManager.RTC, time, pi);
        Toast.makeText(getContext(), "Alarm is set", Toast.LENGTH_SHORT).show();

    }

}