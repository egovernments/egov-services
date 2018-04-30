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

import org.egov.asset.TestConfiguration;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.AssetCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetQueryBuilder.class)
@Import(TestConfiguration.class)
public class AssetQueryBuilderTest {

    @MockBean
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private AssetQueryBuilder assetQueryBuilder;

    @Mock
    private AssetCriteria assetCriteria;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /*
     * @Test public void getQueryWithTenantIdTest() { final List<Object> preparedStatementValues = new ArrayList<>(); final
     * AssetCriteria assetCriteriaQueryWithTenantId = AssetCriteria.builder().tenantId("ap.kurnool").build();
     * Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault(); final String queryWithTenantId =
     * "SELECT *, asset.id AS assetId,assetcategory.id AS assetcategoryId," + "asset.name as assetname,asset.code as assetcode," +
     * "assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode,ywd.id as ywd_id,ywd.depreciationrate as "
     * + "ywd_depreciationrate,assetcategory.depreciationrate as assetcategory_depreciationrate" + " FROM egasset_asset asset " +
     * "INNER JOIN egasset_assetcategory assetcategory " + "ON asset.assetcategory = assetcategory.id " +
     * "LEFT OUTER JOIN egasset_yearwisedepreciation ywd " + "ON asset.id = ywd.assetid " +
     * "left outer join (select current.assetid,current.tenantid," +
     * " maxcurr.currentamount from egasset_current_value current inner join (select curr.currentamount,curr.assetid,curr.tenantid,curr.createdtime "
     * + " from egasset_current_value " +
     * "curr inner join (select max(createdtime) as createdtime,assetid,tenantid from egasset_current_value where tenantid=? " +
     * " GROUP BY assetid,tenantid)  maxtrans ON  curr.assetid=maxtrans.assetid and " +
     * " curr.tenantid=maxtrans.tenantid and curr.createdtime=maxtrans.createdtime order by createdtime desc) as maxcurr " +
     * "ON current.assetid=maxcurr.assetid AND current.tenantid=maxcurr.tenantid " +
     * " and current.createdtime=maxcurr.createdtime order by maxcurr.createdtime desc) as currentval " +
     * "ON asset.id=currentval.assetid AND asset.tenantid=currentval.tenantid "; // put assertEquals(queryWithTenantId,
     * assetQueryBuilder.getQuery(assetCriteriaQueryWithTenantId, preparedStatementValues)); final List<Object>
     * expectedPreparedStatementValues = new ArrayList<>(); expectedPreparedStatementValues.add("ap.kurnool");
     * //expectedPreparedStatementValues.add("ap.kurnool");
     * assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues)); }
     */

