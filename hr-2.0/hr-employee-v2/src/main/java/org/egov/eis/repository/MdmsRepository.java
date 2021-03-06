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
package org.egov.eis.repository;

import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.mdms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MdmsRepository {

	private final RestTemplate restTemplate;

	private final PropertiesManager propertiesManager;

	@Autowired
	public MdmsRepository(final RestTemplate restTemplate, final PropertiesManager propertiesManager) {

		this.restTemplate = restTemplate;
		this.propertiesManager = propertiesManager;
	}

	public JSONArray getByCriteria(final String tenantId, final String moduleName, final String masterName,
                                   final String filterFieldName, final String filterFieldValue, final RequestInfo requestInfo) {

        String filter = "";

        if (filterFieldName != null && filterFieldValue != null && !filterFieldName.isEmpty()
                && !filterFieldValue.isEmpty())
            filter = "[?(@." + filterFieldName + " == '" + filterFieldValue + "')]";

        return getByFilter(tenantId, moduleName, masterName, filter, requestInfo);
    }

    public JSONArray getByFilter(final String tenantId, final String moduleName, final String masterName, String
            filter, final RequestInfo requestInfo) {

        String mdmsBySearchCriteriaUrl = propertiesManager.getMdmsServiceHostname()
                + propertiesManager.getMdmsBySearchCriteriaUrl();
        List<MasterDetail> masterDetails;
        List<ModuleDetail> moduleDetails;
        MdmsCriteriaReq request = null;
        MdmsResponse response = null;
	//TODO remove this once yaml is updated with ts as long
	requestInfo.setTs(null);
        masterDetails = new ArrayList<>();
        moduleDetails = new ArrayList<>();

        masterDetails.add(MasterDetail.builder().name(masterName).build());
        if (filter != null && !filter.isEmpty())
            masterDetails.get(0).setFilter(filter);
        moduleDetails.add(ModuleDetail.builder().moduleName(moduleName).masterDetails(masterDetails).build());

        request = MdmsCriteriaReq.builder()
                .mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId).build())
                .requestInfo(requestInfo).build();
        response = restTemplate.postForObject(mdmsBySearchCriteriaUrl, request, MdmsResponse.class);
        if (response == null || response.getMdmsRes() == null || !response.getMdmsRes().containsKey(moduleName)
                || response.getMdmsRes().get(moduleName) == null
                || !response.getMdmsRes().get(moduleName).containsKey(masterName)
                || response.getMdmsRes().get(moduleName).get(masterName) == null)
            return null;
        else
            return response.getMdmsRes().get(moduleName).get(masterName);
    }
}
