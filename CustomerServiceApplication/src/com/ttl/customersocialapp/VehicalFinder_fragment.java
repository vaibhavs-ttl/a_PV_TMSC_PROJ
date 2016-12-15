package com.ttl.customersocialapp;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ttl.communication.CheckConnectivity;
import com.ttl.communication.SecurePreferences;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.webservice.Constants;

public class VehicalFinder_fragment extends Fragment implements
		LocationListener {

	private View rootView;
	private MapView mapView;
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private Button park, navigate;
	private Location location;
	private Double MyLat, MyLong;
	

	
	private String savedLat, savedLng, saveaddress, savezoom;

	// private Marker singleMarker;
	private double latitude, longitude;
	private boolean onlocationchange = false, savedmarker = false,
			markers = false, loc = false, removeloction = false,
			parvehical = false, chkmarker = false;

	private float zoom;
	private String latString, lngString, address;
	private Double lat, lng;

	private ImageView removeloc;
	private Marker savemarker, marker;
	private String CityName = "";
	private String StateName = "";
	private String CountryName = "";
	private String addressline = "";
	private String sublocality = "";
	private String url = "";
	private String pincode = "";
	private float getZoom;
	private List<Address> addresses;
	private SecurePreferences scPreferences;
	private final String PREFS_NAME = "CREDENTIALS";

	public VehicalFinder_fragment() {
	}

	Tracker mTracker;

	@Override
	public void onStart() {

		super.onStart();
		mTracker.setScreenName("VehicleFinderScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_vehicalfinder, container,
				false);
		CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow.checkNow(getActivity());
		if (!connect) {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}

		mapView = (MapView) rootView.findViewById(R.id.vehicalmap);
		mapView.onCreate(savedInstanceState);
		// Tracker
		AnalyticsApplication application = (AnalyticsApplication) getActivity()
				.getApplication();
		mTracker = application.getDefaultTracker();
		// Gets to GoogleMap from the MapView and does initialization stuff
		googleMap = mapView.getMap();
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.setMyLocationEnabled(true);

		locationManager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);

		

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, // 3 sec
				0, this);		
		
		
		
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		
		/*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, // 3 sec
				0, this);*/

		
	
		
	/*	location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
		
			
		scPreferences = new SecurePreferences(getActivity(), PREFS_NAME,
				Constants.key, true);

		savedLat = scPreferences.getString("latitude");
		savedLng = scPreferences.getString("longitude");
		savezoom = scPreferences.getString("zoom");

		if (savedLat != null && savedLng != null && savezoom != null) {
			googleMap.setTrafficEnabled(false);
			lat = Double.parseDouble(savedLat);
			lng = Double.parseDouble(savedLng);
			getZoom = Float.valueOf(savezoom);
			savemarker = googleMap.addMarker(new MarkerOptions()
					.position(new LatLng(lat, lng)));
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat,
					lng));
			
	
			
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(getZoom);

			googleMap.moveCamera(center);
			googleMap.animateCamera(zoom);
			savedmarker = true;
			chkmarker = true;

		} else {
			loc = true;
			if (location != null) {

				MyLat = location.getLatitude();
				MyLong = location.getLongitude();
				CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
						MyLat, MyLong));
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

				googleMap.moveCamera(center);
				googleMap.animateCamera(zoom);
			}
		}
		parvehical = false;
		removeloc = (ImageView) rootView.findViewById(R.id.removeloc);

		removeloc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			

				if (parvehical == true || savedmarker == true
						&& removeloction == false) {

					final Dialog dialog = new Dialog(v.getContext());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.removelocation);
					TextView txtgetadd = (TextView) dialog
							.findViewById(R.id.txtdsc);
					// saveaddress = settings.getString("address", address);
					saveaddress = scPreferences.getString("address");
					if (saveaddress != null) {
						txtgetadd
								.setText("Do you want to remove Vehicle location?"
										+ "\n" + saveaddress);
					}
					Button btnNo = (Button) dialog.findViewById(R.id.vbtnno);
					btnNo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							//
							dialog.cancel();
						}
					});
					Button btnYes = (Button) dialog.findViewById(R.id.vbtnyes);

					btnYes.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							//
							googleMap.clear();
							saveaddress=null;
							parvehical = false;
							park.setEnabled(true);
							removeloction = true;
							chkmarker = false;
							if (savedmarker == true) {
								savemarker.remove();
								scPreferences.removeValue("address");
						        scPreferences.removeValue("latitude");
						        scPreferences.removeValue("longitude");
						        scPreferences.removeValue("zoom");
					
							} else if (markers = true) {

								marker.remove();
								scPreferences.removeValue("address");
						        scPreferences.removeValue("latitude");
						        scPreferences.removeValue("longitude");
						        scPreferences.removeValue("zoom");
						
							}

							dialog.cancel();
						}
					});

					dialog.show();
				} else {
					Toast.makeText(getActivity(), "Please park your vehicle",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		park = (Button) rootView.findViewById(R.id.btnsaveloc);
		navigate = (Button) rootView.findViewById(R.id.btnNavigate);

		park.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (new CheckConnectivity().checkNow(getActivity())) {

					if (location != null) {

						MyLat = location.getLatitude();
						MyLong = location.getLongitude();
						if (MyLat != null || MyLong != null) {
							try {
								// Getting address from found locations.
								Geocoder geocoder;

								geocoder = new Geocoder(v.getContext(), Locale
										.getDefault());
								addresses = geocoder.getFromLocation(MyLat,
										MyLong, 1);
								if (addresses != null) {
									addressline = addresses.get(0)
											.getAddressLine(0);
									sublocality = addresses.get(0)
											.getSubLocality();
									CityName = addresses.get(0).getLocality();
									StateName = addresses.get(0).getAdminArea();
									CountryName = addresses.get(0)
											.getCountryName();
									url = addresses.get(0).getUrl();
									pincode = addresses.get(0).getPostalCode();

								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (loc == true || removeloction == true) {
							if (addresses != null) {

								final Dialog dialog = new Dialog(v.getContext());
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.vehicalfinderpopup);
								TextView txtaddess = (TextView) dialog
										.findViewById(R.id.txtnotification);
								if (pincode != null) {
									txtaddess.setText("Park Your Vehicle"
											+ "\n" + addressline + ",\n"
											+ CityName + "-" + pincode + ",\n"
											+ StateName + ",\t" + CountryName);
								} else {
									txtaddess.setText("Park Your Vehicle"
											+ "\n" + addressline + ",\n"
											+ CityName + ",\n" + StateName
											+ ",\t" + CountryName);
								}
								Button btnOK = (Button) dialog
										.findViewById(R.id.btnok);

								btnOK.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										//
										// park.setEnabled(false);
										loc = false;
										removeloction = false;
										parvehical = true;
										if (location != null) {

											MyLat = location.getLatitude();
											MyLong = location.getLongitude();
											CameraUpdate center = CameraUpdateFactory
													.newLatLng(new LatLng(
															MyLat, MyLong));
											CameraUpdate zoom = CameraUpdateFactory
													.zoomTo(15);

											googleMap.moveCamera(center);
											googleMap.animateCamera(zoom);
										}
										googleMap.setTrafficEnabled(false);
										marker = googleMap.addMarker(new MarkerOptions()
												.position(new LatLng(MyLat,
														MyLong)));
										markers = true;
										zoom = googleMap.getCameraPosition().zoom;

										latString = MyLat + "";

										lngString = MyLong + "";
										if (pincode != null) {
											address = addressline + ",\n"
													+ CityName + "-" + pincode
													+ ",\n" + StateName + ",\t"
													+ CountryName;
										} else {
											address = addressline + ",\n"
													+ CityName + ",\n"
													+ StateName + ",\t"
													+ CountryName;
										}
										// edit = settings.edit();
										// put latitude and longitude to
										// preferences
										// for
										// later
										// use
										scPreferences
												.put("latitude", latString);
										scPreferences.put("longitude",
												lngString);
										scPreferences.put("address", address);
										scPreferences.put("zoom",
												String.valueOf(zoom));

										/*
										 * edit.putString("latitude",
										 * latString);
										 * edit.putString("longitude",
										 * lngString); edit.putString("address",
										 * address); edit.putFloat("zoom",
										 * zoom); edit.commit();
										 */

										dialog.cancel();
									}
								});

								dialog.show();
							} else {
								Toast.makeText(getActivity(),
										"Unable to fetch current location",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(getActivity(),
									"Your vehicle is already parked",
									Toast.LENGTH_LONG).show();
						}
					}

				} else {
					Toast.makeText(getActivity(),
							getActivity().getString(R.string.no_network_msg),
							Toast.LENGTH_LONG).show();
				}

			}
		});

		navigate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//

				
				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);	
			
				
			
				
				if(location!=null)
				{
				
				latitude=location.getLatitude();
				
				longitude=location.getLongitude();
				
				if (chkmarker == true || parvehical == true) {

				//Double.compare(latitude, Double.parseDouble(savedLat))!=0 && Double.compare(longitude, Double.parseDouble(savedLng))!=0 || 
					
					savedLat = scPreferences.getString("latitude");
					savedLng = scPreferences.getString("longitude");
				
					
					
					if (Double.compare(latitude, Double.parseDouble(savedLat))!=0 && Double.compare(longitude, Double.parseDouble(savedLng))!=0 ||(onlocationchange == true)) {
							
						String uri = "http://maps.google.com/maps?saddr="
								+ latitude + "," + longitude + "&daddr="
								+ savedLat + "," + savedLng;
						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW, Uri
										.parse(uri));
						intent.setClassName("com.google.android.apps.maps",
								"com.google.android.maps.MapsActivity");

						startActivity(intent);
					} else {
						Toast.makeText(rootView.getContext(),
								"You are at Same Location", Toast.LENGTH_LONG)
								.show();
					}
				
			
			
			
			}else
			{
				Toast.makeText(getActivity(),
						"You didn’t park your vehicle", Toast.LENGTH_LONG)
						.show();
			}
			
			
			}
			else
				{
					Toast.makeText(getActivity(),
							"Could not find your location", Toast.LENGTH_LONG)
							.show();
				}
				
				
				
				
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
								.addToBackStack(null).commit();
						return true;
					}
				}
				return false;
			}
		});
		return rootView;

		// end of onCreate Method

	}

	@Override
	public void onResume() {
	
		mapView.onResume();
		super.onResume();
	}

	
	@Override
	public void onPause() {
		mapView.invalidate();
		super.onPause();
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

	@Override
	public void onLocationChanged(Location location) {
		//
		/*String str = "Latitude: " + location.getLatitude() + " \nLongitude: "
				+ location.getLongitude();*/
		// Toast.makeText(rootView.getContext(), str, Toast.LENGTH_LONG).show();
	//	Log.d(str, "lat log");

		latitude = location.getLatitude();

		// Getting longitude of the current location
		longitude = location.getLongitude();

		// Creating a LatLng object for the current location

		onlocationchange = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		

	}

	@Override
	public void onProviderEnabled(String provider) {
		
		Toast.makeText(rootView.getContext(), "Gps turned on ",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {

		Toast.makeText(rootView.getContext(), "Gps turned off ",
				Toast.LENGTH_SHORT).show();
		LocationManager manager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);
	
		
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			showSettingsAlert();

		}
	}

	

	
	
	
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
								.addToBackStack(null).commit();
						// dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
}
