package com.ttl.model;

import android.provider.BaseColumns;

public class ReminderContract {

	/*
	 * To prevent someone from accidentally instantiating the contract class,
	 * give it an empty constructor.
	 */
	public ReminderContract() {

	}

	/* Inner class that defines the table contents */
	public static abstract class Reminder implements BaseColumns {
		public static final String TABLE_NAME = "reminder_master_"+UserDetails.getUser_id();
		public static final String COLUMN_NAME_REMINDER_ID = "registration_id";
		public static final String COLUMN_NAME_REMINDER_VNUMBER = "registration_number";
		public static final String COLUMN_NAME_REGISTRATION_VTYPE = "reminder_type";
		public static final String COLUMN_NAME_VEHICLE_REMINDER_DATE = "vehicle_reminder_date";
		public static final String COLUMN_NAME_VEHICLE_REMINDER_TIME = "vehicle_reminder_time";

	}

}