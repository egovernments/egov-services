package org.egov.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class City {

	private Long id;
	private String name;
	private String localName;
	private String districtCode;
	private String districtName;
	private String regionName;
	private Double longitude;
	private Double latitude;
	private String tenantCode;
	private String ulbGrade;
	private Long createdBy;
	private Date createdDate;
	private Long lastModifiedBy;
	private Date lastModifiedDate;
	private String shapeFileLocation;
	private String captcha;
	private String code;
}