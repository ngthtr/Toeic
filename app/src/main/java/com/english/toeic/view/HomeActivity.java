package com.english.toeic.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.english.toeic.R;
import com.english.toeic.repository.Database;
import com.english.toeic.repository.TestAdapter;

import java.util.ArrayList;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private List<String> mTests;
    private ListView mLvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        init();
    }

    private void initView() {
        Log.i(TAG, "initView(): is called");

        mLvTest = findViewById(R.id.lv_test);
    }

    private void init() {
        Log.i(TAG, "init(): is called");

        mTests = Database.getInstance(this).getAllYears();
        if (mTests == null) {
            Log.e(TAG, "init(): cannot get all tests");
            return;
        }

        TestAdapter adapter = new TestAdapter(this, mTests);
        mLvTest.setAdapter(adapter);
    }
}