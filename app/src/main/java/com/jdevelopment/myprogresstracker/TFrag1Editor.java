package com.jdevelopment.myprogresstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class TFrag1Editor extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tfrag1_editor, container, false);

        final View infoView = inflater.inflate(R.layout.tfrag1_term_info, container, false);

        MainActivity.switchId = "termsBtn";

        final EditText editName = rootView.findViewById(R.id.editTName);
        final TextView editStart = rootView.findViewById(R.id.tvTStartDateResult);
        final TextView editEnd = rootView.findViewById(R.id.tvTEndDateResult);
        final RadioGroup rg = rootView.findViewById(R.id.termRadioGroup);
        //variable to hold course status
        String status = null;

        final Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        if (b.getCharSequence("action").equals(Intent.ACTION_EDIT)){
            //variable to check where data is coming from
            String comingFrom = (String) b.getCharSequence("switchObject");
            switch (comingFrom){
                case "beforeSave":
                    editName.setText(b.getCharSequence("termName"));
                    editStart.setText(b.getCharSequence("termStart"));
                    editEnd.setText(b.getCharSequence("termEnd"));
                    status = (String) b.getCharSequence("termStatus");
                    break;
                case "afterSave":
                    editName.setText(b.getCharSequence("savedTermName"));
                    editStart.setText(b.getCharSequence("savedTermStart"));
                    editEnd.setText(b.getCharSequence("savedTermEnd"));
                    status = (String) b.getCharSequence("savedTermStatus");
                    break;
            }
            for (int i = 0; i < rg.getChildCount(); i++){
                RadioButton button = (RadioButton) rg.getChildAt(i);
                String btnText = button.getText().toString();
                if (btnText.equals(status)){
                    button.toggle();
                    break;
                }
            }
        }

        rootView.findViewById(R.id.saveTFrag1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView infoName = infoView.findViewById(R.id.tvTermNameInfo);
                TextView infoStart = infoView.findViewById(R.id.tvTStartDateInfo);
                TextView infoEnd = infoView.findViewById(R.id.tvTEndDateInfo);
                TextView infoStatus = infoView.findViewById(R.id.tvTermStatusResult);
                //set intent switch object for CFrag1Editor
                intent.putExtra("switchObject", "afterSave");
                //set course name
                String newName = editName.getText().toString();
                infoName.setText(newName);
                intent.putExtra("savedTermName", newName);
                //set course start date
                String newStart = editStart.getText().toString();
                infoStart.setText(newStart);
                intent.putExtra("savedTermStart", newStart);
                //set course end date
                String newEnd = editEnd.getText().toString();
                infoEnd.setText(newEnd);
                intent.putExtra("savedTermEnd", newEnd);
                //set course status
                int id = rg.getCheckedRadioButtonId();
                View radioView = rg.findViewById(id);
                int index = rg.indexOfChild(radioView);
                RadioButton checkedBtn = (RadioButton) rg.getChildAt(index);
                String newStatus = checkedBtn.getText().toString();
                infoStatus.setText(newStatus);
                intent.putExtra("savedTermStatus", newStatus);

                TFrag1TermInfo.updateFragment();
                changeFragment();
            }
        });

        return rootView;
    }

    public void changeFragment(){
        getFragmentManager().popBackStackImmediate();
    }


}