package com.ttl.customersocialapp;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ttl.adapter.GpsPdfAdapter;
import com.ttl.model.UserDetails;

public class PdfListFragment extends Fragment implements OnItemClickListener {

	private File[] files;
	private String[] pdflist;
	private View rootView;
	private File folder;
	private ImageView gpsview;
	private Fragment fragment = null;
	private String History = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ "/CustomerSocialAppDocument/"
			+ UserDetails.getUser_id() + "/History/";

	public PdfListFragment() {
	}

	private GpsPdfAdapter pdfadaptor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater
				.inflate(R.layout.fragment_pdflist, container, false);
		File Historydir = new File(History);
		Historydir.mkdirs();
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		folder = new File(extStorageDirectory, "CustomerSocialAppDocument");
		folder.mkdir();
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/CustomerSocialAppDocument/"
				+ UserDetails.getUser_id() + "/History/");
		if (dir.exists()) {
			files = dir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					
					return ((name.endsWith(".pdf")));
				}
			});

			pdflist = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				pdflist[i] = files[i].getName();
			}

			ListView pdflist1 = (ListView) rootView.findViewById(R.id.pdflist);
			try {
				pdfadaptor = new GpsPdfAdapter(rootView.getContext(), pdflist);

				pdflist1.setAdapter(pdfadaptor);
				pdfadaptor.notifyDataSetChanged();
			} catch (Exception e) {
				
			}

			pdflist1.setOnItemClickListener(this);
		} else {
			Toast.makeText(getActivity(), "You have no trip history.",
					Toast.LENGTH_SHORT).show();
		}

		gpsview = (ImageView) rootView.findViewById(R.id.imggpsview);
		gpsview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				FragmentManager fragmentManager = getFragmentManager();
				fragment = new GPSTripMeter_fragment();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
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
								new GPSTripMeter_fragment()).commit();
						return true;
					}
				}
				return false;
			}
		});
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		

		PackageManager packageManager = rootView.getContext()
				.getPackageManager();
		Intent testIntent = new Intent(Intent.ACTION_VIEW);
		testIntent.setType("application/pdf");
		List list = packageManager.queryIntentActivities(testIntent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (list.size() > 0 && files[(int) id].isFile()) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.fromFile(files[(int) id].getAbsoluteFile()); //
			intent.setDataAndType(uri, "application/pdf");
			startActivity(intent);
		}

	}









}
