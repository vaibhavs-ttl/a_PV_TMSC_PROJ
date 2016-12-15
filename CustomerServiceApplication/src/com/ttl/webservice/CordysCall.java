package com.ttl.webservice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.util.Log;

public class CordysCall {

	
	String url;
	String envelope;
	Context context;
	String samlart;
	String response=null;
	
	
	public String getResponse(String url,String samlart,String envelope)
	{
		
		try {
			
			
			//SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context,"production_ssl.cer");
		        

		        URL url1 = new URL(url+"&SAMLart="+samlart);
		        javax.net.ssl.HttpsURLConnection connection = (javax.net.ssl.HttpsURLConnection) url1.openConnection();
		      //  connection.setSSLSocketFactory(sslContext.getSocketFactory());
		        byte bt[] = null;
		        bt = envelope.getBytes("UTF-8");
		        connection = (javax.net.ssl.HttpsURLConnection) url1.openConnection();
		        if (connection != null) {

		        	connection.setRequestMethod("POST");
		          connection.setDoOutput(true);
		          connection.setDoInput(true);
		          connection.setUseCaches(false);
		          connection.setConnectTimeout(500000);
		          connection.setRequestProperty("Content-Type", "text/xml");
		          
		          connection.setRequestProperty("charset", "UTF-8");
		          connection.setRequestProperty("Content-Length", "" + bt.length);

		          OutputStream opStream = null;
		          opStream = connection.getOutputStream();
		          opStream.write(bt);
		         
		          opStream.flush();
		          opStream.close();
		          int inChar = 0;
		         StringBuffer inBuffer = null;
		         InputStream inStr; 
		         
		         
		        
		         
		         int STATUS = connection.getResponseCode();
		         Log.v("test soap response code", ""+STATUS);
		          if (STATUS == HttpsURLConnection.HTTP_OK) {
		            inStr = null;
		            inStr = connection.getInputStream();
		        
		            BufferedReader reader=new BufferedReader(new InputStreamReader(inStr,"UTF-8"));
		            
		            if (inStr != null) {
		              inBuffer = new StringBuffer();
		             /* while ((inChar = inStr.read()) != -1) {
		                inBuffer.append((char) inChar);
		              }*/
		              String line;
		              while((line=reader.readLine())!=null)
		              {
		            	  
		            	  inBuffer.append(line);
		            	  
		              }
		            	  
		            
		            }

		            if (inBuffer != null) {
		              

		               response = inBuffer.toString();
		             Log.v("test soap result",  response); 
		             return response;
		            }
		          }
			
			
			
		        }
		        else
		        {
		        	
		        	Log.v("Test soap", "null found");
		        	
		        }
			
		        
		        
		
		
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		return response;
	}
	
	
	
}
