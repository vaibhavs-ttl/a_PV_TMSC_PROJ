package com.ttl.customersocialapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.NotificationAdapter;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class Notifiaction_fragment extends Fragment {

	private View rootView;
	private ListView notificationlist;

	public int currentimageindex = 0, currentimageindex1 = 0,
			currentimageindex2 = 0, currentimageindex3 = 0;
	private int loopVariable = 0;

	private ImageView slidingimage;

	private HorizontalScrollView horizontalScrollView;
	private NotificationAdapter notificationadp;
	private LinearLayout lineardynamic;
	private Button dynamicbtn;
	private ArrayList<Button> buttongrp = new ArrayList<Button>();
	private Button buttonArray[];
	private String notificationtype;

	private ArrayList<HashMap<String, String>> ntflist;

	public static ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> addlist = new ArrayList<HashMap<String, String>>();

	private HashMap<String, String> tabList = new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> tabListmap = new ArrayList<HashMap<String, String>>();

	private ArrayList<HashMap<String, String>> tabListObject = new ArrayList<HashMap<String, String>>();

	public Notifiaction_fragment() {

	}

	private Tracker mTracker;

	@Override
	public void onStart() {

		super.onStart();
		mTracker.setScreenName("NotificationScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	// DownloadImageTask DownloadImageTask;//= new
	// DownloadImageTask(slidingimage);
	Runnable mUpdateResults;
	boolean connect;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_notification, container,
				false);
		CheckConnectivity checknow = new CheckConnectivity();
		boolean connect = checknow.checkNow(getActivity());
		notificationlist = (ListView) rootView
				.findViewById(R.id.notificationlist);
		horizontalScrollView = (HorizontalScrollView) rootView
				.findViewById(R.id.horizontalScrollView);
		slidingimage = (ImageView) rootView.findViewById(R.id.imgslider);
		list = new ArrayList<HashMap<String, String>>();
		// DownloadImageTask= new DownloadImageTask(slidingimage);
		// Tracker
		AnalyticsApplication application = (AnalyticsApplication) getActivity()
				.getApplication();
		mTracker = application.getDefaultTracker();
		if (connect) {
			String req = Config.awsserverurl
					+ "tmsc_ch/customerapp/notifications/getNotificationsForApp";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", UserDetails
					.getUser_id()));
			nameValuePairs.add(new BasicNameValuePair("sessionId", UserDetails
					.getSeeionId()));
			new AWS_WebServiceCall(getActivity(), req, ServiceHandler.POST,
					Constants.getNotificationsForApp, nameValuePairs,
					new ResponseCallback() {

						@Override
						public void onResponseReceive(Object object) {
							
						

							ntflist = (ArrayList<HashMap<String, String>>) object;

							for (int i = 0; i < ntflist.size(); i++) {

								addlist.add(ntflist.get(i));

							}
							/*
							 * if (HomeFragment.Notificationchk == true) {
							 * tabList.put("notification_type",HomeFragment.
							 * GetNotificationType); for (int j = 0; j <
							 * AWS_WebServiceCall.notificationhashmaplist
							 * .size(); j++) { if
							 * (!HomeFragment.GetNotificationType
							 * .equalsIgnoreCase
							 * (AWS_WebServiceCall.notificationhashmaplist
							 * .get(j) .get("notification_type"))) {
							 * tabList.put(
							 * "notification_type",AWS_WebServiceCall
							 * .notificationhashmaplist
							 * .get(j).get("notification_type"));
							 * tabListmap.add(tabList); } } buttonArray = new
							 * Button[tabListmap.size()]; loopVariable =
							 * tabListmap.size(); tabListObject = tabListmap; //
							 * tabListObject =tabList; }
							 */if (HomeFragment.Notificationchk == true) {
								tabList = new HashMap<String, String>();
								tabListmap = new ArrayList<HashMap<String, String>>();
								tabList.put("notification_type",
										HomeFragment.GetNotificationType);
								// Log.d("get string home1",
								// tabList.get("notification_type").toString());
								tabListmap.add(tabList);
								for (int j = 0; j < AWS_WebServiceCall.notificationhashmaplist
										.size(); j++) {
									if (!HomeFragment.GetNotificationType
											.equalsIgnoreCase(AWS_WebServiceCall.notificationhashmaplist
													.get(j)
													.get("notification_type"))) {
										tabList = new HashMap<String, String>();
							
										tabList.put(
												"notification_type",
												AWS_WebServiceCall.notificationhashmaplist
														.get(j)
														.get("notification_type"));
										tabListmap.add(tabList);
									}
								}
							
								buttonArray = new Button[tabListmap.size()];
								loopVariable = tabListmap.size();
								tabListObject = tabListmap;
								// tabListObject =tabList;
							} else {

								buttonArray = new Button[AWS_WebServiceCall.notificationhashmaplist
										.size()];
								loopVariable = AWS_WebServiceCall.notificationhashmaplist
										.size();
								tabListObject = AWS_WebServiceCall.notificationhashmaplist;
							}

							for (int j = 0; j < loopVariable; j++) {

								lineardynamic = (LinearLayout) rootView
										.findViewById(R.id.linbtn);
							
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT);
								params.width = 0;
								params.weight = 1;
							
								// Create LinearLayout
								LinearLayout ll = new LinearLayout(
										getActivity());
								ll.setOrientation(LinearLayout.HORIZONTAL);

								// Create Button
								dynamicbtn = new Button(getActivity());
								// Give button an ID

								dynamicbtn.setId(j + 1);

								dynamicbtn.setText(tabListObject.get(j).get(
										"notification_type"));
								

								notificationtype = tabListObject.get(0).get(
										"notification_type");
								dynamicbtn
										.setBackgroundResource(R.drawable.notificationbutton);
								// set the layoutParams on the button
								dynamicbtn.setLayoutParams(params);
								if (HomeFragment.Notificationchk == true) {
									if (HomeFragment.GetNotificationType
											.equalsIgnoreCase(dynamicbtn
													.getText().toString())) {
										Resources res = getResources();
										Drawable drawable = res
												.getDrawable(R.drawable.notificationbg);
										dynamicbtn
												.setBackgroundDrawable(drawable);

										dynamicbtn.setTextColor(getResources()
												.getColor(R.color.litegray));
										// slidingimage.setImageBitmap(null);
										slidingimage
												.setBackgroundResource(R.drawable.ban);
										for (int i = 0; i < AWS_WebServiceCall.notificationhashmaplist
												.size(); i++) {
											if (dynamicbtn
													.getText()
													.toString()
													.equalsIgnoreCase(
															AWS_WebServiceCall.notificationhashmaplist
																	.get(i)
																	.get("notification_type"))) {
												new LoadProfileImage(
														slidingimage)
														.execute(AWS_WebServiceCall.notificationhashmaplist
																.get(i)
																.get("banner_image"));

											
											}
										}

									}
								} else {
									dynamicbtn
											.setBackgroundResource(R.drawable.notificationbutton);
									dynamicbtn.setTextColor(getResources()
											.getColor(R.color.darkgray));
								}
								

								// Set click listener for button
								dynamicbtn
										.setOnClickListener(new OnClickListener() {

											public void onClick(View v) {

												for (int i = 0; i < addlist
														.size(); i++) {
													list.remove(addlist.get(i));

													if (addlist
															.get(i)
															.get("notification_type")
															.equalsIgnoreCase(
																	((Button) v)
																			.getText()
																			.toString())) {
													
														list.add(addlist.get(i));

														changeColor((Button) v);
														notificationadp
																.notifyDataSetChanged();
													}

												}
												slidingimage
														.setImageBitmap(null);
												slidingimage
														.setBackgroundResource(R.drawable.ban);
												for (int j = 0; j < AWS_WebServiceCall.notificationhashmaplist
														.size(); j++) {
													if (((Button) v)
															.getText()
															.toString()
															.equalsIgnoreCase(
																	AWS_WebServiceCall.notificationhashmaplist
																			.get(j)
																			.get("notification_type"))) {
														new LoadProfileImage(
																slidingimage)
																.execute(AWS_WebServiceCall.notificationhashmaplist
																		.get(j)
																		.get("banner_image"));

														

													}
												}
											}

										});

								// Add button to LinearLayout
								buttongrp.add(dynamicbtn);
								ll.addView(dynamicbtn);
								// Add button to LinearLayout defined in XML
								lineardynamic.addView(ll);

							}
							if (HomeFragment.Notificationchk == true) {
								for (int i = 0; i < ntflist.size(); i++) {

									addlist.add(ntflist.get(i));
									list.remove(addlist.get(i));
									if (HomeFragment.GetNotificationType
											.equals(addlist.get(i).get(
													"notification_type"))) {
										Log.d(addlist.get(i).get("id"),
												"get id");
										list.add(addlist.get(i));

										notificationadp.notifyDataSetChanged();
									}

								}

								HomeFragment.Notificationchk = false;
							} else {
								for (int i = 0; i < ntflist.size(); i++) {

									addlist.add(ntflist.get(i));
									Log.d(notificationtype, "get type");
									list.remove(addlist.get(i));
									if (addlist.get(i).get("notification_type")
											.equals(notificationtype)) {
										// Log.d(addlist.get(i).get("id"),
										// "get id");
										list.add(addlist.get(i));

										notificationadp.notifyDataSetChanged();
										buttongrp.get(0).setBackgroundResource(
												R.drawable.notificationbg);
										buttongrp.get(0).setTextColor(
												getResources().getColor(
														R.color.litegray));
									}

								}
								slidingimage.setImageBitmap(null);
								slidingimage
										.setBackgroundResource(R.drawable.ban);
								for (int j = 0; j < AWS_WebServiceCall.notificationhashmaplist
										.size(); j++) {

									if (notificationtype
											.equalsIgnoreCase(AWS_WebServiceCall.notificationhashmaplist
													.get(j)
													.get("notification_type"))) {

										new LoadProfileImage(slidingimage)
												.execute(AWS_WebServiceCall.notificationhashmaplist
														.get(j).get(
																"banner_image"));

										
									}
								}
							}
							rootView.getRootView().setFocusable(true);
							rootView.getRootView().requestFocus();
						}

						@Override
						public void onErrorReceive(String string) {
							
							Log.d(string, "get string");
							Toast.makeText(getActivity(),
									"Notifications not available.",
									Toast.LENGTH_LONG).show();

						}
					}).execute();
		} else {
			Toast toast = Toast.makeText(getActivity(),
					"No network connection available.", Toast.LENGTH_SHORT);
			toast.show();
		}
		notificationadp = new NotificationAdapter(getActivity(),
				R.layout.notifiation_list, list);
		notificationlist.setAdapter(notificationadp);
		notificationadp.notifyDataSetChanged();

		rootView.getRootView().setFocusableInTouchMode(true);
		rootView.getRootView().requestFocus();

		rootView.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						/*
						 * ImageView img = null; DownloadImageTask
						 * downloadImageTask = new DownloadImageTask( img);
						 * downloadImageTask.cancel(true);
						 * DownloadImageTask.cancel(true);
						 * if(DownloadImageTask.isCancelled()){
						 */
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container, new HomeFragment())
								.addToBackStack(null).commit();

						/*
						 * } else { Toast.makeText(getActivity(), "not cancled",
						 * Toast.LENGTH_SHORT).show(); }
						 */
						return true;
					}
				}
				return false;
			}
		});
		return rootView;
	}

	public void changeColor(Button v) {

		for (int i = 0; i < buttongrp.size(); i++) {
			if (v != buttongrp.get(i)) {
				buttongrp.get(i).setBackgroundResource(
						R.drawable.notificationbutton);
				buttongrp.get(i).setTextColor(
						getResources().getColor(R.color.darkgray));

			} else {

				buttongrp.get(i).setBackgroundResource(
						R.drawable.notificationbg);
				buttongrp.get(i).setTextColor(
						getResources().getColor(R.color.litegray));
			}
		}

	}

	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			Bitmap mIcon11 = null;
			try {
				Log.e("image", "Loading");

				InputStream in = new java.net.URL(urls[0]).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				// Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setBackgroundResource(0);
			bmImage.setImageBitmap(result);
		}
	}
}
