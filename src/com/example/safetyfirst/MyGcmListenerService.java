package com.example.safetyfirst;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String alertStr = data.getString("alert");
        String timeStamp = data.getString("time");
        
        if(timeStamp!=null){
        	final int notificationId = 1;
        	Intent in = new Intent(this,DetailsOfNotificationActivity.class);
        	
    		in.putExtra("textDetailsSubject", message);
    		in.putExtra("textDetailsTimeStamp", timeStamp);
    		in.putExtra("textDetailsBody", alertStr);
    		
    		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    in, 0);
            
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Safety First")
                    .setContentText(message)
                    .setContentIntent(pendingIntent);
            
            NotificationManager nm = (NotificationManager) getApplicationContext()
                  .getSystemService(Context.NOTIFICATION_SERVICE);
            
            nm.notify(notificationId, mBuilder.build());
        	
        }else{
        	final int notificationId = 1;
            if("student".equals(message)){
            	 Intent intent = new Intent(this, RideConfirmation.class);
                 
                 PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                         intent, 0);
                 
                 NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                         .setSmallIcon(R.drawable.ic_launcher)
                         .setContentTitle("Safety First")
                         .setContentText("Your ride is on the way.")
                         .setContentIntent(pendingIntent);
                 
                 
                 
                 
                 NotificationManager nm = (NotificationManager) getApplicationContext()
                       .getSystemService(Context.NOTIFICATION_SERVICE);
                 
                 nm.notify(notificationId, mBuilder.build());
            }else if("police".equals(alertStr)){
            	System.out.println("police clicked");
            	 Intent intent = new Intent(this, RideConfimationForPolice.class);
                 
                 PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                         intent, 0);
                 
                 NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                         .setSmallIcon(R.drawable.ic_launcher)
                         .setContentTitle("Safety First")
                         .setContentText("you need to pick student.")
                         .setContentIntent(pendingIntent);
                 
                 
                 
                 
                 NotificationManager nm = (NotificationManager) getApplicationContext()
                       .getSystemService(Context.NOTIFICATION_SERVICE);
                 
                 nm.notify(notificationId, mBuilder.build());
            }
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        System.out.println("hello this is meet who is coding");
    }
}