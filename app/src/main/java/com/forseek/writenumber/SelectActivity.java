package com.forseek.writenumber;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class SelectActivity extends Activity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        if (MainActivity.isPlay==true){
            PlayMusic();
        }
    }
    private void PlayMusic(){
        mediaPlayer = MediaPlayer.create(this,R.raw.number_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    protected void onStop(){
        super.onStop();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
    protected void onRestart(){
        super.onRestart();
        if (MainActivity.isPlay == true){
            PlayMusic();
        }
    }
    public  void OnOne(View v){
        startActivity(new Intent(SelectActivity.this,OneActivity.class));
    }
}
