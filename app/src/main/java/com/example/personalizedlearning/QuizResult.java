package com.example.personalizedlearning;

public class QuizResult {
    private String question;
    private String userAnswer;
    private boolean correct;

    public QuizResult(String question, String userAnswer, boolean correct){
        this.question = question;
        this.userAnswer = userAnswer;
        this.correct = correct;

    }

    public String getQuestion(){
        return question;
    }

    public String getUserAnswer(){
        return userAnswer;
    }


    public boolean isCorrect() {
        return correct;
    }

}
