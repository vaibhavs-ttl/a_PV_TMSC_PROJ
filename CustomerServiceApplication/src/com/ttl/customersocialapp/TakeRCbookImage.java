package com.ttl.customersocialapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ttl.helper.VehicleRegister;
import com.ttl.model.UserDetails;

public class TakeRCbookImage extends Fragment{

	View rootview;
	final int CAMERA_CAPTURE = 1 , EMAIL = 2;
	ImageView rcImage;
	Bitmap bitmap;
	String encodedString;
	String imgPath, fileName;
	Button sendmail;
	File destination;
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.dialog_contacttocustcare, viewGroup,
				false);
	/*	takephotodialog = new Dialog(context);
		takephotodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		takephotodialog
				.setContentView(R.layout.dialog_contacttocustcare);
		takephotodialog.setCancelable(true);
		takephotodialog.show();*/
		ImageView close = (ImageView) rootview
				.findViewById(R.id.imgclose);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	takephotodialog.dismiss();
				HomeFragment.regvehicle = false;
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();
				tx.replace(R.id.frame_container, new VehicleDetails_Fragment())
						.commit();
				// checkvehiclereg(HomeFragment.regvehicle);
			}
		});
		rcImage = (ImageView) rootview
				.findViewById(R.id.capturedimage);
		Button takephoto = (Button) rootview
				.findViewById(R.id.btntakephoto);
		 sendmail = (Button) rootview
				.findViewById(R.id.btnsendmail);
		takephoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectProfileImage();
			}
		});
		sendmail.setVisibility(View.GONE);
		sendmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String body ="Dear Team,\n"
						+"I need assistance to update my details in your CRM system so that I can register my vehicle using your customer App. Attached is the scanned copy of my RC/Insurance document. Please update the records & confirm"
						+"\n\nRegards, \n"
						+"Name: "+UserDetails.getFirst_name()+" "+UserDetails.getLast_name()
						+"\nGender: "+UserDetails.getGender()
						+"\nMobile No: "+UserDetails.getContact_number()
						+"\nEmail: "+UserDetails.getEmail_id()
						+"\nAddress: "+UserDetails.getAddress()
						+"\nState: "+UserDetails.getState()	
						+"\nCity: "+UserDetails.getCity()	
						+"\nPin: "+UserDetails.getPincode()
						+"\nRegistration No: "+VehicleRegister.regnumber1
						+"\nChassis No: "+VehicleRegister.chassis1;
						
				/*File F = new File(RetriveCapturedImagePath());
				Uri U = Uri.fromFile(F);
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("image/png");
				i.putExtra(Intent.EXTRA_STREAM, U);
				startActivity(Intent.createChooser(i,"Email:"));*/
				File F = new File(RetriveCapturedImagePath());
				Uri U = Uri.fromFile(destination);
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				          new String[] { "customerregistration@tatamotors.com" });

				  emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						  VehicleRegister.regnumber1+"- Requires contact updation");

				  emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						  body);
				  emailIntent.putExtra(Intent.EXTRA_STREAM, U);
				startActivityForResult(Intent.createChooser(emailIntent,"Email:"),EMAIL);
				
				
				/*String req = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+"<SOAP:Body>"
						+"<SendMail xmlns=\"http://schemas.cordys.com/1.0/email\">"
						+"<to>"
						+"<address>"
						+"<emailAddress>customerregistration@tatamotors.com</emailAddress>"
						+"<displayName>sam</displayName>"
						+"</address>"
						+"</to>"
						+"<subject>"+VehicleRegister.regnumber1+"-Requires contact updation</subject>"
						+"<body type=\"normal\">Dear Team,\n"
						+"I need assistance to update my details in your CRM system so that I can register my vehicle using your customer App. Attached is the scanned copy of my RC/Insurance document. Please update the records & confirm"
						+"Regards, \n"
						+"Name: "+UserDetails.getFirst_name()+" "+UserDetails.getLast_name()
						+"\nGender: "+UserDetails.getGender()
						+"\nMobile No: "+UserDetails.getContact_number()
						+"\nEmail: "+UserDetails.getEmail_id()
						+"\nAddress: "+UserDetails.getAddress()
						+"\nState: "+UserDetails.getState()	
						+"\nCity: "+UserDetails.getCity()	
						+"\nPin: "+UserDetails.getPincode()
						+"\nRegistration No: "+VehicleRegister.regnumber1
						+"\nChassis No: "+VehicleRegister.chassis1
						+"</body>"
						+"<from>"
						+"<displayName>"+UserDetails.getFirst_name()+"</displayName>"
						+"<emailAddress>"+UserDetails.getEmail_id()+"</emailAddress>" //"+UserDetails.getEmail_id()+"
						+"</from>"
						+"</SendMail>"
						+"</SOAP:Body>"
						+"</SOAP:Envelope>";
				new WebServiceCall(getActivity(), req, Constants.SendMail, new ResponseCallback() {
					
					@Override
					public void onResponseReceive(Object object) {
						// TODO Auto-generated method stub
						HomeFragment.regvehicle = false;
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container, new VehicleDetails_Fragment())
								.commit();
					}
					
					@Override
					public void onErrorReceive(String string) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "Mail Send Failed . Please try again.", Toast.LENGTH_SHORT).show();
					}
				}, "").execute();*/
			}
		});
		File licensedir = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument");
		licensedir.mkdirs();
		rootview.getRootView().setFocusableInTouchMode(true);
		rootview.getRootView().requestFocus();
		rootview.getRootView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						/*if(UserDetails.getUser_id().equals(""))
						{
							Toast.makeText(getActivity(), "Please update Profile first.", Toast.LENGTH_SHORT).show();
						}else
						{*/
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						tx.replace(R.id.frame_container, new VehicleDetails_Fragment())
								.commit();
						//}
						return true;
					}
				}
				return false;
			}
		});
		return rootview;
	}
	public void selectProfileImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Gallery",
				"Cancel" };

		/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAMERA_CAPTURE);*/
		
		Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    File dir=
	        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

	    destination=new File(dir, UserDetails.getFirst_name()+"Document.jpeg");
	    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
	    startActivityForResult(i, CAMERA_CAPTURE);
	
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		int RESULT_OK = -1;
		try {
			if(requestCode == CAMERA_CAPTURE )
			{
				if (resultCode == RESULT_OK) {
			        /*Intent i=new Intent(Intent.ACTION_VIEW);

			        i.setDataAndType(Uri.fromFile(destination), "image/jpeg");
			        startActivity(i);*/
			       // rcImage.setImageURI(Uri.fromFile(destination));
			        sendmail.setVisibility(View.VISIBLE);
			        destination.getAbsolutePath();
			        compressImage(destination.getAbsolutePath());
			       /* BitmapFactory.Options options = new BitmapFactory.Options();
			        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			        Bitmap bitmap = BitmapFactory.decodeFile(destination.getAbsolutePath(), options);
			        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			        rcImage.setImageBitmap(bitmap);*/
			        
			       /* InputStream instream;
			        
			        instream = rootview.getContext().getContentResolver().openInputStream(Uri.fromFile(destination));
			        bitmap = BitmapFactory.decodeStream(instream);
			        rcImage.setImageBitmap(bitmap);*/
			      
			}else if(requestCode == EMAIL )
			{
					if( resultCode == RESULT_OK && null != data)
				{
					HomeFragment.regvehicle = false;
					FragmentManager fm = getFragmentManager();
					FragmentTransaction tx = fm.beginTransaction();
					tx.replace(R.id.frame_container, new VehicleDetails_Fragment())
							.commit();
				}else
				{
					HomeFragment.regvehicle = false;
					FragmentManager fm = getFragmentManager();
					FragmentTransaction tx = fm.beginTransaction();
					tx.replace(R.id.frame_container, new VehicleDetails_Fragment())
							.commit();
				}
			}
		} }catch (Exception e) {
			e.printStackTrace();
			
	        sendmail.setVisibility(View.GONE);

			Toast.makeText(rootview.getContext(), "Something went wrong ", Toast.LENGTH_LONG)
					.show();
		}

	}
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = rootview.getContext().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }
	
	public String RetriveCapturedImagePath()
	{
		String Path = "";
		File f = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/CustomerSocialAppDocument");
		if (f.exists()) {
			
				 Path= f.getPath();
			}
		
		return Path;
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
			rcImage.setImageBitmap(scaledBitmap);
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

		return filename;

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
}
