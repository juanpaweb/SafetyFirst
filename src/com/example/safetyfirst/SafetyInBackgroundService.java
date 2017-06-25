package com.example.safetyfirst;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SafetyInBackgroundService extends Service {
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver mReceiver = new SafetyInBackgroundReceiver();
		registerReceiver(mReceiver, filter);
	}

	private double[] getGPS() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

	@Override
	public void onStart(Intent intent, int startId) {
		boolean screenOn = intent.getBooleanExtra("screen_state", false);

		// if screen state is on
		if (!screenOn) {

			double[] loc = getGPS();
			double latitude = loc[0];
			double longitude = loc[1];
			Geocoder geocoder;
			List<Address> addresses;
			try {
				// get the formatted address form the location coordinates and
				// extract the street, city, state and the country from the
				// formatted address
				geocoder = new Geocoder(SafetyInBackgroundService.this,
						Locale.ENGLISH);
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
				StringBuilder str = new StringBuilder();
				if (Geocoder.isPresent()) {
					Address returnAddress = addresses.get(0);
					String street = returnAddress.getThoroughfare();
					String localityString = returnAddress.getLocality();
					String city = returnAddress.getCountryName();
					String region_code = returnAddress.getCountryCode();
					String zipcode = returnAddress.getPostalCode();
					String link = "Click this : " + '\n'
							+ "http://maps.google.com/maps?q=" + latitude + ","
							+ longitude;
					str.append("I am in trouble please help." + '\n' + '\n');
					str.append(street + '\n');
					str.append(localityString + '\n');
					str.append(city + '\n' + region_code + '\n');
					str.append(zipcode + '\n');
					str.append(link + '\n');

					String number = "+16823517498";
					String number2 = "+19543971744";
					String number3 = "+18174120353";
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(number, null, "\n" + str + "\n",
							null, null);
					Log.i("SOS", number);
					smsManager.sendTextMessage(number2, null,
							"\n" + str + "\n", null, null);
					Log.i("SOS", number2);
					smsManager.sendTextMessage(number3, null,
							"\n" + str + "\n", null, null);
					Log.i("SOS", number3);
//					Toast.makeText(getApplicationContext(), "SMS Sent!",
//							Toast.LENGTH_LONG).show();
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(500);
				} else {
					Toast.makeText(getApplicationContext(),
							"geocoder not present", Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("SIB", "if " + e.getMessage());
			}

		} else {
			double[] loc = getGPS();
			double latitude = loc[0];
			double longitude = loc[1];
			Geocoder geocoder;
			List<Address> addresses;
			try {
				// get the formatted address form the location coordinates and
				// extract the street, city, state and the country from the
				// formatted address
				geocoder = new Geocoder(SafetyInBackgroundService.this,
						Locale.ENGLISH);
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
				StringBuilder str = new StringBuilder();
				if (Geocoder.isPresent()) {
					Address returnAddress = addresses.get(0);
					String street = returnAddress.getThoroughfare();
					String localityString = returnAddress.getLocality();
					String city = returnAddress.getCountryName();
					String region_code = returnAddress.getCountryCode();
					String zipcode = returnAddress.getPostalCode();
					String link = "Click this : " + '\n'
							+ "http://maps.google.com/maps?q=" + latitude + ","
							+ longitude;
					str.append("I am in trouble please help." + '\n' + '\n');
					str.append(street + '\n');
					str.append(localityString + '\n');
					str.append(city + '\n' + region_code + '\n');
					str.append(zipcode + '\n');
					str.append(link + '\n');

					String number = "+16823517498";
					String number2 = "+19543971744";
					String number3 = "+18174120353";
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(number, null, "\n" + str + "\n",
							null, null);
					Log.i("SOS", number);
					smsManager.sendTextMessage(number2, null,
							"\n" + str + "\n", null, null);
					Log.i("SOS", number2);
					smsManager.sendTextMessage(number3, null,
							"\n" + str + "\n", null, null);
					Log.i("SOS", number3);
					Toast.makeText(getApplicationContext(), "SMS Sent!",
							Toast.LENGTH_LONG).show();
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(500);
				} else {
					Toast.makeText(getApplicationContext(),
							"geocoder not present", Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("SIB", "else " + e.getMessage());
			}
		}
	}
}
