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

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class CurrentValueQueryBuilder {

    public String getInsertQuery() {
        return "INSERT INTO egasset_current_value (id,assetid,tenantid,assettrantype,currentamount,createdby,"
                + "createdtime,lastModifiedby,lastModifiedtime) VALUES (?,?,?,?,?,?,?,?,?)";
    }

    public String getCurrentValueQuery(final Set<Long> assetIds, final String tenantId) {

        String assetIdString = "";
        if (assetIds != null && !assetIds.isEmpty())
            assetIdString = "AND assetid IN " + getIdQueryForList(assetIds);

        return "SELECT ungroupedvalue.* FROM egasset_current_value ungroupedvalue "
                + "INNER join (SELECT assetid,max(createdtime) AS createdtime FROM egasset_current_value "
                + "WHERE tenantid='" + tenantId + "'" + assetIdString + " group by assetid) groupedvalue "
                + "ON ungroupedvalue.assetid=groupedvalue.assetid and ungroupedvalue.createdtime=groupedvalue.createdtime "
                + "where ungroupedvalue.tenantid='" + tenantId + "' " + assetIdString.replace(
                        "assetid", "ungroupedvalue.assetid");
    }

    private static String getIdQueryForList(final Set<Long> idList) {

        final StringBuilder query = new StringBuilder("(");
        final Long[] list = idList.toArray(new Long[idList.size()]);
        query.append(list[0]);
        for (int i = 1; i < idList.size(); i++)
            query.append("," + list[i]);
        return query.append(")").toString();
    }

    public String getUpdateQuery() {
        return "UPDATE egasset_current_value SET assetid=?,currentamount=? ,lastmodifiedby=?,lastModifiedtime=?"
                + " WHERE id=? and tenantid=?";
    }
}
