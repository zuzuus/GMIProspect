package com.gmi.prospect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.gmi.prospect.databinding.ActivityResultBinding;
import com.google.android.material.snackbar.Snackbar;

// Shown only when the prospect is NOT eligible for any course
public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    private ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result);

        // Read the name from the Bundle
        String name = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString(EligibilityActivity.KEY_NAME, "");
        }

        Log.i(TAG, name + " is not eligible for any course");
        binding.ivSign.setImageResource(R.drawable.ic_wrong);
        binding.tvResult.setText("Sorry " + name
                + ", based on your SPM results you are not eligible for any course in GMI yet."
                + "\n\nPlease contact GMI to ask about other pathways.");

        Snackbar.make(binding.getRoot(), "Not eligible", Snackbar.LENGTH_LONG).show();

        // Back and Try Again both return to the form (grades are still filled in)
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, ContactActivity.class));
            }
        });
    }
}
