package com.english.toeic.view;

import static com.english.toeic.constant.TestConstant.CATEGORY_LISTENING;
import static com.english.toeic.constant.TestConstant.TYPE_QUESTION;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.english.toeic.R;
import com.english.toeic.repository.Answer;
import com.english.toeic.repository.Information;
import com.english.toeic.repository.Part;
import com.english.toeic.controller.AudioHelper;
import com.english.toeic.controller.DatabaseHelper;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    public static final String KEY_INFORMATION = "key_information";
    public static final int NEXT_MODE = 1;
    public static final int PREVIOUS_MODE = 2;
    public static final int SEEK_BAR_ACTION = 3;
    public static final int MEDIA_PLAYER_ACTION = 4;
    public static final long TIME_UPDATE = 1000;
    private Handler mHandler;
    public static Information mInformation;
    public static List<Answer> mAnswers;
    public static MediaPlayer mMpAudio;
    private DatabaseHelper mDatabaseHelper;
    private final String PDF_EXTENSION = ".pdf";
    private final int TIME_STEP = 10000;
    public static List<Part> mParts;
    private Part mCurrentPart;
    private PDFView mPvTest;
    private SeekBar mSbAudio;
    private Button mBtnAudio, mBtnForward, mBtnReplay, mBtnNextPart, mBtnPreviousPart;
    private LinearLayout mLlAudio;
    private boolean mCurrentStatus = false;
    private AudioHelper mAudioHelper;
    private Button.OnClickListener mOnClickListener;
    private SeekBar.OnSeekBarChangeListener mSbChangeListener;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mInformation = (Information) getIntent().getSerializableExtra(KEY_INFORMATION);
        Log.i(TAG, "onCreate(): is called with " + mInformation);

        initView();
        listenView();
        init();

    }

    private void init() {
        Log.i(TAG, "init(): is called");

        mDatabaseHelper = new DatabaseHelper(this, mInformation);
        mParts = mDatabaseHelper.getParts();
        mAnswers = mDatabaseHelper.getAnswers();
        if (mParts == null || mParts.size() == 0) {
            Log.e(TAG, "init(): list of part is null or empty");
            return;
        }
        mCurrentPart = mParts.get(0);
        showPart(mCurrentPart);
        showAudio(mCurrentPart);
        if (mInformation.type.equals(TYPE_QUESTION)) {
            showQuestion(mCurrentPart);
        } else {
            showAnswer(mCurrentPart);
        }


//        mMpAudio = mController.getMedia();
//        mAudioHelper = new AudioHelper(this, mMpAudio, mSbAudio, mBtnAudio);


    }



    private void initView() {
        Log.i(TAG, "initView(): is called");

        mPvTest = findViewById(R.id.pv_test);
        mSbAudio = findViewById(R.id.sb_audio);
        mBtnAudio = findViewById(R.id.btn_audio);
        mBtnForward = findViewById(R.id.btn_forward);
        mBtnReplay = findViewById(R.id.btn_replay);
        mBtnNextPart = findViewById(R.id.btn_next_part);
        mBtnPreviousPart = findViewById(R.id.btn_previous_part);
        mLlAudio = findViewById(R.id.ll_audio);

        mOnClickListener = view -> {
            switch (view.getId()) {
                case R.id.btn_audio:
                    mAudioHelper.updateMediaPlayerSeekBar(mHandler, mRunnable, mCurrentStatus);
                    mCurrentStatus = mAudioHelper.updateStatusAudio(mCurrentStatus);
                    break;
                case R.id.btn_forward:
                    mAudioHelper.updateMediaPlayerSeekBar(TIME_STEP, NEXT_MODE);
                    break;
                case R.id.btn_replay:
                    mAudioHelper.updateMediaPlayerSeekBar(TIME_STEP, PREVIOUS_MODE);
                    break;
                case R.id.btn_next_part: {
                    int index = mParts.indexOf(mCurrentPart) + 1;
                    if (index >= mParts.size()) {
                        index = 0;
                    }
                    mCurrentPart = mParts.get(index);
                    showPart(mCurrentPart);
                    showQuestion(mCurrentPart);
                    showAudio(mCurrentPart);
                }
                break;
                case R.id.btn_previous_part: {
                    int index = mParts.indexOf(mCurrentPart) - 1;
                    if (index < 0) {
                        index = 0;
                    }
                    mCurrentPart = mParts.get(index);
                    showPart(mCurrentPart);
                    showQuestion(mCurrentPart);
                    showAudio(mCurrentPart);
                }
                break;
                default:
                    Log.e(TAG, "onClick(): cannot find any view");
                    break;
            }
        };

        mSbChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mAudioHelper.updateMediaPlayerSeekBar(SEEK_BAR_ACTION);
            }
        };

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, TIME_UPDATE);
                mAudioHelper.updateMediaPlayerSeekBar(MEDIA_PLAYER_ACTION);
                Log.i(TAG, "run: is called");
            }
        };
    }

    private void showPart(Part part) {
        Log.i(TAG, "showPart(): is called");

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), part.file + PDF_EXTENSION);
        mPvTest.fromFile(file).load();
    }

    private void showQuestion(Part part) {
        Log.i(TAG, "showAnswer(): is called");

        List<Answer> answers = getAnswersByPart(part.part);
        QuestionFragment fragment = new QuestionFragment(answers);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_answer_question, fragment)
                .commit();
    }
    private void showAnswer(Part part) {
        Log.i(TAG, "showAnswer(): is called");

        List<Answer> answers = getAnswersByPart(part.part);
        AnswerFragment fragment = new AnswerFragment(part, answers);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_answer_question, fragment)
                .commit();
    }

    private void showAudio(Part part) {
        Log.i(TAG, "showAudio(): is called");

        if (part.getCategory().equals(CATEGORY_LISTENING)) {
            Log.d(TAG, "showAudio(): this is listening test, enable linear layout audio");
            mLlAudio.setVisibility(SeekBar.VISIBLE);
        } else {
            Log.d(TAG, "showAudio(): this is reading test, disable linear layout audio");
            mLlAudio.setVisibility(SeekBar.INVISIBLE);
        }
    }

    private List<Answer> getAnswersByPart(int part) {
        Log.i(TAG, "getAnswersByPart(): is called with part = [" + part + "]");

        List<Answer> answers = new ArrayList<>();
        for (Answer answer : mAnswers) {
            if (answer.part == part) {
                answers.add(answer);
            }
        }
        return answers;
    }

    private void listenView() {
        Log.i(TAG, "listenView(): is called");

        mBtnAudio.setOnClickListener(mOnClickListener);
        mBtnReplay.setOnClickListener(mOnClickListener);
        mBtnForward.setOnClickListener(mOnClickListener);
        mBtnPreviousPart.setOnClickListener(mOnClickListener);
        mBtnNextPart.setOnClickListener(mOnClickListener);
        mSbAudio.setOnSeekBarChangeListener(mSbChangeListener);
    }


}
