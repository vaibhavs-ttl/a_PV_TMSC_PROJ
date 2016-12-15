package com.ttl.customersocialapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.helper.AnalyticsApplication;

public class InfoandUpdate_Fragment extends Fragment {
	protected static final String LOG_TAG = null;

	private Spinner spinner_cityname;
	/*private String[] cityname = { 
			
			"Customer Care",
			"Head Office(Mumbai)",
			"Regional Office West1 (Mumbai)",
			"Regional Office West2 (Thane)",
			"Regional Office East (Kolkata)",
			"Regional Office North1 (Chandigarh)",
			"Regional Office North2 (Lucknow)",
			"Regional Office North3 (Gurgaon)",
			"State Office - (K1-Bangalore)",
			"Regional Office South2 (Chennai)",
			"State Office -  (Mumbai)",
			"State Office - (Assam & N.E)",
			"State Office - (MP /Indore)",
			"State Office -Chandigarh/Haryana/HP",
			"State Office - (Maharashtra /Pune)",
			"State Office - (Punjab/J & K/Ludhiana)",
			"State Office - (Uttar Pradesh)",
			"State Office - (Orissa- Bhubaneswar)", 
			"State Office - (Jaipur)",
			"State Office - (Ahmedabad)",
			"State Office - (TN 1-Chennai)",
			"State Office -  (NCR )",
			"State Office - (Kerala)",
			"State Office - (Telengana-Hyderabad)",
			"State Office - (Dehradun)",
			"State Office  (AP-Vijaywada)",
			"State Office (Chattisgarh & Vidharbh)",
			"State Office-(West Bengal)", 
			"State Office (TN2-Madurai)",
			"State Office-(Bihar)",
			"State Office -  (Jharkhand)",
			"State Office- (K2-Hubli)" };*/
	
	
private String[] cityname = { 
			
			
			"Regional Office West1 (Mumbai)",
			"Regional Office West2 (Thane)",
			"Regional Office East (Kolkata)",
			"Regional Office North1 (Chandigarh)",
			"Regional Office North2 (Lucknow)",
			"Regional Office North3 (Gurgaon)",
			"State Office - (K1-Bangalore)",
			"Regional Office South2 (Chennai)",
			"State Office -  (Mumbai)",
			"State Office - (Assam & N.E)",
			"State Office - (MP /Indore)",
			"State Office -Chandigarh/Haryana/HP",
			"State Office - (Maharashtra /Pune)",
			"State Office - (Punjab/J & K/Ludhiana)",
			"State Office - (Uttar Pradesh)",
			"State Office - (Orissa- Bhubaneswar)", 
			"State Office - (Jaipur)",
			"State Office - (Ahmedabad)",
			"State Office - (TN 1-Chennai)",
			"State Office -  (NCR )",
			"State Office - (Kerala)",
			"State Office - (Telengana-Hyderabad)",
			"State Office - (Dehradun)",
			"State Office  (AP-Vijaywada)",
			"State Office (Chattisgarh & Vidharbh)",
			"State Office-(West Bengal)", 
			"State Office (TN2-Madurai)",
			"State Office-(Bihar)",
			"State Office -  (Jharkhand)",
			"State Office- (K2-Hubli)" ,
			"Head Office(Mumbai)",
			"Customer Care"};
	
	
	
	private TextView txtcityname, titlename, address, cellno, cellno1, cellno2,
			faxno, faxno1;
	private LinearLayout faxll, faxll1, contactll, contactll1, contactll2,
			btnpdfdwonload;
	private ImageView faxImage;
	private boolean customer_care_selected=false;
	private Tracker mTracker;
	private TextView callCentreContactNo;
	private TextView callCentreEmailID;
	int currentapiVersion = android.os.Build.VERSION.SDK_INT;

