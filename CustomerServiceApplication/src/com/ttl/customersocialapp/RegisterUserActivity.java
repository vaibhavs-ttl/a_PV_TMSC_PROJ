package com.ttl.customersocialapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.helper.CheckValidations;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class RegisterUserActivity extends Activity implements OnClickListener {

	private Button malebutton, female, submit, cancel;
	private boolean selectedmale = true;

	private EditText firstname, lastname, contactnumber, altcontactnumber,
			emailid, address, pincode, userid, password;
	private InstantAutoComplete spinnerstate, spinnercity;
	private String selectedgender, selectedfirsname, selectedlastname,
			selectedcontactnumber, selectedaltcontactnumber, selectedemailid,
			selectedaddress, selectedpincode, selecteduserid, selectedpassword,
			selectedstate, selectedcity;
	private ArrayList<String> stateVlaues = new ArrayList<String>();

	private ArrayList<String> cityVlaues = new ArrayList<String>();

	private TextView txtterms, passwordpolicy;

	private ImageView dialogimgclose;
	private TextView dialogtitle;
	private CheckBox termCheck;

	private Tracker mTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);

		firstname = (EditText) findViewById(R.id.txtfirstname);
		lastname = (EditText) findViewById(R.id.txtlastname);
		contactnumber = (EditText) findViewById(R.id.txtcontactnumber);
		altcontactnumber = (EditText) findViewById(R.id.txtaltcontactnumber);
		emailid = (EditText) findViewById(R.id.txtemailid);
		address = (EditText) findViewById(R.id.txtaddress);
		pincode = (EditText) findViewById(R.id.txtpincode);
		userid = (EditText) findViewById(R.id.txtuserid);
		password = (EditText) findViewById(R.id.txtpassword);
		spinnercity = (InstantAutoComplete) findViewById(R.id.spincity);
		spinnerstate = (InstantAutoComplete) findViewById(R.id.spinstate);
		txtterms = (TextView) findViewById(R.id.txtterms);
		passwordpolicy = (TextView) findViewById(R.id.passwordpolicy);
		
		termCheck = (CheckBox) findViewById(R.id.checkBox1);
		malebutton = (Button) findViewById(R.id.male);
		female = (Button) findViewById(R.id.female);
		submit = (Button) findViewById(R.id.submit);
		cancel = (Button) findViewById(R.id.cancel);
		malebutton.setOnClickListener(this);
		female.setOnClickListener(this);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		if (selectedmale) {
			malebutton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.selectedbutton));
			selectedgender = "Male";
		} else {
			female.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.femaleselected));
			selectedgender = "Female";
		}
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();

		
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_terms);
		dialogimgclose = (ImageView) dialog.findViewById(R.id.imgclose);
		dialogtitle = (TextView) dialog.findViewById(R.id.dialogtitle);
		

		txtterms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		
				dialogtitle.setText("Terms & Services");
		
				dialog.show();
			}
		});

		dialogimgclose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		final Dialog passworddialog = new Dialog(this);
		passworddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		passworddialog.setContentView(R.layout.passwordpolicy);
		ImageView passworddialogimgclose = (ImageView) passworddialog
				.findViewById(R.id.imgclose);
		final TextView passworddialogtitle = (TextView) passworddialog
				.findViewById(R.id.dialogtitle);
		passwordpolicy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				passworddialogtitle.setText("Password Policy");
				passworddialogtitle.setGravity(Gravity.CENTER);
				passworddialog.show();
			}
		});

		passworddialogimgclose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				passworddialog.dismiss();
			}
		});
		//
		populateState();

		

	}

	@Override
	protected void onStart() {
		
		super.onStart();
		mTracker.setScreenName("RegisterUserScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}


	public class DialogAdapter extends ArrayAdapter<String> {
		String[] arr;

		public DialogAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			arr = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {

				LayoutInflater li = LayoutInflater
						.from(RegisterUserActivity.this);
				v = li.inflate(R.layout.dialoglist_row, parent, false);
			}

			TextView t1 = (TextView) v.findViewById(R.id.txtinfo);

			TextView txtdot = (TextView) v.findViewById(R.id.txtdot);

			t1.setText(arr[position]);

			if (t1.getText().toString().startsWith("Probable Cause:")
					|| t1.getText().toString().startsWith("Action be Taken:")
					|| t1.getText().toString().startsWith("Note:")) {
				txtdot.setVisibility(View.GONE);
				t1.setTypeface(null, Typeface.ITALIC);
			}

			return v;
		}
	}

	@Override
	public void onClick(View v) {


		switch (v.getId()) {
		case R.id.male:
			selectedmale = true;
			selectedgender = "Male";
			malebutton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.selectedbutton));
			female.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.femalenormal));

			break;
		case R.id.female:
			selectedmale = false;
			selectedgender = "Female";
			malebutton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.malenormal));
			female.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.femaleselected));
			break;
		case R.id.submit:

			Pattern p = Pattern.compile("[^A-Za-z0-9]");
			Matcher m = p.matcher(password.getText().toString());

			Pattern p1 = Pattern.compile("[A-Z]");
			Matcher m1 = p1.matcher(password.getText().toString());

			Pattern p2 = Pattern.compile("[a-z]");
			Matcher m2 = p2.matcher(password.getText().toString());

			Pattern p3 = Pattern.compile("[0-9]");
			Matcher m3 = p3.matcher(password.getText().toString());

			
			
			selectedfirsname = firstname.getText().toString();
			selectedlastname = lastname.getText().toString();
			if (selectedmale)
				selectedgender = "Male";
			else
				selectedgender = "Female";

			selectedaltcontactnumber = altcontactnumber.getText().toString();
			selectedemailid = emailid.getText().toString();
			selectedaddress = address.getText().toString();
			selectedstate = spinnerstate.getText().toString();
			// selecteddist = spinnerdistrict.getText().toString();
			selectedcity = spinnercity.getText().toString();
			selectedpincode = pincode.getText().toString();
			selecteduserid = userid.getText().toString();
			selectedpassword = password.getText().toString();
			selectedcontactnumber = contactnumber.getText().toString();

			if (firstname.getText().toString().trim().length() == 0) {
				firstname.setError("Please enter your First name.");
				firstname.setFocusable(true);
			} else if (lastname.getText().toString().trim().length() == 0) {
				lastname.requestFocus();
				lastname.setError("Please enter your Last  name.");
				lastname.setFocusable(true);
			} else if (contactnumber.getText().toString().trim().length() != 10) {
				contactnumber.requestFocus();
				contactnumber
						.setError("Please enter your 10 digits Contact Number.");
				contactnumber.setFocusable(true);
			} else if (contactnumber.getText().toString().trim().length() == 0) {
				contactnumber.requestFocus();
				contactnumber
						.setError("Please enter your 10 digits Contact Number.");
				contactnumber.setFocusable(true);
			}
			/*
			 * else if(altcontactnumber.getText().toString().trim().length()==0
			 * ) { altcontactnumber.setError(
			 * "Please Enter your 10 digits Alternate Contact No.");
			 * altcontactnumber.setFocusable(true); }
			 */
			/*
			 * else
			 * if(altcontactnumber.getText().toString().trim().length()!=10) {
			 * altcontactnumber
			 * .setError("Please Enter your 10 digits Alternate Contact No.");
			 * altcontactnumber.setFocusable(true); }
			 */
			else if (!(Config.isEmailValid(emailid.getText().toString()))) {
				Log.d("Email ID", emailid.getText().toString());
				emailid.requestFocus();
				emailid.setError("Please enter valid Email Id.");
				emailid.setFocusable(true);
			} else if (address.getText().toString().trim().length() == 0) {
				address.requestFocus();
				address.setError("Please enter your Address.");
				address.setFocusable(true);
			} else if (spinnerstate.getText().toString().equals("")) {
				spinnerstate.requestFocus();
				spinnerstate.setError("Please select State");
				spinnerstate.setFocusable(true);
			} else if (spinnercity.getText().toString().equals("")) {
				spinnercity.requestFocus();
				spinnercity.setError("Please select City");
				spinnercity.setFocusable(true);
			} else if (pincode.getText().toString().trim().length() == 0) {
				pincode.requestFocus();
				pincode.setError("Please enter Pincode.");
				pincode.setFocusable(true);
			} else if (pincode.getText().toString().trim().length() < 6) {
				pincode.requestFocus();
				pincode.setError("Please enter valid pincode.");
				pincode.setFocusable(true);
			} else if (userid.getText().toString().trim().length() == 0) {
				userid.requestFocus();
				userid.setError("Please enter User Id.");
				userid.setFocusable(true);
			} else if (!(userid.getText().toString().matches("[a-zA-Z0-9_]*"))) {
				userid.requestFocus();
				userid.setError("Please enter valid User Id , only underscore( _ ) special character allowed.");
				userid.setFocusable(true);
				// Added by Trupti Reddy
			} else if (userid.getText().toString().trim()
					.equalsIgnoreCase("or")
					|| (userid.getText().toString().trim()
							.equalsIgnoreCase("union"))) {
				userid.setError("No or/union word Allowed");
				userid.requestFocus();
				userid.setFocusable(true);
			} else if (password.getText().toString().length() == 0) {
				password.requestFocus();
				password.setError("Please enter Password.");
				password.setFocusable(true);

				// Added by Trupti Reddy
			} else if (password.getText().toString().trim()
					.equalsIgnoreCase("or")
					|| (password.getText().toString().trim()
							.equalsIgnoreCase("union"))) {
				password.requestFocus();
				password.setError("No or/union word Allowed");
				password.setFocusable(true);
				// Added by Trupti Reddy
			} else if (password.getText().toString().contains(" ")) {
				password.requestFocus();
				password.setError("No spaces allowed.");
				password.setFocusable(true);
				// Added by Trupti Reddy
			} else if (password.getText().toString().contains("'")
					|| password.getText().toString().trim().contains("-")
					|| password.getText().toString().contains(";")
					|| password.getText().toString().trim().contains("|")) {
				password.setError("No special characters '  -  ;  | are Allowed");
				password.setFocusable(true);
				password.requestFocus();
				// Added by Parag
			} else if ((password.getText().toString().length() < 8)) {
				password.requestFocus();
				password.setError("Password should contain atleast 8 characters.");
				password.setFocusable(true);
			}
				else if((password.getText().toString().length() > 15))
				{
					password.requestFocus();
					password.setError("Password must be between 8 to 15 characters.");
					password.setFocusable(true);
				}
				
				// Added by Parag
			 else if (!m.find()) {
				password.requestFocus();
				password.setError("Password should have atleast one special character.");
				password.setFocusable(true);
				// Added by Parag
			} else if (!m1.find()) {
				password.requestFocus();
				password.setError("Password should have an uppercase character.");
				password.setFocusable(true);
				// Added by Parag
			} else if (!m2.find()) {
				password.requestFocus();
				password.setError("Password should have a lowercase character.");
				password.setFocusable(true);
				// Toast.makeText(getApplicationContext(),
				// "password must have a lowercase character",Toast.LENGTH_SHORT).show();
			} else if (!m3.find()) {
				password.requestFocus();
				password.setError("Password should have a numeric character.");
				password.setFocusable(true);
				// Toast.makeText(getApplicationContext(),
				// "password must have a lowercase character",Toast.LENGTH_SHORT).show();
			}
		
			/*else if(!password.getText().toString().contains("@#^&+="))
			{
				password.requestFocus();
				password.setError("Password must be the combination of uppercase,lowercase,numbers and must contains at least one following special character(@,#,^,&,+,=).");
				password.setFocusable(true);
			}*/
			
			/*	else if(!password.getText().toString().contains("@") || !password.getText().toString().contains("^")|| !password.getText().toString().contains("#") || !password.getText().toString().contains("+") || !password.getText().toString().contains("="))
			{
				
				Toast.makeText(RegisterUserActivity.this,"inside else if", Toast.LENGTH_LONG).show();
				//Hello123//@
				password.requestFocus();
				password.setError("Password must be the combination of uppercase,lowercase,numbers and must contains at least one following special character(@,#,^,&,+,=).");
				password.setFocusable(true);
			}
			*/
			else if(new CheckValidations().validatePassword(password.getText().toString().trim())==false)
			{
				
				password.requestFocus();
				password.setError("Password must be the combination of uppercase,lowercase,numbers and must contains at least one following special character(@,#,^,&,+,=).");
				password.setFocusable(true);
				
			}
			else if (!(termCheck.isChecked())) {
				Toast.makeText(getApplicationContext(),
						"Please agree with Terms and Services",
						Toast.LENGTH_SHORT).show();
			} else {
				CheckConnectivity checknow = new CheckConnectivity();
				boolean connect = checknow.checkNow(this);
				if (connect) {
					registerUser();
				
				Log.v("register ", "called");
				} else {
					Toast toast = Toast.makeText(this,
							"No network connection available.",
							Toast.LENGTH_SHORT);
					toast.show();
				}
			}

			break;
		case R.id.cancel:
			this.finish();
			Intent intent = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(intent);
			break;
		}
	}

	/*
	 * public void populateState() { String req =
	 * "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
	 * +"<SOAP:Header xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
	 * +"<header xmlns=\"http://schemas.cordys.com/General/1.0/\">"
	 * +"<Logger xmlns=\"http://schemas.cordys.com/General/1.0/\">" +
	 * "<DC xmlns=\"http://schemas.cordys.com/General/1.0/\" name=\"XForms\">/testtool/testtool.caf</DC>"
	 * +
	 * "<DC xmlns=\"http://schemas.cordys.com/General/1.0/\" name=\"XForms\">/methodsetsmanager/methodsetexplorer.caf</DC>"
	 * +
	 * "<DC xmlns=\"http://schemas.cordys.com/General/1.0/\" name=\"XForms\">/sysresourcemgr/sysresourcemgr.caf</DC>"
	 * +
	 * "<DC xmlns=\"http://schemas.cordys.com/General/1.0/\" name=\"XForms\">/com/cordys/cusp/cusp.caf</DC>"
	 * +
	 * "<DC xmlns=\"http://schemas.cordys.com/General/1.0/\" name=\"hopCount\">0</DC>"
	 * +
	 * "<DC xmlns=\"http://schemas.cordys.com/General/1.0/\" name=\"correlationID\">00215ef7-d846-11e4-faf1-0ea55009db29</DC>"
	 * +"</Logger> </header></SOAP:Header> <SOAP:Body>" +
	 * "<GetAllIndianStates xmlns=\"http://schemas.cordys.com/com.cordys.tatamotors.utilitysiebelwsapps\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\" />"
	 * +"</SOAP:Body></SOAP:Envelope>";
	 * 
	 * new WebServiceCall(this, req, Constants.GetAllIndianStates, new
	 * ResponseCallback() {
	 * 
	 * @Override public void onResponseReceive(Object object) {
	 * 
	 * stateVlaues = (ArrayList<String>) object;
	 * 
	 * ArrayAdapter<String> aa = new
	 * ArrayAdapter<String>(RegisterUserActivity.this
	 * ,android.R.layout.simple_spinner_item, stateVlaues);
	 * //aa.setDropDownViewResource
	 * (android.R.layout.simple_spinner_dropdown_item);
	 * //spinState.setAdapter(aa); spinnerstate.setThreshold(1);
	 * //spinnerstate.setDropDownBackgroundResource(R.color.spinback);
	 * spinnerstate.setAdapter(aa); spinnerstate.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { 
	 * stub spinnerstate.showDropDown(); } });
	 * 
	 * spinnerstate.setOnItemClickListener(new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) {  selectedstate =
	 * parent.getItemAtPosition(position).toString(); String req =
	 * "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"> <SOAP:Body><GetAllIndianDistricts xmlns=\"http://schemas.cordys.com/com.cordys.tatamotors.utilitysiebelwsapps\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
	 * +"<state>"+selectedstate+"</state>" +"</GetAllIndianDistricts>"
	 * +"</SOAP:Body></SOAP:Envelope>"; new
	 * WebServiceCall(RegisterUserActivity.this, req,
	 * Constants.GetAllIndianDistricts, new ResponseCallback() {
	 * 
	 * @Override public void onResponseReceive(Object object) { // TODO
	 * Auto-generated method stub distVlaues = (ArrayList<String>) object;
	 * 
	 * ArrayAdapter<String> aa = new
	 * ArrayAdapter<String>(RegisterUserActivity.this
	 * ,android.R.layout.simple_spinner_item, distVlaues);
	 * //aa.setDropDownViewResource
	 * (android.R.layout.simple_spinner_dropdown_item);
	 * //spinState.setAdapter(aa); spinnerdistrict.setThreshold(1);
	 * //spinnerstate.setDropDownBackgroundResource(R.color.spinback);
	 * spinnerdistrict.setAdapter(aa); spinnerdistrict.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub spinnerdistrict.showDropDown(); } });
	 * spinnerdistrict.setOnItemClickListener(new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) {  selecteddist =
	 * parent.getItemAtPosition(position).toString(); String req =
	 * "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
	 * +"<SOAP:Body>" +
	 * "<GetAllIndianCity xmlns=\"http://schemas.cordys.com/com.cordys.tatamotors.utilitysiebelwsapps\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
	 * +"<state>"+selectedstate+"</state>" +"<dist>"+selecteddist+"</dist>"
	 * +"</GetAllIndianCity> </SOAP:Body></SOAP:Envelope>"; new
	 * WebServiceCall(RegisterUserActivity.this, req,
	 * Constants.GetAllIndianCity, new ResponseCallback() {
	 * 
	 * @Override public void onResponseReceive(Object object) { // TODO
	 * Auto-generated method stub cityVlaues = (ArrayList<String>) object;
	 * 
	 * ArrayAdapter<String> aa = new
	 * ArrayAdapter<String>(RegisterUserActivity.this
	 * ,android.R.layout.simple_spinner_item, cityVlaues);
	 * //aa.setDropDownViewResource
	 * (android.R.layout.simple_spinner_dropdown_item);
	 * //spinState.setAdapter(aa); spinnercity.setThreshold(1);
	 * //spinnerstate.setDropDownBackgroundResource(R.color.spinback);
	 * spinnercity.setAdapter(aa); spinnercity.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub spinnercity.showDropDown(); } }); selectedcity =
	 * spinnercity.getText().toString(); }
	 * 
	 * @Override public void onErrorReceive(String string) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * 
	 * }, "Run").execute(); } });
	 * 
	 * }
	 * 
	 * @Override public void onErrorReceive(String string) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * 
	 * }, "Run").execute(); } }); }
	 * 
	 * @Override public void onErrorReceive(String string) {
	 * 
	 * //Toast.makeText(getApplicationContext(), "Not a valid user",
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * },"Populating Data..").execute(); }
	 */
	public void populateState() {
		CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow.checkNow(getApplicationContext());
		if (connect) {
			String req = Config.awsserverurl
					+ "tmsc_ch/customerapp/costEstimateServices/getState";
			new AWS_WebServiceCall(this, req, ServiceHandler.GET,
					Constants.getState, new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							
							stateVlaues = (ArrayList<String>) object;

							ArrayAdapter<String> aa = new ArrayAdapter<String>(
									RegisterUserActivity.this,
									android.R.layout.simple_spinner_item,
									stateVlaues);
							
							spinnerstate.setThreshold(1);
							
							spinnerstate.setAdapter(aa);
							/*spinnerstate
									.setValidator(new Validator(stateVlaues));*/
							spinnerstate
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											
											spinnerstate.showDropDown();
										}
									});
						}

						@Override
						public void onErrorReceive(String string) {
							

						}
					}).execute();
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}

		
		spinnerstate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				spinnerstate.performValidation();
				selectedstate = parent.getItemAtPosition(position).toString();
				spinnercity.setText("");
				pincode.setText("");
				CheckConnectivity checknow = new CheckConnectivity();
				boolean connect = checknow.checkNow(getApplicationContext());
				if (connect) {
					String req = Config.awsserverurl
							+ "tmsc_ch/customerapp/costEstimateServices/getCityFromStateMaster";
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs.add(new BasicNameValuePair("state",
							selectedstate));
					new AWS_WebServiceCall(RegisterUserActivity.this, req,
							ServiceHandler.POST,
							Constants.getCityFromStateMaster, nameValuePairs,
							new ResponseCallback() {

								@Override
								public void onResponseReceive(Object object) {
									
									cityVlaues = (ArrayList<String>) object;

		
									ArrayAdapter<String> aa = new ArrayAdapter<String>(
											RegisterUserActivity.this,
											android.R.layout.simple_spinner_item,
											cityVlaues);
		
									spinnercity.setThreshold(1);
		
									spinnercity.setAdapter(aa);
									/*spinnercity.setValidator(new Validator(
											cityVlaues));*/
									spinnercity
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													
													spinnercity.showDropDown();
												}
											});
									spinnercity
											.setOnItemClickListener(new OnItemClickListener() {

												@Override
												public void onItemClick(
														AdapterView<?> arg0,
														View arg1, int arg2,
														long arg3) {
													
													pincode.setText("");

												}
											});
									spinnercity.performValidation();
									selectedcity = spinnercity.getText()
											.toString();

								}

								@Override
								public void onErrorReceive(String string) {
								

								}
							}).execute();
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"No network connection available.",
							Toast.LENGTH_SHORT);
					toast.show();
				}

			}
		});

	}

	public void registerUser() {

		String req = Config.awsserverurl + "tmsc_ch/customerapp/user/registeruser";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(13);
		nameValuePairs.add(new BasicNameValuePair("first_name",
				selectedfirsname));
		nameValuePairs
				.add(new BasicNameValuePair("last_name", selectedlastname));
		nameValuePairs.add(new BasicNameValuePair("gender", selectedgender));
		nameValuePairs.add(new BasicNameValuePair("contact_number",
				selectedcontactnumber));
		nameValuePairs.add(new BasicNameValuePair("alt_contact_number",
				selectedaltcontactnumber));
		nameValuePairs.add(new BasicNameValuePair("email_id", selectedemailid));
		nameValuePairs.add(new BasicNameValuePair("address", selectedaddress));
		nameValuePairs.add(new BasicNameValuePair("city", selectedcity));
		nameValuePairs.add(new BasicNameValuePair("pincode", selectedpincode));
		nameValuePairs.add(new BasicNameValuePair("district", ""));
		nameValuePairs.add(new BasicNameValuePair("state", selectedstate));
		nameValuePairs.add(new BasicNameValuePair("user_id", selecteduserid));
		nameValuePairs
				.add(new BasicNameValuePair("password", selectedpassword));

		new AWS_WebServiceCall(RegisterUserActivity.this, req,
				ServiceHandler.POST, Constants.registeruser, nameValuePairs,
				new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						boolean register = (boolean) object;
						Log.d("Here", "Register ");
						if (register) {

							mTracker.send(new HitBuilders.EventBuilder()
									.setCategory(selecteduserid)
									.setAction("thread_true")
									.setLabel("Register").build());
							UserDetails.setFirst_name(selectedfirsname);
							UserDetails.setLast_name(selectedlastname);
							UserDetails.setGender(selectedgender);
							UserDetails.setEmail_id(selectedemailid);
							UserDetails
									.setContact_number(selectedcontactnumber);
							UserDetails
									.setAlt_contact_number(selectedaltcontactnumber);
							UserDetails.setAddress(selectedaddress);
							UserDetails.setCity(selectedcity);
							UserDetails.setPincode(selectedpincode);
							// UserDetails.setDistrict(selecteddist);
							UserDetails.setState(selectedstate);
							UserDetails.setUser_id(selecteduserid);
							UserDetails.setPassword(selectedpassword);
							if (selectedgender.equals("Male")) {
								UserDetails
										.setPhotourl(Config.awsserverurl
												+ "tmsc_ch/CustomerProfileImages/male.png");
							} else {
								UserDetails
										.setPhotourl(Config.awsserverurl
												+ "tmsc_ch/CustomerProfileImages/female.png");

							}

							Toast.makeText(getApplicationContext(),
									"Registration Succesfully Done.",
									Toast.LENGTH_LONG).show();
							Log.d("Success", "Register ");

							Intent intent = new Intent(
									RegisterUserActivity.this,
									LoginActivity.class);
							startActivity(intent);
							finish();
							String URL = getApplicationContext().getResources()
									.getString(R.string.URL);
							String environment = "";

							if (URL.contains("qa")) {
								environment = "QA";
							} else {
								environment = "Production";
							}
							/*
							 * String req =
							 * Config.awsserverurl+"tmsc_ch/customerapp/user/xml";
							 * List<NameValuePair> nameValuePairs = new
							 * ArrayList<NameValuePair>( 3);
							 * nameValuePairs.add(new
							 * BasicNameValuePair("user_id", selecteduserid ));
							 * nameValuePairs.add(new
							 * BasicNameValuePair("password",
							 * selectedpassword)); nameValuePairs.add(new
							 * BasicNameValuePair( "environment", environment));
							 * Log.d("env", environment); new
							 * AWS_WebServiceCall(RegisterUserActivity.this,
							 * req, ServiceHandler.POST,
							 * Constants.afterregisteruser, nameValuePairs, new
							 * ResponseCallback() {
							 * 
							 * @Override public void onResponseReceive(Object
							 * object) { 
							 * boolean res = (boolean) object; if (res) {
							 * 
							 * Toast.makeText(getApplicationContext(),
							 * "Registration succesful.",
							 * Toast.LENGTH_LONG).show(); Log.d("Success",
							 * "Register ");
							 * 
							 * Intent intent = new Intent(
							 * RegisterUserActivity.this, LoginActivity.class);
							 * startActivity(intent); finish();
							 * 
							 * }
							 * 
							 * }
							 * 
							 * @Override public void onErrorReceive(String
							 * string) { 
							 * 
							 * }
							 * 
							 * }).execute();
							 */
							/*
							 * SharedPreferences settings =
							 * getSharedPreferences( PREFS_NAME, 0);
							 * SharedPreferences.Editor editor = settings
							 * .edit(); editor.putString("USERID",
							 * UserDetails.getUser_id());
							 * editor.putString("PASSWORD",
							 * UserDetails.getPassword()); editor.commit();
							 */

						} else {
							Log.d("Here", "Register ");

							Toast.makeText(
									getApplicationContext(),
									"Registration ussuccesful, please try again",
									Toast.LENGTH_LONG).show();
							mTracker.send(new HitBuilders.EventBuilder()
									.setCategory(selecteduserid)
									.setAction("thread_false")
									.setLabel("Register").build());
						}

					}

					@Override
					public void onErrorReceive(String string) {
												if (string.contains("Duplicate")) {
							userid.setError("User id already exist. Try another.");
							userid.requestFocus();
							userid.setFocusable(true);
						} 
						else if(string.equalsIgnoreCase("Password must not contain black listed characters."))
						{
					
							Toast.makeText(RegisterUserActivity.this, string, Toast.LENGTH_LONG).show();		
							
						}
												
						
						else if (string
								.equalsIgnoreCase("No internet connection.")) {
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(
									RegisterUserActivity.this);
							// Setting Dialog Title
							alertDialog.setTitle("");
							// Setting Dialog Message
							alertDialog
									.setMessage("Couldn't connect to the Server. Please check your Network Connection and try again.");
							// Setting Icon to Dialog
							alertDialog
									.setIcon(android.R.drawable.ic_dialog_alert);
							// Setting Positive "Yes" Button
							alertDialog.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									});

							// Showing Alert Message
							alertDialog.show();
						}
					}

				}).execute();

	}

	public boolean valideUserID(String userid) {
		String regExpn = "[^A-Za-z0-9_]+";

		CharSequence inputStr = userid;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;

	}

	// added by Parag

	/*
	 * public boolean validatePassword(String password){
	 * if(password.equals("")){ passwordValid =true;
	 * Toast.makeText(getApplicationContext(),
	 * "Please enter Password",Toast.LENGTH_SHORT).show(); }else
	 * if((password.length()<8)){ passwordValid =true;
	 * Toast.makeText(getApplicationContext(),
	 * "password should contain at least 8 characters"
	 * ,Toast.LENGTH_SHORT).show(); }else if(!getSpecialCharacter(password)){
	 * passwordValid =true; Toast.makeText(getApplicationContext(),
	 * "password must have at least one special character"
	 * ,Toast.LENGTH_SHORT).show(); }else if(!getUppercaseCharacter(password)){
	 * passwordValid = true; Toast.makeText(getApplicationContext(),
	 * "password must have an uppercase character",Toast.LENGTH_SHORT).show();
	 * }else if(!getLowercaseCharacter(password)){ passwordValid = true;
	 * Toast.makeText(getApplicationContext(),
	 * "password must have a lowercase character",Toast.LENGTH_SHORT).show(); }
	 * return passwordValid;
	 * 
	 * }
	 * 
	 * public boolean getSpecialCharacter(String s) {
	 * 
	 * if (s == null || s.trim().isEmpty()) {
	 * System.out.println("Incorrect format of string"); return false; } Pattern
	 * p = Pattern.compile("[^A-Za-z0-9]"); Matcher m = p.matcher(s); // boolean
	 * b = m.matches(); boolean b = m.find(); if (b == true)
	 * System.out.println("There is a special character in my string "); else
	 * System.out.println("There is no special char.");
	 * 
	 * return b;
	 * 
	 * }
	 * 
	 * public boolean getUppercaseCharacter(String s){
	 * 
	 * Pattern p1 = Pattern.compile("[A-Z]"); Matcher m1= p1.matcher(s); boolean
	 * b = m1.find(); if (b == true)
	 * System.out.println("There is a upper character in my string "); else
	 * System.out.println("There is no upper char."); return b; }
	 * 
	 * public boolean getLowercaseCharacter(String s){
	 * 
	 * Pattern p2 = Pattern.compile("[a-z]"); Matcher m2= p2.matcher(s); boolean
	 * b = m2.find(); if (b == true)
	 * System.out.println("There is a lower character in my string "); else
	 * System.out.println("There is no lower char.");
	 * 
	 * return b; }
	 */

	@Override
	public void onBackPressed() {

		this.finish();
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
	}
}
