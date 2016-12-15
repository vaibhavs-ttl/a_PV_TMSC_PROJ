package com.ttl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ttl.customersocialapp.R;

public class GpsPdfAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] pdftxt;

	public GpsPdfAdapter(Context context, String[] title) {
		super(context, R.layout.pdflist, title);
		this.context = context;
		this.pdftxt = title;

	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.pdflist, parent, false);

		TextView txtpdf = (TextView) rowView.findViewById(R.id.txtpdf);

		txtpdf.setText(pdftxt[position]);

	
		
				
		
		return rowView;
	
	
	
	
	}

}
