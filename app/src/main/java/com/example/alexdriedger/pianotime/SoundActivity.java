package com.example.alexdriedger.pianotime;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SoundActivity extends FragmentActivity {

    private Mixer mMixer;
    private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mMixer = Mixer.create();
        mMixer.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMixer == null) {
            mMixer = Mixer.create();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMixer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMixer.stop();
    }
}