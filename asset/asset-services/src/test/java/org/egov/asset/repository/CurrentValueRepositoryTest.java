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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.repository.builder.CurrentValueQueryBuilder;
import org.egov.asset.repository.rowmapper.CurrentValueRowMapper;
import org.egov.asset.service.AssetConfigurationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class CurrentValueRepositoryTest {

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private CurrentValueQueryBuilder currentValueQueryBuilder;

    @Mock
    private CurrentValueRowMapper currentValueRowMapper;

    @InjectMocks
    private CurrentValueRepository currentValueRepository;

    @Test
    public void test_getCurrentValues() {
        final List<AssetCurrentValue> expectedAssetCurrentValues = getAssetCurrentValues();
        final Set<Long> assetIds = new HashSet<Long>();
        assetIds.add(Long.valueOf("2"));
        when(jdbcTemplate.query(any(String.class), any(CurrentValueRowMapper.class)))
                .thenReturn(expectedAssetCurrentValues);
        final List<AssetCurrentValue> actualAssetCurrentValues = currentValueRepository.getCurrentValues(assetIds,
                "ap.kurnool");

        assertEquals(expectedAssetCurrentValues.toString(), actualAssetCurrentValues.toString());

    }

    @Test
    public void test_create() {
        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETBATCHSIZE,
                "ap.kurnool")).thenReturn("500");
        currentValueRepository.create(getAssetCurrentValues());
    }

    private List<AssetCurrentValue> getAssetCurrentValues() {
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
