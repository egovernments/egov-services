package org.egov.pgr.domain.service.validator.AttributedefinitionValidator;

import java.util.HashMap;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.AttributeDefinition;
import org.springframework.stereotype.Service;

@Service
public class AttributeDefinitionCreateValidator implements AttributeDefinitionValidator {


	@Override
	public boolean canValidate(AttributeDefinition attributeDefinition) {
		return true;
	}

	@Override
	public void validatingLength(AttributeDefinition attributeDefinition) {
		
			
			if(!attributeDefinition.isTenantIdLengthMatch())	
			{
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "AttributeDefinition Vaalidator.1");
			error.put("field", "AttributeDefinition.tenant");
			error.put("message", "tenant Id should be Min: 1 Characters. Max: 256 Characters");
			throw new PGRMasterException(error);
				
			}
			
			if(!attributeDefinition.isCodeLengthMatch())	
			{
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "AttributeDefinition Vaalidator.2");
			error.put("field", "AttributeDefinition.code");
			error.put("message", "code should be Min: 1 Characters. Max: 50 Characters");
			throw new PGRMasterException(error);
				
			}
			
			if(!attributeDefinition.isDatatypedescriptionLengthMatch())	
			{
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "AttributeDefinition Vaalidator.3");
			error.put("field", "AttributeDefinition.dataTypeDescription");
			error.put("message", "dataTypeDescription  should be Min: 1 Characters. Max: 200 Characters");
			throw new PGRMasterException(error);
				
			}
			
			if(!attributeDefinition.isDataTypeLengthMatch())	
			{
				HashMap<String, String> error = new HashMap<>();
				error.put("code", "AttributeDefinition Vaalidator.4");
				error.put("field", "AttributeDefinition.dataType");
				error.put("message", "dataType should be Min: 1 Characters. Max: 100 Characters");
				throw new PGRMasterException(error);
				
			}	
			
			if(!attributeDefinition.isServicecodeLengthMatch())	
			{
				HashMap<String, String> error = new HashMap<>();
				error.put("code", "AttributeDefinition Vaalidator.5");
				error.put("field", "AttributeDefinition.serviceCode");
				error.put("message", "serviceCode should be Min: 1 Characters. Max: 256 Characters");
				throw new PGRMasterException(error);
				
			}
			
			if(!attributeDefinition.isUrlLengthMatch())	
			{
				HashMap<String, String> error = new HashMap<>();
				error.put("code", "AttributeDefinition Vaalidator.6");
				error.put("field", "AttributeDefinition.url");
				error.put("message", "url should be Min: 1 Characters. Max: 300 Characters");
				throw new PGRMasterException(error);
				
			}
			
			if(!attributeDefinition.isGroupCodeLengthMatch())	
			{
				HashMap<String, String> error = new HashMap<>();
				error.put("code", "AttributeDefinition Vaalidator.7");
				error.put("field", "AttributeDefinition.groupCode");
				error.put("message", "groupCode should be Min: 1 Characters. Max: 300 Characters");
				throw new PGRMasterException(error);
				
			}
			
			if(!attributeDefinition.isDescriptionLengthMatch())	
			{
				HashMap<String, String> error = new HashMap<>();
				error.put("code", "AttributeDefinition Vaalidator.8");
				error.put("field", "AttributeDefinition.description");
				error.put("message", "description should be Min: 1 Characters. Max: 300 Characters");
				throw new PGRMasterException(error);
				
			}
			
	}
	
	@Override
	public void checkMandatoryField(AttributeDefinition attributeDefinition) {

		if (attributeDefinition.isTenantAbsent()) {
			HashMap<String, String> error = new HashMap<>();

			error.put("code", "AttributeDefinition Vaalidator.9");
			error.put("field", "AttributeDefinition.tanantId");
			error.put("message", "tanantId Required");

			throw new PGRMasterException(error);
		}

		if (attributeDefinition.isAttributCodeAbsent()) {
			HashMap<String, String> error = new HashMap<>();

			error.put("code", "AttributeDefinition Vaalidator.10");
			error.put("field", "AttributeDefinition.code");
			error.put("message", "AttributeCode Required ");

			throw new PGRMasterException(error);
		}
		
		if (attributeDefinition.isDatatypeAbsent()) {
			HashMap<String, String> error = new HashMap<>();

			error.put("code", " AttributeDefinition Vaalidator.11");
			error.put("field", "AttributeDefinition.dataType");
			error.put("message", "DataType Required ");

			throw new PGRMasterException(error);
		}
		
		if (attributeDefinition.isServiceCodeAbsent()) {
			HashMap<String, String> error = new HashMap<>();

			error.put("code", "  AttributeDefinition Vaalidator.12");
			error.put("field", "AttributeDefinition .servicecode");
			error.put("message", "Servicecode Required ");

			throw new PGRMasterException(error);
		}
	}
	
	@Override
	public void validateDataType(AttributeDefinition attributeDefinition) {
		
	        if (attributeDefinition.isValueList())
	               {
	            HashMap<String, String> error = new HashMap<>();
	            error.put("code", "ValueDefinition.13");
	            error.put("field", "ValueDefinition.DataType,key Pair");
	            error.put("message", "Value List Should be empty for this DataType ");
	            throw new PGRMasterException(error);
	        }

	       	
	}

}