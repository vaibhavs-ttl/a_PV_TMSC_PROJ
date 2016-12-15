package com.ttl.customersocialapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.ttl.adapter.LabourAdapter;
import com.ttl.communication.GlobalAccessObject;
import com.ttl.model.LabourModel;

public class SelectLabourActivity extends Activity implements OnClickListener,OnQueryTextListener{
	private Button labour_parts_done_btn;
	private Button labour_parts_cancel_btn;
	private ArrayList<LabourModel> labour_list=new ArrayList<>();
	private ListView labour_data_list;
	private LabourAdapter adapter;
	private SearchView searchView;
	
	public static TextView empty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_labour_parts);
	
	
	
		getReferences();
		setHandlers();
		getData();
	
	
	
	}

	
	private void getData()
	{
	
	 Bundle bundle=getIntent().getExtras();
     labour_list=(ArrayList<LabourModel>)bundle.getSerializable("labour_list");
	 adapter=new LabourAdapter(SelectLabourActivity.this, labour_list,empty,true);
	 labour_data_list.setAdapter(adapter);
	 labour_data_list.setTextFilterEnabled(true);
	 labour_data_list.setEmptyView(empty);
	 adapter.notifyDataSetChanged();
	
	}
	
	
	

	
	private void setHandlers()
	{
		
		
		labour_parts_done_btn.setOnClickListener(this);
		labour_parts_cancel_btn.setOnClickListener(this);
		searchView.setOnQueryTextListener(this);
	
	}

	
	
	
	private void getReferences()
	{
		
		labour_parts_cancel_btn=(Button)findViewById(R.id.labour_parts_cancel_btn);
		labour_parts_done_btn=(Button)findViewById(R.id.labour_parts_done_btn);
		labour_data_list=(ListView)findViewById(R.id.labour_list);
		searchView=(SearchView)findViewById(R.id.labour_part_search_bar);
		empty=(TextView)findViewById(R.id.empty_view);
	}
	
	@Override
	public void onClick(View v) {

		
		if (v.getId()==R.id.labour_parts_done_btn) {
		
		
		if (GlobalAccessObject.getLabour_obj()!=null) {
				
			
			/*	Intent intent=new Intent();
				intent.putExtra("labour_list_data", GlobalAccessObject.getLabour_obj());
				setResult(Activity.RESULT_OK, intent);
				finish();
				
			}
			else
			{
				setResult(Activity.RESULT_CANCELED);
				finish();	
			}
			
*/			

				Intent intent=new Intent();
			
			if (GlobalAccessObject.isChanged) {
				LabourAdapter.selected_data.clear();
				intent.putExtra("labour_list_data", GlobalAccessObject.getLabour_obj());	
				Log.v("is changed", ""+GlobalAccessObject.isChanged);
				GlobalAccessObject.isChanged=false;
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
			else
			{
			
				
				
				
			setResult(Activity.RESULT_CANCELED);
			
			finish();
			}
		}
			
		}
		else if(v.getId()==R.id.labour_parts_cancel_btn)
		{
			
			ArrayList<LabourModel>temp_list=LabourAdapter.selected_data;
			ArrayList<LabourModel> oriiginal_list=GlobalAccessObject.getLabour_obj();
			
			
			
			
				if (temp_list!=null) {
					
				
			for (int i = 0; i < temp_list.size(); i++) {
				
			
				
				Log.v("temp value", temp_list.get(i).getLabourDescription());
				
				
				for (int j = 0; j < oriiginal_list.size(); j++) {
					
				
				if (temp_list.get(i).getLabourDescription().equals(oriiginal_list.get(j).getLabourDescription())) {
				
					
					GlobalAccessObject.removeLabour_obj(temp_list.get(i));
					//GlobalAccessObject.removeLabour_obj(i,temp_list.get(i));
					Log.v("inside inner if", temp_list.get(i).getLabourDescription());
					
				}
				
					
					
				
					
				}
				
				
				
			}
			
				}
			
			
			LabourAdapter.selected_data.clear();
		//	GlobalAccessObject.NullifyLabour_obj();
			setResult(Activity.RESULT_CANCELED);
			finish();	
			
		}
		

	
	}

	
	
	@Override
	public void onBackPressed() {

		setResult(Activity.RESULT_CANCELED);
		finish();		
	}


	@Override
	public boolean onQueryTextChange(String newText) {

	
		
		
		adapter.getFilter().filter(newText.toString().trim());
	//	labour_data_list.invalidate();
	//	adapter.notifyDataSetChanged();
		
		return false;
	}


	@Override
	public boolean onQueryTextSubmit(String query) {
		
		adapter.getFilter().filter(query.toString().trim());
		labour_data_list.invalidate();
		adapter.notifyDataSetChanged();
		
		return true;
	}
	
	
	
	
	
	
	
}
