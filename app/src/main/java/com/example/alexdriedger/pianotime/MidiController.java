package com.example.alexdriedger.pianotime;

import org.billthefarmer.mididriver.MidiDriver;

/**
 * Controls the Midi driver for the app
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

    /**
     * Factory method for creating an instance on the MidiController.
     * @return MidiController instance
     */
    public static MidiController create() {
        return new MidiController();
    }

    /**
     * Release the resources of the MidiController
     */
    public void release() {
        if (mMidiDriver != null && isStarted) {
            mMidiDriver.stop();
        }
        mMidiDriver = null;
        isStarted = false;
    }

    /**
     * Starts the MidiController. Must be called before any commands will register with
     * the MidiController
     */
    public void start() {
        if (mMidiDriver == null) {
            new MidiController();
        }

        if (!isStarted) {
            mMidiDriver.start();
        }

        isStarted = true;
    }

    /**
     * Stops the MidiController
     */
    public void stop() {
        if (mMidiDriver != null && isStarted) {
            mMidiDriver.stop();
        }

        isStarted = false;

        // TODO : FREE RESOURCES?
    }

    /**
     * Plays a note. start() must be called before playNote
     * @param chan channel the note is played on. Must be 0-16 inclusive
     * @param note that is played. Must be 0-127 inclusive
     * @param vel velocity. This correlates to the volume
     */
    public static void playNote(byte chan, byte note, byte vel) {

        byte [] event = new byte[3];
        event[0] = (byte) (0x90 | chan);  // 0x90 = note On
        event[1] = note;
        event[2] = vel;

        // TODO : REDUNDANT START SHOULD BE INSERTED?
        mMidiDriver.write(event);

    }

    public static void stopNote(byte chan, byte note) {

        byte[] event = new byte[3];
        event[0] = (byte) (0x80 | chan);  // 0x80 = note Off
        event[1] = note;
        event[2] = (byte) 0x00;  // 0x00 = the minimum velocity (0)

        mMidiDriver.write(event);
    }

    public static void changeInstrument(byte instrument, byte channel) {
        byte[] event = new byte[2];
        event[0] = (byte) (0xC0 | channel);  // 0xC0 = change instrument
        event[1] = instrument;

        mMidiDriver.write(event);
    }
}
