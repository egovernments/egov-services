
/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {
    @Value("${egov.services.seva.masters.search.pagesize.default}")
	public  String SEVA_MASTERS_SEARCH_PAGESIZE_DEFAULT ;
    
    @Value("{egov.services.seva.masters.search.pageno.max=50}")
	public String SEVA_MASTERS_SEARCH_PAGENO_MAX ;
    
    @Value("{egov.services.seva.masters.search.pagesize.max}")
    public  String SEVA_MASTERS_SEARCH_PAGESIZE_MAX;
	
	@Autowired
	private Environment environment;
    
	@Value("${kafka.topics.servicegroup.create.name}")
    private String createServiceGroupTopicName;
    
    @Value("${kafka.topics.servicegroup.create.key}")
    private String createServiceGroupTopicKey;

    @Value("${kafka.topics.router.create.name}")
    private String createRouterTopicName;
    
    @Value("${kafka.topics.router.create.key}")
    private String createRouterTopicKey;

    @Value("${kafka.topics.servicegroup.update.name}")
    private String updateServiceGroupTopicName;
    
    @Value("${kafka.topics.servicegroup.update.key}")
    private String updateServiceGroupTopicKey;

	@Value("${kafka.topics.receivingcenter.create.name}")
    private String createReceivingCenterTopicName;
    
    @Value("${kafka.topics.receivingcenter.create.key}")
    private String createReceivingCenterTopicKey;
    
    @Value("${kafka.topics.receivingcenter.update.name}")
    private String updateReceivingCenterTopicName;
    
    @Value("${kafka.topics.receivingcenter.update.key}")
    private String updateReceivingCenterTopicKey;
    
    @Value("${kafka.topics.receivingmode.create.name}")
    private String createReceivingModeTopicName;
    
    @Value("${kafka.topics.receivingmode.create.key}")
    private String createReceivingModeTopicKey;
    
    @Value("${kafka.topics.receivingmode.update.name}")
    private String updateReceivingModeTopicName;
    
    @Value("${kafka.topics.receivingmode.update.key}")
    private String updateReceivingModeTopicKey;
    
    @Value("${kafka.topics.servicetype.create.name}")
    private String createGrievanceTypeTopicName;
    
    @Value("${kafka.topics.servicetype.create.key}")
    private String createGrievanceTypeTopicKey;
    
    @Value("${kafka.topics.servicetype.update.key}")
    private String updateGrievanceTypeTopickey;
    
    @Value("${kafka.topics.servicetype.update.name}")
    private String updateGrievanceTypeTopicName;

    @Value("${kafka.topics.escalationtimetype.create.name}")
    private String createEscalationTimeTypeName;
    
	@Value("${kafka.topics.escalationtimetype.create.key}")
    private String createEscalationTimeTypeKey;
	
    @Value("${kafka.topics.escalationtimetype.update.name}")
    private String updateEscalationTimeTypeName;
    
	@Value("${kafka.topics.escalationtimetype.update.key}")
    private String updateEscalationTimeTypeKey;
	
	@Value("${kafka.topics.otpconfig.create.name}")
    private String createOtpConfigName;
    
	@Value("${kafka.topics.otpconfig.create.key}")
    private String createOtpConfigKey;
	
    @Value("${kafka.topics.otpconfig.update.name}")
    private String updateOtpConfigName;
    
	@Value("${kafka.topics.otpconfig.update.key}")
    private String updateOtpConfigKey;
	
	@Value("${kafka.topics.escalationhierarchy.create.name}")
    private String createEscalationHierarchyName;
    
	@Value("${kafka.topics.escalationhierarchy.create.key}")
    private String createEscalationHierarchyKey;
	
    @Value("${kafka.topics.escalationhierarchy.update.name}")
    private String updateEscalationHierarchyName;
    
	@Value("${kafka.topics.escalationhierarchy.update.key}")
    private String updateEscalationHierarchyKey;

	@Value("${kafka.topics.servicetypeconfiguration.create.name}") 
    String createtopicName;
    
    @Value("${kafka.topics.servicetypeconfiguration.create.key}") 
    String createkey;
    
    @Value("${kafka.topics.servicetypeconfiguration.update.name}") 
    private String updateTopicname;
    
    @Value("${kafka.topics.servicetypeconfiguration.update.key}") 
    private String updatekey;
    
	@Value("${kafka.topics.servicetypes.create.name}")
	private String createServiceTypeTopicName;

	@Value("${kafka.topics.servicetypes.create.key}")
	private String createServiceTypeTopicKey;

	@Value("${kafka.topics.servicetypes.update.name}")
	private String updateServiceTypeTopicName;

	@Value("${kafka.topics.servicetypes.update.key}")
	private String updateServiceTypeTopicKey;

	@Value("${kafka.topics.servicedefinition.create.name}")
	private String createServiceDefinitionName;

	@Value("${kafka.topics.servicedefinition.create.key}")
	private String createServiceDefinitionKey;

	@Value("${kafka.topics.servicedefinition.update.name}")
	private String updateServiceDefinitionName;

	@Value("${kafka.topics.servicedefinition.update.key}")
	private String updateServiceDefinitionKey;

	public String getUpdateEscalationTimeTypeName() {
		return updateEscalationTimeTypeName;
	}

	public String getUpdateEscalationTimeTypeKey() {
		return updateEscalationTimeTypeKey;
	}

	public String getCreateServiceGroupTopicKey() {
		return createServiceGroupTopicKey;
	}

	public String getCreateServiceGroupTopicName() {
		return createServiceGroupTopicName;
	}

	public String getCreateRouterTopicKey() {
		return createRouterTopicKey;
	}

	public String getCreateRouterTopicName() {
		return createRouterTopicName;
	}
	
	public String getCreateReceivingCenterTopicKey() {
		return createReceivingCenterTopicKey;
	}

	public String getCreateReceivingCenterTopicName() {
		return createReceivingCenterTopicName;
	}
	
	public String getUpdateReceivingCenterTopicKey() {
		return updateReceivingCenterTopicKey;
	}

	public String getUpdateReceivingCenterTopicName() {
		return updateReceivingCenterTopicName;
	}
	
	public String getCreateReceivingModeTopicKey() {
		return createReceivingModeTopicKey;
	}

	public String getCreateReceivingModeTopicName() {
		return createReceivingModeTopicName;
	}
	
	public String getUpdateReceivingModeTopicKey() {
		return updateReceivingModeTopicKey;
	}

	public String getUpdateReceivingModeTopicName() {
		return updateReceivingModeTopicName;
	}
	
	public String getCreateGrievanceTypeTopicName(){
		return createGrievanceTypeTopicName;
	}
	public String getCreateGrievanceTypeTopicKey(){
		return createGrievanceTypeTopicKey;
	}
    public String getUpdateServiceGroupTopicName() {
		return updateServiceGroupTopicName;
	}

	public String getUpdateServiceGroupTopicKey() {
		return updateServiceGroupTopicKey;
	}
	
	public String getUpdateGrievanceTypeTopicName(){
		return updateGrievanceTypeTopicName;
	}
	
	public String getUpdateGrievanceTypeTopicKey(){
		return updateGrievanceTypeTopickey;

	}
    public String getCreateEscalationTimeTypeName() {
		return createEscalationTimeTypeName;
	}

	public String getCreateEscalationTimeTypeKey() {
		return createEscalationTimeTypeKey;
	}
	
	public String getCreateOtpConfigTopicName() {
		return createOtpConfigName;
	}

	public String getCreateOtpConfigTopicKey() {
		return createOtpConfigKey;
	}
	
	public String getUpdateOtpConfigTopicName() {
		return updateOtpConfigName;
	}

	public String getUpdateOtpConfigTopicKey() {
		return updateOtpConfigKey;
	}
	
	public String getCreateEscalationHierarchyTopicName() {
		return createEscalationHierarchyName;
	}

	public String getCreateEscalationHierarchyTopicKey() {
		return createEscalationHierarchyKey;
	}
	
	public String getUpdateEscalationHierarchyTopicName() {
		return updateEscalationHierarchyName;
	}

	public String getUpdateEscalationHierarchyTopicKey() {
		return updateEscalationHierarchyKey;
	}
	
	public String sevaSearchPageSizeDefault() {
		return SEVA_MASTERS_SEARCH_PAGESIZE_DEFAULT;
	}

	public String sevaSearchPageNumberMax() {
		return SEVA_MASTERS_SEARCH_PAGENO_MAX;
	}

	public String sevaSearchPageSizeMax() {
		return SEVA_MASTERS_SEARCH_PAGESIZE_MAX;
	}

	public String servicetypeconfigurationCreateTopic() {
		return createtopicName;
	}
	
	public String servicetypeconfigurationCreateKeyName() {
		return createkey;
	}
	
	public String servicetypeconfigurationUpdateTopic() {
		return updateTopicname;
	}
	
	public String servicetypeconfigurationUpdateKeyName() {
		return updatekey;
	}

	public String getCreateServiceTypeTopicName() {
		return createServiceTypeTopicName;
	}

	public String getCreateServiceTypeTopicKey() {
		return createServiceTypeTopicKey;
	}

	public String getCreateServiceDefinitionName() {
		return createServiceDefinitionName;
	}

	public String getCreateServiceDefinitionKey() {
		return createServiceDefinitionKey;
	}

	public String getUpdateServiceTypeTopicName() {
		return updateServiceTypeTopicName;
	}

	public String getUpdateServiceTypeTopicKey() {
		return updateServiceTypeTopicKey;
	}

	public String getUpdateServiceDefinitionName() {
		return updateServiceDefinitionName;
	}

	public String getUpdateServiceDefinitionKey() {
		return updateServiceDefinitionKey;
	}
}