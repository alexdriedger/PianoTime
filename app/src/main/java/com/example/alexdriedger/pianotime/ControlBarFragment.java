package com.example.alexdriedger.pianotime;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ControlBarFragment.OnControlInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ControlBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlBarFragment extends Fragment {

    private static final String ARG_MODE = "MODE";
    private static final String ARG_IS_RECORDING = "IS_RECORDING";


    private SoundActivity.MODE mMode;
    private boolean isRecording;

    private OnControlInteractionListener mListener;

    public ControlBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode the fragment is in. Should be one of KEYBOARD or SOUNDPAD
     * @param isRecording whether the fragment should be recording
     * @return A new instance of fragment ControlBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlBarFragment newInstance(SoundActivity.MODE mode, boolean isRecording) {
        ControlBarFragment fragment = new ControlBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MODE, mode.toString());
        args.putBoolean(ARG_IS_RECORDING, isRecording);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Set Mode
            String mode = getArguments().getString(ARG_MODE);
            if (SoundActivity.MODE.KEYBOARD.toString().equals(mode)) {
                mMode = SoundActivity.MODE.KEYBOARD;
            } else if (SoundActivity.MODE.SOUNDPAD.toString().equals(mode)){
                mMode = SoundActivity.MODE.SOUNDPAD;
            }

            isRecording = getArguments().getBoolean(ARG_IS_RECORDING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control_bar, container, false);
        initButtonListeners(v);
        return v;
    }

    private void initButtonListeners(View v) {
        // TODO : MAKE BUTTON TEXT STRINGS INTO STRING RESOURCES
        final Button modeButton = v.findViewById(R.id.change_mode_button);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("ControlBarFragment", "Mode button clicked");
                SoundActivity.MODE nextMode;
                if (mMode == SoundActivity.MODE.KEYBOARD) {
                    nextMode = SoundActivity.MODE.SOUNDPAD;
                } else if (mMode == SoundActivity.MODE.SOUNDPAD) {
                    nextMode = SoundActivity.MODE.KEYBOARD;
                } else {
                    throw new RuntimeException("Unsupported mode: " + mMode);
                }

                if (mListener != null && mListener.onModeChange(nextMode)) {
                    // TODO : CHANGE BUTTON TEXT SINCE THERE IS NEW STATE
                    mMode = nextMode;
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnControlInteractionListener) {
            mListener = (OnControlInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnControlInteractionListener {
        boolean onModeChange(SoundActivity.MODE mode);
        boolean onStartRecording();
        boolean onStopRecording();
        boolean onPlayRecording();
        boolean onClearRecording();
        void onSetInstrument(int channel, int instrument);
        void onChangeOctave(int channel, int base);
    }
}
