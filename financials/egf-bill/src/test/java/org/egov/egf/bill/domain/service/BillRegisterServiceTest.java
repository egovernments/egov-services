package org.egov.egf.bill.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.BillTestConfiguration;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
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
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.Department;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
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
import org.junit.Ignore;
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
	    		chartOfAccountContractRepository, checklistRepository,
	    		accountDetailTypeContractRepository, accountDetailKeyContractRepository,
	    		fundsourceContractRepository, financialStatusContractRepository,
	    		fundContractRepository, departmentRepository, subSchemeContractRepository, validator);
	}
	
	@Ignore
	@Test
	public final void test_create() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	@Ignore
	@Test
	public final void test_create_null_tenant() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	@Ignore
	@Test(expected = CustomBindException.class)
	public final void test_create_unique_id_false() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(false);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.update(expextedResult, errors, requestInfo);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_ie() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_unique_id_false() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(false);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update1() {

		BillRegister expextedResult = getBillRegisters().get(0);

		when(billRegisterRepository.update(any(BillRegister.class))).thenReturn(expextedResult);

		BillRegister actualResult = billRegisterService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Ignore
	@Test(expected = InvalidDataException.class)
	public final void test_update_null_id() {

		BillRegister expextedResult = getBillRegisters().get(0);
		expextedResult.setId(null);

		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(false);
		when(billRegisterRepository.update(any(BillRegister.class))).thenReturn(expextedResult);

		BillRegister actualResult = billRegisterService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_null_id1() {

		List<BillRegister> expextedResult = getBillRegisters();
		expextedResult.get(0).setId(null);

		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_req() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_id() {

		List<BillRegister> expextedResult = getBillRegisters();
		expextedResult.get(0).setId(null);

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

		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(billRegisterRepository.search(billRegisterSearch)).thenReturn(expextedResult);

		Pagination<BillRegister> actualResult = billRegisterService.search(billRegisterSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_search_null_req() {
		List<BillRegister> billRegisters = getBillRegisters();
		Pagination<BillRegister> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billRegisters);

		when(billRegisterRepository.search(any(BillRegisterSearch.class))).thenReturn(expextedResult);

		Pagination<BillRegister> actualResult = billRegisterService.search(null, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = CustomBindException.class)
	public final void test_search_unique_id_false() {

		List<BillRegister> billRegisters = getBillRegisters();
		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
		billRegisterSearch.setTenantId("default");
		Pagination<BillRegister> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billRegisters);

		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(false);
		when(billRegisterRepository.search(billRegisterSearch)).thenReturn(expextedResult);

		Pagination<BillRegister> actualResult = billRegisterService.search(billRegisterSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_search_null_tenant() {

		List<BillRegister> billRegisters = getBillRegisters();
		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
		Pagination<BillRegister> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billRegisters);

		when(billRegisterRepository.search(any(BillRegisterSearch.class))).thenReturn(expextedResult);

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
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_fund() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(null);
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(null);
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_function() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(null);
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_fundsource() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(null);
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_scheme() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(null);
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_subscheme() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(null);
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_functionary() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(null);
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_department() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(null);
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_division() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(getChartOfAccountContract());
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(null);
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_chartofaccount() {

		List<BillRegister> expextedResult = getBillRegisters();
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(chartOfAccountContractRepository.findByGlcode(any(ChartOfAccountContract.class), any(RequestInfo.class))).thenReturn(null);
		when(accountDetailTypeContractRepository.findById(any(AccountDetailTypeContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailTypeContract());
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(getAccountDetailKeyContract());
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(getFunctionContract());
		when(fundContractRepository.findById(any(FundContract.class), any(RequestInfo.class))).thenReturn(getFundContract1());
		when(fundsourceContractRepository.findById(any(FundsourceContract.class), any(RequestInfo.class))).thenReturn(getFundsourceContract());
		when(schemeContractRepository.findById(any(SchemeContract.class), any(RequestInfo.class))).thenReturn(getSchemeContract());
		when(subSchemeContractRepository.findById(any(SubSchemeContract.class), any(RequestInfo.class))).thenReturn(getSubSchemeContract());
		when(functionaryContractRepository.findById(any(FunctionaryContract.class), any(RequestInfo.class))).thenReturn(getFunctionaryContract());
		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class), any(RequestInfo.class))).thenReturn(getFinancialStatusContract());
		when(boundaryRepository.findById(any(Boundary.class))).thenReturn(getDivision());
		when(departmentRepository.findById(any(Department.class))).thenReturn(getDepartment());
		when(checklistRepository.findById(any(Checklist.class))).thenReturn(getChecklist());
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_create_null_accountdetailkey() {

		List<BillRegister> expextedResult = getBillRegisters();
		expextedResult.get(0).getBillDetails().get(0).getBillPayeeDetails().get(0).setAccountDetailKey(null);
		when(billRegisterRepository.uniqueCheck(any(String.class), any(BillRegister.class))).thenReturn(true);
		when(accountDetailKeyContractRepository.findById(any(AccountDetailKeyContract.class), any(RequestInfo.class))).thenReturn(null);
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_fetch_forbilldetail_function_null() {
		List<BillRegister> expextedResult = getBillRegisters();
		expextedResult.get(0).getBillDetails().get(0).getFunction().setId(null);
		when(functionContractRepository.findById(any(FunctionContract.class), any(RequestInfo.class))).thenReturn(null);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

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
		
		List<BillChecklist> bclList = new ArrayList<>();
		bclList.add(billChecklist);
		
		billDetail.setFunction(getFunctionContract());
		billDetail.setTenantId("default");
		billPayeeDetail.setAccountDetailType(getAccountDetailTypeContract());
		billPayeeDetail.setAccountDetailKey(getAccountDetailKeyContract());
		billPayeeDetail.setTenantId("default");
		
		List<BillPayeeDetail> bpdList = new ArrayList<>();
		bpdList.add(billPayeeDetail);
		
		billDetail.setBillPayeeDetails(bpdList);
		List<BillDetail> bdList = new ArrayList<>();
		bdList.add(billDetail);
		
		billRegister.setBillDetails(bdList);
		billRegister.setCheckLists(bclList);
		billRegister.setFunction(getFunctionContract());
		billRegister.setFunctionary(getFunctionaryContract());
		billRegister.setFund(getFundContract());
		billRegister.setFundsource(getFundsourceContract());
		billRegister.setScheme(getSchemeContract());
		billRegister.setSubScheme(getSubSchemeContract());
		billRegister.setStatus(getFinancialStatusContract());
		billRegister.setDepartment(getDepartment());
		billRegister.setDivision(getDivision());
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
	
	private FunctionContract getFunctionContract() {
		FunctionContract function = FunctionContract.builder().id("1").build();
		function.setTenantId("Default");
		return function;
	}
	
	private FundContract getFundContract() {
		FundContract fund = FundContract.builder().id("1").build();
		fund.setTenantId("Default");
		return fund;
	}
	
	private FundContract getFundContract1() {
		FundContract fund = FundContract.builder().id(null).build();
		return fund;
	}
	
	private FundsourceContract getFundsourceContract() {
		FundsourceContract fundsource = FundsourceContract.builder().id("1").build();
		fundsource.setTenantId("Default");
		return fundsource;
	}
	
	private SchemeContract getSchemeContract() {
		SchemeContract scheme = SchemeContract.builder().id("1").build();
		scheme.setTenantId("Default");
		return scheme;
	}
	
	private SubSchemeContract getSubSchemeContract() {
		SubSchemeContract subScheme = SubSchemeContract.builder().id("1").build();
		subScheme.setTenantId("Default");
		return subScheme;
	}
	
	private FunctionaryContract getFunctionaryContract() {
		FunctionaryContract functionary = FunctionaryContract.builder().id("1").build();
		functionary.setTenantId("Default");
		return functionary;
	}
	
	private FinancialStatusContract getFinancialStatusContract() {
		FinancialStatusContract financialStatus = FinancialStatusContract.builder().id("1").build();
		financialStatus.setTenantId("Default");
		return financialStatus;
	}
	
	private Boundary getDivision() {
		Boundary boundary = Boundary.builder().id("1").build();
		boundary.setTenantId("Default");
		return boundary;
	}
	
	private Department getDepartment() {
		Department department = Department.builder().id("1").build();
		department.setTenantId("Default");
		return department;
	}
	
}
