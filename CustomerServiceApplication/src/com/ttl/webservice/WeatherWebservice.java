package com.ttl.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.ttl.customersocialapp.WeatherReport_fragment;
import com.ttl.helper.FragmentCallback;
import com.ttl.model.Weather;

public class WeatherWebservice extends
		AsyncTask<Void, Void, ArrayList<Weather>> {
	private FragmentCallback mFragmentCallback;
	String apiUrlIcon = "http://openweathermap.org/img/w/";
	String apiUrlFormat = "http://api.openweathermap.org/data/2.1/find/city?&lat=%f&lon=%f&cnt=1&APPID=a1d0a7fc5afe207572cbdb34a1f97c91";
	String apiForecastUrlFormat = "http://api.openweathermap.org/data/2.5/forecast/daily?&lat=%f&lon=%f&APPID=a1d0a7fc5afe207572cbdb34a1f97c91";
	String apiSearchCityFormat = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=a1d0a7fc5afe207572cbdb34a1f97c91";
	String apiUrl;
	Location location;
	boolean todayWeather;
	String citySearch;

	public WeatherWebservice(FragmentCallback fragmentCallback,
			Location location, boolean todayWeather, String citySearch) {
		mFragmentCallback = fragmentCallback;
		this.todayWeather = todayWeather;
		this.citySearch = citySearch;

		if (WeatherReport_fragment.location != null) {
			this.location = Weather.getInstance().location = WeatherReport_fragment.location;
		}

		if (citySearch != null) {
			this.apiUrl = String.format(apiSearchCityFormat.toString(),
					citySearch);
		} else {
			this.apiUrl = String.format(todayWeather ? apiUrlFormat.toString()
					: apiForecastUrlFormat.toString(), location.getLatitude(),
					location.getLongitude());
		}
	}

	@Override
	protected ArrayList<Weather> doInBackground(Void... arg0) {

		if (!(apiUrl.equals(""))) {
			String getresponse;
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(apiUrl);
			StringBuilder builder = null;

			// Get Response
			try {
				HttpResponse response = httpClient.execute(request);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (builder != null) {
			getresponse = builder.toString();
		
				return todayWeather ? parseTodayWeather(getresponse)
						: parseWeekWeather(getresponse);
			}else {
				return null;
			}
		}
		return null;
	}

	private ArrayList<Weather> parseTodayWeather(String response) {
		Weather dayWeather = new Weather();

		// Parse JSON
		JSONObject weatherData = null;
		JSONArray list = null;

		try {
			if (citySearch != null) {
				weatherData = new JSONObject(response);
			} else {
				list = new JSONObject(response).getJSONArray("list");
				if (list.length() > 0) {
					weatherData = list.getJSONObject(0);
				}
			}

			if (weatherData != null) {
				
				if (weatherData.has("name")) {
					dayWeather.place = weatherData.getString("name");	
				}
				else
				{
					dayWeather.place="";
				}
				
				
				dayWeather.temperature = kelvinToCelsius(weatherData
						.getJSONObject("main").getDouble("temp"));
				dayWeather.humidity = weatherData.getJSONObject("main")
						.getDouble("humidity");
				dayWeather.pressure = weatherData.getJSONObject("main").getInt(
						"pressure");
				dayWeather.windSpeed = weatherData.getJSONObject("wind")
						.getDouble("speed");
				dayWeather.description = weatherData.getJSONArray("weather")
						.getJSONObject(0).getString("description");
				dayWeather.iconUri = weatherData.getJSONArray("weather")
						.getJSONObject(0).getString("icon");
				Log.d(dayWeather.iconUri, "day icon");
				dayWeather.isFetched = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<Weather> weathers = new ArrayList<Weather>();
		weathers.add(dayWeather);
		return weathers;
	}

	private ArrayList<Weather> parseWeekWeather(String response) {
		// Parse JSON
		JSONObject jsonObject = null;
		JSONArray list = null;
		ArrayList<Weather> weathers = new ArrayList<Weather>();
		Log.d("weather responce", response);
		try {
			jsonObject = new JSONObject(response);
			list = jsonObject.getJSONArray("list");

			for (int i = 0; i < 5; i++) {
				Weather dayWeather = new Weather();
				JSONObject weatherData = list.getJSONObject(i);
				dayWeather.temperature = kelvinToCelsius(weatherData
						.getJSONObject("temp").getDouble("day"));
				dayWeather.mintemperature = kelvinToCelsius(weatherData
						.getJSONObject("temp").getDouble("min"));
				String mintemp = (String.valueOf(dayWeather.mintemperature)
						.substring(0, 2));
				String Cmintemp = mintemp.replace(".", "");
				Log.d(Cmintemp + "", "min temp");
				dayWeather.maxtemperature = kelvinToCelsius(weatherData
						.getJSONObject("temp").getDouble("max"));
				Log.d(dayWeather.maxtemperature + "", "max temp");
				dayWeather.humidity = weatherData.getDouble("humidity");
				dayWeather.pressure = weatherData.getInt("pressure");
				dayWeather.windSpeed = weatherData.getDouble("speed");
				final Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(weatherData.getLong("dt") * 1000);
				dayWeather.day = cal.getTime();

				dayWeather.iconUri = weatherData.getJSONArray("weather")
						.getJSONObject(0).getString("icon");
				Log.d(dayWeather.iconUri, "icon");
				dayWeather.description = weatherData.getJSONArray("weather")
						.getJSONObject(0).getString("description");
				dayWeather.isFetched = true;

				weathers.add(dayWeather);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return weathers;
	}

	public double kelvinToCelsius(double kelvin) {
		return kelvin - 273.15;
	}

	protected void onPostExecute(ArrayList<Weather> result) {

		mFragmentCallback.onTaskDone(result);

	}
}
