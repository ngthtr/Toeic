package com.english.toeic.controller;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DownloadHelper {
    private static final String TAG = "DownloadHelper";
    private Context mContext;
    private DownloadManager mDownloadManager;

    public DownloadHelper(Context context) {
        mContext = context;
    }

    public long download(String link, String fileName) {
        Log.i(TAG, "download(): is called with link = [" + link + "], file_name = [" + fileName + "]");

        mDownloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://drive.google.com/u/0/uc?id=" + link + "&export=download"))
                .setTitle("File Download")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName + ".pdf");
        return mDownloadManager.enqueue(request);

    }

    public boolean checkExist(String fileName) {
        Log.i(TAG, "checkExist(): is called with file_name = [" + fileName + "]");

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".pdf");
        return file.exists();
    }

    public boolean delete(String fileName) {
        Log.i(TAG, "delete(): is called");

        if (checkExist(fileName)) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            return file.delete();
        }
        return false;
    }



}
