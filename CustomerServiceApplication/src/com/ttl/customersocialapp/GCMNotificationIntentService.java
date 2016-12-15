package com.ttl.customersocialapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
 
import com.google.android.gms.gcm.GoogleCloudMessaging;
 
public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated
    public static final int notifyID = 9001;
    NotificationCompat.Builder builder;
 
    public GCMNotificationIntentService() {
        super("GcmIntentService");
        
      	

    }
 
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
 
        String messageType = gcm.getMessageType(intent);
        
        Log.d("message received", extras.size()+" "+messageType);	
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
         
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
         
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                Log.d("message received", intent.getStringExtra("message") + intent.getStringExtra("message") .length() );	
                
         
                
                try {
                	JSONArray objarray = new JSONArray(intent.getStringExtra("message"));
                	for(int i=0; i<objarray.length(); i++)
					{
					JSONObject obj = objarray.getJSONObject(i);
                    Log.d("message aray", obj.toString());	
                    String type = obj.getString("notificationType");
					String title = obj.getString("notificationTitle");
					String desc = obj.getString("notificationDesc");
					sendNotification(type, title, desc);

					}
				
					
				} catch (JSONException e) {
				
					e.printStackTrace();
				}
        
                
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
 
    private void sendNotification(String type , String title , String desc) {
    	Intent resultIntent;
    		if(LoginActivity.login)
    		{
                 resultIntent = new Intent(this, HomeActivity.class);

    		}else
    		{
                 resultIntent = new Intent(this, SplashScreenActivity.class);

    		}
            resultIntent.putExtra("msg", type);
            Log.d("message sending", type);	
            if(desc.length()>=80)
            {
            	desc = desc.substring(0, 79);
            }else
            {
            	
            }
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);
 
            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;
 
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 
            mNotifyBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.applogo)
			.setLargeIcon(
					BitmapFactory.decodeResource(getResources(),
							R.drawable.applogo))
			.setContentTitle("TMSC - "+type)
			.setTicker("TMSC")
			.setContentText(title +" - "+desc)
			.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(title+" - "+desc));
            // Set pending intent
            mNotifyBuilder.setContentIntent(resultPendingIntent);
 
            // Set Vibrate, Sound and Light         
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
    		int repeat = (int) System.currentTimeMillis();

            mNotifyBuilder.setDefaults(defaults);
            // Set the content for Notification 
         //   mNotifyBuilder.setContentText("Customer Service");
            // Set autocancel
            mNotifyBuilder.setAutoCancel(true);
            // Post a notification
            mNotificationManager.notify(repeat, mNotifyBuilder.build());
    }
}
