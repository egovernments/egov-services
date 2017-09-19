package org.egov.egf.bill.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.BillTestConfiguration;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.repository.BillChecklistRepository;
import org.egov.egf.bill.domain.repository.BillRegisterRepository;
import org.egov.egf.bill.domain.repository.BoundaryRepository;
import org.egov.egf.bill.domain.repository.ChecklistRepository;
import org.egov.egf.bill.domain.repository.DepartmentRepository;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.repository.AccountDetailKeyContractRepository;
import org.egov.egf.master.web.repository.AccountDetailTypeContractRepository;
import org.egov.egf.master.web.repository.ChartOfAccountContractRepository;
import org.egov.egf.master.web.repository.FinancialStatusContractRepository;
import org.egov.egf.master.web.repository.FunctionContractRepository;
import org.egov.egf.master.web.repository.FunctionaryContractRepository;
import org.egov.egf.master.web.repository.FundContractRepository;
import org.egov.egf.master.web.repository.FundsourceContractRepository;
import org.egov.egf.master.web.repository.SchemeContractRepository;
import org.egov.egf.master.web.repository.SubSchemeContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(BillTestConfiguration.class)
@RunWith(SpringRunner.class)
public class BillRegisterServiceTest {

	private BillRegisterService billRegisterService;
	
	@Mock
    private BillRegisterRepository billRegisterRepository;
    
	@Mock
	private SmartValidator validator;
    
	@Mock
	private SchemeContractRepository schemeContractRepository;
    
	@Mock
	private BoundaryRepository boundaryRepository;
    
	@Mock
	private FunctionaryContractRepository functionaryContractRepository;
    
	@Mock
	private FunctionContractRepository functionContractRepository;
    
	@Mock
	private ChartOfAccountContractRepository chartOfAccountContractRepository;
    
	@Mock
	private BillChecklistRepository billChecklistRepository;
    
	@Mock
	private ChecklistRepository checklistRepository;
    
	@Mock
	private AccountDetailTypeContractRepository accountDetailTypeContractRepository;
    
	@Mock
	private AccountDetailKeyContractRepository accountDetailKeyContractRepository;
    
	@Mock
	private FundsourceContractRepository fundsourceContractRepository;
    
	@Mock
	private FinancialStatusContractRepository financialStatusContractRepository;
    
	@Mock
	private FundContractRepository fundContractRepository;
    
	@Mock
	private DepartmentRepository departmentRepository;
    
	@Mock
	private SubSchemeContractRepository subSchemeContractRepository;
	
	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();
	
	@Before
	public void setup() {
		billRegisterService = new BillRegisterService(billRegisterRepository, schemeContractRepository,
	    		boundaryRepository, functionaryContractRepository, functionContractRepository,
	    		chartOfAccountContractRepository, billChecklistRepository, checklistRepository,
	    		accountDetailTypeContractRepository, accountDetailKeyContractRepository,
	    		fundsourceContractRepository, financialStatusContractRepository,
	    		fundContractRepository, departmentRepository, subSchemeContractRepository, validator);
	}
	
	@Test
	public final void test_create() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_search() {

		List<BillRegister> billRegisters = getBillRegisters();
		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
		billRegisterSearch.setTenantId("default");
		Pagination<BillRegister> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billRegisters);

		when(billRegisterRepository.search(billRegisterSearch)).thenReturn(expextedResult);

		Pagination<BillRegister> actualResult = billRegisterService.search(billRegisterSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test
	public final void test_save() {

		BillRegister expextedResult = getBillRegisters().get(0);

		when(billRegisterRepository.save(any(BillRegister.class))).thenReturn(expextedResult);

		BillRegister actualResult = billRegisterService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_save_with_null_req() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.create(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update1() {

		BillRegister expextedResult = getBillRegisters().get(0);

		when(billRegisterRepository.update(any(BillRegister.class))).thenReturn(expextedResult);

		BillRegister actualResult = billRegisterService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_req() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	private List<BillRegister> getBillRegisters() {

		List<BillRegister> billRegisters = new ArrayList<BillRegister>();
		
		BillRegister billRegister = BillRegister.builder().id("30").billType("billtype4321").billAmount(new BigDecimal(4321)).build();
		BillPayeeDetail billPayeeDetail = BillPayeeDetail.builder().id("5").build();
		BillDetail billDetail = BillDetail.builder().id("29").orderId(4321).glcode("1").debitAmount(new BigDecimal(10000))
				.creditAmount(new BigDecimal(10000)).chartOfAccount(getChartOfAccountContract()).build();
		Checklist checklist = Checklist.builder().id("1").build();
		checklist.setTenantId("default");
		BillChecklist billChecklist = BillChecklist.builder().id("1").checklist(checklist).build();
		
		List bclList = new ArrayList<>();
		bclList.add(billChecklist);
		
		billDetail.setTenantId("default");
		billPayeeDetail.setTenantId("default");
		
		List bpdList = new ArrayList<>();
		bpdList.add(billPayeeDetail);
		
		billDetail.setBillPayeeDetails(bpdList);
		List bdList = new ArrayList<>();
		bdList.add(billDetail);
		
		billRegister.setBillDetails(bdList);
		billRegister.setCheckLists(bclList);
		billRegister.setTenantId("default");
		billRegisters.add(billRegister);
		return billRegisters;
	}
	
	private ChartOfAccountContract getChartOfAccountContract() {
		ChartOfAccountContract chartOfAccount = ChartOfAccountContract.builder().glcode("1").build();
		chartOfAccount.setTenantId("default");
		return chartOfAccount;
	}
	
	private Checklist getChecklist() {
		Checklist checklist = Checklist.builder().id("1").build();
		checklist.setTenantId("default");
		return checklist;
	}
	
	private AccountDetailTypeContract getAccountDetailTypeContract() {
		AccountDetailTypeContract accountDetailTypeContract = AccountDetailTypeContract.builder().id("1").build();
		accountDetailTypeContract.setTenantId("default");
		return accountDetailTypeContract;
	}
	
	private AccountDetailKeyContract getAccountDetailKeyContract() {
		AccountDetailKeyContract accountDetailKeyContract = AccountDetailKeyContract.builder().id("1").build();
		accountDetailKeyContract.setTenantId("default");
		return accountDetailKeyContract;
	}
	
}
