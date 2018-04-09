package com.jdevelopment.myprogresstracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MentorEditor extends AppCompatActivity {

    private String action;  //reference to whatever action is occurring, adding or updating term
    private EditText nameEditor;  //represents editor text object user is writing to
    private EditText phoneEditor;  //represents editor text object user is writing to
    private EditText emailEditor;  //represents editor text object user is writing to
    private String mentorFilter;  //where clause for sql statements
    private String oldName;  //reference to the existing name
    private String oldPhone;  //reference to the existing phone
    private String oldEmail;  //reference to the existing email


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_editor);

        MainActivity.editorFlag = "mentorEditor";

        nameEditor = findViewById(R.id.editMName);
        phoneEditor = findViewById(R.id.editMPhone);
        phoneEditor.addTextChangedListener(new PhoneNumberFormattingTextWatcher());  //format phone number
        emailEditor = findViewById(R.id.editMEmail);

        Intent intent = getIntent();  //reference to intent that launched this activity
        Uri uri = intent.getParcelableExtra(DBContentProvider.CONTENT_MENTOR_ITEM_TYPE);

        if (uri == null){  //user is adding new mentor, uri is null and has no value yet
            action = Intent.ACTION_INSERT;  //intention to add new term
            setTitle(getString(R.string.new_mentor));  //change title of screen when adding new term
        }
        else {  //uri is not null so app will edit existing mentor
            action = Intent.ACTION_EDIT;  //intention to edit selected term
            mentorFilter = DBOpener.MENTOR_ID + "=" + uri.getLastPathSegment();  //create where clause
            Cursor cursor = getContentResolver().query(uri, DBOpener.ALL_MENTOR_COLUMNS, mentorFilter,
                    null, null);
            cursor.moveToFirst();

            oldName = cursor.getString(cursor.getColumnIndex(DBOpener.MENTOR_NAME));  //set old text to existing text
            nameEditor.setText(oldName);  //display existing text
            nameEditor.requestFocus();  //move cursor to end of existing text

            oldPhone = cursor.getString(cursor.getColumnIndex(DBOpener.MENTOR_PHONE));  //set old text to existing text
            phoneEditor.setText(oldPhone);  //display existing text

            oldEmail = cursor.getString(cursor.getColumnIndex(DBOpener.MENTOR_EMAIL));  //set old text to existing text
            emailEditor.setText(oldEmail);  //display existing text
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()){
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteMentor();
                break;
        }
        return true;
    }

    private void deleteMentor() {
        getContentResolver().delete(DBContentProvider.CONTENT_URI, mentorFilter, null);  //delete term in DB
        Toast.makeText(this, R.string.mentor_deleted, Toast.LENGTH_SHORT).show();  //display message to user
        setResult(RESULT_OK);  //let application know action was completed and to update DB
        finish();  //return to term list
    }

    //method called when user presses back arrow on menu or back button on device
    private void finishEditing(){
        String newName = nameEditor.getText().toString().trim();  //get new text and trim any leading or trailing white space
        String newPhone = phoneEditor.getText().toString().trim();  //get new text and trim any leading or trailing white space
        String newEmail = emailEditor.getText().toString().trim();  //get new text and trim any leading or trailing white space

        //evaluate action
        switch (action){
            case Intent.ACTION_INSERT:  //case of adding new mentor
                if (newName.length() == 0  //only validate name field, validating other fields makes app crash
//                        &&  //if no fields have been changed
//                        newPhone.length() == 0 &&
//                        newEmail.length() == 0
                        ){
                    setResult(RESULT_CANCELED);  //cancel new mentor
                }
                else {
                    insertMentor(newName, newPhone, newEmail);
                }
                break;
            case Intent.ACTION_EDIT:  //case of editing existing mentor
                if(newName.length() == 0 &&  //if all fields have been erased
                        newPhone.length() == 0 &&
                        newEmail.length() == 0){
                    deleteMentor();
                }
                else if (newName.equals(oldName) &&
                        newPhone.equals(oldPhone) &&
                        newEmail.equals(oldEmail)){  //user did not change data
                    setResult(RESULT_CANCELED);
                }
                else {  //user edited data
                    updateMentor(newName, newPhone, newEmail);
                }
        }
        finish();  //go back to parent activity
    }

    private void updateMentor(String newName, String newPhone, String newEmail) {
        ContentValues newMentor = new ContentValues();
        newMentor.put(DBOpener.MENTOR_NAME, newName);
        newMentor.put(DBOpener.MENTOR_PHONE, newPhone);
        newMentor.put(DBOpener.MENTOR_EMAIL, newEmail);
        getContentResolver().update(DBContentProvider.CONTENT_URI, newMentor, mentorFilter, null);

        Toast.makeText(this, R.string.mentor_updated, Toast.LENGTH_SHORT).show();  //display toast message
        setResult(RESULT_OK);  //action successfully completed
    }

    private void insertMentor(String newName, String newPhone, String newEmail) {

        ContentValues newMentor = new ContentValues();
        newMentor.put(DBOpener.MENTOR_NAME, newName);
        newMentor.put(DBOpener.MENTOR_PHONE, newPhone);
        newMentor.put(DBOpener.MENTOR_EMAIL, newEmail);
        getContentResolver().insert(DBContentProvider.CONTENT_URI, newMentor);

        setResult(RESULT_OK);  //action successfully completed
    }

    /*
    method called when user presses back button on device
     */
    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
