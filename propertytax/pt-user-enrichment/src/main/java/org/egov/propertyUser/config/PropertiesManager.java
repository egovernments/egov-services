package org.egov.propertyUser.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author Yosadhara
 *
 */
@Configuration
@ToString
@NoArgsConstructor
@SuppressWarnings("unused")
@Service
public class PropertiesManager {

	@Autowired
	Environment environment;

	private String createPropertyValidator;

	private String updatePropertyValidator;

	private String createPropertyUserValidator;

	private String updatePropertyUserValidator;

	private String createPropertyTaxCalculated;

	private String bootstrapServer;

	private String autoOffsetReset;

	private String userHostName;

	private String userBasepath;

	private String userSearchpath;

	private String userCreatepath;

	private String userUpdatepath;

	private String serverContextpath;

	private String createPropertyTitletransferValidator;

	private String updatePropertyTitletransferValidator;

	private String createTitletransferUserValidator;

	private String updateTitletransferUserValidator;

	private String serverContextpathConfig;

	private String defaultPassword;
	
	private String channelType;
	
	private String createWorkflow;

	private String modifyPropertyValidator;

	private String modifypropertyUserValidator;

	public String getCreatePropertyValidator() {
		return environment.getProperty("egov.propertytax.create.property.validated");
	}

	public String getUpdatePropertyValidator() {
		return environment.getProperty("egov.propertytax.update.property.validated");
	}

	public String getCreatePropertyUserValidator() {
		return environment.getProperty("egov.propertytax.create.property.user.validated");
	}

	public String getUpdatePropertyUserValidator() {
		return environment.getProperty("egov.propertytax.update.property.user.validated");
	}

	public String getCreatePropertyTaxCalculated() {
		return environment.getProperty("egov.propertytax.create.property.tax.calculated");
	}

	public String getBootstrapServer() {
		return environment.getProperty("spring.kafka.bootstrap.servers");
	}

	public String getAutoOffsetReset() {
		return environment.getProperty("auto.offset.reset.config");
	}

	public String getUserHostName() {
		return environment.getProperty("egov.services.egov_user.hostname");
	}

	public String getUserBasepath() {
		return environment.getProperty("egov.services.egov_user.basepath");
	}

	public String getUserSearchpath() {
		return environment.getProperty("egov.services.egov_user.searchpath");
	}

	public String getUserCreatepath() {
		return environment.getProperty("egov.services.egov_user.createpath");
	}

	public String getUserUpdatepath() {
		return environment.getProperty("egov.services.egov_user.updatepath");
	}

	public String getServerContextpath() {
		return environment.getProperty("server.contextPath");
	}

	public String getCreateTitletransferValidator() {
		return environment.getProperty("egov.propertytax.create.property.titletransfer.validated");
	}

	public String getUpdateTitletransferValidator() {
		return environment.getProperty("egov.propertytax.update.property.titletransfer.validated");
	}

	public String getCreateTitletransferUserValidator() {
		return environment.getProperty("egov.propertytax.create.property.titletransfer.user.validated");
	}

	public String getUpdateTitletransferUserValidator() {
		return environment.getProperty("egov.propertytax.update.property.titletransfer.user.validated");
	}

	public String getServerContextpathConfig() {
		return environment.getProperty("server.contextpath");
	}

	public String getDefaultPassword() {
		return environment.getProperty("default.password");
	}

	public String getChannelType() {
		return environment.getProperty("egov.property.channel.type");
	}
	
	public String getCreateWorkflow() {
		return environment.getProperty("egov.propertytax.property.create.workflow.started");
	}

	public String getModifypropertyUserValidator() {
		return environment.getProperty("egov.propertytax.modify.property.user.validated");
	}

	public String getModifyPropertyValidator() {
		return environment.getProperty("egov.propertytax.modify.property.validated");
	}
}