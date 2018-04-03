package com.example.alexdriedger.pianotime;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;

import java.io.File;
import java.nio.file.Files;

public class SoundActivity extends FragmentActivity
        implements ControlBarFragment.OnControlInteractionListener,
        KeyboardFragment.OnNotePressListener {

    private static final String LOG_TAG = "Sound Activity";
    private static final String KEYBOARD_FRAG_TAG = "KEYBOARD_FRAG";
    private static final String SOUNDPAD_FRAG_TAG = "SOUNDPAD_FRAG";
    private static final String CONTROL_BAR_FRAG_TAG = "CONTROL_BAR_FRAG_TAG";
    private static final MODE DEFAULT_MODE = MODE.KEYBOARD;

    protected enum MODE {
        KEYBOARD, SOUNDPAD
    }

    private Mixer mMixer;
    private MODE mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        mMixer = new Mixer();
        mMixer.start();

        mMode = DEFAULT_MODE;

        initControlBar();
        changePlayerFragment(DEFAULT_MODE);

//        percussionTest();
    }

    private void percussionTest() {
        mMixer.startRecording();
        for (int i = 34; i < 82; i++) {
            int channel = 9;
//            int pitch = 1 + i;
            int velocity = 100;
            long tick = (i * 480) - (40 * 480);
//            long duration = 120;

            NoteOn on = new NoteOn(tick, channel, i, velocity);
            NoteOff off = new NoteOff(tick + 960, channel, i, 0);
            mMixer.processEvent(on);
            mMixer.processEvent(off);
        }

        mMixer.startPlaybackRecording(getApplicationContext());
    }

    private void initControlBar() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment f = fragmentManager.findFragmentByTag(CONTROL_BAR_FRAG_TAG);

        if (f != null) {
            Log.w(LOG_TAG, "ControlBar fragment already initialized");
            return;
        }

        ControlBarFragment cbf = ControlBarFragment.newInstance(mMode);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.control_bar_fragment, cbf, CONTROL_BAR_FRAG_TAG);
        fragmentTransaction.commit();

    }

    /**
     * Changes mode.
     *
     * TODO : FRAGMENT TRANSITIONS
     *
     * @param nextMode mode to change to.
     * @return true on success. False otherwise
     */
    private boolean changePlayerFragment(MODE nextMode) {
        String prevFragTag;
        String nextFragTag;
        Fragment prevFrag;
        Fragment nextFrag;

        FragmentManager fm = getSupportFragmentManager();

        // Determine which fragments to switch in and out
        switch (nextMode) {
            case KEYBOARD: {
                prevFragTag = SOUNDPAD_FRAG_TAG;
                nextFragTag = KEYBOARD_FRAG_TAG;

                nextFrag = new KeyboardFragment();
                break;
            }
            case SOUNDPAD: {
                prevFragTag = KEYBOARD_FRAG_TAG;
                nextFragTag = SOUNDPAD_FRAG_TAG;

                nextFrag = new SoundPadFragment();
                break;
            }
            default: throw new RuntimeException("SoundActivity: Unsupported mode: " + nextMode);
        }

        if (fm.findFragmentByTag(nextFragTag) != null) {
            Log.w(LOG_TAG, nextFragTag + " is trying to be insterted but already exists");
            return false;
        }

        prevFrag = fm.findFragmentByTag(prevFragTag);
        FragmentTransaction ft = fm.beginTransaction();

        if (prevFrag != null) {
            ft.replace(R.id.sound_activity_main_area, nextFrag, nextFragTag);
            Log.v(LOG_TAG, "Replaced " + prevFragTag + " with " + nextFragTag);
        } else {
            // There is no fragment present in the area
            Log.v(LOG_TAG, "Added " + nextFragTag);
            ft.add(R.id.sound_activity_main_area, nextFrag, nextFragTag);
        }

        ft.commit();

        mMode = nextMode;
        mMixer.initMode(nextMode);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMixer == null) {
            mMixer = new Mixer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMixer.start();
        mMixer.initMode(mMode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMixer.stop();
    }

    // TODO : IMPLEMENT THESE!!!

    @Override
    public boolean onModeChange(MODE nextMode) {
        Log.v(this.getLocalClassName(), "onModeChange to " + nextMode);
        return changePlayerFragment(nextMode);
    }

    @Override
    public boolean onStartRecording() {
        if (!mMixer.canRecord()) {
            Toast.makeText(getApplicationContext(), "Midi file is full. You should export it!", Toast.LENGTH_LONG);
        }
        mMixer.exportRecording(getApplicationContext());
        mMixer.startPlaybackRecording(getApplicationContext());
        mMixer.startRecording();
        return true;
    }

    @Override
    public boolean onStopRecording() {
        mMixer.stopRecording();
        mMixer.stopRecordingPlayback();
        return true;
    }

    @Override
    public boolean onPlayRecording() {
        mMixer.exportRecording(getApplicationContext());
        mMixer.startPlaybackRecording(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopPlayback() {
        mMixer.stopRecordingPlayback();
        return true;
    }

    @Override
    public boolean onDeleteTrack() {
        mMixer.deleteTrack();
        return true;
    }

    @Override
    public void onSetInstrument(MODE mode, int instrument, int pos) {
        // TODO : SOUNDPAD
        mMixer.setKeyboardInstrument(instrument);
    }

    @Override
    public void onChangeOctave(int base) {

    }

    @Override
    public boolean onExportRecording() {
        mMixer.exportRecording(getApplicationContext(), "mixtape_" + System.currentTimeMillis());
//        File[] files = getFilesDir().listFiles();
//        for (File f : files) {
//            Log.d(this.getClass().getName(), "File: " + f.getName());
//        }
        return true;
    }

    @Override
    public void onFinishRecording() {
        mMixer.flushRecording();
    }

    @Override
    public void onTouch(boolean noteOn, int pos) {
        mMixer.processEvent(noteOn, pos, mMode);
    }
}
