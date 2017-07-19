package org.egov.access.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.egov.access.domain.model.Action;
import org.egov.access.web.contract.action.ActionRequest;
import org.egov.access.web.contract.action.Module;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActionRepositoryTest {

	@Autowired
	private ActionRepository actionRepository;

		
	@Test
	@Sql(scripts = { "/sql/clearAction.sql" })
	public void testshouldcreateactions() {

		ActionRequest actionRequest = new ActionRequest();

		actionRequest.setRequestInfo(getRequestInfo());
		actionRequest.setActions(getActions());

		List<Action> actions = actionRepository.createAction(actionRequest);
		assertThat(actions.size()).isEqualTo(2);
	}

	@Test
	@Sql(scripts = { "/sql/clearAction.sql" })
	public void testCreateActionsIfThereisActionsAsEmpty() {

		ActionRequest actionRequest = new ActionRequest();

		List<Action> actionList = new ArrayList<Action>();

		actionRequest.setActions(actionList);

		List<Action> actions = actionRepository.createAction(actionRequest);

		assertThat(actions.size()).isEqualTo(0);
	}

	@Test(expected = NullPointerException.class)
	@Sql(scripts = { "/sql/clearAction.sql" })
	public void testCreateActionsIfThereisNoActions() {

		ActionRequest actionRequest = new ActionRequest();

		List<Action> actions = actionRepository.createAction(actionRequest);

		assertThat(actions).isEqualTo(null);
	}

	@Test
	@Sql(scripts = { "/sql/clearAction.sql", "/sql/insertActionData.sql" })
	public void testShouldUpdateActions() {

		ActionRequest actionRequest = new ActionRequest();

		actionRequest.setRequestInfo(getRequestInfo());

		List<Action> actionList = new ArrayList<Action>();

		Action action = new Action();

		action.setName("Get all ReceivingMode");

		action.setUrl("/test/getAllReceivingMode");
		actionList.add(action);

		actionRequest.setActions(actionList);

		List<Action> actions = actionRepository.updateAction(actionRequest);

		assertThat(actions.size()).isEqualTo(1);
		assertThat(actions.get(0).getUrl().equals("/test/getAllReceivingMode"));
	}

	private List<Action> getActions() {

		List<Action> actionList = new ArrayList<Action>();

		Action action1 = new Action();

		action1.setName("ActionOne");
		action1.setUrl("/actionone");
		action1.setDisplayName("ActionOne");
		action1.setTenantId("default");
		action1.setServiceCode("ACTION");

		Action action2 = new Action();

		action2.setName("test");
		action2.setUrl("/test");
		action2.setDisplayName("TEST");
		action2.setTenantId("default");
		action2.setServiceCode("TEST");

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
