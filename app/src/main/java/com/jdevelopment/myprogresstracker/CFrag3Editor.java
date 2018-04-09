package com.jdevelopment.myprogresstracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class CFrag3Editor extends Fragment {

    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.cfrag3_editor, container, false);

        MainActivity.switchId = "courseBtn";

        EditText etNoteContent = rootView.findViewById(R.id.etNoteContent);

        intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            etNoteContent.setText(b.getCharSequence("existingNoteContent"));
        }

        rootView.findViewById(R.id.saveNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set note content
                EditText noteContentEditor = rootView.findViewById(R.id.etNoteContent);
                String newNoteContent = noteContentEditor.getText().toString();
                intent.putExtra("noteContent", newNoteContent);

                CFrag3Notes.addOrUpdateNote();
                changeFragment();

            }
        });

        rootView.findViewById(R.id.deleteNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CFrag3Notes.deleteNote();
                changeFragment();
            }
        });

        rootView.findViewById(R.id.cancelAndReturnNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment();
            }
        });

        rootView.findViewById(R.id.emailNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new CFrag3Email();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.cFrag3EditorLayout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                //put noteContent into bundle
                EditText noteContentEditor = rootView.findViewById(R.id.etNoteContent);
                String emailContent = noteContentEditor.getText().toString();
                intent.putExtra("emailContent", emailContent);

                //disappear all btns
                View returnBtn = rootView.findViewById(R.id.cancelAndReturnNote);
                returnBtn.setVisibility(View.GONE);
                View saveBtn = rootView.findViewById(R.id.saveNote);
                saveBtn.setVisibility(View.GONE);
                View deleteBtn = rootView.findViewById(R.id.deleteNote);
                deleteBtn.setVisibility(View.GONE);
                View emailBtn = rootView.findViewById(R.id.emailNote);
                emailBtn.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    public void changeFragment(){
        getFragmentManager().popBackStackImmediate();
    }

}
