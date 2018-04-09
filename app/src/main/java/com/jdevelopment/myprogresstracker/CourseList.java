package com.jdevelopment.myprogresstracker;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.app.LoaderManager;

public class CourseList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 3001;
    private CursorAdapter cursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        MainActivity.editorFlag = "courseEditor";

        cursorAdapter = new DataCursorAdapter(this, null, 0);

        ListView list = findViewById(R.id.courseList);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseList.this, CourseDetails.class);  //intent from CourseList to CourseDetails
                Uri uri = Uri.parse(DBContentProvider.CONTENT_URI + "/" + id);  //uri object to represent value from currently selected item
                intent.putExtra(DBContentProvider.CONTENT_COURSE_ITEM_TYPE, uri);  //add uri to intent as extra
                //placing content and uri extras for use in CFrag4Editor db query
                intent.putExtra("uriCourse", uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);  //launch activity and assign request code

                //reset all fragmentLoadCounts
                CFrag2Exams.resetFragmentLoadCount();
                CFrag3Notes.resetFragmentLoadCount();
                CFrag4Mentors.resetFragmentLoadCount();
            }
        });

        getLoaderManager().initLoader(0, null, this);

    }

    private void insertCourse(String name, String startDate, String endDate){
        ContentValues values = new ContentValues();
        values.put(DBOpener.COURSE_NAME, name);
        values.put(DBOpener.COURSE_START, startDate);
        values.put(DBOpener.COURSE_END, endDate);
        Uri courseUri = getContentResolver().insert(DBContentProvider.CONTENT_URI, values);
    }

    private void restartLoader(){
        getLoaderManager().restartLoader(0,null, this);
    }

    public void handleAddCourseBtn(View view) {
        Intent intent = new Intent(this, CourseDetails.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }


    /*
    Loader Manager methods
    */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, DBContentProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {cursorAdapter.swapCursor(data); }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

}
