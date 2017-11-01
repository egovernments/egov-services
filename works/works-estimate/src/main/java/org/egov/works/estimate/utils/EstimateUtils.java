package org.egov.works.estimate.utils;

import java.util.ArrayList;

import org.egov.works.commons.domain.model.AppConfiguration;
import org.egov.works.estimate.web.contract.MasterDetails;
import org.egov.works.estimate.web.contract.MdmsCriteria;
import org.egov.works.estimate.web.contract.MdmsRequest;
import org.egov.works.estimate.web.contract.MdmsResponse;
import org.egov.works.estimate.web.contract.ModuleDetails;
import org.egov.works.estimate.web.model.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class EstimateUtils {

	public static final String MODULE_CODE = "Works";

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

	public JSONArray getAppConfigurationData(final String objectName, final String filter,final String tenantId,
			final RequestInfo requestInfo) {
		MasterDetails[] masterDetailsArray;
		ModuleDetails[] moduleDetailsArray;
		MdmsRequest mdmsRequest;
		MdmsResponse mdmsResponse;
		ArrayList<AppConfiguration> acResponseList;
		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();

		masterDetailsArray = new MasterDetails[1];
		masterDetailsArray[0] = MasterDetails.builder().name(objectName).filter("[?(@.code == '" + filter + "')]").build();
		moduleDetailsArray = new ModuleDetails[1];

		moduleDetailsArray = new ModuleDetails[1];
		moduleDetailsArray[0] = ModuleDetails.builder().moduleName(MODULE_CODE).masterDetails(masterDetailsArray)
				.build();

		mdmsRequest = MdmsRequest.builder()
				.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray).tenantId(tenantId).build())
				.requestInfo(requestInfo).build();

		mdmsResponse = restTemplate.postForObject(mdmsBySearchCriteriaUrl, mdmsRequest, MdmsResponse.class);

		acResponseList = new ArrayList<AppConfiguration>();

		// responseJSONArray =

		// for (int i = 0; i < responseJSONArray.size(); i++) {
		// acResponseList.add(mapper.convertValue(responseJSONArray.get(i),
		// AppConfiguration.class));
		// }

		return mdmsResponse.getMdmsRes().get(MODULE_CODE).get(objectName);
	}
}
/*

masterDetailsArray = new MasterDetails[1];
        masterDetailsArray[0] = MasterDetails.builder().name(Constants.COLLECTIONTYPE_MASTER_NAME)
        .filter("[?(@.code == '" + details.getCollectionType().getCode() + "')]").build();
        moduleDetailsArray = new ModuleDetails[1];
        moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
        .masterDetails(masterDetailsArray).build();

        request = MdmsRequest.builder()
        .mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
        .tenantId(details.getTenantId()).build())
        .requestInfo(collectionPointDetailsRequest.getRequestInfo()).build();*/
