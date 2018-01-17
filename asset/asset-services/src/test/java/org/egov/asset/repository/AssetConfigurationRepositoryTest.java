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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.model.AssetConfigurationCriteria;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.repository.builder.AssetConfigurationQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetConfigurationKeyValuesRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class AssetConfigurationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AssetConfigurationKeyValuesRowMapper assetConfigurationKeyValuesRowMapper;

    @Mock
    private AssetConfigurationQueryBuilder assetConfigurationQueryBuilder;

    @InjectMocks
    private AssetConfigurationRepository assetConfigurationRepository;

    @Test
    public void test_findForCriteria() {
        final Map<String, List<String>> expectedAssetConfigurationMap = getAssetConfigurationMap();
        final AssetConfigurationCriteria assetConfigurationCriteria = getAssetConfigurationCriteria();

        when(jdbcTemplate.query(any(String.class), any(Object[].class),
                any(AssetConfigurationKeyValuesRowMapper.class))).thenReturn(expectedAssetConfigurationMap);
        final Map<String, List<String>> actualAssetConfigurationMap = assetConfigurationRepository
                .findForCriteria(assetConfigurationCriteria);

        assertEquals(expectedAssetConfigurationMap.toString(), actualAssetConfigurationMap.toString());
    }

    private AssetConfigurationCriteria getAssetConfigurationCriteria() {
        final AssetConfigurationCriteria assetConfigurationCriteria = new AssetConfigurationCriteria();
        assetConfigurationCriteria.setName(AssetConfigurationKeys.ENABLEVOUCHERGENERATION.toString());
        assetConfigurationCriteria.setTenantId("ap.kurnool");
        return assetConfigurationCriteria;
    }

    private Map<String, List<String>> getAssetConfigurationMap() {
        final Map<String, List<String>> assetConfiguration = new HashMap<String, List<String>>();
        final List<String> configValues = new ArrayList<String>();
        configValues.add("true");
        assetConfiguration.put(AssetConfigurationKeys.ENABLEVOUCHERGENERATION.toString(), configValues);
        return assetConfiguration;
    }

}
