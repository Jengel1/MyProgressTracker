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

public class CFrag4Editor extends ListFragment {

    //populate with query from db
    private ArrayList<Mentor> allMentors = new ArrayList<>();

    //custom adaptor or current cursor adapter
    MentorArrayAdapter adapter;

    //intent object to hold data from handle save method
    static Intent intent;

    //global declaration of ListView
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cfrag4_editor, container, false);

        MainActivity.switchId = "mentorsBtn";

        intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        adapter = new MentorArrayAdapter(getContext(), R.layout.mentor_list_item_editor, allMentors);
        list = rootView.findViewById(android.R.id.list);
        list.setAdapter(adapter);

        if (b.getCharSequence("action").equals(Intent.ACTION_INSERT)){
            Uri uriInsert = DBContentProvider.CONTENT_URI;
            loadAllMentors(uriInsert);
        }

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            Uri uriEdit = b.getParcelable("uriCourse");  //from CourseList 45 if action is edit
            loadAllMentors(uriEdit);
            setCheckBoxForExistingCourseMentors();
        }

        rootView.findViewById(R.id.saveMentorBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CFrag4Mentors.clearCourseMentors();
                    for (int i = 0; i < list.getAdapter().getCount(); i++){
                        Mentor mentor = adapter.getItem(i);
                        if (mentor.isChecked()){
                            CFrag4Mentors.addOrUpdateCourseMentors(allMentors.get(i));
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
    private void setCheckBoxForExistingCourseMentors(){
        Bundle b = intent.getExtras();
        ArrayList<Mentor> courseMentors = (ArrayList<Mentor>) b.getSerializable("arrayCourseMentors");
        for (Mentor m : courseMentors) {
            for (int i = 0; i < list.getAdapter().getCount(); i++){
                Mentor mentor = adapter.getItem(i);
                if (m.getMentorId() == mentor.getMentorId()){
                    mentor.toggleChecked();
                    break;
                }
            }
        }
    }

    /*
    Method to load ArrayList allMentors from db query
     */
    private void loadAllMentors(Uri uri){
        Bundle b = intent.getExtras();
        //create cursor object to query db
        Cursor cursor = getActivity().getContentResolver().query(uri, DBOpener.ALL_MENTOR_COLUMNS, null,
                null, null);
        //move cursor to first row
        cursor.moveToFirst();
        //loop through rows
        for (int i = 0; i < cursor.getCount(); i++){
            //create new Mentor
            Mentor mentor = new Mentor();
            mentor.setMentorId(cursor.getInt(cursor.getColumnIndex(DBOpener.MENTOR_ID)));
            mentor.setMentorName(cursor.getString(cursor.getColumnIndexOrThrow(DBOpener.MENTOR_NAME)));
            mentor.setMentorPhone(cursor.getString(cursor.getColumnIndexOrThrow(DBOpener.MENTOR_PHONE)));
            mentor.setMentorEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBOpener.MENTOR_EMAIL)));
            //add mentor to allMentors arraylist
            allMentors.add(mentor);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void changeFragment(){
        getFragmentManager().popBackStackImmediate();
    }

}