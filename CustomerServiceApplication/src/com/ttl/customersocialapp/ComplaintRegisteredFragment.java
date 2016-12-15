package com.ttl.customersocialapp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.ttl.model.Complaint_Registered_Child;
import com.ttl.model.Complaint_Registered_Parent;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;


public class ComplaintRegisteredFragment extends Fragment {

	
	  ExpandableListView exlv;
	  private ArrayList<Complaint_Registered_Parent> parents;
	  View v;
	  boolean connect;
	  Tracker mTracker;
	  
	  @Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mTracker.setScreenName("ComplaintRegisteredScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	  @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			  v = inflater.inflate(R.layout.activity_complaint_registered, container, false);
				CheckConnectivity checknow = new CheckConnectivity();
			 connect = checknow
						.checkNow(getActivity());

				exlv = (ExpandableListView)v.findViewById(R.id.list);
				
				AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
				mTracker = application.getDefaultTracker();
						
			      buildData();
			      exlv.setOnGroupExpandListener(new OnGroupExpandListener() {
						int previousGroup = -1;

						@Override
						public void onGroupExpand(int groupPosition) {
							 if(groupPosition != previousGroup)
					                exlv.collapseGroup(previousGroup);
					            previousGroup = groupPosition;
							
						}
					});
			        // Adding ArrayList data to ExpandableListView values
			    //    loadHosts(dummyList);
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
	  
	  
		private void buildData()
	    {
	        // Creating ArrayList of type parent class to store parent class objects
	    
			Log.i("buildData", "called");
		
			if (connect) {
	        	 String req = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/getComplaintHistory";
	        	 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
			nameValuePairs.add(new BasicNameValuePair("user_id",UserDetails.getUser_id()));
			nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
				
	        	 final ArrayList<Complaint_Registered_Parent> list_all1 = new ArrayList<Complaint_Registered_Parent>();
                 new AWS_WebServiceCall(getActivity(), req,ServiceHandler.POST ,Constants.getComplaintHistory,nameValuePairs,
                               new ResponseCallback(){

                                     @Override
                                     public void onResponseReceive(Object object) {
                                    	// DatabaseHandler db1 = new DatabaseHandler(getActivity());
            
                                            //Toast.makeText(getActivity(), "Service Booking Details Saved", Toast.LENGTH_LONG).show();
                                    	 @SuppressWarnings("unchecked")
										ArrayList<HashMap<String, String>> complaintHistory = (ArrayList<HashMap<String, String>>) object;
                                    	 for(int i = 0; i < complaintHistory.size(); i++)
                                    	 {
                                    		 Complaint_Registered_Parent complreg=new Complaint_Registered_Parent();
                 							complreg.setUserId(complaintHistory.get(i).get("user_id"));
                 							complreg.setComplaint_no(complaintHistory.get(i).get("complaint_number"));
                 							complreg.setComplaint_reg_no(complaintHistory.get(i).get("registration_number"));
											complreg.setComplaint_date(complaintHistory.get(i).get("complaint_date"));
                 							complreg.setModel(complaintHistory.get(i).get("model"));
                 							complreg.setPrimary_area(complaintHistory.get(i).get("primary_complaint_area"));
                 							complreg.setSub_area(complaintHistory.get(i).get("sub_area"));
                 							complreg.setProblem_area(complaintHistory.get(i).get("problem_area"));
                 						
                 							//String log = "USERID: "+UserDetails.getUser_id() +" SR: " + complreg.getComplaint_no()+ " ,REG: " + complreg.getComplaint_reg_no()+ " ,DATE: " +complreg.getComplaint_date();
            
                 							
                 							//db1.addComplaint(complreg);
                 							list_all1.add(complreg);
                 							
                                    	 }
                                    	// ArrayList<Complaint_Registered_Parent> list_all1 = db1.getAllComplaints();
              					        // ArrayList<Complaint_Registered_Parent> list = db.getComplaintsById(UserDetails.getUser_id());
              					       
                                    	 ArrayList<Complaint_Registered_Parent> list_user1 = new ArrayList<Complaint_Registered_Parent>() ;
              					        
              					        for (int j = 0; j <list_all1.size(); j++) {
              								if(list_all1.get(j).userId.equals(UserDetails.getUser_id())){
              									list_user1.add(list_all1.get(j));
              								}
              							}
              					        
              					      for (int i = 0; i < list_user1.size(); i++) {
              				        	 String log ="USERID: "+list_user1.get(i).getUserId()+ " SR: " + list_user1.get(i).complaint_no+ " ,REG: " + list_all1.get(i).complaint_reg_no+ " ,DATE: " + list_all1.get(i).complaint_date;
              					            Log.i("buildData", log);
              				        	 
              				        	 Complaint_Registered_Child child = new Complaint_Registered_Child();
              				        	 list_user1.get(i).setChildren(new ArrayList<Complaint_Registered_Child>());
              					         
              					         child.setDate(list_user1.get(i).getComplaint_date());
              					         child.setModel(list_user1.get(i).getModel());
              					         child.setPrimary_area(list_user1.get(i).getPrimary_area());
              					         child.setSub_area(list_user1.get(i).getSub_area());
              					         child.setProblem_area(list_user1.get(i).getProblem_area());
              				             
              					         list_user1.get(i).getChildren().add(child);
              						}
              					        loadHosts(list_user1);
              					  	v.getRootView().setFocusable(true);
              				  		v.getRootView().requestFocus();
                                     }

                                     @Override
                                     public void onErrorReceive(String string) {
         
                                            Toast.makeText(getActivity(), "No complaint registered through App.", Toast.LENGTH_LONG).show();
                                        	v.getRootView().setFocusable(true);
                                      		v.getRootView().requestFocus();
                                     }
                 }).execute();
			}
			 else {
				Toast toast = Toast.makeText(getActivity(),
						"No network connection available.",
						Toast.LENGTH_SHORT);
				toast.show();
			}
                 
                
	      //  }
	        
	}
	
	 private void loadHosts(final ArrayList<Complaint_Registered_Parent> newParents)
	    {
	        if (newParents == null)
	            return;
	         
	        parents = newParents;
	         
	        // Check for ExpandableListAdapter object
	       
	             //Create ExpandableListAdapter Object
	            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();
	             
	            // Set Adapter to ExpandableList Adapter
	            exlv.setAdapter(mAdapter);
	        }
	      
	           /*  // Refresh ExpandableListView data 
	        	exlv.deferNotifyDataSetChanged();*/
	        
	
	 

		
		private class MyExpandableListAdapter extends BaseExpandableListAdapter
	    {
	         
	 
	        private LayoutInflater inflater;
	 
	        public MyExpandableListAdapter()
	        {
	            // Create Layout Inflator
	            inflater = LayoutInflater.from(getActivity());
	        }
	     
	         
	        // This Function used to inflate parent rows view
	         
	        @Override
	        public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parentView)
	        {
	            final Complaint_Registered_Parent parent = parents.get(groupPosition);
	             
	            // Inflate grouprow.xml file for parent rows
	            convertView = inflater.inflate(R.layout.complaint_registered_parent_row, parentView, false); 
	             
	            // Get grouprow.xml file elements and set values
	            
	            ((TextView) convertView.findViewById(R.id.txtcomp_no)).setText(parent.getComplaint_no());
	            ((TextView) convertView.findViewById(R.id.txtvehicle_reg_no)).setText(parent.getComplaint_reg_no());
	            ((TextView) convertView.findViewById(R.id.txt_date)).setText(parent.getComplaint_date());
		          
	            if(isExpanded)
	            {
	            	ImageView img =(ImageView) convertView.findViewById(R.id.imgarrow);
	            	img.setBackgroundResource(R.drawable.downarrow);
	            	mTracker.send(new HitBuilders.EventBuilder()
					 .setCategory("ui_action")
					 .setAction("list_cell_press")
					 .setLabel("ComplaintRegistered")
					 .build());	
	            }
	            return convertView;
	        }
	 
	         
	        // This Function used to inflate child rows view
	        @Override
	        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, 
	                View convertView, ViewGroup parentView)
	        {
	            final Complaint_Registered_Parent parent = parents.get(groupPosition);
	            final Complaint_Registered_Child child = parent.getChildren().get(childPosition);
	             
	            // Inflate childrow.xml file for child rows
	            convertView = inflater.inflate(R.layout.complaint_registered_child_row, parentView, false);
	             
	            // Get childrow.xml file elements and set values
	            ((TextView) convertView.findViewById(R.id.txtdec_date)).setText(child.getDate());
	            ((TextView) convertView.findViewById(R.id.txtdec_model)).setText(child.getModel());
	            ((TextView) convertView.findViewById(R.id.txtdec_prim_area)).setText(child.getPrimary_area());
	            ((TextView) convertView.findViewById(R.id.txtdec_subarea)).setText(child.getSub_area());
	            ((TextView) convertView.findViewById(R.id.txtdec_prob_area)).setText(child.getProblem_area());
	            
	         
	            return convertView;
	        }
	 
	         
	        @Override
	        public Object getChild(int groupPosition, int childPosition)
	        {
	            //Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
	            return parents.get(groupPosition).getChildren().get(childPosition);
	        }
	 
