package com.ttl.customersocialapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.color;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.ComplaintArea;
import com.ttl.model.Complaint_Registered_Parent;
import com.ttl.model.Dealer;
import com.ttl.model.ServiceBookingUser;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
import com.ttl.webservice.WebServiceCall;

public class ComplaintRegistrationFragment extends Fragment {
	private Spinner spinner_c_area, spinner_c_subarea, spinner_c_problem,
			spinner_regNo, spinner_dealer;
	private EditText edit_dealerNo;
	private ArrayList<ComplaintArea> lst_c_area = new ArrayList<ComplaintArea>();
	private ArrayAdapter<String> adaptercities;
	private ArrayList<Dealer> dealers = new ArrayList<Dealer>();
	private Button btn_submit, btn_reset;
	private ArrayList<String> complaintSuccess=new ArrayList<>();
	private ServiceBookingUser user = new ServiceBookingUser();
	private EditText edit_varient, edit_email, edit_mobile, edit_complaintback,edit_resolution;
	private String DIVISIONNAME, CHASSISNO, contact_id, REGNO;
	private Complaint_Registered_Parent complreg = new Complaint_Registered_Parent();
	private List<String> regnovalues = new ArrayList<String>();
	private InstantAutoComplete spinner_city;
	private View v;
	private String rowid = "";
	private String[] lovs = { "Warranty", "Dealer Service Related", "Delivery","Others - Complaint", "Parts Related", "Product Related",
			"Value added services" };
	private HashMap<String, String> dealer = new HashMap<String, String>();
	private boolean connect;
	private Tracker mTracker;

	@Override
	public void onStart() {

		super.onStart();
		mTracker.setScreenName("ComplaintRegistrationScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// View v = inflater.inflate(R.layout.fragment_complaint_registration,
		// container, false);
		v = inflater.inflate(R.layout.fragment_complaintregistration,container, false);
		CheckConnectivity checknow = new CheckConnectivity();
		connect = checknow.checkNow(getActivity());
		if (new CheckConnectivity().checkNow(getActivity())) {
		} else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}
		AnalyticsApplication application = (AnalyticsApplication) getActivity()
				.getApplication();
		mTracker = application.getDefaultTracker();
		spinner_c_area = (Spinner) v.findViewById(R.id.spinner_c_area);
		spinner_c_subarea = (Spinner) v.findViewById(R.id.spinner_c_subarea);
		spinner_c_problem = (Spinner) v.findViewById(R.id.spinner_c_problem);
		spinner_regNo = (Spinner) v.findViewById(R.id.spinner_regNo);
		edit_varient = (EditText) v.findViewById(R.id.edit_varient);
		edit_email = (EditText) v.findViewById(R.id.edit_email);
		edit_mobile = (EditText) v.findViewById(R.id.edit_mobile);
		spinner_city = (InstantAutoComplete) v.findViewById(R.id.spinner_city);
		spinner_dealer = (Spinner) v.findViewById(R.id.spinner_dealer);
		edit_dealerNo = (EditText) v.findViewById(R.id.edit_dealerNo);
		btn_submit = (Button) v.findViewById(R.id.btn_submit);
		edit_complaintback = (EditText) v.findViewById(R.id.edit_complaintback);
		edit_resolution = (EditText) v.findViewById(R.id.edit_resolution);
		// new SamlArtifact(v.getContext()).execute();

		regnovalues.add("Select Vehicle");
		regnovalues.add("Not Available");
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
		ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, regnovalues);
		regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_regNo.setAdapter(regno);

		v.getRootView().setFocusableInTouchMode(true);
		v.getRootView().setFocusable(true);
		v.getRootView().requestFocus();

