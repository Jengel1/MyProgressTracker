package com.jdevelopment.myprogresstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CFrag3Email extends Fragment {

    Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.cfrag3_email, container, false);

        intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        final TextView tvEmailContent = rootView.findViewById(R.id.emailContent);
        tvEmailContent.setText(b.getCharSequence("emailContent"));


        rootView.findViewById(R.id.sendEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get email address to send to
                EditText etAddress = rootView.findViewById(R.id.etTo);
                String emailAddressTo = etAddress.getText().toString();
                //get email subject
                EditText etSubject = rootView.findViewById(R.id.etSubject);
                String emailSubject = etSubject.getText().toString();
                //get email content
                String emailContent = tvEmailContent.getText().toString();

                //create Intent to package email
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddressTo});
                i.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                i.putExtra(Intent.EXTRA_TEXT, emailContent);

                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                }
                catch (android.content.ActivityNotFoundException e){
                    Toast.makeText(getActivity(), "There are no email clients installed", Toast.LENGTH_SHORT).show();
                }

                changeFragment();
            }
        });

        rootView.findViewById(R.id.cancelEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment();
            }
        });

        return rootView;
    }

    public void changeFragment(){
        getFragmentManager().popBackStackImmediate();
    }

}
