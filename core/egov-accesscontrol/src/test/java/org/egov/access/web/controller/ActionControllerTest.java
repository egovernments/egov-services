package org.egov.access.web.controller;


import org.egov.access.Resources;
import org.egov.access.TestConfiguration;
import org.egov.access.domain.criteria.ActionSearchCriteria;
import org.egov.access.domain.criteria.ValidateActionCriteria;
import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionValidation;
import org.egov.access.domain.service.ActionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ActionController.class)
@Import(TestConfiguration.class)
public class ActionControllerTest {

    @MockBean
    private ActionService actionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShouldGetActionsForUserRoles() throws Exception {
        List<String> roleCodesList = new ArrayList<String>();
        roleCodesList.add("CITIZEN");
        roleCodesList.add("SUPERUSER");
        ActionSearchCriteria actionSearchCriteria = ActionSearchCriteria.builder().roleCodes(roleCodesList).build();

        final List<Action> actions = getActions();
        when(actionService.getActions(actionSearchCriteria)).thenReturn(actions);

        mockMvc.perform(post("/v1/actions/_search")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Resources().getFileContents("actionRequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(new Resources().getFileContents("actionResponse.json")));
    }

    @Test
    public void testActionValidation() throws Exception {
        ActionValidation actionValidation = ActionValidation.builder().allowed(true).build();
        ValidateActionCriteria criteria = ValidateActionCriteria.builder().roleNames(Arrays.asList("Citizen", "Employee")).
                tenantId("ap.public").
                actionUrl("/pgr/_statuses").build();
        when(actionService.validate(criteria)).thenReturn(actionValidation);

        mockMvc.perform(post("/v1/actions/_validate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Resources().getFileContents("validateActionRequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(new Resources().getFileContents("validateActionResponse.json")));
    }

    private List<Action> getActions() {
        List<Action> actions = new ArrayList<Action>();
        Action action1 = Action.builder().id(1L).displayName("Create Complaint").name("Create Complaint").createdBy(1L).lastModifiedBy(1L)
                .url("/createcomplaint").orderNumber(1).enabled(true).build();
        Action action2 = Action.builder().id(2L).displayName("Update Complaint").name("Update Complaint").createdBy(1L).lastModifiedBy(1L)
                .url("/updatecomplaint").orderNumber(2).enabled(false).build();
        actions.add(action1);
        actions.add(action2);
        return actions;
    }
}