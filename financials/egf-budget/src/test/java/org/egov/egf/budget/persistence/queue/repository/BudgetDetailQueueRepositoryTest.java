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
package org.egov.egf.budget.persistence.queue.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetDetailQueueRepositoryTest {

    @Mock
    private BudgetDetailQueueRepository budgetDetailQueueRepository;

    @Mock
    private FinancialProducer financialProducer;

    private static final String TOPIC_NAME = "topic";

    private static final String KEY_NAME = "key";

    @Before
    public void setup() {
        budgetDetailQueueRepository = new BudgetDetailQueueRepository(financialProducer, TOPIC_NAME, KEY_NAME,
                TOPIC_NAME, KEY_NAME);
    }

    @Test
    public void test_add_to_queue_while_create() {

        final BudgetDetailRequest request = new BudgetDetailRequest();

        request.setBudgetDetails(getBudgetDetails());
        request.setRequestInfo(new RequestInfo());
        request.getRequestInfo().setAction("create");

        budgetDetailQueueRepository.addToQue(request);

        final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

        verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

        final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

        assertEquals(request, actualRequest.get("budgetdetail_create"));

    }

    @Test
    public void test_add_to_queue_while_update() {

        final BudgetDetailRequest request = new BudgetDetailRequest();

        request.setBudgetDetails(getBudgetDetails());
        request.setRequestInfo(new RequestInfo());
        request.getRequestInfo().setAction("update");

        budgetDetailQueueRepository.addToQue(request);

        final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

        verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

        final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

        assertEquals(request, actualRequest.get("budgetdetail_update"));

    }

    @Test
    public void test_add_to_search_queue() {

        final BudgetDetailRequest request = new BudgetDetailRequest();

        request.setBudgetDetails(getBudgetDetails());

        budgetDetailQueueRepository.addToSearchQue(request);

        final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

        verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

        final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

        assertEquals(request, actualRequest.get("budgetdetail_persisted"));

    }

    private List<BudgetDetailContract> getBudgetDetails() {

        final List<BudgetDetailContract> budgetDetails = new ArrayList<BudgetDetailContract>();

        final BudgetDetailContract budgetDetail = BudgetDetailContract.builder()
                .budget(BudgetContract.builder().id("1").build())
                .budgetGroup(BudgetGroupContract.builder().id("1").build()).anticipatoryAmount(BigDecimal.TEN)
                .originalAmount(BigDecimal.TEN).approvedAmount(BigDecimal.TEN).budgetAvailable(BigDecimal.TEN)
                .planningPercent(BigDecimal.valueOf(1500)).build();

        budgetDetail.setTenantId("default");
        budgetDetails.add(budgetDetail);

        return budgetDetails;
    }

}
