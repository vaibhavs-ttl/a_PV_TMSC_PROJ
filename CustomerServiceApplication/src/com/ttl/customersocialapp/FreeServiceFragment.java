package com.ttl.customersocialapp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
public class FreeServiceFragment extends Fragment{
	
	private Spinner spinregno , spinservicetype ;
	private InstantAutoComplete spinstate , spincity;
	private EditText labourcost ,sparecost , consumcost ;
	private List<String> regnovalues = new ArrayList<String>();
	private List<String> statevalues = new ArrayList<String>();
	private List<String> servicetypevalue = new ArrayList<String>();
	private List<String> cityvalues = new ArrayList<String>();
	private String pl="" , state, servicetype="" , city ="",regnumber="";
	private TextView totalcost;
	private List<String> array1 = new  ArrayList<String>();
	private ArrayAdapter<String> servicetypeaa;
	private ArrayAdapter<String> stateaa ;
	private Button fetch;
	private View view;
	@Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState)
	{
        view = inflater.inflate(R.layout.fragment_free_service, viewGroup, false);
      
       spinregno = (Spinner) view.findViewById(R.id.spinregno);
       spinstate = (InstantAutoComplete) view.findViewById(R.id.spinstate);
       spincity = (InstantAutoComplete) view.findViewById(R.id.spincity);
       spinservicetype = (Spinner) view.findViewById(R.id.spinservicetype);
       labourcost = (EditText) view.findViewById(R.id.txtlabourcost);
       sparecost = (EditText) view.findViewById(R.id.txtsparecost);
       consumcost = (EditText) view.findViewById(R.id.txtconsumcost);
       totalcost = (TextView) view.findViewById(R.id.txttotalcost);
       regnovalues.add("Select Vehicle");
       fetch = (Button) view.findViewById(R.id.btnfetch);
     
       int size = new UserDetails().getRegNumberList().size();
       for(int i = 0 ; i<size; i++)
       {
    	   if(!(new UserDetails().getRegNumberList().get(i)
					.get("registration_num").toString().equals("")))
			{
			regnovalues.add(new UserDetails().getRegNumberList().get(i)
					.get("registration_num"));
			}else
			{
				regnovalues.add(new UserDetails().getRegNumberList().get(i)
						.get("chassis_num"));
			}
       }
       
       labourcost.setEnabled(false);
       sparecost.setEnabled(false);
       consumcost.setEnabled(false);
       ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, regnovalues);
       regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinregno.setAdapter(regno);
		array1.add(0, "Service Type");
		 servicetypeaa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, array1);
		servicetypeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	      spinservicetype.setAdapter(servicetypeaa);
	  	if(new UserDetails().getRegNumberList().size() == 0)
		{
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		}
		
       spinregno.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view1,
				int position, long id) {
			
			regnumber = parent.getItemAtPosition(position).toString();
			/* int size = new UserDetails().getRegNumberList().size();
			for(int i = 0 ; i<size; i++)
		       {
				String temp = new UserDetails().getRegNumberList().get(i).get("registration_num");
				if(temp.equals(regnumber))
				{
					pl = new UserDetails().getRegNumberList().get(i).get("pl");
				}
		       }*/
			if(position>0)
			{
				pl = new UserDetails().getRegNumberList().get(position-1).get("pl");
				array1.clear();
				array1.add(0, "Service Type");
				spinservicetype.setSelection(0);
			}else
			{
				pl="";
				array1.clear();
				array1.add(0, "Service Type");
				spinservicetype.setSelection(0);
			}
			if(!(regnumber.equals("Select Vehicle")))
					{
				CheckConnectivity checknow = new CheckConnectivity();
				boolean connect = checknow
						.checkNow(getActivity());
				if (connect) {
			String req = Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getServiceTypes";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("PL",pl));
			nameValuePairs.add(new BasicNameValuePair("serviceTypeGroup","Free"));
			nameValuePairs.add(new BasicNameValuePair("user_id",
					UserDetails.getUser_id()));
			 nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
			new AWS_WebServiceCall(getActivity(), req,ServiceHandler.POST ,Constants.getServiceTypes,
					nameValuePairs,new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							
							servicetypevalue = (List<String>) object;
							
							for(int i=0 ; i<servicetypevalue.size(); i++)
							{
								array1.add(servicetypevalue.get(i));
							
							Log.v("service types", servicetypevalue.get(i));
							
							
							}
							 servicetypeaa.notifyDataSetChanged();
							 backpress(view);
						}

						@Override
						public void onErrorReceive(String string) {
							
							Toast toast = Toast.makeText(getActivity(),
									"Sorry data is under development for this product. Will be available soon!",
									Toast.LENGTH_SHORT);
							toast.show();
						} }).execute();
				}
				 else {
					Toast toast = Toast.makeText(getActivity(),
							"No network connection available.",
							Toast.LENGTH_SHORT);
					toast.show();
				}
			
					}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
			
		}
	});
       CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow
				.checkNow(getActivity());
		if (connect) {
		
       String req = Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getState";
      
       new AWS_WebServiceCall(getActivity(), req,ServiceHandler.GET ,Constants.getState,
				new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						statevalues = (List<String>) object;
						
						 stateaa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, statevalues);
					     stateaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					     spinstate.setThreshold(1);
					     spinstate.setAdapter(stateaa);
					  // Removed by vaibhav because andaman is not getting select
					     // spinstate.setValidator(new Validator(statevalues));
					 	view.getRootView().setFocusable(true);
				  		view.getRootView().requestFocus();
					}

					@Override
					public void onErrorReceive(String string) {
						
						view.getRootView().setFocusable(true);
				  		view.getRootView().requestFocus();
					} }).execute();
		}
		 else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.",
					Toast.LENGTH_SHORT);
			toast.show();
		}
      
       spinstate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				spinstate.showDropDown();
			}
		});
       spinstate.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view1, int position,
				long id) {
			
			spincity.setText("");
			//spinstate.performValidation();
			state = parent.getItemAtPosition(position).toString();
			CheckConnectivity checknow = new CheckConnectivity();
			boolean connect = checknow
					.checkNow(getActivity());
			if (connect) {
				//getCityFromStateMaster
			String req = Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getCityFromState";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			
			
			nameValuePairs.add(new BasicNameValuePair("state",state));
			 //getCityFromState
			new AWS_WebServiceCall(getActivity(), req,ServiceHandler.POST ,Constants.getCityFromState,
					nameValuePairs,new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							
							/*cityvalues = (List<String>) object;
							ArrayAdapter<String> cityaa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, cityvalues);
						    cityaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						    spincity.setThreshold(1);
						    spincity.setAdapter(cityaa);*/
							
							
							cityvalues = (List<String>) object;

							
							
							
							ArrayAdapter<String> aa = new ArrayAdapter<String>(
									getActivity(),
									android.R.layout.simple_spinner_item,
									cityvalues);

							spincity.setThreshold(1);

							spincity.setAdapter(aa);

							
							
							
							
						 // Removed by vaibhav because andaman is not getting select
						    //    spincity.setValidator(new Validator(cityvalues));
						 
						    
						    
						    
						    
						    spincity.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									
									spincity.showDropDown();
								}
							});
						    
						    spincity.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									
									city = parent.getItemAtPosition(position).toString();
								    servicetype = spinservicetype.getSelectedItem().toString();
								  // spincity.performValidation();
								   // calculateCost(pl , city , servicetype);
								}
							});
							view.getRootView().setFocusable(true);
					  		view.getRootView().requestFocus();
					  	//	spincity.is
						}

						@Override
						public void onErrorReceive(String string) {
							
							view.getRootView().setFocusable(true);
					  		view.getRootView().requestFocus();
						} }).execute();
			}
			 else {
				Toast toast = Toast.makeText(getActivity(),
						"No network connection available.",
						Toast.LENGTH_SHORT);
				toast.show();
			}
			
		}
	});
       
       fetch.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		//	spincity.performValidation();
			 consumcost.setText("");
			 labourcost.setText("");
			 sparecost.setText("");
			city = spincity.getText().toString();
			/*for(int i=0; i<cityvalues.size(); i++)
			{*/
				/*if(!(cityvalues.contains(city)))
				{
					Toast.makeText(getActivity(), "Please select valid city.", Toast.LENGTH_LONG).show();
				}*/
			//}
			
			state = spinstate.getText().toString();
		    servicetype = spinservicetype.getSelectedItem().toString();
		    /*regnumber = spinregno.getSelectedItem().toString();
		    int size = new UserDetails().getRegNumberList().size();
			for(int i = 0 ; i<size; i++)
		       {
				String temp = new UserDetails().getRegNumberList().get(i).get("registration_num");
				if(temp.equals(regnumber))
				{
					pl = new UserDetails().getRegNumberList().get(i).get("pl");
				}
		       }*/
			
			if(!(pl.equals("")))
			{
				if(!(servicetype.equals("Service Type")))
				{						   

					if(!(state.equals("")))
   					{
   						if(statevalues.contains(state))
						{
	   					if(!(city.equals("")))
	   					{
	   						
	   						if(!(cityvalues.contains(city)))
							{
								Toast.makeText(getActivity(), "Please select city form list.", Toast.LENGTH_SHORT).show();
							}else
								calculateCost(pl, city, servicetype);
	   					}else
	   					{
	   						Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
	   					}
						}else
						{
							Toast.makeText(getActivity(), "Please select state from list..", Toast.LENGTH_SHORT).show();

						}
   					}else
   					{
   						Toast.makeText(getActivity(), "Please select state.", Toast.LENGTH_SHORT).show();
   					}
				}else
				{
					Toast.makeText(getActivity(), "Please select service type.", Toast.LENGTH_LONG).show();
				}
			}else
			{
				Toast.makeText(getActivity(), "Please select Vehicle.", Toast.LENGTH_LONG).show();
			}
		}
	});
   	view.getRootView().setFocusableInTouchMode(true);
   	view.getRootView().setFocusable(true);
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
	public void calculateCost(String pl , String city , String servicetype)
	{
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		    nameValuePairs.add(new BasicNameValuePair("PL",pl));	
		    nameValuePairs.add(new BasicNameValuePair("serviceType",servicetype));	
			nameValuePairs.add(new BasicNameValuePair("city",city));
			  nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
			  nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
			CheckConnectivity checknow = new CheckConnectivity();
			boolean connect = checknow
					.checkNow(getActivity());
			if (connect) {
			String req = Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getFreeServiceCostEstimate";
			new AWS_WebServiceCall(getActivity(), req,ServiceHandler.POST ,Constants.getFreeServiceCostEstimate,
					nameValuePairs,new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							
							 ArrayList<HashMap<String, String>> costList  =(ArrayList<HashMap<String, String>>) object;
							 for(int i= 0 ;i<costList.size(); i++)
							 {
								 if(costList.get(i).get("costType").equals("Consumable"))
								 {
									 consumcost.setText(costList.get(i).get("cost"));
								 }else if(costList.get(i).get("costType").equals("Labour"))
								 {
									 labourcost.setText(costList.get(i).get("cost"));
								 }else
								 {
									 sparecost.setText(costList.get(i).get("cost"));

								 }
							 }
							 
							 String cos = consumcost.getText().toString();
							 	String lab = labourcost.getText().toString();
							 	String spar = sparecost.getText().toString();
							 	if(cos.equals(""))
							 	{
							 		cos = "0";
							 		 consumcost.setText("0");
							 	}													 	
							 	if(lab.equals(""))
							 	{
							 		lab="0";
							 		labourcost.setText("0");
							 	}
							 	
							 	if(spar.equals(""))
							 	{
							 		spar = "0";
							 		sparecost.setText("0");
							 	}

							 	try {
								
							 		  int total =Integer.parseInt(cos) + Integer.parseInt(lab) + Integer.parseInt(spar);
							 		 totalcost.setText(""+total);		
								} catch (Exception e) {
								    	
									Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.cost_est_data_empty), Toast.LENGTH_LONG).show();
									
									
								}
							 	
							 	
						 
						  
						      
						       
						      
						    	backpress(view);
						}

						@Override
						public void onErrorReceive(String string) {
							
							
						} }).execute();
			}
			 else {
				Toast toast = Toast.makeText(getActivity(),
						"No network connection available.",
						Toast.LENGTH_SHORT);
				toast.show();
			}
	}
	
	public void backpress(View view)
    {
  	  view.getRootView().setFocusableInTouchMode(true);
  	   	view.getRootView().setFocusable(true);
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
    }
	
/*	class FocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
           // Log.v("Test", "Focus changed");
        	switch(v.getId())
        	{
        	case R.id.spincity:
        		
        	}
            if (v.getId() == R.id.spincity && !hasFocus) {
           //     Log.v("Test", "Performing validation");
                ((AutoCompleteTextView)v).performValidation();
            }
        }
    }*/
}
