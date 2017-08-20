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
package org.egov.egf.budget.domain.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.persistence.entity.BudgetDetailEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetDetailQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetDetailJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BudgetDetailRepositoryTest {

    @Mock
    private BudgetDetailJdbcRepository budgetDetailJdbcRepository;

    private BudgetDetailRepository budgetDetailRepositoryWithKafka;

    private BudgetDetailRepository budgetDetailRepositoryWithOutKafka;

    @Mock
    private BudgetDetailQueueRepository budgetDetailQueueRepository;

    @Captor
    private ArgumentCaptor<BudgetDetailRequest> captor;

    private final RequestInfo requestInfo = new RequestInfo();

    @Mock
    private FinancialConfigurationContractRepository financialConfigurationContractRepository;

    @Mock
    private BudgetDetailESRepository budgetDetailESRepository;

    @Before
    public void setup() {
        budgetDetailRepositoryWithKafka = new BudgetDetailRepository(budgetDetailJdbcRepository,
                budgetDetailQueueRepository, "yes",
                financialConfigurationContractRepository,budgetDetailESRepository);

        budgetDetailRepositoryWithOutKafka = new BudgetDetailRepository(budgetDetailJdbcRepository,
                budgetDetailQueueRepository, "no",
                financialConfigurationContractRepository,budgetDetailESRepository);
    }

    @Test
    public void test_find_by_id() {
        final BudgetDetailEntity entity = getBudgetDetailEntity();
        final BudgetDetail expectedResult = entity.toDomain();

        when(budgetDetailJdbcRepository.findById(any(BudgetDetailEntity.class))).thenReturn(entity);

        final BudgetDetail actualResult = budgetDetailRepositoryWithKafka.findById(getBudgetDetailDomin());

        assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
        assertEquals(expectedResult.getApprovedAmount(), actualResult.getApprovedAmount());
        assertEquals(expectedResult.getBudgetAvailable(), actualResult.getBudgetAvailable());
        assertEquals(expectedResult.getOriginalAmount(), actualResult.getOriginalAmount());
        assertEquals(expectedResult.getPlanningPercent(), actualResult.getPlanningPercent());
    }

    @Test
    public void test_find_by_id_return_null() {
        final BudgetDetailEntity entity = getBudgetDetailEntity();

        when(budgetDetailJdbcRepository.findById(null)).thenReturn(entity);

        final BudgetDetail actualResult = budgetDetailRepositoryWithKafka.findById(getBudgetDetailDomin());

        assertEquals(null, actualResult);
    }

    @Test
    public void test_save_with_kafka() {

        final List<BudgetDetail> expectedResult = getBudgetDetails();

        budgetDetailRepositoryWithKafka.save(expectedResult, requestInfo);

        verify(budgetDetailQueueRepository).addToQue(captor.capture());

        final BudgetDetailRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetDetails().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getApprovedAmount(),
                actualRequest.getBudgetDetails().get(0).getApprovedAmount());
        assertEquals(expectedResult.get(0).getBudgetAvailable(),
                actualRequest.getBudgetDetails().get(0).getBudgetAvailable());
        assertEquals(expectedResult.get(0).getOriginalAmount(),
                actualRequest.getBudgetDetails().get(0).getOriginalAmount());
        assertEquals(expectedResult.get(0).getPlanningPercent(),
                actualRequest.getBudgetDetails().get(0).getPlanningPercent());

    }

    @Test
    public void test_save_with_out_kafka() {

        final List<BudgetDetail> expectedResult = getBudgetDetails();

        final BudgetDetailEntity entity = new BudgetDetailEntity().toEntity(expectedResult.get(0));

        when(budgetDetailJdbcRepository.create(any(BudgetDetailEntity.class))).thenReturn(entity);

        budgetDetailRepositoryWithOutKafka.save(expectedResult, requestInfo);

        verify(budgetDetailQueueRepository).addToSearchQue(captor.capture());

        final BudgetDetailRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetDetails().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getApprovedAmount(),
                actualRequest.getBudgetDetails().get(0).getApprovedAmount());
        assertEquals(expectedResult.get(0).getBudgetAvailable(),
                actualRequest.getBudgetDetails().get(0).getBudgetAvailable());
        assertEquals(expectedResult.get(0).getOriginalAmount(),
                actualRequest.getBudgetDetails().get(0).getOriginalAmount());
        assertEquals(expectedResult.get(0).getPlanningPercent(),
                actualRequest.getBudgetDetails().get(0).getPlanningPercent());
    }

    @Test
    public void test_update_with_kafka() {

        final List<BudgetDetail> expectedResult = getBudgetDetails();

        budgetDetailRepositoryWithKafka.update(expectedResult, requestInfo);

        verify(budgetDetailQueueRepository).addToQue(captor.capture());

        final BudgetDetailRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetDetails().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getApprovedAmount(),
                actualRequest.getBudgetDetails().get(0).getApprovedAmount());
        assertEquals(expectedResult.get(0).getBudgetAvailable(),
                actualRequest.getBudgetDetails().get(0).getBudgetAvailable());
        assertEquals(expectedResult.get(0).getOriginalAmount(),
                actualRequest.getBudgetDetails().get(0).getOriginalAmount());
        assertEquals(expectedResult.get(0).getPlanningPercent(),
                actualRequest.getBudgetDetails().get(0).getPlanningPercent());
    }
    
    @Test
    public void test_delete_with_kafka() {

        final List<BudgetDetail> expectedResult = getBudgetDetails();

        budgetDetailRepositoryWithKafka.delete(expectedResult, requestInfo);

        verify(budgetDetailQueueRepository).addToQue(captor.capture());

        final BudgetDetailRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetDetails().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getApprovedAmount(),
                actualRequest.getBudgetDetails().get(0).getApprovedAmount());
        assertEquals(expectedResult.get(0).getBudgetAvailable(),
                actualRequest.getBudgetDetails().get(0).getBudgetAvailable());
        assertEquals(expectedResult.get(0).getOriginalAmount(),
                actualRequest.getBudgetDetails().get(0).getOriginalAmount());
        assertEquals(expectedResult.get(0).getPlanningPercent(),
                actualRequest.getBudgetDetails().get(0).getPlanningPercent());
    }

    @Test
    public void test_update_with_out_kafka() {

        final List<BudgetDetail> expectedResult = getBudgetDetails();

        final BudgetDetailEntity entity = new BudgetDetailEntity().toEntity(expectedResult.get(0));

        when(budgetDetailJdbcRepository.update(any(BudgetDetailEntity.class))).thenReturn(entity);

        budgetDetailRepositoryWithOutKafka.update(expectedResult, requestInfo);

        verify(budgetDetailQueueRepository).addToSearchQue(captor.capture());

        final BudgetDetailRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetDetails().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getApprovedAmount(),
                actualRequest.getBudgetDetails().get(0).getApprovedAmount());
        assertEquals(expectedResult.get(0).getBudgetAvailable(),
                actualRequest.getBudgetDetails().get(0).getBudgetAvailable());
        assertEquals(expectedResult.get(0).getOriginalAmount(),
                actualRequest.getBudgetDetails().get(0).getOriginalAmount());
        assertEquals(expectedResult.get(0).getPlanningPercent(),
                actualRequest.getBudgetDetails().get(0).getPlanningPercent());
    }
    
    @Test
    public void test_delete_with_out_kafka() {

        final List<BudgetDetail> expectedResult = getBudgetDetails();

        final BudgetDetailEntity entity = new BudgetDetailEntity().toEntity(expectedResult.get(0));

        when(budgetDetailJdbcRepository.delete(any(BudgetDetailEntity.class))).thenReturn(entity);

        budgetDetailRepositoryWithOutKafka.delete(expectedResult, requestInfo);

        verify(budgetDetailQueueRepository).addToSearchQue(captor.capture());

        final BudgetDetailRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetDetails().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getApprovedAmount(),
                actualRequest.getBudgetDetails().get(0).getApprovedAmount());
        assertEquals(expectedResult.get(0).getBudgetAvailable(),
                actualRequest.getBudgetDetails().get(0).getBudgetAvailable());
        assertEquals(expectedResult.get(0).getOriginalAmount(),
                actualRequest.getBudgetDetails().get(0).getOriginalAmount());
        assertEquals(expectedResult.get(0).getPlanningPercent(),
                actualRequest.getBudgetDetails().get(0).getPlanningPercent());
    }

    @Test
    public void test_save() {

        final BudgetDetailEntity entity = getBudgetDetailEntity();
        final BudgetDetail expectedResult = entity.toDomain();

        when(budgetDetailJdbcRepository.create(any(BudgetDetailEntity.class))).thenReturn(entity);

        final BudgetDetail actualResult = budgetDetailRepositoryWithKafka.save(getBudgetDetailDomin());

        assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
        assertEquals(expectedResult.getApprovedAmount(), actualResult.getApprovedAmount());
        assertEquals(expectedResult.getBudgetAvailable(), actualResult.getBudgetAvailable());
        assertEquals(expectedResult.getOriginalAmount(), actualResult.getOriginalAmount());
        assertEquals(expectedResult.getPlanningPercent(), actualResult.getPlanningPercent());

    }

    @Test
    public void test_update() {

        final BudgetDetailEntity entity = getBudgetDetailEntity();
        final BudgetDetail expectedResult = entity.toDomain();

        when(budgetDetailJdbcRepository.update(any(BudgetDetailEntity.class))).thenReturn(entity);

        final BudgetDetail actualResult = budgetDetailRepositoryWithKafka.update(getBudgetDetailDomin());

        assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
        assertEquals(expectedResult.getApprovedAmount(), actualResult.getApprovedAmount());
        assertEquals(expectedResult.getBudgetAvailable(), actualResult.getBudgetAvailable());
        assertEquals(expectedResult.getOriginalAmount(), actualResult.getOriginalAmount());
        assertEquals(expectedResult.getPlanningPercent(), actualResult.getPlanningPercent());

    }
    
    @Test
    public void test_delete() {

        final BudgetDetailEntity entity = getBudgetDetailEntity();
        final BudgetDetail expectedResult = entity.toDomain();

        when(budgetDetailJdbcRepository.delete(any(BudgetDetailEntity.class))).thenReturn(entity);

        final BudgetDetail actualResult = budgetDetailRepositoryWithKafka.delete(getBudgetDetailDomin());

        assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
        assertEquals(expectedResult.getApprovedAmount(), actualResult.getApprovedAmount());
        assertEquals(expectedResult.getBudgetAvailable(), actualResult.getBudgetAvailable());
        assertEquals(expectedResult.getOriginalAmount(), actualResult.getOriginalAmount());
        assertEquals(expectedResult.getPlanningPercent(), actualResult.getPlanningPercent());

    }

    @Test
    public void test_search() {

        final Pagination<BudgetDetail> expectedResult = new Pagination<>();
        expectedResult.setPageSize(500);
        expectedResult.setOffset(0);

        when(budgetDetailJdbcRepository.search(any(BudgetDetailSearch.class))).thenReturn(expectedResult);

        final Pagination<BudgetDetail> actualResult = budgetDetailRepositoryWithKafka.search(getBudgetDetailSearch());

        assertEquals(expectedResult, actualResult);

    }

    private BudgetDetail getBudgetDetailDomin() {
        final BudgetDetail budgetDetail = new BudgetDetail();
        budgetDetail.setApprovedAmount(BigDecimal.ONE);
        budgetDetail.setAnticipatoryAmount(BigDecimal.ONE);
        budgetDetail.setBudgetAvailable(BigDecimal.ONE);
        budgetDetail.setOriginalAmount(BigDecimal.ONE);
        budgetDetail.setPlanningPercent(BigDecimal.valueOf(1500));
        budgetDetail.setTenantId("default");
        return budgetDetail;
    }

    private BudgetDetailEntity getBudgetDetailEntity() {
        final BudgetDetailEntity entity = new BudgetDetailEntity();
        entity.setApprovedAmount(BigDecimal.ONE);
        entity.setAnticipatoryAmount(BigDecimal.ONE);
        entity.setBudgetAvailable(BigDecimal.ONE);
        entity.setOriginalAmount(BigDecimal.ONE);
        entity.setPlanningPercent(BigDecimal.valueOf(1500));
        entity.setTenantId("default");
        return entity;
    }

    private BudgetDetailSearch getBudgetDetailSearch() {
        final BudgetDetailSearch budgetSearch = new BudgetDetailSearch();
        budgetSearch.setPageSize(500);
        budgetSearch.setOffset(0);
        return budgetSearch;

    }

    private List<BudgetDetail> getBudgetDetails() {

        final List<BudgetDetail> budgetDetails = new ArrayList<BudgetDetail>();

        final BudgetDetail budgetDetail = BudgetDetail.builder().budget(Budget.builder().id("1").build())
                .budgetGroup(BudgetGroupContract.builder().id("1").build()).anticipatoryAmount(BigDecimal.TEN)
                .originalAmount(BigDecimal.TEN).approvedAmount(BigDecimal.TEN).budgetAvailable(BigDecimal.TEN)
                .planningPercent(BigDecimal.valueOf(1500)).build();

        budgetDetail.setTenantId("default");
        budgetDetails.add(budgetDetail);

        return budgetDetails;
    }
}
