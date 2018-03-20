package com.example.alexdriedger.pianotime;

import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;

import org.billthefarmer.mididriver.MidiDriver;

/**
 * Controls the Midi driver for the app
 */

public class MidiController {

    public static final byte NOTE_ON = (byte) 0x90;
    public static final byte NOTE_OFF = (byte) 0x80;
    public static final byte CHANGE_INSTRUMENT = (byte) 0xC0;
    public static final byte MAX_VEL = (byte) 0x7F;
    public static final byte MIN_VEL = (byte) 0x00;

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
        event[0] = (byte) (NOTE_ON | chan);
        event[1] = note;
        event[2] = vel;

        // TODO : REDUNDANT START SHOULD BE INSERTED?
        mMidiDriver.write(event);

    }

    public static void playNote(NoteOn n) {
        playNote((byte) n.getChannel(), (byte) n.getNoteValue(), (byte) n.getVelocity());
    }

    public static void stopNote(byte chan, byte note) {

        byte[] event = new byte[3];
        event[0] = (byte) (NOTE_OFF | chan);
        event[1] = note;
        event[2] = MIN_VEL;

        mMidiDriver.write(event);
    }

    public static void stopNote(NoteOff n) {
        stopNote((byte) n.getChannel(), (byte) n.getNoteValue());
    }

    public static void changeInstrument(byte instrument, byte channel) {
        byte[] event = new byte[2];
        event[0] = (byte) (CHANGE_INSTRUMENT | channel);
        event[1] = instrument;

        mMidiDriver.write(event);
    }

    public static void changeInstrument(ProgramChange pc) {
        changeInstrument((byte) pc.getProgramNumber(), (byte) pc.getChannel());
    }

}
