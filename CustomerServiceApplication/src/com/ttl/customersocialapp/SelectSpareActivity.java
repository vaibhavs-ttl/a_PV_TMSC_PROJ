package com.ttl.customersocialapp;

import java.util.ArrayList;

import com.ttl.adapter.SpareAdapter;
import com.ttl.communication.GlobalAccessObject;
import com.ttl.model.SpareModel;

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

public class SelectSpareActivity extends Activity implements OnClickListener,OnQueryTextListener{

	
	private Bundle bundle;
	private Button spare_parts_done_btn;
	private Button spare_parts_cancel_btn;
	private TextView show_empty_text;
	private ArrayList<SpareModel> spare_list=new ArrayList<>();
	private ListView spare_data_list;
	private SpareAdapter adapter;
	
	private SearchView searchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_spare_parts);
	
	
		
	
		getReferences(); // To get references ex: calling findViewById()
		setHandlers(); // To set listeners
		getData(); // To get fetched data.
	
	
	
	}

	
	
	
	
	
	private void getData()
	{
		
		bundle=getIntent().getExtras();
		
		
		spare_list=(ArrayList<SpareModel>)bundle.getSerializable("spare_list");
		
		 adapter=new SpareAdapter(SelectSpareActivity.this, spare_list,show_empty_text);
		
		spare_data_list.setAdapter(adapter);
		spare_data_list.setEmptyView(show_empty_text);
		
		spare_data_list.setTextFilterEnabled(true);
		adapter.notifyDataSetChanged();
	}
	
	
	
	
	private void setHandlers()
	{
		
		
		spare_parts_done_btn.setOnClickListener(this);
		spare_parts_cancel_btn.setOnClickListener(this);
		searchView.setOnQueryTextListener(this);
	
		
		
	}



	private void getReferences()
	{
		
		spare_parts_cancel_btn=(Button)findViewById(R.id.spare_parts_cancel_btn);
		spare_parts_done_btn=(Button)findViewById(R.id.spare_parts_done_btn);
		spare_data_list=(ListView)findViewById(R.id.spare_list);
		searchView=(SearchView)findViewById(R.id.spare_part_search_bar);
		show_empty_text=(TextView)findViewById(R.id.spare_empty_view);
	
	}


	@Override
	public void onClick(View v) {
		
		if (v.getId()==R.id.spare_parts_done_btn) {
		
			
			if (GlobalAccessObject.getSpare_obj()!=null) {
			
				
				Intent intent=new Intent();
				
				if (GlobalAccessObject.spareChanged) {
					SpareAdapter.selected_data.clear();
					intent.putExtra("spare_list_data", GlobalAccessObject.getSpare_obj());	
					GlobalAccessObject.spareChanged=false;
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
		else if(v.getId()==R.id.spare_parts_cancel_btn)
		{
			
		
		
			
			ArrayList<SpareModel>temp_list=SpareAdapter.selected_data;
			ArrayList<SpareModel> oriiginal_list=GlobalAccessObject.getSpare_obj();
			
			
			
			
				if (temp_list!=null) {
					
				
			for (int i = 0; i < temp_list.size(); i++) {
				
			
				
				Log.v("temp value", temp_list.get(i).getPartDescription());
				
				
				for (int j = 0; j < oriiginal_list.size(); j++) {
					
				
				if (temp_list.get(i).getPartDescription().equals(oriiginal_list.get(j).getPartDescription())) {
				
					
					GlobalAccessObject.removeSpare_obj(temp_list.get(i));
					
					Log.v("inside inner if", temp_list.get(i).getPartDescription());
					
				}
				
					
					
				
					
				}
				
				
				
			}
			
				}
			
			
				SpareAdapter.selected_data.clear();
			
			setResult(Activity.RESULT_CANCELED);
			finish();	
			
			
			
		}
		
		
	}

	@Override
	public boolean onQueryTextChange(String text) {
		Log.v("entering", text);
		
		adapter.getFilter().filter(text.toString().trim());
		spare_data_list.invalidate();
		adapter.notifyDataSetChanged();
		return true;
	}






	@Override
	public boolean onQueryTextSubmit(String query) {
		
		return false;
	}

}