	@Override
	public void onStart() {

		super.onStart();
		mTracker.setScreenName("InfoAndUpdtaesScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_infoandupdate, container,
				false);

		spinner_cityname = (Spinner) v.findViewById(R.id.spinner_cityname);
		txtcityname = (TextView) v.findViewById(R.id.txtcityname);
		titlename = (TextView) v.findViewById(R.id.titlename);
		address = (TextView) v.findViewById(R.id.address);
		cellno = (TextView) v.findViewById(R.id.cellno);
		cellno1 = (TextView) v.findViewById(R.id.cellno1);
		cellno2 = (TextView) v.findViewById(R.id.cellno2);
		faxno = (TextView) v.findViewById(R.id.faxno);
		faxno1 = (TextView) v.findViewById(R.id.faxno1);
		faxll = (LinearLayout) v.findViewById(R.id.faxll);
		faxll1 = (LinearLayout) v.findViewById(R.id.faxll1);
		contactll = (LinearLayout) v.findViewById(R.id.contactll);
		contactll1 = (LinearLayout) v.findViewById(R.id.contactll1);
		contactll2 = (LinearLayout) v.findViewById(R.id.contactll2);
		btnpdfdwonload = (LinearLayout) v.findViewById(R.id.btnpdfdwonload);
		faxImage=(ImageView) v.findViewById(R.id.faximage);
		
		/*callCentreContactNo = (TextView) v
				.findViewById(R.id.txtContactNumberValue);
		callCentreEmailID = (TextView) v.findViewById(R.id.txtEmailValue);
*/
		// Tracker
		AnalyticsApplication application = (AnalyticsApplication) getActivity()
				.getApplication();
		mTracker = application.getDefaultTracker();

		ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, cityname);
		regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_cityname.setAdapter(regno);

		contactll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"
						+ cellno.getText().toString().trim()));
				getActivity().startActivity(callIntent);

			}
		});
		contactll1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"
						+ cellno1.getText().toString().trim()));
				getActivity().startActivity(callIntent);

			}
		});
		contactll2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"
						+ cellno2.getText().toString().trim()));
				getActivity().startActivity(callIntent);

			}
		});

		//akash
		faxll.setOnClickListener(new OnClickListener() {
			
			
			

			@Override
			public void onClick(View v) {
				
								
				
				if(customer_care_selected){
					
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri
						.fromParts("mailto", faxno.getText()
								.toString(), null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
				}
				

			}

			
		});
		btnpdfdwonload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent();
				 * intent.setAction(Intent.ACTION_VIEW); Uri uri =
				 * Uri.fromFile(new File("//assets/serviceproducts.pdf"));
				 * intent.setDataAndType(uri, "application/pdf");
				 * startActivity(intent);
				 */
				CopyReadAssets();
			}

		});

