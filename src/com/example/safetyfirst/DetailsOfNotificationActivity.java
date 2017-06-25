package com.example.safetyfirst;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsOfNotificationActivity extends Activity {
	TextView textDetailsID, textDetailsSubject, textDetailsTimeStamp, textDetailsBody;
	String stringDetailsID, stringDetailsSubject, stringDetailsTimeStamp, stringDetailsBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_of_notification);		

		Log.i("DetailsOfNotificationActivity - ", "I am here. ");
		
//		textDetailsID = (TextView) findViewById(R.id.textDetailsID);
		textDetailsSubject = (TextView) findViewById(R.id.textDetailsSubject);
		textDetailsTimeStamp = (TextView) findViewById(R.id.textDetailsTimeStamp);
		textDetailsBody = (TextView) findViewById(R.id.textDetailsBody);
		
//		stringDetailsID = getIntent().getStringExtra("textDetailsID");
		stringDetailsSubject = getIntent().getStringExtra("textDetailsSubject");
		stringDetailsTimeStamp = getIntent().getStringExtra("textDetailsTimeStamp");
		stringDetailsBody = getIntent().getStringExtra("textDetailsBody");
		
//		textDetailsID.setText();
		textDetailsSubject.setText(stringDetailsSubject);
		textDetailsTimeStamp.setText(stringDetailsTimeStamp);
		textDetailsBody.setText(stringDetailsBody);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			SharedPreferences settings = getSharedPreferences("safetyfirstpreference", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
            finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
