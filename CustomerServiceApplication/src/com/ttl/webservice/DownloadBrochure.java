package com.ttl.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.ttl.customersocialapp.R;

public class DownloadBrochure extends AsyncTask<Integer, Integer, Integer>{

	
	private Context context;
	private NotificationManager manager;
	private android.support.v4.app.NotificationCompat.Builder builder;
	private String url;
	private String filename;
	private String MIME_TYPE="application/pdf";
	public DownloadBrochure(Context context,String url,String filename)
	{
		
		this.context=context;
		this.url=url;
		this.filename=filename;
		manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		builder=new NotificationCompat.Builder(context);
	
	
	}
	
	
	@Override
	protected void onPreExecute() {
	
		super.onPreExecute();
		Toast.makeText(context, "Download will start automatically.",Toast.LENGTH_LONG).show();
	builder.setContentTitle("Downloading..");
	builder.setContentText("Downloading "+filename);	
	builder.setSmallIcon(R.drawable.applogo);
	
	builder.setProgress(0, 0, true);
	
	
	
	
	}
	
	
	
	@Override
	protected Integer doInBackground(Integer... params) {
	
		
	try {
		
		
		URL link=new URL(url);
		
		HttpURLConnection connection=(HttpURLConnection)link.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoInput(true);
		connection.connect();

		
/*		
		BufferedInputStream bis=new BufferedInputStream(connection.getInputStream());
		
		
		FileWriter fileWriter=new FileWriter(new File(Environment.getExternalStorageDirectory()+File.separator,filename));
		
		BufferedWriter writer=new BufferedWriter(fileWriter);
		
		
		
		int length=connection.getContentLength();

		long total=0;
		int value=0;
		char data[] = new char[1024];
		while((value=bis.read())!=-1)
		{
			total+=value;
		
			
		
			publishProgress((int)(total*100)/length);
			
			writer.write(data, 0, value);
			
			Thread.sleep(10);
		}
		
		
		bis.close();
		writer.close();
		
		
		*/
		
		
		//BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		
		
		File file=new File(Environment.getExternalStorageDirectory()+File.separator,filename);
		FileOutputStream fos=new FileOutputStream(file);
		PdfReader reader2=new PdfReader(connection.getInputStream());
		PdfStamper stamper=new PdfStamper(reader2, fos);
		
		int total = reader2.getNumberOfPages() + 1;
		for ( int i=1; i<total; i++) {
		
			reader2.setPageContent(i + 1, reader2.getPageContent(i + 1));
		}
		stamper.setFullCompression();
		stamper.close();
		
		
		
		Intent open_pdf=new Intent();
		open_pdf.setAction(Intent.ACTION_VIEW);
		open_pdf.setDataAndType(Uri.fromFile(file), MIME_TYPE);
		
		
		
		
		
		
		
		
		
		
/*		int length=connection.getContentLength();
		Log.v("size: ",""+length);
		long total=0;
		int value=0;
		String data;
		StringBuffer buffer=new StringBuffer(length);
		while((data=reader.readLine())!=null)
		{
			total+=value;
		
			buffer.append(data);
		
			publishProgress((int)(total*100)/length);
			
	//		writer.write(data);
			value++;
			
			
			Thread.sleep(10);
		}
		
		Log.v("after loop: ",""+total);
	//	writer.write(buffer.toString().getBytes("UTF-8"));
		
		fos.write(buffer.toString().getBytes("UTF-8"));
		reader.close();
		fos.close();

		
*/		
		
		
		
		
		
		
	} catch (Exception e) {
	e.printStackTrace();
	}
		
		
		return null;
	}
	
	
	
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);

	
	builder.setProgress(100, values[0], false);
	manager.notify(0, builder.build());
	
		
	
		
	}
	
	
	@Override
	protected void onPostExecute(Integer result) {
	super.onPostExecute(result);
	builder.setContentTitle("Completed");	
	builder.setContentText("Download complete");
  		
  		builder.setProgress(0,0,true);
  		
      		manager.notify(0, builder.build());
	
	
	
	
	
	}
	
	
	
	
	
	
	
}
