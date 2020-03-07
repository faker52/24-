package com.example.twenfour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class GradeDialog extends Activity {
    TextView responseText;
    Baiduspeak speek=new Baiduspeak();
    String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade);
        Intent intent=getIntent();

        responseText=(TextView) findViewById(R.id.responetext1);
        x=intent.getStringExtra("time");
        responseText.setText(x+"'");
        speek.narmalspeak(GradeDialog.this,"你的速度为"+x+"秒");
    }
}
