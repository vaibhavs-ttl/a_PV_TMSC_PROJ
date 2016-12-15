package com.ttl.customersocialapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.CustomAdapter;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.ComplaintAndJCDescripti;
import com.ttl.model.ServiceHandler;
import com.ttl.model.ServiceHistory;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class ServiceHistoyFragment extends ListFragment {

	private ArrayList<ServiceHistory> lst_history ;//= new ArrayList<ServiceHistory>();
	Spinner spinner_chassis;
	ArrayList<ComplaintAndJCDescripti>lst_complaintJC;
	
	ArrayList<ArrayList<ComplaintAndJCDescripti>> lst_lst_complaintJC = new ArrayList<ArrayList<ComplaintAndJCDescripti>>();
	
	CustomAdapter ad;
	List<String> chassisvalues = new ArrayList<String>();
	View v;
	
	Tracker mTracker;
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mTracker.setScreenName("ServiceHistoryScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 v = inflater.inflate(R.layout.fragment_servicehistory, container, false);
		spinner_chassis = (Spinner) v.findViewById(R.id.spinner_chassis);
		chassisvalues.add("Select Vehicle");//-Chassis Number
		int size = new UserDetails().getRegNumberList().size();
	       for(int i = 0 ; i<size; i++)
	       {
	    	   if(!(new UserDetails().getRegNumberList().get(i)
						.get("registration_num").toString().equals("")))
				{
				chassisvalues.add(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"));
				}else
				{
					chassisvalues.add(new UserDetails().getRegNumberList().get(i)
							.get("chassis_num"));
				}//+" - "+new UserDetails().getRegNumberList().get(i).get("chassis_num"));
	       }
	   	if(new UserDetails().getRegNumberList().size() == 0)
		{
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		}
		
	  //Tracker
		 AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
					mTracker = application.getDefaultTracker();
		/* ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, chassisvalues);
	       regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	       spinner_chassis.setAdapter(regno);*/
		ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinnertext, chassisvalues);
		sp_adapter.setDropDownViewResource(R.layout.spinner_selector);
		spinner_chassis.setAdapter(sp_adapter); 
	       
		spinner_chassis.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(new CheckConnectivity().checkNow(getActivity())){
				
				if(position!=0){
				/*String[] regchassis = spinner_chassis.getSelectedItem().toString().split("-");	
				String regNo = regchassis[0].trim();*/
			//	String chassis = regchassis[1].trim();
					String chassis = new UserDetails().getRegNumberList().get(position-1).get("chassis_num");
				String URL = getResources().getString(R.string.URL);
				String environment = "";
				
				if(URL.contains("qa"))
				{
					environment = "QA";
				}else
				{
					environment = "Production";
				}	
				/*String req1 = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/GetServiceHistoryByChassis_CSB";
				 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				    
				    
				   
				    nameValuePairs.add(new BasicNameValuePair("chassis_num",chassis));
				    nameValuePairs.add(new BasicNameValuePair("environment",environment));
				    nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
				    nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
				new AWS_WebServiceCall(getActivity(), req1, ServiceHandler.POST, Constants.GetServiceHistoryByChassis_CSB, new ResponseCallback() {
					
					@Override
					public void onResponseReceive(Object object) {
						// TODO Auto-generated method stub
						lst_history = (ArrayList<ServiceHistory>) object;
						if(lst_history.size()>0){
						CustomAdapter ad = new CustomAdapter(getActivity(), lst_history,getResources());
						getListView().setAdapter(ad); 
						}else
						{
							Toast.makeText(getActivity(), "Service history not available for this vehicle.", Toast.LENGTH_SHORT).show();
						}
						
						v.getRootView().setFocusable(true);
				  		v.getRootView().requestFocus();
					}
					
					@Override
					public void onErrorReceive(String string) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "Service history not available for this vehicle.", Toast.LENGTH_SHORT).show();
						v.getRootView().setFocusable(true);
				  		v.getRootView().requestFocus();
					}
				}).execute();*/
				
				String req1 = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/GetServiceHistoryByChassis_CSB";
				  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				  nameValuePairs.add(new BasicNameValuePair("chassis_num",chassis));
				
				  nameValuePairs.add(new BasicNameValuePair("environment",environment));
				
				  nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
				 
				  nameValuePairs.add(new BasicNameValuePair("user_id",
							UserDetails.getUser_id()));
				
				new AWS_WebServiceCall(getActivity(), req1, ServiceHandler.POST, Constants.GetServiceHistoryByChassis_CSB,nameValuePairs, new ResponseCallback() {
					
					@Override
					public void onResponseReceive(Object object) {
						// TODO Auto-generated method stub
						lst_history = (ArrayList<ServiceHistory>) object;
						if(lst_history.size()>0){
						CustomAdapter ad = new CustomAdapter(getActivity(), lst_history,getResources());
						getListView().setAdapter(ad); 
						}else
						{
							Toast.makeText(getActivity(), "Service history not available for this vehicle.", Toast.LENGTH_SHORT).show();
						}
						
						v.getRootView().setFocusable(true);
				  		v.getRootView().requestFocus();
					}
					
					@Override
					public void onErrorReceive(String string) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "Service history not available for this vehicle.", Toast.LENGTH_SHORT).show();
						v.getRootView().setFocusable(true);
				  		v.getRootView().requestFocus();
					}
				}).execute();
				
				/*String req =	"<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					  +"<SOAP:Body>"
					  +"<GetServiceHistoryByChassis_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
					      +"<RegistrationNumber></RegistrationNumber>" //"+regNo+"
					      +"<chassis_no>"+chassis+"</chassis_no>"
					    +"</GetServiceHistoryByChassis_CSB>"
					  +"</SOAP:Body>"
					+"</SOAP:Envelope>";
//					String req ="<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
//					  +"<SOAP:Body>"
//					  +"<GetServiceHistoryByChassis_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
//					      +"<chassis_no>"+spinner_chassis.getSelectedItem().toString()+"</chassis_no>"
//					    +"</GetServiceHistoryByChassis_CSB>"
//					  +"</SOAP:Body>"
//					+"</SOAP:Envelope>";	
					

						new WebServiceCall(getActivity(), req, Constants.GetServiceHistoryByChassis_CSB, new ResponseCallback() {
							
							@Override
							public void onResponseReceive(Object object) {
								// TODO Auto-generated method stub
								lst_history = (ArrayList<ServiceHistory>) object;
								Log.d("Total lst_history", lst_history.size()+"");
//								for ( int i = 0; i < lst_history.size(); i++) {
//                                getComplaintAndJC(lst_history.get(i).CHASSIS_NO,lst_history.get(i).SR_HISTORY_NUM);
//								}
							//	getListView().invalidateViews();
								//getListView().setEnabled(false);
							//	getListView().setOnScrollListener(null);
								
								if(lst_history.size()>0){
									
								String jobcard_id = "";	
								ArrayList<ServiceHistory> sortedListHist = new ArrayList<ServiceHistory>();
								for(int i=0; i<lst_history.size(); i++)
								{
									sortedListHist.add(lst_history.get(i));
									if(jobcard_id.equals(lst_history.get(i).getORDER_ID()))
									{
										if(lst_history.get(i).getINVOICE_STATUS().equals("New"))
										{
											sortedListHist.add(lst_history.get(i));
										}
									}else 
									{
										sortedListHist.add(lst_history.get(i));
									}
									jobcard_id = lst_history.get(i).getORDER_ID();
									if(sortedListHist.size()!=0)
									{
										for(int j= 0 ; j<sortedListHist.size(); j++)
									{
										if(!(lst_history.get(i).getORDER_ID().equals(sortedListHist.get(j).getORDER_ID())))
											{
												sortedListHist.add(lst_history.get(i));
											}
										else
										{
											if(sortedListHist.get(j).getINVOICE_STATUS().equals(""))
												{
													
												}
											if(lst_history.get(i).getINVOICE_STATUS().equals("New"))
												{
													sortedListHist.get(j).setINVC_AMT(lst_history.get(i).getINVC_AMT());
												}
										}
									}
								}else
								{
									sortedListHist.add(lst_history.get(i));
								}
								}
								CustomAdapter ad = new CustomAdapter(getActivity(), sortedListHist,getResources());
								getListView().setAdapter(ad); 
								}
								else
									Toast.makeText(getActivity(), "Service history not found for this vehicle.", Toast.LENGTH_SHORT).show();
								
								
							}
							
							

							@Override
							public void onErrorReceive(String string) {
								// TODO Auto-generated method stub
								Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
							}
						},"Getting Service History.").execute();*/
						
				
				
				
				
				
			}
				}	else {
					Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
//			private ArrayList<ArrayList<ComplaintAndJCDescripti>> getComplaintAndJC(String cHASSIS_NO, String sR_HISTORY_NUM) {
//				// TODO Auto-generated method stub
//				//for ( int i = 0; i < lst_history.size(); i++) {
//				//	Log.i("lst_history", lst_history.get(i).SERVICE_AT_DEALER + " " + lst_history.get(i).SR_TYPE);
//				
//					String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
//							+"<SOAP:Body>"
//							+"<GetComplaintAndJCDescripti_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
//					      +"<chassis_no>"+cHASSIS_NO+"</chassis_no>"
//					      +"<shrecordno>"+sR_HISTORY_NUM+"</shrecordno>"
//					    +"</GetComplaintAndJCDescripti_CSB>"
//					  +"</SOAP:Body>"
//					+"</SOAP:Envelope>";
//	             new WebServiceCall(getActivity(), req1, Constants.GetComplaintAndJCDescripti_CSB, new ResponseCallback() {
//
//						@Override
//						public void onResponseReceive(Object object) {
//							// TODO Auto-generated method stub
//							lst_complaintJC = (ArrayList<ComplaintAndJCDescripti>) object;
//							lst_lst_complaintJC.add(lst_complaintJC);
//							
//							if(lst_lst_complaintJC.size()==lst_history.size()){
//								for (int j = 0; j < lst_lst_complaintJC.size(); j++) {
//									for (int i = 0; i < lst_lst_complaintJC.get(j).size(); i++) {
//										Log.i("ITEMS", "AT " + j + " " + i+" "+ lst_lst_complaintJC.get(j).get(i).RECORD_TYPE+" "+lst_lst_complaintJC.get(j).get(i).C_J_P_DESCRTPTION+" "+lst_lst_complaintJC.get(j).get(i).C_J_P_STATUS+"");
//									}
//									
//								}
//							
//							 ad = new CustomAdapter(getActivity(), lst_history,lst_lst_complaintJC,getResources());
//							 if(getListView().getAdapter() == null){ //Adapter not set yet.
//							     setListAdapter(ad);
//							    }
//							    else{ //Already has an adapter
//							    ad.notifyDataSetChanged();
//							    }
//							 
//							
//							getListView().invalidateViews();
//						
//						//	ad.notifyDataSetInvalidated();
//							}
//						}
//
//						@Override 
//						public void onErrorReceive(String string) {
//							// TODO Auto-generated method stub
//							Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
//						}
//						
//					},"Getting JOB CARD Description..").execute();
//	             	
//	             return lst_lst_complaintJC;
//			}
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
						tx.replace(R.id.frame_container, new HomeFragment()).addToBackStack(null)
								.commit();
						return true;
					}
				}
				return false;
			}
		});
		
		return v;
	}
	
	}
