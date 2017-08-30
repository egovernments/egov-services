package org.egov.egf.master.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.TestConfiguration;
import org.egov.egf.master.domain.model.AccountCodePurpose;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankAccount;
import org.egov.egf.master.domain.model.BankAccountSearch;
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.repository.BankAccountRepository;
import org.egov.egf.master.domain.repository.BankBranchRepository;
import org.egov.egf.master.domain.repository.ChartOfAccountRepository;
import org.egov.egf.master.domain.repository.FundRepository;
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
public class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService bankAccountService;

    @Mock
    private SmartValidator validator;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankBranchRepository bankBranchRepository;

    @Mock
    private ChartOfAccountRepository chartOfAccountRepository;

    @Mock
    private FundRepository fundRepository;

    private BindingResult errors = new BeanPropertyBindingResult(null, null);

    private List<BankAccount> bankAccounts = new ArrayList<>();

    @Before
    public void setup() {

    }

    @Test
    public final void testFetchRelated() {
        when(bankBranchRepository.findById(any(BankBranch.class))).thenReturn(getBankBranch());
        when(chartOfAccountRepository.findById(any(ChartOfAccount.class))).thenReturn(getChartOfAccount());
        when(fundRepository.findById(any(Fund.class))).thenReturn(getFund());
        bankAccounts.add(getBankAccount());
        bankAccountService.fetchRelated(bankAccounts);
    }

    @Test
    public final void testAdd() {
        when(bankBranchRepository.findById(any(BankBranch.class))).thenReturn(getBankBranch());
        when(chartOfAccountRepository.findById(any(ChartOfAccount.class))).thenReturn(getChartOfAccount());
        when(fundRepository.findById(any(Fund.class))).thenReturn(getFund());
        when(bankAccountRepository.uniqueCheck(any(String.class), any(BankAccount.class))).thenReturn(true);
        bankAccounts.add(getBankAccount());
        bankAccountService.add(bankAccounts, errors);
    }

    @Test
    public final void testUpdate() {
        when(bankBranchRepository.findById(any(BankBranch.class))).thenReturn(getBankBranch());
        when(chartOfAccountRepository.findById(any(ChartOfAccount.class))).thenReturn(getChartOfAccount());
        when(fundRepository.findById(any(Fund.class))).thenReturn(getFund());
        when(bankAccountRepository.uniqueCheck(any(String.class), any(BankAccount.class))).thenReturn(true);
        bankAccounts.add(getBankAccount());
        bankAccountService.update(bankAccounts, errors);
    }

    @Test
    public final void testSearch() {
        List<BankAccount> search = new ArrayList<>();
        search.add(getBankAccountSearch());
        Pagination<BankAccount> expectedResult = new Pagination<>();
        expectedResult.setPagedData(search);
        when(bankAccountRepository.search(any(BankAccountSearch.class))).thenReturn(expectedResult);
        Pagination<BankAccount> actualResult = bankAccountService.search(getBankAccountSearch(), errors);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public final void testSave() {
        BankAccount expectedResult = getBankAccount();
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(expectedResult);
        final BankAccount actualResult = bankAccountService.save(getBankAccount());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public final void test_Update() {
        BankAccount expectedResult = getBankAccount();
        when(bankAccountRepository.update(any(BankAccount.class))).thenReturn(expectedResult);
        final BankAccount actualResult = bankAccountService.update(getBankAccount());
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

    private ChartOfAccount getChartOfAccount() {
        ChartOfAccount parent = ChartOfAccount.builder().id("parent").build();
        ChartOfAccount chartOfAccount = ChartOfAccount.builder()
                .glcode("glcode").name("name")
                .description("description").isActiveForPosting(true)
                .type('B').classification((long) 123456).functionRequired(true)
                .budgetCheckRequired(true).build();
        chartOfAccount.setAccountCodePurpose(getAccountCodePurpose());
        chartOfAccount.setParentId(parent);
        chartOfAccount.setTenantId("default");
        return chartOfAccount;
    }

    private AccountCodePurpose getAccountCodePurpose() {
        AccountCodePurpose acp = AccountCodePurpose.builder().id("id")
                .name("name").build();
        acp.setTenantId("default");
        return acp;
    }

    private Fund getFund() {
        return Fund.builder().id("1").code("code").build();
    }

    private BankAccount getBankAccount() {
        return BankAccount.builder().id(UUID.randomUUID().toString().replace("-", ""))
                .chartOfAccount(getChartOfAccount()).fund(getFund()).bankBranch(getBankBranch())
                .accountNumber(UUID.randomUUID().toString().replace("-", ""))
                .build();
    }

    private BankAccountSearch getBankAccountSearch() {
        BankAccountSearch bankAccountSearch = new BankAccountSearch();
        bankAccountSearch.setPageSize(0);
        bankAccountSearch.setOffset(0);
        bankAccountSearch.setSortBy("Sort");
        bankAccountSearch.setTenantId("default");
        return bankAccountSearch;
    }
}