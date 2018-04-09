package com.jdevelopment.myprogresstracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NoteArrayAdapter extends ArrayAdapter<Note> {

    private Context context;
    private ArrayList<Note> notes;
    private static LayoutInflater inflater = null;


    public NoteArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> _notes) {
        super(context, resource, _notes);
        try {
            this.context = context;
            this.notes = _notes;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e){
            Log.d("adapter", "constructor exception");
        }
    }

    public int getCount(){return notes.size();}

    public Note getItem(Note position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder {
        public TextView adapterNoteContent;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        final NoteArrayAdapter.ViewHolder holder;

        Note note = getItem(position);

        try {
            if (convertView == null){
                view = inflater.inflate(R.layout.note_list_item, null);
                holder = new NoteArrayAdapter.ViewHolder();

                holder.adapterNoteContent = (TextView) view.findViewById(R.id.tvListNote);

                view.setTag(holder);
            }
            else {
                holder = (NoteArrayAdapter.ViewHolder) view.getTag();
            }
            holder.adapterNoteContent.setText(notes.get(position).getNoteContent());
        }
        catch (Exception e){
            Log.d("adapter", "getView exception");
        }
        return view;
    }

}