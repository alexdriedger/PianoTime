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

        mMP = MediaPlayer.create(this, R.raw.moana_midi);

        final Button mPlayButton = findViewById(R.id.button_play);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!mMP.isPlaying()) {
                    mMP.start();
//                }
            }
        });

        final Button mPauseButton = findViewById(R.id.button_stop);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!mMP.isPlaying()) {
                    mMP.pause();
//                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMP.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
