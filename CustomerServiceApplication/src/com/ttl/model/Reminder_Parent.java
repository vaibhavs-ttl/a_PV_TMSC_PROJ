package com.ttl.model;

import java.util.ArrayList;

public class Reminder_Parent {

	public String remindertitle;

	public String getRemindertitle() {
		return remindertitle;
	}

	public void setRemindertitle(String remindertitle) {
		this.remindertitle = remindertitle;
	}

	public ArrayList<Reminder_Child> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Reminder_Child> children) {
		this.children = children;
	}

	public ArrayList<Reminder_Child> children;

}
