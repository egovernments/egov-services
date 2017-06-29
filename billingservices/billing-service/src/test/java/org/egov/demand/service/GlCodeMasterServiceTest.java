package org.egov.demand.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.repository.GlCodeMasterRepository;
import org.egov.demand.web.contract.GlCodeMasterResponse;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GlCodeMasterServiceTest {
	
	@InjectMocks
	GlCodeMasterService glCodeMasterService;
	
	@Mock
	private ResponseFactory responseInfoFactory;
	
	@Mock
	private GlCodeMasterRepository glCodeMasterRepository;
	
	@Test
	public void testSearch() {
		List<GlCodeMaster> glCodeMasters = new ArrayList<>();
		glCodeMasters.add(getGlCodeMaster());
		GlCodeMasterResponse glCodeMasterResponse = new GlCodeMasterResponse();
		glCodeMasterResponse.setGlCodeMasters(glCodeMasters);
		
		when(glCodeMasterRepository.findForCriteria(Matchers.any(GlCodeMasterCriteria.class)))
				.thenReturn(glCodeMasters);
		
		GlCodeMasterCriteria taxHeadMasterCriteria = GlCodeMasterCriteria.builder().tenantId("ap.kurnool").build();
		assertEquals(glCodeMasterResponse, glCodeMasterService.getGlCodes(taxHeadMasterCriteria, new RequestInfo()));
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
