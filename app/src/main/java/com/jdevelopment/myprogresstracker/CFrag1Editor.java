package com.jdevelopment.myprogresstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;



public class CFrag1Editor extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cfrag1_editor, container, false);

        final View infoView = inflater.inflate(R.layout.cfrag1_course_info, container, false);

        MainActivity.switchId = "courseBtn";

        //edit variables
        final EditText editName = rootView.findViewById(R.id.editCName);
        final TextView editStart = rootView.findViewById(R.id.tvCStartDateResult);
        final TextView editEnd = rootView.findViewById(R.id.tvCEndDateResult);
        final RadioGroup rg = rootView.findViewById(R.id.courseRadioGroup);
        //variable to hold course status
        String status = null;

        final Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            //variable to check where data is coming from
            String comingFrom = (String) b.getCharSequence("switchObject");
            switch (comingFrom){
                case "beforeSaveEdit":
                    editName.setText(b.getCharSequence("courseName"));
                    editStart.setText(getDate(b.getString("courseStart")));
                    editEnd.setText(getDate(b.getString("courseEnd")));
                    status = (String) b.getCharSequence("courseStatus");
                    break;
                case "afterSave":
                    editName.setText(b.getCharSequence("savedCourseName"));
                    editStart.setText(b.getCharSequence("savedCourseStart"));
                    editEnd.setText(b.getCharSequence("savedCourseEnd"));
                    status = (String) b.getCharSequence("savedCourseStatus");
                    break;
            }
            for (int i = 0; i < rg.getChildCount(); i++){
                RadioButton button = (RadioButton) rg.getChildAt(i);
                String btnText = button.getText().toString();
                if (btnText.equals(status)){
                    button.toggle();
                    break;
                }
            }
        }

        rootView.findViewById(R.id.saveCFrag1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //info variables
                TextView infoName = infoView.findViewById(R.id.tvCourseNameInfo);
                TextView infoStart = infoView.findViewById(R.id.tvCStartDateInfo);
                TextView infoEnd = infoView.findViewById(R.id.tvCEndDateInfo);
                TextView infoStatus = infoView.findViewById(R.id.tvCourseStatusResult);
                //set intent switch object for CFrag1Editor
                intent.putExtra("switchObject", "afterSave");
                //set course name
                String newName = editName.getText().toString();
                infoName.setText(newName);
                intent.putExtra("savedCourseName", newName);
                //set course start date
                String newStart = editStart.getText().toString();
                infoStart.setText(newStart);
                intent.putExtra("savedCourseStart", newStart);
                //set course end date
                String newEnd = editEnd.getText().toString();
                infoEnd.setText(newEnd);
                intent.putExtra("savedCourseEnd", newEnd);
                //set course status
                int id = rg.getCheckedRadioButtonId();
                View radioView = rg.findViewById(id);
                int index = rg.indexOfChild(radioView);
                RadioButton checkedBtn = (RadioButton) rg.getChildAt(index);
                String newStatus = checkedBtn.getText().toString();
                infoStatus.setText(newStatus);
                intent.putExtra("savedCourseStatus", newStatus);

                CFrag1CourseInfo.updateFragment();
                changeFragment();
            }
        });

        return rootView;
    }

    public static String getDate(String dateWithTag){
        String dateWithNoTag = dateWithTag.substring(0, dateWithTag.length() - 1);
        return dateWithNoTag;
    }


    public void changeFragment(){
        getFragmentManager().popBackStackImmediate();
    }


}