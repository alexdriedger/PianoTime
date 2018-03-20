package com.example.alexdriedger.pianotime;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.alexdriedger.pianotime.MidiController;

import org.billthefarmer.mididriver.MidiDriver;

public class KeyboardFragment extends Fragment {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_keyboard);
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_keyboard, container, false);
        keyInit(v);
        return v;
    }

    /**
     * Initializes keys for the touch screen piano
     */
    private void keyInit(View v) {
        final Button mButtonC = v.findViewById(R.id.button_c);
        mButtonC.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x3C, (byte) 0x7F));
        final Button mButtonCSharp = v.findViewById(R.id.button_c_sharp);
        mButtonCSharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x3D, (byte) 0x7F));
        final Button mButtonD = v.findViewById(R.id.button_d);
        mButtonD.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x3E, (byte) 0x7F));
        final Button mButtonDSharp = v.findViewById(R.id.button_d_sharp);
        mButtonDSharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x3F, (byte) 0x7F));
        final Button mButtonE = v.findViewById(R.id.button_e);
        mButtonE.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x40, (byte) 0x7F));
        final Button mButtonF = v.findViewById(R.id.button_f);
        mButtonF.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x41, (byte) 0x7F));
        final Button mButtonFSharp = v.findViewById(R.id.button_f_sharp);
        mButtonFSharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x42, (byte) 0x7F));
        final Button mButtonG = v.findViewById(R.id.button_g);
        mButtonG.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x43, (byte) 0x7F));
        final Button mButtonGSharp = v.findViewById(R.id.button_g_sharp);
        mButtonGSharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x44, (byte) 0x7F));
        final Button mButtonA = v.findViewById(R.id.button_a);
        mButtonA.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x45, (byte) 0x7F));
        final Button mButtonASharp = v.findViewById(R.id.button_a_sharp);
        mButtonASharp.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x46, (byte) 0x7F));
        final Button mButtonB = v.findViewById(R.id.button_b);
        mButtonB.setOnTouchListener(new MidiOnTouchListener((byte) 0x00, (byte) 0x47, (byte) 0x7F));
    }

}
