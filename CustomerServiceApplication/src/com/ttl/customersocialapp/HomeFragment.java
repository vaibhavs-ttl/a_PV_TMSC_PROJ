package com.ttl.customersocialapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.communication.SecurePreferences;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.helper.FragmentCallback;
import com.ttl.helper.VehicleRegister;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.model.Weather;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
import com.ttl.webservice.WeatherWebservice;

public class HomeFragment extends Fragment implements OnClickListener,
		LocationListener {
	
	private View rootView;
	private Fragment fragment = null;
	private AlertDialog warning_dialog;
	private LinearLayout servicebook, montenancecost, servicehistory, feedback,
			vehicledetails, customerdetails, mydocumnets, compliantreg,compliantreg1,
			costcalculator, notification, dealerLocator, gpsTripMeter,
			infoandterms, newandupdate, customerdeatils2;
	private ImageView imagetemp;
	private ImageView complaint_image_click;
	private ImageView complaint_image_click1;
	private TextView complaint_text_click1;
	private final String regnumber_share = "Registration_KEY";
	private final String email_share = "email_KEY";
	private final String user_share = "user_KEY";
	private SecurePreferences scPreferences;
	private Dialog dialog;
	private TextView txttemp, textcity, txtdate;
	private RelativeLayout weatherLayout;
	private LinearLayout button_6, button_9;
	static public boolean regvehicle = false, psfNotifiction=false;
	private Dialog proceeddialog, vehiclenodialog, mobilenumberdialog,
			contactdealerdialog, otpdialog;
	private Button proceed, notnow;

	public static boolean weather = false;
	public static String ntfstring;
	private ListView notifylist;
	private CustomAdapter adapter;
	private ArrayList<HashMap<String, String>> notifictionlist;
	private ArrayList<HashMap<String, String>> addlist = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> PSF_notifictionlist;
	public static String GetNotificationType;
	public static boolean Notificationchk = false;
	private Bundle getbundle;
	private Bundle bundle;
	
	private ImageView img_bell;
	static public boolean dealerDialog = false;
	
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder mBuilder;
	public static Handler mHandler = new Handler();
	private boolean reminder = false;
	public static Runnable runnable;
	public static ImageView typeimg;
	private LocationManager locationManager;
	static Weather dayWeather;
	private double MyLat, MyLong;
	private String CityName = "",CityName1="";
	private String StateName = "";
	private String CountryName = "";
	private String current_address = "", pin_code = "", dayOfTheWeek, stringMonth, day;
	private Location location;
	public static String city, temp, icon;
	private String Reg_number,jobcard_number,services_type,services_date;
	private boolean connect;
	
	//GCM variables
	 private AsyncTask<Void, Void, String> createRegIdTask;
	 private final String PREFS_NAME = "CREDENTIALS"; 	 
	 public static final String REG_ID = "regId";
	 public static final String EMAIL_ID = "eMailId";
	 public static final String UserID = "userId";
	 private String regId = "";
	 private GoogleCloudMessaging gcmObj;
	 private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	 private Tracker mTracker;
	 private static String imeinumber;
	 boolean imeiexist = false;
	 CheckConnectivity checknow;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		checknow = new CheckConnectivity();
		connect = checknow
				.checkNow(getActivity());
		img_bell = (ImageView) rootView.findViewById(R.id.img_bell);
		/*
		 * bundle = getArguments(); if (bundle != null) {
		 * Log.i("BUNDLE HomeFragment", bundle.getString("DATE") + " " +
		 * bundle.getString("SRNO") + " " + bundle.getString("REGNO"));
		 * img_bell.setVisibility(View.VISIBLE); alartDate =
		 * bundle.getString("DATE"); alarmSRNO = bundle.getString("SRNO");
		 * alarmRegNo = bundle.getString("REGNO");
		 * 
		 * } else img_bell.setVisibility(View.GONE);
		 */
		scPreferences = new SecurePreferences(getActivity(), PREFS_NAME, Constants.key, true);
		img_bell.setVisibility(View.GONE);
		AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
		mTracker = application.getDefaultTracker();
		/*
		 * if(Reminder_Fragment.reminderget) {
		 * img_bell.setVisibility(View.VISIBLE); }else {
		 * img_bell.setVisibility(View.GONE); }
		 */
		/*
		 * dialog = new Dialog(getActivity());
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * dialog.setContentView(R.layout.progress_bar);
		 * dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 * dialog.setCancelable(true); dialog.show();
		 */
		
		servicebook = (LinearLayout) rootView.findViewById(R.id.servicebooking);
		montenancecost = (LinearLayout) rootView
				.findViewById(R.id.maintenncecost);
		servicehistory = (LinearLayout) rootView
				.findViewById(R.id.servicehistory);
		feedback = (LinearLayout) rootView.findViewById(R.id.feedback);
		vehicledetails = (LinearLayout) rootView
				.findViewById(R.id.vehicledetails);
		customerdetails = (LinearLayout) rootView
				.findViewById(R.id.customerdetails);
		mydocumnets = (LinearLayout) rootView.findViewById(R.id.mydocument);
		compliantreg = (LinearLayout) rootView.findViewById(R.id.complaintreg12);
		compliantreg1 = (LinearLayout) rootView.findViewById(R.id.complaintreg_layout);
		complaint_image_click=(ImageView)rootView.findViewById(R.id.complaint_image_click);
		complaint_image_click1=(ImageView)rootView.findViewById(R.id.complaint_image_click1);
		complaint_text_click1=(TextView)rootView.findViewById(R.id.complaint_text_click1);
		costcalculator = (LinearLayout) rootView.findViewById(R.id.costcal);

		//notification = (LinearLayout) rootView.findViewById(R.id.notifications);
		dealerLocator = (LinearLayout) rootView
				.findViewById(R.id.dealerlocator);
		gpsTripMeter = (LinearLayout) rootView.findViewById(R.id.gpstripmeter);
		infoandterms = (LinearLayout) rootView.findViewById(R.id.infoandterms);
		newandupdate = (LinearLayout) rootView
				.findViewById(R.id.newsandupdates);
		customerdeatils2 = (LinearLayout) rootView
				.findViewById(R.id.customerdetailswithout);

		imagetemp = (ImageView) rootView.findViewById(R.id.imgtemp);
		txttemp = (TextView) rootView.findViewById(R.id.txttemp);
		textcity = (TextView) rootView.findViewById(R.id.txtcity);
		txtdate = (TextView) rootView.findViewById(R.id.txtdate);
		servicebook.setOnClickListener(this);
		montenancecost.setOnClickListener(this);
		servicehistory.setOnClickListener(this);
		feedback.setOnClickListener(this);
		vehicledetails.setOnClickListener(this);
		customerdetails.setOnClickListener(this);
		mydocumnets.setOnClickListener(this);
		compliantreg.setOnClickListener(this);
		costcalculator.setOnClickListener(this);

		//notification.setOnClickListener(this);
		dealerLocator.setOnClickListener(this);
		gpsTripMeter.setOnClickListener(this);
		infoandterms.setOnClickListener(this);
		newandupdate.setOnClickListener(this);
		customerdeatils2.setOnClickListener(this);
		complaint_image_click.setOnClickListener(this);
		complaint_image_click1.setOnClickListener(this);
		complaint_text_click1.setOnClickListener(this);
		button_6 = (LinearLayout) rootView.findViewById(R.id.layout3);
		button_9 = (LinearLayout) rootView.findViewById(R.id.layout2);
		
		checknow=new CheckConnectivity();
		connect=checknow.checkNow(getActivity());
		
		
		if (connect) {
			if(new UserDetails().getRegNumberList().size()==0 || new UserDetails().getRegNumberList()==null )
			{
				if(!(UserDetails.getUser_id().equals("")))
					{
						getVehicles();
					}
				else
					{
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(getActivity().getResources().getString(R.string.app_name));
					builder.setMessage("Your Session has expired, Please restart app.").setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									dialog.cancel();
									/*Intent mainIntent3 = new Intent(context,SplashScreenActivity.class);			
									mainIntent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									context.startActivity(mainIntent3);*/
									System.exit(0);
								}
							});
					AlertDialog alert = builder.create();
					alert.setCancelable(false);
					alert.show();
					}
			}else
			{
				notification();
				getFeedbacknotifications();
			}
		}
		 else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	
		if(scPreferences.getString(regnumber_share)!=null){
			
			String registrationId = scPreferences.getString(regnumber_share);
		
		/* SharedPreferences prefs = getActivity().getSharedPreferences("UserDetails",
	                Context.MODE_PRIVATE);
	        String registrationId = prefs.getString(REG_ID, "");*/
	        if (!TextUtils.isEmpty(registrationId)) {
	           // Log.d("reg id", registrationId);
	     
	            if(!(scPreferences.getString(user_share).equals(UserDetails.getUser_id())))
	            		{
	            	TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
	            	 
	            	String imeinumber = telephonyManager.getDeviceId();
		            String req = Config.awsserverurl+"tmsc_ch/customerapp/gcmuser/updategcmid";
	                List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(5);
	        		nameValuePairs1.add(new BasicNameValuePair("name",
	        				UserDetails.getFirst_name()+" "+UserDetails.getLast_name()));
	        		nameValuePairs1
	        				.add(new BasicNameValuePair("email", UserDetails.getEmail_id()));
	        		nameValuePairs1.add(new BasicNameValuePair("regid", registrationId));
	        		nameValuePairs1.add(new BasicNameValuePair("imei_number",
							imeinumber));
	        		nameValuePairs1.add(new BasicNameValuePair("user_id", UserDetails.getUser_id()));
	        		  nameValuePairs1.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
	        		System.out.println("Namevaluepair"+nameValuePairs1.get(0));
	        		new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST, Constants.gcmuser,nameValuePairs1, new ResponseCallback() {
	        			
	        			@Override
	        			public void onResponseReceive(Object object) {
	        				
	        			
	        				
	        			
        					
                           //  SharedPreferences.Editor editor = prefs.edit();
                             //editor.putString(REG_ID, regId);
                             scPreferences.put(regnumber_share, regId);
                             //editor.putString(EMAIL_ID, UserDetails.getEmail_id());
                             scPreferences.put(email_share, EMAIL_ID);
                          //   editor.putString(UserID, UserDetails.getUser_id());
                             scPreferences.put(user_share, UserID);
                            // editor.commit();
	        			}
	        			
	        			@Override
	        			public void onErrorReceive(String string) {
	        				
	        				
	        			}
	        		}).execute();
	            		}
	        }else
	        {
	        	/* TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                         .getSystemService(Context.TELEPHONY_SERVICE);
            	 
            	 imeinumber = telephonyManager.getDeviceId();
            	 for(int i = 0; i<UserDetails.getGcmlist().size(); i++)
            	 {
            		 if(imeinumber.equals(UserDetails.getGcmlist().get(i).get("imei_number")))
            		 {
            			 SharedPreferences prefs1 = getActivity().getSharedPreferences("UserDetails",
                                 Context.MODE_PRIVATE);
                         SharedPreferences.Editor editor = prefs1.edit();
                         editor.putString(REG_ID, UserDetails.getGcmlist().get(i).get("gcm_reg_id"));
                         editor.putString(EMAIL_ID, UserDetails.getEmail_id());
                         editor.commit();
                         imeiexist = true;
            		 }
            		 
            	 }
	        	if(!imeiexist)
	        	{*/
	        	
	        	if (checkPlayServices()) {
	        	
	                // Register Device in GCM Server
	                registerInBackground();
	            }
	        	//}
	        }
	}else
    {
    	/* TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                 .getSystemService(Context.TELEPHONY_SERVICE);
    	 
    	 imeinumber = telephonyManager.getDeviceId();
    	 for(int i = 0; i<UserDetails.getGcmlist().size(); i++)
    	 {
    		 if(imeinumber.equals(UserDetails.getGcmlist().get(i).get("imei_number")))
    		 {
    			 SharedPreferences prefs1 = getActivity().getSharedPreferences("UserDetails",
                         Context.MODE_PRIVATE);
                 SharedPreferences.Editor editor = prefs1.edit();
                 editor.putString(REG_ID, UserDetails.getGcmlist().get(i).get("gcm_reg_id"));
                 editor.putString(EMAIL_ID, UserDetails.getEmail_id());
                 editor.commit();
                 imeiexist = true;
    		 }
    		 
    	 }
    	if(!imeiexist)
    	{*/
    	
    	if (checkPlayServices()) {
    		
            // Register Device in GCM Server
            registerInBackground();
        }
    	//}
    }

		Date date = new Date();
		dayOfTheWeek = (String) android.text.format.DateFormat
				.format("E", date);// Thursday
		stringMonth = (String) android.text.format.DateFormat.format("MMM",
				date); // Jun
		day = (String) android.text.format.DateFormat.format("dd", date); // 20
	//	Log.d(dayOfTheWeek + stringMonth + day, "day format");
		getbundle = getArguments();

		weatherLayout = (RelativeLayout) rootView
				.findViewById(R.id.relativeLayout1);
		weatherLayout.setVisibility(View.GONE);
		if (SplashScreenActivity.chkweather == true) {
			// Toast.makeText(getActivity(), "Weather",
			// Toast.LENGTH_SHORT).show();
			getCurrentCity();

			weatherLayout.setVisibility(View.VISIBLE);

		} else {
			if (temp != null) {
				weatherLayout.setVisibility(View.VISIBLE);

				txtdate.setText(dayOfTheWeek + ", " + stringMonth + " " + day);
				txttemp.setText(temp);
				//Log.d("get temp", temp);
				textcity.setText(city);
			//	Log.d("get city", city);
				if (icon != null) {
					if (icon.equalsIgnoreCase("01d")) {
						imagetemp.setBackgroundResource(R.drawable.mon);
					} else if (icon.equalsIgnoreCase("10d")) {
						imagetemp.setBackgroundResource(R.drawable.tue);
					} else if (icon.equalsIgnoreCase("03d")) {
						imagetemp.setBackgroundResource(R.drawable.rain);
					} else if (icon.equalsIgnoreCase("04d")) {
						imagetemp.setBackgroundResource(R.drawable.thu);
					} else if (icon.equalsIgnoreCase("02d")) {
						imagetemp.setBackgroundResource(R.drawable.maintemp);
					} else if (icon.equalsIgnoreCase("50d")) {
						imagetemp.setBackgroundResource(R.drawable.mon);
					} else if (icon.equalsIgnoreCase("01n")
							|| icon.equalsIgnoreCase("03n")) {
						imagetemp.setBackgroundResource(R.drawable.night_clear);
					} else if (icon.equalsIgnoreCase("02n")
							|| icon.equalsIgnoreCase("04n")
							|| icon.equalsIgnoreCase("13n")) {
						imagetemp
								.setBackgroundResource(R.drawable.night_cloudy);
					} else if (icon.equalsIgnoreCase("09n")
							|| icon.equalsIgnoreCase("11n")) {
						imagetemp.setBackgroundResource(R.drawable.night_rain);
					}
				}
			} else if (LoginActivity.temp != null) {
				if (LoginActivity.temp.equals("")) {
					weatherLayout.setVisibility(View.GONE);
				} else {
					weatherLayout.setVisibility(View.VISIBLE);
					txtdate.setText(dayOfTheWeek + ", " + stringMonth + " "
							+ day);
					txttemp.setText(LoginActivity.temp);
					textcity.setText(LoginActivity.city);
					//Log.d("Loginactivty iocn", LoginActivity.icon);
					if (LoginActivity.icon != null) {
						if (LoginActivity.icon.equalsIgnoreCase("01d")) {
							imagetemp.setBackgroundResource(R.drawable.mon);
						} else if (LoginActivity.icon.equalsIgnoreCase("50d")) {
							imagetemp
							.setBackgroundResource(R.drawable.mon);
						} 
						else if (LoginActivity.icon.equalsIgnoreCase("10d")) {
							imagetemp.setBackgroundResource(R.drawable.tue);
						} else if (LoginActivity.icon.equalsIgnoreCase("03d")) {
							imagetemp.setBackgroundResource(R.drawable.rain);
						} else if (LoginActivity.icon.equalsIgnoreCase("04d")) {
							imagetemp.setBackgroundResource(R.drawable.thu);
						} else if (LoginActivity.icon.equalsIgnoreCase("02d")) {
							imagetemp
									.setBackgroundResource(R.drawable.maintemp);
						} else if (LoginActivity.icon.equalsIgnoreCase("")) {
							imagetemp
									.setBackgroundResource(R.drawable.maintemp);
						} else if (LoginActivity.icon.equalsIgnoreCase("01n")
								|| LoginActivity.icon.equalsIgnoreCase("03n")) {
							imagetemp
									.setBackgroundResource(R.drawable.night_clear);
						} else if (LoginActivity.icon.equalsIgnoreCase("02n")
								|| LoginActivity.icon.equalsIgnoreCase("04n")
								|| LoginActivity.icon.equalsIgnoreCase("13n")) {
							imagetemp
									.setBackgroundResource(R.drawable.night_cloudy);
						} else if (LoginActivity.icon.equalsIgnoreCase("09n")
								|| LoginActivity.icon.equalsIgnoreCase("11n")) {
							imagetemp
									.setBackgroundResource(R.drawable.night_rain);
						}
					}
				}
			} else {
				weatherLayout.setVisibility(View.GONE);
			}
		}

		if (getbundle != null) {
			// Log.d("Reminder",getbundle.getString("receiveddata"));
			/*
			 * UserDetails.setcity = getbundle.getString("getcity");
			 * UserDetails.settemp = getbundle.getString("gettemp");
			 * UserDetails.seticon = getbundle.getString("geticon");
			 */

			String remtype = getbundle.getString("receiveddata");
			if (remtype != null) {
				//Log.d("Reminder", getbundle.getString("receiveddata"));
				reminder = true;

			}

		}
	

		// downloadImageTask = new DownloadImageTask(typeimg);
		Intent myIntent = new Intent(getActivity(), HomeActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),
				0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		//notification();
		mBuilder = new NotificationCompat.Builder(getActivity())
				.setSmallIcon(R.drawable.applogo)
				.setLargeIcon(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.applogo))
				.setContentTitle("New Message")
				.setTicker("New Message")
				.setContentText(
						"Customer Service Application: You have received new message ");

		mNotificationManager = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder.setAutoCancel(true);
		mBuilder.setContentIntent(pendingIntent);
		mNotificationManager.cancelAll();

		/*runnable = new Runnable() { // main thread, for accessing web
			// service
			@Override
			public void run() {
				
				while (threadhandler) {
					try {
						Thread.sleep(60000);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								
								// Write your code here to update the UI.
								try {

									notification();
								} catch (Exception e) {

								}
								// /ALSO CALL ALERT FUNCTION HERE
								// mId allows you to update the notification
								// later on.
								if (threadhandler == true) {
									// Toast.makeText(getApplicationContext(),
									// "Updating..", Toast.LENGTH_SHORT).show();
									// mNotificationManager.notify(i++,
									// mBuilder.build());
								}

							}
						});
					} catch (Exception e) {
						
					}
				}
				try {
					Toast.makeText(getActivity(), "Thread stopped",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {

				}
			}
		};
		new Thread(runnable).start();*/

		weatherLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// DownloadImageTask downloadImageTask = new
				// DownloadImageTask(HomeFragment.typeimg);
				/*
				 * if(downloadImageTask.running) {
				 * downloadImageTask.cancel(true); }
				 */
				weather = true;
				fragment = new TrafficAndWeather_fragment();
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment)
							.addToBackStack(null).commit();
					mTracker.send(new HitBuilders.EventBuilder()
					 .setCategory("ui_action")
					 .setAction("button_press")
					 .setLabel("HomeWheather")
					 .build());

				} else {
					// error in creating fragment
					Log.e("MainActivity", "Error in creating fragment");
				}
			}
		});

		/*
		 * CheckConnectivity checknow = new CheckConnectivity(); boolean connect
		 * = checknow.checkNow(getActivity()); if(connect) { if
		 * (Config.getSAMLARTIFACT().equals("")) { new
		 * SamlArtifact(rootView.getContext()).execute(); } } else { Toast toast
		 * = Toast.makeText(getActivity(), "No network connection available.",
		 * Toast.LENGTH_LONG); //toast.setGravity(Gravity.CENTER, 0, 0);
		 * toast.show(); }
		 */
		notifylist = (ListView) rootView.findViewById(R.id.notificationlist);
		if (reminder) {
			img_bell.setVisibility(View.VISIBLE);
			reminder = false;
		} else {
			img_bell.setVisibility(View.GONE);
		}
		img_bell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
				FragmentManager fragmentManager = getFragmentManager();
				Fragment fragment = new Reminder_Fragment();
				fragment.setArguments(bundle);
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				
				
				mTracker.send(new HitBuilders.EventBuilder()
				 .setCategory("ui_action")
				 .setAction("button_press")
				 .setLabel("HomeReminder")
				 .build());
			}
		});
		rootView.getRootView().setFocusableInTouchMode(true);
		rootView.getRootView().requestFocus();
		rootView.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						/*
						 * new AlertDialog.Builder(getActivity())
						 * .setMessage("Are you sure you want to Logout?")
						 * .setCancelable(false) .setPositiveButton("Yes", new
						 * DialogInterface.OnClickListener() { public void
						 * onClick( DialogInterface dialog, int id) {
						 * getActivity().finish();
						 * 
						 * Intent i = new Intent( getActivity(),
						 * LoginActivity.class); startActivity(i);
						 * HomeFragment.mHandler
						 * .removeCallbacks(HomeFragment.runnable); //
						 * DownloadImageTask downloadImageTask = new
						 * DownloadImageTask(HomeFragment.typeimg); //
						 * downloadImageTask.cancel(true); }
						 * }).setNegativeButton("No", null)
						 * .setIcon(android.R.drawable.ic_dialog_alert)
						 * .setTitle("Logout").show();
						 */

						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								getActivity());
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
									public void onClick(DialogInterface dialog,
											int which) {
										appLogout();
										dialog.dismiss();
										//mHandler.removeCallbacks(runnable);

										
										
/*										new UserDetails()
												.setRegNumberList(new ArrayList<HashMap<String, String>>());
										LoginActivity.city = "";
										LoginActivity.temp = "";
										LoginActivity.icon = "";
										city = "";
										temp = "";
										icon = "";
										mHandler.removeCallbacks(runnable);
										System.exit(0);*/
									}
								});
						// Setting Negative "NO" Button
						alertDialog.setNegativeButton("NO",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// Write your code here to invoke NO
										// event
										dialog.dismiss();
									}
								});
						// Showing Alert Message
						alertDialog.show();
						return true;
					}
				}
				return false;
			}
		});
		return rootView;

	}

	
	private void appLogout()
	{
		TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
   	 
    	String imeinumber = telephonyManager.getDeviceId();
		if (new CheckConnectivity().checkNow(getActivity())) {
			String req = Config.awsserverurl+"tmsc_ch/customerapp/user/logout";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					3);
			nameValuePairs.add(new BasicNameValuePair("user_id",
					UserDetails.getUser_id()));
		
			Log.v("app logout method", imeinumber);
			
			nameValuePairs.add(new BasicNameValuePair("imei_number",
					imeinumber));
			
			nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
			
			new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST, Constants.logout, nameValuePairs, new ResponseCallback() {
				
				@Override
				public void onResponseReceive(Object object) {
					boolean res = (boolean) object;
					if(res){
				
						
						getActivity().finishAffinity();
				
				/*	Intent i = new Intent(
							getActivity(),
							LoginActivity.class);
					startActivity(i);*/
					
				/*	LoginActivity.login = false;
					  mTracker.send(new HitBuilders.EventBuilder()
		               	.setCategory(UserDetails.getUser_id())
		   	        	.setAction("thread_true")
		   	        	.setLabel("Log Out")
			            .build());
					new UserDetails()
							.setRegNumberList(new ArrayList<HashMap<String, String>>());
					LoginActivity.city="";
					LoginActivity.temp="";
					LoginActivity.icon="";
					HomeFragment.mHandler.removeCallbacks(HomeFragment.runnable);*/
					/*DownloadImageTask downloadImageTask = new DownloadImageTask(HomeFragment.typeimg);
			        downloadImageTask.cancel(true);*/
				
			
					}else {
					/*	Toast.makeText(
								getActivity(),
								Config.logoutstring,
								Toast.LENGTH_LONG).show();*/
				
						Log.v("exit else", "exit else executed");
					
					}
				}
				
				@Override
				public void onErrorReceive(String string) {
					
				Log.v("error", "Home fragment error recieve");		
					
					
					
				}
			}).execute();

			
			
		/*	SecurePreferences securePreferences= new SecurePreferences(getActivity(), PREFS_NAME,
					Constants.key, true); 
			securePreferences.removeValue("user_id");
			securePreferences.removeValue("password");*/
		//System.exit(0);
			
		}
		else
		{
			showWarningDialog();
		//	Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
			
		}
		
		
	}
	
	
	public void notification() {
		/*
		 * addlist.clear(); addlist = new ArrayList<>();
		 */
		String req = Config.awsserverurl+"tmsc_ch/customerapp/notifications/getLatestNotificationsForApp"
				;
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		 nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
		    nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
		

		new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST,
				Constants.getlatestnotification,nameValuePairs, new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						// boolean notify = (boolean) object;
						notifictionlist = (ArrayList<HashMap<String, String>>) object;
						
						if (addlist.size() <= 0) {
							for (int i = 0; i < notifictionlist.size(); i++) {
							
								addlist.remove(notifictionlist.get(i));
								addlist.add(notifictionlist.get(i));

								try {
									adapter = new CustomAdapter(getActivity(),
											R.layout.lis_item_notification,
											addlist);
									notifylist.setAdapter(adapter);
									adapter.notifyDataSetChanged();

								} catch (Exception e) {
								
									
									
								}

							}
						} else {
							if (notifictionlist.size() > addlist.size()) {

								for (int i = 0; i < notifictionlist.size(); i++) {
									if (!(addlist.contains(notifictionlist
											.get(i)))) {
										addlist.add(notifictionlist.get(i));
										adapter.notifyDataSetChanged();
									
										mNotificationManager.notify(i++,
												mBuilder.build());

									}

								}
							}
						}
					}

					@Override
					public void onErrorReceive(String string) {
						
						Log.d(string, "get string");
					}
				}).execute();

	}
	@Override
	public void onStart() {
		
		super.onStart();
		mTracker.setScreenName("HomeScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	private class CustomAdapter extends ArrayAdapter<HashMap<String, String>> {

		public CustomAdapter(Context context, int textViewResourceId,
				ArrayList<HashMap<String, String>> Strings) {

			
			super(context, textViewResourceId, Strings);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			final TextView txttype;
			TextView txttitle, txtdescription;
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.lis_item_notification, null);

			txttype = (TextView) convertView.findViewById(R.id.ntfytype);
			txttitle = (TextView) convertView.findViewById(R.id.ntftitle);
			txtdescription = (TextView) convertView
					.findViewById(R.id.notifydesc);
			typeimg = (ImageView) convertView.findViewById(R.id.notifyimage);
			txttype.setText(addlist.get(position).get("notification_type"));
			ntfstring = txttype.getText().toString();
			txttitle.setText(addlist.get(position).get("notification_title"));
		
			if (addlist.get(position).get("read_flag").equals("0")) {
				typeimg.setBackgroundResource(R.drawable.message_unopen);
				// typeimg.setImageDrawable(getActivity().getDrawable(R.drawable.message_unopen));
				txttype.setTextColor(getActivity().getResources().getColor(
						R.color.lightorang));

			} else {
				typeimg.setBackgroundResource(R.drawable.message_open);
				txttype.setTextColor(getActivity().getResources().getColor(
						R.color.white));
			}
			txtdescription.setText(addlist.get(position).get(
					"notification_desc"));

		

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					GetNotificationType = txttype.getText().toString();
					Log.d(GetNotificationType, "geting type text");
					Notificationchk = true;
					fragment = new Notifiaction_fragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment)
								.addToBackStack(null).commit();
						mTracker.send(new HitBuilders.EventBuilder()
						 .setCategory("ui_action")
						 .setAction("list_cell_press")
						 .setLabel("HomeNotification")
						 .build());
					} else {
						// error in creating fragment
						// Log.e("MainActivity", "Error in creating fragment");
					}
				}
			});

			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		
		FragmentManager fragmentManager = getFragmentManager();
		// DownloadImageTask downloadImageTask = new
		// DownloadImageTask(HomeFragment.typeimg);
		// downloadImageTask.cancel(true);
		// downloadImageTask.cancel(true);
		switch (v.getId()) {
		case R.id.servicebooking:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_service_booking")
			 .build());
			fragment = new BookServiceFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
			
		case R.id.maintenncecost:
			
			Log.v("Home maintain clicked ", "maintain clicked");
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_maintennace_cost")
			 .build());
			fragment = new MaintenanceCostFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.complaintreg_layout:
			
			Log.v("Home Complain clicked ", "Complain clicked");
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_complaint_registration")
			 .build());
			fragment = new ComplaintFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		
		case R.id.complaint_image_click:
			
			Log.v("Home Complain clicked ", "Complain clicked");
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_complaint_registration")
			 .build());
			fragment = new ComplaintFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		
			
