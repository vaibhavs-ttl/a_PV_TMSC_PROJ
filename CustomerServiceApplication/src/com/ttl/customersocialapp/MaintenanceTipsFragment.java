package com.ttl.customersocialapp;

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
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.helper.AnalyticsApplication;

public class MaintenanceTipsFragment extends Fragment {

	
	private static String value = null;
	private LinearLayout llprecaution, llsteering, llbrakes, llbattery, llengine,
			lltiries, llexteriorcare, llinteriorcare, lltroubleshooting,
			lltipshygiene, llsafety, llwipers, llelectrical,
			llalertsindications,llshocks;


	
	Tracker mTracker;
	
	@Override
	public void onStart() {

		super.onStart();
		mTracker.setScreenName("MaintenanceTipsScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_maintenance_tips,
				viewGroup, false);

		llprecaution = (LinearLayout) v.findViewById(R.id.llprecaution);
		llsteering = (LinearLayout) v.findViewById(R.id.llsteering);
		llbrakes = (LinearLayout) v.findViewById(R.id.llbrakes);
		llbattery = (LinearLayout) v.findViewById(R.id.llbattery);
		llengine = (LinearLayout) v.findViewById(R.id.llengine);
		lltiries = (LinearLayout) v.findViewById(R.id.lltiries);
		llexteriorcare = (LinearLayout) v.findViewById(R.id.llexteriorcare);
		llinteriorcare = (LinearLayout) v.findViewById(R.id.llinteriorcare);
		lltroubleshooting = (LinearLayout) v
				.findViewById(R.id.lltroubleshooting);
		lltipshygiene = (LinearLayout) v.findViewById(R.id.lltipshygiene);
		llsafety = (LinearLayout) v.findViewById(R.id.llsafety);
		llwipers = (LinearLayout) v.findViewById(R.id.llwipers);
		llelectrical = (LinearLayout) v.findViewById(R.id.llelectrical);
		llalertsindications = (LinearLayout) v
				.findViewById(R.id.llalertsindications);
		llshocks = (LinearLayout)v.findViewById(R.id.llshocks);

		llprecaution.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				value = "PRECAUTION";
				callfrag(value);

			}
		});

		llsteering.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "STEERING";
				callfrag(value);

			}
		});
		llbrakes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "BRAKES";
				callfrag(value);

			}
		});

		llbattery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "BATTERY";
				callfrag(value);

			}
		});

		//Tracker
		 AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
					mTracker = application.getDefaultTracker();
		llengine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "ENGINE";
				callfrag(value);

			}
		});

		lltiries.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "TYRES";
				callfrag(value);

			}
		});

		llexteriorcare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "EXTERIOR CARE";
				callfrag(value);

			}
		});

		llinteriorcare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "INTERIOR CARE";
				callfrag(value);

			}
		});

		lltroubleshooting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "TROUBLE SHOOTING";
				callfrag(value);

			}
		});

		lltipshygiene.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "TIPS & HYGIENE";
				callfrag(value);

			}
		});

		llsafety.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "SAFETY";
				callfrag(value);

			}
		});

		llwipers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "WIPERS";
				callfrag(value);

			}
		});
		llelectrical.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "ELECTRICAL";
				callfrag(value);

			}
		});

		llalertsindications.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "ALERTS & INDICATIONS";
				callfrag(value);

			}
		});
		llshocks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = "PARTS";
				callfrag(value);

			}
		});

		
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

	protected void callfrag(String value2) {
		Fragment fr = new MaintaintipsListFragment();
		FragmentManager fm = getFragmentManager();
		android.app.FragmentTransaction ft = fm.beginTransaction();
		Bundle args = new Bundle();
		args.putString("CID", value2);
		fr.setArguments(args);
		ft.replace(R.id.frame_container, fr);
		ft.addToBackStack(null);
		ft.commit();
	}

}
