package com.ttl.model;

import java.util.ArrayList;

public class VehicleAgreement_ParentRow {

	public String agreement_no;
	public String agg_ID;
	public String agree_name;
	public String status;
	public String agreement_amt;
	public String mech_reamaing;
	public String towing_reamaing;
	public String mech_avail;
	public String towing_avail;
	
	

	public String getAgg_ID() {
		return agg_ID;
	}

	public void setAgg_ID(String agg_ID) {
		this.agg_ID = agg_ID;
	}

	public String getAgree_name() {
		return agree_name;
	}

	public void setAgree_name(String agree_name) {
		this.agree_name = agree_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAgreement_amt() {
		return agreement_amt;
	}

	public void setAgreement_amt(String agreement_amt) {
		this.agreement_amt = agreement_amt;
	}

	public String getMech_reamaing() {
		return mech_reamaing;
	}

	public void setMech_reamaing(String mech_reamaing) {
		this.mech_reamaing = mech_reamaing;
	}

	public String getTowing_reamaing() {
		return towing_reamaing;
	}

	public void setTowing_reamaing(String towing_reamaing) {
		this.towing_reamaing = towing_reamaing;
	}

	public String getMech_avail() {
		return mech_avail;
	}

	public void setMech_avail(String mech_avail) {
		this.mech_avail = mech_avail;
	}

	public String getTowing_avail() {
		return towing_avail;
	}

	public void setTowing_avail(String towing_avail) {
		this.towing_avail = towing_avail;
	}

	public ArrayList<VehicleAgreement_ChildRow> children;

	public String getAgreement_no() {
		return agreement_no;
	}

	public void setAgreement_no(String agreement_no) {
		this.agreement_no = agreement_no;
	}

	public ArrayList<VehicleAgreement_ChildRow> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<VehicleAgreement_ChildRow> children) {
		this.children = children;
	}

}
