package com.ttl.communication;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.ttl.customersocialapp.R;

public class SendEstimateReport extends AsyncTask<Void, Void, Void>{

	
	private Dialog dialog;
	private Context context;
	private String url;
	private String response;
	//private ArrayList<NameValuePair> params=new ArrayList<>();
	private String params;
	public SendEstimateReport(Context context,String url,String params)
	{
		
		this.url=url;
		this.context=context;
		this.params=params;
		
		
	}
	
	@Override
	protected void onPreExecute() {
	
		super.onPreExecute();
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progress_bar);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.show();
	
	}
	
	@Override
	protected Void doInBackground(Void... params) {
	
		
		
			
		try {
			sendReport();	
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		
		
		
		
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
	
		super.onPostExecute(result);
	
		dialog.dismiss();
	
		 if (response!=null) {
				
			 try {
			     JSONObject object=new JSONObject(response);
					
		           if (object.has("msg")) {
					
		        	   
		        	   Toast.makeText(context,object.getString("msg"), Toast.LENGTH_LONG).show();
		        	   
		        	   
				}
			} catch (Exception e) {
			e.printStackTrace();
			}
			 
	       
	        
	        }
	
	}
	
	
	
	
	

	public void sendReport()
	{
		
	
		try {
			
		
		/*HttpClient client=new DefaultHttpClient();
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(new UrlEncodedFormEntity(params));
      
        httpResponse = client.execute(httpPost);
        httpEntity = httpResponse.getEntity();
        response = EntityUtils.toString(httpEntity);
        Log.v("estimate response" ,response);
        
*/

			HttpClient client=new DefaultHttpClient();
			HttpPost post=new HttpPost(url);
			StringEntity entity=new StringEntity(params.toString());
			
			
			
			
		post.setEntity(entity);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
		
			HttpResponse httpResponse = client.execute(post);
		
			if (httpResponse!=null) {
				Log.v("response http", "null");		
			}
		
			
			response=EntityUtils.toString(httpResponse.getEntity());
			
				if (response!=null) {
				
					
					
					JSONObject object=new JSONObject();
					
					if (object.has("msg")) {
						
						Toast.makeText(context, object.getString("msg"), Toast.LENGTH_LONG).show();
						
					}
					
					
					
				}
				else
				{
					Log.v("null response", response);
					
				}
		
				
			
			
			
			
	} catch (Exception e) {
	
		
		e.printStackTrace();
		
	}
	
		
		
		
		
	}
	
	
}
