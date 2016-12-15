package com.ttl.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.ttl.customersocialapp.R;
import com.ttl.model.ComplaintAndJCDescripti;
import com.ttl.model.ServiceHistory;
import com.ttl.webservice.Constants;
import com.ttl.webservice.WebServiceCall;

public class CustomAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ServiceHistory> data;
	private static LayoutInflater inflater = null;

	private ArrayList<ComplaintAndJCDescripti> lst_complaintJC;
	private String[] complainttext = new String[] { "Complaint\nDescription",
			"Customer\nVoice", };
	private String[] jobtext = new String[] { "Job\nDescription", "Job\nStatus", };
	private String[] partstext = new String[] { "Part\nDescription", "Part\nStatus", };

	public Resources res;

	public CustomAdapter(Activity a, ArrayList<ServiceHistory> d,
			Resources resLocal) {
		activity = a;
		data = d;
		res = resLocal;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		
		return data.size();

	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	public static class ViewHolder {

		public TextView dealername, type, date, text_KM, text_contact,
				text_city, text5, text_amount;
		TableLayout tableLayout1, tablelayout2, tablelayout3;
		RelativeLayout rltl_hist;
		ImageView downarrow_hist;
		ImageView uparrow_hist;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder = null;
		Log.i("getview called at", position + "");
		

		

		vi = inflater.inflate(R.layout.item_servicehistory, null);

		
		

		holder = new ViewHolder();
		holder.rltl_hist = (RelativeLayout) vi.findViewById(R.id.rltl_hist);
		holder.dealername = (TextView) vi.findViewById(R.id.text_dealername);
		holder.type = (TextView) vi.findViewById(R.id.text_serv_type);
		holder.date = (TextView) vi.findViewById(R.id.text_date);
		holder.text_KM = (TextView) vi.findViewById(R.id.text_KM);
		holder.text_contact = (TextView) vi.findViewById(R.id.text_contact);
		holder.text5 = (TextView) vi.findViewById(R.id.text5);
		holder.text_city = (TextView) vi.findViewById(R.id.text_city);
		holder.downarrow_hist = (ImageView) vi
				.findViewById(R.id.downarrow_hist);
		holder.uparrow_hist = (ImageView) vi.findViewById(R.id.uparrow_hist);
		holder.text_amount = (TextView) vi.findViewById(R.id.text_amount);

		
		holder.tableLayout1 = (TableLayout) vi.findViewById(R.id.tableLayout1);
		holder.tablelayout2 = (TableLayout) vi.findViewById(R.id.tableLayout2);
		holder.tablelayout3 = (TableLayout) vi.findViewById(R.id.tableLayout3);
		
		
		if (data.size() <= 0) {
			holder.dealername.setText("No Data");

		} else {

		
			holder.dealername
					.setText(data.get(position).getSERVICE_AT_DEALER());
			holder.type.setText(data.get(position).getSR_TYPE());
			holder.text_KM.setText(data.get(position).getODOMTR_RDNG());
			holder.text_contact.setText(data.get(position)
					.getDEALER_CONTACT_NUMBER());
			holder.text_city.setText(data.get(position).getCITY());
		
			holder.text_amount.setText(data.get(position).getINVC_AMT());

		
			if (data.get(position).getSR_TYPE().contains("Free")) {
				holder.rltl_hist.setBackgroundColor(res.getColor(R.color.free));
			} else if (data.get(position).getSR_TYPE().contains("Accident")) {
				holder.rltl_hist.setBackgroundColor(res.getColor(R.color.body));
			} else if (data.get(position).getSR_TYPE().contains("Paid")) {
				holder.rltl_hist.setBackgroundColor(res.getColor(R.color.paid));
			}

			final int pos = position;
			final ViewHolder holder1 = holder;
			holder.downarrow_hist
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							
							getComplaintAndJC(data.get(pos).getCHASSIS_NO(),
									data.get(pos).getSR_HISTORY_NUM(), holder1);
							holder1.downarrow_hist.setVisibility(View.GONE);
							holder1.uparrow_hist.setVisibility(View.VISIBLE);

						}
					});
			holder.uparrow_hist.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					holder1.tableLayout1.removeAllViews();
					holder1.tablelayout2.removeAllViews();
					holder1.tablelayout3.removeAllViews();

					holder1.tableLayout1.setVisibility(View.GONE);
					holder1.tablelayout2.setVisibility(View.GONE);
					holder1.tablelayout3.setVisibility(View.GONE);

					holder1.downarrow_hist.setVisibility(View.VISIBLE);
					holder1.uparrow_hist.setVisibility(View.GONE);

				}
			});
		

			String date = data.get(position).getCLOSE_DATE();
			String dt[] = date.split("T");

			SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH);
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy",
					Locale.ENGLISH);
			Date dateVisible = null;
			String date1 = null;

			try {
				dateVisible = (Date) currentFormat.parse(dt[0]);
				date1 = String.valueOf(sdf1.format(dateVisible));

			} catch (java.text.ParseException e) {
				
				e.printStackTrace();
			}
		
			if (!TextUtils.isEmpty(date1))
				holder.date.setText(date1);
			else
				holder.date.setText("Date not found");
		

		}

		return vi;
	}

	private void getComplaintAndJC(String chassis_NO, String sr_HISTORY_NUM,
			final ViewHolder holder) {
		

		String req1 = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<SOAP:Body>"
				+ "<GetComplaintAndJCDescripti_CSB xmlns=\"src.com.CSB\" preserveSpace=\"no\" qAccess=\"0\" qValues=\"\">"
				+ "<chassis_no>"
				+ chassis_NO
				+ "</chassis_no>"
				+ "<shrecordno>"
				+ sr_HISTORY_NUM
				+ "</shrecordno>"
				+ "</GetComplaintAndJCDescripti_CSB>"
				+ "</SOAP:Body>"
				+ "</SOAP:Envelope>";

		new WebServiceCall(activity, req1,
				Constants.GetComplaintAndJCDescripti_CSB,
				new ResponseCallback() {

					@Override
					public void onResponseReceive(Object object) {
						
						ArrayList<String> complaint_desc = new ArrayList<>();
						ArrayList<String> complaint_status = new ArrayList<>();
						ArrayList<String> complaint_custVoice = new ArrayList<>();
						ArrayList<String> job_desc = new ArrayList<>();
						ArrayList<String> job_status = new ArrayList<>();
						ArrayList<String> part_desc = new ArrayList<>();
						ArrayList<String> part_status = new ArrayList<>();
						lst_complaintJC = (ArrayList<ComplaintAndJCDescripti>) object;
						for (int i = 0; i < lst_complaintJC.size(); i++) {
						
							if (lst_complaintJC.get(i).RECORD_TYPE
									.equals("COMPLAINT_CODE")) {
								Log.i("ITEM getComplaintAndJC1 ",
										lst_complaintJC.get(i).RECORD_TYPE
												+ " "
												+ lst_complaintJC.get(i).C_J_P_DESCRTPTION
												+ " "
												+ lst_complaintJC.get(i).C_J_P_STATUS);
								complaint_desc.add(lst_complaintJC.get(i).C_J_P_DESCRTPTION);
								complaint_status.add(lst_complaintJC.get(i).C_J_P_STATUS);
								complaint_custVoice.add(lst_complaintJC.get(i).CUSTOMER_VOICE);

							} else if (lst_complaintJC.get(i).RECORD_TYPE
									.equals("JOB_CODE")) {
								Log.i("ITEM getComplaintAndJC2 ",
										lst_complaintJC.get(i).RECORD_TYPE
												+ " "
												+ lst_complaintJC.get(i).C_J_P_DESCRTPTION
												+ " "
												+ lst_complaintJC.get(i).C_J_P_STATUS);
								job_desc.add(lst_complaintJC.get(i).C_J_P_DESCRTPTION);
								job_status.add(lst_complaintJC.get(i).C_J_P_STATUS);
							} else if (lst_complaintJC.get(i).RECORD_TYPE
									.equals("SPARE_PARTS")) {
								Log.i("ITEM getComplaintAndJC3 ",
										lst_complaintJC.get(i).RECORD_TYPE
												+ " "
												+ lst_complaintJC.get(i).C_J_P_DESCRTPTION
												+ " "
												+ lst_complaintJC.get(i).C_J_P_STATUS);
								part_desc.add(lst_complaintJC.get(i).C_J_P_DESCRTPTION);
								part_status.add(lst_complaintJC.get(i).C_J_P_STATUS);
							}

						}
						if (complaint_desc.size() > 0
								|| complaint_custVoice.size() > 0) {
							holder.tableLayout1.setVisibility(View.VISIBLE);
							BuildTable(complainttext, complaint_desc,
									complaint_custVoice, holder.tableLayout1);
						}

						if (job_desc.size() > 0 || job_status.size() > 0) {
							holder.tablelayout2.setVisibility(View.VISIBLE);
							BuildTable(jobtext, job_desc, job_status,
									holder.tablelayout2);
						}

						if (part_desc.size() > 0 || part_status.size() > 0) {
							holder.tablelayout3.setVisibility(View.VISIBLE);
							BuildTable(partstext, part_desc, part_status,
									holder.tablelayout3);
						}
					}

					@Override
					public void onErrorReceive(String string) {
						
						Toast.makeText(activity, string, Toast.LENGTH_SHORT)
								.show();
					}
				}, "Getting JOB CARD Description..").execute();
	}

	private void BuildTable(String[] complainttext,
			ArrayList<String> complaint_desc,
			ArrayList<String> complaint_status, TableLayout tableLayout1) {

		
		
		boolean firstclick = true;
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		
		TableRow.LayoutParams lp = new TableRow.LayoutParams(width / 2,
				TableRow.LayoutParams.MATCH_PARENT);
		lp.weight = 1;

		if (firstclick) {

			TableRow tbrow0 = new TableRow(activity);
			for (int i = 0; i < 2; i++) {
				TextView tv0 = new TextView(activity);
				tv0.setText(complainttext[i]);
				tv0.setTextColor(Color.WHITE);
				tv0.setLayoutParams(lp);
				tv0.setTextSize(15);
				tv0.setGravity(Gravity.CENTER_HORIZONTAL);
				tv0.setPadding(10, 5, 5, 10);
		

				tv0.setBackgroundDrawable(res
						.getDrawable(R.drawable.grey_border_blackrectangle));
		
				tbrow0.addView(tv0);
			}
			tableLayout1.addView(tbrow0);
			firstclick = false;
		}

		for (int i = 0; i < complaint_desc.size(); i++) {
			TableRow tbrow = new TableRow(activity);
			TextView tv = new TextView(activity);
			tv.setText(complaint_desc.get(i));
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(13);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setPadding(10, 5, 5, 10);
			tv.setLayoutParams(lp);
			// tv.setBackgroundColor(Color.WHITE);
			tv.setBackgroundDrawable(res
					.getDrawable(R.drawable.grey_border_rectangle));
			tbrow.addView(tv);

			TextView tv1 = new TextView(activity);
			tv1.setText(complaint_status.get(i));
			tv1.setTextColor(Color.BLACK);
			tv1.setTextSize(13);
			tv1.setGravity(Gravity.CENTER_HORIZONTAL);
			tv1.setPadding(20, 20, 20, 20);
			tv1.setWidth(100);
			tv1.setLayoutParams(lp);
		
			tv1.setBackgroundDrawable(res
					.getDrawable(R.drawable.grey_border_rectangle));
			tbrow.addView(tv1);

			tableLayout1.addView(tbrow);
		}

		

	}

}
