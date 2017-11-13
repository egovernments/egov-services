package org.egov.lams.services.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.EstateSearchCriteria;
import org.egov.lams.common.web.contract.LandPossession;
import org.egov.lams.common.web.contract.PropertyType;
import org.egov.lams.common.web.contract.Register;
import org.egov.lams.common.web.contract.RegisterName;
import org.egov.lams.common.web.contract.SubRegister;
import org.egov.lams.common.web.contract.SubRegisterName;
import org.egov.lams.common.web.contract.SubUsage;
import org.egov.lams.common.web.contract.SubUsageType;
import org.egov.lams.common.web.contract.Usage;
import org.egov.lams.common.web.contract.UsageType;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Component
public class MasterDataMapperUtil {
	
	@Autowired
	private PropertiesManager propertiesManager;

	public void getEstateMasterData(EstateRegister estateRegister,Map<String, JSONArray> responseMap){
		
		List<Register> registerName;
		List<SubRegister> subRegNames;
		List<PropertyType> propertyTypes;

		try {
			registerName = Arrays.asList(new ObjectMapper().readValue(
					responseMap.get(propertiesManager.getMdmsRegisterName()).toJSONString(), Register[].class));

			subRegNames = Arrays.asList(new ObjectMapper().readValue(
					responseMap.get(propertiesManager.getMdmsSubRegisterName()).toJSONString(),
					SubRegister[].class));

			propertyTypes = Arrays.asList(new ObjectMapper().readValue(
					responseMap.get(propertiesManager.getMdmsPropertyType()).toJSONString(), PropertyType[].class));
			
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (!registerName.isEmpty())
			estateRegister.setRegister(registerName.get(0));
		else
			throw new CustomException("INVALID_REG_NAME", "Register name: "
					+ estateRegister.getSubRegister().getCode() + " details does not exist");

		if (!subRegNames.isEmpty())
			estateRegister.setSubRegister(subRegNames.get(0));
		else
			throw new CustomException("INVALID_SUB_REG_NAME", "Sub Register name: "
					+ estateRegister.getSubRegister().getCode() + " details does not exist");

		if (!propertyTypes.isEmpty())
			estateRegister.setPropertyType(propertyTypes.get(0));
		else
			throw new CustomException("INVALID_PROPERTY_TYPE", "Property type: "
					+ estateRegister.getPropertyType().getCode() + " details does not exist");
	}
	
	public void getEstateMasterCodeForSearch(EstateSearchCriteria searchEstateCrit,Map<String, JSONArray> responseMap){
		
		List<RegisterName> registerName = null;
		List<SubRegisterName> subRegNames = null;
		List<PropertyType> propertyTypes = null;

		try {
			if(responseMap.get(propertiesManager.getMdmsRegisterName())!=null)
			registerName = Arrays.asList(new ObjectMapper().readValue(
					responseMap.get(propertiesManager.getMdmsRegisterName()).toJSONString(), RegisterName[].class));
			if(responseMap.get(propertiesManager.getMdmsSubRegisterName())!=null)
			subRegNames = Arrays.asList(new ObjectMapper().readValue(
					responseMap.get(propertiesManager.getMdmsSubRegisterName()).toJSONString(),
					SubRegisterName[].class));
			if(responseMap.get(propertiesManager.getMdmsPropertyType())!=null)
			propertyTypes = Arrays.asList(new ObjectMapper().readValue(
					responseMap.get(propertiesManager.getMdmsPropertyType()).toJSONString(), PropertyType[].class));
			
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (registerName!=null)
			searchEstateCrit.setRegisterName(registerName.get(0).getCode());

		if (subRegNames!=null)
			searchEstateCrit.setSubRegisterName(subRegNames.get(0).getCode());

		if (propertyTypes!=null)
			searchEstateCrit.setPropertyType(propertyTypes.get(0).getCode());
	}
	
	public void getLandPossessionMasters(LandPossession possession,Map<String, JSONArray> responseMap){
		
		List<Usage> usageTypes;
		List<SubUsage> subUsageTypes;
		try {
			usageTypes = Arrays.asList(new ObjectMapper().readValue(
					responseMap.get(propertiesManager.getMdmsUsageType()).toJSONString(), Usage[].class));

			subUsageTypes = Arrays.asList(new ObjectMapper().readValue(
					responseMap.get(propertiesManager.getMdmsSubUsageType()).toJSONString(),
					SubUsage[].class));

		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (!usageTypes.isEmpty())
			possession.setUsage(usageTypes.get(0));
		else
			throw new CustomException("INVALID_USAGE_TYPE", "Usage type: "
					+ possession.getUsage().getCode() + " details does not exist");

		if (!subUsageTypes.isEmpty())
			possession.setSubUsage(subUsageTypes.get(0));
		else
			throw new CustomException("INVALID_SUB_USAGE_TYPE", "Sub Usage type: "
					+ possession.getSubUsage().getCode() + " details does not exist");
	}
}
