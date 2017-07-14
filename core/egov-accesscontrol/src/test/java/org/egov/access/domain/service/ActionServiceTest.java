package org.egov.access.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.access.domain.criteria.ActionSearchCriteria;
import org.egov.access.domain.criteria.ValidateActionCriteria;
import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionValidation;
import org.egov.access.persistence.repository.ActionRepository;
import org.egov.access.persistence.repository.BaseRepository;
import org.egov.access.persistence.repository.querybuilder.ActionFinderQueryBuilder;
import org.egov.access.persistence.repository.querybuilder.ValidateActionQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.ActionRowMapper;
import org.egov.access.persistence.repository.rowmapper.ActionValidationRowMapper;
import org.egov.access.web.contract.action.ActionRequest;
import org.egov.access.web.contract.action.Module;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ActionServiceTest {

	@Mock
	private BaseRepository repository;

	@Mock
	private ActionRepository actionRepository;

	private ActionService actionService;

	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@Mock
	private NamedParameterJdbcTemplate namedParameterJdbcTemplat;

	@Before
	public void before() {

		actionService = new ActionService(repository, actionRepository);
	}

	@Test
	public void testShouldReturnActionsForUserRole() throws Exception {

		ActionSearchCriteria actionSearchCriteria = ActionSearchCriteria.builder().build();

		List<Object> actionsExpected = getActions();
		ActionFinderQueryBuilder queryBuilder = new ActionFinderQueryBuilder(actionSearchCriteria);
		when(repository.run(Mockito.any(ActionFinderQueryBuilder.class), Mockito.any(ActionRowMapper.class)))
				.thenReturn(actionsExpected);

		List<Action> actualActions = actionService.getActions(actionSearchCriteria);
		assertEquals(actionsExpected, actualActions);
	}

	@Test
	public void testValidateQueriesRepositoryToValidateTheCriteria() {
		ActionValidation expectedValidation = ActionValidation.builder().allowed(true).build();
		when(repository.run(Mockito.any(ValidateActionQueryBuilder.class),
				Mockito.any(ActionValidationRowMapper.class))).thenReturn(Arrays.asList(expectedValidation));

		assert (actionService.validate(ValidateActionCriteria.builder().build()).isAllowed());
	}

	private List<Object> getActions() {
		List<Object> actions = new ArrayList<>();
		Action action1 = Action.builder().id(1L).name("Create Complaint").displayName("Create Complaint").createdBy(1L)
				.lastModifiedBy(1L).url("/createcomplaint").build();
		Action action2 = Action.builder().id(2L).name("Update Complaint").displayName("Update Complaint").createdBy(1L)
				.lastModifiedBy(1L).url("/updatecomplaint").build();
		actions.add(action1);
		actions.add(action2);
		return actions;
	}

	@Test
	public void testShouldCreateActions() {

		ActionRequest actionRequest = new ActionRequest();

		actionRequest.setActions(getListOfActions());

		actionRequest.setRequestInfo(getRequestInfo());

		when(actionRepository.createAction(actionRequest)).thenReturn(actionRequest.getActions());

		List<Action> actions = actionService.createAction(actionRequest);

		assertThat(actions).isEqualTo(actionRequest.getActions());

	}

	@Test
	public void testShouldUpdateActions() {

		ActionRequest actionRequest = new ActionRequest();

		actionRequest.setActions(getListOfActions());

		actionRequest.setRequestInfo(getRequestInfo());

		when(actionRepository.updateAction(actionRequest)).thenReturn(actionRequest.getActions());

		List<Action> actions = actionService.updateAction(actionRequest);

		assertThat(actions).isEqualTo(actionRequest.getActions());

	}

	@Test
	public void testShouldGetModules() {

		ActionRequest actionRequest = new ActionRequest();

		actionRequest.setRequestInfo(getRequestInfo());

		org.egov.access.web.contract.action.ActionService servcie = new org.egov.access.web.contract.action.ActionService();

		servcie.setModules(getModuleList());

		when(actionRepository.getAllActionsBasedOnRoles(actionRequest)).thenReturn(servcie);

		List<Module> modules = actionService.getAllActionsBasedOnRoles(actionRequest);

		assertThat(servcie.getModules()).isEqualTo(modules);

	}

	private List<Module> getModuleList() {

		List<Module> moduleList = new ArrayList<Module>();

		List<Action> actionList = new ArrayList<Action>();

		List<Module> subModule = new ArrayList<Module>();

		Module module = new Module();

		module.setId(74l);
		module.setName("ess");
		module.setSubModules(subModule);
		module.setCode("ESS");
		module.setDisplayName("Employee Self Service");
		module.setEnabled(false);

		Action action1 = new Action();

		action1.setId(268l);

		action1.setName("ESS Leave Application");
		action1.setUrl("/app/hr/leavemaster/apply-leave.html");
		action1.setDisplayName("Apply Leave");
		action1.setEnabled(false);
		action1.setServiceCode("ESS");

		Action action2 = new Action();

		action2.setId(267l);
		action2.setName("View ESS Employee");
		action2.setUrl("/app/hr/employee/create.html");
		action2.setDisplayName("My Details");
		action2.setEnabled(false);
		action2.setServiceCode("ESS");

		actionList.add(action1);
		actionList.add(action2);

		module.setActionList(actionList);

		moduleList.add(module);

		return moduleList;
	}

	private List<Action> getListOfActions() {

		List<Action> actionList = new ArrayList<Action>();

		Action action1 = new Action();

		action1.setName("ActionOne");
		action1.setUrl("/actionone");
		action1.setDisplayName("ActionOne");
		action1.setTenantId("default");
		action1.setServiceCode("ACTION");

		Action action2 = new Action();

		action1.setName("test");
		action1.setUrl("/test");
		action1.setDisplayName("TEST");
		action1.setTenantId("default");
		action1.setServiceCode("TEST");

		actionList.add(action1);
		actionList.add(action2);

		return actionList;
	}

	private RequestInfo getRequestInfo() {

		RequestInfo request = new RequestInfo();

		User user = new User();

		user.setId(1);
		request.setUserInfo(user);

		return request;
	}
}
