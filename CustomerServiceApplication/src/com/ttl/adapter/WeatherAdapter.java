package com.ttl.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttl.customersocialapp.R;
import com.ttl.model.Weather;

public class WeatherAdapter extends ArrayAdapter<Weather> {

	private ArrayList<Weather> weatherlist;

	public WeatherAdapter(Context context, int textViewResourceId,
			ArrayList<Weather> object) {
		super(context, textViewResourceId, object);
		this.weatherlist = object;
		System.out.println(this.weatherlist.size());
	}

	@Override
	public int getCount() {
		
		System.out.println(weatherlist.size() + "get size");
		return weatherlist.size();
	}

	@Override
	public Weather getItem(int arg0) {
		
		return weatherlist.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView txtday, txtlowtemp;
		ImageView tempimg;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.weather_list, null);
		}
		Weather weatherdata = weatherlist.get(position);
		
		txtday = (TextView) v.findViewById(R.id.day);
		txtday.setText(new SimpleDateFormat("EE", Locale.getDefault())
				.format(weatherdata.day));
		tempimg = (ImageView) v.findViewById(R.id.img);

		String icon = weatherdata.iconUri;
		
		if (icon.equalsIgnoreCase("01d")) {
			tempimg.setBackgroundResource(R.drawable.mon);
		} else if (icon.equalsIgnoreCase("10d")) {
			tempimg.setBackgroundResource(R.drawable.tue);
		} else if (icon.equalsIgnoreCase("03d")) {
			tempimg.setBackgroundResource(R.drawable.rain);
		} else if (icon.equalsIgnoreCase("04d")) {
			tempimg.setBackgroundResource(R.drawable.thu);
		} else if (icon.equalsIgnoreCase("02d")) {
			tempimg.setBackgroundResource(R.drawable.maintemp);
		} else if (icon.equalsIgnoreCase("")) {
			tempimg.setBackgroundResource(R.drawable.maintemp);
		} else if (icon.equalsIgnoreCase("01n") || icon.equalsIgnoreCase("03n")) {
			tempimg.setBackgroundResource(R.drawable.night_clear);
		} else if (icon.equalsIgnoreCase("02n") || icon.equalsIgnoreCase("04n")
				|| icon.equalsIgnoreCase("13n")) {
			tempimg.setBackgroundResource(R.drawable.night_cloudy);
		} else if (icon.equalsIgnoreCase("09n") || icon.equalsIgnoreCase("11n")) {
			tempimg.setBackgroundResource(R.drawable.night_rain);
		}

		txtlowtemp = (TextView) v.findViewById(R.id.txtlowtemp);
		String mintemp = (String.valueOf(weatherdata.mintemperature).substring(
				0, 2));
		String Cmintemp = mintemp.replace(".", "");

		String maxtemp = (String.valueOf(weatherdata.maxtemperature).substring(
				0, 2));
		String Cmaxtemp = maxtemp.replace(".", "");

		txtlowtemp.setText(Cmintemp + "ºC" + "-" + Cmaxtemp + "ºC");

		return v;
	}

}