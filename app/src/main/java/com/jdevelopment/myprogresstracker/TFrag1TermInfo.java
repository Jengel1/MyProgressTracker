package com.jdevelopment.myprogresstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TFrag1TermInfo extends Fragment {

    static TextView termName;
    static TextView termStart;
    static TextView termEnd;
    static TextView termStatus;

    static Intent intent;

    //Strings to hold view text values for action_edit
    String someDataName = null;
    String someDataStart = null;
    String someDataEnd = null;
    String someDataStatus = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tfrag1_term_info, container, false);

        MainActivity.switchId = "courseBtn";

        //reference to views
        termName = rootView.findViewById(R.id.tvTermNameInfo);
        termStart = rootView.findViewById(R.id.tvTStartDateInfo);
        termEnd = rootView.findViewById(R.id.tvTEndDateInfo);
        termStatus = rootView.findViewById(R.id.tvTermStatusResult);

        //Strings to hold view text values
        String noDataName = termName.getText().toString();
        String noDataStart = termStart.getText().toString();
        String noDataEnd = termEnd.getText().toString();
        String noDataStatus = termStatus.getText().toString();

        intent = getActivity().getIntent();
        final Bundle b = intent.getExtras();

        if(b.getCharSequence("action").equals(Intent.ACTION_INSERT)){
            //add text values and data tracking variableto intent object
            intent.putExtra("switchObject", "beforeSaveInsert");
            intent.putExtra("noDataName", noDataName);
            intent.putExtra("noDataStart", noDataStart);
            intent.putExtra("noDataEnd", noDataEnd);
            intent.putExtra("noDataStatus", noDataStatus);
        }

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            termName.setText(b.getCharSequence("termName"));
            termStart.setText(b.getCharSequence("termStart"));
            termEnd.setText(b.getCharSequence("termEnd"));
            termStatus.setText(b.getCharSequence("termStatus"));

            //set String variables with existing data
            someDataName = termName.getText().toString();
            someDataStart = termStart.getText().toString();
            someDataEnd = termEnd.getText().toString();
            someDataStatus = termStatus.getText().toString();
            //put String variables in intent bundle for finish editing method
            intent.putExtra("switchObject", "beforeSaveEdit");
            intent.putExtra("someDataName", someDataName);
            intent.putExtra("someDataStart", someDataStart);
            intent.putExtra("someDataEnd", someDataEnd);
            intent.putExtra("someDataStatus", someDataStatus);

        }

        rootView.findViewById(R.id.editTFrag1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new TFrag1Editor();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tFrag1BaseLayout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    public static void updateFragment(){
        Bundle b = intent.getExtras();
        termName.setText(b.getString("savedTermName"));
        termStart.setText(b.getString("savedTermStart"));
        termEnd.setText(b.getString("savedTermEnd"));
        termStatus.setText(b.getString("savedTermStatus"));

        //Strings to hold final values
        String finalTermName = termName.getText().toString();
        String finalTermStart = termStart.getText().toString();
        String finalTermEnd = termEnd.getText().toString();
        String finalTermStatus = termStatus.getText().toString();

        intent.putExtra("switchObject", "afterSave");
        intent.putExtra("finalTermName", finalTermName);
        intent.putExtra("finalTermStart", finalTermStart);
        intent.putExtra("finalTermEnd", finalTermEnd);
        intent.putExtra("finalTermStatus", finalTermStatus);
    }

}
