package com.ttl.webservice;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.ttl.adapter.ResponseCallback;
import com.ttl.customersocialapp.R;
import com.ttl.model.ComplaintAndJCDescripti;
import com.ttl.model.ComplaintArea;
import com.ttl.model.Dealer;
import com.ttl.model.DelearLocatorData;
import com.ttl.model.ServiceBookingUser;
import com.ttl.model.ServiceComplaintDesc;
import com.ttl.model.ServiceHistory;
import com.ttl.model.VehcontactScheduler;
import com.ttl.model.VehicleAMC_ParentRow;
import com.ttl.model.VehicleAgreement_ParentRow;

public class WebServiceCall extends AsyncTask<Void, Void, Boolean> {
	private Context context;
	private String requestString;

	private String dial_message;
	private String webservice;
	private ServiceBookingUser user = new ServiceBookingUser();

	private ArrayList<String> list = new ArrayList<>();
	private ResponseCallback rc;
	private ArrayList<Dealer> dealers = new ArrayList<Dealer>();
	private String dealercntctNum = null;

	private ArrayList<ServiceComplaintDesc> lst_c_desc;// = new
	// ArrayList<ServiceComplaintDesc>();
	private ArrayList<ComplaintArea> lst_c_area;
	private ArrayList<ServiceHistory> lst_history;
	private String err_message = "Data not available.";

	private DelearLocatorData delear = null;
	private ArrayList<String> city;
	private ArrayList<String> stateVlaues = new ArrayList<String>();
	private ArrayList<String> distVlaues = new ArrayList<String>();
	private ArrayList<String> cityVlaues = new ArrayList<String>();

	private ArrayList<DelearLocatorData> delearList;
	private Dialog dialog;
	private ArrayList<ComplaintAndJCDescripti> lst_complaintandJC;
	private VehcontactScheduler vehdetails = null;
	private ArrayList<VehicleAMC_ParentRow> lst_amcParent;// = new
	// VehicleAMC_ParentRow()
	private ArrayList<VehicleAgreement_ParentRow> lst_agreeParent;
	private HashMap<String, String> dealerdetails = new HashMap<String, String>();

	public WebServiceCall(Activity activity, String req, String webservice,
			ResponseCallback responseCallback, String string) {

		super();
		this.context = activity;
		this.requestString = req;
		this.webservice = webservice;
		this.rc = responseCallback;
		this.dial_message = string;

		// new SamlArtifact(context).execute();
	}

	// public WebServiceCall(Activity activity, String req, String webservice,
	// String string) {
	
