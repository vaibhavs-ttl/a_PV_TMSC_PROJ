package com.ttl.model;

import java.io.Serializable;
import java.util.Date;

import android.location.Location;

public class Weather implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7468907373314597663L;
	public Location location;
	public String place;
	public double temperature;
	public double mintemperature;
	public double maxtemperature;
	public double humidity;
	public double windSpeed;
	public int pressure;
	public String iconUri;
	public String description;
	public Date day;
	public boolean isFetched;
	private static Weather INSTANCE = new Weather();
	public Weather() {
		// Default values
		day = new Date();
		temperature = humidity = windSpeed = pressure = 0;
		isFetched = false;
	}
	
	public static Weather getInstance()
	{	return INSTANCE;
	}
}
