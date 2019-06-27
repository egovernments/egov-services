package org.egov.mseva.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.mseva.config.PropertiesManager;
import org.egov.mseva.repository.RestCallRepository;
import org.egov.mseva.utils.ErrorConstants;
import org.egov.mseva.utils.MsevaConstants;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MDMSService {
	
	@Autowired
	private RestCallRepository repository;
	
	@Autowired
	private PropertiesManager props;
	
	
	public List<String> fetchEventTypes(RequestInfo requestInfo, String tenantId) {
		StringBuilder uri = new StringBuilder();
		uri.append(props.getMdmsHost()).append(props.getMdmsSearchEndpoint());
		Optional<Object> response = repository.fetchResult(uri, getRequestForEvents(requestInfo, tenantId));
		List<String> codes = new ArrayList<>();
		try {
			if(response.isPresent())
				codes = JsonPath.read(response.get(), MsevaConstants.MEN_MDMS_EVENTMASTER_CODES_JSONPATH);
			else
				throw new Exception();
		}catch(Exception e) {
			log.error("Exception while fetching from MDMS: ", e);
			throw new CustomException(ErrorConstants.MEN_ERROR_FROM_MDMS_CODE, ErrorConstants.MEN_ERROR_FROM_MDMS_MSG);
		}
		return codes;
	}
	
	private MdmsCriteriaReq getRequestForEvents(RequestInfo requestInfo, String tenantId) {
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(MsevaConstants.MEN_MDMS_EVENTMASTER_CODE)
				.filter(MsevaConstants.MEN_MDMS_EVENTMASTER_FILTER).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(MsevaConstants.MEN_MDMS_MODULE_CODE)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		tenantId = tenantId.split("\\.")[0]; //state-level master
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

}
