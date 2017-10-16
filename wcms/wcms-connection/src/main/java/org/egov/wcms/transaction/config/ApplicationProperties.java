
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

package org.egov.wcms.transaction.config;

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
    private static final String WCMS_SEARCH_PAGESIZE_DEFAULT = "egov.services.wcms.search.pagesize.default";
    public static final String WCMS_SEARCH_PAGENO_MAX = "egov.services.wcms.search.pageno.max";
    public static final String WCMS_SEARCH_PAGESIZE_MAX = "egov.services.wcms.search.pagesize.max";
    @Value("${kafka.topics.wcms.newconnection-workflow.create}")
    private String initiatedWorkFlow;

    @Value("${kafka.topics.wcms.newconnection-workflow.update}")
    private String updatedWorkFlow;
    
    // topic for water-transaction use case
    @Value("${kafka.topics.newconnection.create.name}")
    private String createNewConnectionTopicName;

    @Value("${kafka.topics.newconnection.update.name}")
    private String updateNewConnectionTopicName;

    @Value("${kafka.topics.legacyconnection.create.name}")
    private String createLegacyConnectionTopicName;
    
    @Value("${kafka.topics.estimationnotice.persist.name}")
    private String estimationNoticePersistTopicName; 
    
    @Value("${kafka.topics.demandBill.update.name}")
    private String updateDemandBillTopicName;
    
    @Value("${kafka.topics.updateconn.aftercollection}")
    private String updateconnectionAfterCollection;
    
    @Value("${kafka.topics.workorder.persist.name}")
    private String workOrderNoticePersistTopicName; 
    
    @Value("${kafka.topics.estimationnotice.persist.key}")
    private String estimationNoticePersistTopicKey; 
    
    @Value("${kafka.topics.workorder.persist.key}")
    private String workOrderPersistTopicKey; 
    
    @Value("${kafka.topics.legacyconnection.update.name}")
    private String kafkaUpdateLegacyConnectionTopic;
    
    @Value("${kafka.topics.legacyconnection.update.key}")
    private String kafkaUpdateLegacyConnectionKey;

    @Autowired
    private Environment environment;
    
    public String getLegacyConnectionUpdateTopicName() { 
    	return kafkaUpdateLegacyConnectionTopic; 
    }
    
    public String getLegacyConnectionUpdateTopicKey() { 
    	return kafkaUpdateLegacyConnectionKey; 
    }

    public String getUpdateconnectionAfterCollection() {
        return updateconnectionAfterCollection;
    }


    public String wcmsSearchPageSizeDefault() {
        return environment.getProperty(WCMS_SEARCH_PAGESIZE_DEFAULT);
    }

    public String wcmsSearchPageNumberMax() {
        return environment.getProperty(WCMS_SEARCH_PAGENO_MAX);
    }

    public String wcmsSearchPageSizeMax() {
        return environment.getProperty(WCMS_SEARCH_PAGESIZE_MAX);
    }

  
    public String getInitiatedWorkFlow() {
        return initiatedWorkFlow;
    }

    public void setInitiatedWorkFlow(String initiatedWorkFlow) {
        this.initiatedWorkFlow = initiatedWorkFlow;
    }

    public String getUpdatedWorkFlow() {
        return updatedWorkFlow;
    }

    public void setUpdatedWorkFlow(String updatedWorkFlow) {
        this.updatedWorkFlow = updatedWorkFlow;
    }

    public String getCreateNewConnectionTopicName() {
        return createNewConnectionTopicName;
    }
    
    public String getUpdateDemandBillTopicName() {
        return updateDemandBillTopicName;
    }

    public String getCreateLegacyConnectionTopicName() {
        return createLegacyConnectionTopicName;
    }
    
    public String getUpdateNewConnectionTopicName() {
        return updateNewConnectionTopicName;
    }
    
    public String getEstimationNoticeTopicName() {
    	return estimationNoticePersistTopicName; 
    }
    
    public String getWorkOrderTopicName() { 
    	return workOrderNoticePersistTopicName;
    }
    
    public String getEstimationNoticeTopicKey() { 
    	return estimationNoticePersistTopicKey;
    }
    
    public String getWorkOrderTopicKey() {
    	return workOrderPersistTopicKey;
    }
    
}