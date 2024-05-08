package com.example.personalizedlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AccountActivity extends AppCompatActivity {
    private Button btnBack, btnShare;
    private DatabaseHelper db;
    private TextView tvUsername, tvEmail, tvTotalQuestions, tvCorrectAnswers, tvIncorrectAnswers;

    private String username;
    private ImageView profilePicture; // Reference to the ImageView

    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();



        username = intent.getStringExtra("USERNAME");
        email = fetchEmail(username);

        btnShare = findViewById(R.id.btn_share);
        btnBack = findViewById(R.id.btn_back);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvTotalQuestions = findViewById(R.id.total_questions);
        tvCorrectAnswers = findViewById(R.id.correct_answer);
        tvIncorrectAnswers = findViewById(R.id.incorrect_answer);
        profilePicture = findViewById(R.id.profile_picture);
        loadImage();

        tvUsername.setText(username);
        tvEmail.setText(email);
        loadData();

        btnBack.setOnClickListener(v -> {
            Intent upgradeIntent = new Intent(AccountActivity.this, ProfileActivity.class);
            upgradeIntent.putExtra("USERNAME", username);
            startActivity(upgradeIntent);
        });

        btnShare.setOnClickListener(v ->  shareResult());


    }

    private void loadImage() {
        db = new DatabaseHelper(this);
        int userId = db.getUserId(username);
        String imagePath = db.getUserImage(userId);
        if (imagePath != null && !imagePath.isEmpty()) {
            profilePicture.setImageURI(Uri.parse(imagePath));
        } else {
            // Handle case where no image is available
            profilePicture.setImageResource(R.mipmap.ic_launcher); // Default or placeholder image
        }
    }

    private void shareResult() {
        String totalQuestions = tvTotalQuestions.getText().toString();
        String correctAnswers = tvCorrectAnswers.getText().toString();
        String incorrectAnswers = tvIncorrectAnswers.getText().toString();

        String shareMessage = "Quiz Results Summary:\n" +
                "Total Questions: " + totalQuestions + "\n" +
                "Correct Answers: " + correctAnswers + "\n" +
                "Incorrect Answers: " + incorrectAnswers;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Results");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Share via"));    }


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
