package com.example.alexdriedger.pianotime;

import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;

/**
 * Created by Alex Driedger on 2018-03-16.
 */

public class Mixer {

    private static MidiEncoder mMidiEncoder;
    private static MidiController mMidiController;
    private static boolean mIsRecording;
    private static long mRecordingStartTime;


    // TODO : SINGLETON????
    private Mixer() {
        mMidiEncoder = new MidiEncoder();
        mMidiController = MidiController.create();
        mIsRecording = false;
        mRecordingStartTime = System.currentTimeMillis(); // Default value, no not rely on this
    }

    public static Mixer create() {
        return new Mixer();
    }

    public void start() {
        mMidiController.start();
    }

    public void stop() {
        mMidiController.stop();
    }

    public void release() {
        mMidiController.release();
    }

    public static void startRecording() {
        mIsRecording = true;
        mRecordingStartTime = System.currentTimeMillis();
    }

    public static void stopRecording() {
        mIsRecording = false;
    }

    private static MidiEvent convertEvent(byte[] event) {
        byte type = event[0];
        MidiEvent mEvent;

        if (isType(type, MidiController.NOTE_ON)) {
            return new NoteOn(System.currentTimeMillis() - mRecordingStartTime,
                    (byte) (event[0] | 0x01), event[1], event[2]);
        } else if (isType(type, MidiController.NOTE_OFF)) {
            return new NoteOff(System.currentTimeMillis() - mRecordingStartTime,
                    (byte) (event[0] | 0x01), event[1], 0);
        } else if (isType(type, MidiController.CHANGE_INSTRUMENT)) {
            return new ProgramChange(System.currentTimeMillis() - mRecordingStartTime,
                    (byte) (event[0] | 0x01), event[1]);
        }

        return null;
    }

    public static void processEvent(byte[] event) {

        processEvent(convertEvent(event));

//        byte type = event[0];
//
//        if (isType(type, MidiController.NOTE_ON)) {
//            MidiController.playNote((byte) (event[0] | 0x01), event[1], event[2]);
//        } else if (isType(type, MidiController.NOTE_OFF)) {
//            MidiController.stopNote((byte) (event[0] | 0x01), event[1]);
//        } else if (isType(type, MidiController.CHANGE_INSTRUMENT)) {
//            MidiController.changeInstrument(event[1], (byte) (event[0] | 0x01);
//        }
//
//        if (mIsRecording) {
//            mMidiEncoder.addEvent()
//        }
    }

    public static void processEvent(MidiEvent event) {

        // Write event to the synthesizer
        if (event instanceof NoteOn) {
            MidiController.playNote((NoteOn) event);
        } else if (event instanceof NoteOff) {
            MidiController.stopNote((NoteOff) event);
        } else if (event instanceof ProgramChange) {
            MidiController.changeInstrument((ProgramChange) event);
        }

        if (mIsRecording) {
            mMidiEncoder.addEvent(event, mMidiEncoder.getCurrentTrack());
        }
    }

    /**
     * Returns true if the testByte is within 0x10 of the compareTo byte
     * @param testByte byte to test
     * @param compareTo byte to test against
     * @return true if testByte is within 0x10 of the compareTo byte
     */
    private static boolean isType(byte testByte, byte compareTo) {
        return ((testByte | 0x10) == compareTo);
//        return (Byte.compare(testByte, compareTo) >= 0) &&
//                (Byte.compare(testByte, (byte) (compareTo + (byte) 0x10)) <= 0);
    }
}
