package com.example.alexdriedger.pianotime;

import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex Driedger on 2018-03-16.
 */

public class MidiEncoder {

    private static final int TEMPO_ONE_MILLI = 125;

    private List<MidiTrack> mTracks;
    private Map<Integer, Integer> channels;

    public MidiEncoder() {
        this(getDefaultTimeSignature(), getDefaultTempo());
    }

    public MidiEncoder(TimeSignature ts, Tempo tempo) {
        mTracks = new ArrayList<>();

        MidiTrack mt = new MidiTrack();
        mt.insertEvent(ts);
        mt.insertEvent(tempo);

        mTracks.add(mt);

//        channels = new int[16];
//        Arrays.fill(channels, -1);

        channels = new HashMap<>();
    }

    /**
     *
     * @param event to add to the encoder
     * @param trackNum The track number to add the event to.
     *                 Must be 0 <= trackNum < getNumTracks
     * @return true if added successfully. False if there was an error
     */
    public boolean addEvent(MidiEvent event, int trackNum) {
        if (trackNum < 0 || mTracks.size() <= trackNum) {
            Log.d(this.getClass().getName(), "Could not add event");
            return false;
        }

        mTracks.get(trackNum).insertEvent(event);
        return true;
    }

    /**
     * Removes track if it exists
     * @param track number to remove
     */
    public void removeTrack(int track) {
        if (0 <= track && track < mTracks.size()) {
            mTracks.remove(track);
        }
    }

    /**
     * Adds a new blank track
     */
    public void addTrack() {
        mTracks.add(new MidiTrack());
    }

    /**
     * Writes all of the encoded MIDI data to a file.
     * @param file to write to. File will be overwritten
     * @return true if successful. False otherwise
     */
    public boolean exportToFile(File file) {
        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, mTracks);

        try {
            midi.writeToFile(file);
        }
        catch(IOException e) {
            System.err.println(e);
            Log.e("MidiEncoder", "Could not write to file");
            return false;
        }

        return true;
    }

    /**
     * Returns the channel an instrument is on. Returns -1 if instrument is not on a channel
     * @param instrument to check
     * @return Returns the channel an instrument is on. Returns -1 if instrument is not on a channel
     */
    public int getChannel(int instrument) {
        Integer channel = channels.get(instrument);
        if (channel == null) {
            return -1;
        }
        return channel;
    }

    /**
     * Returns the next available channel
     * @return Returns the next available channel. Returns -1 if all channels are full
     */
    public int getNextAvailableChannel() {
        int size = channels.size();

        // Percussion channel is not available for other instruments
        if (size < 9) {
            return size;
        }
        if (size >= 15) {
            return -1;
        }
        return size + 1;
    }

    /**
     * Sets a channel to certain instrument. Fails if there are no available channels
     * @param instrument to set on the channel
     */
    public void setNextAvailableChannel(int instrument) {
        int channel = getNextAvailableChannel();
        if (channel == -1) {
            return;
        }
        channels.put(instrument, channel);
        addEvent(new ProgramChange(0, 0, instrument), 0);
    }

    /**
     * @return the number of tracks initialized in the encoder
     */
    public int getNumTracks() {
        return mTracks.size();
    }

    public int getCurrentTrack() {
        return Math.max(mTracks.size() - 1, 0);
    }

    private static TimeSignature getDefaultTimeSignature() {
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
        return ts;
    }

    private static Tempo getDefaultTempo() {
        Tempo tempo = new Tempo();
        tempo.setBpm(TEMPO_ONE_MILLI);
        return tempo;
    }
}
