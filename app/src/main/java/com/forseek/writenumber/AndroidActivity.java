package com.forseek.writenumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/*
 * 程序首页面
 */
public class AndroidActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //点击事件 进入不同的数字页面
    public void OnOne(View v) {
        //页面跳转
        startActivity(new Intent(AndroidActivity.this, OneActivity.class));
    }

    public void OnTwo(View v) {
        startActivity(new Intent(AndroidActivity.this, TwoActivity.class));
    }

    public void OnThree(View v) {
        startActivity(new Intent(AndroidActivity.this, ThreeActivity.class));
    }

    public void OnFour(View v) {
        startActivity(new Intent(AndroidActivity.this, FourActivity.class));
    }

    public void OnFive(View v) {
        startActivity(new Intent(AndroidActivity.this, FiveActivity.class));
    }

    public void OnSix(View v) {
        startActivity(new Intent(AndroidActivity.this, SixActivity.class));
    }

    public void OnSeven(View v) {
        startActivity(new Intent(AndroidActivity.this, SevenActivity.class));
    }

    public void OnEight(View v) {
        startActivity(new Intent(AndroidActivity.this, EightActivity.class));
    }

    public void OnNine(View v) {
        startActivity(new Intent(AndroidActivity.this, NineActivity.class));
    }

    public void OnZero(View v) {
        startActivity(new Intent(AndroidActivity.this, ZeroActivity.class));
    }

    //点击事件 进入关于页面
    public void OnAbout(View v) {
        startActivity(new Intent(AndroidActivity.this, AboutActivity.class));
    }
}