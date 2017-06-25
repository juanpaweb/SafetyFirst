package com.example.safetyfirst;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {
	SQLiteDatabase myDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		
		SplashActivity.this.startService(new Intent(
				SplashActivity.this, SafetyInBackgroundService.class));

		// creating sqlite rb to store 3 emergency contacts
		// myDB = this.openOrCreateDatabase("safetyfirst",
		// android.content.Context.MODE_PRIVATE, null);
		// DBAccess.createDB(myDB);

		/*
		 * Creates a thread to display the splash screen for 2 seconds and
		 * redirects to Ride preferences screen
		 */
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
		            
					
					SharedPreferences prefs = getSharedPreferences("safetyfirstpreference", MODE_PRIVATE); 
					
					String restoredText = (String)prefs.getString("login", "");
			        
					String data = "true";
					if (data.compareTo(restoredText)==0) {
						System.out.println("google is here");
						
						Intent i = new Intent("com.example.safetyfirst.TABACTIONBARACTIVITY");
						startActivity(i);
					}else{
						Intent i = new Intent(
								"com.example.safetyfirst.LOGINACTIVITY");
						startActivity(i);
					}
					
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

}
