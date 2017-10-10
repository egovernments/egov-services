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

package org.egov.wcms.workflow.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.workflow.config.ApplicationProperties;
import org.egov.wcms.workflow.model.contract.RequestInfoWrapper;
import org.egov.wcms.workflow.model.contract.WaterChargesConfigResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WaterConfigurationService {
    
    
    public static final String WORKFLOW_REQUIRED_CONFIG_KEY="WORKFLOW_REQUIRED_OR_NOT";

    @Autowired
    private ApplicationProperties applicationProperties;
 

    public Boolean  getConfigurations( final String tenantId, final RequestInfo requestInfo) {

        Boolean isWaterConfigValues=Boolean.FALSE;
        StringBuilder url= new StringBuilder();
         url.append(applicationProperties.getWaterMasterServiceBasePathTopic()
                + applicationProperties.getWaterMasterServiceWaterChargesConfigSearchPathTopic());
        
        url.append("?name=").append(WORKFLOW_REQUIRED_CONFIG_KEY);
        url.append("&tenantId=").append(tenantId);
      

        final WaterChargesConfigResponse configurationResponse = getWaterConfigValues(url.toString());

        if (configurationResponse != null && !configurationResponse.getWaterChargesConfigValue().isEmpty()
                && configurationResponse.getWaterChargesConfigValue().get(0).getValue().equals("YES"))
            isWaterConfigValues = Boolean.TRUE;
        
        return isWaterConfigValues;
    }
    public String getWaterChargeConfigValuesForDesignation(final String name,final String tenantId) {
        String designationName = "";
        StringBuilder url= new StringBuilder();
        url.append(applicationProperties.getWaterMasterServiceBasePathTopic()
               + applicationProperties.getWaterMasterServiceWaterChargesConfigSearchPathTopic());
       
       url.append("?name=").append(name);
       url.append("&tenantId=").append(tenantId);
       final WaterChargesConfigResponse waterChargesConfigRes = getWaterConfigValues(url.toString());

        if (waterChargesConfigRes != null && !waterChargesConfigRes.getWaterChargesConfigValue().isEmpty())
            designationName =waterChargesConfigRes.getWaterChargesConfigValue().get(0).getValue();

        return designationName;
    }
    
    public WaterChargesConfigResponse getWaterConfigValues(final String url) {
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().
                requestInfo(RequestInfo.builder().ts(111111111L).build()).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        return new RestTemplate().postForObject(url.toString(), request,
                WaterChargesConfigResponse.class);
    }
}