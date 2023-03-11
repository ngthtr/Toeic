package com.english.toeic.repository;

import static com.english.toeic.constant.TestConstant.ANSWER_A;
import static com.english.toeic.constant.TestConstant.ANSWER_B;
import static com.english.toeic.constant.TestConstant.ANSWER_C;
import static com.english.toeic.constant.TestConstant.ANSWER_D;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.english.toeic.R;

import java.util.List;

public class QuestionAdapter extends BaseAdapter {
    private static final String TAG = "AnswerAdapter";
    private List<Answer> mArrAnswer;

    public QuestionAdapter(List<Answer> arrAnswer) {
        mArrAnswer = arrAnswer;
    }


    @Override
    public int getCount() {
        return mArrAnswer.size();
    }

    @Override
    public Object getItem(int i) {
        return mArrAnswer.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewAnswer;

        if (view == null) {
            viewAnswer = View.inflate(viewGroup.getContext(), R.layout.item_question, null);
        } else {
            viewAnswer = view;
        }

        try {
            Answer answer = (Answer) getItem(position);
            ((TextView) viewAnswer.findViewById(R.id.tv_answer_number)).setText(String.valueOf(answer.number));
            viewAnswer.findViewById(R.id.rb_answer_a).setOnClickListener(view1 -> answer.answer = ANSWER_A);
            viewAnswer.findViewById(R.id.rb_answer_b).setOnClickListener(view1 -> answer.answer = ANSWER_B);
            viewAnswer.findViewById(R.id.rb_answer_c).setOnClickListener(view1 -> answer.answer = ANSWER_C);
            viewAnswer.findViewById(R.id.rb_answer_d).setOnClickListener(view1 -> answer.answer = ANSWER_D);
        } catch (NullPointerException e) {
            Log.e(TAG, "getView(): cannot find element", e);
        }

        return viewAnswer;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return mArrAnswer.size();
    }


}
