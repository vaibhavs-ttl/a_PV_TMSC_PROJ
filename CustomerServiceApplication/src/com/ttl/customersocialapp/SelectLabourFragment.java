package com.ttl.customersocialapp;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ttl.adapter.LabourAdapter;
import com.ttl.communication.FragmentResponseCallback;
import com.ttl.communication.GlobalAccessObject;
import com.ttl.model.LabourModel;
import com.ttl.webservice.AWS_WebServiceCall;

public class SelectLabourFragment extends DialogFragment implements OnClickListener,TextWatcher{

	
	private Button labour_parts_done_btn;
	private Button labour_parts_cancel_btn;
	private Fragment fragment;
	private int layout;
	
	private AWS_WebServiceCall aws_WebServiceCall;
	private View view;
	private TextView lab_text;
	private EditText qty_text;
	private CheckBox check_data;
	private ArrayList<LabourModel> labour_list=new ArrayList<>();
	private ListView labour_data_list;
	private EditText search_bar;
	private int TARGET_REQUEST_CODE=101;
	private LabourAdapter adapter;
	private FragmentResponseCallback fragmentResponseCallback;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 view=inflater.inflate(R.layout.select_labour_parts, container, false);
		
	

		
		
		getReferences(view);

		
		setHandlers();
		getData();
	
		
		

		return view;

		
	}
	;


	
	private void getData()
	{
	
	Bundle bundle=new Bundle();
	bundle=getArguments();
	
	
	labour_list=(ArrayList<LabourModel>)bundle.getSerializable("labour_list");
	//LabourAdapter adapter=new LabourAdapter(getActivity(), labour_list);
	
	labour_data_list.setAdapter(adapter);
	
	adapter.notifyDataSetChanged();
	
	}
	
	
	

	
	private void setHandlers()
	{
		
		
		labour_parts_done_btn.setOnClickListener(this);
		labour_parts_cancel_btn.setOnClickListener(this);
		search_bar.addTextChangedListener(this);
	
		
		
	}

	
	
	
	
	
	
	
	
	private void getReferences(View view)
	{
		
		labour_parts_cancel_btn=(Button)view.findViewById(R.id.labour_parts_cancel_btn);
		labour_parts_done_btn=(Button)view.findViewById(R.id.labour_parts_done_btn);
		labour_data_list=(ListView)view.findViewById(R.id.labour_list);
		search_bar=(EditText)view.findViewById(R.id.labour_part_search_bar);
	}
	
	@Override
	public void onClick(View v) {

		
		if (v.getId()==R.id.labour_parts_done_btn) {
		
		
			Log.v("done button called", "called");
			
			if (GlobalAccessObject.getLabour_obj()!=null) {
				
				Log.v("data set returned", "data found");
			}
			else
			{
				Log.v("data set returned", "null found");
			}
			
			

		Intent dest=new Intent();
		dest.putExtra("selected_labour_data", labour_list);
		//getTargetFragment().onActivityResult(TARGET_REQUEST_CODE, Activity.RESULT_OK,dest);
		
		fragmentResponseCallback=(FragmentResponseCallback) getActivity();
		
		fragmentResponseCallback.getList(labour_list);
		
		dismiss();
		
		
		
		
		
		}
		else if(v.getId()==R.id.labour_parts_cancel_btn)
		{
			
			dismiss();	
			
		}
		

	
	}
	@Override
	public void afterTextChanged(Editable s) {
	
		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		if (adapter!=null) {
		//	adapter.getFilter().filter(s);		
		}
		else
		{
			
			Toast.makeText(getActivity(), "Null found", Toast.LENGTH_LONG).show();
		}
		
	
		
	}
	
	
	
	

	
	
	
}
