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
