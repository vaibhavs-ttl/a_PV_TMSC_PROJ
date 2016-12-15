package com.ttl.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ttl.communication.GlobalAccessObject;
import com.ttl.customersocialapp.ManualServiceFragment;
import com.ttl.customersocialapp.R;
import com.ttl.model.LabourModel;

public class LabourAdapter extends BaseAdapter implements Filterable{

	
	private Context context;
	private boolean isChanged=false;
	private ArrayList<LabourModel> mDataFiltered;//the filtered data
	private ArrayList<LabourModel> labour_list=new ArrayList<>();
	private ArrayList<LabourModel> checked_list=new ArrayList<>();
	public static  ArrayList<LabourModel> selected_data=new ArrayList<>();
	private boolean loadedFirstTime;
	private TextView show_empty_text;
	
	private HashMap<Integer, LabourModel> map=new HashMap<>();
	
	
	public LabourAdapter(Context context,ArrayList<LabourModel> labour_list,TextView show_empty_text,boolean loadedFirstTime)
	{
		
	//	super(context, R.layout.labour_parts_custom_row, labour_list);
		this.labour_list=labour_list;
		this.context=context;
		mDataFiltered=labour_list;
		this.show_empty_text=show_empty_text;
		checked_list=GlobalAccessObject.getLabour_obj();
		this.loadedFirstTime=loadedFirstTime;
		
		
	}
	