    /*
     * @Test public void getQueryWithTenantId_GrossValueTest() { final List<Object> preparedStatementValues = new ArrayList<>();
     * final AssetCriteria assetCriteriaQueryWithTenantId = AssetCriteria.builder().tenantId("ap.kurnool")
     * .grossValue(Double.valueOf("15.0")).build();
     * Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault(); final String queryWithTenantId =
     * "SELECT *,asset.id AS assetId,assetcategory.id AS assetcategoryId,asset.name as assetname," +
     * "asset.code as assetcode,assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode," +
     * "ywd.id as ywd_id,ywd.depreciationrate as ywd_depreciationrate,assetcategory.depreciationrate as " +
     * "assetcategory_depreciationrate FROM egasset_asset asset INNER JOIN egasset_assetcategory assetcategory ON " +
     * "asset.assetcategory = assetcategory.id LEFT OUTER JOIN egasset_yearwisedepreciation ywd ON " +
     * "asset.id = ywd.assetid WHERE ASSET.tenantId = ? AND ASSET.grossvalue = ? ORDER BY asset.name LIMIT ? OFFSET ?"; // put
     * assertEquals(queryWithTenantId, assetQueryBuilder.getQuery(assetCriteriaQueryWithTenantId, preparedStatementValues)); final
     * List<Object> expectedPreparedStatementValues = new ArrayList<>(); expectedPreparedStatementValues.add("ap.kurnool");
     * expectedPreparedStatementValues.add(Double.valueOf("15.0")); expectedPreparedStatementValues.add(Long.valueOf("500"));
     * expectedPreparedStatementValues.add(Long.valueOf("0"));
     * assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues)); }
     */
    /*
     * @Test public void getQueryWithTenantId_ToCapitalizedValueTest() { final List<Object> preparedStatementValues = new
     * ArrayList<>(); final AssetCriteria assetCriteriaQueryWithTenantId = AssetCriteria.builder().tenantId("ap.kurnool")
     * .toCapitalizedValue(Double.valueOf("15.0")).build();
     * Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault(); final String queryWithTenantId =
     * "SELECT *,asset.id AS assetId,assetcategory.id AS assetcategoryId,asset.name as assetname," +
     * "asset.code as assetcode,assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode," +
     * "ywd.id as ywd_id,ywd.depreciationrate as ywd_depreciationrate,assetcategory.depreciationrate as a" +
     * "ssetcategory_depreciationrate FROM egasset_asset asset INNER JOIN egasset_assetcategory assetcategory ON " +
     * "asset.assetcategory = assetcategory.id LEFT OUTER JOIN egasset_yearwisedepreciation ywd ON asset.id = ywd.assetid" +
     * " WHERE ASSET.tenantId = ? AND ASSET.grossvalue BETWEEN 1 AND ? ORDER BY asset.name LIMIT ? OFFSET ?"; // put
     * assertEquals(queryWithTenantId, assetQueryBuilder.getQuery(assetCriteriaQueryWithTenantId, preparedStatementValues)); final
     * List<Object> expectedPreparedStatementValues = new ArrayList<>(); expectedPreparedStatementValues.add("ap.kurnool");
     * expectedPreparedStatementValues.add(Double.valueOf("15.0")); expectedPreparedStatementValues.add(Long.valueOf("500"));
     * expectedPreparedStatementValues.add(Long.valueOf("0"));
     * assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues)); }
     */
    /*
     * @Test public void getQueryWithTenantId_FromCapitalizedValueAndToCapitalizedValueTest() { final List<Object>
     * preparedStatementValues = new ArrayList<>(); final AssetCriteria assetCriteriaQueryWithTenantId =
     * AssetCriteria.builder().tenantId("ap.kurnool")
     * .fromCapitalizedValue(Double.valueOf("15")).toCapitalizedValue(Double.valueOf("150")).build();
     * Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault(); final String queryWithTenantId =
     * "SELECT *,asset.id AS assetId,assetcategory.id AS assetcategoryId,asset.name as assetname," +
     * "asset.code as assetcode,assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode," +
     * "ywd.id as ywd_id,ywd.depreciationrate as ywd_depreciationrate,assetcategory.depreciationrate as " +
     * "assetcategory_depreciationrate FROM egasset_asset asset INNER JOIN egasset_assetcategory assetcategory ON " +
     * "asset.assetcategory = assetcategory.id LEFT OUTER JOIN egasset_yearwisedepreciation ywd ON " +
     * "asset.id = ywd.assetid WHERE ASSET.tenantId = ? AND ASSET.grossvalue BETWEEN ? AND ? ORDER BY asset.name LIMIT ?" +
     * " OFFSET ?"; // put assertEquals(queryWithTenantId, assetQueryBuilder.getQuery(assetCriteriaQueryWithTenantId,
     * preparedStatementValues)); final List<Object> expectedPreparedStatementValues = new ArrayList<>();
     * expectedPreparedStatementValues.add("ap.kurnool"); expectedPreparedStatementValues.add(Double.valueOf("15"));
     * expectedPreparedStatementValues.add(Double.valueOf("150")); expectedPreparedStatementValues.add(Long.valueOf("500"));
     * expectedPreparedStatementValues.add(Long.valueOf("0"));
     * assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues)); }
     */

