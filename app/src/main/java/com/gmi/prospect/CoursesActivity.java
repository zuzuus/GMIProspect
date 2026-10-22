package com.gmi.prospect;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gmi.prospect.databinding.ActivityCoursesBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// Shows the list of courses (Diploma / GAPP / GUFP).
// The list is downloaded from the web with Volley (JsonArrayRequest).
public class CoursesActivity extends AppCompatActivity {

    // Link to courses.json (change YOUR_USERNAME after uploading the file to GitHub)
    private static final String COURSES_URL =
            "https://raw.githubusercontent.com/YOUR_USERNAME/gmi-prospect/main/courses.json";

    ActivityCoursesBinding binding;
    CourseAdapter adapter;
    ArrayList<Course> allCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_courses);

        binding.recyclerCourses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(allCourses);
        binding.recyclerCourses.setAdapter(adapter);

        loadCoursesFromWeb();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateList(allCourses);
            }
        });

        binding.btnDiploma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateList(filter("DIPLOMA"));
            }
        });

        binding.btnGapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateList(filter("GAPP"));
            }
        });

        binding.btnGufp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateList(filter("GUFP"));
            }
        });
    }

    // Request -> Response -> Parse -> Display
    private void loadCoursesFromWeb() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, COURSES_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        allCourses = parseCourses(response);
                        adapter.updateList(allCourses);
                        binding.tvStatus.setText(allCourses.size() + " COURSES (ONLINE)");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // No internet or wrong link -> use the saved list in CourseData
                        allCourses = CourseData.getAllCourses();
                        adapter.updateList(allCourses);
                        binding.tvStatus.setText(allCourses.size() + " COURSES (OFFLINE)");
                        Snackbar.make(binding.getRoot(), "No connection. Showing saved course list.",
                                Snackbar.LENGTH_LONG).show();
                    }
                });

        queue.add(request);
    }

    // Loop through the JSONArray and turn each JSONObject into a Course
    private ArrayList<Course> parseCourses(JSONArray response) {
        ArrayList<Course> list = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);
                String name = item.getString("name");
                String category = item.getString("category");
                String duration = item.getString("duration");
                String requirement = item.getString("requirement");
                list.add(new Course(name, category, duration, requirement));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Returns only the courses from the chosen category
    private ArrayList<Course> filter(String category) {
        ArrayList<Course> result = new ArrayList<>();
        for (Course c : allCourses) {
            if (c.getCategory().equals(category)) {
                result.add(c);
            }
        }
        return result;
    }
}
