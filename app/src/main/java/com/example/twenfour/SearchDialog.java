package com.example.twenfour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class SearchDialog extends AppCompatActivity {


    private static final String HOST = "192.168.43.3";
    private static final int PORT = 3000;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "";
    Button button;
    EditText editText;
    String TAG="hh";
    int count=0;
    Thread t2;
    Intent SocketPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Intent intent = getIntent();
        editText=(EditText)findViewById(R.id.search);
        button=(Button)findViewById(R.id.sendnum);
        SocketPlay=new Intent(SearchDialog.this,SocketPlay.class);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        new Thread() {
            public void run() {
                try {
                    socket=new Socket(HOST,PORT);
                    out=new PrintWriter(socket.getOutputStream(),true);
                    ((MySocket)getApplication()).setSocket(socket);

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                msg="housenum#"+msg;
                if (socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        out.println(msg);
                    }
                }
                startActivity(SocketPlay);
                finish();

            }
        });
    }




}
