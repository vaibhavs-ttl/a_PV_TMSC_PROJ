package com.ttl.customersocialapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.view.Window;

public class ReminderNotificationIntentService extends FragmentActivity {
	TaskStackBuilder stackBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		int notifyId = 1;
		
		NotificationCompat.Builder mNotify = new NotificationCompat.Builder(
				getBaseContext()).setAutoCancel(true);
		mNotify.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mNotify.setSound(alarmSound);
		mNotify.setSmallIcon(R.drawable.applogo);
		mNotify.setLargeIcon(BitmapFactory.decodeResource(getResources(),
							R.drawable.applogo));
		mNotify.setContentTitle("TMSC - Reminder");
		Intent intent = getIntent();
	    String type = intent.getStringExtra("remindertype");
	    mNotify.setContentText(type);
		if (LoginActivity.login) {
			// Reminder_Fragment.reminderget = true;

			Intent resultIntent = new Intent(getBaseContext(),
					SplashScreenActivity.class);
			resultIntent.putExtra("remindertype", type);
			stackBuilder = TaskStackBuilder.create(getBaseContext());
			stackBuilder.addParentStack(SplashScreenActivity.class); // add the
																// to-be-displayed
			// activity to the top of
			// stack
			stackBuilder.addNextIntent(resultIntent);
		} else {
			// Reminder_Fragment.reminderget = true;

			Intent resultIntent = new Intent(getBaseContext(),
					SplashScreenActivity.class);
			stackBuilder = TaskStackBuilder.create(getBaseContext());
			resultIntent.putExtra("remindertype", type);
			stackBuilder.addParentStack(SplashScreenActivity.class); // add the
			// to-be-displayed
			// activity to the top of
			// stack
			stackBuilder.addNextIntent(resultIntent);
		}
		int repet = (int) System.currentTimeMillis();
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		mNotify.setContentIntent(resultPendingIntent);
		NotificationManager notificationManager = (NotificationManager) getBaseContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(repet, mNotify.build());
		ReminderNotificationIntentService.this.finish();

	}
}
