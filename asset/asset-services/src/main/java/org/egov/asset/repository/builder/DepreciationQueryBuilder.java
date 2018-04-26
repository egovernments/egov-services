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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class DepreciationQueryBuilder {

    private static final String DEPRECIATION_BASE_SEARCH_QUERY = "SELECT * FROM egasset_depreciation ";

    public String getDepreciationSumQuery(final String tenantId) {

        return "select assetid,SUM(depreciationValue) AS totaldepreciationvalue FROM egasset_depreciation "
                + " where tenantid='" + tenantId + "' group by assetid;";
    }

    public String getCalculationCurrentvalueQuery(final Set<Long> assetIds) {

        String assetIdString = "";
        if (assetIds != null && !assetIds.isEmpty())
            assetIdString = "AND assetid IN " + getIdQueryForList(assetIds);

        return "select first.assetid,first.currentamount as beforeseptembercurrentvalue,"
                + "second.currentamount as afterseptembercurrentvalue,"
                + "first.createdtime as createdtimebeforeseptember,"
                + "second.createdtime as createdtimeafterseptember," + "first.tenantid from "

                + "(select a.*  from egasset_current_value  a" + " inner join "
                + "(select assetid,tenantid,max(createdtime) as createdtime from egasset_current_value"
                + " where createdtime <= ?" + assetIdString + " group by assetid,tenantid)"
                + " b ON a.assetid=b.assetid and a.createdtime=b.createdtime and a.tenantid=b.tenantid "
                + "WHERE a.tenantid=?) first"

                + " left outer join "

                + "(select a.* from egasset_current_value  a" + " inner join "
                + "(select assetid,tenantid,max(createdtime) as createdtime from egasset_current_value"
                + " where createdtime > ? " + assetIdString + " group by assetid,tenantid)"
                + " b ON a.assetid=b.assetid and a.createdtime=b.createdtime and a.tenantid=b.tenantid "
                + "WHERE a.tenantid=?) second" + " ON first.assetid=second.assetid and first.tenantid=second.tenantid;";
    }

    public String getCalculationAssetDetailsQuery(final DepreciationCriteria depreciationCriteria) {

        final Set<Long> assetIds = depreciationCriteria.getAssetIds();
        String assetIdString = "";
        if (assetIds != null && !assetIds.isEmpty())
            assetIdString = " AND asset.id IN " + getIdQueryForList(assetIds);

        final StringBuilder sql = new StringBuilder(
                "select asset.id as assetid,asset.grossvalue,asset.accumulateddepreciation,"
                        + "asset.enableyearwisedepreciation,assetcategory.id as assetcategoryid,asset.department as department,"
                        + "assetcategory.depreciationmethod,asset.assetreference,assetcategory.name as assetcategoryname,"
                        + "asset.depreciationrate as assetdepreciationrate,"
                        + "assetcategory.depreciationrate as assetcategorydepreciationrate,"
                        + "ywdep.depreciationrate as yearwisedepreciationrate,ywdep.financialyear,"
                        + "assetcategory.accumulateddepreciationaccount,assetcategory.depreciationexpenseaccount"

                        + " from egasset_asset asset inner join egasset_assetcategory assetcategory "
                        + "ON asset.assetcategory=assetcategory.id left outer join egasset_yearwisedepreciation ywdep"
                        + " ON asset.id=ywdep.assetid AND ywdep.financialyear='"
                        + depreciationCriteria.getFinancialYear()
                        + "' WHERE asset.id NOT IN (SELECT assetid from egasset_depreciation where createdtime>="
                        + depreciationCriteria.getFromDate() + " AND createdtime<=" + depreciationCriteria.getToDate()
                        + ")" + assetIdString + " AND asset.status='" + Status.CAPITALIZED.toString() + "' AND "
                        + "assetcategory.assetcategorytype!='" + AssetCategoryType.LAND.toString() + "'"
                        + " AND assetcategory.accumulatedDepreciationAccount is not null"
                        + " AND assetcategory.depreciationExpenseAccount is not null");

        return sql.toString();
    }

    private static String getIdQueryForList(final Set<Long> idList) {

        final StringBuilder query = new StringBuilder("(");
        final Iterator<Long> itr = idList.iterator();
        query.append(itr.next());
        for (int i = 1; i < idList.size(); i++)
            query.append("," + itr.next());
        return query.append(")").toString();
    }

    public String getInsertQuery() {

        return "INSERT INTO egasset_depreciation (id,assetid,financialyear,fromdate,todate,voucherreference,tenantid,status,depreciationrate"
                + ",valuebeforedepreciation,depreciationvalue,valueafterdepreciation,createdby"
                + ",createdtime,lastmodifiedby,lastmodifiedtime,reasonforfailure) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
    }

    public String getDepreciationSearchQuery(final DepreciationCriteria depreciationCriteria,
            final List<Object> preparedStatementValues) {

        final StringBuilder searchquery = new StringBuilder(DEPRECIATION_BASE_SEARCH_QUERY);
        searchquery.append(" WHERE tenantid=?");
        preparedStatementValues.add(depreciationCriteria.getTenantId());

        if (depreciationCriteria.getAssetIds() != null && !depreciationCriteria.getAssetIds().isEmpty())
            searchquery.append(" AND assetid IN " + getIdQueryForList(depreciationCriteria.getAssetIds()));
        searchquery.append(" ORDER BY id");
        return searchquery.toString();
    }

    static private final String BASEDEPRECIATIONQUERY = "select asset.code as assetcode ,asset.name as assetname,asset.id as assetid,asset.tenantid,asset.grossvalue,asset.accumulateddepreciation,"
            + " assetcategory.id as assetcategory,asset.department as department,asset.function as function, assetcategory.name as assetcategoryname,"
            + " currentval.currentamount as currentvalue,depreciation.maxtodate as lastdepreciationdate,depreciation.depreciationvaluesum ,"
            + " asset.dateofcreation,assetcategory.depreciationrate as assetcategory_depreciationrate ,assetcategory.depreciationmethod as depreciationmethod,assetcategory.assetaccount as assetaccount ,"
            + " assetcategory.accumulateddepreciationaccount as accumulateddepreciationaccount,"
            + " assetcategory.revaluationreserveaccount as revaluationreserveaccount,assetcategory.depreciationexpenseaccount as depreciationexpenseaccount "
         

            + "from egasset_asset asset  INNER JOIN egasset_assetcategory assetcategory "
            + " ON asset.assetcategory = assetcategory.id  "
            + " left outer join (select currval.assetid,currval.createdtime,currval.currentamount,currval.tenantid "

            + "from egasset_current_value currval inner join (select assetid,tenantid,max(createdtime) as createdtime from egasset_current_value where tenantid=? "

            + "group by assetid,tenantid) maxval ON currval.assetid=maxval.assetid AND currval.tenantid=maxval.tenantid AND currval.createdtime=maxval.createdtime) "

            + "currentval ON asset.id=currentval.assetid AND asset.tenantid=currentval.tenantid left outer join "

            + "(select dep.assetid as assetid,dep.tenantid,depsum.depreciationvaluesum,maxdep.maxtodate from egasset_depreciation dep inner join "

            + "(select assetid,tenantid,sum(depreciationvalue) as depreciationvaluesum from egasset_depreciation where tenantid=? AND assetid NOT IN "

            + "(select assetid from egasset_depreciation where todate>=? ) AND status='SUCCESS' group by assetid,tenantid) depsum ON dep.assetid=depsum.assetid "

            + "AND dep.tenantid=depsum.tenantid inner join (select assetid,tenantid,max(todate) as maxtodate from egasset_depreciation dep "

            + "where tenantid=? and status='SUCCESS' AND assetid NOT IN (select assetid from egasset_depreciation where todate>=?) "

            + "group by assetid,tenantid) maxdep ON maxdep.assetid=dep.assetid AND maxdep.tenantid=dep.tenantid  AND maxdep.maxtodate=dep.todate) "

            + "depreciation ON asset.id=depreciation.assetid AND asset.tenantid=depreciation.tenantid WHERE assetcategory.assetcategorytype!='LAND' "

            + "AND asset.tenantid=? AND asset.id NOT IN (select assetid from egasset_depreciation where todate>=? AND status='SUCCESS') "

            + "AND asset.dateofcreation<=? {assetids}";

    public String getDepreciationQuery(final DepreciationCriteria depreciationCriteria,
            final List<Object> preparedStatementValues) {
        //preparedStatementValues.add(depreciationCriteria.getFinancialYear());
        preparedStatementValues.add(depreciationCriteria.getTenantId());
        preparedStatementValues.add(depreciationCriteria.getTenantId());
        preparedStatementValues.add(depreciationCriteria.getToDate());
        preparedStatementValues.add(depreciationCriteria.getTenantId());
        preparedStatementValues.add(depreciationCriteria.getToDate());
        preparedStatementValues.add(depreciationCriteria.getTenantId());
        preparedStatementValues.add(depreciationCriteria.getToDate());
        preparedStatementValues.add(depreciationCriteria.getToDate());

        if (depreciationCriteria.getAssetIds() != null && !depreciationCriteria.getAssetIds().isEmpty())
            return BASEDEPRECIATIONQUERY.replace("{assetids}",
                    " AND asset.id IN (" + getIdQuery(depreciationCriteria.getAssetIds()) + ")");
        else
            return BASEDEPRECIATIONQUERY.replace("{assetids}", "");

    }

    private static String getIdQuery(final Set<Long> idSet) {
        StringBuilder query = null;
        Long[] arr = new Long[idSet.size()];
        arr = idSet.toArray(arr);
        query = new StringBuilder(arr[0].toString());
        for (int i = 1; i < arr.length; i++)
            query.append("," + arr[i]);
        return query.toString();
    }
}
