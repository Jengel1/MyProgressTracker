package com.jdevelopment.myprogresstracker;


public class Course {

    private int courseId;
    private String courseName;
    private String courseStart;
    private String courseEnd;
    private String courseStatus;
    private boolean checkedTerm = false;

    //constructors
    public Course (){this(0,"","","","",false);}

    public Course (int courseId, String courseName, String courseStart, String courseEnd, String courseStatus, boolean checkedTerm){
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.checkedTerm = checkedTerm;
    }

    //getters and setters
    public int getCourseId(){return courseId;}
    public void setCourseId(int newCourseId){courseId = newCourseId;}

    public String getCourseName(){return courseName;}
    public void setCourseName(String newCourseName){courseName = newCourseName;}

    public String getCourseStart(){return courseStart;}
    public void setCourseStart(String newCourseStart){courseStart = newCourseStart;}

    public String getCourseEnd(){return courseEnd;}
    public void setCourseEnd(String newCourseEnd){courseEnd = newCourseEnd;}

    public String getCourseStatus(){return courseStatus;}
    public void setCourseStatus(String newCourseStatus){courseStatus = newCourseStatus;}

    public boolean isCheckedTerm(){return checkedTerm;}
    public void setCheckedTerm(boolean newCheckedTerm){checkedTerm = newCheckedTerm;}
    public void toggleCheckedTerm(){checkedTerm = !checkedTerm;}

}
