package com.jdevelopment.myprogresstracker;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class MentorList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 2001;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_list);

//        insertMentor("Sasha Bordeaux", "(555)555-5555", "sbordeaux@theblackqueen.com");
//        insertMentor("Diana Prince", "(444)444-4444", "dprince@wonderwoman.com");
//        insertMentor("Pamela Lillian Isley", "(333)333-3333", "plisley@poisonivy.com");
//        insertMentor("Selina Kyle", "(888)888-8888", "skyle@catwoman.com");
//        insertMentor("Harley Quin", "(777)777-7777", "hquin@thejokersgirl.com");

        cursorAdapter = new DataCursorAdapter(this, null, 0);
        ListView list = findViewById(R.id.mentorList);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MentorList.this, MentorEditor.class);  //intent from MentorList to MentorEditor
                Uri uri = Uri.parse(DBContentProvider.CONTENT_URI + "/" + id);  //uri object to represent value from currently selected item
                intent.putExtra(DBContentProvider.CONTENT_MENTOR_ITEM_TYPE, uri);  //add uri to intent as extra
                startActivityForResult(intent, EDITOR_REQUEST_CODE);  //launch activity and assign request code
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    private void insertMentor(String name, String phone, String email) {
        ContentValues values = new ContentValues();
        values.put(DBOpener.MENTOR_NAME, name);
        values.put(DBOpener.MENTOR_PHONE, phone);
        values.put(DBOpener.MENTOR_EMAIL, email);
        Uri mentorUri = getContentResolver().insert(DBContentProvider.CONTENT_URI, values);
        Log.d("MentorList", "inserted mentor: " + mentorUri.getLastPathSegment());
    }

    private void restartLoader(){
        getLoaderManager().restartLoader(0,null, this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DBContentProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void handleAddMentorBtn(View view) {
        Intent intent = new Intent(this, MentorEditor.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

}
