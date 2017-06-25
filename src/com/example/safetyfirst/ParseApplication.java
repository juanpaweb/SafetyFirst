package com.example.safetyfirst;

import org.json.JSONObject;

import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

import android.app.Application;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Button;

public class ParseApplication extends Application{
	
	
	Button send;
	JSONObject data = new JSONObject();
	String value = "Student";
	@SuppressWarnings("deprecation")
	public void onCreate(){
		super.onCreate();
		
		Parse.initialize(this, "3yQn8owdssFweahpwExq7aEVzOzChwiAZOoHqr6C", "5rgMHT5SMUW2RB4VBba2pURZAoEWf1rFtbZyNWmL" );
		/*ParseUser.enableAutomaticUser();
		ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser());
		*/
		PushService.setDefaultPushCallback(this, DetailsOfNotificationActivity.class);
		ParseInstallation.getCurrentInstallation().add("channels", value);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
	
}
