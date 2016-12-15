package com.ttl.customersocialapp;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.communication.SecurePreferences;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.helper.CheckValidations;
import com.ttl.helper.FetchDeviceID;
import com.ttl.helper.FragmentCallback;
import com.ttl.model.ServiceHandler;
import com.ttl.model.Weather;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
import com.ttl.webservice.WeatherWebservice;

public class LoginActivity extends Activity implements OnClickListener,
LocationListener, ConnectionCallbacks, OnConnectionFailedListener {

	private Button sign_in;
	private Button sign_up;
	private EditText edtuser_id, edtpassword, emailId;
	private final String PREFS_NAME = "CREDENTIALS";
	private final String secret_key_sharedPref = "SECRET_KEY";
	private SecurePreferences scPreferences;
	private TextView forgot_user_id;
	private LocationManager locationManager;
	static Weather dayWeather;
	private double MyLat, MyLong;
	private String CityName = "", CityName1 = "";
	private String StateName = "";
	private String CountryName = "";
	private String current_address = "", pin_code = "";
	private Location location;
	public static String city = "", temp = "", icon = "";
	public static ImageView imgicon;
	private TextView forgotPassword;
	private Dialog forgotPasswordDialog;

	private String user_email = "";

	// Social Login

	private GoogleApiClient mGoogleApiClient;

	private boolean mSignInClicked;
	private boolean mIntentInProgress;
	private static final int RC_SIGN_IN = 0;
	private ConnectionResult mConnectionResult;
	static boolean login = false;
	private Bundle Loginbundle;
	private Intent intent;
	private Tracker mTracker;
	// Facebook APP ID
	public static String password_;
	private static String APP_ID = "489759044535730"; // Replace your App ID

	private String FILENAME = "AndroidSSO_data";

	private String user_id;
	private String version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_withoutsocial);

		sign_in = (Button) findViewById(R.id.sign_in);
		sign_in.setOnClickListener(this);
		
		//scPreferences = new SecurePreferences(LoginActivity.this, "CRE",Constants.key, true);
		
		sign_up = (Button) findViewById(R.id.sign_up);
		sign_up.setOnClickListener(this);
		edtuser_id = (EditText) findViewById(R.id.txtuser_id);
		edtpassword = (EditText) findViewById(R.id.txtpassword);
		forgotPassword = (TextView) findViewById(R.id.textView1);
		forgot_user_id=(TextView)findViewById(R.id.forgot_user_id);
		forgotPassword.setOnClickListener(this);
		forgot_user_id.setOnClickListener(this);
		HomeFragment.regvehicle = false;
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(Plus.API, Plus.PlusOptions.builder().build())
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		Loginbundle = new Bundle();

		intent = getIntent();
		if (intent.hasExtra("remindertype")) {
			Loginbundle.putString("receiveddata",
					intent.getStringExtra("remindertype"));
		}

		if(intent.hasExtra("blocked"))
		{
			Log.v(getClass().getName(), "blocked status");
			
			Toast.makeText(LoginActivity.this, intent.getStringExtra("blocked"), 18000).show();
			/*if(intent.getStringArrayExtra("blocked").length!=0)
			{
						
			}*/
			}
		else if(intent.hasExtra("max_logins"))
		{
			
			if(intent.getStringExtra("max_logins").length()>0)
			{
				Toast.makeText(LoginActivity.this, intent.getStringExtra("max_logins"), 18000).show();			
			}
			
			
		}
		
		
		
		
		
		
		
		PackageManager manager = LoginActivity.this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(LoginActivity.this.getPackageName(),
					0);
			version = info.versionName;

		} catch (NameNotFoundException e) {

		//	e.printStackTrace();
		}
		// mPrefs = getPreferences(MODE_PRIVATE);
		// added by Parag
		scPreferences = new SecurePreferences(this, PREFS_NAME, Constants.key,
				true);
		
		
		/*if(intent.hasExtra("max_logins"))
		{
		
			Log.v("blocked string in intent", "Login activity");
			Toast.makeText(LoginActivity.this, intent.getStringExtra("max_logins"), Toast.LENGTH_LONG).show();
			
			if (scPreferences.containsKey("user_id") &&  scPreferences.containsKey("password")) {
				edtuser_id.setText(scPreferences.getString("user_id"));
				
				edtpassword.setText(scPreferences.getString("password"));			
			}
	

			
			
			
		}*/
		
		
		
		/*
		 * String versionnew = mPrefs.getString("version", "");
		 * if(!(versionnew.equals(""))) {
		 * if(Float.parseFloat(versionnew)>Float.parseFloat(version)) { Intent
		 * intent = new Intent( LoginActivity.this,
		 * VersionUpdateActivity.class); intent.putExtra("version", versionnew);
		 * startActivity(intent);
		 * 
		 * finish(); } }
		 */
		// facebook native sdk

		/*
		 * facebook = new Facebook(APP_ID); mAsyncRunner = new
		 * AsyncFacebookRunner(facebook);
		 */

		// currentcity();
		// getCurrentCity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private Location getLastKnownLocation(LoginActivity loginActivity) {

		Location location = null;
		LocationManager locationmanager = (LocationManager) getSystemService("location");
		List list = locationmanager.getAllProviders();
		boolean i = false;
		Iterator iterator = list.iterator();
		do {
			
			if (!iterator.hasNext())
				break;
			String s = (String) iterator.next();
			
			if (i != false && !locationmanager.isProviderEnabled(s))
				continue;
			
			Location location1 = locationmanager.getLastKnownLocation(s);
			if (location1 == null)
				continue;
			if (location != null) {
			
				float f = location.getAccuracy();
				float f1 = location1.getAccuracy();
				if (f >= f1) {
					long l = location1.getTime();
					long l1 = location.getTime();
					if (l - l1 <= 600000L)
						continue;
				}
			}
			location = location1;
			
			i = locationmanager.isProviderEnabled(s);
			
		} while (true);
		return location;
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (dayWeather != null) {
			outState.putSerializable("dayWeather", dayWeather);
		}
	}

	

	@Override
	protected void onResume() {

		super.onResume();
		// SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		/*
		 * String userid = settings.getString(name, ""); String pwd =
		 * settings.getString(password, ""); edtuser_id.setText(userid);
		 * edtpassword.setText(pwd);
		 */
		getCurrentCity();
		/*
		 * if(!(userid.equals(""))) { edtuser_id.setText(userid);
		 * edtpassword.setText(pwd); singleSignOn(userid ,pwd); }
		 */
		/*
		 * TextView uName = (TextView) findViewById(R.id.textUserId);
		 * uName.setText(username); TextView uPwd = (TextView)
		 * findViewById(R.id.textPassword); uPwd.setText(pwd);
		 */
	}

	@Override
	protected void onStart() {

		super.onStart();
		// mGoogleApiClient.connect();
		mTracker.setScreenName("LoginScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	/*
	 * @Override protected void onStop() {
	 * 
	 * super.onStop(); if (mGoogleApiClient.isConnected()) {
	 * mGoogleApiClient.disconnect(); } //
	 * GoogleAnalytics.getInstance(this).reportActivityStop(this);
	 * 
	 * }
	 */

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.textView1:
			if (!(edtuser_id.getText().toString().equals(""))) {
				mTracker.send(new HitBuilders.EventBuilder()
				.setCategory("ui_action").setAction("button_press")
				.setLabel("Forgot Password").build());

				CheckConnectivity checknow = new CheckConnectivity();
				boolean connect = checknow.checkNow(getApplicationContext());
				if (connect) {
					user_id = edtuser_id.getText().toString();

					user_id = user_id.replaceAll(" ", "%20");
					String request = Config.awsserverurl
							+ "tmsc_ch/customerapp/user/getDetailsForPasswordResetByUserId";
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("user_id",
							user_id.trim()));
					// nameValuePairs.add(new
					// BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
					new AWS_WebServiceCall(v.getContext(), request,
							ServiceHandler.POST,
							Constants.getDetailsForPasswordResetByUserId,
							nameValuePairs, new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {

							

							ArrayList<HashMap<String, String>> userDetailsArray = (ArrayList<HashMap<String, String>>) object;
							forgotPasswordDialog = new Dialog(
									LoginActivity.this);
							forgotPasswordDialog
							.requestWindowFeature(Window.FEATURE_NO_TITLE);
							forgotPasswordDialog
							.setContentView(R.layout.forgot_password_popup);
							forgotPasswordDialog.setCancelable(false);
							forgotPasswordDialog.show();
							emailId = (EditText) forgotPasswordDialog
									.findViewById(R.id.txtmail);
							ImageView close = (ImageView) forgotPasswordDialog
									.findViewById(R.id.imgclose);
							close.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									forgotPasswordDialog.dismiss();
									
								}
							});
							for (int i = 0; i < userDetailsArray.size(); i++) {
								user_email = userDetailsArray.get(i)
										.get("email_id");
								emailId.setText(user_email);
							}

							Button submit = (Button) forgotPasswordDialog
									.findViewById(R.id.btnsubmitmail);
							submit.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									String req = Config.awsserverurl
											+ "tmsc_ch/customerapp/user/sendPasswordToCustomerEmail";
									List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
											5);
									String URL = getResources()
											.getString(R.string.URL);
									String environment = "";

									if (URL.contains("qa")) {
										environment = "QA";
									} else {
										environment = "Production";
									}
									nameValuePairs
									.add(new BasicNameValuePair(
											"user_id", user_id));
									nameValuePairs
									.add(new BasicNameValuePair(
											"customerMailId",
											emailId.getText()
											.toString()
											.trim()));
									nameValuePairs
									.add(new BasicNameValuePair(
											"environment",
											environment));

									new AWS_WebServiceCall(
											v.getContext(),
											req,
											ServiceHandler.POST,
											Constants.sendPasswordToCustomerEmail,
											nameValuePairs,
											new ResponseCallback() {

												@Override
												public void onResponseReceive(
														Object object) {

													Toast.makeText(
															getApplicationContext(),
															"Password sent to your email Id.",
															Toast.LENGTH_SHORT)
															.show();
													/*
													 * specific_type_editbox
													 * .setText("");
													 * customer_feedback_editbox
													 * .setText("");
													 * feedback_types_spinner
													 * .setSelection
													 * (0);
													 */
													forgotPasswordDialog
													.dismiss();
													mTracker.send(new HitBuilders.EventBuilder()
													.setCategory(
															user_id)
															.setAction(
																	"thread_true")
																	.setLabel(
																			"Forgot_pass_mail_sent")
																			.build());
												}

												@Override
												public void onErrorReceive(
														String string) {

													Toast.makeText(
															getApplicationContext(),
															"Mail not sent. Please try again",
															Toast.LENGTH_SHORT)
															.show();

												}
											}).execute();
									forgotPasswordDialog.dismiss();

								}
							});

						}

						@Override
						public void onErrorReceive(String string) {

							if (string
									.equalsIgnoreCase("No internet connection.")) {
								AlertDialog.Builder alertDialog = new AlertDialog.Builder(
										LoginActivity.this);
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
												dialog.cancel();
											}
										});

								// Showing Alert Message
								alertDialog.show();
							}

							else {
								Toast.makeText(getApplicationContext(),
										string, Toast.LENGTH_SHORT)
										.show();
							}

						}
					}).execute();

				} else {
					Toast.makeText(getApplicationContext(),
							"No network connection available",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Please enter your User ID", Toast.LENGTH_SHORT);
				toast.show();
			}

			break;
		case R.id.sign_in:
			/*
			 * Intent intent = new Intent(LoginActivity.this,
			 * HomeActivity.class); startActivity(intent);
			 */
			String userid = edtuser_id.getText().toString();
			password_ = edtpassword.getText().toString();
			Config.appVersion = "";
			Config.resetpassword = "";
			Config.attempt = "";
			Config.pwdExpire = "";
			Config.resetpasswordtoast = "";
			mTracker.send(new HitBuilders.EventBuilder()
			.setCategory("ui_action").setAction("button_press")
			.setLabel("Sign In").build());
			if (!(userid.equals(""))) {
				if (!(password_.equals(""))) {

					// Added by Trupti Reddy
					if (userid.equalsIgnoreCase("or")
							|| userid.equalsIgnoreCase("union")) {
						edtuser_id.setError("No or/union word Allowed");
						edtuser_id.requestFocus();
						edtuser_id.setFocusable(true);

					} else if (password_.equalsIgnoreCase("or")
							|| password_.equalsIgnoreCase("union")) {
						edtpassword.setError("No or/union word Allowed");
						edtpassword.requestFocus();
						edtpassword.setFocusable(true);

					} else if (password_.contains(" ")) {
						edtpassword.setError("No spaces Allowed");
						edtpassword.requestFocus();
						edtpassword.setFocusable(true);

					} else if (password_.contains("'")
							|| password_.contains("-")
							|| password_.contains(";")
							|| password_.contains("|")) {
						edtpassword
						.setError("No special characters '  -  ;  | are Allowed");
						edtpassword.requestFocus();
						edtpassword.setFocusable(true);

					} else {

						// if(checkWhiteListingValidation(userid, password_)){
						CheckConnectivity checknow = new CheckConnectivity();
						boolean connect = checknow
								.checkNow(getApplicationContext());
						if (connect) {
							// getCurrentCity();

							String URL = getApplicationContext().getResources()
									.getString(R.string.URL);
							String environment = "";

							if (URL.contains("qa")) {
								environment = "QA";
							} else {
								environment = "Production";
							}
							String req = Config.awsserverurl
									+ "tmsc_ch/customerapp/user/xml";
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
									3);
							nameValuePairs.add(new BasicNameValuePair(
									"user_id", userid));
							nameValuePairs.add(new BasicNameValuePair(
									"password", password_));
							nameValuePairs.add(new BasicNameValuePair(
									"environment", environment));

							// sending device id at the time of login.
							// Added by Vaibhav (New change)

							 nameValuePairs.add(new
							 BasicNameValuePair("deviceId",
							 FetchDeviceID.getID(LoginActivity.this)));
						
							
							Log.d("env", environment);
							new AWS_WebServiceCall(LoginActivity.this, req,
									ServiceHandler.POST, Constants.user,
									nameValuePairs, new ResponseCallback() {

								@Override
								public void onResponseReceive(
										Object object) {

									boolean res = (boolean) object;
									if (res) {

										if (Config.resetpassword != null
												&& Config.resetpassword
												.contains("Reset password")) {
											if (Float.parseFloat(version)< Float.parseFloat(Config.appVersion)) {

												scPreferences
												.put("version",
														Config.appVersion);
												Intent intent = new Intent(
														LoginActivity.this,
														VersionUpdateActivity.class);
												intent.putExtra(
														"version",
														Config.appVersion);
												startActivity(intent);

												finish();
											} else {

												user_id = edtuser_id
														.getText()
														.toString();
												user_id = user_id
														.replaceAll(
																" ",
																"%20");

												final Dialog dialog_changePassword;
												dialog_changePassword = new Dialog(
														LoginActivity.this);
												dialog_changePassword
												.requestWindowFeature(Window.FEATURE_NO_TITLE);
												dialog_changePassword
												.setContentView(R.layout.dialog_changepassword);
												dialog_changePassword
												.setCancelable(true);
												dialog_changePassword
												.show();
												TextView dialogtitle = (TextView) dialog_changePassword
														.findViewById(R.id.dialogtitle);
												dialogtitle
												.setText("Reset Password");
												final EditText oldpass = (EditText) dialog_changePassword
														.findViewById(R.id.txtoldpass);
												final EditText newpass = (EditText) dialog_changePassword
														.findViewById(R.id.txtnewpass);
												final EditText confirmpass = (EditText) dialog_changePassword
														.findViewById(R.id.txtconfpass);

												final TextView txtpassword = (TextView) dialog_changePassword
														.findViewById(R.id.txtpasswordpolicy);

												final Dialog passworddialog = new Dialog(
														LoginActivity.this);
												passworddialog
												.requestWindowFeature(Window.FEATURE_NO_TITLE);
												passworddialog
												.setContentView(R.layout.passwordpolicy);
												ImageView passworddialogimgclose = (ImageView) passworddialog
														.findViewById(R.id.imgclose);
												final TextView passworddialogtitle = (TextView) passworddialog
														.findViewById(R.id.dialogtitle);
												txtpassword
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(
															View v) {

														passworddialogtitle
														.setText("Password Policy");
														passworddialog
														.show();
													}
												});

												passworddialogimgclose
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(
															View v) {

														passworddialog
														.dismiss();
													}
												});

												ImageView close = (ImageView) dialog_changePassword
														.findViewById(R.id.imgclose);
												close.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(
															View v) {

														dialog_changePassword
														.dismiss();
													}
												});
												Button confirm = (Button) dialog_changePassword
														.findViewById(R.id.visit_site);
												confirm.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(
															View v) {

														
														if (!new CheckConnectivity().checkNow(LoginActivity.this)) {
															
															Toast.makeText(LoginActivity.this, getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
															
														}else
														{
															
														
														String old = oldpass
																.getText()
																.toString();
														final String newp = newpass
																.getText()
																.toString();
														String confirmp = confirmpass
																.getText()
																.toString();
														String userpassword = edtpassword
																.getText()
																.toString();

														/*dialog_changePassword.dismiss();*/
														if (old.equals(userpassword) ) {

															
															
												if (!(newp.equals(""))) {

																if (newp.equals(confirmp)) {
																	
																	
																	if (newp.length()<=15 || newp.length()>=8) {
																		
																	
																	
																	
																		String req = Config.awsserverurl
																			+ "tmsc_ch/customerapp/user/resetPassword";
																	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
																			4);
																	nameValuePairs
																	.add(new BasicNameValuePair(
																			"user_id",
																			user_id.trim()));
																	nameValuePairs
																	.add(new BasicNameValuePair(
																			"oldPassword",
																			oldpass.getText()
																			.toString()));
																	nameValuePairs
																	.add(new BasicNameValuePair(
																			"newPassword",
																			newpass.getText()
																			.toString()));
																	nameValuePairs
																	.add(new BasicNameValuePair(
																			"sessionId",
																			Config.sessionId));



																	new AWS_WebServiceCall(
																			LoginActivity.this,
																			req,
																			ServiceHandler.POST,
																			Constants.resetPassword,
																			nameValuePairs,
																			new ResponseCallback() {

																				@Override
																				public void onResponseReceive(
																						Object object) {

																					boolean changepassword = (boolean) object;

																					if (changepassword) {


																						// Trupti
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
																								Log.e("ERROR",
																										"AES secret key spec error");
																							}

																						}
																						edtpassword.setText("");
																						dialog_changePassword.dismiss();
																						
																						
																						Toast.makeText(
																								getBaseContext(),
																								"Password changed successfully. Please login with new password",
																								Toast.LENGTH_SHORT)
																								.show();
																						login = true;

/*																						Intent intent = new Intent(
																								LoginActivity.this,
																								HomeActivity.class);
																						if (Loginbundle != null) {
																							intent.putExtras(Loginbundle);
																						}
																						startActivity(intent);

																						finish();
																						*/
																						
																						mTracker.send(new HitBuilders.EventBuilder()
																						.setCategory(
																								edtuser_id
																								.getText()
																								.toString())
																								.setAction(
																										"thread_true")
																										.setLabel(
																												"Login")
																												.build());

																					}
																				}

																				@Override
																				public void onErrorReceive(
																						String string) {
																					Log.v("string response", string);
																					Toast.makeText(
																							getApplicationContext(),
																							string.toString(),
																							Toast.LENGTH_LONG)
																							.show();
																					edtuser_id
																					.setFocusable(true);
																					login = false;
																				}
																			})
																	.execute();

																}else
																	{
																	
															Toast.makeText(LoginActivity.this, "Password must be between 8 to 15 characters.", Toast.LENGTH_LONG).show();		
																	
																	
																	}
																	
																	
																}
																
																
																
																else {
																	confirmpass
																	.setError("Password not matching.");
																	confirmpass
																	.setFocusable(true);
																	confirmpass
																	.requestFocus();
																}
															} else {
																newpass.setError("Please enter new password.");
																newpass.setFocusable(true);
																newpass.requestFocus();
															}
														
														
														
												
												
														
														} else {
															oldpass.setError("Password incorrect.");
															oldpass.setFocusable(true);
															oldpass.requestFocus();
														}
													

														
														}
														
													}

												});
											}

										} else if (Config.pwdExpire != null
												&& Config.pwdExpire
												.contains("Maximum logins reached")) {
											
											// pwdExpire variable contains max attempt value
											Toast.makeText(
													getApplicationContext(),
													Config.pwdExpire
													.toString(),
													Toast.LENGTH_SHORT)
													.show();	
											
											
											
										} else if (Config.attempt != null
												&& Config.attempt
												.contains("Max attempt reached. Please contact customerelations@tatatomors.com to get it unblocked")) {
											Log.d(Config.attempt,
													"attempt reach");
											Toast.makeText(
													getApplicationContext(),
													Config.attempt
													.toString(),
													Toast.LENGTH_LONG)
													.show();
										}
										
										else if (Config.success != null
												&& Config.success
												.contains("Success")) {
											if (Float
													.parseFloat(version) < Float
													.parseFloat(Config.appVersion)) {
												/*
												 * SharedPreferences.Editor
												 * editor =
												 * mPrefs.edit();
												 * editor.
												 * putString("version",
												 * Config.appVersion);
												 * editor.commit();
												 */
												scPreferences.put("version",
														Config.appVersion);
												Intent intent = new Intent(
														LoginActivity.this,
														VersionUpdateActivity.class);
												intent.putExtra(
														"version",
														Config.appVersion);
												startActivity(intent);

												finish();
											} else {
												// changes made by Parag
												/*
												 * scPreferences.put(name
												 * ,
												 * edtuser_id.getText()
												 * .toString());
												 * scPreferences
												 * .put(password
												 * ,edtpassword
												 * .getText()
												 * .toString());
												 */
												// Added by Trupti
												if (scPreferences
														.getString(secret_key_sharedPref) != null) {
													String secret_key = scPreferences
															.getString(secret_key_sharedPref);
													// Log.e("SplashScreenActivity",
													// "decrypted sharedprefs secret key:"+secret_key);

													byte[] encodedKey = Base64
															.decode(secret_key,
																	Base64.DEFAULT);

													// Log.e("SplashScreenActivity",
													// "encoded key:"+encodedKey.toString());
													Constants.secrete_key_spec = new SecretKeySpec(
															encodedKey,
															0,
															encodedKey.length,
															"AES");
													// Log.e("SplashScreenActivity",
													// "Constants  sec key spec:"+Constants.secrete_key_spec.toString());

												} else {
													// Added by Trupti
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
													//	e.printStackTrace();
														Log.e("ERROR",
																"AES secret key spec error");
													}

												}
												/*
												 * SharedPreferences
												 * settings =
												 * getSharedPreferences(
												 * PREFS_NAME, 0);
												 * SharedPreferences
												 * .Editor editor =
												 * settings .edit();
												 * editor
												 * .putString(name,
												 * edtuser_id
												 * .getText().
												 * toString());
												 * editor.putString
												 * (password,
												 * edtpassword.getText()
												 * .toString());
												 * editor.commit();
												 */
												// LoginActivity.LoginWay="AppLogin";
												login = true;
												//scPreferences.put("login", "y");	
												scPreferences.put("user_id", edtuser_id.getText().toString());
												scPreferences.put("password", edtpassword.getText().toString());
												
												Intent intent = new Intent(
														LoginActivity.this,
														HomeActivity.class);
												if (Loginbundle != null) {
													intent.putExtras(Loginbundle);
												}
												startActivity(intent);

												finish();
												mTracker.send(new HitBuilders.EventBuilder()
												.setCategory(
														edtuser_id
														.getText()
														.toString())
														.setAction(
																"thread_true")
																.setLabel(
																		"Login")
																		.build());
											}
										}

									} else {
										Toast.makeText(
												getApplicationContext(),
												"Invalid  Credentials, please check User ID and Password",
												Toast.LENGTH_LONG)
												.show();
										edtuser_id.setFocusable(true);
										login = false;
									}

								
								
								}

								@Override
								public void onErrorReceive(String string) {

									if ("No internet connection"
											.equalsIgnoreCase(string)) {
										AlertDialog.Builder alertDialog = new AlertDialog.Builder(
												LoginActivity.this);
										// Setting Dialog Title
										alertDialog.setTitle("");
										// Setting Dialog Message
										alertDialog
										.setMessage("Couldn't connect to the Server. Please check your Network Connection or Entered User ID & Password and try again.");
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
														dialog.cancel();
													}
												});
										// Showing Alert Message
										alertDialog.show();
									} else {
										Toast.makeText(
												getApplicationContext(),
												"Invalid  Credentials, please check User ID and Password",
												Toast.LENGTH_LONG)
												.show();
										edtuser_id.setFocusable(true);
										login = false;
									}
									login = false;
								}

							}).execute();
							// ///// end of if connection
						} else {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"No network connection available",
									Toast.LENGTH_SHORT);
							toast.show();
						}

					}//

					// new AuthenticateUser(req, userid, password_).execute();
					// end of password if
				} else {
					Toast.makeText(getApplicationContext(),
							"Please enter Password", Toast.LENGTH_SHORT).show();
				}
				// end of userid if
			} else
				Toast.makeText(getApplicationContext(),
						"Please enter User ID and Password", Toast.LENGTH_SHORT)
						.show();
			break;

		case R.id.sign_up:
			// getCurrentCity();
			mTracker.send(new HitBuilders.EventBuilder()
			.setCategory("ui_action").setAction("button_press")
			.setLabel("Sign Up").build());
			Intent intent1 = new Intent(LoginActivity.this,
					RegisterUserActivity.class);

			startActivity(intent1);
			finish();
			break;

		case R.id.imggoogle:
			/*
			 * Log.d("Inside click", "OK");
			 * 
			 * if (mGoogleApiClient.isConnected()) {
			 * Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			 * mGoogleApiClient.disconnect(); mGoogleApiClient.connect();
			 * Log.d("Inside click" , "Connect"); }
			 * 
			 * if (!mGoogleApiClient.isConnecting()) { mSignInClicked = true; //
			 * resolveSignInError(); if (mConnectionResult.hasResolution()) {
			 * try { mIntentInProgress = true;
			 * mConnectionResult.startResolutionForResult(this, RC_SIGN_IN); }
			 * catch (SendIntentException e) { mIntentInProgress = false;
			 * mGoogleApiClient.connect(); } } } // signInWithGplus(); break;
			 * case R.id.imgfacebook: //loginToFacebook(); break;
			 */
		
		
		case R.id.forgot_user_id:
			forgotUserIdDialog();
		
		}
	}

	
	
	
	private void forgotUserIdDialog()
	{
		
		forgotPasswordDialog = new Dialog(
				LoginActivity.this);
		forgotPasswordDialog
		.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgotPasswordDialog
		.setContentView(R.layout.forgot_userid_popup);
		forgotPasswordDialog.setCancelable(false);
		forgotPasswordDialog.show();
		emailId = (EditText) forgotPasswordDialog
				.findViewById(R.id.txt_email);
		ImageView close = (ImageView) forgotPasswordDialog
				.findViewById(R.id.dismissDialogBtn);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				forgotPasswordDialog.dismiss();
				
			}
		});
		
		Button submit = (Button) forgotPasswordDialog
				.findViewById(R.id.btn_submit_mail);
		
	
	
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				if(new CheckConnectivity().checkNow(LoginActivity.this))
				{
						
				
				
				if(emailId.getText().toString().trim().equals(""))
				{
					emailId.setError(getResources().getString(R.string.empty_email_warning));
				}
				
			else if(!new CheckValidations().validate(emailId.getText().toString().trim()))
				{
					emailId.setError(getResources().getString(R.string.invalid_email_warning));
				
				}
				
				else
				{
					recoverUser(emailId.getText().toString().trim());
										
					
				}
				}
			
				else
				{
					Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
					
				}
				
				
				
				
			}
		});
	
	
	}
	
	

	
	
	private void recoverUser(final String email_id)
	{
		


		String req = Config.awsserverurl
				+ "tmsc_ch/customerapp/user/forgotUserId";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				2);
		String URL = getResources()
				.getString(R.string.URL);
		String environment = "";

		if (URL.contains("qa")) {
			environment = "QA";
		} else {
			environment = "Production";
		}
		
		nameValuePairs
		.add(new BasicNameValuePair(
				"customerMailId",
				email_id));
		nameValuePairs
		.add(new BasicNameValuePair(
				"environment",
				environment));

		new AWS_WebServiceCall(
				LoginActivity.this,
				req,
				ServiceHandler.POST,
				Constants.forgotUserId,
				nameValuePairs,
				new ResponseCallback() {

					@Override
					public void onResponseReceive(
							Object object) {

						Toast.makeText(
								getApplicationContext(),
								"Your User ID will be sent to your registered email id.",
								Toast.LENGTH_SHORT)
								.show();
						/*
						 * specific_type_editbox
						 * .setText("");
						 * customer_feedback_editbox
						 * .setText("");
						 * feedback_types_spinner
						 * .setSelection
						 * (0);
						 */
						forgotPasswordDialog
						.dismiss();
						mTracker.send(new HitBuilders.EventBuilder()
						.setCategory(
								email_id)
								.setAction(
										"thread_true")
										.setLabel(
												"Forgot_email_mail_sent")
												.build());
					}

					@Override
					public void onErrorReceive(
							String string) {

						Toast.makeText(
								getApplicationContext(),
								"This email ID does not exist",
								Toast.LENGTH_SHORT)
								.show();

					}
				}).execute();
		forgotPasswordDialog.dismiss();

	
		
		
	}
	
	
	
	private boolean checkWhiteListingValidation(String userid, String password_) {
		// Added by Trupti Reddy
		if (userid.equalsIgnoreCase("or") || userid.equalsIgnoreCase("union")) {
			edtuser_id.setError("No or/union word Allowed");
			edtuser_id.requestFocus();
			edtuser_id.setFocusable(true);
			return false;
		} else if (password_.equalsIgnoreCase("or")
				|| password_.equalsIgnoreCase("union")) {
			edtpassword.setError("No or/union word Allowed");
			edtpassword.requestFocus();
			edtpassword.setFocusable(true);
			return false;
		} else if (password_.contains(" ")) {
			edtpassword.setError("No spaces allowed");
			edtpassword.requestFocus();
			edtpassword.setFocusable(true);
			return false;
		} else if (password_.contains("'") || password_.contains("-")
				|| password_.contains(";") || password_.contains("|")) {
			edtpassword
			.setError("No special characters '  -  ;  | are Allowed");
			edtpassword.requestFocus();
			edtpassword.setFocusable(true);
			return false;
		} else
			return true;
	}

	@Override
	public void onBackPressed() {
		// Display alert message when back button has been pressed
		// backButtonHandler();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				LoginActivity.this);
		// Setting Dialog Title
		alertDialog.setTitle("Exit Application?");
		// Setting Dialog Message
		alertDialog
		.setMessage("Are you sure you want to Exit the Application?");
		// Setting Icon to Dialog
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// finish();
				dialog.dismiss();
				finishAffinity();
			}
		});
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to invoke NO event
				dialog.cancel();
			}
		});
		// Showing Alert Message
		alertDialog.show();
		// return;
	}

	/**
	 * Sign-out from google
	 * */
	/*
	 * public void signOutFromGplus() { if (mGoogleApiClient.isConnected()) {
	 * Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	 * mGoogleApiClient.disconnect(); mGoogleApiClient.connect();
	 * 
	 * } }
	 */

	/*
	 * public void backButtonHandler() { AlertDialog.Builder alertDialog = new
	 * AlertDialog.Builder( LoginActivity.this); // Setting Dialog Title
	 * alertDialog.setTitle("Exit Application?"); // Setting Dialog Message
	 * alertDialog
	 * .setMessage("Are you sure you want to Exit the Application?"); // Setting
	 * Icon to Dialog alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
	 * // Setting Positive "Yes" Button alertDialog.setPositiveButton("YES", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { finish(); } }); // Setting Negative "NO" Button
	 * alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
	 * { public void onClick(DialogInterface dialog, int which) { // Write your
	 * code here to invoke NO event dialog.cancel(); } }); // Showing Alert
	 * Message alertDialog.show(); }
	 */

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

		/*
		 * Toast.makeText(getApplicationContext(), "GPS turned on ",
		 * Toast.LENGTH_SHORT).show();
		 */
	}

	@Override
	public void onProviderDisabled(String provider) {

		/*
		 * Toast.makeText(getApplicationContext(), "GPS turned off ",
		 * Toast.LENGTH_SHORT).show();
		 */

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {

		// super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	/*
	 * @Override public void onConnectionFailed(ConnectionResult result) {
	 * 
	 * if (!result.hasResolution()) {
	 * GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
	 * 0).show(); return; }
	 * 
	 * if (!mIntentInProgress) { // Store the ConnectionResult for later usage
	 * mConnectionResult = result;
	 * 
	 * if (mSignInClicked) {
	 * 
	 * //resolveSignInError(); if (mConnectionResult.hasResolution()) { try {
	 * mIntentInProgress = true;
	 * mConnectionResult.startResolutionForResult(this, RC_SIGN_IN); } catch
	 * (SendIntentException e) { mIntentInProgress = false;
	 * mGoogleApiClient.connect(); } } } }
	 * 
	 * }
	 */

	/*
	 * private void resolveSignInError() { if
	 * (mConnectionResult.hasResolution()) { try { mIntentInProgress = true;
	 * mConnectionResult.startResolutionForResult(this, RC_SIGN_IN); } catch
	 * (SendIntentException e) { mIntentInProgress = false;
	 * mGoogleApiClient.connect(); } } }
	 */

	@Override
	public void onConnected(Bundle arg0) {

		// mSignInClicked = false;
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		getCurrentCity();
		// getProfileInformation();
	}

	/*
	 * @Override public void onConnectionSuspended(int arg0) {
	 * 
	 * mGoogleApiClient.connect(); }
	 */

	/*
	 * private void getProfileInformation() { try { if
	 * (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) { Person
	 * currentPerson = Plus.PeopleApi .getCurrentPerson(mGoogleApiClient);
	 * personName = currentPerson.getDisplayName(); personPhotoUrl =
	 * currentPerson.getImage().getUrl(); personGooglePlusProfile =
	 * currentPerson.getUrl(); email =
	 * Plus.AccountApi.getAccountName(mGoogleApiClient); // Log.d("USer",
	 * currentPerson.toString());
	 * 
	 * if (currentPerson.getGender() == 1) { UserDetails.setGender("Female"); }
	 * else { UserDetails.setGender("Male"); } UserDetails.setAddress("");
	 * UserDetails.setCity("");
	 * UserDetails.setFirst_name(currentPerson.getName() .getGivenName());
	 * UserDetails.setLast_name(currentPerson.getName() .getFamilyName());
	 * UserDetails.setEmail_id(email); UserDetails.setPhotourl(personPhotoUrl);
	 * String URL = getApplicationContext().getResources().getString(
	 * R.string.URL); String environment = "";
	 * 
	 * if (URL.contains("qa")) { environment = "QA"; } else { environment =
	 * "Production"; } String req =
	 * Config.awsserverurl+"CustomerApp_Restws/customerapp/user/xml";
	 * List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 3);
	 * nameValuePairs.add(new BasicNameValuePair("user_id", email));
	 * nameValuePairs.add(new BasicNameValuePair("password", ""));
	 * nameValuePairs.add(new BasicNameValuePair("environment", environment));
	 * Log.d("env", environment); new AWS_WebServiceCall(LoginActivity.this,
	 * req, ServiceHandler.POST, Constants.user, nameValuePairs, new
	 * ResponseCallback() {
	 * 
	 * @Override public void onResponseReceive(Object object) {
	 * 
	 * boolean res = (boolean) object; if (res) {
	 * 
	 * SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	 * SharedPreferences.Editor editor = settings.edit(); editor.putString(name,
	 * edtuser_id.getText().toString()); editor.putString(password,
	 * edtpassword.getText().toString()); editor.commit(); //
	 * LoginActivity.LoginWay="AppLogin";
	 * 
	 * SharedPreferences settings = getSharedPreferences( PREFS_NAME, 0);
	 * SharedPreferences.Editor editor = settings .edit();
	 * editor.putString(name, email); editor.putString(password, "");
	 * editor.commit(); UserDetails.setUser_id(email); Intent intent = new
	 * Intent( LoginActivity.this, HomeActivity.class);
	 * 
	 * startActivity(intent); } else { UserDetails.setUser_id(""); Intent intent
	 * = new Intent( LoginActivity.this, HomeActivity.class);
	 * 
	 * startActivity(intent);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * @Override public void onErrorReceive(String string) {
	 * 
	 * UserDetails.setUser_id(""); Intent intent = new
	 * Intent(LoginActivity.this, HomeActivity.class);
	 * 
	 * startActivity(intent);
	 * 
	 * }
	 * 
	 * }).execute(); // UserDetails.setUser_id(email); //
	 * LoginActivity.LoginWay="SocialLogin";
	 * 
	 * Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
	 * 
	 * startActivity(intent);
	 * 
	 * } else { Toast.makeText(getApplicationContext(),
	 * "Person information is null", Toast.LENGTH_LONG).show(); } } catch
	 * (Exception e) { e.printStackTrace(); } }
	 */

	/*
	 * public void loginToFacebook() { mPrefs = getPreferences(MODE_PRIVATE);
	 * String access_token = mPrefs.getString("access_token", null); long
	 * expires = mPrefs.getLong("access_expires", 0); Log.d("Error", "here"); if
	 * (access_token != null) { facebook.setAccessToken(access_token); } if
	 * (expires != 0) { facebook.setAccessExpires(expires); } String[]
	 * PERMISSIONS = new String[] { "public_profile", "email", "user_friends"
	 * };// ,"user","id","name" , "first_name" // ,"last_name", "gender" //
	 * };//,"picture.type(large)"};
	 * 
	 * if (!facebook.isSessionValid()) {
	 * 
	 * facebook.authorize(this, PERMISSIONS, facebook.FORCE_DIALOG_AUTH, new
	 * DialogListener() {
	 * 
	 * @Override public void onCancel() { // Function to handle cancel // event
	 * Log.d("Error", "here2"); }
	 * 
	 * @Override public void onComplete(Bundle values) { // Function to handle
	 * complete event // Edit // Preferences and update facebook acess_token
	 * SharedPreferences.Editor editor = mPrefs.edit();
	 * editor.putString("access_token", facebook.getAccessToken());
	 * editor.putLong("access_expires", facebook.getAccessExpires());
	 * editor.commit();
	 * 
	 * Toast.makeText(getApplicationContext(), "App login",
	 * Toast.LENGTH_LONG).show(); getFacebookProfileInformation(); }
	 * 
	 * @Override public void onError(DialogError error) { // Function to //
	 * handl // error System.out.println(error); }
	 * 
	 * @Override public void onFacebookError(FacebookError fberror) { //
	 * Function to handle Facebook errors System.out.println(fberror); }
	 * 
	 * }); } }
	 * 
	 * public void getFacebookProfileInformation() { mAsyncRunner.request("me",
	 * new RequestListener() {
	 * 
	 * @Override public void onComplete(String response, Object state) {
	 * Log.d("Profile", response); String json = response; try { JSONObject
	 * profile = new JSONObject(json); JSONObject p =
	 * Util.parseJson(facebook.request("me")); // getting // name // of // the
	 * // user // final final String name = p.getString("name"); // getting
	 * email of // the user // final String email = p.getString("email"); final
	 * String id = p.getString("id"); runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { Toast.makeText(getApplicationContext(),
	 * "Name: " + name + "\nEmail: " + id, Toast.LENGTH_LONG).show(); }
	 * 
	 * });
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); } catch
	 * (MalformedURLException e) {
	 * 
	 * // Auto-generated catch block e.printStackTrace(); } catch (IOException
	 * e) { // Auto-generated catch block e.printStackTrace(); } catch
	 * (FacebookError e) { // Auto-generated catch block e.printStackTrace(); }
	 * }
	 * 
	 * @Override public void onIOException(IOException e, Object state) { }
	 * 
	 * @Override public void onFileNotFoundException(FileNotFoundException e,
	 * Object state) { }
	 * 
	 * @Override public void onMalformedURLException(MalformedURLException e,
	 * Object state) { }
	 * 
	 * @Override public void onFacebookError(FacebookError e, Object state) { }
	 * }); }
	 * 
	 * public void logoutFromFacebook() { mAsyncRunner.logout(this, new
	 * RequestListener() {
	 * 
	 * @Override public void onComplete(String response, Object state) {
	 * Log.d("Logout from Facebook", response); if
	 * (Boolean.parseBoolean(response) == true) { // User // successfully //
	 * Logged out } } }
	 * 
	 * @Override public void onIOException(IOException e, Object state) { }
	 * 
	 * @Override public void onFileNotFoundException(FileNotFoundException e,
	 * Object state) { }
	 * 
	 * @Override public void onMalformedURLException(MalformedURLException e,
	 * Object state) { }
	 * 
	 * @Override public void onFacebookError(FacebookError e, Object state) { }
	 * }); }
	 */

	/*
	 * public void singleSignOn(final String userid, String password_) {
	 * CheckConnectivity checknow = new CheckConnectivity(); boolean connect =
	 * checknow.checkNow(getApplicationContext()); if (connect) {
	 * 
	 * String URL = getApplicationContext().getResources().getString(
	 * R.string.URL); String environment = "";
	 * 
	 * if (URL.contains("qa")) { environment = "QA"; } else { environment =
	 * "Production"; } String req =
	 * Config.awsserverurl+"CustomerApp_Restws/customerapp/user/xml";
	 * List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	 * nameValuePairs.add(new BasicNameValuePair("user_id", userid));
	 * nameValuePairs.add(new BasicNameValuePair("password", password_));
	 * nameValuePairs.add(new BasicNameValuePair("environment", environment));
	 * Log.d("env", environment); new AWS_WebServiceCall(LoginActivity.this,
	 * req, ServiceHandler.POST, Constants.user, nameValuePairs, new
	 * ResponseCallback() {
	 * 
	 * @Override public void onResponseReceive(Object object) {
	 * 
	 * boolean res = (boolean) object; if (res) {
	 * 
	 * SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	 * SharedPreferences.Editor editor = settings.edit(); editor.putString(name,
	 * edtuser_id.getText().toString()); editor.putString(password,
	 * edtpassword.getText().toString()); editor.commit();
	 * 
	 * // LoginActivity.LoginWay="AppLogin"; UserDetails.setUser_id(userid);
	 * Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
	 * 
	 * startActivity(intent); finish(); } else { Toast.makeText(
	 * getApplicationContext(),
	 * "Invalid credentials, please check username passsword you entered.",
	 * Toast.LENGTH_LONG).show(); edtuser_id.setFocusable(true);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * @Override public void onErrorReceive(String string) {
	 * 
	 * Toast.makeText( getApplicationContext(),
	 * "Invalid credentials, please check username passsword you entered.",
	 * Toast.LENGTH_LONG).show(); edtuser_id.setFocusable(true);
	 * 
	 * }
	 * 
	 * }).execute(); } else { Toast toast =
	 * Toast.makeText(getApplicationContext(),
	 * "No network connection available.", Toast.LENGTH_LONG);
	 * toast.setGravity(Gravity.CENTER, 0, 0); toast.show(); } }
	 */

	public void getCurrentCity() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		}

		if (location != null) {

			MyLat = location.getLatitude();
			MyLong = location.getLongitude();

		} else {
			Location loc = getLastKnownLocation(this);
			if (loc != null) {

				MyLat = loc.getLatitude();
				MyLong = loc.getLongitude();

			}
		}

		try {
			// Getting address from found locations.
			Geocoder geocoder;

			List<Address> addresses;
			geocoder = new Geocoder(this, Locale.getDefault());
			addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

			StateName = addresses.get(0).getAdminArea();
			CityName = addresses.get(0).getLocality();
			CityName1 = CityName.replace(" ", "%20");
			CityName1 = CityName1.trim();
			CountryName = addresses.get(0).getCountryName();
			current_address = addresses.get(0).getAddressLine(0);
			pin_code = addresses.get(0).getPostalCode();
			// you can get more details other than this . like country code,
			// state code, etc.

		} catch (Exception e) {
			//e.printStackTrace();
		}

		if (CityName1 != null) {
			WeatherWebservice weatherWS = new WeatherWebservice(
					new FragmentCallback() {

						@Override
						public void onTaskDone(ArrayList<Weather> result) {

							if (result != null) {
								if (result.size() > 0
										&& result.get(0).isFetched) {
									dayWeather = result.get(0);
									// Update UI
									// updateUIwithWeather(dayWeather);
									if (dayWeather != null) {
										city = dayWeather.place;
										temp = String.format("%.1fºC",
												dayWeather.temperature);
										icon = dayWeather.iconUri;
										/*
										 * Loginbundle.putString("getcity",
										 * dayWeather.place);
										 * Loginbundle.putString("gettemp",
										 * String.format("%.1fºC",
										 * dayWeather.temperature));
										 * Loginbundle.putString("geticon",
										 * dayWeather.iconUri);
										 */

									} else {
										city = "";
										temp = "";
										icon = "";
									}
								}
							}
						}
					}, null, true, CityName1.toString());

			weatherWS.execute();
		} else {
			Toast.makeText(this,
					getResources().getString(R.string.network_error),
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}
}
