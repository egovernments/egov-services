package org.egov.egf.budget.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.TestConfiguration;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.budget.web.contract.Boundary;
import org.egov.egf.budget.web.contract.Department;
import org.egov.egf.budget.web.contract.DepartmentRes;
import org.egov.egf.budget.web.contract.repository.BoundaryRepository;
import org.egov.egf.budget.web.contract.repository.DepartmentRepository;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.BudgetGroupSearchContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionSearchContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FunctionarySearchContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundSearchContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SchemeSearchContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.master.web.contract.SubSchemeSearchContract;
import org.egov.egf.master.web.repository.BudgetGroupContractRepository;
import org.egov.egf.master.web.repository.FunctionContractRepository;
import org.egov.egf.master.web.repository.FunctionaryContractRepository;
import org.egov.egf.master.web.repository.FundContractRepository;
import org.egov.egf.master.web.repository.SchemeContractRepository;
import org.egov.egf.master.web.repository.SubSchemeContractRepository;
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
public class BudgetDetailServiceTest {

	@InjectMocks
	private BudgetDetailService budgetDetailService;

	@Mock
	private SmartValidator validator;

	@Mock
	private BudgetDetailRepository budgetDetailRepository;

	@Mock
	private SchemeContractRepository schemeContractRepository;

	@Mock
	private FunctionContractRepository functionContractRepository;

	@Mock
	private FunctionaryContractRepository functionaryContractRepository;

	@Mock
	private BudgetGroupContractRepository budgetGroupContractRepository;

	@Mock
	private FundContractRepository fundContractRepository;

	@Mock
	private SubSchemeContractRepository subSchemeContractRepository;

	@Mock
	private BudgetRepository budgetRepository;

	@Mock
	private BoundaryRepository boundaryRepository;

