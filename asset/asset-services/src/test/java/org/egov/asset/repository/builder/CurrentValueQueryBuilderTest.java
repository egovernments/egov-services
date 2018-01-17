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

package org.egov.asset.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.egov.asset.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(CurrentValueQueryBuilder.class)
@Import(TestConfiguration.class)
public class CurrentValueQueryBuilderTest {

    @InjectMocks
    private CurrentValueQueryBuilder currentValueQueryBuilder;

    private static final String CURRENTVALUEBASEQUERY = "SELECT ungroupedvalue.* FROM egasset_current_value ungroupedvalue "
            + "INNER join (SELECT assetid,max(createdtime) AS createdtime FROM egasset_current_value "
            + "WHERE tenantid='ap.kurnool'";

    @Test
    public void getCurrentValueQueryWithoutAssetIdsTest() {
        final Set<Long> assetIds = new HashSet<Long>();
        final String expectedQuery = CURRENTVALUEBASEQUERY + " group by assetid) groupedvalue "
                + "ON ungroupedvalue.assetid=groupedvalue.assetid and ungroupedvalue.createdtime=groupedvalue.createdtime "
                + "where ungroupedvalue.tenantid='ap.kurnool' ";
        assertEquals(expectedQuery, currentValueQueryBuilder.getCurrentValueQuery(assetIds, "ap.kurnool"));
    }

    @Test
    public void getCurrentValueQueryWithAssetIdsTest() {
        final Set<Long> assetIds = getAssetIds();
        final String assetIdString = "AND assetid IN " + getIdQueryForList(assetIds);
        final String expectedQuery = CURRENTVALUEBASEQUERY + assetIdString + " group by assetid) groupedvalue "
                + "ON ungroupedvalue.assetid=groupedvalue.assetid and ungroupedvalue.createdtime=groupedvalue.createdtime "
                + "where ungroupedvalue.tenantid='ap.kurnool' "
                + assetIdString.replace("assetid", "ungroupedvalue.assetid");
        assertEquals(expectedQuery, currentValueQueryBuilder.getCurrentValueQuery(assetIds, "ap.kurnool"));
    }

    @Test
    public void getInsertQueryTest() {
        final String expectedQuery = "INSERT INTO egasset_current_value (id,assetid,tenantid,assettrantype,currentamount,createdby,"
                + "createdtime,lastModifiedby,lastModifiedtime) VALUES (?,?,?,?,?,?,?,?,?)";
        assertEquals(expectedQuery, currentValueQueryBuilder.getInsertQuery());
    }

    private Set<Long> getAssetIds() {
        final Set<Long> assetIds = new HashSet<Long>();
        assetIds.add(Long.valueOf("1"));
        assetIds.add(Long.valueOf("2"));
        return assetIds;
    }

    private static String getIdQueryForList(final Set<Long> idList) {

        final StringBuilder query = new StringBuilder("(");
        final Long[] list = idList.toArray(new Long[idList.size()]);
        query.append(list[0]);
        for (int i = 1; i < idList.size(); i++)
            query.append("," + list[i]);
        return query.append(")").toString();
    }

}
