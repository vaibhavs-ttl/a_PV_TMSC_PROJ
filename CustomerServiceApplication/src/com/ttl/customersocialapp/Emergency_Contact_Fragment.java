package com.ttl.customersocialapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.UserDetails;

public class Emergency_Contact_Fragment extends Fragment implements
		LocationListener, OnMarkerClickListener {
	View v;
	private LocationManager locationManager;
	MapView mapView;
	GoogleMap googleMap;
	// View rootView;
	Location location;
	Double MyLat, MyLong;
	Marker marker;
	Dialog dialog;
	String CityName = "";
	String StateName = "";
	String CountryName = "";
	String addressline = "";
	String sublocality = "";
	String url = "";
	String pincode = "";
	Spinner regno;
	String regnumber = "", model, chassis;
	ArrayList<String> regvalues = new ArrayList<String>();
	ImageView call;
	TextView calltext;

	Tracker mTracker;
	
	@Override
	public void onStart() {
		
		super.onStart();
		mTracker.setScreenName("EmergencyContactScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		
		v = inflater.inflate(R.layout.fragment_emergency_contact, viewGroup,
				false);
		if (new UserDetails().getRegNumberList().size() == 0) {
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();
		}
		CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow.checkNow(getActivity());
		if (connect) {
		} else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}

		//Tracker
		AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
		mTracker = application.getDefaultTracker();
		
		mapView = (MapView) v.findViewById(R.id.emergencymap);
		mapView.onCreate(savedInstanceState);

		googleMap = mapView.getMap();
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.setMyLocationEnabled(true);
		googleMap.setTrafficEnabled(true);

		locationManager = (LocationManager) v.getContext().getSystemService(
				Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, // 3 sec
				10, this);

		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {

			MyLat = location.getLatitude();
			MyLong = location.getLongitude();
			System.out.println(" URL " + "Link : http://maps.google.com/?q=+"
					+ MyLat + "," + MyLong);

			marker = googleMap.addMarker(new MarkerOptions().position(
					new LatLng(MyLat, MyLong)).title("Share your Location."));
			marker.showInfoWindow();
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
					MyLat, MyLong));
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
			googleMap.setTrafficEnabled(true);
			googleMap.moveCamera(center);
			googleMap.animateCamera(zoom);

			try {
				// Getting address from found locations.
				Geocoder geocoder;

				List<Address> addresses;
				geocoder = new Geocoder(v.getContext(), Locale.getDefault());
				addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
				if (addresses != null) {
					addressline = addresses.get(0).getAddressLine(0);
					sublocality = addresses.get(0).getSubLocality();
					CityName = addresses.get(0).getLocality();
					StateName = addresses.get(0).getAdminArea();
					CountryName = addresses.get(0).getCountryName();
					url = addresses.get(0).getUrl();
					pincode = addresses.get(0).getPostalCode();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		call = (ImageView) v.findViewById(R.id.callimage);
		calltext = (TextView) v.findViewById(R.id.emgno);
		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + "18002097979"));
				startActivity(callIntent);
			}
		});
		calltext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + "18002097979"));
				startActivity(callIntent);
			}
		});

		googleMap.setOnMarkerClickListener(this);

		v.getRootView().setFocusableInTouchMode(true);
		v.getRootView().requestFocus();

		v.getRootView().setOnKeyListener(new OnKeyListener() {
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
		return v;

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		

	}

	@Override
	public void onProviderEnabled(String provider) {
		

	}

	@Override
	public void onProviderDisabled(String provider) {
		
		LocationManager manager = (LocationManager) v.getContext()
				.getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			showSettingsAlert(getActivity());

		}
	}

	public void showSettingsAlert(Activity activity) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				v.getContext());

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
	public void onLocationChanged(Location location) {
		
		
		if (location != null) {

		
	CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(location.getLatitude(), location
							.getLongitude())).zoom(14).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			googleMap.setTrafficEnabled(true);
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		
		if(!new CheckConnectivity().checkNow(getActivity()))
		{
			
			Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
		}
		else
		{
			
		dialog = new Dialog(v.getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.emergency_contact_popup);
		regno = (Spinner) dialog.findViewById(R.id.spinregno);

		regvalues.add("Select Vehicle");
		int size = new UserDetails().getRegNumberList().size();
		
		Log.e("emergency vehicles size", ""+size);
		
		for (int i = 0; i < size; i++) {
			// chassisvalues.add(new UserDetails().getRegNumberList().get(i)
			// .get("chassis_num"));
			// registration_num
			if (!(new UserDetails().getRegNumberList().get(i)
					.get("registration_num").toString().equals(""))) {
				regvalues.add(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"));
			} else {
				regvalues.add(new UserDetails().getRegNumberList().get(i)
						.get("chassis_num"));
			}

		}
		
		ArrayAdapter<String> reg = new ArrayAdapter<String>(getActivity(),
				R.layout.spinnertext, regvalues);
		reg.setDropDownViewResource(R.layout.spinner_selector);
		
	
		
		
		regno.setAdapter(reg);

		regno.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position != 0) {
					regnumber = new UserDetails().getRegNumberList()
							.get(position - 1).get("registration_num");
					model = new UserDetails().getRegNumberList()
							.get(position - 1).get("PPL");
					chassis = new UserDetails().getRegNumberList()
							.get(position - 1).get("chassis_num");
				} else {
					regnumber = null;
					model = null;
					chassis = null;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				

			}
		});
		TextView currentAddress = (TextView) dialog
				.findViewById(R.id.currentAddress);
		
		
		if(addressline==null)
		{
			addressline="";
		}
		if(CityName==null)
		{
			CityName="";
		}
		if(pincode==null)
		{
			pincode="";
		}	
		
		if(StateName==null)
		{
			StateName="";
		}
		
		if(CountryName==null)
		{
			CountryName="";
		}
		
		
		
		
		currentAddress.setText(addressline + ",\n" + CityName + "-" + pincode
				+ ",\n" + StateName + ",\t" + CountryName);

		
		
		
		ImageView img = (ImageView) dialog.findViewById(R.id.imgclose);
		Button btnshare = (Button) dialog.findViewById(R.id.btnshare);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				regvalues.clear();
				dialog.dismiss();
			
				
			}
		});
		btnshare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (chassis != null) {
					dialog.dismiss();

					String body = "Dear Team,\n"
							+ "I need assistance from you. My current vehicle location can be accessed from google maps using the link below. This may also be used by you in addition, to locate my vehicle if required."
							+ "\n\nLink : http://maps.google.com/?q=" + MyLat
							+ "," + MyLong + "\n\nRegards, \n" + "Name: "
							+ UserDetails.getFirst_name() + " "
							+ UserDetails.getLast_name() + "\nMobile No: "
							+ UserDetails.getContact_number()
							+ "\nVehicle No: " + regnumber + "\nChassis No: "
							+ chassis + "\nModel: " + model;
					Intent emailIntent = new Intent(Intent.ACTION_SEND);
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
							new String[] { "customerfeedback@tatamotors.com" });

					emailIntent
							.putExtra(
									android.content.Intent.EXTRA_SUBJECT,
									regnumber
											+ " - Requires your assistance-Chassis no: "
											+ chassis);

					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							body);
					// emailIntent.putExtra(Intent.EXTRA_STREAM, U);
					startActivityForResult(
							Intent.createChooser(emailIntent, "Email:"), 1);

					/*
					 * String req =
					 * "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					 * +"<SOAP:Body>"
					 * +"<SendMail xmlns=\"http://schemas.cordys.com/1.0/email\">"
					 * +"<to>" +"<address>" +
					 * "<emailAddress>customerfeedback@tatamotors.com</emailAddress>"
					 * +"<displayName></displayName>" +"</address>" +"</to>"
					 * +"<subject>"
					 * +regnumber+" - Requires your assistance-Chassis no: "
					 * +chassis+"</subject>"
					 * +"<body type=\"normal\">Dear Team,\n" +
					 * "I need assistance from you. My current vehicle location can be accessed from google maps using the link below. This may also be used by you in addition, to locate my vehicle if required."
					 * +"\nLink : http://maps.google.com/?q="+MyLat+","+MyLong
					 * +"\n\nRegards, \n"
					 * +"Name: "+UserDetails.getFirst_name()+" "
					 * +UserDetails.getLast_name()
					 * +"\nMobile No: "+UserDetails.getContact_number()
					 * +"\nVehicle No: "+regnumber +"\nModel: "+model +"</body>"
					 * +"<from>"
					 * +"<displayName>"+UserDetails.getFirst_name()+"</displayName>"
					 * +
					 * "<emailAddress>"+UserDetails.getEmail_id()+"</emailAddress>"
					 * //"+UserDetails.getEmail_id()+" +"</from>" +"</SendMail>"
					 * +"</SOAP:Body>" +"</SOAP:Envelope>"; new
					 * WebServiceCall(getActivity(), req, Constants.SendMail,
					 * new ResponseCallback() {
					 * 
					 * @Override public void onResponseReceive(Object object) {
					 *  dialog.dismiss(); }
					 * 
					 * @Override public void onErrorReceive(String string) { //
					 * 
					 * Toast.makeText(getActivity(),
					 * "Mail Send Failed . Please try again.",
					 * Toast.LENGTH_SHORT).show(); } }, "").execute();
					 */
				} else {
					Toast.makeText(getActivity(), "Please select Vehicle.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		dialog.show();
		}
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		int RESULT_OK = -1;
		try {
			if (requestCode == 1) {
				if (resultCode == RESULT_OK && null != data) {
					FragmentManager fm = getFragmentManager();
					FragmentTransaction tx = fm.beginTransaction();
					tx.replace(R.id.frame_container, new HomeFragment())
							.commit();
				} else {
					FragmentManager fm = getFragmentManager();
					FragmentTransaction tx = fm.beginTransaction();
					tx.replace(R.id.frame_container,
							new Emergency_Contact_Fragment()).commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			Toast.makeText(v.getContext(), "Something went wrong ",
					Toast.LENGTH_LONG).show();
		}
	
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

	
	}
