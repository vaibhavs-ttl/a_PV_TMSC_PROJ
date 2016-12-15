package com.ttl.customersocialapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.helper.VehicleRegister;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.model.VehcontactScheduler;
import com.ttl.model.VehicleAMC_ChildRow;
import com.ttl.model.VehicleAMC_ParentRow;
import com.ttl.model.VehicleAgreement_ChildRow;
import com.ttl.model.VehicleAgreement_ParentRow;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class VehicleDetails_Fragment extends Fragment {

	private LinearLayout ll1, ll2, ll3, ll4, ll5, ll6;
	private RelativeLayout rltl1, rltl2, rltl3, rltl4, rltl5, rltl6;
	private ImageView img_arrow, img_arrow1, img_arrow2, img_arrow3,
			img_arrow4, img_arrow5 , alarm;
	// private InstantAutoComplete spinner_chassisno;
	private Spinner spinner_chassis;
	private VehcontactScheduler vehdetails;
	private TextView text_chassis, text_engine, text_fuel, text_model,
			text_varient, text_color, text_saledate, text_wend_date,
			text_lastservicedate, text_lastserviceKM, text_lastservicedealer,
			text_dealer, text_dealercode, text_company, text_policy,
			text_policy_sdate, text_policy_edate, text_ew_policyNumber,
			text_war_sdate, text_war_edate, ew_policyNumber, text_addr,
			text_phone, text_fname, text_war_sKM, text_war_eKM, text_lname , text_nextservicedate;
	// private TextView err_veh, err_ins, err_warr, err_cust;

	List<String> chassisvalues = new ArrayList<String>();
	private ImageView img_vehicle;
	private Bitmap veh_bitmap = null;
	ExpandableListView agreementlist, amclist;
	TextView add_another_vehicle;
	// private ArrayList<VehicleAgreement_ParentRow> parents;
	// private ArrayList<VehicleAMC_ParentRow> amcparents;
	private ArrayList<VehicleAMC_ParentRow> list_amcParent = new ArrayList<VehicleAMC_ParentRow>();
	private ArrayList<VehicleAgreement_ParentRow> lst_agreement = new ArrayList<VehicleAgreement_ParentRow>();
	ArrayList<VehicleAgreement_ParentRow> array1 = new ArrayList<VehicleAgreement_ParentRow>();
	Dialog proceeddialog;
	Bundle bundle;
	ArrayList<VehicleAMC_ParentRow> lst_amcParentSorted = new ArrayList<VehicleAMC_ParentRow>();

	Tracker mTracker;
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mTracker.setScreenName("VehicleDetailsScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_vehicle_details, container,
				false);
		ll1 = (LinearLayout) v.findViewById(R.id.ll1);
		ll2 = (LinearLayout) v.findViewById(R.id.ll2);
		ll3 = (LinearLayout) v.findViewById(R.id.ll3);
		ll4 = (LinearLayout) v.findViewById(R.id.ll4);
		ll5 = (LinearLayout) v.findViewById(R.id.ll5);
		ll6 = (LinearLayout) v.findViewById(R.id.ll6);
		rltl1 = (RelativeLayout) v.findViewById(R.id.rltl1);
		rltl2 = (RelativeLayout) v.findViewById(R.id.rltl2);
		rltl3 = (RelativeLayout) v.findViewById(R.id.rltl3);
		rltl4 = (RelativeLayout) v.findViewById(R.id.rltl4);
		rltl5 = (RelativeLayout) v.findViewById(R.id.rltl5);
		rltl6 = (RelativeLayout) v.findViewById(R.id.rltl6);
		img_arrow = (ImageView) v.findViewById(R.id.img_arrow);
		img_arrow1 = (ImageView) v.findViewById(R.id.img_arrow1);
		img_arrow2 = (ImageView) v.findViewById(R.id.img_arrow2);
		img_arrow3 = (ImageView) v.findViewById(R.id.img_arrow3);
		img_arrow4 = (ImageView) v.findViewById(R.id.img_arrow4);
		img_arrow5 = (ImageView) v.findViewById(R.id.img_arrow5);
		// spinner_chassisno = (InstantAutoComplete)
		// v.findViewById(R.id.spinner_chassisno);
		spinner_chassis = (Spinner) v.findViewById(R.id.spinner_chassisNo);
		if (new UserDetails().getRegNumberList().size() == 0) {
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		}
		 //Tracker
		 AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
					mTracker = application.getDefaultTracker();
					
		text_chassis = (TextView) v.findViewById(R.id.text_chassis);
		text_engine = (TextView) v.findViewById(R.id.text_engine);
		text_fuel = (TextView) v.findViewById(R.id.text_fuel);
		text_model = (TextView) v.findViewById(R.id.text_model);
		text_varient = (TextView) v.findViewById(R.id.text_varient);
		text_color = (TextView) v.findViewById(R.id.text_color);
		text_saledate = (TextView) v.findViewById(R.id.text_saledate);
		text_wend_date = (TextView) v.findViewById(R.id.text_wend_date);
		text_lastservicedate = (TextView) v
				.findViewById(R.id.text_lastservicedate);
		text_nextservicedate = (TextView) v.findViewById(R.id.text_nextservicedate);
		alarm = (ImageView) v.findViewById(R.id.alarm);
		text_lastserviceKM = (TextView) v.findViewById(R.id.text_lastserviceKM);
		text_lastservicedealer = (TextView) v
				.findViewById(R.id.text_lastservicedealer);
		text_dealer = (TextView) v.findViewById(R.id.text_dealer);
		text_dealercode = (TextView) v.findViewById(R.id.text_dealercode);
		text_company = (TextView) v.findViewById(R.id.text_company);
		text_policy = (TextView) v.findViewById(R.id.text_policy);
		text_policy_sdate = (TextView) v.findViewById(R.id.text_policy_sdate);
		text_policy_edate = (TextView) v.findViewById(R.id.text_policy_edate);
		text_ew_policyNumber = (TextView) v
				.findViewById(R.id.text_ew_policyNumber);
		text_war_sdate = (TextView) v.findViewById(R.id.text_war_sdate);
		text_war_edate = (TextView) v.findViewById(R.id.text_war_edate);
		text_war_sKM = (TextView) v.findViewById(R.id.text_war_sKM);
		text_war_eKM = (TextView) v.findViewById(R.id.text_war_eKM);
		ew_policyNumber = (TextView) v.findViewById(R.id.ew_policyNumber);

		text_addr = (TextView) v.findViewById(R.id.text_address);
		text_phone = (TextView) v.findViewById(R.id.text_phone);
		text_fname = (TextView) v.findViewById(R.id.text_fname);
		text_lname = (TextView) v.findViewById(R.id.text_lname);
		img_vehicle = (ImageView) v.findViewById(R.id.img_vehicle);
		// err_veh = (TextView) v.findViewById(R.id.err_veh);
		// err_ins = (TextView) v.findViewById(R.id.err_ins);
		// err_warr = (TextView) v.findViewById(R.id.err_warr);
		// err_cust = (TextView) v.findViewById(R.id.err_cust);
		add_another_vehicle = (TextView) v.findViewById(R.id.addvehicle);
		add_another_vehicle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				proceeddialog = new Dialog(getActivity());
				proceeddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				proceeddialog.setContentView(R.layout.add_vehicle_reg_popup);
				proceeddialog.setCancelable(false);
				proceeddialog.show();
				Button proceed = (Button) proceeddialog
						.findViewById(R.id.btnproceed);
				Button notnow = (Button) proceeddialog
						.findViewById(R.id.btnnotnow);
				proceed.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						proceeddialog.dismiss();
						CheckConnectivity checknow = new CheckConnectivity();
						boolean connect = checknow
								.checkNow(getActivity());
						if (connect) {
						new VehicleRegister(getActivity(), "Vehicle");
						}
						 else {
							Toast toast = Toast.makeText(getActivity(),
									"No network connection available.",
									Toast.LENGTH_SHORT);
							toast.show();
						}

					}
				});
				notnow.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						proceeddialog.dismiss();
						HomeFragment.regvehicle = false;
					}
				});

			}
		});
		
		

		agreementlist = (ExpandableListView) v.findViewById(R.id.agreementlist);
		amclist = (ExpandableListView) v.findViewById(R.id.amclist);
		agreementlist
				.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						setListViewHeight(parent, groupPosition);
						return false;
					}
				});

		amclist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				setAMCListViewHeight(parent, groupPosition);
				return false;
			}
		});
		alarm.setVisibility(View.GONE);
		// MAT611464BPN97534
		// 357151BVZ804302
		chassisvalues.add("Select Vehicle");
		int size = new UserDetails().getRegNumberList().size();
		for (int i = 0; i < size; i++) {
			// chassisvalues.add(new UserDetails().getRegNumberList().get(i)
			// .get("chassis_num"));
			// registration_num
			if(!(new UserDetails().getRegNumberList().get(i)
					.get("registration_num").toString().equals("")))
			{
			chassisvalues.add(new UserDetails().getRegNumberList().get(i)
					.get("registration_num"));
			}else
			{
				chassisvalues.add(new UserDetails().getRegNumberList().get(i)
						.get("chassis_num"));
			}

		}
		ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, chassisvalues);
		regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_chassis.setAdapter(regno);

		spinner_chassis
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						lst_amcParentSorted = new ArrayList<VehicleAMC_ParentRow>();
						array1 = new ArrayList<VehicleAgreement_ParentRow>();
						amcloadHosts(lst_amcParentSorted);
						loadHosts(array1);
						if (position != 0) {
							String chassis = new UserDetails()
									.getRegNumberList().get(position - 1)
									.get("chassis_num");

							resetPreviousData();
							/*
							 * String req =
							 * "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							 * + "<SOAP:Body>" +
							 * "<GetVechcontactScheduler_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
							 * + "<chassis_no>"+chassis+"</chassis_no>" +
							 * "</GetVechcontactScheduler_CSB>" + "</SOAP:Body>"
							 * + "</SOAP:Envelope>";
							 */
							
							
							
							rltl1.setVisibility(View.VISIBLE);
							rltl2.setVisibility(View.VISIBLE);
							rltl3.setVisibility(View.VISIBLE);
							rltl4.setVisibility(View.VISIBLE);
							rltl5.setVisibility(View.VISIBLE);
							rltl6.setVisibility(View.VISIBLE);
							
							String[] date;
							text_chassis.setText(chassis);
							text_engine.setText(new UserDetails().getRegNumberList().get(position - 1).get("engine_num"));
							text_fuel.setText(new UserDetails().getRegNumberList().get(position - 1).get("fuel_type"));
							text_model.setText(new UserDetails().getRegNumberList().get(position - 1).get("PPL"));

							text_varient.setText(new UserDetails().getRegNumberList().get(position - 1).get("pl"));
							text_color.setText(new UserDetails().getRegNumberList().get(position - 1).get("color"));
							if (!TextUtils.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("sale_date"))) {
								 date = new UserDetails().getRegNumberList().get(position - 1).get("sale_date").split("T");
								text_saledate.setText(date[0]);
							}

							if (!TextUtils.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("warranty_end_date"))) {
								String[] date1 = new UserDetails().getRegNumberList().get(position - 1).get("warranty_end_date")
										.split("T");
								text_wend_date.setText(date1[0]);
							}

							if (!TextUtils.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("last_service_date"))) {
								date = new UserDetails().getRegNumberList().get(position - 1).get("last_service_date").split("T");
								text_lastservicedate.setText(date[0]);
							}

							if (!TextUtils.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("next_service_due_date"))) {
								date = new UserDetails().getRegNumberList().get(position - 1).get("next_service_due_date").split("T");
								text_nextservicedate.setText(date[0]);
								 SimpleDateFormat currentformat = new SimpleDateFormat("yyyy-MM-dd");
								 SimpleDateFormat required = new SimpleDateFormat("dd-MMM-yyyy");
								 Date dateshow = new Date();
								 String reminddate = "";
								 Calendar cal = Calendar.getInstance();
								 try {
									 dateshow = currentformat.parse(date[0]);
									 
									reminddate = required.format(dateshow);
								} catch (Exception e) {
									// TODO: handle exception
								}
								 
								 
								bundle = new Bundle();
								if(!(new UserDetails()
									.getRegNumberList().get(position - 1)
									.get("registration_num").equals("NA")))
								{
								bundle.putString("remindregNo", new UserDetails()
									.getRegNumberList().get(position - 1)
									.get("registration_num"));
								}else
								{
									bundle.putString("remindregNo", new UserDetails()
									.getRegNumberList().get(position - 1)
									.get("chassis_num"));
								}
								bundle.putString("remindDate", reminddate);
							//	bundle.putString("remindTime", date[1]);
								bundle.putString("remindtype", "Next Service Date");
								bundle.putString("Fragment",
					                    "VehicleDetails_Fragment");
								alarm.setVisibility(View.VISIBLE);
								alarm.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										FragmentManager fragmentManager = getActivity()
												.getFragmentManager();
										Fragment fragment = new Reminder_Fragment();
										fragment.setArguments(bundle);

										fragmentManager.beginTransaction()
												.replace(R.id.frame_container, fragment)
												.commit();
									
									}
								});
							}

							text_nextservicedate.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									
									FragmentManager fragmentManager = getActivity()
											.getFragmentManager();
									Fragment fragment = new Reminder_Fragment();
									fragment.setArguments(bundle);

									fragmentManager.beginTransaction()
											.replace(R.id.frame_container, fragment)
											.commit();
								}
							} );
							text_lastserviceKM
									.setText(new UserDetails().getRegNumberList().get(position - 1).get("last_service_kms"));
							text_lastservicedealer
									.setText(new UserDetails().getRegNumberList().get(position - 1).get("last_service_dealer"));
							text_dealer.setText(new UserDetails().getRegNumberList().get(position - 1).get("selling_dealer"));
							text_dealercode
									.setText(new UserDetails().getRegNumberList().get(position - 1).get("selling_dealer_code"));
							text_company
									.setText(new UserDetails().getRegNumberList().get(position - 1).get("insurance_company_name"));
							text_policy.setText(new UserDetails().getRegNumberList().get(position - 1).get("policy_num"));
							if (!TextUtils.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("policy_start_date"))) {
								date = new UserDetails().getRegNumberList().get(position - 1).get("policy_start_date").split("T");
								text_policy_sdate.setText(date[0]);
							}
							if (!TextUtils.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("policy_end_date"))) {
								date = new UserDetails().getRegNumberList().get(position - 1).get("policy_end_date").split("T");
								text_policy_edate.setText(date[0]);
								
							}
							
							if (new UserDetails().getRegNumberList().get(position - 1).get("extended_warranty_flag").equals("Y")) {
								text_ew_policyNumber
										.setText(new UserDetails().getRegNumberList().get(position - 1).get("ew_policy_num"));
								if (!TextUtils
										.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("ew_start_date"))) {
									date = new UserDetails().getRegNumberList().get(position - 1).get("ew_start_date")
											.split("T");
									text_war_sdate.setText(date[0]);
								}
								if (!TextUtils
										.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("ew_end_date"))) {
									String[] date1 = new UserDetails().getRegNumberList().get(position - 1).get("ew_end_date")
											.split("T");
									text_war_edate.setText(date1[0]);
								}

								text_war_sKM.setText(new UserDetails().getRegNumberList().get(position - 1).get("ew_start_km"));
								text_war_eKM.setText(new UserDetails().getRegNumberList().get(position - 1).get("ew_end_km"));

							}

							text_fname.setText(new UserDetails().getRegNumberList().get(position - 1).get("contact_fisrt_name"));
							text_lname.setText(new UserDetails().getRegNumberList().get(position - 1).get("contact_last_name"));
							text_addr.setText(new UserDetails().getRegNumberList().get(position - 1).get("contact_address_line1"));
							text_phone.setText(new UserDetails().getRegNumberList().get(position - 1).get("contact_cell_phone_num"));
							

					/*		String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
									+ "<SOAP:Body>"
									+ "<GetAMCDetailsScheduler_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
									+ "<chassis_no>"
										+ chassis
									+ "</chassis_no>"
									+ "</GetAMCDetailsScheduler_CSB>"
									+ "</SOAP:Body>" + "</SOAP:Envelope>";
							
							String req2 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
									+ "<SOAP:Body>"
									+ "<GetAgreementScheduler_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
									+ "<chassis_no>"
										+ chassis
									+ "</chassis_no>"
									+ "</GetAgreementScheduler_CSB>"
									+ "</SOAP:Body>" + "</SOAP:Envelope>";*/

						
						/*	new WebServiceCall(getActivity(), req1,
									Constants.GetAMCDetailsScheduler_CSB,
									new ResponseCallback() {

										@Override
										public void onResponseReceive(
												Object object) {
											// TODO Auto-generated method stub
											 * 
*/							//UserDetails.getLst_agreeParent().get(position-1);				
											//Log.d("getLst_amcParent", UserDetails.getLst_agreeParent().get(position-1));
											/*if(new UserDetails().getLst_amcParent()!= null)
											{*/
											list_amcParent = new UserDetails().getAllDataAMC().get(position-1).get("AMC");
											
											for (int i = 0; i < list_amcParent
													.size(); i++) {
												if (!TextUtils
														.isEmpty(list_amcParent
																.get(i)
																.getAmc_type())
														&& list_amcParent
																.get(i)
																.getAmc_no()
																.startsWith("8")) {
													lst_amcParentSorted
															.add(list_amcParent
																	.get(i));
													
												}

											}

											amclist.setOnGroupExpandListener(new OnGroupExpandListener() {
												int previousGroup = -1;

												@Override
												public void onGroupExpand(
														int groupPosition) {
													if (groupPosition != previousGroup)
														amclist.collapseGroup(previousGroup);
													previousGroup = groupPosition;

												}
											});

											if (lst_amcParentSorted.size() > 0) {
												ArrayList<VehicleAMC_ParentRow> list_amcData = buildAMCData(lst_amcParentSorted);
												amcloadHosts(list_amcData);
												//Log.d("Here AMC", list_amcData.size()+"");
											} else {
												ArrayList<VehicleAMC_ParentRow> list_amcData = buildAMCData(lst_amcParentSorted);
												amcloadHosts(null);
												//rltl4.setVisibility(View.GONE);
												//Log.d("nodata AMC", list_amcData.size()+"");
												}
											/*}else
											{
												rltl4.setVisibility(View.GONE);

											}*/
										/*	// ArrayList<VehicleAMC_ParentRow>
											// list_amcData =
											// buildAMCData(list_amcParent);
											// amcloadHosts(list_amcData);

										}

										@Override
										public void onErrorReceive(String string) {
											// TODO Auto-generated method stub
											rltl4.setVisibility(View.GONE);
											// Toast.makeText(getActivity(),
											// "Could not get AMC Details.",
											// Toast.LENGTH_SHORT).show();
											amcloadHosts(null);
										}
									}, "Getting AMC Details.").execute();*/

						/*	new WebServiceCall(getActivity(), req2,
									Constants.GetAgreementScheduler_CSB,
									new ResponseCallback() {

										@Override
										public void onResponseReceive(
												Object object) {
											// TODO Auto-generated method stub
*/											
										/*if(new UserDetails().getLst_agreeParent()!= null)
										{
											*///lst_agreement = new UserDetails().getLst_agreeParent();
											lst_agreement = new UserDetails().getAllDataagreement().get(position-1).get("Agreement");
											for (int i = 0; i < lst_agreement
													.size(); i++) {
												Log.i("GetAgreementScheduler_CSB",
														lst_agreement.get(i).agreement_no);
												if (lst_agreement.get(i).agree_name
														.contains("On Road Assistance")) {
													lst_agreement.get(i).agree_name = lst_agreement.get(i).agree_name.replaceAll("–", "-");
													
													array1.add(lst_agreement
															.get(i));
												}
											}

											agreementlist
													.setOnGroupExpandListener(new OnGroupExpandListener() {
														int previousGroup = -1;

														@Override
														public void onGroupExpand(
																int groupPosition) {
															if (groupPosition != previousGroup)
																agreementlist
																		.collapseGroup(previousGroup);
															previousGroup = groupPosition;

														}
													});
											if(array1.size()>0)
											{
											ArrayList<VehicleAgreement_ParentRow> dummyList = buildAggrData(array1);

											loadHosts(dummyList);
											//Log.d("Here Aggrement", dummyList.size()+"");
											}else
											{
												ArrayList<VehicleAgreement_ParentRow> dummyList = buildAggrData(array1);

												loadHosts(null);
												//Log.d("No data Aggrement", dummyList.size()+"");
											}
										/*}else
										{
											ArrayList<VehicleAgreement_ParentRow> dummyList = buildAggrData(array1);

											// Adding ArrayList data to
											// ExpandableListView values
											loadHosts(dummyList);
											Log.d("Here", dummyList.size()+"");
										//	rltl5.setVisibility(View.GONE);
										}*/

									/*	}

										@Override
										public void onErrorReceive(String string) {
											// TODO Auto-generated method stub
											rltl5.setVisibility(View.GONE);
											// Toast.makeText(getActivity(),
											// "Could not get Agreement Details.",
											// Toast.LENGTH_SHORT).show();
											loadHosts(null);
										}
									}, "Getting Agreement Details.").execute();*/
											img_vehicle.setImageBitmap(null);
											if (!TextUtils
													.isEmpty(new UserDetails().getRegNumberList().get(position - 1).get("PPL")))
												{
												getVehicleImage(new UserDetails().getRegNumberList().get(position - 1).get("PPL"));
												}
											
											// SET VEHICLE IMAGE
												String vehicle_image_url = new UserDetails().getRegNumberList().get(position - 1).get("vehicle_image_url");
												/* vehicle_image_url= vehicle_image_url.replaceAll(" ", "%20");
												//	new DownloadImageTask(img_vehicle).execute(vehicle_image_url);

											       Log.i("IMAGEURL", vehicle_image_url);
											          InputStream input;
													try {
														input = new java.net.URL(vehicle_image_url).openStream();
														veh_bitmap = BitmapFactory.decodeStream(input);
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
											      //  Decode Bitmap
											          
											     	img_vehicle.setImageBitmap(veh_bitmap);*/
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}

				});

		ll1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle_contents(rltl1);
			}
		});
		ll2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle_contents(rltl2);
			}
		});
		ll3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle_contents(rltl3);
			}
		});
		ll4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle_contents(rltl4);
			}
		});
		ll5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle_contents(rltl5);
			}
		});
		ll6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle_contents(rltl6);
			}
		});

		v.getRootView().setFocusableInTouchMode(true);
		v.getRootView().setFocusable(true);
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

		// Agreement

		return v;
	}

	private void loadHosts(
			final ArrayList<VehicleAgreement_ParentRow> newParents) {
		if (newParents == null)
			
			return;

		// parents = newParents;

		// Check for ExpandableListAdapter object

		// Create ExpandableListAdapter Object
		final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter(
				newParents);

		// Set Adapter to ExpandableList Adapter
		agreementlist.setAdapter(mAdapter);
		
		mAdapter.notifyDataSetChanged();
	}

	private void getVehicleImage(String pPL) {
		// TODO Auto-generated method stub
		CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow
				.checkNow(getActivity());
		if (connect) {
		String urlString = pPL;
		urlString = urlString.replaceAll(" ", "%20");
		String url = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/getVehicleImageByPPL"
				;
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				3);
		 nameValuePairs.add(new BasicNameValuePair("PPL",
				 urlString));
		nameValuePairs.add(new BasicNameValuePair("sessionId",
				UserDetails.getSeeionId()));
		nameValuePairs.add(new BasicNameValuePair("user_id",
				UserDetails.getUser_id()));
		// String url =
		// Config.awsserverurl+"CustomerApp_Restws/customerapp/vehicleServices/getVehicleImageByPPL/Indica%20Vista";
		Log.i("getVehicleImage_url", url);
		// new DownloadImage().execute(url);
		//img_vehicle.setImageResource(android.R.color.transparent);
		new AWS_WebServiceCall(getActivity(), url, ServiceHandler.POST,
				Constants.getVehicleImageByPPL,nameValuePairs, new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						// TODO Auto-generated method stub
						
						veh_bitmap = (Bitmap) object;
						img_vehicle.setImageBitmap(veh_bitmap);
					}

					@Override
					public void onErrorReceive(String string) {
						// TODO Auto-generated method stub
						/*Toast.makeText(getActivity(), "Could not load image.",
								Toast.LENGTH_SHORT).show();*/
						veh_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultcar);
						img_vehicle.setImageBitmap(veh_bitmap);
						//img_vehicle.setBackgroundResource(R.drawable.defaultcar);
					}
				}).execute();
		}
		 else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	private ArrayList<VehicleAgreement_ParentRow> buildAggrData(
			ArrayList<VehicleAgreement_ParentRow> lst_agreement2) {
		// Creating ArrayList of type parent class to store parent class objects
		final ArrayList<VehicleAgreement_ParentRow> list = new ArrayList<VehicleAgreement_ParentRow>();

		for (int i = 0; i < lst_agreement2.size(); i++) {
			// Create parent class object
			final VehicleAgreement_ParentRow parent = new VehicleAgreement_ParentRow();

			parent.setAgreement_no(lst_agreement2.get(i).getAgreement_no());

			parent.setChildren(new ArrayList<VehicleAgreement_ChildRow>());

			// Create Child class object
			final VehicleAgreement_ChildRow child = new VehicleAgreement_ChildRow();
			child.setAgree_name(lst_agreement2.get(i).getAgree_name());
			child.setStatus(lst_agreement2.get(i).getStatus());
			child.setAgreement_amt(lst_agreement2.get(i).getAgreement_amt());
			child.setMech_reamaing(lst_agreement2.get(i).getMech_reamaing());
			child.setTowing_reamaing(lst_agreement2.get(i).getTowing_reamaing());
			child.setMech_avail(lst_agreement2.get(i).getMech_avail());
			child.setTowing_avail(lst_agreement2.get(i).getTowing_avail());

			// Add Child class object to parent class object
			parent.getChildren().add(child);
			// }
			list.add(parent);
		}
		return list;
	}

	private class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private LayoutInflater inflater;
		private ArrayList<VehicleAgreement_ParentRow> parents;

		public MyExpandableListAdapter(
				ArrayList<VehicleAgreement_ParentRow> newParents) {
			// Create Layout Inflater
			inflater = LayoutInflater.from(getActivity());
			this.parents = newParents;
		}

		// This Function used to inflate parent rows view

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parentView) {
			final VehicleAgreement_ParentRow parent = parents
					.get(groupPosition);

			// Inflate grouprow.xml file for parent rows
			convertView = inflater.inflate(
					R.layout.vehicledetails_agreement_parent_row, parentView,
					false);

			// Get grouprow.xml file elements and set values

			((TextView) convertView.findViewById(R.id.txtagreementno))
					.setText(parent.getAgreement_no());

			if (isExpanded) {
				ImageView img = (ImageView) convertView.findViewById(R.id.img);
				img.setBackgroundResource(R.drawable.ddownarrow);
			}

			return convertView;
		}

		// This Function used to inflate child rows view
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parentView) {
			final VehicleAgreement_ParentRow parent = parents
					.get(groupPosition);
			final VehicleAgreement_ChildRow child = parent.getChildren().get(
					childPosition);

			// Inflate childrow.xml file for child rows
			convertView = inflater.inflate(
					R.layout.vehicledetails_agreement_child_row, parentView,
					false);

			// Get childrow.xml file elements and set values
			((TextView) convertView.findViewById(R.id.txtdec_agreename))
					.setText(child.getAgree_name());
			((TextView) convertView.findViewById(R.id.txtdec_status))
					.setText(child.getStatus());
			((TextView) convertView.findViewById(R.id.txtdec_agreeamt))
					.setText(child.getAgreement_amt());
			((TextView) convertView.findViewById(R.id.txtdec_mechremaing))
					.setText(child.getMech_reamaing());
			((TextView) convertView.findViewById(R.id.txtdec_towingremaing))
					.setText(child.getTowing_reamaing());
			((TextView) convertView.findViewById(R.id.txtdec_mechaval))
					.setText(child.getMech_avail());
			((TextView) convertView.findViewById(R.id.txtdec_towingaval))
					.setText(child.getTowing_avail());
			return convertView;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
			return parents.get(groupPosition).getChildren().get(childPosition);
		}

		// Call when child row clicked
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			/****** When Child row clicked then this function call *******/

			/*
			 * if( ChildClickStatus!=childPosition) { ChildClickStatus =
			 * childPosition;
			 * 
			 * Toast.makeText(getApplicationContext(), "Parent :"+groupPosition
			 * + " Child :"+childPosition , Toast.LENGTH_LONG).show(); }
			 */

			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			int size = 0;
			if (parents.get(groupPosition).getChildren() != null)
				size = parents.get(groupPosition).getChildren().size();
			return size;
		}

		@Override
		public Object getGroup(int groupPosition) {
			Log.i("Parent", groupPosition + "=  getGroup ");

			return parents.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return parents.size();
		}

		// Call when parent row clicked
		@Override
		public long getGroupId(int groupPosition) {
			/*
			 * if(groupPosition==2 && ParentClickStatus!=groupPosition){
			 * 
			 * //Alert to user Toast.makeText(getApplicationContext(),
			 * "Parent :"+groupPosition , Toast.LENGTH_LONG).show(); }
			 * 
			 * ParentClickStatus=groupPosition; if(ParentClickStatus==0)
			 * ParentClickStatus=-1;
			 */

			return groupPosition;

		}

		@Override
		public void notifyDataSetChanged() {
			// Refresh List rows
			super.notifyDataSetChanged();
		}

		@Override
		public boolean isEmpty() {
			return ((parents == null) || parents.isEmpty());
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

	}

	// AMC
	private void amcloadHosts(final ArrayList<VehicleAMC_ParentRow> newParents) {
		if (newParents == null)
			return;

		// Create ExpandableListAdapter Object
		final AMCMyExpandableListAdapter mAdapter = new AMCMyExpandableListAdapter(
				newParents);

		// Set Adapter to ExpandableList Adapter
		amclist.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private ArrayList<VehicleAMC_ParentRow> buildAMCData(
			ArrayList<VehicleAMC_ParentRow> list_amcParent2) {
		// Creating ArrayList of type parent class to store parent class objects
		final ArrayList<VehicleAMC_ParentRow> list = new ArrayList<VehicleAMC_ParentRow>();

		for (int i = 0; i < list_amcParent2.size(); i++) {
			// Create parent class object
			final VehicleAMC_ParentRow parent = new VehicleAMC_ParentRow();

			parent.setAmc_no(list_amcParent2.get(i).getAmc_no());

			Log.i("amcbuildDummyData", list_amcParent2.get(i).getAmc_no());

			parent.setChildren(new ArrayList<VehicleAMC_ChildRow>());

			// Create Child class object
			VehicleAMC_ChildRow child = new VehicleAMC_ChildRow();
			child.setAmc_type(list_amcParent2.get(i).getAmc_type());
			child.setStart_date(list_amcParent2.get(i).getStart_date());
			child.setEnd_date(list_amcParent2.get(i).getEnd_date());
			/*
			 * if(!TextUtils.isEmpty(list_amcParent2.get(i).getStart_date()))
			 * child
			 * .setStart_date(list_amcParent2.get(i).getStart_date().split("T"
			 * )[0]);
			 * 
			 * if(!TextUtils.isEmpty(list_amcParent2.get(i).getEnd_date()))
			 * child
			 * .setStart_date(list_amcParent2.get(i).getEnd_date().split("T"
			 * )[0]);
			 */

			child.setStart_km(list_amcParent2.get(i).getStart_km());
			child.setEnd_km(list_amcParent2.get(i).getEnd_km());
			child.setDescription(list_amcParent2.get(i).getDescription());
			child.setStatus(list_amcParent2.get(i).getAmc_status());
			// Add Child class object to parent class object
			parent.getChildren().add(child);
			// }
			list.add(parent);

		}
		return list;
	}

	private class AMCMyExpandableListAdapter extends BaseExpandableListAdapter {

		private LayoutInflater inflater = LayoutInflater.from(getActivity());
		ArrayList<VehicleAMC_ParentRow> lst_AMC = new ArrayList<VehicleAMC_ParentRow>();

		// This Function used to inflate parent rows view

		public AMCMyExpandableListAdapter(ArrayList<VehicleAMC_ParentRow> list) {
			// TODO Auto-generated constructor stub
			lst_AMC = list;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parentView) {
			final VehicleAMC_ParentRow parent = lst_AMC.get(groupPosition);

			// Inflate grouprow.xml file for parent rows

			Log.i("getGroupView", parent.getAmc_no());

			convertView = inflater.inflate(
					R.layout.vehicledetails_amc_parent_row, parentView, false);

			// Get grouprow.xml file elements and set values

			((TextView) convertView.findViewById(R.id.txtamcno)).setText(parent
					.getAmc_no());

			if (isExpanded) {
				ImageView img = (ImageView) convertView
						.findViewById(R.id.amcimg);
				img.setBackgroundResource(R.drawable.ddownarrow);
			}

			return convertView;
		}

		// This Function used to inflate child rows view
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parentView) {
			final VehicleAMC_ParentRow parent = lst_AMC.get(groupPosition);
			final VehicleAMC_ChildRow child = parent.getChildren().get(
					childPosition);

			// Inflate childrow.xml file for child rows
			convertView = inflater.inflate(
					R.layout.vehicledetails_amc_child_row, parentView, false);

			// Get childrow.xml file elements and set values
			((TextView) convertView.findViewById(R.id.txtdec_amctype))
					.setText(child.getAmc_type());
			((TextView) convertView.findViewById(R.id.txtdec_amcstartdate))
					.setText(child.getStart_date());
			((TextView) convertView.findViewById(R.id.txtdec_amcendstart))
					.setText(child.getEnd_date());
			((TextView) convertView.findViewById(R.id.txtdec_amcstartkm))
					.setText(child.getStart_km());
			((TextView) convertView.findViewById(R.id.txtdec_amcendkm))
					.setText(child.getEnd_km());
			((TextView) convertView.findViewById(R.id.txtdec_amcdesc))
					.setText(child.getDescription());
			((TextView) convertView.findViewById(R.id.txtdec_amcstatus))
					.setText(child.getStatus());
			return convertView;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
			return lst_AMC.get(groupPosition).getChildren().get(childPosition);
		}

		// Call when child row clicked
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			/****** When Child row clicked then this function call *******/

			/*
			 * if( ChildClickStatus!=childPosition) { ChildClickStatus =
			 * childPosition;
			 * 
			 * Toast.makeText(getApplicationContext(), "Parent :"+groupPosition
			 * + " Child :"+childPosition , Toast.LENGTH_LONG).show(); }
			 */

			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			int size = 0;
			if (lst_AMC.get(groupPosition).getChildren() != null)
				size = lst_AMC.get(groupPosition).getChildren().size();
			return size;
		}

		@Override
		public Object getGroup(int groupPosition) {
			Log.i("Parent", groupPosition + "=  getGroup ");

			return lst_AMC.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return lst_AMC.size();
		}

		// Call when parent row clicked
		@Override
		public long getGroupId(int groupPosition) {
			/*
			 * if(groupPosition==2 && ParentClickStatus!=groupPosition){
			 * 
			 * //Alert to user Toast.makeText(getApplicationContext(),
			 * "Parent :"+groupPosition , Toast.LENGTH_LONG).show(); }
			 * 
			 * ParentClickStatus=groupPosition; if(ParentClickStatus==0)
			 * ParentClickStatus=-1;
			 */

			return groupPosition;

		}

		@Override
		public void notifyDataSetChanged() {
			// Refresh List rows
			super.notifyDataSetChanged();
		}

		@Override
		public boolean isEmpty() {
			return ((lst_AMC == null) || lst_AMC.isEmpty());
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

	}

	private void toggle_contents(View v) {
		if (v.isShown()) {
			slide_up(getActivity(), v);
			v.setVisibility(View.GONE);
		} else {
			v.setVisibility(View.VISIBLE);
			slide_down(getActivity(), v);
		}
	}

	private void slide_down(Context ctx, View v) {
		// Toast.makeText(ctx, "SLIDING DOWN", Toast.LENGTH_SHORT).show();
		/*
		 * Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
		 * if (a != null) { a.reset(); if (v != null) { v.clearAnimation();
		 * v.startAnimation(a); } }
		 */

		switch (v.getId()) {
		case R.id.rltl1:
			setArrowImage(img_arrow, R.drawable.downarrow);
			break;
		case R.id.rltl2:
			setArrowImage(img_arrow1, R.drawable.downarrow);
			break;
		case R.id.rltl3:
			setArrowImage(img_arrow2, R.drawable.downarrow);
			break;
		case R.id.rltl4:
			setArrowImage(img_arrow3, R.drawable.downarrow);
			break;
		case R.id.rltl5:
			setArrowImage(img_arrow4, R.drawable.downarrow);
			break;
		case R.id.rltl6:
			setArrowImage(img_arrow5, R.drawable.downarrow);
			break;
		default:
			break;
		}

	}

	private void slide_up(Context ctx, View v) {
		// Toast.makeText(ctx, "SLIDING UP", Toast.LENGTH_SHORT).show();
		/*
		 * Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up); if
		 * (a != null) { a.reset(); if (v != null) { v.clearAnimation();
		 * v.startAnimation(a); } }
		 */

		switch (v.getId()) {
		case R.id.rltl1:
			setArrowImage(img_arrow, R.drawable.orangerightarrow);
			break;
		case R.id.rltl2:
			setArrowImage(img_arrow1, R.drawable.orangerightarrow);
			break;
		case R.id.rltl3:
			setArrowImage(img_arrow2, R.drawable.orangerightarrow);
			break;
		case R.id.rltl4:
			setArrowImage(img_arrow3, R.drawable.orangerightarrow);
			break;
		case R.id.rltl5:
			setArrowImage(img_arrow4, R.drawable.orangerightarrow);
			break;
		case R.id.rltl6:
			setArrowImage(img_arrow5, R.drawable.orangerightarrow);
			break;
		default:
			break;
		}
	}

	private void setArrowImage(ImageView arrow, int image) {
		arrow.setMaxHeight(20);
		arrow.setMaxWidth(20);
		arrow.setImageDrawable(getResources().getDrawable(image));
	}

	private void setListViewHeight(ExpandableListView listView, int group) {
		MyExpandableListAdapter listAdapter = (MyExpandableListAdapter) listView
				.getExpandableListAdapter();
		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(
				listView.getWidth(), View.MeasureSpec.EXACTLY);
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View groupItem = listAdapter.getGroupView(i, false, null, listView);
			groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

			totalHeight += groupItem.getMeasuredHeight();

			if (((listView.isGroupExpanded(i)) && (i != group))
					|| ((!listView.isGroupExpanded(i)) && (i == group))) {
				for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
					View listItem = listAdapter.getChildView(i, j, false, null,
							listView);
					listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

					totalHeight += listItem.getMeasuredHeight();

				}
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
		if (height < 10)
			height = 50;
		params.height = height;
		listView.setLayoutParams(params);
		listView.requestLayout();

	}

	private void setAMCListViewHeight(ExpandableListView listView, int group) {
		AMCMyExpandableListAdapter listAdapter = (AMCMyExpandableListAdapter) listView
				.getExpandableListAdapter();
		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(
				listView.getWidth(), View.MeasureSpec.EXACTLY);
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View groupItem = listAdapter.getGroupView(i, false, null, listView);
			groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

			totalHeight += groupItem.getMeasuredHeight();

			if (((listView.isGroupExpanded(i)) && (i != group))
					|| ((!listView.isGroupExpanded(i)) && (i == group))) {
				for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
					View listItem = listAdapter.getChildView(i, j, false, null,
							listView);
					listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

					totalHeight += listItem.getMeasuredHeight();

				}
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
		if (height < 10)
			height = 50;
		params.height = height;
		listView.setLayoutParams(params);
		listView.requestLayout();

	}

	public void resetPreviousData() {
		text_chassis.setText("");
		text_engine.setText("");
		text_fuel.setText("");
		text_model.setText("");

		text_varient.setText("");
		text_color.setText("");

		text_saledate.setText("");

		text_wend_date.setText("");

		text_lastservicedate.setText("");

		text_lastserviceKM.setText("");
		text_lastservicedealer.setText("");
		text_dealer.setText("");
		text_dealercode.setText("");
		text_company.setText("");
		text_policy.setText("");

		text_policy_sdate.setText("");

		text_policy_edate.setText("");

		text_ew_policyNumber.setText("");

		text_war_sdate.setText("");

		text_war_edate.setText("");

		text_war_sKM.setText("");
		text_war_eKM.setText("");

		// else
		// if(vehdetails.EXTND_WRNTY_FLG.equals("N")||TextUtils.isEmpty(vehdetails.EXTND_WRNTY_FLG)){
		// ew_policyNumber.setText("NO DATA");
		// }
		text_fname.setText("");
		text_lname.setText("");
		text_addr.setText("");
		text_phone.setText("");
	}
}
