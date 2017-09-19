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


import org.egov.workflow.domain.model.PersistRouter;
import org.egov.workflow.domain.model.PersistRouterReq;
import org.egov.workflow.persistence.repository.RouterRepository;
import org.egov.workflow.web.contract.RouterRequest;
import org.egov.workflow.web.contract.RouterSearch;
import org.egov.workflow.web.contract.RouterSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouterService {


    public List<RouterSearch> getRouterTypes(final RouterSearchRequest routerSearchRequest) {
        return routerRepository.findForCriteria(routerSearchRequest);
    }

    public List<RouterSearch> getRouterByHierarchyType(final RouterSearchRequest routerSearchRequest) {
        return routerRepository.findForHierarchyType(routerSearchRequest);
    }

    public static final Logger logger = LoggerFactory.getLogger(RouterService.class);

    @Autowired
    private RouterRepository routerRepository;

    public void create(final RouterRequest routerRequests) {
        List<Long> serviceTypes = routerRequests.getRouter().getServices();

        List<Long> boundaries = routerRequests.getRouter().getBoundaries();

        PersistRouterReq persistRouterReq = new PersistRouterReq();
        PersistRouter persistRouter = new PersistRouter();
        persistRouterReq.setRequestInfo(routerRequests.getRequestInfo());
        persistRouter.setPosition(routerRequests.getRouter().getPosition());
        persistRouter.setId(routerRequests.getRouter().getId());
        persistRouter.setTenantId(routerRequests.getRouter().getTenantId());
        persistRouter.setActive(routerRequests.getRouter().getActive());

        for (int i = 0; i < boundaries.size(); i++) {
            long boundaryId = boundaries.get(i);
            int flag = 0;
            for (int j = 0; j < serviceTypes.size(); j++) {
                flag++;
                Long serviceId = serviceTypes.get(j);
                logger.info("Boundary Size is" + boundaries.get(i));
                persistRouter.setService(serviceId);
                persistRouter.setBoundary(boundaryId);
                persistRouterReq.setRouterType(persistRouter);
                if (checkforDuplicate(persistRouterReq, true)) {
                    logger.info("Creating the Router Entry");
                    routerRepository.createRouter(persistRouterReq, true);
                } else {
                    logger.info("Updating the Router Entry");
                    routerRepository.updateRouter(persistRouterReq, true);
                }
            }
            if (flag == 0) {
                persistRouter.setBoundary(boundaryId);
                persistRouterReq.setRouterType(persistRouter);
                if (checkforDuplicate(persistRouterReq, false)) {
                    logger.info("Creating the Router Entry -- flag");
                    routerRepository.createRouter(persistRouterReq, false);
                } else {
                    logger.info("Updating the Router Entry -- flag");
                    routerRepository.updateRouter(persistRouterReq, false);
                }
            }
        }
        logger.info("Persisting Router record");
    }

    public boolean checkforDuplicate(PersistRouterReq persistRouterReq, boolean action) {
        PersistRouter persistRouter = routerRepository.ValidateRouter(persistRouterReq, action);
        return persistRouter == null;
    }

    public boolean checkCombinationExists(RouterRequest routerTypeReq) {
        return routerRepository.checkCombinationExists(routerTypeReq);
    }

}

