package com.example.alexdriedger.pianotime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.example.alexdriedger.pianotime.MidiController;

import org.billthefarmer.mididriver.MidiDriver;

public class KeyboardActivity extends AppCompatActivity {

//    private MidiController mMixer;
    private Mixer mMixer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        keyInit();

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

    /**
     * Initializes keys for the touch screen piano
     */
    private void keyInit() {
        final Button mButtonC = findViewById(R.id.button_c);
        mButtonC.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x3C, (byte) 0x7F));
        final Button mButtonCSharp = findViewById(R.id.button_c_sharp);
        mButtonCSharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x3D, (byte) 0x7F));
        final Button mButtonD = findViewById(R.id.button_d);
        mButtonD.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x3E, (byte) 0x7F));
        final Button mButtonDSharp = findViewById(R.id.button_d_sharp);
        mButtonDSharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x3F, (byte) 0x7F));
        final Button mButtonE = findViewById(R.id.button_e);
        mButtonE.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x40, (byte) 0x7F));
        final Button mButtonF = findViewById(R.id.button_f);
        mButtonF.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x41, (byte) 0x7F));
        final Button mButtonFSharp = findViewById(R.id.button_f_sharp);
        mButtonFSharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x42, (byte) 0x7F));
        final Button mButtonG = findViewById(R.id.button_g);
        mButtonG.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x43, (byte) 0x7F));
        final Button mButtonGSharp = findViewById(R.id.button_g_sharp);
        mButtonGSharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x44, (byte) 0x7F));
        final Button mButtonA = findViewById(R.id.button_a);
        mButtonA.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x45, (byte) 0x7F));
        final Button mButtonASharp = findViewById(R.id.button_a_sharp);
        mButtonASharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x46, (byte) 0x7F));
        final Button mButtonB = findViewById(R.id.button_b);
        mButtonB.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x47, (byte) 0x7F));
    }

}
