package com.ttl.customersocialapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ttl.helper.AnalyticsApplication;
import com.ttl.model.UserDetails;

public class DocumentUploadFragment extends Fragment implements
OnItemSelectedListener {
	View rootView;
	final int CAMERA_CAPTURE = 1, SELECT_FILE = 2,CROP_IMAGE=3;
	LinearLayout license, insurance, puc, passport, pancard, voterid, aadhar,vehicalpicture, others;
	private String license_filename,insurance_filename,puc_filename,passport_filename,pancard_filename
	,voterid_filename,aadhar_filename,vehicalpicture_filename,others_filename;

	
	private GallaryImage gallaryImage;
	private ArrayList<String> RealImagePath=new ArrayList<>();
	
	public DocumentUploadFragment() {


	}

	// for creating folders
	
	public static  String license_path = null;
	public static  String insurance_path = null;
	public static  String puc_path = null;
	public static  String pancard_path = null;
	public static  String voterid_path = null;
	public static  String passport_path = null;
	public static  String aadhar_path = null;
	public static  String vehicalpicture_path = null;
	public static  String others_path=null;
	
	// for creating sub folders
	
	public static String license_Thumbpath=null;
	//public static String insurance_Thumbpath=null;
	//public static String puc_Thumbpath=null;
	public static String passport_Thumbpath=null;
	public static String pancard_Thumbpath=null;
	public static String voterid_Thumbpath=null;
	public static String aadhar_Thumbpath=null;
	//public static String vehicalpicture_Thumbpath=null;
	public static String others_Thumbpath=null;
	
	
	private GridView grid;
	private List<String> listOfImagesPath;
	private File[] files;
	private Spinner regno;
	private LinearLayout lindocs;
	private ImageView imgadd;

	private SharedPreferences settings;
	boolean licensechk = false, insurancechk = false, pucchk = false,
			passportchk = false, panchk = false, voteridchk = false,
			aadharchk = false, vehicalpicturechk = false, otherschk = false;
	private Editor edit;
	private RelativeLayout relspinner, relimagelayout;
	private ImageListAdapter imageaptor;
	private ImageView imageView;
	private TextView title;
	private boolean chkitemvalue = false,
			insuranceitemclick = false, pucitemclick = false,
			vehicalitemclick = false;
	private List<String> regnovalues = new ArrayList<String>();
	private String pathinsurance, pucpath, vehicalpicturepath;
	private String selregno;
	private Uri artUri;
	private File license_pathfile,pucpathfile,pathinsurancefile,passport_pathfile,pancard_pathfile,voterid_pathfile,aadhar_pathfile,vehicalpicturepathfile,others_pathfile;
	private Tracker mTracker;
	private AppPrefs prefs;
	@Override
	public void onStart() {

		super.onStart();
		mTracker.setScreenName("MyDocumentScreen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			
			
			prefs=new AppPrefs(getActivity());
			
			if(prefs.getLoginStatus()==null || !prefs.getLoginStatus().equalsIgnoreCase("n"))
			{
				new AppDocument(getActivity()).execute();	
				Log.e("App document", "first time login");
				prefs.setLoginStatus("n");
			}
			else
			{
				Log.e("App document", "else part executed");
			}
	
			
			pathBuilder(); // creates file path 
			createDirectories(); // creates directories
	
			
		} catch (Exception e) {
			Log.v("exception", e.toString());
		}


		rootView = inflater.inflate(R.layout.fragment_documentupload,
				container, false);
		relimagelayout = (RelativeLayout) rootView.findViewById(R.id.relimg);
		relimagelayout.setVisibility(View.GONE);
		relspinner = (RelativeLayout) rootView.findViewById(R.id.relspinner);
		relspinner.setVisibility(View.GONE);
		settings = PreferenceManager.getDefaultSharedPreferences(rootView
				.getContext());
		edit = settings.edit();
		title = (TextView) rootView.findViewById(R.id.txtdoctitle);



		//tracker
		AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
		mTracker = application.getDefaultTracker();
		regno = (Spinner) rootView.findViewById(R.id.spnregno);
		lindocs = (LinearLayout) rootView.findViewById(R.id.lindocs);
		if (new UserDetails().getRegNumberList().size() == 0) {
			FragmentManager fragmentManager = getFragmentManager();
			Fragment fragment = new HomeFragment();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		}
		regnovalues.add("Select Vehicle");
		int size = new UserDetails().getRegNumberList().size();

		for (int i = 0; i < size; i++) {
			if(!(new UserDetails().getRegNumberList().get(i)
					.get("registration_num").toString().equals("")))
			{
				regnovalues.add(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"));
			}else
			{
				regnovalues.add(new UserDetails().getRegNumberList().get(i)
						.get("chassis_num"));
			}
			pathinsurance = pathinsurance + i;
			
		}
		ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinnertext, regnovalues);
		sp_adapter.setDropDownViewResource(R.layout.spinner_selector);
		regno.setAdapter(sp_adapter);
		regno.setOnItemSelectedListener(this);

		license = (LinearLayout) rootView.findViewById(R.id.btnlicense);
		insurance = (LinearLayout) rootView.findViewById(R.id.btninsurance);
		puc = (LinearLayout) rootView.findViewById(R.id.btnpuc);
		passport = (LinearLayout) rootView.findViewById(R.id.btnpassport);
		pancard = (LinearLayout) rootView.findViewById(R.id.btnpancard);
		voterid = (LinearLayout) rootView.findViewById(R.id.btnvoterid);
		aadhar = (LinearLayout) rootView.findViewById(R.id.btnaadhar);
		vehicalpicture = (LinearLayout) rootView
				.findViewById(R.id.btnvehicalpicture);
		others = (LinearLayout) rootView.findViewById(R.id.btnothers);

		license.setOnClickListener(buttonClickListener);
		insurance.setOnClickListener(buttonClickListener);
		puc.setOnClickListener(buttonClickListener);
		passport.setOnClickListener(buttonClickListener);
		pancard.setOnClickListener(buttonClickListener);
		voterid.setOnClickListener(buttonClickListener);
		aadhar.setOnClickListener(buttonClickListener);
		vehicalpicture.setOnClickListener(buttonClickListener);
		others.setOnClickListener(buttonClickListener);

		imgadd = (ImageView) rootView.findViewById(R.id.imgadd);
		imgadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (relspinner.getVisibility() == View.VISIBLE) {
					if (selregno.equalsIgnoreCase("Select Vehicle")) {
						insuranceitemclick = false;
						pucitemclick = false;
						vehicalitemclick = false;
						grid.setAdapter(null);
					}
					if (insuranceitemclick == true || pucitemclick == true
							|| vehicalitemclick == true) {
						selectImage();

					} else {
						Toast.makeText(getActivity(),
								"Please Select Vehicle",
								Toast.LENGTH_SHORT).show();
						insuranceitemclick = false;
						pucitemclick = false;
						vehicalitemclick = false;
					}

				} else {
					selectImage();
				}
			}
		});

		rootView.getRootView().setFocusableInTouchMode(true);
		rootView.getRootView().requestFocus();

		rootView.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if (lindocs.getVisibility() == View.VISIBLE) {
							FragmentManager fm = getFragmentManager();
							FragmentTransaction tx = fm.beginTransaction();
							tx.replace(R.id.frame_container, new HomeFragment())
							.commit();
						} else {
							relimagelayout.setVisibility(View.GONE);
							lindocs.setVisibility(View.VISIBLE);
							insuranceitemclick = false;
							pucitemclick = false;
							vehicalitemclick = false;
						}
						return true;
					}
				}
				return false;
			}
		});
		grid = (GridView) rootView.findViewById(R.id.gridviewimg);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v,
					final int postion, long arg3) {
				RealImagePath=getRealImages();
				final Dialog dialog = new Dialog(v.getContext());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.imagview_popup);
				Button delete = (Button) dialog.findViewById(R.id.btndelet);
				delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						File real_image_check;
						
						// Added by Vaibhav Sharma
						
							
