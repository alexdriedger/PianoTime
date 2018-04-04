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

    private static final int PERCUSSION_CHANNEL = 9;
    private static final int DEFAULT_VELOCITY = 127;
    private static final String DEFAULT_MIDI_FILE_NAME = "temp_midi_recording";

    private MidiEncoder mMidiEncoder;
    private MidiController mMidiController;
    private boolean mIsRecording;
    private long mRecordingStartTime;
    private MediaPlayer mMediaPlayer;
    private File mLastMidiExport;

    private int mBaseKeyboardPos;
    private int[] mSoundPadPos;
    private int mKeyboardInstrument;

    public Mixer() {
        mMidiEncoder = new MidiEncoder();
        mMidiController = MidiController.create();
        mIsRecording = false;
        mRecordingStartTime = System.currentTimeMillis(); // Default value, no not rely on this
        mMediaPlayer = null;
        mLastMidiExport = null;

        mBaseKeyboardPos = 60; // Middle C
        mKeyboardInstrument = 41;
        mSoundPadPos = new int[] {55, 59, 60, 66, 67, 38, 39, 34, 35};

    }

    public void initMode(SoundActivity.MODE mode) {
        switch (mode) {
            case KEYBOARD: {
                processEvent(generateProgramChangeEvent(0, mKeyboardInstrument));
                break;
            }
            case SOUNDPAD: break;
        }
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

    public int getCurrentTrack() {
        return mMidiEncoder.getCurrentTrack();
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    public void startRecording() {
        mIsRecording = true;
        mRecordingStartTime = System.currentTimeMillis();
        newTrack();
    }

    public void stopRecording() {
        mIsRecording = false;
    }

    private MidiEvent convertEvent(byte[] event) {
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

    /**
     * Return true if there are enough channels left to record
     * @return Return true if there are enough channels left to record. False otherwise
     */
    public boolean canRecord() {
        return mMidiEncoder.getNextAvailableChannel() != -1;
    }

    /**
     * Sets the channel for the current selected instrument
     * @param mode on of MODE
     * @return the channel that was set
     */
    private int setChannel(SoundActivity.MODE mode) {
        if (mode == SoundActivity.MODE.SOUNDPAD) {
            return PERCUSSION_CHANNEL;
        }
        if (!mIsRecording) {
            return 0;
        } else {
            int channel = mMidiEncoder.getChannel(mKeyboardInstrument);
            if (channel < 0) {
                channel = mMidiEncoder.getNextAvailableChannel();
                mMidiEncoder.setNextAvailableChannel(mKeyboardInstrument);
                processEvent(generateProgramChangeEvent(channel, mKeyboardInstrument));
            }
            return channel;
        }
    }

    /**
     * Gets the channel for the current selected instrument.
     * @param mode one of MODE
     * @return channel for the current selected instrument.
     */
    private int getChannel(SoundActivity.MODE mode) {
        if (mode == SoundActivity.MODE.SOUNDPAD) {
            return PERCUSSION_CHANNEL;
        }
        if (!mIsRecording) {
            return 0;
        } else {
            int channel = mMidiEncoder.getChannel(mKeyboardInstrument);
            if (channel < 0) {
                channel = mMidiEncoder.getNextAvailableChannel();
            }
            return channel;
        }
    }

    private int getActualPosition(int rawPos, SoundActivity.MODE mode) {
        switch(mode) {
            case KEYBOARD: return rawPos + mBaseKeyboardPos;
            case SOUNDPAD: return mSoundPadPos[rawPos];
            default: throw new RuntimeException(this.getClass().getName() + " : Invalid mode : " + mode);
        }
    }

    public void processEvent(byte[] event) {
        processEvent(convertEvent(event));
    }

    public void processEvent(MidiEvent event) {

        // Write event to the synthesizer
        if (event instanceof NoteOn) {
            MidiController.playNote((NoteOn) event);
        } else if (event instanceof NoteOff) {
            MidiController.stopNote((NoteOff) event);
        } else if (event instanceof ProgramChange) {
            MidiController.changeInstrument((ProgramChange) event);
            mKeyboardInstrument = ((ProgramChange) event).getProgramNumber();
            return; // Getting channel should take of encoding program changes
        }

        if (mIsRecording) {
            mMidiEncoder.addEvent(event, mMidiEncoder.getCurrentTrack());
        }
    }

    /**
     * Used to process note on and note off messages
     * @param noteOn true if note on. false if note off
     * @param pos raw position of key press
     * @param mode one of SoundActivity.MODE
     */
    public void processEvent(boolean noteOn, int pos, SoundActivity.MODE mode) {
        MidiEvent event;
        long tick = getCurrentMidiTime();
        int channel = setChannel(mode); // Handles updating channels if recording
        int note = getActualPosition(pos, mode);
        int velocity = DEFAULT_VELOCITY;

        if (noteOn) {
            processEvent(new NoteOn(tick, channel, note, velocity));
        } else {
            processEvent(new NoteOff(tick, channel, note, 0));
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

    private long getCurrentMidiTime() {
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

    /**
     * Same as exportRecording but with a default file name.
     * @param c Context
     * @return True if sucessful. False otherwise
     */
    public boolean exportRecording(Context c) {
        return exportRecording(c, DEFAULT_MIDI_FILE_NAME);
    }

    /**
     * Exports a recording
     * @param c Context
     * @param fileName of file
     * @return True is successful. False otherwise
     */
    public boolean exportRecording(Context c, String fileName) {
        File f =  new File(c.getFilesDir() + File.separator + fileName);
        if (mMidiEncoder.exportToFile(f)) {
            // Export was successful
            mLastMidiExport = f;
            return true;
        }
        Log.d(this.getClass().getName(), "Could not export midi file");
        return false;
    }

    /**
     * Plays back what has been recorded in the midi file
     * @param c Context
     */
    public void startPlaybackRecording(Context c) {
        if (mLastMidiExport == null) {
            Log.d(this.getClass().getName(), "Could not start playback. No last midi export");
            return;
        }

        mMediaPlayer = MediaPlayer.create(c, Uri.fromFile(mLastMidiExport));
        mMediaPlayer.start();
    }

    /**
     * Stops playback
     */
    public void stopRecordingPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void newTrack() {
        mMidiEncoder.addTrack();
    }

    /**
     * Removes anything from previous recording
     */
    public void flushRecording() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mLastMidiExport = null;
        mIsRecording = false;
        mMidiEncoder = new MidiEncoder();
    }

    /**
     * Deletes most recent track
     */
    public void deleteTrack() {
        mMidiEncoder.removeTrack(mMidiEncoder.getCurrentTrack());
    }

    public void setKeyboardInstrument(int i) {
        // TODO : CHECK FOR OUT OF BOUNDS
        mKeyboardInstrument = i;
        processEvent(generateProgramChangeEvent(setChannel(SoundActivity.MODE.KEYBOARD), mKeyboardInstrument));
    }
}
