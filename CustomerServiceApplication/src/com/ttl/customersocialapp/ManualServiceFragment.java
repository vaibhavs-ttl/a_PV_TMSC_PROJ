package com.ttl.customersocialapp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ttl.adapter.ManualEstimateAdapter;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.communication.GlobalAccessObject;
import com.ttl.communication.SendEstimateReport;
import com.ttl.helper.CheckValidations;
import com.ttl.model.LabourModel;
import com.ttl.model.LabourRateModel;
import com.ttl.model.LabourSpareModel;
import com.ttl.model.ServiceHandler;
import com.ttl.model.SpareModel;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class ManualServiceFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	
	private View view;
	
	private ArrayList<String> vehicles=new ArrayList<>();
	public static double labourCost=0.0;
	public static double spareCost=0.0;
	private Spinner manual_est_vehicle_spinner;
	public static TextView txtLabourSpareNote;
	public static TextView txtCalcalationNote;
	public static View header_view1;
	public static View header_view2;
	
	private InstantAutoComplete manual_est_state_spinner;
	private InstantAutoComplete manual_est_city_spinner;
	private Button manual_est_select_labour;
	private Button manual_est_select_spares;
	public static Button manual_est_calculate_btn;
	public static Button manual_est_reset_btn;
	public static Button manual_est_email_btn;
	private AWS_WebServiceCall aws_WebServiceCall;
	private List<String> states=new ArrayList<>();
	private List<String> cities=new ArrayList<>();
	private ArrayAdapter<String> state_adapter;
	private ArrayAdapter<String> city_adapter;
	private CheckConnectivity check;
	
	private String selected_reg_no;
	public static ManualEstimateAdapter manualEstimateAdapter;
	public static ArrayList<LabourModel> selected_labour_data;
	public static ArrayList<SpareModel> selected_spare_data;
	private double ratePerHour;
	
	public static ArrayList<LabourSpareModel> labourSpareList=new ArrayList<>();
	
	private String selected_chassis;
	private String selected_registration;
	private String selected_state;
	private String selected_city;
	private String selected_PPL;
	private ListView selected_labour_spare_data;
	public static TextView approx_labour_amt;
	public static TextView approx_spare_amt;
	public static LinearLayout manual_est_selected_items_header;
	private ArrayList<LabourModel> labour_list;
	private ArrayList<SpareModel> spare_list;
	NumberFormat nf = NumberFormat.getInstance();
	
	
	@Override
	public void onStart() {
	
		super.onStart();
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.manual_estimate_calculator, viewGroup, false);
	
		getReferences(view);
		
		setHandlers();
		hide();

		getStates();
	
		manual_est_vehicle_spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getVehicles()));
		
		
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(2);
		
		view.getRootView().setFocusableInTouchMode(true);
		view.getRootView().requestFocus();

		view.getRootView().setOnKeyListener(new OnKeyListener() {
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
		
		
		return view;
	
	
	
	
	}
	
	
	
	
	private void getReferences(View view)
	{
	
		manual_est_vehicle_spinner=(Spinner)view.findViewById(R.id.manual_est_vehicle_spinner);
		manual_est_city_spinner=(InstantAutoComplete)view.findViewById(R.id.manual_est_city_spinner);
		manual_est_state_spinner=(InstantAutoComplete)view.findViewById(R.id.manual_est_state_spinner);
		manual_est_select_spares=(Button)view.findViewById(R.id.manual_est_select_spares);
		manual_est_select_labour=(Button)view.findViewById(R.id.manual_est_select_labour);
		manual_est_calculate_btn=(Button)view.findViewById(R.id.manual_est_calculate_btn);
		manual_est_email_btn=(Button)view.findViewById(R.id.manual_est_email_btn);
		manual_est_reset_btn=(Button)view.findViewById(R.id.manual_est_reset_btn);
		selected_labour_spare_data=(ListView)view.findViewById(R.id.mnaual_est_selected_data_list);
		approx_labour_amt=(TextView)view.findViewById(R.id.approx_labour_amt);
		approx_spare_amt=(TextView)view.findViewById(R.id.approx_spare_amt);
		
		txtCalcalationNote=(TextView)view.findViewById(R.id.txtCalcalationNote);
		txtLabourSpareNote=(TextView)view.findViewById(R.id.txtLabourSpareNote);
		header_view1=(View)view.findViewById(R.id.view_manual_est_selected_items_header1);
		header_view2=(View)view.findViewById(R.id.view_manual_est_selected_items_header2);
		manual_est_selected_items_header=(LinearLayout)view.findViewById(R.id.manual_est_selected_items_header);

		check=new CheckConnectivity();
		
		
		
		
	}	
	
	
	
	private void setHandlers()
	{
		
		manual_est_vehicle_spinner.setOnItemSelectedListener(this);
		manual_est_city_spinner.setOnClickListener(this);
		manual_est_state_spinner.setOnClickListener(this);
		manual_est_email_btn.setOnClickListener(this);
		manual_est_calculate_btn.setOnClickListener(this);
		manual_est_reset_btn.setOnClickListener(this);
		manual_est_select_labour.setOnClickListener(this);
		manual_est_select_spares.setOnClickListener(this);
	
		
		
		
		manual_est_state_spinner.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			
				
				if (s.length()==0) {
					
					selected_state=null;
					
				}
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			
				
			
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
				
			}
		});
		
		manual_est_city_spinner.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			
				
				if (s.length()==0) {
					
					selected_city=null;
					
				}
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			
				
			
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
				
			}
		});
		
		
		
		
		
		
		manual_est_state_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long arg3) {
			
				
					
					if (check.checkNow(getActivity())) {
					
						resetView();
						manual_est_city_spinner.setText("");
						city_adapter=null;
						GlobalAccessObject.setLabourModelLoaded(false);
						GlobalAccessObject.setSpareModelLoaded(false);
						
					/*	if (manualEstimateAdapter!=null) {
							
							resetView();
							manual_est_city_spinner.setText("");
							city_adapter=null;
							GlobalAccessObject.setLabourModelLoaded(false);
							GlobalAccessObject.setSpareModelLoaded(false);
						}
					*/	
						selected_state=parent.getItemAtPosition(pos).toString();
						manual_est_city_spinner.setText("");
						city_adapter=null;
						setCities(selected_state);
						
						
					//GlobalAccessObject.NullifyLabour_obj();
					//	GlobalAccessObject.NullifySpare_obj();				
					}
					else
					{
						Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
					}
				
			
					
			}
		
		
		
		});
		
		manual_est_city_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long arg3) {
			
				
			
					if (check.checkNow(getActivity())) {
					
						
						try {
							
							if (manualEstimateAdapter!=null) {
							resetView();	
							}
							
							selected_city=parent.getItemAtPosition(pos).toString();
							
							
							Log.v("selected city", selected_city);
							getLabourRates();		
							
						} catch (Exception e) {
						
							
							Log.v("city execepion", e.toString());
							
						}
					
					
					/*	GlobalAccessObject.NullifyLabour_obj();
						
						GlobalAccessObject.NullifySpare_obj();
*/						
				
					}
					else
					{
						
						Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
					}
				
				
				
			
			}
		
		
		
		});
	
		
		
		
	}
	
	private ArrayList<String> getVehicles()
	{
		
		int size = new UserDetails().getRegNumberList().size();
	    vehicles.add(getString(R.string.select_vehicle));   
		for(int i = 0 ; i<size; i++)
	       {
			
		
			
	    	   if(!(new UserDetails().getRegNumberList().get(i)
						.get("registration_num").toString().equals("")))
				{
				vehicles.add(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"));
				}else
				{
					vehicles.add(new UserDetails().getRegNumberList().get(i)
							.get("chassis_num"));
				}
	       }
		
		return vehicles;
	}




@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {

	
	
	if (resultCode==Activity.RESULT_OK && requestCode==Constants.LABOUR_REQUEST_CODE) {
		
				
		selected_labour_data=(ArrayList<LabourModel>) data.getSerializableExtra("labour_list_data");
		LabourSpareModel labourSpareModel=null;
		
	
		
		for (int i = 0; i < selected_labour_data.size(); i++) {
			
			

			if (labourSpareList.size()==0) {
				
			
			labourSpareModel=new LabourSpareModel();
			labourSpareModel.setDesc(selected_labour_data.get(i).getLabourDescription());
			labourSpareModel.setQty(selected_labour_data.get(i).getDefaultQty());
			labourSpareModel.setType(selected_labour_data.get(i).getType());
			labourSpareModel.setServiceType(selected_labour_data.get(i).getLabourType());
			labourSpareModel.setBillingHours(selected_labour_data.get(i).getBillingHours());
			labourSpareList.add(labourSpareModel);
			Log.v("labourSpareList", "called first time");
			
			}
			else if(searchListItems(selected_labour_data.get(i).getLabourDescription())==false)
			{
		
				labourSpareModel=new LabourSpareModel();
				labourSpareModel.setDesc(selected_labour_data.get(i).getLabourDescription());
				labourSpareModel.setQty(selected_labour_data.get(i).getDefaultQty());
				labourSpareModel.setType(selected_labour_data.get(i).getType());
				labourSpareModel.setServiceType(selected_labour_data.get(i).getLabourType());
				labourSpareModel.setBillingHours(selected_labour_data.get(i).getBillingHours());
				labourSpareList.add(labourSpareModel);
			
				
			}
	
		}	

		if (labourSpareList.size()>0) {
			manualEstimateAdapter=new ManualEstimateAdapter(getActivity(), labourSpareList,ratePerHour);
			manualEstimateAdapter.notifyDataSetChanged();
		
			selected_labour_spare_data.setAdapter(manualEstimateAdapter);
			unhide();			
		}
		
		
		
	}
	
	else if(resultCode==Activity.RESULT_CANCELED && requestCode==Constants.LABOUR_REQUEST_CODE)
	{
		
		
		/*Log.v("called cancelled", "LABOUR_REQUEST_CODE");
		if (selected_labour_data==null) {
			
			txtLabourSpareNote.setVisibility(View.VISIBLE);
		}*/
		
		
		
	}
	/*else if(resultCode==Activity.RESULT_CANCELED && requestCode==Constants.LABOUR_REQUEST_CODE)
	{
		Log.v("cancelled", "called");
		txtLabourSpareNote.setVisibility(View.VISIBLE);
		if (GlobalAccessObject.getLabour_obj()==null) {
			txtLabourSpareNote.setVisibility(View.VISIBLE);	
		}
		
		
		
	}*/
	
	else if(resultCode==Activity.RESULT_OK && requestCode==Constants.SPARE_REQUEST_CODE)
	{
		
			
		selected_spare_data=(ArrayList<SpareModel>) data.getSerializableExtra("spare_list_data");		
			
		LabourSpareModel labourSpareModel=null;
		for (int i = 0; i < selected_spare_data.size(); i++) {
			
			if (labourSpareList.size()==0) {

			labourSpareModel=new LabourSpareModel();
			labourSpareModel.setDesc(selected_spare_data.get(i).getPartDescription());
		
			labourSpareModel.setQty(selected_spare_data.get(i).getDefaultQty());
			
			labourSpareModel.setType(selected_spare_data.get(i).getType());
			
			labourSpareModel.setUmrp(selected_spare_data.get(i).getUMRP());	
			
			labourSpareModel.setUom(selected_spare_data.get(i).getUOM());
			
			labourSpareList.add(labourSpareModel);
			
			}
			else if(searchListItems(selected_spare_data.get(i).getPartDescription())==false)
			{
				
				labourSpareModel=new LabourSpareModel();
				labourSpareModel.setDesc(selected_spare_data.get(i).getPartDescription());
			
				labourSpareModel.setQty(selected_spare_data.get(i).getDefaultQty());
				
				labourSpareModel.setType(selected_spare_data.get(i).getType());
				
				labourSpareModel.setUmrp(selected_spare_data.get(i).getUMRP());	
				
				labourSpareModel.setUom(selected_spare_data.get(i).getUOM());
				
				labourSpareList.add(labourSpareModel);
			
				
				
			}
			
			
			
			
		}
		

		
		if (labourSpareList.size()>0) {
			manualEstimateAdapter=new ManualEstimateAdapter(getActivity(), labourSpareList,ratePerHour);
			manualEstimateAdapter.notifyDataSetChanged();
			selected_labour_spare_data.setAdapter(manualEstimateAdapter);
			unhide();			
		}
		
		
	
	}
	
	

}


	private boolean searchListItems(String temp)
	{
		
		boolean check = false;
		
		for (int i = 0; i < labourSpareList.size(); i++) {
			
			
			Log.v("both values", "temp: "+temp+" origianl: "+labourSpareList.get(i).getDesc());
			
			if (labourSpareList.get(i).getDesc().equals(temp)) {
					
				check= true;
				break;
				}
								
				
		}
		
		
		
		
		return check;
		
	}


	
	private void setCities(String state)
	{
		
	
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("state",state));
		
		aws_WebServiceCall=new AWS_WebServiceCall(getActivity(), Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getCityFromState",ServiceHandler.POST ,Constants.getCityFromState,
				nameValuePairs,new ResponseCallback() {
			
			@Override
			public void onResponseReceive(Object response) {
			
				
				try {
			
					cities=(List<String>)response;
					city_adapter=new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cities);
					city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					manual_est_city_spinner.setThreshold(1);
					manual_est_city_spinner.setAdapter(city_adapter);
					
					
				} catch (Exception e) {
					
					Log.e("Exeception Manager",e.toString());
				}
			
			}
			
			@Override
			public void onErrorReceive(String response) {
			
				Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
				
			}
		});
		
		aws_WebServiceCall.execute();
		
		
	}
	
	
	
	private void getLabourRates()
	{
		
		
		List<NameValuePair> params=new ArrayList<>();
		
		params.add(new BasicNameValuePair("user_id", UserDetails.getUser_id()));
		params.add(new BasicNameValuePair("sessionId", UserDetails.getSeeionId()));
		params.add(new BasicNameValuePair("city", selected_city));
		params.add(new BasicNameValuePair("state", selected_state));
	
	aws_WebServiceCall=new AWS_WebServiceCall(getActivity(), Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getLabourRateByCity", ServiceHandler.POST, Constants.getLabourRateByCity,params,new ResponseCallback() {
			
			@Override
			public void onResponseReceive(Object response) {
			
				
				try {
			
					
					ArrayList<LabourRateModel> rateModel=(ArrayList<LabourRateModel>)response;
					
					ratePerHour=rateModel.get(0).getRatePerHour();
					manual_est_select_labour.setVisibility(View.VISIBLE);
					manual_est_select_spares.setVisibility(View.VISIBLE);
					txtLabourSpareNote.setVisibility(View.VISIBLE);	
					
					
				} catch (Exception e) {
				
					
					Log.e("Exeception Manager",e.toString());
				}
				
				
			
				
				
			}
			
			@Override
			public void onErrorReceive(String response) {
			
				
				
				Log.v("labour rates", response);
				
				Toast.makeText(getActivity(), "Sorry, data not available for selected model.", Toast.LENGTH_LONG).show();
				
			}
		});
		
		aws_WebServiceCall.execute();
		
		
	}
	
	private void getStates()
	{
		
		
		aws_WebServiceCall=new AWS_WebServiceCall(getActivity(), Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getState", ServiceHandler.GET, Constants.getState, new ResponseCallback() {
			
			@Override
			public void onResponseReceive(Object response) {
			
				
				try {
				
					states=(List<String>)response;
					
					state_adapter=new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, states);
					state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					manual_est_state_spinner.setThreshold(1);
					
					manual_est_state_spinner.setAdapter(state_adapter);
					
					
				} catch (Exception e) {
				
					
					Log.e("Exeception Manager",e.toString());
				}
				
				
				
			}
			
			@Override
			public void onErrorReceive(String response) {
			
				Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
				
			}
		});
		
		aws_WebServiceCall.execute();
		
	}



	@Override
	public void onClick(View v) {
		
		
		if(v.getId()==R.id.manual_est_calculate_btn)
		{
			
			if (manualEstimateAdapter==null ) {
				
			
				
				
				txtLabourSpareNote.setText("");
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						txtLabourSpareNote.setText("Please select Labour and Spare items to generate estimate.");
						
					}
				}, 200);
				
			
				
			}
			else
			{
				
				caluclateCost();
			}
			
			
		}
		else if(v.getId()==R.id.manual_est_email_btn)
		{
			if (manualEstimateAdapter!=null) {
			
				CheckConnectivity check=new CheckConnectivity();
				
			
				if (check.checkNow(getActivity())) {
				
					
					
					if (approx_labour_amt.getText().length()==0 || approx_spare_amt.getText().length()==0) {
						
						
						Toast.makeText(getActivity(), "Please click on Calculate button to generate Estimate.", Toast.LENGTH_LONG).show();
					}
					else
					{
					//	generateCostReport();	
						sendInvoiceEmail(v);
					}
					
					//caluclateCost();
						
				}
				else
				{
					Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
				}
			
		
			}
			else
			{
				txtLabourSpareNote.setText("");
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						txtLabourSpareNote.setText("Please select Labour and Spare items to generate estimate.");
						
					}
				}, 200);
			}
			/*else
			{
				Toast.makeText(getActivity(), "Please select sparae or labour items.", Toast.LENGTH_LONG).show();
			}*/
			
			
		}
		else if(v.getId()==R.id.manual_est_reset_btn)
		{
			
			
			resetView();
			txtLabourSpareNote.setText("");
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					txtLabourSpareNote.setText("Please select Labour and Spare items to generate estimate.");
					
				}
			}, 200);
			
			
			
		
		}
		else if(v.getId()==R.id.manual_est_select_labour)
		{
			
			
			if (GlobalAccessObject.isLabourModelLoaded()) {
			
				
				if (selected_PPL!=null) {
				
					Bundle bundle=new Bundle();
					bundle.putSerializable("labour_list", labour_list);
					
					startActivityForResult(new Intent(getActivity(), SelectLabourActivity.class).putExtras(bundle), Constants.LABOUR_REQUEST_CODE);

				}
				else
				{
					Toast.makeText(getActivity(), "Please select vehicle.", Toast.LENGTH_LONG).show();
				}
				
								
			
			}
			else if (check.checkNow(getActivity())) {
			
				if (selected_city!=null && selected_state!=null && selected_PPL!=null) {
					getLabourData();	
				}
				else if(selected_PPL==null)
				{
				
					Toast.makeText(getActivity(), "Please select vehicle.", Toast.LENGTH_LONG).show();
					
					
				}
				else if(selected_state==null)
					
				{
					Toast.makeText(getActivity(), "Please select State.", Toast.LENGTH_LONG).show();
				}
				else if(selected_city==null)
				{
					Toast.makeText(getActivity(), "Please select City.", Toast.LENGTH_LONG).show();
				}
				
			}
			else
			{
				Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
				
			}
		
		}
		else if(v.getId()==R.id.manual_est_select_spares)
		{
			
				
			if (GlobalAccessObject.isSpareModelLoaded()) {
			
				
				if (selected_PPL!=null) {
					
				
				
				
				Bundle bundle=new Bundle();
				bundle.putSerializable("spare_list", spare_list);
				
				startActivityForResult(new Intent(getActivity(), SelectSpareActivity.class).putExtras(bundle), Constants.SPARE_REQUEST_CODE);
				
				}
				else
				{
					Toast.makeText(getActivity(), "Please select vehicle.", Toast.LENGTH_LONG).show();
				}
				
				}
			else if (check.checkNow(getActivity())) {
				

				
				if (selected_city!=null && selected_state!=null && selected_PPL!=null) {
					getSpareData();	
				}
				else if(selected_PPL==null)
				{
				
					Toast.makeText(getActivity(), "Please select Vehicle.", Toast.LENGTH_LONG).show();
					
					
				}
				else if(selected_state==null)
					
				{
					Toast.makeText(getActivity(), "Please select State.", Toast.LENGTH_LONG).show();
				}
				else if(selected_city==null)
				{
					Toast.makeText(getActivity(), "Please select City.", Toast.LENGTH_LONG).show();
				}
	
		
		
		
		
		}
			else
			{
				Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
				
			}
						
		}
		
				
		else if (v.getId()==R.id.manual_est_state_spinner) 
		{
			
			
			manual_est_state_spinner.showDropDown();
		
	
		}
			
	
		else if(v.getId()==R.id.manual_est_city_spinner)		
		{
			
			manual_est_city_spinner.showDropDown();
		}
		
		
		
	}
	
	
	
	private void getLabourData()
	{
		
		
		
	List<NameValuePair> params=new ArrayList<>();
		
		
	params.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
		
	params.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
	
	params.add(new BasicNameValuePair("PPL",selected_PPL));
			
	aws_WebServiceCall=new AWS_WebServiceCall(getActivity(), Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getLabourDataByPPL", ServiceHandler.POST, Constants.getLabourDataByPPL, params, new ResponseCallback() {
			
			@Override
			public void onResponseReceive(Object response) {
	
				
			labour_list=(ArrayList<LabourModel>)response;
			GlobalAccessObject.setLabourModelLoaded(true);
			
				Bundle bundle=new Bundle();
				bundle.putSerializable("labour_list", labour_list);
				
				if (GlobalAccessObject.getLabour_obj()!=null) {
				GlobalAccessObject.NullifyLabour_obj();	
				}
			
				Intent dest=new Intent(getActivity(), SelectLabourActivity.class);
				dest.putExtras(bundle);
				startActivityForResult(dest, Constants.LABOUR_REQUEST_CODE);
				
			}
			
			@Override
			public void onErrorReceive(String response) {
	
				
				Toast.makeText(getActivity(), "Sorry, data not available for selected model.", Toast.LENGTH_LONG).show();
				
				
			}
		});
		aws_WebServiceCall.execute();
		
	}
	
	
	
	
	private void getSpareData()
	{
		
		
		
	List<NameValuePair> params=new ArrayList<>();
		
		
	params.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
		
	params.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
	
	params.add(new BasicNameValuePair("PPL",selected_PPL));
			
	aws_WebServiceCall=new AWS_WebServiceCall(getActivity(), Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getPartsDataByPPL", ServiceHandler.POST, Constants.getPartsDataByPPL, params, new ResponseCallback() {
			
			@Override
			public void onResponseReceive(Object response) {
	
				GlobalAccessObject.setSpareModelLoaded(true);
				
				spare_list=(ArrayList<SpareModel>)response;
				Bundle bundle=new Bundle();
				bundle.putSerializable("spare_list", spare_list);
				if (GlobalAccessObject.getSpare_obj()!=null) {
					GlobalAccessObject.NullifySpare_obj();	
					}
			
				
				startActivityForResult(new Intent(getActivity(), SelectSpareActivity.class).putExtras(bundle), Constants.SPARE_REQUEST_CODE);
				
				
			}
			
			@Override
			public void onErrorReceive(String response) {
	
				
				Toast.makeText(getActivity(), "Sorry, data not available for selected model.", Toast.LENGTH_LONG).show();
				
				
			}
		});
	
	aws_WebServiceCall.execute();
		
		
		
	}
		
	public void resetView()
	{
	
		/*manual_est_city_spinner.setText("");
		manual_est_state_spinner.setText("");
		manual_est_vehicle_spinner.setSelection(0);*/
		
	//	selected_PPL=null;
	//	selected_registration=null;
	//	selected_chassis=null;
//		selected_city=null;
//		selected_state=null;
		
		/*if (state_adapter!=null) {
			state_adapter.clear();	
		}
		
		if (city_adapter!=null) {
			city_adapter.clear();	
		}
		*/
		
		if (manualEstimateAdapter!=null) {
			
			manualEstimateAdapter=null;
		
		}

		selected_labour_data=null;
	
		
		if (labourSpareList.size()>0) {
			labourSpareList.clear();
		}
		
		
		selected_spare_data=null;
		
		labour_list=null;
		spare_list=null;
		selected_labour_spare_data.invalidate();
		GlobalAccessObject.NullifyLabour_obj();
		GlobalAccessObject.NullifySpare_obj();
		
		GlobalAccessObject.setSpareModelLoaded(false);
		GlobalAccessObject.setLabourModelLoaded(false);
		GlobalAccessObject.Nullifylabour_spare_obj();
		GlobalAccessObject.isChanged=false;
		GlobalAccessObject.spareChanged=false;
		
		/*manual_est_city_spinner.setAdapter(null);
		manual_est_state_spinner.setAdapter(null);
		manual_est_city_spinner.clearFocus();
		manual_est_state_spinner.clearFocus();*/
		approx_labour_amt.setText("");
		approx_spare_amt.setText("");
		
		approx_labour_amt.setVisibility(View.INVISIBLE);
		approx_spare_amt.setVisibility(View.INVISIBLE);
	/*	manual_est_calculate_btn.setVisibility(View.INVISIBLE);
		manual_est_email_btn.setVisibility(View.INVISIBLE);*/
	
		manual_est_selected_items_header.setVisibility(View.INVISIBLE);		
		selected_labour_spare_data.setVisibility(View.INVISIBLE);
		/*manual_est_select_labour.setVisibility(View.INVISIBLE);
		manual_est_select_spares.setVisibility(View.INVISIBLE);
		manual_est_reset_btn.setVisibility(View.INVISIBLE);*/
		header_view1.setVisibility(View.INVISIBLE);
		header_view2.setVisibility(View.INVISIBLE);
		txtCalcalationNote.setVisibility(View.INVISIBLE);
		txtLabourSpareNote.setVisibility(View.VISIBLE);
	
	}


	
	@Override
	public void onDestroyView() {
	
		super.onDestroyView();
	
	GlobalAccessObject.NullifyLabour_obj();
	GlobalAccessObject.NullifySpare_obj();
	GlobalAccessObject.Nullifylabour_spare_obj();
	}
	
	@Override
	public void onDestroy() {
	
		super.onDestroy();
		GlobalAccessObject.NullifyLabour_obj();
		GlobalAccessObject.NullifySpare_obj();
		GlobalAccessObject.Nullifylabour_spare_obj();
	}
	
	
	private void hide()
	{
		
	//	manual_est_calculate_btn.setVisibility(View.INVISIBLE);
	//	manual_est_reset_btn.setVisibility(View.INVISIBLE);
	//	manual_est_email_btn.setVisibility(View.INVISIBLE);
	//	manual_est_select_labour.setVisibility(View.INVISIBLE);
	//	manual_est_select_spares.setVisibility(View.INVISIBLE);
		manual_est_selected_items_header.setVisibility(View.INVISIBLE);
		selected_labour_spare_data.setVisibility(View.INVISIBLE);
		approx_labour_amt.setVisibility(View.INVISIBLE);
		approx_spare_amt.setVisibility(View.INVISIBLE);
		txtCalcalationNote.setVisibility(View.INVISIBLE);
	//	txtLabourSpareNote.setVisibility(View.INVISIBLE);
		header_view1.setVisibility(View.INVISIBLE);
		header_view2.setVisibility(View.INVISIBLE);
		
	}
	
	private void unhide()
	{
	
		manual_est_selected_items_header.setVisibility(View.VISIBLE);
		selected_labour_spare_data.setVisibility(View.VISIBLE);
		//manual_est_calculate_btn.setVisibility(View.VISIBLE);
		//manual_est_reset_btn.setVisibility(View.VISIBLE);
		//manual_est_email_btn.setVisibility(View.VISIBLE);
		header_view1.setVisibility(View.VISIBLE);
		header_view2.setVisibility(View.VISIBLE);
	}




	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long arg3) {
		
		 if(parent.getId()==R.id.manual_est_vehicle_spinner)
		{
	
			 if (pos>0) {
					if (check.checkNow(getActivity())) {
				
						selected_PPL=new UserDetails().getRegNumberList().get(pos-1).get("PPL");
					
						selected_registration =new UserDetails().getRegNumberList().get(pos-1)
								.get("registration_num");
						
						
						selected_chassis=new UserDetails().getRegNumberList().get(pos-1)
								.get("chassis_num");
					
						
						Log.v("selected Reg",selected_registration);
						Log.v("selected Chassis",selected_chassis);
						
						Log.v("selected PPL", selected_PPL);
					//	clearView();	
						resetView();
						//getStates();
					
					}
			else
			{
				Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
			
				manual_est_vehicle_spinner.setSelection(0);
				selected_PPL=null;
				
			}		
			}
			 else
			 {
					selected_PPL=null;
			 }
		
	
	
}

		 
	
	}



	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
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





	
	@Override
	public void onResume() {
		super.onResume();
	
		/*if (manualEstimateAdapter!=null) {
			
			txtLabourSpareNote.setVisibility(View.INVISIBLE);
			
		}
		else if(spare_list!=null)
		{
			txtLabourSpareNote.setVisibility(View.INVISIBLE);
		}
		else
		{
			txtLabourSpareNote.setVisibility(View.VISIBLE);
		}
	*/
	
		Log.v("onResume ", "Called");
	
		if (manualEstimateAdapter!=null) {
			
			txtLabourSpareNote.setVisibility(View.INVISIBLE);
			
			Log.v("selected_labour_data", "called");
		
		}
		else if(manualEstimateAdapter!=null)
		{
			txtLabourSpareNote.setVisibility(View.INVISIBLE);
		
			Log.v("selected_spare_data", "called");
		}
		else
		{
			txtLabourSpareNote.setVisibility(View.VISIBLE);
		}
	
	}


	

	
	private void sendInvoiceEmail(View v)
	{
		
		final EditText mailBox;
		Button sendMail;
		Button dismissDialog;
		final Dialog dialog=new Dialog(getActivity());
		LayoutInflater inflater =(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view=inflater.inflate(R.layout.manual_est_pdf_email_dialog,null,false);
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);

		mailBox=(EditText)dialog.findViewById(R.id.email_invoice_box);
		mailBox.setText(UserDetails.getEmail_id());
		sendMail=(Button)dialog.findViewById(R.id.mail_invoice_btn);
		//dismissDialog=(ImageView)dialog.findViewById(R.id.dismissDialogBtn);
		
		dismissDialog=(Button)dialog.findViewById(R.id.mail_invoice_cancel_btn);
		dismissDialog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			dialog.dismiss();
				
			}
		});

		
		sendMail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				String email=mailBox.getText().toString().trim();
				
				if (email.length()==0) {
				
				mailBox.setError("Please enter email ID to send PDF report.");
					
				}
				else if(!new CheckValidations().validate(email))
				{
					
					mailBox.setError("Invalid Email ID.");
				}
				else
				{
					dialog.dismiss();
					sendMail(email);
				}
				
			}
		});
		
		dialog.show();
			
		
		
	}

	
	private void sendMail(String mailID)
	{
	
		
		Log.v("PDF response", ""+GeneratePDFResponse(mailID).toString());
		

		String response=GeneratePDFResponse(mailID);

		
		
		new SendEstimateReport(getActivity(), Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/sendMailForManualCostEstimate", response).execute();
		
		
	}
	
	
	
	private void caluclateCost()
	{
		
		 labourCost=0.0;
		 spareCost=0.0;
			
		
	ArrayList<LabourSpareModel> labourSpareModels=	GlobalAccessObject.getLabour_spare_obj();
		
		
	
		for (int i = 0; i < labourSpareModels.size(); i++) {
		
		
			if (labourSpareModels.get(i).getType().equalsIgnoreCase(Constants.TYPE_LABOUR)) {
				
			labourCost=	labourCost+Double.valueOf(labourSpareModels.get(i).getValue());
				
			
			
	//			long data=Double.longBitsToDouble(Long.parseLong(Double.valueOf(labourSpareModels.get(i).getValue())));
				
			//	Log.v("labour cost", ""+labourSpareModels.get(i).getValue());
				
		//		labourCost=labourCost+Double.valueOf(String.format("%.2f", labourSpareModels.get(i).getValue()));
				
				
				
			}
			else if(labourSpareModels.get(i).getType().equalsIgnoreCase(Constants.TYPE_SPARE))
			{
				
			spareCost=spareCost+Double.valueOf(labourSpareModels.get(i).getValue());
				
				
		}
		
		
		
	}
	
		
		
		
		
		Log.v("labour cost", ""+nf.format(labourCost));
		
		
	approx_labour_amt.setVisibility(View.VISIBLE);
	approx_spare_amt.setVisibility(View.VISIBLE);
	txtCalcalationNote.setVisibility(View.VISIBLE);
	approx_labour_amt.setText("Labour Amount (Approx) ="+nf.format(labourCost)+" INR*");

	approx_spare_amt.setText("Spares Amount (Approx) ="+nf.format(spareCost)+" INR*");	
		
		
		
	}
	
	

private String GeneratePDFResponse(String email)
{
	
	
	ArrayList<LabourSpareModel> PDFModel=labourSpareList;
	JSONObject object;
	
	JSONArray json_arr=new JSONArray();
	
	JSONObject outer_object=new JSONObject();
	
	JSONObject inner_object=new JSONObject();
	
	
	
	
	
	
	try {
	
		outer_object.put("user_id", UserDetails.getUser_id());
		outer_object.put("sessionId", UserDetails.getSeeionId());
		outer_object.put("reportedGeneratedAt", String.valueOf(new Date().getTime()));
		outer_object.put("emailId", email);
		outer_object.put("model", selected_PPL);
		outer_object.put("selState", selected_state);
		outer_object.put("selCity", selected_city);
		outer_object.put("resgistrationNum", selected_registration);
		outer_object.put("chassisNum", selected_chassis);
		outer_object.put("labourAmount", nf.format(labourCost));
		outer_object.put("sparesAmount", nf.format(spareCost));
		
	
		
	
		
		for (int i = 0; i < PDFModel.size(); i++) {

			object=new JSONObject();
			
			
			Log.v("UOM Value", PDFModel.get(i).getUom());
			
			object.put("billingHours", String.valueOf(PDFModel.get(i).getBillingHours()));
			object.put("description", String.valueOf(PDFModel.get(i).getDesc()));
			object.put("value", String.valueOf(String.format( "%.2f", PDFModel.get(i).getValue())));
			object.put("qty", String.valueOf(PDFModel.get(i).getQty()));
			object.put("type", String.valueOf(PDFModel.get(i).getType()));
			object.put("umrp", String.valueOf(PDFModel.get(i).getUmrp()));
			object.put("UOM", String.valueOf(PDFModel.get(i).getUom()));
		
		
			
			json_arr.put(object);
		
		}
		
		outer_object.put("selData", json_arr);

		
		inner_object.put("data", outer_object);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
		
	
	

return inner_object	.toString();

}
	


private void clearView()
{


	//selected_PPL=null;
	selected_registration=null;
	selected_chassis=null;
	//selected_city=null;
	//selected_state=null;
	
	
	if (manualEstimateAdapter!=null) {
		
		manualEstimateAdapter=null;
	}

	
	GlobalAccessObject.NullifyLabour_obj();
	GlobalAccessObject.NullifySpare_obj();
	GlobalAccessObject.setSpareModelLoaded(false);
	GlobalAccessObject.setLabourModelLoaded(false);
	GlobalAccessObject.Nullifylabour_spare_obj();
	

	if (labour_list!=null) {
		labour_list=null;		
	}

	
	if (spare_list!=null) {
		spare_list=null;	
	}
	
	
	if (selected_labour_data!=null) {
		selected_labour_data=null;	
	}
	
	/*if (selected_labour_spare_data!=null) {
		selected_labour_spare_data=null;	
	}*/

	if (selected_spare_data!=null) {

		selected_spare_data=null;
	}
	
	

	

	
	
	
	approx_labour_amt.setText("");
	approx_spare_amt.setText("");
	
	approx_labour_amt.setVisibility(View.INVISIBLE);
	approx_spare_amt.setVisibility(View.INVISIBLE);
/*	manual_est_calculate_btn.setVisibility(View.INVISIBLE);
	manual_est_email_btn.setVisibility(View.INVISIBLE);*/

	manual_est_selected_items_header.setVisibility(View.INVISIBLE);		
	selected_labour_spare_data.setVisibility(View.INVISIBLE);
	/*manual_est_select_labour.setVisibility(View.INVISIBLE);
	manual_est_select_spares.setVisibility(View.INVISIBLE);
	manual_est_reset_btn.setVisibility(View.INVISIBLE);*/
	header_view1.setVisibility(View.INVISIBLE);
	header_view2.setVisibility(View.INVISIBLE);
	txtCalcalationNote.setVisibility(View.INVISIBLE);
	txtLabourSpareNote.setVisibility(View.VISIBLE);






}


	
private void generateCostReport()
{
	


	 labourCost=0.0;
	 spareCost=0.0;
		
	
ArrayList<LabourSpareModel> labourSpareModels=	GlobalAccessObject.getLabour_spare_obj();
	
	
	for (int i = 0; i < labourSpareModels.size(); i++) {
	
	
		if (labourSpareModels.get(i).getType().equalsIgnoreCase(Constants.TYPE_LABOUR)) {
			
		labourCost=	labourCost+Double.valueOf(labourSpareModels.get(i).getValue());
			
			
		}
		else if(labourSpareModels.get(i).getType().equalsIgnoreCase(Constants.TYPE_SPARE))
		{
			
		spareCost=spareCost+Double.valueOf(labourSpareModels.get(i).getValue());
			
			
		}
	
	
	
}




}

}
