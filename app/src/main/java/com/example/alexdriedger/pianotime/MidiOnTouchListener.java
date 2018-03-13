package com.example.alexdriedger.pianotime;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alex Driedger on 2018-03-13.
 */

public class MidiOnTouchListener implements View.OnTouchListener {

    // TODO : MAKE METHODS TO CHANGE THESE VALUES
    // TODO : MAKE A KEYBOARDKEY CLASS THAT CREATES AND HOLDS ONTO THE LISTENER SO THAT VALUES
    //        CAN BE CHANGED AFTER ASSIGNING THE LISTENER

    private byte mChan;
    private byte mNote;
    private byte mVel;

    MidiOnTouchListener(byte chan, byte note, byte vel) {
        mChan = chan;
        mNote = note;
        mVel = vel;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            MidiController.playNote(mChan, mNote, mVel);
        }
        else if (e.getAction() == MotionEvent.ACTION_UP) {
            MidiController.stopNote(mChan, mNote);
        }
        return true;
    }
}
