package org.egov.lcms.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
	private String code;
}