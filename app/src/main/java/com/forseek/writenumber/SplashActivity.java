package com.forseek.writenumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		new Thread(new Taskk()).start();
	}
	private class Taskk implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
				loadMainUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void loadMainUI() {
		Intent intent = new Intent(SplashActivity.this, AndroidActivity.class);
		startActivity(intent);
		finish();
	}
}