package org.egov.eis.service;

import java.util.List;

import org.egov.eis.model.Designation;
import org.egov.eis.repository.DesignationRepository;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.egov.eis.web.contract.DesignationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DesignationServiceTest {

	    @Mock
	    private DesignationRepository designationRepository;

	    @Mock
	    private List<Designation> designations;

	    @InjectMocks
	    private DesignationService designationService;
	    
	    @Test
	    public void test_getDesignation() {
	    	DesignationGetRequest criteria = new DesignationGetRequest().builder().tenantId("default").code("100").active(true).build();
	        when(designationRepository.findForCriteria(criteria)).thenReturn(designations);
	        List<Designation> result = designationService.getDesignations(criteria);   
	        
	        assertThat(result).isEqualTo(designations);
	    }
 	    
}
