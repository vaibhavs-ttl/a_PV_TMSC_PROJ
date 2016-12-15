package com.ttl.adapter;

import java.io.File;
import java.util.ArrayList;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ttl.communication.CheckConnectivity;
import com.ttl.communication.MyDownloadManager;
import com.ttl.customersocialapp.R;
import com.ttl.customersocialapp.WebsiteBrowser;
import com.ttl.model.ProductBroucher;
import com.ttl.webservice.Constants;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductBrochureAdapter extends BaseAdapter {

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

		
			holder=new BroucherHolder();
			
			if(row==null){
	    	
		    	
	    		
	    		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	row = inflater.inflate(R.layout.product_broucher_row, parent, false);
		    	
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
	public static class BroucherHolder {

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
