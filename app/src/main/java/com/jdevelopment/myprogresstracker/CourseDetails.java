package com.jdevelopment.myprogresstracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class CourseDetails extends AppCompatActivity {

    //variable to track each fragment section
    private SectionsPagerAdapter mSectionsPagerAdapter;
    //holds the content for each fragment
    private ViewPager mViewPager;

    //date picker object
    DatePickerDialog datePickerDialog;

    //variable to track activity action
    private String action;

    //Intent object to package data and pass between fragments
    Intent intent;


    //---------------- Course Fragments variables -----------------------
    //variable to use in whereClause
    private String courseFilter;
    //variables to hold existing course values
    private String existingName;
    private String existingStart;
    private String existingEnd;
    private String existingStatus;
    private String existingExams;
    private String existingNotes;
    private String existingMentors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the tabs
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //get intention and uri from CourseList activity action
        intent = getIntent();
        Uri uri = intent.getParcelableExtra(DBContentProvider.CONTENT_COURSE_ITEM_TYPE);

        if (uri == null){
            //insert new course
            action = Intent.ACTION_INSERT;
            setTitle("New course");  //not working
            intent.putExtra("action", action);
        }
        else {
            //edit existing course
            action = Intent.ACTION_EDIT;
            courseFilter = DBOpener.COURSE_ID + "=" + uri.getLastPathSegment();
            //create cursor to give access to the one row requested
            MainActivity.switchId = "courseBtn";
            Cursor cursor = getContentResolver().query(uri, DBOpener.ALL_COURSE_COLUMNS, courseFilter,
                    null, null);
            cursor.moveToFirst();
            //add extras to intent to set action and values in CFrag1CourseInfo and CFrag1Editor
            //set intent switch object for CFrag1Editor
            intent.putExtra("switchObject", "beforeSave");
            //set course variables
            intent.putExtra("action", action);
            existingName = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_NAME));
            intent.putExtra("courseName", existingName);
            existingStart = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_START));
            intent.putExtra("courseStart", existingStart);
            existingEnd = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_END));
            intent.putExtra("courseEnd", existingEnd);
            existingStatus = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_STATUS));
            intent.putExtra("courseStatus", existingStatus);
            //course exams
            existingExams = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_EXAMS));
            intent.putExtra("courseExams", existingExams);
            //course notes
            existingNotes = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_NOTES));
            intent.putExtra("courseNotes", existingNotes);
            //course mentors
            existingMentors = cursor.getString(cursor.getColumnIndex(DBOpener.COURSE_MENTORS));
            intent.putExtra("courseCourseMentors", existingMentors);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteCourse();
                break;
        }
        return true;
    }

    /*
    Method to handle all date picker dialogs
    */
    private void handleDatePicker(final TextView resultEdit){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);  //current year
        int month = calendar.get(Calendar.MONTH);  //current month
        int day = calendar.get(Calendar.DAY_OF_MONTH);  //current day
        //initiate datePickerDialoge
        datePickerDialog = new DatePickerDialog(CourseDetails.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                resultEdit.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);  //set date text on editor scrn
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    /*
    ---------------------------- Methods for Course Fragments -------------------------------
    */

    private void deleteCourse() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE){
                    getContentResolver().delete(DBContentProvider.CONTENT_URI, courseFilter, null);
                    Toast.makeText(CourseDetails.this, "Course deleted", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this course?")
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void updateCourse(String name, String start, String end, String status, String exams, String notes, String mentors) {
        ContentValues values = new ContentValues();
        values.put(DBOpener.COURSE_NAME, name);
        values.put(DBOpener.COURSE_START, start);
        values.put(DBOpener.COURSE_END, end);
        values.put(DBOpener.COURSE_STATUS, status);
        values.put(DBOpener.COURSE_EXAMS, exams);
        values.put(DBOpener.COURSE_NOTES, notes);
        values.put(DBOpener.COURSE_MENTORS, mentors);
        getContentResolver().update(DBContentProvider.CONTENT_URI, values, courseFilter, null);
        Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertCourse(String name, String start, String end, String status, String exams, String notes, String mentors) {
        ContentValues values = new ContentValues();
        values.put(DBOpener.COURSE_NAME, name);
        values.put(DBOpener.COURSE_START, start);
        values.put(DBOpener.COURSE_END, end);
        values.put(DBOpener.COURSE_STATUS, status);
        values.put(DBOpener.COURSE_EXAMS, exams);
        values.put(DBOpener.COURSE_NOTES, notes);
        values.put(DBOpener.COURSE_MENTORS, mentors);
        getContentResolver().insert(DBContentProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }


    /*
    Button handlers for CFrag1Editor date pickers
    */
    public void handleCStartDateBtn(View view) {
        final TextView resultEdit = findViewById(R.id.tvCStartDateResult);  //date text on editor scrn
        //final TextView resultInfo = findViewById(R.id.tvCStartDateInfo);  //date text on info scrn
        handleDatePicker(resultEdit);
    }
    public void handleCEndDateBtn(View view) {
        final TextView resultEdit = findViewById(R.id.tvCEndDateResult);  //date text on editor scrn
        //final TextView resultInfo = findViewById(R.id.tvCEndDateInfo);  //date text on editor scrn
        handleDatePicker(resultEdit);
    }


    /*
    Method to finish Editing course
     */
    private void finishEditing(){
        MainActivity.switchId = "courseBtn";
        //bundle holding comingFrom values
        Bundle b = intent.getExtras();
        //Declare variables to hold finished values
        String finishName = null;
        String finishStart = null;
        String finishEnd = null;
        String finishStatus = null;
        String finishExams = null;
        String finishNotes = null;
        String finishMentors = null;
        //course alert values to append to date text object in db
        //for insert course alerts
        String startAlertStatus = b.getString("startAlertStatus");
        String endAlertStatus = b.getString("endAlertStatus");
        String startAlertTag = "F";  //F == no alert set, T == alert set
        String endAlertTag = "F";
        if(startAlertStatus != null && startAlertStatus.equals("alertOn")){
            startAlertTag = "T";
        }
        if(endAlertStatus != null && endAlertStatus.equals("alertOn")){
            endAlertTag = "T";
        }
        //for edit course alerts

        //variable to check where data is coming from
        String comingFrom = (String) b.getCharSequence("switchObject");

        switch (comingFrom){
            case "beforeSaveInsert":
                finishName = (String) b.getCharSequence("noDataName");
                finishStart = (String) b.getCharSequence("noDataStart") + startAlertTag;
                finishEnd = (String) b.getCharSequence("noDataEnd") + endAlertTag;
                finishStatus = (String) b.getCharSequence("noDataStatus");
                finishExams = CFrag2Exams.toJson();
                finishNotes = CFrag3Notes.toJson();
                finishMentors = CFrag4Mentors.toJson();
                break;
            case "beforeSaveEdit":
                finishName = (String) b.getCharSequence("someDataName");
                finishStart = (String) b.getCharSequence("someDataStart") + startAlertTag;
                finishEnd = (String) b.getCharSequence("someDataEnd") + endAlertTag;
                finishStatus = (String) b.getCharSequence("someDataStatus");
                finishExams = CFrag2Exams.toJson();
                finishNotes = CFrag3Notes.toJson();
                finishMentors = CFrag4Mentors.toJson();
                break;
            case "afterSave":
                finishName = (String) b.getCharSequence("finalCourseName");
                finishStart = (String) b.getCharSequence("finalCourseStart") + startAlertTag;
                finishEnd = (String) b.getCharSequence("finalCourseEnd") + endAlertTag;
                finishStatus = (String) b.getCharSequence("finalCourseStatus");
                finishExams = CFrag2Exams.toJson();
                finishNotes = CFrag3Notes.toJson();
                finishMentors = CFrag4Mentors.toJson();
                break;
        }

        switch (action){
            case Intent.ACTION_INSERT:
                if ((finishName.length() == 0) && (finishStart.length() == 0) &&
                        (finishEnd.length() == 0) && (finishStatus.length() == 0)){
                    //values before save
                    setResult(RESULT_CANCELED);
                }
                else {
                    //values after save
                    insertCourse(finishName, finishStart, finishEnd, finishStatus, finishExams, finishNotes, finishMentors);
                }
                break;
            case Intent.ACTION_EDIT:
                //if old values equal new values
                if ((finishName.equals(existingName)) && (finishStart.equals(existingStart)) &&
                        (finishEnd.equals(existingEnd)) && (finishStatus.equals(existingStatus)) &&
                        (finishExams.equals(existingExams)) && (finishNotes.equals(existingNotes)) &&
                        (finishMentors.equals(existingMentors))){
                    //values before save
                    setResult(RESULT_CANCELED);
                }
                else {
                    //values after save
                    updateCourse(finishName, finishStart, finishEnd, finishStatus, finishExams, finishNotes, finishMentors);
                }
        }
        finish();
    }

    /*
    ---------------------------- Methods for Exam Fragments -------------------------------
    */
    public void handleAddExamBtn(View view) {
        Fragment newFragment = new CFrag2Editor();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cFrag2BaseLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        intent.putExtra("newOrExisting", "newExam");
        View button = view.findViewById(R.id.addExamBtn);
        button.setVisibility(View.INVISIBLE);
    }

    /*
    Handle method for CFrag2Editor date picker
     */
    public void handleExamDueDateBtn(View view) {
        final TextView resultEdit = findViewById(R.id.examDueDateResult);  //date text on editor scrn
        //final TextView resultInfo = findViewById(R.id.examDueDate);  //date text on editor scrn
        handleDatePicker(resultEdit);
    }



    /*
    ---------------------------- Methods for Note Fragments -------------------------------
    */
    public void handleAddNoteBtn(View view) {
        Fragment newFragment = new CFrag3Editor();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cFrag3BaseLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        intent.putExtra("newOrExisting", "newNote");
        View button = view.findViewById(R.id.addNoteBtn);
        button.setVisibility(View.GONE);
    }



    @Override
    public void onBackPressed() {
        finishEditing();
    }

    /*
    SectionsPagerAdapter class
    Methods to manage fragments within tabs
    */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    CFrag1CourseInfo tab1 = new CFrag1CourseInfo();
                    return tab1;
                case 1:
                    CFrag2Exams tab2 = new CFrag2Exams();
                    return tab2;
                case 2:
                    CFrag3Notes tab3 = new CFrag3Notes();
                    return tab3;
                case 3:
                    CFrag4Mentors tab4 = new CFrag4Mentors();
                    return tab4;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Course Info";
                case 1:
                    return "Exams";
                case 2:
                    return "Notes";
                case 3:
                    return "Mentors";
            }
            return null;
        }
    }
}