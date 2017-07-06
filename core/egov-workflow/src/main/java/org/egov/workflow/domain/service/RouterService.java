/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.workflow.domain.service;
/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */



import java.util.ArrayList;
import java.util.List;

import org.egov.workflow.domain.model.PersistRouter;
import org.egov.workflow.domain.model.PersistRouterReq;
import org.egov.workflow.domain.model.ServiceType;
import org.egov.workflow.persistence.repository.RouterRepository;
import org.egov.workflow.web.contract.BoundaryIdType;
import org.egov.workflow.web.contract.RouterType;
import org.egov.workflow.web.contract.RouterTypeGetReq;
import org.egov.workflow.web.contract.RouterTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouterService {
	
	
	
	public List<RouterType> getRouterTypes(final RouterTypeGetReq routerTypeGetRequest) {
        return routerRepository.findForCriteria(routerTypeGetRequest);
    }

	public static final Logger logger = LoggerFactory.getLogger(RouterService.class);

	@Autowired
	private RouterRepository routerRepository;

	
	
	public PersistRouterReq create(final RouterTypeReq routerRequests) {
		List<ServiceType> serviceTypes = new ArrayList<ServiceType>();
		serviceTypes = routerRequests.getRouterType().getServices();
		logger.info("Service Type size is" + serviceTypes.size());

		List<BoundaryIdType> boundaries = new ArrayList<BoundaryIdType>();
		boundaries = routerRequests.getRouterType().getBoundaries();

		PersistRouterReq prq = new PersistRouterReq();
		PersistRouter pr = new PersistRouter();
		prq.setRequestInfo(routerRequests.getRequestInfo());
		pr.setPosition(routerRequests.getRouterType().getPosition());
		pr.setId(routerRequests.getRouterType().getId());
		pr.setTenantId(routerRequests.getRouterType().getTenantId());
		pr.setActive(routerRequests.getRouterType().getActive());

		for (int i = 0; i < boundaries.size(); i++) {
			int boundaryID = boundaries.get(i).getBoundaryType();
			int flag = 0;
			for (int j = 0; j < serviceTypes.size(); j++) {
				flag++;
				Long serviceID = serviceTypes.get(j).getId();
				logger.info("Boundary Size is" + boundaries.get(i).getBoundaryType());
				pr.setService(serviceID);
				pr.setBoundary(boundaryID);
				prq.setRouterType(pr);
				if (checkforDuplicate(prq, true)) {
					routerRepository.createRouter(prq, true);
					logger.info("Creating the Router Entry");
				} else {
					routerRepository.updateRouter(prq, true);
					logger.info("Updating the Router Entry");
				}
			}
			if (flag == 0) {
				pr.setBoundary(boundaryID);
				prq.setRouterType(pr);
				if (checkforDuplicate(prq, false)) {
					routerRepository.createRouter(prq, false);
					logger.info("Creating the Router Entry");
				} else {
					routerRepository.updateRouter(prq, false);
					logger.info("Updating the Router Entry");
				}
			}
		}
		logger.info("Persisting Router record");
		// return routerRepository.createRouter(prq);
		return null;

	}
	
	public boolean checkforDuplicate(PersistRouterReq persistRouterReq, boolean action){
		PersistRouter pr = new PersistRouter();
		pr = routerRepository.ValidateRouter(persistRouterReq, action);
		if (pr != null){
			return false;
		}
			else {
				return true;
		}
	}
	
	public boolean verifyUniquenessOfRequest(RouterTypeReq routerTypeReq) {
		return routerRepository.verifyUniquenessOfRequest(routerTypeReq);
	}
	

	/*public RouterType createRouter(final String topic, final String key,
			final RouterTypeReq routerTypeReq) {
		final ObjectMapper mapper = new ObjectMapper();
		String routerValue = null;
		try {
			logger.info("RouterTypeRequest::" + routerTypeReq);
			routerValue = mapper.writeValueAsString(routerTypeReq);
			logger.info("Value being pushed on the Queue, RouterGroupValue::" + routerValue);
		} catch (final JsonProcessingException e) {
			logger.error("Exception Encountered : " + e);
		}
		try {
			pgrProducer.sendMessage(topic, key, routerValue);
		} catch (final Exception e) {
			logger.error("Exception while posting to kafka Queue : " + e);
			return routerTypeReq.getRouterType();
		}
		logger.error("Producer successfully posted the request to Queue");
		return routerTypeReq.getRouterType();
	}*/
	
	
	
	

}

