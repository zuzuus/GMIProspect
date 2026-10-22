package com.gmi.prospect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gmi.prospect.databinding.ActivityCoursesBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// Course list screen. Downloads courses.json with Volley.
public class CoursesActivity extends AppCompatActivity {

    private static final String TAG = "CoursesActivity";
    private static final String COURSES_URL =
            "https://raw.githubusercontent.com/zuzuus/GMIProspect/master/courses.json";

    private ActivityCoursesBinding binding;
    private CourseAdapter adapter;
    private ArrayList<Course> allCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_courses);

        adapter = new CourseAdapter(allCourses);
        binding.recyclerCourses.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerCourses.setAdapter(adapter);

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
                adapter.updateList(filter(CourseData.CAT_DIPLOMA));
            }
        });

        binding.btnGapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateList(filter(CourseData.CAT_GAPP));
            }
        });

        binding.btnGufp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateList(filter(CourseData.CAT_GUFP));
            }
        });

        loadCoursesFromWeb();
    }

    // Request -> Response -> Parse -> Display
    private void loadCoursesFromWeb() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, COURSES_URL, null,
                response -> {
                    Log.i(TAG, "Download OK, " + response.length() + " courses received");
                    showCourses(parseCourses(response), true);
                },
                error -> {
                    Log.e(TAG, "Download failed: " + error.toString());
                    showCourses(CourseData.getAllCourses(), false);
                    Snackbar.make(binding.getRoot(),
                            "No connection. Showing saved course list.",
                            Snackbar.LENGTH_LONG).show();
                });

        queue.add(request);
    }

    private void showCourses(ArrayList<Course> list, boolean online) {
        allCourses = list;
        adapter.updateList(allCourses);
        binding.tvStatus.setText(allCourses.size() + " COURSES "
                + (online ? "(ONLINE)" : "(OFFLINE)"));
    }

    // JSONArray -> JSONObject -> getString()
    private ArrayList<Course> parseCourses(JSONArray response) {
        ArrayList<Course> list = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);

                list.add(new Course(
                        item.getString("name"),
                        item.getString("category"),
                        item.getString("duration"),
                        item.getString("requirement")));
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON parse error: " + e.getMessage());
        }

        return list;
    }

    // Keep only the courses from one category
    private ArrayList<Course> filter(String category) {
        ArrayList<Course> result = new ArrayList<>();

        for (Course c : allCourses) {
            if (c.getCategory().equals(category)) {
                result.add(c);
            }
        }

        Log.d(TAG, "Filter " + category + ": " + result.size() + " courses");
        return result;
    }
}
