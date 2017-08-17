package org.egov.workflow.web.interceptor;

import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.egov.workflow.web.contract.ResponseInfo;
import org.egov.workflow.web.contract.TaskRequest;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class ActivitiTaskListener implements TaskListener{

	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask arg0) {
		if(arg0.getEventName().equals("complete")){
			arg0.getTaskDefinitionKey();
			arg0.getProcessInstanceId();
		}

		StringBuilder url = new StringBuilder();
		url.append("http://www.google.com");

		final RequestInfo requestInfo = RequestInfo.builder().ts(new Date("")).build();
		//TaskRequest wrapper = TaskRequest.builder().requestInfo(requestInfo).build();
		TaskRequest wrapper = new TaskRequest();
		wrapper.setRequestInfo(requestInfo);
		final HttpEntity<TaskRequest> request = new HttpEntity<>(wrapper);
		new RestTemplate().put(url.toString(), request);

	}

}
