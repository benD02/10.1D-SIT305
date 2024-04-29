package com.example.personalizedlearning;

public class QuizResult {
    private String question;
    private static String userAnswer;
    private static boolean correct;

    public QuizResult(String question, String userAnswer, boolean correct){
        this.question = question;
        this.userAnswer = userAnswer;
        this.correct = correct;

    }

    public String getQuestion(){
        return question;
    }

    public static String getUserAnswer(){
        return userAnswer;
    }


    public static boolean isCorrect() {
        return correct;
    }

}
