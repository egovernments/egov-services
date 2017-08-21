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
package org.egov.egf.budget.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.TestConfiguration;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.utils.RequestJsonReader;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(BudgetReAppropriationController.class)
@Import(TestConfiguration.class)
public class BudgetReAppropriationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetReAppropriationService budgetReAppropriationService;

    @Captor
    private ArgumentCaptor<BudgetReAppropriationRequest> captor;

    private final RequestJsonReader resources = new RequestJsonReader();

    @Test
    public void test_create() throws IOException, Exception {

        when(budgetReAppropriationService.create(any(List.class), any(BindingResult.class), any(RequestInfo.class)))
                .thenReturn(getBudgetReAppropriations());

        mockMvc.perform(post("/budgetreappropriations/_create")
                .content(resources.readRequest("budgetreappropriation/budgetreappropriation_create_valid_request.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
                        .readResponse("budgetreappropriation/budgetreappropriation_create_valid_response.json")));

    }

    @Test
    public void test_create_error() throws IOException, Exception {

        when(budgetReAppropriationService.create(any(List.class), any(BindingResult.class), any(RequestInfo.class)))
                .thenReturn(getBudgetReAppropriations());

        mockMvc.perform(post("/budgetreappropriations/_create")
                .content(resources
                        .readRequest("budgetreappropriation/budgetreappropriation_create_invalid_field_value.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

    }

    @Test
    public void test_update() throws IOException, Exception {

        final List<BudgetReAppropriation> budgetReAppropriations = getBudgetReAppropriations();
        budgetReAppropriations.get(0).setId("1");

        when(budgetReAppropriationService.update(any(List.class), any(BindingResult.class), any(RequestInfo.class)))
                .thenReturn(budgetReAppropriations);

        mockMvc.perform(post("/budgetreappropriations/_update")
                .content(resources.readRequest("budgetreappropriation/budgetreappropriation_update_valid_request.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
                        .readResponse("budgetreappropriation/budgetreappropriation_update_valid_response.json")));

    }
    
    @Test
    public void test_delete() throws IOException, Exception {

        final List<BudgetReAppropriation> budgetReAppropriations = getBudgetReAppropriations();
        budgetReAppropriations.get(0).setId("1");

        when(budgetReAppropriationService.delete(any(List.class), any(BindingResult.class), any(RequestInfo.class)))
                .thenReturn(budgetReAppropriations);

        mockMvc.perform(post("/budgetreappropriations/_delete")
                .content(resources.readRequest("budgetreappropriation/budgetreappropriation_delete_valid_request.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
                        .readResponse("budgetreappropriation/budgetreappropriation_delete_valid_response.json")));

    }

    @Test
    public void test_update_error() throws IOException, Exception {

        when(budgetReAppropriationService.update(any(List.class), any(BindingResult.class), any(RequestInfo.class)))
                .thenReturn(getBudgetReAppropriations());

        mockMvc.perform(post("/budgetreappropriations/_update")
                .content(resources
                        .readRequest("budgetreappropriation/budgetreappropriation_create_invalid_field_value.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

    }
    
    @Test
    public void test_delete_error() throws IOException, Exception {

        when(budgetReAppropriationService.delete(any(List.class), any(BindingResult.class), any(RequestInfo.class)))
                .thenReturn(getBudgetReAppropriations());

        mockMvc.perform(post("/budgetreappropriations/_delete")
                .content(resources
                        .readRequest("budgetreappropriation/budgetreappropriation_create_invalid_field_value.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

    }

    @Test
    public void test_search() throws IOException, Exception {

        final Pagination<BudgetReAppropriation> page = new Pagination<>();
        page.setTotalPages(1);
        page.setTotalResults(1);
        page.setCurrentPage(0);
        page.setPagedData(getBudgetReAppropriations());
        page.getPagedData().get(0).setId("1");

        when(budgetReAppropriationService.search(any(BudgetReAppropriationSearch.class))).thenReturn(page);

        mockMvc.perform(post("/budgetreappropriations/_search").content(resources.getRequestInfo())
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
                        .readResponse("budgetreappropriation/budgetreappropriation_search_valid_response.json")));

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
