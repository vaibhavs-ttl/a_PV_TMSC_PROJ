package com.ttl.customersocialapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.ServiceHandler;
import com.ttl.model.ServiceHistory;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class MaintenanceCostFragment extends Fragment
{
	
	private Button btnsch,btnbodyshop,btnunshedules ,btngraph, btnlist;
	private View rootView;
	private CombinedChart mChart;
	
	private ArrayList<ServiceHistory> lst_history = new ArrayList<ServiceHistory>() ;
	private Spinner spinner_regno;
	private List<String> regnovalues = new ArrayList<String>();
	private ListView list_history;
	private LinearLayout lllist, llgraph;
	private Tracker mTracker;
	
	@Override
	public void onStart() {
	
		super.onStart();
		mTracker.setScreenName("MaintenanceCostScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_maintenance_cost,
				container, false);
		spinner_regno = (Spinner) rootView.findViewById(R.id.spinner_regno);

				llgraph = (LinearLayout) rootView.findViewById(R.id.llgraph);
				lllist = (LinearLayout) rootView.findViewById(R.id.lllist);
				if(new UserDetails().getRegNumberList().size() == 0)
				{
					FragmentManager fragmentManager = getFragmentManager();
					Fragment fragment = new HomeFragment();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				}
				
		btnsch = (Button)rootView.findViewById(R.id.btnsch);
		btnbodyshop = (Button)rootView.findViewById(R.id.btnbodyshop);
		btnunshedules = (Button)rootView.findViewById(R.id.btnunshedules);
		btngraph = (Button) rootView.findViewById(R.id.btngraph);
		btnlist = (Button) rootView.findViewById(R.id.btnlist);

		list_history = (ListView) rootView.findViewById(R.id.listview);
		
		btngraph.setBackgroundColor(rootView.getContext()
				.getResources().getColor(R.color.darkblue));
		btngraph.setTextColor(rootView.getContext().getResources()
				.getColor(R.color.white));

		btnlist.setBackgroundColor(rootView.getContext().getResources()
				.getColor(R.color.verysoftblue));
		btnlist.setTextColor(rootView.getContext().getResources()
				.getColor(R.color.darkblue));
		llgraph.setVisibility(rootView.VISIBLE);
		lllist.setVisibility(rootView.GONE);

		 mChart = (CombinedChart) rootView.findViewById(R.id.chart1);
	        mChart.setDescription("");
	   
	        mChart.setBackgroundColor(getResources().getColor(R.color.gray_light));
	        mChart.setDrawGridBackground(false);
	        mChart.setDrawBarShadow(false);
	        
	        // draw bars behind lines
	        mChart.setDrawOrder(new DrawOrder[] {
	                DrawOrder.BAR,  DrawOrder.LINE
	        });
	        
	        //Tracker
	        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
			mTracker = application.getDefaultTracker();
	        
	        YAxis rightAxis = mChart.getAxisRight();
	        rightAxis.setAxisMinValue(0);

	        rightAxis.setValueFormatter(new YAxisValueFormatter() {
				
				@Override
				public String getFormattedValue(float value, YAxis yAxis) {
			
					 return Math.round(value)+"";
				}
			});
	        
	        rightAxis.setDrawGridLines(false);
	        rightAxis.setDrawLabels(false);
	        YAxis leftAxis = mChart.getAxisLeft();
	        leftAxis.setDrawGridLines(false);

	        XAxis xAxis = mChart.getXAxis();
	        xAxis.setPosition(XAxisPosition.BOTTOM);

	        regnovalues.add("Select Vehicle");
	    
	        int size = new UserDetails().getRegNumberList().size();
	        for (int i = 0; i < size; i++) {
	        	if(!(new UserDetails().getRegNumberList().get(i)
						.get("registration_num").toString().equals("")))
				{
				regnovalues.add(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"));
				}else
				{
					regnovalues.add(new UserDetails().getRegNumberList().get(i)
							.get("chassis_num"));
				}
	        }
	        ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),
	        		 R.layout.spinnertext, regnovalues);
	        regno.setDropDownViewResource(R.layout.spinner_selector);
	        spinner_regno.setAdapter(regno); 
	    
		
		spinner_regno.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
				if(new CheckConnectivity().checkNow(getActivity()))
				{
					
				if(position!=0){
			
				String chassis =new UserDetails().getRegNumberList().get(position-1).get("chassis_num");
				String URL = getResources().getString(R.string.URL);
				String environment = "";
				
				if(URL.contains("qa"))
				{
					environment = "QA";
				}else
				{
					environment = "Production";
				}	
				String req1 = Config.awsserverurl+"tmsc_ch/customerapp/vehicleServices/GetServiceHistoryByChassis_CSB";
				  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				  nameValuePairs.add(new BasicNameValuePair("chassis_num",chassis));
				  nameValuePairs.add(new BasicNameValuePair("environment",environment));
				  nameValuePairs.add(new BasicNameValuePair("sessionId",UserDetails.getSeeionId()));
				  nameValuePairs.add(new BasicNameValuePair("user_id",
							UserDetails.getUser_id()));
				new AWS_WebServiceCall(getActivity(), req1, ServiceHandler.POST, Constants.GetServiceHistoryByChassis_CSB,nameValuePairs, new ResponseCallback() {
					
					@Override
					public void onResponseReceive(Object object) {
						
						lst_history = (ArrayList<ServiceHistory>) object;
						
						btnsch.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.darkblue));
						btnsch.setTextColor(rootView.getContext().getResources().getColor(R.color.white));
						
						btnbodyshop.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
						btnbodyshop.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
						
						btnunshedules.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
						btnunshedules.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
						//buildChart(FREESERVICE);
						buildChartSchedule();
						rootView.getRootView().setFocusable(true);
				  		rootView.getRootView().requestFocus();
					}
					
					@Override
					public void onErrorReceive(String string) {
						
						Toast.makeText(getActivity(), "Maintenance cost not available for this Vehicle.", Toast.LENGTH_SHORT).show();
						rootView.getRootView().setFocusable(true);
				  		rootView.getRootView().requestFocus();
					}
				}).execute();	
				/*String req =	"<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					  +"<SOAP:Body>"
					  +"<GetServiceHistoryByChassis_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
					      +"<RegistrationNumber></RegistrationNumber>"
					      +"<chassis_no>"+chassis+"</chassis_no>"
					    +"</GetServiceHistoryByChassis_CSB>"
					  +"</SOAP:Body>"
					+"</SOAP:Envelope>";


						new WebServiceCall(getActivity(), req, Constants.GetServiceHistoryByChassis_CSB, new ResponseCallback() {
							
							@Override
							public void onResponseReceive(Object object) {
								
								lst_history = (ArrayList<ServiceHistory>) object;
							
								btnsch.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.darkblue));
								btnsch.setTextColor(rootView.getContext().getResources().getColor(R.color.white));
								
								btnbodyshop.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
								btnbodyshop.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
								
								btnunshedules.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
								btnunshedules.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
								//buildChart(FREESERVICE);
								buildChartSchedule();
							}
							
							

							@Override
							public void onErrorReceive(String string) {
								
								Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
							}
						},"Getting Service History.").execute();*/
						
			}
				}
				else
				{
					
					
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_network_msg), Toast.LENGTH_SHORT).show();		
					
					
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
		
				
			}
		});			
		
		btnsch.setOnClickListener(new View.OnClickListener()
	        {
				
				@Override
				public void onClick(View v) 
				{
					btnsch.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.darkblue));
					btnsch.setTextColor(rootView.getContext().getResources().getColor(R.color.white));
					
					btnbodyshop.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
					btnbodyshop.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
					
					btnunshedules.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
					btnunshedules.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
					
					if(spinner_regno.getSelectedItemPosition()!=0)
						buildChartSchedule();
					
				}

				
			});
		
		btnbodyshop.setOnClickListener(new View.OnClickListener()
        {
			
			@Override
			public void onClick(View v) 
			{
				
				btnbodyshop.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.darkblue));
				btnbodyshop.setTextColor(rootView.getContext().getResources().getColor(R.color.white));
				
				btnsch.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
				btnsch.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
				
				btnunshedules.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
				btnunshedules.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
				if(spinner_regno.getSelectedItemPosition()!=0)
				buildChartBodyShop();
			}
		});
		
		btnunshedules.setOnClickListener(new View.OnClickListener()
        {
			
			@Override
			public void onClick(View v) 
			{
				
				btnunshedules.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.darkblue));
				btnunshedules.setTextColor(rootView.getContext().getResources().getColor(R.color.white));
				
				btnbodyshop.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
				btnbodyshop.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
				
				btnsch.setBackgroundColor(rootView.getContext().getResources().getColor(R.color.verysoftblue));
				btnsch.setTextColor(rootView.getContext().getResources().getColor(R.color.darkblue));
				if(spinner_regno.getSelectedItemPosition()!=0)
				buildChartUnsch();
				
			}

			
		});
		
		btngraph.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				btngraph.setBackgroundColor(rootView.getContext()
						.getResources().getColor(R.color.darkblue));
				btngraph.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.white));

				btnlist.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.verysoftblue));
				btnlist.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.darkblue));
				llgraph.setVisibility(v.VISIBLE);
				lllist.setVisibility(v.GONE);

			}
		});

		btnlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				btnlist.setBackgroundColor(rootView.getContext().getResources()
						.getColor(R.color.darkblue));
				btnlist.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.white));

				btngraph.setBackgroundColor(rootView.getContext()
						.getResources().getColor(R.color.verysoftblue));
				btngraph.setTextColor(rootView.getContext().getResources()
						.getColor(R.color.darkblue));
				lllist.setVisibility(v.VISIBLE);
				llgraph.setVisibility(v.GONE);
				


			}
		});
		rootView.getRootView().setFocusableInTouchMode(true);
		rootView.getRootView().requestFocus();
		rootView.getRootView().setOnKeyListener(new OnKeyListener() {
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
	return rootView;	
	}
	
	private void buildChartBodyShop() {
		
		ArrayList<String > dates = new ArrayList<>();
		ArrayList<String> invc_amt = new ArrayList<>();
		if(lst_history.size()>0){
		for (int i = 0; i < lst_history.size(); i++) {
		
			if(lst_history.get(i).getSR_TYPE().contains("Accident")){
			String date = lst_history.get(i).getCLOSE_DATE().substring(0, 4);
		
			dates.add(date);
			invc_amt.add(lst_history.get(i).getINVC_AMT());
			}
		}
		
		buildChart(dates,invc_amt);
		}

	}
	


	private void buildChartSchedule() {
	
		ArrayList<String > dates = new ArrayList<>();
		ArrayList<String> invc_amt = new ArrayList<>();
		
		if(lst_history.size()>0){
		
		for (int i = 0; i < lst_history.size(); i++) {
	
			if(lst_history.get(i).getSR_TYPE().contains("Free")|| lst_history.get(i).getSR_TYPE().contains("Paid")){
			String date = lst_history.get(i).getCLOSE_DATE().substring(0, 4);
	
			dates.add(date);
			invc_amt.add(lst_history.get(i).getINVC_AMT());
			}
		}
		buildChart(dates, invc_amt);
		}else
		{
			buildChart(dates, invc_amt);
		}

	}
	
	
	
	 
	 private void buildChartUnsch() {
	
			ArrayList<String > dates = new ArrayList<>();
			ArrayList<String> invc_amt = new ArrayList<>();
			if(lst_history.size()>0){
			for (int i = 0; i < lst_history.size(); i++) {
	
				if(lst_history.get(i).getSR_TYPE().contains("PDI")||
						lst_history.get(i).getSR_TYPE().contains("Refurbish")||
						lst_history.get(i).getSR_TYPE().contains("Paid")||
						lst_history.get(i).getSR_TYPE().contains("Free")||
						lst_history.get(i).getSR_TYPE().contains("Accident")){
				
				}else{
					if(!(lst_history.get(i).getCLOSE_DATE().isEmpty()))
					{
					String date = lst_history.get(i).getCLOSE_DATE().substring(0, 4);
	
					dates.add(date);
					invc_amt.add(lst_history.get(i).getINVC_AMT());
					}
				
				}
			}
			buildChart(dates, invc_amt);
			}
		}
		
		private void buildChart(ArrayList<String> dates, ArrayList<String> invc_amt) {
	
			ArrayList<String> uniqueDates = new ArrayList<>();
			ArrayList<Float> uniqueamount = new ArrayList<>();
			ArrayList<Integer> servicecount = new ArrayList<>();
			
			HashSet<String> hs = new HashSet<String>();
	        hs.addAll(dates);
	        uniqueDates.addAll(hs);
	        Collections.sort(uniqueDates);
	        
			for (int j = 0; j < uniqueDates.size(); j++) {
	
				float	amount =0;
				int count = 0;
				//calculate amt
				for (int i = 0; i < dates.size(); i++) {
					
					if (dates.get(i).equals(uniqueDates.get(j)) ){
	
						int INVC_AMT;
						//get amount
						//get count
						count++;
						//check for INVC_AMT
						if(TextUtils.isEmpty(invc_amt.get(i)))
							INVC_AMT = 0;
						else
							INVC_AMT = Integer.parseInt(invc_amt.get(i));
						
						amount = amount + INVC_AMT ;
					}
					
				}
	
				servicecount.add(count);
				uniqueamount.add(amount);
			}
			
			
				CombinedData data = new CombinedData(uniqueDates);
				data.setValueTextColor(Color.DKGRAY);
				data.setData(generateLineData(servicecount));
		        data.setData(generateBarData(uniqueamount));
		        mChart.setData(data);
		        mChart.invalidate();
		        mChart.animateX(2000);
		        mChart.animateY(2000);
		        mChart.animateXY(2000, 2000);
				HistoryListAdapter ad = new HistoryListAdapter(getActivity(),uniqueDates,uniqueamount,servicecount);
				list_history.setAdapter(ad);
		        
		}
		
	 private BarData generateBarData(ArrayList<Float> uniqueamount) {

	        BarData d = new BarData();

	        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

	        for (int index = 0; index < uniqueamount.size(); index++)
	            entries.add(new BarEntry(uniqueamount.get(index), index));

	        BarDataSet set = new BarDataSet(entries, "Invoiced Amount");
	        set.setColor(getResources().getColor(R.color.yellow));
	        set.setDrawValues(false);
	        set.setBarSpacePercent(50f);
	
	        d.addDataSet(set);

	        set.setAxisDependency(YAxis.AxisDependency.LEFT);

	        return d;
	    }
		 
		 private LineData generateLineData(ArrayList<Integer> servicecount) {

		        LineData d = new LineData();

		        ArrayList<Entry> entries = new ArrayList<Entry>();

		        for (int index = 0; index < servicecount.size(); index++)
		            entries.add(new Entry(servicecount.get(index), index));

		        LineDataSet set = new LineDataSet(entries, "Number of Visits");
		        set.setColor(getResources().getColor(R.color.navyblue));
		        set.setLineWidth(2.5f);
		        set.setCircleColor(getResources().getColor(R.color.navyblue));
		        set.setCircleSize(5f);
		        set.setFillColor(getResources().getColor(R.color.navyblue));
	
		        set.setDrawValues(true);
		        set.setValueTextSize(15f);
		        set.setValueTextColor(getResources().getColor(R.color.navyblue));
		        set.setAxisDependency(YAxis.AxisDependency.RIGHT);

		        d.addDataSet(set);

		        return d;
		    }
		 
		 public class HistoryListAdapter extends BaseAdapter {
				
				private Context context;
				ArrayList<String> uniqueDates = new ArrayList<>();
				ArrayList<Float> uniqueamount = new ArrayList<>();
				ArrayList<Integer> servicecount = new ArrayList<>();
				
				public HistoryListAdapter(Context context, ArrayList<String> uniqueDates, ArrayList<Float> uniqueamount, ArrayList<Integer> servicecount){
					this.context = context;
					this.uniqueDates = uniqueDates;
					this.uniqueamount = uniqueamount;
					this.servicecount = servicecount;
				}

				@Override
				public int getCount() {
					return uniqueDates.size();
				}

				@Override
				public Object getItem(int position) {		
					return null;
				}

				@Override
				public long getItemId(int position) {
					return position;
				}

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
			            LayoutInflater mInflater = (LayoutInflater)
			                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			            convertView = mInflater.inflate(R.layout.maintainacecost_list_row, null);
			        }
			         
			       TextView txtyear = (TextView) convertView.findViewById(R.id.txtyear);
			       TextView txtrs = (TextView) convertView.findViewById(R.id.txtrs);
			       TextView txtvisits = (TextView) convertView.findViewById(R.id.txtvisits);
			       txtvisits.setText(servicecount.get(position) +"");
			       
			       txtyear.setText(uniqueDates.get(position));
			       txtrs.setText(String.valueOf(uniqueamount.get(position)));
			        
			        return convertView;
				}

			}
}
