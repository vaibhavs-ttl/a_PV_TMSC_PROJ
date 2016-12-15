package com.ttl.webservice;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.ttl.customersocialapp.R;

public class Config {
	private static String SAMLARTIFACT = "";
	
	//public static String awsserverurl= "https://tmlpvapi.tatamotors.com/";
	//public static String awsserverurl= "http://tmlpvapi.tatamotors.com:8080/";
	public static String awsserverurl= "http://tmlmobilityservices.co.in:8080/";	
	public static long currentActivityTime , notDoingActivityTime;
	Context context;
	public static boolean appstate = false;
	public static String appVersion;
	public static String attempt=null;
	public static String max_attempt="";
	public static String pwdExpire="";
	public static String resetpassword="";
	public static String resetpasswordtoast="";
	public static String success="";
	public static String sessionId="";
	public static String sessionExpired="";
	public static String logoutstring="";
	public static String getSAMLARTIFACT() {
	
		return SAMLARTIFACT;
	}

	public static void setSAMLARTIFACT(String sAMLARTIFACT) {
		SAMLARTIFACT = sAMLARTIFACT;
	}
	
	public static boolean isEmailValid(String email)
    {
         String regExpn =
             "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                 +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

     CharSequence inputStr = email;

     Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
     Matcher matcher = pattern.matcher(inputStr);

     if(matcher.matches())
        return true;
     else
        return false;
    }
	
	public void checkLastSession(Context context1 , long lastActvityTime )
	{
		this.context = context1;
		currentActivityTime = new Date().getTime();
		Log.d("now",""+ currentActivityTime);
		Log.d("now",""+ lastActvityTime);
		if(lastActvityTime>0)
		{
		notDoingActivityTime = (currentActivityTime - lastActvityTime);
		Log.d("now",""+ notDoingActivityTime);
		
		//1800000 = 30min 600000
		
		if(notDoingActivityTime > 600000)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getResources().getString(R.string.app_name));
			builder.setMessage("Your Session has expired, Please restart app.").setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							
							dialog.dismiss();
						/*	Intent mainIntent3 = new Intent(context,SplashScreenActivity.class);			
							mainIntent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(mainIntent3);
							System.exit(0);*/
							
							
							Activity activity=(Activity)context;
							activity.finishAffinity();
							
						
						
							
						}
					});
			AlertDialog alert = builder.create();
			alert.setCancelable(false);
			alert.show();
			
		}
		
		}
		
	}
	
	public void checkSessionExpired(final Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.app_name));
	
		builder.setMessage("Your Session has expired").setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

			
			
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
		
						//dialog.cancel();
					//	Intent mainIntent3 = new Intent(context,LoginActivity.class);
		
						
						/*dialog.dismiss();
						Intent mainIntent3 = new Intent(context,SplashScreenActivity.class);			
						mainIntent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(mainIntent3);
						System.exit(0);
				*/
					
						Activity activity=(Activity)context;
						activity.finishAffinity();	
						
							
					}
				});
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		if (!alert.isShowing()) {
			alert.show();		
		}
		
	}
}
