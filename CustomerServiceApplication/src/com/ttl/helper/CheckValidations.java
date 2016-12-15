package com.ttl.helper;

import android.util.Log;

public class CheckValidations {

	String allowed_characters="@#^&+=";
//	String allowed_characters=";'- |";
	

	public boolean validate(String email_id)
	{
		
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email_id).matches();
		
		
		
	}
	
	public boolean validatePassword(String value)
	{
		
		
		for(int index=0;index<value.length();index++)
		{
			
			if (allowed_characters.contains(String.valueOf(value.charAt(index)))) {
			//hello123H@
				
				Log.v("chars", ""+value.charAt(index));
				
				return true;
				
			}
			
			
			
		}
		
		
		return false;
		
	}
	
}
