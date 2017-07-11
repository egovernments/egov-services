package org.egov.asset.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tenant {
	private String code;
	private String description;
	private String logoId;
	private String imageId;
	private String domainUrl;
	private String type;
	private String twitterUrl;
	private String facebookUrl;
	private String emailId;
	private City city;
	private String address;
	private String contactNumber;
	private String helpLineNumber;

	public Tenant(final Tenant tenant, final City city) {
		code = tenant.getCode();
		description = tenant.getDescription();
		logoId = tenant.getLogoId();
		imageId = tenant.getImageId();
		domainUrl = tenant.getDomainUrl();
		type = tenant.getType().toString();
		twitterUrl = tenant.getTwitterUrl();
		facebookUrl = tenant.getFacebookUrl();
		address = tenant.getAddress();
		contactNumber = tenant.getContactNumber();
		helpLineNumber = tenant.getHelpLineNumber();
		emailId = tenant.getEmailId();

		this.city = city;
	}

	@JsonIgnore
	public Tenant toDomain() {
		City city;

		if (this.city != null)
			city = this.city.toDomain();
		else
			city = null;

		return Tenant.builder().code(code).description(description).logoId(logoId).imageId(imageId).domainUrl(domainUrl)
				.type(type).twitterUrl(twitterUrl).facebookUrl(facebookUrl).emailId(emailId).address(address)
				.contactNumber(contactNumber).helpLineNumber(helpLineNumber).city(city).build();
	}
}
