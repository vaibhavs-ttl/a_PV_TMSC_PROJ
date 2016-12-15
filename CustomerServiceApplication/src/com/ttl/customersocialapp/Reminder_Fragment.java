package com.ttl.customersocialapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.crypto.Cipher;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.helper.ReminderDBHelper;
import com.ttl.model.ReminderContract.Reminder;
import com.ttl.model.Reminder_Child;
import com.ttl.model.Reminder_Parent;
import com.ttl.model.UserDetails;
import com.ttl.webservice.Constants;

public class Reminder_Fragment extends Fragment {

	private Spinner spinner_vehicle_no, sipnner_remindertype;

	private String[] remindertype = { "Reminder Type", "Next Service Date",
			"Insurance Renewal", "AMC Purchase/Renewal",
			"Extended Warranty Purchase/Renewal", "PUC Renewal",
			"Service Booking", "Tyre Change", "Battery Change",
			"24X7 On Road Assistance Renewal" };
	private List<String> regnovalues = new ArrayList<String>();
	private String dateandtime;
	private ArrayList<Reminder_Parent> parents;
	private ExpandableListView exlv;
	private EditText date, time;
	private View v;
	private Button submit;
	static final int DATE_DIALOG_ID = 0;
	public static Vector<Reminder_Child> insList = new Vector<Reminder_Child>();
	public static Vector<Reminder_Parent> pList = new Vector<Reminder_Parent>();
	private int year, month, day, hour, minute;
	private String BOOKINGDATE, BOOKINGTIME, historyDate, historyTime;
	private DatePicker dp;
	private int gethour, getmin;
	public static String spinnerno, spinnertype;
	private Bundle bundle;
	private String regno = "", timenext = "", datenext = "", remindtype = "",
			getdisplaydate, getdisplaydate1;
	private String getdate;
	private Tracker mTracker;

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		mTracker.setScreenName("ReminderScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		v = inflater.inflate(R.layout.fragment_reminder, container, false);

		v.getRootView().setFocusableInTouchMode(true);
		v.getRootView().requestFocus();
		AnalyticsApplication application = (AnalyticsApplication) getActivity()
				.getApplication();
		mTracker = application.getDefaultTracker();
		if (new UserDetails().getRegNumberList().size() == 0) {
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();
		}

		if (getArguments() != null) {
			if (getArguments().getString("Fragment").equals(
					"ServiceBookingHistoryFragment")) {
				v.getRootView().setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (event.getAction() == KeyEvent.ACTION_DOWN) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								FragmentManager fm = getFragmentManager();
								FragmentTransaction tx = fm.beginTransaction();
								tx.replace(R.id.frame_container,
										new ServiceBookingHistoryFragment())
										.commit();
								return true;
							}
						}
						return false;
					}
				});
			} else if (getArguments().getString("Fragment").equals(
					"VehicleDetails_Fragment")) {
				v.getRootView().setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (event.getAction() == KeyEvent.ACTION_DOWN) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								FragmentManager fm = getFragmentManager();
								FragmentTransaction tx = fm.beginTransaction();
								tx.replace(R.id.frame_container,
										new VehicleDetails_Fragment()).commit();
								return true;
							}
						}
						return false;
					}
				});
			}
		} else {
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
		}

		bundle = getArguments();
		if (bundle != null) {
			Log.i("BUNDLE HomeFragment",
					bundle.getString("remindregNo") + " "
							+ bundle.getString("remindTime") + " "
							+ bundle.getString("remindDate"));
			regno = bundle.getString("remindregNo");
			timenext = bundle.getString("remindTime");
			datenext = bundle.getString("remindDate");
			remindtype = bundle.getString("remindtype");
		}

		exlv = (ExpandableListView) v.findViewById(R.id.remlist);

		loadReminders(getActivity());
		spinner_vehicle_no = (Spinner) v.findViewById(R.id.sipnner_vehicle_no);
		int size = new UserDetails().getRegNumberList().size();
		regnovalues.add("Select Vehicle");
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
		ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.reminder_vehicle_sipnnerbgtitle,
				regnovalues);

		sp_adapter
				.setDropDownViewResource(R.layout.reminder_vehicle_sipnneritem);
		spinner_vehicle_no.setAdapter(sp_adapter);
		for (int i = 0; i < regnovalues.size(); i++) {
			if (regno.equals(regnovalues.get(i))) {
				spinner_vehicle_no.setSelection(i);
			}
		}
		spinner_vehicle_no
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
	
						if (position != 0) {
							((TextView) parent.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.textcolor));

						} else {
							((TextView) parent.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.hintcolor));

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
	

					}
				});

		sipnner_remindertype = (Spinner) v
				.findViewById(R.id.sipnner_remindertype);
		ArrayAdapter<String> sptype_adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.reminder_type_sipnnerbgtitle,
				remindertype);
		sptype_adapter
				.setDropDownViewResource(R.layout.reminder_type_sipnneritem);

		sipnner_remindertype.setAdapter(sptype_adapter);

		for (int i = 0; i < remindertype.length; i++) {
			if (remindtype.equals(remindertype[i])) {
				sipnner_remindertype.setSelection(i);
			}
		}
		sipnner_remindertype
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
	
						if (position != 0) {
							((TextView) parent.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.textcolor));

						} else {
							((TextView) parent.getChildAt(0)).setTextColor(v
									.getContext().getResources()
									.getColor(R.color.hintcolor));

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
	

					}
				});
		/*
		 * String req =
		 * "http://tmlmobilityservices.co.in:8080/CustomerApp_Restws/customerapp/productServices/getReminderTypes"
		 * ; new AWS_WebServiceCall(getActivity(), req, ServiceHandler.GET,
		 * Constants.getReminderTypes, new AwsResponseCallback() {
		 * 
		 * @Override public void onResponseReceive(Object object) { // TODO
		 * Auto-generated method stub List<String> regtype = new
		 * ArrayList<String>(); regtype = (List<String>) object;
		 * ArrayAdapter<String> sptype_adapter = new ArrayAdapter<String>(
		 * getActivity(), R.layout.reminder_type_sipnnerbgtitle, regtype);
		 * sptype_adapter
		 * .setDropDownViewResource(R.layout.reminder_type_sipnneritem);
		 * 
		 * sipnner_remindertype.setAdapter(sptype_adapter);
		 * if(!(regno.equals(""))) { sipnner_remindertype.setSelection(1); } }
		 * 
		 * @Override public void onErrorReceive(String string) { // TODO
		 * Auto-generated method stub
		 * 
		 * } }).execute();
		 */

		date = (EditText) v.findViewById(R.id.edtdate);

		date.setText(datenext);
		date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v1) {
	
				showDatePickerDialog(v1);
			}
		});

		time = (EditText) v.findViewById(R.id.edttime);
		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v1) {
	
				showTimePickerDialog(v1);
			}
		});
		submit = (Button) v.findViewById(R.id.reminder_submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
	
				loadReminders(getActivity());
				getdate = date.getText().toString();
				String gettime = time.getText().toString();
				SimpleDateFormat required = new SimpleDateFormat("dd-MMM-yyyy");
				Date dateshow = new Date();
				
				try {
					dateshow = required.parse(getdate);
				
				} catch (Exception e) {
				
				}

				Date showdate = new Date();
				SimpleDateFormat currentFormat = new SimpleDateFormat(
						"dd-MMM-yyyy", Locale.ENGLISH);
				try {
					showdate = currentFormat.parse(getdate);
				} catch (ParseException e) {
				
					e.printStackTrace();
				}
				SimpleDateFormat required1 = new SimpleDateFormat("dd-MM-yyyy");
				getdisplaydate1 = required1.format(showdate);
				dateandtime = getdisplaydate1 + "  " + gettime;

				spinnerno = spinner_vehicle_no.getSelectedItem().toString();
				spinnertype = sipnner_remindertype.getSelectedItem().toString();
				Log.d(spinnerno, "number get");
				Log.d(spinnertype, "type get");
				if (spinnerno.equalsIgnoreCase("Select Vehicle")) {
					Toast.makeText(getActivity(), "Please select Vehicle.",
							Toast.LENGTH_SHORT).show();
				} else if (spinnertype.equalsIgnoreCase("Reminder type")) {
					Toast.makeText(getActivity(),
							"Please select Reminder Type.", Toast.LENGTH_SHORT)
							.show();
				} else if (getdate != null && getdate.length() == 0) {
					Toast.makeText(getActivity(), "Please select Date.",
							Toast.LENGTH_SHORT).show();
				} else if (isBefore(getToday(), getdate)) {
					Toast.makeText(getActivity(), "Please select Future Date.",
							Toast.LENGTH_SHORT).show();
				} else if (gettime != null && gettime.length() == 0) {
					Toast.makeText(getActivity(), "Please select Time.",
							Toast.LENGTH_SHORT).show();
				} else if (CompareTime(dateandtime)) {
					date.setText("");
					time.setText("");
					Toast.makeText(getActivity(), "Please select Future Time.",
							Toast.LENGTH_SHORT).show();
				} else {

					// Added by Trupti Reddy

					byte[] spinnerNoEncodedBytes = null;
					byte[] spinnerTypeEncodedBytes = null;
					byte[] dateEncodedBytes = null;
					byte[] timeEncodedBytes = null;

					try {
						Cipher c = Cipher.getInstance("AES");
						c.init(Cipher.ENCRYPT_MODE, Constants.secrete_key_spec);
						Log.e("key in insert data",
								Constants.secrete_key_spec.toString());

						Log.e("insert data", "insert data");
						spinnerNoEncodedBytes = c.doFinal(spinnerno.getBytes());
						Log.e("encrypted spinner no", Base64.encodeToString(
								spinnerNoEncodedBytes, Base64.DEFAULT));
						spinnerTypeEncodedBytes = c.doFinal(spinnertype
								.getBytes());
						Log.e("encrypted spinner type", Base64.encodeToString(
								spinnerTypeEncodedBytes, Base64.DEFAULT));
						dateEncodedBytes = c.doFinal(getdate.getBytes());
						Log.e("encrypted date", Base64.encodeToString(
								dateEncodedBytes, Base64.DEFAULT));
						timeEncodedBytes = c.doFinal(gettime.getBytes());
						Log.e("encrypted time", Base64.encodeToString(
								timeEncodedBytes, Base64.DEFAULT));

					} catch (Exception e) {
						Log.e("ERROR", "AES encryption error");
					}

					int result = ReminderDBHelper.insertReminder(Base64
							.encodeToString(spinnerNoEncodedBytes,
									Base64.DEFAULT), Base64.encodeToString(
							spinnerTypeEncodedBytes, Base64.DEFAULT), Base64
							.encodeToString(dateEncodedBytes, Base64.DEFAULT),
							Base64.encodeToString(timeEncodedBytes,
									Base64.DEFAULT), getActivity());

				
					if (result > 0) {
						mTracker.send(new HitBuilders.EventBuilder()
								.setCategory(UserDetails.getUser_id())
								.setAction("thread_true")
								.setLabel("ReminderSet").build());
						Intent intent = new Intent(
								"com.ttl.customersocialapp.ReminderNotificationIntentService");
						int repet = (int) System.currentTimeMillis();
						intent.putExtra("remindertype", spinnerno + " "
								+ spinnertype);
						/** Creating a Pending Intent */
						PendingIntent operation = PendingIntent.getActivity(
								getActivity(), repet, intent,
								Intent.FLAG_ACTIVITY_NEW_TASK);

						AlarmManager alarmManager = (AlarmManager) getActivity()
								.getBaseContext().getSystemService(
										Service.ALARM_SERVICE);

				
						Calendar calnow = Calendar.getInstance();
						calnow.setTime(dateshow);
				
						GregorianCalendar calendar = new GregorianCalendar(
								calnow.get(Calendar.YEAR), calnow
										.get(Calendar.MONTH), calnow
										.get(Calendar.DAY_OF_MONTH), gethour,
								getmin);
						long alarm_time = calendar.getTimeInMillis();

						alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_time,
								operation);

						Toast.makeText(getActivity(),
								"Reminder set successfully", Toast.LENGTH_SHORT)
								.show();

						try {
							ContentResolver cr = getActivity()
									.getContentResolver();
							ContentValues values = new ContentValues();
							values.put(CalendarContract.Events.DTSTART,
									calendar.getTimeInMillis());
							values.put(CalendarContract.Events.DTEND,
									calendar.getTimeInMillis() + 60 * 60 * 1000);
							values.put(CalendarContract.Events.TITLE,
									"TMSC - Reminder");
							values.put(CalendarContract.Events.DESCRIPTION,
									spinnerno + " " + spinnertype);
							values.put(CalendarContract.Events.CALENDAR_ID, 1);
							values.put(CalendarContract.Events.EVENT_TIMEZONE,
									Calendar.getInstance().getTimeZone()
											.getID());

							System.out.println(Calendar.getInstance()
									.getTimeZone().getID());
							Uri uri = cr
									.insert(CalendarContract.Events.CONTENT_URI,
											values);

							// Save the eventId into the Task object for
							// possible future delete.
							long _eventId = Long.parseLong(uri
									.getLastPathSegment());

							setReminder(cr, _eventId, 5);

						} catch (Exception e) {
							e.printStackTrace();
						}
						final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

						// Set Adapter to ExpandableList Adapter
						exlv.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container,
								new Reminder_Fragment()).addToBackStack(null)
								.commit();
					}

				}
			}
		});

		return v;
	}

	public void loadReminders(Context ctx) {
		ReminderDBHelper mDbHelper;
		SQLiteDatabase mdb;

		mDbHelper = new ReminderDBHelper(ctx);
		mdb = mDbHelper.getReadableDatabase();

		insList = new Vector<Reminder_Child>();
		pList = new Vector<Reminder_Parent>();
		Cursor c = mdb.rawQuery("SELECT * FROM " + Reminder.TABLE_NAME, null);

		// added by Trupti Reddy
		// ***
		insList.removeAllElements();
		pList.removeAllElements();
		if (c.moveToFirst()) {

			while (c.isAfterLast() == false) {
				try {
					Cipher cipher = Cipher.getInstance("AES");
					cipher.init(Cipher.DECRYPT_MODE, Constants.secrete_key_spec);
					// Log.e("key in load 1",
					// Constants.secrete_key_spec.toString());
					String name = c.getString(1);
					// Log.e(" from database name reminder :", name);
					byte[] nameBytes = Base64.decode(name, Base64.DEFAULT);
					// Log.e(" byte name ",new String(nameBytes));
					byte[] nameDecodeBytes = cipher.doFinal(nameBytes);
					// Log.e(" byte decoded name ",new String(nameDecodeBytes));

					String type = c.getString(2);
					// Log.e(" from database type :", type);
					byte[] typeBytes = Base64.decode(type, Base64.DEFAULT);
					// Log.e(" byte type ",new String(typeBytes));
					byte[] typeDecodeBytes = cipher.doFinal(typeBytes);
					// Log.e(" byte decoded type ",new String(typeDecodeBytes));

					Reminder_Child i = new Reminder_Child();
					i.setRem_id(c.getString(0));
					// i.setRem_id(new
					// String(cipher.doFinal(Base64.decode(c.getString(0),
					// Base64.DEFAULT))));
					// i.setReminder_type(c.getString(2));
					i.setReminder_type(new String(typeDecodeBytes));
					// i.setRem_vehicleno(c.getString(1));
					i.setRem_vehicleno(new String(nameDecodeBytes));
					// i.setRem_date(c.getString(3));
					i.setRem_date(new String(cipher.doFinal(Base64.decode(
							c.getString(3), Base64.DEFAULT))));
					// i.setRem_time((c.getString(4)));
					i.setRem_time(new String(cipher.doFinal(Base64.decode(
							c.getString(4), Base64.DEFAULT))));

					Reminder_Fragment.insList.add(i);


				} catch (Exception e) {
					e.printStackTrace();
		
				}
				c.moveToNext();
			}
		}
		c.close();

		Cursor reminder_types = mdb.rawQuery(
				"SELECT DISTINCT reminder_type  FROM " + Reminder.TABLE_NAME,
				null);

		if (reminder_types.moveToFirst()) {
			while (reminder_types.isAfterLast() == false) {
				try {
					Cipher cipher = Cipher.getInstance("AES");
					cipher.init(Cipher.DECRYPT_MODE, Constants.secrete_key_spec);





					String rem_type = reminder_types.getString(0);
					byte[] typeBytes = Base64.decode(rem_type, Base64.DEFAULT);
					byte[] typeDecodeBytes = cipher.doFinal(typeBytes);

					Reminder_Parent p = new Reminder_Parent();


					p.setRemindertitle(new String(typeDecodeBytes));

					Reminder_Fragment.pList.add(p);

				} catch (Exception e) {
					e.printStackTrace();

				}
				reminder_types.moveToNext();
			}

		}

		reminder_types.close();
		mdb.close();

		final ArrayList<Reminder_Parent> dummyList = buildDummyData();
		loadHosts(dummyList);
	}

	public void setReminder(ContentResolver cr, long eventID, int timeBefore) {
		try {
			ContentValues values = new ContentValues();
			values.put(CalendarContract.Reminders.MINUTES, timeBefore);
			values.put(CalendarContract.Reminders.EVENT_ID, eventID);
			values.put(CalendarContract.Reminders.METHOD,
					CalendarContract.Reminders.METHOD_ALERT);
			Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
			Cursor c = CalendarContract.Reminders.query(cr, eventID,
					new String[] { CalendarContract.Reminders.MINUTES });
			if (c.moveToFirst()) {
				System.out
						.println("calendar"
								+ c.getInt(c
										.getColumnIndex(CalendarContract.Reminders.MINUTES)));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getActivity().getFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getActivity().getFragmentManager(), "timePicker");
	}

	private class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			// Create a new instance of DatePickerDialog and return it
			DatePickerDialog d = new DatePickerDialog(getActivity(), this,
					year, month, day);
			dp = d.getDatePicker();
			// dp.setMinDate(c.getTimeInMillis());
			c.add(Calendar.DAY_OF_YEAR, 0);
			dp.setMinDate(c.getTimeInMillis());
			return d;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user

			historyDate = day + "-" + (month + 1) + "-" + year;
			SimpleDateFormat required = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat required1 = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat currentFormat = new SimpleDateFormat("dd-MM-yyyy",
					Locale.ENGLISH);
			SimpleDateFormat dbdateFormat = new SimpleDateFormat("MM/dd/yyyy",
					Locale.ENGLISH);
			Date showdate = new Date();
			String displaydate;
			try {
				showdate = currentFormat.parse(historyDate);
				BOOKINGDATE = dbdateFormat.format(showdate);
				displaydate = required.format(showdate);
				getdisplaydate = required1.format(showdate);
				date.setText(displaydate);

			} catch (ParseException e) {

				e.printStackTrace();
			}



		}

	}

	private class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		String aMpM = "AM";
		int currentHour;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));

		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			// edit_servicetime.setText(hourOfDay+":"+minute);

			if (hourOfDay > 11) {
				aMpM = "PM";
			}

			// Make the 24 hour time format to 12 hour time format

			if (hourOfDay > 11) {
				currentHour = hourOfDay - 12;
			} else {
				currentHour = hourOfDay;
			}
			if (minute < 10) {
				historyTime = String.valueOf(currentHour) + ":0"
						+ String.valueOf(minute) + " " + aMpM;
			} else
				historyTime = String.valueOf(currentHour) + ":"
						+ String.valueOf(minute) + " " + aMpM;

			time.setText(historyTime);
			BOOKINGTIME = hourOfDay + ":" + minute;
			gethour = hourOfDay;
			getmin = minute;
		}
	}

	public boolean isBefore(String today, String compareTo) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			Date start = formatter.parse(today);
			Date end = formatter.parse(compareTo);

			if (start.after(end)) {
				return true;
			} else
				return false;
		} catch (ParseException e) {
			
			e.printStackTrace();
	
			return false;
		}
	}

	// ### get today's date
	public String getToday() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.toString());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String today = df.format(c.getTime());
		return today;
	}

	private ArrayList<Reminder_Parent> buildDummyData() {
		// Creating ArrayList of type parent class to store parent class objects
		ArrayList<Reminder_Parent> list = new ArrayList<Reminder_Parent>();
	
		String type = null;
		for (int i = 0; i < pList.size(); i++) {

			
			Reminder_Parent parent = new Reminder_Parent();
			
			parent.setRemindertitle(pList.get(i).remindertitle);
			parent.setChildren(new ArrayList<Reminder_Child>());
			type = pList.get(i).remindertitle;

			// Create Child class object
			for (int j = 0; j < insList.size(); j++) {
				if (type.equalsIgnoreCase(insList.get(j).reminder_type)) {
				
					final Reminder_Child child = new Reminder_Child();

					child.setRem_vehicleno(insList.get(j).rem_vehicleno);
					child.setReminder_type(insList.get(j).reminder_type);
					child.setRem_date(insList.get(j).rem_date + " "
							+ insList.get(j).rem_time);
					parent.getChildren().add(insList.get(j));
				}

			}
			list.add(parent);

		}
		// Add Child class object to parent class object

		return list;
	}

	private void loadHosts(final ArrayList<Reminder_Parent> newParents) {
		if (newParents == null)
			return;

		parents = newParents;

				final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

		// Set Adapter to ExpandableList Adapter
		exlv.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private LayoutInflater inflater;

		public MyExpandableListAdapter() {
			// Create Layout Inflator
			inflater = LayoutInflater.from(getActivity());
		}

		// This Function used to inflate parent rows view

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parentView) {
			final Reminder_Parent parent = parents.get(groupPosition);

			// Inflate grouprow.xml file for parent rows
			convertView = inflater.inflate(R.layout.reminder_parent_row,
					parentView, false);

			// Get grouprow.xml file elements and set values
			if (parent.getRemindertitle().equals("Next Service Date")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.next_service);
			} else if (parent.getRemindertitle().equals("Insurance Renewal")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.insurancealert_vehicle);

			}
			if (parent.getRemindertitle().equals("AMC Purchase/Renewal")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.amc_white);

			} else if (parent.getRemindertitle().equals(
					"Extended Warranty Purchase/Renewal")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.extendedwarranty);

			}
			if (parent.getRemindertitle().equals("PUC Renewal")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.puc);

			} else if (parent.getRemindertitle().equals("Service Booking")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.vehicledetails_1);

			} else if (parent.getRemindertitle().equals("Tyre Change")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.whitetiries);

			} else if (parent.getRemindertitle().equals("Battery Change")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.whitebattery);

			} else if (parent.getRemindertitle().equals(
					"24X7 On Road Assistance Renewal")) {
				convertView.findViewById(R.id.imgcomp).setBackgroundResource(
						R.drawable.wemergency);

			}
			((TextView) convertView.findViewById(R.id.remparenttitle))
					.setText(parent.getRemindertitle());

			if (isExpanded) {
				ImageView img = (ImageView) convertView
						.findViewById(R.id.imgarrow);
				img.setBackgroundResource(R.drawable.downarrow);
			}
			return convertView;
		}

		// This Function used to inflate child rows view
		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parentView) {
			final Reminder_Parent parent = parents.get(groupPosition);
			final Reminder_Child child = parent.getChildren()
					.get(childPosition);

			// Inflate childrow.xml file for child rows
			convertView = inflater.inflate(R.layout.reminder_child_row,
					parentView, false);

			// / Get childrow.xml file elements and set values
			((TextView) convertView.findViewById(R.id.remvehicleno))
					.setText(child.getRem_vehicleno());
			((TextView) convertView.findViewById(R.id.remtype)).setText(child
					.getReminder_type());
			((TextView) convertView.findViewById(R.id.txtdatetime))
					.setText(child.getRem_date() + " " + child.getRem_time());
			ImageView imgdelet = (ImageView) convertView
					.findViewById(R.id.delete);
			imgdelet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
		
					confirmAction(groupPosition + childPosition);
				}
			});

			return convertView;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			Log.i("Childs", groupPosition + "=  getChild ==" + childPosition);
			return parents.get(groupPosition).getChildren().get(childPosition);
		}

		// Call when child row clicked
		@Override
		public long getChildId(int groupPosition, int childPosition) {
		

		

			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			int size = 0;
			if (parents.get(groupPosition).getChildren() != null)
				size = parents.get(groupPosition).getChildren().size();
			return size;
		}

		@Override
		public Object getGroup(int groupPosition) {
		

			return parents.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return parents.size();
		}

		// Call when parent row clicked
		@Override
		public long getGroupId(int groupPosition) {

			return groupPosition;

		}

		@Override
		public void notifyDataSetChanged() {
			// Refresh List rows
			super.notifyDataSetChanged();
		}

		@Override
		public boolean isEmpty() {
			return ((parents == null) || parents.isEmpty());
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

	}

	public int deleteUser(SQLiteDatabase mdb, String deleteMatch,
			String deleteMatch2, int arg3) {
		int opState = 0;

		try {
			int delStatus = mdb.delete(Reminder.TABLE_NAME,
					Reminder.COLUMN_NAME_REMINDER_ID + " ='" + deleteMatch
							+ "'", null);

			if (delStatus != 0) {
				Log.d("Rows Deleted ", " " + delStatus);

				opState = 1;
				return opState;
			} else {
				Log.d("Rows Deleted ", " None");
				opState = 0;
				return opState;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Error ", "Invalid SQL Query");
			opState = 0;
			return opState;

		}

	}

	private void confirmAction(final long arg3) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Delete Reminder ?");

		builder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// TODO

						ReminderDBHelper mDbHelper;
						SQLiteDatabase mdb;

						mDbHelper = new ReminderDBHelper(getActivity());
						mdb = mDbHelper.getReadableDatabase();

						if (deleteUser(mdb,
								insList.get((int) arg3).getRem_id(), insList
										.get((int) arg3).getRem_time(),
								(int) arg3) == 1) {

							loadReminders(getActivity());
							// userNames.remove(position);
							MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

							// Set Adapter to ExpandableList Adapter
							exlv.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();
							FragmentManager fm = getFragmentManager();
							FragmentTransaction tx = fm.beginTransaction();
							tx.replace(R.id.frame_container,
									new Reminder_Fragment())
									.addToBackStack(null).commit();
						}

						Toast.makeText(getActivity(), "Reminder deleted !",
								Toast.LENGTH_SHORT).show();
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/*
	 * public boolean CompareTime(String strTimeToCompare)
	 * 
	 * {
	 * 
	 * Calendar cal = Calendar.getInstance();
	 * 
	 * int dtHour;
	 * 
	 * int dtMin;
	 * 
	 * int iAMPM; int day = 0, month = 0, year = 0;
	 * 
	 * String strAMorPM = null;
	 * 
	 * Date dtCurrentDate;
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm aa");
	 * 
	 * try {
	 * 
	 * Date TimeToCompare = sdf.parse(strTimeToCompare);
	 * 
	 * day = cal.get(Calendar.DAY_OF_MONTH); month = cal.get(Calendar.MONTH);
	 * year = cal.get(Calendar.YEAR);
	 * 
	 * dtMin = cal.get(Calendar.MINUTE);
	 * 
	 * dtHour = cal.get(Calendar.HOUR);
	 * 
	 * iAMPM = cal.get(Calendar.AM_PM);
	 * 
	 * if (iAMPM == 1)
	 * 
	 * {
	 * 
	 * strAMorPM = "PM";
	 * 
	 * }
	 * 
	 * if (iAMPM == 0)
	 * 
	 * {
	 * 
	 * strAMorPM = "AM";
	 * 
	 * }
	 * 
	 * dtCurrentDate = sdf.parse(day + "-" + month + "-" + year + " " + dtHour +
	 * ":" + dtMin + " " + strAMorPM); // Log.d("get current date",
	 * dtCurrentDate + ""); if (dtCurrentDate.after(TimeToCompare))
	 * 
	 * { if (bundle != null) { if(datenext.equalsIgnoreCase(getdate)){
	 * 
	 * Log.d("get after time", dtCurrentDate.after(TimeToCompare) + ""); return
	 * false; }else { Log.d("get after time", dtCurrentDate.after(TimeToCompare)
	 * + ""); return true; } } else { Log.d("get after time",
	 * dtCurrentDate.after(TimeToCompare) + ""); return true; } }
	 * 
	 * if (dtCurrentDate.before(TimeToCompare))
	 * 
	 * { Log.d("get before time", dtCurrentDate.before(TimeToCompare) + "");
	 * return false;
	 * 
	 * }
	 * 
	 * if (dtCurrentDate.equals(TimeToCompare))
	 * 
	 * { Log.d("get equals time", dtCurrentDate.equals(TimeToCompare) + "");
	 * return false;
	 * 
	 * }
	 * 
	 * } catch (ParseException e) {
	 * 
	 * // TODO Auto-generated catch block
	 * 
	 * e.printStackTrace();
	 * 
	 * }
	 * 
	 * return true;
	 * 
	 * }
	 */

	public boolean CompareTime(String strTimeToCompare)

	{

		Calendar cal = Calendar.getInstance();

		int dtHour;

		int dtMin;

		int iAMPM;
		int day = 0, month = 0, year = 0;

		String strAMorPM = null;

		Date dtCurrentDate = null;

		SimpleDateFormat sdf = null;

		try {

			if (Build.VERSION.SDK_INT <= 21) {

				sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm aa");
				Date TimeToCompare = sdf.parse(strTimeToCompare);

				Log.v("date time", TimeToCompare.toString());

				day = cal.get(Calendar.DAY_OF_MONTH);
				month = cal.get(Calendar.MONTH);
				year = cal.get(Calendar.YEAR);

				dtMin = cal.get(Calendar.MINUTE);

				dtHour = cal.get(Calendar.HOUR);

				iAMPM = cal.get(Calendar.AM_PM);

				if (iAMPM == 1)

				{

					strAMorPM = "PM";

				}

				if (iAMPM == 0)

				{

					strAMorPM = "AM";

				}

				dtCurrentDate = sdf.parse(day + "-" + month + "-" + year + " "
						+ dtHour + ":" + dtMin + " " + strAMorPM);

				if (dtCurrentDate.after(TimeToCompare))

				{
					if (bundle != null) {
						if (datenext.equalsIgnoreCase(getdate)) {

							Log.d("get after time",
									dtCurrentDate.after(TimeToCompare) + "");
							return false;
						} else {
							Log.d("get after time",
									dtCurrentDate.after(TimeToCompare) + "");
							return true;
						}
					} else {
						Log.d("get after time",
								dtCurrentDate.after(TimeToCompare) + "");
						return true;
					}
				}

				if (dtCurrentDate.before(TimeToCompare))

				{
					Log.d("get before time",
							dtCurrentDate.before(TimeToCompare) + "");
					return false;

				}

				if (dtCurrentDate.equals(TimeToCompare))

				{
					Log.d("get equals time",
							dtCurrentDate.equals(TimeToCompare) + "");
					return false;

				}

			} else {

				// for api version 6.0 or above

				sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm aa", Locale.US);
				Date TimeToCompare = sdf.parse(strTimeToCompare);

				Log.v("date time", TimeToCompare.toString());

				day = cal.get(Calendar.DAY_OF_MONTH);
				month = cal.get(Calendar.MONTH);
				year = cal.get(Calendar.YEAR);

				dtMin = cal.get(Calendar.MINUTE);

				dtHour = cal.get(Calendar.HOUR);

				iAMPM = cal.get(Calendar.AM_PM);

				if (iAMPM == 1)

				{

					strAMorPM = "PM";

				}

				if (iAMPM == 0)

				{

					strAMorPM = "AM";

				}

				dtCurrentDate = sdf.parse(day + "-" + month + "-" + year + " "
						+ dtHour + ":" + dtMin + " " + strAMorPM);

				if (dtCurrentDate.after(TimeToCompare))

				{
					if (bundle != null) {
						if (datenext.equalsIgnoreCase(getdate)) {

							Log.d("get after time",
									dtCurrentDate.after(TimeToCompare) + "");
							return false;
						} else {
							Log.d("get after time",
									dtCurrentDate.after(TimeToCompare) + "");
							return true;
						}
					} else {
						Log.d("get after time",
								dtCurrentDate.after(TimeToCompare) + "");
						return true;
					}
				}

				if (dtCurrentDate.before(TimeToCompare))

				{
					Log.d("get before time",
							dtCurrentDate.before(TimeToCompare) + "");
					return false;

				}

				if (dtCurrentDate.equals(TimeToCompare))

				{
					Log.d("get equals time",
							dtCurrentDate.equals(TimeToCompare) + "");
					return false;

				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return true;

	}

}
