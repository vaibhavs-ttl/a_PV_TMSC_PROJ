package com.ttl.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ttl.adapter.ResponseCallback;
//import com.ttl.communication.SamlArtifact;
import com.ttl.customersocialapp.DelearLocator_fragment;
import com.ttl.customersocialapp.HomeFragment;
import com.ttl.customersocialapp.R;
import com.ttl.customersocialapp.TakeRCbookImage;
import com.ttl.customersocialapp.VehicleDetails_Fragment;
import com.ttl.model.ServiceBookingUser;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
import com.ttl.webservice.WebServiceCall;

public class VehicleRegister {

	private Activity context;
	private Dialog vehiclenodialog, mobilenumberdialog,
			contactdealerdialog, otpdialog;
	private EditText regnumber, chassis;
	
	private String fromActivity = "";
	private final int CAMERA_CAPTURE = 1, SELECT_FILE = 2;
	public VehicleRegister(Activity context, String fromActivity) {
		this.context = context;
		this.fromActivity = fromActivity;
		register();
	}

	public void register()

	{
		vehiclenodialog = new Dialog(context);
		vehiclenodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		vehiclenodialog.setContentView(R.layout.vehicle_vari_regno_popup);
		vehiclenodialog.setCancelable(false);
		vehiclenodialog.show();
		ImageView close = (ImageView) vehiclenodialog
				.findViewById(R.id.imgclose);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				vehiclenodialog.dismiss();
				HomeFragment.regvehicle = false;
				// checkvehiclereg(HomeFragment.regvehicle);
			}
		});
		Button submit = (Button) vehiclenodialog.findViewById(R.id.visit_site);
		regnumber = (EditText) vehiclenodialog.findViewById(R.id.txtregnum);
		chassis = (EditText) vehiclenodialog.findViewById(R.id.txtchassisnum);
	
		regnumber.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before,
	                int count) {
	            
	            if (s.toString().equals("")) {
	            	chassis.setEnabled(true);
	            } else {
	            	chassis.setEnabled(false);

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
		
		chassis.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before,
	                int count) {
	            
	            if (s.toString().equals("")) {
	            	regnumber.setEnabled(true);
	            } else {
	            	regnumber.setEnabled(false);

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
		
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (!(regnumber.getText().toString().equals(""))
						|| !(chassis.getText().toString().equals(""))) {
					if (Config.getSAMLARTIFACT().equals("")) {
				
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle(context.getResources().getString(R.string.app_name));
						builder.setMessage("Your Session has expired, Please restart app.").setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										
										dialog.cancel();
									
										System.exit(0);
									}
								});
						AlertDialog alert = builder.create();
						alert.setCancelable(false);
						alert.show();
					}

				
					String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							+ "<SOAP:Body>"
							+ "<GetCustomerVehicleDetailsCSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
							+ "<RegistrationNumber>"
							+ regnumber.getText().toString()
									.toUpperCase(Locale.ENGLISH)
							+ "</RegistrationNumber>"
							+ "<ChassisNumber>"
							+ chassis.getText().toString()
									.toUpperCase(Locale.ENGLISH)
							+ "</ChassisNumber>"
							+ "<MobileNo></MobileNo>"
							+ "</GetCustomerVehicleDetailsCSB>"
							+ "</SOAP:Body>" + "</SOAP:Envelope>";

					new WebServiceCall(context, req,
							Constants.GetCustomerVehicleDetailsCSB,
							new ResponseCallback() {

								@Override
								public void onResponseReceive(Object object) {

									final ServiceBookingUser user = (ServiceBookingUser) object;
									if (!(user.getPhoneno().equals(""))) {
										vehiclenodialog.dismiss();

										mobilenumberdialog = new Dialog(context);
										mobilenumberdialog
												.requestWindowFeature(Window.FEATURE_NO_TITLE);
										mobilenumberdialog
												.setContentView(R.layout.vehicle_vari_mobno);
										mobilenumberdialog.setCancelable(false);
										mobilenumberdialog.show();

										ImageView close = (ImageView) mobilenumberdialog
												.findViewById(R.id.imgclose);
										close.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
				
												mobilenumberdialog.dismiss();
												HomeFragment.regvehicle = false;
				
											}
										});
										TextView forgot = (TextView) mobilenumberdialog
												.findViewById(R.id.forgotmob);
										forgot.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
				
												mobilenumberdialog.dismiss();
												contactDealer(user);
											}
										});

										TextView reg = (TextView) mobilenumberdialog
												.findViewById(R.id.txtregno);
										String regnumber = "(Reg.No: "
												+ user.getREGISTRATIONNUMBER()
												+ ")";
										reg.setText(regnumber);
										String mob = "(********"
												+ user.getPhoneno()
														.toString()
														.substring(
																user.getPhoneno()
																		.length() - 3)
												+ ") to proceed";
										TextView mobtext = (TextView) mobilenumberdialog
												.findViewById(R.id.txtmoilenumber);
										mobtext.setText(mob);

										final EditText mobile = (EditText) mobilenumberdialog
												.findViewById(R.id.edtmobileno);
										Button submitmob = (Button) mobilenumberdialog
												.findViewById(R.id.visit_site);
										submitmob
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
				
														String enteredmob = mobile
																.getText()
																.toString();
												
				
														if (enteredmob
																.equals(user
																		.getPhoneno()
																		.toString())) {
															otpGeneration(user);

														} else {
				
															mobile.setError("Please enter a correct Mobile Number");
															mobile.setFocusable(true);
														}
													}
												});
									} else {
				
										Toast.makeText(context, "Contact details not available against this Vehicle. Please get in touch with our Customer Care Team(18002097979) for furher assistance.", Toast.LENGTH_LONG).show();
									}
								}

								@Override
								public void onErrorReceive(String string) {

			
									if(string.equals("There is some issue with the service. Please try again later."))
									{
										Toast.makeText(
												context,
												"There is some issue with the service. Please try again later.",
												Toast.LENGTH_SHORT).show();
									}else
									{
									if(!(regnumber.getText().toString().equals("")))
									{
										regnumber.setError("Registration Number you have entered is not matching with our record. Please verify number with nearest Tata Dealer.");
										regnumber.setFocusable(true);
									}else
									{
										chassis.setError("Chassis Number you have entered is not matching with our record. Please verify number with nearest Tata Dealer.");
										chassis.setFocusable(true);
									}
									}
								}

							}, "Populating Data..").execute();
				}
				else{
				Toast.makeText(context,"Please enter Vehicle's Registration number or Chassis number", Toast.LENGTH_SHORT).show();
				}
				}
		});

		
	}

	public void otpGeneration(final ServiceBookingUser user) {
		
		
		String currenttime = System.currentTimeMillis() +"";
		final String ref = currenttime.substring(Math.max(currenttime.length() - 2, 0));
		final int otp = gen();

		
		
		String otpmessage = "OTP is "+otp+" for Msg ref # " +ref+
				" to register your vehicle "
				+ user.getREGISTRATIONNUMBER().toString()
				+ " in TMSC. Do not share OTP with anyone for security reasons.";
		String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<SOAP:Body>"
				+ "<bpmSendSMS xmlns=\"http://schemas.cordys.com/default\">"
				  
				+ "<PHNUMBER>"
				+ user.getPhoneno().toString()
				+ "</PHNUMBER>"
				+ "<MESSAGETEXT>"
				+ otpmessage
				+ "</MESSAGETEXT>"
				+ "<RULENAME>Customer App OTP</RULENAME>"
				+ "</bpmSendSMS>"
				+ "</SOAP:Body></SOAP:Envelope>";
		new WebServiceCall(context, req, Constants.bpmSendSMS,
				new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						boolean otpsent = (boolean) object;

						
						Log.v("is otp sent", ""+otpsent);
						
						if (otpsent) {
							mobilenumberdialog.dismiss();
							otpdialog = new Dialog(context);
							otpdialog
									.requestWindowFeature(Window.FEATURE_NO_TITLE);
							otpdialog
									.setContentView(R.layout.vehicle_vari_otpno);
							otpdialog.setCancelable(false);
							otpdialog.show();

							ImageView close = (ImageView) otpdialog
									.findViewById(R.id.imgclose);
							close.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									
									otpdialog.dismiss();
									HomeFragment.regvehicle = false;

								}
							});

							TextView regnum = (TextView) otpdialog
									.findViewById(R.id.txtregno);
							String regnumber = "(Reg.No: "
									+ user.getREGISTRATIONNUMBER() + ")";
							regnum.setText(regnumber);
							String refno = "Message Ref No: "+ref;
							TextView reftext = (TextView) otpdialog
									.findViewById(R.id.ref);
							reftext.setText(refno);
							String mob = "Mobile Number (*******"
									+ user.getPhoneno()
											.toString()
											.substring(
													user.getPhoneno().length() - 3)
									+ ")";
							TextView mobtext = (TextView) otpdialog
									.findViewById(R.id.custmobnum);
							mobtext.setText(mob);
							final EditText otptext = (EditText) otpdialog
									.findViewById(R.id.edtotpno);


							Button verifyotp = (Button) otpdialog
									.findViewById(R.id.visit_site);
							verifyotp.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									
									Log.v("generated otp", ""+otp);
									
									String otpstring = ""+otp;
									String enteredotp = otptext.getText().toString();


									if (enteredotp.toString().equals(otpstring)) {
										otpdialog.dismiss();

										if(UserDetails.getUser_id().equals(""))
										{
											AlertDialog.Builder builder = new AlertDialog.Builder(context);
											builder.setTitle(context.getResources().getString(R.string.app_name));
											builder.setMessage("Your Session has expired, Please restart app.").setPositiveButton("OK",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface dialog,
																int which) {
															
															dialog.cancel();

															System.exit(0);
														}
													});
											AlertDialog alert = builder.create();
											alert.setCancelable(false);
											alert.show();
										}else
											registerVehicle(user);
									} else {
										Toast.makeText(
												context,
												"OTP not matching please re-enter.",
												Toast.LENGTH_SHORT).show();
										HomeFragment.regvehicle = false;

									}
								}
							});
							Button resend = (Button) otpdialog
									.findViewById(R.id.btnresend);
							resend.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									
									otpGeneration(user);
									otpdialog.dismiss();
								}
							});
						}
					}

					@Override
					public void onErrorReceive(String string) {
						

					}
				}, "").execute();
	}

	private int gen() {
		Random r = new Random(System.currentTimeMillis());
		return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
	}

	public void registerVehicle(ServiceBookingUser user) {
		String req = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/insertVehicleDetails";
		String environment = "";
		String URL = context.getResources().getString(R.string.URL);

		if(URL.contains("qa"))
		{
			environment = "QA";
		}else
		{
			environment = "Production";
		}
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
		nameValuePairs.add(new BasicNameValuePair("chassis_num", user
				.getCHASSISNUMBER()));
		nameValuePairs.add(new BasicNameValuePair("registration_num", user
				.getREGISTRATIONNUMBER()));
		nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails
				.getUser_id()));
		nameValuePairs.add(new BasicNameValuePair("PL", user.getPL()));
		nameValuePairs.add(new BasicNameValuePair("environment",environment));
		nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
		Log.d("Input", user.getCHASSISNUMBER() + user.getPL());
		new AWS_WebServiceCall(context, req, ServiceHandler.POST,
				Constants.insertVehicleDetails, nameValuePairs,
				new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						boolean res = (boolean) object;
						if (res) {

							getVehicles();
							HomeFragment.regvehicle = true;
							Toast.makeText(context,
									"Congratulations. Your vehicle has been added. To see the details, please Select Vehicle from the dropdown.",
									Toast.LENGTH_LONG).show();

						} else {
							Toast.makeText(context,
									"Vehicle not added please try again.",
									Toast.LENGTH_LONG).show();
						}

					}

					@Override
					public void onErrorReceive(String string) {
						
						Toast.makeText(context,
								"Vehicle not added please try again.",
								Toast.LENGTH_LONG).show();
					
					}

				}).execute();

	}

	public void getVehicles() {
		String req = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/getVehicleDetailsByUserId"
				;
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
		    nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
		new AWS_WebServiceCall(context, req, ServiceHandler.POST,
				Constants.getVehicleDetailsByUserId,nameValuePairs, new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						

						new UserDetails()
								.setRegNumberList((ArrayList<HashMap<String, String>>) object);
						HomeFragment.regvehicle = true;
						FragmentManager fragmentManager = context
								.getFragmentManager();
						Fragment fragment = new VehicleDetails_Fragment();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment)
								.commit();
					

					}

					@Override
					public void onErrorReceive(String string) {
						
					
						HomeFragment.regvehicle = false;
					

					}

				}).execute();
	}

	ImageView rcImage;
	Bitmap bitmap;
	String encodedString;
	String imgPath, fileName;
	public static String regnumber1="";
	public static String chassis1 = "";
	public void contactDealer(ServiceBookingUser user) {
		contactdealerdialog = new Dialog(context);
		contactdealerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		contactdealerdialog.setContentView(R.layout.vehicle_reg_contactcusto);
		contactdealerdialog.setCancelable(true);
		contactdealerdialog.show();

		TextView regnum = (TextView) contactdealerdialog
				.findViewById(R.id.txtregno);
		 VehicleRegister.regnumber1 = user.getREGISTRATIONNUMBER();
		 VehicleRegister.chassis1 = user.getCHASSISNUMBER();
		regnum.setText("(Reg.No: " + user.getREGISTRATIONNUMBER() + ")");
		Button dealer = (Button) contactdealerdialog
				.findViewById(R.id.btndealer);
		ImageView close = (ImageView) contactdealerdialog
				.findViewById(R.id.imgclose);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				contactdealerdialog.dismiss();
				HomeFragment.regvehicle = false;
			
			}
		});
		dealer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				FragmentManager fragmentManager = context.getFragmentManager();
				fragmentManager
						.beginTransaction()
						.replace(R.id.frame_container,
								new DelearLocator_fragment())
						.addToBackStack(null).commit();
				contactdealerdialog.dismiss();
			}
		});

		Button customer = (Button) contactdealerdialog
				.findViewById(R.id.btncustomer);

		customer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				FragmentManager fragmentManager = context.getFragmentManager();
				fragmentManager
						.beginTransaction()
						.replace(R.id.frame_container,
								new TakeRCbookImage())
						.addToBackStack(null).commit();
				contactdealerdialog.dismiss();
			
			}
		});

	}

	    
	    
}
