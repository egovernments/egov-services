package org.egov.asset.service;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DepreciationIndex;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepreciationIndexService {
	
	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private RestTemplate restTemplate;

	public void indexDepreciaiton(Depreciation depreciation) {

		ObjectMapper mapper = new ObjectMapper();

		StringBuilder bulkRequestBody = new StringBuilder();
		String format = "{ \"index\" : { \"_id\" : \"%s\" } }%n";

		Long voucherRef = depreciation.getVoucherReference();
		AuditDetails auditDetails = depreciation.getAuditDetails();
		String tenantId = depreciation.getTenantId();
		String financialYear = depreciation.getFinancialYear();
		Long fromDate = depreciation.getFromDate();
		Long toDate = depreciation.getToDate();

		for (DepreciationDetail depreciationDetail : depreciation.getDepreciationDetails()) {
			String actionMetaData = String.format(format, "" + depreciationDetail.getId());
			bulkRequestBody.append(actionMetaData);
			DepreciationIndex depreciationIndex = DepreciationIndex.toDepreciationIndex(depreciationDetail, voucherRef,
					auditDetails, tenantId, financialYear, fromDate, toDate);
			try {
				bulkRequestBody.append(mapper.writeValueAsString(depreciationIndex));
			} catch (JsonProcessingException e) {
				log.info(e.toString());
			}
			bulkRequestBody.append("\n");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(bulkRequestBody.toString(), headers);
		String url = applicationProperties.getIndexerHost()+applicationProperties.getDepreciaitionIndexUrl();
		try {
			restTemplate.postForEntity( url, entity, String.class);
		} catch (Exception e) {
			// do something
		}
	}
}
