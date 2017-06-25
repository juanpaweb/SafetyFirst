package com.example.safetyfirst;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends Activity implements OnClickListener {
	SQLiteDatabase myDB;
	private static final int PICK_CONTACT = 1;
	Button buttonAutoSelect, buttonSave;
	EditText editTextName, editTextPN;

	String contactNum, contactName, contactPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		editTextName = (EditText) findViewById(R.id.editTextName);
		editTextPN = (EditText) findViewById(R.id.editTextPN);

		buttonAutoSelect = (Button) findViewById(R.id.buttonAutoSelect);
		buttonSave = (Button) findViewById(R.id.buttonSave);

		buttonSave.setOnClickListener(this);
		buttonAutoSelect.setOnClickListener(this);
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
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.buttonAutoSelect:
			intent = new Intent(Intent.ACTION_PICK);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			startActivityForResult(intent, PICK_CONTACT);
			break;

		case R.id.buttonSave:
			myDB = openOrCreateDatabase("safetyfirst", MODE_PRIVATE, null);
			String contactNum = getIntent().getStringExtra("contactNum");
			String contactName = editTextName.getText().toString();
			String contactPhone = editTextPN.getText().toString();
			DBAccess.insertDB(myDB, contactNum, contactName, contactPhone);
			
			intent = new Intent(ContactActivity.this, TabActionBarActivity.class);
			startActivity(intent);
			finish();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		try {

			if (requestCode == PICK_CONTACT) {
				Cursor cursor = getApplicationContext().getContentResolver()
						.query(intent.getData(), null, null, null, null);
				cursor.moveToNext();
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cursor
						.getString(cursor
								.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
				String phone = cursor
						.getString(cursor
								.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				String phoneNumber = "test";

				if (phone.equalsIgnoreCase("1"))
					phone = "true";
				else
					phone = "false";

				if (Boolean.parseBoolean(phone)) {
					Cursor phones = getApplicationContext()
							.getContentResolver()
							.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
									null,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID
											+ " = " + contactId, null, null);
					while (phones.moveToNext()) {
						phoneNumber = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					phones.close();
				}
				Toast.makeText(getApplicationContext(),
						"You selected Contact name " + name, Toast.LENGTH_LONG)
						.show();

				editTextName.setText(name);
				editTextPN.setText(phoneNumber);

			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Contact not found",
					Toast.LENGTH_LONG).show();
			Log.e("contact list", e.getMessage());
		}
	}
}
