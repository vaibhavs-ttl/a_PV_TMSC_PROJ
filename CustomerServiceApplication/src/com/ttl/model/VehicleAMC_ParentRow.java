package com.ttl.model;

import java.util.ArrayList;

public class VehicleAMC_ParentRow {

	public String amc_no;
	public String amc_status;
	public String amc_type;
	public String start_date;
	public String end_date;
	public String start_km;
	public String end_km;
	public String description;
	
	
	

	public String getAmc_status() {
		return amc_status;
	}

	public void setAmc_status(String amc_status) {
		this.amc_status = amc_status;
	}

	public String getEnd_km() {
		return end_km;
	}

	public void setEnd_km(String end_km) {
		this.end_km = end_km;
	}

	public String getAmc_type() {
		return amc_type;
	}

	public void setAmc_type(String amc_type) {
		this.amc_type = amc_type;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		String date[] = start_date.split("T");
		this.start_date = date[0];
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		String date[] = end_date.split("T");
		this.end_date = date[0];
	
	}

	public String getStart_km() {
		return start_km;
	}

	public void setStart_km(String start_km) {
		this.start_km = start_km;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<VehicleAMC_ChildRow> children;

	public String getAmc_no() {
		return amc_no;
	}

	public void setAmc_no(String amc_no) {
		this.amc_no = amc_no;
	}

	public ArrayList<VehicleAMC_ChildRow> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<VehicleAMC_ChildRow> children) {
		this.children = children;
	}

}
