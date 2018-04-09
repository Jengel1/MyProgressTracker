package com.jdevelopment.myprogresstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CFrag2Exams extends ListFragment {

    //variable to hold list of exams
    static ArrayList<Exam> exams = new ArrayList<>();

    //variables to hold exam info for array
    static String listExamType;
    static String listExamDate;
    static String listExamStatus;
    static String listExamAlert;

    //intent object to hold data from handle save method
    static Intent intent;

    //custom adapter to display list items
    static ExamArrayAdapter adapter;

    //variable to track how many times page is loaded
    private static int fragmentLoadCount = 0;

    View rootView;
    View editorView;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cfrag2_exams, container, false);
        editorView = inflater.inflate(R.layout.cfrag2_editor, container, false);

        MainActivity.switchId = "courseBtn";

        intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        //String to hold value for action_insert
        String noDataExams = exams.toString();

        //String to hold value for action_edit
        String someDataExams = null;


        if(b.getCharSequence("action").equals(Intent.ACTION_INSERT)){
            intent.putExtra("switchObject", "beforeSaveInsert");
            intent.putExtra("noDataExams", noDataExams);
        }

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            someDataExams = b.getString("courseExams");
            if (someDataExams != null && fragmentLoadCount == 0){
                clearExams();
                try {
                    fromJson(someDataExams);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            fragmentLoadCount++;
            intent.putExtra("arrayExams", someDataExams);
        }

        adapter = new ExamArrayAdapter(getContext(), R.layout.exam_list_item, exams);
        list = rootView.findViewById(android.R.id.list);
        list.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("onClick", "Clicked on item: " + position + " and " + id);

        Exam existingExam = exams.get(position);
        String existingExamType = existingExam.getExamType();
        String existingExamDate = existingExam.getExamDate();
        String existingExamStatus = existingExam.getExamSatus();
        boolean existingExamAlertStatus = existingExam.isChecked();

        intent.putExtra("action", Intent.ACTION_EDIT);
        intent.putExtra("position", position);
        intent.putExtra("newOrExisting", "existingExam");
        intent.putExtra("existingExamType", existingExamType);
        intent.putExtra("existingExamDate", existingExamDate);
        intent.putExtra("existingExamStatus", existingExamStatus);
        intent.putExtra("existingExamAlertStatus", existingExamAlertStatus);

        Fragment newFragment = new CFrag2Editor();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cFrag2BaseLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addOrUpdateExam() {
        Bundle b = intent.getExtras();
        listExamType = b.getString("examType");
        listExamDate = b.getString("examDate");
        listExamStatus = b.getString("examStatus");
        listExamAlert = b.getString("alertStatus");
        //marker to indicate whether new exam or existing exam
        String newOrExisting = b.getString("newOrExisting");

        switch (newOrExisting){
            case "newExam":
                if (exams.isEmpty()){
                    exams.add(new Exam());
                    exams.get(0).setExamId(1);
                    exams.get(0).setExamType(listExamType);
                    exams.get(0).setExamDate(listExamDate);
                    exams.get(0).setExamStatus(listExamStatus);
                    if (listExamAlert.equals("alertOn")){
                        exams.get(0).setChecked(true);
                    }
                    else {
                        exams.get(0).setChecked(false);
                    }
                    Log.d("array", "This is the first item Id: " + exams.get(0).getExamId()
                            + ", Type: " + exams.get(0).getExamType() + ", Date: " + exams.get(0).getExamDate()
                            + ", Status: " + exams.get(0).getExamSatus());
                }
                else {
                    exams.add(new Exam());
                    int id = exams.get(exams.size() - 2).getExamId();  //get Id of position in front of new note
                    exams.get(exams.size() - 1).setExamId(id + 1);
                    exams.get(exams.size() - 1).setExamType(listExamType);
                    exams.get(exams.size() - 1).setExamDate(listExamDate);
                    exams.get(exams.size() - 1).setExamStatus(listExamStatus);
                    if (listExamAlert.equals("alertOn")){
                        exams.get(0).setChecked(true);
                    }
                    else {
                        exams.get(0).setChecked(false);
                    }
                    Log.d("array", "This is the next item Id: " + exams.get(exams.size() - 1).getExamId()
                            + ", Type: " + exams.get(exams.size() - 1).getExamType() + ", Date: " + exams.get(exams.size() - 1).getExamDate()
                            + ", Status: " + exams.get(exams.size() - 1).getExamSatus());
                    Log.d("array", "array size is: " + exams.size());
                }
                break;
            case "existingExam":
                int position = b.getInt("position");
                exams.get(position).setExamType(listExamType);
                exams.get(position).setExamDate(listExamDate);
                exams.get(position).setExamStatus(listExamStatus);
                if (listExamAlert.equals("alertOn")){
                    exams.get(position).setChecked(true);
                }
                else {
                    exams.get(position).setChecked(false);
                }
                break;
        }
        Log.d("array", "Array size is: " + exams.size());
        adapter.notifyDataSetChanged();
    }

    public static void deleteExam(){
        Bundle b = intent.getExtras();
        int position = b.getInt("position");
        exams.remove(position);
        Log.d("array", "Array size is: " + exams.size());
        adapter.notifyDataSetChanged();
    }

    public static String toJson(){
        String jsonExams = new Gson().toJson(exams);
        return jsonExams;
    }

    public void fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Exam exam = new Exam();
                exam.setExamId(jsonObject.getInt("examId"));
                exam.setExamType(jsonObject.getString("examType"));
                exam.setExamDate(jsonObject.getString("examDate"));
                exam.setExamStatus(jsonObject.getString("examStatus"));
                exam.setChecked(jsonObject.getBoolean("checked"));
                exams.add(exam);
            }
        }
    }

    //clear ArrayList exams for fragment Reload
    public static void clearExams(){
        exams.clear();
        if (!(adapter == null)){
            adapter.notifyDataSetChanged();
        }
    }

    //getter and setter for fragmentLoadCount
    public static int getFragmentLoadCount(){
        return fragmentLoadCount;
    }
    public static void resetFragmentLoadCount(){fragmentLoadCount = 0;}
}