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


public class TFrag2Courses extends ListFragment {

    //populate with selected course from TFrag2Editor
    private static ArrayList<Course> termCourses = new ArrayList<>();

    //custom adaptor
    static TermCourseArrayAdapter adapter;

    //intent object to hold data from handle save method
    static Intent intent;

    //variable to track how many times page is loaded
    private static int fragmentLoadCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tfrag2_courses, container, false);

        MainActivity.switchId = "courseBtn";

        intent = getActivity().getIntent();
        final Bundle b = intent.getExtras();

        //String to hold value for action_insert
        String noDataTermCourses = termCourses.toString();

        //String to hold value for action_edit
        String someDataTermCourses = null;


        if(b.getCharSequence("action").equals(Intent.ACTION_INSERT)){
            intent.putExtra("switchObject", "beforeSaveInsert");
            intent.putExtra("noDataTermCourses", noDataTermCourses);
        }

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            someDataTermCourses = b.getString("termTermCourses");
            if (someDataTermCourses != null && fragmentLoadCount == 0){
                clearCourseMentors();
                try {
                    fromJson(someDataTermCourses);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            fragmentLoadCount++;
        }

        adapter = new TermCourseArrayAdapter(getContext(), R.layout.course_list_item, termCourses);
        final ListView list = rootView.findViewById(android.R.id.list);
        list.setAdapter(adapter);

        rootView.findViewById(R.id.editCourseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
                    intent.putExtra("arrayTermCourses", termCourses);
                }

                Fragment newFragment = new TFrag2Editor();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tFrag2BaseLayout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    public static void addOrUpdateCourseMentors(Course course){
        termCourses.add(course);
        adapter.notifyDataSetChanged();
    }

    public static void clearCourseMentors(){
        termCourses.clear();
        if (!(adapter == null)){
            adapter.notifyDataSetChanged();
        }
    }

    public static boolean isTermCoursesEmpty(){
        boolean result = false;
        if (termCourses.isEmpty()){
            result = true;
        }
        return result;
    }


    public static String toJson(){
        String jsonExams = new Gson().toJson(termCourses);
        return jsonExams;
    }

    public void fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Course course = new Course();
                course.setCourseId(jsonObject.getInt("courseId"));
                course.setCourseName(jsonObject.getString("courseName"));
                course.setCourseStart(jsonObject.getString("courseStart"));
                course.setCourseEnd(jsonObject.getString("courseEnd"));
                course.setCourseStatus(jsonObject.getString("courseStatus"));
                termCourses.add(course);
            }
        }
    }

    public static void resetFragmentLoadCount(){fragmentLoadCount = 0;}

}