package com.jdevelopment.myprogresstracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class TFrag2Editor extends ListFragment {

    //populate with query from db
    private ArrayList<Course> allCourses = new ArrayList<>();

    //custom adaptor
    CourseArrayAdaptor adapter;

    //intent object to hold data from handle save method
    static Intent intent;

    //global declaration of ListView
    ListView list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tfrag2_editor, container, false);

        MainActivity.switchId = "courseBtn";

        intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        adapter = new CourseArrayAdaptor(getContext(), R.layout.course_list_item_editor, allCourses);
        list = rootView.findViewById(android.R.id.list);
        list.setAdapter(adapter);

        if (b.getCharSequence("action").equals(Intent.ACTION_INSERT)){
            Uri uriInsert = DBContentProvider.CONTENT_URI;
            loadAllCourses(uriInsert);
        }

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            Uri uriEdit = b.getParcelable("uriTerm");  //from TermList 41 if action is edit
            loadAllCourses(uriEdit);
            setCheckBoxForExistingTermCourses();
        }

        rootView.findViewById(R.id.saveCoursesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TFrag2Courses.clearCourseMentors();
                for (int i = 0; i < list.getAdapter().getCount(); i++){
                    Course course = adapter.getItem(i);
                    if (course.isCheckedTerm()){
                        TFrag2Courses.addOrUpdateCourseMentors(allCourses.get(i));
                    }
                }
                changeFragment();
            }
        });
        return rootView;
    }

    /*
    Method to loop through ArrayList courseMentors and for each MentorId that matches MentorId in ArrayList allMentors
    Checkbox is toggled on
     */
    private void setCheckBoxForExistingTermCourses(){
        Bundle b = intent.getExtras();
        ArrayList<Course> termCourses = (ArrayList<Course>) b.getSerializable("arrayTermCourses");
        for (Course c : termCourses) {
            for (int i = 0; i < list.getAdapter().getCount(); i++){
                Course course = adapter.getItem(i);
                if (c.getCourseId() == course.getCourseId()){
                    course.toggleCheckedTerm();
                    break;
                }
            }
        }
    }

    /*
    Method to load ArrayList allMentors from db query
     */
    private void loadAllCourses(Uri uri){
        Bundle b = intent.getExtras();
        //create cursor object to query db
        Cursor cursor = getActivity().getContentResolver().query(uri, DBOpener.ALL_COURSE_COLUMNS, null,
                null, null);
        //move cursor to first row
        cursor.moveToFirst();
        //loop through rows
        for (int i = 0; i < cursor.getCount(); i++){
            //create new Mentor
            Course course = new Course();
            course.setCourseId(cursor.getInt(cursor.getColumnIndex(DBOpener.COURSE_ID)));
            course.setCourseName(cursor.getString(cursor.getColumnIndexOrThrow(DBOpener.COURSE_NAME)));
            course.setCourseStart(cursor.getString(cursor.getColumnIndexOrThrow(DBOpener.COURSE_START)));
            course.setCourseEnd(cursor.getString(cursor.getColumnIndexOrThrow(DBOpener.COURSE_END)));
            course.setCourseStatus(cursor.getString(cursor.getColumnIndexOrThrow(DBOpener.COURSE_STATUS)));
            //add mentor to allMentors arraylist
            allCourses.add(course);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void changeFragment(){
        getFragmentManager().popBackStackImmediate();
    }

}