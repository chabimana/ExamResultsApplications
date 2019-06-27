package student.com.examresultsapplications;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentResultActivity extends AppCompatActivity {

    private TextView nameTextView;

    private TextView regnoTextView;

    private TextView genderTextView;

    private TextView averageTextView;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        nameTextView = findViewById(R.id.textViewName);
        regnoTextView = findViewById(R.id.textViewRegNumber);
        listView=findViewById(R.id.listViewCourses);
        genderTextView=findViewById(R.id.textViewGender);
        averageTextView=findViewById(R.id.textViewAverage);

        Intent intent = getIntent();
        try {
            JSONObject studentResult = new JSONObject(intent.getStringExtra("result"));
            nameTextView.setText(studentResult.getJSONObject("student").getString("firstName"));
            regnoTextView.setText(studentResult.getJSONObject("student").getString("regNumber"));
            genderTextView.setText(studentResult.getJSONObject("student").getString("gender"));
            averageTextView.setText(String.valueOf(studentResult.getDouble("average")));
            //creating the adapter and setting it to the custom list view that is supposed
            // to hold the list of students along side with the buttong to update or delete
            List<Course> courseList=new ArrayList<>();

            JSONArray courseArray=studentResult.getJSONArray("courses");
             for(int i=0;i<courseArray.length();i++){
                 Course course=new Course();
                 course.setCourseCode(courseArray.getJSONObject(i).getString("courseCode"));
                 course.setId(courseArray.getJSONObject(i).getLong("id"));
                 course.setCourseName(courseArray.getJSONObject(i).getString("courseName"));
                 course.setMarks(courseArray.getJSONObject(i).getDouble("marks"));

                 courseList.add(course);
             }

            StudentAdapter adapter = new StudentAdapter(courseList);
            listView.setAdapter(adapter);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }


    //Populate the courses into the customized list view
    class StudentAdapter extends ArrayAdapter<Course> {

        //our hero list
        List<Course> courseList;


        //constructor to get the list
        public StudentAdapter(List<Course> courseList) {
            super(StudentResultActivity.this, R.layout.layout_result_list, courseList);
            this.courseList = courseList;
        }

        //method returning list item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_result_list, null, true);

            //getting the textview for displaying name
            TextView courseName = listViewItem.findViewById(R.id.textView);
            //the update and delete textview
            TextView courseCode = listViewItem.findViewById(R.id.textView2);
            TextView courseMarks=listViewItem.findViewById(R.id.textView4);

            final Course course = courseList.get(position);

            courseName.setText(course.getCourseName());
            courseCode.setText(course.getCourseCode());
            courseMarks.setText(course.getMarks().toString());

            return listViewItem;
        }
    }
}