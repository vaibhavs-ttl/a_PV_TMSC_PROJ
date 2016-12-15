package com.ttl.model;

import java.util.ArrayList;

public class Service_Booking_History_Parent {
	public int ID;
	public String userId;
	public String complaint_reg_no;
	public String complaint_sr_no;
	public String complaint_date;
	public String kms;
	public String model;
	public String date_of_booking;
	public String booked_for_time;
	public String booked_for_dealer;
	public String service_type;
	public String msv_flag;
	
	public String getMsv_flag() {
		return msv_flag;
	}
	public void setMsv_flag(String msv_flag) {
		this.msv_flag = msv_flag;
	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ArrayList< Service_Booking_History_Child> children;

	public String getComplaint_reg_no() {
		return complaint_reg_no;
	}

	public void setComplaint_reg_no(String complaint_reg_no) {
		this.complaint_reg_no = complaint_reg_no;
	}

	
	public String getComplaint_date() {
		return complaint_date;
	}

	public void setComplaint_date(String complaint_date) {
		this.complaint_date = complaint_date;
	}

	public ArrayList<Service_Booking_History_Child> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Service_Booking_History_Child> children) {
		this.children = children;
	}

	public String getComplaint_sr_no() {
		return complaint_sr_no;
	}

	public void setComplaint_sr_no(String complaint_sr_no) {
		this.complaint_sr_no = complaint_sr_no;
	}

	public int getID() {
		return ID;
	}

	public void setID(int i) {
		ID = i;
	}

	//CHILD ELEMENTS
	
	
	public String getKms() {
		return kms;
	}

	public void setKms(String kms) {
		this.kms = kms;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDate_of_booking() {
		return date_of_booking;
	}

	public void setDate_of_booking(String date_of_booking) {
		this.date_of_booking = date_of_booking;
	}

	public String getBooked_for_time() {
		return booked_for_time;
	}

	public void setBooked_for_time(String booked_for_time) {
		this.booked_for_time = booked_for_time;
	}

	public String getBooked_for_dealer() {
		return booked_for_dealer;
	}

	public void setBooked_for_dealer(String booked_for_dealer) {
		this.booked_for_dealer = booked_for_dealer;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}
	


}
