package com.english.toeic.controller;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.english.toeic.repository.Answer;
import com.english.toeic.repository.Database;
import com.english.toeic.repository.Information;
import com.english.toeic.repository.Part;

import java.util.ArrayList;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.N)
public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private static final int VALUE_ZERO = 0;
    private final String TYPE_AUDIO = "raw";
    private Context mContext;
    private Information mInformation;
    private Database mDatabase;

    public DatabaseHelper(Context context, Information information) {
        mContext = context;
        mInformation = information;
        mDatabase = Database.getInstance(context);
    }

    public MediaPlayer createMediaPlayer(String fileName) {
        Log.i(TAG, "createMediaPlayer(): is called");

        int id = mContext.getResources().getIdentifier(fileName, TYPE_AUDIO, mContext.getPackageName());
        if (id == VALUE_ZERO) {
            Log.e(TAG, "createMediaPlayer(): cannot find audio file name");
            return null;
        }
        Log.d(TAG, "createMediaPlayer(): id_audio = [" + id + "]");
        return MediaPlayer.create(mContext, id);
    }

    public List<Answer> getAnswers() {
        Log.i(TAG, "getAnswers(): is called");

        List<Answer> answers = new ArrayList<>();
        for (int part : mInformation.parts) {
            List<Answer> answersByPart = mDatabase.getAnswers(mInformation.year, mInformation.test, part);
            answers.addAll(answersByPart);
        }
        return answers;
    }

    public MediaPlayer getMedia() {
        Log.i(TAG, "getMedia(): is called");

        List<String> nameAudios = mDatabase.getMainAudio(mInformation.year, mInformation.test);
        if (nameAudios != null) {
            Log.d(TAG, "getMedia(): get success with name_audio = [" + nameAudios.get(0) + "]");
            return createMediaPlayer(nameAudios.get(0));
        }
        Log.i(TAG, "getMedia(): Fail to get name file audio");
        return null;
    }

    public List<Part> getParts() {
        Log.i(TAG, "getParts(): is called");

        List<Part> parts = new ArrayList<>();
        for (int part : mInformation.parts) {
            Part partItem = mDatabase.getPart(mInformation.year, mInformation.test, part);
            parts.add(partItem);
        }
        return parts;
    }


}
