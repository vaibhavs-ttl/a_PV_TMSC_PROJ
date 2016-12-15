package com.ttl.model;

public class GenericFeedback {

	String feedback_category;
	String generic_feedback_type;
	String specific_type;
	String customer_feedback;
	
	public String getFeedback_category() {
		return feedback_category;
	}
	public void setFeedback_category(String feedback_category) {
		this.feedback_category = feedback_category;
	}
	public String getGeneric_feedback_type() {
		return generic_feedback_type;
	}
	public void setGeneric_feedback_type(String generic_feedback_type) {
		this.generic_feedback_type = generic_feedback_type;
	}
	public String getSpecific_type() {
		return specific_type;
	}
	public void setSpecific_type(String specific_type) {
		this.specific_type = specific_type;
	}
	public String getCustomer_feedback() {
		return customer_feedback;
	}
	public void setCustomer_feedback(String customer_feedback) {
		this.customer_feedback = customer_feedback;
	}
}
