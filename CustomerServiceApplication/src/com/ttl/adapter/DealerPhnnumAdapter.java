package com.ttl.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.ttl.customersocialapp.R;

public class DealerPhnnumAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final ArrayList<String> data;
	ViewHolder holder = null;
	String phone;
	public DealerPhnnumAdapter(Context context, ArrayList<String> data) {
		super(context, R.layout.dealer_phn_list, data);
		this.context = context;
		this.data = data;

	}
	public static class ViewHolder {

		TextView txtphnnum;
		ImageView call;

	}
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.dealer_phn_list, parent, false);
		
		holder = new ViewHolder();
		holder.txtphnnum = (TextView) rowView.findViewById(R.id.txtphnno);
		holder.call = (ImageView) rowView.findViewById(R.id.call);
		if(data.size()>0)
		{
			holder.txtphnnum.setText(data.get(position));
		
		
		}
		
		Log.v("dealer phn adapter", data.get(position));
		
		//phone = data.get(position);
		holder.call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
		
			//	Toast.makeText(context, "position "+position+" No: "+data.get(position),Toast.LENGTH_LONG).show();
				
				
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + data.get(position)));
				context.startActivity(callIntent);

			}
		});
		return rowView;
	}

}
