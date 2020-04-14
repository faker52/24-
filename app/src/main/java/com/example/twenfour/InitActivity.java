package com.example.twenfour;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.sensingai.commonlib.BaseApplication;

public class InitActivity extends AppCompatActivity {


    List<Fruit> function=new ArrayList<>();
    Baiduspeak speak=new Baiduspeak();
    Intent rule,pra,speed,socketlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity);
        ListView listView=(ListView) findViewById(R.id.listview);
        inital();
        MyAdapter adapter=new MyAdapter(InitActivity.this,R.layout.listview_item,function);
        listView.setAdapter(adapter);
        rule=new Intent(InitActivity.this,Rule.class);
        pra=new Intent(InitActivity.this,MainActivity.class);
        speed=new Intent(InitActivity.this,SpeedActivity.class);
        socketlist=new Intent(InitActivity.this,SocketList.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Fruit fruit=function.get(position);
                if(position==0){
                    startActivity(rule);}
                if(position==1)
                    startActivity(pra);
                if(position==2)
                    startActivity(speed);
                if(position==3)
                    startActivity(socketlist);
            }});

        listView.setOnTouchListener(new CommonOnTouchListener(new CommonOnTouchListener.CommonOnTouchCallback() {
            @Override
            public void onActionDown(MotionEvent event ) {

            }
            @Override
            public void onActionMove(MotionEvent event, View view) {
                nativeSpeak(getItemName(view));
            }
            @Override
            public void onActionUp(MotionEvent event) {

            }
        }));


    }











    private String mMenuName="";//功能菜单名称
    private String mEmptyArea="";//空白区域

    private String getItemName(View v) {
        if (v != null ) {
            TextView et = v.findViewById(R.id.textname);
            String menuName1 = et.getText().toString();
            if (!mMenuName.equals(menuName1)) {
                mMenuName = menuName1;
                if (BaseApplication.mBDTts != null) BaseApplication.mBDTts.stop();
                mEmptyArea = "";
                return mMenuName;
            }
        } else {
            mMenuName = "";
            if (TextUtils.isEmpty(mEmptyArea)) {
                mEmptyArea = NewsConatant.EMPTY_AREA;
                return mEmptyArea;
            }
        }
        return "-1";
    }

    private void nativeSpeak(String text) {
        if (!"-1".equals(text)) {
            speak.stop();
            speak.narmalspeak(this,text);
        }
    }




    public void inital(){
        Fruit cc=new Fruit("游戏规则",R.drawable.guize);
        Fruit cc1=new Fruit("练习模式",R.drawable.pra);
        Fruit cc2=new Fruit("竞速模式",R.drawable.speed);
        Fruit cc3=new Fruit("多人游戏",R.drawable.duoren);
        function.add(cc);
        function.add(cc1);
        function.add(cc2);
        function.add(cc3);
    }
}
