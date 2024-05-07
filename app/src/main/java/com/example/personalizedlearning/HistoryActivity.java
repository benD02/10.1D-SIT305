package com.example.personalizedlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {

    private ListView quizResultsListView;
    private List<QuizResult> listData;

    private CustomQuizAdapter adapter;


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
//        Set<Integer> addedQuizzes = new HashSet<>();
//        listData.clear();
//
//        List<Quiz> quizzes = db.getAllQuizzesWithResults(userId);
//        for (Quiz quiz : quizzes) {
//            if (!addedQuizzes.contains(quiz.getQuizId())) {
//                listData.add("Quiz: " + quiz.getQuizName());
//                Log.d("HistoryActivity", "Quiz: " + quiz.getQuizName() + " | Quiz ID: " + quiz.getQuizId());
//
//                // Assuming each question has a corresponding result in a list within the Quiz object
//                for (int i = 0; i < quiz.getQuestions().size(); i++) {
//                    Question question = quiz.getQuestions().get(i);
//                    QuizResult result = quiz.getResults().get(i);  // This assumes that the results are stored in the same order as questions
//                    String logMessage = "Question: " + question.getQuestionText() +
//                            "\nAnswer given: " + result.getUserAnswer() +
//                            "\nCorrect: " + (result.isCorrect() ? "Yes" : "No");
//                    Log.d("HistoryActivity", logMessage);
//                    listData.add(question.getQuestionText() + "\nAnswer: " + result.getUserAnswer() +
//                            " (" + (result.isCorrect() ? "Correct" : "Incorrect") + ")");
//                }
//                listData.add(""); // Spacer between quizzes
//                addedQuizzes.add(quiz.getQuizId());
//            }
//        }
//
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
//        quizResultsListView.setAdapter(adapter);
//    }

    private void loadAllQuizResults(int userId) {
        DatabaseHelper db = new DatabaseHelper(this);
        Set<Integer> addedQuizzes = new HashSet<>();
        List<QuizResult> listData = new ArrayList<>();  // Changed to store QuizResult objects

        List<Quiz> quizzes = db.getAllQuizzesWithResults(userId);
        for (Quiz quiz : quizzes) {
            if (!addedQuizzes.contains(quiz.getQuizId())) {
                List<Question> questions = quiz.getQuestions();
                List<QuizResult> results = quiz.getResults();

                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
                    QuizResult result = results.get(i);
                    listData.add(result);  // Directly add QuizResult objects to the list
                }
                // Add a blank QuizResult as a spacer if it's not the last quiz
                if (quizzes.indexOf(quiz) < quizzes.size() - 1) {
                    listData.add(new QuizResult("", "", false));  // Spacer between each quiz
                }
                addedQuizzes.add(quiz.getQuizId());
            }
        }

        // Update adapter to handle List<QuizResult>
        adapter = new CustomQuizAdapter(this, R.layout.list_item_quiz_result, listData);
        quizResultsListView.setAdapter(adapter);
    }




    // You need to implement this method based on your user authentication system
    private int getUserId(String username) {
        DatabaseHelper db = new DatabaseHelper(this);
        int userid = db.getUserId(username);
        return userid;
    }
}
