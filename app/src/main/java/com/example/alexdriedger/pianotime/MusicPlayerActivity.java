package com.example.alexdriedger.pianotime;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MusicPlayerActivity extends AppCompatActivity {

    private MediaPlayer mMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        initMediaPlayer();

        final Button mPlayButton = findViewById(R.id.button_play);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMP.start();
            }
        });

        final Button mPauseButton = findViewById(R.id.button_stop);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMP.pause();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseResources();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMediaPlayer();
    }

    private void releaseResources() {
        if (mMP != null) {
            mMP.stop();
            mMP.release();
            mMP = null;
        }
    }

    private void initMediaPlayer() {
        releaseResources();
        mMP = MediaPlayer.create(this, R.raw.moana_midi);
        mMP.start();mMP.pause(); // To stop pause button from causing error state
    }
}
