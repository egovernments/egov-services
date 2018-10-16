package org.egov.demand.repository.builder;

import org.egov.demand.TestConfiguration;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.repository.querybuilder.TaxHeadMasterQueryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@WebMvcTest(TaxHeadMasterQueryBuilder.class)
@Import(TestConfiguration.class)
@ActiveProfiles("test")
public class TaxHeadMasterQueryBuilderTest {

	@MockBean
	private ApplicationProperties applicationProperties;
	
	@MockBean
	private TaxHeadMasterCriteria taxHeadMasterCriteria;
	
	@InjectMocks
	private TaxHeadMasterQueryBuilder taxHeadMasterQueryBuilder;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	private String queryWithTenantId = "SELECT *,taxhead.id AS taxheadId, taxhead.tenantid AS taxheadTenantid,"
			+ " taxhead.service taxheadService, taxhead.createdby AS taxcreatedby, taxhead.createdtime AS taxcreatedtime,"
			+ " taxhead.lAStmodifiedby AS taxlAStmodifiedby, taxhead.lAStmodifiedtime AS taxlAStmodifiedtime,"
			+ "glcode.id AS glCodeId, glcode.tenantid AS glCodeTenantId,glcode.service AS glCodeService,"
			+ " glcode.createdby AS glcreatedby, glcode.createdtime AS glcreatedtime,"
			+ " glcode.lastmodifiedby AS gllastmodifiedby, glcode.lastmodifiedtime AS gllastmodifiedtime"
			+ " FROM egbs_taxheadmaster taxhead LEFT OUTER Join egbs_glcodemaster glcode "
			+ " ON taxhead.code=glcode.taxhead and taxhead.tenantid=glcode.tenantid "
			+ " WHERE taxhead.tenantId = ?  ORDER BY taxhead.validfrom,taxhead.code LIMIT ? OFFSET ?";
	
	private String insertQuery = "INSERT INTO egbs_taxheadmaster(id, tenantid, category, service, name, code,"
			+ " isdebit,isactualdemand, orderno, validfrom, validtill, createdby, createdtime, lastmodifiedby,"
			+ " lastmodifiedtime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private String updateQuery  = "UPDATE public.egbs_taxheadmaster SET  category = ?, service = ?,"
			+ " name = ?, code=?, isdebit = ?, isactualdemand = ?, orderno = ?, validfrom = ?, validtill = ?,"
			+ " lastmodifiedby = ?, lastmodifiedtime = ? WHERE tenantid = ? and id = ?";
	
	@Test
	public void getQueryTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		TaxHeadMasterCriteria taxHeadMasterCriteriaQuery = TaxHeadMasterCriteria.builder().tenantId("ap.kurnool").build();
		Mockito.doReturn("500").when(applicationProperties).commonsSearchPageSizeDefault();
		
		assertEquals(queryWithTenantId,
				taxHeadMasterQueryBuilder.getQuery(taxHeadMasterCriteriaQuery, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("500"));
		expectedPreparedStatementValues.add(Long.valueOf("0"));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}
	
	@Test
	public void getInsertQueryTest() {
		assertEquals(insertQuery, taxHeadMasterQueryBuilder.Insert_Query);
	}
	
	@Test
	public void getUpdateQueryTest() {
		assertEquals(updateQuery, taxHeadMasterQueryBuilder.Update_Query);
	}
}
