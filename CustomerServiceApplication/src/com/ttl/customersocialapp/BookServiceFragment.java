package com.ttl.customersocialapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.color;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.helper.Validator;
import com.ttl.model.Dealer;
import com.ttl.model.ServiceBookingUser;
import com.ttl.model.ServiceComplaintDesc;
import com.ttl.model.ServiceHandler;
import com.ttl.model.Service_Booking_History_Parent;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
import com.ttl.webservice.WebServiceCall;

public class BookServiceFragment extends Fragment {

	private Spinner spinner_regNo;
	private Spinner spinner_dealer, spinner_servicetype;
	InstantAutoComplete spinner_city;
	private EditText edit_model, edit_varient, edit_email, edit_mobile,
			edit_fname, edit_lname, edit_servicetime;
	private EditText edit_servicedate, edit_dealerNo;
	private CheckBox checkbox_van;
	private ArrayList<Dealer> dealers = new ArrayList<Dealer>();

	private int year, month, day;
	private ArrayAdapter<String> adaptercities;
	private ServiceBookingUser user = new ServiceBookingUser();

	private LinearLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7, ll8;
	private TableLayout table_layout;
	private boolean firstclick = true;
	private Spinner spinner_complainarea, spinner_complaindescr;
	private EditText edit_voice, edit_currentkm;
	private Button btn_addComplain, btn_bookService, btn_reset;

	private ArrayAdapter<String> adapteraea;
	private ArrayList<ServiceComplaintDesc> lst_c_desc = new ArrayList<ServiceComplaintDesc>();
	private int tag = 0;
	private ImageView del;
	private String DIVISIONID;
	private ImageView img_arrow, img_arrow1, img_arrow2, img_arrow3;
	private ImageView left, right;
	private HorizontalScrollView scrollView;
	private ArrayList<String[]> serv_compl = new ArrayList<>();
	static boolean chekboxcheck = false;

	private String PRI_EMP_ID = null, ORG_NAME = null;
	private String BOOKINGDATE, BOOKINGTIME, historyDate, historyTime;

	private Service_Booking_History_Parent s_parent = new Service_Booking_History_Parent();

	private String tick;
	private List<String> regnovalues = new ArrayList<String>();
	private View v;
	private TextView link_costest;

	private boolean connect;
	private Tracker mTracker;

