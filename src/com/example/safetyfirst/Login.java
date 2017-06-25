package com.example.safetyfirst;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;
import android.os.AsyncTask;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class Login  extends AsyncTask<String,Void,String>{
	private LoginActivity signupactivityobj;
	public Login(LoginActivity obj){
		this.signupactivityobj=obj;
	}
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		String utaemail = (String)arg0[0];
		String password = (String)arg0[1];
		String isstudent = (String)arg0[2];
		String deviceToken = (String)arg0[3];
		
		if(validateEmail(utaemail)){
			
			try{
				String singuplink = "http://omega.uta.edu/~sxc3409/SafetyFirst/login.php";
				URL url = new URL(singuplink);
				
				String data  = URLEncoder.encode("email", "UTF-8") 
				+ "=" + URLEncoder.encode(utaemail, "UTF-8");
				data += "&" + URLEncoder.encode("password", "UTF-8") 
				+ "=" + URLEncoder.encode(password, "UTF-8");
				data += "&" + URLEncoder.encode("isstudent", "UTF-8") 
				+ "=" + URLEncoder.encode(isstudent, "UTF-8");
				data += "&" + URLEncoder.encode("deviceToken", "UTF-8") 
				+ "=" + URLEncoder.encode(deviceToken, "UTF-8");
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
	            wr.write( data ); 
	            wr.flush(); 
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            // Read Server Response
	            while((line = reader.readLine()) != null)
	            {
	               sb.append(line);
	               break;
	            }
	            String convertedSb = sb.toString();
//	            System.out.println("srinidhi="+sb);
	            JSONObject reader1 = new JSONObject(convertedSb);
//	            System.out.println(sb.toString());
	            if(reader1.get("error").toString()=="false"){
	            	JSONObject userobj = reader1.getJSONObject("user");
	            	
	            	String useremail=(String)userobj.get("email");
	            	String usrefname = (String)userobj.get("fname");
	            	String userlname = (String)userobj.get("lname");
	            	signupactivityobj.storeInSharedPreference(useremail, usrefname, userlname, isstudent);
	            	signupactivityobj.successfulLogin();
	            	
	            }else{
	            	signupactivityobj.invalidCredential();
	            }
	            
	            
			}catch(Exception e){
				System.out.println("Exception:"+e);
			}
				 
		}else{
			
			signupactivityobj.invalideEmail();
		}
		return null;
	}
	private boolean validateEmail(String email){
		final String coDomain = "mavs.uta.edu";
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ Pattern.quote(coDomain) + "$";
		return email.matches(EMAIL_PATTERN);
	}
	
}