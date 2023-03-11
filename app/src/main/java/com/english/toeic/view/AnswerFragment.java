package com.english.toeic.view;

import static com.english.toeic.constant.TestConstant.CATEGORY_LISTENING;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.english.toeic.R;
import com.english.toeic.repository.Answer;
import com.english.toeic.repository.AnswerListeningAdapter;
import com.english.toeic.repository.AnswerReadingAdapter;
import com.english.toeic.repository.Part;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AnswerFragment extends Fragment {
    private static final String TAG = "AnswerFragment";
    private ListView mLvAnswer;
    private Context mContext;
    private AnswerReadingAdapter answerReadingAdapter;
    private AnswerListeningAdapter answerListeningAdapter;
    private List<Answer> mAnswers;
    private Part mPart;

    public AnswerFragment(Part part, List<Answer> answers) {
        mPart = part;
        mAnswers = answers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);
        mContext = getActivity();
        initView(view);
        init();

        return view;
    }

    private void init() {
        Log.i(TAG, "init(): is called");

        if (mPart.getCategory().equals(CATEGORY_LISTENING)) {
            Log.d(TAG, "init(): set answer listening adapter");
            answerListeningAdapter = new AnswerListeningAdapter(mContext, mAnswers);
            mLvAnswer.setAdapter(answerListeningAdapter);
        } else {
            Log.d(TAG, "init(): set answer reading adapter");
            answerReadingAdapter = new AnswerReadingAdapter(mContext, mAnswers);
            mLvAnswer.setAdapter(answerReadingAdapter);
        }
    }


    private void initView(View view) {
        Log.i(TAG, "initView(): is called");

        mLvAnswer = view.findViewById(R.id.lv_answer);

    }
}