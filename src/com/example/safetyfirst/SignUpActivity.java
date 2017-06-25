package com.example.safetyfirst;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	
	Button register;
	EditText f_name,l_name,uta_email,password;
	CheckBox chkStu;
	CheckBox chkPol;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		f_name = (EditText)findViewById(R.id.editTextFirstName);
		l_name = (EditText)findViewById(R.id.editTextLastName);
		uta_email = (EditText)findViewById(R.id.editTextUtaEmailId);
		password = (EditText)findViewById(R.id.editTextSign);
		chkStu = (CheckBox)findViewById(R.id.checkBox1);
		chkPol = (CheckBox)findViewById(R.id.checkBox2);
		chkStu.setOnClickListener(checkBoxStuClicked);
		chkPol.setOnClickListener(checkBoxPolClicked);
		register = (Button) findViewById(R.id.buttonRegister);
		register.setOnClickListener(registerHandler);
		
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
	View.OnClickListener registerHandler = new View.OnClickListener() {
	    public void onClick(View v) {
	      // it was the 1st button
	    	
	    	SharedPreferences prefs = getSharedPreferences("safetyfirstpreference", MODE_PRIVATE); 
			
			String deviceToken = (String)prefs.getString("deviceToken", "");
			
//			System.out.println("Device Token in sign up:"+deviceToken);
			
	    	System.out.println("Register Button clicked");
			String f_name_string = f_name.getText().toString();
			
			String l_name_string = l_name.getText().toString();
			
			String ut_email_string = uta_email.getText().toString();
			
			String password_string = password.getText().toString();
			
			String isChecked;
			if(chkStu.isChecked()){
				isChecked="1";
			}else{
				isChecked="0";
			}
			
			
			if(!f_name_string.isEmpty() && !l_name_string.isEmpty() && !ut_email_string.isEmpty() && !password_string.isEmpty()){
//				doSignUp(f_name_string,l_name_string,ut_email_string,password_string);
				new SignUp(SignUpActivity.this).execute(f_name_string,l_name_string,ut_email_string,password_string,deviceToken,isChecked);
				
			}else{
				entervaluesinfields();
			}
	    }
	    
	  };
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}
	public void showSuccessfulRegistration(){
		System.out.println("golmal");
		SignUpActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				Toast.makeText(getApplicationContext(),"Successful Registration",Toast.LENGTH_SHORT).show();
			}
		});
		Intent i = new Intent(
				"com.example.safetyfirst.LOGINACTIVITY");
		startActivity(i);
	}
	public void userAlreadyExists(){
		SignUpActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				Toast.makeText(getApplicationContext(),"User already exist! Try login.",Toast.LENGTH_SHORT).show();
			}
		});
	}
	public void entervaluesinfields(){
		SignUpActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				Toast.makeText(getApplicationContext(),"Enter values in empty fields",Toast.LENGTH_SHORT).show();
			}
		});
	}
	public void invalideEmail(){
		SignUpActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
