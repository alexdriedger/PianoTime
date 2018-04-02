package com.example.alexdriedger.pianotime;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

public abstract class AbstractMusicInteractionFragment extends Fragment{

    protected OnNotePressListener mListener;

    public interface OnNotePressListener {
        void onTouch(boolean noteOn, int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotePressListener) {
            mListener = (OnNotePressListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotePressListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected class MusicInteractionOnTouchListener implements View.OnTouchListener {

        private int mPos;

        /**
         * Creates new KeyboardOnTouchListener
         * @param pos must be >= 0.
         */
        public MusicInteractionOnTouchListener(int pos) {
            this.mPos = pos;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                mListener.onTouch(true, mPos);
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                mListener.onTouch(false, mPos);
            }
            return true;
        }
    }
}
