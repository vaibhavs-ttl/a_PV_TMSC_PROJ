package com.ttl.customersocialapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class AppDocument extends AsyncTask<Void, Void, Void>{

	private File[] dirs;
	private File baseURL;
	
	private File directory;
	private Context context;
	private String thumb_path;
	private ProgressDialog dialog;
	public AppDocument(Context context)
	{
		this.context=context;
		baseURL=new File(Environment
	            .getExternalStorageDirectory().getAbsolutePath()
	            + "/CustomerSocialAppDocument/");
	}
	
	@Override
	protected void onPreExecute() {
		

		dialog=new ProgressDialog(context);
		dialog.setMessage("Initializing your documents");
		dialog.dismiss();
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {
	
		
		try {
			
			dirs=baseURL.listFiles();
			Arrays.sort(dirs);
			
			for(int dirIndex=0;dirIndex<dirs.length;dirIndex++) // to get list of user directories
			{
				
				
			//	Log.e("user dir name",dirs[dirIndex].getName());// printing user directory name
				
				directory=new File(baseURL+File.separator+dirs[dirIndex].getName()+File.separator); 
				File subDirs[]=directory.listFiles(); // getting list of sub directories resides inside user directory
				
				
				deleteThumnailsDir(subDirs,directory.getName());
				
				for(int sub_dir_index=0;sub_dir_index<subDirs.length;sub_dir_index++)
				{
					String path=baseURL+File.separator+dirs[dirIndex].getName()+File.separator+subDirs[sub_dir_index].getName();
					File folders=new File(path+File.separator);
					thumb_path=path+File.separator+"thumb";
					
					createThumbs(path, thumb_path, new File(path).list());
					
				
				}
				
				
				
			
			
		}
			
		} catch (Exception e) {
			
			Log.e("document error", e.toString());
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	@Override
	protected void onPostExecute(Void result) {
	
	
	
		dialog.dismiss();
	}
	
	private void deleteThumnailsDir(File[] subDirs,String dirName)
	{
		
		String thumb_path;
		File thumb_dir=null;
		String path;
		File folders;
		for(int index=0;index<subDirs.length;index++)
		{
			
			
			
			path=baseURL+File.separator+dirName+File.separator+subDirs[index].getName();
			folders=new File(path+File.separator);
		
			
			if(folders.isDirectory())
			{
			
			if(subDirs[index].isDirectory())
			{
				
		
				// Scenario 1 - If thumb directory exists then remove the old one and recreate directory
			
				thumb_path=path+File.separator+"thumb";
				thumb_dir=new File(thumb_path);
				
				//File[] inner_dirs=directoryFilter(subDirs[index].listFiles());
				
				
				
			File reg_dirs[]=directoryFilter(subDirs[index].listFiles());
				
				if(thumb_dir.isDirectory())
				{
				//	Log.e("if condition", thumb_path);
				//	Log.e("folder deleted","thumbs deleted");// printing user sub thumb dir directory name
					
				deleteDirectory(thumb_dir); // removing thumb directory if exist

				thumb_dir.mkdirs(); // creating new directory after removing the old directory.
				
				
				}
				else if(reg_dirs.length>0)
				{
					
					for(int i=0;i<reg_dirs.length;i++)
					{
						if(reg_dirs[i].isDirectory())
						{
							String reg_dir_thumb_path=path+File.separator+reg_dirs[i].getName()+File.separator+"thumb"+File.separator;
							File file=new File(reg_dir_thumb_path);	
							if(file.isDirectory())
							{
							deleteDirectory(file); // removing thumb directory if exist

							file.mkdirs(); // creating new directory after removing the old directory.
	
				//			Log.e("Deleted thumb dir ", path+File.separator+reg_dirs[i].getName()+File.separator+"thumb");	
							
							
						//	createThumbs(path+File.separator+reg_dirs[index]+File.separator, reg_dir_thumb_path,fileFilter(path+File.separator+reg_dirs[index]+File.separator));
							}
							else
							{
								file.mkdirs();
							//	createThumbs(path, reg_dir_thumb_path,fileFilter(path));
								Log.e("Created thumb dir ", file.getName());
							}
							
							
							
							
							
							}
								
						
					}
					
				
					
					
				}
				
				else
				{
					
					// Scenario 2 - If thumb directory does not exist then it will create a new thumb directory
					thumb_path=path+File.separator+"thumb";
					thumb_dir=new File(thumb_path);
					thumb_dir.mkdirs();
				
				//	Log.e("else condition", thumb_path);
				
				}
			}
			
			
			

			}
			}
		
		}
	
	
	
	
	private File[] directoryFilter(File[] vehicle_dirs)
	{
		
		
		
		ArrayList<File> filtered_list=new ArrayList<>();
	
	
		
		for(int index=0;index<vehicle_dirs.length;index++)
		{
			
			if(vehicle_dirs[index].isDirectory())
			{
				
				filtered_list.add(vehicle_dirs[index]);
				
				
				
			}
			
			
			
			
		}
		
		
		
		return filtered_list.toArray(new File[filtered_list.size()]);
		
		
		
		
	}
	
	
	private String[] fileFilter(String path)
	{
		
		
		File file=new File(path+File.separator);
		
		File[] files=file.listFiles();
		
		
		ArrayList<String> filered_files=new ArrayList<>();
		for(int index=0;index<files.length;index++)
		{
			Log.e("files", files[index].getName());
			if(!files[index].isDirectory())
			{
				
				filered_files.add(files[index].getName());	
			}
			
			
		}
		
		
		return filered_files.toArray(new String[filered_files.size()]);
	}
	
	
	
	
	
	
	public void createThumbs(String filePath, String thumbPath, String[] files_name) 
	{

		/*Log.e("file path", filePath);
		Log.e("thumb path", thumbPath);
	
		for(int i=0;i<files_name.length;i++)
		{
			Log.e("file name", ""+files_name[i]);		
		}*/
	
		
		
		File check_dir;
		if(files_name.length>0)
		{
		
		for(int i=0;i<files_name.length;i++)
		{
			
			
		check_dir=new File(filePath+File.separator+files_name[i]);
		
		if(!check_dir.isDirectory())
		{
			
		
	/*	Log.e("File path",filePath+File.separator+files_name[i]);
		Log.e("File thumb path",thumbPath);*/
		
	Bitmap thumbnails = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath+File.separator+files_name[i]), 100, 100);

	  if (thumbPath != null && files_name[i] != null) {

		        
		        try {

		        	
		            File file = new File(thumbPath, files_name[i]);
		            OutputStream fOut = null;
		            fOut = new FileOutputStream(file);
		            thumbnails.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		            fOut.flush();
		            fOut.close();
		            Log.e("Job status","Finished");

		        } catch (Exception ex) {

		        ex.printStackTrace();
		        }


		    }

		}
		else
		{
			
			if(check_dir.getName()!="thumb")
			{
			
				File inner_folder=new File(filePath+File.separator+files_name[i]+File.separator);
				
				File[] inner_files_names=inner_folder.listFiles();
				
				
				for(int index=0;index<inner_files_names.length;index++)
				{
					
					
					
					if(!inner_files_names[index].isDirectory())
					{
						
						
						
						
	Bitmap thumbnails = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath+File.separator+files_name[i]+File.separator+inner_files_names[index].getName()), 100, 100);
	Log.e("else decode", filePath+File.separator+files_name[i]+File.separator+inner_files_names[index].getName());

					
						  if (thumbPath != null && inner_files_names[index].getName() != null) {

							        
							        try {	
							        	
							        	Log.e("thumb path", thumbPath.substring(1, thumbPath.length()-5)+files_name[i]+File.separator+"thumb/"+inner_files_names[index].getName());
							        
							            File file = new File(thumbPath.substring(1, thumbPath.length()-5)+files_name[i]+File.separator+"thumb/"+inner_files_names[index].getName());
							            OutputStream fOut = null;
							            fOut = new FileOutputStream(file);
							            thumbnails.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
							            fOut.flush();
							            fOut.close();
							            Log.e("Job status","Finished");

							        } catch (Exception ex) {

							        ex.printStackTrace();
							        }


							    }
						
						
						
						
					}
					
					
					
					
				}
				
			
			
			
			}
			
			
			
		}
	}
		
		
	}
		else
		{

		}
		
		}
	
	private void deleteDirectory(File file)
	{
		
			if (file.isDirectory()) {
		        for (File sub : file.listFiles()) {
		        	deleteDirectory(sub);
		        }
		    }
		  file.delete();
		
		
	}

	
}