case R.id.complaint_image_click1:
			
			Log.v("Home Complain clicked ", "Complain clicked");
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_complaint_registration")
			 .build());
			fragment = new ComplaintFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
			

case R.id.complaint_text_click1:
	
	Log.v("Home Complain clicked ", "Complain clicked");
	mTracker.send(new HitBuilders.EventBuilder()
	 .setCategory("ui_action")
	 .setAction("button_press")
	 .setLabel("Home_complaint_registration")
	 .build());
	fragment = new ComplaintFragment();
	fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
	break;
				
			
			
			
			
		case R.id.complaintreg12:
		
			Log.v("Home Complain clicked ", "Complain clicked");
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_complaint_registration")
			 .build());
			fragment = new ComplaintFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.costcal:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_cost_estimate_calculator")
			 .build());
			fragment = new Cost_EstimationFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;

		case R.id.feedback:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_feedback")
			 .build());
			fragment = new FeedbackFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.servicehistory:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_service_history")
			 .build());
			fragment = new ServiceHistoyFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.mydocument:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_my_document")
			 .build());
			fragment = new DocumentUploadFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.vehicledetails:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_vehicle_details")
			 .build());
			fragment = new VehicleDetails_Fragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.customerdetails:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_customer_details")
			 .build());
			fragment = new CustomerDetailFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		/*case R.id.notifications:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_notification")
			 .build());
			fragment = new Notifiaction_fragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;*/
		case R.id.dealerlocator:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_dealer_locator")
			 .build());
			fragment = new DelearLocator_fragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.gpstripmeter:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_gps_trip_meter")
			 .build());
			fragment = new GPSTripMeter_fragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.infoandterms:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_termsofuse")
			 .build());
			fragment = new InfoTerms_fragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.newsandupdates:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_infoandupdates")
			 .build());
			fragment = new InfoandUpdate_Fragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.customerdetailswithout:
			mTracker.send(new HitBuilders.EventBuilder()
			 .setCategory("ui_action")
			 .setAction("button_press")
			 .setLabel("Home_customer_details")
			 .build());
			fragment = new CustomerDetailFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			break;
		case R.id.btnproceed:
			
			checknow=new CheckConnectivity();
			connect=checknow.checkNow(getActivity());
			if (connect) {
				mTracker.send(new HitBuilders.EventBuilder()
				 .setCategory("ui_action")
				 .setAction("button_press")
				 .setLabel("Home_add_vehicle")
				 .build());
			proceeddialog.dismiss();
			new VehicleRegister(getActivity(), "Home");
			
			}
			 else {
				 checkvehiclereg(regvehicle);
				Toast toast = Toast.makeText(getActivity(),
						"No network connection available.",
						Toast.LENGTH_SHORT);
				toast.show();
			}
			break;
		case R.id.btnnotnow:
			proceeddialog.dismiss();
			regvehicle = false;
			// checkvehiclereg(regvehicle);
			break;
		default:
			break;

		}
	}



	@Override
	public void onDestroy() {
		
		hideProgressDialog();
		dialog = null;
		super.onDestroy();
	}

	private void hideProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
	}

	public void checkvehiclereg(boolean regvehicle) {
		if (regvehicle) {
			button_9.setVisibility(View.VISIBLE);
			button_6.setVisibility(View.GONE);
		} else {
			button_6.setVisibility(View.VISIBLE);
			button_9.setVisibility(View.GONE);
			// new VehicleRegister(getActivity(), "Home");

			proceeddialog = new Dialog(getActivity());
			proceeddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			proceeddialog.setContentView(R.layout.vehicle_reg_popup);
			proceeddialog.setCancelable(false);
			proceeddialog.show();
			proceed = (Button) proceeddialog.findViewById(R.id.btnproceed);
			notnow = (Button) proceeddialog.findViewById(R.id.btnnotnow);
			proceed.setOnClickListener(this);
			notnow.setOnClickListener(this);

		}
	}

	public void getVehicles() {
		new UserDetails()
				.setRegNumberList(new ArrayList<HashMap<String, String>>());
		String req = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/getVehicleDetailsByUserId"
				;
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		 nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
		    nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
		new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST,
				Constants.getVehicleDetailsByUserId,nameValuePairs, new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						

						new UserDetails()
								.setRegNumberList((ArrayList<HashMap<String, String>>) object);
						HomeFragment.regvehicle = true;
						checkvehiclereg(regvehicle);
						notification();
						getFeedbacknotifications();

					}

					@Override
					public void onErrorReceive(String string) {
						
						// Toast.makeText(getActivity(), "Vehicle not re",
						// Toast.LENGTH_LONG).show();
						// edtuser_id.setFocusable(true);
						// new UserDetails().setRegNumberList(null);
						new UserDetails()
								.setRegNumberList((new ArrayList<HashMap<String, String>>()));
						HomeFragment.regvehicle = false;
						checkvehiclereg(regvehicle);

					}

				}).execute();
	}

	@Override
	public void onStop() {
		
		super.onStop();
		HomeFragment.regvehicle = false;
	}

	

	public void getCurrentCity() {
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 3000, // 3 sec
					10, this);
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0,
					this);
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		}

		if (location != null) {

			MyLat = location.getLatitude();
			MyLong = location.getLongitude();

		} else {
			Location loc = getLastKnownLocation(getActivity());
			if (loc != null) {

				MyLat = loc.getLatitude();
				MyLong = loc.getLongitude();

			}
		}
		SplashScreenActivity.chkweather = false;
		try {
			// Getting address from found locations.
			Geocoder geocoder;

			List<Address> addresses;
			geocoder = new Geocoder(getActivity(), Locale.getDefault());
			addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

			StateName = addresses.get(0).getAdminArea();
			CityName = addresses.get(0).getLocality();
			/*CityName = "kolkata";*/
			CityName1 = CityName.replace(" ", "%20");
			CityName1 = CityName1.trim();
			CountryName = addresses.get(0).getCountryName();
			current_address = addresses.get(0).getAddressLine(0);
			pin_code = addresses.get(0).getPostalCode();
			// you can get more details other than this . like country code,
			// state code, etc.

			System.out.println(" StateName " + StateName);
			System.out.println(" CityName " + CityName);
			System.out.println(" CountryName " + CountryName);
			System.out.println(" current_address " + current_address + "pin"
					+ pin_code);

		} catch (Exception e) {
			e.printStackTrace();
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
										txtdate.setText(dayOfTheWeek + ", "
												+ stringMonth + " " + day);
										txttemp.setText(temp);
										Log.d("get temp", temp);
										textcity.setText(city);
										Log.d("get city", city);
										if (icon != null) {
											if (icon.equalsIgnoreCase("01d")|| icon
													.equalsIgnoreCase("50d")) {
												imagetemp.setBackgroundResource(R.drawable.mon);
											} else if (icon
													.equalsIgnoreCase("10d")) {
												imagetemp
														.setBackgroundResource(R.drawable.tue);
											} else if (icon
													.equalsIgnoreCase("03d")) {
												imagetemp
														.setBackgroundResource(R.drawable.rain);
											} else if (icon
													.equalsIgnoreCase("04d")) {
												imagetemp
														.setBackgroundResource(R.drawable.thu);
											} else if (icon
													.equalsIgnoreCase("02d")) {
												imagetemp
														.setBackgroundResource(R.drawable.maintemp);
											} else if (icon
													.equalsIgnoreCase("")) {
												imagetemp
														.setBackgroundResource(R.drawable.maintemp);
											} else if (icon
													.equalsIgnoreCase("01n")
													|| icon.equalsIgnoreCase("03n")) {
												imagetemp
														.setBackgroundResource(R.drawable.night_clear);
											} else if (icon
													.equalsIgnoreCase("02n")
													|| icon.equalsIgnoreCase("04n")
													|| icon.equalsIgnoreCase("13n")) {
												imagetemp
														.setBackgroundResource(R.drawable.night_cloudy);
											} else if (icon
													.equalsIgnoreCase("09n")
													|| icon.equalsIgnoreCase("11n")) {
												imagetemp
														.setBackgroundResource(R.drawable.night_rain);
											}
										}
									} else {
										weatherLayout.setVisibility(View.GONE);
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
			Toast.makeText(getActivity(),
					getResources().getString(R.string.network_error),
					Toast.LENGTH_SHORT).show();
		}

	}

	private Location getLastKnownLocation(Activity activity) {
		
		Location location = null;
		LocationManager locationmanager = (LocationManager) getActivity()
				.getSystemService("location");
		List list = locationmanager.getAllProviders();
		boolean i = false;
		Iterator iterator = list.iterator();
		do {
			// System.out.println("---------------------------------------------------------------------");
			if (!iterator.hasNext())
				break;
			String s = (String) iterator.next();
			// if(i != 0 && !locationmanager.isProviderEnabled(s))
			if (i != false && !locationmanager.isProviderEnabled(s))
				continue;
			// System.out.println("provider ===> "+s);
			Location location1 = locationmanager.getLastKnownLocation(s);
			if (location1 == null)
				continue;
			if (location != null) {
				// System.out.println("location ===> "+location);
				// System.out.println("location1 ===> "+location);
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
			// System.out.println("location  out ===> "+location);
			// System.out.println("location1 out===> "+location);
			i = locationmanager.isProviderEnabled(s);
			// System.out.println("---------------------------------------------------------------------");
		} while (true);
		return location;
	}

	@Override
	public void onLocationChanged(Location location) {
		

	}

	@Override
	public void onProviderDisabled(String provider) {
		
		Toast.makeText(rootView.getContext(), "GPS turned off ",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		
		Toast.makeText(rootView.getContext(), "GPS turned on ",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		

	}
	
	public void getFeedbacknotifications()
	{
		String req =Config.awsserverurl+"tmsc_ch/customerapp/notifications/getPSFNotifications";
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		 nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
		    nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
		new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST, Constants.getPSFNotifications,nameValuePairs, new ResponseCallback() {
			
			@Override
			public void onResponseReceive(Object object) {
				
				
				PSF_notifictionlist = (ArrayList<HashMap<String, String>>) object;
				//Log.d("get psf",PSF_notifictionlist.size()+"");
				if(PSF_notifictionlist.size()!=0){
					for (int i = 0; i < PSF_notifictionlist.size(); i++) {
						Reg_number=PSF_notifictionlist.get(i).get("registration_num");
						jobcard_number=PSF_notifictionlist.get(i).get("job_card_number");
						services_type=PSF_notifictionlist.get(i).get("sr_type");
						services_date=PSF_notifictionlist.get(i).get("sr_date");
					//	Log.d("get data", Reg_number+" "+jobcard_number+" "+services_type+" "+services_date);
					}
				final Dialog PSFdialog = new Dialog(getActivity());
				PSFdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				PSFdialog.setContentView(R.layout.psfnotification);
				PSFdialog.setCancelable(false);
				PSFdialog.show();
				
				TextView startjourny=(TextView)PSFdialog.findViewById(R.id.txtstartjourny);
                startjourny.setText("You have just completed your service of vehicle"+" "+Reg_number +". "+"Kindly give your feedback.");
				
				Button submit=(Button)PSFdialog.findViewById(R.id.visit_site);
				submit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						psfNotifiction=true;
					//	Log.d("get data on submit", Reg_number+" "+jobcard_number+" "+services_type+" "+services_date);

						  Bundle bundle = new Bundle();
				        bundle.putString("Reg_number", Reg_number);
				        bundle.putString("jobcard_number", jobcard_number);
				        bundle.putString("services_type", services_type);
				        bundle.putString("services_date", services_date);
				      
				        FragmentManager fragmentManager = getActivity()
								.getFragmentManager();
						Fragment fragment = new FeedbackFragment();
						fragment.setArguments(bundle);

						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment)
								.commit();
						PSFdialog.cancel();
					}
				});
				
				Button later=(Button)PSFdialog.findViewById(R.id.btnlater);
				later.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						PSFdialog.cancel();
					}
				});
				
				}

			}
			
			@Override
			public void onErrorReceive(String string) {
				
				
			}
		}).execute();
	}

	
	//GCM register

    // AsyncTask to register Device in GCM Server
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(getActivity());
                    }
                    regId = gcmObj
                            .register(Constants.project_id);
                    msg = "Registration ID :" + regId;
               	 TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                         .getSystemService(Context.TELEPHONY_SERVICE);
            	 
            	 imeinumber = telephonyManager.getDeviceId();
            	// Log.d("imeinumber", imeinumber);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
 
            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    // Store RegId created by GCM Server in SharedPref
                
                			 
                	
                	String req = Config.awsserverurl+"tmsc_ch/customerapp/gcmuser/registeruser";
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            		nameValuePairs.add(new BasicNameValuePair("name",
            				UserDetails.getFirst_name()+" "+UserDetails.getLast_name()));
            		nameValuePairs
            				.add(new BasicNameValuePair("email", UserDetails.getEmail_id()));
            		nameValuePairs.add(new BasicNameValuePair("regid", regId));
            		nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails.getUser_id()));
            		nameValuePairs.add(new BasicNameValuePair("imei_number", imeinumber));
            		
            		    nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
            		new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST, Constants.gcmuser,nameValuePairs, new ResponseCallback() {
            			
            			@Override
            			public void onResponseReceive(Object object) {
            				
            				
            				/*if(response.equals("Record Inserted"))
            				{
            					 SharedPreferences prefs = getActivity().getSharedPreferences("UserDetails",
                                         Context.MODE_PRIVATE);
                                 SharedPreferences.Editor editor = prefs.edit();
                                 editor.putString(REG_ID, regId);
                                 editor.putString(EMAIL_ID, UserDetails.getEmail_id());
                                 editor.commit();
            				}else
            				{*/
            				  //  SharedPreferences.Editor editor = prefs.edit();
                            //editor.putString(REG_ID, regId);
                            scPreferences.put(regnumber_share, regId);
                            //editor.putString(EMAIL_ID, UserDetails.getEmail_id());
                            scPreferences.put(email_share, UserDetails.getEmail_id());
                         //   editor.putString(UserID, UserDetails.getUser_id());
                            scPreferences.put(user_share, UserDetails.getUser_id());
                           // editor.commit();
            				//}
            			}
            			
            			@Override
            			public void onErrorReceive(String string) {
            				
            				
            			}
            		}).execute();
                   
                } else { 
                 
                }
            }
        }.execute(null, null, null);
    }
    // Check if Google Playservices is installed in Device or not
   
    
    
    
    private void showWarningDialog()
    {
    	
    	AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
    	builder.setMessage(getActivity().getResources().getString(R.string.warning_dialog));
    	builder.setCancelable(false);
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				
				getActivity().finishAffinity();
				warning_dialog.dismiss();
				
				
			}
		});
    	
    	warning_dialog=builder.create();
    	warning_dialog.show();
    	
    }
    
    private boolean checkPlayServices() {
        
    	if(scPreferences.getString("playservice_check")==null)
    	{
    		scPreferences.put("playservice_check", "y");
    		
    		int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(getActivity());
            // When Play services not found in device
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    // Show Error dialog to install Play services
                    GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(
                            getActivity(),
                            "This device doesn't support Play services.",
                            Toast.LENGTH_LONG).show();
                }
                return false;
            } else {
                Toast.makeText(
                        getActivity(),
                        "This device supports Play services.",
                        Toast.LENGTH_LONG).show();
            }
            return true;
       
    	}
    	else
    	{
    		return false;
    	}
    
    	
    
    
    
    }
 
}
