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

        currentValueService.createCurrentValue(assetCurrentValueRequest);
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
