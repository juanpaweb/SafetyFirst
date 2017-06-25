package com.example.safetyfirst;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TabActionBarActivity extends Activity {
	int keyCount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		String SOS = getResources().getString(R.string.sos);
		Tab tab = actionBar.newTab();
		tab.setText("SOS");
		TabListener<SOSFragment> tl = new TabListener<SOSFragment>(this, SOS,
				SOSFragment.class);
		tab.setTabListener(tl);
		actionBar.addTab(tab);

		String pickMe = getResources().getString(R.string.pickMe);
		tab = actionBar.newTab();
		tab.setText("Pick Me");
		TabListener<PickmeFragment> tl2 = new TabListener<PickmeFragment>(this,
				pickMe, PickmeFragment.class);
		tab.setTabListener(tl2);
		actionBar.addTab(tab);

		String MavAlerts = getResources().getString(R.string.mavAlerts);
		tab = actionBar.newTab();
		tab.setText("Mav Alerts");
		TabListener<MavAlertsFragment> tl3 = new TabListener<MavAlertsFragment>(
				this, MavAlerts, MavAlertsFragment.class);
		tab.setTabListener(tl3);
		actionBar.addTab(tab);

		// TabActionBarActivity.this.startService(new Intent(
		// TabActionBarActivity.this, SafetyInBackgroundService.class));

	}

	 @Override
	 public boolean dispatchKeyEvent(KeyEvent event) {
	 if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
	// Toast.makeText(getApplicationContext(), "dispatch to SOS",
	// Toast.LENGTH_LONG).show();
	// Log.i("TABA", "dispatch to SOS");
	// new SOSFragment().customKeyDown(getApplicationContext());
	// Toast.makeText(getApplicationContext(), "from SOS",
	// Toast.LENGTH_LONG).show();
	// Log.i("TABA", "back to dispatch");

	 LocationManager locationManager;
	 LocationListener listener;
	 listener = new TabMainAsyncTask();
	 Log.i("TABA", "1");
	 locationManager = (LocationManager)
	 getSystemService(Context.LOCATION_SERVICE);
	 Log.i("TABA", "2");
	 locationManager.requestLocationUpdates(
	 LocationManager.GPS_PROVIDER, 15000, 1, listener);
	 Log.i("TABA", "3");
	 new TabMainAsyncTask().execute();
	 Log.i("TABA", "4");
	
	 StringBuilder str = new StringBuilder();
	 double[] gps = getGPS();
	 String link = "Click this : " + '\n'
	 + "http://maps.google.com/maps?q=" + gps[0] + "," + gps[1];
	 str.append("I am in trouble please help." + '\n' + '\n');
	 str.append(link + '\n');
	
	 new TabMainAsyncTask().sendText(str);
	 Log.i("SOS", "4");
	
	 return true;
	 }
	 return super.dispatchKeyEvent(event);
	 }

	 private double[] getGPS() {
	 LocationManager lm = (LocationManager) getSystemService(
	 Context.LOCATION_SERVICE);
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

//	 @Override
//	 public boolean onKeyDown(int keyCode, KeyEvent event) {
//	 if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
//	 event.startTracking();
//	 keyCount++;
//	 if (keyCount == 3) {
//	 LocationManager locationManager;
//	 LocationListener listener;
//	 listener = new TabMainAsyncTask();
//	 Log.i("TABA", "onKeyDown 1");
//	 locationManager = (LocationManager)
//	 getSystemService(Context.LOCATION_SERVICE);
//	 Log.i("TABA", "onKeyDown 2");
//	 locationManager.requestLocationUpdates(
//	 LocationManager.GPS_PROVIDER, 15000, 1, listener);
//	 Log.i("TABA", "onKeyDown 3");
//	 new TabMainAsyncTask().execute();
//	 Log.i("TABA", "onKeyDown 4");
//	 }
//	 return true;
//	 }
//	 return super.onKeyDown(keyCode, event);
//	 }

//	 @Override
//	 public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//	 if(event.getKeyCode() == KeyEvent.KEYCODE_POWER){
//	 Log.i("TABA", "Long press to SOS");
//	 new SOSFragment().customKeyDown();
//	 return true;
//	 }
//	 return super.onKeyLongPress(keyCode, event);
//	 }
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


	private class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}

	private class TabMainAsyncTask extends AsyncTask<Double, Void, Void>
			implements LocationListener {
		Geocoder geocoder;
		List<Address> addresses;

		@Override
		public void onLocationChanged(Location location) {
			double latitude;
			double longitude;

			Log.i("TABA", "TABA TabMainAsyncTask");

			// getting the location coordinates
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			try {
				// formatting the coordinates to the formatted address
				geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
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
					Toast.makeText(getApplicationContext(),
							"geocoder not present", Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("TABA", "Are you the one? " + e.getMessage());
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
			Log.i("TABA", "TABA TabMainAsyncTask DIB");
			return null;
		}

		// using SmsManager API for sending messages
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
//		 Toast.makeText(getApplicationContext(), "SMS Sent!",
//		 Toast.LENGTH_LONG).show();
		 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(500);
		
		 } catch (Exception e) {
		 Toast.makeText(getApplicationContext(),
		 "SMS faild, please try again later!", Toast.LENGTH_LONG)
		 .show();
		 e.printStackTrace();
		 }
		 }

	}

}