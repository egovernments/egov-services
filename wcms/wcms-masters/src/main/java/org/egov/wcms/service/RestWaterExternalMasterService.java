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

package org.egov.wcms.service;

import org.egov.wcms.config.PropertiesManager;
import org.egov.wcms.web.contract.BoundaryRequestInfo;
import org.egov.wcms.web.contract.BoundaryRequestInfoWrapper;
import org.egov.wcms.web.contract.BoundaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestWaterExternalMasterService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    public BoundaryResponse getBoundaryNum(final String boundaryType, final String boundaryNum, final String tenantId) {
        String url = propertiesManager.getLocationServiceBasePathTopic()
                + propertiesManager.getLocationServiceBoundarySearchPathTopic();
        url = url.replace("{boundaryType}", boundaryType);
        url = url.replace("{boundaryNum}", boundaryNum);
        url = url.replace("{tenantId}", tenantId);
        final BoundaryResponse boundary = getBoundary(url);
        return boundary;
    }

    public BoundaryResponse getBoundary(final String url) {
        final BoundaryRequestInfo requestInfo = BoundaryRequestInfo.builder().build();
        final BoundaryRequestInfoWrapper wrapper = BoundaryRequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<BoundaryRequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final BoundaryResponse boundary = restTemplate.postForObject(url.toString(), request,
                BoundaryResponse.class);
        return boundary;
    }

    public BoundaryResponse getBoundaryName(final String boundaryType, final String[] boundaryNum, final String tenantId) {
        final String url = propertiesManager.getLocationServiceBasePathTopic()
                + propertiesManager.getLocationServiceBoundarySearchPathTopic();
        final BoundaryRequestInfo requestInfo = BoundaryRequestInfo.builder().build();
        final BoundaryRequestInfoWrapper wrapper = BoundaryRequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<BoundaryRequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final BoundaryResponse boundary = restTemplate.postForObject(url.toString(), request,
                BoundaryResponse.class, boundaryType, boundaryNum, tenantId);
        return boundary;

    }

}
