package com.example.alexdriedger.pianotime;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.leff.midi.event.ProgramChange;

import java.security.Key;

public class SoundActivity extends FragmentActivity implements ControlBarFragment.OnControlInteractionListener{

    private static final String KEYBOARD_FRAG_TAG = "KEYBOARD_FRAG";
    private static final String SOUNDPAD_FRAG_TAG = "SOUNDPAD_FRAG";
    private static final String CONTROL_BAR_FRAG_TAG = "CONTROL_BAR_FRAG_TAG";
    private static final MODE DEFAULT_MODE = MODE.KEYBOARD;

    protected enum MODE {
        KEYBOARD, SOUNDPAD
    }

    private Mixer mMixer;
    private MODE mMode; // TODO : ADD MODE SUPPORT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        mMixer = Mixer.create();
        mMixer.start();

        mMode = DEFAULT_MODE;

//        Button button = findViewById(R.id.switch_to_keyboard);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                mMixer.startRecording();
//                initModeKeyboard();
//            }
//        });
//
//        button = findViewById(R.id.switch_to_soundpad);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                mMixer.playRecording(getApplicationContext());
//                initModeSoundpad();
//            }
//        });
//
//        button = findViewById(R.id.start_record_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMixer.startRecording();
//
//                // TODO : REMOVE THIS, IT IS SUPER SKETCH
//                if (mMode == MODE.SOUNDPAD) {
//                    initModeSoundpad();
//                }
//            }
//        });
//
//        button = findViewById(R.id.play_recording_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMixer.stopRecording();
//                mMixer.playRecording(getApplicationContext());
//                mMixer.newTrack();
//            }
//        });

        // TODO : FRAGMENT TRANSITIONS
        // TODO : GENERALIZE INIT KEYBOARD AND SOUNDPAD METHODS AND TAKE A MODE INSTEAD

        initModeKeyboard();
        initControlBar();



//        SoundPadFragment soundPadFragment = new SoundPadFragment();
//        fragmentTransaction.add(R.id.sound_activity_main_area, soundPadFragment);

    }

    private void initControlBar() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment f = fragmentManager.findFragmentByTag(CONTROL_BAR_FRAG_TAG);

        if (f != null) {
            Log.w("SoundActivity", "ControlBar fragment already initialized");
            return;
        }

        ControlBarFragment cbf = ControlBarFragment.newInstance(mMode, false);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.control_bar_fragment, cbf, CONTROL_BAR_FRAG_TAG);
        fragmentTransaction.commit();

    }

    private void initModeKeyboard() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment f = fragmentManager.findFragmentByTag(KEYBOARD_FRAG_TAG);

        if (f != null) {
            Log.w("SoundActivity", "Keyboard already initialized");
            return;
        }

        mMode = MODE.KEYBOARD;

        // Change instrument
        Mixer.processEvent(mMixer.generateProgramChangeEvent(0, 8));

        // Insert Piano fragment
        SoundPadFragment spf = (SoundPadFragment) fragmentManager.findFragmentByTag(SOUNDPAD_FRAG_TAG);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        KeyboardFragment keyboardFragment = new KeyboardFragment();

        // Determine if soundpad fragment exists
        if (spf != null) {
            fragmentTransaction.replace(R.id.sound_activity_main_area, keyboardFragment, KEYBOARD_FRAG_TAG);
        } else {
            fragmentTransaction.add(R.id.sound_activity_main_area, keyboardFragment, KEYBOARD_FRAG_TAG);
        }

        fragmentTransaction.commit();

    }

    private void initModeSoundpad() {
        mMode = MODE.SOUNDPAD;

        // Initialize instruments
        for (int i = 1; i < 10; i++) {
            Mixer.processEvent(mMixer.generateProgramChangeEvent(i, 111 + i));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment f = fragmentManager.findFragmentByTag(SOUNDPAD_FRAG_TAG);

        if (f != null) {
            Log.w("SoundActivity", "Soundpad already initialized");
            return;
        }

        // Insert Piano fragment
        KeyboardFragment kbf = (KeyboardFragment) fragmentManager.findFragmentByTag(KEYBOARD_FRAG_TAG);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SoundPadFragment soundPadFragment = new SoundPadFragment();

        // Determine if soundpad fragment exists
        if (kbf != null) {
            fragmentTransaction.replace(R.id.sound_activity_main_area, soundPadFragment, SOUNDPAD_FRAG_TAG);
        } else {
            fragmentTransaction.add(R.id.sound_activity_main_area, soundPadFragment, SOUNDPAD_FRAG_TAG);
        }

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        fragmentTransaction.commit();

//        Mixer.processEvent(mMixer.generateProgramChangeEvent(0, 8));
//        Mixer.processEvent(mMixer.generateProgramChangeEvent(0, 8));
//
//
//        MidiController.changeInstrument((byte) 0x70, (byte) 0x01);
//        MidiController.changeInstrument((byte) 0x71, (byte) 0x02);
//        MidiController.changeInstrument((byte) 0x78, (byte) 0x03);
//        MidiController.changeInstrument((byte) 0x77, (byte) 0x04);
//        MidiController.changeInstrument((byte) 0x72, (byte) 0x05);
//        MidiController.changeInstrument((byte) 0x73, (byte) 0x06);
//        MidiController.changeInstrument((byte) 0x74, (byte) 0x07);
//        MidiController.changeInstrument((byte) 0x75, (byte) 0x08);
//        MidiController.changeInstrument((byte) 0x76, (byte) 0x09);

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

        // TODO : REINITIALIZE INSTRUMENT SELECTIONS
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMixer.stop();
    }

    // TODO : IMPLEMENT THESE!!!

    @Override
    public boolean onModeChange(MODE mode) {
        Log.v(this.getLocalClassName(), "onModeChange to " + mode);
        return true;
    }

    @Override
    public boolean onStartRecording() {
        return false;
    }

    @Override
    public boolean onStopRecording() {
        return false;
    }

    @Override
    public boolean onPlayRecording() {
        return false;
    }

    @Override
    public boolean onClearRecording() {
        return false;
    }

    @Override
    public void onSetInstrument(int channel, int instrument) {

    }

    @Override
    public void onChangeOctave(int channel, int base) {

    }
}
