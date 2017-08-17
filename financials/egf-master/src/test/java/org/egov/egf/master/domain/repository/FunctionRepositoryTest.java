package org.egov.egf.master.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.domain.service.FinancialConfigurationService;
import org.egov.egf.master.persistence.entity.FunctionEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.requests.FunctionRequest;
import org.egov.egf.master.persistence.repository.FunctionJdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FunctionRepositoryTest {

    @Mock
    private FunctionJdbcRepository functionJdbcRepository;
    @Mock
    private MastersQueueRepository functionQueueRepository;

    @InjectMocks
    private FunctionRepository functionRepository;

    @Mock
    private FinancialConfigurationService financialConfigurationService;
    
    @Test
    public void find_by_id_test() {
        FunctionEntity entity = getFunctionEntity();
        Function expectedResult = entity.toDomain();
        when(functionJdbcRepository.findById(any(FunctionEntity.class))).thenReturn(entity);
        Function actualResult = functionRepository.findById(getFunctionDomin());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void save_test() {

        FunctionEntity entity = getFunctionEntity();
        Function expectedResult = entity.toDomain();
        when(functionJdbcRepository.create(any(FunctionEntity.class))).thenReturn(entity);
        Function actualResult = functionRepository.save(getFunctionDomin());
        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void update_test() {

        FunctionEntity entity = getFunctionEntity();
        Function expectedResult = entity.toDomain();
        when(functionJdbcRepository.update(any(FunctionEntity.class))).thenReturn(entity);
        Function actualResult = functionRepository.update(getFunctionDomin());
        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void add_test() {
        Mockito.doNothing().when(functionQueueRepository).add(Mockito.any());
        FunctionRequest request = new FunctionRequest();
        request.setRequestInfo(getRequestInfo());
        request.setFunctions(new ArrayList<>());
        request.getFunctions().add(getFunctionContract());
        functionRepository.add(request);
        // verify(functionQueueRepository).add(request);

    }

    @Test
    public void test_search() {

        Pagination<Function> expectedResult = new Pagination<>();
        expectedResult.setPageSize(500);
        expectedResult.setOffset(0);
        when(financialConfigurationService.fetchDataFrom()).thenReturn("db");
        when(functionJdbcRepository.search(any(FunctionSearch.class))).thenReturn(expectedResult);
        Pagination<Function> actualResult = functionRepository.search(getFunctionSearch());
        assertEquals(expectedResult, actualResult);

    }

    private FunctionContract getFunctionContract() {
        return FunctionContract.builder().code("001").name("test").active(true).level(1).build();
    }

    private Function getFunctionDomin() {
        Function function = new Function();
        function.setCode("code");
        function.setName("name");
        function.setActive(true);
        function.setLevel(1);
        function.setTenantId("default");
        return function;
    }

    private FunctionEntity getFunctionEntity() {
        FunctionEntity entity = new FunctionEntity();
        Function function = getFunctionDomin();
        entity.setCode(function.getCode());
        entity.setName(function.getName());
        entity.setActive(function.getActive());
        entity.setLevel(function.getLevel());
        entity.setTenantId(function.getTenantId());
        return entity;
    }

    private RequestInfo getRequestInfo() {
        RequestInfo info = new RequestInfo();
        User user = new User();
        user.setId(1l);
        info.setAction("create ");
        info.setDid("did");
        info.setApiId("apiId");
        info.setKey("key");
        info.setMsgId("msgId");
        // info.setRequesterId("requesterId");
        // info.setTenantId("default");
        info.setTs(new Date());
        info.setUserInfo(user);
        info.setAuthToken("null");
        return info;
    }

    private FunctionSearch getFunctionSearch() {
        FunctionSearch functionSearch = new FunctionSearch();
        functionSearch.setPageSize(500);
        functionSearch.setOffset(0);
        functionSearch.setSortBy("name desc");
        return functionSearch;

    }

}
