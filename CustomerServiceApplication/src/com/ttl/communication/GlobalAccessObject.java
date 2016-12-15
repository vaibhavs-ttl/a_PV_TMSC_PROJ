package com.ttl.communication;

import java.util.ArrayList;

import android.util.Log;

import com.ttl.model.DownloadModel;
import com.ttl.model.LabourModel;
import com.ttl.model.LabourSpareModel;
import com.ttl.model.SpareModel;

public class GlobalAccessObject {

	
	
	private static ArrayList<LabourModel> labour_obj;
	private static ArrayList<SpareModel> spare_obj;
	private static boolean isSpareModelLoaded=false;
	private static boolean isLabourModelLoaded=false;
	private static ArrayList<LabourSpareModel> labour_spare_obj;
	private static boolean loaded=false;
	
	public static boolean isChanged=false; 
	public static boolean spareChanged=false;
	private static ArrayList<DownloadModel> downloadModels;


	public static ArrayList<LabourModel> getLabour_obj() {
		return labour_obj;
	}

	public static void setLabour_obj(ArrayList<LabourModel> labour_obj) {
		GlobalAccessObject.labour_obj = labour_obj;
	}
	
	
	public static void NullifyLabour_obj()
	{
		
		
		if (labour_obj!=null) {
			
			labour_obj=null;
			
		}
		
		
	}
	
	

	public static ArrayList<SpareModel> getSpare_obj() {
		return spare_obj;
	}

	public static void setSpare_obj(ArrayList<SpareModel> spare_obj) {
		GlobalAccessObject.spare_obj = spare_obj;
	}
	
	
	public static void NullifySpare_obj()
	{
		
		
		if (spare_obj!=null) {
			
			spare_obj=null;
			
		}
		
		
	}

	public static boolean isSpareModelLoaded() {
		return isSpareModelLoaded;
	}

	public static void setSpareModelLoaded(boolean isSpareModelLoaded) {
		GlobalAccessObject.isSpareModelLoaded = isSpareModelLoaded;
	}

	public static boolean isLabourModelLoaded() {
		return isLabourModelLoaded;
	}

	public static void setLabourModelLoaded(boolean isLabourModelLoaded) {
		GlobalAccessObject.isLabourModelLoaded = isLabourModelLoaded;
	}

	
	
	
	
	public static ArrayList<LabourSpareModel> getLabour_spare_obj() {
		return labour_spare_obj;
	}

	public static void setLabour_spare_obj(ArrayList<LabourSpareModel> labour_spare_obj) {
		GlobalAccessObject.labour_spare_obj = labour_spare_obj;
	}

	
	
	public static void Nullifylabour_spare_obj()
	{
		
		
		if (labour_spare_obj!=null) {
			
			labour_spare_obj=null;
			
		}
		
		
	}

	public static boolean isLoaded() {
		return loaded;
	}

	public static void setLoaded(boolean loaded) {
		GlobalAccessObject.loaded = loaded;
	}
	
	
	
	
	
	
	
	//-----------------------------------------------------------------------------------------------------------
	
	
	public static void setLabour_obj(LabourModel model)
	{
		Log.v("setLabour called", "called");
		boolean exist=false;
		if (labour_obj==null) {
			
			labour_obj=new ArrayList<>();
			labour_obj.add(model);
			isChanged=true;
			Log.v("labour obj", "null found");
		}
		else
		{
	
			for(int index=0;index<labour_obj.size();index++)
			{
			
				if (labour_obj.get(index).getLabourDescription().equalsIgnoreCase(model.getLabourDescription())) {
					
					Log.v("inside","for if");
					
					exist=true;
					break;
				}
				
				
			}
			
			if (exist==false) {
				labour_obj.add(model);		
				isChanged=true;
				Log.v("last if", "called");
			}
		
			
			
		}
		
	
	
	}
	
	
	
