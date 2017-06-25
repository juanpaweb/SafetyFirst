package com.example.safetyfirst;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SOSFragment extends Fragment implements OnClickListener {
	SQLiteDatabase myDB;
	ImageButton contact1, contact2, contact3;
	TextView textViewContact1, textViewContact2, textViewContact3;
	Button sendSos, stopSos;
	Intent intent;
	LocationManager locationManager;
	LocationListener locationListener;
	LocationListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_tab_sos, container, false);

		contact1 = (ImageButton) v.findViewById(R.id.imageButtonContact1);
		contact2 = (ImageButton) v.findViewById(R.id.imageButtonContact2);
		contact3 = (ImageButton) v.findViewById(R.id.imageButtonContact3);
		textViewContact1 = (TextView) v.findViewById(R.id.textViewContact1);
		textViewContact2 = (TextView) v.findViewById(R.id.textViewContact2);
		textViewContact3 = (TextView) v.findViewById(R.id.textViewContact3);
		sendSos = (Button) v.findViewById(R.id.buttonSOS);
		stopSos = (Button) v.findViewById(R.id.buttonStopSos);
		contact1.setOnClickListener(this);
		contact2.setOnClickListener(this);
		contact3.setOnClickListener(this);
		sendSos.setOnClickListener(this);
		stopSos.setOnClickListener(this);

		 KeyguardManager guard = (KeyguardManager) getActivity()
		 .getSystemService(getActivity().KEYGUARD_SERVICE);
		 KeyguardLock keyGuardLock = guard
		 .newKeyguardLock("KeyguardLockWrapper");
		 keyGuardLock.disableKeyguard();

		return v;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageButtonContact1:
			intent = new Intent(getActivity(), ContactActivity.class);
			intent.putExtra("contactNum", "1");
			startActivity(intent);

			break;

		case R.id.imageButtonContact2:
			intent = new Intent(getActivity(), ContactActivity.class);
			intent.putExtra("contactNum", "2");
			startActivity(intent);

			break;

		case R.id.imageButtonContact3:
			intent = new Intent(getActivity(), ContactActivity.class);
			intent.putExtra("contactNum", "3");
			startActivity(intent);

			break;

		 case R.id.buttonSOS:
		 listener = new SMSAsyncTask();
		 Log.i("SOS", "1");
		 locationManager = (LocationManager) getActivity().getSystemService(
		 Context.LOCATION_SERVICE);
		 Log.i("SOS", "2");
		 locationManager.requestLocationUpdates(
		 LocationManager.GPS_PROVIDER, 15000, 1, listener);
		 Log.i("SOS", "3");
		 // new SMSAsyncTask().execute();
		 StringBuilder str = new StringBuilder();
		 double[] gps = getGPS();
		 String link = "Click this : " + '\n'
		 + "http://maps.google.com/maps?q=" + gps[0] + "," + gps[1];
		 str.append("I am in trouble please help." + '\n' + '\n');
		 str.append(link + '\n');
		
		 new SMSAsyncTask().sendText(str);
		 Log.i("SOS", "4");
		 break;

		case R.id.buttonStopSos:
			Log.i("SOS", "buttonStopSOS");
			getActivity();
			break;

		}
	}

	 private double[] getGPS() {
	 LocationManager lm = (LocationManager)
	 getActivity().getSystemService(Context.LOCATION_SERVICE);
	 List<String> providers = lm.getProviders(true);
	
	 // Loop over the array backwards, and if you get an accurate location,
	 // then break out the loop
	 Location l = null;
	
	 for (int i = providers.size() - 1; i >= 0; i--) {
	 l = lm.getLastKnownLocation(providers.get(i));
	 if (l != null)
	 break;
	 }
	
	 // getting the location coordinates
	 double[] gps = new double[2];
	 if (l != null) {
	 gps[0] = l.getLatitude();
	 gps[1] = l.getLongitude();
	 }
	 return gps;
	 }

	 public void customKeyDown(Context ctx) {
	 // new SMSAsyncTask().execute();
	 listener = new SMSAsyncTask();
	 Log.i("SOS", "custkey 1");
	 locationManager = (LocationManager) ctx
	 .getSystemService(Context.LOCATION_SERVICE);
	 Log.i("SOS", "custkey 2");
	 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	 5000, 1, listener);
	 Log.i("SOS", "custkey 3");
	 }

	 private class SMSAsyncTask extends AsyncTask<Double, Void, Void>
	 implements
	 LocationListener {
	 Geocoder geocoder;
	 List<Address> addresses;
	
	 @Override
	 public void onLocationChanged(Location location) {
	 double latitude;
	 double longitude;
	
	 Log.i("SOS", "SMSAsyncTask");
	
	 // getting the location coordinates
	 latitude = location.getLatitude();
	 longitude = location.getLongitude();
	
	 try {
	 // formatting the coordinates to the formatted address
	 geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
	 addresses = geocoder.getFromLocation(latitude, longitude, 1);
	 StringBuilder str = new StringBuilder();
	 if (Geocoder.isPresent()) {
	 // extracting the street, city, state and the country from
	 // the formatted address
	 Address returnAddress = addresses.get(0);
	 String street = returnAddress.getThoroughfare();
	 String localityString = returnAddress.getLocality();
	 String city = returnAddress.getCountryName();
	 String region_code = returnAddress.getCountryCode();
	 String zipcode = returnAddress.getPostalCode();
	 // appending the coordinates to the Google maps which are
	 // sent as a part of the message
	 String link = "Click this : " + '\n'
	 + "http://maps.google.com/maps?q=" + latitude + ","
	 + longitude;
	 str.append("I am in trouble please help." + '\n' + '\n');
	 str.append(street + '\n');
	 str.append(localityString + '\n');
	 str.append(city + '\n' + region_code + '\n');
	 str.append(zipcode + '\n');
	 str.append(link + '\n');
	 // sendText(str);
	 } else {
	 Toast.makeText(getActivity(), "geocoder not present",
	 Toast.LENGTH_SHORT).show();
	 }
	 } catch (Exception e) {
	 Log.e("SOS", "Are you the one?? " + e.getMessage());
	 }
	 }
	
	 @Override
	 public void onStatusChanged(String provider, int status, Bundle extras) {
	 // TODO Auto-generated method stub
	
	 }
	
	 @Override
	 public void onProviderEnabled(String provider) {
	 // TODO Auto-generated method stub
	
	 }
	
	 @Override
	 public void onProviderDisabled(String provider) {
	 // TODO Auto-generated method stub
	
	 }
	
	 @Override
	 protected Void doInBackground(Double... params) {
	 Log.i("SOS", "SMSAsyncTask DIB");
	 return null;
	 }
	
	 //using SmsManager API for sending messages
	 public void sendText(StringBuilder str) {
	 try {
	 String number = "+16823517498";
	 String number2 = "+19543971744";
	 String number3 = "+18174120353";
	 SmsManager smsManager = SmsManager.getDefault();
	 smsManager.sendTextMessage(number, null, "\n" + str + "\n",
	 null, null);
	 Log.i("SOS", number);
	 smsManager.sendTextMessage(number2, null, "\n" + str + "\n",
	 null, null);
	 Log.i("SOS", number2);
	 smsManager.sendTextMessage(number3, null, "\n" + str + "\n",
	 null, null);
	 Log.i("SOS", number3);
//	 Toast.makeText(getActivity(), "SMS Sent!", Toast.LENGTH_LONG)
//	 .show();
	 Vibrator v = (Vibrator) getActivity().getSystemService(
	 Context.VIBRATOR_SERVICE);
	 v.vibrate(500);
	 } catch (Exception e) {
	 Toast.makeText(getActivity(),
	 "SMS faild, please try again later!", Toast.LENGTH_LONG)
	 .show();
	 e.printStackTrace();
	 }
	 }
	
	 }

}