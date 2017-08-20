package org.egov.pgr.domain.service.validatorAttributedefinitionUniqueValidator;

import java.util.HashMap;
import java.util.List;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.AttributeDefinition;
import org.egov.pgr.persistence.repository.AttributeDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeDefinitionUniqueValuesValidator implements AttributeDefinitionUniqueValidator {

	  public static final String FIELD = "field";
	    public static final String MESSAGE = "message";
	    public static final String CODE = "code";
	    
	    @Autowired
	    private AttributeDefinitionRepository attributeDefinitionRepository;
	    
	@Override
	public boolean canValidate(AttributeDefinition attributeDefinition) {
		return true;
	}

	@Override
	public void validateUniqueConstratint(AttributeDefinition attributeDefinition) {
		  List<AttributeDefinition> attributeList = attributeDefinitionRepository.getServicecodeCodeTenantData(attributeDefinition);


	        if (!attributeList.isEmpty()
	                && (attributeList.get(0).getServiceCode().equalsIgnoreCase(attributeDefinition.getServiceCode())
	                && attributeList.get(0).getTenantId().equalsIgnoreCase(attributeDefinition.getTenantId()))
	                && attributeList.get(0).getCode().equalsIgnoreCase(attributeDefinition.getCode())) {
	            HashMap<String, String> error = new HashMap<>();
	            error.put(CODE, "AttributeDefinition.1");
	            error.put(FIELD, "AttributeDefinition.AttribCode");
	            error.put(MESSAGE, "Data Already Exist");
	            throw new PGRMasterException(error);
	        }

	       	
	}

	

}