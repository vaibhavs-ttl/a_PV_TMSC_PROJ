package com.ttl.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.squareup.picasso.Picasso;

public class ColorConstants {

	
	
//private static String colorCodes[]={"#00CED1","#FFDEAD","#2F4F4F","#DA70D6","#F08080","#FFA500","#D2691E","#BC8F8F","#F4A460"};
	
private static List<String> colorCodes;


	


	
	/*public static final String TURQUOISE="#00CED1";
	public static final String NAVAJO_WHITE="#FFDEAD";
	public static final String SLATE_GRAY="#2F4F4F";
	public static final String ORCHID="#DA70D6";
	public static final String LIGHT_CORAL="#F08080";
	public static final String ORANGE="#FFA500";
	public static final String CHOCOLATE="#D2691E";
	public static final String STEEL_BLUE="#B0C4DE";
	public static final String ROSY_BROWN="#BC8F8F";
	public static final String SANDY_BROWN="#F4A460";*/



public static List<String> getColors()
{

	colorCodes=new ArrayList<String>();
	colorCodes.add("#88cc00");
	colorCodes.add("#b84dff");
	colorCodes.add("#008080");
    colorCodes.add("#b36b00");
	

	
	
	Collections.shuffle(colorCodes);	
	
	return colorCodes;


}







}
