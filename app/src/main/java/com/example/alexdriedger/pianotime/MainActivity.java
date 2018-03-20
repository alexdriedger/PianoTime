package com.example.alexdriedger.pianotime;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;
import com.leff.midi.util.MidiProcessor;

import org.billthefarmer.mididriver.MidiDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mNavButton = findViewById(R.id.button_to_keyboard);
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(KeyboardActivity.class);
            }
        });

        mNavButton = findViewById(R.id.button_to_soundboard);
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(SoundPadActivity.class);
            }
        });

        mNavButton = findViewById(R.id.button_to_music_player);
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(MusicPlayerActivity.class);
            }
        });

//        // 1. Create some MidiTracks
//        MidiTrack tempoTrack = new MidiTrack();
//        MidiTrack noteTrack = new MidiTrack();
//        MidiTrack noteTrack2 = new MidiTrack();
//
//// 2. Add events to the tracks
//// Track 0 is the tempo map
//        TimeSignature ts = new TimeSignature();
//        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
//
//        Tempo tempo = new Tempo();
//        tempo.setBpm(228);
//
//        tempoTrack.insertEvent(ts);
//        tempoTrack.insertEvent(tempo);
//
//        MidiTrack instrumentTrack = new MidiTrack();
//
//        ProgramChange pc = new ProgramChange(0, 1, ProgramChange.MidiProgram.STRING_ENSEMBLE_1.programNumber());
//        noteTrack2.insertEvent(pc);
//
//// Track 1 will have some notes in it
//        for(int i = 0; i < 80; i++)
//        {
//            int channel = 0, pitch = 1 + i, velocity = 100;
//            NoteOn on = new NoteOn(i * 480, channel, pitch, velocity);
//            NoteOff off = new NoteOff(i * 480 + 120, channel, pitch, 0);
//
//            noteTrack.insertEvent(on);
//            noteTrack.insertEvent(off);
//
//            // There is also a utility function for notes that you should use
//            // instead of the above.
//            noteTrack2.insertNote(1, pitch + 2, velocity, i * 480, 600);
//        }
//
//// 3. Create a MidiFile with the tracks we created
//        List<MidiTrack> tracks = new ArrayList<MidiTrack>();
//        tracks.add(tempoTrack);
//        tracks.add(noteTrack);
//        tracks.add(noteTrack2);
//        tracks.add(instrumentTrack);
//
//        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
////        for (MidiTrack track : tracks) {
////            midi.addTrack(track);
////        }
//
////        MidiFile midi2 = new MidiFile();
////
////        NoteOn on = new NoteOn(480, 0, 20, 100);
////
////        MidiTrack noteTrack2 = new MidiTrack();
////
////        noteTrack2.insertEvent(on);
////
////        midi2.addTrack(noteTrack2);
//
//
//
//// 4. Write the MIDI data to a file
//        File output = new File(getApplicationContext().getFilesDir(), "exampleout.mid");
//        try
//        {
//            midi.writeToFile(output);
//        }
//        catch(IOException e)
//        {
//            System.err.println(e);
//            Log.d("midi", "Failed to write midi file");
//        }
//
////        // 2. Create a MidiProcessor
////        MidiProcessor processor = new MidiProcessor(midi2);
////
////        // 3. Register listeners for the events you're interested in
////        EventPrinter ep = new EventPrinter("Individual Listener");
////        processor.registerEventListener(ep, Tempo.class);
////        processor.registerEventListener(ep, NoteOn.class);
////
////        // or listen for all events:
//////        EventPrinter ep2 = new EventPrinter("Listener For All");
//////        processor.registerEventListener(ep2, MidiEvent.class);
////
////        // 4. Start the processor
////        processor.start();
//
//
//
//
//        MediaPlayer mp = MediaPlayer.create(this, Uri.fromFile(output));
//        mp.start();
////
//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
//            }
//        });

    }

    private void changeActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}
