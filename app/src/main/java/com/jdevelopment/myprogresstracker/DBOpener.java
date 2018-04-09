package com.jdevelopment.myprogresstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpener extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "progress3.db";  //DB name must include file extention .db
    private static final int DATABASE_VERSION = 1;  //always set to 1 the first time DB is created


    /*
    Mentor Table Constants
     */
    //Constant for identifying mentor table
    public static final String TABLE_MENTOR = "mentor";
    //Constants for identifying mentor columns
    public static final String MENTOR_ID = "_id";
    public static final String MENTOR_NAME = "mentorName";
    public static final String MENTOR_PHONE = "mentorPhone";
    public static final String MENTOR_EMAIL = "mentorEmail";
    public static final String MENTOR_CREATED = "mentorCreated";
    //Constant for retrieving mentor data
    public static final String[] ALL_MENTOR_COLUMNS = {MENTOR_ID, MENTOR_NAME, MENTOR_PHONE, MENTOR_EMAIL, MENTOR_CREATED};
    //SQL statment to create mentor table
    private static final String MENTOR_TABLE_CREATE =
            "CREATE TABLE " + TABLE_MENTOR + " (" +
                    MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MENTOR_NAME + " TEXT, " +
                    MENTOR_PHONE + " TEXT, " +
                    MENTOR_EMAIL + " TEXT, " +
                    MENTOR_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";


    /*
    Course Table Constants
     */
    //Constant for identifying mentor table
    public static final String TABLE_COURSE = "course";
    //Constants for identifying mentor columns
    public static final String COURSE_ID = "_id";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_START = "courseStart";
    public static final String COURSE_END = "courseEnd";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_EXAMS = "courseExams";
    public static final String COURSE_NOTES = "courseNotes";
    public static final String COURSE_MENTORS = "courseMentors";
    public static final String COURSE_CREATED = "courseCreated";
    //public static final String COURSE_TERMID = "courseTermId";  //add in later
    //Constant for retrieving mentor data
    public static final String[] ALL_COURSE_COLUMNS = {COURSE_ID, COURSE_NAME, COURSE_START,
            COURSE_END, COURSE_STATUS, COURSE_EXAMS, COURSE_NOTES, COURSE_MENTORS, COURSE_CREATED};
    //SQL statement to create mentor table
    private static final String COURSE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSE + " (" +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NAME + " TEXT, " +
                    COURSE_START + " TEXT, " +
                    COURSE_END + " TEXT, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_EXAMS + " TEXT, " +
                    COURSE_NOTES + " TEXT, " +
                    COURSE_MENTORS + " TEXT, " +
                    COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";


    /*
    Term table constants
     */
    //constant for identifying term table
    public static final String TABLE_TERM = "term";
    //constants for identifying term columns
    public static final String TERM_ID = "_id";
    public static final String TERM_NAME = "termName";
    public static final String TERM_START = "termStart";
    public static final String TERM_END = "termEnd";
    public static final String TERM_STATUS = "termStatus";
    public static final String TERM_COURSES = "termCourses";
    public static final String TERM_CREATED = "termCreated";
    //constant for retrieving term data
    public static final String[] ALL_TERM_COLUMNS = {TERM_ID, TERM_NAME, TERM_START, TERM_END, TERM_STATUS, TERM_COURSES, TERM_CREATED};
    //SQL statement to create term table
    public static final String TERM_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERM + " (" +
                    TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_NAME + " TEXT, " +
                    TERM_START + " TEXT, " +
                    TERM_END + " TEXT, " +
                    TERM_STATUS + " TEXT, " +
                    TERM_COURSES + " TEXT, " +
                    TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";


    public DBOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MENTOR_TABLE_CREATE);  //SQL statement to create mentor table
        db.execSQL(COURSE_TABLE_CREATE);  //SQL statement to create course table
        db.execSQL(TERM_TABLE_CREATE);  //SQL statement to create term table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENTOR);  //drop mentor table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);  //drop course table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERM);  //drop term table if exists
        onCreate(db);
    }
}
