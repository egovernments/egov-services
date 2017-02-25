package org.egov.boundary.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "eg_city", uniqueConstraints = @UniqueConstraint(columnNames = { "domainURL" }))
@NamedQuery(name = City.QUERY_CITY_BY_URL, query = "Select cw FROM City cw WHERE cw.domainURL=:domainURL")
@SequenceGenerator(name = City.SEQ_CITY, sequenceName = City.SEQ_CITY, allocationSize = 1)
public class City extends AbstractPersistable<Long> {

	public static final String SEQ_CITY = "SEQ_EG_CITY";
	public static final String QUERY_CITY_BY_URL = "CITY_BY_URL";
	private static final long serialVersionUID = -6267923687226233397L;
	@Id
	@GeneratedValue(generator = SEQ_CITY, strategy = GenerationType.SEQUENCE)
	private Long id;

	@SafeHtml
	@NotBlank
	private String name;

	@SafeHtml
	@NotBlank
	@Column(name = "local_name")
	private String localName;

	private boolean active;

	@SafeHtml
	@NotBlank
	@Column(name = "domainURL", unique = true)
	private String domainURL;

	@SafeHtml
	@NotBlank
	private String code;

	@SafeHtml
	@NotBlank
	@Column(name = "district_code")
	private String districtCode;

	@SafeHtml
	@NotBlank
	@Column(name = "district_name")
	private String districtName;

	@SafeHtml
	@Column(name = "region_name")
	private String regionName;

	@SafeHtml
	private String grade;

	private Float longitude;

	private Float latitude;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "preferences")
	@Fetch(FetchMode.JOIN)
	private CityPreferences preferences;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(final String localName) {
		this.localName = localName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public String getDomainURL() {
		return domainURL;
	}

	public void setDomainURL(final String domainURL) {
		this.domainURL = domainURL;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(final Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(final Float latitude) {
		this.latitude = latitude;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(final String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(final String districtName) {
		this.districtName = districtName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(final String regionName) {
		this.regionName = regionName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(final String grade) {
		this.grade = grade;
	}

	public CityPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(final CityPreferences preferences) {
		this.preferences = preferences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (domainURL == null ? 0 : domainURL.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final City other = (City) obj;
		if (domainURL == null) {
			if (other.domainURL != null)
				return false;
		} else if (!domainURL.equals(other.domainURL))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
