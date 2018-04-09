package com.jdevelopment.myprogresstracker;


public class Mentor {

    private int mentorId;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;
    private boolean checked = false;

    //constructors
    public Mentor (){this(0,"","","", false);}

    public Mentor (int mentorId, String mentorName, String mentorPhone, String mentorEmail, boolean checked){
        this.mentorId = mentorId;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.checked = checked;
    }

    //getters and setters
    public int getMentorId(){return mentorId;}
    public void setMentorId(int newMentorId){mentorId = newMentorId;}

    public String getMentorName(){return mentorName;}
    public void setMentorName(String newMentorName){mentorName = newMentorName;}

    public String getMentorPhone(){return mentorPhone;}
    public void setMentorPhone(String newMentorPhone){mentorPhone = newMentorPhone;}

    public String getMentorEmail(){return mentorEmail;}
    public void setMentorEmail(String newMentorEmail){mentorEmail = newMentorEmail;}

    public boolean isChecked(){return checked;}
    public void setChecked(boolean newChecked){checked = newChecked;}
    public void toggleChecked(){checked = !checked;}

}
