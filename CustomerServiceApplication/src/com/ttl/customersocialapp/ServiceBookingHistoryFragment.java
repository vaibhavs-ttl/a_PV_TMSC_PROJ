package com.ttl.customersocialapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.ServiceHandler;
import com.ttl.model.Service_Booking_History_Child;
import com.ttl.model.Service_Booking_History_Parent;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class ServiceBookingHistoryFragment extends Fragment {

	ExpandableListView exlv;
	private ArrayList<Service_Booking_History_Parent> parents;
	 String dateForAlarm;
	  boolean isDate = false;
	  Button btn_reminder;
	  private String reminderDate;//,reminderTime;
	SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy",Locale.US); 
	  SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm",Locale.US);
		 SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm",Locale.US);
		 SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm aa",Locale.US);
		 
		 View v;
		 
		 Tracker mTracker;
		 @Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			mTracker.setScreenName("ServiceBookingHistoryScreen");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 v = inflater.inflate(R.layout.fragment_servicebookinghistory,
				container, false);

		exlv = (ExpandableListView) v.findViewById(R.id.list);
		if(new UserDetails().getRegNumberList().size() == 0)
		{
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		}
		
		AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
		mTracker = application.getDefaultTracker();
		// buildDummyData();
		buildData();
		exlv.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					exlv.collapseGroup(previousGroup);
				previousGroup = groupPosition;

			}
		});
		// Adding ArrayList data to ExpandableListView values
		// loadHosts(dummyList);

		v.getRootView().setFocusableInTouchMode(true);
		v.getRootView().requestFocus();

		v.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container, new HomeFragment()).addToBackStack(null)
								.commit();
						return true;
					}
				}
				return false;
			}
		});

		return v;
	}

	private void loadHosts(
			final ArrayList<Service_Booking_History_Parent> newParents) {
		if (newParents == null)
			return;
		
		parents = newParents;
	
		// Check for ExpandableListAdapter object

		// Create ExpandableListAdapter Object
		final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();
		exlv.setClickable(true);

		// Set Adapter to ExpandableList Adapter
		exlv.setAdapter(mAdapter);
	/*	exlv.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					exlv.collapseGroup(previousGroup);
				previousGroup = groupPosition;

			}
		});*/
		mAdapter.notifyDataSetChanged();
	
	}

	/*
	 * // Refresh ExpandableListView data exlv.deferNotifyDataSetChanged();
	 */

	private void buildData() {
		// Creating ArrayList of type parent class to store parent class objects
		// ArrayList<Service_Booking_History_Parent> list = new
		// ArrayList<Service_Booking_History_Parent>();
		/*DatabaseHandler db = new DatabaseHandler(getActivity());
		ArrayList<Service_Booking_History_Parent> list = db.getAllServices();
		for (int i = 0; i < list.size(); i++) {
			Service_Booking_History_Child child = new Service_Booking_History_Child();
			list.get(i).setChildren(
					new ArrayList<Service_Booking_History_Child>());
			child.setKms(list.get(i).getKms());
			child.setModel(list.get(i).getModel());
			child.setDate_of_booking(list.get(i).getComplaint_date());
			child.setBooked_for_time(list.get(i).getBooked_for_time());
			child.setBooked_for_dealer(list.get(i).getBooked_for_dealer());
			child.setService_type(list.get(i).getService_type());
			child.setMsv_flag(list.get(i).getMsv_flag());
			list.get(i).getChildren().add(child);
		}*/

		
		/*DatabaseHandler db = new DatabaseHandler(getActivity());
        ArrayList<Service_Booking_History_Parent> list_all = db.getAllServices();
        // ArrayList<Complaint_Registered_Parent> list = db.getComplaintsById(UserDetails.getUser_id());
        ArrayList<Service_Booking_History_Parent> list_user = new ArrayList<Service_Booking_History_Parent>() ;
        
        for (int i = 0; i <list_all.size(); i++) {
			if(list_all.get(i).userId.equals(UserDetails.getUser_id())){
				list_user.add(list_all.get(i));
			}
		}
         
        if(list_user.size()>0)
        {
        
	         for (int i = 0; i < list_user.size(); i++) {
	        	 String log ="USERID: "+list_user.get(i).getUserId()+ " SR: " + list_user.get(i).complaint_no+ " ,REG: " + list_all.get(i).complaint_reg_no+ " ,DATE: " + list_all.get(i).complaint_date;
		            Log.i("buildData", log);
	        	 
		            Service_Booking_History_Child child = new Service_Booking_History_Child();
	        	 list_user.get(i).setChildren(new ArrayList<Service_Booking_History_Child>());
		         
		         child.setDate_of_booking(list_user.get(i).getDate_of_booking());
		         child.setModel(list_user.get(i).getModel());
		         child.setKms(list_user.get(i).getKms());
		         child.setBooked_for_time(list_user.get(i).getBooked_for_time());
		         child.setBooked_for_dealer(list_user.get(i).getBooked_for_dealer());
		         child.setService_type(list_user.get(i).getService_type());
		         child.setMsv_flag(list_user.get(i).getMsv_flag());	         
		         list_user.get(i).getChildren().add(child);
			}
	         loadHosts(list_user);
        }
        else
        {*/
		if (new CheckConnectivity().checkNow(getActivity())) {
			
		
        	String req = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/getServiceBookingHistory";
        	// final DatabaseHandler db1 = new DatabaseHandler(getActivity());
        	 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
     	    nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
     	    nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
            new AWS_WebServiceCall(getActivity(), req,ServiceHandler.POST ,Constants.getServiceBookingHistory,nameValuePairs,
                          new ResponseCallback(){
            	
                                @Override
                                public void onResponseReceive(Object object) {
                               	
                                       // TODO Auto-generated method stub
                                       //Toast.makeText(getActivity(), "Service Booking Details Saved", Toast.LENGTH_LONG).show();
                               	 @SuppressWarnings("unchecked")
                               	 
                               	ArrayList<Service_Booking_History_Parent> list_all1 = new ArrayList<Service_Booking_History_Parent>();
								ArrayList<HashMap<String, String>> serviceBookingHistory = (ArrayList<HashMap<String, String>>) object;
                               	 for(int i = 0; i < serviceBookingHistory.size(); i++)
                               	 {
                               		 Service_Booking_History_Parent serviceBookingHistoryObject = new Service_Booking_History_Parent();
                               		serviceBookingHistoryObject.setUserId(serviceBookingHistory.get(i).get("user_id"));
                               		serviceBookingHistoryObject.setComplaint_sr_no(serviceBookingHistory.get(i).get("sr_number"));
                               		serviceBookingHistoryObject.setComplaint_reg_no(serviceBookingHistory.get(i).get("registration_number"));
                               		serviceBookingHistoryObject.setComplaint_date(serviceBookingHistory.get(i).get("booked_date"));
                               		serviceBookingHistoryObject.setModel(serviceBookingHistory.get(i).get("model"));
                               		serviceBookingHistoryObject.setBooked_for_time(serviceBookingHistory.get(i).get("booked_time"));
                               		serviceBookingHistoryObject.setBooked_for_dealer(serviceBookingHistory.get(i).get("service_dealer"));
                               		serviceBookingHistoryObject.setService_type(serviceBookingHistory.get(i).get("service_type"));
                               		serviceBookingHistoryObject.setMsv_flag(serviceBookingHistory.get(i).get("msv_flag"));
                               		serviceBookingHistoryObject.setKms(serviceBookingHistory.get(i).get("kms"));
            						
            							String log = "USERID: "+UserDetails.getUser_id() +" SR: " + serviceBookingHistoryObject.getComplaint_sr_no()+ " ,REG: " + serviceBookingHistoryObject.getComplaint_reg_no()+ " ,DATE: " +serviceBookingHistoryObject.getDate_of_booking();
            				            Log.i("complreg", log);
            							
            							//db1.addServiceParent(serviceBookingHistoryObject);
            							list_all1.add(serviceBookingHistoryObject);
            							//DatabaseHandler db1 = new DatabaseHandler(getActivity());
            					        
                               	 }
            							// ArrayList<Service_Booking_History_Parent> list_all1 = db1.getAllServices();
            					            // ArrayList<Complaint_Registered_Parent> list = db.getComplaintsById(UserDetails.getUser_id());
            					            ArrayList<Service_Booking_History_Parent> list_user1 = new ArrayList<Service_Booking_History_Parent>() ;
            					        	exlv.setOnGroupExpandListener(new OnGroupExpandListener() {
            					    			int previousGroup = -1;

            					    			@Override
            					    			public void onGroupExpand(int groupPosition) {
            					    				if (groupPosition != previousGroup)
            					    					exlv.collapseGroup(previousGroup);
            					    				previousGroup = groupPosition;

            					    			}
            					    		
            					    		});
            					        	
            					        	/*exlv.setOnGroupClickListener(new OnGroupClickListener() {
												
												@Override
												public boolean onGroupClick(ExpandableListView parent, View v,
														int groupPosition, long id) {
													// TODO Auto-generated method stub
													parent.expandGroup(groupPosition);
													return false;
												}
											});*/
            					            for (int j = 0; j <list_all1.size(); j++) {
            					    			if(list_all1.get(j).userId.equals(UserDetails.getUser_id())){
            					    				list_user1.add(list_all1.get(j));
            					    			}
            					    		}
            					            
            					            for (int i = 0; i < list_user1.size(); i++) {
            						        //	 String log ="USERID: "+list_user1.get(i).getUserId()+ " SR: " + list_user1.get(i).complaint_no+ " ,REG: " + list_all1.get(i).complaint_reg_no+ " ,DATE: " + list_all1.get(i).complaint_date;
            							         //   Log.i("buildData", log);
            						        	 
            							            Service_Booking_History_Child child = new Service_Booking_History_Child();
            						        	 list_user1.get(i).setChildren(new ArrayList<Service_Booking_History_Child>());
            							         
            						        	 child.setDate_of_booking(list_user1.get(i).getComplaint_date());
            							         child.setModel(list_user1.get(i).getModel());
            							         child.setKms(list_user1.get(i).getKms());
            							         child.setBooked_for_time(list_user1.get(i).getBooked_for_time());
            							         child.setBooked_for_dealer(list_user1.get(i).getBooked_for_dealer());
            							         child.setService_type(list_user1.get(i).getService_type());
            							         child.setMsv_flag(list_user1.get(i).getMsv_flag());	         
            							         list_user1.get(i).getChildren().add(child);
            								}
            						        loadHosts(list_user1);
            					
            						    	v.getRootView().setFocusable(true);
            						  		v.getRootView().requestFocus();
                                }

                                @Override
                                public void onErrorReceive(String string) {
                                       // TODO Auto-generated method stub
                                       Toast.makeText(getActivity(), "No Booking History available.", Toast.LENGTH_LONG).show();
                                   	v.getRootView().setFocusable(true);
                              		v.getRootView().requestFocus();
                                }
            }).execute();
            
           
            
     //   }
	}else {
	Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_SHORT).show();
}
       
}

	private class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private LayoutInflater inflater;

		public MyExpandableListAdapter() {
			// Create Layout Inflator
			inflater = LayoutInflater.from(getActivity());
		}

		// This Function used to inflate parent rows view

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parentView) {
			final Service_Booking_History_Parent parent = parents
					.get(groupPosition);

			// Inflate grouprow.xml file for parent rows
			convertView = inflater.inflate(R.layout.service_booking_parent_row,
					parentView, false);

			// Get grouprow.xml file elements and set values

			((TextView) convertView.findViewById(R.id.txtcomp_sr_no))
					.setText(parent.getComplaint_sr_no());
			((TextView) convertView.findViewById(R.id.txtvehicle_reg_no))
					.setText(parent.getComplaint_reg_no());
			((TextView) convertView.findViewById(R.id.txt_date)).setText(parent
					.getComplaint_date());

			if (isExpanded) {
				ImageView img = (ImageView) convertView
						.findViewById(R.id.imgarrow);
				img.setBackgroundResource(R.drawable.downarrow);
				
				mTracker.send(new HitBuilders.EventBuilder()
				 .setCategory("ui_action")
				 .setAction("list_cell_press")
				 .setLabel("ServiceBookingHistory")
				 .build());	
			}
			return convertView;
		}

		// This Function used to inflate child rows view
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parentView) {
			final Service_Booking_History_Parent parent = parents
					.get(groupPosition);
			final Service_Booking_History_Child child = parent.getChildren()
					.get(childPosition);

			// Inflate childrow.xml file for child rows
			convertView = inflater.inflate(R.layout.service_booking_child_row,
					parentView, false);

			// Get childrow.xml file elements and set values
			((TextView) convertView.findViewById(R.id.txtdec_kms))
					.setText(child.getKms());
			((TextView) convertView.findViewById(R.id.txtdec_models))
					.setText(child.getModel());
			/*((TextView) convertView.findViewById(R.id.txtdec_dateofbooking))
					.setText(child.getDate_of_booking());*/
			((TextView) convertView.findViewById(R.id.txtdec_bookfortime))
					.setText(child.getBooked_for_time());
			((TextView) convertView.findViewById(R.id.txtdec_bookfordealer))
					.setText(child.getBooked_for_dealer());
			((TextView) convertView.findViewById(R.id.txtdec_servicetype))
					.setText(child.getService_type());
			((TextView) convertView.findViewById(R.id.txtdec_msgflag))
					.setText(child.getMsv_flag());

			btn_reminder = (Button) convertView.findViewById(R.id.btn_reminder);
		

			//Calendar cal = Calendar.getInstance();
			
			
				
	        	btn_reminder.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						FragmentManager fragmentManager = getFragmentManager();
//						Fragment fragment = new NewReminderSetFragment();
//						fragmentManager.beginTransaction()
//								.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						
						mTracker.send(new HitBuilders.EventBuilder()
						 .setCategory("ui_action")
						 .setAction("button_press")
						 .setLabel("ServiceBookingHistoryReminder")
						 .build());
						
					//	Log.i("btn_reminder", child.getDate_of_booking() + " " + child.getBooked_for_time());
						 reminderDate = child.getDate_of_booking();
						 
						 SimpleDateFormat currentformat = new SimpleDateFormat("MM/dd/yyyy");
						 SimpleDateFormat required = new SimpleDateFormat("dd-MMM-yyyy");
						 Date dateshow = new Date();
						 String reminddate = "";
						 try {
							 dateshow = currentformat.parse(reminderDate);
							reminddate = required.format(dateshow);
						} catch (Exception e) {
							// TODO: handle exception
						}
				           Bundle bundle = new Bundle();
				           bundle.putString("remindregNo", parent.complaint_reg_no);
						bundle.putString("remindDate", reminddate);
						bundle.putString("remindTime", child.getBooked_for_time());
						bundle.putString("remindtype", "Service Booking");
						bundle.putString("Fragment",
					              "ServiceBookingHistoryFragment");
						FragmentManager fragmentManager = getActivity()
								.getFragmentManager();
						Fragment fragment = new Reminder_Fragment();
						fragment.setArguments(bundle);

						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment)
								.commit();
					   /*    Date date = null;
						Date dateObj;
						Calendar calNow = Calendar.getInstance();
			            Calendar calSet = (Calendar) calNow.clone();
						try {
							dateObj = curFormater.parse(child.getDate_of_booking());
							calSet .setTime(dateObj);
							date = parseFormat.parse(child.getBooked_for_time());
							 reminderTime = displayFormat.format(date);
							 Log.i("FORMATTED DATE",  reminderTime);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					   
					    String[] time = reminderTime.split(":"); 
			            calSet.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0])-1);
			            calSet.set(Calendar.MINUTE, Integer.parseInt(time[1]));
			            
					 //   calSet.set(Calendar.HOUR_OF_DAY, 14);
			         //  calSet.set(Calendar.MINUTE, 4);
			            calSet.set(Calendar.SECOND, 0);
			            calSet.set(Calendar.MILLISECOND, 0);
			           
			           if(calSet.after(calNow))
			            setAlarm(parent.complaint_sr_no,parent.complaint_reg_no,calSet);*/
					}
				});
			

			return convertView;
		}
	
	@Override
		public Object getChild(int groupPosition, int childPosition) {
			// Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
			return parents.get(groupPosition).getChildren().get(childPosition);
		}

		// Call when child row clicked
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			/****** When Child row clicked then this function call *******/

			/*
			 * if( ChildClickStatus!=childPosition) { ChildClickStatus =
			 * childPosition;
			 * 
			 * Toast.makeText(getApplicationContext(), "Parent :"+groupPosition
			 * + " Child :"+childPosition , Toast.LENGTH_LONG).show(); }
			 */

			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			int size = 0;
			if (parents.get(groupPosition).getChildren() != null)
				size = parents.get(groupPosition).getChildren().size();
			return size;
		}

		@Override
		public Object getGroup(int groupPosition) {
			Log.i("Parent", groupPosition + "=  getGroup ");

			return parents.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return parents.size();
		}

		// Call when parent row clicked
		@Override
		public long getGroupId(int groupPosition) {
			/*
			 * if(groupPosition==2 && ParentClickStatus!=groupPosition){
			 * 
			 * //Alert to user Toast.makeText(getApplicationContext(),
			 * "Parent :"+groupPosition , Toast.LENGTH_LONG).show(); }
			 * 
			 * ParentClickStatus=groupPosition; if(ParentClickStatus==0)
			 * ParentClickStatus=-1;
			 */

			return groupPosition;

		}

		@Override
		public void notifyDataSetChanged() {
			// Refresh List rows
			super.notifyDataSetChanged();
		}

		@Override
		public boolean isEmpty() {
			return ((parents == null) || parents.isEmpty());
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

	}
	/*private void setAlarm(String complaint_sr_no, String complaint_reg_no, Calendar targetCal) {

        Toast.makeText(getActivity(), "Alarm is set at " + targetCal.getTime(),
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
       
        String formattedDate = df.format(targetCal.getTime());
        intent.putExtra("DATE", formattedDate);
        intent.putExtra("SRNO", complaint_sr_no);
        intent.putExtra("REGNO", complaint_reg_no);

		 PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
		 AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
		 alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);  

    }*/
}
