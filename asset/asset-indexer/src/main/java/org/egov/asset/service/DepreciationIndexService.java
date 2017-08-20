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

    @Autowired
    private ObjectMapper mapper;

    public void indexDepreciaiton(final Depreciation depreciation) {

        final StringBuilder bulkRequestBody = new StringBuilder();
        final String format = "{ \"index\" : { \"_id\" : \"%s\" } }%n";

        final Long voucherRef = depreciation.getVoucherReference();
        final AuditDetails auditDetails = depreciation.getAuditDetails();
        final String tenantId = depreciation.getTenantId();
        final String financialYear = depreciation.getFinancialYear();
        final Long fromDate = depreciation.getFromDate();
        final Long toDate = depreciation.getToDate();

        for (final DepreciationDetail depreciationDetail : depreciation.getDepreciationDetails()) {
            final String actionMetaData = String.format(format, "" + depreciationDetail.getId());
            bulkRequestBody.append(actionMetaData);
            final DepreciationIndex depreciationIndex = DepreciationIndex.toDepreciationIndex(depreciationDetail,
                    voucherRef, auditDetails, tenantId, financialYear, fromDate, toDate);
            try {
                bulkRequestBody.append(mapper.writeValueAsString(depreciationIndex));
            } catch (final JsonProcessingException e) {
                log.info(e.toString());
            }
            bulkRequestBody.append("\n");
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>(bulkRequestBody.toString(), headers);
        final String url = applicationProperties.getIndexerHost() + applicationProperties.getDepreciaitionIndexUrl();
        try {
            restTemplate.postForEntity(url, entity, String.class);
        } catch (final Exception e) {
            // do something
        }
    }
}
