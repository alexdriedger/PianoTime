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


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mNavButton = findViewById(R.id.button_to_keyboard);
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(SoundActivity.class);
            }
        });

        mNavButton = findViewById(R.id.button_to_database);
        mNavButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                changeActivity(CloudStorageActivity.class);
            }
        });

        mNavButton = findViewById(R.id.button_to_bluetooth);
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(BluetoothActivity.class);
            }
        });

    }

    private void changeActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }



}
