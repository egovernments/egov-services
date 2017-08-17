package org.egov.pgr.domain.service.validator.valuedefinitionUniqueValidator;

import java.util.HashMap;
import java.util.List;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.ValueDefinition;
import org.egov.pgr.persistence.repository.ValueDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValueDefinitionUniqueValuesValidator implements ValueDefinitionUniqueValidator {

	  public static final String FIELD = "field";
	    public static final String MESSAGE = "message";
	    public static final String CODE = "code";
	    
	    @Autowired
	    private ValueDefinitionRepository valueDefinitionRepository;
	    
	@Override
	public boolean canValidate(ValueDefinition valueDefinition) {
		return true;
	}

	@Override
	public void validateUniqueConstratint(ValueDefinition valueDefinition,String serviceCode,String attributeCode,String tenantid) {
		  List<ValueDefinition> valueList = valueDefinitionRepository.getValueDefinitionTenantData(valueDefinition,serviceCode,attributeCode,tenantid);
	        if (!valueList.isEmpty()
	                && (valueList.get(0).getServiceCode().equalsIgnoreCase(valueDefinition.getServiceCode())
	                && valueList.get(0).getKey().equalsIgnoreCase(valueDefinition.getKey()))
	                && valueList.get(0).getAttributeCode().equalsIgnoreCase(valueDefinition.getAttributeCode())) {
	            HashMap<String, String> error = new HashMap<>();
	            error.put(CODE, "ValueDefinition.1");
	            error.put(FIELD, "ValueDefinition.ValueCode");
	            error.put(MESSAGE, "Data Already present");
	            throw new PGRMasterException(error);
	        }

	       	
	}

	

}