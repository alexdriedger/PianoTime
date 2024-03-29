package com.example.alexdriedger.pianotime;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;

/**
 * Created by ryan on 2018/4/4.
 */

public class MyBTService {
    //Interface for BluetoothEventListener
    public interface BluetoothEventListener {
        public void onBluetoothDataReady(LinkedList<MidiEvent> midiEvents);

    }

    // private listener
    private BluetoothEventListener listener;


    //MyBTService constructor
    public MyBTService(Context context){

        this.listener = null;
        SetupConnection(context);
    }

    //Lisener setup function
    public void setMyBTServiceListener(BluetoothEventListener listener){
        this.listener = listener;

    }


    private long tick = 0;
    private Queue<Byte> dataQueue = new LinkedList<>();
    private LinkedList<MidiEvent> midiEvents = new LinkedList<>();


    private static final String TAG = "BTConnectionService";
    private final static int REQUEST_ENABLE_BT = 1;
    private final String BTChipMAC = "00:06:66:6C:A6:25";
    private final String TERMINATION_EVENT = "ffffffffffffffff";

    private boolean isClosed;

    private static final UUID Serial_uuid =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mBTAdapter;
    private BluetoothDevice mBtDevice;
    private BluetoothSocket mBtSocket;

    private clientThread mBtClientConnectThread;
    private ConnectedThread mConnectedThread;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    private Context mContext;

    private Boolean userCancel;

    // Connection setup function. Called in the MyBTService constructor
    public void SetupConnection (Context context) {
        mContext = context;
        BluetoothSocket mBtSocket = null;
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        //
        if(mBTAdapter == null){
            //Device does not support bluetooth
            Log.d(TAG, "Device does not support bluetooth");

        }
        if(!mBTAdapter.isEnabled()) { //enable bt if not yet
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableIntent);
            Log.d(TAG, "Bluetooth enabled");
        }

        mBtDevice = mBTAdapter.getRemoteDevice(BTChipMAC);


        if((mBtDevice.getName()=="null")){
            Log.d(TAG, "Bluetooth module not found ");
        }

        else {
            Log.d(TAG, "Device name:" + mBtDevice.getName());

            try {
                mBtSocket = mBtDevice.createRfcommSocketToServiceRecord(Serial_uuid);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "", e);
                Log.d(TAG, "create socket failed");
            }
        }


        if(mBtDevice.getName()!="null"){
            mConnectedThread = new ConnectedThread(mBtSocket,mBtDevice);
            mConnectedThread.start();
        }



    }

    public void disconnect() {

        Log.d(TAG, "ConnectionManager disconnect connection");

        if(mConnectedThread != null) {
            mConnectedThread.cancel();
        }
    }

    private class clientThread extends Thread{
        private BluetoothSocket mBtSocket;

        public void run(){

            Log.d(TAG, "mBtSocket start-->"+mBtSocket);
            try {
                //stop Discovery, or connection will be unstable
                mBTAdapter.cancelDiscovery();

                // get the socket to mBTDevice
                mBtSocket = mBtDevice.createRfcommSocketToServiceRecord(Serial_uuid);

                //connect the socket
                mBtSocket.connect();

                Log.d("mBtSocket", "socket connected");


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "", e);
                Log.d(TAG, "connection failed");
            }

            if(mBtSocket != null){
                connected(mBtSocket, mBtDevice);
                Log.d("mBtSocket", "start connected thread");
            }

        }

        public void cancel(){
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                mBtSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket, mBtDevice);
        mConnectedThread.start();
    }

    //connected thread
    private class ConnectedThread extends Thread {


        private BluetoothSocket mmSocket;
        private InputStream mmInStream;
        private OutputStream mmOutStream;
        private BluetoothDevice mmDevice;

        public ConnectedThread(BluetoothSocket socket, BluetoothDevice device) {
            mmSocket = socket;
            mmDevice = device;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            try {
                mBtSocket = mmDevice.createRfcommSocketToServiceRecord(Serial_uuid);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "", e);
                Log.d(TAG, "create socket failed");
            }

            // Keep listening to the InputStream until an exception occurs
            try {
                mmSocket.connect();
                Log.d(TAG, "Socket connected");

            } catch (IOException e) {
                mmSocket = null;
                mConnectedThread = null;

                Log.e("Socket connect", "", e);

                return;
            }

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //get data stream form the socket
            try {

                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
                Log.d(TAG,"Getting data stream from the socket");
            } catch (IOException e) {
                mmSocket = null;
                mConnectedThread = null;

                Log.e("get stream from socket", "", e);

                return;
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            while (true) {
                try {
                    //wait here until input stream gets value
                    bytes = mmInStream.read(buffer);
                    //pass the data bytes to the listener

                    if(listener != null && bytes > 0) {

                        int [] data = new int[bytes];

                        StringBuilder sb = new StringBuilder();

                        for(int i=0; i<bytes; i++) {
                            data[i] = (int)buffer[i];
                            dataQueue.add(buffer[i]);

                            sb.append(String.format("%x", buffer[i]));
                        }


                        Log.d(TAG, "message ready:" + sb.toString());

                        parse();

                    }
                } catch (IOException e) {
                    //if mSocket.close() called somewhere else
                    //or connection closed
                    break;
                }
            }



        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    //send message
    private void sendMessageHandle(String msg)
    {
        if (mBtSocket == null)
        {
            Log.d("----------------------", "No connection");
            return;
        }

        try {
            OutputStream os = mBtSocket.getOutputStream();
            os.write(msg.getBytes()); //
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Call this function when all the data buffers sent are received
    private void parse(){
        byte[] dataBuffer = new byte[8];
        while (dataQueue.size()>=8){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                dataBuffer[i] = dataQueue.remove();
                sb.append(String.format("%x", dataBuffer[i]));
            }



            if(sb.toString().equals(TERMINATION_EVENT)){
                Log.d(TAG, "Found TERMINATION_EVENT");

                listener.onBluetoothDataReady(midiEvents);

                tick = 0;
                dataQueue.clear();
                midiEvents.clear();
            }


            int bpm = castByteToUnsignedint(dataBuffer[0]);
            int deltaSmaple = castByteToUnsignedint(dataBuffer[1]) << 24 |
                    castByteToUnsignedint(dataBuffer[2]) << 16 |
                    castByteToUnsignedint(dataBuffer[3]) << 8 |
                    castByteToUnsignedint(dataBuffer[4]);
            int deltaTick = (int)((double)(480*deltaSmaple*bpm)/60.0/32000.0);
            int note = (int)dataBuffer[5];
            int on_off = dataBuffer[6];
            int vol = (int)dataBuffer[7];

            tick += deltaTick;

            Long[] event = new Long[]{(long)bpm, (long)deltaTick, (long)note, (long)on_off, (long)vol};


            Log.d(TAG, event.toString());

            if(on_off==1){
                midiEvents.add( new NoteOn(tick, 0, note, vol));
            }

            else{
                midiEvents.add(new NoteOff(tick, 0, note, vol));
            }

            Log.d(TAG, midiEvents.peekLast().toString());

        }

    }

    private int castByteToUnsignedint(byte b){
        return ((int)b&0xff);

    }
}
