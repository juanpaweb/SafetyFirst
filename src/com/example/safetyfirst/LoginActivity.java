package com.example.safetyfirst;

import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class LoginActivity extends Activity implements OnClickListener {
	Intent i;
	EditText userName, password;
	Button login, forgotPassword, signUp;

	CheckBox chkStu;
	CheckBox chkPol;
	int isStudent1= 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		userName = (EditText) findViewById(R.id.editTextUtaID);
		password = (EditText) findViewById(R.id.editTextPassword);

		login = (Button) findViewById(R.id.buttonLogin);
		forgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
		signUp = (Button) findViewById(R.id.buttonSignUp);

		login.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);

		chkStu = (CheckBox) findViewById(R.id.checkBox1);
		chkPol = (CheckBox) findViewById(R.id.checkBox2);
		chkStu.setOnClickListener(checkBoxStuClicked);
		chkPol.setOnClickListener(checkBoxPolClicked);
		
		System.out.println("In login activity");
		
		Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
	}

	View.OnClickListener checkBoxStuClicked = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			chkPol.setChecked(false);
			chkStu.setChecked(true);
		}
	};
	View.OnClickListener checkBoxPolClicked = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			chkPol.setChecked(true);
			chkStu.setChecked(false);
		}
	};

	
		public void storeInSharedPreference(String email,String fname,String lname,String isstudent){
		SharedPreferences.Editor editor = getSharedPreferences("safetyfirstpreference", MODE_PRIVATE).edit();
    	
   	 	editor.putString("email", email);
   	 	editor.putString("fname", fname);
   	 	editor.putString("lname", lname);
   	 	editor.putString("isstudent", isstudent);
   	 	editor.putString("login", "true");
   	 	editor.commit();
	}

	@SuppressLint("ShowToast")
	private void checkLogin(String email, String password) {
		if (validateEmail(email)) {
			// RequestParams param = new RequestParams();
			// param.put("email", email);
			// param.put("password", password);
			// invokeLoginWS(param);
		}
	}

	// public void invokeLoginWS(RequestParams param){
	// AsyncHttpClient client = new AsyncHttpClient();
	// client.get("http://192.168.2.2:9999/useraccount/login/dologin",param ,new
	// AsyncHttpResponseHandler(){
	// public void onSuccess(String response){
	// try{
	// JSONObject obj = new JSONObject(response);
	// if(obj["ERROR"]==true){
	// //Pop out an dialogue with text = " Login failed"
	// }else if(obj["ERROR"]==true){
	// //successful login
	// //navigate to home activity;
	// }
	// }catch(JSONException e){
	// System.out.println("JSONException e:"+e);
	// }
	// }
	//
	// });
	//
	// }

	private boolean validateEmail(String email) {
		final String coDomain = "mavs.uta.edu";
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ Pattern.quote(coDomain) + "$";
		return email.matches(EMAIL_PATTERN);
	}

	public void invalideEmail() {
//		Toast.makeText(getApplicationContext(),
//				"Invalid Email",
//				Toast.LENGTH_SHORT).show();
		
		LoginActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
			}
		});

	}

	public void successfulLogin() {
		if(isStudent1==1){
			System.out.println("Meet");
			i = new Intent("com.example.safetyfirst.TABACTIONBARACTIVITY");
			System.out.println("coming to succesfullogin");
			startActivity(i);
		}else{
			System.out.println("srinidhi");
			i = new Intent("com.example.safetyfirst.POLICEHOMEACTIVITY");
			System.out.println("coming to succesfullogin");
			startActivity(i);
		}

	}

	public void invalidCredential() {
		LoginActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
			}
		});
		
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonLogin:
			
			SharedPreferences prefs = getSharedPreferences("safetyfirstpreference", MODE_PRIVATE); 
			
			String deviceToken = (String)prefs.getString("deviceToken", "");
			

			EditText uta_id_ET = (EditText) findViewById(R.id.editTextUtaID);
			String email_id_et = uta_id_ET.getText().toString();
			
			//System.out.println(email_id_et);

			EditText uta_pass_ET = (EditText) findViewById(R.id.editTextPassword);
			String pass_uta_et = uta_pass_ET.getText().toString();
			//System.out.println(pass_uta_et);
			if(email_id_et.isEmpty()){
				Toast.makeText(getApplicationContext(),
						"Enter Username!",
						Toast.LENGTH_SHORT).show();
			}
			else if(pass_uta_et.isEmpty()){
				Toast.makeText(getApplicationContext(),
						"Enter the password!",
						Toast.LENGTH_SHORT).show();
			}
			String isstudent;
			if (chkStu.isChecked()) {
				isstudent = "1";
				isStudent1=1;
			} else {
				isStudent1=0;
				isstudent = "0";
			}

			if (!email_id_et.isEmpty() && !pass_uta_et.isEmpty()) {
				new Login(LoginActivity.this).execute(email_id_et, pass_uta_et,isstudent,deviceToken);
				
			}
			break;

		case R.id.buttonForgotPassword:
			i = new Intent("com.example.safetyfirst.FORGOTPASSWORDACTIVITY");
			startActivity(i);
			break;

		case R.id.buttonSignUp:
			Toast.makeText(getApplicationContext(), "Good job signing up",
					Toast.LENGTH_LONG);
			i = new Intent("com.example.safetyfirst.SIGNUPACTIVITY");
			startActivity(i);
			break;
		}

	}
}
