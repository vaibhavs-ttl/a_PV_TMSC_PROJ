package com.ttl.communication;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class AppUninstallListener extends BroadcastReceiver{

	private String folder_path=Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/CustomerSocialAppDocument/";
	
	@Override
	public void onReceive(Context context, Intent intent) {

		try {
	
			
			Log.e("intent called", intent.getAction());
			
		
			File removeFolder=new File(folder_path);
		
			Log.e("AppUninstallListener", "reached");
			
			deleteDirectory(removeFolder);
	//		Log.v("Package removed", "success");	
		/*	if(removeFolder.delete())
			{
			
					
		
				
				
				
				Toast.makeText(context, "Folder deleted", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(context, "Folder could not be created", Toast.LENGTH_LONG).show();
			}
		*/	
		} catch (Exception e) {
		e.printStackTrace();
		}
		
	
		
		
		
		
		
	}

	
	
	private void deleteDirectory(File file)
	{
		
		
		  if (file.isDirectory()) {
		        for (File sub : file.listFiles()) {
		        	deleteDirectory(sub);
		        }
		    }
		  file.delete();
		
		
	}
	
	
	
	
	
	
}

