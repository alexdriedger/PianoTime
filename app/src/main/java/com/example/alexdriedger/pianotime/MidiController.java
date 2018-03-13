package com.example.alexdriedger.pianotime;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.billthefarmer.mididriver.MidiDriver;

/**
 * Created by Alex Driedger on 2018-03-10.
 */

public class MidiController {

    private static MidiDriver mMidiDriver;
    private static boolean isStarted;

    private MidiController() {
        if (mMidiDriver == null) {
            mMidiDriver = new MidiDriver();
        }

        isStarted = false;
    }

    public static MidiController create() {
        return new MidiController();
    }

    public void release() {
        if (mMidiDriver != null && isStarted) {
            mMidiDriver.stop();
        }
        mMidiDriver = null;
        isStarted = false;
    }

    public void start() {
        if (mMidiDriver == null) {
            new MidiController();
        }

        if (!isStarted) {
            mMidiDriver.start();
        }

        isStarted = true;
    }

    public void stop() {
        if (mMidiDriver != null && isStarted) {
            mMidiDriver.stop();
        }

        isStarted = false;

    }

    public static void playNote(byte chan, byte note, byte vel) {

        // Construct a note ON message for the middle C at maximum velocity on channel 1:
        byte [] event = new byte[3];
        event[0] = (byte) (0x90 | chan);  // 0x90 = note On, 0x00 = channel 1
        event[1] = note;  // 0x3C = middle C
        event[2] = vel;  // 0x7F = the maximum velocity (127)

        // TODO : REDUNDANT START SHOULD BE INSERTED?

        // Send the MIDI event to the synthesizer.
        mMidiDriver.write(event);

    }

    public static void stopNote(byte chan, byte note) {

        // Construct a note OFF message for the middle C at minimum velocity on channel 1:
        byte[] event = new byte[3];
        event[0] = (byte) (0x80 | chan);  // 0x80 = note Off, 0x00 = channel 1
        event[1] = note;  // 0x3C = middle C
        event[2] = (byte) 0x00;  // 0x00 = the minimum velocity (0)

        // Send the MIDI event to the synthesizer.
        mMidiDriver.write(event);
    }

    public static void changeInstrument(byte instrument, byte channel) {
        byte[] event = new byte[2];
        event[0] = (byte) (0xC0 | channel);  // 0xC0 = change instrument, 0x00 = channel 1
        event[1] = instrument;  // 0x3C = middle C // byte

        // Send the MIDI event to the synthesizer.
        mMidiDriver.write(event);
    }
}
