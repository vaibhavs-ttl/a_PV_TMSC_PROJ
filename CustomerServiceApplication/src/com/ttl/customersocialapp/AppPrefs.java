package com.ttl.customersocialapp;

import android.content.Context;

import com.ttl.communication.SecurePreferences;
import com.ttl.webservice.Constants;

public class AppPrefs {

	private Context context;
	private SecurePreferences prefs;
	
	public AppPrefs(Context context)
	{
		
		this.context=context;
		prefs=new SecurePreferences(context, "app", Constants.key, true);
	
	
	}
	
	
	
	public void setLoginStatus(String value)
	{
		
		prefs.put("isLogin", value);
		
		
	}
	
	
	public String getLoginStatus()
	{
		
		return prefs.getString("isLogin");
	
	}
	
	
	
	
	
	
	
	
	
}
