package org.egov.workflow.domain.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.workflow.domain.model.PersistRouter;
import org.egov.workflow.domain.model.PersistRouterReq;
import org.egov.workflow.persistence.repository.RouterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class RouterServiceTest {
    
	public static final Logger logger = LoggerFactory.getLogger(RouterServiceTest.class);
	
    @Mock
    private RouterRepository routerRepository;

    @Autowired
    private RouterService routerService;
    
  
    
  //testcase needs to be enhanced
    @Test
    public void test_should_create_or_update_router() {
    	
    	PersistRouterReq prq = new PersistRouterReq();
		PersistRouter pr = new PersistRouter();
		RequestInfo ri = new RequestInfo();
		prq.setRequestInfo(ri);
	    pr.setPosition(100);
	    pr.setService(100l);
	    pr.setBoundary(5);
	    pr.setTenantId("default");
	    if(checkforDuplicate(prq)){
	    	logger.info("Creating the Router Entry");
	    }else {
	    	logger.info("Updating the Router Entry");
	    }
	    
	    
    }
    
    public boolean checkforDuplicate(PersistRouterReq persistRouterReq){
		PersistRouter pr = new PersistRouter();
		pr = routerRepository.ValidateRouter(persistRouterReq);
		
		if (pr != null){
			return false;
		}
			else {
				return true;
			}
		
		
		
	}
}