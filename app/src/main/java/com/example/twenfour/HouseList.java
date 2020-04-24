package com.example.twenfour;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HouseList extends AppCompatActivity {

    private Socket socket = null;
    Thread mythread;
    Baiduspeak speak = new Baiduspeak();
    private PrintWriter out = null;
    private BufferedReader in = null;
    String msg="";
    String content="";
    String TAG="HouseList";
    List<Fruit> function = new ArrayList<>();
    Intent SocketPlay;
    TextView mtextview;
    String[] xx ;
    ListView listView;
    MyAdapter adapter;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 0x125) {

                if(content.equals("get")){
                    mtextview.setText("暂时没有房间");
                }
                else {
                    Log.e("houslist",content);
                    Toast.makeText(HouseList.this,content,Toast.LENGTH_LONG).show();
                    mtextview.setText("房间列表如下");
                    xx=content.split("#");
                    inital(xx.length-1, xx);
                    listView.setAdapter(adapter);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.houselist);
        listView = (ListView) findViewById(R.id.houselistview);
        mtextview = (TextView) findViewById(R.id.housetext);
        SocketPlay=new Intent(HouseList.this,SocketPlay.class);
        adapter = new MyAdapter(HouseList.this, R.layout.listview_item, function);
        Intent intent=getIntent();





       mythread=new Thread() {
            public void run() {
                try {

                    socket= ((MySocket)getApplication()).getSocket();

                    out=new PrintWriter(socket.getOutputStream(),true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    if (socket.isConnected()) {
                        if (!socket.isOutputShutdown()) {

                            out.println("get");
                        }
                    }

                        if((msg=in.readLine()) != null){
                            content=msg;
                            handler.sendEmptyMessage(0x125);
                        }

                }catch(Exception e){e.printStackTrace();}

            }
        };
       mythread.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    if(xx[position+1].split(",")[1].equals("1")){
                    SocketPlay.putExtra("housenum",xx[position+1].split(",")[0]);

                    if (socket.isConnected()) {
                        if (!socket.isOutputShutdown()) {
                            out.println("housenum"+"#"+xx[position+1].split(",")[0]);
                        }
                    }
                        speak.narmalspeak(HouseList.this,"已进入房间号"+xx[position+1].split(",")[0]);
                    mythread.interrupt();
                    startActivity(SocketPlay);
                    finish();
                    }
                    else{
                        speak.narmalspeak(HouseList.this,"房间已满，不可进入");
                    }
            }});
    }

    public void inital(int num,String[] housenum) {
        Log.e(TAG, "第二个位置成功" );
        for(int i=1;i<num+1;i++) {
            if(housenum[i].split(",")[1].equals("1")) {
                Log.e(TAG, "第三个位置成功" );
                Fruit cc = new Fruit(housenum[i].split(",")[0]+">"+"可以进入", R.drawable.smallhouse);
                function.add(cc);
            }
            else{
                Log.e(TAG, "第四个位置成功" );
                Fruit cc = new Fruit(housenum[i].split(",")[0]+">"+"房间已满", R.drawable.smallhouse);
                function.add(cc);
            }
        }


    }



}
