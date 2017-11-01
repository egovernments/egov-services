package org.egov.works.estimate.utils;

import org.egov.works.commons.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.MasterDetails;
import org.egov.works.estimate.web.contract.MdmsCriteria;
import org.egov.works.estimate.web.contract.MdmsRequest;
import org.egov.works.estimate.web.contract.MdmsResponse;
import org.egov.works.estimate.web.contract.ModuleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONArray;

@Service
public class EstimateUtils {

	private final RestTemplate restTemplate;

	private final String mdmsBySearchCriteriaUrl;

	@Autowired
	public EstimateUtils(final RestTemplate restTemplate,
			@Value("${egov.services.egov_mdms.hostname}") final String mdmsServiceHostname,
			@Value("${egov.services.egov_mdms.searchpath}") final String mdmsBySearchCriteriaUrl) {

		this.restTemplate = restTemplate;
		this.mdmsBySearchCriteriaUrl = mdmsServiceHostname + mdmsBySearchCriteriaUrl;
	}

	/**
	 * 
	 * @param objectName
	 *            accepts the name of entity like : ScheduleOfRate,Contractor
	 *            camelcase should be follwed
	 * @param tenantId
	 *            tenantId for which the data to should be retrived
	 * @param requestInfo
	 * @return the json map it to your object.
	 */

	public JSONArray getMDMSData(final String objectName, final String filter, final String tenantId,
			final RequestInfo requestInfo,final String moduleName) {
		MasterDetails[] masterDetailsArray;
		ModuleDetails[] moduleDetailsArray;
		MdmsRequest mdmsRequest;
		MdmsResponse mdmsResponse;

		masterDetailsArray = new MasterDetails[1];
		masterDetailsArray[0] = MasterDetails.builder().name(objectName).filter("[?(@.code == '" + filter + "')]")
				.build();
		moduleDetailsArray = new ModuleDetails[1];

		moduleDetailsArray = new ModuleDetails[1];
		moduleDetailsArray[0] = ModuleDetails.builder().moduleName(moduleName).masterDetails(masterDetailsArray)
				.build();

		mdmsRequest = MdmsRequest.builder()
				.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray).tenantId(tenantId).build())
				.requestInfo(requestInfo).build();

		mdmsResponse = restTemplate.postForObject(mdmsBySearchCriteriaUrl, mdmsRequest, MdmsResponse.class);

		return mdmsResponse.getMdmsRes().get(moduleName).get(objectName);
	}
}