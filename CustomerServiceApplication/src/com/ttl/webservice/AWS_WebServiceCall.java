package com.ttl.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.ttl.adapter.ResponseCallback;
import com.ttl.customersocialapp.R;
import com.ttl.model.LabourModel;
import com.ttl.model.LabourRateModel;
import com.ttl.model.ProductBroucher;
import com.ttl.model.ServiceHandler;
import com.ttl.model.ServiceHistory;
import com.ttl.model.SpareModel;
import com.ttl.model.UserDetails;
import com.ttl.model.VehicleAMC_ParentRow;
import com.ttl.model.VehicleAgreement_ParentRow;

public class AWS_WebServiceCall extends AsyncTask<Void, Void, Void> {

	private String user_id, password, photoUrl;
	private String req, error, response;
	private List<NameValuePair> nameValuePairs;
	private Dialog dialog;
	private boolean dataReceived = false;
	private Context context;
	private ResponseCallback rc;
	private String webservice;
	private int servicetype;
	private String success_msg=null;
	private ArrayList<ProductBroucher> productBrouchersModel;
	private List<String> listvalues = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> hashmaplist = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, ArrayList<VehicleAMC_ParentRow>>> amclist = new ArrayList<HashMap<String, ArrayList<VehicleAMC_ParentRow>>>();
	private ArrayList<HashMap<String, ArrayList<VehicleAgreement_ParentRow>>> agreelist = new ArrayList<HashMap<String, ArrayList<VehicleAgreement_ParentRow>>>();
	public static ArrayList<String> typelist = new ArrayList<>();
	private Bitmap bitmap = null;
	private ArrayList<ServiceHistory> lst_history;
	private HashMap<String, String> notificationlist = new HashMap<String, String>();
	private ArrayList<LabourModel> data_list;
	private ArrayList<SpareModel> spare_list;
	private ArrayList<LabourRateModel> labour_rate_model_list=new ArrayList<>();
	public static ArrayList<HashMap<String, String>> notificationhashmaplist = new ArrayList<HashMap<String, String>>();

	public AWS_WebServiceCall(Context context, String req, int servicetype,
			String webservice, List<NameValuePair> nameValuePairs,
			ResponseCallback responseCallback) {
		this.context = context;
		this.req = req;

		this.nameValuePairs = new ArrayList<NameValuePair>(
				nameValuePairs.size());
		this.nameValuePairs = nameValuePairs;
		this.rc = responseCallback;
		this.webservice = webservice;
		this.servicetype = servicetype;
		typelist = new ArrayList<String>();
	}

	public AWS_WebServiceCall(Context context, String req, int servicetype,
			String webservice, ResponseCallback responseCallback) {
		this.context = context;
		this.req = req;

		this.nameValuePairs = new ArrayList<NameValuePair>(0);
		this.rc = responseCallback;
		this.webservice = webservice;
		this.servicetype = servicetype;

	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		notificationhashmaplist = new ArrayList<HashMap<String, String>>();
		if (webservice.equalsIgnoreCase(Constants.insertServiceBookingHistory)
				|| webservice.equalsIgnoreCase(Constants.setReadFlag)) {
			

		} else {
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.progress_bar);
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.setCancelable(false);
			dialog.show();
		}
	
