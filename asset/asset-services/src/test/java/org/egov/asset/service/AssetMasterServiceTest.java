package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.contract.AssetStatusResponse;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.StatusValue;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.AssetMasterRepository;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssetMasterServiceTest {

    @Mock
    private AssetMasterRepository assetMasterRepository;

    @InjectMocks
    private AssetMasterService assetMasterService;

    @Test
    public void test_search() {
        final AssetStatusCriteria assetStatusCriteria = getAssetStatusCriteria();
        final AssetStatusResponse expectedAssetStatusResponse = getAssetStatusResponse();
        when(assetMasterRepository.search(any(AssetStatusCriteria.class))).thenReturn(getAssetStatuses());
        final AssetStatusResponse actualAssetStatusResponse = assetMasterService.search(assetStatusCriteria,
                new RequestInfo());

        assertEquals(expectedAssetStatusResponse.toString(), actualAssetStatusResponse.toString());
    }

    @Test
    public void test_getStatuses() {
        final List<AssetStatus> expectedAssetStatuses = getAssetStatuses();
        when(assetMasterRepository.search(any(AssetStatusCriteria.class))).thenReturn(getAssetStatuses());
        final List<AssetStatus> actualAssetStatuses = assetMasterService.getStatuses(AssetStatusObjectName.REVALUATION,
                Status.APPROVED, "ap.kurnool");

        assertEquals(expectedAssetStatuses.toString(), actualAssetStatuses.toString());
    }

    private AssetStatusResponse getAssetStatusResponse() {
        final AssetStatusResponse assetStatusResponse = new AssetStatusResponse();
        assetStatusResponse.setResponseInfo(new ResponseInfo());
        assetStatusResponse.setAssetStatus(getAssetStatuses());
        return assetStatusResponse;
    }

    private List<AssetStatus> getAssetStatuses() {
        final List<AssetStatus> assetStatus = new ArrayList<AssetStatus>();
        final List<StatusValue> statusValues = new ArrayList<StatusValue>();
        final StatusValue statusValue = new StatusValue();
        final AssetStatus asStatus = new AssetStatus();
        asStatus.setObjectName(AssetStatusObjectName.REVALUATION.toString());
        asStatus.setAuditDetails(getAuditDetails());
        statusValue.setCode(Status.APPROVED.toString());
        statusValue.setName(Status.APPROVED.toString());
        statusValue.setDescription("Asset Revaluation is created");
        statusValues.add(statusValue);
        asStatus.setStatusValues(statusValues);
        assetStatus.add(asStatus);
        return assetStatus;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(String.valueOf("5"));
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy(String.valueOf("5"));
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        return auditDetails;
    }

    private AssetStatusCriteria getAssetStatusCriteria() {
        final AssetStatusCriteria assetStatusCriteria = new AssetStatusCriteria();
        assetStatusCriteria.setCode(Status.APPROVED.toString());
        assetStatusCriteria.setObjectName(AssetStatusObjectName.REVALUATION.toString());
        assetStatusCriteria.setTenantId("ap.kurnool");
        return assetStatusCriteria;
    }
}
