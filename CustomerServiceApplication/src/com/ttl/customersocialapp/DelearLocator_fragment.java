package com.ttl.customersocialapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ttl.adapter.DealerEmailAdapter;
import com.ttl.adapter.DealerListAdapter;
import com.ttl.adapter.DealerPhnnumAdapter;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.DelearLocatorData;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
import com.ttl.webservice.WebServiceCall;

public class DelearLocator_fragment extends Fragment implements
		LocationListener , OnMapLoadedCallback {
	private View rootView;
	private Button list, map, viewmap, viewlist, btnhide;
	private RelativeLayout relmap, rellist;
	private LocationManager locationManager;
	public static MapView mapView;
	public static GoogleMap googleMap;
	private InstantAutoComplete txtcity ,txtstate , txtPPL;
	private ArrayList<String> statelist = new ArrayList<String>();
	private ArrayList<String> citylist = new ArrayList<String>();

	private ArrayAdapter<String> stateaadapter , cityAdapter , pplAdapter;
	private ListView dealerlist;
	public String acticityname_DealerLocatorcity = "DealerLocatorCity";
	public String acticityname_DealerLocatordata = "DealerLocatorData";
	private boolean flag = false;
	private String cityname , statename , pplname;
	private ArrayList<DelearLocatorData> dealerlist1 = new ArrayList<DelearLocatorData>();
	private DealerListAdapter dealeradapter;
	private boolean deleardata = false;
	public static Double MyLat, MyLong;
	private Location location;
	public static int selecteditem;
	private ImageView preferredDealer;
	private Dialog prefDealerDialog;
	private boolean connect;
	private TextView txtdealerName , txtDealerAddress , txtDealerEmail ;
	private ArrayList<HashMap<String , Double>> latlong= new ArrayList<HashMap<String, Double>>();
	private Tracker mTracker;
    @Override
	public void onStart() {
		
		super.onStart();
		mTracker.setScreenName("DealerLocatorScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		deleardata = false;
		rootView = inflater.inflate(R.layout.fragment_delearlocator,
				container, false);
		CheckConnectivity checknow = new CheckConnectivity();
		 connect = checknow
				.checkNow(getActivity());
		 if (connect) {
			}
			 else {
				Toast toast = Toast.makeText(getActivity(),
						"No network connection available.",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		list = (Button) rootView.findViewById(R.id.btnlist);
		map = (Button) rootView.findViewById(R.id.btnmap);
		//tracker
		 AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
			mTracker = application.getDefaultTracker();
			
		relmap = (RelativeLayout) rootView.findViewById(R.id.relview);
		rellist = (RelativeLayout) rootView.findViewById(R.id.rellistview);
		preferredDealer = (ImageView) rootView.findViewById(R.id.dealer_pref);
		viewlist = (Button) rootView.findViewById(R.id.viewlist);
		viewmap = (Button) rootView.findViewById(R.id.viewmap);
		list.setBackgroundColor(rootView.getContext().getResources()
				.getColor(R.color.stronggray));
		list.setTextColor(rootView.getContext().getResources()
				.getColor(R.color.litegray));
		viewlist.setBackgroundColor(rootView.getContext().getResources()
				.getColor(R.color.stronggray));
		rellist.setVisibility(View.GONE);
		preferredDealer.setVisibility(View.GONE);
		mapView = (MapView) rootView.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		dealerlist = (ListView) rootView
				.findViewById(R.id.delearloc);
		// Gets to GoogleMap from the MapView and does initialization stuff
		googleMap = mapView.getMap();
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.setMyLocationEnabled(true);
		mapView.onResume();// needed to get the map to display immediately
		googleMap.setOnMapLoadedCallback(this);
		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

		btnhide = (Button) rootView.findViewById(R.id.btnhide);
		txtcity = (InstantAutoComplete) rootView.findViewById(R.id.selectcity);
		txtstate = (InstantAutoComplete) rootView.findViewById(R.id.selectstate);
		txtPPL = (InstantAutoComplete) rootView.findViewById(R.id.selectppl);
		
		locationManager = (LocationManager) rootView.getContext()
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, // 3 sec
				10, this);

		/*********
		 * After registration onLocationChanged method called periodically after
		 * each 3 sec
		 ***********/
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {

			MyLat = location.getLatitude();
			MyLong = location.getLongitude();
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(MyLat, MyLong)).zoom(13).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		}

		
	
		 
		 
		 
		

		btnhide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				if (flag) {
					flag = false;
					txtcity.clearAnimation();
					txtcity.setVisibility(View.VISIBLE);
					btnhide.setCompoundDrawablesWithIntrinsicBounds(0,
							R.drawable.rightarrow, 0, 0);

				} else {
					flag = true;
					TranslateAnimation animate = new TranslateAnimation(0,
							1000, 0, 0);
					animate.setDuration(500);
					animate.setFillAfter(true);
					txtcity.startAnimation(animate);
					btnhide.setCompoundDrawablesWithIntrinsicBounds(0,
							R.drawable.leftarrows, 0, 0);
					txtcity.setVisibility(View.GONE);

				}
			}
		});

		map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				relmap.setVisibility(View.VISIBLE);
				map.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.darkgray));
				map.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.litegray));
				viewmap.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.yellow));
				list.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.stronggray));
				list.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.litegray));
				viewlist.setBackgroundColor(rootView.getContext()
						.getResources().getColor(R.color.stronggray));

			}
		});

		list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				if (deleardata) {
					
				/*	dealerlist.setAdapter(dealeradapter);

					dealeradapter.notifyDataSetChanged();*/
					rellist.setVisibility(View.VISIBLE);
					
						preferredDealer.setVisibility(View.VISIBLE);
						/*if(!(UserDetails.getPreffered_dealer_name().equals("")) || UserDetails.getPreffered_dealer_name().equals(null))
						{
							preferredDealer.setBackgroundResource(R.drawable.myprefereddealer);
						}else
						{
							preferredDealer.setBackgroundResource(R.drawable.blackstar);
						}*/
					relmap.setVisibility(View.GONE);
					list.setBackgroundColor(rootView.getContext()
							.getResources().getColor(R.color.darkgray));
					list.setTextColor(rootView.getContext().getResources()
							.getColor(R.color.litegray));
					viewlist.setBackgroundColor(rootView.getContext()
							.getResources().getColor(R.color.yellow));
					map.setBackgroundColor(rootView.getContext().getResources()
							.getColor(R.color.stronggray));
					map.setTextColor(rootView.getContext().getResources()
							.getColor(R.color.darkgray));
					viewmap.setBackgroundColor(rootView.getContext()
							.getResources().getColor(R.color.stronggray));
				} else
				{
					
					Toast.makeText(getActivity(), "Please select all fields and try again.",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		//populateState();
		
		preferredDealer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!(UserDetails.getPreffered_dealer_name().equals("")) || UserDetails.getPreffered_dealer_name().equals(null))
				{
				 prefDealerDialog = new Dialog(getActivity());
				 prefDealerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				 prefDealerDialog.setContentView(R.layout.mypreffered_dealer_popup);
				 prefDealerDialog.setCancelable(false);
				 prefDealerDialog.show();
				 
				 txtdealerName = (TextView) prefDealerDialog.findViewById(R.id.txtdelername);
				 txtDealerAddress = (TextView) prefDealerDialog.findViewById(R.id.txtdeleradd);
				
				 ImageView close = (ImageView) prefDealerDialog.findViewById(R.id.imgclose);
				
				 Button yes , no;
				 yes = (Button) prefDealerDialog.findViewById(R.id.btnyes);
				 no = (Button) prefDealerDialog.findViewById(R.id.btnno);
				 ListView phnnumlist = (ListView) prefDealerDialog.findViewById(R.id.delearphonenumberlist) ;
				 ListView email_list = (ListView) prefDealerDialog.findViewById(R.id.delearemaillist) ;
				 txtdealerName.setText(UserDetails.getPreffered_dealer_name());
				 txtDealerAddress.setText(UserDetails.getPreffered_dealer_address());
				
				 
				 
					ArrayList<String> dealerphnnolist = new ArrayList<String>();
					String[] phnarray = UserDetails.getPreffered_dealer_number().split("/");
					Log.d("Phone number	list size", phnarray.length+"");
					for(int i= 0; i<phnarray.length; i++)
					{
						dealerphnnolist.add(phnarray[i]);
						Log.d("Phone numbers", phnarray[i]);
					
					
					}
					

					ArrayList<String> emaillist = new ArrayList<String>();
					String[] emailarray = UserDetails.getPreffered_dealer_email().split("/");
					Log.d("Phone number	list size", emailarray.length+"");
					for(int i= 0; i<emailarray.length; i++)
					{
						emaillist.add(emailarray[i]);
						Log.v("Email address", emailarray[i]);
					
					
					}

					
					
					DealerPhnnumAdapter phno = new DealerPhnnumAdapter(getActivity(), dealerphnnolist);
					//regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					phnnumlist.setAdapter(phno);
				
					
					DealerEmailAdapter dealerEmailAdapter=new DealerEmailAdapter(getActivity(), emaillist);
					
					email_list.setAdapter(dealerEmailAdapter);
					close.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						prefDealerDialog.dismiss();
					}
				});
				 
				 no.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							prefDealerDialog.dismiss();
						}
					});
				 
				 yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							connect=new CheckConnectivity().checkNow(getActivity());
							//prefDealerDialog.dismiss();
							if (connect) {
							String req = Config.awsserverurl+"tmsc_ch/customerapp/user/setPrefferedDealer";
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_name",""));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_address",""));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_number",""));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_email",""));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_latitude",""));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_longitude",""));
							nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails.getUser_id()));
							 nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
							new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST,
									Constants.setPrefferedDealer, nameValuePairs,
									new ResponseCallback() {

										@Override
										public void onResponseReceive(Object object) {
											
											boolean res = (boolean) object;
											if (res) {
												prefDealerDialog.dismiss();
												//getVehicles();
												Toast.makeText(getActivity(), "Successful",
														Toast.LENGTH_LONG).show();
												
												UserDetails.setPreffered_dealer_name("");
												UserDetails.setPreffered_dealer_email("");
												UserDetails.setPreffered_dealer_address("");
												UserDetails.setPreffered_dealer_number("");
												UserDetails.setPreffered_dealer_latitude("");
												UserDetails.setPreffered_dealer_longitude("");
											} else {
												Toast.makeText(getActivity(), "Unsuccessful",
														Toast.LENGTH_LONG).show();
											}
											rootView.getRootView().setFocusable(true);
									  		rootView.getRootView().requestFocus();
										}

										@Override
										public void onErrorReceive(String string) {
											
										/*	Toast.makeText(
													context,
													"Invalid credentials, please check username passsword you entered.",
													Toast.LENGTH_LONG).show();*/
											// edtuser_id.setFocusable(true);
											rootView.getRootView().setFocusable(true);
									  		rootView.getRootView().requestFocus();
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
					});
				}
				else
				{
					Toast.makeText(getActivity(), "Preferred Dealer not set. Please set first.",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		rootView.getRootView().setFocusableInTouchMode(true);
		rootView.getRootView().requestFocus();

		rootView.getRootView().setOnKeyListener(new OnKeyListener() {
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
		return rootView;
	}

	
	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onLocationChanged(Location location) {
		

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		

	}

	@Override
	public void onProviderEnabled(String provider) {
		

	}

	@Override
	public void onProviderDisabled(String provider) {
		

	}
	private String selectState(String tempstate2) {
		
        if(tempstate2.equalsIgnoreCase("Andaman and Nicobar"))
            return "AN";
        else

		if(tempstate2.equalsIgnoreCase("Andaman"))
			return "AN";
		else
			if(tempstate2.equalsIgnoreCase("Andhra Pradesh"))
				return "AP";
			else
				if(tempstate2.equalsIgnoreCase("Telangana"))
					return "TG";
				else
					if(tempstate2.equalsIgnoreCase("Arunachal Pradesh"))
						return "AR";
					else
						if(tempstate2.equalsIgnoreCase("Assam"))
							return "AS";
						else
							if(tempstate2.equalsIgnoreCase("Bihar"))
								return "BR";
							else
								if(tempstate2.equalsIgnoreCase("Chandigarh"))
									return "CH";
								else
									if(tempstate2.equalsIgnoreCase("Chattisgarh"))
                                    return "CG";

									if(tempstate2.equalsIgnoreCase("Chhattisgarh"))
										return "CG";
									else
										if(tempstate2.equalsIgnoreCase("Chennai"))
											return "CHN";
										else
											if(tempstate2.equalsIgnoreCase("Dadra, Nagarhaveli"))
												return "DN";
											else
											if(tempstate2.equalsIgnoreCase("Dadra haveli"))
												return "DN";
									
											else
												if(tempstate2.equalsIgnoreCase("Daman and Diu"))
													return "DD";
												else
												if(tempstate2.equalsIgnoreCase("Daman"))
													return "DD";
												else
													if(tempstate2.equalsIgnoreCase("Delhi"))
														return "DL";
													else
														if(tempstate2.equalsIgnoreCase("Goa"))
															return "GA";
														else
															if(tempstate2.equalsIgnoreCase("Gujarat"))
																return "GJ";
															else
																if(tempstate2.equalsIgnoreCase("Haryana"))
																	return "HR";
																else
																	if(tempstate2.equalsIgnoreCase("Himachal Pradesh"))
																		return "HP";
																	  else
	                                                                        if(tempstate2.equalsIgnoreCase("Jammu and Kashmir"))
	                                                                            return "JK";
																	else
																		
																		if(tempstate2.equalsIgnoreCase("Jammu"))
																			return "JK";
																		else
																			if(tempstate2.equalsIgnoreCase("Jharkhand"))
																				return "JH";
																			else
																				if(tempstate2.equalsIgnoreCase("Karnataka"))
																					return "KA";
																				else
																					if(tempstate2.equalsIgnoreCase("Kerala"))
																						return "KL";
																					else
																						if(tempstate2.equalsIgnoreCase("Lakshwadeep"))
																							return "LD";
																						else
																							if(tempstate2.equalsIgnoreCase("Madhya Pradesh"))
																								return "MP";
																							else
																								if(tempstate2.equalsIgnoreCase("Maharashtra"))
																									return "MH";
				if(tempstate2.equalsIgnoreCase("Manipur"))
					return "MN";
				else
					if(tempstate2.equalsIgnoreCase("Meghalaya"))
						return "ML";
					else
						if(tempstate2.equalsIgnoreCase("Mizoram"))
							return "MZ";
						else
							if(tempstate2.equalsIgnoreCase("Nagaland"))
								return "NL";
							else
								if(tempstate2.equalsIgnoreCase("North Tamil Nadu"))
									return "NTN";
								else
									if(tempstate2.equalsIgnoreCase("Orissa"))
										return "OR";
									else
										if(tempstate2.equalsIgnoreCase("Pondicherry"))
											return "PY";
										else
											if(tempstate2.equalsIgnoreCase("Punjab"))
												return "PB";
											else
												if(tempstate2.equalsIgnoreCase("Rajasthan"))
													return "RJ";
												else
													if(tempstate2.equalsIgnoreCase("Sikkim"))
														return "SK";
													else
														if(tempstate2.equalsIgnoreCase("Tamil Nadu"))
															return "TN";
														else
															if(tempstate2.equalsIgnoreCase("Tripura"))
																return "TR";
															else
																if(tempstate2.equalsIgnoreCase("Uttrakhand"))
																	return "UA";
																else
																	if(tempstate2.equalsIgnoreCase("Uttar Pradesh"))
																		return "UP";
																	else
																		if(tempstate2.equalsIgnoreCase("West Bengal"))
																			return "WB";
																		else
																			if(tempstate2.equalsIgnoreCase("APO"))
																				return "APO";
																			else
																				if(tempstate2.equalsIgnoreCase("Others"))
																				return "OT";
																				else
																					return "";
					
																
	}

	@Override
	public void onMapLoaded() {
		
		if(googleMap!=null)
		{
			connect=new CheckConnectivity().checkNow(getActivity());
			if (connect) {
			populateState();
			}
			 else {
				Toast toast = Toast.makeText(getActivity(),
						"No network connection available.",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}
	
	public void populateState()
	{
		String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+"<SOAP:Body>"
				+"<GetState_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\" />"
				+"</SOAP:Body>"
				+"</SOAP:Envelope>";

		new WebServiceCall(getActivity(), req,
				Constants.GetState_CSB, new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						statelist = (ArrayList<String>) object;
						stateaadapter = new ArrayAdapter<String>(

						rootView.getContext(),
								android.R.layout.simple_list_item_1, statelist);

						txtstate.setAdapter(stateaadapter);
						if (statelist == null) {
							Toast.makeText(getActivity(),
									"Server Error Please Try Again",
									Toast.LENGTH_LONG).show();
						} else {

							stateaadapter.notifyDataSetChanged();
						}
						
						rootView.getRootView().setFocusable(true);
				  		rootView.getRootView().requestFocus();
					}

					@Override
					public void onErrorReceive(String string) {
						
						rootView.getRootView().setFocusable(true);
				  		rootView.getRootView().requestFocus();
					}
				}, "Run").execute();

		// aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinState.setAdapter(aa);
		txtstate.setThreshold(1);
		txtstate.setDropDownBackgroundResource(color.white );

		txtstate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				txtstate.showDropDown();
				txtstate.requestFocus();
			}
		});

		txtstate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(!txtcity.getText().equals("")||!txtPPL.getText().equals("")){
					googleMap.clear();
					txtcity.setText("");
					txtPPL.setText("");
					deleardata=false;
					citylist.clear();
					statename = txtstate.getText().toString();
					
					statename = statename.replaceAll("&", "&amp;");
					connect=new CheckConnectivity().checkNow(getActivity());
					if (connect) {
					String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							+"<SOAP:Body>"
							+"<GetCity_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
							+"<State>"+selectState(statename)+"</State>"
							+"</GetCity_CSB>"
							+"</SOAP:Body>"
							+"</SOAP:Envelope>";
					
					new WebServiceCall(getActivity(), req,
							Constants.GetCity_CSB, new ResponseCallback() {

								@Override
								public void onResponseReceive(Object object) {
									
									citylist = (ArrayList<String>) object;
									cityAdapter = new ArrayAdapter<String>(

									rootView.getContext(),
											android.R.layout.simple_list_item_1, citylist);

									txtcity.setAdapter(cityAdapter);
									if (statelist == null) {
										Toast.makeText(getActivity(),
												"Server Error Please Try Again",
												Toast.LENGTH_LONG).show();
									} else {

										cityAdapter.notifyDataSetChanged();
									}
									txtcity.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											
											txtcity.showDropDown();
											txtcity.requestFocus();
										}
									});
									String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
											+"<SOAP:Body>"
											+"<GetPPL_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\" />"
											+"</SOAP:Body>"
											+"</SOAP:Envelope>";
									new WebServiceCall(getActivity(), req1,
											Constants.GetPPL_CSB, new ResponseCallback() {

												@Override
												public void onResponseReceive(Object object) {
													
													statelist = (ArrayList<String>) object;
													pplAdapter = new ArrayAdapter<String>(

													rootView.getContext(),
															android.R.layout.simple_list_item_1, statelist);

													txtPPL.setAdapter(pplAdapter);
													if (statelist == null) {
														Toast.makeText(getActivity(),
																"Server Error Please Try Again",
																Toast.LENGTH_LONG).show();
													} else {

														pplAdapter.notifyDataSetChanged();
													}
													
													rootView.getRootView().setFocusable(true);
											  		rootView.getRootView().requestFocus();
												}

												@Override
												public void onErrorReceive(String string) {
													
													rootView.getRootView().setFocusable(true);
											  		rootView.getRootView().requestFocus();
												}
											}, "Run").execute();
								}

								@Override
								public void onErrorReceive(String string) {
									
									citylist.clear();
								}
							}, "Run").execute();
					}
					 else {
						Toast toast = Toast.makeText(getActivity(),
								"No network connection available.",
								Toast.LENGTH_SHORT);
						toast.show();
					}
				} else{
				statename = txtstate.getText().toString();
			
				statename = statename.replaceAll("&", "&amp;");
				connect=new CheckConnectivity().checkNow(getActivity());
				if (connect) {
				String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+"<SOAP:Body>"
						+"<GetCity_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
						+"<State>"+selectState(statename)+"</State>"
						+"</GetCity_CSB>"
						+"</SOAP:Body>"
						+"</SOAP:Envelope>";
				
				new WebServiceCall(getActivity(), req,
						Constants.GetCity_CSB, new ResponseCallback() {

							@Override
							public void onResponseReceive(Object object) {
								
								statelist = (ArrayList<String>) object;
								cityAdapter = new ArrayAdapter<String>(

								rootView.getContext(),
										android.R.layout.simple_list_item_1, statelist);

								txtcity.setAdapter(cityAdapter);
								if (statelist == null) {
									Toast.makeText(getActivity(),
											"Server Error Please Try Again",
											Toast.LENGTH_LONG).show();
								} else {

									cityAdapter.notifyDataSetChanged();
								}
								
								String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
										+"<SOAP:Body>"
										+"<GetPPL_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\" />"
										+"</SOAP:Body>"
										+"</SOAP:Envelope>";
								new WebServiceCall(getActivity(), req1,
										Constants.GetPPL_CSB, new ResponseCallback() {

											@Override
											public void onResponseReceive(Object object) {
												
												statelist = (ArrayList<String>) object;
												pplAdapter = new ArrayAdapter<String>(

												rootView.getContext(),
														android.R.layout.simple_list_item_1, statelist);

												txtPPL.setAdapter(pplAdapter);
												if (statelist == null) {
													Toast.makeText(getActivity(),
															"Server Error Please Try Again",
															Toast.LENGTH_LONG).show();
												} else {

													pplAdapter.notifyDataSetChanged();
												}
												
												rootView.getRootView().setFocusable(true);
										  		rootView.getRootView().requestFocus();
											}

											@Override
											public void onErrorReceive(String string) {
												
												rootView.getRootView().setFocusable(true);
										  		rootView.getRootView().requestFocus();
											}
										}, "Run").execute();
							}

							@Override
							public void onErrorReceive(String string) {
								

							}
						}, "Run").execute();
				}
				 else {
					Toast toast = Toast.makeText(getActivity(),
							"No network connection available.",
							Toast.LENGTH_SHORT);
					toast.show();
				}
				}
	
			}
		});
		
		txtcity.setThreshold(1);
		txtcity.setDropDownBackgroundResource(color.white);

		txtcity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				txtcity.showDropDown();
				txtcity.requestFocus();
			}
		});
		
		txtcity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(txtPPL.getText().toString().equals(""))
				{
					Log.d("PPL", "blank");
				}else if (new CheckConnectivity().checkNow(getActivity())) {

					populateDealer();
					}
					 else {
						Toast toast = Toast.makeText(getActivity(),
								"No network connection available.",
								Toast.LENGTH_SHORT);
						toast.show();
					}
			}
		});
		txtPPL.setThreshold(1);
		txtPPL.setDropDownBackgroundResource(color.white);

		txtPPL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				txtPPL.showDropDown();
				txtPPL.requestFocus();
			}
		});
		txtPPL.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				connect=new CheckConnectivity().checkNow(getActivity());
				if (connect) {
				populateDealer();
				}
				 else {
					Toast toast = Toast.makeText(getActivity(),
							"No network connection available.",
							Toast.LENGTH_SHORT);
					toast.show();
				}
				
				InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
						.getSystemService(getActivity().INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(), 0);

			}
		});
	}
	
	public void populateDealer()
	{
		cityname = txtcity.getText().toString();
		pplname = txtPPL.getText().toString();
		
		if(!(cityname.equals("")) || !(pplname.equals("")))
		{
			
		dealerlist1.clear();
		
		
		googleMap.clear();
		

		/*String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<SOAP:Body>"
				+ "<GetDlrLocDtlsCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
				+ "<City>"
				+ cityname.toString()
				+ "</City>"
				+ "<LAT></LAT>"
				+ "<LONG></LONG>"
				+ " </GetDlrLocDtlsCSB>"
				+ "</SOAP:Body>"
				+ "</SOAP:Envelope>";*/
		String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+"<SOAP:Body>"
				+"<GetDlrLocDtlsCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
				+"<PPL>"+pplname+"</PPL>"
				+"<State>"+statename+"</State>"
				+"<City>"+cityname+"</City>"
				+"</GetDlrLocDtlsCSB>"
				+"</SOAP:Body>"
				+"</SOAP:Envelope>";
		new WebServiceCall(getActivity(), req,
				Constants.GetDlrLocDtlsCSB, new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						dealerlist1 = (ArrayList<DelearLocatorData>) object;
						Log.d("dealerlist size", dealerlist1.size()
								+ "");
						if(dealerlist1.size()>0)
						{
							deleardata = true;
						}
						for (int i = 0; i < dealerlist1.size(); i++) {
							/*Log.d(dealerlist1.get(i).delear_LATITUDE,
									"yet ahe");*/
							if(!(dealerlist1.get(i).delear_LATITUDE.equals("")))
									{
							double lat = Double.parseDouble(dealerlist1
									.get(i).delear_LATITUDE);
							double lng = Double.parseDouble(dealerlist1
									.get(i).delear_LONGITUDE);
							MarkerOptions marker = new MarkerOptions()
									.position(new LatLng(lat, lng))
									.title(dealerlist1.get(i).delear_DIV_COMMON_NAME)
									;
							// Changing marker icon
							marker.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.marker));

							// adding marker
							googleMap.addMarker(marker);
							CameraPosition cameraPosition = new CameraPosition.Builder()
									.target(new LatLng(lat, lng))
									.zoom(10).build();

							googleMap.animateCamera(CameraUpdateFactory
									.newCameraPosition(cameraPosition));
							Log.d("selected item",
									""
											+ selecteditem
											+ "Value"
											+ dealerlist1
													.get(selecteditem));
							/*String Address = dealerlist1.get(i).delear_DIV_COMMON_NAME+" "+
									dealerlist1.get(i).delear_DIV_ADDRESS_1 +" "+dealerlist1
									.get(i).delear_DIV_ADDRESS_2 + " "
									+ dealerlist1.get(i).delear_DIV_CITY + " "
									+ dealerlist1.get(i).delear_DIV_STATE + "-"
									+ dealerlist1.get(i).delear_DIV_ZIP_CODE;
							Log.d("Address", Address);*/
							
							/*ArrayList<HashMap<String, Double>> latlong = getLocationFromAddress(Address);
							
							Log.d("Latitude"+latlong.get(0).get("Latitude"), "Longitude"+latlong.get(0).get("Longitude"));*/
							if(selecteditem==i){
								
							}
									}
						}

						dealeradapter = new DealerListAdapter(rootView
								.getContext(),
								R.layout.delear_locator_list,
								dealerlist1);
						Log.d("dealerlist size", dealerlist1.size()
								
								+ "");
						dealeradapter.notifyDataSetChanged();
						dealerlist.setAdapter(dealeradapter);
						/*
						 * 
						 * dealeradapter.notifyDataSetChanged();
						 */
						rootView.getRootView().setFocusable(true);
				  		rootView.getRootView().requestFocus();
					}

					@Override
					public void onErrorReceive(String string) {
						
						
						Toast.makeText(getActivity(), "Dealer not available", Toast.LENGTH_SHORT).show();
						
					
					}
				}, "RUN").execute();
		}else
		{
			Toast.makeText(getActivity(),
					"Plaese select City and PPL",
					Toast.LENGTH_LONG).show();
		}
	}
	
	public ArrayList<HashMap<String, Double>> getLocationFromAddress(String strAddress){

		Geocoder coder = new Geocoder(getActivity());
		List<Address> address;
		//GeoPoint p1 = null;
		ArrayList<HashMap<String , Double>> latlong= new ArrayList<HashMap<String, Double>>();
		try {
		    address = coder.getFromLocationName(strAddress,5);
		    Log.d("Addresss getting", address.toString());
		   /* if (address==null) {
		       return null;
		    }*/
		    Address location=address.get(0);
		    location.getLatitude();
		    location.getLongitude();
		    HashMap<String , Double> values = new HashMap<>();
		    values.put("Latitude", location.getLatitude());
		    values.put("Longitude", location.getLongitude());
		    
		    latlong.add(values);
		   /* p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
		                      (int) (location.getLongitude() * 1E6), 0);*/

		    return latlong;
		    } catch (IOException e) {
				
				e.printStackTrace();
			}
		return null;
		
		}
	
	public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

        address = address.replaceAll(" ","%20");    

        HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
           
            e.printStackTrace();
        }

        return jsonObject;
    }
	
	public  boolean getLatLong(JSONObject jsonObject) {

        try {

           Double longitute = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

           Double  latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");
           HashMap<String , Double> values = new HashMap<>();
		    values.put("Latitude", latitude);
		    values.put("Longitude", longitute);
		    
		    latlong.add(values);
        } catch (JSONException e) {
            return false;

        }

        return true;
    }
}