		Log.v("inside","preexecute");
	}

	@Override
	protected Void doInBackground(Void... params) {

		
		
		try {
		ServiceHandler sh = new ServiceHandler();
		String jsonStr = null;
		Log.v("inside","doInBackground");
		if (nameValuePairs.size() == 0)
			jsonStr = sh.makeServiceCall(req, servicetype);
		else {
			jsonStr = sh.makeServiceCall(req, servicetype, nameValuePairs);
		}

		Log.v("json response", jsonStr);
		if (jsonStr != null) {
			if (jsonStr.contains("Session Expired")) {
				Config.sessionExpired = jsonStr.toString();
				Log.v("logout dialog", "inside logout session expire dialog ");
				Log.v("session expired",Config.sessionExpired);

				publishProgress();
				Log.v(context.getClass().getPackage().getName(),
						"dialog called");
			} 
			
			else if(jsonStr.contains("Could not connect to server.Please check your network connection."))
			{
				
				error = "Could not connect to server.Please check your network connection.";
				dataReceived=false;
				
			}	
			
			else {

				try {

					Log.v("return insert response", jsonStr);

					if (webservice.equals(Constants.user)) {
						if (jsonStr.contains("Forbidden Content")) {
							dataReceived = false;
							error = jsonStr;
						} else {
							JSONObject user = new JSONObject(jsonStr);
							JSONObject result = user.getJSONObject("result");
							JSONObject c = result.getJSONObject("user");

							if (c.has("message")
									&& c.getString("message").contains(
											"Success")) {

								UserDetails.setFirst_name(c
										.getString("first_name"));
								UserDetails.setLast_name(c
										.getString("last_name"));
								UserDetails.setGender(c.getString("gender"));
								UserDetails
										.setEmail_id(c.getString("email_id"));
								UserDetails.setContact_number(c
										.getString("contact_number"));
								UserDetails.setAlt_contact_number(c
										.getString("alt_contact_number"));
								UserDetails.setAddress(c.getString("address"));
								UserDetails.setCity(c.getString("city"));
								UserDetails.setPincode(c.getString("pincode"));
								UserDetails
										.setDistrict(c.getString("district"));
								UserDetails.setState(c.getString("state"));
								UserDetails.setUser_id(c.getString("user_id"));

								UserDetails.setPhotourl(c
										.getString("profile_image_link"));
								UserDetails.setPreffered_dealer_name(c
										.getString("preffered_dealer_name"));
								UserDetails.setPreffered_dealer_address(c
										.getString("preffered_dealer_address"));
								UserDetails.setPreffered_dealer_number(c
										.getString("preffered_dealer_number"));
								UserDetails.setPreffered_dealer_email(c
										.getString("preffered_dealer_email"));
								UserDetails
										.setPreffered_dealer_latitude(c
												.getString("preffered_dealer_latitude"));
								UserDetails
										.setPreffered_dealer_longitude(c
												.getString("preffered_dealer_longitude"));
								UserDetails.setSeeionId(c
										.getString("sessionId"));
								Config.sessionId = c.getString("sessionId");
								Config.setSAMLARTIFACT(c.getString("SAMLART"));
								Config.appVersion = c.getString("appVersion");
								Config.success = c.getString("message");
								dataReceived = true;
								Config.attempt=null;
								Config.pwdExpire=null;
								
							} else if (c.has("message")
									&& c.getString("message").contains(
											"Reset password")) {

								UserDetails.setFirst_name(c
										.getString("first_name"));
								UserDetails.setLast_name(c
										.getString("last_name"));
								UserDetails.setGender(c.getString("gender"));
								UserDetails
										.setEmail_id(c.getString("email_id"));
								UserDetails.setContact_number(c
										.getString("contact_number"));
								UserDetails.setAlt_contact_number(c
										.getString("alt_contact_number"));
								UserDetails.setAddress(c.getString("address"));
								UserDetails.setCity(c.getString("city"));
								UserDetails.setPincode(c.getString("pincode"));
								UserDetails
										.setDistrict(c.getString("district"));
								UserDetails.setState(c.getString("state"));
								UserDetails.setUser_id(c.getString("user_id"));

								UserDetails.setPhotourl(c
										.getString("profile_image_link"));
								UserDetails.setPreffered_dealer_name(c
										.getString("preffered_dealer_name"));
								UserDetails.setPreffered_dealer_address(c
										.getString("preffered_dealer_address"));
								UserDetails.setPreffered_dealer_number(c
										.getString("preffered_dealer_number"));
								UserDetails.setPreffered_dealer_email(c
										.getString("preffered_dealer_email"));
								UserDetails
										.setPreffered_dealer_latitude(c
												.getString("preffered_dealer_latitude"));
								UserDetails
										.setPreffered_dealer_longitude(c
												.getString("preffered_dealer_longitude"));
								UserDetails.setSeeionId(c
										.getString("sessionId"));
								Config.sessionId = c.getString("sessionId");
								Config.setSAMLARTIFACT(c.getString("SAMLART"));
								Config.appVersion = c.getString("appVersion");
								Config.resetpassword = c.getString("message");
								dataReceived = true;
								
							
								Config.attempt=null;
								Config.pwdExpire=null;
								
							
							}

							else if (c.has("pwdExpire")
									&& c.getString("pwdExpire").contains(
											"Maximum logins reached")) {
							/*	UserDetails.setFirst_name(c
										.getString("first_name"));
								UserDetails.setLast_name(c
										.getString("last_name"));
								UserDetails.setGender(c.getString("gender"));
								UserDetails
										.setEmail_id(c.getString("email_id"));
								UserDetails.setContact_number(c
										.getString("contact_number"));
								UserDetails.setAlt_contact_number(c
										.getString("alt_contact_number"));
								UserDetails.setAddress(c.getString("address"));
								UserDetails.setCity(c.getString("city"));
								UserDetails.setPincode(c.getString("pincode"));
								UserDetails
										.setDistrict(c.getString("district"));
								UserDetails.setState(c.getString("state"));
								UserDetails.setUser_id(c.getString("user_id"));

								UserDetails.setPhotourl(c
										.getString("profile_image_link"));
								UserDetails.setPreffered_dealer_name(c
										.getString("preffered_dealer_name"));
								UserDetails.setPreffered_dealer_address(c
										.getString("preffered_dealer_address"));
								UserDetails.setPreffered_dealer_number(c
										.getString("preffered_dealer_number"));
								UserDetails.setPreffered_dealer_email(c
										.getString("preffered_dealer_email"));
								UserDetails
										.setPreffered_dealer_latitude(c
												.getString("preffered_dealer_latitude"));
								UserDetails
										.setPreffered_dealer_longitude(c
												.getString("preffered_dealer_longitude"));
								UserDetails.setSeeionId(c
										.getString("sessionId"));
								Config.sessionId = c.getString("sessionId");
								Config.setSAMLARTIFACT(c.getString("SAMLART"));
								Config.appVersion = c.getString("appVersion");
								Config.pwdExpire = c.getString("pwdExpire");
								dataReceived = true;*/
								
								
								
								Config.pwdExpire = c.getString("pwdExpire");
								dataReceived = true;
								Config.attempt=null;
								
								
								
								
							} else if (c.has("attempt")
									&& c.getString("attempt").contains(
											"attempt")) {

								
								
								Config.attempt = c.getString("attempt");
								dataReceived = true;
								
							
							}
													
							

						}
					} else if (webservice.equals(Constants.afterregisteruser)) {
						if (jsonStr.contains("Forbidden Content")) {
							dataReceived = false;
							error = jsonStr;
						} else {
							JSONObject user = new JSONObject(jsonStr);
							JSONObject result = user.getJSONObject("result");
							JSONObject c = result.getJSONObject("user");

							UserDetails
									.setFirst_name(c.getString("first_name"));
							UserDetails.setLast_name(c.getString("last_name"));
							UserDetails.setGender(c.getString("gender"));
							UserDetails.setEmail_id(c.getString("email_id"));
							UserDetails.setContact_number(c
									.getString("contact_number"));
							UserDetails.setAlt_contact_number(c
									.getString("alt_contact_number"));
							UserDetails.setAddress(c.getString("address"));
							UserDetails.setCity(c.getString("city"));
							UserDetails.setPincode(c.getString("pincode"));
							UserDetails.setDistrict(c.getString("district"));
							UserDetails.setState(c.getString("state"));
							UserDetails.setUser_id(c.getString("user_id"));

							UserDetails.setPhotourl(c
									.getString("profile_image_link"));
							UserDetails.setPreffered_dealer_name(c
									.getString("preffered_dealer_name"));
							UserDetails.setPreffered_dealer_address(c
									.getString("preffered_dealer_address"));
							UserDetails.setPreffered_dealer_number(c
									.getString("preffered_dealer_number"));
							UserDetails.setPreffered_dealer_email(c
									.getString("preffered_dealer_email"));
							UserDetails.setPreffered_dealer_latitude(c
									.getString("preffered_dealer_latitude"));
							UserDetails.setPreffered_dealer_longitude(c
									.getString("preffered_dealer_longitude"));
							UserDetails.setSeeionId(c.getString("sessionId"));
							Config.sessionId = c.getString("sessionId");
							Config.setSAMLARTIFACT(c.getString("SAMLART"));
							Config.appVersion = c.getString("appVersion");

							dataReceived = true;
						}
					}

					else if (webservice.equals(Constants.registeruser)) {
						if (jsonStr.contains("Record Inserted")) {
							dataReceived = true;

						} else {
							Log.v("register", "response");
							dataReceived = false;
							error = jsonStr;
						}
					} else if (webservice.equals(Constants.resetPassword)) {
						if (jsonStr.contains("Password changed successfully")) {
							Config.resetpasswordtoast = jsonStr.toString();
							dataReceived = true;

						} else {
							dataReceived = false;
							error = jsonStr;
							Config.resetpasswordtoast = jsonStr.toString();
						}
					} else if (webservice.equals(Constants.gcmuser)) {
						if (!(jsonStr.equals(""))) {
							dataReceived = true;
							response = jsonStr;
						} else
							dataReceived = false;
					} else if (webservice.equals(Constants.updateUserDetails)) {
						if (jsonStr.contains("Record Updated")) {
							dataReceived = true;

						} else
							dataReceived = false;
					} else if (webservice.equals(Constants.getServiceTypes)) {
						JSONArray types = new JSONArray(jsonStr);
						for (int i = 0; i < types.length(); i++) {
							JSONObject type = types.getJSONObject(i);
							String service = type.getString("serviceType");
							listvalues.add(service);
							dataReceived = true;

						}

					} else if (webservice.equals(Constants.getKms)) {
						JSONArray types = new JSONArray(jsonStr);

						for (int i = 0; i < types.length(); i++) {
							JSONObject type = types.getJSONObject(i);
							String service = type.getString("kms_period");
							listvalues.add(service);
							dataReceived = true;

						}

					} else if (webservice.equals(Constants.getState)) {
						JSONArray types = new JSONArray(jsonStr);

						for (int i = 0; i < types.length(); i++) {
							JSONObject type = types.getJSONObject(i);
							String service = type.getString("state");
							listvalues.add(service);
							dataReceived = true;

						}

					} else if (webservice.equals(Constants.getCityFromState)) {
						
						Log.e(webservice, jsonStr);
						JSONArray types = new JSONArray(jsonStr);

						for (int i = 0; i < types.length(); i++) {
							JSONObject type = types.getJSONObject(i);
							String service = type.getString("city");
							listvalues.add(service);
							dataReceived = true;

						}

					} else if (webservice
							.equals(Constants.getCityFromStateMaster)) {
						Log.e(webservice, jsonStr);
						JSONArray types = new JSONArray(jsonStr);

						for (int i = 0; i < types.length(); i++) {
							JSONObject type = types.getJSONObject(i);
							String service = type.getString("city");
							listvalues.add(service);
							dataReceived = true;

						}

					} else if (webservice
							.equals(Constants.getFreeServiceCostEstimate)) {
						JSONArray types = new JSONArray(jsonStr);
						
						for (int i = 0; i < types.length(); i++) {
							JSONObject type = types.getJSONObject(i);
							String cost = type.getString("cost");
							String ctype = type.getString("costType");

							HashMap<String, String> costdetails = new HashMap<String, String>();
							costdetails.put("cost", cost);
							costdetails.put("costType", ctype);
							hashmaplist.add(costdetails);
							dataReceived = true;

						}

					} else if (webservice
							.equals(Constants.getPaidServiceCostEstimate)) {
						JSONArray types = new JSONArray(jsonStr);

						for (int i = 0; i < types.length(); i++) {
							JSONObject type = types.getJSONObject(i);
							String cost = type.getString("cost");
							String ctype = type.getString("costType");

							HashMap<String, String> costdetails = new HashMap<String, String>();
							costdetails.put("cost", cost);
							costdetails.put("costType", ctype);
							hashmaplist.add(costdetails);
							dataReceived = true;

						}

					}

					else if (webservice.equals(Constants.insertVehicleDetails)) {
						
						Log.v("insert vehicle result", jsonStr);
						if (jsonStr.contains("Record Inserted")) {
							dataReceived = true;

						} else
							dataReceived = false;
					} else if (webservice
							.equals(Constants.getVehicleDetailsByUserId)) {
						JSONArray vehicle = new JSONArray(jsonStr);
						// Log.d("Length", vehicle.length() + "");
						if (!(vehicle.equals(""))) {
							for (int i = 0; i < vehicle.length(); i++) {
								JSONObject type = vehicle.getJSONObject(i);
								String chassis = type.getString("chassis_num");
								String reg = type.getString("registration_num");
								String pl = type.getString("PL");
								String engine_num = type
										.getString("engine_num");
								String fuel_type = type.getString("fuel_type");
								String PPL = type.getString("PPL");
								String color = type.getString("color");
								String sale_date = type.getString("sale_date");
								String warranty_end_date = type
										.getString("warranty_end_date");
								String last_service_date = type
										.getString("last_service_date");
								String last_service_kms = type
										.getString("last_service_kms");
								String last_service_dealer = type
										.getString("last_service_dealer");
								String selling_dealer = type
										.getString("selling_dealer");
								String selling_dealer_code = type
										.getString("selling_dealer_code");
								String dealer_city = type
										.getString("dealer_city");
								String insurance_company_name = type
										.getString("insurance_company_name");
								String policy_num = type
										.getString("policy_num");
								String policy_start_date = type
										.getString("policy_start_date");
								String policy_end_date = type
										.getString("policy_end_date");
								String extended_warranty_flag = type
										.getString("extended_warranty_flag");
								String ew_policy_num = type
										.getString("ew_policy_num");
								String ew_start_date = type
										.getString("ew_start_date");
								String ew_end_date = type
										.getString("ew_end_date");
								String ew_start_km = type
										.getString("ew_start_km");
								String ew_end_km = type.getString("ew_end_km");
								String vehicle_image_url = type
										.getString("vehicle_image_url");
								String contact_fisrt_name = type
										.getString("contact_fisrt_name");
								String contact_last_name = type
										.getString("contact_last_name");
								String contact_cell_phone_num = type
										.getString("contact_cell_phone_num");
								String contact_address_line1 = type
										.getString("contact_address_line1");
								String next_service_due_date = type
										.getString("next_service_due_date");

								HashMap<String, String> details = new HashMap<String, String>();
								details.put("chassis_num", chassis);
								details.put("registration_num", reg);
								details.put("pl", pl);
								details.put("engine_num", engine_num);
								details.put("fuel_type", fuel_type);
								details.put("PPL", PPL);
								details.put("color", color);
								details.put("sale_date", sale_date);
								details.put("warranty_end_date",
										warranty_end_date);
								details.put("last_service_date",
										last_service_date);
								details.put("last_service_kms",
										last_service_kms);
								details.put("last_service_dealer",
										last_service_dealer);
								details.put("selling_dealer", selling_dealer);
								details.put("selling_dealer_code",
										selling_dealer_code);
								details.put("dealer_city", dealer_city);
								details.put("insurance_company_name",
										insurance_company_name);
								details.put("policy_num", policy_num);
								details.put("policy_start_date",
										policy_start_date);
								details.put("policy_end_date", policy_end_date);
								details.put("extended_warranty_flag",
										extended_warranty_flag);
								details.put("ew_policy_num", ew_policy_num);
								details.put("ew_start_date", ew_start_date);
								details.put("ew_end_date", ew_end_date);
								details.put("ew_start_km", ew_start_km);
								details.put("ew_end_km", ew_end_km);
								details.put("vehicle_image_url",
										vehicle_image_url);
								details.put("contact_last_name",
										contact_last_name);
								details.put("contact_fisrt_name",
										contact_fisrt_name);
								details.put("contact_cell_phone_num",
										contact_cell_phone_num);
								details.put("contact_address_line1",
										contact_address_line1);
								details.put("next_service_due_date",
										next_service_due_date);

								hashmaplist.add(details);
								dataReceived = true;
								JSONArray aggrement = type
										.getJSONArray("agreementDetails");
								VehicleAgreement_ParentRow agreeParent = null;
								ArrayList<VehicleAgreement_ParentRow> lst_agreeParent = new ArrayList<VehicleAgreement_ParentRow>();
								for (int j = 0; j < aggrement.length(); j++) {
									JSONObject agtype = aggrement
											.getJSONObject(j).getJSONObject(
													"aggObj");
									agreeParent = new VehicleAgreement_ParentRow();
									agreeParent.setAgg_ID(agtype
											.getString("agreement_id"));
									agreeParent.setAgree_name(agtype
											.getString("agreement_name"));
									agreeParent.setStatus(agtype
											.getString("agreement_status"));
									agreeParent.setAgreement_amt(agtype
											.getString("agreement_amt"));
									agreeParent.setAgreement_no(agtype
											.getString("agreement_num"));
									agreeParent.setMech_avail(agtype
											.getString("mech_service_availed"));
									agreeParent
											.setMech_reamaing(agtype
													.getString("mech_service_remaining"));
									agreeParent
											.setTowing_avail(agtype
													.getString("towing_service_availed"));
									agreeParent
											.setTowing_reamaing(agtype
													.getString("towing_service_remaining"));
									lst_agreeParent.add(agreeParent);
									agreeParent = null;

								}
								HashMap<String, ArrayList<VehicleAgreement_ParentRow>> hashagreelist = new HashMap<String, ArrayList<VehicleAgreement_ParentRow>>();
								hashagreelist.put("Agreement", lst_agreeParent);
								agreelist.add(hashagreelist);
								/*
								 * Log.d("agreement size",
								 * lst_agreeParent.size()+"");
								 * if(lst_agreeParent.size()>0) { new
								 * UserDetails().setLst_agreeParent
								 * (lst_agreeParent); }else { new
								 * UserDetails().setLst_agreeParent( new
								 * ArrayList<VehicleAgreement_ParentRow>()); }
								 */
								// String amcobj = type.getString("amcObj");
								JSONArray amc = type.getJSONArray("amcDetails");
								ArrayList<VehicleAMC_ParentRow> lst_amcParent = new ArrayList<VehicleAMC_ParentRow>();
								VehicleAMC_ParentRow amcParent = null;
								for (int j = 0; j < amc.length(); j++) {
									JSONObject atype = amc.getJSONObject(j)
											.getJSONObject("amcObj");
									;
									amcParent = new VehicleAMC_ParentRow();
									amcParent.setAmc_no(atype
											.getString("amc_num"));
									amcParent.setAmc_type(atype
											.getString("amc_type"));
									amcParent.setStart_date(atype
											.getString("amc_start_date"));
									amcParent.setEnd_date(atype
											.getString("amc_end_date"));
									amcParent.setStart_km(atype
											.getString("amc_start_km"));
									amcParent.setEnd_km(atype
											.getString("amc_end_km"));
									amcParent.setDescription(atype
											.getString("amc_description"));
									amcParent.setAmc_status(atype
											.getString("amc_status"));
									lst_amcParent.add(amcParent);
									amcParent = null;
								}
								HashMap<String, ArrayList<VehicleAMC_ParentRow>> hashamclist = new HashMap<String, ArrayList<VehicleAMC_ParentRow>>();
								hashamclist.put("AMC", lst_amcParent);
								amclist.add(hashamclist);
								/*
								 * Log.d("agreement size",
								 * lst_amcParent.size()+"");
								 * 
								 * if(lst_amcParent.size()>0) {
								 * 
								 * UserDetails.setLst_amcParent(lst_amcParent);
								 * }else { UserDetails.setLst_amcParent( new
								 * ArrayList<VehicleAMC_ParentRow>());
								 * 
								 * }
								 */

							}

							UserDetails.setAllDataAMC(amclist);
							UserDetails.setAllDataagreement(agreelist);
						} else {
							// new UserDetails().setLst_amcParent( new
							// ArrayList<VehicleAMC_ParentRow>());
							// new UserDetails().setLst_agreeParent( new
							// ArrayList<VehicleAgreement_ParentRow>());

							dataReceived = false;
						}

					}

					else if (webservice
							.equals(Constants.getNotificationsForApp)) {
						String notification_type = null, banner_image = null, id, notification_typerecord, notification_title, notification_desc, notification_startDate, notification_endDate, notification_image, city, PPL, PL, kms_run, read_flag, ready_to_publish, all_cities_flag;
						JSONArray notification = new JSONArray(jsonStr);
					
						if (notification.length() != 0) {
							for (int i = 0; i < notification.length(); i++) {
								JSONObject ntf = notification.getJSONObject(i);

								if (ntf.length() != 0) {
									notificationlist = new HashMap<String, String>();
									

									notification_type = ntf
											.getString("notification_type");
					

									banner_image = ntf
											.getString("banner_image");


									typelist.remove(notification_type);
									typelist.add(notification_type);


									notificationlist.put("notification_type",
											notification_type);
									notificationlist.put("banner_image",
											banner_image);
									notificationhashmaplist
											.add(notificationlist);

									JSONArray record = ntf
											.getJSONArray("notification_record");
									if (record.length() != 0) {

										for (int j = 0; j < record.length(); j++) {
											JSONObject reobj = record
													.getJSONObject(j);
											if (reobj != null) {
												id = reobj.getString("id");
												notification_typerecord = reobj
														.getString("notification_type");
												notification_title = reobj
														.getString("notification_title");
												notification_desc = reobj
														.getString("notification_desc");


												/*
												 * city =
												 * reobj.getString("city"); PPL
												 * = reobj.getString("PPL"); PL
												 * = reobj.getString("PL");
												 * kms_run = reobj
												 * .getString("kms_run");
												 */
												read_flag = reobj
														.getString("read_flag");
												ready_to_publish = reobj
														.getString("ready_to_publish");
												all_cities_flag = reobj
														.getString("all_cities_flag");

												HashMap<String, String> notify = new HashMap<String, String>();

												notify.put("notification_type",
														notification_type);
												notify.put("banner_image",
														banner_image);
												notify.put("id", id);
												notify.put("notification_type",
														notification_typerecord);
												notify.put(
														"notification_title",
														notification_title);
												notify.put("notification_desc",
														notification_desc);
												/*
												 * notify.put(
												 * "notification_startDate",
												 * notification_startDate);
												 * notify
												 * .put("notification_endDate",
												 * notification_endDate);
												 */
												/*
												 * notify.put("notification_image"
												 * , notification_image);
												 */
												/*
												 * notify.put("city", city);
												 * notify.put("PL", PL);
												 * notify.put("kms_run",
												 * kms_run);
												 */
												notify.put("read_flag",
														read_flag);
												notify.put("ready_to_publish",
														ready_to_publish);
												notify.put("all_cities_flag",
														all_cities_flag);
												hashmaplist.add(notify);
											}
										}
									}
								}
							}

						}
						dataReceived = true;

					} /*
					 * else if
					 * (webservice.equals(Constants.getlatestnotification)) {
					 * String notification_type, id, notification_typerecord,
					 * notification_title, notification_image,
					 * notification_desc, notification_startDate,
					 * notification_endDate, city, PPL, PL, kms_run, read_flag,
					 * ready_to_publish, all_cities_flag; JSONArray notification
					 * = new JSONArray(jsonStr); Log.d(notification.length() +
					 * "", "latestnotificaion length"); if
					 * (notification.length() != 0) { for (int i = 0; i <
					 * notification.length(); i++) { JSONObject ntf =
					 * notification.getJSONObject(i); if (ntf.length() != 0) {
					 * Log.d(ntf.length() + "", "latestnotificaion length");
					 * notification_type = ntf .getString("notification_type");
					 * JSONArray record = ntf
					 * .getJSONArray("notification_record");
					 * Log.d("record length", "" + record.length());
					 * 
					 * if (record != null) { for (int j = 0; j <
					 * record.length(); j++) { JSONObject reobj = record
					 * .getJSONObject(j); if (reobj != null) { id =
					 * reobj.getString("id"); notification_typerecord = reobj
					 * .getString("notification_type"); notification_title =
					 * reobj .getString("notification_title"); notification_desc
					 * = reobj .getString("notification_desc");
					 * notification_startDate = reobj
					 * .getString("notification_startDate");
					 * notification_endDate = reobj
					 * .getString("notification_endDate");
					 * 
					 * notification_image = reobj
					 * .getString("notification_image");
					 * 
					 * city = reobj.getString("city"); PPL =
					 * reobj.getString("PPL"); PL = reobj.getString("PL");
					 * kms_run = reobj .getString("kms_run"); read_flag = reobj
					 * .getString("read_flag"); ready_to_publish = reobj
					 * .getString("ready_to_publish"); all_cities_flag = reobj
					 * .getString("all_cities_flag");
					 * 
					 * HashMap<String, String> notify = new HashMap<String,
					 * String>();
					 * 
					 * notify.put("notification_type", notification_type);
					 * notify.put("id", id); notify.put("notification_type",
					 * notification_typerecord);
					 * notify.put("notification_title", notification_title);
					 * notify.put("notification_desc", notification_desc);
					 * notify.put( "notification_startDate",
					 * notification_startDate);
					 * notify.put("notification_endDate", notification_endDate);
					 * 
					 * notify.put("notification_image", notification_image);
					 * notify.put("city", city); notify.put("PL", PL);
					 * notify.put("kms_run", kms_run); notify.put("read_flag",
					 * read_flag); notify.put("ready_to_publish",
					 * ready_to_publish); notify.put("all_cities_flag",
					 * all_cities_flag); hashmaplist.add(notify);
					 * 
					 * Log.d("Hashmap size", hashmaplist.size() + ""); } } } } }
					 * } dataReceived = true; }
					 */else if (webservice
							.equals(Constants.getlatestnotification)) {
						String notification_type, id, notification_typerecord, notification_title, notification_image, notification_desc, notification_startDate, notification_endDate, city, PPL, PL, kms_run, read_flag, ready_to_publish, all_cities_flag;
						JSONArray notification = new JSONArray(jsonStr);

						if (notification.length() != 0) {
							for (int i = 0; i < notification.length(); i++) {
								JSONObject ntf = notification.getJSONObject(i);
								if (ntf.length() != 0) {
									notification_type = ntf
											.getString("notification_type");
									notification_title = ntf
											.getString("notification_title");
									notification_desc = ntf
											.getString("notification_desc");
									notification_image = ntf
											.getString("banner_image");
									kms_run = ntf.getString("kms_run");
									read_flag = ntf.getString("read_flag");
									ready_to_publish = ntf
											.getString("ready_to_publish");
									all_cities_flag = ntf
											.getString("all_cities_flag");
									HashMap<String, String> getnotification = new HashMap<String, String>();
									getnotification.put("notification_type",
											notification_type);
									getnotification.put("notification_title",
											notification_title);
									getnotification.put("notification_desc",
											notification_desc);
									getnotification.put("banner_image",
											notification_image);
									getnotification.put("kms_run", kms_run);
									getnotification.put("read_flag", read_flag);
									getnotification.put("ready_to_publish",
											ready_to_publish);
									getnotification.put("all_cities_flag",
											all_cities_flag);
									hashmaplist.add(getnotification);
								}
							}
						}
						dataReceived = true;
					} else if (webservice
							.equals(Constants.getVehicleImageByPPL)) {
						JSONArray img = new JSONArray(jsonStr);

						for (int i = 0; i < img.length(); i++) {
							dataReceived = true;
							JSONObject vehicle_image_url = img.getJSONObject(i);
							String IMAGEURL = vehicle_image_url
									.getString("vehicle_image_url");
							IMAGEURL = IMAGEURL.replaceAll(" ", "%20");

							InputStream input = new java.net.URL(IMAGEURL)
									.openStream();

							bitmap = BitmapFactory.decodeStream(input);
						}
					} else if (webservice.equals(Constants.uploadProfileImage)) {

						JSONObject image = new JSONObject(jsonStr);
						photoUrl = image.getString("profile_image_link");
						if (!(photoUrl.equals(""))) {
							dataReceived = true;
						} else
							dataReceived = false;

					} else if (webservice.equals(Constants.setPrefferedDealer)) {

						if (jsonStr.equals("Preffered Dealer Added")) {
							dataReceived = true;
						} else
							dataReceived = false;

					} else if (webservice
							.equals(Constants.addGenericCustomerFeedback)) {
						if (jsonStr.contains("Feedback Added")) {
							dataReceived = true;

						} else
							dataReceived = false;
					} else if (webservice
							.equals(Constants.insertServiceBookingHistory)) {
						if (jsonStr.contains("Record Inserted")) {
							dataReceived = true;

						} else
							dataReceived = false;
					} else if (webservice
							.equals(Constants.insertComplaintHistory)) {
						if (jsonStr.contains("Record Inserted")) {
							dataReceived = true;

						} else
							dataReceived = false;
					} else if (webservice.equals(Constants.getComplaintHistory)) {
						// Log.i("JSONSTRING", jsonStr);
						JSONArray resultArray = new JSONArray(jsonStr);

						for (int i = 0; i < resultArray.length(); i++) {
							JSONObject resultObj = resultArray.getJSONObject(i);
							String registration_number = resultObj
									.getString("registration_number");
							String user_id = resultObj.getString("user_id");
							String complaint_number = resultObj
									.getString("complaint_number");
							String model = resultObj.getString("model");
							String complaint_date = resultObj
									.getString("complaint_date");
							String primary_complaint_area = resultObj
									.getString("primary_complaint_area");
							String sub_area = resultObj.getString("sub_area");
							String problem_area = resultObj
									.getString("problem_area");
							HashMap<String, String> details = new HashMap<String, String>();
							details.put("registration_number",
									registration_number);
							details.put("user_id", user_id);
							details.put("complaint_number", complaint_number);
							details.put("model", model);
							details.put("complaint_date", complaint_date);
							details.put("primary_complaint_area",
									primary_complaint_area);
							details.put("sub_area", sub_area);
							details.put("problem_area", problem_area);
							hashmaplist.add(details);
							dataReceived = true;

						}
					} else if (webservice
							.equals(Constants.getServiceBookingHistory)) {
						// Log.e("JSONSTRING", jsonStr);
						JSONArray resultArray = new JSONArray(jsonStr);

						for (int i = 0; i < resultArray.length(); i++) {
							JSONObject resultObj = resultArray.getJSONObject(i);
							String registration_number = resultObj
									.getString("registration_number");
							String user_id = resultObj.getString("user_id");
							String sr_number = resultObj.getString("sr_number");
							String model = resultObj.getString("model");
							String booked_date = resultObj
									.getString("booked_date");
							String booked_time = resultObj
									.getString("booked_time");
							String service_dealer = resultObj
									.getString("service_dealer");
							String service_type = resultObj
									.getString("service_type");
							String msv_flag = resultObj.getString("msv_flag");
							String kms = resultObj.getString("kms");

							HashMap<String, String> details = new HashMap<String, String>();
							details.put("registration_number",
									registration_number);
							details.put("user_id", user_id);
							details.put("sr_number", sr_number);
							details.put("model", model);
							details.put("booked_date", booked_date);
							details.put("booked_time", booked_time);
							details.put("service_dealer", service_dealer);
							details.put("service_type", service_type);
							details.put("msv_flag", msv_flag);
							details.put("kms", kms);
							hashmaplist.add(details);
							dataReceived = true;

						}
					}

					else if (webservice.equals(Constants.getJobCardList)) {
						JSONArray jobCards = new JSONArray(jsonStr);

						for (int i = 0; i < jobCards.length(); i++) {
							JSONObject jobCardsObj = jobCards.getJSONObject(i);
							String job_card_number = jobCardsObj
									.getString("job_card_number");
							String SR_CREATED_DATE = jobCardsObj
									.getString("sr_date");
							String SR_TYPE = jobCardsObj.getString("sr_type");

							HashMap<String, String> details = new HashMap<String, String>();
							details.put("job_card_number", job_card_number);
							details.put("SR_CREATED_DT", SR_CREATED_DATE);
							details.put("SR_TYPE", SR_TYPE);
							hashmaplist.add(details);
							dataReceived = true;

						}

					} else if (webservice.equals(Constants.addPSFFeedback)) {
					
						if (jsonStr.equalsIgnoreCase("Feedback Added")) {
							
							dataReceived = true;
							
						
						} else
							{Log.e("err response", jsonStr);
							dataReceived = false;
							}
					} else if (webservice
							.equals(Constants.sendPasswordToCustomerEmail)) {
						if (jsonStr.contains("Success")) {
							dataReceived = true;

						} else
							dataReceived = false;

					} else if (webservice
							.equals(Constants.getDetailsForPasswordResetByUserId)) {
						// JSONArray userDetailsArray = new JSONArray(jsonStr);

						// for(int i = 0 ; i<userDetailsArray.length(); i++)
						// {
						Log.v("else if", jsonStr);
						JSONObject userDetailsObj = new JSONObject(jsonStr);
						if (userDetailsObj.has("email_id")) {

							String email_id = userDetailsObj
									.getString("email_id");

							HashMap<String, String> details = new HashMap<String, String>();
							details.put("email_id", email_id);

							hashmaplist.add(details);
							dataReceived = true;

						} else if (userDetailsObj.has("tml_support_email")) {

							dataReceived = false;
							error = context.getResources().getString(
									R.string.invalid_user_id);
						}

						// }
					} else if (webservice
							.equals(Constants.GetServiceHistoryByChassis_CSB)) {

						JSONArray types = new JSONArray(jsonStr);

						lst_history = new ArrayList<ServiceHistory>();
						ServiceHistory history = null;
						for (int i = 0; i < types.length(); i++) {
							history = new ServiceHistory();
							JSONObject type = types.getJSONObject(i);
							history.setSR_HISTORY_NUM(type
									.getString("SR_HISTORY_NUM"));
							history.setCHASSIS_NO(type.getString("CHASSIS_NO"));
							history.setREG_NUM(type.getString("REG_NUM"));
							history.setCLOSE_DATE(type.getString("CLOSE_DATE"));
							history.setSERVICE_AT_DEALER(type
									.getString("SERVICE_AT_DEALER"));
							history.setODOMTR_RDNG(type
									.getString("ODOMTR_RDNG"));
							/* history.setCLOSE_DATE(type.getString("X_HOURS")); */
							history.setSR_TYPE(type.getString("SR_TYPE"));
							history.setORDER_ID(type.getString("ORDER_ID"));
							/* history.setCLOSE_DATE(type.getString("INVC_ID")); */
							history.setINVC_AMT(type.getString("INVC_AMT"));
							history.setCITY(type.getString("CITY"));
							history.setDEALER_CONTACT_NUMBER(type
									.getString("DEALER_CONTACT_NUMBER"));
							history.setINVOICE_STATUS(type
									.getString("INVOICE_STATUS"));
							dataReceived = true;
							lst_history.add(history);
						}

					} else if (webservice.equals(Constants.getReminderTypes)) {

						JSONArray types = new JSONArray(jsonStr);
						listvalues.add("Reminder type");
						for (int i = 0; i < types.length(); i++) {
							JSONObject type = types.getJSONObject(i);
							String reminderType = type
									.getString("reminderType");
							listvalues.add(reminderType);
							dataReceived = true;

						}
					} else if (webservice.equals(Constants.setVersions)) {
						if (jsonStr.contains("dbVersion")
								&& jsonStr.contains("appVersion")) {
							dataReceived = true;

						} else
							dataReceived = false;
					} else if (webservice.equals(Constants.setReadFlag)) {

						if (jsonStr.equals("true")) {
							dataReceived = true;
						} else
							dataReceived = false;

					} else if (webservice.equals(Constants.logout)) {

						if (jsonStr.equals("User logged out successfully")) {
							Config.logoutstring = jsonStr.toString();
							dataReceived = true;
						} else
							dataReceived = false;

					} else if (webservice.equals(Constants.getPSFNotifications)) {
						JSONArray resultArray = new JSONArray(jsonStr);

						for (int i = 0; i < resultArray.length(); i++) {
							JSONObject resultObj = resultArray.getJSONObject(i);
							String registration_num = resultObj
									.getString("registration_num");
							String sr_number = resultObj.getString("sr_number");
							String sr_type = resultObj.getString("sr_type");
							String sr_date = resultObj.getString("sr_date");
							String job_card_number = resultObj
									.getString("job_card_number");
							HashMap<String, String> getdetails = new HashMap<String, String>();
							getdetails
									.put("registration_num", registration_num);
							getdetails.put("sr_number", sr_number);
							getdetails.put("sr_type", sr_type);
							getdetails.put("sr_date", sr_date);
							getdetails.put("job_card_number", job_card_number);
							hashmaplist.add(getdetails);
							dataReceived = true;
						}
					}

					else if(webservice.equalsIgnoreCase(Constants.forgotUserId))
					{
						if (jsonStr.contains("Success")) {
							dataReceived = true;

						} else
						{	dataReceived = false;
						}
						
						
					}
					else if(webservice.equalsIgnoreCase(Constants.brochures))
					{
						
						JSONArray json_arr=new JSONArray(jsonStr);
						
						JSONObject json_obj=json_arr.getJSONObject(0);
						
						if (json_obj.has("msg")) {
							
							dataReceived=false;
							error=json_obj.getString("msg");
						}
						else
						{
						
							productBrouchersModel=new ArrayList<>();
							try {
								
								for(int index=0;index<json_arr.length();index++)
								{
							
									
									json_obj=json_arr.getJSONObject(index);
									
								
									ProductBroucher productBroucher=new ProductBroucher();
									
									System.out.println("brochure "+ json_obj.getString("modelName"));
									Log.v("updatedAt", json_obj.getString("updatedDt"));
									
									productBroucher.setProduct(json_obj.getString("modelName"));
									productBroucher.setIcon(json_obj.getString("imagePath"));
									productBroucher.setBrochure(json_obj.getString("pdfPath"));
									productBroucher.setWebsite(json_obj.getString("websiteLink"));
									productBroucher.setUpdated_at(json_obj.getString("updatedDt"));
									productBroucher.setCreated_at(json_obj.getString("createdDt"));
									productBrouchersModel.add(productBroucher);
								}
							
							} catch (Exception e) {
							e.printStackTrace();
							}
						
							
							
							
							
							dataReceived=true;
						
						}
					
						
						
						
						
					}
					else if(webservice.equalsIgnoreCase(Constants.getLabourDataByPPL))
					{
						Log.e("getLabourDataByPL", jsonStr);
						
						JSONArray obj_arr=new JSONArray(jsonStr);
						JSONObject object;
						
						
						data_list=new ArrayList<>();
						
						
						if (obj_arr.length()>0) {
							
							
							
							for (int i = 0; i < obj_arr.length(); i++) {
								
					
								object=obj_arr.getJSONObject(i);
								
								LabourModel model=new LabourModel();
								
								
								model.setBillingHours(object.getString("billingHours"));
								model.setDefaultQty(object.getString("defaultQty"));
								model.setLabourCode(object.getString("labourCode"));
								model.setModelName(object.getString("modelName"));
								model.setLabourDescription(object.getString("labourDescription"));
								model.setLabourType(object.getString("labourType"));
								
								
								data_list.add(model);
								
								
								
							}
							
							dataReceived=true;
							
						}
						else
						{
							error="No data found";
							dataReceived=false;
						}
						
						
						
						
						
						
						
					}
					else if(webservice.equalsIgnoreCase(Constants.getPartsDataByPPL))
					{
							Log.e("getPartsDataByPL", jsonStr);
						
						JSONArray obj_arr=new JSONArray(jsonStr);
						JSONObject object;
						
						
						spare_list=new ArrayList<>();
						
						
						
						if (obj_arr.length()>0) {
							
							
							
							for (int i = 0; i < obj_arr.length(); i++) {
								
					
								object=obj_arr.getJSONObject(i);
								
								SpareModel model=new SpareModel();
								
								
								model.setModelName(object.getString("modelName"));
								model.setDefaultQty(object.getString("defaultQty"));
								model.setPartDescription(object.getString("partDescription"));
								model.setPartNumber(object.getString("partNumber"));
								model.setUMRP(object.getString("UMRP"));
								model.setUOM(object.getString("UOM"));
								spare_list.add(model);
								
								
								
							}
							
							dataReceived=true;
							
						}
						else
						{
							error="No data found";
							dataReceived=false;
						}
						
						
						
						
					}
					
					else if(webservice.equalsIgnoreCase(Constants.getLabourRateByCity))
					{
						
						
						
						JSONObject object=new JSONObject(jsonStr);
						
						
						LabourRateModel rateModel=new LabourRateModel();
						
						if (object.has("city")) {
							rateModel.setCity(object.getString("city"));	
						}
						if (object.has("cityClass")) {
							rateModel.setCityClass(object.getString("cityClass"));	
						}
						
						
						if (object.has("ratePerHour")) {
							rateModel.setRatePerHour(Double.valueOf(object.getString("ratePerHour")));	
							dataReceived=true;
						}
												
						labour_rate_model_list.add(rateModel);
						
						
						
						
					}
					else if(webservice.equalsIgnoreCase(Constants.sendMailForManualCostEstimate))
					{
						
						
					
						
						JSONObject object=new JSONObject(jsonStr);
						
						if (object.has("msg")) {
							
							success_msg=object.getString("msg");
							
							dataReceived=true;
						}
						else
						{
							dataReceived=false;
							error="Mail could not be sent.";	
						}
						
							
					
						
						
					}
					
					
					
					
					
					
					
					

				} catch (JSONException | IOException e) {
				
					error = "Invalid credentials, please check username and passsword you entered.";
					dataReceived = false;
				}
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
			error = "No internet connection.";
			dataReceived = false;
		}
		
	
		}	catch (Exception hostException) {
		
			
			
			
			
		}
		
		
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		new Config().checkSessionExpired(context);

	}

	@Override
	protected void onPostExecute(Void result) {
		
		super.onPostExecute(result);
		if (webservice.equalsIgnoreCase(Constants.insertServiceBookingHistory)
				|| webservice.equalsIgnoreCase(Constants.setReadFlag)) {

		} else {
			dialog.dismiss();
		}
		
		if (dataReceived) {
			if (webservice.equals(Constants.user)) {
				rc.onResponseReceive(dataReceived);
			} else if (webservice.equals(Constants.registeruser)) {
				rc.onResponseReceive(dataReceived);
			} else if (webservice.equals(Constants.afterregisteruser)) {
				rc.onResponseReceive(dataReceived);
			} else if (webservice.equals(Constants.resetPassword)) {
				rc.onResponseReceive(dataReceived);
			} else if (webservice.equals(Constants.updateUserDetails)) {
				rc.onResponseReceive(dataReceived);
			} else if (webservice.equals(Constants.getServiceTypes)) {
				rc.onResponseReceive(listvalues);
			} else if (webservice.equals(Constants.getKms)) {
				rc.onResponseReceive(listvalues);
			} else if (webservice.equals(Constants.getState)) {
				rc.onResponseReceive(listvalues);
			} else if (webservice.equals(Constants.getCityFromState)) {
				rc.onResponseReceive(listvalues);
			} else if (webservice.equals(Constants.getCityFromStateMaster)) {
				rc.onResponseReceive(listvalues);
			} else if (webservice.equals(Constants.getFreeServiceCostEstimate)) {
				rc.onResponseReceive(hashmaplist);
			} else if (webservice.equals(Constants.getPaidServiceCostEstimate)) {
				rc.onResponseReceive(hashmaplist);
			} else if (webservice.equals(Constants.getNotificationsForApp)) {
				rc.onResponseReceive(hashmaplist);
			} else if (webservice.equals(Constants.getlatestnotification)) {
				Log.d("Notification size", hashmaplist.size() + "");
				rc.onResponseReceive(hashmaplist);
			} else if (webservice.equals(Constants.insertVehicleDetails)) {
				rc.onResponseReceive(dataReceived);
			} else if (webservice.equals(Constants.getVehicleDetailsByUserId)) {
				rc.onResponseReceive(hashmaplist);
			} else if (webservice.equals(Constants.getVehicleImageByPPL)) {
				rc.onResponseReceive(bitmap);
			} else if (webservice.equals(Constants.uploadProfileImage))
				rc.onResponseReceive(photoUrl);
			else if (webservice.equals(Constants.setPrefferedDealer)) {
				rc.onResponseReceive(dataReceived);
			} else if (webservice.equals(Constants.addGenericCustomerFeedback))
				rc.onResponseReceive(dataReceived);
			else if (webservice.equals(Constants.insertServiceBookingHistory))
				rc.onResponseReceive(dataReceived);
			else if (webservice.equals(Constants.insertComplaintHistory))
				rc.onResponseReceive(dataReceived);
			else if (webservice.equals(Constants.getServiceBookingHistory))
				rc.onResponseReceive(hashmaplist);
			else if (webservice.equals(Constants.getComplaintHistory))
				rc.onResponseReceive(hashmaplist);
			else if (webservice.equals(Constants.getJobCardList))
				rc.onResponseReceive(hashmaplist);
			else if (webservice.equals(Constants.addPSFFeedback))
			{
				Log.v("psf on response recieve", ""+dataReceived);
				rc.onResponseReceive(dataReceived);
		}
			else if (webservice.equals(Constants.sendPasswordToCustomerEmail))
				rc.onResponseReceive(dataReceived);
			else if (webservice
					.equals(Constants.getDetailsForPasswordResetByUserId)) {

				rc.onResponseReceive(hashmaplist);

			}
			else if(webservice.equals(Constants.forgotUserId))
			{
				rc.onResponseReceive(dataReceived);
			}
			
			else if (webservice
					.equals(Constants.GetServiceHistoryByChassis_CSB))
				rc.onResponseReceive(lst_history);
			else if (webservice.equals(Constants.getReminderTypes))
				rc.onResponseReceive(listvalues);
			else if (webservice.equals(Constants.setVersions))
				rc.onResponseReceive(dataReceived);
			else if (webservice.equals(Constants.setReadFlag))
				rc.onResponseReceive(dataReceived);
			else if (webservice.equals(Constants.logout))
				rc.onResponseReceive(dataReceived);
			else if (webservice.equals(Constants.getPSFNotifications))
				rc.onResponseReceive(hashmaplist);

			else if (webservice.equals(Constants.gcmuser))
				rc.onResponseReceive(response);
			else if(webservice.equalsIgnoreCase(Constants.brochures))
			{
				rc.onResponseReceive(productBrouchersModel);
			}
			else if(webservice.equalsIgnoreCase(Constants.getLabourDataByPPL))
			{
				
				rc.onResponseReceive(data_list);
				
			}
			else if(webservice.equalsIgnoreCase(Constants.getLabourRateByCity))
			{
				
				
				rc.onResponseReceive(labour_rate_model_list);
			}
			else if(webservice.equalsIgnoreCase(Constants.getPartsDataByPPL))
			{
				
				rc.onResponseReceive(spare_list);
				
			}
			else if(webservice.equalsIgnoreCase(Constants.sendMailForManualCostEstimate))
			{
				rc.onResponseReceive(success_msg);
			}
			
			
		}
		
		
		
		
		
		else {
			
			rc.onErrorReceive(error);
			
		}
	}

}
