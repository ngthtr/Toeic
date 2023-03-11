package com.english.toeic.repository;

import static com.english.toeic.view.TestActivity.MEDIA_PLAYER_ACTION;
import static com.english.toeic.view.TestActivity.TIME_UPDATE;
import static com.english.toeic.view.TestActivity.mAnswers;
import static com.english.toeic.constant.TestConstant.ANSWER_A;
import static com.english.toeic.constant.TestConstant.ANSWER_B;
import static com.english.toeic.constant.TestConstant.ANSWER_C;
import static com.english.toeic.constant.TestConstant.ANSWER_D;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.english.toeic.R;
import com.english.toeic.controller.AudioHelper;
import com.english.toeic.controller.DatabaseHelper;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AnswerListeningAdapter extends BaseAdapter {
    private static final String TAG = "AnswerListeningAdapter";
    private Context mContext;
    private boolean mCurrentStatus = false;
    private Handler mHandler;
    private List<Answer> mAnswers;

    public AnswerListeningAdapter(Context context, List<Answer> answers) {
        mContext = context;
        mHandler = new Handler();
        mAnswers = answers;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewAnswer;
        if (view == null) {
            viewAnswer = View.inflate(viewGroup.getContext(), R.layout.item_answer_listening, null);
        } else {
            viewAnswer = view;
        }

        Drawable greenDrawable = mContext.getDrawable(R.drawable.ic_green_circle);
        Drawable redDrawable = mContext.getDrawable(R.drawable.ic_red_circle);

        Answer answer = (Answer) getItem(position);

        setBackGround(viewAnswer, answer.answer, redDrawable);
        setBackGround(viewAnswer, answer.correct, greenDrawable);

        TextView tvAnswerNumber = viewAnswer.findViewById(R.id.tv_answer_number);
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext, null);
        Button btnAudio = viewAnswer.findViewById(R.id.btn_audio_answer);
        SeekBar sbAudio = viewAnswer.findViewById(R.id.sb_audio_answer);
        MediaPlayer mediaPlayer = databaseHelper.createMediaPlayer(answer.audio);
        AudioHelper audioHelper = new AudioHelper(mContext, mediaPlayer, sbAudio, btnAudio);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, TIME_UPDATE);
                audioHelper.updateMediaPlayerSeekBar(MEDIA_PLAYER_ACTION);
            }
        };

        tvAnswerNumber.setText(String.valueOf(answer.number));
        btnAudio.setOnClickListener(view1 -> {
            audioHelper.updateMediaPlayerSeekBar(mHandler, runnable, mCurrentStatus);
            mCurrentStatus = audioHelper.updateStatusAudio(mCurrentStatus);
        });
        sbAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioHelper.updateMediaPlayerSeekBar(MEDIA_PLAYER_ACTION);
            }
        });

        return viewAnswer;
    }


    private void setBackGround(View view, String answer, Drawable drawable) {
        Log.i(TAG, "setBackGround(): is called");

        if (answer == null) {
            Log.d(TAG, "setBackGround: answer is null");
            return;
        }

        switch (answer) {
            case ANSWER_A:
                view.findViewById(R.id.iv_answer_listen_a).setBackground(drawable);
                break;
            case ANSWER_B:
                view.findViewById(R.id.iv_answer_listen_b).setBackground(drawable);
                break;
            case ANSWER_C:
                view.findViewById(R.id.iv_answer_listen_c).setBackground(drawable);
                break;
            case ANSWER_D:
                view.findViewById(R.id.iv_answer_listen_d).setBackground(drawable);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return mAnswers.size();
    }

}
