package com.example.alexdriedger.pianotime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import org.billthefarmer.mididriver.MidiDriver;

public class SoundPadActivity extends AppCompatActivity {

    // TODO : MAKE ABSTRACT CLASS FOR MIDI ACTIVTIES TO HANDLE MIDI CONTROLLER DRY

    private MidiController mMidiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_pad);

        // Instantiate the driver.
        // TODO : LIFE CYCLE METHODS FOR MIDI DRIVER
        // TODO : CHECK IF HAVING MULTIPLE MIDI DRIVERS MAKES WEIRD SOUNDS OR IF IT WAS FROM NOT STOPPING IT
//        if (midiDriver == null) {
//            midiDriver = new MidiDriver();
//        }

        mMidiController = MidiController.create();
        mMidiController.start();

        initListeners();

//        midiDriver.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (midiDriver == null) {
//            midiDriver = new MidiDriver();
//        }
        if (mMidiController == null) {
            mMidiController = MidiController.create();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        midiDriver.start();
        mMidiController.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        midiDriver.stop();
        mMidiController.stop();
    }

    private void initListeners() {

        Button mButton = findViewById(R.id.button_soundpad_1_1);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x01, (byte) 0x3C, (byte) 0x7F));
        mButton = findViewById(R.id.button_soundpad_1_2);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x02, (byte) 0x3C, (byte) 0x7F));
        mButton =findViewById(R.id.button_soundpad_1_3);
        mButton.setOnTouchListener(new MidiOnTouchListener((byte) 0x03, (byte) 0x3C, (byte) 0x7F));

        // Change instruments
        MidiController.changeInstrument((byte) 0x70, (byte) 0x01);
        MidiController.changeInstrument((byte) 0x71, (byte) 0x02);
        MidiController.changeInstrument((byte) 0x78, (byte) 0x03);
    }
}
