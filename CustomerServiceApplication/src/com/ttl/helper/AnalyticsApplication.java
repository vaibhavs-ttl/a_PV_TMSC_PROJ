package com.ttl.helper;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.app.Application;

public class AnalyticsApplication extends Application{

	 private Tracker mTracker;

	  /**
	   * Gets the default {@link Tracker} for this {@link Application}.
	   * @return tracker
	   */
	  synchronized public Tracker getDefaultTracker() {
	    if (mTracker == null) {
	      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
	      // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
	      mTracker = analytics.newTracker("UA-74127163-1");
	      mTracker.enableExceptionReporting(true);
          //mTracker.enableAutoActivityTracking(true);

	    }
	    return mTracker;
	  }
}
