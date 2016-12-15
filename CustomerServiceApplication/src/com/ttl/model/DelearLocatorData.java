package com.ttl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DelearLocatorData implements Parcelable {
	public String delear_ID;
	public String delear_Name;
	public String delear_DIV_COMMON_NAME;
	public String delear_DIV_ADDRESS_1;
	public String delear_DIV_ADDRESS_2;
	public String delear_DIV_STATE;
	public String delear_DIV_CITY;
	public String delear_DIV_COUNTRY;
	public String delear_DIV_ZIP_CODE;
	public String delear_CATEGORY_OF_LOCATION;
	public String delear_DIV_PHONE;
	public String delear_DIV_EMAIL;
	public String delear_WEEKLY_OFF;
	public String delear_SHOWROOM;
	public String delear_WORKSHOP;
	public String delear_SPAREPARTS;
	public String delear_ACCESSORIES;
	public String delear_TESTDR;
	public String delear_TMA;
	public String delear_LATITUDE;
	public String delear_LONGITUDE;
	
	public DelearLocatorData() {
		super();
		delear_ID="";
		delear_Name="";
		delear_DIV_COMMON_NAME="";
		delear_DIV_ADDRESS_1="";
		delear_DIV_ADDRESS_2="";
		delear_DIV_STATE="";
		delear_DIV_CITY="";
		delear_DIV_COUNTRY="";
		delear_DIV_ZIP_CODE="";
		delear_CATEGORY_OF_LOCATION="";
		delear_DIV_PHONE="";
		delear_DIV_EMAIL="";
		delear_WEEKLY_OFF="";
		delear_SHOWROOM="";
		delear_WORKSHOP="";
		delear_SPAREPARTS="";
		delear_ACCESSORIES="";
		delear_TESTDR="";
		delear_TMA="";
		delear_LATITUDE="";
		delear_LONGITUDE="";
	}

	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(delear_ID);
		dest.writeString(delear_Name);
		dest.writeString(delear_DIV_COMMON_NAME);
		dest.writeString(delear_DIV_ADDRESS_1);
		dest.writeString(delear_DIV_ADDRESS_2);
		dest.writeString(delear_DIV_STATE);
		dest.writeString(delear_DIV_CITY);
		dest.writeString(delear_DIV_COUNTRY);
		dest.writeString(delear_DIV_ZIP_CODE);
		dest.writeString(delear_CATEGORY_OF_LOCATION);
		dest.writeString(delear_DIV_PHONE);
		dest.writeString(delear_DIV_EMAIL);
		dest.writeString(delear_WEEKLY_OFF);
		dest.writeString(delear_SHOWROOM);
		dest.writeString(delear_WORKSHOP);
		dest.writeString(delear_SPAREPARTS);
		dest.writeString(delear_ACCESSORIES);
		dest.writeString(delear_TESTDR);
		dest.writeString(delear_TMA);
		dest.writeString(delear_LATITUDE);
		dest.writeString(delear_LONGITUDE);
		
	}
	
	public static final Parcelable.Creator<DelearLocatorData> CREATOR =

			new Parcelable.Creator<DelearLocatorData>() {
				@Override
				public DelearLocatorData createFromParcel(Parcel source) {
					return new DelearLocatorData(source);
				}

				@Override
				public DelearLocatorData[] newArray(int size) {
					return new DelearLocatorData[size];
				}
			};

			private DelearLocatorData(Parcel in) {
				delear_ID = in.readString();
				delear_Name = in.readString();
				delear_DIV_COMMON_NAME = in.readString();
				delear_DIV_ADDRESS_1 = in.readString();
				delear_DIV_ADDRESS_2 = in.readString();
				delear_DIV_STATE = in.readString();
				delear_DIV_CITY = in.readString();
				delear_DIV_COUNTRY = in.readString();
				delear_DIV_ZIP_CODE=in.readString();
				delear_CATEGORY_OF_LOCATION=in.readString();
				delear_DIV_PHONE=in.readString();
				delear_DIV_EMAIL=in.readString();
				delear_WEEKLY_OFF=in.readString();
				delear_SHOWROOM=in.readString();
				delear_WORKSHOP=in.readString();
				delear_SPAREPARTS=in.readString();
				delear_ACCESSORIES=in.readString();
				delear_TESTDR=in.readString();
				delear_TMA=in.readString();
				delear_LATITUDE=in.readString();
				delear_LONGITUDE=in.readString();
			}
			
			

}
