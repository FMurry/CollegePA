package simplify.fwm.collegepa.Course;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fredericmurry on 11/26/15.
 */
public class Professor implements Parcelable{
    private String name;
    private String office;
    //f
    private static final String TAG = "PROFESSOR CLASS";

    public Professor(){
        name = "";
        office = "";
    }

    public Professor(String name, String office){
        this.name = name;
        this.office = office;
    }

    public Professor(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.name = data[0];
        this.office = data[1];
    }

    public Professor(JSONObject object){
        try{
            name = object.getString("name");
            office = object.getString("office");
        }catch (JSONException JEX){
            JEX.printStackTrace();
            Log.d(TAG, "JSON CONSTRUCTOR FAILED");
        }
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
        dest.writeStringArray(new String[]{ this.name, this.office});
    }

    public static final Parcelable.Creator CREATOR = new Creator<Professor>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Professor createFromParcel(Parcel source) {
            return new Professor(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Professor[] newArray(int size) {
            return new Professor[0];
        }
    };

    public void setName(String newName){
        name = newName;
    }

    public void setOffice(String newOffice){
        office = newOffice;
    }

    public String getName(){
        return name;
    }

    public String getOffice(){
        return office;
    }

    public JSONObject getJSON(){
        JSONObject prof = new JSONObject();
        try{
            prof.put("name",name);
            prof.put("office", office);

        } catch (JSONException Jex){
            Jex.printStackTrace();
        }
        return prof;
    }

}