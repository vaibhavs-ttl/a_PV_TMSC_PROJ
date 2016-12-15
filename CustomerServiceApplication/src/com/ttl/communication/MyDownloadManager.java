package com.ttl.communication;

import java.io.File;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.ttl.customersocialapp.R;
import com.ttl.model.DownloadModel;
import com.ttl.webservice.Constants;

public class MyDownloadManager {

	
	
	//private Context context;
	private DownloadManager downloadManager;
	private String url;
	private String filename;
	
	
	public MyDownloadManager(Context context,String url,String filename)
	{
		
		
		downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
		this.filename=filename;
		this.url=url;
		Toast.makeText(context, context.getString(R.string.brochur_download_info),Toast.LENGTH_LONG).show();
		
	}
	
	public void downloadFile(String file)
	{
	
	DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
	request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
	request.setTitle(filename);
	request.setVisibleInDownloadsUi(true);
	request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	//request.setDestinationInExternalPublicDir("/Download", filename);
	
	
	deleteIfExists(Constants.BROCHURE_PATH+File.separator+file);
	
	if (Build.VERSION.SDK_INT==21) {
		File location=new File(Constants.BROCHURE_PATH+File.separator+file);
		
	//	request.setDestinationUri(Uri.parse("file:///"+Constants.BROCHURE_PATH+File.separator+file));
	
		
		Log.v("inside 21 api", "21 api");
		request.setDestinationUri(Uri.fromFile(location));
		
	
	}
	else
	{
	request.setDestinationUri(Uri.parse("file:///"+Constants.BROCHURE_PATH+File.separator+file));
	}
	long download_id=downloadManager.enqueue(request);
		
		
		
	
	DownloadModel model=new DownloadModel();
	model.setDownloadID(download_id);
	//model.setPosition(position);
//	model.setObject(object);
	GlobalAccessObject.addFile(model);
	
	
	
	
	}
	
	
	
	private void deleteIfExists(String check)
	{
		
		
		File file=new File(check);
		
		if (file.exists()) {
			
			file.delete();
			Log.v("file deleted", "deleted");
			
		}
		
		
		
		
	}
	
	
	
}
