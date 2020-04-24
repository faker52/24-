package com.example.twenfour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sensingai.commonlib.BaseApplication;

public class SocketPlay extends AppCompatActivity {

    private GridView mGridView;
    private GridView mGridView1,mGridView3;
    private NumAdapter mProvinceAdapter;
    private GirdView3Adapter mGird3;
    private OpAdapter mOpAdapter;
    String TAG="hh";
    int Turn=0;
    int Operator=-1;
    int Count=0;
    List<Integer> nums0=new ArrayList<>();
    List<Integer> nums1=new ArrayList<>();
    List<Integer> nums2=new ArrayList<>();
    TextView textView;
    int LastPosition;
    Baiduspeak speak=new Baiduspeak();
    private String[] provinceNames = new String[4];
    private String[] provinceNames1=new String[4];
    private int[] bgColor = new int[]{R.color.bgc, R.color.bgc, R.color.bgc, R.color.bgc};
    private String[] opName=new String[]{"加","减","乘","除"};
    private int[] images=new int[]{R.drawable.add,R.drawable.sub,R.drawable.mul,R.drawable.chu};

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "";
    private String content1 = "";
    int Finishnum=0;
    int OFinishnum=-2;
    boolean Isyou=false;
    String housenum;


    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 0x123) {
                textView.setText(content);
                content="";
            }

            else if(msg.what== 0x124){
                if(Isyou==false){OFinishnum++;}
                String[] split1=content1.split(",");
                random(split1);
                initView1();
                textView.setText("你的得分"+Finishnum+":"+"对方得分"+OFinishnum);
                Count=0;
                Isyou=false;
            }
            else if(msg.what==0x125){
                if (socket.isConnected()) {

                    if (!socket.isOutputShutdown()) {

                        out.println("请出题");
                    }
                }

                Finishnum=0;
                OFinishnum=-4;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    out.println("bye"+"#"+housenum);
                    this.finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.gird_view);
        textView=(TextView) findViewById(R.id.timetext);

        housenum=intent.getStringExtra("housenum");
        new Thread() {
            public void run() {
                try {

                    socket= ((MySocket)getApplication()).getSocket();
                    out=new PrintWriter(socket.getOutputStream(),true);
                    Thread t1 = new Thread(new SocketPlay.clientru());
                    t1.start();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        provinceNames[0]="";
        provinceNames[1]="";
        provinceNames[2]="";
        provinceNames[3]="";
        initView1();
        initView2();
        initView3();



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }




        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Operator==-1) Turn=0;
                if(provinceNames[position]==""){
                    speak.narmalspeak(SocketPlay.this,"此区域无数字");
                }
                else if(Turn==0){
                    speak.narmalspeak(SocketPlay.this,provinceNames[position]);
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
                        speak.narmalspeak(SocketPlay.this,"不可以使用相同的数字");
                    }
                    else if(provinceNames[position]==""){
                        speak.narmalspeak(SocketPlay.this,"此区域无数字");
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

                        if (Operator == 0) nums2 = myadd(nums0, nums1);
                        if (Operator == 1) nums2 = mysub(nums0, nums1);
                        if (Operator == 2) nums2 = mul(nums0, nums1);
                        if (Operator == 3 && nums1.get(0) != 0) nums2 = mychu(nums0, nums1);
                        if (nums1.get(0) == 0 && Operator == 3)
                            speak.narmalspeak(SocketPlay.this, "操作错误");
                        if (nums2.get(1) == 1) {
                            provinceNames[position] = String.valueOf(nums2.get(0));
                            provinceNames[LastPosition] = "";
                            speak.narmalspeak(SocketPlay.this,"等于"+provinceNames[position]);
                            Count++;
                        } else {
                            provinceNames[position] = (String.valueOf(nums2.get(0)) + "/" + String.valueOf(nums2.get(1)));
                            provinceNames[LastPosition] = "";
                            speak.narmalspeak(SocketPlay.this,"等于"+provinceNames[position]);
                            Count++;

                        }

                        if(Count==3&&nums2.get(0)==24&&nums2.get(1)==1){
                            speak.narmalspeak(SocketPlay.this,"回答正确");
                            Count=0;
                            Isyou=true;
                            if (socket.isConnected()) {
                                if (!socket.isOutputShutdown()) {
                                    out.println("对方获胜");
                                    Finishnum++;
                                }
                            }

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

                    if (socket.isConnected()) {
                        if (!socket.isOutputShutdown()) {
                            out.println("bye"+"#"+housenum);
                            Finishnum++;
                        }
                    }
                    Finishnum=0;
                    OFinishnum=0;
                    Intent grade=new Intent(SocketPlay.this,GradeDialog.class);
                    grade.putExtra("time","Loser");
                    SocketPlay.this.finish();
                    startActivity(grade);

                }
                if(position==1){
                    provinceNames[0]=provinceNames1[0];
                    provinceNames[1]=provinceNames1[1];
                    provinceNames[2]=provinceNames1[2];
                    provinceNames[3]=provinceNames1[3];
                    Count=0;
                    Operator=-1;
                    initView1();
                    Turn=0;
                    speak.narmalspeak(SocketPlay.this,"已重置");
                }
            }

        });
        mGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Operator=position;
                speak.narmalspeak(SocketPlay.this,opName[Operator]);
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



    class clientru implements Runnable
    {
        @Override
        public void run() {


            String msg="";
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(true)
                {
                    if((msg=in.readLine()) != null)
                    {
                        Log.e(TAG, "接受的信息+play界面 "+msg);
                        if(msg.substring(0,3).equals("123")){
                            content1=msg;
                            handler.sendEmptyMessage(0x124);
                        }
                        else if(msg.equals("创建成功")){
                            handler.sendEmptyMessage(0x125);
                        }
                        else{content=msg+"\n";
                        handler.sendEmptyMessage(0x123);}

                    }
                }
            }catch(Exception e){e.printStackTrace();}
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
        provinceBean.setName("认输");
        provinceBean.setColor(bgColor[0]);
        provinceBeanList.add(provinceBean);
        ProvinceBean provinceBean1 = new ProvinceBean();
        provinceBean1.setName("重置");
        provinceBean1.setColor(bgColor[0]);
        provinceBeanList.add(provinceBean1);
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



    public void random(String[] cc){
                provinceNames[0]=cc[1];
                provinceNames[1]=cc[2];
                provinceNames[2]=cc[3];
                provinceNames[3]=cc[4];
                provinceNames1[0]=cc[1];
                provinceNames1[1]=cc[2];
                provinceNames1[2]=cc[3];
                provinceNames1[3]=cc[4];


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



    }



