package org.egov.asset.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.repository.DepreciationRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class DepreciationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Mock
    private DepreciationRepository depreciationRepository;

    @Mock
    private CurrentValueService currentValueService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private AssetDepreciator assetDepreciator;

    @Mock
    private SequenceGenService sequenceGenService;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private AssetCommonService assetCommonService;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private DepreciationService depreciationService;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void test_Depreciate_Asset() {
        final DepreciationResponse depreciationResponse = getDepreciationReponse();
        final DepreciationRequest depreciationRequest = DepreciationRequest.builder().requestInfo(new RequestInfo())
                .depreciationCriteria(getDepreciationCriteria()).build();
        final HttpHeaders headers = getHttpHeaders();
        assertEquals(depreciationResponse, depreciationService.depreciateAsset(depreciationRequest, headers));
    }

    private HttpHeaders getHttpHeaders() {
        final List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.ALL);
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.COOKIE, "SESSIONID=123");
        requestHeaders.setPragma("no-cache");
        requestHeaders.setConnection("keep-alive");
        requestHeaders.setCacheControl("no-cache");
        requestHeaders.setAccept(mediaTypes);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }

    private DepreciationResponse getDepreciationReponse() {
        final DepreciationResponse depreciationResponse = new DepreciationResponse();
        final DepreciationCriteria depreciationCriteria = getDepreciationCriteria();
        final Depreciation depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
                .depreciationDetails(new ArrayList<DepreciationDetail>()).build();
        depreciationResponse.setDepreciation(depreciation);
        depreciationResponse.setResponseInfo(null);
        return depreciationResponse;
    }

    private DepreciationCriteria getDepreciationCriteria() {
        final DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
        depreciationCriteria.setAssetIds(null);
        depreciationCriteria.setFinancialYear("2017");
        depreciationCriteria.setFromDate(Long.valueOf(0));
        depreciationCriteria.setToDate(Long.valueOf("0"));
        depreciationCriteria.setTenantId("ap.kurnool");
        return depreciationCriteria;
    }

}
