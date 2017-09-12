package org.egov.asset.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.repository.CurrentValueRepository;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class CurrentValueServiceTest {

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private CurrentValueRepository currentValueRepository;

    @Mock
    private SequenceGenService sequenceGenService;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private CurrentValueService currentValueService;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private FileUtils fileUtils;

    @Mock
    private AssetCommonService assetCommonService;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void testCreateAsync() {
        final AssetCurrentValueRequest assetCurrentValueRequest = new AssetCurrentValueRequest();
        assetCurrentValueRequest.setAssetCurrentValues(getAssetCurrentValuesForCreateAsync());

        final AuditDetails auditDetails = getAuditDetails();
        final List<Long> idList = getAssetIds();

        when(assetCommonService.getAuditDetails(any(RequestInfo.class))).thenReturn(auditDetails);
        when(sequenceGenService.getIds(any(Integer.class), any(String.class))).thenReturn(idList);
        when(applicationProperties.getSaveCurrentvalueTopic()).thenReturn("save-currentvalue-db");

        currentValueService.createCurrentValueAsync(assetCurrentValueRequest);
    }

    private List<Long> getAssetIds() {
        final List<Long> idList = new ArrayList<Long>();
        idList.add(Long.valueOf("1"));
        idList.add(Long.valueOf("2"));
        return idList;
    }

    @Test
    public void testGetCurrentValues() {
        final Set<Long> assetIds = new HashSet<Long>(getAssetIds());
        currentValueService.getCurrentValues(assetIds, "ap.kurnool", new RequestInfo());
    }

    private List<AssetCurrentValue> getAssetCurrentValuesForCreateAsync() {
        final List<AssetCurrentValue> assetCurrentValues = new ArrayList<AssetCurrentValue>();
        final AssetCurrentValue assetCurrentValue = new AssetCurrentValue();
        assetCurrentValue.setAssetId(2L);
        assetCurrentValue.setAssetTranType(TransactionType.DISPOSAL);
        assetCurrentValue.setAuditDetails(getAuditDetails());
        assetCurrentValue.setId(1L);
        assetCurrentValue.setTenantId("ap.kurnool");
        assetCurrentValues.add(assetCurrentValue);
        return assetCurrentValues;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(String.valueOf("5"));
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy(String.valueOf("5"));
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        return auditDetails;
    }

}
