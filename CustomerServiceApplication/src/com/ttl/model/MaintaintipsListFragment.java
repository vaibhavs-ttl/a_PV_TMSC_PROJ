package com.ttl.model;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ttl.customersocialapp.MaintenanceTipsFragment;
import com.ttl.customersocialapp.R;

public class MaintaintipsListFragment extends Fragment {

	String SpinerSelected;
	Spinner spinner;


	String[] taskname = { "PRECAUTION", "STEERING", "BRAKES", "BATTERY",
			"ENGINE", "TYRES", "EXTERIOR CARE", "INTERIOR CARE",
			"TROUBLE SHOOTING", "TIPS & HYGIENE", "SAFETY", "WIPERS",
			"ELECTRICAL", "ALERTS & INDICATIONS", "PARTS" };
	String value;
	int arr_images[] = { R.drawable.whiteprecautions, R.drawable.whitesteering,
			R.drawable.whitebreak, R.drawable.whitebattery,
			R.drawable.whiteengine, R.drawable.whitetiries,
			R.drawable.whiteexteriorcare, R.drawable.whiteinteriorcare,
			R.drawable.whitetroubleshooting, R.drawable.whitetipshygiene,
			R.drawable.whitesafety, R.drawable.whitewipers,
			R.drawable.whiteelectrical, R.drawable.whitealertsindications,
			R.drawable.whiteshocks };

