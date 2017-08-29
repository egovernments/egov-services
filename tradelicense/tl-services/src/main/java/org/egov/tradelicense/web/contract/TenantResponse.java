package org.egov.tradelicense.web.contract;

import lombok.Getter;

import java.util.List;

@Getter
public class TenantResponse {
     private List<Tenant> tenant;
}