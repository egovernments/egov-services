package org.egov.propertytax.config;

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

	private String serverContextPath;

	private String dbUrl;

	private String driverClassName;

	private String username;

	private String password;

	private String flywayEnabled;

	private String flywayUser;

	private String flywayPassword;

	private String flywayOutOfOrder;

	private String flywayTable;

	private String flywayUrl;

	private String flywayLocations;

	private String createPropertyTaxGenerated;

	private String billingServiceHostName;

	private String billingServiceCreatedDemand;

	private String bootstrapServer;

	private String autoOffsetReset;

	private String createPropertyTaxCalculated;

	private String createPropertyWorkflow;

	private String demandBusinessService;

	private String dateFormat;
	
    private String modifyPropertyTaxGenerated;
	
	private String updatePropertyTaxGenerated;
	
	private String modifyPropertyTaxCalculated;
	
	private String billingServiceSearchDemand;
	
	private String billingServiceUpdateDemand;
	
	private String billindServiceAdvTaxHead;

	private String createTitleTransferTaxCalculated;

	private String updateTitleTransferTaxCalculated;

	private String createTitleTransferTaxGenerated;

	private String updateTitleTransferTaxGenerated;

	private String dbDateFormat;

	private String defaultDateFormat;

	private String titleTransferBusinessService;

	private String titleTransfer;

	private String failed;

	private String calculatorHostName;

	private String calculatorTaxperiodsSearch;

	private String invalidDemand;

	private String invalidUpdateDemand;

	private String demandSearchPath;

	private String demandUpdatePath;

	private String titleTransferTaxhead;
	
	private String specialNoticeAction;

	public String getServerContextPath() {
		return environment.getProperty("server.contextPath");
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

	public String getCreatePropertyTaxGenerated() {
		return environment.getProperty("egov.propertytax.property.tax.generated");
	}

	public String getBillingServiceHostName() {
		return environment.getProperty("egov.services.billing_service.hostname");
	}

	public String getBillingServiceCreatedDemand() {
		return environment.getProperty("egov.services.billing_service.createdemand");
	}

	public String getBootstrapServer() {
		return environment.getProperty("spring.kafka.bootstrap.servers");
	}

	public String getAutoOffsetReset() {
		return environment.getProperty("auto.offset.reset.config");
	}

	public String getCreatePropertyTaxCalculated() {
		return environment.getProperty("egov.propertytax.create.tax.calculated");
	}

	public String getCreatePropertyWorkflow() {
		return environment.getProperty("egov.propertytax.create.workflow.started");
	}

	public String getDemandBusinessService() {
		return environment.getProperty("businessService");
	}

	public String getDateFormat() {
		return environment.getProperty("dd/MM/yyyy hh:mm:ss");
	}


	public String getCreateTitleTransferTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.create.tax.calculated");
	}

	public String getUpdateTitleTransferTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.update.tax.calculated");
	}

	public String getCreateTitleTransferTaxGenerated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.create.tax.generated");
	}

	public String getUpdateTitleTransferTaxGenerated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.update.tax.generated");
	}

	public String getDefaultDateFormat() {
		return environment.getProperty("egov.propertytax.default.dateformat");
	}

	public String getDbDateFormat() {
		return environment.getProperty("egov.propertytax.db.dateformat");
	}

	public String getCalculatorHostName() {
		// TODO Auto-generated method stub
		return environment.getProperty("egov.services.pt_calculator.hostname");
	}

	public String getCalculatorTaxperiodsSearch() {
		// TODO Auto-generated method stub
		return environment.getProperty("egov.services.pt_calculator.taxperiods.search");
	}

	public String getTitleTransferBusinessService() {
		return environment.getProperty("egov.propertytax.titletransfer.businessService");
	}

	public String getTitleTransfer() {
		// TODO Auto-generated method stub
		return environment.getProperty("egov.propertytax.titletransfer");
	}

	public String getFailed() {
		// TODO Auto-generated method stub
		return environment.getProperty("failed");
	}

	public String getInvalidInput() {
		// TODO Auto-generated method stub
		return environment.getProperty("egov.propertytax.invalid.taxperiod");
	}

	public String getDemandSearchPath() {
		return environment.getProperty("egov.services.billing_service.searchdemand");
	}

	public String getDemandUpdatePath() {
		return environment.getProperty("egov.services.billing_service.update.demand");
	}

	public String getInvalidDemand() {
		// TODO Auto-generated method stub
		return environment.getProperty("egov.propertytax.invalid.demand");
	}

	public String getInvalidUpdateDemand() {
		// TODO Auto-generated method stub
		return environment.getProperty("egov.propertytax.invalid.demand.update");
	}

	public String getTitleTransferTaxhead() {
		return environment.getProperty("egov.propertytax.titletransfer.taxhead");
	}

	
	public String getModifyPropertyTaxGenerated() {
		return  environment.getProperty("egov.propertytax.modify.demand");
	}
	
	public String getModifyPropertyTaxCalculated() {
		return environment.getProperty("egov.propertytax.modify.tax.calculated");
	}
	
	public String getBillingServiceSearchDemand() {
		return environment.getProperty("egov.services.billing_service.searchdemand");
	}
	
	public String getBillingServiceUpdateDemand() {
		return environment.getProperty("egov.services.billing_service.updatedemand");
	}
	
	public String getUpdatePropertyTaxCalculated() {
		return environment.getProperty("egov.propertytax.update.tax.calculated");
	}

	public String getUpdatePropertyTaxGenerated() {
		return  environment.getProperty("egov.propertytax.update.demand");
	}

	public String getBillindServiceAdvTaxHead() {
		return environment.getProperty("advancetaxhead");
	}

	public String getSpecialNoticeAction() {
		return environment.getProperty("special.notice.action");
	}
}
