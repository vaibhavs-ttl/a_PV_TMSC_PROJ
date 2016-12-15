package com.ttl.model;

import java.util.ArrayList;

public class Complaint_Registered_Parent {
	
	
	public int complID;
	public String complaint_no;
	public String complaint_reg_no;
	public String complaint_date;
	public String model;
	public String primary_area;
	public String sub_area;
	public String problem_area;
	public String userId;
	public ArrayList<Complaint_Registered_Child> children;
	
	
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getComplID() {
		return complID;
	}

	public void setComplID(int i) {
		this.complID = i;
	}

	public String getComplaint_reg_no() {
		return complaint_reg_no;
	}

	public void setComplaint_reg_no(String complaint_reg_no) {
		this.complaint_reg_no = complaint_reg_no;
	}

	public String getComplaint_no() {
		return complaint_no;
	}

	public void setComplaint_no(String complaint_no) {
		this.complaint_no = complaint_no;
	}

	

	public String getComplaint_date() {
		return complaint_date;
	}

	public void setComplaint_date(String complaint_date) {
		this.complaint_date = complaint_date;
	}

	public ArrayList<Complaint_Registered_Child> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Complaint_Registered_Child> children) {
		this.children = children;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPrimary_area() {
		return primary_area;
	}

	public void setPrimary_area(String primary_area) {
		this.primary_area = primary_area;
	}

	public String getSub_area() {
		return sub_area;
	}

	public void setSub_area(String sub_area) {
		this.sub_area = sub_area;
	}

	public String getProblem_area() {
		return problem_area;
	}

	public void setProblem_area(String problem_area) {
		this.problem_area = problem_area;
	}
	

	
	

}
