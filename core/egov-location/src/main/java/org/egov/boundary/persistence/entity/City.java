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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "eg_city", uniqueConstraints = @UniqueConstraint(columnNames = { "domainURL" }))
@SequenceGenerator(name = City.SEQ_CITY, sequenceName = City.SEQ_CITY, allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City extends AbstractPersistable<Long> {

	public static final String SEQ_CITY = "SEQ_EG_CITY";
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

	@Column(name = "tenantid")
	private String tenantId;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}
}
