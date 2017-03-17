package org.egov.eis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City extends AbstractPersistable<Long> {


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
	
	
	private CityPreferences preferences;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}
}