	        //Call when child row clicked
	        @Override
	        public long getChildId(int groupPosition, int childPosition)
	        {
	            /****** When Child row clicked then this function call *******/
	            
	        	/* if( ChildClickStatus!=childPosition)
	             {
	                ChildClickStatus = childPosition;
	                 
	                Toast.makeText(getApplicationContext(), "Parent :"+groupPosition + " Child :"+childPosition , 
	                         Toast.LENGTH_LONG).show();
	             }  */
	              
	            return childPosition;
	        }
	 
	        @Override
	        public int getChildrenCount(int groupPosition)
	        {
	            int size=0;
	            if(parents.get(groupPosition).getChildren()!=null)
	                size = parents.get(groupPosition).getChildren().size();
	            return size;
	        }
	      
	         
	        @Override
	        public Object getGroup(int groupPosition)
	        {
	     
	             
	            return parents.get(groupPosition);
	        }
	 
	        @Override
	        public int getGroupCount()
	        {
	            return parents.size();
	        }
	 
	        //Call when parent row clicked
	        @Override
	        public long getGroupId(int groupPosition)
	        {
	        	/*if(groupPosition==2 && ParentClickStatus!=groupPosition){
	                 
	                //Alert to user
	                Toast.makeText(getApplicationContext(), "Parent :"+groupPosition , 
	                        Toast.LENGTH_LONG).show();
	            }
	             
	            ParentClickStatus=groupPosition;
	            if(ParentClickStatus==0)
	                ParentClickStatus=-1;*/
	             
	            return groupPosition;
	      
	        }
	 
	        @Override
	        public void notifyDataSetChanged()
	        {
	            // Refresh List rows
	            super.notifyDataSetChanged();
	        }
	 
	        @Override
	        public boolean isEmpty()
	        {
	            return ((parents == null) || parents.isEmpty());
	        }
	 
	        @Override
	        public boolean isChildSelectable(int groupPosition, int childPosition)
	        {
	            return true;
	        }
	 
	        @Override
	        public boolean hasStableIds()
	        {
	            return true;
	        }
	 
	        @Override
	        public boolean areAllItemsEnabled()
	        {
	            return true;
	        }


	    }	
		
		
}
