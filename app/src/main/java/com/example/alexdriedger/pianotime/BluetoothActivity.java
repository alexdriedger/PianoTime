package com.example.alexdriedger.pianotime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.leff.midi.event.MidiEvent;

import java.io.File;
import java.util.LinkedList;

public class BluetoothActivity extends FragmentActivity
        implements MyBTService.BluetoothEventListener, AIDialogFragment.AIDialogListener {

    private MidiEncoder mMidiEncoder;
    MyBTService bts;
    private String fileName; // TODO : CHANGE THIS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        mMidiEncoder = new MidiEncoder();
        bts = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (bts != null) {
            bts = new MyBTService(getApplicationContext());
            bts.setMyBTServiceListener(this);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (bts != null) {
            bts.disconnect();
            bts = null;
//        }
    }

    @Override
    public void onBluetoothDataReady(LinkedList<MidiEvent> midiEvents) {
        Log.d("Bluetooth", "Midi events received");
        for (MidiEvent e : midiEvents) {
            mMidiEncoder.addEvent(e, 0);
        }

        File f =  new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC) + File.separator + "de1_mixtape_" + System.currentTimeMillis() + ".mid");
        mMidiEncoder.exportToFile(f);
        fileName = f.getName();

        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Uri.fromFile(f));
        mp.start();

        startService(new Intent(this, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, Uri.fromFile(f))
                .putExtra(MyUploadService.EXTRA_UPLOAD_FOLDER, "DE1/" + f.getName())
                .setAction(MyUploadService.ACTION_UPLOAD));

        showAIDialog();

    }

    private void showAIDialog() {
        DialogFragment newFragment = new AIDialogFragment();
        newFragment.show(getFragmentManager(), "ai");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.d("Bluetooth", "Dialog callback success success");
        TextView tv = findViewById(R.id.bt_text);
        tv.setText("You have AI generated music!");
        SocketThread tem = new SocketThread(fileName);
        tem.start();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d("Bluetooth", "Dialog callback negative success");
        TextView tv = findViewById(R.id.bt_text);
        tv.setText("Upload complete");
    }
}
