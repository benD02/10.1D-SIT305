package com.example.personalizedlearning;

public class QuizResult {
    private  String question;
    private  String userAnswer;
    private boolean correct;
    private boolean isHeader;  // New field to determine if this is a header


    public QuizResult(String question, String userAnswer, boolean correct){
        this.question = question;
        this.userAnswer = userAnswer;
        this.correct = correct;
        this.isHeader = false;  // Default value


    }

    public QuizResult(String header) {
        this.question = header;
        this.userAnswer = "";
        this.correct = false;
        this.isHeader = true;
    }

    public  String getQuestion(){
        return question;
    }

    public  String getUserAnswer(){
        return userAnswer;
    }


    public boolean isCorrect() {
        return correct;
    }

    public boolean isHeader() {
        return isHeader;
    }

}
