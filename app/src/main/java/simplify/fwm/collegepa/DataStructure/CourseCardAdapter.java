package simplify.fwm.collegepa.DataStructure;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import simplify.fwm.collegepa.Course.Course;
import simplify.fwm.collegepa.CourseActivity;
import simplify.fwm.collegepa.R;

/**
 * Created by fredericmurry on 1/1/16.
 */
public class CourseCardAdapter extends RecyclerView.Adapter<CourseCardAdapter.CourseViewHolder> {

    private List<Course> courses;

    static final int darkBlue = Color.parseColor("#1976D2");
    static final int lightBlue = Color.parseColor("#03A9F4");
    static final int green = Color.parseColor("#4CAF50");
    static final int red = Color.parseColor("#F44336");
    static final int darkRed = Color.parseColor("#D32F2F");
    static final int amber = Color.parseColor("#FFC107");

    public CourseCardAdapter(List<Course> list){
        courses = list;
    }

    /**
     * Called when RecyclerView needs a new @link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * @link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     */
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View items = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);

        return new CourseViewHolder(items);
    }


    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.courseName.setText(course.getCourseFullName());
        holder.courseID.setText(course.getCourseName());
        holder.courseType.setText(course.getStringType());
        holder.courseGrade.setText(course.getGrade());
        holder.days.setText(course.getStringDays());
        holder.courseRoom.setText(course.getRoom());
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        private CardView cvData;
        private TextView courseID;
        private TextView courseName;
        private TextView days;
        private TextView courseType;
        private TextView courseGrade;
        private TextView courseRoom;
        private ImageView icon;

        CourseViewHolder(View v){
            super(v);

            cvData = (CardView)v.findViewById(R.id.card_view);
            courseID = (TextView)v.findViewById(R.id.cv_course_id);
            courseName = (TextView)v.findViewById(R.id.cv_course_name);
            courseRoom = (TextView)v.findViewById(R.id.cv_course_room);

            days = (TextView)v.findViewById(R.id.cv_course_days);
            courseType = (TextView)v.findViewById(R.id.cv_course_type);
            courseGrade = (TextView)v.findViewById(R.id.cv_grade);
            icon = (ImageView)v.findViewById(R.id.cv_course_icon);

            int[] androidColors = v.getContext().getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            courseName.setBackgroundColor(randomAndroidColor);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent courseIntent = new Intent(v.getContext(),CourseActivity.class);
                    v.getContext().startActivity(courseIntent);
                    Toast.makeText(v.getContext(), String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    //TODO: Implement Onclick for Specific Courses
                }
            });



        }


    }


}
