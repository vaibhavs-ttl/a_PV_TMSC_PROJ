package com.ttl.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ttl.customersocialapp.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.ttl.customersocialapp.BookServiceFragment;
import com.ttl.customersocialapp.DelearLocator_fragment;
import com.ttl.model.DelearLocatorData;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;
public class DealerListAdapter extends ArrayAdapter<DelearLocatorData>
		implements LocationListener {
	protected static final Bundle savedInstanceState = null;
	private ArrayList<DelearLocatorData> object;
	private Context context;
	
	
	

	public DealerListAdapter(Context context, int textViewResourceId,
			ArrayList<DelearLocatorData> object) {
		super(context, textViewResourceId, object);
		this.object = object;
		this.context = context;
		System.out.println(object.size());
	}

	

	@Override
	public int getCount() {
	
		return object.size();
	}

	@Override
	public DelearLocatorData getItem(int arg0) {
	
		return object.get(arg0);
	}

	public void setAllItems(ArrayList<DelearLocatorData> paramArrayList) {
		this.object.addAll(paramArrayList);
	}

	@Override
	public int getViewTypeCount() {
	
		return 1;
	}

	public static class ViewHolder {

		TextView txtdelearname, txtaddress, txtphonenumber, txtshowonmap,
				txtdirection, txtdelercount, txtbookservices, txtemail;
		RadioButton rdbtn;
		ListView phnumlist,emailList;
		ImageView mail;
	}

	ViewHolder holder = null;
	int selectedposition = -1;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.delear_locator_list, null);
		holder = new ViewHolder();
		holder.txtdelearname = (TextView) v.findViewById(R.id.txtdelearname);
		holder.txtaddress = (TextView) v.findViewById(R.id.txtdelearaddress);
	
		holder.txtemail = (TextView) v.findViewById(R.id.txtemail);
		holder.txtshowonmap = (TextView) v.findViewById(R.id.txtshowonmap);
		holder.txtdirection = (TextView) v.findViewById(R.id.txtdirection);
		holder.txtdelercount = (TextView) v.findViewById(R.id.txtdelercount);
		holder.txtbookservices = (TextView) v
				.findViewById(R.id.txtbookservices);
		holder.rdbtn = (RadioButton) v.findViewById(R.id.rdbtn);
		holder.phnumlist = (ListView) v.findViewById(R.id.delearphonenumberlist);
		holder.emailList = (ListView) v.findViewById(R.id.delearemaillist);
		
		holder.mail = (ImageView) v.findViewById(R.id.email);
		if (object.size() <= 0) {

		} else {
			holder.txtdelercount.setText((position + 1) + "");
			Log.d("position object", position + "");

			holder.txtdelearname
					.setText(object.get(position).delear_DIV_COMMON_NAME);
			holder.txtaddress.setText(object.get(position).delear_DIV_ADDRESS_1
					+ object.get(position).delear_DIV_ADDRESS_2 + " "
					+ object.get(position).delear_DIV_CITY + " "
					+ object.get(position).delear_DIV_STATE + "-"
					+ object.get(position).delear_DIV_ZIP_CODE);

	
			String phnnumber = object.get(position).delear_DIV_PHONE;
			ArrayList<String> dealerphnnolist = new ArrayList<String>();
			if(phnnumber.contains("/"))
			{
				String[] phnarray = phnnumber.split("/");
				Log.d("Phone number	list size", phnarray.length+"");
				for(int i= 0; i<phnarray.length; i++)
				{
					dealerphnnolist.add(phnarray[i]);
					Log.d("Phone number	", phnarray[i]);
				}
			}else
			{
				String[] phnarray = phnnumber.split(",");
				Log.d("Phone number	list size", phnarray.length+"");
				for(int i= 0; i<phnarray.length; i++)
				{
					dealerphnnolist.add(phnarray[i]);
					Log.d("Phone number	", phnarray[i]);
				}
			}
			
			// Email 
			
			String email_text = object.get(position).delear_DIV_EMAIL;
			final ArrayList<String> dealeremaillist = new ArrayList<String>();
			if(email_text.contains("/"))
			{
				String[] emailarray = email_text.split("/");
				//Log.d("Phone number	list size", emailarray.length+"");
				for(int i= 0; i<emailarray.length; i++)
				{
					dealeremaillist.add(emailarray[i]);
					Log.v("email address", emailarray[i]);
				}
			}else
			{
				String[] emailarray = email_text.split(",");
				//Log.d("Phone number	list size", emailarray.length+"");
				for(int i= 0; i<emailarray.length; i++)
				{
					dealeremaillist.add(emailarray[i]);
					Log.v("email address", emailarray[i]);
				}
			}
			
			
			DealerPhnnumAdapter phno = new DealerPhnnumAdapter(context, dealerphnnolist);
	
			holder.phnumlist.setAdapter(phno);
			
			
			
			
			
			
			DealerEmailAdapter emailAdapter=new DealerEmailAdapter(context, dealeremaillist);
			holder.emailList.setAdapter(emailAdapter);
			
			
			
/*			if(object.get(position).delear_DIV_EMAIL.equals(""))
			{
				holder.txtemail.setVisibility(View.GONE);
				holder.mail.setVisibility(View.GONE);
			}else
			{
				holder.txtemail.setVisibility(View.VISIBLE);
				holder.mail.setVisibility(View.VISIBLE);
			}
			holder.txtemail.setText(object.get(position).delear_DIV_EMAIL);
			
			holder.txtemail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
	

					
					String mailid = object.get(position).delear_DIV_EMAIL;
					
					String URI = "mailto:?subject=" + "" + "&body=" + "";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri data = Uri.parse(URI);
					intent.setData(data);
					intent.putExtra(Intent.EXTRA_EMAIL, mailid);

					intent.putExtra(Intent.EXTRA_CC, "");
					context.startActivity(intent);
				}
			});
			holder.mail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
	

					String mailid[] = { object.get(position).delear_DIV_EMAIL };

					String URI = "mailto:?subject=" + "" + "&body=" + "";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri data = Uri.parse(URI);
					intent.setData(data);
					intent.putExtra(Intent.EXTRA_EMAIL, mailid);

					intent.putExtra(Intent.EXTRA_CC, "");
					context.startActivity(intent);
				}
			});
			*/
			/*holder.txtshowonmap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
	
	
	
					String lat = object.get(position).delear_LATITUDE;
					String lng = object.get(position).delear_LONGITUDE;
					if(lat.equals(""))
					{
						String uri = "http://maps.google.com/maps?daddr="+object.get(position).delear_DIV_COMMON_NAME+" "+object.get(position).delear_DIV_ADDRESS_1 +" "
								+ object.get(position).delear_DIV_ADDRESS_2 + " "
								+ object.get(position).delear_DIV_CITY + " "
								+ object.get(position).delear_DIV_STATE + "-"
								+ object.get(position).delear_DIV_ZIP_CODE+ "&saddr="
								+ DelearLocator_fragment.MyLat + ","
								+ DelearLocator_fragment.MyLong;
						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW, Uri.parse(uri));
						intent.setClassName("com.google.android.apps.maps",
								"com.google.android.maps.MapsActivity");
						Intent i = new Intent(android.content.Intent.ACTION_VIEW, 
								Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
								context.startActivity(i);
							getContext().startActivity(intent);
						  
						getContext().startActivity(intent);
						
					}else
					{
						double lat1 = Double.parseDouble(object.get(position).delear_LATITUDE);
						double lng1 = Double.parseDouble(object.get(position).delear_LONGITUDE);
						String uri = "http://maps.google.com/maps?daddr="
								+ object.get(position).delear_LATITUDE + ","
								+ object.get(position).delear_LONGITUDE + "&saddr="
								+ DelearLocator_fragment.MyLat + ","
								+ DelearLocator_fragment.MyLong;
						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW, Uri.parse(uri));
						intent.setClassName("com.google.android.apps.maps",
								"com.google.android.maps.MapsActivity");

						getContext().startActivity(intent);
						String uriBegin = "geo:" + lat1 + "," + lng1;  
						String query = lat1 + "," + lng1 ;  
						String encodedQuery = Uri.encode(query);  
						String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";  
						Uri uri = Uri.parse(uriString);  
						Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri); 
						mapIntent.setClassName("com.google.android.apps.maps",
								"com.google.android.maps.MapsActivity");
						getContext().startActivity(mapIntent);
					}
					

				}
			});*/
			
			holder.txtshowonmap.setOnClickListener(new OnClickListener() {

		        @Override
		        public void onClick(View v) {
		  
		  
		  
		          String lat = object.get(position).delear_LATITUDE;
		          String lng = object.get(position).delear_LONGITUDE;
		          if(lat.equals(""))
		          {
		            /*String uri = "http://maps.google.com/maps?daddr="+object.get(position).delear_DIV_COMMON_NAME+" "+object.get(position).delear_DIV_ADDRESS_1 +" "
		                + object.get(position).delear_DIV_ADDRESS_2 + " "
		                + object.get(position).delear_DIV_CITY + " "
		                + object.get(position).delear_DIV_STATE + "-"
		                + object.get(position).delear_DIV_ZIP_CODE+ "&saddr="
		                + DelearLocator_fragment.MyLat + ","
		                + DelearLocator_fragment.MyLong;*/
		            String uriBegin = "geo:" + object.get(position).delear_DIV_COMMON_NAME+" "+object.get(position).delear_DIV_ADDRESS_1 +" "
		                + object.get(position).delear_DIV_ADDRESS_2 + " "
		                + object.get(position).delear_DIV_CITY + " "
		                + object.get(position).delear_DIV_STATE + "-"
		                + object.get(position).delear_DIV_ZIP_CODE+ "&saddr="
		                + DelearLocator_fragment.MyLat + "," + DelearLocator_fragment.MyLong;  
		            String query = object.get(position).delear_DIV_COMMON_NAME+" "+object.get(position).delear_DIV_ADDRESS_1 +" "
		                + object.get(position).delear_DIV_ADDRESS_2 + " "
		                + object.get(position).delear_DIV_CITY + " "
		                + object.get(position).delear_DIV_STATE + "-"
		                + object.get(position).delear_DIV_ZIP_CODE+ "&saddr="
		                + DelearLocator_fragment.MyLat + "," + DelearLocator_fragment.MyLong ;  
		            String encodedQuery = Uri.encode(query);  
		            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";  
		            Uri uri = Uri.parse(uriString);  
		            
		            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);  
		            intent.setClassName("com.google.android.apps.maps",
		                "com.google.android.maps.MapsActivity");

		            getContext().startActivity(intent);
		              
		          //  getContext().startActivity(intent);
		            
		          }else
		          {
		            double lat1 = Double.parseDouble(object.get(position).delear_LATITUDE);
		            double lng1 = Double.parseDouble(object.get(position).delear_LONGITUDE);
		            /*String uri = "http://maps.google.com/maps?daddr="
		                + object.get(position).delear_LATITUDE + ","
		                + object.get(position).delear_LONGITUDE + "&saddr="
		                + DelearLocator_fragment.MyLat + ","
		                + DelearLocator_fragment.MyLong;
		            Intent intent = new Intent(
		                android.content.Intent.ACTION_VIEW, Uri.parse(uri));
		            intent.setClassName("com.google.android.apps.maps",
		                "com.google.android.maps.MapsActivity");

		            getContext().startActivity(intent);*/
		            String uriBegin = "geo:" + lat1 + "," + lng1;  
		            String query = lat1 + "," + lng1 ;  
		            String encodedQuery = Uri.encode(query);  
		            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";  
		            Uri uri = Uri.parse(uriString);  
		            Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);  
		            mapIntent.setClassName("com.google.android.apps.maps",
		                "com.google.android.maps.MapsActivity");
		            getContext().startActivity(mapIntent);
		          }
		          

		        }
		      });

			holder.txtdirection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					
					
				/*	String uri = "http://maps.google.com/maps?daddr="
							+ object.get(position).delear_LATITUDE + ","
							+ object.get(position).delear_LONGITUDE + "&saddr="
							+ DelearLocator_fragment.MyLat + ","
							+ DelearLocator_fragment.MyLong;
					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(uri));
					intent.setClassName("com.google.android.apps.maps",
							"com.google.android.maps.MapsActivity");

					getContext().startActivity(intent);*/
					
					
					String lat = object.get(position).delear_LATITUDE;
					String lng = object.get(position).delear_LONGITUDE;
					if(lat.equals(""))
					{
						String uri = "http://maps.google.com/maps?daddr="+object.get(position).delear_DIV_COMMON_NAME+" "+object.get(position).delear_DIV_ADDRESS_1 +" "
								+ object.get(position).delear_DIV_ADDRESS_2 + " "
								+ object.get(position).delear_DIV_CITY + " "
								+ object.get(position).delear_DIV_STATE + "-"
								+ object.get(position).delear_DIV_ZIP_CODE+ "&saddr="
								+ DelearLocator_fragment.MyLat + ","
								+ DelearLocator_fragment.MyLong;
						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW, Uri.parse(uri));
						intent.setClassName("com.google.android.apps.maps",
								"com.google.android.maps.MapsActivity");

						getContext().startActivity(intent);
						
					}else
					{
					
						String uri = "http://maps.google.com/maps?daddr="
								+ object.get(position).delear_LATITUDE + ","
								+ object.get(position).delear_LONGITUDE + "&saddr="
								+ DelearLocator_fragment.MyLat + ","
								+ DelearLocator_fragment.MyLong;
						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW, Uri.parse(uri));
						intent.setClassName("com.google.android.apps.maps",
								"com.google.android.maps.MapsActivity");

						getContext().startActivity(intent);
					}

				}
			});

			holder.txtbookservices.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Fragment fragment = new BookServiceFragment();
					FragmentManager fragmentManager = ((Activity) context)
							.getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment)
							.addToBackStack(null).commit();
				}
			});

			holder.rdbtn.setChecked(position == selectedposition);
			holder.rdbtn.setTag(position);
			holder.rdbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					selectedposition = (Integer) v.getTag();
					
					notifyDataSetChanged();
					DelearLocator_fragment.selecteditem = selectedposition;
                    
                    final Dialog proceeddialog = new Dialog(context);
            		proceeddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            		proceeddialog.setContentView(R.layout.preferreddealer_popup);
            		proceeddialog.setCancelable(false);
            		proceeddialog.show();
            		
            		Button yes, no;
            		yes = (Button) proceeddialog.findViewById(R.id.btnyes);
            		no = (Button) proceeddialog.findViewById(R.id.btnno);
            		yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
					
							final String dealername = object.get(selectedposition).delear_DIV_COMMON_NAME;
							final String dealeraddress = object.get(selectedposition).delear_DIV_ADDRESS_1
									+ object.get(selectedposition).delear_DIV_ADDRESS_2 + " "
									+ object.get(selectedposition).delear_DIV_CITY + " "
									+ object.get(selectedposition).delear_DIV_STATE + "-"
									+ object.get(selectedposition).delear_DIV_ZIP_CODE;
							final String dealeremail = object.get(selectedposition).delear_DIV_EMAIL;
							final String dealernumber = object.get(selectedposition).delear_DIV_PHONE;
							final String dealerlat = object.get(selectedposition).delear_LATITUDE;
							final String dealerlong = object.get(selectedposition).delear_LONGITUDE;
							
							String req = Config.awsserverurl+"tmsc_ch/customerapp/user/setPrefferedDealer";
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_name",dealername));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_address",dealeraddress));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_number",dealernumber));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_email",dealeremail));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_latitude",dealerlat));
							nameValuePairs.add(new BasicNameValuePair("preffered_dealer_longitude",dealerlong));
							nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails.getUser_id()));
							nameValuePairs.add(new BasicNameValuePair("sessionId", UserDetails.getSeeionId()));
							Log.d("Selected dealer", object.get(selectedposition).delear_DIV_COMMON_NAME);
							
							new AWS_WebServiceCall(context, req, ServiceHandler.POST,
									Constants.setPrefferedDealer, nameValuePairs,
									new ResponseCallback() {

										@Override
										public void onResponseReceive(Object object) {
					
											boolean res = (boolean) object;
											if (res) {
												proceeddialog.dismiss();
					
												Toast.makeText(context, "successful",
														Toast.LENGTH_LONG).show();
												
												UserDetails.setPreffered_dealer_name(dealername);
												UserDetails.setPreffered_dealer_email(dealeremail);
												UserDetails.setPreffered_dealer_address(dealeraddress);
												UserDetails.setPreffered_dealer_number(dealernumber);
												UserDetails.setPreffered_dealer_latitude(dealerlat);
												UserDetails.setPreffered_dealer_longitude(dealerlong);
												proceeddialog.dismiss();
											
											} else {
												Toast.makeText(context, "unsuccessful",
														Toast.LENGTH_LONG).show();
											
												proceeddialog.dismiss();
											}

										}

										@Override
										public void onErrorReceive(String string) {
					
										}

									}).execute();
						}
					});
            		
            		no.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							proceeddialog.dismiss();
						}
					});
				}
			});

		}
		return v;
	}

	@Override
	public void onLocationChanged(Location location) {
		

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		

	}

	@Override
	public void onProviderEnabled(String provider) {
		

	}

	@Override
	public void onProviderDisabled(String provider) {
		

	}
}
