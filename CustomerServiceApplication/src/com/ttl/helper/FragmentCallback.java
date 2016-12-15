package com.ttl.helper;

import java.util.ArrayList;

import com.ttl.model.Weather;

public interface FragmentCallback {
	public void onTaskDone(ArrayList<Weather> result);
}

