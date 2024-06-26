package com.example.personalizedlearning;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class CustomQuizAdapter extends ArrayAdapter<QuizResult> { // Changed to QuizResult
    private int resourceLayout;
    private Context mContext;

    public CustomQuizAdapter(Context context, int resource, List<QuizResult> items) { // List of QuizResults
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuizResult quizResult = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceLayout, parent, false);
        }

        TextView tvQuestion = convertView.findViewById(R.id.tvQuestion);
        TextView tvAnswer = convertView.findViewById(R.id.tvAnswer);

        // Handling empty quiz results (spacers)
        if(quizResult.isHeader()){
            tvQuestion.setText(quizResult.getQuestion());
            tvAnswer.setText("");

        } else if (quizResult.getQuestion().isEmpty() && quizResult.getUserAnswer().isEmpty()) {
            tvQuestion.setText(" ");
            tvAnswer.setText(" ");
            convertView.setVisibility(View.GONE);
        } else {
            tvQuestion.setText(quizResult.getQuestion());

            String answerText = quizResult.getUserAnswer() + " (" + (quizResult.isCorrect() ? "Correct" : "Incorrect") + ")";
            SpannableString spannableString = new SpannableString(answerText);
            int start = answerText.indexOf("(") + 1;
            int end = answerText.indexOf(")");
            int color = quizResult.isCorrect() ? Color.GREEN : Color.RED;
            spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvAnswer.setText(spannableString);

            convertView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


}
