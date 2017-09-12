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
}