	@Override
	public void onStart() {

		super.onStart();
		mTracker.setScreenName("ServiceBookingScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_servcebooking, container, false);
		spinner_regNo = (Spinner) v.findViewById(R.id.spinner_regNo);
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
		AnalyticsApplication application = (AnalyticsApplication) getActivity()
				.getApplication();
		mTracker = application.getDefaultTracker();
		ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, regnovalues);
		regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_regNo.setAdapter(regno);
		if (new UserDetails().getRegNumberList().size() == 0) {
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();
		}

		CheckConnectivity checknow = new CheckConnectivity();
		connect = checknow.checkNow(getActivity());
		spinner_city = (InstantAutoComplete) v.findViewById(R.id.spinner_city);
		spinner_dealer = (Spinner) v.findViewById(R.id.spinner_dealer);
		edit_model = (EditText) v.findViewById(R.id.edit_model);
		edit_varient = (EditText) v.findViewById(R.id.edit_varient);
		edit_email = (EditText) v.findViewById(R.id.edit_email);
		edit_mobile = (EditText) v.findViewById(R.id.edit_mobile);
		edit_fname = (EditText) v.findViewById(R.id.edit_fname);
		edit_lname = (EditText) v.findViewById(R.id.edit_lname);
		edit_currentkm = (EditText) v.findViewById(R.id.edit_currentkm);
		img_arrow = (ImageView) v.findViewById(R.id.img_arrow);
		img_arrow1 = (ImageView) v.findViewById(R.id.img_arrow1);
		img_arrow2 = (ImageView) v.findViewById(R.id.img_arrow2);
		img_arrow3 = (ImageView) v.findViewById(R.id.img_arrow3);
		left = (ImageView) v.findViewById(R.id.left);
		right = (ImageView) v.findViewById(R.id.right);
		scrollView = (HorizontalScrollView) v
				.findViewById(R.id.horizontalScroll);
		spinner_servicetype = (Spinner) v
				.findViewById(R.id.spinner_servicetype);
		checkbox_van = (CheckBox) v.findViewById(R.id.checkbox_van);
		edit_servicedate = (EditText) v.findViewById(R.id.edit_servicedate);
		edit_servicetime = (EditText) v.findViewById(R.id.edit_servicetime);
		// btn_reminder = (Button) v.findViewById(R.id.btn_reminder);
		edit_dealerNo = (EditText) v.findViewById(R.id.edit_dealerNo);
		// check_agree = (CheckBox) v.findViewById(R.id.check_agree);
		// check_agree.setPaintFlags(check_agree.getPaintFlags() |
		// Paint.UNDERLINE_TEXT_FLAG);
		// COMPLAINT
		link_costest = (TextView) v.findViewById(R.id.link_costest);
		link_costest.setPaintFlags(link_costest.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
		link_costest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				FragmentManager fragmentManager = getFragmentManager();
				Fragment fragment = new Cost_EstimationFragment();
				Bundle b = new Bundle();
				b.putString("Fragment", "BookServiceFragment");
				fragment.setArguments(b);
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment)
						.addToBackStack(null).commit();
			}
		});
		ll1 = (LinearLayout) v.findViewById(R.id.ll1);
		ll2 = (LinearLayout) v.findViewById(R.id.ll2);
		ll3 = (LinearLayout) v.findViewById(R.id.ll3);
		ll4 = (LinearLayout) v.findViewById(R.id.ll4);
		ll5 = (LinearLayout) v.findViewById(R.id.ll5);
		ll6 = (LinearLayout) v.findViewById(R.id.ll6);
		ll7 = (LinearLayout) v.findViewById(R.id.ll7);
		ll8 = (LinearLayout) v.findViewById(R.id.ll8);
		// float i = getResources().getDisplayMetrics().density;
		// Toast.makeText(getActivity(), i+"", Toast.LENGTH_SHORT).show();
		ll2.setVisibility(View.GONE);
		ll4.setVisibility(View.GONE);
		ll6.setVisibility(View.GONE);
		ll8.setVisibility(View.GONE);

		spinner_servicetype
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

		// db = new DatabaseHandler(getActivity());
		/*
		 * if(Config.getSAMLARTIFACT().equals("")) { new
		 * SamlArtifact(getActivity()).execute(); }
		 */
		// SERVICE

		if (new CheckConnectivity().checkNow(getActivity())) {
			String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<SOAP:Body>"
					+ "<GetCitiesforServiceOrderCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\" />"
					+ "</SOAP:Body>" + "</SOAP:Envelope>";

			Log.v("inside if condition", "Testsoap executed");

			new WebServiceCall(getActivity(), req,
					Constants.GetDSSDealerCitiesCSB, new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {

							@SuppressWarnings("unchecked")
							ArrayList<String> list = (ArrayList<String>) object;
							// adaptercities = new
							// ArrayAdapter<String>(getActivity(),R.layout.spinner_text,
							// list);
							// adaptercities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							// spinner_city.setAdapter(adaptercities);

							adaptercities = new ArrayAdapter<String>(
									getActivity(),
									android.R.layout.simple_spinner_item, list);
							adaptercities
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							// spinState.setAdapter(aa);
							spinner_city.setThreshold(1);
							spinner_city.setValidator(new Validator(list));
							// spinnerstate.setDropDownBackgroundResource(R.color.spinback);
							spinner_city
									.setDropDownBackgroundResource(color.white);
							spinner_city.setAdapter(adaptercities);
							spinner_city
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											spinner_city.showDropDown();
											spinner_city.requestFocus();
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

					}, "Populating Cities..").execute();
		} else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}

		// COMPLAINTS
		table_layout = (TableLayout) v.findViewById(R.id.tableLayout1);
		spinner_complainarea = (Spinner) v
				.findViewById(R.id.spinner_complainarea);
		spinner_complaindescr = (Spinner) v
				.findViewById(R.id.spinner_complaindesc);
		btn_addComplain = (Button) v.findViewById(R.id.btn_addComplain);
		edit_voice = (EditText) v.findViewById(R.id.edit_custVoice);
		btn_bookService = (Button) v.findViewById(R.id.btn_boookService);
		table_layout.removeAllViews();

		spinner_complaindescr
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
			String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<SOAP:Body>"
					+ "<GetComplaintAreaCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
					+ "<SUB_TYPE>TMPC</SUB_TYPE>"
					+ "</GetComplaintAreaCSB>"
					+ "</SOAP:Body>" + "</SOAP:Envelope>";

			new WebServiceCall(getActivity(), req1,
					Constants.GetComplaintAreaCSB, new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							@SuppressWarnings("unchecked")
							ArrayList<String> list = (ArrayList<String>) object;
							adapteraea = new ArrayAdapter<String>(
									getActivity(), R.layout.spinner_text, list);
							adapteraea
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spinner_complainarea.setAdapter(adapteraea);
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
					}, "Populating Complaint Area..").execute();
		} else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}

		// SERVICE

		checkbox_van.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				String req = null;
				if (checkbox_van.isChecked()) {
					spinner_city.setText("");
					spinner_dealer.setSelection(0);
					edit_dealerNo.setText("");
					edit_servicedate.setText("");
					edit_servicetime.setText("");
					spinner_servicetype.setSelection(0);
					edit_currentkm.setText("");
					req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							+ "<SOAP:Body>"
							+ "<GetDSSDealerCitiesCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\" />"
							+ "</SOAP:Body>" + "</SOAP:Envelope>";

					chekboxcheck = true;
				} else {
					spinner_city.setText("");
					spinner_dealer.setSelection(0);
					edit_dealerNo.setText("");
					edit_servicedate.setText("");
					spinner_servicetype.setSelection(0);
					edit_servicetime.setText("");
					edit_currentkm.setText("");
					req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							+ "<SOAP:Body>"
							+ "<GetCitiesforServiceOrderCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\" />"
							+ "</SOAP:Body>" + "</SOAP:Envelope>";

					chekboxcheck = false;
				}
				if (new CheckConnectivity().checkNow(getActivity())) {

					new WebServiceCall(getActivity(), req,
							Constants.GetDSSDealerCitiesCSB,
							new ResponseCallback() {

								@Override
								public void onResponseReceive(Object object) {

									@SuppressWarnings("unchecked")
									ArrayList<String> list = (ArrayList<String>) object;
									adaptercities = new ArrayAdapter<String>(
											getActivity(),
											android.R.layout.simple_spinner_item,
											list);
									adaptercities
											.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spinner_city.setAdapter(adaptercities);
									spinner_city.setValidator(new Validator(
											list));
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

							}, "Populating Cities..").execute();
				} else {
					Toast toast = Toast.makeText(getActivity(),
							"No network connection available.",
							Toast.LENGTH_SHORT);
					toast.show();
				}

			}

		});

		spinner_regNo
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {

						if (pos != 0) {
							// fire web service
							edit_model.setText("");
							edit_varient.setText("");

							edit_email.setText("");
							edit_fname.setText("");
							edit_lname.setText("");
							edit_mobile.setText("");
							((TextView) arg0.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.textcolor));
							String chassis = new UserDetails()
									.getRegNumberList().get(pos - 1)
									.get("chassis_num");
							if (new CheckConnectivity().checkNow(getActivity())) {
								String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
										+ "<SOAP:Body>"
										+ "<GetCustomerVehicleDetailsCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
										+ "<RegistrationNumber></RegistrationNumber>"
										+ "<ChassisNumber>"
										+ chassis
										+ "</ChassisNumber><MobileNo></MobileNo>"
										+ "</GetCustomerVehicleDetailsCSB>"
										+ "</SOAP:Body>" + "</SOAP:Envelope>";

								new WebServiceCall(getActivity(), req,
										Constants.GetCustomerVehicleDetailsCSB,
										new ResponseCallback() {

											@Override
											public void onResponseReceive(
													Object object) {

												user = (ServiceBookingUser) object;
												// Log.i("onResponseRecieve",
												// user.PL + " " + user.PPL +
												// " "+user.email +
												// "REG"+user.REGISTRATIONNUMBER);
												edit_model.setText(user.PPL);
												edit_varient.setText(user.PL);

												edit_email.setText(user
														.getEmail());
												edit_fname.setText(user
														.getFname());
												edit_lname.setText(user
														.getLname());
												edit_mobile.setText(user
														.getPhoneno());
												if (user.REGISTRATIONNUMBER
														.equals("")) {
													user.REGISTRATIONNUMBER = "NA";
												}
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
										}, "Populating Data..").execute();

							} else {
								Toast toast = Toast.makeText(getActivity(),
										"No network connection available.",
										Toast.LENGTH_SHORT);
								toast.show();
							}
						} else {
							((TextView) arg0.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.hintcolor));
							edit_model.setText("");
							edit_varient.setText("");

							edit_email.setText("");
							edit_fname.setText("");
							edit_lname.setText("");
							edit_mobile.setText("");
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});
		spinner_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// spinner_city.performValidation();
				spinner_dealer.setSelection(0);
				edit_dealerNo.setText("");
				edit_servicedate.setText("");
				edit_servicetime.setText("");
				spinner_servicetype.setSelection(0);

				edit_currentkm.setText("");
				String city = parent.getItemAtPosition(position).toString();
				/*
				 * String req =
				 * "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				 * + "<SOAP:Body>" +
				 * "<GetDlrsByCityAndTypeCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
				 * + "<CITY>" + city+"</CITY>" + "<BU>TMPC</BU>" +
				 * "</GetDlrsByCityAndTypeCSB>" + "</SOAP:Body>" +
				 * "</SOAP:Envelope>";
				 */
				String req;
				if (chekboxcheck) {
					req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							+ "<SOAP:Body>"
							+ "<GetDSSDealersByCity_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
							+ "<CITY>"
							+ city
							+ "</CITY>"
							+ "</GetDSSDealersByCity_CSB>"
							+ "</SOAP:Body>"
							+ "</SOAP:Envelope>";
				} else {

					req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							+ "<SOAP:Body>"
							+ "<GetOrgbyCityCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
							+ "<CITY>"
							+ city
							+ "</CITY>"
							+ "</GetOrgbyCityCSB>"
							+ "</SOAP:Body>"
							+ "</SOAP:Envelope>";

				}
				if (new CheckConnectivity().checkNow(getActivity())) {
					new WebServiceCall(getActivity(), req,
							Constants.GetDlrsByCityAndTypeCSB,
							new ResponseCallback() {

								@SuppressWarnings("unchecked")
								@Override
								public void onResponseReceive(Object object) {

									dealers = (ArrayList<Dealer>) object;
									List<String> commonname = new ArrayList<String>();
									commonname.add("Select Dealer");
									for (int i = 0; i < dealers.size(); i++) {
										// Log.i("dealers",
										// dealers.get(i).commonname + " "+
										// dealers.get(i).divisionId);
										commonname.add(dealers.get(i).commonname);
									}
									ArrayAdapter<String> adapter = new ArrayAdapter<String>(
											getActivity(),
											R.layout.spinner_text, commonname);
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

				edit_dealerNo.setText("");
				edit_servicedate.setText("");
				edit_servicetime.setText("");
				spinner_servicetype.setSelection(0);
				edit_currentkm.setText("");
				if (position != 0) {
					((TextView) parent.getChildAt(0)).setTextColor(v
							.getContext().getResources()
							.getColor(R.color.textcolor));

					// Log.i("DIVISIONID",
					// dealers.get(position-1).getDivisionId() + " at " +
					// (position-1));
					DIVISIONID = dealers.get(position - 1).getDivisionId();
					/*
					 * orgname = dealers.get(position-1).getDivisionName();
					 * orgname = orgname.replaceAll("&", "&amp;");
					 */
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
								+ "<GetPositionbyPositionAndOUID_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
								+ "<POSTN_TYPE_CD>DSvCRO</POSTN_TYPE_CD>"
								+ "<OU_ID>"
								+ DIVISIONID
								+ "</OU_ID>"
								+ "</GetPositionbyPositionAndOUID_CSB>"
								+ "</SOAP:Body>" + "</SOAP:Envelope>";

						// new AsyncServiceBooking(getActivity(), req,
						// Constants.GetdivphonebydivCSB,
						// "Getting dealer contatct number.").execute();

						new WebServiceCall(getActivity(), req,
								Constants.GetdivphonebydivCSB,
								new ResponseCallback() {

									@Override
									public void onResponseReceive(Object object) {

										String dealercntctNum = (String) object;
										if (TextUtils.isEmpty(dealercntctNum))
											edit_dealerNo
													.setText("Not available");
										else
											edit_dealerNo
													.setText(dealercntctNum);

										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
									}

									@Override
									public void onErrorReceive(String string) {

										edit_dealerNo.setText("Not Available");
										// Toast.makeText(getActivity(), string,
										// Toast.LENGTH_SHORT).show();
										v.getRootView().setFocusable(true);
										v.getRootView().requestFocus();
									}
								}, "Getting dealer contact number.").execute();

						/*new WebServiceCall(getActivity(), req1,
								Constants.GetPositionbyPositionAndOUID_CSB,
								new ResponseCallback() {

							

									@Override
									public void onErrorReceive(String string) {
										Toast.makeText(getActivity(), string,
												Toast.LENGTH_SHORT).show();

									
									}

									@Override
									public void onResponseReceive(Object object) {
										
										
										
										
									}
								}, "Getting dealer contact number.").execute();

*/						
						
						new WebServiceCall(getActivity(), req1, Constants.GetPositionbyPositionAndOUID_CSB, new ResponseCallback() {
							
							@Override
							public void onResponseReceive(Object object) {
								HashMap<String, String>  response = (HashMap<String, String>) object;
								PRI_EMP_ID = response.get("PRI_EMP_ID");	
								ORG_NAME =  response.get("ORG_NAME");	
								
								
							}
							
							@Override
							public void onErrorReceive(String string) {
								Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
								
							}
						} ,"Getting dealer contact number.").execute();
						
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

		edit_servicedate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDatePickerDialog(v);
			}
		});

		edit_servicetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showTimePickerDialog(v);
			}
		});

		left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				scrollView.scrollTo((int) scrollView.getScrollX() - 150,
						(int) scrollView.getScrollY());
				changeArrowImages();
			}
		});
		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				scrollView.scrollTo((int) scrollView.getScrollX() + 150,
						(int) scrollView.getScrollY());
				changeArrowImages();
			}
		});

		// COMPLAINTS
		btn_addComplain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Log.i("POSITIONS",
				// spinner_complainarea.getSelectedItemPosition() +
				// " "+spinner_complaindescr.getSelectedItemPosition()) ;

				if (spinner_complainarea.getSelectedItemPosition() == 0) {
					Toast.makeText(getActivity(), "Select complaint Area",
							Toast.LENGTH_SHORT).show();
				}
				if (spinner_complaindescr.getSelectedItemPosition() == 0) {
					Toast.makeText(getActivity(),
							"Select complaint Description", Toast.LENGTH_SHORT)
							.show();
				} else {
					// ArrayList<String>complaints = new ArrayList<String>();

					String area = spinner_complainarea.getSelectedItem()
							.toString();
					String desc = spinner_complaindescr.getSelectedItem()
							.toString();
					String code = lst_c_desc
							.get(spinner_complaindescr
									.getSelectedItemPosition() - 1)
							.getDEFECT_NUM();
					String voice = edit_voice.getText().toString();
					String[] complainttext = new String[] { "Complaint\nArea",
							"Complaint\nDescription", "Complaint\nCode",
							"Customer\nVoice" };
					String[] complaints = new String[] { area, desc, code,
							voice };
					serv_compl.add(complaints);
					BuildTable(complainttext, complaints);
					spinner_complainarea.setSelection(0);
					spinner_complaindescr.setSelection(0);
					edit_voice.setText("");
				}

			}

		});

		spinner_complainarea
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {

						String area = spinner_complainarea.getSelectedItem()
								.toString();
						if (area.equals("MUV&CARS")) {
							area = "MUV&amp;CARS";
						}
						spinner_complaindescr.setSelection(0);
						edit_voice.setText("");
						// Log.i("Selected item : ", area + " AT " + pos);
						if (pos != 0) {
							// fire web service
							((TextView) arg0.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.textcolor));
							if (new CheckConnectivity().checkNow(getActivity())) {
								String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
										+ "<SOAP:Body>"
										+ "<GetComplaintDesc xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
										+ "<FAILCODETYPEBU>"
										+ area
										+ "</FAILCODETYPEBU>"
										+ "</GetComplaintDesc>"
										+ "</SOAP:Body>" + "</SOAP:Envelope>";

								new WebServiceCall(getActivity(), req,
										Constants.GetComplaintDesc,
										new ResponseCallback() {

											@SuppressWarnings("unchecked")
											@Override
											public void onResponseReceive(
													Object object) {

												lst_c_desc = (ArrayList<ServiceComplaintDesc>) object;
												List<String> c_desc_text = new ArrayList<String>();
												c_desc_text
														.add("Complaint Description");
												for (int i = 0; i < lst_c_desc
														.size(); i++) {
													Log.i("lst_c_desc",
															lst_c_desc.get(i).DESC_TEXT
																	+ " "
																	+ lst_c_desc
																			.get(i).DEFECT_NUM);
													c_desc_text.add(lst_c_desc
															.get(i).DESC_TEXT);
												}
												ArrayAdapter<String> adapter = new ArrayAdapter<String>(
														getActivity(),
														R.layout.spinner_text,
														c_desc_text);
												adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
												spinner_complaindescr
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
										}, "Populating complaint desc")
										.execute();
							} else {
								Toast toast = Toast.makeText(getActivity(),
										"No network connection available.",
										Toast.LENGTH_SHORT);
								toast.show();
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

		btn_reset = (Button) v.findViewById(R.id.btn_reset);

		btn_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				spinner_regNo.setSelection(0);
				edit_varient.setText("");
				edit_model.setText("");
				edit_email.setText("");
				edit_mobile.setText("");
				edit_fname.setText("");
				edit_lname.setText("");
				spinner_city.setText("");
				spinner_dealer.setSelection(0);
				edit_dealerNo.setText("");
				edit_servicedate.setText("");
				edit_servicetime.setText("");
				spinner_servicetype.setSelection(0);
				edit_currentkm.setText("");
				spinner_complainarea.setSelection(0);
				spinner_complaindescr.setSelection(0);
				edit_voice.setText("");

				// ll2.setVisibility(View.GONE);
				ll4.setVisibility(View.GONE);
				ll6.setVisibility(View.GONE);
				ll8.setVisibility(View.GONE);
				serv_compl.clear();
				table_layout.removeAllViews();
				left.setVisibility(View.GONE);
				right.setVisibility(View.GONE);

			}
		});
		btn_bookService.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				/*String descr = user.fname + " " + user.lname + " "
						+ edit_mobile.getText().toString() + " "
						+ edit_email.getText().toString();*/
				
				
				
				
				
				ArrayList<String> emptyFields = new ArrayList<>();
				// validations
				if (spinner_regNo.getSelectedItemPosition() == 0) {
					emptyFields.add("Select Vehicle");
				}
				if (spinner_city.getText().toString().equals(""))
					emptyFields.add("City");
				if (spinner_dealer.getSelectedItemPosition() == 0)
					emptyFields.add("Dealer");
				/*
				 * if(TextUtils.isEmpty(PRI_EMP_ID))
				 * emptyFields.add("Dealer EMP ID.Select Other dealer.");
				 */
				if (edit_servicedate.getText().toString().equals(""))
					emptyFields.add("Booked for Date");
				if (edit_servicetime.getText().toString().equals(""))
					emptyFields.add("Booked for Time");
				if (spinner_servicetype.getSelectedItemPosition() == 0)
					emptyFields.add("Service Type");
				if (TextUtils.isEmpty(edit_currentkm.getText().toString()))
					emptyFields.add("Current KM");
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
				}else if (edit_mobile.getText().toString().trim().length() != 10) {
					emptyFields.add("10 digits Contact Number.");
				}
				// if(spinner_complainarea.getSelectedItemPosition()==0)
				// emptyFields.add("Complaint Area");
				// if(spinner_complaindescr.getSelectedItemPosition()==0)
				// emptyFields.add("Complaint Description");

				if (emptyFields.size() == 0) {

					// area,desc,code,voice
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < serv_compl.size(); i++) {
						String compl = "<TmCustomerComplaint operation=\"\" searchspec=\"\">"
								+ "<Id>"
								+ (System.currentTimeMillis() + i)
								+ "</Id>"
								+ "<TMCustomerComplaintCode>"
								+ serv_compl.get(i)[2]
								+ "</TMCustomerComplaintCode>"
								+ "<TMCustomerComplaintDesc>"
								+ serv_compl.get(i)[1]
								+ "</TMCustomerComplaintDesc>"
								+ "<TMCustomerVoice>"
								+ serv_compl.get(i)[3]
								+ "</TMCustomerVoice>"
								+ "</TmCustomerComplaint>";
						sb.append(compl);
					}

					if (chekboxcheck) {
						tick = "Y";
					} else
						tick = "";
					/*
					 * SimpleDateFormat sdfCheck1 = new
					 * SimpleDateFormat("MM/dd/yyyy hh:mm a",Locale.ENGLISH);
					 * SimpleDateFormat sdfCheck3 = new
					 * SimpleDateFormat("MM/dd/yyyy kk:mm",Locale.ENGLISH);
					 * String check1 = BOOKINGDATE + " "+ BOOKINGTIME; Date
					 * date1 = null; try { date1 = sdfCheck1.parse(check1); }
					 * catch (ParseException e1) {
					 * 
					 * e1.printStackTrace(); }
					 */
					if (new CheckConnectivity().checkNow(getActivity())) {
						String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
								+ "<SOAP:Body>"
								+ "<TMSiebelServiceInsertOrUpdate_Input xmlns=\"http://siebel.com/asi/\">"
								+ "<ListOfTmServiceInterface xmlns=\"http://www.siebel.com/xml/TM%20Service%20Interface\">"
								+ "<ServiceRequest operation=\"\">"
								+ "<Created />" + "<AssetNumber>"
								+ user.CHASSISNUMBER
								+ "</AssetNumber>"
								+ "<IntegrationId>"
								+ (System.currentTimeMillis() - 1)
								+ "</IntegrationId>"
								+ "<OwnedByGroupId>"
								+ DIVISIONID
								+ "</OwnedByGroupId>"
								+ "<OwnedById>"
								+ PRI_EMP_ID
								+ "</OwnedById>"
								+ "<SRType>"
								+ spinner_servicetype.getSelectedItem()
										.toString()
								+ "</SRType>"
								+ "<TMBookingDate>"
								+ BOOKINGDATE
								+ " "
								+ BOOKINGTIME
								+ ":00"
								+ "</TMBookingDate>"
								+ "<TMBusinessUnit>TMPC</TMBusinessUnit>"
								+ "<CurrentMileage>"
								+ edit_currentkm.getText().toString()
								+ "</CurrentMileage>"
								+ "<Description>"
								+ ""
								+ "</Description>"
								+ "<VehicleLicenseNumber>"
								+ user.REGISTRATIONNUMBER
								+ "</VehicleLicenseNumber>" // spinner_regNo.getSelectedItem().toString()
								+ "<TTSource>Customer App</TTSource>"
								+ "<TMMSVRequired>"
								+ tick
								+ "</TMMSVRequired>"// if tick set to Y
								+ "<ListOfServiceRequest_Organization>"
								+ "<ServiceRequest_Organization IsPrimaryMVG=\"Y\" operation=\"\" searchspec=\"\">"// Set
																													// Value
																													// to
																													// Y
								+ "<Organization>"
								+ ORG_NAME
								+ "</Organization>"
								+ "</ServiceRequest_Organization>"
								+ "</ListOfServiceRequest_Organization>"
								+ "<ListOfTmCustomerComplaint>"
								+ sb
								+ "</ListOfTmCustomerComplaint>"
								+ "</ServiceRequest>"
								+ "</ListOfTmServiceInterface>"
								+ "<StatusObject></StatusObject>"
								+ "</TMSiebelServiceInsertOrUpdate_Input>"
								+ "</SOAP:Body>" + "</SOAP:Envelope>";

						
						Log.v("book service",req);
						
						// calling service booking webservice (cordys)

						new WebServiceCall(getActivity(), req,
								Constants.TMSiebelServiceInsertOrUpdate_Input,
								new ResponseCallback() {

									@Override
									public void onResponseReceive(Object object) {

										Log.v("inside Dialog",
												"service book dialog");

										// Tracking
										mTracker.send(new HitBuilders.EventBuilder()
												.setCategory(
														UserDetails
																.getUser_id())
												.setAction("thread_true")
												.setLabel("Service_Booking")
												.build());

										ArrayList<String> serviceSuccess = (ArrayList<String>) object;

										AlertDialog.Builder builder1 = new AlertDialog.Builder(
												getActivity());
										// builder1.setTitle("Service Booked with SR Number "+
										// serviceSuccess.get(1));
										builder1.setTitle("Service Booking");
										builder1.setMessage("Service Booked with SR Number "
												+ serviceSuccess.get(1));
										builder1.setCancelable(false);
										builder1.setPositiveButton(
												"OK",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														dialog.cancel();
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

										// setting service booking response

										s_parent.setUserId(UserDetails
												.getUser_id());
										s_parent.setComplaint_sr_no(serviceSuccess
												.get(1));
										s_parent.setComplaint_reg_no(user.REGISTRATIONNUMBER);
										// s_parent.setComplaint_date(offlineDate);
										s_parent.setKms(edit_currentkm
												.getText().toString());

										s_parent.setModel(user.PPL);
										// s_parent.setDate_of_booking(BOOKINGDATE);
										// s_parent.setBooked_for_time(historyTime);
										s_parent.setBooked_for_dealer(spinner_dealer
												.getSelectedItem().toString());
										s_parent.setService_type(spinner_servicetype
												.getSelectedItem().toString());
										if (tick.equals("")) {
											s_parent.setMsv_flag("N");
										} else
											s_parent.setMsv_flag(tick);

										// Save Service booking history on
										// Cloud.
										String req = Config.awsserverurl
												+ "tmsc_ch/customerapp/vehicleServices/insertServiceBookingHistory";
										List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
												19);
										String environment = "";
										String URL = getActivity()
												.getResources().getString(
														R.string.URL);

										if (URL.contains("qa")) {
											environment = "QA";
										} else {
											environment = "Production";
										}
										nameValuePairs
												.add(new BasicNameValuePair(
														"user_id", UserDetails
																.getUser_id()));
										nameValuePairs.add(new BasicNameValuePair(
												"registration_number", s_parent
														.getComplaint_reg_no()));
										nameValuePairs.add(new BasicNameValuePair(
												"sr_number", s_parent
														.getComplaint_sr_no()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"model", s_parent
																.getModel()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"kms", s_parent
																.getKms()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"booked_date",
														BOOKINGDATE));
										nameValuePairs
												.add(new BasicNameValuePair(
														"booked_time",
														BOOKINGTIME + ":00"));
										nameValuePairs.add(new BasicNameValuePair(
												"service_dealer", s_parent
														.getBooked_for_dealer()));
										nameValuePairs.add(new BasicNameValuePair(
												"service_type", s_parent
														.getService_type()));
										nameValuePairs.add(new BasicNameValuePair(
												"msv_flag", s_parent
														.getMsv_flag()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"environment",
														environment));
										nameValuePairs
												.add(new BasicNameValuePair(
														"div_id", DIVISIONID));
										nameValuePairs
												.add(new BasicNameValuePair(
														"customerMobile_num",
														edit_mobile.getText()
																.toString()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"customer_email_id",
														edit_email.getText()
																.toString()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"customer_first_name",
														edit_fname.getText()
																.toString()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"customer_last_name",
														edit_lname.getText()
																.toString()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"dealer_city",
														spinner_city.getText()
																.toString()));
										nameValuePairs
												.add(new BasicNameValuePair(
														"dealer_phone_num",
														edit_dealerNo.getText()
																.toString()));
										nameValuePairs.add(new BasicNameValuePair(
												"sessionId", UserDetails
														.getSeeionId()));
										String input = UserDetails.getUser_id()
												+ " "
												+ s_parent
														.getComplaint_reg_no()
												+ " "
												+ s_parent.getComplaint_sr_no()
												+ " "
												+ s_parent.getModel()
												+ " "
												+ s_parent.getKms()
												+ " "
												+ BOOKINGDATE
												+ " "
												+ BOOKINGTIME
												+ ":00"
												+ " "
												+ s_parent
														.getBooked_for_dealer()
												+ " "
												+ s_parent.getService_type()
												+ " "
												+ s_parent.getMsv_flag()
												+ " "
												+ environment
												+ " "
												+ DIVISIONID
												+ " "
												+ edit_mobile.getText()
														.toString()
												+ " "
												+ edit_email.getText()
														.toString();

										new AWS_WebServiceCall(
												getActivity(),
												req,
												ServiceHandler.POST,
												Constants.insertServiceBookingHistory,
												nameValuePairs,
												new ResponseCallback() {

													@Override
													public void onResponseReceive(
															Object object) {

														// Toast.makeText(getActivity(),
														// "Service Booking Details Saved",
														// Toast.LENGTH_LONG).show();
													}

													@Override
													public void onErrorReceive(
															String string) {

														// Toast.makeText(getActivity(),
														// "There is some issue with the Service. Please try again.",
														// Toast.LENGTH_LONG).show();
													}
												}).execute();

									}

									@Override
									public void onErrorReceive(String string) {

										Log.v("service error",
												"service booking error");

										mTracker.send(new HitBuilders.EventBuilder()
												.setCategory(
														UserDetails
																.getUser_id())
												.setAction("thread_false")
												.setLabel("Service_Booking")
												.build());
										Toast toast = Toast
												.makeText(
														getActivity(),
														"Service not booked. Please try again.",
														Toast.LENGTH_SHORT);
										toast.show();
									}
								}, "Booking Service.").execute();
					} else {
						Toast toast = Toast.makeText(getActivity(),
								"No network connection available.",
								Toast.LENGTH_SHORT);
						toast.show();
					}
				} else {
					// Toast.makeText(getActivity(), "Fields Missing.",
					// Toast.LENGTH_SHORT).show();
					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < emptyFields.size(); j++) {
						sb.append(emptyFields.get(j) + "\n");
					}

					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							getActivity());
					builder1.setTitle("Following fields are missing:");
					builder1.setMessage(sb);

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
		ll1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				toggle_contents(ll2);
			}
		});
		ll3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				toggle_contents(ll4);
			}
		});
		ll5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				toggle_contents(ll6);
			}
		});
		ll7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				toggle_contents(ll8);
			}
		});

		scrollView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("ScrollValue", Integer.toString(scrollView.getScrollX()));

				changeArrowImages();

				return false;
			}

		});
		// TableRow tr = (TableRow)table_layout.getChildAt(tag);
		// ImageView delete = (ImageView)tr.getChildAt(4);
		v.getRootView().setFocusableInTouchMode(true);
		v.getRootView().requestFocus();

		v.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					ll2.setVisibility(View.GONE);
					ll4.setVisibility(View.GONE);
					ll6.setVisibility(View.GONE);
					ll8.setVisibility(View.GONE);
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

	private void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getActivity().getFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getActivity().getFragmentManager(), "timePicker");
	}

	private class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			final Calendar end = Calendar.getInstance();
			year = end.get(Calendar.YEAR);
			month = end.get(Calendar.MONTH);
			day = end.get(Calendar.DAY_OF_MONTH);
			/*
			 * hour = c.get(Calendar.HOUR_OF_DAY); minute =
			 * c.get(Calendar.MINUTE);
			 */
			// Create a new instance of DatePickerDialog and return it
			DatePickerDialog d = new DatePickerDialog(getActivity(), this,
					year, month, day);
			DatePicker dp = d.getDatePicker();
			// dp.setMinDate(c.getTimeInMillis());
			c.add(Calendar.DAY_OF_YEAR, 1);
			end.add(Calendar.DAY_OF_YEAR, 15);
			dp.setMinDate(c.getTimeInMillis());
			dp.setMaxDate(end.getTimeInMillis());
			return d;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user

			historyDate = day + "-" + (month + 1) + "-" + year;
			SimpleDateFormat currentFormat = new SimpleDateFormat("dd-MM-yyyy",
					Locale.ENGLISH);
			SimpleDateFormat dbdateFormat = new SimpleDateFormat("MM/dd/yyyy",
					Locale.ENGLISH);
			Date showdate = new Date();
			try {
				showdate = currentFormat.parse(historyDate);
				BOOKINGDATE = dbdateFormat.format(showdate);
				edit_servicedate.setText(BOOKINGDATE);
			} catch (ParseException e) {

				e.printStackTrace();
			}

		}

	}

	private class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		String aMpM = "AM";
		int currentHour;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker

			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			/*TimePickerDialog tpd = new TimePickerDialog(getActivity(), this,
					hour, minute, DateFormat.is24HourFormat(getActivity()));*/

			TimePickerDialog tpd = new TimePickerDialog(getActivity(), this,
					hour, minute, true);
			
			// Create a new instance of TimePickerDialog and return it
			return tpd;

		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			// edit_servicetime.setText(hourOfDay+":"+minute);

			/*if (hourOfDay > 11) {
				aMpM = "PM";
			}

			// Make the 24 hour time format to 12 hour time format

			if (hourOfDay > 11) {
				currentHour = hourOfDay - 12;
			} else {
				currentHour = hourOfDay;
			}
			
			
			
			if (minute < 10) {
				historyTime = String.valueOf(currentHour) + ":0"
						+ String.valueOf(minute) + " " + aMpM;
				
	
				
			} else
				historyTime = String.valueOf(currentHour) + ":"
			+ String.valueOf(minute) + " " + aMpM;
		
			
			if (CompareTime(historyTime)) {
				edit_servicetime.setText(historyTime);
				BOOKINGTIME = hourOfDay + ":" + minute;// time to pass in Ws
			} else {
				edit_servicetime.setText("");
				Toast.makeText(getActivity(),
						"Please select time in between 8.00AM to 4.00PM.",
						Toast.LENGTH_LONG).show();
			}*/
			
			Log.e("history hour ", ""+hourOfDay);
			Log.e("history minute ", ""+minute);
			if (minute < 10) {
				historyTime = String.valueOf(hourOfDay) + ":0"
						+ String.valueOf(minute);
				
	
				
			}else
			{
				historyTime = String.valueOf(hourOfDay) + ":"
						+ String.valueOf(minute);
			}
			Log.e("history time", historyTime);
			if (CompareTime(historyTime)) {
				edit_servicetime.setText(historyTime);
				BOOKINGTIME = hourOfDay + ":" + minute;// time to pass in Ws
			} else {
				edit_servicetime.setText("");
				Toast.makeText(getActivity(),
						"Please select time in between 8.00AM to 4.00PM.",
						Toast.LENGTH_LONG).show();
			}
		
			
		}
		
	
	
	}

	private void toggle_contents(View v) {
		if (v.isShown()) {
			slide_up(getActivity(), v);
			v.setVisibility(View.GONE);
		} else {
			v.setVisibility(View.VISIBLE);
			slide_down(getActivity(), v);
			if (v.getId() == R.id.ll4) {
				slide_up(getActivity(), ll2);
				slide_up(getActivity(), ll6);
				slide_up(getActivity(), ll8);
				ll2.setVisibility(View.GONE);
				ll6.setVisibility(View.GONE);
				ll8.setVisibility(View.GONE);
			} else if (v.getId() == R.id.ll6) {
				slide_up(getActivity(), ll2);
				slide_up(getActivity(), ll4);
				slide_up(getActivity(), ll8);
				ll2.setVisibility(View.GONE);
				ll4.setVisibility(View.GONE);
				ll8.setVisibility(View.GONE);
			} else if (v.getId() == R.id.ll8) {
				slide_up(getActivity(), ll4);
				slide_up(getActivity(), ll6);
				slide_up(getActivity(), ll2);
				ll4.setVisibility(View.GONE);
				ll6.setVisibility(View.GONE);
				ll2.setVisibility(View.GONE);
			} else if (v.getId() == R.id.ll2) {
				slide_up(getActivity(), ll4);
				slide_up(getActivity(), ll6);
				slide_up(getActivity(), ll8);
				ll4.setVisibility(View.GONE);
				ll6.setVisibility(View.GONE);
				ll8.setVisibility(View.GONE);
			}
		}
	}

	private void slide_down(Context ctx, View v) {
		// Toast.makeText(ctx, "SLIDING DOWN", Toast.LENGTH_SHORT).show();
		/*
		 * Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
		 * if(a != null){ a.reset(); if(v != null){ v.clearAnimation();
		 * v.startAnimation(a); } }
		 */

		switch (v.getId()) {
		case R.id.ll2:
			setArrowImage(img_arrow, R.drawable.downarrow);
			break;
		case R.id.ll4:
			setArrowImage(img_arrow1, R.drawable.downarrow);
			break;
		case R.id.ll6:
			setArrowImage(img_arrow2, R.drawable.downarrow);
			break;
		case R.id.ll8:
			setArrowImage(img_arrow3, R.drawable.downarrow);
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

	private void slide_up(Context ctx, View v) {
		// Toast.makeText(ctx, "SLIDING UP", Toast.LENGTH_SHORT).show();
		/*
		 * Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
		 * if(a != null){ a.reset(); if(v != null){ v.clearAnimation();
		 * v.startAnimation(a); } }
		 */

		switch (v.getId()) {
		case R.id.ll2:
			setArrowImage(img_arrow, R.drawable.orangerightarrow);
			break;
		case R.id.ll4:
			setArrowImage(img_arrow1, R.drawable.orangerightarrow);
			break;
		case R.id.ll6:
			setArrowImage(img_arrow2, R.drawable.orangerightarrow);
			break;
		case R.id.ll8:
			setArrowImage(img_arrow3, R.drawable.orangerightarrow);
			break;
		default:
			break;
		}
	}

	private void BuildTable(String[] complainttext, String[] complaints) {
		// LayoutParams params = new LayoutParams(500,
		// LayoutParams.MATCH_PARENT);
		TableRow.LayoutParams lp = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT);
		/*
		 * TableLayout.LayoutParams tbl = new TableLayout.LayoutParams(
		 * TableLayout.LayoutParams.WRAP_CONTENT,
		 * TableLayout.LayoutParams.WRAP_CONTENT );
		 */

		if (firstclick) {

			TableRow tbrow0 = new TableRow(getActivity());
			for (int i = 0; i < 4; i++) {
				TextView tv0 = new TextView(getActivity());
				tv0.setText(complainttext[i]);
				tv0.setTextColor(Color.GRAY);

				// tv0.setHeight(LayoutParams.FILL_PARENT);
				tv0.setLayoutParams(lp);
				tv0.setTextSize(15);
				tv0.setGravity(Gravity.LEFT);
				tv0.setPadding(10, 5, 5, 10);
				// tv0.setBackgroundColor(Color.BLACK);
				tv0.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.grey_border_blackrectangle));
				// tbrow0.setLayoutParams(tbl);
				tbrow0.addView(tv0);
			}
			// tbrow0.setBackgroundColor(Color.BLACK);
			// tbrow0.setLayoutParams(new
			// LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.FILL_PARENT));
			left.setVisibility(View.VISIBLE);
			right.setVisibility(View.VISIBLE);
			table_layout.addView(tbrow0);
			firstclick = false;
		}

		TableRow tbrow = new TableRow(getActivity());
		// Toast.makeText(getActivity(), "TABLEROW AT " +tag+"",
		// Toast.LENGTH_SHORT).show();
		tbrow.setTag(tag);
		for (int i = 0; i < 4; i++) {
			TextView tv = new TextView(getActivity());
			tv.setText(complaints[i]);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(14);
			tv.setGravity(Gravity.LEFT);
			tv.setPadding(5, 5, 5, 5);
			tv.setLayoutParams(lp);
			if (tag % 2 == 0)
				tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.gray_border_grayrectangle));
			else
				tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.grey_border_lightgrayrectangle));

			tbrow.addView(tv);
		}

		// add delete button
		del = new ImageView(getActivity());
		del.setTag(tag);
		del.setBackgroundColor(Color.TRANSPARENT);
		TableRow.LayoutParams lp1 = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.MATCH_PARENT);
		del.setLayoutParams(lp1);
		del.setPadding(20, 0, 20, 0);
		del.setImageDrawable(getResources().getDrawable(R.drawable.whitedelete));

		tbrow.addView(del);
		del.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final TableRow parent = (TableRow) v.getParent();
				table_layout.removeView(parent);

			}
		});

		table_layout.addView(tbrow);
		tag++;
	}

	private void changeArrowImages() {

		int maxScrollX;
		maxScrollX = scrollView.getChildAt(0).getMeasuredWidth()
				- scrollView.getMeasuredWidth();
		if (scrollView.getScrollX() == 0) {

			left.setImageResource(R.drawable.left_lightarrow);
		} else {
			left.setImageResource(R.drawable.letf_darkarrow);
		}

		if (scrollView.getScrollX() == maxScrollX) {
			Log.i("scrollView2", "reached  " + maxScrollX);

			right.setImageResource(R.drawable.right_lightarrow);
		} else {
			right.setImageResource(R.drawable.rightarrow_dark);
		}
	}

	/*
	 * public boolean CompareTime(String strTimeToCompare)
	 * 
	 * {
	 * 
	 * Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	 * 
	 * int dtHour;
	 * 
	 * int dtMin;
	 * 
	 * int iAMPM;
	 * 
	 * String strAMorPM = null;
	 * 
	 * Date dtCurrentDate;
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa",
	 * Locale.getDefault());
	 * 
	 * try {
	 * 
	 * Date TimeToCompare = sdf.parse(strTimeToCompare);
	 * 
	 * dtMin = cal.get(Calendar.MINUTE);
	 * 
	 * dtHour = cal.get(Calendar.HOUR);
	 * 
	 * iAMPM = cal.get(Calendar.AM_PM);
	 * 
	 * if (iAMPM == 1)
	 * 
	 * {
	 * 
	 * strAMorPM = "PM";
	 * 
	 * }
	 * 
	 * if (iAMPM == 0)
	 * 
	 * {
	 * 
	 * strAMorPM = "AM";
	 * 
	 * }
	 * 
	 * dtCurrentDate = sdf.parse(dtHour + ":" + dtMin + " " + strAMorPM);
	 * 
	 * Date Starttime = sdf.parse("08:00 AM"); Date Endtime =
	 * sdf.parse("04:00 PM"); Log.d("Here",""+dtCurrentDate+Starttime);
	 * 
	 * if(TimeToCompare.after(Starttime)) { if(TimeToCompare.before(Endtime)) {
	 * Log.d("Here1",""+dtCurrentDate+Starttime +"endtime"+Endtime);
	 * 
	 * return true; }else { return false;
	 * 
	 * } }else if (dtCurrentDate.equals(TimeToCompare))
	 * 
	 * { Log.d("Here3",""+dtCurrentDate+Starttime); return true;
	 * 
	 * }else { Log.d("Here false",""+dtCurrentDate+Starttime);
	 * 
	 * return false;
	 * 
	 * } if (dtCurrentDate.after(Starttime)) {
	 * Log.d("Here1",""+dtCurrentDate+Starttime); return true;
	 * 
	 * }else if (dtCurrentDate.before(Endtime))
	 * 
	 * { Log.d("Here2",""+dtCurrentDate+Starttime); return true;
	 * 
	 * }else if (dtCurrentDate.equals(TimeToCompare))
	 * 
	 * { Log.d("Here3",""+dtCurrentDate+Starttime); return true;
	 * 
	 * }else { return false;
	 * 
	 * }
	 * 
	 * } catch (ParseException e) {
	 * 
	 * 
	 * 
	 * e.printStackTrace();
	 * 
	 * }
	 * 
	 * return false;
	 * 
	 * }
	 */
	public boolean CompareTime(String strTimeToCompare)

	{
Calendar cal = Calendar.getInstance(TimeZone.getDefault());

		int dtHour;

		int dtMin;

		int iAMPM;

		String strAMorPM = null;

		Date dtCurrentDate;

		SimpleDateFormat sdf = null;

		Date TimeToCompare = null;
		try {
			/*sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
			TimeToCompare = sdf.parse(strTimeToCompare);

			dtMin = cal.get(Calendar.MINUTE);

			dtHour = cal.get(Calendar.HOUR);*/

			
			
			
			if (Build.VERSION.SDK_INT <= 21) {
				
				
				sdf = new SimpleDateFormat("HH:mm");
				TimeToCompare = sdf.parse(strTimeToCompare);

				
			//	Log.e("parse pattern", ""+sdf.parse("02:50 PM").toLocaleString());
			//	Log.e("parse pattern 1", ""+sdf.parse("02:10 AM").toLocaleString());
				dtMin = cal.get(Calendar.MINUTE);

				dtHour = cal.get(Calendar.HOUR);
				
				
				iAMPM = cal.get(Calendar.AM_PM);

				if (iAMPM == 1) {

					strAMorPM = "PM";

				}

				if (iAMPM == 0)

				{

					strAMorPM = "AM";

				}

				dtCurrentDate = sdf.parse(dtHour + ":" + dtMin + " "
						+ strAMorPM);
				//Log.e("if", ""+dtHour+":"+dtMin+" "+strAMorPM);
				//Log.e("start", ""+"8"+":"+"00"+" "+"AM");
				String st="7"+":"+"59";
				String ed="16"+":"+"01";

			//	Log.e("get hours", ""+TimeToCompare.getHours());
				
				//Log.e("selected time", TimeToCompare.toLocaleString());
				
				Date Starttime = sdf.parse(st);
				Date Endtime = sdf.parse(ed);
				//Log.e("start time", Starttime.toLocaleString());
			//	Log.e("end time", Endtime.toLocaleString());
//				Log.d("Here", "" + dtCurrentDate + Starttime);

				
				Log.e("start time", ""+Starttime.toLocaleString());
				Log.e("end time", Endtime.toLocaleString());
				Log.e("selected time", TimeToCompare.toLocaleString());
				
				if (TimeToCompare.after(Starttime) && TimeToCompare.before(Endtime)) {
					
				return true;
				
				} 
				else {
					Log.d("Here false", "" + dtCurrentDate + Starttime);

					return false;

				}
			} else // for api version 6.0 or above
			{
			//	sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
//				TimeToCompare = sdf.parse(strTimeToCompare);

				/*	sdf = new SimpleDateFormat("hh:mm aa", Locale.US);*/
				/*sdf = new SimpleDateFormat("HH:mm", Locale.US);
				TimeToCompare = sdf.parse(strTimeToCompare);
				dtMin = cal.get(Calendar.MINUTE);

				dtHour = cal.get(Calendar.HOUR);
			
				
				iAMPM = cal.get(Calendar.AM_PM);

				if (iAMPM == 1)

				{

					strAMorPM = "PM";

				}

				if (iAMPM == 0)

				{

					strAMorPM = "AM";

				}

				dtCurrentDate = sdf.parse(dtHour + ":" + dtMin + " "
						+ strAMorPM);
			//	Log.e("else", ""+dtHour+":"+dtMin+" "+strAMorPM);
				Date Starttime = sdf.parse("8:00");
				Date Endtime = sdf.parse("16:00");
				
				
				Log.e("Here", "" + dtCurrentDate + Starttime);

				if (TimeToCompare.after(Starttime)) {
					if (TimeToCompare.before(Endtime)) {
						Log.d("Here1", "" + dtCurrentDate + Starttime
								+ "endtime" + Endtime);

						return true;
					} else {
						return false;

					}
				} else if (dtCurrentDate.equals(TimeToCompare))

				{
					Log.e("Here3", "" + dtCurrentDate + Starttime);
					return true;

				} else {
					Log.e("Here false", "" + dtCurrentDate + Starttime);

					return false;

				}
*/
				
				
				
				// NEW CODE
				
				sdf = new SimpleDateFormat("HH:mm");
				TimeToCompare = sdf.parse(strTimeToCompare);

				
			//	Log.e("parse pattern", ""+sdf.parse("02:50 PM").toLocaleString());
			//	Log.e("parse pattern 1", ""+sdf.parse("02:10 AM").toLocaleString());
				dtMin = cal.get(Calendar.MINUTE);

				dtHour = cal.get(Calendar.HOUR);
				
				
				iAMPM = cal.get(Calendar.AM_PM);

				if (iAMPM == 1) {

					strAMorPM = "PM";

				}

				if (iAMPM == 0)

				{

					strAMorPM = "AM";

				}

				dtCurrentDate = sdf.parse(dtHour + ":" + dtMin + " "
						+ strAMorPM);
				//Log.e("if", ""+dtHour+":"+dtMin+" "+strAMorPM);
				//Log.e("start", ""+"8"+":"+"00"+" "+"AM");
				String st="7"+":"+"59";
				String ed="16"+":"+"01";

			//	Log.e("get hours", ""+TimeToCompare.getHours());
				
				//Log.e("selected time", TimeToCompare.toLocaleString());
				
				Date Starttime = sdf.parse(st);
				Date Endtime = sdf.parse(ed);
				//Log.e("start time", Starttime.toLocaleString());
			//	Log.e("end time", Endtime.toLocaleString());
//				Log.d("Here", "" + dtCurrentDate + Starttime);

				
				Log.e("start time", ""+Starttime.toLocaleString());
				Log.e("end time", Endtime.toLocaleString());
				Log.e("selected time", TimeToCompare.toLocaleString());
				
				if (TimeToCompare.after(Starttime) && TimeToCompare.before(Endtime)) {
					
				return true;
				
				} 
				else {
					Log.d("Here false", "" + dtCurrentDate + Starttime);

					return false;

				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}

			/*
			 * if (dtCurrentDate.after(Starttime)) {
			 * Log.d("Here1",""+dtCurrentDate+Starttime); return true;
			 * 
			 * }else if (dtCurrentDate.before(Endtime))
			 * 
			 * { Log.d("Here2",""+dtCurrentDate+Starttime); return true;
			 * 
			 * }else if (dtCurrentDate.equals(TimeToCompare))
			 * 
			 * { Log.d("Here3",""+dtCurrentDate+Starttime); return true;
			 * 
			 * }else { return false;
			 * 
			 * }
			 */

		} catch (ParseException e) {

			e.printStackTrace();

		}

		return false;

	}
}
