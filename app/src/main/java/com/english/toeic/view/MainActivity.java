package com.english.toeic.view;


import static com.english.toeic.constant.DatabaseContract.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.english.toeic.R;
import com.english.toeic.repository.Database;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button mBtnStart;
    private static final int REQUEST_PERMISSIONS = 100;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();

//        this.deleteDatabase(DATABASE_NAME);
//        Database mDatabaseHelper = Database.getInstance(this);
//        mDatabaseHelper.getWritableDatabase();

        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
//            Information information = new Information(2022, 1, "lc", "q", new ArrayList<>(Arrays.asList(1, 2, 3)));
//            Intent intent = new Intent(this, TestActivity.class);
//            intent.putExtra(KEY_INFORMATION, information);
//            startActivity(intent);
        });

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: request success");
            } else {
                Log.i(TAG, "onRequestPermissionsResult: request failure");
            }
        }
    }









}