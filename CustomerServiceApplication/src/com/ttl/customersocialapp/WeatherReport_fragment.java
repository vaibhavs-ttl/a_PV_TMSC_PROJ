package com.ttl.customersocialapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ttl.adapter.WeatherAdapter;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.FragmentCallback;
import com.ttl.helper.HorizontalListView;
import com.ttl.model.Weather;
import com.ttl.webservice.WeatherWebservice;

public class WeatherReport_fragment extends Fragment implements
		LocationListener {

	private static final String TAG = "AppWeather";
	private LocationManager locationManager;
	private TextView city;
	private TextView date;
	private TextView temperature;
	private TextView wind;
	static Weather dayWeather;
	Double MyLat, MyLong;
	String CityName = "", CityName1="";
	String StateName = "";
	String CountryName = "";
	public static Location location;
	View rootView;
	ImageView imgwind;
	HorizontalListView daylistview;
	private WeatherAdapter adapter;

	ArrayList<Weather> weathers = new ArrayList<Weather>();

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater
				.inflate(R.layout.fragment_weather, container, false);
		imgwind = (ImageView) rootView.findViewById(R.id.imgwind);

		// Geoloc user
		locationManager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
			geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
			addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

			StateName = addresses.get(0).getAdminArea();
			CityName = addresses.get(0).getLocality();
			/*CityName = "New Delhi";*/
			CityName1 = CityName.replace(" ", "%20");
			CityName1 = CityName1.trim();
			
			CountryName = addresses.get(0).getCountryName();
			// you can get more details other than this . like country code,
			// state code, etc.

			System.out.println(" StateName " + StateName);
			System.out.println(" CityName " + CityName);
			System.out.println(" CountryName " + CountryName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		city = (TextView) rootView.findViewById(R.id.txtcity);
		date = (TextView) rootView.findViewById(R.id.txtdate);
		temperature = (TextView) rootView.findViewById(R.id.txttemp);
		wind = (TextView) rootView.findViewById(R.id.txtwindspeed);
		// icon = (ImageView) rootView.findViewById(R.id.icon);

		Time now = new Time();
		now.setToNow();

		currentcity();

		weather();
		weathers = new ArrayList<Weather>();

		return rootView;
	}

	/*
	 * public boolean isOnline() { ConnectivityManager cm =
	 * (ConnectivityManager) rootView.getContext()
	 * .getSystemService(Context.CONNECTIVITY_SERVICE); NetworkInfo netInfo =
	 * cm.getActiveNetworkInfo(); if (netInfo != null &&
	 * netInfo.isConnectedOrConnecting()) { return true; }
	 * 
	 * return false; }
	 */

	private Location getLastKnownLocation(
			WeatherReport_fragment weatherReportFragment) {
		
		
		Location location = null;
		LocationManager locationmanager = (LocationManager) rootView
				.getContext().getSystemService("location");
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
		
		
		/*
		 * Log.v(TAG, location.getLatitude() + " - " + location.getLongitude());
		 * 
		 * 
		 * // Get weather data Activity activity = getActivity();
		 * 
		 * if (activity != null && this.isOnline() && isAdded()) {
		 * Toast.makeText(rootView.getContext(),
		 * getResources().getString(R.string.fetching_data),
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * WeatherWebservice weatherWS = new WeatherWebservice( new
		 * FragmentCallback() {
		 * 
		 * @Override public void onTaskDone(ArrayList<Weather> result) { //
		 * Update UI if (result.size() > 0 && result.get(0).isFetched) {
		 * dayWeather = result.get(0); // Update UI
		 * updateUIwithWeather(dayWeather); } } }, location, true, null);
		 * weatherWS.execute();
		 * 
		 * } else { locationManager.removeUpdates(this);
		 * Toast.makeText(rootView.getContext(),
		 * getResources().getString(R.string.network_error),
		 * Toast.LENGTH_SHORT).show(); }
		 */
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		

	}

	@Override
	public void onProviderEnabled(String provider) {
		

	}

	@Override
	public void onProviderDisabled(String provider) {
		

	}

	private void currentcity() {
		
		// Get weather data
		CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow.checkNow(getActivity());
		if (connect) {

			/*
			 * Toast.makeText(rootView.getContext(),
			 * getResources().getString(R.string.fetching_data),
			 * Toast.LENGTH_SHORT).show();
			 */

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
									city.setText(dayWeather.place);
									temperature.setText(String.format("%.1fºC",
											dayWeather.temperature));
									wind.setText(dayWeather.windSpeed + "km/h");
									// humidity.setText(weather.humidity + "%");
									date.setText(new SimpleDateFormat(
											"EEEE dd/MM", Locale.getDefault())
											.format(dayWeather.day));
									imgwind.setBackgroundResource(R.drawable.wind);
								}
							}
						}
					}, null, true, CityName1.toString());

			weatherWS.execute();

		} else {
			Toast.makeText(rootView.getContext(),
					getResources().getString(R.string.network_error),
					Toast.LENGTH_SHORT).show();
		}

	}

	private void weather() {
		/*
		 * adapter = new WeatherAdapter(getActivity(), R.layout.weather_list,
		 * weathers); daylistview.setAdapter(adapter);
		 */
		// Get weather data
		if (Weather.getInstance().location != null) {
			CheckConnectivity checknow = new CheckConnectivity();
			boolean connect = checknow.checkNow(getActivity());
			if (connect) {
				/*
				 * Toast.makeText( this.getActivity().getApplicationContext(),
				 * getResources().getString(R.string.alert_search_message),
				 * Toast.LENGTH_SHORT).show();
				 */

				WeatherWebservice weatherWS = new WeatherWebservice(
						new FragmentCallback() {
							@Override
							public void onTaskDone(ArrayList<Weather> result) {
								if (result != null) {
									// weathers.clear();
									Log.d("get result", result.toString());
									weathers.addAll(result);
									if (weathers != null) {
										Log.d("add resutl into Weather",
												weathers.get(0).temperature
														+ "");
										daylistview = (HorizontalListView) rootView.findViewById(R.id.horizonallist);
										try {
											adapter = new WeatherAdapter(
													getActivity(),
													R.layout.weather_list,
													weathers);

											daylistview.setAdapter(adapter);
											adapter.notifyDataSetChanged();
										} catch (Exception e) {
									
										}
									}
								}
							}
						}, Weather.getInstance().location, false, null);
				weatherWS.execute();

			} else {
				Toast.makeText(this.getActivity().getApplicationContext(),
						getResources().getString(R.string.network_error),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this.getActivity().getApplicationContext(),
					getResources().getString(R.string.no_location),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	

		if (dayWeather != null) {
			outState.putSerializable("dayWeather", dayWeather);
		}
	}

	

}
