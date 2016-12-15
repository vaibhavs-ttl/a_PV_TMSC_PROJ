package com.ttl.customersocialapp;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.UserDetails;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

public class FeedbackFragment extends Fragment {
	FragmentManager fragmentManager;
	Fragment fragment;

	Button txtgen, txtpost, linegen, linepost;
	View view;

	Tracker mTracker;

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mTracker.setScreenName("FeedbackScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_feedback, viewGroup, false);

		/*
		 * super.onCreate(savedInstanceState);
		 * setContentView(R.layout.fragment_feedback);
		 */
		AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
		mTracker = application.getDefaultTracker();
		txtgen = (Button) view.findViewById(R.id.btngen);
		txtpost = (Button) view.findViewById(R.id.btnpost);
		linegen = (Button) view.findViewById(R.id.linebtngen);
		linepost = (Button) view.findViewById(R.id.linebtnpost);

		fragment = new GenericFeedbakFragment();
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.relview, fragment)
				.commit();
		if (HomeFragment.psfNotifiction == true) {
			 	
			post();
			txtgen.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					geniric();
					 mTracker.send(new HitBuilders.EventBuilder()
					 .setCategory("ui_action")
					 .setAction("button_press")
					 .setLabel("GenericFeedback")
					 .build());
				}
			});
			

			txtpost.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					post();
					 mTracker.send(new HitBuilders.EventBuilder()
					 .setCategory("ui_action")
					 .setAction("button_press")
					 .setLabel("PostServiceFeedback")
					 .build());
				}
			});
			HomeFragment.psfNotifiction=false;
		} else {

			final Dialog dialog = new Dialog(view.getContext());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.feedback_popup);
			dialog.show();

			ImageView img = (ImageView) dialog.findViewById(R.id.imgclose);
			Button btngen = (Button) dialog.findViewById(R.id.btngen);
			Button btnpost = (Button) dialog.findViewById(R.id.btnpost);

			// if button is clicked, close the custom dialog
			img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			btngen.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					geniric();
					dialog.dismiss();
				}
			});
			btnpost.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					post();
					dialog.dismiss();
				}
			});

			


			txtgen.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				
					
					geniric();
					
				
				
				
				}
			});

			txtpost.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					post();

				}
			});
		}

		view.getRootView().setFocusableInTouchMode(true);
		view.getRootView().requestFocus();

		view.getRootView().setOnKeyListener(new OnKeyListener() {
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

		return view;

	}

	public void geniric() {
		txtgen.setBackgroundColor(view.getContext().getResources()
				.getColor(R.color.darkgray));
		txtgen.setTextColor(view.getContext().getResources()
				.getColor(R.color.litegray));
		linegen.setBackgroundColor(view.getContext().getResources()
				.getColor(R.color.yellow));

		txtpost.setBackgroundColor(view.getContext().getResources()
				.getColor(R.color.stronggray));
		txtpost.setTextColor(view.getContext().getResources()
				.getColor(R.color.darkgray));
		linepost.setBackgroundColor(view.getContext().getResources()
				.getColor(R.color.stronggray));

		fragment = new GenericFeedbakFragment();
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.relview, fragment)
				.commit();
	}

	public void post() {
		txtpost.setBackgroundColor(view.getContext().getResources()
				.getColor(R.color.darkgray));
		txtpost.setTextColor(view.getContext().getResources()
				.getColor(R.color.litegray));
		linepost.setBackgroundColor(view.getContext().getResources()
				.getColor(R.color.yellow));

		txtgen.setBackgroundColor(view.getContext().getResources()
				.getColor(R.color.stronggray));
		txtgen.setTextColor(view.getContext().getResources()
				.getColor(R.color.darkgray));
		linegen.setBackgroundColor(view.getContext().getResources()
				.getColor(R.color.stronggray));
		

		Bundle bundle = getArguments();
		if (bundle != null) {
			Log.d("BUNDLE FeedbackFragment in",
					bundle.getString("Reg_number") + " "
							+ bundle.getString("jobcard_number") + " "
							+ bundle.getString("services_type")+" "+bundle.getString("services_date"));
			}

		fragment = new PostServiceFeedbackFragment();
		fragment.setArguments(bundle);
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.relview, fragment)
				.commit();
	}

}
