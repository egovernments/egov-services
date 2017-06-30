package org.egov.workflow.persistence.repository;

import static org.junit.Assert.assertTrue;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.workflow.domain.model.PersistRouter;
import org.egov.workflow.domain.model.PersistRouterReq;
import org.egov.workflow.domain.service.RouterService;
import org.egov.workflow.persistence.repository.builder.RouterQueryBuilder;
import org.egov.workflow.persistence.repository.rowmapper.RouterRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.web.WebAppConfiguration;




@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(RouterRepository.class)
@WebAppConfiguration
public class RouterRepositoryTest {
 
	@InjectMocks
    RouterRepository routerRepository;
	@Mock
    private JdbcTemplate jdbcTemplate;
	@Mock
    private RouterQueryBuilder routerQueryBuilder;

    @Mock
    private RouterRowMapper routerRowMapper;

    @MockBean
    private RouterService routerService;
    // test cases needs to be enhanced
@Test
public void test_Should_Create_Router() {
	
	PersistRouterReq pr = new PersistRouterReq();
	final RequestInfo requestInfo = new RequestInfo();
    final User user = new User();
    user.setId(1l);
    requestInfo.setUserInfo(user);
    pr.setRequestInfo(requestInfo);
    PersistRouter rt = new PersistRouter();
    rt = getRouter();
    pr.setRouterType(rt);	
	assertTrue(pr
                .equals(routerRepository.createRouter(pr,true)));

}
@Test
public void test_Should_Update_Router() {
	
	PersistRouterReq pr = new PersistRouterReq();
	final RequestInfo requestInfo = new RequestInfo();
    final User user = new User();
    user.setId(1l);
    requestInfo.setUserInfo(user);
    pr.setRequestInfo(requestInfo);
    pr.setRouterType(getRouter());	
	assertTrue(pr
                .equals(routerRepository.updateRouter(pr,true)));

}

@Test
public void test_Should_Validate_Router() {
    
	PersistRouterReq pr = new PersistRouterReq();
	final RequestInfo requestInfo = new RequestInfo();
    final User user = new User();
    user.setId(1l);
    requestInfo.setUserInfo(user);
    pr.setRequestInfo(requestInfo);
    PersistRouter rt = new PersistRouter();
    rt = getRouter();
    pr.setRouterType(rt);
	//assertTrue(rt.equals(routerRepository.ValidateRouter(pr)));
}
   
    public PersistRouter getRouter() {
    	PersistRouter rt = new PersistRouter();
		rt.setPosition(10);
		rt.setService(new Long(102));
		rt.setBoundary(5);
		rt.setTenantId("default");
		return rt;
    	
    }

}