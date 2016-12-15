package com.ttl.webservice;

import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.os.Environment;

import com.ttl.model.Reminder_Data;

public class Constants {

	public static final String project_id = "276408736612";
	public static final String GetCustomerVehicleDetailsCSB = "GetCustomerVehicleDetailsCSB";
	public static final String GetDSSDealerCitiesCSB = "GetDSSDealerCitiesCSB";
	public static final String GetDlrsByCityAndTypeCSB = "GetDlrsByCityAndTypeCSB";
	public static final String GetComplaintAreaCSB = "GetComplaintAreaCSB";
	public static final String GetComplaintDesc  = "GetComplaintDesc";
	public static final String  GetdivphonebydivCSB = "GetdivphonebydivCSB";
	public static final String GetCompTypeByBUCSB = "GetCompTypeByBUCSB";
	public static final String GetCompSubTypeByParRowIDCSB = "GetCompSubTypeByParRowIDCSB";
	public static final String GetCompCitiesByType = "GetCompCitiesByType";
	public static final String ServiceRequest = "ServiceRequest";
	public static final String GetServiceHistoryByChassis_CSB = "GetServiceHistoryByChassis_CSB";
	public static final String GetCompPrblmAreabySubAreaCSB ="GetCompPrblmAreabySubAreaCSB";
	public static final String TMPCComplaintsInsertOrUpdate_Input = "TMPCComplaintsInsertOrUpdate_Input";
	public static final String GetCitiesforServiceOrderCSB ="GetCitiesforServiceOrderCSB";
	public static final String GetDlrLocDtlsCSB ="GetDlrLocDtlsCSB";
	public static final String TMSiebelServiceInsertOrUpdate_Input = "TMSiebelServiceInsertOrUpdate_Input";
	public static final String GetComplaintAndJCDescripti_CSB = "GetComplaintAndJCDescripti_CSB";
	public static final String GetVechcontactScheduler_CSB = "GetVechcontactScheduler_CSB";
	public static final String GetPositionbyPositionAndOUID_CSB = "GetPositionbyPositionAndOUID_CSB";
	public static final String GetAllIndianStates = "GetAllIndianStates";
	public static final String GetAllIndianDistricts = "GetAllIndianDistricts";
	public static final String GetAllIndianCity = "GetAllIndianCity";
	public static final String bpmSendSMS = "bpmSendSMS";
	public static final String GetAMCDetailsScheduler_CSB = "GetAMCDetailsScheduler_CSB";
	public static final String GetAgreementScheduler_CSB = "GetAgreementScheduler_CSB";
	public static final String GetState_CSB = "GetState_CSB";
	public static final String GetCity_CSB = "GetCity_CSB";
	public static final String GetPPL_CSB = "GetPPL_CSB";
	public static final String GetParUsrPostnByPostTypeAndDivID_CSB ="GetParUsrPostnByPostTypeAndDivID_CSB";
	
	public static final String user = "user";
	public static final String registeruser = "registeruser";
	public static final String afterregisteruser = "afterregisteruser";
	public static final String getServiceTypes = "getServiceTypes";
	public static final String getCityFromState = "getCityFromState";
	public static final String getFreeServiceCostEstimate = "getFreeServiceCostEstimate";
	public static final String getKms = "getKms";
	public static final String getPaidServiceCostEstimate = "getPaidServiceCostEstimate";
	public static final String getState = "getState";
	public static final String getNotificationsForApp = "getNotificationsForApp";
	public static final String insertVehicleDetails = "insertVehicleDetails";
	public static final String getVehicleDetailsByUserId = "getVehicleDetailsByUserId";
	public static final String updateUserDetails = "updateUserDetails";
	public static final String getCityFromStateMaster = "getCityFromStateMaster";
	public static String getVehicleImageByPPL = "getVehicleImageByPPL";
	public static String uploadProfileImage = "uploadProfileImage";
	public static final String getlatestnotification = "getlatestnotification";
	public static final String setPrefferedDealer = "setPrefferedDealer";
	public static final String addGenericCustomerFeedback = "addGenericCustomerFeedback";
	public static final ArrayList<Reminder_Data> list_reminder = new ArrayList<Reminder_Data>();
	public static final String insertServiceBookingHistory = "insertServiceBookingHistory";
	public static final String insertComplaintHistory = "insertComplaintHistory";
	public static final String getServiceBookingHistory = "getServiceBookingHistory";
	  public static final String getComplaintHistory = "getComplaintHistory";
	  public static final String getJobCardList = "getJobCardList";
      public static final String addPSFFeedback = "addPSFFeedback";
      public static final String sendPasswordToCustomerEmail = "sendPasswordToCustomerEmail";
      public static final String getDetailsForPasswordResetByUserId = "getDetailsForPasswordResetByUserId";
      public static final String SendMail = "SendMail";
      public static final String getReminderTypes = "getReminderTypes";
      public static final String setVersions = "setVersions";
      public static final String setReadFlag = "setReadFlag";
      public static final String getPSFNotifications = "getPSFNotifications";
      public static final String gcmuser = "gcmuser";
      public static final String logout = "logout";
      public static final String resetPassword = "resetPassword";
      public static final String forgotUserId = "forgotUserId";
      public static final String brochures="getAllBrochures";
      
      public static final String getLabourDataByPPL="getLabourDataByPPL";
      
      public static final String getPartsDataByPPL="getPartsDataByPPL";
      
      public static final String getLabourRateByCity="getLabourRateByCity";
      
      public static String key = "abc2764,?.EDH"; // key for encryption 
	public static SecretKeySpec secrete_key_spec;
	public static SecretKey secretKey;
	
	public static final String JOB_CODE="Job Code";
	public static final String SERVICE_CODE="Service Code";
	
	public static final int LABOUR_REQUEST_CODE=101;
	public static final int SPARE_REQUEST_CODE=102;
	
	public static final String TYPE_LABOUR="labour";
	public static final String TYPE_SPARE="spare";
	public static final String BROCHURE_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/CustomerSocialAppDocument/brochures";

	public static final String sendMailForManualCostEstimate="sendMailForManualCostEstimate";
	
	
}
