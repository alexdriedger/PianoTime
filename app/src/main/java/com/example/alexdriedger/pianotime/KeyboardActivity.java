package com.example.alexdriedger.pianotime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.billthefarmer.mididriver.MidiDriver;

public class KeyboardActivity extends AppCompatActivity {

    private MidiDriver midiDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        keyInit();

        // Instantiate the driver.
        midiDriver = new MidiDriver();
        midiDriver.start();
    }

    private void keyInit() {
        final Button mButtonC = findViewById(R.id.button_c);
        mButtonC.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x3C, (byte) 0x7F));
        final Button mButtonCSharp = findViewById(R.id.button_c_sharp);
        mButtonCSharp.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x3D, (byte) 0x7F));
        final Button mButtonD = findViewById(R.id.button_d);
        mButtonD.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x3E, (byte) 0x7F));
        final Button mButtonDSharp = findViewById(R.id.button_d_sharp);
        mButtonDSharp.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x3F, (byte) 0x7F));
        final Button mButtonE = findViewById(R.id.button_e);
        mButtonE.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x40, (byte) 0x7F));
        final Button mButtonF = findViewById(R.id.button_f);
        mButtonF.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x41, (byte) 0x7F));
        final Button mButtonFSharp = findViewById(R.id.button_f_sharp);
        mButtonFSharp.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x42, (byte) 0x7F));
        final Button mButtonG = findViewById(R.id.button_g);
        mButtonG.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x43, (byte) 0x7F));
        final Button mButtonGSharp = findViewById(R.id.button_g_sharp);
        mButtonGSharp.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x44, (byte) 0x7F));
        final Button mButtonA = findViewById(R.id.button_a);
        mButtonA.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x45, (byte) 0x7F));
        final Button mButtonASharp = findViewById(R.id.button_a_sharp);
        mButtonASharp.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x46, (byte) 0x7F));
        final Button mButtonB = findViewById(R.id.button_b);
        mButtonB.setOnTouchListener(new KeyboardOnTouchListener((byte) 0x00, (byte) 0x47, (byte) 0x7F));
    }

    private void playNote(byte chan, byte note, byte vel) {

        // Construct a note ON message for the middle C at maximum velocity on channel 1:
        byte [] event = new byte[3];
        event[0] = (byte) (0x90 | chan);  // 0x90 = note On, 0x00 = channel 1
        event[1] = note;  // 0x3C = middle C
        event[2] = vel;  // 0x7F = the maximum velocity (127)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

        Log.d("Main", "playNote");

    }

    // private void stopNote(byte chan, byte note)
    private void stopNote(byte chan, byte note) {

        // Construct a note OFF message for the middle C at minimum velocity on channel 1:
        byte[] event = new byte[3];
        event[0] = (byte) (0x80 | chan);  // 0x80 = note Off, 0x00 = channel 1
        event[1] = note;  // 0x3C = middle C
        event[2] = (byte) 0x00;  // 0x00 = the minimum velocity (0)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

        Log.d("Main", "pauseNote");

    }

    private class KeyboardOnTouchListener implements View.OnTouchListener {

        // TODO : MAKE METHODS TO CHANGE THESE VALUES
        // TODO : MAKE A KEYBOARDKEY CLASS THAT CREATES AND HOLDS ONTO THE LISTENER SO THAT VALUES
        //        CAN BE CHANGED AFTER ASSIGNING THE LISTENER

        private byte mChan;
        private byte mNote;
        private byte mVel;

        KeyboardOnTouchListener(byte chan, byte note, byte vel) {
            mChan = chan;
            mNote = note;
            mVel = vel;
        }

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                playNote(mChan, mNote, mVel);
            }
            else if (e.getAction() == MotionEvent.ACTION_UP) {
                stopNote(mChan, mNote);
            }
            return true;
        }
    }
}
