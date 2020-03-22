package com.example.twenfour;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import cn.sensingai.commonlib.BaseApplication;


public class MainActivity extends AppCompatActivity {

    private GridView mGridView;
    private GridView mGridView1,mGridView3;
    private NumAdapter mProvinceAdapter;
    private GirdView3Adapter mGird3;
    private OpAdapter mOpAdapter;
    String TAG="hh";
    int Turn=0;
    String x="";
    static List<Integer> remember = new ArrayList<>();
    static List<Integer> remember1 = new ArrayList<>();

    int Operator=-1;
    int Count=0;
    List<Integer> nums0=new ArrayList<>();
    List<Integer> nums1=new ArrayList<>();
    List<Integer> nums2=new ArrayList<>();
    TextView textView;
    int numcount=0;
    int LastPosition;
    Baiduspeak speak=new Baiduspeak();
    private String[] provinceNames = new String[4];
    private String[] provinceNames1=new String[4];
    private int[] bgColor = new int[]{R.color.bgc, R.color.bgc, R.color.bgc, R.color.bgc};
    private String[] opName=new String[]{"加","减","乘","除"};
    private int[] images=new int[]{R.drawable.add,R.drawable.sub,R.drawable.mul,R.drawable.chu};


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        numcount=0;
        setContentView(R.layout.gird_view);
        textView=(TextView) findViewById(R.id.timetext);
        random();
        initView1();
        initView2();
        initView3();




        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Operator==-1) Turn=0;
                if(provinceNames[position]==""){
                    speak.narmalspeak(MainActivity.this,"此区域无数字");
                }
                else if(Turn==0){
                    speak.narmalspeak(MainActivity.this,provinceNames[position]);
                    LastPosition=position;
                    nums0.clear();
                    nums2.clear();
                    nums1.clear();
                    if(provinceNames[position].contains("/")){
                        String[] x=provinceNames[position].split("/");
                        nums0.add(Integer.valueOf(x[0]));
                        nums0.add(Integer.valueOf(x[1]));
                    }
                    else{
                        int a=Integer.valueOf(provinceNames[position]);
                        nums0.add(a);
                        nums0.add(1);
                    }
                    Turn=1;

                }
                else if(Turn==1) {
                    if (position == LastPosition) {
                        speak.narmalspeak(MainActivity.this,"不可以使用相同的数字");
                    }
                    else if(provinceNames[position]==""){
                        speak.narmalspeak(MainActivity.this,"此区域无数字");
                    }
                    else {

                        if (provinceNames[position].contains("/")) {
                            String[] x = provinceNames[position].split("/");
                            nums1.add(Integer.valueOf(x[0]));
                            nums1.add(Integer.valueOf(x[1]));
                        }
                        else {
                            nums1.add(Integer.valueOf(provinceNames[position]));
                            nums1.add(1);
                        }
                    Log.e(TAG, "onItemClick: nums0" + nums0);
                    Log.e(TAG, "onItemClick: nums1" + nums1);
                    Log.e(TAG, "onItemClick: op" + Operator);
                    if (Operator == 0) nums2 = myadd(nums0, nums1);
                    if (Operator == 1) nums2 = mysub(nums0, nums1);
                    if (Operator == 2) nums2 = mul(nums0, nums1);
                    if (Operator == 3 && nums1.get(0) != 0) nums2 = mychu(nums0, nums1);
                    if (nums1.get(0) == 0 && Operator == 3)
                        speak.narmalspeak(MainActivity.this, "操作错误");
                    if (nums2.get(1) == 1) {
                        provinceNames[position] = String.valueOf(nums2.get(0));
                        provinceNames[LastPosition] = "";
                        speak.narmalspeak(MainActivity.this,"等于"+provinceNames[position]);
                        Count++;
                    } else {

                        provinceNames[position] = (String.valueOf(nums2.get(0)) + "/" + String.valueOf(nums2.get(1)));
                        provinceNames[LastPosition] = "";
                        speak.narmalspeak(MainActivity.this,"等于"+provinceNames[position]);
                        Count++;
                    }
                        Log.e(TAG, "onItemClick: nums2"+nums2 );
                        if(Count==3&&nums2.get(0)==24&&nums2.get(1)==1){
                            speak.narmalspeak(MainActivity.this,"回答正确");
                            Log.e(TAG, "onItemClick: nums2"+nums2 );
                            random();
                            initView1();
                            textView.setText(null);

                            Count=0;

                    }

                }
                    Operator=-1;
                    initView1();
                    Turn=0;
                }
            }});


        mGridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if(position==0){
                                                    Count=0;
                                                    random();
                                                    Operator=-1;
                                                    initView1();
                                                    Turn=0;

                                                    textView.setText(null);
                                                    speak.stop();
                                                    speak.narmalspeak(MainActivity.this,"已切换下一题");
                                                }
                                                if(position==1){
                                                    provinceNames[0]=provinceNames1[0];
                                                    provinceNames[1]=provinceNames1[1];
                                                    provinceNames[2]=provinceNames1[2];
                                                    provinceNames[3]=provinceNames1[3];
                                                    Count=0;
                                                    textView.setText(null);
                                                    Operator=-1;
                                                    initView1();
                                                    Turn=0;
                                                    speak.stop();
                                                    speak.narmalspeak(MainActivity.this,"已重置");
                                                }
                                                 if(position==2){
                                                     getanswer();
                                                     textView.setText(x);
                                                 }
                                             }

                                         });
        mGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Operator=position;
             speak.narmalspeak(MainActivity.this,opName[Operator]);
            }});


        mGridView.setOnTouchListener(new CommonOnTouchListener(new CommonOnTouchListener.CommonOnTouchCallback() {
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
        mGridView1.setOnTouchListener(new CommonOnTouchListener(new CommonOnTouchListener.CommonOnTouchCallback() {
            @Override
            public void onActionDown(MotionEvent event ) {

            }
            @Override
            public void onActionMove(MotionEvent event, View view) {

                nativeSpeak(getItemName1(view));
            }
            @Override
            public void onActionUp(MotionEvent event) {

            }
        }));

        mGridView3.setOnTouchListener(new CommonOnTouchListener(new CommonOnTouchListener.CommonOnTouchCallback() {
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


    public void getanswer(){
        List<Integer> temp=new ArrayList<>();
        if(remember1.get(0)==1){
            x="("+String.valueOf(remember.get(0))+"*"+String.valueOf(remember.get(1))+")";
            temp.add(remember.get(0)*remember.get(1));
            temp.add(1);
            speak.narmalspeak(MainActivity.this,String.valueOf(remember.get(0))+"乘"+String.valueOf(remember.get(1))+"等于"+temp.get(0));
        }
        if(remember1.get(0)==2){
            x="("+String.valueOf(remember.get(0))+"+"+String.valueOf(remember.get(1))+")";
            temp.add(remember.get(0)+remember.get(1));
            temp.add(1);
            speak.narmalspeak(MainActivity.this,String.valueOf(remember.get(0))+"加"+String.valueOf(remember.get(1))+"等于"+temp.get(0));
        }
        if(remember1.get(0)==5){
            x="("+String.valueOf(remember.get(0))+"/"+String.valueOf(remember.get(1))+")";
            int t=gcd1(remember.get(0),remember.get(1));
            temp.add(remember.get(0)/t);
            temp.add(remember.get(1)/t);
            if(temp.get(1)==1)
            speak.narmalspeak(MainActivity.this,String.valueOf(remember.get(0))+"除"+String.valueOf(remember.get(1))+"等于"+temp.get(0));
            else{
                speak.narmalspeak(MainActivity.this,String.valueOf(remember.get(0))+"除"+String.valueOf(remember.get(1))+"等于"+temp.get(1)+"分之"+temp.get(0));
            }

        }
        if(remember1.get(0)==6){
            x="("+String.valueOf(remember.get(0))+"-"+String.valueOf(remember.get(1))+")";
            temp.add(remember.get(0)-remember.get(1));
            temp.add(1);
            speak.narmalspeak(MainActivity.this,String.valueOf(remember.get(0))+"减"+String.valueOf(remember.get(1))+"等于"+temp.get(0));
        }
        if(remember1.get(0)==3){
            x="("+String.valueOf(remember.get(1))+"-"+String.valueOf(remember.get(0))+")";

            temp.add(remember.get(1)-remember.get(0));
            temp.add(1);
            speak.narmalspeak(MainActivity.this,String.valueOf(remember.get(1))+"减"+String.valueOf(remember.get(0))+"等于"+temp.get(0));
        }
        if(remember1.get(0)==4){
            x="("+String.valueOf(remember.get(1))+"/"+String.valueOf(remember.get(0))+")";
            int t=gcd1(remember.get(0),remember.get(1));
            temp.add(remember.get(1)/t);
            temp.add(remember.get(0)/t);
            if(temp.get(1)==1)
                speak.narmalspeak(MainActivity.this,String.valueOf(remember.get(1))+"除"+String.valueOf(remember.get(0))+"等于"+temp.get(0));
            else{
                speak.narmalspeak(MainActivity.this,String.valueOf(remember.get(1))+"除"+String.valueOf(remember.get(0))+"等于"+temp.get(1)+"分之"+temp.get(0));
            }
        }





        if(remember1.get(1)==1){
            x="("+x+"*"+String.valueOf(remember.get(2))+")";
            if(temp.get(1)==1){
                speak.narmalspeak(MainActivity.this,temp.get(0)+"乘"+remember.get(2)+"等于"+remember.get(2)*temp.get(0));
                temp.set(0,remember.get(2)*temp.get(0));
            }
            else {
                List<Integer> cc = new ArrayList<>();
                List<Integer> in = new ArrayList<>();
                cc.add(remember.get(2));
                cc.add(1);
                in=mul(temp,cc);
                if(in.get(1)==1)
                {
                    speak.narmalspeak(MainActivity.this,temp.get(0)+"分之"+temp.get(1)+"乘"+remember.get(2)+"等于"+remember.get(2)*temp.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,temp.get(0)+"分之"+temp.get(1)+"乘"+remember.get(2)+"等于"+in.get(0)+"分子"+in.get(1));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }

            }

        }
        if(remember1.get(1)==2){
            x="("+x+"+"+String.valueOf(remember.get(2))+")";
            if(temp.get(1)==1){
                speak.narmalspeak(MainActivity.this,temp.get(0)+"加"+remember.get(2)+"等于"+(remember.get(2)+temp.get(0)));
                temp.set(0,remember.get(2)+temp.get(0));
            }
            else {
                List<Integer> cc = new ArrayList<>();
                List<Integer> in = new ArrayList<>();
                cc.add(remember.get(2));
                cc.add(1);
                in=myadd(temp,cc);

                speak.narmalspeak(MainActivity.this,temp.get(0)+"分之"+temp.get(1)+"加"+remember.get(2)+"等于"+in.get(0)+"分子"+in.get(1));
                temp.set(0,in.get(0));
                temp.set(1,in.get(1));
            }


        }
        if(remember1.get(1)==5){
            x="("+x+"/"+String.valueOf(remember.get(2))+")";
            List<Integer> cc = new ArrayList<>();
            List<Integer> in = new ArrayList<>();
            if(temp.get(1)==1) {
                cc.add(remember.get(2));
                cc.add(1);
                in=mychu(temp,cc);
                if(in.get(1)==1){
                    speak.narmalspeak(MainActivity.this,temp.get(0)+"除"+remember.get(2)+"等于"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,temp.get(0)+"除"+remember.get(2)+"等于"+in.get(1)+"分之"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
            }
            else{
                cc.add(remember.get(2));
                cc.add(1);
                in=mychu(temp,cc);
                if(in.get(1)==1){
                    speak.narmalspeak(MainActivity.this,temp.get(1)+"分之"+temp.get(0)+"除"+remember.get(2)+"等于"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,temp.get(1)+"分之"+temp.get(0)+"除"+remember.get(2)+"等于"+in.get(1)+"分之"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
            }

        }
        if(remember1.get(1)==6){
            x="("+x+"-"+String.valueOf(remember.get(2))+")";
            if(temp.get(1)==1){
                speak.narmalspeak(MainActivity.this,temp.get(0)+"减"+remember.get(2)+"等于"+(temp.get(0)-remember.get(2)));
                temp.set(0,temp.get(0)-remember.get(2));
            }
            else {
                List<Integer> cc = new ArrayList<>();
                List<Integer> in = new ArrayList<>();
                cc.add(remember.get(2));
                cc.add(1);
                in=mysub(temp,cc);
                speak.narmalspeak(MainActivity.this,temp.get(0)+"分之"+temp.get(1)+"减"+remember.get(2)+"等于"+in.get(1)+"分子"+in.get(0));
                temp.set(0,in.get(0));
                temp.set(1,in.get(1));

            }
        }
        if(remember1.get(1)==3){
            x="("+String.valueOf(remember.get(2))+"-"+x+")";
            if(temp.get(1)==1){
                speak.narmalspeak(MainActivity.this,remember.get(2)+"减"+temp.get(0)+"等于"+(remember.get(2)-temp.get(0)));
                temp.set(0,remember.get(2)-temp.get(0));
            }
            else {
                List<Integer> cc = new ArrayList<>();
                List<Integer> in = new ArrayList<>();
                cc.add(remember.get(2));
                cc.add(1);
                in=mysub(cc,temp);
                speak.narmalspeak(MainActivity.this,remember.get(2)+"减"+temp.get(1)+"分之"+temp.get(0)+"等于"+in.get(1)+"分子"+in.get(0));
                temp.set(0,in.get(0));
                temp.set(1,in.get(1));

            }
        }
        if(remember1.get(1)==4){
            x="("+String.valueOf(remember.get(2))+"/"+x+")";
            List<Integer> cc = new ArrayList<>();
            List<Integer> in = new ArrayList<>();
            if(temp.get(1)==1) {
                cc.add(remember.get(2));
                cc.add(1);
                in=mychu(cc,temp);
                if(in.get(1)==1){
                    speak.narmalspeak(MainActivity.this,remember.get(2)+"除"+temp.get(0)+"等于"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,remember.get(2)+"除"+temp.get(0)+"等于"+in.get(1)+"分之"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
            }
            else{
                cc.add(remember.get(2));
                cc.add(1);
                in=mychu(cc,temp);
                if(in.get(1)==1){
                    speak.narmalspeak(MainActivity.this,remember.get(2)+"除"+temp.get(1)+"分之"+temp.get(0)+"等于"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,remember.get(2)+"除"+temp.get(1)+"分之"+temp.get(0)+"等于"+in.get(0)+"分之"+in.get(1));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
            }

        }





        if(remember1.get(2)==1){
            x="("+x+"*"+String.valueOf(remember.get(3))+")";
            if(temp.get(1)==1){
                speak.narmalspeak(MainActivity.this,temp.get(0)+"乘"+remember.get(3)+"等于"+remember.get(3)*temp.get(0));
                temp.set(0,remember.get(3)*temp.get(0));
            }
            else {
                List<Integer> cc = new ArrayList<>();
                List<Integer> in = new ArrayList<>();
                cc.add(remember.get(3));
                cc.add(1);
                in=mul(temp,cc);
                if(in.get(1)==1)
                {
                    speak.narmalspeak(MainActivity.this,temp.get(0)+"分之"+temp.get(1)+"乘"+remember.get(3)+"等于"+remember.get(3)*temp.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,temp.get(0)+"分之"+temp.get(1)+"乘"+remember.get(3)+"等于"+in.get(1)+"分子"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }

            }
        }
        if(remember1.get(2)==2){
            x="("+x+"+"+String.valueOf(remember.get(3))+")";
            if(temp.get(1)==1){
                speak.narmalspeak(MainActivity.this,temp.get(0)+"加"+remember.get(3)+"等于"+(remember.get(3)+temp.get(0)));
                temp.set(0,remember.get(3)+temp.get(0));
            }
            else {
                List<Integer> cc = new ArrayList<>();
                List<Integer> in = new ArrayList<>();
                cc.add(remember.get(3));
                cc.add(1);
                in=myadd(temp,cc);

                speak.narmalspeak(MainActivity.this,temp.get(0)+"分之"+temp.get(1)+"加"+remember.get(3)+"等于"+in.get(1)+"分子"+in.get(0));
                temp.set(0,in.get(0));
                temp.set(1,in.get(1));
            }
        }
        if(remember1.get(2)==5){
            x="("+x+"/"+String.valueOf(remember.get(3))+")";
            List<Integer> cc = new ArrayList<>();
            List<Integer> in = new ArrayList<>();
            if(temp.get(1)==1) {
                cc.add(remember.get(3));
                cc.add(1);
                in=mychu(temp,cc);
                if(in.get(1)==1){
                    speak.narmalspeak(MainActivity.this,temp.get(0)+"除"+remember.get(3)+"等于"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,temp.get(0)+"除"+remember.get(3)+"等于"+in.get(1)+"分之"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
            }
            else{
                cc.add(remember.get(3));
                cc.add(1);
                in=mychu(temp,cc);
                if(in.get(1)==1){
                    speak.narmalspeak(MainActivity.this,temp.get(1)+"分之"+temp.get(0)+"除"+remember.get(3)+"等于"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,temp.get(1)+"分之"+temp.get(0)+"除"+remember.get(3)+"等于"+in.get(1)+"分之"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
            }

        }
        if(remember1.get(2)==6){
            x="("+x+"-"+String.valueOf(remember.get(3))+")";
            if(temp.get(1)==1){
                speak.narmalspeak(MainActivity.this,temp.get(0)+"减"+remember.get(3)+"等于"+(temp.get(0)-remember.get(3)));
                temp.set(0,temp.get(0)-remember.get(3));
            }
            else {
                List<Integer> cc = new ArrayList<>();
                List<Integer> in = new ArrayList<>();
                cc.add(remember.get(3));
                cc.add(1);
                in=mysub(temp,cc);
                speak.narmalspeak(MainActivity.this,temp.get(0)+"分之"+temp.get(1)+"减"+remember.get(3)+"等于"+in.get(1)+"分子"+in.get(0));
                temp.set(0,in.get(0));
                temp.set(1,in.get(1));

            }
        }
        if(remember1.get(2)==3){
            x="("+String.valueOf(remember.get(3))+"-"+x+")";

            if(temp.get(1)==1){
                speak.narmalspeak(MainActivity.this,remember.get(3)+"减"+temp.get(0)+"等于"+(remember.get(3)-temp.get(0)));
                temp.set(0,remember.get(3)-temp.get(0));
            }
            else {
                List<Integer> cc = new ArrayList<>();
                List<Integer> in = new ArrayList<>();
                cc.add(remember.get(3));
                cc.add(1);
                in=mysub(cc,temp);
                speak.narmalspeak(MainActivity.this,remember.get(3)+"减"+temp.get(1)+"分之"+temp.get(0)+"等于"+in.get(1)+"分子"+in.get(0));
                temp.set(0,in.get(0));
                temp.set(1,in.get(1));

            }
        }
        if(remember1.get(2)==4){
            x="("+String.valueOf(remember.get(3))+"/"+x+")";

            List<Integer> cc = new ArrayList<>();
            List<Integer> in = new ArrayList<>();
            if(temp.get(1)==1) {
                cc.add(remember.get(3));
                cc.add(1);
                in=mychu(cc,temp);
                if(in.get(1)==1){
                    speak.narmalspeak(MainActivity.this,remember.get(3)+"除"+temp.get(0)+"等于"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,remember.get(3)+"除"+temp.get(0)+"等于"+in.get(1)+"分之"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
            }
            else{
                cc.add(remember.get(3));
                cc.add(1);
                in=mychu(cc,temp);
                if(in.get(1)==1){
                    speak.narmalspeak(MainActivity.this,remember.get(3)+"除"+temp.get(1)+"分之"+temp.get(0)+"等于"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
                else{
                    speak.narmalspeak(MainActivity.this,remember.get(3)+"除"+temp.get(1)+"分之"+temp.get(0)+"等于"+in.get(1)+"分之"+in.get(0));
                    temp.set(0,in.get(0));
                    temp.set(1,in.get(1));
                }
            }

        }

    }



    private String mMenuName="";//功能菜单名称
    private String mEmptyArea="";//空白区域

    private String getItemName(View v) {
        if (v != null ) {
            TextView et = v.findViewById(R.id.text);
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
    private String getItemName1(View v) {
        if (v != null ) {
            TextView et = v.findViewById(R.id.text1);
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

    private void initView1() {
        mGridView = (GridView) this.findViewById(R.id.grid_view);
        List<ProvinceBean> provinceBeanList = new ArrayList<>();
        for (int i = 0; i < provinceNames.length; i++) {
            ProvinceBean provinceBean = new ProvinceBean();
            provinceBean.setName(provinceNames[i]);
            provinceBean.setColor(bgColor[i]);
            provinceBeanList.add(provinceBean);
        }
        mProvinceAdapter = new NumAdapter(this, provinceBeanList);
        mGridView.setAdapter(mProvinceAdapter);
    }

    private void initView3() {
        mGridView3 = (GridView) this.findViewById(R.id.grid_view3);
        List<ProvinceBean> provinceBeanList = new ArrayList<>();
            ProvinceBean provinceBean = new ProvinceBean();
            provinceBean.setName("下一题");
            provinceBean.setColor(bgColor[0]);
            provinceBeanList.add(provinceBean);
            ProvinceBean provinceBean1 = new ProvinceBean();
            provinceBean1.setName("重置");
            provinceBean1.setColor(bgColor[0]);
            provinceBeanList.add(provinceBean1);
            ProvinceBean provinceBean2 = new ProvinceBean();
            provinceBean2.setName("查看答案");
            provinceBean2.setColor(bgColor[0]);
            provinceBeanList.add(provinceBean2);
        mGird3 = new GirdView3Adapter(this, provinceBeanList);
        mGridView3.setAdapter(mGird3);
    }

    private void initView2() {
        mGridView1 = (GridView) this.findViewById(R.id.grid_view2);
        List<Bean> BeanList = new ArrayList<>();
        for (int i = 0; i < opName.length; i++) {
            Bean provinceBean = new Bean(images[i],opName[i],bgColor[i]);
            BeanList.add(provinceBean);
        }
        mOpAdapter= new OpAdapter(BeanList,this);
        mGridView1.setAdapter(mOpAdapter);
    }



    public void random(){
        while (true){
        int a=(int)(Math.random() * 10+1);
        int b=(int)(Math.random() * 10+1);
        int c=(int)(Math.random() * 10+1);
        int d=(int)(Math.random() * 10+1);
            Log.e(TAG, "random: "+a+","+b+","+c+","+d );
        if(check1(a,b,c,d)){
            provinceNames[0]=String.valueOf(a);
            provinceNames[1]=String.valueOf(b);
            provinceNames[2]=String.valueOf(c);
            provinceNames[3]=String.valueOf(d);

            provinceNames1[0]=String.valueOf(a);
            provinceNames1[1]=String.valueOf(b);
            provinceNames1[2]=String.valueOf(c);
            provinceNames1[3]=String.valueOf(d);

            break;
        }
        else continue;


    }
    }


    public static boolean check1(int a,int b,int c ,int d){
        int[] nums=new int[4];
        nums[0]=a;
        nums[1]=b;
        nums[2]=c;
        nums[3]=d;


        List<List<Integer>> res=new ArrayList<>();
        res= permute(nums);
        System.out.println(res);
        for(int i=0;i<res.size();i++){
            List<Integer> res1=new ArrayList<>();
            res1=res.get(i);
            remember=res1;
            if(check(res1.get(0),res1.get(1),res1.get(2),res1.get(3))) return true;
        }
        return false;

    }

    public static boolean check(int a, int b, int c, int d) {
        int[] nums = new int[4];
        nums[0] = a;
        nums[1] = b;
        nums[2] = c;
        nums[3] = d;

        List<List<Integer>> cc = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Integer> in = new ArrayList<>();
            in.add(nums[i]);
            in.add(1);
            cc.add(in);
        }

        List<Integer> in = new ArrayList<>();
        List<List<Integer>> in1 = new ArrayList<>();
        List<List<Integer>> rem = new ArrayList<>();
        in.add(nums[0]);
        in.add(1);
        in1.add(in);
        rem.add(new ArrayList<Integer>());
        if (caculate(rem,in1, cc, 1))
            return true;
        return false;
    }

    public static boolean caculate(List<List<Integer>> rem,List<List<Integer>> cc, List<List<Integer>> all, int step) {
        if (step >= 4)
            return false;
        List<Integer> in = new ArrayList<>();
        in = (List<Integer>) all.get(step);

        for (int i = 0; i < cc.size(); i++) {
            List<List<Integer>> cc1 = new ArrayList<>();
            List<Integer> in1 = new ArrayList<>();

            List<Integer> in2 = new ArrayList<>();
            List<Integer> rem1 = new ArrayList<>();
            List<List<Integer>> cc2 = new ArrayList<>();

            rem1 = rem.get(i);
            in2=new ArrayList<>(rem1);
            in2.add(1);
            cc2.add(in2);
            in2=new ArrayList<>(rem1);
            in2.add(2);
            cc2.add(in2);
            in2=new ArrayList<>(rem1);
            in2.add(3);
            cc2.add(in2);
            in2=new ArrayList<>(rem1);
            in2.add(4);
            cc2.add(in2);
            in2=new ArrayList<>(rem1);
            in2.add(5);
            cc2.add(in2);
            in2=new ArrayList<>(rem1);
            in2.add(6);
            cc2.add(in2);
            in1 = (List<Integer>) cc.get(i);
            cc1.add(mul(in, in1));
            cc1.add(myadd(in, in1));
            cc1.add(mysub(in, in1));
            cc1.add(mychu(in, in1));
            cc1.add(mychu(in1, in));
            cc1.add(mysub(in1, in));

            for (int j = 0; j < cc1.size(); j++) {
                in1 = (List<Integer>) cc1.get(j);
                if (in1.get(0) == 24 && in1.get(1) == 1 && step == 3){
                    remember1=cc2.get(j);
                    System.out.println(cc2.get(j));
                    System.out.println(remember);
                    Log.e("hh", "caculate: cc2.get(i)"+ remember1);
                    Log.e("hh" ,"caculate: cc2.get(i)"+ remember);
                    return true;}
                if (in1.get(0) == 0 || in1.get(1) == 0)
                { cc1.remove(j);
                    cc2.remove(j);}
            }


            if (caculate(cc2,cc1,all, step + 1)) {
                return true;
            }
        }
        return false;

    }

    public static List<Integer> mul(List<Integer> L1, List<Integer> L2) {
        List<Integer> re = new ArrayList<>();
        int i = (int) L1.get(0);
        int i1 = (int) L2.get(0);
        int j = (int) L1.get(1);
        int j1 = (int) L2.get(1);

        int i2 = i * i1;
        int j2 = j * j1;

        int t = gcd1(i2, j2);
        if (t != 1) {
            i2 = i2 / t;
            j2 = j2 / t;
        }
        re.add(i2);
        re.add(j2);
        return re;

    }

    public static List<Integer> myadd(List<Integer> L1, List<Integer> L2) {
        List<Integer> re = new ArrayList<>();
        int i = (int) L1.get(0);
        int i1 = (int) L2.get(0);
        int j = (int) L1.get(1);
        int j1 = (int) L2.get(1);

        int i2 = i * j1 + i1 * j;
        int j2 = j1 * j;

        int t = gcd1(i2, j2);
        if (t != 1) {
            i2 = i2 / t;
            j2 = j2 / t;
        }
        re.add(i2);
        re.add(j2);
        return re;

    }

    public static List<Integer> mysub(List<Integer> L1, List<Integer> L2) {
        List<Integer> re = new ArrayList<>();
        int i = (int) L1.get(0);
        int i1 = (int) L2.get(0);
        int j = (int) L1.get(1);
        int j1 = (int) L2.get(1);

        int i2 = i * j1 - i1 * j;
        int j2 = j1 * j;

        int t = gcd1(i2, j2);
        if (t != 1) {
            i2 = i2 / t;
            j2 = j2 / t;
        }
        re.add(i2);
        re.add(j2);
        return re;

    }

    public static List<Integer> mychu(List<Integer> L1, List<Integer> L2) {
        List<Integer> re = new ArrayList<>();
        int i = (int) L1.get(0);
        int i1 = (int) L2.get(0);
        int j = (int) L1.get(1);
        int j1 = (int) L2.get(1);

        int i2 = i * j1;
        int j2 = j * i1;

        int t = gcd1(i2, j2);
        if (t != 1) {
            i2 = i2 / t;
            j2 = j2 / t;
        }
        re.add(i2);
        re.add(j2);
        return re;

    }

    public static int gcd1(int a, int b) {

        int k = 0;
        if (b == 0)
            System.out.println("????");
        do {
            k = a % b;// 得到余数
            a = b;// 根据辗转相除法,把被除数赋给除数
            b = k;// 余数赋给被除数
        } while (k != 0);
        return a;// 返回被除数

    }

    public static List<List<Integer>> permute(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();
        int[] visited = new int[nums.length];
        backtrack(res, nums, new ArrayList<Integer>(), visited);
        return res;

    }

    private static void backtrack(List<List<Integer>> res, int[] nums, ArrayList<Integer> tmp, int[] visited) {
        if (tmp.size() == nums.length) {
            res.add(new ArrayList<>(tmp));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (visited[i] == 1) continue;
            visited[i] = 1;
            tmp.add(nums[i]);
            backtrack(res, nums, tmp, visited);
            visited[i] = 0;
            tmp.remove(tmp.size() - 1);
        }
    }

}


