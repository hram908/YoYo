package com.asktun.mg.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationUtil {
	private Context context;
	private LocationManager locationManager;
	private Location location;

	public LocationUtil(Context context) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  

	}
	
	public void requestLocation(LocationListener locationListener){
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,   
				 1000, 0, locationListener);  
		location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);   
	}
	
	public double getLatitude(){
		return location.getLatitude();
	}
	public double getLongitude(){
		return location.getLongitude();
	}
	public double getAltitude(){
		return location.getAltitude();
	}
}
