package org.egov.lcms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Prasad
 *
 */
@Configuration
@SuppressWarnings("unused")
public class PropertiesManager {
	@Autowired
	Environment environment;
	
	private String businessKey;
	private String type;
	private String state;
	private String workflowHostName;
	private String workflowBasePath;
	private String workflowStartPath;
	private String workflowUpdatepath;
	private String comment;
	private String cancel;
	private String specialNoticeAction;
	
	private String createOpinionWorkflow;
	private String updateOpinionWorkflow;
	private String createOpinion;
	private String updateOpinion;
	private String opinionBusinessKey;
	private String opinionType;
	private String opinionState;

	public String getBusinessKey() {
		return environment.getProperty("businessKey");
	}

	public String getType() {
		return environment.getProperty("type");
	}
	public String getComment() {
		return environment.getProperty("create.property.comments");
	}
	public String getState() {
		return environment.getProperty("state");
	}
	public String getWorkflowHostName() {
		return environment.getProperty("egov.services.egov-common-workflows.hostname");
	}

	public String getWorkflowBasepath() {
		return environment.getProperty("egov.services.egov-common-workflows.basepath");
	}

	public String getWorkflowStartpath() {
		return environment.getProperty("egov.services.egov-common-workflows.startpath");
	}
	public String getWorkflowUpdatepath() {
		return environment.getProperty("egov.services.egov-common-workflows.updatepath");
	}
	public String getSpecialNoticeAction() {
		return environment.getProperty("special.notice.action");
	}

	public String getCancel() {
		return environment.getProperty("cancel");
	}
	
	public String getCreateOpinionWorkflow() {
		return environment.getProperty("lcms.opinion.create.workflow");
	}
	
	public String getUpdateOpinionWorkflow() {
		return environment.getProperty("lcms.opinion.update.workflow");
	}
	
	public String getCreateOpinion(){
		return environment.getProperty("lcms.opinion.create");
	}
	
	public String getUpdateOpinion(){
		return environment.getProperty("lcms.opinion.update");
	}
	
	public String getOpinionBusinessKey(){
		return environment.getProperty("opinion.businesskey");
	}
	
	public String getOpinionType() {
		return environment.getProperty("opinion.type");
	}
	public String getOpinionComment() {
		return environment.getProperty("opinion.create.comments");
	}
}
