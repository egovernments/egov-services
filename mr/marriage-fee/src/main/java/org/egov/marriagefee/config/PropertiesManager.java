package org.egov.marriagefee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import lombok.ToString;

@Configuration
@ToString
@NoArgsConstructor
@SuppressWarnings("unused")
@Service
public class PropertiesManager {

	@Autowired
	Environment environment;

	private String serverContextPath;

	private String CreateMarriageFeeGenerated;

	private String billingServiceHostName;

	private String billingServiceCreatedDemand;

	private String bootstrapServer;

	private String autoOffsetReset;

	private String dbUrl;

	private String driverClassName;

	private String username;

	private String password;

	private String dateFormat;

	

	public String getServerContextPath() {
		return environment.getProperty("server.contextPath");
	}

	public String getBootstrapServer() {
		return environment.getProperty("spring.kafka.bootstrap.servers");
	}

	public String getAutoOffsetReset() {
		return environment.getProperty("auto.offset.reset.config");
	}

	public String getDateFormat() {
		return environment.getProperty("dd/MM/yyyy hh:mm:ss");
	}

	public String getDbUrl() {
		return environment.getProperty("spring.datasource.url");
	}

	public String getDriverClassName() {
		return environment.getProperty("spring.datasource.driver-class-name");
	}

	public String getUsername() {
		return environment.getProperty("spring.datasource.username");
	}

	public String getPassword() {
		return environment.getProperty("spring.datasource.password");
	}

	public String getFlywayEnabled() {
		return environment.getProperty("flyway.enabled");
	}

	public String getFlywayUser() {
		return environment.getProperty("flyway.user");
	}

	public String getFlywayPassword() {
		return environment.getProperty("flyway.password");
	}

	public String getFlywayOutOfOrder() {
		return environment.getProperty("flyway.outOfOrder");
	}

	public String getFlywayTable() {
		return environment.getProperty("flyway.table");
	}

	public String getFlywayUrl() {
		return environment.getProperty("flyway.url");
	}

	public String getFlywayLocations() {
		return environment.getProperty("flyway.locations");
	}

	// demand generation
	public String getBillingServiceHostName() {
		return environment.getProperty("egov.services.billing_service.hostname");
	}

	public String getBillingServiceCreatedDemand() {
		return environment.getProperty("egov.services.billing_service.createdemand");
	}

	public String getDemandBusinessService() {

		return environment.getProperty("business.service");
	}

	public String getCreateMarriageFeeGenerated() {

		return environment.getProperty("egov.marriageregn.property.demand.generated");
	}

	public String getCreateMarriageRegnTopicName() {

		return environment.getProperty("kafka.topics.create.marriageregn");
	}

	public String getBillingServiceGenerateBill() {

		return environment.getProperty("egov.services.billing_service.createbill");
	}

	public String getTaxPeriods() {

		return environment.getProperty("egov.services.billing_service.searchtaxperiods");
	}
	public String getTaxHead() {

		return environment.getProperty("egov.services.billing_service.searchhead");
	}

	public String getCollectionServiceHost() {

		return environment.getProperty("egov.services.collection_service.hostname");
	}

	public String getCollectionServiceGenerateReceipt() {

		return environment.getProperty("egov.services.collection_service.createreciept");
	}
	
	public String getTaxHeadCode(){
		
		return environment.getProperty("egov.services.marriageregn.property.taxheadcode");
		
	}
	
	public String getCreateWorkflowTopicName(){
		return environment.getProperty("kafka.topics.create.workflow");
		
	}
}
