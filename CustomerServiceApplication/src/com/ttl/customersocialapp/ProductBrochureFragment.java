package com.ttl.customersocialapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.communication.GlobalAccessObject;
import com.ttl.communication.MyDownloadManager;
import com.ttl.customersocialapp.ProductBrochureFragment.ProductBrochureAdapter.BroucherHolder;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.DownloadModel;
import com.ttl.model.ProductBroucher;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class ProductBrochureFragment extends Fragment {

	private Tracker mTracker;
	private ListView products_list;
	BroucherHolder holder;
	View container_layout;
	DownloadNotification notification;
	private CheckConnectivity check;
	@Override
	public void onStart() {

		super.onStart();
		mTracker.setScreenName("ProductBrochureFragmentScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	
/*	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		 Log.v("Oncreate called", "On create called");
		   this.setRetainInstance(true);
	
	}*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.product_brochure_information_layout,
				container, false);

		
		notification=new DownloadNotification();
		
		
		
		setupAppTracker();
		
		
		products_list = (ListView) v.findViewById(R.id.productListView);
		
		
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
		

		
		Log.v("onCreateView", "onCreateView called");
		
		
		check=new CheckConnectivity();
		if (check.checkNow(getActivity())) {
			getBrochures();	
		}
		else
		{
			Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
			
		}
		
		


		return v;

	}

	

	
	private View getBrochures() {

		CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow.checkNow(getActivity());
		if (connect) {
			String req = Config.awsserverurl
					+ "tmsc_ch/customerapp/brochureServices/getAllBrochures";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("sessionId", UserDetails
					.getSeeionId()));
			nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails
					.getUser_id()));
			new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST,
					Constants.brochures, nameValuePairs,
					new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {

							ArrayList<ProductBroucher> brochure_list = (ArrayList<ProductBroucher>) object;
							products_list
									.setAdapter(new ProductBrochureAdapter(
											getActivity(),
											getFragmentManager(), brochure_list,R.id.product_brochure_container_layout));

						}

						@Override
						public void onErrorReceive(String response) {

							Toast.makeText(getActivity(), response,
									Toast.LENGTH_SHORT).show();

						}
					}).execute();
		} else {
			Toast toast = Toast.makeText(getActivity(),
					getString(R.string.no_network_msg), Toast.LENGTH_SHORT);
			toast.show();
		}

		return null;

	}

/*
	@Override
	public void onDestroy() {
	
		super.onDestroy();
	
	if (notification!=null) {
		getActivity().unregisterReceiver(notification);	
	}
	

	
	
	}*/

	
	@Override
	public void onResume() {
		
		if (notification!=null) {
			getActivity().registerReceiver(notification, new IntentFilter(
	                DownloadManager.ACTION_DOWNLOAD_COMPLETE));	
				
			Log.v("Onresume if", "notification is not null");
		}
		
		try {
			
		
		if (check.checkNow(getActivity())) {
			getBrochures();	
			Log.v("Onresume called", "Onresume called");
		}
		else
		{
			Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
			
		}
		
		} catch (Exception e) {
		
		
			Log.v("product exception", e.toString());
			
		}
		
		
		
		super.onResume();
	}
	
	
	@Override
	public void onStop() {
	
		if (notification!=null) {
			getActivity().unregisterReceiver(notification);	
		}
		
		
		
		
		
		super.onStop();
	}
	
	
/*	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getActivity().unregisterReceiver(notification);
	}
*/


	private void setupAppTracker()
	{
		AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
		mTracker = application.getDefaultTracker();
		  mTracker.send(new HitBuilders.EventBuilder()
		 .setCategory("ui_action")
		 .setAction("button_press")
		 .setLabel("ProductBrochureFragment")
		 .build());
		
	}


	
	

class DownloadNotification extends BroadcastReceiver
{
	
@Override
public void onReceive(Context context, Intent intent) {

	
	

	String action = intent.getAction();
    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
        long downloadId = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, 0);

        ArrayList<DownloadModel> downloadModels=GlobalAccessObject.getFiles();
    
        
        for (int i = 0; i < downloadModels.size(); i++) {
		
        	Log.v("inside if","if called");
        	
        	if (downloadId==downloadModels.get(i).getDownloadID()) {
        		
      //  		BroucherHolder h=(BroucherHolder)downloadModels.get(i).getObject();
         	   
        	//	h.dwnload.setText("View Brochure");
        		
        		
        		FragmentTransaction ft = getFragmentManager().beginTransaction();
        		ft.detach(ProductBrochureFragment.this).attach(ProductBrochureFragment.this).commit();

        		
        	
        		
         	   
         	} 	
                
        		
        	
        	
		}
        
        
        
       
    
    
    
    }



}





}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	class ProductBrochureAdapter extends BaseAdapter {

		private Context context;
		private int res;
		private BroucherHolder holder;
		private FragmentManager manager;
		private ArrayList<ProductBroucher> brochure_data;
		private CheckConnectivity check;
		private File file;

		
		
		private String files[];	
			
		public ProductBrochureAdapter(Context context,FragmentManager manager,ArrayList<ProductBroucher> brochure_data,int res) {		
			
		
			this.manager=manager;
			this.context=context;
			this.res=res;
			this.brochure_data=brochure_data;
			
			file=new File(Constants.BROCHURE_PATH+File.separator);
			files=file.list();

		}

		@Override
		public View getView(final int position,  View row, ViewGroup parent) {

			
				holder=null;
				
				if(row==null){
		    	
			    	
		    		
		    		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    	row = inflater.inflate(R.layout.product_broucher_row, parent, false);
			    	
			    	holder=new BroucherHolder();
			    	holder.icon= (ImageView) row.findViewById(R.id.imgProductIcon);
			    	holder.productName=(TextView) row.findViewById(R.id.productName);
			    	holder.dwnload=(Button)row.findViewById(R.id.btn_brochure_download);
			    	holder.urlBtn=(Button)row.findViewById(R.id.btn_visit_site);
			    	holder.container=(RelativeLayout)row.findViewById(R.id.brochure_row_container_layout);
			    	holder.inner_container_layout=(LinearLayout)row.findViewById(R.id.inner_container_layout);
			    	
			    	row.setTag(holder);
			           
			    	}
			    	else{
			    	
			    		holder=(BroucherHolder) row.getTag();
			    	}
		    	
				
			
				
				if ((position%2)==0) {
					
					
					holder.inner_container_layout.setBackgroundColor(Color.parseColor("#508dba"));
				
				}
				else
				{
			
					holder.inner_container_layout.setBackgroundColor(Color.parseColor("#387caf"));
				
				}
				
				
				
				
				
		    	Picasso.with(context).load(brochure_data.get(position).getIcon()).resize(320, 320).placeholder(R.drawable.noimage).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.icon);
		    	holder.productName.setText(brochure_data.get(position).getProduct());
		    	
		    	if (files!=null && isFileExist(brochure_data.get(position).getUpdated_at()+".pdf")) {
				
		    		holder.dwnload.setText("View Brochure");
		    		
				}
		    	else
		    	{
		    		holder.dwnload.setText("Download Brochure");
		    	}
		    	
		    	
		    	
		    	
		    	holder.dwnload.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
				
						 check=new CheckConnectivity();
					
						 LinearLayout layout=(LinearLayout)v.getParent();
						 
						 Button common_btn=(Button)layout.findViewById(R.id.btn_brochure_download);
						 
						 if (common_btn.getText().toString().equalsIgnoreCase("View Brochure")) {
							
							 openPDFViewer(Constants.BROCHURE_PATH+File.separator+brochure_data.get(position).getUpdated_at()+".pdf");
						 }
						 else if(check.checkNow(context))
						{
							 
							 Log.v("updated at", brochure_data.get(position).getUpdated_at());
							 	 
							 new MyDownloadManager(context, brochure_data.get(position).getBrochure(), brochure_data.get(position).getBrochure().substring(brochure_data.get(position).getBrochure().lastIndexOf('/')+1)).downloadFile(brochure_data.get(position).getUpdated_at()+".pdf");						
						}
						else
						{
							
							
					Toast.makeText(context, context.getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();		
							
							
						}
				
							
						
			}
				});
		    	
		    	holder.urlBtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
				
						 check=new CheckConnectivity();
				
						if(check.checkNow(context))
							
						{
							
							
						
							WebsiteBrowser websiteBrowser=new WebsiteBrowser();
							Bundle bundle=new Bundle();
							bundle.putString("website", brochure_data.get(position).getWebsite());
							bundle.putString("productNameKey", brochure_data.get(position).getProduct());
							websiteBrowser.setArguments(bundle);
							
							manager.beginTransaction().add(res, websiteBrowser).addToBackStack("website").commit();			
						}
						else 
						{
							
							Toast.makeText(context, context.getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();		
						}
						
				
				
					
					}
				});
		    	
		    return row;
			
		}
		  class BroucherHolder {

			ImageView icon;
			TextView productName;
			Button dwnload;
			Button urlBtn;
			RelativeLayout container;
			LinearLayout inner_container_layout;
		}
		@Override
		public int getCount() {
		
			return brochure_data.size();
		}

		@Override
		public Object getItem(int position) {
			
			return brochure_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}


	private void openPDFViewer(String path)
	{
		
		//Log.v("pdf view path", path);
		
		File loc=new File(path);
		
		Uri dest = Uri.fromFile(loc);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(dest, "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		
		

	}


	private boolean  isFileExist(String fileName)
	{
		

		

	for (int i = 0; i < files.length; i++) {
		
		if (files[i].equalsIgnoreCase(fileName)) {

		return true;
		
		}
		

		
	}
		
	return false;



	}



	}

	
	

}
