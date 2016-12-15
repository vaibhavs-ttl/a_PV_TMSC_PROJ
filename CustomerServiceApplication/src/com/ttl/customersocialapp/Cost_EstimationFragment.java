package com.ttl.customersocialapp;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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

public class Cost_EstimationFragment extends Fragment implements OnClickListener {
	
	FragmentManager fragmentManager;
	Fragment fragment;
	View rootview;
	private Button btnfree,btnpaid,linefree,linepaid;
	private Button btnManual;
	private Button lineManual;
	Tracker mTracker;
	
	@Override
	public void onStart() {
		
		super.onStart();
		mTracker.setScreenName("CostEstimateCalculatorScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	@Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) 
	{
		rootview = inflater.inflate(R.layout.fragment_cost__estimationn, viewGroup, false);
		AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
		mTracker = application.getDefaultTracker();
		
		fragment = new FreeServiceFragment();
		
	//	fragment = new ManualServiceFragment();
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
		.replace(R.id.relview, fragment).commit();
		
		btnfree = (Button)rootview.findViewById(R.id.btnfree);
		btnpaid = (Button)rootview.findViewById(R.id.btnpaid);
		btnManual= (Button) rootview.findViewById(R.id.btnmanual);
		
		linefree = (Button)rootview.findViewById(R.id.linebtnfree);
		linepaid =(Button)rootview.findViewById(R.id.linebtnpaid);
		lineManual = (Button) rootview.findViewById(R.id.linebtnmanual);
	        
		btnfree.setOnClickListener(this);
		btnpaid.setOnClickListener(this);
		btnManual.setOnClickListener(this);
		
		
		btnfree.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.darkgray));
		btnfree.setTextColor(rootview.getContext().getResources().getColor(R.color.litegray));
		linefree.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.yellow));
			
		 btnpaid.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
		 btnpaid.setTextColor(rootview.getContext().getResources().getColor(R.color.darkgray));
		 linepaid.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
		 
		 lineManual.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.yellow));
		 btnManual.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
		 btnManual.setTextColor(rootview.getContext().getResources().getColor(R.color.darkgray));
		
		
		rootview.getRootView().setFocusableInTouchMode(true);
		rootview.getRootView().requestFocus();

	
		if(getArguments()!=null){
			   if(getArguments().getString("Fragment").equals("BookServiceFragment")){
			    rootview.getRootView().setOnKeyListener(new OnKeyListener() {
			     @Override
			     public boolean onKey(View v, int keyCode, KeyEvent event) {
			      if (event.getAction() == KeyEvent.ACTION_DOWN) {
			       if (keyCode == KeyEvent.KEYCODE_BACK) {      
			        FragmentManager fm = getFragmentManager();
			        FragmentTransaction tx = fm.beginTransaction();
			        tx.replace(R.id.frame_container, new BookServiceFragment())
			          .commit();
			        return true;
			       }
			      }
			      return false;
			     }
			    });
			   }
			  }else{
			   rootview.getRootView().setOnKeyListener(new OnKeyListener() {
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
			  }
		
		return rootview;
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
				case R.id.btnfree:
					
					btnfree.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					btnfree.setTextColor(rootview.getContext().getResources().getColor(R.color.litegray));
					linefree.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.yellow));
						
					 btnpaid.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					 btnpaid.setTextColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					 linepaid.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					 
					 lineManual.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.yellow));
					 btnManual.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					 btnManual.setTextColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					
					 
						
				     fragment = new FreeServiceFragment();
					 fragmentManager = getFragmentManager();
					 fragmentManager.beginTransaction()
					 .replace(R.id.relview, fragment).commit();
					 mTracker.send(new HitBuilders.EventBuilder()
					 .setCategory("ui_action")
					 .setAction("button_press")
					 .setLabel("CostEstimateFreeService")
					 .build());
					break;
			
				case R.id.btnpaid:
					
					btnpaid.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					btnpaid.setTextColor(rootview.getContext().getResources().getColor(R.color.litegray));
					linepaid.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					
					 btnfree.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					 btnfree.setTextColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					 linefree.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.yellow));
					 
					 btnManual.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					 btnManual.setTextColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					 lineManual.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.yellow));
					 
					 
			
					 fragment = new PaidServiceFragment();
					 fragmentManager = getFragmentManager();
					 fragmentManager.beginTransaction()
					 .replace(R.id.relview, fragment).commit();
					 mTracker.send(new HitBuilders.EventBuilder()
					 .setCategory("ui_action")
					 .setAction("button_press")
					 .setLabel("CostEstimatePaidService")
					 .build());
					break;
					
					
				case R.id.btnmanual:
					
					btnManual.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					btnManual.setTextColor(rootview.getContext().getResources().getColor(R.color.litegray));
					lineManual.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					
					 btnfree.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					 btnfree.setTextColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					 linefree.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.yellow));
					 
					 btnpaid.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.stronggray));
					 btnpaid.setTextColor(rootview.getContext().getResources().getColor(R.color.darkgray));
					 linepaid.setBackgroundColor(rootview.getContext().getResources().getColor(R.color.yellow));
					
					 
			
					 fragment = new ManualServiceFragment();
					 fragmentManager = getFragmentManager();
					 fragmentManager.beginTransaction()
					 .replace(R.id.relview, fragment).commit();
					 mTracker.send(new HitBuilders.EventBuilder()
					 .setCategory("ui_action")
					 .setAction("button_press")
					 .setLabel("CostEstimatePaidService")
					 .build());
					break;
			
					
			}
		}
}
