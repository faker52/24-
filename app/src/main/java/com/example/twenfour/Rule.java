package com.example.twenfour;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Rule extends AppCompatActivity {
    TextView responseText;
    Baiduspeak speek=new Baiduspeak();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule);
        Intent intent=getIntent();
        responseText=(TextView) findViewById(R.id.responetext);
        String x="屏幕上方将出现四个数字，你只能使用加法，减法，乘法和除法的一种或者多种进行计算，算出24，则游戏胜利，四个数字必须使用且只能使用一次，竞速模式下，你要完成5道题目，并计算你完成的时间";
        responseText.setText(x);
        speek.narmalspeak(Rule.this,x);
    }
}

