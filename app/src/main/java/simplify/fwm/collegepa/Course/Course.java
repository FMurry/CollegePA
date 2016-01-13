package simplify.fwm.collegepa.Course;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by fredericmurry on 11/26/15.
 */
public class Course implements Comparable<Course>{
    private int id;
    private String courseName;
    private String courseFullName;
    private String type;
    private String grade;
    private Professor professor;
    private String icon;

    private ArrayList<Assignment> assignments;

    private static final String TAG = "COURSE";

    /**
     * Simple Constructor
     */
    public Course(){
        assignments = new ArrayList<>();
    }
//f
    /**
     * Constructor 3 parameters
     * @param courseName name of course ex CS 151
     * @param courseFullName full name of course ex Object oriented design
     * @param type type of course 0 lecture, 1 seminar, 2 lab, 3 workshop
     */
    public Course(String courseName, String courseFullName, String type){
        this.courseName = courseName;
        this.courseFullName = courseFullName;
        this.type = type;
        assignments = new ArrayList<>();
        String regex = "[0-3]+";
        if(type.matches(regex)) {
            this.type = type;
        }
        else{
            this.type = "0";

        }
        professor = new Professor();
        grade = "-1";
    }
    public Course(String courseName, String courseFullName, String type, Professor professor){
        this.id = id;
        this.courseName = courseName;
        this.courseFullName = courseFullName;
        String regex = "[0-3]+";
        if(type.matches(regex)) {
            this.type = type;
        }
        else{
            this.type = "0";
        }
        grade = "-1";
        this.professor = professor;
        assignments = new ArrayList<>();
    }

    public Course(int id, String courseFullName,String courseName, String type){
        this.id = id;
        this.courseName = courseName;
        this.courseFullName = courseFullName;
        String regex = "[0-3]+";
        if(type.matches(regex)) {
            this.type = type;
        }
        else{
            this.type = "0";
        }
        grade = "-1";
        this.professor = new Professor("","");
        assignments = new ArrayList<>();
    }


    /*       Accessors         */

    public int getId(){
        return id;
    }
    public String getCourseName(){
        return courseName;
    }

    public String getCourseFullName(){
        return courseFullName;
    }

    public String getType(){
        return type;
    }

    /**
     * Returns type of class 0-Lecture 1-Seminar 2-Lab 3-Workshop
     * @return Lecture, Seminar, or Lab
     */
    public String getStringType(){
        String newType;
        switch (type){
            case "0":
                newType = "Lecture";
                break;
            case "1":
                newType = "Seminar";
                break;
            case "2":
                newType = "Lab";
                break;
            case "3":
                newType = "Workshop";
                break;
            case "Lecture":
                newType = "Lecture";
                break;
            case "Seminar":
                newType = "Seminar";
                break;
            case "Lab":
                newType = "Lab";
                break;
            case "Workshop":
                newType = "Workshop";
            default:
                newType = "-";
                break;
        }
        return newType;
    }
    public Professor getProfessor(){return professor;}

    public String getGrade(){
        if(Integer.valueOf(grade) >= 0){
            return grade;
        }
        else{
            return "-";
        }

    }
    /*         Mutators              */
    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public void setCourseFullName(String courseFullName){
        this.courseName = courseFullName;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setGrade(String grade) {this.grade = grade;}

    public void setProfessor(Professor professor){this.professor = professor;}

    public void setAssignments(ArrayList assignments){this.assignments = assignments; }





    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Course another) {
        if(this.courseName.compareTo(another.courseName) > 0){
            return 1;
        }
        else if(this.courseName.compareTo(another.courseName) == 0){
            return 0;
        }
        else
            return -1;
    }

    /**
     * Returns A string that Displays the Days The Course takes place
     * @return
     */
    public String getStringDays(){
        String result = "";

        return result;
    }

    /**
     * Sets the drawable string name to be displayed
     * @param newDrawable
     */
    public void setDrawable(String newDrawable){
        icon = newDrawable;
    }

    public void SaveToParse(ParseObject object){
        object.put("createdby", ParseUser.getCurrentUser());
        object.put("CourseName",this.getCourseFullName());
        object.put("CourseID",this.getCourseName());
        object.put("Type",this.getType());
    }
}
