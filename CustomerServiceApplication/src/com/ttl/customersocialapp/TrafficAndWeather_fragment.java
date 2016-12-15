package com.ttl.customersocialapp;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TrafficAndWeather_fragment extends Fragment {
	Button traffic, weather, viewtraffic, viewweather;

	View rootView;

	public TrafficAndWeather_fragment() {
	}

	Tracker mTracker;
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mTracker.setScreenName("TrafficandWeatherScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_trafficweather,
				container, false);
		traffic = (Button) rootView.findViewById(R.id.btntraffic);
		weather = (Button) rootView.findViewById(R.id.btnweather);
		viewtraffic = (Button) rootView.findViewById(R.id.viewtraffic);
		viewweather = (Button) rootView.findViewById(R.id.viewweather);
		
		 //Tracker
		 AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
					mTracker = application.getDefaultTracker();
		
		if (HomeFragment.weather == true) {

			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			WeatherReport_fragment llf = new WeatherReport_fragment();
			ft.replace(R.id.reltrafficweather, llf);
			ft.commit();
			weather.setBackgroundColor(rootView.getContext().getResources()
					.getColor(R.color.darkgray));
			weather.setTextColor(rootView.getContext().getResources()
					.getColor(R.color.litegray));
			viewweather.setBackgroundColor(rootView.getContext()
					.getResources().getColor(R.color.yellow));
			traffic.setBackgroundColor(rootView.getContext().getResources()
					.getColor(R.color.stronggray));
			traffic.setTextColor(rootView.getContext().getResources()
					.getColor(R.color.darkgray));
			viewtraffic.setBackgroundColor(rootView.getContext()
					.getResources().getColor(R.color.stronggray));
		} else {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Traffic_fragments TF = new Traffic_fragments();
			ft.replace(R.id.reltrafficweather, TF);
			ft.commit();
			weather.setBackgroundColor(rootView.getContext().getResources()
					.getColor(R.color.stronggray));
			weather.setTextColor(rootView.getContext().getResources()
					.getColor(R.color.litegray));
			viewweather.setBackgroundColor(rootView.getContext().getResources()
					.getColor(R.color.stronggray));
		}

		traffic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				traffic.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.darkgray));
				traffic.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.litegray));
				viewtraffic.setBackgroundColor(rootView.getContext()
						.getResources().getColor(R.color.yellow));
				weather.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.stronggray));
				weather.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.darkgray));
				viewweather.setBackgroundColor(rootView.getContext()
						.getResources().getColor(R.color.stronggray));
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Traffic_fragments TF = new Traffic_fragments();
				ft.replace(R.id.reltrafficweather, TF);
				ft.commit();
			}
		});

		weather.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (new CheckConnectivity().checkNow(getActivity())) {

				weather.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.darkgray));
				weather.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.litegray));
				viewweather.setBackgroundColor(rootView.getContext()
						.getResources().getColor(R.color.yellow));
				traffic.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.stronggray));
				traffic.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.darkgray));
				viewtraffic.setBackgroundColor(rootView.getContext()
						.getResources().getColor(R.color.stronggray));

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				WeatherReport_fragment llf = new WeatherReport_fragment();
				ft.replace(R.id.reltrafficweather, llf);
				ft.commit();
				}
				else
				{
					Toast.makeText(getActivity(), R.string.no_network_msg, Toast.LENGTH_LONG).show();
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
						tx.replace(R.id.frame_container, new HomeFragment()).addToBackStack(null)
								.commit();
						return true;
					}
				}
				return false;
			}
		});
		return rootView;
	}

}
