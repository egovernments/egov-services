package org.egov.egf.master.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.TestConfiguration;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.BankBranchSearch;
import org.egov.egf.master.domain.repository.BankBranchRepository;
import org.egov.egf.master.domain.repository.BankRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class BankBranchServiceTest {

    @InjectMocks
    private BankBranchService bankBranchService;

    @Mock
    private SmartValidator validator;

    @Mock
    private BankBranchRepository bankBranchRepository;

    @Mock
    private BankRepository bankRepository;

    private BindingResult errors = new BeanPropertyBindingResult(null, null);

    private List<BankBranch> bankBranches = new ArrayList<>();

    @Before
    public void setup() {

    }

    @Test
    public final void testFetchRelated() {
        when(bankRepository.findById(any(Bank.class))).thenReturn(getBank());
        bankBranches.add(getBankBranch());
        bankBranchService.fetchRelated(bankBranches);
    }

    @Test
    public final void testAdd() {
        when(bankRepository.findById(any(Bank.class))).thenReturn(getBank());
        bankBranches.add(getBankBranch());
        bankBranchService.add(bankBranches, errors);
    }

    @Test
    public final void testUpdate() {
        when(bankRepository.findById(any(Bank.class))).thenReturn(getBank());
        bankBranches.add(getBankBranch());
        bankBranchService.update(bankBranches, errors);
    }

    @Test
    public final void testSearch() {
        List<BankBranch> search = new ArrayList<>();
        search.add(getBankBranchSearch());
        Pagination<BankBranch> expectedResult = new Pagination<>();
        expectedResult.setPagedData(search);
        when(bankBranchRepository.search(any(BankBranchSearch.class))).thenReturn(expectedResult);
        Pagination<BankBranch> actualResult = bankBranchService.search(getBankBranchSearch());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public final void testSave() {
        BankBranch expectedResult = getBankBranch();
        when(bankBranchRepository.save(any(BankBranch.class))).thenReturn(expectedResult);
        final BankBranch actualResult = bankBranchService.save(getBankBranch());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public final void test_Update() {
        BankBranch expectedResult = getBankBranch();
        when(bankBranchRepository.update(any(BankBranch.class))).thenReturn(expectedResult);
        final BankBranch actualResult = bankBranchService.update(getBankBranch());
        assertEquals(expectedResult, actualResult);
    }

    private Bank getBank() {
        Bank bank = Bank.builder().id("1").code("code").description("description").build();
        bank.setTenantId("default");
        return bank;
    }

    private BankBranch getBankBranch() {
        BankBranch bankBranch = BankBranch.builder().code("code").build();
        bankBranch.setTenantId("default");
        bankBranch.setBank(getBank());
        return bankBranch;
    }

    private BankBranchSearch getBankBranchSearch() {
        BankBranchSearch bankBranchSearch = new BankBranchSearch();
        bankBranchSearch.setPageSize(0);
        bankBranchSearch.setOffset(0);
        bankBranchSearch.setSortBy("Sort");
        return bankBranchSearch;
    }
}