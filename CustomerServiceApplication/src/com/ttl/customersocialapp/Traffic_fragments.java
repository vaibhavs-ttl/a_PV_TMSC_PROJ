package com.ttl.customersocialapp;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.ttl.communication.CheckConnectivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Traffic_fragments extends Fragment implements LocationListener {

	public Traffic_fragments() {
	}

	private LocationManager locationManager;
	MapView mapView;
	GoogleMap googleMap;
	View rootView;
	Location location;
	Double MyLat, MyLong;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater
				.inflate(R.layout.fragment_traffic, container, false);
		mapView = (MapView) rootView.findViewById(R.id.trafficmap);
		mapView.onCreate(savedInstanceState);
		googleMap = mapView.getMap();
		//googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.setMyLocationEnabled(true);
		googleMap.setTrafficEnabled(true);
		CheckConnectivity checknow = new CheckConnectivity();
		if(checknow.checkNow(getActivity())){
			
		
		
		locationManager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, // 3 sec
				10, this);
		
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {

			MyLat = location.getLatitude();
			MyLong = location.getLongitude();
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
					MyLat, MyLong));
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
			googleMap.setTrafficEnabled(true);
			googleMap.moveCamera(center);
			googleMap.animateCamera(zoom);

		}
	}else {
		Toast.makeText(getActivity(),"No network connection available", Toast.LENGTH_SHORT).show();
	}
		return rootView;
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

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		String str = "Latitude: " + location.getLatitude() + " \nLongitude: "
				+ location.getLongitude();
		//Toast.makeText(rootView.getContext(), str, Toast.LENGTH_LONG).show();
		//Log.d(str, "lat log");
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location
						.getLongitude())).zoom(14).build();

		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		googleMap.setTrafficEnabled(true);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
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
						tx.replace(R.id.frame_container, new HomeFragment()).addToBackStack(null)
								.commit();
						// dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
}
