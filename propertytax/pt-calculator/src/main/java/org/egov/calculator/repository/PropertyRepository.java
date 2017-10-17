package org.egov.calculator.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.calculator.config.PropertiesManager;
import org.egov.calculator.exception.InvalidInputException;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.StructureClassResponse;
import org.egov.models.UsageMasterResponse;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PropertyRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	/**
	 * It will check occupancy master
	 * 
	 * @param calculationFactor
	 * @param calculationFactorRequest
	 * @param factor
	 * @return OccuapancyMasterResponse
	 */
	public OccuapancyMasterResponse getOccuapancyMaster(CalculationFactor calculationFactor,
			CalculationFactorRequest calculationFactorRequest, String factor) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(calculationFactorRequest.getRequestInfo());
		StringBuilder uri = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		uri.append(propertiesManager.getPropertyHostName());
		uri.append(propertiesManager.getPropertyBasepath());
		uri.append(propertiesManager.getPropertySearch());
		params.put("tenantId", calculationFactor.getTenantId());
		params.put("code", calculationFactor.getFactorCode());
		params.put("factorType", factor);

		log.info("PropertyRepository occuapancies uri is:" + uri + "\n PropertyRepository occuapancies is:"
				+ requestInfoWrapper + "occuapancies params :" + params);
		OccuapancyMasterResponse occuapancyMasterResponse = null;
		try {
			occuapancyMasterResponse = restTemplate.postForObject(uri.toString(), requestInfoWrapper,
					OccuapancyMasterResponse.class, params);
			log.info("PropertyRepository OccupancyMasterResponse is:" + occuapancyMasterResponse);
			if (occuapancyMasterResponse.getOccuapancyMasters().size() == 0) {
				throw new InvalidInputException(calculationFactorRequest.getRequestInfo());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return occuapancyMasterResponse;

	}

	/**
	 * It will check usage master
	 * 
	 * @param calculationFactor
	 * @param calculationFactorRequest
	 * @param factor
	 * @return
	 */
	public UsageMasterResponse getUsageMaster(CalculationFactor calculationFactor,
			CalculationFactorRequest calculationFactorRequest, String factor) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(calculationFactorRequest.getRequestInfo());
		StringBuilder uri = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		uri.append(propertiesManager.getPropertyHostName());
		uri.append(propertiesManager.getPropertyBasepath());
		uri.append(propertiesManager.getPropertySearch());
		params.put("tenantId", calculationFactor.getTenantId());
		params.put("code", calculationFactor.getFactorCode());
		params.put("factorType", factor);

		log.info("PropertyRepository USAGE uri is:" + uri + "\n PropertyRepository USAGE is:" + requestInfoWrapper
				+ "USAGE params :" + params);

		UsageMasterResponse usageMasterResponse = null;
		try {
			usageMasterResponse = restTemplate.postForObject(uri.toString(), requestInfoWrapper,
					UsageMasterResponse.class, params);
			log.info("PropertyRepository UsageMasterResponse is:" + usageMasterResponse);
			if (usageMasterResponse.getUsageMasters().size() == 0) {
				throw new InvalidInputException(calculationFactorRequest.getRequestInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return usageMasterResponse;

	}

	/**
	 * It will check structure master
	 * 
	 * @param calculationFactor
	 * @param calculationFactorRequest
	 * @param factor
	 * @return
	 */
	public StructureClassResponse getStructure(CalculationFactor calculationFactor,
			CalculationFactorRequest calculationFactorRequest, String factor) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(calculationFactorRequest.getRequestInfo());
		StringBuilder uri = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		uri.append(propertiesManager.getPropertyHostName());
		uri.append(propertiesManager.getPropertyBasepath());
		uri.append(propertiesManager.getPropertySearch());
		params.put("tenantId", calculationFactor.getTenantId());
		params.put("code", calculationFactor.getFactorCode());
		params.put("factorType", factor);

		log.info("PropertyRepository structureclasses uri is:" + uri + "\n PropertyRepository structureclasses is:"
				+ requestInfoWrapper + "structureclasses params :" + params);

		StructureClassResponse structureClassResponse = null;
		try {
			structureClassResponse = restTemplate.postForObject(uri.toString(), requestInfoWrapper,
					StructureClassResponse.class, params);
			log.info("PropertyRepository StructureClassResponse is:" + structureClassResponse);
			if (structureClassResponse.getStructureClasses().size() == 0) {
				throw new InvalidInputException(calculationFactorRequest.getRequestInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return structureClassResponse;

	}

	/**
	 * It will check usage property type master
	 * 
	 * @param calculationFactor
	 * @param calculationFactorRequest
	 * @param factor
	 * @return
	 */
	public PropertyTypeResponse getPropertyType(CalculationFactor calculationFactor,
			CalculationFactorRequest calculationFactorRequest, String factor) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(calculationFactorRequest.getRequestInfo());
		StringBuilder uri = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		uri.append(propertiesManager.getPropertyHostName());
		uri.append(propertiesManager.getPropertyBasepath());
		uri.append(propertiesManager.getPropertySearch());
		params.put("tenantId", calculationFactor.getTenantId());
		params.put("code", calculationFactor.getFactorCode());
		params.put("factorType", factor);

		log.info("PropertyRepository propertytypes uri is:" + uri + "\n PropertyRepository propertytypes is:"
				+ requestInfoWrapper + "propertytypes params :" + params);

		PropertyTypeResponse propertyTypeResponse = null;
		try {
			propertyTypeResponse = restTemplate.postForObject(uri.toString(), requestInfoWrapper,
					PropertyTypeResponse.class, params);
			log.info("PropertyRepository PropertyTypeResponse is:" + propertyTypeResponse);
			if (propertyTypeResponse.getPropertyTypes().size() == 0) {
				throw new InvalidInputException(calculationFactorRequest.getRequestInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return propertyTypeResponse;

	}

}
