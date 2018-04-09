package com.jdevelopment.myprogresstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CFrag4Mentors extends ListFragment {

    //populate with selected mentors from CFrag4Editor
    private static ArrayList<Mentor> courseMentors = new ArrayList<>();

    //custom adaptor
    static CourseMentorArrayAdapter adapter;
    //private static CursorAdapter cursorAdapter;

    //intent object to hold data from handle save method
    static Intent intent;

    //variable to track how many times page is loaded
    private static int fragmentLoadCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cfrag4_mentors, container, false);

        intent = getActivity().getIntent();
        final Bundle b = intent.getExtras();

        //String to hold value for action_insert
        String noDataCourseMentors = courseMentors.toString();

        //String to hold value for action_edit
        String someDataCourseMentors = null;

        if(b.getCharSequence("action").equals(Intent.ACTION_INSERT)){
            intent.putExtra("switchObject", "beforeSaveInsert");
            intent.putExtra("noDataCourseMentors", noDataCourseMentors);
        }

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            someDataCourseMentors = b.getString("courseCourseMentors");
            if (someDataCourseMentors != null && fragmentLoadCount == 0){
                clearCourseMentors();
                try {
                    fromJson(someDataCourseMentors);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            fragmentLoadCount++;
        }

        adapter = new CourseMentorArrayAdapter(getContext(), R.layout.mentor_list_item, courseMentors);
        final ListView list = rootView.findViewById(android.R.id.list);
        list.setAdapter(adapter);

        rootView.findViewById(R.id.addMentorBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
                    intent.putExtra("arrayCourseMentors", courseMentors);
                }

                Fragment newFragment = new CFrag4Editor();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.cFrag4BaseLayout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return rootView;
    }

    public static void addOrUpdateCourseMentors(Mentor mentor){
        courseMentors.add(mentor);
        adapter.notifyDataSetChanged();
    }

    public static void clearCourseMentors(){
        courseMentors.clear();
        if (!(adapter == null)){
            adapter.notifyDataSetChanged();
        }
    }

    public static String toJson(){
        String jsonExams = new Gson().toJson(courseMentors);
        return jsonExams;
    }

    public void fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Mentor mentor = new Mentor();
                mentor.setMentorId(jsonObject.getInt("mentorId"));
                mentor.setMentorName(jsonObject.getString("mentorName"));
                mentor.setMentorPhone(jsonObject.getString("mentorPhone"));
                mentor.setMentorEmail(jsonObject.getString("mentorEmail"));
                courseMentors.add(mentor);
            }
        }
    }

    //getter and setter for fragmentLoadCount
    public static void resetFragmentLoadCount(){fragmentLoadCount = 0;}

}
