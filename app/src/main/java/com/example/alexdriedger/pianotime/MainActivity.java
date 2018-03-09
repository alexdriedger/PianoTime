package com.example.alexdriedger.pianotime;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.billthefarmer.mididriver.MidiDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private MidiDriver midiDriver;
    private byte[] event;
    private int[] config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button playButton = findViewById(R.id.play_button);
        playButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    playNote((byte) 0x01);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopNote((byte) 0x01);
                }
                return true;
            }
        });

        final Button playButton2 = findViewById(R.id.play_button_2);
        playButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    playNote((byte) 0x00);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopNote((byte) 0x00);
                }
                return true;
            }
        });

//        final Button pauseButton = findViewById(R.id.pause_button);
//        pauseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopNote();
//            }
//        });

        // Instantiate the driver.
        midiDriver = new MidiDriver();

        midiDriver.start();

        // Get the configuration.
        config = midiDriver.config();

        // Print out the details.
        Log.d(this.getClass().getName(), "maxVoices: " + config[0]);
        Log.d(this.getClass().getName(), "numChannels: " + config[1]);
        Log.d(this.getClass().getName(), "sampleRate: " + config[2]);
        Log.d(this.getClass().getName(), "mixBufferSize: " + config[3]);

        changeInstrument((byte) 0x5B, (byte) 0x01);

//        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.moana_midi);
//        mediaPlayer.start();

    }

    // private void playNote(byte chan, byte note, byte vel)
    private void playNote(byte channel) {

        // Construct a note ON message for the middle C at maximum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (0x90 | channel);  // 0x90 = note On, 0x00 = channel 1
        event[1] = (byte) 0x3C;  // 0x3C = middle C
        event[2] = (byte) 0x7F;  // 0x7F = the maximum velocity (127)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

        Log.d("Main", "playNote");

    }

    private void changeInstrument(byte instrument, byte channel) {
        event = new byte[2];
        event[0] = (byte) (0xC0 | channel);  // 0xC0 = change instrument, 0x00 = channel 1
        event[1] = instrument;  // 0x3C = middle C // byte
//        event[2] = (byte) 0x7F;  // 0x7F = the maximum velocity (127)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);
    }

    // private void stopNote(byte chan, byte note)
    private void stopNote(byte channel) {

        // Construct a note OFF message for the middle C at minimum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (0x80 | channel);  // 0x80 = note Off, 0x00 = channel 1
        event[1] = (byte) 0x3C;  // 0x3C = middle C
        event[2] = (byte) 0x00;  // 0x00 = the minimum velocity (0)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

        Log.d("Main", "pauseNote");

    }
}
