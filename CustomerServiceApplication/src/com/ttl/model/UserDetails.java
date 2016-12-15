package com.ttl.model;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDetails {

	static String first_name, last_name , gender="" , email_id ="", contact_number="" ,alt_contact_number="",
	address="" , city="" , pincode="" , state="" , district ="",user_id="" , password="" , photourl="",sessionId="",message="";
	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		UserDetails.message = message;
	}

	static ArrayList<HashMap<String, String>> user_vehicle_model  = new ArrayList<HashMap<String, String>>();
	
	static ArrayList<HashMap<String, String>> regNumberList  = new ArrayList<HashMap<String, String>>();
	static String preffered_dealer_name,preffered_dealer_address,preffered_dealer_number,preffered_dealer_email,preffered_dealer_latitude,preffered_dealer_longitude;
	
	static ArrayList<VehicleAgreement_ParentRow> lst_agreeParent = new ArrayList<VehicleAgreement_ParentRow>();
	static ArrayList<VehicleAMC_ParentRow> lst_amcParent = new ArrayList<VehicleAMC_ParentRow>(); 
	
	static ArrayList<HashMap<String, ArrayList<VehicleAMC_ParentRow>>> allDataAMC = new ArrayList<HashMap<String, ArrayList<VehicleAMC_ParentRow>>>();
	static ArrayList<HashMap<String, ArrayList<VehicleAgreement_ParentRow>>> allDataagreement = new ArrayList<HashMap<String, ArrayList<VehicleAgreement_ParentRow>>>();
	
	/*static ArrayList<HashMap<String, String>> gcmlist = new ArrayList<HashMap<String, String>>();
	
	
	public static ArrayList<HashMap<String, String>> getGcmlist() {
		return gcmlist;
	}

	public static void setGcmlist(ArrayList<HashMap<String, String>> gcmlist) {
		UserDetails.gcmlist = gcmlist;
	}*/

	public static ArrayList<HashMap<String, ArrayList<VehicleAgreement_ParentRow>>> getAllDataagreement() {
		return allDataagreement;
	}

	public static String getSeeionId() {
		return sessionId;
	}

	public static void setSeeionId(String seeionId) {
		UserDetails.sessionId = seeionId;
	}

	public static void setAllDataagreement(
			ArrayList<HashMap<String, ArrayList<VehicleAgreement_ParentRow>>> allDataagreement) {
		UserDetails.allDataagreement = allDataagreement;
	}

	public static ArrayList<HashMap<String, ArrayList<VehicleAMC_ParentRow>>> getAllDataAMC() {
		return allDataAMC;
	}

	public static void setAllDataAMC(
			ArrayList<HashMap<String, ArrayList<VehicleAMC_ParentRow>>> allDataAMC) {
		UserDetails.allDataAMC = allDataAMC;
	}

	public static ArrayList<VehicleAMC_ParentRow> getLst_amcParent() {
		return lst_amcParent;
	}

	public static void setLst_amcParent(
			ArrayList<VehicleAMC_ParentRow> lst_amcParent) {
		UserDetails.lst_amcParent = lst_amcParent;
	}

	public static ArrayList<VehicleAgreement_ParentRow> getLst_agreeParent() {
		return lst_agreeParent;
	}

	public static void setLst_agreeParent(
			ArrayList<VehicleAgreement_ParentRow> lst_agreeParent) {
		UserDetails.lst_agreeParent = lst_agreeParent;
	}

	public static String getFirst_name() {
		return first_name;
	}

	public static void setFirst_name(String first_name) {
		UserDetails.first_name = first_name;
	}

	public static String getLast_name() {
		return last_name;
	}

	public static void setLast_name(String last_name) {
		UserDetails.last_name = last_name;
	}

	public static String getGender() {
		return gender;
	}

	public static void setGender(String gender) {
		UserDetails.gender = gender;
	}

	public static String getEmail_id() {
		return email_id;
	}

	public static void setEmail_id(String email_id) {
		UserDetails.email_id = email_id;
	}

	public static String getContact_number() {
		return contact_number;
	}

	public static void setContact_number(String contact_number) {
		UserDetails.contact_number = contact_number;
	}

	public static String getAlt_contact_number() {
		return alt_contact_number;
	}

	public static void setAlt_contact_number(String alt_contact_number) {
		UserDetails.alt_contact_number = alt_contact_number;
	}

	public static String getAddress() {
		return address;
	}

	public static void setAddress(String address) {
		UserDetails.address = address;
	}

	public static String getCity() {
		return city;
	}

	public static void setCity(String city) {
		UserDetails.city = city;
	}

	public static String getPincode() {
		return pincode;
	}

	public static void setPincode(String pincode) {
		UserDetails.pincode = pincode;
	}

	public static String getState() {
		return state;
	}

	public static void setState(String state) {
		UserDetails.state = state;
	}

	public static String getDistrict() {
		return district;
	}

	public static void setDistrict(String district) {
		UserDetails.district = district;
	}

	public static String getUser_id() {
		return user_id;
	}

	public static void setUser_id(String user_id) {
		UserDetails.user_id = user_id;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		UserDetails.password = password;
	}

	public static String getPhotourl() {
		return photourl;
	}

	public static void setPhotourl(String photourl) {
		UserDetails.photourl = photourl;
	}

	public ArrayList<HashMap<String, String>> getRegNumberList() {
		return regNumberList;
	}

	public void setRegNumberList(ArrayList<HashMap<String, String>> regNumberList) {
		this.regNumberList = regNumberList;
	}

	public ArrayList<HashMap<String, String>> getUserVehicleModel() {
		return user_vehicle_model;
	}

	public void setUserVehicleModel(ArrayList<HashMap<String, String>> user_vehicle_model) {
		this.user_vehicle_model = user_vehicle_model;
	}


	
	
	
	
	public static String getPreffered_dealer_name() {
		return preffered_dealer_name;
	}

	public static void setPreffered_dealer_name(String preffered_dealer_name) {
		UserDetails.preffered_dealer_name = preffered_dealer_name;
	}

	public static String getPreffered_dealer_address() {
		return preffered_dealer_address;
	}

	public static void setPreffered_dealer_address(String preffered_dealer_address) {
		UserDetails.preffered_dealer_address = preffered_dealer_address;
	}

	public static String getPreffered_dealer_number() {
		return preffered_dealer_number;
	}

	public static void setPreffered_dealer_number(String preffered_dealer_number) {
		UserDetails.preffered_dealer_number = preffered_dealer_number;
	}

	public static String getPreffered_dealer_email() {
		return preffered_dealer_email;
	}

	public static void setPreffered_dealer_email(String preffered_dealer_email) {
		UserDetails.preffered_dealer_email = preffered_dealer_email;
	}

	public static String getPreffered_dealer_latitude() {
		return preffered_dealer_latitude;
	}

	public static void setPreffered_dealer_latitude(String preffered_dealer_latitude) {
		UserDetails.preffered_dealer_latitude = preffered_dealer_latitude;
	}

	public static String getPreffered_dealer_longitude() {
		return preffered_dealer_longitude;
	}

	public static void setPreffered_dealer_longitude(
			String preffered_dealer_longitude) {
		UserDetails.preffered_dealer_longitude = preffered_dealer_longitude;
	}

	
	
	
}
