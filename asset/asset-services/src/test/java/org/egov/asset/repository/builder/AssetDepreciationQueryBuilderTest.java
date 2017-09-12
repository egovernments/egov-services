package org.egov.asset.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.egov.asset.TestConfiguration;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(DepreciationQueryBuilder.class)
@Import(TestConfiguration.class)
public class AssetDepreciationQueryBuilderTest {

    @InjectMocks
    private DepreciationQueryBuilder depreciationQueryBuilder;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private static final String CALCULATIONCURRENTVALUEBASEQUERY = "select first.assetid,first.currentamount as beforeseptembercurrentvalue,"
            + "second.currentamount as afterseptembercurrentvalue," + "first.createdtime as createdtimebeforeseptember,"
            + "second.createdtime as createdtimeafterseptember," + "first.tenantid from "

            + "(select a.*  from egasset_current_value  a" + " inner join "
            + "(select assetid,tenantid,max(createdtime) as createdtime from egasset_current_value";

    private static final String CALCULATIONASSETDETAILSBASEQUERY = "select asset.id as assetid,asset.grossvalue,asset.accumulateddepreciation,"
            + "asset.enableyearwisedepreciation,assetcategory.id as assetcategoryid,asset.department as department,"
            + "assetcategory.depreciationmethod,asset.assetreference,assetcategory.name as assetcategoryname,"
            + "asset.depreciationrate as assetdepreciationrate,"
            + "assetcategory.depreciationrate as assetcategorydepreciationrate,"
            + "ywdep.depreciationrate as yearwisedepreciationrate,ywdep.financialyear,"
            + "assetcategory.accumulateddepreciationaccount,assetcategory.depreciationexpenseaccount"

            + " from egasset_asset asset inner join egasset_assetcategory assetcategory "
            + "ON asset.assetcategory=assetcategory.id left outer join egasset_yearwisedepreciation ywdep"
            + " ON asset.id=ywdep.assetid AND ywdep.financialyear='";

    private static final String CALCULATIONASSETDETAILSSUFFIXQUERY = " AND asset.status='"
            + Status.CAPITALIZED.toString() + "' AND " + "assetcategory.assetcategorytype!='"
            + AssetCategoryType.LAND.toString() + "'" + " AND assetcategory.accumulatedDepreciationAccount is not null"
            + " AND assetcategory.depreciationExpenseAccount is not null";

    @Test
    public void getCalculationCurrentValueQueryTest() {
        final Set<Long> assetIds = new HashSet<Long>();
        final String expectedQueryWithAssetIds = CALCULATIONCURRENTVALUEBASEQUERY
                + " where createdtime <= ? group by assetid,tenantid)"
                + " b ON a.assetid=b.assetid and a.createdtime=b.createdtime and a.tenantid=b.tenantid "
                + "WHERE a.tenantid=?) first left outer join "

                + "(select a.* from egasset_current_value  a" + " inner join "
                + "(select assetid,tenantid,max(createdtime) as createdtime from egasset_current_value"
                + " where createdtime > ?  group by assetid,tenantid)"
                + " b ON a.assetid=b.assetid and a.createdtime=b.createdtime and a.tenantid=b.tenantid "
                + "WHERE a.tenantid=?) second" + " ON first.assetid=second.assetid and first.tenantid=second.tenantid;";
        assertEquals(expectedQueryWithAssetIds, depreciationQueryBuilder.getCalculationCurrentvalueQuery(assetIds));
    }

    @Test
    public void getCalculationCurrentValueQueryWithAssetIdsTest() {
        final Set<Long> assetIds = getAssetIds();
        final String expectedQueryWithAssetIds = CALCULATIONCURRENTVALUEBASEQUERY
                + " where createdtime <= ?AND assetid IN " + getIdQueryForList(assetIds) + " group by assetid,tenantid)"
                + " b ON a.assetid=b.assetid and a.createdtime=b.createdtime and a.tenantid=b.tenantid "
                + "WHERE a.tenantid=?) first left outer join "

                + "(select a.* from egasset_current_value  a" + " inner join "
                + "(select assetid,tenantid,max(createdtime) as createdtime from egasset_current_value"
                + " where createdtime > ? AND assetid IN " + getIdQueryForList(assetIds) + " group by assetid,tenantid)"
                + " b ON a.assetid=b.assetid and a.createdtime=b.createdtime and a.tenantid=b.tenantid "
                + "WHERE a.tenantid=?) second" + " ON first.assetid=second.assetid and first.tenantid=second.tenantid;";
        assertEquals(expectedQueryWithAssetIds, depreciationQueryBuilder.getCalculationCurrentvalueQuery(assetIds));
    }

