package org.egov.lams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class City {
	private Long id;
	private String name;
	private String localName;
	private boolean active;
	private String domainURL;
	private String code;
	private String districtCode;
	private String districtName;
	private String regionName;
	private String grade;
	private Float longitude;
	private Float latitude;
	//private CityPreferences preferences;
}
