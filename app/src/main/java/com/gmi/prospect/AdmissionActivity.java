package com.gmi.prospect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.gmi.prospect.databinding.ActivityAdmissionBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

// Admission page - the prospect is redirected here every time they are eligible.
// It only shows the courses they can apply for based on the eligibility check.
public class AdmissionActivity extends AppCompatActivity {

    // GMI website (change if your lecturer gives a specific admission link)
    String admissionUrl = "https://www.gmi.edu.my";

    ActivityAdmissionBinding binding;
    String name;
    ArrayList<String> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admission);

        // Get data from EligibilityActivity using Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            courses = bundle.getStringArrayList("courses");
        }

        binding.ivSign.setImageResource(R.drawable.ic_correct);
        binding.tvMessage.setText("Congratulations " + name + "! Based on your SPM results, you are eligible for "
                + courses.size() + " course(s).");
        Snackbar.make(binding.getRoot(), "You are eligible! Choose a course to apply.",
                Snackbar.LENGTH_LONG).show();

        // Show eligible courses with a radio button so the prospect can pick one
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, courses);
        binding.listCourses.setAdapter(adapter);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitApplication();
            }
        });
    }

    private void submitApplication() {
        int position = binding.listCourses.getCheckedItemPosition();
        String phone = binding.etPhone.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();

        // Validation
        if (position == ListView.INVALID_POSITION) {
            Snackbar.make(binding.getRoot(), "Please choose a course", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() < 10) {
            binding.etPhone.setError("Please enter a valid phone number");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Please enter a valid email");
            return;
        }

        String course = courses.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Application Submitted")
                .setMessage("Thank you " + name + "!\n\nCourse: " + course
                        + "\nPhone: " + phone + "\nEmail: " + email
                        + "\n\nGMI admission office will contact you soon.")
                .setPositiveButton("Visit GMI Website", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(admissionUrl)));
                    }
                })
                .setNegativeButton("Back to Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AdmissionActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .show();
    }
}
