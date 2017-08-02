package org.egov.egf.master.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.BankBranchSearch;
import org.egov.egf.master.persistence.entity.BankBranchEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BankBranchJdbcRepository;
import org.egov.egf.master.web.contract.BankBranchContract;
import org.egov.egf.master.web.requests.BankBranchRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BankBranchRepositoryTest {

    @Mock
    private BankBranchJdbcRepository bankBranchJdbcRepository;

    @Mock
    private MastersQueueRepository bankBranchQueueRepository;

    @InjectMocks
    private BankBranchRepository bankBranchRepository;

    @Test
    public void testFindById() {
        BankBranchEntity bankBranchEntity = getBankBranchEntity();
        BankBranch expectedResult = bankBranchEntity.toDomain();
        when(bankBranchJdbcRepository.findById(any(BankBranchEntity.class))).thenReturn(bankBranchEntity);
        BankBranch actualResult = bankBranchRepository.findById(getBankBranchDomain());
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getCode(), actualResult.getCode());
        assertEquals(expectedResult.getName(), actualResult.getName());
        assertEquals(expectedResult.getDescription(), actualResult.getDescription());
        assertEquals(expectedResult.getActive(), actualResult.getActive());
        assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
    }

    @Test
    public void testAdd() {
        Mockito.doNothing().when(bankBranchQueueRepository).add(Mockito.any());
        BankBranchRequest request = new BankBranchRequest();
        request.setRequestInfo(getRequestInfo());
        request.setBankBranches(new ArrayList<BankBranchContract>());
        request.getBankBranches().add(getBankBranchContract());
        bankBranchRepository.add(request);
        Map<String, Object> message = new HashMap<>();
        message.put("bankbranch_create", request);
        Mockito.verify(bankBranchQueueRepository).add(message);
    }

    @Test
    public void testSave() {
        BankBranchEntity bankBranchEntity = getBankBranchEntity();
        BankBranch expectedResult = bankBranchEntity.toDomain();
        when(bankBranchJdbcRepository.create(any(BankBranchEntity.class))).thenReturn(bankBranchEntity);
        BankBranch actualResult = bankBranchRepository.save(getBankBranchDomain());
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getCode(), actualResult.getCode());
        assertEquals(expectedResult.getName(), actualResult.getName());
        assertEquals(expectedResult.getDescription(), actualResult.getDescription());
        assertEquals(expectedResult.getActive(), actualResult.getActive());
        assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
    }

    @Test
    public void testUpdate() {
        BankBranchEntity bankBranchEntity = getBankBranchEntity();
        BankBranch expectedResult = bankBranchEntity.toDomain();
        when(bankBranchJdbcRepository.update(any(BankBranchEntity.class))).thenReturn(bankBranchEntity);
        BankBranch actualResult = bankBranchRepository.update(getBankBranchDomain());
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getCode(), actualResult.getCode());
        assertEquals(expectedResult.getName(), actualResult.getName());
        assertEquals(expectedResult.getDescription(), actualResult.getDescription());
        assertEquals(expectedResult.getActive(), actualResult.getActive());
        assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
    }

    @Test
    public void testSearch() {
        Pagination<BankBranch> expectedResult = new Pagination<>();
        expectedResult.setPageSize(500);
        expectedResult.setOffset(0);
        when(bankBranchJdbcRepository.search(any(BankBranchSearch.class))).thenReturn(expectedResult);
        Pagination<BankBranch> actualResult = bankBranchRepository.search(getBankBranchSearch());
        assertEquals(expectedResult, actualResult);
    }

    private BankBranchEntity getBankBranchEntity() {
        BankBranchEntity bankBranchEntity = new BankBranchEntity();
        BankBranch bankBranch = getBankBranchDomain();
        bankBranchEntity.setId(bankBranch.getId());
        bankBranchEntity.setCode(bankBranch.getCode());
        bankBranchEntity.setName(bankBranch.getName());
        bankBranchEntity.setDescription(bankBranch.getDescription());
        bankBranchEntity.setActive(bankBranch.getActive());
        bankBranchEntity.setTenantId(bankBranch.getTenantId());
        return bankBranchEntity;
    }

    private BankBranch getBankBranchDomain() {
        BankBranch bankBranch = new BankBranch();
        bankBranch.setId("1");
        bankBranch.setCode("code");
        bankBranch.setName("name");
        bankBranch.setDescription("description");
        bankBranch.setActive(true);
        bankBranch.setTenantId("default");
        return bankBranch;
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

    private BankBranchContract getBankBranchContract() {
        BankBranchContract bankBranchContract = BankBranchContract.builder().id("1")
                .code("code").name("name")
                .description("description").active(true)
                .build();
        bankBranchContract.setTenantId("default");
        return bankBranchContract;
    }

    private BankBranchSearch getBankBranchSearch() {
        BankBranchSearch bankBranchSearch = new BankBranchSearch();
        bankBranchSearch.setPageSize(500);
        bankBranchSearch.setOffset(0);
        bankBranchSearch.setSortBy("name desc");
        return bankBranchSearch;
    }

}