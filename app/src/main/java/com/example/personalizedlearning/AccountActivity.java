package com.example.personalizedlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AccountActivity extends AppCompatActivity {
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent upgradeIntent = new Intent(AccountActivity.this, ProfileActivity.class);
            startActivity(upgradeIntent);
        });
    }
}