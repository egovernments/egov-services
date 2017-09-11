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
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.TestConfiguration;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetReAppropriationRepository;
import org.egov.egf.master.web.repository.FinancialYearContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class BudgetReAppropriationServiceTest {

    private BudgetReAppropriationService budgetReAppropriationService;

    @Mock
    private SmartValidator validator;

    @Mock
    private BudgetReAppropriationRepository budgetReAppropriationRepository;

    @Mock
    private BudgetDetailRepository budgetDetailRepository;

    @Mock
    private FinancialYearContractRepository financialYearContractRepository;

    private final BindingResult errors = new BeanPropertyBindingResult(null, null);

    private final RequestInfo requestInfo = new RequestInfo();

    @Before
    public void setup() {
        budgetReAppropriationService = new BudgetReAppropriationService(budgetReAppropriationRepository, validator,
                budgetDetailRepository);
    }

    @Test
    public final void test_save_with_out_kafka() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

        when(budgetReAppropriationRepository.uniqueCheck(any(String.class), any(BudgetReAppropriation.class))).thenReturn(true);
        when(budgetReAppropriationRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.create(expextedResult, errors,
                requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=CustomBindException.class)
    public final void test_save_with_out_kafka_uniquefalse() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

        when(budgetReAppropriationRepository.uniqueCheck(any(String.class), any(BudgetReAppropriation.class))).thenReturn(false);
        when(budgetReAppropriationRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.create(expextedResult, errors,
                requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test(expected = InvalidDataException.class)
    public final void test_save_with_out_kafka_and_with_null_req() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

        when(budgetReAppropriationRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.create(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test
    public final void test_update_with_out_kafka() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

        when(budgetReAppropriationRepository.uniqueCheck(any(String.class), any(BudgetReAppropriation.class))).thenReturn(true);
        when(budgetReAppropriationRepository.update(any(List.class), any(RequestInfo.class)))
                .thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.update(expextedResult, errors,
                requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=InvalidDataException.class)
    public final void test_update_with_out_kafka_id_null() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();
        expextedResult.get(0).setId(null);

        when(budgetReAppropriationRepository.uniqueCheck(any(String.class), any(BudgetReAppropriation.class))).thenReturn(true);
        when(budgetReAppropriationRepository.update(any(List.class), any(RequestInfo.class)))
                .thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.update(expextedResult, errors,
                requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=CustomBindException.class)
    public final void test_update_with_out_kafka_uniquefalse() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

        when(budgetReAppropriationRepository.uniqueCheck(any(String.class), any(BudgetReAppropriation.class))).thenReturn(false);
        when(budgetReAppropriationRepository.update(any(List.class), any(RequestInfo.class)))
                .thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.update(expextedResult, errors,
                requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test
    public final void test_delete_with_out_kafka() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

        when(budgetReAppropriationRepository.delete(any(List.class), any(RequestInfo.class)))
                .thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.delete(expextedResult, errors,
                requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=InvalidDataException.class)
    public final void test_delete_with_out_kafka_id_null() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();
        expextedResult.get(0).setId(null);

        when(budgetReAppropriationRepository.delete(any(List.class), any(RequestInfo.class)))
                .thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.delete(expextedResult, errors,
                requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test(expected = InvalidDataException.class)
    public final void test_update_with_out_kafka_and_with_null_req() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

        when(budgetReAppropriationRepository.update(any(List.class), any(RequestInfo.class)))
                .thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.update(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected = InvalidDataException.class)
    public final void test_delete_with_out_kafka_and_with_null_req() {

        final List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

        when(budgetReAppropriationRepository.delete(any(List.class), any(RequestInfo.class)))
                .thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.delete(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test
    public final void test_search() {

        final List<BudgetReAppropriation> budgets = getBudgetReAppropriations();
        final BudgetReAppropriationSearch budgetSearch = new BudgetReAppropriationSearch();
        final Pagination<BudgetReAppropriation> expextedResult = new Pagination<>();

        expextedResult.setPagedData(budgets);

        when(budgetReAppropriationRepository.search(budgetSearch)).thenReturn(expextedResult);

        final Pagination<BudgetReAppropriation> actualResult = budgetReAppropriationService.search(budgetSearch);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_save() {

        final BudgetReAppropriation expextedResult = getBudgetReAppropriations().get(0);

        when(budgetReAppropriationRepository.save(any(BudgetReAppropriation.class))).thenReturn(expextedResult);

        final BudgetReAppropriation actualResult = budgetReAppropriationService.save(expextedResult);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_update() {

        final BudgetReAppropriation expextedResult = getBudgetReAppropriations().get(0);

        when(budgetReAppropriationRepository.update(any(BudgetReAppropriation.class))).thenReturn(expextedResult);

        final BudgetReAppropriation actualResult = budgetReAppropriationService.update(expextedResult);

        assertEquals(expextedResult, actualResult);
    }
    
    @Test
    public final void test_delete() {

        final BudgetReAppropriation expextedResult = getBudgetReAppropriations().get(0);

        when(budgetReAppropriationRepository.delete(any(BudgetReAppropriation.class))).thenReturn(expextedResult);

        final BudgetReAppropriation actualResult = budgetReAppropriationService.delete(expextedResult);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_fetch_budget_detail() {

        final List<BudgetReAppropriation> budgets = getBudgetReAppropriations();

        final BudgetDetail expextedResult = BudgetDetail.builder().id("1").build();
        expextedResult.setTenantId("tenantId");

        budgets.get(0).setBudgetDetail(expextedResult);

        when(budgetDetailRepository.findById(any(BudgetDetail.class))).thenReturn(expextedResult);

        final List<BudgetReAppropriation> actualResult = budgetReAppropriationService.fetchRelated(budgets);

        assertEquals(expextedResult, actualResult.get(0).getBudgetDetail());
    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_null_for_budget_detail() {

        final List<BudgetReAppropriation> budgets = getBudgetReAppropriations();

        final BudgetDetail expextedResult = BudgetDetail.builder().id("1").build();
        expextedResult.setTenantId("tenantId");

        budgets.get(0).setBudgetDetail(expextedResult);

        when(budgetDetailRepository.findById(null)).thenReturn(expextedResult);

        budgetReAppropriationService.fetchRelated(budgets);

    }

    private List<BudgetReAppropriation> getBudgetReAppropriations() {

        final List<BudgetReAppropriation> budgetReAppropriations = new ArrayList<BudgetReAppropriation>();

        final BudgetReAppropriation budgetReAppropriation = BudgetReAppropriation.builder().id("1")
                .budgetDetail(BudgetDetail.builder().id("1").build()).additionAmount(BigDecimal.TEN)
                .deductionAmount(BigDecimal.TEN).originalAdditionAmount(BigDecimal.TEN)
                .originalDeductionAmount(BigDecimal.TEN).anticipatoryAmount(BigDecimal.TEN).build();

        budgetReAppropriation.setTenantId("default");
        budgetReAppropriations.add(budgetReAppropriation);

        return budgetReAppropriations;
    }

}
