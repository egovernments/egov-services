package org.egov.wcms.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.repository.builder.GapcodeQueryBuilder;
import org.egov.wcms.repository.rowmapper.GapcodeRowMapper;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(GapcodeRepository.class)
public class GapcodeRepositoryTest {
	
	    @Mock
	    private JdbcTemplate jdbcTemplate;

	    @Mock
	    private GapcodeQueryBuilder gapcodeQueryBuilder;

	    @Mock
	    private GapcodeRowMapper gapcodeRowMapper;

	    @InjectMocks
	    private GapcodeRepository gapcodeRepository;
	    
	    @Mock
	    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	    @Test
	    public void test_Should_Create_Gapcode() {
	        final GapcodeRequest gapcodeRequest = new GapcodeRequest();
	        final RequestInfo requestInfo = new RequestInfo();
	        final User user = new User();
	        user.setId(1l);
	        requestInfo.setUserInfo(user);
	        gapcodeRequest.setRequestInfo(requestInfo);
	        final List<Gapcode> gapcodeList = new ArrayList<>();
	        final Gapcode gapcode = getCategory();
	        gapcodeList.add(gapcode);
	        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
	        assertTrue(gapcodeRequest.equals(gapcodeRepository.persist(gapcodeRequest)));
	    }

	   
	    @Test
	    public void test_Should_Update_Gapcode() {
	        final GapcodeRequest gapcodeRequest = new GapcodeRequest();
	        final RequestInfo requestInfo = new RequestInfo();
	        final User user = new User();
	        user.setId(1l);
	        requestInfo.setUserInfo(user);
	        gapcodeRequest.setRequestInfo(requestInfo);
	        final List<Gapcode> gapcodeList = new ArrayList<>();
	        final Gapcode gapcode = getCategory();
	        gapcodeList.add(gapcode);
	        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
	        assertTrue(gapcodeRequest.equals(gapcodeRepository.persistUpdate(gapcodeRequest)));
	    }

	   
	   
	    @Test
	    public void test_Should_Find_CategoryType_Valid() {
	        final List<Object> preparedStatementValues = new ArrayList<>();
	        final GapcodeGetRequest gapcodeGetRequest = Mockito.mock(GapcodeGetRequest.class);
	        final String queryString = "MyQuery";
	        when(gapcodeQueryBuilder.getQuery(gapcodeGetRequest, preparedStatementValues)).thenReturn(queryString);
	        final List<Gapcode> connectionCategories = new ArrayList<>();
	        when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), gapcodeRowMapper))
	                .thenReturn(connectionCategories);

	        assertTrue(
	                connectionCategories.equals(gapcodeRepository.findForCriteria(gapcodeGetRequest)));
	    }

	    private Gapcode getCategory() {
	        final Gapcode gapcode = new Gapcode();
	        gapcode.setCode("23");
	        gapcode.setName("New Gapcode");
	        gapcode.setActive(true);
	        gapcode.setOutSideUlb(true);
	        gapcode.setLogic("Average");
	        gapcode.setNoOfMonths("Last 3 months Average");
	        gapcode.setDescription("New Gaocode of Connection");
	        return gapcode;
	    }
	}

