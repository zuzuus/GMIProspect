package com.gmi.prospect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.gmi.prospect.databinding.ActivityEligibilityBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EligibilityActivity extends AppCompatActivity {

    private static final String TAG = "EligibilityActivity";

    public static final String KEY_NAME = "name";
    public static final String KEY_COURSES = "courses";

    private static final String NOT_TAKEN = "-";
    private static final String[] GRADES = {
            NOT_TAKEN, "A+", "A", "A-", "B+", "B",
            "C+", "C", "D", "E", "G"
    };

    private static final int MIN_CREDITS = 3;

    private ActivityEligibilityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_eligibility
        );

        setupSpinners();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEligibility();
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                GRADES
        );

        gradeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        Spinner[] spinners = {
                binding.spBM,
                binding.spEnglish,
                binding.spSejarah,
                binding.spMaths,
                binding.spAddMaths,
                binding.spPhysics,
                binding.spChemistry,
                binding.spBiology
        };

        for (Spinner spinner : spinners) {
            spinner.setAdapter(gradeAdapter);
        }
    }

    private void checkEligibility() {
        String name = binding.etName.getText().toString().trim();

        // Validate name.
        if (name.isEmpty()) {
            Log.w(TAG, "Check stopped: name is empty");

            binding.etName.setError("Please enter your name");
            showMessage("Please enter your name");

            return;
        }

        binding.etName.setError(null);

        // Get the selected SPM grades.
        String bm = grade(binding.spBM);
        String english = grade(binding.spEnglish);
        String sejarah = grade(binding.spSejarah);
        String maths = grade(binding.spMaths);
        String addMaths = grade(binding.spAddMaths);
        String physics = grade(binding.spPhysics);
        String chemistry = grade(binding.spChemistry);
        String biology = grade(binding.spBiology);

        // Check compulsory subjects.
        if (bm.equals(NOT_TAKEN)
                || english.equals(NOT_TAKEN)
                || sejarah.equals(NOT_TAKEN)
                || maths.equals(NOT_TAKEN)) {

            showMessage(
                    "Please select grades for BM, English, Sejarah and Maths"
            );

            return;
        }

        // Count the credits.
        int credits = 0;

        String[] allGrades = {
                bm, english, sejarah, maths,
                addMaths, physics, chemistry, biology
        };

        for (String grade : allGrades) {
            if (isCredit(grade)) {
                credits++;
            }
        }

        boolean hasScienceCredit =
                isCredit(physics)
                        || isCredit(chemistry)
                        || isCredit(biology);

        Log.d(TAG, "Credits = " + credits
                + ", science credit = " + hasScienceCredit);

        // Check eligibility for every course.
        ArrayList<String> eligibleCourses = new ArrayList<>();

        for (Course course : CourseData.getAllCourses()) {

            String requirement = course.getRequirement();
            boolean eligible = false;

            if (requirement.equals(CourseData.REQ_DIPLOMA)) {

                eligible = isCredit(maths)
                        && hasScienceCredit
                        && credits >= MIN_CREDITS
                        && isPass(english);

            } else if (requirement.equals(CourseData.REQ_DESIGN)) {

                eligible = credits >= MIN_CREDITS;

            } else if (requirement.equals(CourseData.REQ_GAPP)) {

                eligible = isCredit(english)
                        && isCredit(maths)
                        && isCredit(addMaths)
                        && isCredit(physics)
                        && isCredit(chemistry);

            } else if (requirement.equals(CourseData.REQ_GUFP)) {

                eligible = isCredit(bm)
                        && isCredit(english)
                        && isCredit(maths)
                        && isCredit(addMaths)
                        && isCredit(physics)
                        && isCredit(chemistry);
            }

            if (eligible) {
                eligibleCourses.add(course.getName());
            }
        }

        Log.i(TAG, name + " is eligible for "
                + eligibleCourses.size() + " course(s)");

        openNextScreen(name, eligibleCourses);
    }

    // Eligible prospects go to AdmissionActivity.
    // Ineligible prospects go to ResultActivity.
    private void openNextScreen(
            String name,
            ArrayList<String> eligibleCourses
    ) {

        Bundle bundle = new Bundle();

        bundle.putString(KEY_NAME, name);
        bundle.putStringArrayList(KEY_COURSES, eligibleCourses);

        Class<?> nextActivity = eligibleCourses.isEmpty()
                ? ResultActivity.class
                : AdmissionActivity.class;

        Intent intent = new Intent(this, nextActivity);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private String grade(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    // Credit = C and above.
    private boolean isCredit(String grade) {
        return grade.equals("A+")
                || grade.equals("A")
                || grade.equals("A-")
                || grade.equals("B+")
                || grade.equals("B")
                || grade.equals("C+")
                || grade.equals("C");
    }

    // Pass = E and above.
    private boolean isPass(String grade) {
        return isCredit(grade)
                || grade.equals("D")
                || grade.equals("E");
    }

    private void showMessage(String message) {
        Snackbar.make(
                binding.getRoot(),
                message,
                Snackbar.LENGTH_SHORT
        ).show();
    }
}