	@Override
	public int getCount() {

		
	
	//	return mDataFiltered.size();
	return labour_list.size();
	
	}

	

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder=null;
		
		
		if (convertView==null) {
			
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView=inflater.inflate(R.layout.labour_parts_custom_row, parent,false);
			
			holder=new ViewHolder();
			
			holder.labourPartRow=(LinearLayout)convertView.findViewById(R.id.labourPartRow);
			holder.labour_desc=(TextView)convertView.findViewById(R.id.labour_desc);
			holder.labour_qty=(TextView)convertView.findViewById(R.id.labour_qty);
			holder.select_data=(CheckBox)convertView.findViewById(R.id.labour_select_box);

			holder.select_data.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					
					try
					{
					
					
					CheckBox buttonView=(CheckBox)v;
					int getPosition=(Integer)buttonView.getTag();
					labour_list.get(getPosition).setSelected_state(buttonView.isChecked());
					
						LabourModel labourModel=null;
						
						if (buttonView.isChecked()) {
						
						labourModel=new LabourModel();
						labourModel.setDefaultQty(labour_list.get(position).getDefaultQty());
						labourModel.setLabourDescription(labour_list.get(position).getLabourDescription());
						labourModel.setType("Labour");
						labourModel.setLabourType(labour_list.get(position).getLabourType());
						labourModel.setBillingHours(labour_list.get(position).getBillingHours());
						labourModel.setChecked_state(true);
						labourModel.setCheckedPosition(getPosition);	
						/*	labourModel=new LabourModel();
							labourModel.setDefaultQty(labour_list.get(getPosition).getDefaultQty());
							labourModel.setLabourDescription(labour_list.get(getPosition).getLabourDescription());
							labourModel.setType("Labour");
							labourModel.setLabourType(labour_list.get(getPosition).getLabourType());
							labourModel.setBillingHours(labour_list.get(getPosition).getBillingHours());	
							labourModel.setChecked_state(true);	*/
							
						/*	Log.v("position value", labour_list.get(position).getLabourDescription());
							Log.v("Get position value", labour_list.get(getPosition).getLabourDescription());*/
							
							
					
						map.put(getPosition, labourModel);
						selected_data.add(labourModel);
						GlobalAccessObject.setLabour_obj(map.get(getPosition));
						Log.v("item added", ""+getPosition);
				
						}
					else 
					{
						
						
						
				//		removeItems(labour_list.get((position)).getLabourDescription());
						
						
						if (map.size()>0) {
						
							
							if (selected_data!=null) {
								selected_data.remove(map.get(getPosition));	
					
								GlobalAccessObject.removeLabour_obj(map.get(getPosition));
								map.remove(getPosition);
							
							}
							
							
						}
						
						
						
						Log.v("item removed", ""+getPosition);
						
						
					}
					
					
						
				}
				catch(Exception e)
				{
					
					
					Log.v("labour error", e.toString());
					
				}
						
						
				}
			});
			
			
			
			
			/*holder.labour_desc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			
					
					
					
					showLabourDescription(labour_list.get(position).getLabourDescription());
					
					
					
					
				}
			});
			*/
			
			
			
			
			
			
			
			convertView.setTag(holder);
			convertView.setTag(R.id.labourPartRow, holder.labourPartRow);
			convertView.setTag(R.id.labour_desc, holder.labour_desc);
			convertView.setTag(R.id.labour_qty, holder.labour_qty);
			convertView.setTag(R.id.labour_select_box, holder.select_data);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		
		
		
		
		
		
		
		

		if ((position%2)==0) {
			
			
			holder.labourPartRow.setBackgroundColor(Color.parseColor("#508dba"));
		
		}
		else
		{
	
			holder.labourPartRow.setBackgroundColor(Color.parseColor("#387caf"));
		
		}
		
		
		
		
			holder.select_data.setTag(position);	
		
		
		
		holder.labour_desc.setText(labour_list.get(position).getLabourDescription());
		holder.labour_qty.setText(labour_list.get(position).getDefaultQty());
		
		
		
		
		
		
		
		
		
		/*holder.select_data.setChecked(labour_list.get(position).isSelected_state());*/
			
		
			
		
	/*	
		if (checked_list!=null) {
			
			
			
			if (position<checked_list.size()) {

			
				
				if (checked_list.get(position).isChecked_state()) {
					holder.select_data.setChecked(true);	
				}
				
				//holder.select_data.setTag(checked_list.get(position).getCheckedPosition());
			//		Log.v("checked list", "position: "+position+" checked position"+ checked_list.get(position).getCheckedPosition());

					if (checked_list.get(position).isChecked_state()) {
						holder.select_data.setChecked(true);	
					}
					
					
					for (LabourModel labourModel : checked_list) {
						
				
						Log.v("loop iteration", ""+position);
						
						if (position==labourModel.getCheckedPosition()) {
							
							
							holder.select_data.setChecked(true);
							
							
						}
						
						
					}
					
					
					
					
				
			//	int pos=checked_list.get(position).getCheckedPosition();
			
			
				
				
			}
		}*/
			
		
		if (checked_list!=null) {
			
			
		for (LabourModel labourModel : checked_list) {
			
			
			if (labourModel.getCheckedPosition()==position) {
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
	
	
	private boolean removeItems(String name)
	{
		
		boolean check=false;
		
		if (ManualServiceFragment.labourSpareList.size()>0) {
		
			
			Log.v("labourspare list ", "not empty");
			
			for (int i = 0; i < ManualServiceFragment.labourSpareList.size(); i++) {
				
				
				
				
				if (ManualServiceFragment.labourSpareList.get(i).getDesc().equalsIgnoreCase(name)) {
					
					
					ManualServiceFragment.labourSpareList.remove(i);
					check=true;
					break;
					
					
					
					
				}
				
				
				
			}
			
			
			
			
			
		}
		else
		{
			Log.v("labourspare list ", "empty");
		}
		
		
		
		return check;
		
	}
	
	
	
	static class ViewHolder
	{
		
		TextView labour_desc;
		TextView labour_qty;
		CheckBox select_data;
		LinearLayout labourPartRow;
		
	}

	
	@Override
	public Filter getFilter() {
	
		
	//	return new FilterItems();
		
		return new Filter() {
	
			
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
			
				

						
			mDataFiltered=(ArrayList<LabourModel>)results.values;
			
			
			if (mDataFiltered.size()==0) {
				
				show_empty_text.setVisibility(View.VISIBLE);
				Log.v("if called", "called");
				
			}
			else
			{
				show_empty_text.setVisibility(View.GONE);
				Log.v("else called", "called");
			}
			
			//	labour_list=mDataFiltered=(ArrayList<LabourModel>)results.values;
				
			for (int j = 0; j < mDataFiltered.size(); j++) {
				
				
				Log.v("mDataFiltered", mDataFiltered.get(j).getLabourDescription().toLowerCase());
				
			}
				
			notifyDataSetChanged();
			
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
			
				
			//	Log.v("entered character", ""+constraint);
				
				String filterString=constraint.toString().toLowerCase();
				FilterResults results=new FilterResults();
				
				final List<LabourModel> list=labour_list;
				
				int count=list.size();
				final ArrayList<LabourModel> nList=new ArrayList<>(count);
				
			//	if (!TextUtils.isEmpty(constraint)) {
					
			
				
				for(int i=0;i<count;i++)
				{
					
				
				//	Log.v("inside loop"+" "+i, list.get(i).getLabourDescription().toLowerCase());
					
					if (list.get(i).getLabourDescription().toLowerCase().startsWith(filterString)) {
						
						nList.add(list.get(i));
				
					//	Log.v("inside if", list.get(i).getLabourDescription().toLowerCase());
					}
					
					
				
					
					
				}
				
				
							
				results.values=nList;
				results.count=nList.size();
				
				
				//}
			
				/*else
				{
					
					results.values=labour_list;
					results.count=labour_list.size();
					
				}
				*/
				
				
			//	notifyDataSetChanged();
				
				
				return results;
			}
		};
	}

	@Override
	public Object getItem(int position) {
		
	//	return mDataFiltered.get(position);
		return labour_list.get(position);
	
	}


	
	
/*	
	class FilterItems extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			FilterResults results=new FilterResults();
			
			if (constraint==null || constraint.length()==0) {
			
				
				Log.v("inside filter results if", constraint.toString());
				labour_list=mDataFiltered;
				results.values=labour_list;
				results.count=labour_list.size();
				results.values=mDataFiltered;
				results.count=mDataFiltered.size();
			}
			else
			{
				
				
				
				Log.v("inside filter else results", constraint.toString());
				
				
				Log.v("labour list size", ""+labour_list.size());
				
				ArrayList<LabourModel> newList=new ArrayList<>();
				
				
					for (LabourModel labourModel : labour_list) {
						
						
						if (labourModel.getLabourDescription().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
							
							newList.add(labourModel);
						}
						
						
						results.values=newList;
						results.count=newList.size();
						
					}
				}
				
			
			
			
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			
			
			if (results.count==0) {
				
			//	mDataFiltered=labour_list;
				labour_list.clear();
				notifyDataSetChanged();
				Log.v("inside publish if results", constraint.toString());
			}
			else
			{
				
				
				labour_list=(ArrayList<LabourModel>)results.values;
				notifyDataSetChanged();
				Log.v("inside publish else results", constraint.toString());
			}
			
			
			
		}
		
	}*/
	
	
	private void showLabourDescription(String name)
	{
		
		final Dialog dialog=new Dialog(context);
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.desc_dialog);
		TextView labour_dialog_desc=(TextView)dialog.findViewById(R.id.dialog_labour_spare_desc);
		
		
		labour_dialog_desc.setText(name);
		
		
		ImageView cancel_dialog=(ImageView)dialog.findViewById(R.id.cancel_dialog_btn);
		
		cancel_dialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				dialog.dismiss();
				
				
			}
		});
		
		
		
		dialog.create();
		dialog.show();	
		
		
		
		
	}
	
	
	
	
	
}
