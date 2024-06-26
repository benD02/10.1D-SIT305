package com.example.personalizedlearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    private int quizId; // global variable
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        username = getIntent().getStringExtra("username");
        String quizData = getIntent().getStringExtra("quiz_data");
        quizId = getIntent().getIntExtra("quizId", -1); // Retrieve and store in the global variable


        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);

        TextView resultsTextView = findViewById(R.id.resultsTextView);
        String resultsText = "Congratulations, " + username + "!\nYou answered " + correctAnswers +
                " out of " + totalQuestions + " questions correctly.";
        resultsTextView.setText(resultsText);

        Button retakeQuizButton = findViewById(R.id.retakeQuizButton);
        Button finishQuizButton = findViewById(R.id.finishQuizButton);

        retakeQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, QuizActivity.class);
            intent.putExtra("quiz_data", quizData);
            intent.putExtra("quizId", quizId);
            startActivity(intent);
            finish();
        });

        finishQuizButton.setOnClickListener(v -> {
            if (quizId != -1) {
                markCompletedAndShowResults(quizId);
            } else {
                Toast.makeText(ResultsActivity.this, "Error: Quiz ID not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markCompletedAndShowResults(int quizId) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.markQuizAsCompleted(quizId); // Mark the quiz as completed in the database
        Toast.makeText(this, "Quiz marked as completed.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("USERNAME", username);  // Pass the username as an intent extra
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
