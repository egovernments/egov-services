package org.egov.pa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantList {
	
	@JsonProperty("tenants")
	private List<Tenant> tenantList ;
}