		// new WebServiceCall(getActivity(), req3,
		// Constants.GetCompPrblmAreabySubAreaCSB,
		// "Populating Compplain Area.").execute();
		spinner_c_problem
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						if (position != 0) {
							((TextView) parent.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.textcolor));

						} else {
							((TextView) parent.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.hintcolor));

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		
		if (new CheckConnectivity().checkNow(getActivity())) {
			String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<SOAP:Body>"
					+ "<GetCompTypeByBUCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
					+ "<SUB_TYPE>TMPC</SUB_TYPE>"
					+ "</GetCompTypeByBUCSB>"
					+ "</SOAP:Body>" + "</SOAP:Envelope>";
			Log.v("req", req + Config.getSAMLARTIFACT());
			new WebServiceCall(getActivity(), req,
					Constants.GetCompTypeByBUCSB, new ResponseCallback() {

						@SuppressWarnings("unchecked")
						@Override
						public void onResponseReceive(Object object) {

							lst_c_area = (ArrayList<ComplaintArea>) object;
							List<String> complainarea = new ArrayList<String>();
							complainarea.add("Complaint Area");

							for (int i = 0; i < lst_c_area.size(); i++) {
								Log.i("lst_c_area", lst_c_area.get(i).VAL + " "
										+ lst_c_area.get(i).ROW_ID);
								for (int j = 0; j < lovs.length; j++) {
									if (lst_c_area.get(i).VAL.equals(lovs[j])) {
										complainarea.add(lst_c_area.get(i).VAL);
									}
								}

							}
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									getActivity(),
									android.R.layout.simple_spinner_item,
									complainarea);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spinner_c_area.setAdapter(adapter);

							v.getRootView().setFocusable(true);
							v.getRootView().requestFocus();
						}

						@Override
						public void onErrorReceive(String string) {

							Toast.makeText(getActivity(), string,
									Toast.LENGTH_SHORT).show();
							v.getRootView().setFocusable(true);
							v.getRootView().requestFocus();
						}
					}, "Populating Complaint Area.").execute();

		} else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}

		spinner_regNo
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {

						String regNo = spinner_regNo.getSelectedItem()
								.toString();
						spinner_c_area.setSelection(0);
						spinner_c_subarea.setSelection(0);
						spinner_c_problem.setSelection(0);
						spinner_city.setText("");
						spinner_dealer.setSelection(0);
						edit_varient.setText("");
						edit_complaintback.setText("");
						edit_resolution.setText("");
						edit_mobile.setText("");
						edit_email.setText("");
						edit_dealerNo.setText("");
						// Log.i("Selected item : ", regNo + " AT " + pos);
						if (pos != 0) {
							// fire web service
							((TextView) arg0.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.textcolor));

							if (regNo.toString().equals("Not Available")) {
								edit_email.setText(UserDetails.getEmail_id()
										.toString());
								edit_mobile.setText(UserDetails
										.getContact_number().toString());
								contact_id = "";
							} else {
								String chassis = new UserDetails()
										.getRegNumberList().get(pos - 2)
										.get("chassis_num");

								if (new CheckConnectivity().checkNow(getActivity())) {
									String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
											+ "<SOAP:Body>"
											+ "<GetCustomerVehicleDetailsCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
											+ "<RegistrationNumber></RegistrationNumber>"
											+ "<ChassisNumber>"
											+ chassis
											+ "</ChassisNumber>"
											+ "<MobileNo></MobileNo>"
											+ "</GetCustomerVehicleDetailsCSB>"
											+ "</SOAP:Body>"
											+ "</SOAP:Envelope>";
		
									new WebServiceCall(
											getActivity(),
											req,
											Constants.GetCustomerVehicleDetailsCSB,
											new ResponseCallback() {

												@Override
												public void onResponseReceive(
														Object object) {

													user = (ServiceBookingUser) object;
		
													edit_varient
															.setText(user.PL);
													edit_email.setText(user
															.getEmail());
													edit_mobile.setText(user
															.getPhoneno());
													CHASSISNO = user
															.getCHASSISNUMBER();
													contact_id = user
															.getContact_id();
													REGNO = user
															.getREGISTRATIONNUMBER();
													v.getRootView()
															.setFocusable(true);
													v.getRootView()
															.requestFocus();
												}

												@Override
												public void onErrorReceive(
														String string) {

													Toast.makeText(
															getActivity(),
															string,
															Toast.LENGTH_SHORT)
															.show();
													v.getRootView()
															.setFocusable(true);
													v.getRootView()
															.requestFocus();
												}
											}, "Populating Data..").execute();
								} else {
									Toast toast = Toast.makeText(getActivity(),
											"No network connection available.",
											Toast.LENGTH_SHORT);
									toast.show();
								}
							}
						} else {
							((TextView) arg0.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.hintcolor));

						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		spinner_c_area.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				spinner_c_subarea.setSelection(0);
				spinner_c_problem.setSelection(0);
				spinner_city.setText("");
				spinner_dealer.setSelection(0);
				edit_complaintback.setText("");
				edit_resolution.setText("");

				edit_dealerNo.setText("");

				if (position != 0) {
					// String PAR_ROW_ID =
					// lst_c_area.get(position-1).getPAR_ROW_ID();

					for (int i = 0; i < lst_c_area.size(); i++) {
						// Log.i("lst_c_area", lst_c_area.get(i).VAL + " "+
						// lst_c_area.get(i).ROW_ID);

						if (lst_c_area.get(i).VAL.equals(parent
								.getSelectedItem().toString())) {
							rowid = lst_c_area.get(i).ROW_ID;
						
					
						
						}

					}
					((TextView) parent.getChildAt(0)).setTextColor(v
							.getContext().getResources()
							.getColor(R.color.textcolor));

					/*
					 * for (int i = 0; i < lst_c_area.size(); i++) {
					 * Log.i("lst_c_area", lst_c_area.get(i).VAL + " " +
					 * lst_c_area.get(i).PAR_ROW_ID + " " +
					 * lst_c_area.get(i).ROW_ID); }
					 */

		
					if (new CheckConnectivity().checkNow(getActivity())) {
						
						Log.e("row id value",""+rowid);
						
						String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
								+ "<SOAP:Body>"
								+ "<GetCompSubTypeByParRowIDCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
								+ "<PAR_ROW_ID>"
								+ rowid
								+ "</PAR_ROW_ID>"
								+ "<BU>TMPC</BU>"
								+ "</GetCompSubTypeByParRowIDCSB>"
								+ "</SOAP:Body>" + "</SOAP:Envelope>";
						new WebServiceCall(getActivity(), req1,
								Constants.GetCompSubTypeByParRowIDCSB,
								new ResponseCallback() {

									@Override
									public void onResponseReceive(Object object) {

										@SuppressWarnings("unchecked")
										ArrayList<String> list = (ArrayList<String>) object;
									
										
										for(int index=0;index<list.size();index++)
										{
											
											Log.e("sub type values",list.get(index));
											
										}
										
										
										ArrayAdapter<String> adapter = new ArrayAdapter<String>(
												getActivity(),
												android.R.layout.simple_spinner_item,
												list);
										adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
										spinner_c_subarea.setAdapter(adapter);
										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
									}

									@Override
									public void onErrorReceive(String string) {

										Toast.makeText(getActivity(), string,
												Toast.LENGTH_SHORT).show();
										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
									}

								}, "Populating Complaint SubArea.").execute();

						// Log.i("CMPL_AREAROW_ID",
						// lst_c_area.get(position-1).ROW_ID);
						String req2 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
								+ "<SOAP:Body>"
								+ "<GetCompCitiesByType xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
								+ "<CMPL_AREAROW_ID>"
								+ rowid
								+ "</CMPL_AREAROW_ID>"
								+ "</GetCompCitiesByType>"
								+ "</SOAP:Body>"
								+ "</SOAP:Envelope>";

						new WebServiceCall(getActivity(), req2,
								Constants.GetDSSDealerCitiesCSB,
								new ResponseCallback() {

									@Override
									public void onResponseReceive(Object object) {

										@SuppressWarnings("unchecked")
										ArrayList<String> list = (ArrayList<String>) object;
										Collections.sort(list,
												new Comparator<String>() {

													@Override
													public int compare(
															String lhs,
															String rhs) {

														return lhs
																.compareToIgnoreCase(rhs);
													}
												});

										adaptercities = new ArrayAdapter<String>(
												getActivity(),
												android.R.layout.simple_spinner_item,
												list);
										adaptercities
												.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

										spinner_city.setThreshold(1);
										// spinner_city.setValidator(new
										// Validator(list));
										spinner_city
												.setDropDownBackgroundResource(color.white);
										spinner_city.setAdapter(adaptercities);
										spinner_city
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {

														spinner_city
																.showDropDown();
													}
												});
										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
									}

									@Override
									public void onErrorReceive(String string) {

										Toast.makeText(getActivity(), string,
												Toast.LENGTH_SHORT).show();
										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
									}

								}, "Populating Cities.").execute();

					} else {
						Toast toast = Toast.makeText(getActivity(),
								"No network connection available.",
								Toast.LENGTH_SHORT);
						toast.show();
					}
				} else {
					((TextView) parent.getChildAt(0)).setTextColor(v
							.getContext().getResources()
							.getColor(R.color.hintcolor));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spinner_c_subarea
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						spinner_c_problem.setSelection(0);

						if (position != 0) {
							((TextView) parent.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.textcolor));
							if (new CheckConnectivity().checkNow(getActivity())) {
								String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
										+ "<SOAP:Body>"
										+ "<GetCompPrblmAreabySubAreaCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
										+ "<SUB_AREA>"
										+ spinner_c_subarea.getSelectedItem()
												.toString()
										+ "</SUB_AREA>"
										+ "</GetCompPrblmAreabySubAreaCSB>"
										+ "</SOAP:Body>" + "</SOAP:Envelope>";
								new WebServiceCall(getActivity(), req,
										Constants.GetCompPrblmAreabySubAreaCSB,
										new ResponseCallback() {

											@Override
											public void onResponseReceive(
													Object object) {
												@SuppressWarnings("unchecked")
												ArrayList<String> list = (ArrayList<String>) object;
												ArrayAdapter<String> adapter = new ArrayAdapter<String>(
														getActivity(),
														android.R.layout.simple_spinner_item,
														list);
												adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
												spinner_c_problem
														.setAdapter(adapter);

												v.getRootView().setFocusable(
														true);
												v.getRootView().requestFocus();
											}

											@Override
											public void onErrorReceive(
													String string) {

												Toast.makeText(getActivity(),
														string,
														Toast.LENGTH_SHORT)
														.show();
												v.getRootView().setFocusable(
														true);
												v.getRootView().requestFocus();
											}

										}, "Populating Complaint Problem Area.")
										.execute();
							} else {
								Toast toast = Toast.makeText(getActivity(),
										"No network connection available.",
										Toast.LENGTH_SHORT);
								toast.show();
							}
						} else {
							((TextView) parent.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.hintcolor));

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		spinner_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// if(position!=0){
				// ((TextView)
				// parent.getChildAt(0)).setTextColor(v.getContext().getResources().getColor(R.color.textcolor));
				// spinner_city.performValidation();
				if (new CheckConnectivity().checkNow(getActivity())) {
					/*
					 * String req =
					 * "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					 * + "<SOAP:Body>" +
					 * "<GetDlrsByCityAndTypeCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
					 * + "<CITY>" +
					 * lst_c_area.get(position).ROW_ID+spinner_city.
					 * getText().toString() + "</CITY>" + "<BU>TMPC</BU>" +
					 * "</GetDlrsByCityAndTypeCSB>" + "</SOAP:Body>" +
					 * "</SOAP:Envelope>";
					 */
					String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							+ "<SOAP:Body>"
							+ "<GetDlrsByCityAndTypeCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
							+ "<CITY>"
							+ rowid
							+ spinner_city.getText().toString()
							+ "</CITY>"
							+ "<BU>TMPC</BU>"
							+ "</GetDlrsByCityAndTypeCSB>"
							+ "</SOAP:Body>" + "</SOAP:Envelope>";
					new WebServiceCall(getActivity(), req,
							Constants.GetDlrsByCityAndTypeCSB,
							new ResponseCallback() {

								@SuppressWarnings("unchecked")
								@Override
								public void onResponseReceive(Object object) {

									dealers = (ArrayList<Dealer>) object;
									List<String> commonname = new ArrayList<String>();
									commonname.add("Dealer");
									for (int i = 0; i < dealers.size(); i++) {
										Log.i("dealers",
												dealers.get(i).commonname
														+ " "
														+ dealers.get(i).divisionId);
										commonname.add(dealers.get(i).commonname);
									}
									ArrayAdapter<String> adapter = new ArrayAdapter<String>(
											getActivity(),
											android.R.layout.simple_spinner_item,
											commonname);
									adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spinner_dealer.setAdapter(adapter);
									v.getRootView().setFocusable(true);
									v.getRootView().requestFocus();
								}

								@Override
								public void onErrorReceive(String string) {

									Toast.makeText(getActivity(), string,
											Toast.LENGTH_SHORT).show();
									v.getRootView().setFocusable(true);
									v.getRootView().requestFocus();
								}
							}, "Populating Dealers..").execute();
				} else {
					Toast toast = Toast.makeText(getActivity(),
							"No network connection available.",
							Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});

		spinner_dealer.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (position != 0) {
					((TextView) parent.getChildAt(0)).setTextColor(v
							.getContext().getResources()
							.getColor(R.color.textcolor));


					String DIVISIONID = dealers.get(position - 1)
							.getDivisionId();
					DIVISIONNAME = dealers.get(position - 1).getDivisionName();
					if (new CheckConnectivity().checkNow(getActivity())) {
						String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
								+ "<SOAP:Body>"
								+ "<GetdivphonebydivCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
								+ "<DIVISIONID>"
								+ DIVISIONID
								+ "</DIVISIONID>"
								+ "</GetdivphonebydivCSB>"
								+ "</SOAP:Body>"
								+ "</SOAP:Envelope>";

						String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
								+ "<SOAP:Body>"
								+ "<GetParUsrPostnByPostTypeAndDivID_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
								+ "<POSITIONTYPE>DSvCRO</POSITIONTYPE>"
								+ "<DIVISIONID>"
								+ DIVISIONID
								+ "</DIVISIONID>"
								+ "</GetParUsrPostnByPostTypeAndDivID_CSB>"
								+ "</SOAP:Body>" + "</SOAP:Envelope>";
						new WebServiceCall(getActivity(), req1,
								Constants.GetParUsrPostnByPostTypeAndDivID_CSB,
								new ResponseCallback() {

									@Override
									public void onResponseReceive(Object object) {

										if (TextUtils.isEmpty(dealer
												.get("mobile")))
											edit_dealerNo
													.setText("Not available");
										else
											edit_dealerNo.setText(dealer
													.get("mobile"));

										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
									}

									@Override
									public void onErrorReceive(String string) {

										edit_dealerNo.setText("Not available");
										// Toast.makeText(getActivity(), string,
										// Toast.LENGTH_SHORT).show();
										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
									}
								}, "Getting dealer contatct number.").execute();
					} else {
						Toast toast = Toast.makeText(getActivity(),
								"No network connection available.",
								Toast.LENGTH_SHORT);
						toast.show();
					}

				} else {
					((TextView) parent.getChildAt(0)).setTextColor(v
							.getContext().getResources()
							.getColor(R.color.hintcolor));

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btn_reset = (Button) v.findViewById(R.id.btn_reset);

		btn_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				spinner_regNo.setSelection(0);
				spinner_c_area.setSelection(0);
				spinner_c_subarea.setSelection(0);
				spinner_c_problem.setSelection(0);
				spinner_city.setText("");
				spinner_dealer.setSelection(0);
				edit_varient.setText("");
				edit_complaintback.setText("");
				edit_resolution.setText("");
				edit_mobile.setText("");
				edit_email.setText("");
				edit_dealerNo.setText("");
			}
		});
		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v1) {

				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				String formattedDate = df.format(c.getTime());
				ArrayList<String> emptyFields = new ArrayList<>();
				if (spinner_regNo.getSelectedItemPosition() == 0)
					emptyFields.add("Select Vehicle");
				if (spinner_c_area.getSelectedItemPosition() == 0)
					emptyFields.add("Complaint Area");
				if (spinner_c_subarea.getSelectedItemPosition() == 0)
					emptyFields.add("Complaint Sub Area");
				if (spinner_c_problem.getSelectedItemPosition() == 0)
					emptyFields.add("Problem Area");
				if (TextUtils.isEmpty(edit_complaintback.getText().toString()))
					emptyFields.add("Complaint Background");
				if (TextUtils.isEmpty(edit_resolution.getText().toString()))
					emptyFields.add("Resolution Required");
				if (edit_email.getText().toString().equals("")) {
					emptyFields.add("Email Id");
				} else if (!(Config.isEmailValid(edit_email.getText()
						.toString()))) {
					/*
					 * edit_email.setError("Email id should not blank.");
					 * edit_email.setFocusable(true);
					 */
					emptyFields.add("Valid Email Id");
				}
				if (edit_mobile.getText().toString().equals("")) {
					/*
					 * edit_email.setError("Email id should not blank.");
					 * edit_email.setFocusable(true);
					 */
					emptyFields.add("Mobile Number");
				} else if (edit_mobile.getText().toString().trim().length() != 10) {
					emptyFields.add("10 digits Contact Number.");
				}
				if (spinner_city.getText().equals(""))
					emptyFields.add("City");
				if (spinner_dealer.getSelectedItemPosition() == 0)
					emptyFields.add("Dealer");

				if (emptyFields.size() == 0) {

					if (DIVISIONNAME.contains("&")) {
						DIVISIONNAME = DIVISIONNAME.replace("&", "&amp;");
					}
					String reg;
					if (spinner_regNo.getSelectedItem().toString()
							.equals("Not Available")) {
						reg = "<TMChassisRegNo>NA</TMChassisRegNo>";
					} else {
						if (REGNO.equals("")) {
							REGNO = "NA";
						}
						reg = "<TMChassisRegNo>" + REGNO + "</TMChassisRegNo>"
								+ "<SerialNumber>" + CHASSISNO
								+ "</SerialNumber>";
					}
					// KANCHAN REQ
					if (new CheckConnectivity().checkNow(getActivity())) {
						String ownerid = "";
						if (dealer.get("login") != null) {
							ownerid = dealer.get("login");
						}
						String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
								+ "<SOAP:Body>"
								+ "<TMPCComplaintsInsertOrUpdate_Input xmlns=\"http://siebel.com/asi/\">"
								+ "<ListOfTmCimsInterface xmlns=\"http://www.siebel.com/xml/TM%20CIMS%20Interface\">"
								+ "<TmCims>" + "<IntegrationId>"
								+ System.currentTimeMillis()
								+ "</IntegrationId>"
								+ "<Area>"
								+ spinner_c_area.getSelectedItem().toString()
								+ "</Area>"
								+ "<ContactId>"
								+ contact_id
								+ "</ContactId>"
								+ "<Description/>"
								+ "<Owner>"
								+ ownerid
								+ "</Owner>"
								+ "<PrimaryOrganizationId></PrimaryOrganizationId>"
								+ "<ServiceRequestType>Complaint</ServiceRequestType>"
								+ "<Source>Mobile</Source>"
								+ "<Status>Open</Status>"
								+ "<SubArea>"
								+ spinner_c_subarea.getSelectedItem()
								.toString()
								+ "</SubArea>"
								+ "<SubStatus>Case ref to dealer for action</SubStatus>"
								+ "<TMBusinessUnit>TMPC</TMBusinessUnit>"
								+ reg
								+ "<TMComplaintDateTime>"
								+ formattedDate
								+ "</TMComplaintDateTime>"
								+ "<TMComplaintType>Non-Legal</TMComplaintType>"
								+ "<TMMode>Customer App</TMMode>"
								+ "<TMOriginPoint>GM</TMOriginPoint>"
								+ "<TMProblemArea>"
								+ spinner_c_problem.getSelectedItem()
								.toString()
								+ "</TMProblemArea>"
								+ "<Description>Complaint Background:"
								+ edit_complaintback.getText().toString()
								+ " "
								+ "Resolution Required:"
								+ edit_resolution.getText().toString()
								+ " "
								+ edit_email.getText()
								+ " "
								+ edit_mobile.getText()
								+ "</Description>"
								+ "<TMDealer>"
								+ DIVISIONNAME
								+ "</TMDealer>"
								+ "</TmCims>"
								+ "</ListOfTmCimsInterface>"
								+ "<StatusObject></StatusObject>"
								+ "</TMPCComplaintsInsertOrUpdate_Input>"
								+ "</SOAP:Body>" + "</SOAP:Envelope>";

						
						Log.v("final request", req);
						
						Log.v("insert complaint request", "insert complaint request reached");
						
						
						new WebServiceCall(getActivity(), req,
								Constants.TMPCComplaintsInsertOrUpdate_Input,
								new ResponseCallback() {

									@Override
									public void onResponseReceive(Object object) {

										complaintSuccess = (ArrayList<String>) object;
										mTracker.send(new HitBuilders.EventBuilder()
												.setCategory(
														UserDetails.getUser_id())
												.setAction("thread_true")
												.setLabel(
														"ComplaintRegistration")
												.build());

										AlertDialog.Builder builder1 = new AlertDialog.Builder(
												getActivity());

										builder1.setTitle("Complaint Registration");
										builder1.setMessage("Complaint Registered"
												+ "\n"
												+ "CR No: "
												+ complaintSuccess.get(1));
										builder1.setCancelable(false);
										builder1.setPositiveButton(
												"OK",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														dialog.cancel();
														// navigate
														FragmentManager fragmentManager = getFragmentManager();
														Fragment fragment = new HomeFragment();
														fragmentManager
																.beginTransaction()
																.replace(
																		R.id.frame_container,
																		fragment)
																.addToBackStack(
																		null)
																.commit();
													}
												});

										AlertDialog alert11 = builder1.create();
										alert11.show();

										// Calendar.getInstance().getTime();

										// STORE OFFLINE
										// DatabaseHandler db = new
										// DatabaseHandler(getActivity());
										complreg.setUserId(UserDetails
												.getUser_id());
										complreg.setComplaint_no(complaintSuccess
												.get(1));
										complreg.setComplaint_reg_no(user.REGISTRATIONNUMBER);

										SimpleDateFormat dateFormat = new SimpleDateFormat(
												"dd-MMM-yyyy", Locale.US);
										Date date = new Date();
										System.out.println(dateFormat
												.format(date));
										complreg.setComplaint_date(dateFormat
												.format(date));
										complreg.setModel(user.PPL);
										complreg.setPrimary_area(spinner_c_area
												.getSelectedItem().toString());
										complreg.setSub_area(spinner_c_subarea
												.getSelectedItem().toString());
										complreg.setProblem_area(spinner_c_problem
												.getSelectedItem().toString());

										String log = "USERID: "
												+ UserDetails.getUser_id()
												+ " SR: "
												+ complreg.getComplaint_no()
												+ " ,REG: "
												+ complreg
														.getComplaint_reg_no()
												+ " ,DATE: "
												+ dateFormat.format(date);

										// db.addComplaint(complreg);

										// Save Complaint registration history
										// on Cloud.
											String req = Config.awsserverurl
												+ "tmsc_ch/customerapp/vehicleServices/insertComplaintHistory";
										List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
												9);

										nameValuePairs
												.add(new BasicNameValuePair(
														"user_id", UserDetails
																.getUser_id()));
										nameValuePairs.add(new BasicNameValuePair(
												"registration_number", complreg
														.getComplaint_reg_no()));
										nameValuePairs.add(new BasicNameValuePair(
												"complaint_number", complreg
														.getComplaint_no()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"model", complreg
																.getModel()));
										nameValuePairs.add(new BasicNameValuePair(
												"complaint_date", complreg
														.getComplaint_date()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"primary_complaint_area",
														complreg.getPrimary_area()));
										nameValuePairs.add(new BasicNameValuePair(
												"sub_area", complreg
														.getSub_area()));
										nameValuePairs.add(new BasicNameValuePair(
												"problem_area", complreg
														.getProblem_area()));
										nameValuePairs.add(new BasicNameValuePair(
												"sessionId", UserDetails
														.getSeeionId()));
										new AWS_WebServiceCall(
												getActivity(),
												req,
												ServiceHandler.POST,
												Constants.insertComplaintHistory,
												nameValuePairs,
												new ResponseCallback() {

													@Override
													public void onResponseReceive(
															Object object) {

										
														v.getRootView()
																.setFocusable(
																		true);
														v.getRootView()
																.requestFocus();
													}

													@Override
													public void onErrorReceive(
															String string) {

										
														v.getRootView()
																.setFocusable(
																		true);
														v.getRootView()
																.requestFocus();
													}
												}).execute();

										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
							
									}

									@Override
									public void onErrorReceive(String string) {

										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
										mTracker.send(new HitBuilders.EventBuilder()
												.setCategory(
														UserDetails
																.getUser_id())
												.setAction("thread_false")
												.setLabel(
														"ComplaintRegistration")
												.build());
										Toast toast = Toast
												.makeText(
														getActivity(),
														"Complaint not registered. Please try again.",
														Toast.LENGTH_SHORT);
										toast.show();
							
									}
							
						
						}, "Please Wait..").execute();  
					
					} else {
						Toast toast = Toast.makeText(getActivity(),
								"No network connection available.",
								Toast.LENGTH_SHORT);
						toast.show();
					}
				} else {
					
					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < emptyFields.size(); j++) {
						sb.append(emptyFields.get(j) + "\n");
					}

					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							getActivity());
					builder1.setTitle("Following fields are missing:");
					builder1.setMessage(sb);
					// TextView messageText =
					// (TextView)findViewById(android.R.id.message);
					// messageText.setGravity(Gravity.CENTER);
					builder1.setCancelable(true);
					builder1.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

					AlertDialog alert11 = builder1.create();
					alert11.show();

				}

			}
		});

		v.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				v.setFocusable(true);
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

		return v;
	}

}