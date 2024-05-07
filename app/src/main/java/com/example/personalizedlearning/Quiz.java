package com.example.personalizedlearning;

import java.io.Serializable;
import java.util.List;

public class Quiz implements Serializable {
    private int quizId;
    private String quizName;
    private String quizDescription;
    private List<Question> questions;
    private List<QuizResult> results; // Added to hold quiz results
    private boolean isCompleted;

    // Updated constructor
    public Quiz(int quizId, String quizName, String quizDescription, List<Question> questions, boolean isCompleted) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.questions = questions;
        this.isCompleted = isCompleted;
    }

    // Getters and setters
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<QuizResult> getResults() {
        return results;
    }

    public void setResults(List<QuizResult> results) {
        this.results = results;
    }

    public int getTotalQuestions() {
        return questions != null ? questions.size() : 0;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
