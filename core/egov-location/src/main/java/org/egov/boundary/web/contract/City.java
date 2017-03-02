package org.egov.boundary.web.contract;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

	 	@NotEmpty
	 	@JsonProperty("id")
	    private String id;
	    @NotEmpty
		@JsonProperty("code")
	    private String code;
		@JsonProperty("name")
	    private String name;
		@JsonProperty("districtCode")
	    private String districtCode ;
		@JsonProperty("districtName")
	    private String districtName ;
		@JsonProperty("grade")
	    private String grade ;
		@JsonProperty("domainURL")
	    private String domainURL;
		@JsonProperty("regionName")
	    private String regionName ;

		public City(org.egov.boundary.persistence.entity.City entityCity) {
			this.id = entityCity.getId().toString();
			this.code = entityCity.getCode();
			this.name = entityCity.getName();
			this.districtCode = entityCity.getDistrictCode();
			this.districtName = entityCity.getDistrictName();
			this.grade = entityCity.getGrade();
			this.domainURL = entityCity.getDomainURL();
			this.regionName = entityCity.getRegionName();
		}
}
