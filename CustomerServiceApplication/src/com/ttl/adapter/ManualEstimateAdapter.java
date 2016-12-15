package com.ttl.adapter;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ttl.communication.GlobalAccessObject;
import com.ttl.customersocialapp.ManualServiceFragment;
import com.ttl.customersocialapp.R;
import com.ttl.model.LabourSpareModel;
import com.ttl.webservice.Constants;

public class ManualEstimateAdapter extends BaseAdapter{

	
	private Context context;
	private ViewHolder holder;
	private ArrayList<LabourSpareModel> labourSpareModel;
	private double rate;
	private String labourType;
	private String qty;
	
	public ManualEstimateAdapter(Context context,ArrayList<LabourSpareModel> labourSpareModel,double rate)
	{
		
		this.context=context;
		this.rate=rate;
		this.labourSpareModel=labourSpareModel;
	}
	
	@Override
	public int getCount() {
	
		return labourSpareModel.size();
	}

	@Override
	public Object getItem(int position) {
		
		return labourSpareModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
		
		
		
		
		if (convertView==null) {
			
			holder=new ViewHolder();
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.manual_est_list_custom_row, parent, false);
			
			holder.selected_labour_spare_deletebox=(ImageView)convertView.findViewById(R.id.selected_spare_labour_deletebox);
			holder.selected_labour_spare_editbox=(ImageView)convertView.findViewById(R.id.selected_spare_labour_editbox);
			holder.selected_labour_spare_desc=(TextView)convertView.findViewById(R.id.selected_spare_labour_desc);
			holder.selected_labour_spare_value=(TextView)convertView.findViewById(R.id.selected_spare_labour_value);
			holder.selected_labour_spare_type=(TextView)convertView.findViewById(R.id.selected_spare_labour_type);
			holder.selected_labour_spare_qty=(EditText)convertView.findViewById(R.id.selected_spare_labour_qty);
			holder.custom_parent_row=(LinearLayout)convertView.findViewById(R.id.custom_parent_row);
			
			convertView.setTag(holder);
		
			
		}
		else
		{
			
	
			holder=(ViewHolder)convertView.getTag();
			
			
		}
		
		
		
		if ((position%2)==0) {
			
			
			holder.custom_parent_row.setBackgroundColor(Color.parseColor("#508dba"));
		
		}
		else
		{
	
			holder.custom_parent_row.setBackgroundColor(Color.parseColor("#387caf"));
		
		}
		
		
		holder.selected_labour_spare_desc.setTextColor(Color.parseColor("#FFFFFF"));
		holder.selected_labour_spare_qty.setTextColor(Color.parseColor("#FFFFFF"));
		
		holder.selected_labour_spare_type.setTextColor(Color.parseColor("#FFFFFF"));
		
		holder.selected_labour_spare_value.setTextColor(Color.parseColor("#FFFFFF"));

