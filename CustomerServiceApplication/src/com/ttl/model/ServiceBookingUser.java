package com.ttl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceBookingUser implements Parcelable{
 public String fname;
 public String lname;
 public String address;
 public String phoneno;
 public String email;
 public String PL;
 public String PPL;
 public String REGISTRATIONNUMBER;
 public String CHASSISNUMBER;
 public String contact_id;
 
public String getContact_id() {
	return contact_id;
}
public void setContact_id(String contact_id) {
	this.contact_id = contact_id;
}
public void setREGISTRATIONNUMBER(String rEGISTRATIONNUMBER) {
		REGISTRATIONNUMBER = rEGISTRATIONNUMBER;
	} 
public String getREGISTRATIONNUMBER() {
	return REGISTRATIONNUMBER;
}

public String getCHASSISNUMBER() {
	return CHASSISNUMBER;
}
public void setCHASSISNUMBER(String cHASSISNUMBER) {
	CHASSISNUMBER = cHASSISNUMBER;
}
public String getLname() {
	return lname;
}
public void setLname(String lname) {
	this.lname = lname;
}
public String getFname() {
	return fname;
}
public void setFname(String fname) {
	this.fname = fname;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getPhoneno() {
	return phoneno;
}
public void setPhoneno(String phoneno) {
	this.phoneno = phoneno;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPL() {
	return PL;
}
public void setPL(String pL) {
	PL = pL;
}
public String getPPL() {
	return PPL;
}
public void setPPL(String pPL) {
	PPL = pPL;
}
@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void writeToParcel(Parcel dest, int flags) {
	// TODO Auto-generated method stub
	
}




}
