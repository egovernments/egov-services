package org.egov.demand.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.repository.querybuilder.GlCodeMasterQueryBuilder;
import org.egov.demand.repository.rowmapper.GlCodeMasterRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class GlCodeMasterRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@InjectMocks
	private GlCodeMasterRepository glCodeMasterRepository;
	
	@Mock
	private GlCodeMasterQueryBuilder glCodeMasterQueryBuilder;
	@Mock
	private GlCodeMasterRowMapper glCodeMasterRowMapper;
	
	@Test
	public void testFindForCriteria() {
		List<GlCodeMaster> glCodeMaster = new ArrayList<>();
		glCodeMaster.add(getGlCodeMaster());
		String query ="";
		when(glCodeMasterQueryBuilder.getQuery(new GlCodeMasterCriteria(),new ArrayList<>())).thenReturn(query);
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(GlCodeMasterRowMapper.class))).thenReturn(glCodeMaster);

		assertTrue(glCodeMaster.equals(glCodeMasterRepository.findForCriteria(new GlCodeMasterCriteria())));
	}
	
	private GlCodeMaster getGlCodeMaster(){
		GlCodeMaster glCodeMaster=new GlCodeMaster();
		
		glCodeMaster.setId("12");
		glCodeMaster.setService("string");
		glCodeMaster.setTaxHead("string");
		glCodeMaster.setTenantId("ap.kurnool");
		glCodeMaster.setGlCode("string");
		glCodeMaster.setFromDate(0l);
		glCodeMaster.setToDate(0l);
		
		return glCodeMaster;
	}
}
