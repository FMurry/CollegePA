package simplify.fwm.collegepa.Course;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by fredericmurry on 11/26/15.
 */
public class Assignment implements Parcelable{


    //f
    private String name;
    private String description;
    private String points;
    private String totalPoints;


    public Assignment(){
        name = "";
        description = "";
        points = "-1";
        totalPoints = "1";
    }

    public Assignment(String name, String description, String totalPoints){
        this.name = name;
        this.description = description;
        this.totalPoints = totalPoints;
        points = "-1.00";
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

    /*  Mutators  */

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

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


    /*   Method Name   */

    /**
     * Generate Grade f
     * @return
     */
    public String generateGrade(){
        double dPoints = Double.parseDouble(points);
        double dTotalPoints = Double.parseDouble(totalPoints);
        if(dPoints < 0){
            return "-";
        }
        else if(dTotalPoints!=0){

            return String.valueOf((dPoints/dTotalPoints)*100);
        }
        else
            return "-";
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{name,description,totalPoints,points});
    }

    public Parcelable.Creator CREATOR = new Creator<Assignment>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Assignment createFromParcel(Parcel source) {
            return new Assignment(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[0];
        }
    };
}