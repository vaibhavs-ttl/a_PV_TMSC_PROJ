package com.ttl.adapter;

public interface ResponseCallback {
	public void onResponseReceive(Object object);
	public void onErrorReceive(String string);
}
