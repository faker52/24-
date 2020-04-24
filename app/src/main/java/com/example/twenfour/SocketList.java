package com.example.twenfour;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketList extends AppCompatActivity {

    private static final String HOST = "47.115.49.205";//47.115.49.205
    private static final int PORT = 3000;
    private Socket socket = null;

    private PrintWriter out = null;
    List<Fruit> function = new ArrayList<>();
    Baiduspeak speak = new Baiduspeak();
    Intent SearchDialog,HouseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity);
        ListView listView = (ListView) findViewById(R.id.listview);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        inital();
        MyAdapter adapter = new MyAdapter(SocketList.this, R.layout.listview_item, function);
        listView.setAdapter(adapter);
        SearchDialog=new Intent(SocketList.this,SearchDialog.class);
        HouseList=new Intent(SocketList.this,HouseList.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Fruit fruit=function.get(position);
                if(position==0){
                    startActivity(SearchDialog);
                finish();}
                else if(position==1){
                    startActivity(HouseList);
                    finish();}

            }});


        Log.e("SocketList","1" );
        new Thread() {
            public void run() {
                try {
                    Log.e("SocketList","2" );
                    socket=new Socket(HOST,PORT);
                    out=new PrintWriter(socket.getOutputStream(),true);
                    ((MySocket)getApplication()).setSocket(socket);
                    Log.e("SocketList","3" );

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void inital() {
        Fruit cc = new Fruit("创建/搜索房间", R.drawable.serch);
        Fruit cc1 = new Fruit("房间列表", R.drawable.bighouse);
        function.add(cc);
        function.add(cc1);


    }

}