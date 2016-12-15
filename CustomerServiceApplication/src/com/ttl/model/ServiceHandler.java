package com.ttl.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;
 
public class ServiceHandler {
 
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
 
    public ServiceHandler() {
 
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,
            List<NameValuePair> params) {
        try {
            // http client
        	HttpParams httpParameters = new BasicHttpParams();
        	int timeoutConnection = 60000;
        	int timeoutSocket = 60000;

        			DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpEntity httpEntity = null;
                    HttpResponse httpResponse = null;
                     
                    // Checking http request method type
                    if (method == POST) {
                        HttpPost httpPost = new HttpPost(url);
        				HttpConnectionParams.setConnectionTimeout(httpParameters,
        						timeoutConnection);
        				HttpConnectionParams
        						.setSoTimeout(httpParameters, timeoutSocket);

                        // adding post params
                        if (params != null) {
                            httpPost.setEntity(new UrlEncodedFormEntity(params));
                        }
                        httpResponse = httpClient.execute(httpPost);
                   /*     Log.d("Request" ,url );

                        Log.d("httpResponse" ,httpResponse.toString() );*/
                    } else if (method == GET) {
                        // appending params to url
                    	HttpConnectionParams.setConnectionTimeout(httpParameters,
        						timeoutConnection);
        				HttpConnectionParams
        						.setSoTimeout(httpParameters, timeoutSocket);
                        if (params != null) {
                            String paramString = URLEncodedUtils
                                    .format(params, "utf-8");
                            url += "?" + paramString;
                        }
                        HttpGet httpGet = new HttpGet(url);
                      //  Log.d("Request" ,url );
                        httpResponse = httpClient.execute(httpGet);
         
                    }
                    httpEntity = httpResponse.getEntity();
                    if(httpEntity!=null)
                    	response = EntityUtils.toString(httpEntity);
            Log.d("response" ,response);
        } catch (UnsupportedEncodingException e) {
           // e.printStackTrace();
        } catch (ClientProtocolException e) {
          //  e.printStackTrace();
        } catch (ConnectTimeoutException e) {
			// TODO: handle exception
        	 //e.printStackTrace();
        	 return "Connection time out";
		}
        
        catch (UnknownHostException e) {
        	
        	return "Could not connect to server.Please check your network connection.";
        	
		}
        catch (IOException e) {
           // e.printStackTrace();
        }
         
        return response;
 
    }
}
