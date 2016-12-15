package com.ttl.customersocialapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.NavDrawerListAdapter;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.communication.SecurePreferences;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.NavDrawerItem;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class HomeActivity extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	public static ArrayList<String> labour_data;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	static int fragmentnum = 0;
	// private String alartDate, alarmSRNO, alarmRegNo;
	Bundle b, getloginbundle;
	Intent intent;
	private final String PREFS_NAME = "CREDENTIALS";
	FragmentManager fragmentManager;
	// username password
	private final String name = "USERID";
	private final String password = "PASSWORD";
	Tracker mTracker;
	static long lastactivitydone;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mTracker.setScreenName("HomeScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		lastactivitydone = new Date().getTime();

		/*
		 * b = getIntent().getExtras(); if (b != null) {
		 * Log.i("BUNDLE HomeActivity", b.getString("DATE") + " " +
		 * b.getString("SRNO") + " " + b.getString("REGNO")); alartDate =
		 * b.getString("DATE"); alarmSRNO = b.getString("SRNO"); alarmRegNo =
		 * b.getString("REGNO"); }
		 */

		getloginbundle = getIntent().getExtras();
		if (getloginbundle != null) {
			/*
			 * Log.i("BUNDLE loginActivty", getloginbundle.getString("getcity")
			 * + " " + getloginbundle.getString("gettemp") + " " +
			 * getloginbundle.getString("geticon"));
			 */

		}
		intent = getIntent();
		if (intent != null) {
			String remtype = intent.getStringExtra("remindertype");
			if (remtype != null) {
				Log.d("remtype", remtype);
				if (!(remtype.equals(""))) {
					b = new Bundle();
					getloginbundle.putString("receiveddata", remtype);

				}
			}
		}
		fragmentManager = getFragmentManager();
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Service booking
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Service history
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// Maintenance cost
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// complaint registration
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));

		// Complaint registared
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		// cost calsulator
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));
		// feedback
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, -1)));
		// Service history
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons
				.getResourceId(8, -1)));
		// vehicle details
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons
				.getResourceId(9, -1)));
		// my documents
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons
				.getResourceId(10, -1)));
		// Notifications
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons
				.getResourceId(11, -1)));
		// dealer Locator
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[12], navMenuIcons
				.getResourceId(12, -1)));
		// GPS Trip meter
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[13], navMenuIcons
				.getResourceId(13, -1)));
		// Vehical Finder
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[14], navMenuIcons
				.getResourceId(14, -1)));
		// Traffic And Weather
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[15], navMenuIcons
				.getResourceId(15, -1)));
		// Customer details
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[16], navMenuIcons
				.getResourceId(16, -1)));
		// Maintenance Tips
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[17], navMenuIcons
				.getResourceId(17, -1)));
		// Reminder
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[18], navMenuIcons
				.getResourceId(18, -1)));
		// News & updates
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[19], navMenuIcons
				.getResourceId(19, -1)));
		// Emergency contact
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[20], navMenuIcons
				.getResourceId(21, -1)));

		// Product Broucher
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[21], navMenuIcons
				.getResourceId(20, -1)));

		// Info & terms
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[22], navMenuIcons
				.getResourceId(22, -1)));
		// Know your car
		/*
		 * navDrawerItems.add(new NavDrawerItem(navMenuTitles[22], navMenuIcons
		 * .getResourceId(22, -1)));
		 */
		// Logout
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[23], navMenuIcons
				.getResourceId(23, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.actionbar)));

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(actionBar.getDisplayOptions()
				| ActionBar.DISPLAY_SHOW_CUSTOM);
		ImageView imageView = new ImageView(actionBar.getThemedContext());
		imageView.setScaleType(ImageView.ScaleType.CENTER);
		imageView.setImageResource(R.drawable.small_tataservice_logo);
		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);
		layoutParams.rightMargin = 40;
		imageView.setLayoutParams(layoutParams);
		actionBar.setCustomView(imageView);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, // nav menu toggle icon
				R.string.blank, // nav drawer open - description for
								// accessibility
				R.string.blank // nav drawer close - description for
								// accessibility
		) {
			public void onDrawerClosed(View view) {
				// getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		// Toast.makeText(getApplicationContext(), UserProfile.username,
		// Toast.LENGTH_SHORT).show();
		// new SamlArtifact(getApplicationContext()).execute();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		/*
		 * DownloadImageTask downloadImageTask = new
		 * DownloadImageTask(HomeFragment.typeimg);
		 * downloadImageTask.cancel(true);
		 */
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			/*
			 * if(b!= null || getloginbundle != null) {
			 */
			// fragment.setArguments(b);
			fragment.setArguments(getloginbundle);
			// }
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Home").build());
			break;
		case 1:
			fragment = new BookServiceFragment();
			fragmentnum = 1;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Service Booking").build());
			break;
		case 2:
			fragment = new ServiceBookingHistoryFragment();
			fragmentnum = 2;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Service Booking History").build());
			break;
		case 3:
			fragment = new MaintenanceCostFragment();
			fragmentnum = 3;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Maintenance Cost").build());
			break;
		case 4:
			fragment = new ComplaintFragment();
			fragmentnum = 4;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Complaint Registraction").build());
			break;
		case 5:
			fragment = new ComplaintRegisteredFragment();
			fragmentnum = 5;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Complaint Regestered").build());
			break;
		case 6:
			fragment = new Cost_EstimationFragment();
			fragmentnum = 6;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Cost Estimate Calculator").build());
			break;
		case 7:
			fragment = new FeedbackFragment();
			fragmentnum = 7;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Feedback").build());
			break;
		case 8:
			fragment = new ServiceHistoyFragment();
			fragmentnum = 8;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Service History").build());
			break;
		case 9:
			fragment = new VehicleDetails_Fragment();
			fragmentnum = 9;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Vehicle Details").build());
			break;
		case 10:
			fragment = new DocumentUploadFragment();
			fragmentnum = 10;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Document Upload").build());
			break;
		case 11:
			fragment = new Notifiaction_fragment();
			fragmentnum = 11;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Notification").build());
			break;
		case 12:
			fragment = new DelearLocator_fragment();
			fragmentnum = 12;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Dealer Locator").build());
			break;
		case 13:
			fragment = new GPSTripMeter_fragment();
			fragmentnum = 13;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("GPS Trip Meter").build());
			break;
		case 14:
			fragment = new VehicalFinder_fragment();
			fragmentnum = 14;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Vehicle Finder").build());
			break;
		case 15:
			fragment = new TrafficAndWeather_fragment();
			fragmentnum = 15;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Traffic & Weather").build());
			break;
		case 16:
			fragment = new CustomerDetailFragment();
			fragmentnum = 16;

			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Customer Details").build());
			break;
		case 17:
			fragment = new MaintenanceTipsFragment();

			fragmentnum = 17;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Maintenance Cost").build());
			break;
		case 18:
			fragment = new Reminder_Fragment();

			fragmentnum = 18;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Reminder").build());
			break;
		case 19:
			fragment = new InfoandUpdate_Fragment();

			fragmentnum = 19;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Info & Updates").build());
			break;
		case 21:
			fragment = new Emergency_Contact_Fragment();

			fragmentnum = 21;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Emergency Contact").build());
			break;

		case 20:

			fragment = new ProductBrochureFragment();
			fragmentnum = 20;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Product Information").build());
			break;

		case 22:
			fragment = new InfoTerms_fragment();
			// Toast.makeText(getApplicationContext(), "Data not available.",
			// Toast.LENGTH_SHORT).show();
			fragmentnum = 22;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Terms of Use").build());
			break;

		/*
		 * case 22: // fragment = new Emergency_Contact_Fragment(); boolean
		 * installed = appInstalledOrNot("com.tata.skoolman"); if(installed) {
		 * //This intent will help you to launch if the package is already
		 * installed Intent LaunchIntent = getPackageManager()
		 * .getLaunchIntentForPackage("com.tata.skoolman");
		 * startActivity(LaunchIntent); Toast.makeText(getApplicationContext(),
		 * "Data not available.", Toast.LENGTH_SHORT).show();
		 * 
		 * System.out.println("App is already installed on your phone"); } else
		 * { System.out.println("App is not currently installed on your phone");
		 * String url =
		 * "https://play.google.com/store/apps/details?id=com.tata.skoolman&hl=en"
		 * ; Intent i = new Intent(Intent.ACTION_VIEW);
		 * i.setData(Uri.parse(url)); startActivity(i);
		 * Toast.makeText(getApplicationContext(), "Data not available.",
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * } //fragmentnum = 21; break;
		 */
		case 23:
			// fragment = new Emergency_Contact_Fragment();

			// fragmentnum = 22;
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("ui_action").setAction("button_press")
					.setLabel("Logout").build());

			new AlertDialog.Builder(HomeActivity.this)
					.setMessage("Are you sure you want to Logout?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

									String imeinumber = telephonyManager
											.getDeviceId();
									if (new CheckConnectivity()
											.checkNow(HomeActivity.this)) {
										String req = Config.awsserverurl
												+ "tmsc_ch/customerapp/user/logout";
										List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
												3);
										nameValuePairs
												.add(new BasicNameValuePair(
														"user_id", UserDetails
																.getUser_id()));

										Log.v("home activity logout",
												imeinumber);
										nameValuePairs
												.add(new BasicNameValuePair(
														"imei_number",
														imeinumber));
										nameValuePairs.add(new BasicNameValuePair(
												"sessionId", UserDetails
														.getSeeionId()));

										new AWS_WebServiceCall(
												HomeActivity.this, req,
												ServiceHandler.POST,
												Constants.logout,
												nameValuePairs,
												new ResponseCallback() {

													@Override
													public void onResponseReceive(
															Object object) {
														boolean res = (boolean) object;
														if (res) {

															HomeActivity.this
																	.finish();
															Intent i = new Intent(
																	HomeActivity.this,
																	LoginActivity.class);
															startActivity(i);
															// finishAffinity();
															LoginActivity.login = false;
															mTracker.send(new HitBuilders.EventBuilder()
																	.setCategory(
																			UserDetails
																					.getUser_id())
																	.setAction(
																			"thread_true")
																	.setLabel(
																			"Log Out")
																	.build());
															new UserDetails()
																	.setRegNumberList(new ArrayList<HashMap<String, String>>());
															LoginActivity.city = "";
															LoginActivity.temp = "";
															LoginActivity.icon = "";
															HomeFragment.mHandler
																	.removeCallbacks(HomeFragment.runnable);
															/*
															 * DownloadImageTask
															 * downloadImageTask
															 * = new
															 * DownloadImageTask
															 * (
															 * HomeFragment.typeimg
															 * );
															 * downloadImageTask
															 * .cancel(true);
															 */

														} else {
															Toast.makeText(
																	getApplicationContext(),
																	Config.logoutstring,
																	Toast.LENGTH_LONG)
																	.show();
														}
													}

													@Override
													public void onErrorReceive(
															String string) {
														// TODO Auto-generated
														// method stub

													}
												}).execute();

										SecurePreferences securePreferences = new SecurePreferences(
												HomeActivity.this, PREFS_NAME,
												Constants.key, true);
										securePreferences
												.removeValue("user_id");
										securePreferences
												.removeValue("password");

									} else {

										Toast.makeText(
												HomeActivity.this,
												getString(R.string.no_network_msg),
												Toast.LENGTH_LONG).show();

									}

								}
							}).setNegativeButton("No", null)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Logout").show();
			break;

		default:
			break;
		}

		if (fragment != null) {

			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	private boolean appInstalledOrNot(String uri) {
		// TODO Auto-generated method stub
		PackageManager pm = getPackageManager();
		boolean app_installed;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		// getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * stub // super.onBackPressed(); new AlertDialog.Builder(HomeActivity.this)
	 * .setMessage("Are you sure you want to Logout?") .setCancelable(false)
	 * .setPositiveButton("Yes", new DialogInterface.OnClickListener() { public
	 * void onClick(DialogInterface dialog, int id) {
	 * 
	 * HomeActivity.this.finish(); LoginActivity.login = false; new
	 * UserDetails() .setRegNumberList(new ArrayList<HashMap<String,
	 * String>>()); LoginActivity.city=""; LoginActivity.temp="";
	 * LoginActivity.icon="";
	 * HomeFragment.mHandler.removeCallbacks(HomeFragment.runnable);
	 * DownloadImageTask downloadImageTask = new
	 * DownloadImageTask(HomeFragment.typeimg); downloadImageTask.cancel(true);
	 * 
	 * SharedPreferences settings = getSharedPreferences( PREFS_NAME, 0);
	 * Log.d("get name ", settings.getString(name, "")); Log.d("get password",
	 * settings.getString(password, "")); SharedPreferences.Editor editor =
	 * settings.edit(); editor.clear();
	 * 
	 * editor.commit(); } }).setNegativeButton("No", null)
	 * .setIcon(android.R.drawable.ic_dialog_alert).setTitle("Logout") .show();
	 * if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
	 * mDrawerLayout.closeDrawer(GravityCompat.START);
	 * 
	 * }else { if(fragmentnum!= 1) { if(fragmentManager.getBackStackEntryCount()
	 * != 0) { fragmentManager.popBackStack(); } else { final
	 * AlertDialog.Builder alertDialog = new AlertDialog.Builder(
	 * HomeActivity.this); // Setting Dialog Title
	 * alertDialog.setTitle("Exit Application?"); // Setting Dialog Message
	 * alertDialog
	 * .setMessage("Are you sure you want to Exit the Application?"); // Setting
	 * Icon to Dialog alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
	 * // Setting Positive "Yes" Button alertDialog.setPositiveButton("YES", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) {
	 * 
	 * if(new CheckConnectivity().checkNow(HomeActivity.this)) {
	 * 
	 * appLogout(); dialog.dismiss(); } else { Toast.makeText(HomeActivity.this,
	 * getResources().getString(R.string.no_network_msg),
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * HomeActivity.this.finish();
	 * 
	 * new UserDetails() .setRegNumberList(new ArrayList<HashMap<String,
	 * String>>()); LoginActivity.city=""; LoginActivity.temp="";
	 * LoginActivity.icon="";
	 * HomeFragment.mHandler.removeCallbacks(HomeFragment.runnable);
	 * 
	 * } }); // Setting Negative "NO" Button alertDialog.setNegativeButton("NO",
	 * new DialogInterface.OnClickListener() { public void
	 * onClick(DialogInterface dialog, int which) {
	 * 
	 * dialog.cancel(); } });
	 * 
	 * 
	 * } } }
	 */

	
	
	@Override
	protected void onRestart() {

		super.onRestart();


		
		
	//Toast.makeText(HomeActivity.this, "session ID: "+UserDetails.getSeeionId(), Toast.LENGTH_LONG).show();
	
	}
	
	
	
	
	
	@Override
	protected void onResume() {

		super.onResume();
		if (fragmentnum != 13) {

			if (GPSTripMeter_fragment.check) {

				/*
				 * new Config().checkLastSession(this, lastactivitydone); if(new
				 * Config().appstate) { HomeActivity.this.finish();
				 * 
				 * new UserDetails() .setRegNumberList(new
				 * ArrayList<HashMap<String, String>>()); LoginActivity.city="";
				 * LoginActivity.temp=""; LoginActivity.icon="";
				 * HomeFragment.mHandler.removeCallbacks(HomeFragment.runnable);
				 * System.exit(0);
				 * 
				 * }
				 */

			} else {

				new Config().checkLastSession(this, lastactivitydone);
				if (new Config().appstate) {
					HomeActivity.this.finish();

					new UserDetails()
							.setRegNumberList(new ArrayList<HashMap<String, String>>());
					LoginActivity.city = "";
					LoginActivity.temp = "";
					LoginActivity.icon = "";
					HomeFragment.mHandler
							.removeCallbacks(HomeFragment.runnable);
					System.exit(0);

				}
			}

		}
	}

	// New change

	@Override
	public void onBackPressed() {

		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			super.onBackPressed();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		lastactivitydone = new Date().getTime();

		return super.dispatchTouchEvent(ev);
	}

	private void appLogout() {

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		String imeinumber = telephonyManager.getDeviceId();
		if (new CheckConnectivity().checkNow(HomeActivity.this)) {
			String req = Config.awsserverurl
					+ "tmsc_ch/customerapp/user/logout";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails
					.getUser_id()));

			nameValuePairs
					.add(new BasicNameValuePair("imei_number", imeinumber));

			nameValuePairs.add(new BasicNameValuePair("sessionId", UserDetails
					.getSeeionId()));

			new AWS_WebServiceCall(HomeActivity.this, req, ServiceHandler.POST,
					Constants.logout, nameValuePairs, new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							boolean res = (boolean) object;
							if (res) {
								// System.exit(0);

								/*
								 * Intent i = new Intent( HomeActivity.this,
								 * LoginActivity.class); startActivity(i);
								 */
								/*
								 * LoginActivity.login = false;
								 * mTracker.send(new HitBuilders.EventBuilder()
								 * .setCategory(UserDetails.getUser_id())
								 * .setAction("thread_true")
								 * .setLabel("Log Out") .build()); new
								 * UserDetails() .setRegNumberList(new
								 * ArrayList<HashMap<String, String>>());
								 * LoginActivity.city=""; LoginActivity.temp="";
								 * LoginActivity.icon="";
								 * HomeFragment.mHandler.removeCallbacks
								 * (HomeFragment.runnable);
								 */
								// finish();
								/*
								 * DownloadImageTask downloadImageTask = new
								 * DownloadImageTask(HomeFragment.typeimg);
								 * downloadImageTask.cancel(true);
								 */

							} else {
								Toast.makeText(HomeActivity.this,
										Config.logoutstring, Toast.LENGTH_LONG)
										.show();
							}
						}

						@Override
						public void onErrorReceive(String string) {

						}
					}).execute();

			/*
			 * SecurePreferences securePreferences= new
			 * SecurePreferences(getActivity(), PREFS_NAME, Constants.key,
			 * true); securePreferences.removeValue("user_id");
			 * securePreferences.removeValue("password");
			 */
			// System.exit(0);

		} else {

			Toast.makeText(HomeActivity.this,
					getString(R.string.no_network_msg), Toast.LENGTH_LONG)
					.show();

		}

	}

}
