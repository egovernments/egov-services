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

package org.egov.eis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.bulk.Designation;
import org.egov.eis.repository.MdmsRepository;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DesignationService {

    private static final String MODULE_NAME = "common-masters";
    private static final String MASTER_NAME = "Designation";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ObjectMapper mapper;

	public Designation getDesignation(String code, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, MODULE_NAME, MASTER_NAME, "code", code,
                requestInfoWrapper.getRequestInfo());

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), Designation.class);
        else {
            log.error("Given Designation is invalid: " + code);
            return null;
        }
	}
	public List<Designation> getDesignations(DesignationGetRequest designationGetRequest ,String tenantId,RequestInfoWrapper requestInfoWrapper) {
        final List<Designation> designations = new ArrayList<>();

        String codes = getIdsAsCSV(designationGetRequest.getCodes());

        JSONArray responseJSONArray;

        String filter = "[?(@.code in [" + codes + "])]";

        responseJSONArray = mdmsRepository.getByFilter(tenantId, MODULE_NAME, MASTER_NAME, filter,
                requestInfoWrapper.getRequestInfo());

        if (responseJSONArray != null && responseJSONArray.size() > 0) {
            for (final Object obj : responseJSONArray)
                designations.add(mapper.convertValue(obj, Designation.class));
            return designations;
        } else
            return null;


    }

    private String getIdsAsCSV(List<String> ids) {
        return String.join(",", ids.stream().map(Object::toString).collect(Collectors.toList()));
    }
   
}