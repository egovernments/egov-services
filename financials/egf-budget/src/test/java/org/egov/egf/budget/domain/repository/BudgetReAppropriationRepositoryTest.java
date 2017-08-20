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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetReAppropriationQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetReAppropriationJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetReAppropriationRepositoryTest {

    @Mock
    private BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;

    private BudgetReAppropriationRepository budgetReAppropriationRepositoryWithKafka;

    private BudgetReAppropriationRepository budgetReAppropriationRepositoryWithOutKafka;

    @Mock
    private BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository;

    @Captor
    private ArgumentCaptor<BudgetReAppropriationRequest> captor;

    private final RequestInfo requestInfo = new RequestInfo();

    @Mock
    private FinancialConfigurationContractRepository financialConfigurationContractRepository;

    @Mock
    private BudgetReAppropriationESRepository budgetReAppropriationESRepository;


    @Before
    public void setup() {
        budgetReAppropriationRepositoryWithKafka = new BudgetReAppropriationRepository(
                budgetReAppropriationJdbcRepository, budgetReAppropriationQueueRepository, "yes",
                financialConfigurationContractRepository,budgetReAppropriationESRepository);

        budgetReAppropriationRepositoryWithOutKafka = new BudgetReAppropriationRepository(
                budgetReAppropriationJdbcRepository, budgetReAppropriationQueueRepository, "no",
                financialConfigurationContractRepository,budgetReAppropriationESRepository);
    }

    @Test
    public void test_find_by_id() {
        final BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
        final BudgetReAppropriation expectedResult = entity.toDomain();

        when(budgetReAppropriationJdbcRepository.findById(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

        final BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
                .findById(getBudgetReAppropriationDomin());

        assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
        assertEquals(expectedResult.getAdditionAmount(), actualResult.getAdditionAmount());
        assertEquals(expectedResult.getDeductionAmount(), actualResult.getDeductionAmount());
        assertEquals(expectedResult.getOriginalAdditionAmount(), actualResult.getOriginalAdditionAmount());
        assertEquals(expectedResult.getOriginalDeductionAmount(), actualResult.getOriginalDeductionAmount());
    }

    @Test
    public void test_find_by_id_return_null() {
        final BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();

        when(budgetReAppropriationJdbcRepository.findById(null)).thenReturn(entity);

        final BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
                .findById(getBudgetReAppropriationDomin());

        assertEquals(null, actualResult);
    }

    @Test
    public void test_save_with_kafka() {

        final List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

        budgetReAppropriationRepositoryWithKafka.save(expectedResult, requestInfo);

        verify(budgetReAppropriationQueueRepository).addToQue(captor.capture());

        final BudgetReAppropriationRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
        assertEquals(expectedResult.get(0).getDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
        assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
        assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

    }

    @Test
    public void test_save_with_out_kafka() {

        final List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

        final BudgetReAppropriationEntity entity = new BudgetReAppropriationEntity().toEntity(expectedResult.get(0));

        when(budgetReAppropriationJdbcRepository.create(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

        budgetReAppropriationRepositoryWithOutKafka.save(expectedResult, requestInfo);

        verify(budgetReAppropriationQueueRepository).addToSearchQue(captor.capture());

        final BudgetReAppropriationRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
        assertEquals(expectedResult.get(0).getDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
        assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
        assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

    }

    @Test
    public void test_update_with_kafka() {

        final List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

        budgetReAppropriationRepositoryWithKafka.update(expectedResult, requestInfo);

        verify(budgetReAppropriationQueueRepository).addToQue(captor.capture());

        final BudgetReAppropriationRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
        assertEquals(expectedResult.get(0).getDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
        assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
        assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

    }
    
    @Test
    public void test_delete_with_kafka() {

        final List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

        budgetReAppropriationRepositoryWithKafka.delete(expectedResult, requestInfo);

        verify(budgetReAppropriationQueueRepository).addToQue(captor.capture());

        final BudgetReAppropriationRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
        assertEquals(expectedResult.get(0).getDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
        assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
        assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

    }

    @Test
    public void test_update_with_out_kafka() {

        final List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

        final BudgetReAppropriationEntity entity = new BudgetReAppropriationEntity().toEntity(expectedResult.get(0));

        when(budgetReAppropriationJdbcRepository.update(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

        budgetReAppropriationRepositoryWithOutKafka.update(expectedResult, requestInfo);

        verify(budgetReAppropriationQueueRepository).addToSearchQue(captor.capture());

        final BudgetReAppropriationRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
        assertEquals(expectedResult.get(0).getDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
        assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
        assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

    }
    
    @Test
    public void test_delete_with_out_kafka() {

        final List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

        final BudgetReAppropriationEntity entity = new BudgetReAppropriationEntity().toEntity(expectedResult.get(0));

        when(budgetReAppropriationJdbcRepository.delete(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

        budgetReAppropriationRepositoryWithOutKafka.delete(expectedResult, requestInfo);

        verify(budgetReAppropriationQueueRepository).addToSearchQue(captor.capture());

        final BudgetReAppropriationRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
        assertEquals(expectedResult.get(0).getAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
        assertEquals(expectedResult.get(0).getDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
        assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
        assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
                actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

    }

    @Test
    public void test_save() {

        final BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
        final BudgetReAppropriation expectedResult = entity.toDomain();

        when(budgetReAppropriationJdbcRepository.create(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

        final BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
                .save(getBudgetReAppropriationDomin());

        assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
        assertEquals(expectedResult.getAdditionAmount(), actualResult.getAdditionAmount());
        assertEquals(expectedResult.getDeductionAmount(), actualResult.getDeductionAmount());
        assertEquals(expectedResult.getOriginalAdditionAmount(), actualResult.getOriginalAdditionAmount());
        assertEquals(expectedResult.getOriginalDeductionAmount(), actualResult.getOriginalDeductionAmount());

    }

    @Test
    public void test_update() {

        final BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
        final BudgetReAppropriation expectedResult = entity.toDomain();

        when(budgetReAppropriationJdbcRepository.update(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

        final BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
                .update(getBudgetReAppropriationDomin());

        assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
        assertEquals(expectedResult.getAdditionAmount(), actualResult.getAdditionAmount());
        assertEquals(expectedResult.getDeductionAmount(), actualResult.getDeductionAmount());
        assertEquals(expectedResult.getOriginalAdditionAmount(), actualResult.getOriginalAdditionAmount());
        assertEquals(expectedResult.getOriginalDeductionAmount(), actualResult.getOriginalDeductionAmount());

    }
    
    @Test
    public void test_delete() {

        final BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
        final BudgetReAppropriation expectedResult = entity.toDomain();

        when(budgetReAppropriationJdbcRepository.delete(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

        final BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
                .delete(getBudgetReAppropriationDomin());

        assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
        assertEquals(expectedResult.getAdditionAmount(), actualResult.getAdditionAmount());
        assertEquals(expectedResult.getDeductionAmount(), actualResult.getDeductionAmount());
        assertEquals(expectedResult.getOriginalAdditionAmount(), actualResult.getOriginalAdditionAmount());
        assertEquals(expectedResult.getOriginalDeductionAmount(), actualResult.getOriginalDeductionAmount());

    }

    @Test
    public void test_search() {

        final Pagination<BudgetReAppropriation> expectedResult = new Pagination<>();
        expectedResult.setPageSize(500);
        expectedResult.setOffset(0);

        when(budgetReAppropriationJdbcRepository.search(any(BudgetReAppropriationSearch.class)))
                .thenReturn(expectedResult);

        final Pagination<BudgetReAppropriation> actualResult = budgetReAppropriationRepositoryWithKafka
                .search(getBudgetReAppropriationSearch());

        assertEquals(expectedResult, actualResult);

    }

    private BudgetReAppropriation getBudgetReAppropriationDomin() {
        final BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();
        budgetReAppropriation.setAnticipatoryAmount(BigDecimal.ONE);
        budgetReAppropriation.setAdditionAmount(BigDecimal.ONE);
        budgetReAppropriation.setDeductionAmount(BigDecimal.ONE);
        budgetReAppropriation.setOriginalAdditionAmount(BigDecimal.ONE);
        budgetReAppropriation.setOriginalDeductionAmount(BigDecimal.valueOf(1500));
        budgetReAppropriation.setTenantId("default");
        return budgetReAppropriation;
    }

    private BudgetReAppropriationEntity getBudgetReAppropriationEntity() {
        final BudgetReAppropriationEntity entity = new BudgetReAppropriationEntity();
        entity.setAnticipatoryAmount(BigDecimal.ONE);
        entity.setAdditionAmount(BigDecimal.ONE);
        entity.setDeductionAmount(BigDecimal.ONE);
        entity.setOriginalAdditionAmount(BigDecimal.ONE);
        entity.setOriginalDeductionAmount(BigDecimal.valueOf(1500));
        entity.setTenantId("default");
        return entity;
    }

    private BudgetReAppropriationSearch getBudgetReAppropriationSearch() {
        final BudgetReAppropriationSearch budgetSearch = new BudgetReAppropriationSearch();
        budgetSearch.setPageSize(500);
        budgetSearch.setOffset(0);
        return budgetSearch;

    }

    private List<BudgetReAppropriation> getBudgetReAppropriations() {

        final List<BudgetReAppropriation> budgetReAppropriations = new ArrayList<BudgetReAppropriation>();

        final BudgetReAppropriation budgetReAppropriation = BudgetReAppropriation.builder()
                .budgetDetail(BudgetDetail.builder().id("1").build()).additionAmount(BigDecimal.TEN)
                .deductionAmount(BigDecimal.TEN).originalAdditionAmount(BigDecimal.TEN)
                .originalDeductionAmount(BigDecimal.TEN).anticipatoryAmount(BigDecimal.TEN).build();

        budgetReAppropriation.setTenantId("default");
        budgetReAppropriations.add(budgetReAppropriation);

        return budgetReAppropriations;
    }

}
