package com.example.alexdriedger.pianotime;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketThread extends Thread{
    private String filename;
    public SocketThread(String filename) {
        this.filename = filename;
    }

    @Override
    public void run(){
        Socket socket;

        try{
            socket = new Socket("52.36.163.68",23333);

            OutputStream outputStream = socket.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(this.filename);
            writer.flush();

            writer.close();

            socket.close();

            Log.d("Socket thread:","Closed.");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}