package com.forseek.writenumber;

import android.app.Activity;

import android.content.Intent;

import android.media.MediaPlayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class StartGameActivity extends Activity {  //StartGameActivity类头部
    static boolean isPlay = true;        //设置音乐播放状态变量
    MediaPlayer mediaPlayer;      //定义音乐播放器对象
    Button music_btn;         //定义控制音乐播放按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {   // onCreate()方法头部
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        //获取布局文件中控制背景音乐按钮
        music_btn = (Button) findViewById(R.id.btn_music);
        PlayMusic();     //调用播放音乐的方法
    }   // onCreate()方法尾部

    private void PlayMusic() {   //播放背景音乐方法
        //创建音乐播放器对象并加载播放音乐文件
        mediaPlayer = MediaPlayer.create(this, R.raw.start_music);
        mediaPlayer.setLooping(true);   //设置音乐循环播放
        mediaPlayer.start();    //启动播放音乐
    }


    public void OnPlay(View v) {  //单击事件 进入游戏主界面
        //当前界面跳转至游戏主界面
        startActivity(new Intent(StartGameActivity.this, MainActivity.class));
    }

    public void OnAbout(View v) {  //单击事件  进入关于界面
        //当前界面跳转至关于界面
        startActivity(new Intent(StartGameActivity.this, AboutActivity.class));
    }

    //单击事件  音乐播放时单击按钮停止音乐播放，音乐停止时单击按钮播放音乐
    public void OnMusic(View v) {
        if (isPlay == true) {   //如果音乐处于播放状态
            if (mediaPlayer != null) {  //音乐播放器不为空时
                mediaPlayer.stop();     //停止音乐播放
                //设置按钮为停止状态背景
                music_btn.setBackgroundResource(R.drawable.btn_music2);
                isPlay = false;  //设置音乐处于停止状态
            }

        } else {   //如果音乐处于停止状态
            PlayMusic();   //调用播放背景音乐方法，播放音乐
            //设置按钮为播放状态背景
            music_btn.setBackgroundResource(R.drawable.btn_music1);
            isPlay = true;   //设置音乐处于播放状态
        }
    }

    //该方法实现开始游戏界面停止时，背景音乐停止
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {   //音乐播放器不为空时
            mediaPlayer.stop();   //停止音乐播放
        }
    }

    // 该方法实现开始游戏界面销毁时，背景音乐停止并释放音乐资源
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {   //音乐播放器不为空时
            mediaPlayer.stop();    //停止音乐播放
            mediaPlayer.release();    //释放音乐资源
            mediaPlayer = null;    //设置音乐播放器为空
        }
    }

    //该方法实现从另一界面返回开始游戏界面时，根据音乐播放状态播放音乐
    protected void onRestart() {
        super.onRestart();
        if (isPlay == true) {    //如果音乐处于播放状态
            PlayMusic();      //调用播放背景音乐方法，播放音乐
        }
    }

}   //StartGameActivity类尾部








