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

public class PaidServiceFragment extends Fragment {

	private Spinner spinregno, kmtype;
	private InstantAutoComplete spinstate, spincity;
	private EditText labourcost, sparecost, consumcost;
	private List<String> regnovalues = new ArrayList<String>();
	private List<String> statevalues = new ArrayList<String>();
	private List<String> kmvalue = new ArrayList<String>();
	private List<String> cityvalues = new ArrayList<String>();
	private String pl = "", state, km = "", city = "", regnumber = "";
	private TextView totalcost;
	private List<String> array1 = new ArrayList<String>();

	private ArrayAdapter<String> kmaa;
	private ArrayAdapter<String> stateaa;
	private Button fetch;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_paid_service, viewGroup,
				false);

		spinregno = (Spinner) view.findViewById(R.id.spinregno);
		spinstate = (InstantAutoComplete) view.findViewById(R.id.spinstate);
		spincity = (InstantAutoComplete) view.findViewById(R.id.spincity);
		kmtype = (Spinner) view.findViewById(R.id.spinkmtype);
		labourcost = (EditText) view.findViewById(R.id.txtlabourcost);
		sparecost = (EditText) view.findViewById(R.id.txtsparecost);
		consumcost = (EditText) view.findViewById(R.id.txtconsumcost);
		totalcost = (TextView) view.findViewById(R.id.txttotalcost);
		regnovalues.add("Select Vehicle");
		int size = new UserDetails().getRegNumberList().size();
		for (int i = 0; i < size; i++) {
			if (!(new UserDetails().getRegNumberList().get(i)
					.get("registration_num").toString().equals(""))) {
				regnovalues.add(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"));
			} else {
				regnovalues.add(new UserDetails().getRegNumberList().get(i)
						.get("chassis_num"));
			}
		}

		labourcost.setEnabled(false);
		sparecost.setEnabled(false);
		consumcost.setEnabled(false);

		ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, regnovalues);
		regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinregno.setAdapter(regno);
		array1.add(0, "KM/Period (In months)");
		kmaa = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, array1);
		kmaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		kmtype.setAdapter(kmaa);

		kmtype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position != 0) {
					km = parent.getItemAtPosition(position).toString();
					/*
					 * if(!(city.equals(""))) { if(!(km.equals(""))) {
					 * if(!(pl.equals(""))) { calculateCost(pl , city , km);
					 * 
					 * } } }
					 */
				} else {
					// Toast.makeText(getActivity(), "KM/Period (In months)",
					// Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				

			}
		});
		spinregno.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view1,
					int position, long id) {
				

				regnumber = parent.getItemAtPosition(position).toString();


				
				if (position > 0) {
					pl = new UserDetails().getRegNumberList().get(position - 1)
							.get("pl");
					array1.clear();
					array1.add(0, "KM/Period (In months)");
					kmtype.setSelection(0);
				} else {
					pl = "";
					array1.clear();
					array1.add(0, "KM/Period (In months)");
					kmtype.setSelection(0);
				}
				
				if (!(regnumber.equals("Select Vehicle"))) {
					CheckConnectivity checknow = new CheckConnectivity();
					boolean connect = checknow.checkNow(getActivity());
					if (connect) {
						String req = Config.awsserverurl
								+ "tmsc_ch/customerapp/costEstimateServices/getKms";
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								4);
						nameValuePairs.add(new BasicNameValuePair("PL", pl));
						nameValuePairs.add(new BasicNameValuePair(
								"serviceTypeGroup", "Scheduled"));
						nameValuePairs.add(new BasicNameValuePair("sessionId",
								UserDetails.getSeeionId()));
						nameValuePairs.add(new BasicNameValuePair("user_id",
								UserDetails.getUser_id()));
						new AWS_WebServiceCall(getActivity(), req,
								ServiceHandler.POST, Constants.getKms,
								nameValuePairs, new ResponseCallback() {

									@Override
									public void onResponseReceive(Object object) {
										
										kmvalue = (List<String>) object;
										for (int i = 0; i < kmvalue.size(); i++) {
											array1.add(kmvalue.get(i));
										}

										kmaa.notifyDataSetChanged();
										view.getRootView().setFocusable(true);
										view.getRootView().requestFocus();
									}

									@Override
									public void onErrorReceive(String string) {
										
										Toast toast = Toast
												.makeText(
														getActivity(),
														"Sorry data is under development for this product. Will be available soon!",
														Toast.LENGTH_SHORT);
										toast.show();
										view.getRootView().setFocusable(true);
										view.getRootView().requestFocus();
									}
								}).execute();
					} else {
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
		boolean connect = checknow.checkNow(getActivity());
		if (connect) {
			String req = Config.awsserverurl
					+ "tmsc_ch/customerapp/costEstimateServices/getState";
			new AWS_WebServiceCall(getActivity(), req, ServiceHandler.GET,
					Constants.getState, new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							
							statevalues = (List<String>) object;

							stateaa = new ArrayAdapter<String>(getActivity(),
									android.R.layout.simple_spinner_item,
									statevalues);
							stateaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spinstate.setThreshold(1);
							spinstate.setAdapter(stateaa);
							spincity.setText("");
						// Removed by vaibhav because andaman is not getting select
						//	spinstate.setValidator(new Validator(statevalues));
							view.getRootView().setFocusable(true);
							view.getRootView().requestFocus();
						}

						@Override
						public void onErrorReceive(String string) {
							
							view.getRootView().setFocusable(true);
							view.getRootView().requestFocus();
						}
					}).execute();
		} else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}
		// spinnerstate.setDropDownBackgroundResource(R.color.spinback);
		spinstate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				spinstate.showDropDown();
			}
		});
		spinstate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view1,
					int position, long id) {
				
				spincity.setText("");
				state = parent.getItemAtPosition(position).toString();
				CheckConnectivity checknow = new CheckConnectivity();
				boolean connect = checknow.checkNow(getActivity());
				if (connect) {
					String req = Config.awsserverurl
							+ "tmsc_ch/customerapp/costEstimateServices/getCityFromState";
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs.add(new BasicNameValuePair("state", state));
					new AWS_WebServiceCall(getActivity(), req,
							ServiceHandler.POST, Constants.getCityFromState,
							nameValuePairs, new ResponseCallback() {

								@Override
								public void onResponseReceive(Object object) {
									
									cityvalues = (List<String>) object;
									ArrayAdapter<String> cityaa = new ArrayAdapter<String>(
											getActivity(),
											android.R.layout.simple_spinner_item,
											cityvalues);
									cityaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spincity.setThreshold(1);
									spincity.setAdapter(cityaa);
									/*spincity.setValidator(new Validator(
											cityvalues));*/
									spincity.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											
											spincity.showDropDown();
										}
									});

									spincity.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											
											city = parent.getItemAtPosition(
													position).toString();
											km = kmtype.getSelectedItem()
													.toString();
											// spincity.performValidation();
											/*
											 * if(!(city.equals(""))) {
											 * if(!(km.equals(""))) {
											 * if(!(pl.equals(""))) {
											 * calculateCost(pl , city , km);
											 * 
											 * } } }
											 */

										}
									});
									view.getRootView().setFocusable(true);
									view.getRootView().requestFocus();
								}

								@Override
								public void onErrorReceive(String string) {
									
									view.getRootView().setFocusable(true);
									view.getRootView().requestFocus();
								}
							}).execute();
				} else {
					Toast toast = Toast.makeText(getActivity(),
							"No network connection available.",
							Toast.LENGTH_SHORT);
					toast.show();
				}

			}
		});
		fetch = (Button) view.findViewById(R.id.btnfetch);
		fetch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// spincity.performValidation();
				consumcost.setText("");
				labourcost.setText("");
				sparecost.setText("");
				city = spincity.getText().toString();
				state = spinstate.getText().toString();
				km = kmtype.getSelectedItem().toString();
				/*
				 * regnumber = spinregno.getSelectedItem().toString(); int size
				 * = new UserDetails().getRegNumberList().size(); for(int i = 0
				 * ; i<size; i++) { String temp = new
				 * UserDetails().getRegNumberList
				 * ().get(i).get("registration_num"); if(temp.equals(regnumber))
				 * { pl = new UserDetails().getRegNumberList().get(i).get("pl");
				 * } }
				 */

				if (!(pl.equals(""))) {
					if (!(km.equals("KM/Period (In months)"))) {
						if (!(state.equals(""))) {
							if (statevalues.contains(state)) {
								if (!(city.equals(""))) {

									if (!(cityvalues.contains(city))) {
										Toast.makeText(
												getActivity(),
												"Please select city form list.",
												Toast.LENGTH_SHORT).show();
									} else
										calculateCost(pl, city, km);
								} else {
									Toast.makeText(getActivity(),
											"Please select city.",
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(getActivity(),
										"Please select state from list..",
										Toast.LENGTH_SHORT).show();

							}
						} else {
							Toast.makeText(getActivity(),
									"Please select state.", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						Toast.makeText(getActivity(),
								"Please select KM/Period (In months).",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), "Please select Vehicle.",
							Toast.LENGTH_SHORT).show();
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
								.addToBackStack(null).commit();
						return true;
					}
				}
				return false;
			}
		});
		return view;
	}

	public void calculateCost(String pl, String city, String km) {
		CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow.checkNow(getActivity());
		if (connect) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
			nameValuePairs.add(new BasicNameValuePair("PL", pl));
			nameValuePairs.add(new BasicNameValuePair("serviceTypeGroup",
					"Scheduled"));
			nameValuePairs.add(new BasicNameValuePair("city", city));
			nameValuePairs.add(new BasicNameValuePair("kms_period", km));
			nameValuePairs.add(new BasicNameValuePair("sessionId", UserDetails
					.getSeeionId()));
			nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails
					.getUser_id()));
			String req = Config.awsserverurl
					+ "tmsc_ch/customerapp/costEstimateServices/getPaidServiceCostEstimate";
			new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST,
					Constants.getPaidServiceCostEstimate, nameValuePairs,
					new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							
							ArrayList<HashMap<String, String>> costList = (ArrayList<HashMap<String, String>>) object;
							for (int i = 0; i < costList.size(); i++) {
								if (costList.get(i).get("costType")
										.equals("Consumable")) {
									consumcost.setText(costList.get(i).get(
											"cost"));
								} else if (costList.get(i).get("costType")
										.equals("Labour")) {
									labourcost.setText(costList.get(i).get(
											"cost"));
								} else {
									sparecost.setText(costList.get(i).get(
											"cost"));

								}
							}
							String cos = consumcost.getText().toString();
							String lab = labourcost.getText().toString();
							String spar = sparecost.getText().toString();
							if (cos.equals("")) {
								cos = "0";
								consumcost.setText("0");
							}
							if (lab.equals("")) {
								lab = "0";
								labourcost.setText("0");
							}

							if (spar.equals("")) {
								spar = "0";
								sparecost.setText("0");
							}

							int total = Integer.parseInt(cos)
									+ Integer.parseInt(lab)
									+ Integer.parseInt(spar);
							totalcost.setText("" + total);
							view.getRootView().setFocusable(true);
							view.getRootView().requestFocus();
						}

						@Override
						public void onErrorReceive(String string) {
							
							view.getRootView().setFocusable(true);
							view.getRootView().requestFocus();
						}
					}).execute();
		} else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
