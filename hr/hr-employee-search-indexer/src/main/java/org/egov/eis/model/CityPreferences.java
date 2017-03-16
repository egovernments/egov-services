package org.egov.eis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityPreferences extends AbstractPersistable<Long> {

	public static final String SEQ_CITY_PREF = "SEQ_EG_CITYPREFERENCES";

	private Long id;

	private Long municipalityLogo;


	private String municipalityName;

	private String municipalityContactNo;

	private String municipalityAddress;

	private String municipalityContactEmail;

	private String municipalityGisLocation;

	private String municipalityCallCenterNo;

	private String municipalityFacebookLink;

	private String municipalityTwitterLink;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	protected void setId(final Long id) {
		this.id = id;
	}
}
