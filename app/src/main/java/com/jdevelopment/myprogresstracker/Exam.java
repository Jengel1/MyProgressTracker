package com.jdevelopment.myprogresstracker;


public class Exam {

    private int examId;
    private String examType;
    private String examDate;
    private String examStatus;
    private boolean checked = false;

    //constructors
    public Exam(){this(0,"","","", false);}

    public Exam(int examId, String examType, String examDate, String examStatus, boolean checked){
        this.examId = examId;
        this.examType = examType;
        this.examDate = examDate;
        this.examStatus = examStatus;
        this.checked = checked;
    }

    //getters and setters
    public int getExamId(){return examId;}
    public void setExamId(int newExamId){examId = newExamId;}

    public String getExamType(){return examType;}
    public void setExamType(String newExamType){examType = newExamType;}

    public String getExamDate(){return examDate;}
    public void setExamDate(String newExamDate){examDate = newExamDate;}

    public String getExamSatus(){return examStatus;}
    public void setExamStatus(String newExamStatus){examStatus = newExamStatus;}

    public boolean isChecked(){return checked;}
    public void setChecked(boolean newChecked){checked = newChecked;}
    public void toggleChecked(){checked = !checked;}
}
