package com.ttl.customersocialapp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;


public class CustomerDetailFragment extends Fragment implements OnClickListener {
	private View rootView;
	private TextView username, city;
	private Button submit, cancel;
	private ImageView update;
	private Spinner spingender;
	private EditText contactnumer, altcontactnumber, email_id, address, pincode;
	private InstantAutoComplete state , citytxt;
	private TextView gendertext ;
	private LinearLayout buttonLayout;
	private String selectedgender ,selectedfirsname , selectedlastname ,
	 selectedcontactnumber ,selectedaltcontactnumber ,selectedemailid ,
	 selectedaddress , selectedpincode , selecteduserid ,selectedpassword ,selectedstate ,selectedcity;
	private ImageView profileImage,camera;
	
	private String encodedString;
	
	
	private ArrayList<String> stateVlaues = new ArrayList<String>();
	
	private ArrayList<String> cityVlaues = new ArrayList<String>();
	private TextView changePassword;
	private Dialog dialog_changePassword;
	private final int CAMERA_CAPTURE = 1, SELECT_FILE = 2 , CROP_PIC=3;
	private boolean connect;
	private Tracker mTracker;
		@Override
		public void onStart() {
			
			super.onStart();
			mTracker.setScreenName("CustomerDetailsScreen");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		rootView = inflater.inflate(R.layout.fragment_customer_detail,
				container, false);
		CheckConnectivity checknow = new CheckConnectivity();
		 connect = checknow
				.checkNow(getActivity());
		username = (TextView) rootView.findViewById(R.id.username);
		city = (TextView) rootView.findViewById(R.id.city);
		contactnumer = (EditText) rootView.findViewById(R.id.txtcontactnumber);
		altcontactnumber = (EditText) rootView.findViewById(R.id.txtaltnumber);
		email_id = (EditText) rootView.findViewById(R.id.txtemailid);
		address = (EditText) rootView.findViewById(R.id.txtAddress);
		pincode = (EditText) rootView.findViewById(R.id.txtpincode);
		state = (InstantAutoComplete) rootView.findViewById(R.id.txtstate);
		citytxt = (InstantAutoComplete) rootView.findViewById(R.id.txtcity);
		//district = (EditText) rootView.findViewById(R.id.txtdistrict);
		spingender = (Spinner) rootView.findViewById(R.id.txtgender);
		update = (ImageView) rootView.findViewById(R.id.btn_update);
		buttonLayout = (LinearLayout) rootView.findViewById(R.id.buttonlayout);
		submit = (Button) rootView.findViewById(R.id.submit);
		cancel = (Button) rootView.findViewById(R.id.cancel);
		gendertext = (TextView) rootView.findViewById(R.id.gender);
		profileImage = (ImageView) rootView.findViewById(R.id.profileimage);
		camera = (ImageView) rootView.findViewById(R.id.changephoto);
		//emailText = (TextView) rootView.findViewById(R.id.txtemailid_view);
		
		username.setText(UserDetails.getFirst_name() + " "
				+ UserDetails.getLast_name());
		city.setText(UserDetails.getCity());
		contactnumer.setText(UserDetails.getContact_number());
		altcontactnumber.setText(UserDetails.getAlt_contact_number());
		email_id.setText(UserDetails.getEmail_id());
		address.setText(UserDetails.getAddress());
		pincode.setText(UserDetails.getPincode());
		state.setText(UserDetails.getState());
		//district.setText(UserDetails.getDistrict());
		citytxt.setText(UserDetails.getCity());
		gendertext.setText(UserDetails.getGender());
	
		AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
		mTracker = application.getDefaultTracker();
		
		contactnumer.setEnabled(false);
		altcontactnumber.setEnabled(false);
		email_id.setEnabled(false);
		
		address.setEnabled(false);
		pincode.setEnabled(false);
		spingender.setVisibility(View.GONE);
		state.setEnabled(false);
		//district.setEnabled(false);
		citytxt.setEnabled(false);
		buttonLayout.setVisibility(View.GONE);
		cancel.setVisibility(View.GONE);
		submit.setVisibility(View.GONE);

		update.setOnClickListener(this);
		
		contactnumer.setTextColor(Color.WHITE);
		altcontactnumber.setTextColor(Color.WHITE);
		email_id.setTextColor(Color.WHITE);
		address.setTextColor(Color.WHITE);
		state.setTextColor(Color.WHITE);
		citytxt.setTextColor(Color.WHITE);
		pincode.setTextColor(Color.WHITE);
		
		
		changePassword = (TextView) rootView.findViewById(R.id.changePassword);
		changePassword.setOnClickListener(this);
		
		String photoURL = UserDetails.getPhotourl();
		photoURL = photoURL.substring(0,
				photoURL.length())+200;

        new LoadProfileImage(profileImage).execute(photoURL);
      /*  if(UserDetails.getUser_id().equals(""))
        {
        	enableUpdate();
        }*/
				
		profileImage.setOnClickListener(this);
		camera.setOnClickListener(this);
		
		rootView.getRootView().setFocusableInTouchMode(true);
		rootView.getRootView().requestFocus();
		rootView.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if(UserDetails.getUser_id().equals(""))
						{
							Toast.makeText(getActivity(), "Please update Profile first.", Toast.LENGTH_SHORT).show();
						}else
						{
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container, new HomeFragment())
								.commit();
						}
						return true;
					}
				}
				return false;
			}
		});
		return rootView;
	}
	
			private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
			    ImageView bmImage;
			    
			    public LoadProfileImage(ImageView bmImage) {
			        this.bmImage = bmImage;
			    }
			 
			    protected Bitmap doInBackground(String... urls) {
			        Bitmap mIcon11 = null;
			        try {
			            InputStream in = new java.net.URL(UserDetails.getPhotourl()).openStream();
			            mIcon11 = BitmapFactory.decodeStream(in);
			        } catch (Exception e) {
			            Log.e("Error", e.getMessage());
			            e.printStackTrace();
			        }
			        return mIcon11;
			    }
			 
			    protected void onPostExecute(Bitmap result) {
			        bmImage.setImageBitmap(result);
			    }
			}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.changePassword:
			dialog_changePassword = new Dialog(getActivity());
			dialog_changePassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog_changePassword.setContentView(R.layout.dialog_changepassword);
			dialog_changePassword.setCancelable(false);
			dialog_changePassword.show();
			TextView dialogtitle =(TextView)dialog_changePassword.findViewById(R.id.dialogtitle);
			dialogtitle.setText("Change Password");
			final EditText oldpass = (EditText) dialog_changePassword.findViewById(R.id.txtoldpass);
			final EditText newpass = (EditText) dialog_changePassword.findViewById(R.id.txtnewpass);
			final EditText confirmpass = (EditText) dialog_changePassword.findViewById(R.id.txtconfpass);
			final TextView txtpassword = (TextView) dialog_changePassword.findViewById(R.id.txtpasswordpolicy);

			final Dialog passworddialog = new Dialog(getActivity());
			passworddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			passworddialog.setContentView(R.layout.passwordpolicy);		
		ImageView	passworddialogimgclose = (ImageView) passworddialog.findViewById(R.id.imgclose);
		final TextView passworddialogtitle = (TextView) passworddialog.findViewById(R.id.dialogtitle);
			txtpassword.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					passworddialogtitle.setGravity(Gravity.CENTER_HORIZONTAL);
					passworddialogtitle.setText("Password Policy");
					passworddialog.show();
				}
			});
			
			passworddialogimgclose.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					passworddialog.dismiss();
				}
			});
			
			ImageView close = (ImageView) dialog_changePassword.findViewById(R.id.imgclose);
			close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					dialog_changePassword.dismiss();
				}
			});
			Button confirm = (Button) dialog_changePassword.findViewById(R.id.visit_site);
			confirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(new CheckConnectivity().checkNow(getActivity()))
					{
					
					String old = oldpass.getText().toString();
					final String newp = newpass.getText().toString();
					String confirmp = confirmpass.getText().toString();
					
					if(old.equals(LoginActivity.password_))
					{
						if(!(newp.equals("")))
						{
							if(newp.equals(confirmp))
							{
									
								
								if (new CheckConnectivity().checkNow(getActivity())) {
								//changePassword(newp);
									Log.d("password change ty", "password");
									String req = Config.awsserverurl+"tmsc_ch/customerapp/user/resetPassword";
									List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
											4);
									 nameValuePairs.add(new BasicNameValuePair("user_id",
											 UserDetails.getUser_id()));
									nameValuePairs.add(new BasicNameValuePair("oldPassword",
											oldpass.getText().toString()));
									nameValuePairs.add(new BasicNameValuePair(
											"newPassword",newpass.getText().toString()));
									nameValuePairs.add(new BasicNameValuePair(
											"sessionId",Config.sessionId));
									
								/*	nameValuePairs.add(new BasicNameValuePair(
											"confirmPassword", confirmpass.getText().toString()));*/
									
									
									new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST, Constants.resetPassword, nameValuePairs, new ResponseCallback() {
										
										
										@Override
										public void onResponseReceive(Object object) {
											
											boolean changepassword = (boolean) object;
											if(changepassword){
												Log.d("password change", Config.resetpasswordtoast);
												mTracker.send(new HitBuilders.EventBuilder()
								               	.setCategory(UserDetails.getUser_id())
								   	        	.setAction("thread_true")
								   	        	.setLabel("Passwordchange")
									            .build());
												UserDetails.setPassword(newp);
												LoginActivity.password_=newp;
												
										//		Toast.makeText(rootView.getContext(),Config.resetpasswordtoast, Toast.LENGTH_LONG).show();
											
												Toast.makeText(rootView.getContext(),"Password Changed sucessfully,please re login app with changed password", Toast.LENGTH_LONG).show();
												
												
												dialog_changePassword.dismiss();
											
												
												startActivity(new Intent(getActivity(), LoginActivity.class));
												getActivity().finishAffinity();
												
											}else
											{
												Toast.makeText(rootView.getContext(), "Password change failed. Please try again.", Toast.LENGTH_LONG).show();
											}
											rootView.getRootView().setFocusable(true);
									  		rootView.getRootView().requestFocus();
										}
										
										@Override
										public void onErrorReceive(String string) {
											Toast.makeText(rootView.getContext(),string.toString(), Toast.LENGTH_LONG).show();

											
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

							}else
							{
								confirmpass.setError("Password not matching.");
								confirmpass.setFocusable(true);
								confirmpass.requestFocus();
							}
							
						}else
						{
							newpass.setError("Please enter new password.");
							newpass.setFocusable(true);
							newpass.requestFocus();
						}
						
					}else
					{
						oldpass.setError("Password incorrect.");
						oldpass.setFocusable(true);
						oldpass.requestFocus();
					}
				
				}else
				{
					Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
				}
				
				}
			});
			break;
		case R.id.profileimage:
			//selectProfileImage();
			// Create intent to Open Image applications like Gallery, Google Photos
	
			
			if (new CheckConnectivity().checkNow((Context)getActivity())) {
				Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// Start the Intent
				startActivityForResult(galleryIntent, 1);	
			}
			else
			{
				
				Toast.makeText(getActivity(), getActivity().getString(R.string.no_network_msg), Toast.LENGTH_SHORT).show();	
				return;
			}
			
			
			break;
		case R.id.changephoto:
			//selectProfileImage();
			// Create intent to Open Image applications like Gallery, Google Photos
			
		
			if (new CheckConnectivity().checkNow((Context)getActivity())) {
				Log.v("changephoto clicked", "clicked");
				Intent galleryIntent1 = new Intent(Intent.ACTION_PICK,
				        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// Start the Intent
				startActivityForResult(galleryIntent1, 1);	
			}
			else
			{
				Toast.makeText(getActivity(), getActivity().getString(R.string.no_network_msg), Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			
			break;
		case R.id.btn_update:
			/*if(!(UserDetails.getUser_id().equals("")))
			{*/
			enableUpdate();
			//}
			break;
		case R.id.submit:
			/*if(firstname.getText().toString().trim().length()==0)
			{
				firstname.setError("Please Enter your FristName");
				firstname.setFocusable(true);
			}
			else if(lastname.getText().toString().trim().length()==0)
			{
				lastname.setError("Please Enter your LastName");
				lastname.setFocusable(true);
			}		
			else */if( contactnumer.getText().toString().trim().length() != 10)
			{
				contactnumer.setError("Please Enter your 10 digits Contact Number");
				contactnumer.setFocusable(true);
			}
			else if( contactnumer.getText().toString().trim().length()==0)
			{
				contactnumer.setError("Please Enter your 10 digits Contact Number");
				contactnumer.setFocusable(true);
			}
			/*else if(altcontactnumber.getText().toString().trim().length()==0 )
			{
				altcontactnumber.setError("Please Enter your 10 digits Alternate Contact No.");
				altcontactnumber.setFocusable(true);
			}
			else if(altcontactnumber.getText().toString().trim().length()!=10)
			{
				altcontactnumber.setError("Please Enter your 10 digits Alternate Contact No.");
				altcontactnumber.setFocusable(true);
			}*/
			else if(!(Config.isEmailValid(email_id.getText().toString())))
			{
				email_id.setError("Please Enter valid Email Id");
				email_id.setFocusable(true);
			}
			else if(address.getText().toString().trim().length()==0)
			{
				address.setError("Please Enter your Address");
				address.setFocusable(true);
			}else if(state.getText().toString().trim().length()==0)
			{
				state.setError("Please select state");
				state.setFocusable(true);
			}
			/*else if(district.getText().toString().trim().length()==0)
			{	
				district.setError("Please select district");
				district.setFocusable(true);
			}*/
			else if(citytxt.getText().toString().trim().length()==0)
			{	
				citytxt.setError("Please select city");
				citytxt.setFocusable(true);
			}
			else if(pincode.getText().toString().trim().length()==0)
			{
				pincode.setError("Please Enter PinCode");
				pincode.setFocusable(true);
			}
			else if(pincode.getText().toString().trim().length()<6)
			{
				pincode.setError("Please Enter compelete PinCode  ");
				pincode.setFocusable(true);
			}
			
			else
			{
				CheckConnectivity checknow = new  CheckConnectivity();
				boolean connect = checknow.checkNow(getActivity());
				if(connect)
				{
					if(UserDetails.getUser_id().equals(""))
					{
						if (connect) {
						registerUser();
						}
						 else {
							Toast toast = Toast.makeText(getActivity(),
									"No network connection available.",
									Toast.LENGTH_SHORT);
							toast.show();
						}
					
					}else
					{
						if (connect) {
						updateUser();
						}
						 else {
							Toast toast = Toast.makeText(getActivity(),
									"No network connection available.",
									Toast.LENGTH_SHORT);
							toast.show();
						}
					}
				}
				else
				{
					Toast toast = Toast.makeText(getActivity(), "No network connection available.", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
			
			break;
		case R.id.cancel:
			
			contactnumer.setError(null);
			email_id.setError(null);
			address.setError(null);
			state.setError(null);
			citytxt.setError(null);
			pincode.setError(null);
			/*if(!(UserDetails.getUser_id().equals("")))
			{*/
			contactnumer.setEnabled(false);
			altcontactnumber.setEnabled(false);
			email_id.setEnabled(false);
			//emailText.setVisibility(View.VISIBLE);
			address.setEnabled(false);
			pincode.setEnabled(false);
			spingender.setVisibility(View.GONE);
			gendertext.setVisibility(View.VISIBLE);
			state.setEnabled(false);
			//district.setEnabled(false);
			citytxt.setEnabled(false);
			buttonLayout.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			submit.setVisibility(View.GONE);
			contactnumer.setTextColor(Color.WHITE);
			altcontactnumber.setTextColor(Color.WHITE);
			email_id.setTextColor(Color.WHITE);
			address.setTextColor(Color.WHITE);
			state.setTextColor(Color.WHITE);
			citytxt.setTextColor(Color.WHITE);
			pincode.setTextColor(Color.WHITE);
			
			contactnumer.setText(UserDetails.getContact_number());
			altcontactnumber.setText(UserDetails.getAlt_contact_number());
			email_id.setText(UserDetails.getEmail_id());
			address.setText(UserDetails.getAddress());
			pincode.setText(UserDetails.getPincode());
			state.setText(UserDetails.getState());
			//district.setText(UserDetails.getDistrict());
			citytxt.setText(UserDetails.getCity());
			gendertext.setText(UserDetails.getGender());
			/*}else
			{
				Toast.makeText(getActivity(), "Please update Profile first.", Toast.LENGTH_SHORT).show();
			}*/
			break;
		}

	}
	
	
	public void enableUpdate()
	{
		contactnumer.setEnabled(true);
		altcontactnumber.setEnabled(true);
		email_id.setEnabled(true);
		address.setEnabled(true);
		pincode.setEnabled(true);
		spingender.setVisibility(View.VISIBLE);
		gendertext.setVisibility(View.GONE);
		email_id.setVisibility(View.VISIBLE);
	//	emailText.setVisibility(View.GONE);
		cancel.setVisibility(View.VISIBLE);
		submit.setVisibility(View.VISIBLE);
		state.setEnabled(true);
	//	district.setEnabled(true);
		citytxt.setEnabled(true);
		buttonLayout.setVisibility(View.VISIBLE);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		populateState();
		contactnumer.setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
		altcontactnumber.setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
		email_id.setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
		address.setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
		state.setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
		citytxt.setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
		pincode.setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
		
		if(UserDetails.getGender().equals("Male"))
		{
			spingender.setSelection(0);
		}else
		{
			spingender.setSelection(1);
		}
		spingender.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				
				
					((TextView) parent.getChildAt(0)).setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
			}
	
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
				((TextView) parent.getChildAt(0)).setTextColor(rootView.getContext().getResources().getColor(R.color.textcolor));
	
			}
	
		
		});
	}
	public void updateUser()
	{
		selectedaltcontactnumber = altcontactnumber.getText().toString();
		selectedemailid =  email_id.getText().toString();
		selectedaddress = address.getText().toString();
		selectedstate = state.getText().toString();
	//	selecteddist = district.getText().toString();
		selectedcity = citytxt.getText().toString();
		selectedpincode = pincode.getText().toString();
		selectedcontactnumber = contactnumer.getText().toString();
		selectedgender = spingender.getSelectedItem().toString();
			String req = Config.awsserverurl+"tmsc_ch/customerapp/user/updateUserDetails";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(14);
			nameValuePairs.add(new BasicNameValuePair("first_name",UserDetails.getFirst_name()));
			nameValuePairs.add(new BasicNameValuePair("last_name",UserDetails.getLast_name()));
			nameValuePairs.add(new BasicNameValuePair("gender",selectedgender));
			nameValuePairs.add(new BasicNameValuePair("contact_number",selectedcontactnumber));
			nameValuePairs.add(new BasicNameValuePair("alt_contact_number",selectedaltcontactnumber));
			nameValuePairs.add(new BasicNameValuePair("email_id",selectedemailid));
			nameValuePairs.add(new BasicNameValuePair("address",selectedaddress));
			nameValuePairs.add(new BasicNameValuePair("city",selectedcity));
			nameValuePairs.add(new BasicNameValuePair("pincode",selectedpincode));
			nameValuePairs.add(new BasicNameValuePair("district",""));
			nameValuePairs.add(new BasicNameValuePair("state",selectedstate));
			nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
			   nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));

			new AWS_WebServiceCall(getActivity(), req,ServiceHandler.POST ,Constants.updateUserDetails,
					nameValuePairs,new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							
							boolean register = (boolean) object;
							Log.d("Here", "Register ");
							if(register)
							{
								/* UserDetails.setFirst_name(selectedfirsname);
				                  UserDetails.setLast_name(selectedlastname);*/
								
								mTracker.send(new HitBuilders.EventBuilder()
				               	.setCategory(UserDetails.getUser_id())
				   	        	.setAction("thread_true")
				   	        	.setLabel("CustomerDetailsUpdate")
					            .build());
				                  UserDetails.setGender(selectedgender);
				                  UserDetails.setEmail_id(selectedemailid);
				                  UserDetails.setContact_number(selectedcontactnumber);
				                  UserDetails.setAlt_contact_number(selectedaltcontactnumber);
				                  UserDetails.setAddress(selectedaddress);
				                  UserDetails.setCity(selectedcity);
				                  UserDetails.setPincode(selectedpincode);
				                //  UserDetails.setDistrict(selecteddist);
				                  UserDetails.setState(selectedstate);
				                  
				                 if(UserDetails.getUser_id().equals(""))
				                 {
				                	 UserDetails.setUser_id(selectedemailid);
				                 }
								Toast.makeText(rootView.getContext(),"Details updated succesfully.", Toast.LENGTH_LONG).show();
							
								
								FragmentManager fm = getFragmentManager();
								FragmentTransaction tx = fm.beginTransaction();
								tx.replace(R.id.frame_container, new HomeFragment())
										.commit();
								
							}
							else
							{
								//Log.d("Here", "Register ");

								Toast.makeText(rootView.getContext(), "Updation failed plaese try again.", Toast.LENGTH_LONG).show();
								
								
							}
							rootView.getRootView().setFocusable(true);
					  		rootView.getRootView().requestFocus();
						
						}

						@Override
						public void onErrorReceive(String string) {
							
							/*Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
							startActivity(intent);*/
							Toast.makeText(getActivity(), string, Toast.LENGTH_LONG);
							rootView.getRootView().setFocusable(true);
					  		rootView.getRootView().requestFocus();
						}

						
					}).execute();
			
		}
	
	/*public void selectProfileImage()
	{
		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
		        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		startActivityForResult(galleryIntent, 1);
		
	
	}*/
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    	int RESULT_OK = -1;
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
 
                Uri selectedImage = data.getData();
               /* String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
                // Get the cursor
                Cursor cursor = rootView.getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
 
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                imgPath = cursor.getString(columnIndex);
                compressImage(imgPath);*/
                performCrop(selectedImage);
               /* cursor.close();
              
                // Set the Image in ImageView after decoding the String
                profileImage.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Java web app
                params.put("filename", fileName);
                Log.d("File name", fileName);*/
               
 
 
            } else if (requestCode == CROP_PIC && new CheckConnectivity().checkNow(getActivity())) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
           
                profileImage.setImageBitmap(thePic);
                encodeImagetoString(thePic);
            } 
            /*else if(new CheckConnectivity().checkNow(getActivity()))
            {
            	
            	Toast.makeText(rootView.getContext(), getActivity().getString(R.string.no_network_msg),
                        Toast.LENGTH_LONG).show();
            	
            	
            }*/
           /* else {
                Toast.makeText(rootView.getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }*/
        } catch (Exception e) {
            Toast.makeText(rootView.getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
 
    }
	
	/*public void uploadImage(Bitmap bitmap1)
	{
		encodeImagetoString(bitmap1);
		Log.d("String", "Uplaoding");
	}*/
	public void encodeImagetoString(final Bitmap bitmap1) {
        new AsyncTask<Void, Void, String>() {
 
            protected void onPreExecute() {
 
            };
 
            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
              /*  bitmap1 = BitmapFactory.decodeFile(imgPath,
                        options);*/
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap1.compress(Bitmap.CompressFormat.PNG, 50, stream); 
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                Log.d("Encoded String", encodedString);
                return "";
            }
 
            @Override
            protected void onPostExecute(String msg) {
                /*prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                // Trigger Image upload
                triggerImageUpload();*/
            
            	String req = Config.awsserverurl+"tmsc_ch/customerapp/user/uploadProfileImage";
            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
    			nameValuePairs.add(new BasicNameValuePair("uploadedInputStream",encodedString));
    			 // Log.d("Encoded String", encodedString);
    			nameValuePairs.add(new BasicNameValuePair("filename",UserDetails.getUser_id()));
    			 
    			nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
    			   nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
    			//  Log.d("User Id", UserDetails.getUser_id());
    			   new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST, Constants.uploadProfileImage,nameValuePairs, new ResponseCallback() {
    		             
    		             @Override
    		             public void onResponseReceive(Object object) {
    
    		            	UserDetails.setPhotourl(object.toString());
    		            	 
    		             }
    		             
    		             @Override
    		             public void onErrorReceive(String string) {
    
    		                 Toast.makeText(getActivity(), "Image could be uploaded.", Toast.LENGTH_SHORT).show();
    		             }
    		         }).execute();
            }
        }.execute(null, null, null);
        
    }
	
	public void populateState() {
		String req = Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getState";
	
		new AWS_WebServiceCall(getActivity(), req, ServiceHandler.GET,
				Constants.getState, new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						stateVlaues=(ArrayList<String>)object;

						ArrayAdapter<String> aa = new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_spinner_item,
								stateVlaues);
						
						state.setThreshold(1);


						state.setAdapter(aa);
						state.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								
								state.showDropDown();
							}
						});
						state.setText(UserDetails.getState().toString());
						selectedstate = state.getText().toString();
						if(!(selectedstate.equals("")))
						{
							if (connect) {
							populateCities(selectedstate);
						}
						 else {
							Toast toast = Toast.makeText(getActivity(),
									"No network connection available.",
									Toast.LENGTH_SHORT);
							toast.show();
						}
						}
						//citytxt.setText("");
						rootView.getRootView().setFocusable(true);
				  		rootView.getRootView().requestFocus();
					}

					@Override
					public void onErrorReceive(String string) {
						
						rootView.getRootView().setFocusable(true);
				  		rootView.getRootView().requestFocus();
					}
				}).execute();

		
		state.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				selectedstate = parent.getItemAtPosition(position).toString();
				if (connect) {
				populateCities(selectedstate);
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
	
	public void populateCities(String state)
	{
		String req = Config.awsserverurl+"tmsc_ch/customerapp/costEstimateServices/getCityFromStateMaster";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				1);
		nameValuePairs.add(new BasicNameValuePair("state",
				state));
	
		new AWS_WebServiceCall(rootView.getContext(), req,
				ServiceHandler.POST, Constants.getCityFromStateMaster,
				nameValuePairs, new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						cityVlaues = (ArrayList<String>) object;
						
						
						ArrayAdapter<String> aa = new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_spinner_item,
								cityVlaues);
						
						citytxt.setThreshold(1);
						
						citytxt.setAdapter(aa);
						citytxt
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
									
										citytxt.showDropDown();
									}
								});
						for(int i=0 ; i<cityVlaues.size(); i++)
						{
							if(cityVlaues.get(i).equals(UserDetails.getCity().toString()))
							{
								citytxt.setText(cityVlaues.get(i));
							}/*else
							{
								citytxt.setText("");
							}*/
						}
						
						selectedcity = citytxt.getText().toString();
						rootView.getRootView().setFocusable(true);
				  		rootView.getRootView().requestFocus();
					}

					@Override
					public void onErrorReceive(String string) {
						

					}
				}).execute();
	}
	
	public void registerUser() {
		selectedaltcontactnumber = altcontactnumber.getText().toString();
		selectedemailid =  email_id.getText().toString();
		selectedaddress = address.getText().toString();
		selectedstate = state.getText().toString();
	//	selecteddist = district.getText().toString();
		selectedcity = citytxt.getText().toString();
		selectedpincode = pincode.getText().toString();
		selectedcontactnumber = contactnumer.getText().toString();
		selectedgender = spingender.getSelectedItem().toString();
		String req = Config.awsserverurl+"tmsc_ch/customerapp/user/registeruser";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
		nameValuePairs.add(new BasicNameValuePair("first_name",
				UserDetails.getFirst_name()));
		nameValuePairs
				.add(new BasicNameValuePair("last_name", UserDetails.getLast_name()));
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
		nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails.getEmail_id()));
		nameValuePairs.add(new BasicNameValuePair("password", ""));
		nameValuePairs.add(new BasicNameValuePair("profile_image_link", UserDetails.getPhotourl()));
		
		new AWS_WebServiceCall(getActivity(), req,
				ServiceHandler.POST, Constants.registeruser, nameValuePairs,
				new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						boolean register = (boolean) object;
						Log.d("Here", "Register ");
						if (register) {
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
							Toast.makeText(getActivity(),
									"Registration succesful.",
									Toast.LENGTH_LONG).show();
							//Log.d("Success", "Register ");

							FragmentManager fm = getFragmentManager();
							FragmentTransaction tx = fm.beginTransaction();
							tx.replace(R.id.frame_container, new HomeFragment())
									.commit();
						} else {
							Log.d("Here", "Register ");

							Toast.makeText(
									getActivity(),
									"Registration ussuccesful, please try again",
									Toast.LENGTH_LONG).show();
						}

					}

					@Override
					public void onErrorReceive(String string) {
						

					}

				}).execute();

	}
	

	/*
	public String compressImage(String imageUri) {

		String filePath = getRealPathFromURI(imageUri);
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();

		// by setting this field as true, the actual bitmap pixels are not
		// loaded in the memory. Just the bounds are loaded. If
		// you try the use the bitmap here, you will get null.
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;

		// max Height and width values of the compressed image is taken as
		// 816x612

		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		// width and height values are set maintaining the aspect ratio of the
		// image

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		// setting inSampleSize value allows to load a scaled down version of
		// the original image

		options.inSampleSize = calculateInSampleSize(options, actualWidth,
				actualHeight);

		// inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;

		// this options allow android to claim the bitmap memory if it runs low
		// on memory
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			// load the bitmap from its path
			bmp = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
					Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
				middleY - bmp.getHeight() / 2, new Paint(
						Paint.FILTER_BITMAP_FLAG));

		// check the rotation of the image and display it properly
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
					scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
					true);
			profileImage.setImageBitmap(scaledBitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);

			// write the compressed bitmap at the destination specified by
			// filename.
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG,40, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return filename;

	}

	public String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "Images/*");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/"
				+ System.currentTimeMillis() + ".jpg");
		return uriSting;

	}

	private String getRealPathFromURI(String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null,
				null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int index = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(index);
		}
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;
		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}*/
	
	private void performCrop(Uri picUri) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
      
		
			// indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            
        	Log.v("customer profile pic", anfe.toString());
        	Toast toast = Toast.makeText(getActivity(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
        
        catch (Exception e) {
		//	e.printStackTrace();
         	Log.v("exception", e.toString());
       
        }
        
    }
}