    /*
     * @Test public void getQueryWithParametersSet1Test() { final List<Object> preparedStatementValues = new ArrayList<>(); final
     * List<Long> ids = new ArrayList<>(); ids.add(Long.valueOf(10)); ids.add(Long.valueOf(20)); ids.add(Long.valueOf(30)); final
     * AssetCriteria assetCriteriaQueryWithTenantId = AssetCriteria.builder().tenantId("ap.kurnool").id(ids)
     * .block(Long.valueOf(20)).code(String.valueOf(560042)).department(Long.valueOf(30))
     * .description(String.valueOf(123)).build(); Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
     * final String queryWithTenantId =
     * "SELECT *,asset.id AS assetId,assetcategory.id AS assetcategoryId,asset.name as assetname," +
     * "asset.code as assetcode,assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode," +
     * "ywd.id as ywd_id,ywd.depreciationrate as ywd_depreciationrate,assetcategory.depreciationrate as " +
     * "assetcategory_depreciationrate FROM egasset_asset asset INNER JOIN egasset_assetcategory assetcategory ON " +
     * "asset.assetcategory = assetcategory.id LEFT OUTER JOIN egasset_yearwisedepreciation ywd ON " +
     * "asset.id = ywd.assetid WHERE ASSET.tenantId = ? AND ASSET.id IN (10,20,30) AND ASSET.code like ? AND" +
     * " ASSET.department = ? AND ASSET.block = ? AND ASSET.description ilike ? ORDER BY asset.name LIMIT ? OFFSET ?"; // put
     * assertEquals(queryWithTenantId, assetQueryBuilder.getQuery(assetCriteriaQueryWithTenantId, preparedStatementValues)); final
     * List<Object> expectedPreparedStatementValues = new ArrayList<>(); expectedPreparedStatementValues.add("ap.kurnool");
     * expectedPreparedStatementValues.add("%560042%"); expectedPreparedStatementValues.add(30L);
     * expectedPreparedStatementValues.add(20L); expectedPreparedStatementValues.add("%123%");
     * expectedPreparedStatementValues.add(Long.valueOf("500")); expectedPreparedStatementValues.add(Long.valueOf("0"));
     * assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues)); }
     */
    /*
     * @Test public void getQueryWithParametersSet2Test() { final List<Object> preparedStatementValues = new ArrayList<>(); final
     * AssetCriteria assetCriteriaQueryWithTenantId = AssetCriteria.builder().tenantId("ap.kurnool").doorNo("10")
     * .electionWard(Long.valueOf(20)).grossValue(Double.valueOf(30)).locality(Long.valueOf(40)).name("Land")
     * .offset(Long.valueOf(50)).pinCode(Long.valueOf(60)).revenueWard(Long.valueOf(70)).size(Long.valueOf(80))
     * .status("CREATED").street(Long.valueOf(90)).zone(Long.valueOf(100)).build();
     * Mockito.doReturn("80").when(applicationProperties).getSearchPageSizeDefault(); final String queryWithTenantId =
     * "SELECT *,asset.id AS assetId,assetcategory.id AS assetcategoryId,asset.name as " +
     * "assetname,asset.code as assetcode,assetcategory.name AS assetcategoryname,assetcategory.code AS " +
     * "assetcategorycode,ywd.id as ywd_id,ywd.depreciationrate as ywd_depreciationrate,assetcategory.depreciationrate as " +
     * "assetcategory_depreciationrate FROM egasset_asset asset INNER JOIN egasset_assetcategory assetcategory ON " +
     * "asset.assetcategory = assetcategory.id LEFT OUTER JOIN egasset_yearwisedepreciation ywd ON " +
     * "asset.id = ywd.assetid WHERE ASSET.tenantId = ? AND ASSET.name ilike ? AND ASSET.status = ? AND " +
     * "ASSET.locality = ? AND ASSET.zone = ? AND ASSET.revenueWard = ? AND ASSET.street = ? AND ASSET.electionWard = ? " +
     * "AND ASSET.pinCode = ? AND ASSET.doorno = ? AND ASSET.grossvalue = ? ORDER BY asset.name LIMIT ? OFFSET ?"; // put
     * assertEquals(queryWithTenantId, assetQueryBuilder.getQuery(assetCriteriaQueryWithTenantId, preparedStatementValues));
     * System.out.println(preparedStatementValues); final List<Object> expectedPreparedStatementValues = new ArrayList<>();
     * expectedPreparedStatementValues.add("ap.kurnool"); expectedPreparedStatementValues.add("%Land%");
     * expectedPreparedStatementValues.add("CREATED"); expectedPreparedStatementValues.add(40L);
     * expectedPreparedStatementValues.add(100L); expectedPreparedStatementValues.add(70L);
     * expectedPreparedStatementValues.add(90L); expectedPreparedStatementValues.add(20L);
     * expectedPreparedStatementValues.add(60L); expectedPreparedStatementValues.add("10");
     * expectedPreparedStatementValues.add(30.0); expectedPreparedStatementValues.add(80L);
     * expectedPreparedStatementValues.add(3920L); assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues)); }
     */
    @Test
    public void getInsertQuery() {
        final String queryWithTenantId = "INSERT into egasset_asset(id,assetcategory,name,code,department,assetdetails,description,"
                + "dateofcreation,remarks,length,width,totalarea,modeofacquisition,status,tenantid,"
                + "zone,revenueward,street,electionward,doorno,pincode,locality,block,properties,createdby,"
                + "createddate,lastmodifiedby,lastmodifieddate,grossvalue,accumulateddepreciation,assetreference,version,"
                + "depreciationrate,surveynumber,marketvalue,function,scheme,subscheme,purchaseValue,purchaseDate,constructionValue,"
                + " acquisitionValue,acquisitionDate,notApplicableForSaleOrDisposal)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?)";
        assertEquals(queryWithTenantId, assetQueryBuilder.getInsertQuery());
    }

    @Test
    public void getUpdateQuery() {
        final String queryWithTenantId = "UPDATE egasset_asset SET assetcategory=?,name=?,department=?,assetdetails=?,description=?,remarks=?,length=?,width=?,"
                + "totalarea=?,modeofacquisition=?,status=?,zone=?,revenueward=?,street=?,electionward=?,doorno=?,pincode=?,locality=?,"
                + "block=?,properties=?,lastmodifiedby=?,lastmodifieddate=?,grossvalue=?,accumulateddepreciation=?,assetreference=?,version=?,"
                + "surveynumber=?,marketvalue=?,function=?,scheme=?,subscheme=?,purchaseValue=?,purchaseDate=?,"
                + "constructionValue=?,acquisitionValue=?,acquisitionDate=?,notApplicableForSaleOrDisposal=?"
                + "WHERE code=? and tenantid=?";
        assertEquals(queryWithTenantId, assetQueryBuilder.getUpdateQuery());
    }
}
