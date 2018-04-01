package com.example.alexdriedger.pianotime;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


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

    private enum RECORDING_STATE {
        NO_RECORDING,
        IDLE,
        ACTIVE_RECORDING,
        PLAYBACK_RECORDING,
    }


    private RECORDING_STATE mRecordingState;
    private SoundActivity.MODE mMode;
    private boolean isRecording; // TODO : CHANGE TO RECORDING STATE

    private OnControlInteractionListener mListener;

    public ControlBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode the fragment is in. Should be one of KEYBOARD or SOUNDPAD
     * @return A new instance of fragment ControlBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlBarFragment newInstance(SoundActivity.MODE mode) {
        ControlBarFragment fragment = new ControlBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MODE, mode.toString());
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
        //  TODO : PUT THIS IN THE ARGUMENTS PASSED IN BUNDLE
        mRecordingState = RECORDING_STATE.NO_RECORDING;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control_bar, container, false);
        initButtonListeners(v);
        setButtonVisibilities(v);
        return v;
    }

    private void initButtonListeners(View v) {
        final Button modeButton = v.findViewById(R.id.change_mode_button);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("ControlBarFragment", "Mode button clicked");

                // Determine next mode
                SoundActivity.MODE nextMode;
                switch (mMode) {
                    case KEYBOARD: nextMode = SoundActivity.MODE.SOUNDPAD; break;
                    case SOUNDPAD: nextMode = SoundActivity.MODE.KEYBOARD; break;
                    default: throw new RuntimeException("ControlBarFragment: Invalid nextMode: " + mMode);
                }

                if (mListener != null && mListener.onModeChange(nextMode)) {
                    onModeChange(nextMode);
                }
            }
        });

        final Button recordButton = v.findViewById(R.id.start_record_mode_button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordingState = RECORDING_STATE.IDLE;
                setButtonVisibilities();
            }
        });

        final ImageButton newLayerButton = v.findViewById(R.id.control_button_new_layer);
        newLayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null && mListener.onStartRecording()) {
                    mRecordingState = RECORDING_STATE.ACTIVE_RECORDING;
                    setButtonVisibilities();
                }
            }
        });

        final ImageButton deleteLayerButton = v.findViewById(R.id.control_button_delete);
        deleteLayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onDeleteTrack();
                }
            }
        });

        final ImageButton playbackRecordingButton = v.findViewById(R.id.control_button_play_pause);
        playbackRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null && mListener.onPlayRecording()) {
                    mRecordingState = RECORDING_STATE.PLAYBACK_RECORDING;
                    setButtonVisibilities();
                }
            }
        });

        final ImageButton confirmButton = v.findViewById(R.id.control_button_checkmark);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mRecordingState) {
                    case IDLE: {
                        if (mListener != null && mListener.onExportRecording()) {
                            mRecordingState = RECORDING_STATE.NO_RECORDING;
                            setButtonVisibilities();
                        }
                        break;
                    }
                    case ACTIVE_RECORDING: {
                        if (mListener != null && mListener.onStopRecording()) {
                            mRecordingState = RECORDING_STATE.IDLE;
                            setButtonVisibilities();
                        }
                        break;
                    }

                    case PLAYBACK_RECORDING: {
                        if (mListener != null && mListener.onStopPlayback()) {
                            mRecordingState = RECORDING_STATE.IDLE;
                            setButtonVisibilities();
                        }
                        break;
                    }
                }
            }
        });
    }

    private void setButtonVisibilities() {
        setButtonVisibilities(getView());
    }

    /**
     * Sets visibilities on buttons based on current state.
     * This method must be called OnCreateView instead of setButtonVisibilities() because getView
     * will fail in OnCreateView
     * @param v parent view of control bar
     */
    private void setButtonVisibilities(View v) {
        switch (mRecordingState) {
            case NO_RECORDING: {
                v.findViewById(R.id.start_record_mode_button).setVisibility(View.VISIBLE);
                v.findViewById(R.id.recording_controls).setVisibility(View.GONE);
                break;
            }
            case IDLE: {
                v.findViewById(R.id.start_record_mode_button).setVisibility(View.GONE);
                v.findViewById(R.id.recording_controls).setVisibility(View.VISIBLE);
                v.findViewById(R.id.control_button_new_layer).setVisibility(View.VISIBLE);
                v.findViewById(R.id.control_button_delete).setVisibility(View.VISIBLE);
                v.findViewById(R.id.control_button_play_pause).setVisibility(View.VISIBLE);
                v.findViewById(R.id.control_button_checkmark).setVisibility(View.VISIBLE);
                break;
            }
            case PLAYBACK_RECORDING:
            case ACTIVE_RECORDING: {
                v.findViewById(R.id.start_record_mode_button).setVisibility(View.GONE);
                v.findViewById(R.id.recording_controls).setVisibility(View.VISIBLE);
                v.findViewById(R.id.control_button_new_layer).setVisibility(View.GONE);
                v.findViewById(R.id.control_button_delete).setVisibility(View.GONE);
                v.findViewById(R.id.control_button_play_pause).setVisibility(View.GONE);
                v.findViewById(R.id.control_button_checkmark).setVisibility(View.VISIBLE);
                break;
            }
            default: throw new RuntimeException("ControlBarFragment. Invalid recording state: " + mRecordingState);
        }
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
        boolean onModeChange(SoundActivity.MODE nextMode);
        boolean onStartRecording();
        boolean onStopRecording();
        boolean onPlayRecording();
        boolean onStopPlayback();
        boolean onDeleteTrack();
        boolean onExportRecording();
        void onSetInstrument(int channel, int instrument);
        void onChangeOctave(int channel, int base);
    }

    private void onModeChange(SoundActivity.MODE nextMode) {
        this.mMode = nextMode;

        // Change button text
        int buttonRes;
        switch (nextMode) {
            case KEYBOARD: buttonRes = R.string.mode_button_soundpad; break;
            case SOUNDPAD: buttonRes = R.string.mode_button_keyboard; break;
            default: throw new RuntimeException("ControlBarFragment: Invalid nextMode: " + nextMode);
        }

        final Button button = getView().findViewById(R.id.change_mode_button);
        button.setText(buttonRes);
    }
}
