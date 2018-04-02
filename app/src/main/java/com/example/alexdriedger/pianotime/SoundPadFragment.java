package com.example.alexdriedger.pianotime;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SoundPadFragment extends AbstractMusicInteractionFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_sound_pad, container, false);
        initListeners(v);
        return v;
    }

    private void initListeners(View v) {

        Button mButton = v.findViewById(R.id.button_soundpad_1_1);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(0));
        mButton = v.findViewById(R.id.button_soundpad_1_2);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(1));
        mButton = v.findViewById(R.id.button_soundpad_1_3);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(2));
        mButton = v.findViewById(R.id.button_soundpad_2_1);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(3));
        mButton = v.findViewById(R.id.button_soundpad_2_2);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(4));
        mButton = v.findViewById(R.id.button_soundpad_2_3);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(5));
        mButton = v.findViewById(R.id.button_soundpad_3_1);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(6));
        mButton = v.findViewById(R.id.button_soundpad_3_2);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(7));
        mButton = v.findViewById(R.id.button_soundpad_3_3);
        mButton.setOnTouchListener(new MusicInteractionOnTouchListener(8));
    }
}
