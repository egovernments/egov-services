/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *
 *       Copyright (C) <2015>  eGovernments Foundation
 *
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.swm.web.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.web.contract.DesignationResponse;
import org.egov.swm.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DesignationRepository {

    private final RestTemplate restTemplate;

    private final String designationByNameUrl;

    private final String designationByCodeUrl;

    @Autowired
    public DesignationRepository(final RestTemplate restTemplate,
            @Value("${egov.services.hr_masters.hostname}") final String designationServiceHostname,
            @Value("${egov.services.hr_masters.designations.by.name}") final String designationByNameUrl,
            @Value("${egov.services.hr_masters.designations.by.code}") final String designationByCodeUrl) {

        this.restTemplate = restTemplate;
        this.designationByNameUrl = designationServiceHostname + designationByNameUrl;
        this.designationByCodeUrl = designationServiceHostname + designationByCodeUrl;
    }

    public DesignationResponse getDesignationByName(final String designationName, final String tenantId,
            final RequestInfo requestInfo) {

        final RequestInfoWrapper wrapper = new RequestInfoWrapper();
        wrapper.setRequestInfo(requestInfo);

        return restTemplate.postForObject(designationByNameUrl, wrapper, DesignationResponse.class, designationName,
                tenantId);

    }

    public DesignationResponse getDesignationByCode(final String designationCode, final String tenantId,
            final RequestInfo requestInfo) {

        final RequestInfoWrapper wrapper = new RequestInfoWrapper();
        wrapper.setRequestInfo(requestInfo);

        return restTemplate.postForObject(designationByCodeUrl, wrapper, DesignationResponse.class, designationCode,
                tenantId);

    }
}