	@Mock
	private DepartmentRepository departmentRepository;

	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	@Test
	public final void test_save_for_create() {

		List<BudgetDetail> expextedResult = getBudgetDetails();

		List<BudgetDetail> actualResult = budgetDetailService.save(expextedResult, errors, "create");

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_save_for_update() {

		List<BudgetDetail> expextedResult = getBudgetDetails();

		List<BudgetDetail> actualResult = budgetDetailService.save(expextedResult, errors, "update");

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_search() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		BudgetDetailSearch budgetDetailSearch = new BudgetDetailSearch();
		Pagination<BudgetDetail> expextedResult = new Pagination<>();

		expextedResult.setPagedData(budgetDetails);

		when(budgetDetailRepository.search(budgetDetailSearch)).thenReturn(expextedResult);

		Pagination<BudgetDetail> actualResult = budgetDetailService.search(budgetDetailSearch);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_save() {

		BudgetDetail expextedResult = getBudgetDetails().get(0);

		when(budgetDetailRepository.save(any(BudgetDetail.class))).thenReturn(expextedResult);

		BudgetDetail actualResult = budgetDetailService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_update() {

		BudgetDetail expextedResult = getBudgetDetails().get(0);

		when(budgetDetailRepository.update(any(BudgetDetail.class))).thenReturn(expextedResult);

		BudgetDetail actualResult = budgetDetailService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_fetch_budget() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();

		Budget expextedResult = Budget.builder().id("1").build();
		expextedResult.setTenantId("tenantId");

		budgetDetails.get(0).setBudget(expextedResult);

		when(budgetRepository.findById(any(Budget.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult, actualResult.get(0).getBudget());
	}

	@Test
	public final void test_fetch_fund() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setFund(new FundContract());
		budgetDetails.get(0).getFund().setId("1");
		budgetDetails.get(0).getFund().setTenantId("tenantId");
		FundContract expextedResult = FundContract.builder().name("MunicipalFund").id("1").build();

		when(fundContractRepository.findById(any(FundSearchContract.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult, actualResult.get(0).getFund());
	}

	@Test
	public final void test_fetch_budget_group() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();

		budgetDetails.get(0).getBudgetGroup().setId("1");
		budgetDetails.get(0).getBudgetGroup().setTenantId("tenantId");
		BudgetGroupContract expextedResult = BudgetGroupContract.builder().name("BudgetGroup").id("1").build();

		when(budgetGroupContractRepository.findById(any(BudgetGroupSearchContract.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult, actualResult.get(0).getBudgetGroup());
	}

	@Test
	public final void test_fetch_using_department() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setUsingDepartment(new Department());
		budgetDetails.get(0).getUsingDepartment().setId("1");
		budgetDetails.get(0).getUsingDepartment().setTenantId("tenantId");
		DepartmentRes expextedResult = new DepartmentRes();
		Department dept = Department.builder().name("Department").id("1").build();
		expextedResult.setDepartment(new ArrayList<Department>());
		expextedResult.getDepartment().add(dept);

		when(departmentRepository.getDepartmentById(any(String.class), any(String.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult.getDepartment().get(0), actualResult.get(0).getUsingDepartment());
	}

	@Test
	public final void test_fetch_executing_department() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setExecutingDepartment(new Department());
		budgetDetails.get(0).getExecutingDepartment().setId("1");
		budgetDetails.get(0).getExecutingDepartment().setTenantId("tenantId");
		DepartmentRes expextedResult = new DepartmentRes();
		Department dept = Department.builder().name("Department").id("1").build();
		expextedResult.setDepartment(new ArrayList<Department>());
		expextedResult.getDepartment().add(dept);

		when(departmentRepository.getDepartmentById(any(String.class), any(String.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult.getDepartment().get(0), actualResult.get(0).getExecutingDepartment());
	}

	@Test
	public final void test_fetch_function() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setFunction(new FunctionContract());
		budgetDetails.get(0).getFunction().setId("1");
		budgetDetails.get(0).getFunction().setTenantId("tenantId");
		FunctionContract expextedResult = FunctionContract.builder().name("Function").id("1").build();

		when(functionContractRepository.findById(any(FunctionSearchContract.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult, actualResult.get(0).getFunction());
	}

	@Test
	public final void test_fetch_scheme() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setScheme(new SchemeContract());
		budgetDetails.get(0).getScheme().setId("1");
		budgetDetails.get(0).getScheme().setTenantId("tenantId");
		SchemeContract expextedResult = SchemeContract.builder().name("Scheme").id("1").build();

		when(schemeContractRepository.findById(any(SchemeSearchContract.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult, actualResult.get(0).getScheme());
	}

	@Test
	public final void test_fetch_sub_scheme() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setSubScheme(new SubSchemeContract());
		budgetDetails.get(0).getSubScheme().setId("1");
		budgetDetails.get(0).getSubScheme().setTenantId("tenantId");
		SubSchemeContract expextedResult = SubSchemeContract.builder().name("SubScheme").id("1").build();

		when(subSchemeContractRepository.findById(any(SubSchemeSearchContract.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult, actualResult.get(0).getSubScheme());
	}

	@Test
	public final void test_fetch_sub_functionary() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setFunctionary(new FunctionaryContract());
		budgetDetails.get(0).getFunctionary().setId("1");
		budgetDetails.get(0).getFunctionary().setTenantId("tenantId");
		FunctionaryContract expextedResult = FunctionaryContract.builder().name("Functionary").id("1").build();

		when(functionaryContractRepository.findById(any(FunctionarySearchContract.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult, actualResult.get(0).getFunctionary());
	}

	@Test
	public final void test_fetch_boundary() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setBoundary(new Boundary());
		budgetDetails.get(0).getBoundary().setId("1");
		budgetDetails.get(0).getBoundary().setTenantId("tenantId");
		Boundary expextedResult = Boundary.builder().name("Boundary").id("1").build();

		when(boundaryRepository.getBoundaryById(any(String.class), any(String.class))).thenReturn(expextedResult);

		List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails);

		assertEquals(expextedResult, actualResult.get(0).getBoundary());
	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_budget_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();

		Budget expextedResult = Budget.builder().id("1").build();
		expextedResult.setTenantId("tenantId");

		budgetDetails.get(0).setBudget(expextedResult);

		when(budgetRepository.findById(new Budget())).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_fund_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setFund(new FundContract());
		budgetDetails.get(0).getFund().setId("1");
		budgetDetails.get(0).getFund().setTenantId("tenantId");
		FundContract expextedResult = FundContract.builder().name("MunicipalFund").id("1").build();

		when(fundContractRepository.findById(null)).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_budget_group_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();

		budgetDetails.get(0).getBudgetGroup().setId("1");
		budgetDetails.get(0).getBudgetGroup().setTenantId("tenantId");
		BudgetGroupContract expextedResult = BudgetGroupContract.builder().name("BudgetGroup").id("1").build();

		when(budgetGroupContractRepository.findById(null)).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_using_department_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setUsingDepartment(new Department());
		budgetDetails.get(0).getUsingDepartment().setId("1");
		budgetDetails.get(0).getUsingDepartment().setTenantId("tenantId");
		DepartmentRes expextedResult = new DepartmentRes();
		Department dept = Department.builder().name("Department").id("1").build();
		expextedResult.setDepartment(new ArrayList<Department>());
		expextedResult.getDepartment().add(dept);

		when(departmentRepository.getDepartmentById("", "")).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_executing_department_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setExecutingDepartment(new Department());
		budgetDetails.get(0).getExecutingDepartment().setId("1");
		budgetDetails.get(0).getExecutingDepartment().setTenantId("tenantId");
		DepartmentRes expextedResult = new DepartmentRes();
		Department dept = Department.builder().name("Department").id("1").build();
		expextedResult.setDepartment(new ArrayList<Department>());
		expextedResult.getDepartment().add(dept);

		when(departmentRepository.getDepartmentById("", "")).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_function_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setFunction(new FunctionContract());
		budgetDetails.get(0).getFunction().setId("1");
		budgetDetails.get(0).getFunction().setTenantId("tenantId");
		FunctionContract expextedResult = FunctionContract.builder().name("Function").id("1").build();

		when(functionContractRepository.findById(null)).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_scheme_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setScheme(new SchemeContract());
		budgetDetails.get(0).getScheme().setId("1");
		budgetDetails.get(0).getScheme().setTenantId("tenantId");
		SchemeContract expextedResult = SchemeContract.builder().name("Scheme").id("1").build();

		when(schemeContractRepository.findById(null)).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_sub_scheme_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setSubScheme(new SubSchemeContract());
		budgetDetails.get(0).getSubScheme().setId("1");
		budgetDetails.get(0).getSubScheme().setTenantId("tenantId");
		SubSchemeContract expextedResult = SubSchemeContract.builder().name("SubScheme").id("1").build();

		when(subSchemeContractRepository.findById(null)).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_sub_functionary_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setFunctionary(new FunctionaryContract());
		budgetDetails.get(0).getFunctionary().setId("1");
		budgetDetails.get(0).getFunctionary().setTenantId("tenantId");
		FunctionaryContract expextedResult = FunctionaryContract.builder().name("Functionary").id("1").build();

		when(functionaryContractRepository.findById(null)).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_boundary_null() {

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setBoundary(new Boundary());
		budgetDetails.get(0).getBoundary().setId("1");
		budgetDetails.get(0).getBoundary().setTenantId("tenantId");
		Boundary expextedResult = Boundary.builder().name("Boundary").id("1").build();

		when(boundaryRepository.getBoundaryById("", "")).thenReturn(expextedResult);

		budgetDetailService.fetchRelated(budgetDetails);

	}

	private List<BudgetDetail> getBudgetDetails() {

		List<BudgetDetail> budgetDetails = new ArrayList<BudgetDetail>();

		BudgetDetail budgetDetail = BudgetDetail.builder().budget(Budget.builder().id("1").build())
				.budgetGroup(BudgetGroupContract.builder().id("1").build()).anticipatoryAmount(BigDecimal.TEN)
				.originalAmount(BigDecimal.TEN).approvedAmount(BigDecimal.TEN).budgetAvailable(BigDecimal.TEN)
				.planningPercent(BigDecimal.valueOf(1500)).build();

		budgetDetail.setTenantId("default");
		budgetDetails.add(budgetDetail);

		return budgetDetails;
	}

}
