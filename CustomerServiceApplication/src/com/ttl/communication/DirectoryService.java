package com.ttl.communication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class DirectoryService extends Service{

	private BackgroundTask backgroundTask;
	private File[] dirs;
	private File baseURL;
	private String[] dirsName;
	private File directory;
	
	private String[] directories={"license","insurance","aadhar","puc","voterid","passport","vehicalpicture","pancard","others"};
	
	private String sub_directories="/thumb";
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

	
		
	
		Log.e("service started", "service started successfully");
		
		
		backgroundTask=new BackgroundTask();
		backgroundTask.execute();
	//	stopSelf();
		
		
		
		
		return START_STICKY;
	}





class BackgroundTask extends AsyncTask<Void, Void, Void>
{
	
		
	String thumb_path;
@Override
protected Void doInBackground(Void... params) {

	
	try {
	
		baseURL=new File(Environment
	            .getExternalStorageDirectory().getAbsolutePath()
	            + "/CustomerSocialAppDocument/");
	
		
				dirs=baseURL.listFiles();
		
		
		
		
			Arrays.sort(dirs);
			
			for(int dirIndex=0;dirIndex<dirs.length;dirIndex++) // to get list of user directories
			{
				
				
				Log.e("user dir name",dirs[dirIndex].getName());// printing user directory name
				
				directory=new File(baseURL+File.separator+dirs[dirIndex].getName()+File.separator); 
				
			
				 
		
				File subDirs[]=directory.listFiles(); // getting list of sub directories resides inside user directory
				
				
				deleteThumnailsDir(subDirs,directory.getName());
				
				for(int sub_dir_index=0;sub_dir_index<subDirs.length;sub_dir_index++)
				{
					String path=baseURL+File.separator+dirs[dirIndex].getName()+File.separator+subDirs[sub_dir_index].getName();
					File folders=new File(path+File.separator);
					thumb_path=path+File.separator+"thumb";
					/*if(folders.isDirectory())
					{
					Log.e("sub dir name",path);// printing user sub dirs directory name
					if(subDirs[sub_dir_index].isDirectory())
					{
					
						thumb_path=path+File.separator+"thumb";
						File thumb_dir=new File(thumb_path);
						
						if(thumb_dir.isDirectory())
						{
						Log.e("thumb dir name","thumb exist");// printing user sub thumb dir directory name
							
						deleteDirectory(thumb_dir); // removing thumb directory if exist
		
						thumb_dir.mkdirs(); // creating new directory after removing the old directory.
						
						
						}
						
					}
					else
					{
						createThumbs(path, thumb_path, folders.getName());
					}
				 
				}
					
					
					*/
					
					
					
					if(!folders.isDirectory())
					{
					createThumbs(path, thumb_path, folders.getName());
					}
				}
				
				
				
			
			
		}
		
		
	} catch (Exception e) {
	
		e.printStackTrace();
	}
	
	return null;
}



}


private void deleteThumnailsDir(File[] subDirs,String dirName)
{
	
	
	
	for(int index=0;index<subDirs.length;index++)
	{
		
		
		
		String path=baseURL+File.separator+dirName+File.separator+subDirs[index].getName();
		File folders=new File(path+File.separator);
	
		
		if(folders.isDirectory())
		{
		
		if(subDirs[index].isDirectory())
		{
		
			String thumb_path=path+File.separator+"thumb";
			File thumb_dir=new File(thumb_path);
			
			if(thumb_dir.isDirectory())
			{
			Log.e("folder deleted","thumbs deleted");// printing user sub thumb dir directory name
				
			deleteDirectory(thumb_dir); // removing thumb directory if exist

			thumb_dir.mkdirs(); // creating new directory after removing the old directory.
			
			
			}
			
		}

		
		
		
		
		

	
	
	
	

		

		}
		}
	
	}






// This function takes original image name and call createThumbs method to create thumb nails.


public String getFiles(String folder_path)
{

File[] files;
File temp_file;
    try {


        File f = new File(folder_path);

            files = f.listFiles();
            Arrays.sort(files);

            for (int i = 0; i < files.length; i++) {
            	temp_file = files[i];

              Log.v("file name",temp_file.getName());
//                createThumbs(folder_path,license_Thumb_path,file.getName());



        }
    }
    catch (Exception ex)
    {
        ex.printStackTrace();
    }




    return  null;


}











// This function creates thumnails of existing images

public void createThumbs(String filePath, String thumbPath, String filename) {

 Log.v("create thumbs",filePath);
    Bitmap thumbnails = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath+"/"+filename), 100, 100);


    if (thumbPath != null && filename != null) {

        
        try {

            File file = new File(thumbPath, filename);
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            thumbnails.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            Log.v("Job status","Finished");

        } catch (Exception ex) {

        ex.printStackTrace();
        }


    }


}






// To delete directory files and then delete directory once it gets emptied

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
