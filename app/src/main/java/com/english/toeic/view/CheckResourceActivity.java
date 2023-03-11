package com.english.toeic.view;

import static com.english.toeic.view.TestActivity.KEY_INFORMATION;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.english.toeic.R;
import com.english.toeic.repository.Database;
import com.english.toeic.repository.Information;
import com.english.toeic.repository.Part;
import com.english.toeic.controller.DownloadHelper;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CheckResourceActivity extends AppCompatActivity {
    private static final String TAG = "CheckResourceActivity";
    private DownloadHelper mDownloadHelper;
    private Information mInformation;
    private Database mDatabase;
    private Handler mHandler;
    private Runnable mRunnable;
    private List<String> mFileNotExist;
    private TextView mTvNotification;
    private String NOTIFICATION = "Downloading...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_resource);

        mInformation = (Information) getIntent().getSerializableExtra(KEY_INFORMATION);
        init();
        checkExistedFile();
    }

    private void init() {
        Log.i(TAG, "init(): is called " + mInformation);

        mDownloadHelper = new DownloadHelper(this);
        mDatabase = Database.getInstance(this);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mTvNotification.setText(NOTIFICATION);
                mHandler.postDelayed(this, 3000);
                boolean isAllExisted = true;
                for (String fileName : mFileNotExist) {
                    if (!mDownloadHelper.checkExist(fileName)) {
                        isAllExisted = false;
                        break;
                    }
                }
                if (!isAllExisted) {
                    NOTIFICATION += ".";
                } else {
                    mHandler.removeCallbacks(this);
                    startTestActivity();
                }
            }
        };
        mFileNotExist = new ArrayList<>();
        mTvNotification = findViewById(R.id.tv_notification_download);
    }

    private void checkExistedFile() {
        Log.i(TAG, "checkExistedFile(): is called");

        boolean isAllExisted = true;
        for (int part : mInformation.parts) {
            Part partFile = mDatabase.getPartFileLink(mInformation.year, mInformation.test, part);
            boolean isExisted = mDownloadHelper.checkExist(partFile.file);
            if (!isExisted) {
                mFileNotExist.add(partFile.file);
                mDownloadHelper.download(partFile.link, partFile.file);
                isAllExisted = false;
            }
        }
        if (!isAllExisted) {
            mHandler.postDelayed(mRunnable, 0);
        } else {
            startTestActivity();
        }
    }

    private void startTestActivity() {
        Log.i(TAG, "startTestActivity(): is called");

        Intent intent = new Intent(CheckResourceActivity.this, TestActivity.class);
        intent.putExtra(KEY_INFORMATION, mInformation);
        startActivity(intent);
        finish();
    }
}