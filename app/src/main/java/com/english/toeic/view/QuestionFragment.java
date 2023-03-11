package com.english.toeic.view;

import static com.english.toeic.view.TestActivity.mMpAudio;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.english.toeic.R;
import com.english.toeic.repository.Answer;
import com.english.toeic.repository.QuestionAdapter;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class QuestionFragment extends Fragment {
    private static final String TAG = "QuestionFragment";
    private final String CONFIRM_YES = "Yes";
    private final String CONFIRM_NO = "No";
    private final String CONFIRM_TITLE = "Confirm Submit";
    private final String CONFIRM_MESSAGE = "Do you want to submit?";
    private Context mContext;
    private ListView mLvAnswer;
    private Button mBtnSubmit;
    private QuestionAdapter mQuestionAdapter;
    private Button.OnClickListener mOnClickListener;
    private List<Answer> mAnswers;

    public QuestionFragment(List<Answer> answers) {
        mAnswers = answers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        initView(view);
        listenView();
        init();
        return view;
    }

    private void init() {
        Log.i(TAG, "init(): is called");

        mContext = getActivity();
        mQuestionAdapter = new QuestionAdapter(mAnswers);
        mLvAnswer.setAdapter(mQuestionAdapter);
    }

    private void initView(View view) {
        Log.i(TAG, "initView(): is called");

        mBtnSubmit = view.findViewById(R.id.btn_submit);
        mLvAnswer = view.findViewById(R.id.lv_question);

        mOnClickListener = view1 -> {
            switch (view1.getId()) {
                case R.id.btn_submit:
                    submitAnswer();
                    break;
                default:
                    Log.e(TAG, "onClick(): cannot find any view");
                    break;
            }
        };
    }

    private void listenView() {
        Log.i(TAG, "listenView(): is called");

        mBtnSubmit.setOnClickListener(mOnClickListener);
    }

    private void submitAnswer() {
        Log.i(TAG, "showDialogConfirm(): is called");

        new AlertDialog.Builder(mContext)
                .setTitle(CONFIRM_TITLE)
                .setMessage(CONFIRM_MESSAGE)
                .setPositiveButton(CONFIRM_YES, (dialogInterface, i) -> {
//                    mMpAudio.stop();
                    Intent intent = new Intent(mContext, ChartActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(CONFIRM_NO, null)
                .show();
    }
}