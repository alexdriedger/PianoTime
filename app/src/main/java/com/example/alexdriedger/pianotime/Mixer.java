package com.example.alexdriedger.pianotime;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;

import java.io.File;

/**
 * Created by Alex Driedger on 2018-03-16.
 */

public class Mixer {

    private static MidiEncoder mMidiEncoder;
    private static MidiController mMidiController;
    private static boolean mIsRecording;
    private static long mRecordingStartTime;
    private static MediaPlayer mMediaPlayer;


    // TODO : SINGLETON???? NO. THE MIDICONTROLLER SHOULD BE THE ONLY SINGLETON OBJECT IN HERE
    private Mixer() {
        mMidiEncoder = new MidiEncoder();
        mMidiController = MidiController.create();
        mIsRecording = false;
        mRecordingStartTime = System.currentTimeMillis(); // Default value, no not rely on this
        mMediaPlayer = null;

    }

    public static Mixer create() {
        return new Mixer();
    }

    public void start() {
        mMidiController.start();

//        // Change instruments
//        // TODO : FIGURE OUT WHY THESE DON'T WORK
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

    public void stop() {
        mMidiController.stop();
    }

    public void release() {
        mMidiController.release();
    }

    public void startRecording() {
        mIsRecording = true;
        mRecordingStartTime = System.currentTimeMillis();
    }

    public void stopRecording() {
        mIsRecording = false;
    }

    private static MidiEvent convertEvent(byte[] event) {
        byte type = event[0];
        MidiEvent mEvent;

        if (isType(type, MidiController.NOTE_ON)) {
            return new NoteOn(System.currentTimeMillis() - mRecordingStartTime,
                    (byte) (event[0] & 0x0F), event[1], event[2]);
        } else if (isType(type, MidiController.NOTE_OFF)) {
            return new NoteOff(System.currentTimeMillis() - mRecordingStartTime,
                    (byte) (event[0] & 0x0F), event[1], 0);
        } else if (isType(type, MidiController.CHANGE_INSTRUMENT)) {
            return new ProgramChange(System.currentTimeMillis() - mRecordingStartTime,
                    (byte) (event[0] & 0x0F), event[1]);
        }

        Log.e("Mixer", "Did not convert event correctly");

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

        if (mIsRecording || event instanceof ProgramChange) {
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
//        byte test = ((byte) (testByte | (byte) 0x10));
//        boolean result = test == compareTo;
//        return result;

        return ((byte)(testByte & 0xF0)) == compareTo;

//        return (Byte.compare(testByte, compareTo) >= 0) &&
//                (Byte.compare(testByte, (byte) (compareTo + (byte) 0x10)) <= 0);
    }

    private static long getCurrentMidiTime() {
        return System.currentTimeMillis() - mRecordingStartTime;
    }

    /**
     * Creates a ProgramChange event with the given parameters
     * @param channel to change the instrument on. Must be 0 <= channel <= 15
     * @param instruNum Number of the instrument. Must be 0 <= instruNum <= 127
     * @return a ProgramChange event
     */
    public ProgramChange generateProgramChangeEvent(int channel, int instruNum) {
        if (channel < 0 || 15 < channel) {
            throw new IllegalArgumentException("Channel out of bounds");
        }

        if (instruNum < 0 || 127 < instruNum) {
            throw new IllegalArgumentException("Instrument number out of bounds");
        }

        return new ProgramChange(getCurrentMidiTime(), channel, instruNum);
    }

    // TODO : CHANGE THIS
    public void playRecording(Context c) {
        File f = new File(c.getFilesDir() + File.separator + "testfile.mid");

        mMidiEncoder.exportToFile(f);

        mMediaPlayer = MediaPlayer.create(c, Uri.fromFile(f));
        mMediaPlayer.start();
    }

    public void newTrack() {
        mMidiEncoder.addTrack();
    }
}
