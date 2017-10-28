package org.egov.lcms.util;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.IdGenerationResponse;
import org.egov.lcms.models.SearchTenantResponse;
import org.egov.lcms.repository.IdGenerationRepository;
import org.egov.lcms.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Prasad
 *
 */
@Service
public class UniqueCodeGeneration {
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired
	IdGenerationRepository idGenerationRepository;
	
	@Autowired
	PropertiesManager propertiesManager;
	
	
	
	
	/**
	 * This will generate the unique code based on the given parameters
	 * @param tenantId
	 * @param requestInfo
	 * @param ulbFormat
	 * @param ulbName
	 * @return {@link String} Unique code sequence
	 * @throws Exception
	 */
	public String  getUniqueCode(String tenantId,RequestInfo requestInfo,String ulbFormat,String ulbName,Boolean isCaseRef,String caseCode) throws Exception{
		String response = tenantRepository.getTenantRepository(tenantId, requestInfo);
		String SequenceNo ="";
		
		SearchTenantResponse searchTenantResponse =null;
		String ulbCode="";
		
		if (response != null) {
			ObjectMapper mapper = new ObjectMapper();
			searchTenantResponse = mapper.readValue(response, SearchTenantResponse.class);
		}

		ulbCode = searchTenantResponse.getTenant().get(0).getCode();
		if ( ulbCode!=null && !ulbCode.isEmpty()){
			IdGenerationResponse idResponse;
			
			idResponse = idGenerationRepository.getIdGeneration(tenantId, requestInfo,ulbFormat,
					ulbName);
			if ( idResponse!=null && idResponse.getIdResponses()!=null && idResponse.getIdResponses().size()>0) {
				idResponse.getIdResponses().get(0).getId();
				SequenceNo=idResponse.getIdResponses().get(0).getId();
			}
			
		}
		
		if ( isCaseRef ){
		return ulbCode+SequenceNo;
		}
		else{
			return ulbCode+caseCode+SequenceNo; 
		}
		
	}
	

}
