package org.egov.egf.master.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.egov.egf.master.domain.service.FinancialConfigurationService;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankSearch;
import org.egov.egf.master.persistence.entity.BankEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BankJdbcRepository;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.requests.BankRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BankRepositoryTest {

    @Mock
    private BankJdbcRepository bankJdbcRepository;

    @Mock
    private MastersQueueRepository bankQueueRepository;

    @InjectMocks
    private BankRepository bankRepository;
    
    @Mock
    private FinancialConfigurationService financialConfigurationService;

    @Test
    public void testFindById() {
        BankEntity bankEntity = getBankEntity();
        Bank expectedResult = bankEntity.toDomain();
        when(bankJdbcRepository.findById(any(BankEntity.class))).thenReturn(bankEntity);
        Bank actualResult = bankRepository.findById(getBankDomain());
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getCode(), actualResult.getCode());
        assertEquals(expectedResult.getName(), actualResult.getName());
        assertEquals(expectedResult.getDescription(), actualResult.getDescription());
        assertEquals(expectedResult.getType(), actualResult.getType());
        assertEquals(expectedResult.getActive(), actualResult.getActive());
        assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
    }

    @Test
    public void testAdd() {
        Mockito.doNothing().when(bankQueueRepository).add(Mockito.any());
        BankRequest request = new BankRequest();
        request.setRequestInfo(getRequestInfo());
        request.setBanks(new ArrayList<BankContract>());
        request.getBanks().add(getBankContract());
        bankRepository.add(request);
        Map<String, Object> message = new HashMap<>();
        message.put("bank_create", request);
        Mockito.verify(bankQueueRepository).add(message);
    }

    @Test
    public void testSave() {
        BankEntity bankEntity = getBankEntity();
        Bank expectedResult = bankEntity.toDomain();
        when(bankJdbcRepository.create(any(BankEntity.class))).thenReturn(bankEntity);
        Bank actualResult = bankRepository.save(getBankDomain());
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getCode(), actualResult.getCode());
        assertEquals(expectedResult.getName(), actualResult.getName());
        assertEquals(expectedResult.getDescription(), actualResult.getDescription());
        assertEquals(expectedResult.getType(), actualResult.getType());
        assertEquals(expectedResult.getActive(), actualResult.getActive());
        assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
    }

    @Test
    public void testUpdate() {
        BankEntity bankEntity = getBankEntity();
        Bank expectedResult = bankEntity.toDomain();
        when(bankJdbcRepository.update(any(BankEntity.class))).thenReturn(bankEntity);
        Bank actualResult = bankRepository.update(getBankDomain());
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getCode(), actualResult.getCode());
        assertEquals(expectedResult.getName(), actualResult.getName());
        assertEquals(expectedResult.getDescription(), actualResult.getDescription());
        assertEquals(expectedResult.getType(), actualResult.getType());
        assertEquals(expectedResult.getActive(), actualResult.getActive());
        assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
    }
    
    @Test
    public void testSearch() {
        Pagination<Bank> expectedResult = new Pagination<>();
        expectedResult.setPageSize(500);
        expectedResult.setOffset(0);
        when(financialConfigurationService.fetchDataFrom()).thenReturn("db");
        when(bankJdbcRepository.search(any(BankSearch.class))).thenReturn(expectedResult);
        Pagination<Bank> actualResult = bankRepository.search(getBankSearch());
        assertEquals(expectedResult, actualResult);
    }

    private BankEntity getBankEntity() {
        BankEntity bankEntity = new BankEntity();
        Bank bank = getBankDomain();
        bankEntity.setId(bank.getId());
        bankEntity.setCode(bank.getCode());
        bankEntity.setName(bank.getName());
        bankEntity.setDescription(bank.getDescription());
        bankEntity.setType(bank.getType());
        bankEntity.setActive(bank.getActive());
        bankEntity.setTenantId(bank.getTenantId());
        return bankEntity;
    }

    private Bank getBankDomain() {
        Bank bank = new Bank();
        bank.setId("1");
        bank.setCode("code");
        bank.setName("name");
        bank.setDescription("description");
        bank.setType("type");
        bank.setActive(true);
        bank.setTenantId("default");
        return bank;
    }

    private RequestInfo getRequestInfo() {
        RequestInfo info = new RequestInfo();
        User user = new User();
        user.setId(1l);
        info.setAction(Constants.ACTION_CREATE);
        info.setDid("did");
        info.setApiId("apiId");
        info.setKey("key");
        info.setMsgId("msgId");
        info.setTs(new Date());
        info.setUserInfo(user);
        info.setAuthToken("null");
        return info;
    }

    private BankContract getBankContract() {

        BankContract bankContract = BankContract.builder().id("1")
                .code("code").name("name")
                .description("description").active(true)
                .type("type").build();
        bankContract.setTenantId("default");
        return bankContract;
    }

    private BankSearch getBankSearch() {
        BankSearch bankSearch = new BankSearch();
        bankSearch.setPageSize(500);
        bankSearch.setOffset(0);
        bankSearch.setSortBy("name desc");
        return bankSearch;
    }
}