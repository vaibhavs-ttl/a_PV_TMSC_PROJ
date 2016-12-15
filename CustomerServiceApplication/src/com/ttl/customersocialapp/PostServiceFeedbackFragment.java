package com.ttl.customersocialapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.adapter.ResponseCallback;
import com.ttl.communication.CheckConnectivity;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.ServiceHandler;
import com.ttl.model.UserDetails;
import com.ttl.webservice.AWS_WebServiceCall;
import com.ttl.webservice.Config;
import com.ttl.webservice.Constants;

public class PostServiceFeedbackFragment extends Fragment {

	private Spinner spinner_regNo;

	private SeekBar seek1, seek2, seek3, seek4, seek5, seek6, seek7, seek8,
			seek9, seek10, seek11, seek12, seek13;
	private ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9,
			img10, img11, img12, img13;
	private TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9,
			txt10, txt11, txt12, txt13;
	private EditText txtservicetype, txtdateout, generalfeedbak;
	private Button submit;
	private Spinner jobCardSpinner;
	private List<String> jobCardsList = new ArrayList<String>();
	private List<String> SR_DATE_List = new ArrayList<String>();
	private List<String> SR_TYPE_LIST = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> jobCardList;
	private List<String> regnovalues = new ArrayList<String>();
	private Bundle bundle;
	private boolean connect;

	private Tracker mTracker;

	@Override
	public void onStart() {
		
		super.onStart();
		mTracker.setScreenName("GenericFeedbackScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(
				R.layout.fragment_post_service_feedback, viewGroup, false);
	
		AnalyticsApplication application = (AnalyticsApplication) getActivity()
				.getApplication();
		mTracker = application.getDefaultTracker();

		seek1 = (SeekBar) v.findViewById(R.id.seek1);
		seek2 = (SeekBar) v.findViewById(R.id.seek2);
		seek3 = (SeekBar) v.findViewById(R.id.seek3);
		seek4 = (SeekBar) v.findViewById(R.id.seek4);
		seek5 = (SeekBar) v.findViewById(R.id.seek5);
		seek6 = (SeekBar) v.findViewById(R.id.seek6);
		seek7 = (SeekBar) v.findViewById(R.id.seek7);
		seek8 = (SeekBar) v.findViewById(R.id.seek8);
		seek9 = (SeekBar) v.findViewById(R.id.seek9);
		seek10 = (SeekBar) v.findViewById(R.id.seek10);
		seek11 = (SeekBar) v.findViewById(R.id.seek11);
		seek12 = (SeekBar) v.findViewById(R.id.seek12);
		seek13 = (SeekBar) v.findViewById(R.id.seek13);

		img1 = (ImageView) v.findViewById(R.id.imgsmiley1);
		img2 = (ImageView) v.findViewById(R.id.imgsmiley2);
		img3 = (ImageView) v.findViewById(R.id.imgsmiley3);
		img4 = (ImageView) v.findViewById(R.id.imgsmiley4);
		img5 = (ImageView) v.findViewById(R.id.imgsmiley5);
		img6 = (ImageView) v.findViewById(R.id.imgsmiley6);
		img7 = (ImageView) v.findViewById(R.id.imgsmiley7);
		img8 = (ImageView) v.findViewById(R.id.imgsmiley8);
		img9 = (ImageView) v.findViewById(R.id.imgsmiley9);
		img10 = (ImageView) v.findViewById(R.id.imgsmiley10);
		img11 = (ImageView) v.findViewById(R.id.imgsmiley11);
		img12 = (ImageView) v.findViewById(R.id.imgsmiley12);
		img13 = (ImageView) v.findViewById(R.id.imgsmiley13);

		txt1 = (TextView) v.findViewById(R.id.txtque1);
		txt2 = (TextView) v.findViewById(R.id.txtque2);
		txt3 = (TextView) v.findViewById(R.id.txtque3);
		txt4 = (TextView) v.findViewById(R.id.txtque4);
		txt5 = (TextView) v.findViewById(R.id.txtque5);
		txt6 = (TextView) v.findViewById(R.id.txtque6);
		txt7 = (TextView) v.findViewById(R.id.txtque7);
		txt8 = (TextView) v.findViewById(R.id.txtque8);
		txt9 = (TextView) v.findViewById(R.id.txtque9);
		txt10 = (TextView) v.findViewById(R.id.txtque10);
		txt11 = (TextView) v.findViewById(R.id.txtque11);
		txt12 = (TextView) v.findViewById(R.id.txtque12);
		txt13 = (TextView) v.findViewById(R.id.txtque13);

		txtservicetype = (EditText) v.findViewById(R.id.txtservicetype);
		txtdateout = (EditText) v.findViewById(R.id.txtdate);
		generalfeedbak = (EditText) v.findViewById(R.id.txtpsffeedbak);
		spinner_regNo = (Spinner) v.findViewById(R.id.spinregnumber);
		jobCardSpinner = (Spinner) v.findViewById(R.id.spinjobcardnum);

		bundle = getArguments();
		if (bundle != null) {
			Log.d("BUNDLE PostServiceFeedbackFragment",
					bundle.getString("Reg_number") + " "
							+ bundle.getString("jobcard_number") + " "
							+ bundle.getString("services_type") + " "
							+ bundle.getString("services_date"));
			txtservicetype.setText(bundle.getString("services_type"));
			txtdateout.setText(bundle.getString("services_date"));
		}

		regnovalues.add("Select Vehicle");
		int size = new UserDetails().getRegNumberList().size();
		for (int i = 0; i < size; i++) {
			if (!(new UserDetails().getRegNumberList().get(i)
					.get("registration_num").toString().equals(""))) {
				regnovalues.add(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"));
			} else {
				regnovalues.add(new UserDetails().getRegNumberList().get(i)
						.get("chassis_num"));
			}
		}
		ArrayAdapter<String> regno = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, regnovalues);
		regno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_regNo.setAdapter(regno);

		if (bundle != null) {
			String getReg_number = bundle.getString("Reg_number");
			Log.d("get reg _no", getReg_number);
			for (int i = 0; i < regnovalues.size(); i++) {
				if (getReg_number.equalsIgnoreCase(regnovalues.get(i))) {
					spinner_regNo.setSelection(i);
				}

			}
		}

		jobCardsList.add("Job Card Number");
		final ArrayAdapter<String> jobCardSpinner_adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				jobCardsList);
		jobCardSpinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jobCardSpinner.setAdapter(jobCardSpinner_adapter);

