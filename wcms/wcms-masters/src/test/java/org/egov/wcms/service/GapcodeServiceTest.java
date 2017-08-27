package org.egov.wcms.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.repository.GapcodeRepository;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GapcodeServiceTest {
	
	
	    @Mock
	    private GapcodeRepository gapcodeRepository;

	    @Mock
	    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	    @Mock
	    private CodeGeneratorService codeGeneratorService;

	    @InjectMocks
	    private GapcodeService gapcodeService;

	    @Test
	    public void test_Search_For_Gapcode() {
	        final List<Gapcode> gapcodeList = new ArrayList<>();
	        final Gapcode gapcode = Mockito.mock(Gapcode.class);
	        gapcodeList.add(gapcode);

	        when(gapcodeRepository.findForCriteria(any(GapcodeGetRequest.class))).thenReturn(gapcodeList);
	        assertTrue(gapcodeList.equals(gapcodeService.getGapcodes(any(GapcodeGetRequest.class))));
	    }

	    @Test
	    public void test_Search_For_Gapcode_Notnull() {
	        final List<Gapcode> gapcodeList = new ArrayList<>();
	        final Gapcode gapcode = Mockito.mock(Gapcode.class);
	        gapcodeList.add(gapcode);

	        when(gapcodeRepository.findForCriteria(any(GapcodeGetRequest.class))).thenReturn(gapcodeList);
	        assertNotNull(gapcodeService.getGapcodes(any(GapcodeGetRequest.class)));
	    }

	    @Test
	    public void test_Search_For_Gapcode_Null() {
	        final List<Gapcode> gapcodeList = new ArrayList<>();
	        final Gapcode gapcode = Mockito.mock(Gapcode.class);
	        gapcodeList.add(gapcode);

	        when(gapcodeRepository.findForCriteria(any(GapcodeGetRequest.class))).thenReturn(null);
	        assertNull(gapcodeService.getGapcodes(any(GapcodeGetRequest.class)));
	    }


	    @Test
	    public void test_throwException_Create_Gapcode() {

	        final List<Gapcode> gapcodeList = new ArrayList<>();
	        gapcodeList.add(getGapcode());
	        final GapcodeRequest gapcodeRequest = new GapcodeRequest();
	        gapcodeRequest.setGapcode(gapcodeList);
	        when(gapcodeRepository.persist(any(GapcodeRequest.class)))
	                .thenReturn(gapcodeRequest);
	        assertTrue(gapcodeRequest.equals(gapcodeService.create(gapcodeRequest)));
	    }

	    @SuppressWarnings("unchecked")
	    @Test(expected = Exception.class)
	    public void test_throwException_Update_Gapcode() throws Exception {

	        final GapcodeRequest gapcodeRequest = Mockito.mock(GapcodeRequest.class);
	        when(gapcodeRepository.persistUpdate(gapcodeRequest)).thenThrow(Exception.class);

	        assertTrue(gapcodeRequest.equals(gapcodeService.update(gapcodeRequest)));
	    }

	    private Gapcode getGapcode() {
	        final Gapcode gapcode = new Gapcode();
	        gapcode.setId(2L);
	        gapcode.setCode("2");
	        gapcode.setName("New Gapcode");
	        gapcode.setActive(true);
	        gapcode.setOutSideUlb(true);
	        gapcode.setLogic("Average");
	        gapcode.setNoOfMonths("Last 3 months Average");
	        gapcode.setDescription("New Gaocode of Connection");
	        return gapcode;
	    }

	}

