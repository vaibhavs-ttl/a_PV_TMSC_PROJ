package com.ttl.customersocialapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SelectSpareFragment extends Fragment{
	
	
	private View view;
	
	private Button spare_parts_done_btn;
	private Button spare_parts_cancel_btn;
	private Fragment fragment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view=inflater.inflate(R.layout.select_spare_parts, container, false);
		
		
		getReferences(view);
		
		
		return view;
	}
	
	
	
	private void getReferences(View view)
	{
		
		
		spare_parts_cancel_btn=(Button)view.findViewById(R.id.spare_parts_cancel_btn);
		
		spare_parts_done_btn=(Button)view.findViewById(R.id.spare_parts_done_btn);
		
		
		
		
		
		
		
		
		
	}
	
	
	

}