		spinner_regNo
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {

						String regNo = spinner_regNo.getSelectedItem()
								.toString();
						jobCardsList.clear();
						jobCardsList.add("Job Card Number");
						jobCardSpinner.setSelection(0);
						Log.i("Selected item : ", regNo + " AT " + pos);
						if (pos != 0) {
							txtdateout.setText("");
							txtservicetype.setText("");
							// fire web service
							((TextView) arg0.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.textcolor));
							String chassis = new UserDetails()
									.getRegNumberList().get(pos - 1)
									.get("chassis_num");
							
							
							connect=new CheckConnectivity().checkNow(getActivity());
							
							if (connect) {
								String req = Config.awsserverurl
										+ "tmsc_ch/customerapp/feedbackServices/getJobCardList";
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										3);
								nameValuePairs.add(new BasicNameValuePair(
										"chassis_num", chassis));
								nameValuePairs.add(new BasicNameValuePair(
										"sessionId", UserDetails.getSeeionId()));
								nameValuePairs.add(new BasicNameValuePair(
										"user_id", UserDetails.getUser_id()));
								new AWS_WebServiceCall(v.getContext(), req,
										ServiceHandler.POST,
										Constants.getJobCardList,
										nameValuePairs, new ResponseCallback() {

											@Override
											public void onResponseReceive(
													Object object) {
											
												jobCardList = (ArrayList<HashMap<String, String>>) object;
												
												Log.v("psf data", ""+jobCardList.size());			
												
												
												for (int i = 0; i < jobCardList
														.size(); i++) {
													jobCardsList
															.add(jobCardList
																	.get(i)
																	.get("job_card_number"));
													SR_DATE_List
															.add(jobCardList
																	.get(i)
																	.get("SR_CREATED_DT"));
													SR_TYPE_LIST
															.add(jobCardList
																	.get(i)
																	.get("SR_TYPE"));
											
												
									/*			Log.v("jc card", jobCardList
														.get(i)
														.get("job_card_number"));
									
												Log.v("SR DATE", jobCardList
														.get(i)
														.get("SR_CREATED_DT"));
									
													
												Log.v("SR_TYPE_LIST", jobCardList
														.get(i)
														.get("SR_TYPE"));
									
												*/
												
												
													
												}

												if (bundle != null) {
													for (int i = 0; i < jobCardsList
															.size(); i++) {
														String jobcard_number = bundle
																.getString("jobcard_number");
														if (jobcard_number
																.equalsIgnoreCase(jobCardsList
																		.get(i))) {
															jobCardSpinner
																	.setSelection(i);
														}
													}
												}
												jobCardSpinner_adapter
														.notifyDataSetChanged();
												v.getRootView().setFocusable(
														true);
												v.getRootView().requestFocus();
											}

											@Override
											public void onErrorReceive(
													String string) {
												
												Toast.makeText(
														getActivity(),
														"No job cards found for this Vehicle",
														Toast.LENGTH_SHORT)
														.show();
											}
										}).execute();
							} else {
								Toast toast = Toast.makeText(getActivity(),
										"No network connection available.",
										Toast.LENGTH_SHORT);
								toast.show();
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						

					}
				});

		jobCardSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						if (pos != 0) {

							String[] dateTime = SR_DATE_List.get(pos - 1)
									.split("T");
							String date = dateTime[0];
							String[] day = date.split("-");
							String dayval = day[2];
							String monthval = day[1];
							String yearval = day[0];
							txtdateout.setText(dayval + "-" + monthval + "-"
									+ yearval);
							txtservicetype.setText(SR_TYPE_LIST.get(pos - 1));
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						

					}
				});

		if (new UserDetails().getRegNumberList().size() == 0) {
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();
		}

		submit = (Button) v.findViewById(R.id.visit_site);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v1) {
				// sumbitFeedback();
				
				connect=new CheckConnectivity().checkNow(getActivity());
				
				if (spinner_regNo.getSelectedItemPosition() == 0) {
					Toast.makeText(getActivity(), "Please select a Vehicle.",
							Toast.LENGTH_SHORT).show();
				} else if (jobCardSpinner.getSelectedItemPosition() == 0) {
					Toast.makeText(getActivity(), "Please select a Job Card.",
							Toast.LENGTH_SHORT).show();
				} else {
					if (generalfeedbak.getText().toString().length() > 0) {
						if (connect) {
							Calendar c = Calendar.getInstance();
							System.out.println("Current time => " + c.getTime());

							SimpleDateFormat df = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							String formattedDate = df.format(c.getTime());

							String req = Config.awsserverurl
									+ "tmsc_ch/customerapp/feedbackServices/addPSFFeedback";
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
									20);
							nameValuePairs.add(new BasicNameValuePair(
									"user_id", UserDetails.getUser_id()));
							// nameValuePairs.add(new
							// BasicNameValuePair("registration_num",state));
							// nameValuePairs.add(new
							// BasicNameValuePair("chassis_num",state));
							nameValuePairs.add(new BasicNameValuePair(
									"job_card_number", jobCardSpinner
											.getSelectedItem().toString()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_1", txt1.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_2", txt2.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_3", txt3.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_4", txt4.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_5", txt5.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_6", txt6.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_7", txt7.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_8", txt8.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_9", txt9.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_10", txt10.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_11", txt11.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_12", txt12.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"feedback_for_ques_13", txt13.getText()
											.toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"general_feedback", generalfeedbak
											.getText().toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"SR_TYPE", txtservicetype.getText()
											.toString()));
							nameValuePairs.add(new BasicNameValuePair(
									"SR_CREATED_DT", txtdateout.getText()
											.toString()));
							nameValuePairs.add(new BasicNameValuePair(
									"timestamp", formattedDate));
							nameValuePairs.add(new BasicNameValuePair(
									"sessionId", UserDetails.getSeeionId()));
							new AWS_WebServiceCall(v.getContext(), req,
									ServiceHandler.POST,
									Constants.addPSFFeedback, nameValuePairs,
									new ResponseCallback() {

										@Override
										public void onResponseReceive(
												Object object) {
								
							
											
											Toast.makeText(getActivity(),
											 "Thanks for giving your valueable feedback.",
											 Toast.LENGTH_SHORT).show();
										
											mTracker.send(new HitBuilders.EventBuilder()
													.setCategory(
															UserDetails
																	.getUser_id())
													.setAction("thread_true")
													.setLabel(
															"PostServiceFeedback")
													.build());

											FragmentManager fm = getFragmentManager();
											FragmentTransaction tx = fm
													.beginTransaction();
											tx.replace(R.id.frame_container,
													new HomeFragment())
													.commit();
										}

										@Override
										public void onErrorReceive(String string) {
											
											/*Toast.makeText(
													getActivity(),
													"There is some issue with the Service. Please try again.",
													Toast.LENGTH_SHORT).show();
											*/
											Toast.makeText(
													getActivity(),
											string,
													Toast.LENGTH_SHORT).show();
											
											mTracker.send(new HitBuilders.EventBuilder()
													.setCategory(
															UserDetails
																	.getUser_id())
													.setAction("thread_false")
													.setLabel(
															"PostServiceFeedback")
													.build());
										}
									}).execute();
						} else {
							Toast toast = Toast.makeText(getActivity(),
									"No network connection available.",
									Toast.LENGTH_SHORT);
							toast.show();
						}
					} else {
						Toast.makeText(getActivity(),
								"Please enter your feedback.",
								Toast.LENGTH_SHORT).show();
					}
				}

				// dialog.dismiss();
			}
		});

		seek1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt1.setText(String.valueOf(progress));

				if (progress == 0)
					img1.setImageResource(R.drawable.one);
				else if (progress == 1)
					img1.setImageResource(R.drawable.one);
				else if (progress == 2)
					img1.setImageResource(R.drawable.two);
				else if (progress == 3)
					img1.setImageResource(R.drawable.three);
				else if (progress == 4)
					img1.setImageResource(R.drawable.four);
				else if (progress == 5)
					img1.setImageResource(R.drawable.five);
				else if (progress == 6)
					img1.setImageResource(R.drawable.six);
				else if (progress == 7)
					img1.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img1.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img1.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img1.setImageResource(R.drawable.ten);

			}

		});

		seek2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt2.setText(String.valueOf(progress));

				if (progress == 0)
					img2.setImageResource(R.drawable.one);
				else if (progress == 1)
					img2.setImageResource(R.drawable.one);
				else if (progress == 2)
					img2.setImageResource(R.drawable.two);
				else if (progress == 3)
					img2.setImageResource(R.drawable.three);
				else if (progress == 4)
					img2.setImageResource(R.drawable.four);
				else if (progress == 5)
					img2.setImageResource(R.drawable.five);
				else if (progress == 6)
					img2.setImageResource(R.drawable.six);
				else if (progress == 7)
					img2.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img2.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img2.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img2.setImageResource(R.drawable.ten);

			}

		});
		seek3.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt3.setText(String.valueOf(progress));

				if (progress == 0)
					img3.setImageResource(R.drawable.one);
				else if (progress == 1)
					img3.setImageResource(R.drawable.one);
				else if (progress == 2)
					img3.setImageResource(R.drawable.two);
				else if (progress == 3)
					img3.setImageResource(R.drawable.three);
				else if (progress == 4)
					img3.setImageResource(R.drawable.four);
				else if (progress == 5)
					img3.setImageResource(R.drawable.five);
				else if (progress == 6)
					img3.setImageResource(R.drawable.six);
				else if (progress == 7)
					img3.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img3.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img3.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img3.setImageResource(R.drawable.ten);
			}

		});
		seek4.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt4.setText(String.valueOf(progress));
				if (progress == 0)
					img4.setImageResource(R.drawable.one);
				else if (progress == 1)
					img4.setImageResource(R.drawable.one);
				else if (progress == 2)
					img4.setImageResource(R.drawable.two);
				else if (progress == 3)
					img4.setImageResource(R.drawable.three);
				else if (progress == 4)
					img4.setImageResource(R.drawable.four);
				else if (progress == 5)
					img4.setImageResource(R.drawable.five);
				else if (progress == 6)
					img4.setImageResource(R.drawable.six);
				else if (progress == 7)
					img4.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img4.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img4.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img4.setImageResource(R.drawable.ten);

			}

		});
		seek5.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt5.setText(String.valueOf(progress));

				if (progress == 0)
					img5.setImageResource(R.drawable.one);
				else if (progress == 1)
					img5.setImageResource(R.drawable.one);
				else if (progress == 2)
					img5.setImageResource(R.drawable.two);
				else if (progress == 3)
					img5.setImageResource(R.drawable.three);
				else if (progress == 4)
					img5.setImageResource(R.drawable.four);
				else if (progress == 5)
					img5.setImageResource(R.drawable.five);
				else if (progress == 6)
					img5.setImageResource(R.drawable.six);
				else if (progress == 7)
					img5.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img5.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img5.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img5.setImageResource(R.drawable.ten);

			}

		});
		seek6.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt6.setText(String.valueOf(progress));

				if (progress == 0)
					img6.setImageResource(R.drawable.one);
				else if (progress == 1)
					img6.setImageResource(R.drawable.one);
				else if (progress == 2)
					img6.setImageResource(R.drawable.two);
				else if (progress == 3)
					img6.setImageResource(R.drawable.three);
				else if (progress == 4)
					img6.setImageResource(R.drawable.four);
				else if (progress == 5)
					img6.setImageResource(R.drawable.five);
				else if (progress == 6)
					img6.setImageResource(R.drawable.six);
				else if (progress == 7)
					img6.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img6.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img6.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img6.setImageResource(R.drawable.ten);
			}

		});
		seek7.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt7.setText(String.valueOf(progress));

				if (progress == 0)
					img7.setImageResource(R.drawable.one);
				else if (progress == 1)
					img7.setImageResource(R.drawable.one);
				else if (progress == 2)
					img7.setImageResource(R.drawable.two);
				else if (progress == 3)
					img7.setImageResource(R.drawable.three);
				else if (progress == 4)
					img7.setImageResource(R.drawable.four);
				else if (progress == 5)
					img7.setImageResource(R.drawable.five);
				else if (progress == 6)
					img7.setImageResource(R.drawable.six);
				else if (progress == 7)
					img7.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img7.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img7.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img7.setImageResource(R.drawable.ten);
			}

		});
		seek8.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt8.setText(String.valueOf(progress));

				if (progress == 0)
					img8.setImageResource(R.drawable.one);
				else if (progress == 1)
					img8.setImageResource(R.drawable.one);
				else if (progress == 2)
					img8.setImageResource(R.drawable.two);
				else if (progress == 3)
					img8.setImageResource(R.drawable.three);
				else if (progress == 4)
					img8.setImageResource(R.drawable.four);
				else if (progress == 5)
					img8.setImageResource(R.drawable.five);
				else if (progress == 6)
					img8.setImageResource(R.drawable.six);
				else if (progress == 7)
					img8.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img8.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img8.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img8.setImageResource(R.drawable.ten);

			}

		});
		seek9.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt9.setText(String.valueOf(progress));

				if (progress == 0)
					img9.setImageResource(R.drawable.one);
				else if (progress == 1)
					img9.setImageResource(R.drawable.one);
				else if (progress == 2)
					img9.setImageResource(R.drawable.two);
				else if (progress == 3)
					img9.setImageResource(R.drawable.three);
				else if (progress == 4)
					img9.setImageResource(R.drawable.four);
				else if (progress == 5)
					img9.setImageResource(R.drawable.five);
				else if (progress == 6)
					img9.setImageResource(R.drawable.six);
				else if (progress == 7)
					img9.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img9.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img9.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img9.setImageResource(R.drawable.ten);

			}

		});
		seek10.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt10.setText(String.valueOf(progress));

				if (progress == 0)
					img10.setImageResource(R.drawable.one);
				else if (progress == 1)
					img10.setImageResource(R.drawable.one);
				else if (progress == 2)
					img10.setImageResource(R.drawable.two);
				else if (progress == 3)
					img10.setImageResource(R.drawable.three);
				else if (progress == 4)
					img10.setImageResource(R.drawable.four);
				else if (progress == 5)
					img10.setImageResource(R.drawable.five);
				else if (progress == 6)
					img10.setImageResource(R.drawable.six);
				else if (progress == 7)
					img10.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img10.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img10.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img10.setImageResource(R.drawable.ten);

			}

		});
		seek11.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt11.setText(String.valueOf(progress));

				if (progress == 0)
					img11.setImageResource(R.drawable.one);
				else if (progress == 1)
					img11.setImageResource(R.drawable.one);
				else if (progress == 2)
					img11.setImageResource(R.drawable.two);
				else if (progress == 3)
					img11.setImageResource(R.drawable.three);
				else if (progress == 4)
					img11.setImageResource(R.drawable.four);
				else if (progress == 5)
					img11.setImageResource(R.drawable.five);
				else if (progress == 6)
					img11.setImageResource(R.drawable.six);
				else if (progress == 7)
					img11.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img11.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img11.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img11.setImageResource(R.drawable.ten);

			}

		});
		seek12.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt12.setText(String.valueOf(progress));

				if (progress == 0)
					img12.setImageResource(R.drawable.one);
				else if (progress == 1)
					img12.setImageResource(R.drawable.one);
				else if (progress == 2)
					img12.setImageResource(R.drawable.two);
				else if (progress == 3)
					img12.setImageResource(R.drawable.three);
				else if (progress == 4)
					img12.setImageResource(R.drawable.four);
				else if (progress == 5)
					img12.setImageResource(R.drawable.five);
				else if (progress == 6)
					img12.setImageResource(R.drawable.six);
				else if (progress == 7)
					img12.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img12.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img12.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img12.setImageResource(R.drawable.ten);

			}

		});
		seek13.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				

				txt13.setText(String.valueOf(progress));

				if (progress == 0)
					img13.setImageResource(R.drawable.one);
				else if (progress == 1)
					img13.setImageResource(R.drawable.one);
				else if (progress == 2)
					img13.setImageResource(R.drawable.two);
				else if (progress == 3)
					img13.setImageResource(R.drawable.three);
				else if (progress == 4)
					img13.setImageResource(R.drawable.four);
				else if (progress == 5)
					img13.setImageResource(R.drawable.five);
				else if (progress == 6)
					img13.setImageResource(R.drawable.six);
				else if (progress == 7)
					img13.setImageResource(R.drawable.seven);
				else if (progress == 8)
					img13.setImageResource(R.drawable.eight);
				else if (progress == 9)
					img13.setImageResource(R.drawable.nine);
				else if (progress == 10)
					img13.setImageResource(R.drawable.ten);

			}

		});
		// back
		v.getRootView().setFocusableInTouchMode(true);
		v.getRootView().requestFocus();

		v.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container, new HomeFragment())
								.addToBackStack(null).commit();
						return true;
					}
				}
				return false;
			}
		});

		return v;
	}

}
