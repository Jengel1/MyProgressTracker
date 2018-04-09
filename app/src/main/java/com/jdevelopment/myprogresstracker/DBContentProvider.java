package com.jdevelopment.myprogresstracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class DBContentProvider extends ContentProvider {

    //globally unique string that identifies the location of the content provider
    private static final String AUTHORITY = "com.jdevelopment.myprogresstracker.dbcontentprovider";
    //represents entire data set of DB
    private static final String BASE_PATH = "progress3";
    //uri that identifies content provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int DATA = 1;  //reference to all data from DB
    private static final int MENTOR_ID = 2;  //reference to only a single record from mentor table
    private static final int COURSE_ID = 3;  //reference to only a single record from course table
    private static final int TERM_ID = 4;  //reference to only a single record from term table
    private static final int NOTE_ID = 5;  //reference to only a single record from the note table
    //add exam id

    //constants to reference item types
    public static final String CONTENT_MENTOR_ITEM_TYPE = "Mentor";
    public static final String CONTENT_COURSE_ITEM_TYPE = "Course";
    public static final String CONTENT_TERM_ITEM_TYPE = "Term";
    public static final String CONTENT_NOTE_ITEM_TYPE = "Note";
    //add exam

    //purpose is to parse uri and return which operation has been requested
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer.  Executes the first time anything is called from this calss
    //there are five operations possible
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, DATA);  //return all data
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MENTOR_ID);  //return particular mentor
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", COURSE_ID);  //return particular course
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TERM_ID);  //return particular term
//        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTE_ID);  //return particular note
        //add exam
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        DBOpener opener = new DBOpener(getContext());
        db = opener.getWritableDatabase();
        return true;
    }

    //method to get data from specified database table
    //returns either all data from table or just a single row
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] listOfColumns, @Nullable String whereClause, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //declare variables first
        String tableName = null;
        String btnPressed = MainActivity.switchId;
        Log.d("DBContentProvider", "btn= " + btnPressed);

        switch (btnPressed){
            case "mentorsBtn":
                tableName = DBOpener.TABLE_MENTOR;
                listOfColumns = DBOpener.ALL_MENTOR_COLUMNS;
                sortOrder = DBOpener.MENTOR_CREATED;
                if (uriMatcher.match(uri) == MENTOR_ID){
                    whereClause = DBOpener.MENTOR_ID + "=" + uri.getLastPathSegment();
                }
                break;
            case "courseBtn":
                tableName = DBOpener.TABLE_COURSE;
                listOfColumns = DBOpener.ALL_COURSE_COLUMNS;
                sortOrder = DBOpener.COURSE_CREATED;
                if (uriMatcher.match(uri) == COURSE_ID){
                    whereClause = DBOpener.COURSE_ID + "=" + uri.getLastPathSegment();
                }
                break;
            case "termsBtn":
                tableName = DBOpener.TABLE_TERM;
                listOfColumns = DBOpener.ALL_TERM_COLUMNS;
                sortOrder = DBOpener.TERM_CREATED;
                if (uriMatcher.match(uri) == TERM_ID){
                    whereClause = DBOpener.TERM_ID + "=" + uri.getLastPathSegment();
                }
                break;
        }
        return db.query(tableName, listOfColumns, whereClause,
                null, null, null, sortOrder + " DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Long id = null;
        String fromEditor = MainActivity.editorFlag;
        switch (fromEditor){
            case "mentorEditor":
                id = db.insert(DBOpener.TABLE_MENTOR, null, values);
                break;
            case "courseEditor":
                id = db.insert(DBOpener.TABLE_COURSE, null, values);
                break;
            case "termEditor":
                id = db.insert(DBOpener.TABLE_TERM, null, values);
                break;
        }
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] selectionArgs) {
        String tableName = null;
        String fromEditor = MainActivity.editorFlag;
        switch (fromEditor){
            case "mentorEditor":
                tableName = DBOpener.TABLE_MENTOR;
                break;
            case "courseEditor":
                tableName = DBOpener.TABLE_COURSE;
                break;
            case "termEditor":
                tableName = DBOpener.TABLE_TERM;
                break;
        }
        return db.delete(tableName, whereClause, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String whereClause, @Nullable String[] selectionArgs) {
        String tableName = null;
        String fromEditor = MainActivity.editorFlag;
        switch (fromEditor){
            case "mentorEditor":
                tableName = DBOpener.TABLE_MENTOR;
                break;
            case "courseEditor":
                tableName = DBOpener.TABLE_COURSE;
                break;
            case "termEditor":
                tableName = DBOpener.TABLE_TERM;
                break;
        }
        return db.update(tableName, values, whereClause, selectionArgs);
    }

}
