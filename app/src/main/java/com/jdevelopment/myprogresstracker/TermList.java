package com.jdevelopment.myprogresstracker;

import android.app.LoaderManager;
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

public class TermList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 4001;
    private CursorAdapter cursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        MainActivity.editorFlag = "termEditor";

        cursorAdapter = new DataCursorAdapter(this, null, 0);

        ListView list = findViewById(R.id.termList);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermList.this, TermDetails.class);  //intent from CourseList to CourseDetails
                Uri uri = Uri.parse(DBContentProvider.CONTENT_URI + "/" + id);  //uri object to represent value from currently selected item
                intent.putExtra(DBContentProvider.CONTENT_TERM_ITEM_TYPE, uri);  //add uri to intent as extra
                //placing content and uri extras for use in TFrag2Editor db query
                intent.putExtra("uriTerm", uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);  //launch activity and assign request code

                TFrag2Courses.resetFragmentLoadCount();
            }
        });

        getLoaderManager().initLoader(0, null, this);

    }

    private void restartLoader(){
        getLoaderManager().restartLoader(0,null, this);
    }

    public void handleAddTermBtn(View view) {
        Intent intent = new Intent(this, TermDetails.class);
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
