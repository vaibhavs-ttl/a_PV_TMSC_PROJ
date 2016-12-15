package com.ttl.customersocialapp;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.UserDetails;

public class GPSTripMeter_fragment extends Fragment implements LocationListener {

	LocationManager gps;
	static boolean check=false;
	View rootView;
	Location lkg, lkn, lk;
	String cs,
			gString,
			dbase,
			sbase,
			ffurl,
			furl,
			murl = "http://maps.google.com/staticmap?path=rgba%3A0xff000099%2Cweight%3A6";
	Float s = 0.0f; // Value of Speed
	int chk = 0; // To reduce the static map url Strin
	private LocationManager locationManager;
	MapView mapView;
	GoogleMap googleMap;
	Button start, stop, clear;
	Double MyLat, MyLong ,MyEndLat,MyEndLong;
	String CityName = "";
	String StateName = "";
	String CountryName = "";
	String addressline = "";
	String sublocality = "";
	Location location;
	DateFormat df1;
	View view;
	Date date;
	String currentDateTimeString, currentDateTimeString1;
	File folder, folder1;
	String History = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/CustomerSocialAppDocument/"+UserDetails.getUser_id()+"/History/";
//	private Handler handler = new Handler();
	Fragment fragment = null;
	
	Double dist = 0.0; // Value of distance

	boolean st = false, sep = false, onlocation = false;

	TextView distance, counter;

	private Handler myHandler = new Handler();
	private long startTime = 0L;
	long timeInMillies = 0L;
	long timeSwap = 0L;
	long finalTime = 0L;
	ImageView pdfhistory;

	Tracker mTracker;
	
	@Override
	public void onStart() {
		
		super.onStart();
		mTracker.setScreenName("GPSTripMeterScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		if (!new CheckConnectivity().checkNow(getActivity())) {
			
			Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
			
		}
		
		
		rootView = inflater.inflate(R.layout.fragment_gpstripmeter, container,
				false);
		start = (Button) rootView.findViewById(R.id.btnstarttrip);
		stop = (Button) rootView.findViewById(R.id.btnstoptrip);
		clear = (Button) rootView.findViewById(R.id.btnclear);
		counter = (TextView) rootView.findViewById(R.id.txtcounter);
		File Historydir = new File(History);
		Historydir.mkdirs();
		String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath()
				.toString();
		folder1 = new File(extStorageDirectory, "CustomerSocialAppDocument");
		folder1.mkdir();
		folder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"+UserDetails.getUser_id()+"/History/");
		folder.mkdir();
		//Tracker
		 AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
			mTracker = application.getDefaultTracker();
		
		distance = (TextView) rootView.findViewById(R.id.txtdistn);
		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);

		// Gets to GoogleMap from the MapView and does initialization stuff
		googleMap = mapView.getMap();
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.setMyLocationEnabled(true);

		mapView.onResume();// needed to get the map to display immediately

		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		stop.setBackgroundResource(R.drawable.gps_btn);
		stop.setEnabled(false);
		clear.setBackgroundResource(R.drawable.gps_btn);
		clear.setEnabled(false);

		Date d2 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		currentDateTimeString = sdf.format(d2);
		date = new Date(System.currentTimeMillis());
		df1 = new SimpleDateFormat("yyyy/MM/dd");
		System.out.println(df1.format(date));

	//	LatLng markerLoc = new LatLng(21.000000, 78.000000);

