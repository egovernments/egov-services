package org.egov.web.indexer.contract;

import lombok.Getter;

import java.util.List;

@Getter
public class TenantResponse {
     private List<Tenant> tenant;
}