/*							if(RealImagePath.size()>0)
							{
								
								real_image_check=new File(RealImagePath.get(postion));
								
						
							if(real_image_check.exists())
							{
								if(real_image_check.delete())
								{
								
								RealImagePath.remove(postion);
								Log.v("real file deleted", RealImagePath.get(postion));	
								}
							
								imageaptor.notifyDataSetChanged();
							}
							
							}
							
							if(listOfImagesPath.size()>0)
							{
							
								real_image_check=new File(listOfImagesPath.get(postion));
								
							if(real_image_check.exists())
									{
											real_image_check=new File(listOfImagesPath.get(postion));
								
										if(real_image_check.delete())
										{
												listOfImagesPath.remove(postion);
											Log.v("thumb file deleted", listOfImagesPath.get(postion));
											
											imageaptor.notifyDataSetChanged();
										}
									
									
									
									}		
								
								
								
							}
						*/
					
						
						try {
							if(new File(listOfImagesPath.get(postion)).exists() && new File(listOfImagesPath.get(postion)).delete())
							{
								listOfImagesPath.remove(postion);
								Log.v("Thumb deleted", "Deleted");
								imageaptor.notifyDataSetChanged();
							}
						
							if(new File(RealImagePath.get(postion)).exists() && new File(RealImagePath.get(postion)).delete())
							{
								
								RealImagePath.remove(postion);
								Log.v("Real image deleted","deleted");
								imageaptor.notifyDataSetChanged();
							}
							
						} catch (Exception e) {
						
							Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
							
						}
						
						
						
						
						

						dialog.cancel();
					}
				});
				Button view = (Button) dialog.findViewById(R.id.btnview);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						// Added by Vaibhav Sharma
						
					/*	Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.parse("file://" + RealImagePath.get(postion)),"image/*");
						startActivity(intent);*/
						
						if(RealImagePath.size()>0)
						{
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.parse("file://" + RealImagePath.get(postion)),"image/*");
							startActivity(intent);
						}
						else
						{
							Toast.makeText(getActivity(), "Image does not exist!!", Toast.LENGTH_LONG).show();
						}
						
						dialog.cancel();
					}
				});
				dialog.show();
			}
		});
		return rootView;
	}

	final OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {

			case R.id.btnlicense:
				

				
				Log.v("bool values ", " insurance "+insurancechk+" puc "+pucchk);
				
				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.GONE);
				
				licensechk = true;
				
				insurancechk=false;
				passportchk=false;
				pucchk=false;
				panchk=false;
				aadharchk=false;
				vehicalpicturechk=false;
				otherschk=false;
				voteridchk=false;
				
				
				lindocs.setVisibility(View.GONE);
				title.setText("LICENSE");
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();  // Added by Vaibhav Sharma
				//RetriveCapturedImagePath();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),
							listOfImagesPath);
					grid.setAdapter(imageaptor);
					
				}

				break;

			case R.id.btninsurance:

				regno.setSelection(0);
				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.VISIBLE);
				insurancechk = true;
				
				licensechk = false;
				pucchk=false;
				panchk=false;
				aadharchk=false;
				vehicalpicturechk=false;
				otherschk=false;
				voteridchk=false;
				passportchk=false;
				
				lindocs.setVisibility(View.GONE);
				title.setText("INSURANCE");
				if (chkitemvalue == true) {
					chkitemvalue = false;
					listOfImagesPath = null;
					listOfImagesPath = loadThumbs();
					if (listOfImagesPath != null) {
						imageaptor = new ImageListAdapter(getActivity(),
								listOfImagesPath);
						grid.setAdapter(imageaptor);
						imageaptor.notifyDataSetChanged();
					}
				}
				break;
			case R.id.btnpuc:

				regno.setSelection(0);
				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.VISIBLE);
				pucchk = true;
				insurancechk = false;
				licensechk = false;
				panchk=false;
				aadharchk=false;
				vehicalpicturechk=false;
				otherschk=false;
				voteridchk=false;
				passportchk=false;				
				title.setText("PUC");
				lindocs.setVisibility(View.GONE);
				if (chkitemvalue = true) {
					chkitemvalue = false;
					listOfImagesPath = null;
					listOfImagesPath = loadThumbs();
					if (listOfImagesPath != null) {
						imageaptor = new ImageListAdapter(getActivity(),
								listOfImagesPath);
						grid.setAdapter(imageaptor);
						imageaptor.notifyDataSetChanged();
					}
				}
				break;
			case R.id.btnpassport:
				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.GONE);
				
				passportchk=true;
				pucchk = false;
				insurancechk = false;
				licensechk = false;
				panchk=false;
				aadharchk=false;
				vehicalpicturechk=false;
				otherschk=false;
				voteridchk=false;
				title.setText("PASSPORT");
				lindocs.setVisibility(View.GONE);
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),
							listOfImagesPath);
					grid.setAdapter(imageaptor);
					imageaptor.notifyDataSetChanged();
				}
				break;
			case R.id.btnpancard:
				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.GONE);
				panchk=true;
				passportchk=false;
				pucchk = false;
				insurancechk = false;
				licensechk = false;
				aadharchk=false;
				vehicalpicturechk=false;
				otherschk=false;
				voteridchk=false;				
				title.setText("PANCARD");
				lindocs.setVisibility(View.GONE);
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),
							listOfImagesPath);
					grid.setAdapter(imageaptor);
				}

				break;
			case R.id.btnvoterid:
				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.GONE);
				voteridchk=true;
				panchk=false;
				passportchk=false;
				pucchk = false;
				insurancechk = false;
				licensechk = false;
				aadharchk=false;
				vehicalpicturechk=false;
				otherschk=false;
				title.setText("VOTER ID");
				lindocs.setVisibility(View.GONE);
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),listOfImagesPath);
					grid.setAdapter(imageaptor);
				}

				break;
			case R.id.btnaadhar:
				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.GONE);
				aadharchk=true;
				voteridchk=false;
				panchk=false;
				passportchk=false;
				pucchk = false;
				insurancechk = false;
				licensechk = false;
				vehicalpicturechk=false;
				otherschk=false;
				
				title.setText("AADHAR");
				lindocs.setVisibility(View.GONE);
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),
							listOfImagesPath);
					grid.setAdapter(imageaptor);
				}

				break;
			case R.id.btnvehicalpicture:

				regno.setSelection(0);
				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.VISIBLE);
				
				vehicalpicturechk=true;
				aadharchk=false;
				voteridchk=false;
				panchk=false;
				passportchk=false;
				pucchk = false;
				insurancechk = false;
				licensechk = false;
				otherschk=false;
				
				
				
				title.setText("VEHICLE PICTURE");
				lindocs.setVisibility(View.GONE);
				if (chkitemvalue = true) {
					chkitemvalue = false;
					listOfImagesPath = null;
					listOfImagesPath = loadThumbs();
					if (listOfImagesPath != null) {
						imageaptor = new ImageListAdapter(getActivity(),
								listOfImagesPath);
						grid.setAdapter(imageaptor);
						imageaptor.notifyDataSetChanged();
					}
				}
				break;
			case R.id.btnothers:
				/*relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.GONE);
				
				otherschk=true;
				vehicalpicturechk=false;
				aadharchk=false;
				voteridchk=false;
				panchk=false;
				passportchk=false;
				pucchk = false;
				insurancechk = false;
				licensechk = false;
				
				
				title.setText("OTHER");
				lindocs.setVisibility(View.GONE);
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),
							listOfImagesPath);
					grid.setAdapter(imageaptor);
				}*/

				relimagelayout.setVisibility(View.VISIBLE);
				relspinner.setVisibility(View.GONE);
			
				otherschk=true;
				licensechk = false;
				insurancechk=false;
				passportchk=false;
				pucchk=false;
				panchk=false;
				aadharchk=false;
				vehicalpicturechk=false;
				voteridchk=false;
				
				
				lindocs.setVisibility(View.GONE);
				title.setText("OTHER");
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();  // Added by Vaibhav Sharma
				//RetriveCapturedImagePath();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),
							listOfImagesPath);
					grid.setAdapter(imageaptor);
					
				}
				
				break;

			}

		}

	};



	protected void selectImage() {

		final CharSequence[] items = { "Take Photo", "Choose from Gallery",
		"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				rootView.getContext());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (licensechk == true) {
						
						license_filename=System.currentTimeMillis() + ".jpg";
						
						license_pathfile = new File(license_path,
								license_filename);
							
							i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(license_pathfile));
				
					}else if (insurancechk == true) {
						
						insurance_filename=System.currentTimeMillis() + ".jpg";
						
						pathinsurancefile = new File(insurance_path+selregno,
								insurance_filename);
		
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pathinsurancefile));
					}else if (pucchk == true) {
						
						puc_filename=System.currentTimeMillis()+ ".jpg";
						
						pucpathfile = new File(puc_path+selregno, puc_filename);
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pucpathfile));

					}else if (passportchk == true) {

						passport_filename=System.currentTimeMillis()
								+ ".jpg";
						
						passport_pathfile = new File(passport_path, passport_filename);
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(passport_pathfile));

					}else if (panchk == true) {
						
						pancard_filename=System.currentTimeMillis()+ ".jpg";
						
						pancard_pathfile = new File(pancard_path, pancard_filename);
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pancard_pathfile));

					}else if (voteridchk == true) {
						voterid_filename= System.currentTimeMillis()+ ".jpg";
						
						voterid_pathfile = new File(voterid_path,voterid_filename);
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(voterid_pathfile));
					} else if (aadharchk == true) {
						
						aadhar_filename= System.currentTimeMillis()+ ".jpg";
						aadhar_pathfile = new File(aadhar_path,aadhar_filename);
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(aadhar_pathfile));	
					}else if (vehicalpicturechk == true) {
						
						vehicalpicture_filename=System.currentTimeMillis()
								+ ".jpg";
						vehicalpicturepathfile = new File(vehicalpicture_path+selregno, vehicalpicture_filename);
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(vehicalpicturepathfile));	
					}else if (otherschk == true) {
						
						others_filename=System.currentTimeMillis()+".jpg";
						others_pathfile = new File(others_path, others_filename);
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(others_pathfile));	
						
					}
					startActivityForResult(i, CAMERA_CAPTURE);
				} else if (items[item].equals("Choose from Gallery")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();

	}


	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		int RESULT_OK = -1;
		if (resultCode == RESULT_OK) {
			// user is returning from capturing an image using the camera
			if (requestCode == CAMERA_CAPTURE) {

			
				//Added by Trupti Reddy
				AsyncTaskRunner runner = new AsyncTaskRunner();

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
					runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				else 
					runner.execute();
				
			}


			else if (requestCode == SELECT_FILE) {
				onSelectFromGalleryResult(data);

			}else if (requestCode == CROP_IMAGE) {

			}
		}
	}


	protected void CropImage() {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(artUri, "image/*");

			intent.putExtra("crop", "true");
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 3);
			intent.putExtra("aspectY", 4);
			intent.putExtra("scaleUpIfNeeded", true);
			intent.putExtra("return-data", true);

			startActivityForResult(intent, CROP_IMAGE);

		} catch (ActivityNotFoundException e) {
			Toast.makeText(getActivity(),
					"Your device doesn't support the crop action!",
					Toast.LENGTH_LONG).show();
		}
	}

	@SuppressLint("NewApi")
	private void onSelectFromGalleryResult(Intent data) {

		Uri selectedImageUri = data.getData();
		String[] projection = { MediaColumns.DATA };

		Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		lindocs.setVisibility(View.GONE);
		String selectedImagePath = cursor.getString(column_index);
		artUri = Uri.fromFile(new File(selectedImagePath));
		//CropImage();
		if (licensechk == true) {

		license_filename=System.currentTimeMillis()+".jpg";
		gallaryImage=new GallaryImage(selectedImagePath, license_filename,license_path,license_Thumbpath);
		gallaryImage.execute();
			
		} else if (insurancechk == true) {

	        insurance_filename=System.currentTimeMillis()+".jpg";
			gallaryImage=new GallaryImage(selectedImagePath, insurance_filename,insurance_path+selregno,insurance_path+selregno+getString(R.string.thumbPath));
			gallaryImage.execute();
		} else if (pucchk == true) {

				
			    puc_filename=System.currentTimeMillis()+".jpg";
				gallaryImage=new GallaryImage(selectedImagePath, puc_filename,puc_path+selregno,puc_path+selregno+getString(R.string.thumbPath));
				gallaryImage.execute();
			
			/*if (chkitemvalue == true) {
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),
							listOfImagesPath);
					grid.setAdapter(imageaptor);
					imageaptor.notifyDataSetChanged();
				}
			}*/
		} else if (passportchk == true) {


			
			passport_filename=System.currentTimeMillis()+".jpg";
			gallaryImage=new GallaryImage(selectedImagePath, passport_filename,passport_path,passport_Thumbpath);
			gallaryImage.execute();
			
		} else if (panchk == true) {

			
			pancard_filename=System.currentTimeMillis()+".jpg";
			gallaryImage=new GallaryImage(selectedImagePath, pancard_filename,pancard_path,pancard_Thumbpath);
			gallaryImage.execute();
			
		} else if (voteridchk == true) {

			
			voterid_filename=System.currentTimeMillis()+".jpg";
			gallaryImage=new GallaryImage(selectedImagePath, voterid_filename,voterid_path,voterid_Thumbpath);
			gallaryImage.execute();
			
			
			
		} else if (aadharchk == true) {

			aadhar_filename=System.currentTimeMillis()+".jpg";
			gallaryImage=new GallaryImage(selectedImagePath, aadhar_filename,aadhar_path,aadhar_Thumbpath);
			gallaryImage.execute();
			
			
			
		} else if (vehicalpicturechk == true) {

			
		    			vehicalpicture_filename=System.currentTimeMillis()+".jpg";
						gallaryImage=new GallaryImage(selectedImagePath, vehicalpicture_filename,vehicalpicture_path+selregno,vehicalpicture_path+selregno+getString(R.string.thumbPath));
						gallaryImage.execute();
			
			/*if (chkitemvalue == true) {
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),
							listOfImagesPath);
					grid.setAdapter(imageaptor);
					imageaptor.notifyDataSetChanged();
				}
			}*/
		} else if (otherschk == true) {

			
			others_filename=System.currentTimeMillis()+".jpg";
			gallaryImage=new GallaryImage(selectedImagePath, others_filename,others_path,others_Thumbpath);
			gallaryImage.execute();
			
			
			
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		regno.setSelection(position);
		selregno = (String) regno.getSelectedItem();
		
		chkitemvalue = true;
		if (selregno.equalsIgnoreCase("Select Vehicle")) {
			insuranceitemclick = false;
			pucitemclick = false;
			vehicalitemclick = false;
			grid.setAdapter(null);
		}
		//	int size = new UserDetails().getRegNumberList().size();
		/*for (int i = 0; i < size; i++) {*/
		if (insurancechk == true) {
			chkitemvalue = true;
			grid.setVisibility(View.VISIBLE);
			/*if (selregno.equals(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"))) {*/
			insuranceitemclick = true;
			/*pathinsurance = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/CustomerSocialAppDocument/"+ UserDetails.getUser_id()
					+ "/Insurance/"
					+ selregno;
			Log.d(pathinsurance, "get insurance path");
			File indir = new File(pathinsurance);
			indir.mkdirs();*/
			listOfImagesPath = null;
			listOfImagesPath = loadThumbs();
			if (listOfImagesPath != null) {
				imageaptor = new ImageListAdapter(getActivity(),
						listOfImagesPath);
				grid.setAdapter(imageaptor);
				imageaptor.notifyDataSetChanged();
			}

			//}
	} else if (pucchk == true) {
		chkitemvalue = true;
		grid.setVisibility(View.VISIBLE);
		/*if (selregno.equals(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"))) {*/
		pucitemclick = true;
		/*pucpath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/CustomerSocialAppDocument/"+ UserDetails.getUser_id()+"/puc/"
				+ selregno;
		
		File indir = new File(pucpath);
		indir.mkdirs();*/
		listOfImagesPath = null;
		listOfImagesPath = loadThumbs();
		if (listOfImagesPath != null) {
			imageaptor = new ImageListAdapter(getActivity(),
					listOfImagesPath);
			grid.setAdapter(imageaptor);
			imageaptor.notifyDataSetChanged();
		}

		//}
	} else if (vehicalpicturechk == true) {
		chkitemvalue = true;
		grid.setVisibility(View.VISIBLE);
		/*	if (selregno.equals(new UserDetails().getRegNumberList().get(i)
						.get("registration_num"))) {*/
		vehicalitemclick = true;
		/*vehicalpicturepath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"+ UserDetails.getUser_id()+"/vehicalpicture/"
				+ selregno;
		Log.d(vehicalpicturepath, "get insurance path");
		File indir = new File(vehicalpicturepath);
		indir.mkdirs();*/
		listOfImagesPath = null;
		listOfImagesPath = loadThumbs();
		if (listOfImagesPath != null) {
			imageaptor = new ImageListAdapter(getActivity(),
					listOfImagesPath);
			grid.setAdapter(imageaptor);
			imageaptor.notifyDataSetChanged();
		}

		//}
	}
		//}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {


	}

	public class ImageListAdapter extends BaseAdapter {
		private Context context;
		private List<String> imgPic;

		public ImageListAdapter(Context c, List<String> thePic) {
			context = c;
			imgPic = thePic;
		}

		public int getCount() {
			if (imgPic != null)
				return imgPic.size();
			else
				return 0;
		}

		// ---returns the ID of an item---
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// ---returns an ImageView view---
		public View getView(int position, View convertView, ViewGroup parent) {

			BitmapFactory.Options bfOptions = new BitmapFactory.Options();
			bfOptions.inDither = false; // Disable Dithering mode
			bfOptions.inPurgeable = true; // Tell to gc that whether it needs
			// free memory, the Bitmap can be
			// cleared
			bfOptions.inInputShareable = true; // Which kind of reference will
			// be used to recover the Bitmap
			// data after being clear, when
			// it will be used in the future
			bfOptions.inTempStorage = new byte[32 * 1024];
			if (convertView == null) {
				/*
				 * imageView = new ImageView(context);
				 * imageView.setLayoutParams(new GridView.LayoutParams(
				 * ViewGroup.LayoutParams.MATCH_PARENT,
				 * ViewGroup.LayoutParams.MATCH_PARENT));
				 * imageView.setPadding(0, 0, 0, 0);
				 */
				imageView = new ImageView(context);
				imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(10, 10, 10, 10);
			} else {
				imageView = (ImageView) convertView;
			}
			FileInputStream fs = null;
			Bitmap bm;
			try {
				fs = new FileInputStream(new File(imgPic.get(position)
						.toString()));

				if (fs != null) {
					bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
							bfOptions);
					imageView.setImageBitmap(bm);
					imageView.setId(position);
					imageView.setLayoutParams(new GridView.LayoutParams(200,
							160));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e1) {

				e1.printStackTrace();
				Log.d(e1.toString(), "");
				File filestring = files[position];
				if (filestring != null) {
					filestring.delete();
					listOfImagesPath.remove(position);
					imageaptor.notifyDataSetChanged();

				}

			} finally {
				if (fs != null) {
					try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return imageView;
		}
	}

	 public String compressImage(String imageUri) {

		String filePath = getRealPathFromURI(imageUri);
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();

		// by setting this field as true, the actual bitmap pixels are not
		// loaded in the memory. Just the bounds are loaded. If
		// you try the use the bitmap here, you will get null.
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;

		// max Height and width values of the compressed image is taken as
		// 816x612

		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		// width and height values are set maintaining the aspect ratio of the
		// image

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		// setting inSampleSize value allows to load a scaled down version of
		// the original image

		options.inSampleSize = calculateInSampleSize(options, actualWidth,
				actualHeight);

		// inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;

		// this options allow android to claim the bitmap memory if it runs low
		// on memory
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			// load the bitmap from its path
			bmp = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
					Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
				middleY - bmp.getHeight() / 2, new Paint(
						Paint.FILTER_BITMAP_FLAG));

		// check the rotation of the image and display it properly
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
					scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
					true);
			//	rcImage.setImageBitmap(scaledBitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);

			// write the compressed bitmap at the destination specified by
			// filename.
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG,40, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		Log.v("compress image path", filename);
		return filename;

	}

	
	// Added by Vaibhav Sharma
	
	private void createThumbs(String filePath,String thumbPath,String filename)
	{
		
		try {
			
		
		Bitmap thumbnails=ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath), 100, 100);
		
		
		if(thumbPath!=null && filename!=null)
		{
			
			if(!new File(thumbPath).exists())
			{
				new File(thumbPath).mkdir();
			}
			
		File file=new File(thumbPath,filename);
		OutputStream fOut = null;
		fOut = new FileOutputStream(file);
		thumbnails.compress(Bitmap.CompressFormat.JPEG, 100, fOut); 
		fOut.flush();
		fOut.close(); 	

		
		}
		
		
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		
	}
	
	
	public String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "Images/*");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/"
				+ System.currentTimeMillis() + ".jpg");
		return uriSting;

	}

	private String getRealPathFromURI(String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null,
				null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int index = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(index);
		}
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;
		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}


	// Added by Vaibhav Sharma
	
	// For creating directories

	public void createDirectories()
	{
		File licensedir = new File(license_path);

		if (licensedir.mkdirs()) {
			Log.v("license folder created", "success");
		
			if(new File(license_Thumbpath).mkdir())
			{
				Log.v("license thumb folder created", "success");
			}
			
		}

		File insurancedir = new File(insurance_path);

		if (insurancedir.mkdirs()) {
			Log.v("insurance folder created", "success");
		
		}

		File pucdir = new File(puc_path);


		if (pucdir.mkdirs()) {
			Log.v("puc folder created", "success");
		
		}


		File passportdir = new File(passport_path);

		if (passportdir.mkdirs()) {
			Log.v("passport folder created", "success");

			if(new File(passport_Thumbpath).mkdir())
			{
				Log.v("passport thumb folder created", "success");
			}
		
		
		}


		File pancarddir = new File(pancard_path);

		if (pancarddir.mkdirs()) {
			Log.v("pancard folder created", "success");
		
			if(new File(pancard_Thumbpath).mkdir())
			{
				Log.v("pancard thumb folder created", "success");
			}

			
			
		}


		File voteriddir = new File(voterid_path);

		if (voteriddir.mkdirs()) {
			Log.v("voter folder created", "success");
		
		
			if(new File(voterid_Thumbpath).mkdirs())
			{
				Log.v("voiterid thumb folder created", "success");
			}
		
		}


		File aadhardir = new File(aadhar_path);
		if (aadhardir.mkdirs()) {
			Log.v("aadhar folder created", "success");
			if(new File(aadhar_Thumbpath).mkdirs())
			{
				Log.v("aadhar thumb folder created", "success");
			}
		
		}

		File vehicalpituredir = new File(vehicalpicture_path);

		if (vehicalpituredir.mkdirs()) {
			Log.v("vehicle folder created", "success");

			
			
		}




		File othersdir = new File(others_path);

		if (othersdir.mkdirs()) {
			Log.v("others folder created", "success");
		
			if(new File(others_Thumbpath).mkdirs())
			{
				Log.v("others thumb folder created", "success");
			}
		
		}


		ArrayList<HashMap<String, String>> vehicles=new ArrayList<HashMap<String, String>>();
		
		vehicles=new UserDetails().getRegNumberList();
		for(int index=0;index<vehicles.size();index++)
		{
			
			licensedir=new File(puc_path+vehicles.get(index).get("registration_num").toString()+getString(R.string.thumbPath));
			licensedir.mkdirs();	
			licensedir=new File(insurance_path+vehicles.get(index).get("registration_num").toString()+getString(R.string.thumbPath));
			licensedir.mkdirs();	
			licensedir=new File(vehicalpicture_path+vehicles.get(index).get("registration_num").toString()+getString(R.string.thumbPath));
			licensedir.mkdirs();		
			
			
		}
			}

	
	// Added by Vaibhav Sharma
	
	// for fetching thumbnails
	
	private List<String> loadThumbs() {
		
		List<String> tFileList = new ArrayList<String>();
		if (licensechk == true) {
		
			
			File f = new File(license_Thumbpath);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					tFileList.add(file.getPath());
				}
			}
		}
		
		
		if (insurancechk == true) {
			if (chkitemvalue == true) {
			
				File f = new File(insurance_path+selregno+getString(R.string.thumbPath));
				
				if (f.exists()) {
					files = f.listFiles();
					Log.d(files + "", "file added");
					Arrays.sort(files);

					for (int j = 0; j < files.length; j++) {
						File file = files[j];
						if (file.isDirectory())
							continue;
						tFileList.add(file.getPath());
					}
				}

			}
		}

		
		if (pucchk == true) {
	
			if (chkitemvalue == true) {
			
				File f = new File(puc_path+selregno+getString(R.string.thumbPath));
				
				if (f.exists()) {
					files = f.listFiles();
					
					Arrays.sort(files);

					for (int j = 0; j < files.length; j++) {
						File file = files[j];
						if (file.isDirectory())
							continue;
						tFileList.add(file.getPath());
					}
				}

			}
			
			
		}

		
		
		if (passportchk == true) {
			File f = new File(passport_Thumbpath);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					tFileList.add(file.getPath());
				}
			}
		}

		
		if (panchk == true) {
			File f = new File(pancard_Thumbpath);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					tFileList.add(file.getPath());
				}
			}
		}

		
		if (voteridchk == true) {
			File f = new File(voterid_Thumbpath);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					tFileList.add(file.getPath());
				}
			}
		}

		
		if (aadharchk == true) {
			File f = new File(aadhar_Thumbpath);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					tFileList.add(file.getPath());
				}
			}
		}

		
		
		if (vehicalpicturechk == true) {
			
			if (chkitemvalue == true) {

				File f = new File(vehicalpicture_path+selregno+getString(R.string.thumbPath));

				if (f.exists()) {
					files = f.listFiles();

					Arrays.sort(files);

					for (int j = 0; j < files.length; j++) {
						File file = files[j];
						if (file.isDirectory())
							continue;
						tFileList.add(file.getPath());
					}
				}
				}

		}
		if (otherschk == true) {
			
			Log.v("others", others_Thumbpath);
			File f = new File(others_Thumbpath);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					tFileList.add(file.getPath());
				}
			}
		}

		
		
		
	return tFileList;	
		
		
		
		
	}
	
	// Added by Vaibhav Sharma
	
	public void pathBuilder()
	{


		others_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/others/";



		vehicalpicture_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/vehicalpicture/";




		aadhar_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/aadhar/";



		voterid_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/voterid/";


		pancard_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/pancard/";

		passport_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/passport/";

		license_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/license/";


		insurance_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/insurance/";
		puc_path = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/puc/";


		// Added by Vaibhav Sharma
		
		
		// Thumbnails path
		

		license_Thumbpath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/license/thumb";
	
		
		
		/*insurance_Thumbpath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/insurance/thumb";*/
		
		/*puc_Thumbpath=Environment.getExternalStorageDirectory().getAbsolutePath()
		+ "/CustomerSocialAppDocument/"
		+ UserDetails.getUser_id()
		+ "/puc/thumb";*/
		
		
		
		passport_Thumbpath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/passport/thumb";
		
		
		pancard_Thumbpath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/pancard/thumb";
		
		
		voterid_Thumbpath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/voterid/thumb";
		
		
		aadhar_Thumbpath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/aadhar/thumb";
		
		
		
		/*vehicalpicture_Thumbpath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/vehicalpicture/thumb";*/
		
		
		others_Thumbpath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id()
				+ "/others/thumb";
		
		
		

	}

	
	
	// Added by Vaibhav Sharma
	
	// For fetching original images
	
	private ArrayList<String> getRealImages()
	{
		
		ArrayList<String> ImagePath=new ArrayList<>();
		
		
		if (licensechk == true) {
			File f = new File(license_path);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					ImagePath.add(file.getPath());
				}
			}
		} else if (insurancechk == true) {
			if (chkitemvalue == true) {
			
				
				File f = new File(insurance_path+selregno);
			
				
				if (f.exists()) {
					files = f.listFiles();
			
					Arrays.sort(files);

					for (int j = 0; j < files.length; j++) {
						File file = files[j];
						if (file.isDirectory())
							continue;
						ImagePath.add(file.getPath());
					}
				}

			}
		} else if (pucchk == true) {
			if (chkitemvalue == true) {
				// chkitemvalue = false;
				File f = new File(puc_path+selregno);
				Log.d(f + "", "file data");
				if (f.exists()) {
					files = f.listFiles();
					Log.d(files + "", "file added");
					Arrays.sort(files);

					for (int j = 0; j < files.length; j++) {
						File file = files[j];
						if (file.isDirectory())
							continue;
						ImagePath.add(file.getPath());
					}
				}

			}
		} else if (passportchk == true) {
			File f = new File(passport_path);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					ImagePath.add(file.getPath());
				}
			}
		} else if (panchk == true) {
			File f = new File(pancard_path);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					ImagePath.add(file.getPath());
				}
			}
		} else if (voteridchk == true) {
			File f = new File(voterid_path);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					ImagePath.add(file.getPath());
				}
			}
		} else if (aadharchk == true) {
			File f = new File(aadhar_path);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					ImagePath.add(file.getPath());
				}
			}
		} else if (vehicalpicturechk == true) {
			if (chkitemvalue == true) {
				
				File f = new File(vehicalpicture_path+selregno);
				
				if (f.exists()) {
					files = f.listFiles();
				
					Arrays.sort(files);

					for (int j = 0; j < files.length; j++) {
						File file = files[j];
						if (file.isDirectory())
							continue;
						ImagePath.add(file.getPath());
					}
				}

			}
		} else if (otherschk == true) {
			File f = new File(others_path);
			if (f.exists()) {
				files = f.listFiles();
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory())
						continue;
					ImagePath.add(file.getPath());
				}
			}
		}

		
		
		
		return ImagePath;
		
		

	}
	
	
	
	// Added by Vaibhav Sharma

	class GallaryImage extends AsyncTask<Void, Void, Void>
	{
		private ProgressDialog dialog;
		private String source;
		private String filename;
		private String destination;
		private String thumbPath;
		public GallaryImage(String source,String filename,String destination,String thumbPath)
		{
		
			this.thumbPath=thumbPath;
			this.source=source;
			this.filename=filename;
			this.destination=destination;
			
		}
		
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
		
		dialog=new ProgressDialog(getActivity());
		dialog.setMessage(getString(R.string.progress_dialog_msg));
		dialog.setCancelable(false);
		dialog.show();
		
		
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
		
			
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(source, options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;


				InputStream inStream = null;
				OutputStream outStream = null;

				try {

				//	compressImage(source);	
					createThumbs(source, thumbPath, filename);  // creating thumbnails
					
					
					File afile = new File(source);
					File bfile = new File(destination, filename);
					inStream = new FileInputStream(afile);
					outStream = new FileOutputStream(bfile);
					
					byte[] buffer = new byte[1024];

					int length=0;
					
					while ((length = inStream.read(buffer)) > 0) {
						outStream.write(buffer, 0, length);
					}
					inStream.close();
					outStream.close();

					

				} catch (IOException e) {
					Log.v("file copy", e.toString());
				}
				
				
				listOfImagesPath = null;
				listOfImagesPath =  loadThumbs();
				
				if (RealImagePath!=null) {
					RealImagePath=null;
					RealImagePath=getRealImages();
				}
				else
				{
					RealImagePath=getRealImages();
				}
				
				
				return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
		
			super.onPostExecute(result);

			if(listOfImagesPath.size()>0)
			{
			
				imageaptor = new ImageListAdapter(getActivity(),listOfImagesPath);
				grid.setAdapter(imageaptor);
				dialog.dismiss();
			}
			else
			{
				dialog.dismiss();
				
			}
			
			
			
		}
		
		
	}
	
	
	
	// Added by Trupti Reddy

	private class AsyncTaskRunner extends AsyncTask< Void, Void, Void> {

		private ProgressDialog progressDialog;

		@Override
		protected Void doInBackground(Void... params) {
			Log.e("in background", "background");
			if (licensechk == true) {
				
				compressImage(license_pathfile.getAbsolutePath());
				createThumbs(license_pathfile.getAbsolutePath(), license_Thumbpath,license_filename);
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();//RetriveCapturedImagePath();
			} else if (insurancechk == true) {
				if (chkitemvalue == true) {
					compressImage(pathinsurancefile.getAbsolutePath());
					createThumbs(pathinsurancefile.getAbsolutePath(), insurance_path+selregno+getString(R.string.thumbPath),insurance_filename);
					listOfImagesPath = null;
					listOfImagesPath = loadThumbs();;
				}
			} else if (pucchk == true) {
				if (chkitemvalue == true) {
					compressImage(pucpathfile.getAbsolutePath());
					createThumbs(pucpathfile.getAbsolutePath(), puc_path+selregno+getString(R.string.thumbPath),puc_filename);		
					
					listOfImagesPath = null;
					listOfImagesPath = loadThumbs();;
				}
			} else if (passportchk == true) {
			
				compressImage(passport_pathfile.getAbsolutePath());
				createThumbs(passport_pathfile.getAbsolutePath(),passport_Thumbpath,passport_filename);
				
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
			} else if (panchk == true) {
			
				compressImage(pancard_pathfile.getAbsolutePath());
				createThumbs(pancard_pathfile.getAbsolutePath(),pancard_Thumbpath,pancard_filename);
				
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
			} else if (voteridchk == true) {
			
				compressImage(voterid_pathfile.getAbsolutePath());
				createThumbs(voterid_pathfile.getAbsolutePath(),voterid_Thumbpath,voterid_filename);
				
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
			} else if (aadharchk == true) {
		
				compressImage(aadhar_pathfile.getAbsolutePath());
				createThumbs(aadhar_pathfile.getAbsolutePath(),aadhar_Thumbpath,aadhar_filename);
				
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();			              
			} else if (vehicalpicturechk == true) {	
			
				
				compressImage(vehicalpicturepathfile.getAbsolutePath());
				createThumbs(vehicalpicturepathfile.getAbsolutePath(),vehicalpicture_path+selregno+getString(R.string.thumbPath)
						,vehicalpicture_filename);
				
				if (chkitemvalue == true) {
					listOfImagesPath = null;
				
					listOfImagesPath = loadThumbs();
				
				}
			} else if (otherschk == true) {
			
				
				compressImage(others_pathfile.getAbsolutePath());
				createThumbs(others_pathfile.getAbsolutePath(),others_Thumbpath,others_filename);
		
				
				listOfImagesPath = null;
				listOfImagesPath = loadThumbs();
			
			
			
			}

			return null;
		}


		@Override
		protected void onPostExecute(Void result) {
			
			if (progressDialog.isShowing()) {
				if (listOfImagesPath != null) {
					imageaptor = new ImageListAdapter(getActivity(),listOfImagesPath);
					grid.setAdapter(imageaptor);
					imageaptor.notifyDataSetChanged();
				}
				lindocs.setVisibility(View.GONE); 
				edit.commit();
				progressDialog.dismiss();
			}
		}

		public AsyncTaskRunner() {
			progressDialog = new ProgressDialog(getActivity());
		}

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getString(R.string.progress_dialog_msg));
			progressDialog.setCancelable(false);
			progressDialog.show();
		
		
		}
	}




}
