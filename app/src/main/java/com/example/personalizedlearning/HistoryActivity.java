package com.example.personalizedlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {

    private ListView quizResultsListView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        String username = getIntent().getStringExtra("USERNAME_KEY");
        if (username == null) {
            // Handle case where username is not passed or null
            Toast.makeText(this, "Username is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        quizResultsListView = findViewById(R.id.quizResultsListView);
        listData = new ArrayList<>();
        int userId = getUserId(username); // You need to implement this based on your app's user session management
        loadAllQuizResults(userId);
    }

//    private void loadAllQuizResults(int userId) {
//        DatabaseHelper db = new DatabaseHelper(this);
//        listData.clear(); // Clear existing data
//
//        List<Quiz> quizzes = db.getAllQuizzesWithResults(userId);
//        for (Quiz quiz : quizzes) {
//            listData.add("Quiz: " + quiz.getQuizName());
//            for (Question question : quiz.getQuestions()) {
//                listData.add(question.getQuestionText() + "\nAnswer: " + QuizResult.getUserAnswer() +
//                        " (" + (QuizResult.isCorrect() ? "Correct" : "Incorrect") + ")");
//            }
//            listData.add(""); // Add a spacer between quizzes
//        }
//
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
//        quizResultsListView.setAdapter(adapter);
//        adapter.notifyDataSetChanged(); // Notify the adapter of data changes
//
//    }


    private void loadAllQuizResults(int userId) {
        DatabaseHelper db = new DatabaseHelper(this);
        Set<Integer> addedQuizzes = new HashSet<>();  // Set to track added quizzes
        listData.clear(); // Clear existing data

        List<Quiz> quizzes = db.getAllQuizzesWithResults(userId);
        for (Quiz quiz : quizzes) {
            if (!addedQuizzes.contains(quiz.getQuizId())) {  // Only add if not already added
                listData.add("Quiz: " + quiz.getQuizName());
                for (Question question : quiz.getQuestions()) {
                    listData.add(question.getQuestionText() + "\nAnswer: " + QuizResult.getUserAnswer() +
                            " (" + (QuizResult.isCorrect() ? "Correct" : "Incorrect") + ")");
                }
                listData.add(""); // Add a spacer between quizzes
                addedQuizzes.add(quiz.getQuizId());  // Mark this quiz as added
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        quizResultsListView.setAdapter(adapter);
    }


    // You need to implement this method based on your user authentication system
    private int getUserId(String username) {
        DatabaseHelper db = new DatabaseHelper(this);
        int userid = db.getUserId(username);
        return userid;
    }
}
