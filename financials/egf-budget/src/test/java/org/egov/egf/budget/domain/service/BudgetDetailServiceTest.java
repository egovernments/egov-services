/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.budget.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
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
import org.egov.egf.budget.web.repository.BoundaryRepository;
import org.egov.egf.budget.web.repository.DepartmentRepository;
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
import org.mockito.Matchers;
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

    private final BindingResult errors = new BeanPropertyBindingResult(null, null);

    private final RequestInfo requestInfo = new RequestInfo();

    @Test
    public final void test_save_with_out_kafka() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();

        when(budgetDetailRepository.uniqueCheck(any(String.class), any(BudgetDetail.class))).thenReturn(true);
        when(budgetDetailRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.create(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected = CustomBindException.class)
    public final void test_save_with_out_kafkaa() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();

        when(budgetDetailRepository.uniqueCheck(any(String.class), any(BudgetDetail.class))).thenReturn(false);
        when(budgetDetailRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.create(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test(expected = InvalidDataException.class)
    public final void test_save_with_out_kafka_and_with_null_req() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();

        when(budgetDetailRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.create(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test
    public final void test_update_with_out_kafka() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();

        when(budgetDetailRepository.uniqueCheck(any(String.class), any(BudgetDetail.class))).thenReturn(true);        
        when(budgetDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.update(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=InvalidDataException.class)
    public final void test_update_with_out_kafka_null_id() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();
        expextedResult.get(0).setId(null);

        when(budgetDetailRepository.uniqueCheck(any(String.class), any(BudgetDetail.class))).thenReturn(true);        
        when(budgetDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.update(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected = CustomBindException.class)
    public final void test_update_with_out_kafkaa() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();

        when(budgetDetailRepository.uniqueCheck(any(String.class), any(BudgetDetail.class))).thenReturn(false);        
        when(budgetDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.update(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test
    public final void test_deletete_with_out_kafka() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();

        when(budgetDetailRepository.delete(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.delete(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=InvalidDataException.class)
    public final void test_deletete_with_out_kafka_null_id() {
    	
    	final List<BudgetDetail> expextedResult = getBudgetDetails();
    	expextedResult.get(0).setId(null);
    	
    	when(budgetDetailRepository.delete(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
    	
    	final List<BudgetDetail> actualResult = budgetDetailService.delete(expextedResult, errors, requestInfo);
    	
    	assertEquals(expextedResult, actualResult);
    	
    }

    @Test(expected = InvalidDataException.class)
    public final void test_update_with_out_kafka_and_with_null_req() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();

        when(budgetDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.update(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected = InvalidDataException.class)
    public final void test_delete_with_out_kafka_and_with_null_req() {

        final List<BudgetDetail> expextedResult = getBudgetDetails();

        when(budgetDetailRepository.delete(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.delete(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test
    public final void test_search() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        final BudgetDetailSearch budgetDetailSearch = new BudgetDetailSearch();
        final Pagination<BudgetDetail> expextedResult = new Pagination<>();

        expextedResult.setPagedData(budgetDetails);

        when(budgetDetailRepository.search(budgetDetailSearch)).thenReturn(expextedResult);

        final Pagination<BudgetDetail> actualResult = budgetDetailService.search(budgetDetailSearch);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_save() {

        final BudgetDetail expextedResult = getBudgetDetails().get(0);

        when(budgetDetailRepository.save(any(BudgetDetail.class))).thenReturn(expextedResult);

        final BudgetDetail actualResult = budgetDetailService.save(expextedResult);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_update() {

        final BudgetDetail expextedResult = getBudgetDetails().get(0);

        when(budgetDetailRepository.update(any(BudgetDetail.class))).thenReturn(expextedResult);

        final BudgetDetail actualResult = budgetDetailService.update(expextedResult);

        assertEquals(expextedResult, actualResult);
    }
    
    @Test
    public final void test_delete() {

        final BudgetDetail expextedResult = getBudgetDetails().get(0);

        when(budgetDetailRepository.delete(any(BudgetDetail.class))).thenReturn(expextedResult);

        final BudgetDetail actualResult = budgetDetailService.delete(expextedResult);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_fetch_budget() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();

        final Budget expextedResult = Budget.builder().id("1").build();
        expextedResult.setTenantId("tenantId");

        budgetDetails.get(0).setBudget(expextedResult);

        when(budgetRepository.findById(any(Budget.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getBudget());
    }

    @Test
    public final void test_fetch_fund() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setFund(new FundContract());
        budgetDetails.get(0).getFund().setId("1");
        budgetDetails.get(0).getFund().setTenantId("tenantId");
        final FundContract expextedResult = FundContract.builder().name("MunicipalFund").id("1").build();

        when(fundContractRepository.findById(any(FundSearchContract.class), Matchers.anyObject())).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getFund());
    }

    @Test
    public final void test_fetch_budget_group() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();

        budgetDetails.get(0).getBudgetGroup().setId("1");
        budgetDetails.get(0).getBudgetGroup().setTenantId("tenantId");
        final BudgetGroupContract expextedResult = BudgetGroupContract.builder().name("BudgetGroup").id("1").build();

        when(budgetGroupContractRepository.findById(any(BudgetGroupSearchContract.class),Matchers.anyObject())).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getBudgetGroup());
    }

    @Test
    public final void test_fetch_using_department() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setUsingDepartment(new Department());
        budgetDetails.get(0).getUsingDepartment().setId("1");
        budgetDetails.get(0).getUsingDepartment().setTenantId("tenantId");
        final DepartmentRes expextedResult = new DepartmentRes();
        final Department dept = Department.builder().name("Department").id("1").build();
        expextedResult.setDepartment(new ArrayList<Department>());
        expextedResult.getDepartment().add(dept);

        when(departmentRepository.getDepartmentById(any(String.class), any(String.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult.getDepartment().get(0), actualResult.get(0).getUsingDepartment());
    }

    @Test
    public final void test_fetch_executing_department() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setExecutingDepartment(new Department());
        budgetDetails.get(0).getExecutingDepartment().setId("1");
        budgetDetails.get(0).getExecutingDepartment().setTenantId("tenantId");
        final DepartmentRes expextedResult = new DepartmentRes();
        final Department dept = Department.builder().name("Department").id("1").build();
        expextedResult.setDepartment(new ArrayList<Department>());
        expextedResult.getDepartment().add(dept);

        when(departmentRepository.getDepartmentById(any(String.class), any(String.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult.getDepartment().get(0), actualResult.get(0).getExecutingDepartment());
    }

    @Test
    public final void test_fetch_function() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setFunction(new FunctionContract());
        budgetDetails.get(0).getFunction().setId("1");
        budgetDetails.get(0).getFunction().setTenantId("tenantId");
        final FunctionContract expextedResult = FunctionContract.builder().name("Function").id("1").build();

        when(functionContractRepository.findById(any(FunctionSearchContract.class),Matchers.anyObject())).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getFunction());
    }

    @Test
    public final void test_fetch_scheme() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setScheme(new SchemeContract());
        budgetDetails.get(0).getScheme().setId("1");
        budgetDetails.get(0).getScheme().setTenantId("tenantId");
        final SchemeContract expextedResult = SchemeContract.builder().name("Scheme").id("1").build();

        when(schemeContractRepository.findById(any(SchemeSearchContract.class),Matchers.anyObject())).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getScheme());
    }

    @Test
    public final void test_fetch_sub_scheme() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setSubScheme(new SubSchemeContract());
        budgetDetails.get(0).getSubScheme().setId("1");
        budgetDetails.get(0).getSubScheme().setTenantId("tenantId");
        final SubSchemeContract expextedResult = SubSchemeContract.builder().name("SubScheme").id("1").build();

        when(subSchemeContractRepository.findById(any(SubSchemeSearchContract.class),Matchers.anyObject())).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getSubScheme());
    }

    @Test
    public final void test_fetch_sub_functionary() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setFunctionary(new FunctionaryContract());
        budgetDetails.get(0).getFunctionary().setId("1");
        budgetDetails.get(0).getFunctionary().setTenantId("tenantId");
        final FunctionaryContract expextedResult = FunctionaryContract.builder().name("Functionary").id("1").build();

        when(functionaryContractRepository.findById(any(FunctionarySearchContract.class),Matchers.anyObject())).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getFunctionary());
    }

    @Test
    public final void test_fetch_boundary() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setBoundary(new Boundary());
        budgetDetails.get(0).getBoundary().setId("1");
        budgetDetails.get(0).getBoundary().setTenantId("tenantId");
        final Boundary expextedResult = Boundary.builder().name("Boundary").id("1").build();

        when(boundaryRepository.getBoundaryById(any(String.class), any(String.class))).thenReturn(expextedResult);

        final List<BudgetDetail> actualResult = budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getBoundary());
    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_budget_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();

        final Budget expextedResult = Budget.builder().id("1").build();
        expextedResult.setTenantId("tenantId");

        budgetDetails.get(0).setBudget(expextedResult);

        when(budgetRepository.findById(new Budget())).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_fund_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setFund(new FundContract());
        budgetDetails.get(0).getFund().setId("1");
        budgetDetails.get(0).getFund().setTenantId("tenantId");
        final FundContract expextedResult = FundContract.builder().name("MunicipalFund").id("1").build();

        when(fundContractRepository.findById(null,new RequestInfo())).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_budget_group_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();

        budgetDetails.get(0).getBudgetGroup().setId("1");
        budgetDetails.get(0).getBudgetGroup().setTenantId("tenantId");
        final BudgetGroupContract expextedResult = BudgetGroupContract.builder().name("BudgetGroup").id("1").build();

        when(budgetGroupContractRepository.findById(null,new RequestInfo())).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_using_department_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setUsingDepartment(new Department());
        budgetDetails.get(0).getUsingDepartment().setId("1");
        budgetDetails.get(0).getUsingDepartment().setTenantId("tenantId");
        final DepartmentRes expextedResult = new DepartmentRes();
        final Department dept = Department.builder().name("Department").id("1").build();
        expextedResult.setDepartment(new ArrayList<Department>());
        expextedResult.getDepartment().add(dept);

        when(departmentRepository.getDepartmentById("", "")).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_executing_department_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setExecutingDepartment(new Department());
        budgetDetails.get(0).getExecutingDepartment().setId("1");
        budgetDetails.get(0).getExecutingDepartment().setTenantId("tenantId");
        final DepartmentRes expextedResult = new DepartmentRes();
        final Department dept = Department.builder().name("Department").id("1").build();
        expextedResult.setDepartment(new ArrayList<Department>());
        expextedResult.getDepartment().add(dept);

        when(departmentRepository.getDepartmentById("", "")).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_function_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setFunction(new FunctionContract());
        budgetDetails.get(0).getFunction().setId("1");
        budgetDetails.get(0).getFunction().setTenantId("tenantId");
        final FunctionContract expextedResult = FunctionContract.builder().name("Function").id("1").build();

        when(functionContractRepository.findById(null,new RequestInfo())).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_scheme_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setScheme(new SchemeContract());
        budgetDetails.get(0).getScheme().setId("1");
        budgetDetails.get(0).getScheme().setTenantId("tenantId");
        final SchemeContract expextedResult = SchemeContract.builder().name("Scheme").id("1").build();

        when(schemeContractRepository.findById(null,new RequestInfo())).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_sub_scheme_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setSubScheme(new SubSchemeContract());
        budgetDetails.get(0).getSubScheme().setId("1");
        budgetDetails.get(0).getSubScheme().setTenantId("tenantId");
        final SubSchemeContract expextedResult = SubSchemeContract.builder().name("SubScheme").id("1").build();

        when(subSchemeContractRepository.findById(null,new RequestInfo())).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_sub_functionary_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setFunctionary(new FunctionaryContract());
        budgetDetails.get(0).getFunctionary().setId("1");
        budgetDetails.get(0).getFunctionary().setTenantId("tenantId");
        final FunctionaryContract expextedResult = FunctionaryContract.builder().name("Functionary").id("1").build();

        when(functionaryContractRepository.findById(null,new RequestInfo())).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_boundary_null() {

        final List<BudgetDetail> budgetDetails = getBudgetDetails();
        budgetDetails.get(0).setBoundary(new Boundary());
        budgetDetails.get(0).getBoundary().setId("1");
        budgetDetails.get(0).getBoundary().setTenantId("tenantId");
        final Boundary expextedResult = Boundary.builder().name("Boundary").id("1").build();

        when(boundaryRepository.getBoundaryById("", "")).thenReturn(expextedResult);

        budgetDetailService.fetchRelated(budgetDetails,new RequestInfo());

    }

    private List<BudgetDetail> getBudgetDetails() {

        final List<BudgetDetail> budgetDetails = new ArrayList<BudgetDetail>();

        final BudgetDetail budgetDetail = BudgetDetail.builder().id("1").budget(Budget.builder().id("1").build())
                .budgetGroup(BudgetGroupContract.builder().id("1").build()).anticipatoryAmount(BigDecimal.TEN)
                .originalAmount(BigDecimal.TEN).approvedAmount(BigDecimal.TEN).budgetAvailable(BigDecimal.TEN)
                .planningPercent(BigDecimal.valueOf(1500)).build();

        budgetDetail.setTenantId("default");
        budgetDetails.add(budgetDetail);

        return budgetDetails;
    }

}
