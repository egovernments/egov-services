package org.egov.pgr.domain.service.validator.serviceTypeCreateValidator;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.ServiceType;
import org.egov.pgr.persistence.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ServiceCodeCreateValidator implements ServiceTypeCreateValidator {

    public static final String FIELD = "field";
    public static final String MESSAGE = "message";
    public static final String CODE = "code";
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public boolean canValidate(ServiceType serviceType) {
        return true;
    }


    @Override
    public void checkMandatoryField(ServiceType serviceType) {

        if (serviceType.isTenantIdAbsent()) {
            HashMap<String, String> error = new HashMap<>();
            error.put("code", "servicetype Validator.1");
            error.put("field", "serviceType.tenantId");
            error.put("message", "TenantId Required ");
            throw new PGRMasterException(error);
        }

        if (serviceType.isServiceCodeAbsent()) {
            HashMap<String, String> error = new HashMap<>();
            error.put("code", "servicetype Validator.2");
            error.put("field", "serviceType.serviceCode");
            error.put("message", "ServiceCode Required ");
            throw new PGRMasterException(error);
        }

        if (serviceType.isServiceNameAbsent()) {
            HashMap<String, String> error = new HashMap<>();
            error.put("code", "servicetype Vaalidator.3");
            error.put("field", "serviceType.serviceName");
            error.put("message", "ServiceName Required ");
            throw new PGRMasterException(error);
        }
        
        if (serviceType.isKeywordAbsent()) {
            HashMap<String, String> error = new HashMap<>();
            error.put("code", "servicetype Vaalidator.4");
            error.put("field", "serviceType.keyWord");
            error.put("message", "KeyWord Required ");
            throw new PGRMasterException(error);
        }
        
        if(serviceType.isKeywordValid())
        {
        	 HashMap<String, String> error = new HashMap<>();
             error.put("code", "servicetype Vaalidator.5");
             error.put("field", "serviceType.keyWord");
             error.put("message", "Not a valid Required ");
             throw new PGRMasterException(error);
        	
        }
        

    }

    @Override
    public void validateUniqueCombinations(ServiceType serviceType) {

        List<org.egov.pgr.domain.model.ServiceType> serviceTypeList = serviceTypeRepository.getCodeTenantData(serviceType);
        List<org.egov.pgr.domain.model.ServiceType> getall = serviceTypeRepository.getData(serviceType);

        List<org.egov.pgr.domain.model.ServiceType> categoryTypeList = serviceTypeRepository.getCodeTenantDataFromCategory(serviceType);

        if (categoryTypeList.isEmpty()) {
            HashMap<String, String> error = new HashMap<>();
            error.put(CODE, "ServiceCodeValidator.");
            error.put(FIELD, "serviceTypeConfiguration.category");
            error.put(MESSAGE, "Mapping Not Presentt");
            throw new PGRMasterException(error);
        }

        if (!serviceTypeList.isEmpty()
                && (serviceTypeList.get(0).getServiceCode().equalsIgnoreCase(serviceType.getServiceCode())
                && serviceTypeList.get(0).getTenantId().equalsIgnoreCase(serviceType.getTenantId()))) {
            HashMap<String, String> error = new HashMap<>();
            error.put(CODE, "ServiceCodeValidator.4");
            error.put(FIELD, "serviceTypeConfiguration.serviceCode");
            error.put(MESSAGE, "Data already exists");
            throw new PGRMasterException(error);
        }

        if (!getall.isEmpty()
                && (getall.get(0).getServiceCode().equalsIgnoreCase(serviceType.getServiceCode())
                && getall.get(0).getCategory() == serviceType.getCategory()
                && (getall.get(0).getServiceName().equalsIgnoreCase(serviceType.getServiceName()))
                && getall.get(0).getTenantId().equalsIgnoreCase(serviceType.getTenantId()))) {
            HashMap<String, String> error = new HashMap<>();
            error.put(CODE, "ServiceCodeValidator.5");
            error.put(FIELD, "serviceTypeConfiguration.serviceCode");
            error.put(MESSAGE, "Data already exists");
            throw new PGRMasterException(error);
        }

    }


	@Override
	public void lengthValidate(ServiceType serviceType) {
		
		if(!serviceType.isTenantIdLengthMatch())	
		{
			HashMap<String, String> error = new HashMap<>();
            error.put(CODE, "ServiceCodeValidator.6");
            error.put(FIELD, "ServiceLengthValidator.tenant");
            error.put(MESSAGE, "Tenant Id should be >0 and <=256");
            throw new PGRMasterException(error);
			
		}
		if(!serviceType.isDescriptionLengthMatch())	
		{
			HashMap<String, String> error = new HashMap<>();
            error.put(CODE, "ServiceCodeValidator.6");
            error.put(FIELD, "ServiceLengthValidator.Description");
            error.put(MESSAGE, "Description Length should be >0 and <=250");
            throw new PGRMasterException(error);
			
		}
		if(!serviceType.isCodeLengthMatch())	
		{
			HashMap<String, String> error = new HashMap<>();
            error.put(CODE, "ServiceLengthValidator.7");
            error.put(FIELD, "ServiceLengthValidator.Code");
            error.put(MESSAGE, "Code should be >0 and <=20");
            throw new PGRMasterException(error);
			
		}
		
		if(!serviceType.isnameLengthMatch())	
		{
			HashMap<String, String> error = new HashMap<>();
            error.put(CODE, "ServiceCodeValidator.9");
            error.put(FIELD, "ServiceLengthValidator.name");
            error.put(MESSAGE, "Name should be >0 and <=150");
            throw new PGRMasterException(error);
			
		}
		if(!serviceType.isTypeLengthMatch())	
		{
			HashMap<String, String> error = new HashMap<>();
            error.put(CODE, "ServiceCodeValidator.10");
            error.put(FIELD, "ServiceLengthValidator.type");
            error.put(MESSAGE, "Type should be >0 and <=50");
            throw new PGRMasterException(error);
			
		}
		}
    
}