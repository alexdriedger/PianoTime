package com.example.alexdriedger.pianotime;

import android.content.Intent;
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

        Intent intent = new Intent(this,  KeyboardActivity.class);
        startActivity(intent);

//        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.moana_midi);
//        mediaPlayer.start();

    }

    private void changeInstrument(byte instrument, byte channel) {
        event = new byte[2];
        event[0] = (byte) (0xC0 | channel);  // 0xC0 = change instrument, 0x00 = channel 1
        event[1] = instrument;  // 0x3C = middle C // byte
//        event[2] = (byte) 0x7F;  // 0x7F = the maximum velocity (127)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);
    }

}
