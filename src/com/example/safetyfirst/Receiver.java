package com.example.safetyfirst;


import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.parse.ParsePushBroadcastReceiver;



public class Receiver extends ParsePushBroadcastReceiver {
	@Override
	public void onPushOpen(Context context, Intent intent) {
		String PARSE="" ;
		String subject="";
		String Time_stamp="";
		String Details="";
		try {
			  System.out.println("Before Json data");
		      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
		      subject=json.getString("alert");
		      Details = json.getString("subject");
		      Time_stamp=json.getString("timestamp");
		      //Details=json.getString(Details);
		      System.out.println("timestamp:"+Time_stamp);
		 } catch (JSONException e) {
		      Log.d(PARSE, "JSONException: " + e.getMessage());
		    }
		Intent i = new Intent(context,DetailsOfNotificationActivity.class);
		i.putExtras(intent.getExtras());
		i.putExtra("textDetailsSubject", subject);
		i.putExtra("textDetailsTimeStamp", Time_stamp);
		i.putExtra("textDetailsBody", Details);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		
	}
}