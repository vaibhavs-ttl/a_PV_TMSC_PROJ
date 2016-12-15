package com.ttl.adapter;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttl.customersocialapp.R;

public class DealerEmailAdapter extends ArrayAdapter<String>{

	private Context context;
	private ArrayList<String> email_data;
	private ViewHolder holder = null;
	private String email_text;
	public DealerEmailAdapter(Context context,ArrayList<String> data)
	{
		super(context, R.layout.dealer_email_list, data);
		this.context = context;
		this.email_data = data;
		
	}
	
	
	@Override
	public int getCount() {

		return email_data.size();
	}

	

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder {

		TextView emailView;
		ImageView msg;

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.dealer_email_list, parent, false);
		
		holder = new ViewHolder();
		holder.emailView = (TextView) rowView.findViewById(R.id.txtemail);
		holder.msg = (ImageView) rowView.findViewById(R.id.email);
		if(email_data.size()>0)
		{
			holder.emailView.setText(email_data.get(position));
		
		}
		
		Log.v("dealer email adapter", email_data.get(position));
		
		
		
		email_text = email_data.get(position);
		holder.msg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
			
				Log.v("email text", email_text);
				
				String email_to[]=email_data.toArray(new String[email_data.size()]);
				
				String URI = "mailto:?subject=" + "" + "&body=" + "";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri data = Uri.parse(URI);
				intent.setData(data);
				intent.putExtra(Intent.EXTRA_EMAIL,email_to);

				intent.putExtra(Intent.EXTRA_CC, "");
				context.startActivity(intent);


			}
		});


holder.emailView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
			
				
				/*
				String URI = "mailto:?subject=" + "" + "&body=" + "";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri data = Uri.parse(URI);
				intent.setData(data);
				intent.putExtra(Intent.EXTRA_EMAIL, email_text);

				intent.putExtra(Intent.EXTRA_CC, "");
				context.startActivity(intent);
*/

					Log.v("email text", email_text);
				
				String email_to[]=email_data.toArray(new String[email_data.size()]);
				
				String URI = "mailto:?subject=" + "" + "&body=" + "";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri data = Uri.parse(URI);
				intent.setData(data);
				intent.putExtra(Intent.EXTRA_EMAIL,email_to);

				intent.putExtra(Intent.EXTRA_CC, "");
				context.startActivity(intent);

				
				
			}
		});

		
		
		return rowView;
	}

}
