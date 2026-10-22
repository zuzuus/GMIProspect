package com.gmi.prospect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.gmi.prospect.databinding.ActivityContactBinding;

// Contact & About Us page (both on one screen)
public class ContactActivity extends AppCompatActivity {

    private static final String PHONE_NUMBER = "0389219000";
    private static final String MAP_LOCATION =
            "geo:2.933958,101.7979588?q=German-Malaysian Institute";

    private ActivityContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Implicit intents: Android picks the right app
        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntent(Intent.ACTION_DIAL, "tel:" + PHONE_NUMBER);
            }
        });

        binding.btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        binding.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntent(Intent.ACTION_VIEW, MAP_LOCATION);
            }
        });

        binding.btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntent(Intent.ACTION_VIEW, getString(R.string.gmi_website));
            }
        });
    }

    private void openIntent(String action, String uri) {
        startActivity(new Intent(action, Uri.parse(uri)));
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + getString(R.string.gmi_email)));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry about GMI courses");
        startActivity(intent);
    }
}