	// super();
	// this.context = activity;
	// this.requestString = req;
	// this.webservice = webservice;
	// this.dial_message = string;
	// }

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		// Log.d("doInBackground", requestString + Config.getSAMLARTIFACT());
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progress_bar);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.show();
		/*
		 * mProgressDialog = ProgressDialog.show(context, context.getResources()
		 * .getString(R.string.app_name), dial_message, true, true, new
		 * OnCancelListener() {
		 * 
		 * @Override public void onCancel(DialogInterface dialog) {
		 * cancel(true); mProgressDialog.dismiss(); } });
		 */
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.v("Config.getSAMLARTIFACT()", Config.getSAMLARTIFACT());
		String url = context.getResources().getString(R.string.URL);
		
	
		boolean dataReceived = false;
		

	

		String xml = null;
		try {

			/*
			 * soapConnection sc = new soapConnection(url,
			 * requestString.getBytes(), context);
			 * 
			 * xml = sc.getResponce();
			 */

			xml = new CordysCall().getResponse(context.getResources()
					.getString(R.string.URL), Config.getSAMLARTIFACT(),
					requestString);

		} catch (Exception e) {
			e.printStackTrace();
		}

	//	Log.v("Response: ", xml);
		try {
			
			if(xml!=null)
			{
				
			

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xmlParser = factory.newPullParser();
			xmlParser.setInput(new StringReader(xml));
			int eventType = xmlParser.getEventType();
			// serach for fragment by tag
			if (webservice.equals(Constants.GetCustomerVehicleDetailsCSB)) {
				user = new ServiceBookingUser();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						// S_Asset REGISTRATIONNUMBER CHASSISNUMBER CONTACTID
						// MIDDLENAME
						if (tagName.equalsIgnoreCase("S_Asset")) {
							dataReceived = true;
						} else if (tagName
								.equalsIgnoreCase("REGISTRATIONNUMBER")) {
							user.setREGISTRATIONNUMBER(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("CHASSISNUMBER")) {
							user.setCHASSISNUMBER(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("CONTACTID")) {
							user.setContact_id(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("FIRSTNAME")) {
							user.setFname(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("LASTNAME")) {
							user.setLname(xmlParser.nextText());
						} else if (tagName
								.equalsIgnoreCase("PHONE_NUMBER_CELL")) {
							user.setPhoneno(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("ADDRESS_LINE_1")) {
							user.setAddress(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("EMAILID")) {
							user.setEmail(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("PL")) {
							user.setPL(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("PPL")) {
							user.setPPL(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					}
					eventType = xmlParser.next();
				}

			} else if (webservice.equals(Constants.GetDSSDealerCitiesCSB)) {
				list = new ArrayList<>();
				// list.add("Dealer City");
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("CITY")) {
							dataReceived = true;
							list.add(xmlParser.nextText());
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetDlrsByCityAndTypeCSB)) {
				dealers = new ArrayList<Dealer>();
				// dealers.clear();
				Dealer dealer = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						// Log.v("START_TAG", tagName);

						if (tagName.equalsIgnoreCase("tuple")) {
							dataReceived = true;
							dealer = null;
							dealer = new Dealer();
						}
						// make changes from here
						else if (tagName.equalsIgnoreCase("COMMONNAME")) {
							dealer.setCommonname(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("DIVISIONID")) {
							dealer.setDivisionId(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("DIVISIONNAME")) {
							dealer.setDivisionName(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}
						break;

					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();
						// Log.i("END_TAG", tagName1);

						if (tagName1.equalsIgnoreCase("tuple")) {
							// if(!dealer.commonname.equalsIgnoreCase("") &&
							// !dealer.divisionId.equalsIgnoreCase("")){
							dealers.add(dealer);
							dealer = null;
							// }

						}
						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetdivphonebydivCSB)) {
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("DIVPHONE")) {
							dataReceived = true;
							dealercntctNum = xmlParser.nextText();
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}
						// else {
						// dataReceived = false;
						// //message =
						// "There is some issue with the service. Please try again later.";
						// err_message = "Contact Number Not Found";
						// }

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetComplaintAreaCSB)) {
				list = new ArrayList<>();
				list.add("Complaint Area");
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("VAL")) {
							// IF(xmlParser.nextText().equals("MUV"))
							dataReceived = true;
							list.add(xmlParser.nextText());
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					}
					eventType = xmlParser.next();
				}
			}
			if (webservice.equals(Constants.GetComplaintDesc)) {
				lst_c_desc = new ArrayList<ServiceComplaintDesc>();
				// lst_c_desc.clear();
				ServiceComplaintDesc c_desc = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						// Log.v("START_TAG", tagName);

						if (tagName.equalsIgnoreCase("S_PROD_DEFECT")) {
							dataReceived = true;
							c_desc = null;
							c_desc = new ServiceComplaintDesc();
						}
						// make changes from here
						else if (tagName.equalsIgnoreCase("DESC_TEXT")) {
							c_desc.setDESC_TEXT(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("DEFECT_NUM")) {
							c_desc.setDEFECT_NUM(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}
						break;

					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();
						// Log.i("END_TAG", tagName1);

						if (tagName1.equalsIgnoreCase("S_PROD_DEFECT")) {
							// if(!dealer.commonname.equalsIgnoreCase("") &&
							// !dealer.divisionId.equalsIgnoreCase("")){
							lst_c_desc.add(c_desc);
							c_desc = null;
							// }

						}
						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetCompTypeByBUCSB)) {
				lst_c_area = new ArrayList<ComplaintArea>();
				ComplaintArea c_area = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("S_LST_OF_VAL")) {
							// lst_c_area.clear();
							dataReceived = true;
							c_area = null;
							c_area = new ComplaintArea();
						}
						// make changes from here
						else if (tagName.equalsIgnoreCase("VAL")) {
							c_area.setVAL(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("ROW_ID")) {
							c_area.setROW_ID(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("PAR_ROW_ID")) {
							c_area.setPAR_ROW_ID(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}
						break;

					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();
						if (tagName1.equalsIgnoreCase("S_LST_OF_VAL")) {
							// if(!dealer.commonname.equalsIgnoreCase("") &&
							// !dealer.divisionId.equalsIgnoreCase("")){
							lst_c_area.add(c_area);
							c_area = null;
							// }

						}
						break;
					}
					eventType = xmlParser.next();
				}
			}// HIGH
			else if (webservice.equals(Constants.GetCompSubTypeByParRowIDCSB)) {

				list = new ArrayList<>();
				list.add("Complaint Sub Area");
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("VAL")) {
							dataReceived = true;
							
							list.add(xmlParser.nextText());
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					}
					eventType = xmlParser.next();
				}

			} else if (webservice
					.equals(Constants.GetCompPrblmAreabySubAreaCSB)) {

				list = new ArrayList<>();
				list.add("Problem Area");
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("VAL")) {
							dataReceived = true;
							list.add(xmlParser.nextText());
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					}
					eventType = xmlParser.next();
				}

			} else if (webservice
					.equals(Constants.GetServiceHistoryByChassis_CSB)) {
				lst_history = new ArrayList<ServiceHistory>();
				ServiceHistory history = null;

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("TABLE")) {
							dataReceived = true;
							history = null;
							history = new ServiceHistory();
						} else if (tagName.equalsIgnoreCase("ORDER_ID")) {
							history.setORDER_ID(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("SR_HISTORY_NUM")) {
							history.setSR_HISTORY_NUM(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("CHASSIS_NO")) {
							history.setCHASSIS_NO(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("REG_NUM")) {
							history.setREG_NUM(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("CLOSE_DATE")) {
							history.setCLOSE_DATE(xmlParser.nextText());
						} else if (tagName
								.equalsIgnoreCase("SERVICE_AT_DEALER")) {
							history.setSERVICE_AT_DEALER(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("SR_TYPE")) {
							history.setSR_TYPE(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("CITY")) {
							history.setCITY(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("INVC_AMT")) {
							history.setINVC_AMT(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("ODOMTR_RDNG")) {
							history.setODOMTR_RDNG(xmlParser.nextText());
						} else if (tagName
								.equalsIgnoreCase("DEALER_CONTACT_NUMBER")) {
							history.setDEALER_CONTACT_NUMBER(xmlParser
									.nextText());
						} else if (tagName.equalsIgnoreCase("INVOICE_STATUS")) {
							history.setINVOICE_STATUS(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();
						// Log.i("END_TAG", tagName1);

						if (tagName1.equalsIgnoreCase("TABLE")) {
							// if(!dealer.commonname.equalsIgnoreCase("") &&
							// !dealer.divisionId.equalsIgnoreCase("")){
							lst_history.add(history);
							history = null;
							// }

						}
						break;
					}
					eventType = xmlParser.next();
				}

			} else if (webservice
					.equals(Constants.TMPCComplaintsInsertOrUpdate_Input)) {
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("TmCims")) {
							dataReceived = true;
							list = new ArrayList<>();

						} else if (tagName.equalsIgnoreCase("Id")) {
							list.add(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("SRNumber")) {
							list.add(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					}
					eventType = xmlParser.next();
				}

			} else if (webservice
					.equals(Constants.TMSiebelServiceInsertOrUpdate_Input)) {
				// Insert service book in cordys
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("ServiceRequest")) {
							dataReceived = true;
							list = new ArrayList<>();

						} else if (tagName.equalsIgnoreCase("Id")) {
							list.add(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("SRNumber")) {
							list.add(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					}
					eventType = xmlParser.next();
				}

			} else if (webservice
					.equals(Constants.GetComplaintAndJCDescripti_CSB)) {
				lst_complaintandJC = new ArrayList<ComplaintAndJCDescripti>();
				ComplaintAndJCDescripti complaintandJC = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("S_PROD_INT")) {
							dataReceived = true;
							complaintandJC = null;
							complaintandJC = new ComplaintAndJCDescripti();
						}
						// make changes from here
						else if (tagName.equalsIgnoreCase("RECORD_TYPE")) {
							complaintandJC.setRECORD_TYPE(xmlParser.nextText());

						} else if (tagName
								.equalsIgnoreCase("C_J_P_DESCRTPTION")) {
							complaintandJC.setC_J_P_DESCRTPTION(xmlParser
									.nextText());

						} else if (tagName.equalsIgnoreCase("C_J_P_STATUS")) {
							complaintandJC
									.setC_J_P_STATUS(xmlParser.nextText());

						} else if (tagName.equalsIgnoreCase("CUSTOMER_VOICE")) {
							complaintandJC.setCUSTOMER_VOICE(xmlParser
									.nextText());

						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}
						break;

					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();
						if (tagName1.equalsIgnoreCase("S_PROD_INT")) {
							lst_complaintandJC.add(complaintandJC);
							complaintandJC = null;
							// }

						}
						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetState_CSB)) {
				city = new ArrayList<String>();
				// city.add("DealerLocatorCity");

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("tuple")) {
							dataReceived = true;
						} else if (tagName.equalsIgnoreCase("STATENAME")) {
							String text = xmlParser.nextText();
							text = text.replace("&amp;", "&");
							city.add(text);
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dial_message = "There is some issue with the service. Please try again later.";
							dataReceived = false;
						}

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetCity_CSB)) {
				city = new ArrayList<String>();
				// city.add("DealerLocatorCity");

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("tuple")) {
							dataReceived = true;
						} else if (tagName.equalsIgnoreCase("DIV_CITY")) {
							String text = xmlParser.nextText();
							text = text.replace("&amp;", "&");
							city.add(text);
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dial_message = "There is some issue with the service. Please try again later.";
							dataReceived = false;
						}

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetPPL_CSB)) {
				city = new ArrayList<String>();
				// city.add("DealerLocatorCity");

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("tuple")) {
							dataReceived = true;
						} else if (tagName.equalsIgnoreCase("PPL")) {
							String text = xmlParser.nextText();
							city.add(text);
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dial_message = "There is some issue with the service. Please try again later.";
							dataReceived = false;
						}

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetCitiesforServiceOrderCSB)) {
				city = new ArrayList<String>();
				// city.add("DealerLocatorCity");

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("tuple")) {
							dial_message = "Service executed successfully!";
							dataReceived = true;
						} else if (tagName.equalsIgnoreCase("CITY")) {

							city.add(xmlParser.nextText());

						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dial_message = "There is some issue with the service. Please try again later.";
							dataReceived = false;
						}

						break;
					}
					eventType = xmlParser.next();
				}
			}

			else if (webservice.equals(Constants.GetDlrLocDtlsCSB)) {
				delearList = new ArrayList<DelearLocatorData>();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("tuple")) {
							dial_message = "Service executed successfully!";
							delear = new DelearLocatorData();
							dataReceived = true;

						}/*
						 * else if (tagName.equalsIgnoreCase("err") ||
						 * tagName.equalsIgnoreCase("faultstring")) {
						 * dial_message =
						 * "There is some issue with the service. Please try again later."
						 * ; } if (tagName.equalsIgnoreCase("tmpc_import")) {
						 * delearList.add(delear); Log.d(delear.toString(),
						 * "coming"); }
						 */else if (tagName.equals("ID")) {
							delear.delear_ID = xmlParser.nextText(); // xmlParser.nextText();
							// Log.d("id", delear.delear_ID);
						} else if (tagName.equalsIgnoreCase("NAME")) {
							delear.delear_Name = xmlParser.nextText();
							// Log.d("name", delear.delear_Name);

						} else if (tagName.equalsIgnoreCase("DIV_COMMON_NAME")) {
							delear.delear_DIV_COMMON_NAME = xmlParser
									.nextText();
							/*
							 * Log.d("DIV_COMMON_NAME",
							 * delear.delear_DIV_COMMON_NAME);
							 */
						} else if (tagName.equalsIgnoreCase("DIV_ADDRESS_1")) {
							delear.delear_DIV_ADDRESS_1 = xmlParser.nextText();
							// Log.d("DIV_ADDRESS_1",
							// delear.delear_DIV_ADDRESS_1);
						} else if (tagName.equalsIgnoreCase("DIV_ADDRESS_2")) {
							delear.delear_DIV_ADDRESS_2 = xmlParser.nextText();
							// Log.d("DIV_ADDRESS_2",
							// delear.delear_DIV_ADDRESS_2);
						} else if (tagName.equalsIgnoreCase("DIV_STATE")) {
							delear.delear_DIV_STATE = xmlParser.nextText();
							// Log.d("DIV_STATE", delear.delear_DIV_STATE);
						} else if (tagName.equalsIgnoreCase("DIV_CITY")) {
							delear.delear_DIV_CITY = xmlParser.nextText();
							// Log.d("DIV_CITY", delear.delear_DIV_CITY);
						} else if (tagName.equalsIgnoreCase("DIV_COUNTRY")) {
							delear.delear_DIV_COUNTRY = xmlParser.nextText();
							// Log.d("DIV_COUNTRY", delear.delear_DIV_COUNTRY);
						} else if (tagName.equalsIgnoreCase("DIV_ZIP_CODE")) {
							delear.delear_DIV_ZIP_CODE = xmlParser.nextText();
							// Log.d("DIV_ZIP_CODE",
							// delear.delear_DIV_ZIP_CODE);
						} else if (tagName
								.equalsIgnoreCase("CATEGORY_OF_LOCATION")) {
							delear.delear_CATEGORY_OF_LOCATION = xmlParser
									.nextText();
							/*
							 * Log.d("CATEGORY_OF_LOCATION",
							 * delear.delear_CATEGORY_OF_LOCATION);
							 */
						} else if (tagName.equalsIgnoreCase("DIV_PHONE")) {
							delear.delear_DIV_PHONE = xmlParser.nextText();
							// Log.d("DIV_PHONE", delear.delear_DIV_PHONE);
						} else if (tagName.equalsIgnoreCase("DIV_EMAIL")) {
							delear.delear_DIV_EMAIL = xmlParser.nextText();
							// Log.d("DIV_EMAIL", delear.delear_DIV_EMAIL);
						} else if (tagName.equalsIgnoreCase("WEEKLY_OFF")) {
							delear.delear_WEEKLY_OFF = xmlParser.nextText();
							// Log.d("WEEKLY_OFF", delear.delear_WEEKLY_OFF);
						} else if (tagName.equalsIgnoreCase("SHOWROOM")) {
							delear.delear_SHOWROOM = xmlParser.nextText();
							// Log.d("SHOWROOM", delear.delear_SHOWROOM);
						} else if (tagName.equalsIgnoreCase("WORKSHOP")) {
							delear.delear_WORKSHOP = xmlParser.nextText();
							// Log.d("WORKSHOP", delear.delear_WORKSHOP);
						} else if (tagName.equalsIgnoreCase("SPAREPARTS")) {
							delear.delear_SPAREPARTS = xmlParser.nextText();
							// Log.d("SPAREPARTS", delear.delear_SPAREPARTS);
						} else if (tagName.equalsIgnoreCase("ACCESSORIES")) {
							delear.delear_ACCESSORIES = xmlParser.nextText();
							// Log.d("ACCESSORIES", delear.delear_ACCESSORIES);
						} else if (tagName.equalsIgnoreCase("TESTDR")) {
							delear.delear_TESTDR = xmlParser.nextText();
							Log.d("TESTDR", delear.delear_TESTDR);
						} else if (tagName.equalsIgnoreCase("TMA")) {
							delear.delear_TMA = xmlParser.nextText();
							// Log.d("TMA", delear.delear_TMA);
						} else if (tagName.equalsIgnoreCase("LATITUDE")) {
							delear.delear_LATITUDE = xmlParser.nextText();
							// Log.d("LATITUDE", delear.delear_LATITUDE);
						} else if (tagName.equalsIgnoreCase("LONGITUDE")) {
							delear.delear_LONGITUDE = xmlParser.nextText();
							/*
							 * Log.d("LONGITUDE", delear.delear_LONGITUDE);
							 * System
							 * .out.println(delear.delear_LONGITUDE.length() +
							 * "length");
							 */
						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();

						if (tagName1.equalsIgnoreCase("tuple")) {

							delearList.add(delear);
						}
						break;
					}
					eventType = xmlParser.next();
				}

			} else if (webservice.equals(Constants.GetVechcontactScheduler_CSB)) {
				// vehdetails = new VehcontactScheduler();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equals("S_PROD_LN")) {
							dataReceived = true;
							vehdetails = new VehcontactScheduler();
						} else if (tagName.equals("ASSET_NUM"))
							vehdetails.setASSET_NUM(xmlParser.nextText());
						else if (tagName.equals("ENGINE_NUM"))
							vehdetails.setENGINE_NUM(xmlParser.nextText());
						else if (tagName.equals("REGISTRATION_NUM"))
							vehdetails
									.setREGISTRATION_NUM(xmlParser.nextText());
						else if (tagName.equals("COLOR"))
							vehdetails.setCOLOR(xmlParser.nextText());
						else if (tagName.equals("WARRANTY_DT"))
							vehdetails.setWARRANTY_DT(xmlParser.nextText());
						else if (tagName.equals("LOB"))
							vehdetails.setLOB(xmlParser.nextText());
						else if (tagName.equals("PPL"))
							vehdetails.setPPL(xmlParser.nextText());
						else if (tagName.equals("PL"))
							vehdetails.setPL(xmlParser.nextText());
						else if (tagName.equals("NAME"))
							vehdetails.setNAME(xmlParser.nextText());
						else if (tagName.equals("FUEL_TYPE"))
							vehdetails.setFUEL_TYPE(xmlParser.nextText());
						else if (tagName.equals("LAST_SERVICE_DLR"))
							vehdetails
									.setLAST_SERVICE_DLR(xmlParser.nextText());
						else if (tagName.equals("WARRANTY_END_DT"))
							vehdetails.setWARRANTY_END_DT(xmlParser.nextText());
						else if (tagName.equals("POLICY_NUMBER"))
							vehdetails.setPOLICY_NUMBER(xmlParser.nextText());
						else if (tagName.equals("INSURENCE_COMPONY_NAME"))
							vehdetails.setINSURENCE_COMPONY_NAME(xmlParser
									.nextText());
						else if (tagName.equals("POLICY_ST_DT"))
							vehdetails.setPOLICY_ST_DT(xmlParser.nextText());
						else if (tagName.equals("POLICY_END_DT"))
							vehdetails.setPOLICY_END_DT(xmlParser.nextText());
						else if (tagName.equals("JPD_FLG"))
							vehdetails.setJPD_FLG(xmlParser.nextText());
						else if (tagName.equals("EW_POLICY_NUM"))
							vehdetails.setEW_POLICY_NUM(xmlParser.nextText());
						else if (tagName.equals("EW_ST_KM"))
							vehdetails.setEW_ST_KM(xmlParser.nextText());
						else if (tagName.equals("EW_END_KM"))
							vehdetails.setEW_END_KM(xmlParser.nextText());
						else if (tagName.equals("LAST_SERVICE_DIVISION"))
							vehdetails.setLAST_SERVICE_DIVISION(xmlParser
									.nextText());
						else if (tagName.equals("SELLING_DLR"))
							vehdetails.setSELLING_DLR(xmlParser.nextText());
						else if (tagName.equals("SELLING_DLR_CODE"))
							vehdetails
									.setSELLING_DLR_CODE(xmlParser.nextText());
						else if (tagName.equals("FIRST_SALE_DT"))
							vehdetails.setFIRST_SALE_DT(xmlParser.nextText());
						else if (tagName.equals("LAST_SERVICE_DT"))
							vehdetails.setLAST_SERVICE_DT(xmlParser.nextText());
						else if (tagName.equals("LAST_SERVICE_KM"))
							vehdetails.setLAST_SERVICE_KM(xmlParser.nextText());
						// extd warr
						else if (tagName.equals("EXTND_WRNTY_FLG"))
							vehdetails.setEXTND_WRNTY_FLG(xmlParser.nextText());
						else if (tagName.equals("EW_POLICY_NUM"))
							vehdetails.setEW_POLICY_NUM(xmlParser.nextText());
						else if (tagName.equals("EXTND_WRNTY_ST_DT"))
							vehdetails.setEXTND_WRNTY_ST_DT(xmlParser
									.nextText());
						else if (tagName.equals("EXTND_WRNTY_END_DT"))
							vehdetails.setEXTND_WRNTY_END_DT(xmlParser
									.nextText());
						else if (tagName.equals("EW_ST_KM"))
							vehdetails.setEXTND_WRNTY_ST_DT(xmlParser
									.nextText());
						else if (tagName.equals("EW_END_KM"))
							vehdetails.setEXTND_WRNTY_END_DT(xmlParser
									.nextText());
						// cust details
						else if (tagName.equals("CON_FSTNAME"))
							vehdetails.setCON_FSTNAME(xmlParser.nextText());
						else if (tagName.equals("CON_LSTNAME"))
							vehdetails.setCON_LSTNAME(xmlParser.nextText());
						else if (tagName.equals("CON_ADDR_LINE1"))
							vehdetails.setCON_ADDR_LINE1(xmlParser.nextText());
						else if (tagName.equals("CON_PAN_NUMBER"))
							vehdetails.setCON_PAN_NUMBER(xmlParser.nextText());
						else if (tagName.equals("CON_CELL_PH_NUM"))
							vehdetails.setCON_CELL_PH_NUM(xmlParser.nextText());
						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}
						break;
					}
					eventType = xmlParser.next();
				}

			} else if (webservice
					.equals(Constants.GetPositionbyPositionAndOUID_CSB)) {
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("POSITION")) {
							dataReceived = true;
							dealerdetails = new HashMap<>();
						} else if (tagName.equalsIgnoreCase("PRI_EMP_ID")) {

							dealerdetails.put("PRI_EMP_ID",
									xmlParser.nextText());
							// PRI_EMP_ID = xmlParser.nextText();
						} else if (tagName.equalsIgnoreCase("ORG_NAME")) {
							dealerdetails.put("ORG_NAME", xmlParser.nextText());
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}
						// else {
						// dataReceived = false;
						// //message =
						// "There is some issue with the service. Please try again later.";
						// err_message = "Contact Number Not Found";
						// }

						break;
					}
					eventType = xmlParser.next();
				}
			}

			else if (webservice.equals(Constants.GetAllIndianStates)) {
				city = new ArrayList<String>();
				// city.add("DealerLocatorCity");

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("CX_DISTRICT_MAS")) {

							dataReceived = true;

						} else if (tagName.equalsIgnoreCase("X_STATE")) {
							String stateName = xmlParser.nextText();
							stateName.replaceAll("&", "&amp;");
							stateVlaues.add(stateName);

						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dial_message = xmlParser.nextText();

						}

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetAllIndianDistricts)) {
				city = new ArrayList<String>();
				// city.add("DealerLocatorCity");

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("CX_DISTRICT_MAS")) {
							dataReceived = true;

						} else if (tagName.equalsIgnoreCase("X_DISTRICT")) {
							String stateName = xmlParser.nextText();
							stateName.replaceAll("&", "&amp;");
							distVlaues.add(stateName);
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dial_message = xmlParser.nextText();
							dataReceived = false;
						}

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetAllIndianCity)) {
				city = new ArrayList<String>();
				// city.add("DealerLocatorCity");

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("CX_DISTRICT_MAS")) {
							dataReceived = true;

						} else if (tagName.equalsIgnoreCase("X_CITY")) {
							String stateName = xmlParser.nextText();
							stateName.replaceAll("&", "&amp;");
							cityVlaues.add(stateName);
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dial_message = xmlParser.nextText();
							dataReceived = false;
						}

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.SendMail)) {
				dataReceived = false;
				/*
				 * city = new ArrayList<String>(); //
				 * city.add("DealerLocatorCity");
				 * 
				 * while (eventType != XmlPullParser.END_DOCUMENT) { switch
				 * (eventType) { case XmlPullParser.START_TAG: String tagName =
				 * xmlParser.getName();
				 * 
				 * if (tagName.equalsIgnoreCase("CX_DISTRICT_MAS")) {
				 * dataReceived = true;
				 * 
				 * }else if (tagName.equalsIgnoreCase("X_CITY")) { String
				 * stateName = xmlParser.nextText(); stateName.replaceAll("&",
				 * "&amp;"); cityVlaues.add(stateName); Log.d("distVlaues size",
				 * distVlaues.size()+""); }
				 * 
				 * else if (tagName.equalsIgnoreCase("err") ||
				 * tagName.equalsIgnoreCase("faultstring")) { dial_message =
				 * xmlParser.nextText(); dataReceived = false; }
				 * 
				 * break; } eventType = xmlParser.next(); }
				 */
			} else if (webservice.equals(Constants.bpmSendSMS)) {

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();

						if (tagName.equalsIgnoreCase("bpmSendSMSResponse")) {

						} else if (tagName.equalsIgnoreCase("Status")) {
							/*
							 * String stateName = xmlParser.nextText();
							 * stateName.replaceAll("&", "&amp;");
							 * cityVlaues.add(stateName);
							 * Log.d("distVlaues size", distVlaues.size()+"");
							 */
							String status = xmlParser.nextText();
							if (status.equals("Y")) {
								dataReceived = true;

							} else {
								dataReceived = false;

							}
						}

						else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dial_message = xmlParser.nextText();
							dataReceived = false;
						}

						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetAgreementScheduler_CSB)) {

				lst_agreeParent = new ArrayList<VehicleAgreement_ParentRow>();
				VehicleAgreement_ParentRow agreeParent = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("S_ASSET")) {
							dataReceived = true;
							agreeParent = null;
							agreeParent = new VehicleAgreement_ParentRow();
						}

						// make changes from here
						else if (tagName.equalsIgnoreCase("AGG_ID")) {
							agreeParent.setAgg_ID(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AGG_NAME")) {
							agreeParent.setAgree_name(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AGG_NUM")) {
							agreeParent.setAgreement_no(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AGG_STATUS")) {
							agreeParent.setStatus(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AGG_AMOUNT")) {
							agreeParent.setAgreement_amt(xmlParser.nextText());
						} else if (tagName
								.equalsIgnoreCase("AGG_MECH_SER_REMANING")) {
							agreeParent.setMech_reamaing(xmlParser.nextText());
						} else if (tagName
								.equalsIgnoreCase("AGG_TOWIN_SER_REMANING")) {
							agreeParent
									.setTowing_reamaing(xmlParser.nextText());
						} else if (tagName
								.equalsIgnoreCase("AGG_MECH_SERVICE_AVIL")) {
							agreeParent.setMech_avail(xmlParser.nextText());
						} else if (tagName
								.equalsIgnoreCase("AGG_TOWING_SERVICE_AVIL")) {
							agreeParent.setTowing_avail(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();
						Log.i("END_TAG", tagName1);

						if (tagName1.equalsIgnoreCase("S_ASSET")) {
							// if(!dealer.commonname.equalsIgnoreCase("") &&
							// !dealer.divisionId.equalsIgnoreCase("")){
							lst_agreeParent.add(agreeParent);
							agreeParent = null;
							// }

						}
						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice.equals(Constants.GetAMCDetailsScheduler_CSB)) {

				lst_amcParent = new ArrayList<VehicleAMC_ParentRow>();
				VehicleAMC_ParentRow amcParent = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("S_DOC_AGREE")) {
							dataReceived = true;
							amcParent = null;
							amcParent = new VehicleAMC_ParentRow();
						}

						// make changes from here
						else if (tagName.equalsIgnoreCase("AMC_NUM")) {
							amcParent.setAmc_no(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AMC_TYPE")) {
							amcParent.setAmc_type(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AMC_STATAUS")) {
							amcParent.setAmc_status(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AMC_STDT")) {
							amcParent.setStart_date(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AMC_EDDT")) {
							amcParent.setEnd_date(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AMC_STKM")) {
							amcParent.setStart_km(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AMC_ENDKM")) {
							amcParent.setEnd_km(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("AMC_DESCRIPTION")) {
							amcParent.setDescription(xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();
						// Log.i("END_TAG", tagName1);

						if (tagName1.equalsIgnoreCase("S_DOC_AGREE")) {
							// if(!dealer.commonname.equalsIgnoreCase("") &&
							// !dealer.divisionId.equalsIgnoreCase("")){
							lst_amcParent.add(amcParent);
							amcParent = null;
							// }

						}
						break;
					}
					eventType = xmlParser.next();
				}
			} else if (webservice
					.equals(Constants.GetParUsrPostnByPostTypeAndDivID_CSB)) {

				lst_amcParent = new ArrayList<VehicleAMC_ParentRow>();
				VehicleAMC_ParentRow amcParent = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String tagName = xmlParser.getName();
						if (tagName.equalsIgnoreCase("TM_USER_POSTN")) {
							dataReceived = true;
							dealerdetails = new HashMap<>();
						}

						// make changes from here
						else if (tagName.equalsIgnoreCase("LOGIN")) {
							dealerdetails.put("login", xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("MOBILE")) {
							dealerdetails.put("mobile", xmlParser.nextText());
						} else if (tagName.equalsIgnoreCase("err")
								|| tagName.equalsIgnoreCase("faultstring")) {
							dataReceived = false;
							// err_message = xmlParser.nextText();
							err_message = "There is some issue with the service. Please try again later.";
						}

						break;
					case XmlPullParser.END_TAG:
						String tagName1 = xmlParser.getName();
						Log.i("END_TAG", tagName1);
						break;
					}
					eventType = xmlParser.next();
				}
			}
			}
		} catch (Exception e) {
			Log.v(this.getClass().getSimpleName(), e.toString());
		}
		return dataReceived;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		

		dialog.dismiss();
		
		/*
		 * if (webservice.equals(Constants.GetCitiesforServiceOrderCSB))
		 * rc.onResponseReceive(city);
		 */

		if (result) {
			if (webservice.equals(Constants.GetCustomerVehicleDetailsCSB)) {
				rc.onResponseReceive(user);
			} else if (webservice.equals(Constants.GetDSSDealerCitiesCSB)
					|| webservice.equals(Constants.GetComplaintAreaCSB)
					|| webservice.equals(Constants.GetCompSubTypeByParRowIDCSB)

					|| webservice
							.equals(Constants.GetCompPrblmAreabySubAreaCSB)
					|| webservice
							.equals(Constants.TMPCComplaintsInsertOrUpdate_Input)
					|| webservice
							.equals(Constants.TMSiebelServiceInsertOrUpdate_Input))
				rc.onResponseReceive(list);
			else if (webservice.equals(Constants.GetDlrsByCityAndTypeCSB))
				rc.onResponseReceive(dealers);
			else if (webservice.equals(Constants.GetdivphonebydivCSB))
				rc.onResponseReceive(dealercntctNum);
			else if (webservice.equals(Constants.GetComplaintDesc))
				rc.onResponseReceive(lst_c_desc);
			else if (webservice.equals(Constants.GetCompTypeByBUCSB))
				rc.onResponseReceive(lst_c_area);
			else if (webservice
					.equals(Constants.GetServiceHistoryByChassis_CSB))
				rc.onResponseReceive(lst_history);
			else if (webservice
					.equals(Constants.GetComplaintAndJCDescripti_CSB)) {
				rc.onResponseReceive(lst_complaintandJC);
			} else if (webservice.equals(Constants.GetVechcontactScheduler_CSB)) {
				rc.onResponseReceive(vehdetails);
			} else if (webservice
					.equals(Constants.GetPositionbyPositionAndOUID_CSB)) {
				rc.onResponseReceive(dealerdetails);
			} else if (webservice.equals(Constants.GetAllIndianStates)) {
				rc.onResponseReceive(stateVlaues);
			} else if (webservice.equals(Constants.GetAllIndianDistricts)) {
				rc.onResponseReceive(distVlaues);
			} else if (webservice.equals(Constants.GetAllIndianCity)) {
				rc.onResponseReceive(cityVlaues);
			} else if (webservice.equals(Constants.bpmSendSMS)) {
				rc.onResponseReceive(result);
			} else if (webservice.equals(Constants.GetDlrLocDtlsCSB)) {
				rc.onResponseReceive(delearList);
			} else if (webservice.equals(Constants.GetCitiesforServiceOrderCSB)) {
				rc.onResponseReceive(city);
			} else if (webservice.equals(Constants.GetState_CSB)) {
				rc.onResponseReceive(city);
			} else if (webservice.equals(Constants.GetCity_CSB)) {
				rc.onResponseReceive(city);
			} else if (webservice.equals(Constants.GetPPL_CSB)) {
				rc.onResponseReceive(city);
			} else if (webservice.equals(Constants.GetAgreementScheduler_CSB)) {
				rc.onResponseReceive(lst_agreeParent);
			} else if (webservice.equals(Constants.GetAMCDetailsScheduler_CSB)) {
				rc.onResponseReceive(lst_amcParent);
			} else if (webservice.equals(Constants.SendMail)) {
				
			} else if (webservice
					.equals(Constants.GetParUsrPostnByPostTypeAndDivID_CSB)) {
				rc.onResponseReceive(dealerdetails);
			}
		} else {
			rc.onErrorReceive(err_message);
		}
	}
}
