package com.ttl.customersocialapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VersionUpdateActivity extends Activity {

	TextView versionText, updatelater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version_update);
		versionText = (TextView) findViewById(R.id.version);
		Intent intent = getIntent();
		String version = intent.getStringExtra("version");
		versionText.setText("Update to version "+version);
		Button updatenow = (Button) findViewById(R.id.btnupdate);
		updatelater = (TextView) findViewById(R.id.updatelater);
		
		updatenow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  String url = "https://play.google.com/store/apps/details?id=com.ttl.customersocialapp&hl=en";
	              Intent i = new Intent(Intent.ACTION_VIEW);
	              i.setData(Uri.parse(url));
	              startActivity(i);
			}
		});
		
		updatelater.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
	}

	

}
