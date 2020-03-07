package com.example.twenfour;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sensingai.commonlib.BaseApplication;

public class SpeedActivity extends AppCompatActivity {

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
    Timer timer;
    TimerTask timerTask;
    DecimalFormat decimalFormat;
    int time=0;
    int LastPosition;
    int pracount=0;

    Baiduspeak speak=new Baiduspeak();
    private String[] provinceNames = new String[4];
    private String[] provinceNames1=new String[4];
    private int[] bgColor = new int[]{R.color.bgc, R.color.bgc, R.color.bgc, R.color.bgc};
    private String[] opName=new String[]{"加","减","乘","除"};
    private int[] images=new int[]{R.drawable.add,R.drawable.sub,R.drawable.mul,R.drawable.chu};
    Intent grade;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        pracount=0;
        setContentView(R.layout.gird_view);
        textView=(TextView) findViewById(R.id.timetext);
        random();
        initView1();
        initView2();
        initView3();
        statTime();



        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Operator==-1) Turn=0;
                if(provinceNames[position]==""){
                    speak.narmalspeak(SpeedActivity.this,"此区域无数字");
                }
                else if(Turn==0){
                    speak.narmalspeak(SpeedActivity.this,provinceNames[position]);
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
                        speak.narmalspeak(SpeedActivity.this,"不可以使用相同的数字");
                    }
                    else if(provinceNames[position]==""){
                        speak.narmalspeak(SpeedActivity.this,"此区域无数字");
                    }
                    else {
                        speak.narmalspeak(SpeedActivity.this,provinceNames[position]);
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
                            speak.narmalspeak(SpeedActivity.this, "操作错误");
                        if (nums2.get(1) == 1) {
                            provinceNames[LastPosition] = String.valueOf(nums2.get(0));
                            provinceNames[position] = "";
                            Count++;
                        } else {
                            provinceNames[LastPosition] = (String.valueOf(nums2.get(0)) + "/" + String.valueOf(nums2.get(1)));
                            provinceNames[position] = "";
                            Count++;
                        }
                        Log.e(TAG, "onItemClick: nums2"+nums2 );
                        if(Count==3&&nums2.get(0)==24&&nums2.get(1)==1){
                            speak.narmalspeak(SpeedActivity.this,"回答正确");
                            pracount++;
                            if(pracount==5){
                                String x=formatTime(time);
                                grade=new Intent(SpeedActivity.this,GradeDialog.class);
                                grade.putExtra("time",String.valueOf(time));
                                SpeedActivity.this.finish();
                                startActivity(grade);
                                Toast.makeText(SpeedActivity.this,x,Toast.LENGTH_LONG).show();
                            }
                            Log.e(TAG, "onItemClick: nums2"+nums2 );
                            random();
                            initView1();
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
                    speak.narmalspeak(SpeedActivity.this,"已切换下一题");
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
                    speak.narmalspeak(SpeedActivity.this,"已重置");
                }
            }

        });
        mGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Operator=position;
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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    freshTime();
                    break;
                default:
                    break;
            }
        }
    };

    private void statTime(){
        timer = new Timer();
        timerTask = new TimerTask(){
            @Override
            public void run() {

                Message msg = new Message();
                msg.what = 10;
                //发送
                handler.sendMessage(msg);

            }
        };
        if(timer != null){
            timer.schedule(timerTask, 1000, 1000);//如果时间过长，间隔时间会不准
        }
    }
    private void stopTime(){
        if(timer!= null){
            timer.cancel();
        }
    }



    private void freshTime() {
        time++;
        textView.setText(formatTime(time));
    }  /**
     * 将秒转化为 HH:mm:ss 的格式
     * *     * @param time 秒
     * * @return     */
    private String formatTime(int time) {
        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat("00");
        }
        String hh = decimalFormat.format(time / 3600);
        String mm = decimalFormat.format(time % 3600 / 60);
        String ss = decimalFormat.format(time % 60);
        return hh + ":" + mm + ":" + ss;
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

        in.add(nums[0]);
        in.add(1);
        in1.add(in);

        if (caculate(in1, cc, 1))
            return true;
        return false;
    }

    public static boolean caculate(List<List<Integer>> cc, List<List<Integer>> all, int step) {
        if (step >= 4)
            return false;
        List<Integer> in = new ArrayList<>();
        in = (List<Integer>) all.get(step);

        for (int i = 0; i < cc.size(); i++) {
            List<List<Integer>> cc1 = new ArrayList<>();
            List<Integer> in1 = new ArrayList<>();


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

                    return true;}

                if (in1.get(0) == 0 || in1.get(1) == 0){
                    cc1.remove(j);

                }
            }


            if (caculate(cc1, all, step + 1)) {
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