	View rootView;
	Fragment fragment = null;
	ListView lv, dialoglist, enginedialoglist;
	TextView dialogtitle, enginedialogtitle;
	ImageView dialogimg, dialogimgclose, enginedialogimg, enginedialogimgclose,
			enginedialogimgg, dialogimg1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_maintain_list, container,
				false);

		value = getArguments().getString("CID");

		spinner = (Spinner) rootView.findViewById(R.id.spinner1);
		spinner.setAdapter(new MyAdapter(rootView.getContext(),
				R.layout.maintain_list_spinner_row, taskname));

		lv = (ListView) rootView.findViewById(R.id.listview);


		for (int i = 0; i < spinner.getCount(); i++) {
			if (spinner.getItemAtPosition(i).equals(value)) {
				spinner.setSelection(i);

				break;
			}
		}



		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				SpinerSelected = parent.getItemAtPosition(position).toString();
				switch (position) {
				case 0:
					set_to_adapter(MaintaintanceList_StringArray.precautions);

					break;

				case 1:
					set_to_adapter(MaintaintanceList_StringArray.steering);
					break;

				case 2:
					set_to_adapter(MaintaintanceList_StringArray.brakes);
					break;

				case 3:
					set_to_adapter(MaintaintanceList_StringArray.battary);
					break;

				case 4:
					set_to_adapter(MaintaintanceList_StringArray.engine);
					break;

				case 5:
					set_to_adapter(MaintaintanceList_StringArray.tyres);
					break;
				case 6:
					set_to_adapter(MaintaintanceList_StringArray.exterior_care);
					break;

				case 7:
					set_to_adapter(MaintaintanceList_StringArray.interior_care);
					break;

				case 8:
					set_to_adapter(MaintaintanceList_StringArray.trouble_shooting);
					break;
				case 9:
					set_to_adapter(MaintaintanceList_StringArray.tips);
					break;
				case 10:
					set_to_adapter(MaintaintanceList_StringArray.safety_tips);
					break;
				case 11:
					set_to_adapter(MaintaintanceList_StringArray.wipers);
					break;
				case 12:
					set_to_adapter(MaintaintanceList_StringArray.electrical);
					break;
				case 13:

					set_to_adapter(MaintaintanceList_StringArray.not_found);
					break;
				case 14:
					set_to_adapter(MaintaintanceList_StringArray.shocks);
					break;
				default:
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {


			}
		});



		final Dialog dialog = new Dialog(rootView.getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_popup);
		dialogimgclose = (ImageView) dialog.findViewById(R.id.imgclose);
		dialogtitle = (TextView) dialog.findViewById(R.id.dialogtitle);
		dialoglist = (ListView) dialog.findViewById(R.id.dialoglist);
		dialogimg = (ImageView) dialog.findViewById(R.id.dialogimg);

		final Dialog enginedialog = new Dialog(rootView.getContext());
		enginedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		enginedialog.setContentView(R.layout.engine_popup);
		enginedialogimgclose = (ImageView) enginedialog
				.findViewById(R.id.engineimgclose);
		enginedialogtitle = (TextView) enginedialog
				.findViewById(R.id.enginedialogtitle);
		enginedialoglist = (ListView) enginedialog
				.findViewById(R.id.enginedialoglist);
		enginedialogimg = (ImageView) enginedialog
				.findViewById(R.id.enginedialogimg);
		enginedialogimgg = (ImageView) enginedialog
				.findViewById(R.id.enginedialogimgg);



		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (SpinerSelected.equals("PRECAUTION")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.precautions);

						set_to_dialogadapter(MaintaintanceList_StringArray.precautions_info);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("STEERING")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.steeringfluid);

						set_to_dialogadapter(MaintaintanceList_StringArray.steering_info);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("BRAKES")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.brake);

						set_to_dialogadapter(MaintaintanceList_StringArray.brakes_info);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("BATTERY")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.increasebattery);

						set_to_dialogadapter(MaintaintanceList_StringArray.increase_thelifeof_battery);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("ENGINE")) {
					if (position == 0) {
						enginedialogtitle.setText(parent.getItemAtPosition(
								position).toString());
						enginedialogimg
								.setBackgroundResource(R.drawable.engineoillevel);
						enginedialogimgg
								.setBackgroundResource(R.drawable.engineoillevell);
						set_to_enginedialogadapter(MaintaintanceList_StringArray.engine_oil_level);
						enginedialog.show();
					} else if (position == 1) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.enginecoolantlevel_new);

						set_to_dialogadapter(MaintaintanceList_StringArray.engine_coolant_level);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("TYRES")) {
					if (position == 0) {
						enginedialogtitle.setText(parent.getItemAtPosition(
								position).toString());
						enginedialogimg
								.setBackgroundResource(R.drawable.tyersuse);
						enginedialogimgg
								.setBackgroundResource(R.drawable.tyrerotation);

						set_to_enginedialogadapter(MaintaintanceList_StringArray.prolonging_tyre_life);
						enginedialog.show();
					}
				}

				if (SpinerSelected.equals("EXTERIOR CARE")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);
						set_to_dialogadapter(MaintaintanceList_StringArray.body_exterior_care);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("INTERIOR CARE")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);
						set_to_dialogadapter(MaintaintanceList_StringArray.interior_care);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("TROUBLE SHOOTING")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);
						set_to_dialogadapter(MaintaintanceList_StringArray.engine_not_cranking);
						dialog.show();
					} else if (position == 1) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);
						set_to_dialogadapter(MaintaintanceList_StringArray.enginecranks_butdoes_notstart);
						dialog.show();
					} else if (position == 2) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);
						set_to_dialogadapter(MaintaintanceList_StringArray.nonfunctional_electrical_accessories);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("TIPS & HYGIENE")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.gooddriving);

						set_to_dialogadapter(MaintaintanceList_StringArray.good_driving);
						dialog.show();
					} else if (position == 1) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.fuelsavingtips);
						set_to_dialogadapter(MaintaintanceList_StringArray.fuel_saving_tips);
						dialog.show();
					}
					if (position == 2) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);
						set_to_dialogadapter(MaintaintanceList_StringArray.do_s);
						dialog.show();
					} else if (position == 3) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);
						set_to_dialogadapter(MaintaintanceList_StringArray.dont_s);
						dialog.show();
					} else if (position == 4) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);
						set_to_dialogadapter(MaintaintanceList_StringArray.in_extended_use);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("SAFETY")) {
					if (position == 1) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.seatbelt);

						set_to_dialogadapter(MaintaintanceList_StringArray.seat_belt);
						dialog.show();
					} else if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.avoidhighspeed);

						set_to_dialogadapter(MaintaintanceList_StringArray.avoid_highspeeds_shortcuts);
						dialog.show();
					} else if (position == 2) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.infuenceofalcohol);

						set_to_dialogadapter(MaintaintanceList_StringArray.influence_of_alcohol);
						dialog.show();
					} else if (position == 3) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.nonuseofmobile);

						set_to_dialogadapter(MaintaintanceList_StringArray.nonuse_of_mobilephones);
						dialog.show();
					} else if (position == 4) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.advancewarning);

						set_to_dialogadapter(MaintaintanceList_StringArray.advance_warning_triangle);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("WIPERS")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.wiper);

						set_to_dialogadapter(MaintaintanceList_StringArray.maintaining_of_wipers);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("ELECTRICAL")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.GONE);

						set_to_dialogadapter(MaintaintanceList_StringArray.electrical_systems);
						dialog.show();
					}
				}

				if (SpinerSelected.equals("ALERTS & INDICATIONS")) {

					if (position == 0) {

						Toast.makeText(getActivity(), "Sorry Data in WIP",
								Toast.LENGTH_SHORT).show();
					/*} else if (position == 1) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());

						set_to_dialogadapter(MaintaintanceList_StringArray.dont_s);
						dialog.show();
					} else if (position == 2) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());

						set_to_dialogadapter(MaintaintanceList_StringArray.in_extended_use);
						dialog.show();*/
					}

				}

				if (SpinerSelected.equals("PARTS")) {
					if (position == 0) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.engineoilfilter);

						set_to_dialogadapter(MaintaintanceList_StringArray.engine_oil_filter);
						dialog.show();
					}

					else if (position == 1) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.timingbelt);

						set_to_dialogadapter(MaintaintanceList_StringArray.timing_belt);
						dialog.show();
					}

					else if (position == 2) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.drivebelts);

						set_to_dialogadapter(MaintaintanceList_StringArray.drive_belts);
						dialog.show();
					}

					else if (position == 3) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.coolingsystem);

						set_to_dialogadapter(MaintaintanceList_StringArray.cooling_system);
						dialog.show();
					} else if (position == 4) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						// remove image visibility if added
						dialogimg.setVisibility(View.GONE);

						set_to_dialogadapter(MaintaintanceList_StringArray.exhaustpipe_catalytic_converters);
						dialog.show();
					} else if (position == 5) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.sparkplugs);

						set_to_dialogadapter(MaintaintanceList_StringArray.spark_plugs);
						dialog.show();
					} else if (position == 6) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.aircleaner);

						set_to_dialogadapter(MaintaintanceList_StringArray.air_cleaner);
						dialog.show();
					} else if (position == 7) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.fuelfilter);

						set_to_dialogadapter(MaintaintanceList_StringArray.fuel_filter);
						dialog.show();
					} else if (position == 8) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg
								.setBackgroundResource(R.drawable.brakecomponent);

						set_to_dialogadapter(MaintaintanceList_StringArray.brake_component);
						dialog.show();
					} else if (position == 9) {
						dialogtitle.setText(parent.getItemAtPosition(position)
								.toString());
						dialogimg.setVisibility(rootView.VISIBLE);
						dialogimg.setBackgroundResource(R.drawable.engine_oil);

						set_to_dialogadapter(MaintaintanceList_StringArray.engine_oil);
						dialog.show();
					}

				}

			}
		});

		// Close Custom Dialog Box

		dialogimgclose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		enginedialogimgclose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				enginedialog.dismiss();
			}
		});

		rootView.getRootView().setFocusableInTouchMode(true);
		rootView.getRootView().requestFocus();

		rootView.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container,
								new MaintenanceTipsFragment()).commit();
						return true;
					}
				}
				return false;
			}
		});
		return rootView;

	}

	private void set_to_adapter(String[] arry) {

		lv.setAdapter(new ListAdapter(rootView.getContext(),
				R.layout.maintainlist_listview_row, arry));
	}

	private void set_to_enginedialogadapter(String[] arry) {

		enginedialoglist.setAdapter(new DialogAdapter(rootView.getContext(),
				R.layout.dialoglist_row, arry));
	}

	private void set_to_dialogadapter(String[] arry) {

		dialoglist.setAdapter(new DialogAdapter(rootView.getContext(),
				R.layout.dialoglist_row, arry));
	}

	// Spinner Adapter

	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// LayoutInflater inflater= getContext().getLayoutInflater();
			View row = inflater.inflate(R.layout.maintain_list_spinner_row,
					parent, false);
			TextView label = (TextView) row.findViewById(R.id.company);
			label.setText(taskname[position]);

			ImageView icon = (ImageView) row.findViewById(R.id.image);
			icon.setImageResource(arr_images[position]);
			return row;
		}
	}

	// ListView Adapter

	public class ListAdapter extends ArrayAdapter<String> {

		String[] arr;

		public ListAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			arr = objects;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {

				LayoutInflater li = LayoutInflater.from(rootView.getContext());
				v = li.inflate(R.layout.maintainlist_listview_row, parent,
						false);
			}

			TextView t1 = (TextView) v.findViewById(R.id.txt);
			t1.setText(arr[position]);

			return v;
		}

	}

	

	public class DialogAdapter extends ArrayAdapter<String> {
		String[] arr;

		public DialogAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			arr = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {

				LayoutInflater li = LayoutInflater.from(rootView.getContext());
				v = li.inflate(R.layout.dialoglist_row, parent, false);
			}

			TextView t1 = (TextView) v.findViewById(R.id.txtinfo);

			TextView txtdot = (TextView) v.findViewById(R.id.txtdot);

			t1.setText(arr[position]);

			if (t1.getText().toString().startsWith("Probable Cause:")
					|| t1.getText().toString().startsWith("Action be Taken:")
					|| t1.getText().toString().startsWith("Note:")) {
				txtdot.setVisibility(View.GONE);
				t1.setTypeface(null, Typeface.ITALIC);
			}

			return v;
		}
	}

}
