package org.egov.asset.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {

	private String name;
	private String localName;
	private String districtCode;
	private String districtName;
	private String regionName;
	@JsonProperty("ulbGrade")
	private String ulbGrade;
	private Double longitude;
	private Double latitude;
	private String shapeFileLocation;
	private String captcha;

	public City(final City city) {
		name = city.getName();
		localName = city.getLocalName();
		districtCode = city.getDistrictCode();
		districtName = city.getDistrictName();
		regionName = city.getRegionName();
		longitude = city.getLongitude();
		latitude = city.getLatitude();
		ulbGrade = city.getUlbGrade();
		shapeFileLocation = city.getShapeFileLocation();
		captcha = city.getCaptcha();
	}

	@JsonIgnore
	public City toDomain() {
		return City.builder().name(name).localName(localName).districtCode(districtCode).districtName(districtName)
				.ulbGrade(ulbGrade).regionName(regionName).longitude(longitude).latitude(latitude)
				.shapeFileLocation(shapeFileLocation).captcha(captcha).build();
	}
}
