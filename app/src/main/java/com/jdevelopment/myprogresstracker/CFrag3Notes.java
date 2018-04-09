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

public class CFrag3Notes extends ListFragment {

    //variable to hold list of exams
    static ArrayList<Note> notes = new ArrayList<>();

    //variables to hold exam info for array
    static String listNoteContent;

    //intent object to hold data from handle save method
    static Intent intent;

    //custom adapter to display list items
    static NoteArrayAdapter adapter;

    //variable to track how many times page is loaded
    private static int fragmentLoadCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cfrag3_notes, container, false);

        MainActivity.switchId = "courseBtn";

        intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        //String to hold value for action_insert
        String noDataNotes = notes.toString();

        //String to hold value for action_edit
        String someDataNotes = null;

        if(b.getCharSequence("action").equals(Intent.ACTION_INSERT)){
            intent.putExtra("switchObject", "beforeSaveInsert");
            intent.putExtra("noDataNotes", noDataNotes);
        }

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            someDataNotes = b.getString("courseNotes");
            if (someDataNotes != null && fragmentLoadCount == 0){
                clearNotes();
                try {
                    fromJson(someDataNotes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            fragmentLoadCount++;
        }

        adapter = new NoteArrayAdapter(getContext(), R.layout.note_list_item, notes);
        ListView list = rootView.findViewById(android.R.id.list);
        list.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("onClick", "Clicked on item: " + position + " and " + id);

        Note existingNote = notes.get(position);
        String existingNoteContent = existingNote.getNoteContent();

        intent.putExtra("action", Intent.ACTION_EDIT);
        intent.putExtra("position", position);
        intent.putExtra("newOrExisting", "existingNote");
        intent.putExtra("existingNoteContent", existingNoteContent);

        Fragment newFragment = new CFrag3Editor();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cFrag3BaseLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addOrUpdateNote(){
        Bundle b = intent.getExtras();
        listNoteContent = b.getString("noteContent");
        //marker to indicate whether new exam or existing exam
        String newOrExisting = b.getString("newOrExisting");

        switch (newOrExisting){
            case "newNote":
                if (notes.isEmpty()){
                    notes.add(new Note());
                    notes.get(0).setNoteId(1);
                    notes.get(0).setNoteContent(listNoteContent);
                    Log.d("array", "This is the first note: Id is- " + notes.get(0).getNoteId()
                            + ", content is- " + notes.get(0).getNoteContent());
                }
                else {
                    notes.add(new Note());
                    int id = notes.get(notes.size() - 2).getNoteId();  //get Id of position in front of new note
                    notes.get(notes.size() - 1).setNoteId(id + 1);
                    notes.get(notes.size() - 1).setNoteContent(listNoteContent);
                    Log.d("array", "This is the next note: Id is- " + notes.get(notes.size() - 1).getNoteId()
                            + ", content is- " + notes.get(notes.size() - 1).getNoteContent());
                }
                break;
            case "existingNote":
                int position = b.getInt("position");
                notes.get(position).setNoteContent(listNoteContent);
                break;
        }
        Log.d("array", "Array size is: " + notes.size());
        adapter.notifyDataSetChanged();
    }

    public static void deleteNote(){
        Bundle b = intent.getExtras();
        int position = b.getInt("position");
        notes.remove(position);
        Log.d("array", "Array size is: " + notes.size());
        adapter.notifyDataSetChanged();
    }

    public static String toJson(){
        String jsonExams = new Gson().toJson(notes);
        return jsonExams;
    }

    public void fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Note note = new Note();
                note.setNoteId(jsonObject.getInt("noteId"));
                note.setNoteContent(jsonObject.getString("noteContent"));
                notes.add(note);
            }
        }
    }

    //clear ArrayList notes for fragment Reload
    public static void clearNotes(){
        notes.clear();
        if (!(adapter == null)){
            adapter.notifyDataSetChanged();
        }
    }

    //getter and setter for fragmentLoadCount
    public static void resetFragmentLoadCount(){fragmentLoadCount = 0;}

}