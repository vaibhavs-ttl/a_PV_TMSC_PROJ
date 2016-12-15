package com.ttl.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ttl.customersocialapp.R;
import com.ttl.webservice.Config;

public class soapConnection {
	private String _theUrl;
	public byte bt[] = null;
	private Integer STATUS = -1;
	private Context context;
	private EncryptDecrypt ed;
	
	public soapConnection(String url, byte[] req, Context ctx) {
		/*
		 * Use this block if encrypted data is not to be used 
		 * */
//		_theUrl = url;
//		this.bt = req;
		
		context = ctx;
		
		/*
		 * Use this block if encrypted data is to be used 
		 * */
		String s = new String(req);
		String _reqEncrypt = "";
		_theUrl = ctx.getResources().getString(R.string.URL);
		ed = new EncryptDecrypt();
		
		if(s.contains("GetWebAuthByKey")){
			// use this string for getting SAML artifact
			_reqEncrypt = s + "[#]" +ctx.getResources().getString(R.string.URL1);
		}else{
			Log.e("samlart key: ", Config.getSAMLARTIFACT());
			// use this sting if SAML art is present
			_reqEncrypt = s + "[#]" +ctx.getResources().getString(R.string.URL1)+"[#]" + Config.getSAMLARTIFACT();
		
		
		}
		
		//_req will contain the _reqEncrypt in encoded format
				//a new request is created using the encoded string and 
				//a secure connection(using https link) is implemented
		String _req = "";
		try {
	_req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><bpmDecryptNExecuteWebservice xmlns=\"http://schemas.cordys.com/default\"><Request>"+_reqEncrypt+
		ed.encrypt(_reqEncrypt,ctx.getResources().getString(R.string.key),ctx.getResources().getString(R.string.iv).getBytes("UTF-8"))+			
					"</Request></bpmDecryptNExecuteWebservice></SOAP:Body></SOAP:Envelope>";
		
			
			
			
			Log.v("inside soap connection", _req);
		
		
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.d("exception", e1.toString());
		}
		
		try {
			//Log.d("reqencrypt", _req);
			this.bt = _req.getBytes("UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("error in connection", String.valueOf(e));
			e.printStackTrace();
		}
	}

	//this function performs a network connection over a secure https link
	public String getResponce() {
		String response = "";
		HttpURLConnection connection = null;
		InputStream inStr = null;

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			// SSL Verification
			try {
				//Read the SSL certificate 
				//Verify the certificate and throw aexception if the verification fails
				/*SSLContext sslctx = null;
				try {
					KeyStore trusted = KeyStore.getInstance("BKS");
					//QA Cretificate
					//InputStream in = context.getResources().openRawResource(R.raw.qa_store);
					//Prod Cretificate
					InputStream in = context.getResources().openRawResource(R.raw.store);
					char[] pwd = context.getResources().getString(R.string.keypwd).toCharArray();
					try {
						trusted.load(in, pwd);
					} finally {
						in.close();
					}
					//TrustManager For QA
					TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
					tmf.init(trusted);
////					//TrustManager For PROD
					TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
					tmf.init((KeyStore)null);
					
					sslctx = SSLContext.getInstance("SSL");
					sslctx.init(null, tmf.getTrustManagers(), null);
				} catch (Exception e) {
					StringBuilder sb = new StringBuilder();
					sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
					sb.append("SSL Certificate Error.");
					sb.append("</err></SOAP:Body></SOAP:Envelope>");
					return sb.toString();
				}

				// Create an HostnameVerifier that validates the expected hostname.
				HostnameVerifier verifier = new HostnameVerifier() {
				    @Override
				    public boolean verify(String hostname, SSLSession session) {
				        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
				        //PROD
				       boolean result = hv.verify("tmcrmapps.inservices.tatamotors.com", session); 
				        //QA
				     //   boolean result = hv.verify("tmcrmappsqa.inservices.tatamotors.com", session);
				        return result;
				    }

				};
				
				//Sets the default SSL socket factory to be used by new instances.
				HttpsURLConnection.setDefaultSSLSocketFactory(sslctx
						.getSocketFactory());
*/
		       /* QA*/
			//	SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context,"tmcr_certificate.cer");
		//		SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context,"production_ssl.cer");
				/*Prod*/
				//SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context,"tmcr_certificate.cer");
				//if verification is OK perform network connection
				URL url = new URL(_theUrl);
				connection = (HttpURLConnection) url.openConnection();
			//	((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
				
				if (connection != null) {
					//connection.setHostnameVerifier(verifier);
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setUseCaches(false);
					connection.setConnectTimeout(60000);
					connection.setRequestProperty("Content-Type", "text/xml");
					connection.setRequestProperty("charset", "utf-8");
					connection.setRequestProperty("Content-Length", ""
							+ bt.length);

					OutputStream opStream = null;
					opStream = connection.getOutputStream();
					opStream.write(bt);
					opStream.flush();
					opStream.close();
					int inChar = 0;
					StringBuffer inBuffer = null;

					STATUS = connection.getResponseCode();
					if (STATUS == HttpURLConnection.HTTP_OK) {
						inStr = connection.getInputStream();
						if (inStr != null) {
							inBuffer = new StringBuffer();
							while ((inChar = inStr.read()) != -1) {
								inBuffer.append((char) inChar);
							}
						}

						if (inBuffer != null) {
							response = inBuffer.toString();
							//Log.d("Response", response);
						} else {
							StringBuilder sb = new StringBuilder();
							sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
							sb.append("No Data.");
							sb.append("</err></SOAP:Body></SOAP:Envelope>");
							return sb.toString();
						}
						/********************************ENCRYPTED DATA**********************************************/
						//Received response in an encrypted format
						//parse the response and readthe contents of "Message" tag
						//decrypt the contents of the parded tag
						String xml1 = response;
						try {
							XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
							factory.setNamespaceAware(true);
							XmlPullParser xmlParser = factory.newPullParser();
							xmlParser.setInput(new StringReader(xml1));
							int eventType = xmlParser.getEventType();

							while (eventType != XmlPullParser.END_DOCUMENT) {
								
								switch (eventType) {
								case XmlPullParser.START_TAG:
									String tagName = xmlParser.getName();
									if (tagName.equalsIgnoreCase("Message")) { 
										response = xmlParser.nextText().toString();								
									}						
									break;
								}

								eventType = xmlParser.next();
							}
							//decrypt data
							try {
								response = ed.decrypt(response,context.getResources().getString(R.string.key),context.getResources().getString(R.string.iv).getBytes("UTF-8"));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								return "<err>The connection has timed out. The server is taking too long to respond.</err>";
							}
							//convert decrypted data in a proper XML format for further processing
							response = response.replaceAll("&gt;", ">");
							response = response.replaceAll("&lg;", "<");
							if(response.contains("javax.crypto")){
								return  "<err>The connection has timed out. The server is taking too long to respond.</err>";
							}
							if(!response.contains("SOAP:Envelope")){
								return "<err>"+response+"</err>";
							}
						} catch (Exception e) {
							return "<err>No Data.</err>";
						}
						/********************************DECRYPTED DATA**********************************************/
						inStr.close();
					} else {
						inStr = connection.getErrorStream();
						if (inStr != null) {
							inBuffer = new StringBuffer();
							while ((inChar = inStr.read()) != -1) {
								inBuffer.append((char) inChar);
							}
						}
						boolean tag = false;
						if (inBuffer != null) {
							response = inBuffer.toString();
						} else {
							StringBuilder sb = new StringBuilder();
							sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
							sb.append("No Data");
							sb.append("</err></SOAP:Body></SOAP:Envelope>");
							return sb.toString();
						}
						/********************************ENCRYPTED DATA**********************************************/
						//Received error response in an encrypted format
						//parse the response and readthe contents of "Message" tag
						//decrypt the contents of the parded tag
						String xml1 = response;
						try {
							XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
							factory.setNamespaceAware(true);
							XmlPullParser xmlParser = factory.newPullParser();
							xmlParser.setInput(new StringReader(xml1));
							int eventType = xmlParser.getEventType();

							while (eventType != XmlPullParser.END_DOCUMENT) {
								
								switch (eventType) {
								case XmlPullParser.START_TAG:
									String tagName = xmlParser.getName();
									if (tagName.equalsIgnoreCase("Message")) { 
										response = xmlParser.nextText().toString();								
									}						
									break;
								}

								eventType = xmlParser.next();
							}
							//decrypt data
							try {
								response = ed.decrypt(response,context.getResources().getString(R.string.key),context.getResources().getString(R.string.iv).getBytes("UTF-8"));
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								return "<err>The connection has timed out. The server is taking too long to respond.</err>";
							}
							//convert decrypted data in a proper XML format for further processing
							response = response.replaceAll("&gt;", ">");
							response = response.replaceAll("&lg;", "<");
							if(response.contains("javax.crypto")){
								return  "<err>The connection has timed out. The server is taking too long to respond.</err>";
							}
							if(!response.contains("SOAP:Envelope")){
								return "<err>"+response+"</err>";
							}
						} catch (Exception e) {
							return "<err>No Data.</err>";
						}
						/********************************ENCRYPTED DATA**********************************************/
						inStr.close();

						//parse the error response to give some user friendly error messages
						String xml = response;
						try {
							XmlPullParserFactory factory = XmlPullParserFactory
									.newInstance();
							factory.setNamespaceAware(true);
							XmlPullParser xmlParser = factory.newPullParser();
							xmlParser.setInput(new StringReader(xml));
							int eventType = xmlParser.getEventType();

							while (eventType != XmlPullParser.END_DOCUMENT) {

								switch (eventType) {
								case XmlPullParser.START_TAG:
									String tagName = xmlParser.getName();
									if (tagName.equalsIgnoreCase("faultstring")) {
										String r = xmlParser.nextText();
										
										r=r.replaceAll(">=", "GREATER_THAN_OR_EQUAL_TO");
										r=r.replaceAll("<=", "LESS_THAN_OR_EQUAL_TO");
										r=r.replaceAll(">", "GREATER_THAN");
										r=r.replaceAll("<", "LESS_THAN");
										
										tag = true;
										if (r.contains("Unable to bind an artifact")) {
											StringBuilder sb = new StringBuilder();
											sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
											sb.append("Your login session has expired. Please login once again.");
											sb.append("</err></SOAP:Body></SOAP:Envelope>");
											
											Config.setSAMLARTIFACT("");
											return sb.toString();
										} else {
											StringBuilder sb = new StringBuilder();
											sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
											sb.append(r);
											sb.append("</err></SOAP:Body></SOAP:Envelope>");
											return sb.toString();
										}
										
									}
									break;
								}

								eventType = xmlParser.next();
							}

							if (!tag) {
								StringBuilder sb = new StringBuilder();
								sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
								sb.append("An error occured while communicating.\nERROR CODE : ");
								sb.append(STATUS.toString());
								sb.append("</err></SOAP:Body></SOAP:Envelope>");
								return sb.toString();
							}

						} catch (Exception e) {
							Log.v(this.getClass().getSimpleName(), e.toString());
							StringBuilder sb = new StringBuilder();
							sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
							sb.append("An error occured while communicating.");
							sb.append("</err></SOAP:Body></SOAP:Envelope>");
							return sb.toString();
						}

					}
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
					sb.append("Unable to Connect. Please check your network connection.");
					sb.append("</err></SOAP:Body></SOAP:Envelope>");
					return sb.toString();
				}

			} catch (IOException e) {
				Log.v(this.getClass().getSimpleName(), e.toString());
				try {
					if (inStr != null) {
						inStr.close();
					}
					if (connection != null) {
					}
				} catch (IOException io) {

				}
				StringBuilder sb = new StringBuilder();
				sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
				sb.append("An error occured while communicating.");
				sb.append("</err></SOAP:Body></SOAP:Envelope>");
				return sb.toString();

			}
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body><err>");
			sb.append("No network connection available.");
			sb.append("</err></SOAP:Body></SOAP:Envelope>");
			return sb.toString();
		}

		return response;
	}
}
