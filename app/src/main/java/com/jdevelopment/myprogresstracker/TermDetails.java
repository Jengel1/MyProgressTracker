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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TermDetails extends AppCompatActivity {

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

    //---------------- Term Fragments variables -----------------------
    //variables from TFrag1TermInfo
    private TextView infoName;
    private TextView infoStart;
    private TextView infoEnd;
    private TextView infoStatus;
    //variable from TFrag1Editor
    private EditText editName;
    private TextView editStartResult;
    private TextView editEndResult;
    private TextView editStatus;
    //variable to use in whereClause
    private String termFilter;
    //variables to hold existing term values
    private String existingName;
    private String existingStart;
    private String existingEnd;
    private String existingStatus;
    private String existingCourses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

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
        Uri uri = intent.getParcelableExtra(DBContentProvider.CONTENT_TERM_ITEM_TYPE);

        if (uri == null){
            //insert new course
            action = Intent.ACTION_INSERT;
            setTitle("New term");  //not working
            intent.putExtra("action", action);
        }
        else {
            //edit existing course
            action = Intent.ACTION_EDIT;
            termFilter = DBOpener.TERM_ID + "=" + uri.getLastPathSegment();
            //create cursor to give access to the one row requested
            MainActivity.switchId = "termsBtn";
            Cursor cursor = getContentResolver().query(uri, DBOpener.ALL_TERM_COLUMNS, termFilter,
                    null, null);
            cursor.moveToFirst();
            //add extras to intent to set action and values in CFrag1CourseInfo and CFrag1Editor
            //set intent switch object for CFrag1Editor
            intent.putExtra("switchObject", "beforeSave");
            //set course variables
            intent.putExtra("action", action);
            existingName = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_NAME));
            intent.putExtra("termName", existingName);
            existingStart = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_START));
            intent.putExtra("termStart", existingStart);
            existingEnd = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_END));
            intent.putExtra("termEnd", existingEnd);
            existingStatus = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_STATUS));
            intent.putExtra("termStatus", existingStatus);
            //term courses
            existingCourses = cursor.getString(cursor.getColumnIndex(DBOpener.TERM_COURSES));
            intent.putExtra("termTermCourses", existingCourses);
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
                deleteTerm();
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
        datePickerDialog = new DatePickerDialog(TermDetails.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                resultEdit.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);  //set date text on editor scrn
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    /*
    ---------------------------- Methods for Term Fragments -------------------------------
    */

    private void deleteTerm() {
        MainActivity.switchId = "termsBtn";

        if(TFrag2Courses.isTermCoursesEmpty()) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int button) {
                    if (button == DialogInterface.BUTTON_POSITIVE) {
                        getContentResolver().delete(DBContentProvider.CONTENT_URI, termFilter, null);
                        Toast.makeText(TermDetails.this, "Term deleted", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this term?")
                    .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                    .show();
        }
        else {
            //you cannot delete a term that contains courses
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int button) {
                    if (button == DialogInterface.BUTTON_NEUTRAL) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You cannot delete a term that contains courses!")
                    .setNeutralButton("Ok", dialogClickListener)
                    .show();
        }

    }

    private void updateTerm(String name, String start, String end, String status, String termCourses) {
        ContentValues values = new ContentValues();
        values.put(DBOpener.TERM_NAME, name);
        values.put(DBOpener.TERM_START, start);
        values.put(DBOpener.TERM_END, end);
        values.put(DBOpener.TERM_STATUS, status);
        values.put(DBOpener.TERM_COURSES, termCourses);
        getContentResolver().update(DBContentProvider.CONTENT_URI, values, termFilter, null);
        Toast.makeText(this, "Term updated", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertTerm(String name, String start, String end, String status, String termCourses) {
        ContentValues values = new ContentValues();
        values.put(DBOpener.TERM_NAME, name);
        values.put(DBOpener.TERM_START, start);
        values.put(DBOpener.TERM_END, end);
        values.put(DBOpener.TERM_STATUS, status);
        values.put(DBOpener.TERM_COURSES, termCourses);
        getContentResolver().insert(DBContentProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }


    /*
    Button handlers for TFrag1Editor date pickers
     */
    public void handleTStartDateBtn(View view) {
        final TextView resultEdit = findViewById(R.id.tvTStartDateResult);  //date text on editor scrn
        handleDatePicker(resultEdit);
    }
    public void handleTEndDateBtn(View view) {
        final TextView resultEdit = findViewById(R.id.tvTEndDateResult);  //date text on editor scrn
        handleDatePicker(resultEdit);
    }

    /*
    Method to finish Editing term
     */
    private void finishEditing(){
        MainActivity.switchId = "termsBtn";
        //bundle holding comingFrom values
        Bundle b = intent.getExtras();
        //Declare variables to hold finished values
        String finishName = null;
        String finishStart = null;
        String finishEnd = null;
        String finishStatus = null;
        String finishTermCourses = null;
        //variable to check where data is coming from
        String comingFrom = (String) b.getCharSequence("switchObject");

        switch (comingFrom){
            case "beforeSaveInsert":
                finishName = (String) b.getCharSequence("noDataName");
                finishStart = (String) b.getCharSequence("noDataStart");
                finishEnd = (String) b.getCharSequence("noDataEnd");
                finishStatus = (String) b.getCharSequence("noDataStatus");
                finishTermCourses = TFrag2Courses.toJson();
                break;
            case "beforeSaveEdit":
                finishName = (String) b.getCharSequence("someDataName");
                finishStart = (String) b.getCharSequence("someDataStart");
                finishEnd = (String) b.getCharSequence("someDataEnd");
                finishStatus = (String) b.getCharSequence("someDataStatus");
                finishTermCourses = TFrag2Courses.toJson();
                break;
            case "afterSave":
                finishName = b.getString("finalTermName");
                finishStart = b.getString("finalTermStart");
                finishEnd = b.getString("finalTermEnd");
                finishStatus = b.getString("finalTermStatus");
                finishTermCourses = TFrag2Courses.toJson();
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
                    insertTerm(finishName, finishStart, finishEnd, finishStatus, finishTermCourses);
                }
                break;
            case Intent.ACTION_EDIT:
                //if old name equals new name
                if ((finishName.equals(existingName)) && (finishStart.equals(existingStart)) &&
                        (finishEnd.equals(existingEnd)) && (finishStatus.equals(existingStatus)) &&
                        (finishTermCourses.equals(existingCourses))){
                    //values before save
                    setResult(RESULT_CANCELED);
                }
                else {
                    //values after save
                    updateTerm(finishName, finishStart, finishEnd, finishStatus, finishTermCourses);
                }
        }
        finish();
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
                    TFrag1TermInfo tab1 = new TFrag1TermInfo();
                    return tab1;
                case 1:
                    TFrag2Courses tab2 = new TFrag2Courses();
                    return tab2;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Term Info";
                case 1:
                    return "Courses";
            }
            return null;
        }
    }
}
