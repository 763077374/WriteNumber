package com.forseek.writenumber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.forseek.writenumber.util.mCustomProgressDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;



/**
 * Created by li on 2016/10/14.
 */
public class ThreeActivity extends Activity {  //ThreeActivity类头部
    public mCustomProgressDialog mdialog;        //定义自定义对话框对象
    MediaPlayer mediaPlayer;    //定义音乐播放器对象
    private ImageView iv_frame;    // 定义显示写数字的ImageView控件
    int i = 1;                    // 图片展示到第几张标记
    float x1;                        // 屏幕按下时的X值
    float y1;                        // 屏幕按下时的y值
    float x2;                        // 屏幕离开时的X值
    float y2;                        // 屏幕离开时的y值
    float x3;                        // 移动中的坐标的X值
    float y3;                        // 移动中的坐标的y值
    int igvx;                        // 图片x坐标
    int igvy;                        // 图片y坐标
    int type = 0;                    // 是否可以书写标识 开关 1开启0关闭
    int widthPixels;                // 屏幕宽度
    int heightPixels;                // 屏幕高度
    float scaleWidth;                // 宽度的缩放比例
    float scaleHeight;            // 高度的缩放比例
    Timer touchTimer = null;        // 点击在虚拟按钮上后用于连续动作的计时器.
    Bitmap arrdown;                // Bitmap图像处理
    boolean typedialog = true;        // dialog对话框状态
    private LinearLayout linearLayout = null;    // LinearLayout线性布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {//创建的onCreate()方法头部
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);   //设置数字书写界面的布局文件
        //如果游戏主界面设置背景音乐为播放音乐状态
        if (MainActivity.isPlay == true) {
            PlayMusic();                            //调用播放音乐的方法
        }

        initView();                                    //创建并调用initView()方法
    }   //创建的onCreate()方法尾部

    public void OnYS(View v) {    // 创建演示按钮，单击事件方法头部
        if (mdialog == null) {    // 如果自定义对话框为空
            // 实例化自定义对话框，设置显示文字和动画文件
            mdialog = new mCustomProgressDialog(this, "演示中点击边缘取消", R.anim.frame3);
        }
        mdialog.show();        // 显示对话框
    }   // 创建演示按钮，单击事件方法尾部


    private void initView() {  //initView()方法头部
        // 获取显示写数字的ImageView组件
        iv_frame = (ImageView) findViewById(R.id.iv_frame);
        // 获取写数字区域的布局
        linearLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
        // 获取书写界面布局
        LinearLayout write_layout = (LinearLayout) findViewById(R.id.LinearLayout_number);
        // 设置书写界面布局背景
        write_layout.setBackgroundResource(R.drawable.bg3);
        // 获取屏幕宽度
        widthPixels = this.getResources().getDisplayMetrics().widthPixels;
        // 获取屏幕高度
        heightPixels = this.getResources().getDisplayMetrics().heightPixels;
        // 因为图片等资源是按1280*720来准备的，如果是其它分辨率，适应屏幕做准备
        scaleWidth = ((float) widthPixels / 720);
        scaleHeight = ((float) heightPixels / 1280);
        try {
            // 通过输入流打开第一张图片
            InputStream is = getResources().getAssets().open("on3_1.png");
            // 使用Bitmap解析第一张图片
            arrdown = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取布局的宽高信息
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_frame.getLayoutParams();
        // 获取图片缩放后宽度
        layoutParams.width = (int) (arrdown.getWidth() * scaleHeight);
        // 获取图片缩放后高度
        layoutParams.height = (int) (arrdown.getHeight() * scaleHeight);
        // 根据图片缩放后的宽高，设置iv_frame的宽高
        iv_frame.setLayoutParams(layoutParams);
        lodimagep(1);// 调用lodimagep()方法，进入页面后加载第一个图片
        linearLayout.setOnTouchListener(new View.OnTouchListener() {  //设置手势判断事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {       //手势按下判断的onTouch()方法
                switch (event.getAction()) {                           //获取行动方式头部
                    case MotionEvent.ACTION_DOWN:                    //手指按下事件
                        // 当手指按下的时候
                        x1 = event.getX();                              //获取手指按下的X坐标
                        y1 = event.getY();                              // 获取手指按下的Y坐标
                        igvx = iv_frame.getLeft();                    // 获取手指按下图片的X坐标
                        igvy = iv_frame.getTop();                     // 获取手指按下图片的Y坐标
                        // 判断当手指按下的坐标大于按下图片的坐标时，证明手指按住移动，此时开启书写
                        if (x1 >= igvx
                                && x1 <= igvx
                                + (int) (arrdown.getWidth() * scaleWidth)
                                / 5
                                && y1 >= igvy
                                + (int) (arrdown.getHeight() * scaleHeight)
                                / 4
                                - (int) (arrdown.getWidth() * scaleWidth)
                                / 16
                                && y1 <= igvy
                                + (int) (arrdown.getHeight() * scaleHeight)
                                / 4) {
                            type = 1;      // 开启书写
                            lodimagep(1);
                        } else {
                            type = 0;      // 否则关闭书写
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:           // 手势移动中判断
                        igvx = iv_frame.getLeft();          // 获取图片的X坐标
                        igvy = iv_frame.getTop();           // 获取图片的Y坐标
                        x2 = event.getX();                  // 获取移动中手指在屏幕X坐标的位置
                        y2 = event.getY();                  // 获取移动中手指在屏幕Y坐标的位置
                        // 下边 是根据比划 以及 手势 做图片的处理 滑动到不同位置 加载不同图片
                        if (type == 1) {
                            if (x2 >= igvx
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 4 * 2
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4 * 2
                                    && y2 >= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    / 4
                                    - (int) (arrdown.getHeight() * scaleHeight)
                                    / 16 * 2
                                    && y2 <= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    / 4
                                    - (int) (arrdown.getHeight() * scaleHeight)
                                    / 16 * 1) {
                                lodimagep(2);  // 调用lodimagep()方法，加载第二张显示图片
                            } else if (x2 >= igvx
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 4 * 3
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4 * 3
                                    && y2 >= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    / 4
                                    - (int) (arrdown.getHeight() * scaleHeight)
                                    / 16 * 3
                                    && y2 <= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    / 4
                                    - (int) (arrdown.getHeight() * scaleHeight)
                                    / 16 * 2) {
                                lodimagep(3);  // 调用lodimagep()方法，加载第三张显示图片
                            } else if (x2 >= igvx
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 4 * 4
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4 * 4
                                    && y2 >= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    / 4
                                    - (int) (arrdown.getHeight() * scaleHeight)
                                    / 16 * 4
                                    && y2 <= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    / 4
                                    - (int) (arrdown.getHeight() * scaleHeight)
                                    / 16 * 3) {
                                lodimagep(4);  // 调用lodimagep()方法，加载第四张显示图片
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth) / 2
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    && y2 >= igvy
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5) {
                                lodimagep(5);  // 调用lodimagep()方法，加载第五张显示图片
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 2
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 2
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15) {
                                lodimagep(6);  // 调用lodimagep()方法，加载第六张显示图片
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth) / 5
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 2
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 2) {
                                lodimagep(7);  // 调用lodimagep()方法，加载第七张显示图片
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 3
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 3
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 3
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 3) {
                                lodimagep(8);  // 调用lodimagep()方法，加载第八张显示图片
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 3 * 2
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 3 * 2
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 4
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 4) {
                                lodimagep(9);  // 调用lodimagep()方法，加载第九张显示图片
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 3 * 3
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 3 * 3
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 5
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 5) {
                                lodimagep(10);  // 调用lodimagep()方法，加载第十张显示图片
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth) / 2
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5

                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 6
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 6) {
                                lodimagep(11);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 5
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 5
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 7
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 7) {
                                lodimagep(12);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 5 * 2
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 5 * 2
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 8
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 8) {
                                lodimagep(13);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 5 * 3
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 5 * 3
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 9
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 9) {
                                lodimagep(14);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 5 * 4
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 2
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getWidth() * scaleWidth) / 2 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 5 * 4
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 10
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 10) {
                                lodimagep(15);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth) / 5
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 11
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 11) {
                                lodimagep(16);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 8
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 8
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 12
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 12) {
                                lodimagep(17);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 8 * 2
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 8 * 2
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 13
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 13) {
                                lodimagep(18);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 8 * 3
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 8 * 3
                                    && y2 >= igvy
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 14
                                    && y2 <= igvy
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    + ((int) (arrdown.getHeight() * scaleHeight) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 15 * 14) {
                                lodimagep(19);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 8 * 4
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 8 * 4
                                    && y2 >= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5

                                    && y2 <= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)) {
                                lodimagep(20);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 8 * 5
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 8 * 5
                                    && y2 >= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getHeight() * scaleHeight) / 4 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4
                                    && y2 <= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    - ((int) (arrdown.getHeight() * scaleHeight) / 4 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4) {
                                lodimagep(21);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 8 * 6
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 8 * 6
                                    && y2 >= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getHeight() * scaleHeight) / 4 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4 * 2
                                    && y2 <= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    - ((int) (arrdown.getHeight() * scaleHeight) / 4 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4 * 2) {
                                lodimagep(22);
                            } else if (x2 >= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5) / 8 * 7
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    - ((int) (arrdown.getWidth() * scaleWidth) - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 8 * 7
                                    && y2 >= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    - (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    - ((int) (arrdown.getHeight() * scaleHeight) / 4 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4 * 3
                                    && y2 <= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    - ((int) (arrdown.getHeight() * scaleHeight) / 4 - (int) (arrdown
                                    .getWidth() * scaleWidth) / 5)
                                    / 4 * 3) {
                                lodimagep(23);
                            } else if (x2 >= igvx
                                    && x2 <= igvx
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5
                                    && y2 >= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    / 4 * 3
                                    && y2 <= igvy
                                    + (int) (arrdown.getHeight() * scaleHeight)
                                    / 4
                                    * 3
                                    + (int) (arrdown.getWidth() * scaleWidth)
                                    / 5) {
                                lodimagep(24);  //加载最后一张图片时，将在lodimagep()方法中调用书写完成对话框
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:                    // 手势抬起判断
                        type = 0;                                // 手势关闭
                        // 当手指离开的时候
                        if (touchTimer != null) {                // 判断计时器是否为空
                            touchTimer.cancel();                // 中断计时器
                            touchTimer = null;                    // 设置计时器为空
                        }
                        touchTimer = new Timer();                // 初始化计时器
                        touchTimer.schedule(new TimerTask() {    // 开启时间计时器
                            @Override
                            public void run() {
                                Thread thread = new Thread(new Runnable() { //创建子线程
                                    @Override
                                    public void run() {
                                        // 创建Message用于发送消息
                                        Message message = new Message();
                                        message.what = 2;            // message消息为2
                                        // 发送消息给handler实现倒退显示图片
                                        mHandler.sendMessage(message);
                                    }
                                });
                                thread.start();                    // 开启线程
                            }
                        }, 300, 200);                              // 设置0.3秒后执行定时器，定时器每0.2秒发送一次

                }   // 获取行动方式尾部
                return true;
            }
        });


    }  //initView()方法尾部

    //递减显示帧图片的handler消息头部
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:            // 当接收到手势抬起子线程消息时
                    jlodimage();    // 调用资源图片倒退显示方法
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };   //递减显示帧图片的handler消息尾部

    private void jlodimage() {    //当手势抬起时数字资源图片倒退显示jlodimage()方法头部
        if (i == 25) {            // 如果当前图片位置等于25
        } else if (i < 25) {        // 否则如果当前图片小于25
            if (i > 1) {            // 如果当前图片大于1
                i--;
            } else if (i == 1) {    // 否则如果当前图片等于1
                i = 1;
                if (touchTimer != null) {    // 判断计时器是否为空
                    touchTimer.cancel();    // 中断计时器
                    touchTimer = null;        // 设置计时器为空
                }
            }
            String name = "on3_" + i;        // 图片的名称
            // 获取图片资源
            int imgid = getResources().getIdentifier(name, "drawable",
                    "com.mingrisoft.writenumber");
            // 给imageview设置图片
            iv_frame.setBackgroundResource(imgid);
        }
    }  //当手势抬起时数字资源图片倒退显示jlodimage()方法尾部


    private synchronized void lodimagep(int j) {        //lodimagep()方法头部
        i = j;                                // 当前图片位置
        if (i < 25) {                            // 如果当前图片位置小于25
            String name = "on3_" + i;            // 当前图片名称
            // 获取图片资源id
            int imgid = getResources().getIdentifier(name, "drawable", "com.mingrisoft.writenumber");
            iv_frame.setBackgroundResource(imgid);    // 设置图片
            i++;
        }
        if (j == 24) {                            // 如果当前图片位置为24
            if (typedialog) {                    // 没有对话框的情况下
                dialog();                    // 调用书写完成对话框方法
            }
        }
    }  //lodimagep()方法尾部

    protected void dialog() {        // 完成后提示对话框头部
        typedialog = false;                        // 修改对话框状态
        // 实例化对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(ThreeActivity.this);
        builder.setMessage("太棒了！书写完成！");        // 设置对话框文本信息
        builder.setTitle("提示");                    // 设置对话框标题
        //设置对话框完成按钮单击事件头部
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();                    // dialog消失
                typedialog = true;                    // 修改对话框状态
                finish();                            // 关闭当前页面
            }
        });       //对话框完成按钮单击事件尾部
        //设置对话框再来一次按钮单击事件头部
        builder.setNegativeButton("再来一次", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();                    // dialog消失
                typedialog = true;                // 修改对话框状态
                i = 1;
                lodimagep(i);                        // 调用加载图片方法中的第一张图片
            }
        });          //对话框再来一次按钮单击事件尾部
        builder.create().show();                        // 创建并显示对话框
    }    //完成后提示对话框尾部


    private void PlayMusic() {   //播放背景音乐方法
        //创建音乐播放器对象并加载播放音乐文件
        mediaPlayer = MediaPlayer.create(this, R.raw.music3);
        mediaPlayer.setLooping(true);    //设置音乐循环播放
        mediaPlayer.start();                //启动播放音乐
    }

    //该方法实现数字书写界面停止时，背景音乐停止
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {        //音乐播放器不为空时
            mediaPlayer.stop();            //停止音乐播放
        }
    }

    // 该方法实现数字书写界面销毁时，背景音乐停止并释放音乐资源
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {        //音乐播放器不为空时
            mediaPlayer.stop();                //停止音乐播放
            mediaPlayer.release();            //释放音乐资源
            mediaPlayer = null;                //设置音乐播放器为空
        }
    }


}  //ThreeActivity类尾部
