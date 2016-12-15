package com.ttl.model;

import android.widget.ListView;

public class ServiceHistory {
	public String SR_HISTORY_NUM ;
	public String CHASSIS_NO ;
	public String REG_NUM;
	public String CLOSE_DATE;
	public String SERVICE_AT_DEALER;
	public String SR_TYPE;
	public String INVC_AMT;
	public String ODOMTR_RDNG;
	public String DEALER_CONTACT_NUMBER;
	public String CITY;
	public ListView lst_complaintJC;
	public String INVOICE_STATUS;
	public String ORDER_ID;
	
	
	public String getORDER_ID() {
		return ORDER_ID;
	}
	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}
	public String getCITY() {
		return CITY;
	}
	public void setCITY(String cITY) {
		CITY = cITY;
	}
	public ListView getLst_complaintJC() {
		return lst_complaintJC;
	}
	public void setLst_complaintJC(ListView lst_complaintJC) {
		this.lst_complaintJC = lst_complaintJC;
	}
	public String getDEALER_CONTACT_NUMBER() {
		return DEALER_CONTACT_NUMBER;
	}
	public void setDEALER_CONTACT_NUMBER(String dEALER_CONTACT_NUMBER) {
		DEALER_CONTACT_NUMBER = dEALER_CONTACT_NUMBER;
	}
	public String getODOMTR_RDNG() {
		return ODOMTR_RDNG;
	}
	public void setODOMTR_RDNG(String oDOMTR_RDNG) {
		ODOMTR_RDNG = oDOMTR_RDNG;
	}
	public String getINVC_AMT() {
		return INVC_AMT;
	}
	public void setINVC_AMT(String iNVC_AMT) {
		INVC_AMT = iNVC_AMT;
	}
	public String getSR_HISTORY_NUM() {
		return SR_HISTORY_NUM;
	}
	public void setSR_HISTORY_NUM(String sR_HISTORY_NUM) {
		SR_HISTORY_NUM = sR_HISTORY_NUM;
	}
	public String getCHASSIS_NO() {
		return CHASSIS_NO;
	}
	public void setCHASSIS_NO(String cHASSIS_NO) {
		CHASSIS_NO = cHASSIS_NO;
	}
	public String getREG_NUM() {
		return REG_NUM;
	}
	public void setREG_NUM(String rEG_NUM) {
		REG_NUM = rEG_NUM;
	}
	public String getCLOSE_DATE() {
		return CLOSE_DATE;
	}
	public void setCLOSE_DATE(String cLOSE_DATE) {
		CLOSE_DATE = cLOSE_DATE;
	}
	public String getSERVICE_AT_DEALER() {
		return SERVICE_AT_DEALER;
	}
	public void setSERVICE_AT_DEALER(String sERVICE_AT_DEALER) {
		SERVICE_AT_DEALER = sERVICE_AT_DEALER;
	}
	public String getSR_TYPE() {
		return SR_TYPE;
	}
	public void setSR_TYPE(String sR_TYPE) {
		SR_TYPE = sR_TYPE;
	}
	public String getINVOICE_STATUS() {
		return INVOICE_STATUS;
	}
	public void setINVOICE_STATUS(String iNVOICE_STATUS) {
		INVOICE_STATUS = iNVOICE_STATUS;
	}
	
	
	
}
