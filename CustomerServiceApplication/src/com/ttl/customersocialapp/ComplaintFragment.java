package com.ttl.customersocialapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;

public class ComplaintFragment extends Fragment {
	
	FragmentManager fragmentManager;
	Fragment fragment;
	View rootview;
	@Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) 
	{
		rootview = inflater.inflate(R.layout.compalingfragment, viewGroup, false);
		
		fragment = new ComplaintRegistrationFragment();
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
		.replace(R.id.relview, fragment).commit();
	
		rootview.getRootView().setFocusableInTouchMode(true);
		rootview.getRootView().requestFocus();

	
		
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
		
		
		return rootview;
	}

}