    @Test
    public void getDepreciationSumQueryTest() {
        final String expectedQuery = "select assetid,SUM(depreciationValue) AS totaldepreciationvalue FROM egasset_depreciation "
                + " where tenantid='ap.kurnool' group by assetid;";
        assertEquals(expectedQuery, depreciationQueryBuilder.getDepreciationSumQuery("ap.kurnool"));
    }

    @Test
    public void getInsertQueryTest() {
        final String expectedQuery = "INSERT INTO egasset_depreciation (id,assetid,financialyear,fromdate,todate,voucherreference,tenantid,status,depreciationrate"
                + ",valuebeforedepreciation,depreciationvalue,valueafterdepreciation,createdby"
                + ",createdtime,lastmodifiedby,lastmodifiedtime,reasonforfailure) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        assertEquals(expectedQuery, depreciationQueryBuilder.getInsertQuery());
    }

    @Test
    public void getCalculationDetailsWithoutAssetIdsQueryTest() {
        final DepreciationCriteria depreciationCriteria = getDepreciationCriteria();
        final String expectedQueryWithOutAssetIds = CALCULATIONASSETDETAILSBASEQUERY + "2017-18"
                + "' WHERE asset.id NOT IN (SELECT assetid from egasset_depreciation where createdtime>="
                + "1490985000000" + " AND createdtime<=" + "1522434600000)" + CALCULATIONASSETDETAILSSUFFIXQUERY;
        assertEquals(expectedQueryWithOutAssetIds,
                depreciationQueryBuilder.getCalculationAssetDetailsQuery(depreciationCriteria));
    }

    @Test
    public void getCalculationDetailsWithAssetIdsQueryTest() {
        final Set<Long> assetIds = getAssetIds();
        final DepreciationCriteria depreciationCriteria = getDepreciationCriteria();
        depreciationCriteria.setAssetIds(assetIds);
        final String expectedQueryWithOutAssetIds = CALCULATIONASSETDETAILSBASEQUERY + "2017-18"
                + "' WHERE asset.id NOT IN (SELECT assetid from egasset_depreciation where createdtime>="
                + "1490985000000" + " AND createdtime<=" + "1522434600000) AND asset.id IN "
                + getIdQueryForList(assetIds) + CALCULATIONASSETDETAILSSUFFIXQUERY;
        assertEquals(expectedQueryWithOutAssetIds,
                depreciationQueryBuilder.getCalculationAssetDetailsQuery(depreciationCriteria));
    }

    @Test
    public void getDepreciationQueryTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
        depreciationCriteria.setTenantId("ap.kurnool");
        depreciationCriteria.setAssetIds(getAssetIds());
        final String expectedQuery = "SELECT * FROM egasset_depreciation  WHERE tenantid=? AND assetid IN "
                + getIdQueryForList(depreciationCriteria.getAssetIds()) +" ORDER BY id";
        assertEquals(expectedQuery,
                depreciationQueryBuilder.getDepreciationSearchQuery(depreciationCriteria, preparedStatementValues));
    }

    private Set<Long> getAssetIds() {
        final Set<Long> assetIds = new HashSet<Long>();
        assetIds.add(Long.valueOf("1"));
        assetIds.add(Long.valueOf("2"));
        return assetIds;
    }

    private DepreciationCriteria getDepreciationCriteria() {
        final DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
        depreciationCriteria.setFinancialYear("2017-18");
        depreciationCriteria.setFromDate(Long.valueOf("1490985000000"));
        depreciationCriteria.setToDate(Long.valueOf("1522434600000"));
        return depreciationCriteria;
    }

    private static String getIdQueryForList(final Set<Long> idList) {

        final StringBuilder query = new StringBuilder("(");
        final Iterator<Long> itr = idList.iterator();
        query.append(itr.next());
        for (int i = 1; i < idList.size(); i++)
            query.append("," + itr.next());
        return query.append(")").toString();
    }
}
