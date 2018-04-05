package com.example.alexdriedger.pianotime;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.leff.midi.event.MidiEvent;

import java.io.File;
import java.util.LinkedList;

public class BluetoothActivity extends AppCompatActivity implements MyBTService.BluetoothEventListener {

    private MidiEncoder mMidiEncoder;
    MyBTService bts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        mMidiEncoder = new MidiEncoder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bts != null) {
            bts = new MyBTService(getApplicationContext());
            bts.setMyBTServiceListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        bts.disconnect();
        bts = null;
    }

    @Override
    public void onBluetoothDataReady(LinkedList<MidiEvent> midiEvents) {
        Log.d("Bluetooth", "Midi events received");
        for (MidiEvent e : midiEvents) {
            mMidiEncoder.addEvent(e, 0);
        }

        File f =  new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC) + File.separator + "de1_mixtape_" + System.currentTimeMillis() + ".mid");
        mMidiEncoder.exportToFile(f);

        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Uri.fromFile(f));
        mp.start();
    }
}