	public static void removeLabour_obj(LabourModel model)
	{
		
		boolean exist=false;
		if (labour_obj==null) {
			
			labour_obj=new ArrayList<>();
		//	labour_obj.add(model);
		}
		else
		{
	
			for(int index=0;index<labour_obj.size();index++)
			{
			
				if (labour_obj.get(index).getLabourDescription().equalsIgnoreCase(model.getLabourDescription())) {
					
					exist=true;
					break;
				}
				
				
			}
			if (exist) {
				labour_obj.remove(model);		
				isChanged=false;
			}
		
			
			
		}
		
		
		
		
		
	
	}
	
	
	
	public static boolean removeLabourByName(String name)
	{
		
		boolean check=false;
		
		if (name!=null && labour_obj!=null) {
			
			
			
			
			
			for (int i = 0; i < labour_obj.size(); i++) {
				
				
				
				if (name.equalsIgnoreCase(labour_obj.get(i).getLabourDescription())) {
			
					
					Log.v("value", ""+labour_obj.get(i).getLabourDescription());
					
					labour_obj.remove(i);
					check=true;
					break;
					
				}
				
				
			}
			
			
			
			
			
			
			
			
			
			
			return check;
			
		}
		else
		{
		
			return check;
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//// Spare 
	
	
	
	
	public static void setSpare_obj(SpareModel model)
	{
		Log.v("setSpare_obj called", "called");
		boolean exist=false;
		if (spare_obj==null) {
			
			spare_obj=new ArrayList<>();
			spare_obj.add(model);
			spareChanged=true;
			Log.v("labour obj", "null found");
		}
		else
		{
	
			for(int index=0;index<spare_obj.size();index++)
			{
			
				if (spare_obj.get(index).getPartDescription().equalsIgnoreCase(model.getPartDescription())) {
					
					Log.v("inside","for if");
					
					exist=true;
					break;
				}
				
				
			}
			
			if (exist==false) {
				spare_obj.add(model);		
				spareChanged=true;
				Log.v("last if", "called");
			}
		
			
			
		}
		
	
	
	}
	
	
	
	public static void removeSpare_obj(SpareModel model)
	{
		
		boolean exist=false;
		if (spare_obj==null) {
			
			spare_obj=new ArrayList<>();
		//	labour_obj.add(model);
		}
		else
		{
	
			for(int index=0;index<spare_obj.size();index++)
			{
			
				if (spare_obj.get(index).getPartDescription().equalsIgnoreCase(model.getPartDescription())) {
					
					exist=true;
					break;
				}
				
				
			}
			if (exist) {
				spare_obj.remove(model);		
				spareChanged=false;
			}
		
			
			
		}
		
		
		
		
		
	
	}
	
	
	
	
	public static boolean removeSpareByName(String name)
	{
		
		boolean check=false;
		
		if (name!=null && spare_obj!=null) {
			
			
			
			
			
			for (int i = 0; i < spare_obj.size(); i++) {
				
				
				
				if (name.equalsIgnoreCase(spare_obj.get(i).getPartDescription())) {
			
					
					Log.v("value", ""+spare_obj.get(i).getPartDescription());
					
					spare_obj.remove(i);
					check=true;
					break;
					
				}
				
				
			}
			
			
			
			
			
			
			
			
			
			
			return check;
			
		}
		else
		{
		
			return check;
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Downloads
	
	
	
	
	
	
	public static void addFile(DownloadModel model){
	
		
		if (downloadModels==null) {
			
			downloadModels=new ArrayList<>();
			downloadModels.add(model);
		}
		else
		{
			downloadModels.add(model);
		}
		
		
		
	}
	
	
public static void removeFile(DownloadModel model){
	
		
	if (downloadModels!=null) {
		downloadModels.remove(model);
	}
	
		
		
	}
	
	
public static ArrayList<DownloadModel> getFiles(){
	

	
	return downloadModels;
	
	
}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
