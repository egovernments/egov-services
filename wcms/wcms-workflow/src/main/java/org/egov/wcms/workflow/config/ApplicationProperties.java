
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

package org.egov.wcms.workflow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class ApplicationProperties {

    @Value("${kafka.topics.connection.create.name}")
    private String createConnection;

    @Value("${egov.services.wcms_masters.hostname}")
    private String waterMasterServiceBasePathTopic;

    @Value("${egov.services.wcms_masters.waterChargesConfig.searchpath}")
    private String waterMasterServiceWaterChargesConfigSearchPathTopic;

    @Value("${egov.services.workflow_service.hostname.businesskey}")
    private String businessKey;

    @Value("${kafka.topics.connection.update.name}")
    private String updateConnection;

    @Value("${kafka.topics.wcms.newconnection-workflow.create}")
    private String initiatedWorkFlow;

    @Value("${kafka.topics.wcms.newconnection-workflow.update}")
    private String updatedWorkFlow;

    @Value("${egov.services.workflow_service.hostname}")
    private String workserviceHostaName;
    @Value("${egov.services.workflow_service.startpath}")
    private String workflowservicestarturl;

    @Value("${egov.services.workflow_service.updatepath}")
    private String workflowserviceupdateurl;

    @Value("${egov.services.egov_user.hostname}")
    private String userHostName;

    @Value("${egov.services.egov_user.basepath}")
    private String userBasePath;

    @Value("${egov.services.egov_user.searchpath}")
    private String userSearchPath;
    
    @Value("${egov.services.tenant.host}")
    private String tenantServiceBasePath;
    
    @Value("${egov.services.tenant.searchpath}")
    private String tenantServiceSearchPath;
    
    @Value("${kafka.topics.updateconn.aftercollection}")
    private String updateconnectionAfterCollection;

   

}