	/*	holder.selected_labour_spare_desc.setGravity(Gravity.LEFT);
		holder.selected_labour_spare_desc.setTextSize(10);
	
	=//	holder.selected_labour_spare_desc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	//	holder.selected_labour_spare_desc.setPadding(5, 5, 5, 5);
	*/	
		
		
		
		
		holder.selected_labour_spare_qty.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
				
				
				if (actionId==EditorInfo.IME_ACTION_DONE) {
				
				InputMethodManager manager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.hideSoftInputFromWindow(holder.selected_labour_spare_qty.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
				LinearLayout layout=(LinearLayout)view.getParent();
				EditText text=(EditText)layout.findViewById(R.id.selected_spare_labour_qty);	
				text.setEnabled(false);
				
				ManualServiceFragment.approx_labour_amt.setText("");
				ManualServiceFragment.approx_spare_amt.setText("");
				
				ManualServiceFragment.approx_labour_amt.setVisibility(View.INVISIBLE);
				ManualServiceFragment.approx_spare_amt.setVisibility(View.INVISIBLE);
				
				TextView valueView=(TextView)layout.findViewById(R.id.selected_spare_labour_value);	
				labourSpareModel.get(position).setQty(text.getText().toString());
				valueView.setText(""+calculateResults(text.getText().toString(),position,valueView));

				return true;
				
				}
				
				return false;
			}
		});
		
		
		
		holder.selected_labour_spare_deletebox.setOnClickListener(new View.OnClickListener() {
			
			
			
			
			@Override
			public void onClick(View v) {
			
			
				
			if (labourSpareModel.size()>0) {
				Log.v("remove type", labourSpareModel.get(position).getType());
				
				if (labourSpareModel.get(position).getType().equalsIgnoreCase("Labour")) {
					
					
				
					if (GlobalAccessObject.removeLabourByName(labourSpareModel.get(position).getDesc())) {
						
						
						Log.v("labour model deleted", "labour model deleted");
						
					}
					
					
					
					
				}
		
			
				else if (labourSpareModel.get(position).getType().equalsIgnoreCase("Spare")) {
					
					
					
					if (GlobalAccessObject.removeSpareByName(labourSpareModel.get(position).getDesc())) {
						
						
						Log.v("labour model deleted", "labour model deleted");
						
					}
					
					
					
					
				}
		
			}
			
		
			
				
				labourSpareModel.remove(position);
				
				/*else if(labourSpareModel.get(position).getType().equalsIgnoreCase("Spare"))
				{
					
				
					if (GlobalAccessObject.removeLabourByName(labourSpareModel.get(position).getDesc())) {
						
						
						Log.v("", "");
						
					}
					
				}*/
				
				if (labourSpareModel.size()==0) {
					
				ManualServiceFragment.manual_est_selected_items_header.setVisibility(View.INVISIBLE);
				ManualServiceFragment.approx_labour_amt.setVisibility(View.INVISIBLE);
				ManualServiceFragment.approx_spare_amt.setVisibility(View.INVISIBLE);	
			/*	ManualServiceFragment.manual_est_calculate_btn.setVisibility(View.INVISIBLE);
				ManualServiceFragment.manual_est_reset_btn.setVisibility(View.INVISIBLE);
				ManualServiceFragment.manual_est_email_btn.setVisibility(View.INVISIBLE);*/
				ManualServiceFragment.approx_labour_amt.setVisibility(View.INVISIBLE);
				ManualServiceFragment.approx_spare_amt.setVisibility(View.INVISIBLE);
				ManualServiceFragment.approx_spare_amt.setText("");
				ManualServiceFragment.approx_labour_amt.setText("");
				ManualServiceFragment.header_view1.setVisibility(View.INVISIBLE);
				ManualServiceFragment.header_view2.setVisibility(View.INVISIBLE);
				ManualServiceFragment.manualEstimateAdapter=null;
				ManualServiceFragment.labourCost=0.0;
				ManualServiceFragment.spareCost=0.0;
				ManualServiceFragment.txtLabourSpareNote.setVisibility(View.VISIBLE);
				ManualServiceFragment.txtCalcalationNote.setVisibility(View.INVISIBLE);
				ManualServiceFragment.labourSpareList.clear();
				if (ManualServiceFragment.selected_labour_data!=null) {
				
					ManualServiceFragment.selected_labour_data.clear();
				}
				
				
				if (ManualServiceFragment.selected_spare_data!=null) {
					
					ManualServiceFragment.selected_spare_data.clear();	
				}
				
			
				//	ManualServiceFragment.selected_spare_data.clear();
				}
				
				else
				{
					
					ManualServiceFragment.txtCalcalationNote.setVisibility(View.INVISIBLE);
					ManualServiceFragment.approx_labour_amt.setVisibility(View.INVISIBLE);
					ManualServiceFragment.approx_spare_amt.setVisibility(View.INVISIBLE);
					ManualServiceFragment.approx_spare_amt.setText("");
					ManualServiceFragment.approx_labour_amt.setText("");
					ManualServiceFragment.labourCost=0.0;
					ManualServiceFragment.spareCost=0.0;
				}
				
				notifyDataSetChanged();
				
				
			}
		});
		
		holder.selected_labour_spare_editbox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
		
					
				LinearLayout layout=(LinearLayout)view.getParent();
				EditText text=(EditText)layout.findViewById(R.id.selected_spare_labour_qty);	
				text.setEnabled(true);
				
				TextView value_text=(TextView)layout.findViewById(R.id.selected_spare_labour_value);
				
				
				QuantityEditDialog(labourSpareModel.get(position).getDesc(),position,text,value_text,labourSpareModel.get(position).getQty());
				
				
			}
		});
		
		
		
		labourType=labourSpareModel.get(position).getServiceType();
		
		 qty= labourSpareModel.get(position).getQty();
		
		
		if (labourType.length()>0 && labourType.equalsIgnoreCase(Constants.JOB_CODE)) {
			
			double result=(rate*Double.valueOf(labourSpareModel.get(position).getBillingHours())*Double.valueOf(labourSpareModel.get(position).getQty()));
			
			
			
			holder.selected_labour_spare_value.setText(""+String.format( "%.2f", result));
			
			labourSpareModel.get(position).setValue(Float.parseFloat(String.format( "%.2f", result)));
			GlobalAccessObject.setLabour_spare_obj(labourSpareModel);
			
		}
		else if(labourType.length()>0 && labourType.equalsIgnoreCase(Constants.SERVICE_CODE)) 
		{
			
			double result=(Double.valueOf(labourSpareModel.get(position).getQty())*Double.valueOf(labourSpareModel.get(position).getBillingHours()));
		
			
			holder.selected_labour_spare_value.setText(""+String.format( "%.2f", result));
			labourSpareModel.get(position).setValue(Float.parseFloat(String.format( "%.2f", result)));
			GlobalAccessObject.setLabour_spare_obj(labourSpareModel);
		}
		else
		{
			double result=(Double.valueOf(labourSpareModel.get(position).getQty())*Double.valueOf(labourSpareModel.get(position).getUmrp()));
			holder.selected_labour_spare_value.setText(""+String.format( "%.2f", result));
			labourSpareModel.get(position).setValue(Float.parseFloat(String.format( "%.2f", result)));
			GlobalAccessObject.setLabour_spare_obj(labourSpareModel);
		}
		
		
		
		holder.selected_labour_spare_type.setText(labourSpareModel.get(position).getType());
		
		holder.selected_labour_spare_desc.setText(labourSpareModel.get(position).getDesc());
		
		holder.selected_labour_spare_qty.setText(labourSpareModel.get(position).getQty());
		
		
		return convertView;
	}
	
	
	private double calculateResults(String value,int position,TextView totalValue)
	{
		double result=0.0;
		String labourType=labourSpareModel.get(position).getServiceType();
		
		String qty= labourSpareModel.get(position).getQty();
		
		Log.v("data", "quantity: "+qty+" labour type: "+labourType);
		
		
		if (labourType.length()>0 && labourType.equalsIgnoreCase(Constants.JOB_CODE)) {
			
			result=(rate*Double.valueOf(labourSpareModel.get(position).getBillingHours())*Double.valueOf(value));
			
			totalValue.setText(String.valueOf(String.format( "%.2f", result)));
			
			labourSpareModel.get(position).setValue(Float.parseFloat(String.format( "%.2f", result)));
			GlobalAccessObject.setLabour_spare_obj(labourSpareModel);
		//	notifyDataSetChanged();
			
		}
		else if(labourType.length()>0 && labourType.equalsIgnoreCase(Constants.SERVICE_CODE)) 
		{
			
			result=(Double.valueOf(value)*Double.valueOf(labourSpareModel.get(position).getBillingHours()));
		
			
			totalValue.setText(String.valueOf(String.format( "%.2f", result)));
			labourSpareModel.get(position).setValue(Float.parseFloat(String.format( "%.2f", result)));
			GlobalAccessObject.setLabour_spare_obj(labourSpareModel);
		//	notifyDataSetChanged();
		}
		else
		{
			Log.v("rate result", "QTY: "+labourSpareModel.get(position).getQty()+"");
			
			result=(Double.valueOf(value)*Double.valueOf(labourSpareModel.get(position).getUmrp()));
			
			Log.v("inside else", String.valueOf(result));
			totalValue.setText(String.valueOf(String.format( "%.2f", result)));
			labourSpareModel.get(position).setValue(Float.parseFloat(String.format( "%.2f", result)));
			GlobalAccessObject.setLabour_spare_obj(labourSpareModel);
			//	notifyDataSetChanged();
		}
		

		
		return result;
	}
	
	
	
	
	static class ViewHolder
	{
		TextView selected_labour_spare_type;
		EditText selected_labour_spare_qty;
		TextView selected_labour_spare_desc;
		TextView selected_labour_spare_value;
		ImageView selected_labour_spare_editbox;
		ImageView selected_labour_spare_deletebox;
		LinearLayout custom_parent_row;
	
	}
	
	private void QuantityEditDialog(String desc,final int pos,final EditText text,final TextView valueView,String default_qty)
	{
		
		
			
		final EditText quantityBox;
		Button updateQty;
		ImageView close_dialog;
		TextView edit_dialog_lbr_spare_desc;
		final EditText quantity_dialog_box;
		final Dialog dialog=new Dialog(context);
		LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view=inflater.inflate(R.layout.update_quanity_dialog,null,false);
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		updateQty=(Button)view.findViewById(R.id.update_quantity_btn);
		quantity_dialog_box=(EditText)view.findViewById(R.id.quantity_dialog_box);
		close_dialog=(ImageView)view.findViewById(R.id.dismiss_update_dialog);
		edit_dialog_lbr_spare_desc=(TextView)view.findViewById(R.id.dialog_labour_spare_desc);
		
		edit_dialog_lbr_spare_desc.setText(desc);
		quantity_dialog_box.setText(default_qty);
		
		
		
	

		
		updateQty.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
		
				
				
				if (quantity_dialog_box.getText().toString().trim().length()==0 || Integer.valueOf(quantity_dialog_box.getText().toString())==0) {
				
				
					quantity_dialog_box.setError("Please enter Quantity.");
					
				}
				else
				{
				View layout=(View)v.getParent();
				ManualServiceFragment.approx_labour_amt.setText("");
				ManualServiceFragment.approx_spare_amt.setText("");
				ManualServiceFragment.approx_labour_amt.setVisibility(View.INVISIBLE);
				ManualServiceFragment.approx_spare_amt.setVisibility(View.INVISIBLE);
				
				// valueView=(TextView)layout.findViewById(R.id.selected_spare_labour_value);	
			
				labourSpareModel.get(pos).setQty(quantity_dialog_box.getText().toString());
				calculateResults(quantity_dialog_box.getText().toString(),pos,valueView);
				text.setEnabled(false);
				text.setText(quantity_dialog_box.getText().toString().trim());
				dialog.dismiss();
				
				}
				
				
			}
		});
		
		
		close_dialog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				text.setEnabled(false);
				dialog.dismiss();
				
			}
		});
		
		
		
		dialog.show();
		
		
		
		
	}





}

