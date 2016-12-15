package com.ttl.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttl.customersocialapp.Notifiaction_fragment;
import com.ttl.customersocialapp.R;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class NotificationAdapter extends ArrayAdapter<HashMap<String, String>> {
	Context context;

	public NotificationAdapter(Context context, int textViewResourceId,
			ArrayList<HashMap<String, String>> Strings) {

		super(context, textViewResourceId, Strings);
		this.context = context;
	}

	private class ViewHolder {

	}

	ViewHolder viewHolder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		final TextView txttitle;
		TextView txtdescription;
		final ImageView setimg;
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.notifiation_list, null);

		txttitle = (TextView) v.findViewById(R.id.txttitle);
		txtdescription = (TextView) v.findViewById(R.id.txtdescription);
		setimg = (ImageView) v.findViewById(R.id.img);
		
		txttitle.setText(Notifiaction_fragment.list.get(position).get(
				"notification_title"));
		txtdescription.setText(Notifiaction_fragment.list.get(position).get(
				"notification_desc"));
		if(Notifiaction_fragment.list.get(position).get("read_flag").equals("0"))
		{
			setimg.setBackgroundResource(R.drawable.message_unopen);
			txttitle.setTextColor(context.getResources().getColor(R.color.lightorang));

		}else
		{
			setimg.setBackgroundResource(R.drawable.message_open);
			txttitle.setTextColor(context.getResources().getColor(R.color.white));
		}
	
	

		HashMap<String, String> data = Notifiaction_fragment.list.get(position);
		if (data != null) {
			final HashMap<String, String> _data = data;
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					final Dialog dialog = new Dialog(v.getContext());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.notificationpopup);
					TextView title = (TextView) dialog
							.findViewById(R.id.txtnotification);
					TextView description = (TextView) dialog
							.findViewById(R.id.txtntfdetail);

					Button btnOK = (Button) dialog.findViewById(R.id.btnok);
					title.setText(_data.get("notification_title"));
					description.setText(_data.get("notification_desc"));
					setimg.setBackgroundResource(R.drawable.message_open);
					txttitle.setTextColor(context.getResources().getColor(R.color.white));
					if(_data.get("read_flag").equals("0"))
					{
					String req = Config.awsserverurl+"tmsc_ch/customerapp/notifications/setReadFlag";
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
					
					nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails.getUser_id()));
					nameValuePairs.add(new BasicNameValuePair("sessionId", UserDetails.getSeeionId()));
					nameValuePairs.add(new BasicNameValuePair("id",_data.get("id")));
					nameValuePairs.add(new BasicNameValuePair("readFlag","1"));
					
					new AWS_WebServiceCall(context, req, ServiceHandler.POST, Constants.setReadFlag,nameValuePairs, new ResponseCallback() {
						
						@Override
						public void onResponseReceive(Object object) {
							
							boolean success = (boolean) object;
							if(success)
							{
								_data.put("read_flag", "1");
								Notifiaction_fragment.list.get(position).put("read_flag", "1");
								
							/*	setimg.setBackgroundResource(R.drawable.message_open);
								txttitle.setTextColor(context.getResources().getColor(R.color.white));*/

							}
						}
						
						@Override
						public void onErrorReceive(String string) {
							
							
						}
					}).execute();
					}
					btnOK.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							
							dialog.cancel();
							
						}
					});

					dialog.show();
				}
			});
		}

		return v;
	}
	


}
