package com.example.alexdriedger.pianotime;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SoundPadFragment extends android.support.v4.app.Fragment {

//    private MidiController mMidiController;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sound_pad);
//
//        mMidiController = MidiController.create();
//        mMidiController.start();
//
//        initListeners();
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_sound_pad, container, false);
        initListeners(v);
        return v;
    }

    private void initListeners(View v) {

        Button mButton = v.findViewById(R.id.button_soundpad_1_1);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x01, (byte) 0x3C, (byte) 0x7F));
        mButton = v.findViewById(R.id.button_soundpad_1_2);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x02, (byte) 0x2C, (byte) 0x7F));
        mButton = v.findViewById(R.id.button_soundpad_1_3);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x03, (byte) 0x41, (byte) 0x7F));
        mButton = v.findViewById(R.id.button_soundpad_2_1);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x04, (byte) 0x43, (byte) 0x7F));
        mButton = v.findViewById(R.id.button_soundpad_2_2);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x05, (byte) 0x3F, (byte) 0x7F));
        mButton = v.findViewById(R.id.button_soundpad_2_3);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x06, (byte) 0x2C, (byte) 0x7F));
        mButton = v.findViewById(R.id.button_soundpad_3_1);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x07, (byte) 0x2D, (byte) 0x7F));
        mButton = v.findViewById(R.id.button_soundpad_3_2);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x08, (byte) 0x2E, (byte) 0x7F));
        mButton = v.findViewById(R.id.button_soundpad_3_3);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x09, (byte) 0x3C, (byte) 0x7F));
    }
}
