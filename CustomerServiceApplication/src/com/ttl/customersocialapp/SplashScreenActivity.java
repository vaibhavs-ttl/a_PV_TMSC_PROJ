package com.ttl.customersocialapp;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.communication.SecurePreferences;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.helper.FetchDeviceID;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class SplashScreenActivity extends Activity {

	public static boolean chkweather = false;
	private Bundle Loginbundle;
	private Intent intent;
	private final String secret_key_sharedPref = "SECRET_KEY";
	private Tracker mTracker;
	private static final String TAG = "SplashScreenActivity";
	private String version;
	private final String PREFS_NAME = "CREDENTIALS";
	private SecurePreferences scPreferences;

	// Added by Parag

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();

		scPreferences = new SecurePreferences(SplashScreenActivity.this, PREFS_NAME,
				Constants.key, true);

		
	//	startService(new Intent(SplashScreenActivity.this,DirectoryService.class));
			
		
		PackageManager manager = SplashScreenActivity.this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(SplashScreenActivity.this.getPackageName(), 0);
			version = info.versionName;

		} catch (NameNotFoundException e) {

			
		

		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				SharedPreferences pref = getSharedPreferences("ActivityPREF",
						Context.MODE_PRIVATE);
				
				Loginbundle = new Bundle();
				LoginActivity.login = false;

				intent = getIntent();
				if (intent.hasExtra("remindertype")) {
					Loginbundle.putString("receiveddata",
							intent.getStringExtra("remindertype"));
				}

				if (pref.getBoolean("activity_executed", true)) {

					
					Editor ed = pref.edit();
					ed.putBoolean("activity_executed", false);
					Intent mainIntent = new Intent(SplashScreenActivity.this,
							GuideActivity.class);
					SplashScreenActivity.this.startActivity(mainIntent);
					SplashScreenActivity.this.finish();
					finish();
					ed.commit();
				} 
				else if (scPreferences.getString("user_id")==null && scPreferences.getString("password")==null) {
					chkweather = false;
					Intent mainIntent = new Intent(SplashScreenActivity.this,
							LoginActivity.class);
					// mainIntent.putExtras(Loginbundle);
					mainIntent.putExtra("remindertype",
							intent.getStringExtra("remindertype"));

					SplashScreenActivity.this.startActivity(mainIntent);
					SplashScreenActivity.this.finish();
				} 
				
				
				else if(scPreferences.getString("user_id")!=null && scPreferences.getString("password")!=null)
				{
					/* 
			  	Intent mainIntent = new Intent(SplashScreenActivity.this,
							HomeActivity.class);
				
						
					SplashScreenActivity.this.startActivity(mainIntent);
					SplashScreenActivity.this.finish();*/

					// Calling web service to refresh login
					if (new CheckConnectivity().checkNow(SplashScreenActivity.this)) {
						refreshLogin(scPreferences.getString("user_id"), scPreferences.getString("password"));	
					}
					else
					{
						Toast.makeText(SplashScreenActivity.this, "No network connection available", Toast.LENGTH_LONG).show();
						finish();
					}

					
					
			
				}
				
				
				else if (pref.getBoolean("activity_executed", false)) {
					chkweather = false;
					Intent mainIntent = new Intent(SplashScreenActivity.this,
							LoginActivity.class);
					// mainIntent.putExtras(Loginbundle);
					mainIntent.putExtra("remindertype",
							intent.getStringExtra("remindertype"));

					SplashScreenActivity.this.startActivity(mainIntent);
					SplashScreenActivity.this.finish();
				} 
				
				else {
					chkweather = true;
					Intent mainIntent = new Intent(SplashScreenActivity.this,
							LoginActivity.class);
					mainIntent.putExtra("remindertype",
							intent.getStringExtra("remindertype"));

					SplashScreenActivity.this.startActivity(mainIntent);
					SplashScreenActivity.this.finish();
				}
				/*
				 * else {
				 * 
				 * chkweather = true; UserDetails.setUser_id(getname);
				 * CheckConnectivity checknow = new CheckConnectivity(); boolean
				 * connect = checknow .checkNow(getApplicationContext()); if
				 * (connect) { // getCurrentCity();
				 * 
				 * String URL = getApplicationContext().getResources()
				 * .getString(R.string.URL); String environment = "";
				 * 
				 * if (URL.contains("qa")) { environment = "QA"; } else {
				 * environment = "Production"; } try {
				 * 
				 * String req = Config.awsserverurl +
				 * "CustomerApp_Restws/customerapp/user/xml";
				 * List<NameValuePair> nameValuePairs = new
				 * ArrayList<NameValuePair>( 3); nameValuePairs.add(new
				 * BasicNameValuePair( "user_id", getname));
				 * nameValuePairs.add(new BasicNameValuePair( "password",
				 * getpassword)); nameValuePairs.add(new BasicNameValuePair(
				 * "environment", environment)); // Log.d("env", environment);
				 * new AWS_WebServiceCall(SplashScreenActivity.this, req,
				 * ServiceHandler.POST, Constants.user, nameValuePairs, new
				 * ResponseCallback() {
				 * 
				 * @Override public void onResponseReceive( Object object) { //
				 * TODO Auto-generated method stub boolean res = (boolean)
				 * object; if (res) {
				 * 
				 * SharedPreferences settings = getSharedPreferences(
				 * PREFS_NAME, 0); SharedPreferences.Editor editor = settings
				 * .edit(); editor.putString(name, edtuser_id
				 * .getText().toString()); editor.putString(password,
				 * edtpassword.getText() .toString()); editor.commit();
				 * 
				 * // LoginActivity.LoginWay="AppLogin";
				 * 
				 * if (Float.parseFloat(version) < Float
				 * .parseFloat(Config.appVersion)) { Log.d("version",
				 * Config.appVersion); Intent intent = new Intent(
				 * SplashScreenActivity.this, VersionUpdateActivity.class);
				 * intent.putExtra("version", Config.appVersion);
				 * startActivity(intent);
				 * 
				 * finish(); } else { LoginActivity.login = true;
				 * 
				 * Intent mainIntent = new Intent( SplashScreenActivity.this,
				 * HomeActivity.class); if (Loginbundle != null) { mainIntent
				 * .putExtras(Loginbundle); } SplashScreenActivity.this
				 * .startActivity(mainIntent); SplashScreenActivity.this
				 * .finish(); mTracker.send(new HitBuilders.EventBuilder()
				 * .setCategory( UserDetails .getUser_id()) .setAction(
				 * "thread_true") .setLabel("Login") .build()); } // finish(); }
				 * else {
				 * 
				 * LoginActivity.login = false; Intent mainIntent = new Intent(
				 * SplashScreenActivity.this, LoginActivity.class);
				 * mainIntent.putExtra( "remindertype",
				 * intent.getStringExtra("remindertype"));
				 * 
				 * SplashScreenActivity.this .startActivity(mainIntent);
				 * SplashScreenActivity.this .finish(); }
				 * 
				 * }
				 * 
				 * @Override public void onErrorReceive(String string) { // TODO
				 * Auto-generated method stub if (string
				 * .equalsIgnoreCase("No internet connection.")) {
				 * AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				 * SplashScreenActivity.this); // Setting Dialog Title
				 * alertDialog.setTitle(""); // Setting Dialog Message
				 * alertDialog .setMessage(
				 * "Couldn't connect to the Server. Please check your Network Connection and try again."
				 * ); // Setting Icon to Dialog alertDialog
				 * .setIcon(android.R.drawable.ic_dialog_alert); // Setting
				 * Positive "Yes" Button alertDialog .setPositiveButton( "OK",
				 * new DialogInterface.OnClickListener() { public void onClick(
				 * DialogInterface dialog, int which) { finish(); } });
				 * 
				 * // Showing Alert Message alertDialog.show(); } else {
				 * 
				 * Toast.makeText( getApplicationContext(),
				 * "Invalid  Credentials, please relogin." ,
				 * Toast.LENGTH_LONG).show();
				 * 
				 * LoginActivity.login = false; Intent mainIntent = new Intent(
				 * SplashScreenActivity.this, LoginActivity.class);
				 * mainIntent.putExtra( "remindertype",
				 * intent.getStringExtra("remindertype"));
				 * 
				 * SplashScreenActivity.this .startActivity(mainIntent);
				 * SplashScreenActivity.this .finish(); }
				 * 
				 * Toast.makeText( getApplicationContext(),
				 * "Couldn't connect to the Server. Please check your Network Connection and try again."
				 * , Toast.LENGTH_LONG).show();
				 * 
				 * // edtuser_id.setFocusable(true); LoginActivity.login =
				 * false;
				 * 
				 * Intent mainIntent = new Intent(SplashScreenActivity.this,
				 * LoginActivity.class); mainIntent.putExtra ("remindertype",
				 * intent.getStringExtra ("remindertype"));
				 * 
				 * SplashScreenActivity.this. startActivity(mainIntent);
				 * SplashScreenActivity .this.finish();
				 * 
				 * }
				 * 
				 * }).execute(); } catch (Exception e) {
				 * 
				 * 
				 * Log.d("exception login", e.toString()); } } else { Toast
				 * toast = Toast.makeText(getApplicationContext(),
				 * "No network connection available.", Toast.LENGTH_SHORT);
				 * toast.show(); }
				 * 
				 * }
				 */

			}
		}, 2000);

	}

	@Override
	protected void onResume() {

		super.onResume();

		mTracker.setScreenName(TAG);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	protected void onStop() {
		
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);

	}

	private void refreshLogin(String getname,String getpassword) {

		chkweather=true;
		String URL = getApplicationContext().getResources().getString(
				R.string.URL);
		String environment = "";

		if (URL.contains("qa")) {
			environment = "QA";
		} else {
			environment = "Production";
		}

		try {

			String req = Config.awsserverurl + "tmsc_ch/customerapp/user/xml";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("user_id", getname));
			nameValuePairs.add(new BasicNameValuePair("password", getpassword));
			nameValuePairs.add(new BasicNameValuePair("environment",environment));
			nameValuePairs.add(new BasicNameValuePair("deviceId",FetchDeviceID.getID(SplashScreenActivity.this)));
			
			new AWS_WebServiceCall(SplashScreenActivity.this, req,
					ServiceHandler.POST, Constants.user, nameValuePairs,
					new ResponseCallback() {
					
				@Override
				public void onResponseReceive(Object object) {
					
					boolean res = (boolean) object;
					if(res){
			
					
						
/*						
					if (Float.parseFloat(version) < Float
							.parseFloat(Config.appVersion)) {

						Intent intent = new Intent(
								SplashScreenActivity.this,
								VersionUpdateActivity.class);
						intent.putExtra("version", Config.appVersion);
						startActivity(intent);

						finish();
					}*/ /*else */
					
				//	float version_check=Float.parseFloat(Config.appVersion);
						
					if (Config.attempt!=null) {

						/*scPreferences
						.put("version",
								Config.appVersion);
						Intent intent = new Intent(
								SplashScreenActivity.this,
								VersionUpdateActivity.class);
						intent.putExtra(
								"version",
								Config.appVersion);
						startActivity(intent);

						finish();
*/					
					
						Intent intent = new Intent(
								SplashScreenActivity.this,
								LoginActivity.class);
						Log.v("blocked response", ""+Config.attempt);
						intent.putExtra(
								"blocked",Config.attempt);
						startActivity(intent);
						finish();
					
					
					
					
					}
					else if(Config.pwdExpire!=null)
					{
						Intent intent = new Intent(
								SplashScreenActivity.this,
								LoginActivity.class);
						intent.putExtra(
								"max_logins",Config.pwdExpire);
						startActivity(intent);
						finish();
					
						
						
					}
					
					else {
						
					
						
						
						Intent mainIntent = new Intent(
								SplashScreenActivity.this,
								HomeActivity.class);
						if (Loginbundle != null) {
							mainIntent.putExtras(Loginbundle);
						}
						
						if (scPreferences
								.getString(secret_key_sharedPref) != null) {
							String secret_key = scPreferences
									.getString(secret_key_sharedPref);


							byte[] encodedKey = Base64
									.decode(secret_key,
											Base64.DEFAULT);


							Constants.secrete_key_spec = new SecretKeySpec(
									encodedKey,
									0,
									encodedKey.length,
									"AES");


						} else {
							// Added
							// by
							// Trupti
							// Reddy
							try {
								SecureRandom secure_random = SecureRandom
										.getInstance("SHA1PRNG");
								secure_random
								.setSeed("SECRET_KEY"
										.getBytes());
								KeyGenerator key_generator = KeyGenerator
										.getInstance("AES");
								key_generator
								.init(128,
										secure_random);
								Constants.secretKey = key_generator
										.generateKey();

								Constants.secrete_key_spec = new SecretKeySpec(
										Constants.secretKey
										.getEncoded(),
										"AES");

								// scPreferences.put(secret_key_sharedPref,
								// Constants.secrete_key_spec.toString());

								if (Constants.secretKey != null) {
									String stringKey = Base64
											.encodeToString(
													Constants.secretKey
													.getEncoded(),
													Base64.DEFAULT);
									scPreferences
									.put(secret_key_sharedPref,
											stringKey);
									// Log.e("string key in login",
									// stringKey);
								}

								// Log.e("secet key spec in login",
								// Constants.secrete_key_spec.toString());
								// Log.e("secet key in login",
								// Constants.secretKey.toString());
							} catch (Exception e) {
								//e.printStackTrace();
								
							}

						}
								
						SplashScreenActivity.this
						.startActivity(mainIntent);
						SplashScreenActivity.this.finish();
						mTracker.send(new HitBuilders.EventBuilder()
						.setCategory(UserDetails.getUser_id())
						.setAction("thread_true")
						.setLabel("Login").build());
					}
					
				}
					else {

						LoginActivity.login = false;
						Intent mainIntent = new Intent(
								SplashScreenActivity.this,
								LoginActivity.class);
						mainIntent.putExtra(
								"remindertype",
								intent.getStringExtra("remindertype"));
						
						
						
						SplashScreenActivity.this
								.startActivity(mainIntent);
						SplashScreenActivity.this
								.finish();
					}
					
					

				}

				@Override
				public void onErrorReceive(String string) {
				
					
					try {
			            if (string.equalsIgnoreCase("No internet connection.")) {
			      AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreenActivity.this);
			              // Setting Dialog Title
			      alertDialog.setTitle("");
			              // Setting Dialog Message
			      alertDialog.setMessage("Couldn't connect to the Server. Please check your Network Connection and try again.");
			              // Setting Icon to Dialog
			      alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
			              // Setting Positive "Yes" Button
			      alertDialog.setPositiveButton(
			                      "OK",
			                      new DialogInterface.OnClickListener() {
			                        public void onClick(
			                            DialogInterface dialog,
			                            int which) {
			                          finish();
			                        }
			                      });

			              // Showing Alert Message
			              alertDialog.show();
			            } else {
			              
			              
			               
			              LoginActivity.login = false;
			              Intent mainIntent = new Intent(
			                  SplashScreenActivity.this,
			                  LoginActivity.class);
			              mainIntent.putExtra(
			                  "remindertype",
			                  intent.getStringExtra("remindertype"));

			              SplashScreenActivity.this
			                  .startActivity(mainIntent);
			              SplashScreenActivity.this
			                  .finish();
			            }
			          } catch (Exception e) {
			            // TODO: handle exception
			            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
			                SplashScreenActivity.this);
			            // Setting Dialog Title
			            alertDialog.setTitle("");
			            // Setting Dialog Message
			            alertDialog
			                .setMessage("Couldn't connect to the Server. Please check your Network Connection and try again.");
			            // Setting Icon to Dialog
			            alertDialog
			                .setIcon(android.R.drawable.ic_dialog_alert);
			            // Setting Positive "Yes" Button
			            alertDialog
			                .setPositiveButton(
			                    "OK",
			                    new DialogInterface.OnClickListener() {
			                      public void onClick(
			                          DialogInterface dialog,
			                          int which) {
			                        finish();
			                      }
			                    });

			            // Showing Alert Message
			            alertDialog.show();
			          }
				}

			}).execute();

		
		
		
		}
		
		catch (Exception e) {
			
			Log.e("error response", e.toString());
		}
		

	}

	
	
	
	
	
	
}
