package com.example.twenfour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SocketList extends AppCompatActivity {


    List<Fruit> function = new ArrayList<>();
    Baiduspeak speak = new Baiduspeak();
    Intent SearchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity);
        ListView listView = (ListView) findViewById(R.id.listview);
        inital();
        MyAdapter adapter = new MyAdapter(SocketList.this, R.layout.listview_item, function);
        listView.setAdapter(adapter);
        SearchDialog=new Intent(SocketList.this,SearchDialog.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Fruit fruit=function.get(position);
                if(position==0){
                    startActivity(SearchDialog);}

            }});
    }


    public void inital() {
        Fruit cc = new Fruit("创建房间", R.drawable.guize);
        Fruit cc1 = new Fruit("加入房间", R.drawable.guize);
        function.add(cc);
        function.add(cc1);


    }

}