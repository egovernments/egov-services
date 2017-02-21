package org.egov.web.indexer.models;

import lombok.Getter;

@Getter
public class GeoPoint {

	private double lat;
	private double lon;

	private GeoPoint() {
		//required by mapper to instantiate object
	}

	public GeoPoint(double latitude, double longitude) {
		this.lat = latitude;
		this.lon = longitude;
	}
}


