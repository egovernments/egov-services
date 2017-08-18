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

package org.egov.collection.repository;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.PositionSearchCriteriaWrapper;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;



@Repository
public class WorkflowRepository {
	public static final Logger logger = LoggerFactory
			.getLogger(WorkflowRepository.class);
	
	@Autowired
	private RestTemplate restTemplate;

	
	@Autowired
	private ApplicationProperties applicationProperties;
		
	public Object getPositionForUser(PositionSearchCriteriaWrapper positionSearchCriteriaWrapper){
		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getHremployeeServiceHost();
		String uri = applicationProperties.getGetPosition();
		String id = positionSearchCriteriaWrapper.getPositionSearchCriteria().getEmployeeId().toString();
		String uriAppend = applicationProperties.getGetPositionAppend();
		String searchCriteria="?tenantId="+positionSearchCriteriaWrapper.getPositionSearchCriteria().getTenantId();
		
		builder.append(hostname)
			   .append(uri)
		       .append(id)
			   .append(uriAppend)
			   .append(searchCriteria);
				
				
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(positionSearchCriteriaWrapper.getRequestInfo());
		
		logger.info("URI: "+builder.toString());
		
		Object response = null;
		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper, Object.class);
		}catch(Exception e){
			logger.error("Couldn't fetch departments. Exception!"+e.getCause());
		}
		
		return response;

	}

}
