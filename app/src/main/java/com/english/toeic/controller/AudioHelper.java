package com.english.toeic.controller;

import static com.english.toeic.view.TestActivity.MEDIA_PLAYER_ACTION;
import static com.english.toeic.view.TestActivity.NEXT_MODE;
import static com.english.toeic.view.TestActivity.SEEK_BAR_ACTION;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;

import com.english.toeic.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AudioHelper {
    private static final String TAG = "AudioHelper";
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private Button mButton;

    public AudioHelper(Context context, MediaPlayer mediaPlayer, SeekBar seekBar, Button button) {
        mContext = context;
        mMediaPlayer = mediaPlayer;
        mSeekBar = seekBar;
        mButton = button;
    }

    public void updateMediaPlayerSeekBar(int mode) {
        Log.i(TAG, "updateAudioSeekBar(): is called with mode = [" + mode + "]");

        if (mode == SEEK_BAR_ACTION) {
            int positionAudio = (int) Math.floor(mSeekBar.getProgress() / 100.0 * mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(positionAudio);
            return;
        }

        if (mode == MEDIA_PLAYER_ACTION) {
            int positionSeekBar = (int) Math.floor(mMediaPlayer.getCurrentPosition() * 100.0 / mMediaPlayer.getDuration());
            mSeekBar.setProgress(positionSeekBar);
        }
    }

    public void updateMediaPlayerSeekBar(int time, int mode) {
        Log.i(TAG, "updateAudioSeekBar(): is called with time = [" + time + "], mode = [" + mode + "]");

        int newPositionAudio;

        if (mode == NEXT_MODE) {
            newPositionAudio = mMediaPlayer.getCurrentPosition() + time;
        } else {
            newPositionAudio = mMediaPlayer.getCurrentPosition() - time;
        }
        mMediaPlayer.seekTo(newPositionAudio);
        int positionSeekBar = (int) Math.floor(newPositionAudio * 100.0 / mMediaPlayer.getDuration());
        mSeekBar.setProgress(positionSeekBar);
    }

    public void updateMediaPlayerSeekBar(Handler handler, Runnable runnable, boolean currentStatus) {
        Log.i(TAG, "updateMediaPlayerSeekBar(): is called with current_status = [" + currentStatus + "]");

        if (currentStatus) {
            handler.removeCallbacks(runnable);
            return;
        }
        handler.postDelayed(runnable, 0);
    }

    public boolean updateStatusAudio(boolean currentStatus) {
        Log.i(TAG, "updateStatusAudio(): is called with current_status = [" + currentStatus + "]");

        if (!currentStatus) {
            mMediaPlayer.start();
            Drawable icPlay = mContext.getDrawable(R.drawable.ic_play);
            mButton.setBackground(icPlay);
            return true;
        } else {
            mMediaPlayer.pause();
            Drawable icPause = mContext.getDrawable(R.drawable.ic_pause);
            mButton.setBackground(icPause);
            return false;
        }
    }
}
