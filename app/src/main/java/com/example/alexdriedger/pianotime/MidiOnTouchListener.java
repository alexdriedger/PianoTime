package com.example.alexdriedger.pianotime;

import android.view.MotionEvent;
import android.view.View;

/**
 * OnTouchListener for playing midi notes
 */

public class MidiOnTouchListener implements View.OnTouchListener {

    // TODO : MAKE METHODS TO CHANGE THESE VALUES
    // TODO : MAKE A KEYBOARDKEY CLASS THAT CREATES AND HOLDS ONTO THE LISTENER SO THAT VALUES
    //        CAN BE CHANGED AFTER ASSIGNING THE LISTENER

    private byte mChan;
    private byte mNote;
    private byte mVel;

    private byte[] play;
    private byte[] stop;

    MidiOnTouchListener(byte chan, byte note, byte vel) {
        mChan = chan;
        mNote = note;
        mVel = vel;

        play = new byte[3];
        stop = new byte[3];

        play[0] = (byte) (0x90 | chan);
        play[1] = note;
        play[2] = vel;

        stop[0] = (byte) (0x80 | chan);
        stop[1] = note;
        stop[2] = 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
//            MidiController.playNote(mChan, mNote, mVel);
            Mixer.processEvent(play);
        }
        else if (e.getAction() == MotionEvent.ACTION_UP) {
//            MidiController.stopNote(mChan, mNote);
            Mixer.processEvent(stop);
        }
        return true;
    }
}
