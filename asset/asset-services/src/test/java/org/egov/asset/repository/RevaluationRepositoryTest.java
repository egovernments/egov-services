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

package org.egov.asset.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.StatusValue;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.repository.builder.RevaluationQueryBuilder;
import org.egov.asset.repository.rowmapper.RevaluationRowMapper;
import org.egov.asset.service.AssetMasterService;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RevaluationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RevaluationQueryBuilder revaluationQueryBuilder;

    @Mock
    private RevaluationRowMapper revaluationRowMapper;

    @Mock
    private AssetMasterService assetMasterService;

    @InjectMocks
    private RevaluationRepository revaluationRepository;

    @Test
    public void test_should_search_Revaluations() {

        final List<Revaluation> revaluations = new ArrayList<>();
        final Revaluation revaluation = getRevaluationFromDB();
        revaluations.add(revaluation);
        when(revaluationQueryBuilder.getQuery(any(RevaluationCriteria.class), any(List.class)))
                .thenReturn(StringUtils.EMPTY);
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(RevaluationRowMapper.class)))
                .thenReturn(revaluations);
        assertTrue(revaluations.equals(revaluationRepository.search(new RevaluationCriteria())));
    }

    @Test
    public void testCreateRevaluation() {
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        final Revaluation revaluation = getRevaluationFromDB();
        final RevaluationRequest revaluationRequest = new RevaluationRequest();
        revaluationRequest.setRequestInfo(requestInfo);
        revaluationRequest.setRevaluation(revaluation);
        final List<AssetStatus> assetStatuses = new ArrayList<>();
        final AssetStatus assetStatus = getRevaluationApprovedStatus();
        assetStatuses.add(assetStatus);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        when(assetMasterService.getStatuses(any(AssetStatusObjectName.class), any(Status.class), any(String.class)))
                .thenReturn(assetStatuses);
        revaluationRepository.create(revaluationRequest);
    }

    private AssetStatus getRevaluationApprovedStatus() {
        final AssetStatus assetStatus = new AssetStatus();
        assetStatus.setObjectName(AssetStatusObjectName.REVALUATION.toString());
        assetStatus.setTenantId("ap.kurnool");
        final List<StatusValue> statusValues = new ArrayList<>();
        final StatusValue statusValue = new StatusValue();
        statusValue.setCode(Status.APPROVED.toString());
        statusValue.setName(Status.APPROVED.toString());
        statusValues.add(statusValue);
        assetStatus.setStatusValues(statusValues);
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("2");
        auditDetails.setCreatedDate(Long.valueOf("1496746205544"));
        auditDetails.setLastModifiedBy("2");
        auditDetails.setLastModifiedDate(Long.valueOf("1496746205544"));
        assetStatus.setAuditDetails(auditDetails);
        return assetStatus;
    }

    private Revaluation getRevaluationFromDB() {
        final Revaluation revaluation = new Revaluation();
        revaluation.setAssetId(Long.valueOf("298"));
        revaluation.setComments("Do revaluation of asset");
        revaluation.setCurrentCapitalizedValue(new BigDecimal("5000"));
        revaluation.setRevaluationAmount(new BigDecimal("678"));
        revaluation.setValueAfterRevaluation(new BigDecimal("5678"));
        revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("6"));
        revaluation.setFunction(Long.valueOf("5"));
        revaluation.setFund(Long.valueOf("1"));
        revaluation.setId(Long.valueOf("2"));
        revaluation.setReasonForRevaluation("Asset Features Enhanced");
        revaluation.setReevaluatedBy("Raman");
        revaluation.setRevaluationDate(Long.valueOf("1495978422356"));
        revaluation.setScheme(null);
        revaluation.setSubScheme(null);
        revaluation.setStatus(Status.APPROVED.toString());
        revaluation.setTenantId("ap.kurnool");
        revaluation.setTypeOfChange(TypeOfChangeEnum.INCREASED);
        revaluation.setVoucherReference("12660");
        return revaluation;
    }
}
