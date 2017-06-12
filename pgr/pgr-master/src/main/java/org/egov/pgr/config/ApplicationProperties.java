
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {
	
    
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
    private String createServiceTypeTopicName;
    
    @Value("${kafka.topics.servicetype.create.key}")
    private String createServiceTypeTopicKey;
    
    @Value("${kafka.topics.servicetype.update.key}")
    private String updateServiceTypeTopickey; 
    
    @Value("${kafka.topics.servicetype.update.name}")
    private String updateServiceTypeTopicName;

    @Value("${kafka.topics.escalationtimetype.create.name}")
    private String createEscalationTimeTypeName;
    
	@Value("${kafka.topics.escalationtimetype.create.key}")
    private String createEscalationTimeTypeKey;
	
    @Value("${kafka.topics.escalationtimetype.update.name}")
    private String updateEscalationTimeTypeName;
    
	@Value("${kafka.topics.escalationtimetype.update.key}")
    private String updateEscalationTimeTypeKey;
    
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
	
	public String getCreateServiceTypeTopicName(){
		return createServiceTypeTopicName;
	}
	public String getCreateServiceTypeTopicKey(){
		return createServiceTypeTopicKey;
	}
    public String getUpdateServiceGroupTopicName() {
		return updateServiceGroupTopicName;
	}

	public String getUpdateServiceGroupTopicKey() {
		return updateServiceGroupTopicKey;
	}
	
	public String getUpdateServiceTypeTopicName(){
		return updateServiceTypeTopicName;
	}
	
	public String getUpdateServiceTypeTopicKey(){
		return updateServiceTypeTopickey;

	}
    public String getCreateEscalationTimeTypeName() {
		return createEscalationTimeTypeName;
	}

	public String getCreateEscalationTimeTypeKey() {
		return createEscalationTimeTypeKey;
	}


}