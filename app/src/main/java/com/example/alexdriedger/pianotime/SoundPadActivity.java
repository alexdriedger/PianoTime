package com.example.alexdriedger.pianotime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class SoundPadActivity extends AppCompatActivity {

    // TODO : MAKE ABSTRACT CLASS FOR MIDI ACTIVTIES TO HANDLE MIDI CONTROLLER LIFE CYCLE METHODS

    private MidiController mMidiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_pad);

        mMidiController = MidiController.create();
        mMidiController.start();

        initListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMidiController == null) {
            mMidiController = MidiController.create();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMidiController.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMidiController.stop();
    }

    private void initListeners() {

        Button mButton = findViewById(R.id.button_soundpad_1_1);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x01, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_1_2);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x02, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_1_3);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x03, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_2_1);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x04, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_2_2);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x05, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_2_3);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x06, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_3_1);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x07, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_3_2);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x08, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_3_3);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x09, (byte) 0x3C, (byte) 0x7F));

        // Change instruments
        MidiController.changeInstrument((byte) 0x70, (byte) 0x01);
        MidiController.changeInstrument((byte) 0x71, (byte) 0x02);
        MidiController.changeInstrument((byte) 0x78, (byte) 0x03);
        MidiController.changeInstrument((byte) 0x77, (byte) 0x04);
        MidiController.changeInstrument((byte) 0x72, (byte) 0x05);
        MidiController.changeInstrument((byte) 0x73, (byte) 0x06);
        MidiController.changeInstrument((byte) 0x74, (byte) 0x07);
        MidiController.changeInstrument((byte) 0x75, (byte) 0x08);
        MidiController.changeInstrument((byte) 0x76, (byte) 0x09);


    }
}
