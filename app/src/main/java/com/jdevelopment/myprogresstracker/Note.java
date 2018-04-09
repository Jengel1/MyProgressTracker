package com.jdevelopment.myprogresstracker;


public class Note {

    int noteId;
    String noteContent;

    //constructors
    public Note (){this(0,"");}

    public Note (int noteId, String noteContent){
        this.noteId = noteId;
        this.noteContent = noteContent;
    }

    //getters and setters
    public int getNoteId(){return noteId;}
    public void setNoteId(int newNoteId){noteId = newNoteId;}

    public String getNoteContent(){return noteContent;}
    public void setNoteContent(String newNoteContent){noteContent = newNoteContent;}

}
