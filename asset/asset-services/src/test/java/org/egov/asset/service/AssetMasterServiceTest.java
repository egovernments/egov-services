/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

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
