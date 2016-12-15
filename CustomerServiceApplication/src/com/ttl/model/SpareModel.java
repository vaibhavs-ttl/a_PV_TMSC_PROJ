package com.ttl.model;

import java.io.Serializable;

public class SpareModel implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String modelName;
	private String partNumber;
	private String partDescription;
	private String defaultQty;
	private String UOM;
	private String UMRP;
	private String type;
	public int checkedPosition=0;
	public boolean selected_state;
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getPartDescription() {
		return partDescription;
	}
	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}
	public String getDefaultQty() {
		return defaultQty;
	}
	public void setDefaultQty(String defaultQty) {
		this.defaultQty = defaultQty;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	public String getUMRP() {
		return UMRP;
	}
	public void setUMRP(String uMRP) {
		UMRP = uMRP;
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
	public boolean isSelected_state() {
		return selected_state;
	}
	public void setSelected_state(boolean selected_state) {
		this.selected_state = selected_state;
	}
	
	
	
}