	/*	final CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(markerLoc).build();
*/
		// Enabling MyLocation Layer of Google Map

		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
				21.000000, 78.000000));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(3);

		googleMap.moveCamera(center);
		googleMap.animateCamera(zoom);

		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		
				stop.setBackgroundResource(R.drawable.gps_btn);
				stop.setEnabled(false);
				start.setEnabled(true);
				start.setBackgroundResource(R.drawable.gps_btn_bg);
				pdfhistory.setVisibility(View.VISIBLE);
				locationManager.removeUpdates(GPSTripMeter_fragment.this);
				googleMap.clear();

				distance.setText("0.000");
				startTime = 0L;
				timeInMillies = 0L;
				timeSwap = 0L;
				finalTime = 0L;
				timeSwap += timeInMillies;
				myHandler.removeCallbacks(updateTimerMethod);
				counter.setText("00:00:00");
			}
		});
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(new CheckConnectivity().checkNow(getActivity()))
				{
				
				final Dialog dialog = new Dialog(v.getContext());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.gpsstart);

				Button btnsubmit = (Button) dialog.findViewById(R.id.visit_site);
				btnsubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						check=true;
						startupdate();

						dialog.cancel();
					}
				});

				Button btnNo = (Button) dialog.findViewById(R.id.btnno);

				btnNo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						check=false;
						dialog.cancel();
					}
				});

				dialog.show();
			}
				else
				{
					Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
				}
	
		
			
			}
		
		
		});

		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				check=true;
				
				SnapshotReadyCallback callback = new SnapshotReadyCallback() {
					Bitmap bitmap;
					
					@Override
					public void onSnapshotReady(Bitmap snapshot) {
						bitmap = snapshot;
						
						try {
							FileOutputStream out = new FileOutputStream(folder
									+ "/CustomerSocial.png");
							bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				googleMap.snapshot(callback);
				final Dialog dialog = new Dialog(v.getContext());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.gpsstop);
				Button btnstopsubmit = (Button) dialog
						.findViewById(R.id.btnstopsubmit);

				btnstopsubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
					
					//	if (onlocation == true) {
							stop.setBackgroundResource(R.drawable.gps_btn);
							stop.setEnabled(false);
							start.setBackgroundResource(R.drawable.gps_btn_bg);
							pdfhistory.setVisibility(View.VISIBLE);
							start.setEnabled(true);
							Date d1 = new Date();
							SimpleDateFormat sdf1 = new SimpleDateFormat(
									"hh:mm a");
							currentDateTimeString1 = sdf1.format(d1);
							clear.setEnabled(true);
							dist = 0.0;
							s = 0.0f;
							// dbase = "";
							sbase = "";
							// sp.setText(sbase);
							// distance.setText(dbase);
							st = false;

							stopupdate();

							try {

								Document document = new Document();
								SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy");	
								String currentDateTime = formatter.format(
												new Date()) + " "+ new SimpleDateFormat("hh:mm a").format(new Date());
								
								PdfWriter.getInstance(document,
										new FileOutputStream(folder + "/Trip "
												+ currentDateTime + ".pdf"));
								document.open();
								PdfGenerator.addMetaData(document);
								PdfGenerator.addTitlePage(document);
								PdfGenerator.createTable(document);
								document.add(Chunk.NEWLINE);
								document.add(Chunk.NEWLINE);
								Image img = Image.getInstance(folder
										+ "/CustomerSocial.png");
								/*
								 * int indentation = 10; float scaler =
								 * ((document.getPageSize().getWidth() -
								 * document.leftMargin() -
								 * document.rightMargin() -
								 * document.bottomMargin() -
								 * document.topMargin() - indentation) / img
								 * .getWidth()) * 100;
								 */
								img.scalePercent(80f);
								// img.scalePercent(scaler);
								document.add(img);
								document.close();

							} catch (Exception e) {
								e.printStackTrace();

							}
							locationManager
									.removeUpdates(GPSTripMeter_fragment.this);
							startTime = 0L;
							timeInMillies = 0L;
							timeSwap = 0L;
							finalTime = 0L;
							timeSwap += timeInMillies;
							myHandler.removeCallbacks(updateTimerMethod);
							dialog.cancel();
						/*} else {
							Toast.makeText(getActivity(),
									"You are at same location",
									Toast.LENGTH_SHORT).show();
							dialog.cancel();
						}*/

					}
				});
				Button btnstopNo = (Button) dialog.findViewById(R.id.btnstopno);

				btnstopNo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						check=false;
						dialog.cancel();
					}
				});

				dialog.show();

			}
		});
		/* runnable.run(); */
		LocationManager manager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			showSettingsAlert();

		}
		setHasOptionsMenu(true);

		pdfhistory = (ImageView) rootView.findViewById(R.id.imgpdfhistory);
		pdfhistory.setOnClickListener(new OnClickListener() {

			
		
			
			@Override
			public void onClick(View v) {
		
				FragmentManager fragmentManager = getFragmentManager();
				fragment = new PdfListFragment();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}
		});

		rootView.getRootView().setFocusableInTouchMode(true);
		rootView.getRootView().requestFocus();

		rootView.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container, new HomeFragment())
								.commit();
						return true;
					}
				}
				return false;
			}
		});
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

		super.onCreateOptionsMenu(menu, menuInflater);
	}

	/**
	 * react to the user tapping/selecting an options menu item
	 */
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit_item:
		
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void startupdate() {

		/********** get Gps location service LocationManager object ***********/
		locationManager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, // 3 sec
				10, this);

		/*********
		 * After registration onLocationChanged method called periodically after
		 * each 3 sec
		 ***********/
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {

			MyLat = location.getLatitude();
			MyLong = location.getLongitude();
			
            MarkerOptions marker = new MarkerOptions().position(new LatLng(MyLat,MyLong));
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                         .target(new LatLng(location.getLatitude(), location
                                       .getLongitude())).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                          .newCameraPosition(cameraPosition));

			try {
				// Getting address from found locations.
				Geocoder geocoder;

				List<Address> addresses;
				geocoder = new Geocoder(rootView.getContext(),
						Locale.getDefault());
				addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
				if (addresses != null) {

					addressline = addresses.get(0).getAddressLine(0);
					sublocality = addresses.get(0).getSubLocality();
					CityName = addresses.get(0).getLocality();
					StateName = addresses.get(0).getAdminArea();
					CountryName = addresses.get(0).getCountryName();
					// you can get more details other than this . like country
					// code,
					// state code, etc.
					pdfhistory.setVisibility(View.GONE);
					furl = murl;
					st = true;
					// dbase = "DISTANCE\n";
					sbase = "SPEED\n";
					// sp.setText(sbase);
					// distance.setText(dbase);

					start.setBackgroundResource(R.drawable.gps_btn);
					start.setEnabled(false);
					stop.setBackgroundResource(R.drawable.gps_btn_bg);

					clear.setBackgroundResource(R.drawable.gps_btn_bg);
					stop.setEnabled(true);
					clear.setEnabled(true);
					startTime = SystemClock.uptimeMillis();
					myHandler.postDelayed(updateTimerMethod, 0);
					System.out.println(" addressline " + addressline);
					System.out.println(" sublocality " + addressline);
					System.out.println(" CityName " + CityName);
					System.out.println(" StateName " + StateName);
					System.out.println(" CountryName " + CountryName);
					PdfGenerator.starloc = addressline + sublocality + CityName
							+ StateName + CountryName;
					PdfGenerator.strttime = currentDateTimeString;
				} else {
					Toast.makeText(getActivity(),
							"Please check internet connection",
							Toast.LENGTH_SHORT).show();
					locationManager.removeUpdates(GPSTripMeter_fragment.this);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void stopupdate() {

		/********** get Gps location service LocationManager object ***********/
		locationManager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, // 3 sec
				10, this);

		/*********
		 * After registration onLocationChanged method called periodically after
		 * each 3 sec
		 ***********/
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {

			MyEndLat = location.getLatitude();
            MyEndLong = location.getLongitude();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(MyEndLat,MyEndLong));
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                         .target(new LatLng(location.getLatitude(), location
                                       .getLongitude())).zoom(9).build();
            googleMap.animateCamera(CameraUpdateFactory
                         .newCameraPosition(cameraPosition));


		}
            if(onlocation==false){
            	PdfGenerator.endloc=PdfGenerator.starloc.toString();
            	PdfGenerator.dst="0.0";
            	PdfGenerator.jdate = df1.format(date);
        		PdfGenerator.endtime = currentDateTimeString1;
            }else {
            	PdfGenerator.jdate = df1.format(date);
        		PdfGenerator.endtime = currentDateTimeString1;
			}
		

	}

	@Override
	public void onLocationChanged(Location location) {
		

		onlocation = true;
		
		// Toast.makeText(rootView.getContext(), str, Toast.LENGTH_LONG).show();
		// Log.d(str, "lat log");
		/*LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());*/
		MarkerOptions marker = new MarkerOptions().position(new LatLng(location
				.getLatitude(), location.getLongitude()));

		// Changing marker icon
		marker.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.delearlistcountimg));

		// adding marker
		googleMap.addMarker(marker);

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location
						.getLongitude())).zoom(12).build();

		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		googleMap.setTrafficEnabled(true);

		//String check = "0";
		try {
			if (lkg == null) {
				lkg = new Location(location);
				//check = "1";
			}
			if (location != null && lkg != null && st) {
				if (chk == 0) {
					furl = furl + "|" + lkg.getLatitude() + ","
							+ lkg.getLongitude();
					chk = 5;
				} else
					chk--;
				dist += lkg.distanceTo(location);
				//check = "2";
				lkg = new Location(location);
				//check = "3";
				s = location.getSpeed();
				//check = "4";
				// sp.setText(sbase+(s*3.6)+" KM/Hour");
				//check = "5";
				distance.setText((dist / 1000) + "");
				String s = (String.valueOf((dist / 1000)).substring(0,5));
                PdfGenerator.dst = (s + "KM");

			}
		} catch (Exception e) {
			/*
			 * Toast tet = Toast.makeText(rootView.getContext(), e + " " +
			 * check, Toast.LENGTH_SHORT); tet.show();
			 */
		}

		try {
			// Getting address from found locations.
			Geocoder geocoder;

			List<Address> addresses;
			geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
			addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
			if (addresses != null) {
				addressline = addresses.get(0).getAddressLine(0);
				sublocality = addresses.get(0).getSubLocality();
				CityName = addresses.get(0).getLocality();
				StateName = addresses.get(0).getAdminArea();
				CountryName = addresses.get(0).getCountryName();
				// you can get more details other than this . like country code,
				// state code, etc.

				System.out.println(" addressline " + addressline);
				System.out.println(" sublocality " + addressline);
				System.out.println(" CityName " + CityName);
				System.out.println(" StateName " + StateName);
				System.out.println(" CountryName " + CountryName);

				PdfGenerator.endloc = addressline + sublocality + CityName
						+ StateName + CountryName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

		Toast.makeText(rootView.getContext(), "GPS turned on ",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		
		Toast.makeText(rootView.getContext(), "GPS turned off ",
				Toast.LENGTH_SHORT).show();
		LocationManager manager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			showSettingsAlert();

		}

	}

	public final void setTrafficEnabled(boolean enabled) {
		googleMap.setTrafficEnabled(enabled);
	}

	/*
	 * private Runnable runnable = new Runnable() {
	 * 
	 * public void run() { LocationManager manager = (LocationManager)
	 * rootView.getContext() .getSystemService(Context.LOCATION_SERVICE); if
	 * (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	 * 
	 * showSettingsAlert();
	 * 
	 * } handler.postDelayed(this, 20000); } };
	 */

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				rootView.getContext());

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/* System.exit(0); */
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container, new HomeFragment())
								.commit();
						// dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	private Runnable updateTimerMethod = new Runnable() {

		public void run() {
			timeInMillies = SystemClock.uptimeMillis() - startTime;
			finalTime = timeSwap + timeInMillies;

			int seconds = (int) (finalTime / 1000);
			int minutes = seconds / 60;
			int hrs = minutes / 60;
			seconds = seconds % 60;
		//	int milliseconds = (int) (finalTime % 1000);
			counter.setText("" + hrs + ":" + minutes + ":"
					+ String.format("%02d", seconds));
			myHandler.postDelayed(this, 0);
		}

	};

}
