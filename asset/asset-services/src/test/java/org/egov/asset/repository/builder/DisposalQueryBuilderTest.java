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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.DisposalCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(DisposalQueryBuilder.class)
@Import(TestConfiguration.class)
public class DisposalQueryBuilderTest {

	@MockBean
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private DisposalQueryBuilder disposalQueryBuilder;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getQueryWithTenantIdTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
		String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,createdby,createddate,lastmodifiedby,lastmodifieddate,profitlossvoucherreference FROM egasset_disposal as disposal WHERE disposal.tenantId = ? AND disposal.assetid IN (10,20) LIMIT ? OFFSET ?";

		List<Long> assetIds = new ArrayList<>();
		assetIds.add(10L);
		assetIds.add(20L);
		DisposalCriteria disposalCriteria = DisposalCriteria.builder().tenantId("ap.kurnool").assetId(assetIds).build();

		assertEquals(expectedQueryWithTenantId,
				disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues));
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("500"));
		expectedPreparedStatementValues.add(Long.valueOf("0"));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryWithIdTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
		String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,createdby,createddate,lastmodifiedby,lastmodifieddate,profitlossvoucherreference FROM egasset_disposal as disposal WHERE disposal.tenantId = ? AND disposal.id IN (2,6) LIMIT ? OFFSET ?";
		List<Long> ids = new ArrayList<>();
		ids.add(2L);
		ids.add(6L);
		DisposalCriteria disposalCriteria = DisposalCriteria.builder().tenantId("ap.kurnool").id(ids).build();
		assertEquals(expectedQueryWithTenantId,
				disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues));
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("500"));
		expectedPreparedStatementValues.add(Long.valueOf("0"));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryWithSizeTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
		String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,createdby,createddate,lastmodifiedby,lastmodifieddate,profitlossvoucherreference FROM egasset_disposal as disposal WHERE disposal.tenantId = ? LIMIT ? OFFSET ?";
		DisposalCriteria disposalCriteria = DisposalCriteria.builder().tenantId("ap.kurnool").size(Long.valueOf(80))
				.build();

		assertEquals(expectedQueryWithTenantId,
				disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("80"));
		expectedPreparedStatementValues.add(Long.valueOf("0"));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}
}
