package xyz.fmsoft.collegepa.DataStructure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import xyz.fmsoft.collegepa.Course.Assignment;
import xyz.fmsoft.collegepa.Course.Course;
import xyz.fmsoft.collegepa.CourseActivity;
import xyz.fmsoft.collegepa.R;
import xyz.fmsoft.collegepa.utils.Constants;

/**
 * Created by fredericmurry on 1/1/16.
 * Handles obtaining information for the Cardview
 */
public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.AssignmentViewHolder> {

    private List<Assignment> assignments;

    public AssignmentListAdapter(List<Assignment> list){
        assignments = list;
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
    public AssignmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View items = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_layout,parent,false);

        return new AssignmentViewHolder(items);
    }


    @Override
    public void onBindViewHolder(AssignmentViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);
        holder.itemName.setText(assignment.getName());
        holder.itemDescription.setText(assignment.getDescription());
//        holder.courseName.setText(course.getCourseFullName());
//        holder.courseID.setText(course.getCourseName());
//        holder.courseType.setText(course.getStringType());
//        holder.courseGrade.setText(course.getGrade());
//        holder.days.setText(course.getStringDays());
//        holder.courseRoom.setText(course.getRoom());
//        holder.color = course.getColorID();
//        holder.courseName.setBackgroundColor(Color.parseColor(holder.color));

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public static class AssignmentViewHolder extends RecyclerView.ViewHolder{
//        private CardView cvData;
//        private TextView courseID;
//        private TextView courseName;
//        private TextView days;
//        private TextView courseType;
//        private TextView courseGrade;
//        private TextView courseRoom;
//        private ImageView icon;
//        private String color;
        private TextView itemName;
        private TextView itemDescription;


        AssignmentViewHolder(View v){
            super(v);

            itemName = (TextView)v.findViewById(R.id.assignment_name);
            itemDescription = (TextView)v.findViewById(R.id.assignment_description);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
//            color = Constants.THEME_MAIN_COLOR;
//            cvData = (CardView)v.findViewById(R.id.card_view);
//            courseID = (TextView)v.findViewById(R.id.cv_course_id);
//            courseName = (TextView)v.findViewById(R.id.cv_course_name);
//            courseRoom = (TextView)v.findViewById(R.id.cv_course_room);
//
//            days = (TextView)v.findViewById(R.id.cv_course_days);
//            courseType = (TextView)v.findViewById(R.id.cv_course_type);
//            courseGrade = (TextView)v.findViewById(R.id.cv_grade);
//
//            final String name;
//            final int theme;
//            if(!courseName.getText().toString().equals(null)){
//                name = courseName.getText().toString();
//            }
//            else{
//                name = null;
//            }
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    Intent courseIntent = new Intent(v.getContext(),CourseActivity.class);
//                    courseIntent.putExtra("position", pos);
//                    courseIntent.putExtra("name",name);
//                    courseIntent.putExtra("color",color);
//                    AppCompatActivity activity = (AppCompatActivity)v.getContext();
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
//                                v,
//                                activity.getResources().getString(R.string.transition_string));
//                        v.getContext().startActivity(courseIntent, options.toBundle());
//                    }
//                    else{
//                        v.getContext().startActivity(courseIntent);
//                    }
//                }
//            });
        }
    }
}
