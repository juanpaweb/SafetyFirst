package com.example.safetyfirst;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.crypto.NullCipher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MavAlertsFragment extends Fragment implements OnItemClickListener {
	String[] values;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_tab_mavalerts, container,
				false);

		final ListView listview = (ListView) v
				.findViewById(R.id.listViewNotifications);

		LinkToOmega omegaData = new LinkToOmega();
		try {
			values = omegaData.execute().get();
			//System.out.println("Values :" +values);
		} catch (InterruptedException e) {
			Log.e("MavAlertsFragment - ", "Error in InterruptedException - "
					+ e.toString());
		} catch (ExecutionException e) {
			Log.e("MavAlertsFragment - ",
					"Error in ExecutionException - " + e.toString());
		}

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length;i++) {
			System.out.println("inside for loop of converting values of i to string and splitting");
			System.out.println("value of i" +values[i]);
			String[] sub_time_stamp = values[i].split(";");
			System.out.println("after splitting" +sub_time_stamp);
			String subject = sub_time_stamp[2];
			String time_stamp = sub_time_stamp[1];
			list.add(subject + " " + time_stamp);
			Log.i("MavAlertsFragment - ", "Added data to list " + subject + " "
					+ time_stamp);
		}
		// final StableArrayAdapter adapter = new StableArrayAdapter(this,
		// android.R.layout.simple_list_item_1, list);
		final StableArrayAdapter adapter = new StableArrayAdapter(
				getActivity(), android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);

		return v;

		// return (RelativeLayout) inflater.inflate(
		// R.layout.activity_tab_mavalerts, container, false);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final String item = (String) parent.getItemAtPosition(position);
		Toast.makeText(getActivity(), item + " " + id + " " + position,
				Toast.LENGTH_LONG).show();

		Intent i = new Intent(getActivity(),
				DetailsOfNotificationActivity.class);

		Log.i("MavAlertsFragment - ", "Data at clicked " + values[position]);
		String [] sendData = values[position].split(";");

		i.putExtra("textDetailsID", sendData[0]);
		i.putExtra("textDetailsSubject", sendData[2]);
		i.putExtra("textDetailsTimeStamp", sendData[1]);
		i.putExtra("textDetailsBody", sendData[3]);
		startActivity(i);
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			Log.i("MavAlertsFragment - ",
					"Inside StableArrayAdapter constructor");
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	private class LinkToOmega extends AsyncTask<String, String, String[]> {
		HttpClient httpClient;
		HttpResponse httpResponse;
		HttpPost httpPost;
		HttpEntity httpEntity;
		InputStream isr;
		BufferedReader bReader;
		String line;
		String data[];

		@Override
		protected String[] doInBackground(String... params) {
			// Create Http request and response objects to connect to Omega
			try {
				httpClient = new DefaultHttpClient();
				//Log.i("MavAlertsFragment - ", "Created httpClient");

				httpPost = new HttpPost(
						"http://omega.uta.edu/~sxc3409/postNotification.php");
				//Log.i("MavAlertsFragment - ", "Created httpPost to omega");

				httpResponse = httpClient.execute(httpPost);
				//Log.i("MavAlertsFragment - ", "Created httpResponse");

				httpEntity = httpResponse.getEntity();
				//Log.i("MavAlertsFragment - ", "Created httpEntity");
				if (httpEntity != null) {
					isr = httpEntity.getContent();
					Log.i("MavAlertsFragment - ",
							"Availability of isr " + isr.available());
				}
			} catch (ClientProtocolException e) {
				Log.e("MavAlertsFragment - ",
						"Error in ClientProtocolException - " + e.toString());
			} catch (IOException e) {
				Log.e("MavAlertsFragment - ",
						"Error in IOException - " + e.toString());
			} catch (Exception e) {
				Log.e("MavAlertsFragment - ",
						"Error in Connection - " + e.toString());
			}

			// Convert the data in InputStream to String
			try {
				bReader = new BufferedReader(new InputStreamReader(isr), 8);
				line = null;
				data = new String[] { " " };
				while ((line = bReader.readLine()) != null) {
					data = line.split("<br>");
					Log.i("MavAlertsFragment - ", "Data from omega " + line);
				}
				for (int i = 0; i < data.length; i++) {
					Log.i("MavAlertsFragment - ",
							"Data from omega converted to String [] " + data[i]);
					//System.out.println("data in perticular line " +data[i]+ "awfgsad" + i);
				}
				return data;
			} catch (IOException e) {
				Log.e("MavAlertsFragment - ",
						"Error in ISR to String conversion - " + e.toString());
				return null;
			}
		}
	}
}