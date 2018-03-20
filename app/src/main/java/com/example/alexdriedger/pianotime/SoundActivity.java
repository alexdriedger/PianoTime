package com.example.alexdriedger.pianotime;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SoundActivity extends FragmentActivity {

    private enum MODE {
        KEYBOARD, SOUNDPAD
    }

    private Mixer mMixer;
    private MODE mMode; // TODO : ADD MODE SUPPORT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        mMode = MODE.KEYBOARD;

        mMixer = Mixer.create();
        mMixer.start();

        Button button = findViewById(R.id.record_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMixer.startRecording();
            }
        });

        button = findViewById(R.id.play_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMixer.playRecording(getApplicationContext());
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//        KeyboardFragment keyboardFragment = new KeyboardFragment();
//        fragmentTransaction.add(R.id.sound_activity_main_area, keyboardFragment);
        SoundPadFragment soundPadFragment = new SoundPadFragment();
        fragmentTransaction.add(R.id.sound_activity_main_area, soundPadFragment);
        fragmentTransaction.commit();
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