/*		callCentreContactNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Toast.makeText(v.getContext(),
				// callCentreContactNo.getText()+""+currentapiVersion,
				// Toast.LENGTH_SHORT).show();

				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"
						+ callCentreContactNo.getText().toString().trim()));
				getActivity().startActivity(callIntent);

				
				 * if (currentapiVersion >=
				 * android.os.Build.VERSION_CODES.LOLLIPOP){ // Do something for
				 * lollipop and above versions } else{ // do something for
				 * phones running an SDK before lollipop
				 * 
				 * Intent callIntent = new Intent(Intent.ACTION_CALL);
				 * callIntent.setData(Uri.parse("tel:" +
				 * callCentreContactNo.getText().toString().trim()));
				 * getActivity().startActivity(callIntent);
				 * 
				 * }
				 

			}
		});

		callCentreEmailID.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Toast.makeText(v.getContext(), callCentreEmailID.getText(),
				// Toast.LENGTH_SHORT).show();

				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri
						.fromParts("mailto", callCentreEmailID.getText()
								.toString(), null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
				startActivity(Intent
						.createChooser(emailIntent, "Send email..."));

			}
		});*/

		spinner_cityname
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						switch (position) {
						
						case 31:
							txtcityname.setText("Customer Care");
						//	titlename
							//		.setText("Tata Motors Limited ");
							cellno.setText("1800 209 7979");
							//faxno.setText("022-66586010");
							address.setText("Tata motors Customer Care");
							customer_care_selected=true;
							
							faxImage.setImageResource(R.drawable.product_info_email);
							faxImage.setTag(R.drawable.product_info_email);
							
							
										
							faxno.setText("customercare@tatamotors.com");
							faxll.setVisibility(View.VISIBLE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							titlename.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							faxll1.setVisibility(View.GONE);
							break;
						
						
						case 30:
							customer_care_selected=false;
							txtcityname.setText("Head Office(Mumbai)");
							titlename
									.setText("Tata Motors Limited One Indiabulls Centre");
							address.setText("Tower 2A & B, 20th floor,841, Senapati Bapat Marg,Jupiter Mills Compound,Elphinstone Road (West),Mumbai – 400 013.");
							cellno.setText("022-66586000");
							faxno.setText("022-66586010");
							faxImage.setImageResource(R.drawable.whitefax);
							faxll.setVisibility(View.VISIBLE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;

						case 0:
							customer_care_selected=false;
							txtcityname
									.setText("Regional Office West1 (Mumbai)");
							titlename.setText("Tata Motors Ltd");
							address.setText("PVBU, C7, 3rd Floor, Laxmi Towers,C Wing, G Block,Bandra Kurla Complex,Bandra (East),Mumbai - 400051");
							cellno.setText("022-66930400");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 1:
							customer_care_selected=false;
							txtcityname
									.setText("Regional Office West2 (Thane)");
							titlename.setText("Tata Motors Ltd");
							address.setText("3rd floor, Teen Hath Naka,Gyan Sadhana College Service Road,Thane -400064");
							cellno.setText("022-67927072");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;

						case 2:
							customer_care_selected=false;
							txtcityname
									.setText("Regional Office East (Kolkata)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Rene Tower,2nd Floor,1842,Rajdanga Main Road,Kolkata -700107");
							cellno.setText("033-66027400");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;

						case 3
						
						
						:
							customer_care_selected=false;
							txtcityname
									.setText("Regional Office North1 (Chandigarh)");
							titlename.setText("Tata Motors Ltd");
							address.setText("SCO 364,65,66,Second Floor , Sector 34 A,Chandigarh- 160034");
							cellno.setText("0172-6629555");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;

						case 4:
							customer_care_selected=false;
							txtcityname
									.setText("Regional Office North2 (Lucknow)");
							titlename.setText("Tata Motors Ltd");
							address.setText("PVBU Ro North 2, 7Th Floor,Cyber Tower, Opp. Indira Gandhi Pratisthan,Vibhuti Khand, Gomti Nagar,Lucknow - 226 010");
							cellno.setText("0522-6654001");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 5:
							customer_care_selected=false;
							txtcityname
									.setText("Regional Office North3 (Gurgaon)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Passenger Car Business Unit,305, 3rd Floor, Tower A,Signature Towers,South City 1, NH-8,Gurgaon 122001");
							cellno.setText("0124-6470509");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;

						case 6:
							customer_care_selected=false;
							txtcityname
									.setText("State Office - (K1-Bangalore)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd, Fortune Summit Towers I 2nd Floor I #244 Hosur Main Road I Banglore 560102");
							cellno.setText("080-66373400");
							faxno.setText("080-66373410");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 7:
							customer_care_selected=false;
							txtcityname
									.setText("Regional Office South2 (Chennai)");
							titlename.setText("Tata Motors Ltd");
							address.setText("6th Floor, ASV Ramana's Tower,52,Venkatnarayana Road,T Nagar,Chennai-600017");
							cellno.setText("044-66500900");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;

						case 8:
							customer_care_selected=false;
							txtcityname.setText("State Office -  (Mumbai)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd. B/4,3rd Floor,G-25,G Block, Laxmi Towers, Behind ICICI Bank,Banrda Kurla Complex, Bandra(East), Mumbai-400051");
							cellno.setText("022 -66930400");
							faxno.setText("022 6693 0474");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 9:
							customer_care_selected=false;
							txtcityname.setText("State Office - (Assam & N.E)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors,AO-PVBU,3rd Floor behind SBI dispur Br. G.S. Road,Dispur ,Guwahati-731006");
							cellno.setText("0361-2636856");
							cellno1.setText("0361-2601511");
							cellno2.setText("0361-6113517");
							faxno.setText("0361-2631770");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.VISIBLE);
							contactll2.setVisibility(View.VISIBLE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 10:
							customer_care_selected=false;
							txtcityname.setText("State Office - (MP /Indore)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd. Apolla Trade Centre, Geeta Bhavan Square, Agra Bombay Road, Indore - 452 001");
							cellno.setText("0731-6699003");
							faxno.setText("0731-2498634");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 11:
							customer_care_selected=false;
							txtcityname
									.setText("State Office -Chandigarh/Haryana/HP");
							titlename.setText("Tata Motors Ltd");
							address.setText("SCO-364-365-366, Second Floor, Sector 34 A, Chandigarh - 160034");
							cellno.setText("0172-6629555");
							cellno1.setText("0172-6629500");
							faxno.setText("0172-6629501");
							faxno1.setText("0172-6629502");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.VISIBLE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.VISIBLE);
							address.setVisibility(View.VISIBLE);
							break;
						case 12:
							customer_care_selected=false;
							txtcityname
									.setText("State Office - (Maharashtra /Pune)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd. 413-416, Silicon Plaza, Senapati Bapat Marg, Pune - 411016");
							cellno.setText("020-66098650");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 13:
							customer_care_selected=false;
							txtcityname
									.setText("State Office - (Punjab/J & K/Ludhiana)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Sco 16-17. Fortune Towers. 6th Floor. Feroze Gandhi Market. Opp. Ludhiana Stock Exchange, Ludhiana- 141001");
							cellno.setText("0161-6454268");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 14:
							customer_care_selected=false;
							txtcityname
									.setText("State Office - (Uttar Pradesh)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Limited, PVBU RO North 2, 7Th Floor, Cyber Tower, Opp. Indira Gandhi Pratisthan, Vibhuti Khand, Gomti Nagar, Lucknow - 226 010");
							cellno.setText("0522-6654001");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 15:
							customer_care_selected=false;
							txtcityname
									.setText("State Office - (Orissa- Bhubaneswar)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd,2nd floor plot no 364,above Nokia Care,Ruplai Square,Sahid Nagar,Janpath,Bhubneswar-751007");
							cellno.setText("0674-6099099");
							cellno1.setText("0674-6626100");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.VISIBLE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 16:
							customer_care_selected=false;
							txtcityname.setText("State Office - (Jaipur)");
							titlename.setText("Tata Motors Ltd");
							address.setText("TML,D-232, 2nd Floor, Atlantis Tower, Amrapali Marg, Near Amrapali Circle, Vaishali Nagar, Jaipur -302021");
							cellno.setText("0141-6440551");
							faxno.setText("0141-5118369");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 17:
							customer_care_selected=false;
							txtcityname.setText("State Office - (Ahmedabad)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd, C-1, Safal Profitaire, Prahaladnagar Corporate Road, Opp.Auda Garden Prahaladnagar Ahmedabad-380015");
							cellno.setText("079-66637212");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 18:
							customer_care_selected=false;
							txtcityname
									.setText("State Office - (TN 1-Chennai)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors ( Regional Office) PVBU, 6th Floor , ASV Ramana Towers, Venkatnarayana Road, Chennai - 600 017");
							cellno.setText("044-66500900");
							cellno1.setText("044-66500905");
							faxno.setText("044-66500910");
							faxll.setVisibility(View.VISIBLE);
							
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.VISIBLE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 19:
							customer_care_selected=false;
							txtcityname.setText("State Office -  (NCR )");
							titlename.setText("Tata Motors Ltd");
							address.setText("304, Tower-A Signature Tower South City-1 NH-8, Gurgaon");
							cellno.setText("0124-6470509");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;

						case 20:
							customer_care_selected=false;
							txtcityname.setText("State Office - (Kerala)");
							titlename.setText("Tata Motors Ltd");
							address.setText("TATA Motors Ltd (PVBU), 4th Floor ,Liv N Tower, Opposite Gold Souk,Vyttila, Cochin - 682019");
							cellno.setText("0484-6601400");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 21:
							customer_care_selected=false;
							txtcityname
									.setText("State Office - (Telengana-Hyderabad)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd, Plot Ni. 1-10-39 to 44; 4th Floor Gumidelli Towers, Old Airport Road,Begumpet; Hyderabad-500016");
							cellno.setText("040-66563551");
							cellno1.setText("040-66563552");
							faxno.setText("040-66563550");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.VISIBLE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 22:
							customer_care_selected=false;
							txtcityname.setText("State Office - (Dehradun)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Limited, PVBU State Office Dehradun,I & S Building, 2Nd Floor, 30 Mohhebawala, Saharanpur Road, Dehradun-248002");
							cellno.setText("0135-6549537");
							cellno1.setText("0135-6549518");
							faxno.setText("0135-6549532");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.VISIBLE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 23:
							customer_care_selected=false;
							txtcityname.setText("State Office  (AP-Vijaywada)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd, Indira Arcade,D No. 54-15-4D, 2nd Floor,Mahanadu Road Corner, Srinivasa Nagar Bank Colony, Vijayawada-520008.");
							cellno.setText("0866-6678001");
							faxno.setText("0866-2543522");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 24:
							customer_care_selected=false;
							txtcityname
									.setText("State Office (Chattisgarh & Vidharbh)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd. Artefact Towers, 7th Floor,54/3, Chattrapati Square,Wardha Road, Nagpur - 440015");
							cellno.setText("0712-3071713");
							cellno1.setText("0712-3071712");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.VISIBLE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 25:
							customer_care_selected=false;
							txtcityname.setText("State Office-(West Bengal)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd,Rene Tower,2nd Floor,1842,Rajdanga Main Road,Kolkata -700107");
							cellno.setText("033-66027400");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 26:
							customer_care_selected=false;
							txtcityname.setText("State Office  (TN2-Madurai)");
							titlename.setText("Tata Motors Ltd");
							address.setText("TATA Motors Ltd (PVBU), GV Towers , Second Floor, No.2/3 Melakal Road,Kochadai,Madurai - 625010");
							cellno.setText("0452-2383800");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 27:
							customer_care_selected=false;
							txtcityname.setText("State Office -(Bihar)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd, 4th floor,sai corporate Park,Block-B,Rukunpura, Patna-800014");
							cellno.setText("0612-6510332");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 28:
							customer_care_selected=false;
							txtcityname.setText("State Office -  (Jharkhand)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd,PVBU-Area Office,Maru Tower ,5th floor,Kanke Road,Ranchi-834008");
							cellno.setText("0651- 6450154");
							faxll.setVisibility(View.GONE);
							contactll1.setVisibility(View.GONE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;
						case 29:
							customer_care_selected=false;
							txtcityname.setText("State Office- (K2-Hubli)");
							titlename.setText("Tata Motors Ltd");
							address.setText("Tata Motors Ltd, Shiva Avenue, 1st Floor, Gokul Road, Banneigada, Hubli");
							cellno.setText("0836-6576445");
							cellno1.setText("0836-6576446");
							faxno.setText("0836-2334962");
							faxll.setVisibility(View.VISIBLE);
							faxImage.setImageResource(R.drawable.whitefax);
							contactll1.setVisibility(View.VISIBLE);
							contactll2.setVisibility(View.GONE);
							faxll1.setVisibility(View.GONE);
							address.setVisibility(View.VISIBLE);
							break;

						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		v.getRootView().setFocusableInTouchMode(true);
		v.getRootView().requestFocus();

		v.getRootView().setOnKeyListener(new OnKeyListener() {
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

		return v;
	}

	protected void setContentView(TextView tv) {

	}

	private void CopyReadAssets() {
		AssetManager assetManager = getActivity().getResources().getAssets();

		InputStream input = null;
		OutputStream out = null;
		File file = new File(getActivity().getFilesDir(), "serviceproducts.pdf");
		try {
			input = assetManager.open("serviceproducts.pdf");
			out = getActivity().openFileOutput(file.getName(),
					Context.MODE_WORLD_READABLE);

			copyFile(input, out);
			input.close();
			input = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.parse("file://" + getActivity().getFilesDir()
						+ "/serviceproducts.pdf"), "application/pdf");

		startActivity(intent);
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
