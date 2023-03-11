package com.english.toeic.repository;

import static com.english.toeic.constant.TestConstant.ANSWER_A;
import static com.english.toeic.constant.TestConstant.ANSWER_B;
import static com.english.toeic.constant.TestConstant.ANSWER_C;
import static com.english.toeic.constant.TestConstant.ANSWER_D;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.english.toeic.R;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AnswerReadingAdapter extends BaseAdapter {
    private static final String TAG = "AnswerReadingAdapter";
    private Context mContext;
    private List<Answer> mAnswers;
    public AnswerReadingAdapter(Context context, List<Answer> answer) {
        mContext = context;
        mAnswers = answer;
    }

    @Override
    public int getCount() {
        return mAnswers.size();
    }

    @Override
    public Object getItem(int i) {
        return mAnswers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

     @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewAnswerReading;
        if (view == null) {
            viewAnswerReading = View.inflate(viewGroup.getContext(), R.layout.item_answer_reading, null);
        } else {
            viewAnswerReading = view;
        }

        Answer answer = (Answer) getItem(i);
        ((TextView) viewAnswerReading.findViewById(R.id.tv_answer_number)).setText(answer.number);
        setColorAnswer(viewAnswerReading, answer.correct, mContext.getResources().getDrawable(R.drawable.ic_green_circle));
        setColorAnswer(viewAnswerReading, answer.answer, mContext.getResources().getDrawable(R.drawable.ic_red_circle));

        return viewAnswerReading;
    }

    private void setColorAnswer(View view, String answer, Drawable drawable) {
        Log.i(TAG, "setColorAnswer(): is called");

        switch (answer) {
            case ANSWER_A:
                view.findViewById(R.id.iv_answer_a).setBackground(drawable);
                break;
            case ANSWER_B:
                view.findViewById(R.id.iv_answer_b).setBackground(drawable);
                break;
            case ANSWER_C:
                view.findViewById(R.id.iv_answer_c).setBackground(drawable);
                break;
            case ANSWER_D:
                view.findViewById(R.id.iv_answer_d).setBackground(drawable);
                break;
        }
    }
}
