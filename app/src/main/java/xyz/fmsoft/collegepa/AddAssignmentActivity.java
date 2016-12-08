package xyz.fmsoft.collegepa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.Course.Assignment;

public class AddAssignmentActivity extends AppCompatActivity {

    private static final String TAG = "AddAssignmentActivity";

    @Bind(R.id.add_assignment_name) EditText _name;
    @Bind(R.id.add_assignment_description) EditText _description;
    @Bind(R.id.add_assignment_spinner) AppCompatSpinner _spinner;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        final ArrayList<String> items = new ArrayList<>();
        if(user!=null) {
            DatabaseReference courseBranch = (FirebaseDatabase.getInstance().getReference()).child("users").child(user.getUid()).child("Courses");
            courseBranch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int size = (int)dataSnapshot.getChildrenCount();
                    if(size > 0){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            items.add(child.child("CourseName").getValue(String.class));
                        }
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, items);
                    _spinner.setAdapter(arrayAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG,databaseError.getMessage());
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_course, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.action_submit:
                addAssignment();
                break;
            case android.R.id.home:
                startActivity(new Intent(this,DrawerActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,DrawerActivity.class));
        finish();
    }

    public boolean isValid(){
        boolean valid = true;
        if(_name.getText().toString().isEmpty()){
            valid = false;
        }
        if(_description.getText().toString().isEmpty()){
            valid = false;
        }

        return valid;
    }

    public void addAssignment(){

        if(isValid()){
            String name = _name.getText().toString();
            String description = _description.getText().toString();
            String courseName = _spinner.getSelectedItem().toString();
            Assignment assignment = new Assignment(name,description,"100");
            assignment.setCourseName(courseName);
            DatabaseReference assignmentBranch = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Assignments").child(name);
            assignment.saveToFirebase(assignmentBranch);
            startActivity(new Intent(this,DrawerActivity.class));
            finish();
        }
        else{
            Toast.makeText(this, "Please fill out name and description", Toast.LENGTH_SHORT).show();
        }


    }
}
