package com.ttl.helper;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;


public class FetchDeviceID {

	
	
	public static String getID(Context context)
	{
		
		TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		Log.v("login device id", telephonyManager.getDeviceId());

		
		if (telephonyManager.getDeviceId()!=null) {
			
			
			return telephonyManager.getDeviceId();
		}
		else
		{
			return "";	
		}
		
		
	
	
	}
	
	
}
