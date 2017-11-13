package org.egov.lams.services.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.EstateSearchCriteria;
import org.egov.lams.common.web.contract.LandPossession;
import org.egov.lams.common.web.contract.PropertyType;
import org.egov.lams.common.web.contract.RegisterName;
import org.egov.lams.common.web.contract.SubRegisterName;
import org.egov.lams.common.web.contract.SubUsageType;
import org.egov.lams.common.web.contract.UsageType;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.service.persistence.repository.MasterDataRepository;
import org.egov.lams.services.util.MasterDataMapperUtil;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class MasterDataService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private MasterDataRepository mdmsRepository;
	
	@Autowired
	private MasterDataMapperUtil mapperUtil;

	
	public void getEstateMaster(RequestInfo requestInfo, EstateRegister estateRegister) {

		Map<String, String> argsMap = new HashMap<>();
		argsMap.put(propertiesManager.getMdmsRegisterName(), estateRegister.getRegister().getCode());
		argsMap.put(propertiesManager.getMdmsSubRegisterName(), estateRegister.getSubRegister().getCode());
		argsMap.put(propertiesManager.getMdmsPropertyType(), estateRegister.getPropertyType().getCode());

		Map<String, JSONArray> responseMap = mdmsRepository.getMastersByCode(argsMap, requestInfo,
				estateRegister.getTenantId());
		
		mapperUtil.getEstateMasterData(estateRegister, responseMap);
	}
	
	public void getLandPossessionMaster(RequestInfo requestInfo, LandPossession possession) {

		Map<String, String> argsMap = new HashMap<>();
		argsMap.put(propertiesManager.getMdmsUsageType(), possession.getUsage().getCode());
		argsMap.put(propertiesManager.getMdmsSubUsageType(), possession.getSubUsage().getCode());

		Map<String, JSONArray> responseMap = mdmsRepository.getMastersByCode(argsMap, requestInfo,
				possession.getTenantId());

		mapperUtil.getLandPossessionMasters(possession, responseMap);
	}
	
	public void getEstateMasterCodes(RequestInfo requestInfo,EstateSearchCriteria searchEstateCrit){
		
		Map<String, String> argsMap = new HashMap<>();
		if (searchEstateCrit.getRegisterName() != null)
			argsMap.put(propertiesManager.getMdmsRegisterName(), searchEstateCrit.getRegisterName());
		if (searchEstateCrit.getSubRegisterName() != null)
			argsMap.put(propertiesManager.getMdmsSubRegisterName(), searchEstateCrit.getSubRegisterName());
		if (searchEstateCrit.getPropertyType() != null)
			argsMap.put(propertiesManager.getMdmsPropertyType(), searchEstateCrit.getPropertyType());

		Map<String, JSONArray> responseMap = mdmsRepository.getEstateMastersCodesForSearch(argsMap, requestInfo,
				searchEstateCrit.getTenantId());
		mapperUtil.getEstateMasterCodeForSearch(searchEstateCrit, responseMap);
//		searchEstateCrit.setPropertyType(estateRegister.getPropertyType().getCode());
//		searchEstateCrit.setRegisterName(estateRegister.getRegisterName().getCode());
//		searchEstateCrit.setSubRegisterName(estateRegister.getSubRegisterName().getCode());
	}
}
