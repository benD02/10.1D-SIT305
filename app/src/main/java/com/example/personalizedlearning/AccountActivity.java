package com.example.personalizedlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AccountActivity extends AppCompatActivity {
    private Button btnBack;
    private DatabaseHelper db;
    private TextView tvUsername, tvEmail, tvTotalQuestions, tvCorrectAnswers, tvIncorrectAnswers;

    private String username;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();

        username = intent.getStringExtra("USERNAME");
        email = fetchEmail(username);

        btnBack = findViewById(R.id.btn_back);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvTotalQuestions = findViewById(R.id.total_questions);
        tvCorrectAnswers = findViewById(R.id.correct_answer);
        tvIncorrectAnswers = findViewById(R.id.incorrect_answer);

        tvUsername.setText(username);
        tvEmail.setText(email);

        btnBack.setOnClickListener(v -> {
            Intent upgradeIntent = new Intent(AccountActivity.this, ProfileActivity.class);
            upgradeIntent.putExtra("USERNAME", username);
            startActivity(upgradeIntent);
        });

        loadData();

    }
    private void loadData() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        db = new DatabaseHelper(this);

        if (username != null) {
            tvUsername.setText(username);
            tvEmail.setText(db.getEmail(username));

            int userId = db.getUserId(username);
            List<Quiz> quizzes = db.getAllQuizzesWithResults(userId);
            int totalQuestions = 0;
            int correctAnswers = 0;
            int incorrectAnswers = 0;

            for (Quiz quiz : quizzes) {
                List<QuizResult> results = quiz.getResults();
                for (QuizResult result : results) {
                    totalQuestions++;
                    if (result.isCorrect()) {
                        correctAnswers++;
                    } else {
                        incorrectAnswers++;
                    }
                }
            }

            tvTotalQuestions.setText(String.valueOf(totalQuestions));
            tvCorrectAnswers.setText(String.valueOf(correctAnswers));
            tvIncorrectAnswers.setText(String.valueOf(incorrectAnswers));
        } else {
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private String fetchEmail(String username) {
        if(username == null){
            return null;
        }
        else{
            DatabaseHelper db = new DatabaseHelper(this);
            String email = db.getEmail(username);
            return email;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close(); // Close database when activity is destroyed
    }



}
