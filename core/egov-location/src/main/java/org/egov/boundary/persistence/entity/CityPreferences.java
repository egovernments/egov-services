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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "eg_citypreferences")
@SequenceGenerator(name = CityPreferences.SEQ_CITY_PREF, sequenceName = CityPreferences.SEQ_CITY_PREF, allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
public class CityPreferences extends AbstractPersistable<Long> {

	public static final String SEQ_CITY_PREF = "SEQ_EG_CITYPREFERENCES";
	@Id
	@GeneratedValue(generator = SEQ_CITY_PREF, strategy = GenerationType.SEQUENCE)
	private Long id;

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

	@Column(name = "tenantid")
	private String tenantId;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	protected void setId(final Long id) {
		this.id = id;
	}
}
