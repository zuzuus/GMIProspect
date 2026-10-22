package com.gmi.prospect;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.gmi.prospect.databinding.ActivityAdmissionBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

// Admission page: opened every time the prospect is eligible
public class AdmissionActivity extends AppCompatActivity {

    private static final String TAG = "AdmissionActivity";
    private static final String ADMISSION_URL = "https://www.gmi.edu.my";
    private static final int MIN_PHONE_LENGTH = 10;

    private ActivityAdmissionBinding binding;
    private String name = "";
    private ArrayList<String> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admission);

        // Move the screen up when the keyboard opens, so phone/email stay visible
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets bars = insets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            }
        });

        // Read the Bundle sent from EligibilityActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString(EligibilityActivity.KEY_NAME, "");
            courses = bundle.getStringArrayList(EligibilityActivity.KEY_COURSES);
        }

        binding.ivSign.setImageResource(R.drawable.ic_correct);
        binding.tvMessage.setText("Congratulations " + name
                + "! Based on your SPM results, you are eligible for "
                + courses.size() + " course(s).");

        Snackbar.make(binding.getRoot(), "You are eligible! Choose a course to apply.",
                Snackbar.LENGTH_LONG).show();

        binding.listCourses.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_single_choice, courses));

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

        // ----- Validation -----
        if (position == ListView.INVALID_POSITION) {
            Log.w(TAG, "Submit stopped: no course chosen");
            Snackbar.make(binding.getRoot(), "Please choose a course",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() < MIN_PHONE_LENGTH) {
            binding.etPhone.setError("Please enter a valid phone number");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Please enter a valid email");
            return;
        }

        Log.i(TAG, "Application submitted for " + courses.get(position));
        Log.d(TAG, "Name: " + name + ", Phone: " + phone + ", Email: " + email);
        showSubmittedDialog(courses.get(position), phone, email);
    }

    private void showSubmittedDialog(String course, String phone, String email) {
        StringBuilder message = new StringBuilder();
        message.append("Thank you ").append(name).append("!\n\n");
        message.append("Course: ").append(course).append("\n");
        message.append("Phone: ").append(phone).append("\n");
        message.append("Email: ").append(email).append("\n\n");
        message.append("GMI admission office will contact you soon.");

        new AlertDialog.Builder(this)
                .setTitle("Application Submitted")
                .setMessage(message.toString())
                .setPositiveButton("Visit GMI Website", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ADMISSION_URL)));
                    }
                })
                .setNegativeButton("Back to Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goHome();
                    }
                })
                .setCancelable(false)
                .show();
    }

    // CLEAR_TOP closes the screens in between, so Back won't return to the form
    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
