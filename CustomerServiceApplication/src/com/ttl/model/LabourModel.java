package com.ttl.model;

import java.io.Serializable;

public class LabourModel implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String modelName;
	public String labourCode;
	public String labourDescription;
	public String defaultQty;
	public String billingHours;
	public String labourType;
	public String type;
	public boolean checked_state=false;
	public int checkedPosition=0;
	
	public boolean selected_state;
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getLabourCode() {
		return labourCode;
	}
	public void setLabourCode(String labourCode) {
		this.labourCode = labourCode;
	}
	public String getLabourDescription() {
		return labourDescription;
	}
	public void setLabourDescription(String labourDescription) {
		this.labourDescription = labourDescription;
	}
	public String getDefaultQty() {
		return defaultQty;
	}
	public void setDefaultQty(String defaultQty) {
		this.defaultQty = defaultQty;
	}
	public String getBillingHours() {
		return billingHours;
	}
	public void setBillingHours(String billingHours) {
		this.billingHours = billingHours;
	}
	public String getLabourType() {
		return labourType;
	}
	public void setLabourType(String labourType) {
		this.labourType = labourType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCheckedPosition() {
		return checkedPosition;
	}
	public void setCheckedPosition(int checkedPosition) {
		this.checkedPosition = checkedPosition;
	}
	public boolean isChecked_state() {
		return checked_state;
	}
	public void setChecked_state(boolean checked_state) {
		this.checked_state = checked_state;
	}
	public boolean isSelected_state() {
		return selected_state;
	}
	public void setSelected_state(boolean selected_state) {
		this.selected_state = selected_state;
	}
	
	
	
	
	
}
