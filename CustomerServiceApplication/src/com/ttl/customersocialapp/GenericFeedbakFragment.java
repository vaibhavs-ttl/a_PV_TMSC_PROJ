package com.ttl.customersocialapp;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
public class GenericFeedbakFragment extends Fragment{

	String feedback_category, generic_feedback_type, specific_type, customer_feedback;
	EditText specific_type_editbox, customer_feedback_editbox;
	FloatLabelLayout feedsubparent;
	Spinner feedback_types_spinner;
	List<String> feedback_types = new ArrayList<String>();
	View view ;
	
	Tracker mTracker;
	@Override
	public void onStart() {
		
		super.onStart();
		mTracker.setScreenName("GenericFeedbackScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	@Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.fragment_generic_feedback, viewGroup, false);
		/*final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.feedback_popup);
        dialog.show();*/
		//Toast.makeText(getActivity(), "here.", Toast.LENGTH_LONG).show();
		 AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
			mTracker = application.getDefaultTracker();
        Button btnsubmit = (Button) view.findViewById(R.id.visit_site);
        
        specific_type_editbox = (EditText)view.findViewById(R.id.txtfeedbacksub);
        feedsubparent = (FloatLabelLayout) view.findViewById(R.id.feedsubparent);
        customer_feedback_editbox = (EditText)view.findViewById(R.id.txtfeedbak);
        feedback_category = "Generic";
        feedback_types_spinner = (Spinner)view.findViewById(R.id.spinfeedback);
        
        feedback_types.add("Select Feedback Type");
        feedback_types.add("Feedback on the App");
        feedback_types.add("Feedback on Product-Vehicle");
        feedback_types.add("Feedback on Call center");
        feedback_types.add("Feedback on Mega-camps");
        feedback_types.add("Feedback on AMC");
        feedback_types.add("Feedback on Extended Warranty");
        feedback_types.add("Feedback on Roadside Assistance");
        feedback_types.add("Feedback on Other Areas");
        
        
        ArrayAdapter<String> feedback_types_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, feedback_types);
        feedback_types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedback_types_spinner.setAdapter(feedback_types_adapter);
        
        btnsubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sumbitFeedback();
				//dialog.dismiss();
			}
		});
         
        feedback_types_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				generic_feedback_type = parent.getItemAtPosition(position).toString();
				if(generic_feedback_type.equals("Select Feedback Type"))
				{
					((TextView) parent.getChildAt(0)).setTextColor(view.getContext().getResources().getColor(R.color.hintcolor));
				}else
				{
					((TextView) parent.getChildAt(0)).setTextColor(view.getContext().getResources().getColor(R.color.textcolor));
				}
				specific_type_editbox.setText("");
		        customer_feedback_editbox.setText("");
				 if(generic_feedback_type.equalsIgnoreCase("Feedback on Other Areas"))
			        {
					 feedsubparent.setVisibility(View.VISIBLE);
			         specific_type_editbox.setVisibility(View.VISIBLE);
			         
			        }
			        else
			        {
			        	feedsubparent.setVisibility(View.GONE);
			         specific_type_editbox.setVisibility(View.GONE);
			        }
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
        view.getRootView().setFocusableInTouchMode(true);
		view.getRootView().requestFocus();

		view.getRootView().setOnKeyListener(new OnKeyListener() {
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
        return view;
    }
	
	private void sumbitFeedback()
	{
		if(generic_feedback_type.equalsIgnoreCase("Select Feedback Type"))
		{
			Toast.makeText(getActivity(), "Please Select Feedback Type", Toast.LENGTH_LONG).show();
		}
		else
		{
			if(generic_feedback_type.equalsIgnoreCase("Feedback on Other Areas")&&specific_type_editbox.getText().toString().equalsIgnoreCase("")||generic_feedback_type.equalsIgnoreCase("Feedback on Other Areas")&&specific_type_editbox.getText().toString() == null)
			{
				Toast.makeText(getActivity(), "Please Specify the Type of Feedback in the text box.", Toast.LENGTH_LONG).show();
			}
			else
			{
				specific_type = specific_type_editbox.getText().toString();
				if(customer_feedback_editbox.getText().toString().equalsIgnoreCase("")||customer_feedback_editbox.getText().toString() == null)
				{
					Toast.makeText(getActivity(), "Please fill in the Feedback.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					CheckConnectivity checknow = new  CheckConnectivity();
					boolean connect = checknow.checkNow(getActivity());
					if(connect)
					{
						 Calendar c = Calendar.getInstance();
					        System.out.println("Current time => "+c.getTime());

					        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					        String formattedDate = df.format(c.getTime());
						
					
					customer_feedback = customer_feedback_editbox.getText().toString();
					String req = Config.awsserverurl+"tmsc_ch/customerapp/feedbackServices/addGenericCustomerFeedback";
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
					
					nameValuePairs.add(new BasicNameValuePair("feedback_category",feedback_category));
					nameValuePairs.add(new BasicNameValuePair("generic_feedback_type",generic_feedback_type));
					nameValuePairs.add(new BasicNameValuePair("specific_type",specific_type));
					nameValuePairs.add(new BasicNameValuePair("customer_feedback",customer_feedback));
					nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails.getUser_id()));
					nameValuePairs.add(new BasicNameValuePair("timestamp", formattedDate ));
					  nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
					new AWS_WebServiceCall(view.getContext(), req,ServiceHandler.POST ,Constants.addGenericCustomerFeedback,
							nameValuePairs,new ResponseCallback(){

								@Override
								public void onResponseReceive(Object object) {
									
									Toast.makeText(getActivity(), "Thanks for giving your valuable Feedback.", Toast.LENGTH_SHORT).show();
								
							        FragmentManager fm = getFragmentManager();
									FragmentTransaction tx = fm.beginTransaction();
									tx.replace(R.id.frame_container, new HomeFragment())
											.commit();
									view.getRootView().setFocusable(true);
							  		view.getRootView().requestFocus();
							  		
							  		 mTracker.send(new HitBuilders.EventBuilder()
						               	.setCategory(UserDetails.getUser_id())
						   	        	.setAction("thread_true")
						   	        	.setLabel("GenericFeedback")
							            .build());
								}

								@Override
								public void onErrorReceive(String string) {
									
									Toast.makeText(getActivity(), "There is some issue with the Service. Please try again.", Toast.LENGTH_SHORT).show();
									view.getRootView().setFocusable(true);
							  		view.getRootView().requestFocus();
							  		 mTracker.send(new HitBuilders.EventBuilder()
						               	.setCategory(UserDetails.getUser_id())
						   	        	.setAction("thread_false")
						   	        	.setLabel("GenericFeedback")
							            .build());
								}
					}).execute();
					}
					else
					{
						Toast toast = Toast.makeText(getActivity(), "No network connection available.", Toast.LENGTH_LONG);
						
						toast.show();
					}
					
				}
			}
		}
	}
}
