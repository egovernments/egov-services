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

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Position;
import org.egov.eis.repository.NonVacantPositionsRepository;
import org.egov.eis.web.contract.DepartmentResponse;
import org.egov.eis.web.contract.NonVacantPositionsGetRequest;
import org.egov.eis.web.contract.PositionResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class VacantPositionsService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public List<Position> getVacantPositions(Long departmentId, Long designationId, Date asOnDate, String tenantId,
									   RequestInfoWrapper requestInfoWrapper) {
		PositionResponse positionResponse;
		try {
			URI url = new URI(propertiesManager.getHrMastersServiceHostName()
					+ propertiesManager.getHrMastersServiceBasePath()
					+ propertiesManager.getHrMastersServiceVacantPositionsSearchPath()
					+ "?tenantId=" + tenantId + "&departmentId=" + departmentId
					+ "&designationId=" + designationId + "&asOnDate=" + getFormattedAsOnDate(asOnDate));
			log.debug(url.toString());
			positionResponse = restTemplate.postForObject(url, requestInfoWrapper, PositionResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Following exception occurred while accessing Vacant Position API : " + e.getMessage());
			return null;
		}
		return positionResponse.getPosition();
	}

	private String getFormattedAsOnDate(Date asOnDate) {
		String dateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			dateString = sdf.format(asOnDate);
		}catch (Exception e){
			log.error("Following Error occurred while formatting asOnDate :: " + e.getMessage());
			e.printStackTrace();
		}
		return dateString;
	}
}