package com.jdevelopment.myprogresstracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //identifiers for use throughout application
    public static String editorFlag = null;
    public static String switchId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleTermsBtn(View view) {
        switchId = "termsBtn";
        Intent intent = new Intent(this, TermList.class);
        startActivity(intent);
    }

    public void handleCoursesBtn(View view) {
        switchId = "courseBtn";
        Intent intent = new Intent(this, CourseList.class);
        startActivity(intent);
    }

    public void handleMentorBtn(View view) {
        switchId = "mentorsBtn";
        Intent intent = new Intent(this, MentorList.class);
        startActivity(intent);
    }

}