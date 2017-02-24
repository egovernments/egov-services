package org.egov.boundary.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "eg_citypreferences")
@SequenceGenerator(name = CityPreferences.SEQ_CITY_PREF, sequenceName = CityPreferences.SEQ_CITY_PREF, allocationSize = 1)
public class CityPreferences extends AbstractPersistable<Long>  {

	public static final String SEQ_CITY_PREF = "SEQ_EG_CITYPREFERENCES";
	private static final long serialVersionUID = -7160795726709889116L;
	@Id
	@GeneratedValue(generator = SEQ_CITY_PREF, strategy = GenerationType.SEQUENCE)
	private Long id;

	//@ManyToOne(cascade = CascadeType.ALL)
	//@JoinColumn(name = "municipality_logo")
	@NotNull
	@Fetch(FetchMode.JOIN)
	@Column(name = "municipality_logo")
	private Long municipalityLogo;

	@NotNull
	@SafeHtml
	@Length(max = 50)
	@Column(name = "municipality_name")
	private String municipalityName;

	@SafeHtml
	@Length(max = 20)
	@Column(name = "municipality_contact_no")
	private String municipalityContactNo;

	@SafeHtml
	@Length(max = 200)
	@Column(name = "municipality_address")
	private String municipalityAddress;

	@SafeHtml
	@Length(max = 50)
	@Column(name = "municipality_contact_email")
	private String municipalityContactEmail;

	@SafeHtml
	@Length(max = 100)
	@Column(name = "municipality_gis_location")
	private String municipalityGisLocation;

	@SafeHtml
	@Length(max = 20)
	@Column(name = "municipality_callcenter_no")
	private String municipalityCallCenterNo;

	@SafeHtml
	@Length(max = 100)
	@Column(name = "municipality_facebooklink")
	private String municipalityFacebookLink;

	@SafeHtml
	@Length(max = 100)
	@Column(name = "municipality_twitterlink")
	private String municipalityTwitterLink;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	protected void setId(final Long id) {
		this.id = id;
	}

	public Long getMunicipalityLogo() {
		return municipalityLogo;
	}

	public void setMunicipalityLogo(final Long municipalityLogo) {
		this.municipalityLogo = municipalityLogo;
	}

	public String getMunicipalityName() {
		return municipalityName;
	}

	public void setMunicipalityName(final String municipalityName) {
		this.municipalityName = municipalityName;
	}

	public String getMunicipalityContactNo() {
		return municipalityContactNo;
	}

	public void setMunicipalityContactNo(final String municipalityContactNo) {
		this.municipalityContactNo = municipalityContactNo;
	}

	public String getMunicipalityAddress() {
		return municipalityAddress;
	}

	public void setMunicipalityAddress(final String municipalityAddress) {
		this.municipalityAddress = municipalityAddress;
	}

	public String getMunicipalityContactEmail() {
		return municipalityContactEmail;
	}

	public void setMunicipalityContactEmail(final String municipalityContactEmail) {
		this.municipalityContactEmail = municipalityContactEmail;
	}

	public String getMunicipalityGisLocation() {
		return municipalityGisLocation;
	}

	public void setMunicipalityGisLocation(final String municipalityGisLocation) {
		this.municipalityGisLocation = municipalityGisLocation;
	}

	public String getMunicipalityCallCenterNo() {
		return municipalityCallCenterNo;
	}

	public void setMunicipalityCallCenterNo(final String municipalityCallCenterNo) {
		this.municipalityCallCenterNo = municipalityCallCenterNo;
	}

	public String getMunicipalityFacebookLink() {
		return municipalityFacebookLink;
	}

	public void setMunicipalityFacebookLink(final String municipalityFacebookLink) {
		this.municipalityFacebookLink = municipalityFacebookLink;
	}

	public String getMunicipalityTwitterLink() {
		return municipalityTwitterLink;
	}

	public void setMunicipalityTwitterLink(final String municipalityTwitterLink) {
		this.municipalityTwitterLink = municipalityTwitterLink;
	}

	/*
	 * public boolean logoExist() { return municipalityLogo != null; }
	 */
}
