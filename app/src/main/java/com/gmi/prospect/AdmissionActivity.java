package com.gmi.prospect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.gmi.prospect.databinding.ActivityAdmissionBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AdmissionActivity extends AppCompatActivity {

    private static final String TAG = "AdmissionActivity";

    // Official GMI admission pages.
    private static final String DIPLOMA_ADMISSION_URL =
            "https://www.gmi.edu.my/diploma-programme-admission/";

    private static final String GAPP_ADMISSION_URL =
            "https://www.gmi.edu.my/a-level-programme-admission/";

    private static final String GUFP_ADMISSION_URL =
            "https://www.gmi.edu.my/foundation-programme-admission/";

    private ActivityAdmissionBinding binding;

    private String name = "";

    private ArrayList<String> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_admission
        );

        // Handle system bars.
        ViewCompat.setOnApplyWindowInsetsListener(
                binding.getRoot(),
                new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(
                            View view,
                            WindowInsetsCompat insets
                    ) {

                        Insets bars = insets.getInsets(
                                WindowInsetsCompat.Type.systemBars()
                                        | WindowInsetsCompat.Type.ime()
                        );

                        view.setPadding(
                                bars.left,
                                bars.top,
                                bars.right,
                                bars.bottom
                        );

                        return insets;
                    }
                }
        );

        // Receive the name and eligible courses.
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            name = bundle.getString(
                    EligibilityActivity.KEY_NAME,
                    ""
            );

            ArrayList<String> passedCourses =
                    bundle.getStringArrayList(
                            EligibilityActivity.KEY_COURSES
                    );

            if (passedCourses != null) {
                courses = passedCourses;
            }
        }

        // This screen requires at least one eligible course.
        if (courses.isEmpty()) {

            Snackbar.make(
                    binding.getRoot(),
                    "No eligible courses were received. Please check your SPM results again.",
                    Snackbar.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        // Display the eligibility message.
        binding.ivSign.setImageResource(R.drawable.ic_correct);

        binding.tvMessage.setText(
                "Congratulations " + name
                        + "! Based on your SPM results, you are eligible for "
                        + courses.size()
                        + " course(s). Select a course below to continue to admission."
        );

        Snackbar.make(
                binding.getRoot(),
                "You are eligible! Choose a course to apply.",
                Snackbar.LENGTH_LONG
        ).show();

        // Display only courses the prospect qualifies for.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_single_choice,
                courses
        );

        binding.listCourses.setChoiceMode(
                ListView.CHOICE_MODE_SINGLE
        );

        binding.listCourses.setAdapter(adapter);

        // Back button.
        binding.btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );

        // Admission redirection button.
        binding.btnSubmit.setText("Apply for Admission");

        binding.btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAdmissionPage();
                    }
                }
        );
    }

    /**
     * Redirects the prospect to the relevant official GMI
     * admission page according to the selected eligible course.
     */
    private void openAdmissionPage() {

        int position = binding.listCourses.getCheckedItemPosition();

        // Ensure a course has been selected.
        if (position == ListView.INVALID_POSITION) {

            Snackbar.make(
                    binding.getRoot(),
                    "Please select one of your eligible courses first.",
                    Snackbar.LENGTH_SHORT
            ).show();

            return;
        }

        String selectedCourse = courses.get(position);

        String url = getAdmissionUrl(selectedCourse);

        Log.i(
                TAG,
                "Redirecting prospect to admission page for: "
                        + selectedCourse
        );

        // Open the admission page in the browser.
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
        );

        try {

            startActivity(intent);

        } catch (android.content.ActivityNotFoundException exception) {

            Snackbar.make(
                    binding.getRoot(),
                    "No browser is available to open the GMI admission page.",
                    Snackbar.LENGTH_LONG
            ).show();

            Log.e(
                    TAG,
                    "Could not open admission URL: " + url,
                    exception
            );
        }
    }

    /**
     * Select the admission URL based on the course category.
     */
    private String getAdmissionUrl(String courseName) {

        String upperCaseName = courseName.toUpperCase();

        if (upperCaseName.contains("GAPP")) {
            return GAPP_ADMISSION_URL;
        }

        if (upperCaseName.contains("GUFP")) {
            return GUFP_ADMISSION_URL;
        }

        // Diploma courses use the Diploma admission page.
        return DIPLOMA_ADMISSION_URL;
    }
}
