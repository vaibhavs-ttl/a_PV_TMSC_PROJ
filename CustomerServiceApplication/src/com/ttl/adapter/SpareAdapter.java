package com.ttl.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ttl.communication.GlobalAccessObject;
import com.ttl.customersocialapp.R;
import com.ttl.model.LabourModel;
import com.ttl.model.SpareModel;

public class SpareAdapter extends BaseAdapter implements Filterable{

	

	private Context context;
	
	private ArrayList<SpareModel> mDataFiltered;//the filtered data
	private ArrayList<SpareModel> spare_list=new ArrayList<>();
	private ArrayList<SpareModel> checked_list=new ArrayList<>();
	public static ArrayList<SpareModel> selected_data=new ArrayList<>();
	private TextView show_empty_text;
	private HashMap<Integer, SpareModel> map=new HashMap<>();
	public SpareAdapter(Context context,ArrayList<SpareModel> spare_list,TextView show_empty_text)
	{
		this.spare_list=spare_list;
		this.context=context;
	this.show_empty_text=show_empty_text;
		mDataFiltered=spare_list;
		checked_list=GlobalAccessObject.getSpare_obj();
	}
	
	@Override
	public int getCount() {

		return mDataFiltered.size();
	}

	@Override
	public Object getItem(int pos) {

		return mDataFiltered.get(pos);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		/*ViewHolder holder=new ViewHolder();
		
		if (convertView==null) {
		
			LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView=inflater.inflate(R.layout.spare_part_custom_row, parent,false);
			
			holder.spare_desc=(TextView)convertView.findViewById(R.id.spare_desc);
			holder.spare_qty=(TextView)convertView.findViewById(R.id.spare_qty);
			holder.spare_uom=(TextView)convertView.findViewById(R.id.spare_uom);
			holder.select_data=(CheckBox)convertView.findViewById(R.id.spare_select_box);
			holder.sparePartRow=(LinearLayout)convertView.findViewById(R.id.sparePartRow);
			convertView.setTag(holder);
			
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		
		
		if ((position%2)==0) {
			
			
			holder.sparePartRow.setBackgroundColor(Color.parseColor("#508dba"));
		
		}
		else
		{
	
			holder.sparePartRow.setBackgroundColor(Color.parseColor("#387caf"));
		
		}
		
		
		
		
		
		
		
		
		holder.spare_desc.setText(spare_list.get(position).getPartDescription());
		holder.spare_qty.setText(spare_list.get(position).getDefaultQty());
		holder.spare_uom.setText(spare_list.get(position).getUOM());
		holder.select_data.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
				SpareModel spareModel = null;
				if (isChecked) {
					
					spareModel=new SpareModel();
					
					spareModel.setDefaultQty(spare_list.get(position).getDefaultQty());
					spareModel.setPartDescription(spare_list.get(position).getPartDescription());
					spareModel.setUOM(spare_list.get(position).getUOM());
					spareModel.setUMRP(spare_list.get(position).getUMRP());
					spareModel.setType("Spare");
					spareModel.setCheckedPosition(position);
					
					
					selected_data.add(spareModel);
				
					GlobalAccessObject.setSpare_obj(selected_data);
					
				}
				else 
				{
				
					
					selected_data.remove(spareModel);
				
					GlobalAccessObject.NullifySpare_obj();
				}
				
			}
		});
		
		
		
		if (checked_list!=null) {
			
			
			
			if (position<checked_list.size()) {

				int pos=checked_list.get(position).getCheckedPosition();
			
				
				holder.select_data.setChecked(true);
				
				
			}
		}

		*/
		
		ViewHolder holder=null;
		
		if (convertView==null) {
			
			LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView=inflater.inflate(R.layout.spare_part_custom_row, parent,false);
			holder=new ViewHolder();
			holder.spare_desc=(TextView)convertView.findViewById(R.id.spare_desc);
			holder.spare_qty=(TextView)convertView.findViewById(R.id.spare_qty);
			holder.spare_uom=(TextView)convertView.findViewById(R.id.spare_uom);
			holder.select_data=(CheckBox)convertView.findViewById(R.id.spare_select_box);
			holder.sparePartRow=(LinearLayout)convertView.findViewById(R.id.sparePartRow);
			
			
			
			holder.select_data.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					try
					{
					
					
					CheckBox buttonView=(CheckBox)v;
					int getPosition=(Integer)buttonView.getTag();
					spare_list.get(getPosition).setSelected_state(buttonView.isChecked());
					
					
					SpareModel spareModel = null;
					if (buttonView.isChecked()) {
						
						spareModel=new SpareModel();
						
					
						
						spareModel.setDefaultQty(spare_list.get(position).getDefaultQty());
						spareModel.setPartDescription(spare_list.get(position).getPartDescription());
						spareModel.setUOM(spare_list.get(position).getUOM());
						spareModel.setUMRP(spare_list.get(position).getUMRP());
						spareModel.setType("Spare");
						
						spareModel.setCheckedPosition(getPosition);
						
						selected_data.add(spareModel);
						map.put(getPosition, spareModel);
						
					
					//	GlobalAccessObject.setSpare_obj(selected_data);
						
						GlobalAccessObject.setSpare_obj(map.get(getPosition));
						Log.v("item added", "item added");
					}
					else 
					{
					//	selected_data.add(spareModel);
					
						selected_data.remove(map.get(getPosition));
						
						GlobalAccessObject.removeSpare_obj(map.get(getPosition));	
					//	selected_data.remove(spareModel);
				//	spareModel=null;
					Log.v("item removed", "item removed");
					map.remove(getPosition);
					
				//		GlobalAccessObject.NullifySpare_obj();
					}
					

					
				}
				catch(Exception ex)
				{
					
				Log.v("error log", ex.toString());
					
				}
				
					
					
				}
			});
			
			
			
			convertView.setTag(holder);
			
			
			convertView.setTag(R.id.spare_desc, holder.spare_desc);
			convertView.setTag(R.id.spare_qty, holder.spare_qty);
			convertView.setTag(R.id.spare_uom, holder.spare_uom);
			convertView.setTag(R.id.sparePartRow, holder.sparePartRow);
			convertView.setTag(R.id.spare_select_box, holder.select_data);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		
		
		if ((position%2)==0) {
			
			
			holder.sparePartRow.setBackgroundColor(Color.parseColor("#508dba"));
		
		}
		else
		{
	
			holder.sparePartRow.setBackgroundColor(Color.parseColor("#387caf"));
		
		}
		
		holder.select_data.setTag(position);
		holder.spare_desc.setText(spare_list.get(position).getPartDescription());
		holder.spare_qty.setText(spare_list.get(position).getDefaultQty());
		holder.spare_uom.setText(spare_list.get(position).getUOM());
		
	//	holder.select_data.setChecked(spare_list.get(position).isSelected_state());
		
		
	/*	if (checked_list!=null) {
			
			
			
			if (position<checked_list.size()) {

				int pos=checked_list.get(position).getCheckedPosition();
			
				holder.select_data.setChecked(true);
				
				
			}
		}*/
		
		
		if (checked_list!=null) {
			
			
			for (SpareModel spareModel : checked_list) {
				
				
				if (spareModel.getCheckedPosition()==position) {
					holder.select_data.setChecked(true);
				}
				
			}
			
			
			}
		
		
		
		
		
		
		
		
		
		
		return convertView;
	}

	
	@Override

	public int getViewTypeCount() {                 

	    return getCount();
	}

	@Override
	public int getItemViewType(int position) {

	    return position;
	}

	
	static class ViewHolder
	{
		
		TextView spare_desc;
		TextView spare_qty;
		TextView spare_uom;
		CheckBox select_data;
		LinearLayout sparePartRow;
		
		
		
	}



	@Override
	public Filter getFilter() {
	
		return new Filter() {
	
			
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
			
			
						
			mDataFiltered=(ArrayList<SpareModel>)results.values;
	
				if (mDataFiltered.size()==0) {
				
				show_empty_text.setVisibility(View.VISIBLE);
				Log.v("if called", "called");
				
			}
			else
			{
				show_empty_text.setVisibility(View.GONE);
				Log.v("else called", "called");
			}
			
			
			notifyDataSetChanged();
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
			
				
				
				String filterString=constraint.toString().toLowerCase();
				FilterResults results=new FilterResults();
				
				final List<SpareModel> list=spare_list;
				
				int count=list.size();
				final ArrayList<SpareModel> nList=new ArrayList<>(count);
				
				for(int i=0;i<count;i++)
				{
					
				
					
					if (list.get(i).getPartDescription().toLowerCase().contains(filterString)) {
						
				
						
						Log.v("spare search", ""+list.get(i).getPartDescription()+" contains"+" "+filterString);
						
						nList.add(list.get(i));
					}
					
					
				
					
					
				}
				
				results.values=nList;
				results.count=nList.size();
				
				return results;
			}
		};
	}




	
	
}
