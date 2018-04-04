package com.example.alexdriedger.pianotime;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.example.alexdriedger.pianotime.MidiController;

import org.billthefarmer.mididriver.MidiDriver;

public class KeyboardFragment extends AbstractMusicInteractionFragment {

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
        final View mButtonC = v.findViewById(R.id.button_c);
        mButtonC.setOnTouchListener(new MusicInteractionOnTouchListener(0));
        final View mButtonCSharp = v.findViewById(R.id.button_c_sharp);
        mButtonCSharp.setOnTouchListener(new MusicInteractionOnTouchListener(1));
        final View mButtonD = v.findViewById(R.id.button_d);
        mButtonD.setOnTouchListener(new MusicInteractionOnTouchListener(2));
        final View mButtonDSharp = v.findViewById(R.id.button_d_sharp);
        mButtonDSharp.setOnTouchListener(new MusicInteractionOnTouchListener(3));
        final View mButtonE = v.findViewById(R.id.button_e);
        mButtonE.setOnTouchListener(new MusicInteractionOnTouchListener(4));
        final View mButtonF = v.findViewById(R.id.button_f);
        mButtonF.setOnTouchListener(new MusicInteractionOnTouchListener(5));
        final View mButtonFSharp = v.findViewById(R.id.button_f_sharp);
        mButtonFSharp.setOnTouchListener(new MusicInteractionOnTouchListener(6));
        final View mButtonG = v.findViewById(R.id.button_g);
        mButtonG.setOnTouchListener(new MusicInteractionOnTouchListener(7));
        final View mButtonGSharp = v.findViewById(R.id.button_g_sharp);
        mButtonGSharp.setOnTouchListener(new MusicInteractionOnTouchListener(8));
        final View mButtonA = v.findViewById(R.id.button_a);
        mButtonA.setOnTouchListener(new MusicInteractionOnTouchListener(9));
        final View mButtonASharp = v.findViewById(R.id.button_a_sharp);
        mButtonASharp.setOnTouchListener(new MusicInteractionOnTouchListener(10));
        final View mButtonB = v.findViewById(R.id.button_b);
        mButtonB.setOnTouchListener(new MusicInteractionOnTouchListener(11));

    }

}
