package com.example.alexdriedger.pianotime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ryan on 2018/4/5.
 */

public class ServerActivity extends AppCompatActivity {
    Button button_send_file;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        button_send_file = findViewById(R.id.button_send_file);
        button_send_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SocketThread tem = new SocketThread("abc");
                tem.start();
            }
        });


    }






}
