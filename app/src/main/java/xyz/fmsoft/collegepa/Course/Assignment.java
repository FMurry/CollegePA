package xyz.fmsoft.collegepa.Course;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fredericmurry on 11/26/15.
 */
public class Assignment {


    //f
    private String name;
    private String description;
    private String points;
    private String totalPoints;
    private String courseName;
    private String dueDate;
    private String dueTime;


    /**
     * Default Constructor
     */
    public Assignment(){
        name = "";
        description = "";
        points = "-1";
        totalPoints = "1";
        courseName = "";
    }

    /**
     * Constructor
     * @param name the name of assignment
     * @param description the description of assignment
     * @param totalPoints
     */
    public Assignment(String name, String description, String totalPoints){
        this.name = name;
        this.description = description;
        this.totalPoints = totalPoints;
        points = "-1.00";
        courseName = "";
    }

    public Assignment(Parcel in)
    {
        String[] data = new String[4];

        in.readStringArray(data);
        name = data[0];
        description = data[1];
        totalPoints = data[2];
        points = data[3];
    }    /* Accessor  */

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public double getPoints(){
        return Double.parseDouble(points);
    }

    public double getTotalPoints(){
        return Double.parseDouble(totalPoints);
    }

    public String getCourseName(){ return courseName; }

    public String getDueDate() {return dueDate; }

    public String getDueTime(){ return dueTime; }

    /*  Mutators  */

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setCourseName(String newCourseName) { this.courseName = newCourseName; }

    public void setPoints(String points){
        String regex = "(\\\\d+\\\\.?)+";
        if(points.matches(regex)) {
            this.points = points;
        }
        else{
            Log.d("Course Class","Does not match regex");
        }
    }

    public void setTotalPoints(String totalPoints){
        String regex = "(\\\\d+\\\\.?)+";
        if(totalPoints.matches(regex)) {
            this.totalPoints = totalPoints;
        }
        else{
            Log.d("Course Class","Total Points are not all numbers");
        }
    }

    public void setDueTime(String dueTime){
        this.dueTime = dueTime;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /*   Method Name   */

    /**
     * Generate Grade f
     * @return
     */
    public String generateGrade(){
        double dPoints = Double.parseDouble(points);
        double dTotalPoints = Double.parseDouble(totalPoints);
        if(dPoints < 0){
            return "-"+"/"+dTotalPoints;
        }
        else if(dTotalPoints!=0){

            return String.valueOf((dPoints/dTotalPoints)*100);
        }
        else
            return "-";
    }




    public void saveToFirebase(DatabaseReference branch){

        Map<String,String> infoMap = new HashMap<>();
        infoMap.put("CourseName",this.getCourseName());
        infoMap.put("AssignmentName",this.getName());
        infoMap.put("Description", this.getDescription());
        infoMap.put("TotalPoints", String.valueOf(this.getTotalPoints()));
        infoMap.put("DueDate", this.getDueDate());
        infoMap.put("DueTime", this.getDueTime());
        branch.setValue(infoMap);
    }
}