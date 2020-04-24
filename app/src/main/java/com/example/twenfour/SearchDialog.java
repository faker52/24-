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



    private Socket socket = null;

    private PrintWriter out = null;

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
        Log.e("Search","1" );

        new Thread() {
            public void run() {
                try {
                    Log.e("Search","1" );
                    socket= ((MySocket)getApplication()).getSocket();
                    out=new PrintWriter(socket.getOutputStream(),true);
                    Log.e("Search","2" );
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                SocketPlay.putExtra("housenum",msg);
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
