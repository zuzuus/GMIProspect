package com.gmi.prospect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.gmi.prospect.databinding.ActivityEligibilityBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

// User enters SPM results and the app checks which courses they can apply for
public class EligibilityActivity extends AppCompatActivity {

    ActivityEligibilityBinding binding;

    String[] grades = {"-", "A+", "A", "A-", "B+", "B", "C+", "C", "D", "E", "G"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_eligibility);

        // Put the grades into every spinner
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, grades);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner[] spinners = {binding.spBM, binding.spEnglish, binding.spSejarah, binding.spMaths,
                binding.spAddMaths, binding.spPhysics, binding.spChemistry, binding.spBiology};
        for (Spinner sp : spinners) {
            sp.setAdapter(gradeAdapter);
        }

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

    private void checkEligibility() {
        String name = binding.etName.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            binding.etName.setError("Please enter your name");
            Snackbar.make(binding.getRoot(), "Please enter your name", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String bm = binding.spBM.getSelectedItem().toString();
        String english = binding.spEnglish.getSelectedItem().toString();
        String sejarah = binding.spSejarah.getSelectedItem().toString();
        String maths = binding.spMaths.getSelectedItem().toString();
        String addMaths = binding.spAddMaths.getSelectedItem().toString();
        String physics = binding.spPhysics.getSelectedItem().toString();
        String chemistry = binding.spChemistry.getSelectedItem().toString();
        String biology = binding.spBiology.getSelectedItem().toString();

        if (bm.equals("-") || english.equals("-") || sejarah.equals("-") || maths.equals("-")) {
            Snackbar.make(binding.getRoot(), "Please select grade for BM, English, Sejarah and Maths",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Count how many credits (C and above)
        String[] allGrades = {bm, english, sejarah, maths, addMaths, physics, chemistry, biology};
        int credits = 0;
        for (String g : allGrades) {
            if (isCredit(g)) {
                credits++;
            }
        }

        boolean scienceCredit = isCredit(physics) || isCredit(chemistry) || isCredit(biology);

        ArrayList<String> eligibleCourses = new ArrayList<>();

        for (Course c : CourseData.getAllCourses()) {
            String req = c.getRequirement();
            boolean eligible = false;

            if (req.equals(CourseData.REQ_DIPLOMA)) {
                eligible = isCredit(maths) && scienceCredit && credits >= 3 && isPass(english);
            } else if (req.equals(CourseData.REQ_DESIGN)) {
                eligible = credits >= 3;
            } else if (req.equals(CourseData.REQ_GAPP)) {
                eligible = isCredit(english) && isCredit(maths) && isCredit(addMaths)
                        && isCredit(physics) && isCredit(chemistry);
            } else if (req.equals(CourseData.REQ_GUFP)) {
                eligible = isCredit(bm) && isCredit(english) && isCredit(maths)
                        && isCredit(addMaths) && isCredit(physics) && isCredit(chemistry);
            }

            if (eligible) {
                eligibleCourses.add(c.getName());
            }
        }

        // Put the data into a Bundle to send to the next activity
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putStringArrayList("courses", eligibleCourses);

        Intent intent;
        if (eligibleCourses.size() > 0) {
            // ELIGIBLE -> redirect to the admission page
            intent = new Intent(EligibilityActivity.this, AdmissionActivity.class);
        } else {
            // NOT ELIGIBLE -> show result page
            intent = new Intent(EligibilityActivity.this, ResultActivity.class);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // Credit = grade C and above
    private boolean isCredit(String grade) {
        return grade.equals("A+") || grade.equals("A") || grade.equals("A-")
                || grade.equals("B+") || grade.equals("B")
                || grade.equals("C+") || grade.equals("C");
    }

    // Pass = grade E and above
    private boolean isPass(String grade) {
        return isCredit(grade) || grade.equals("D") || grade.equals("E");
    }
}