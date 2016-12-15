package com.ttl.communication;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DirectoryInitializer extends BroadcastReceiver{
	private File[] dirs;
	private File baseURL;
	private String[] dirsName;
	
	@Override
	public void onReceive(Context context, Intent intent) {


		
		Log.v("my broadcast", "Reciever called");
			
	//	context.startService(new Intent(context, DirectoryService.class));
		
		/*try {
			
			baseURL=new File(Environment
		            .getExternalStorageDirectory().getAbsolutePath()
		            + "/CustomerSocialAppDocument/");
		
			
			dirs=baseURL.listFiles();
			
			for(File name:dirs)
			{
				
				
				if(name.isDirectory())
				{
				Log.v("dir name",name.getName());
				}
				
			}
			
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}*/	
		
		
		
		
	}
	
}
