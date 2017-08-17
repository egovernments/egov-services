package org.egov.pgr.domain.service.validator.servicetypevalidators;

import org.egov.common.contract.response.ErrorField;
import org.egov.pgr.domain.exception.InvalidServiceTypeException;
import org.egov.pgr.domain.model.ServiceType;
import org.egov.pgr.persistence.dto.ServiceCategory;
import org.egov.pgr.persistence.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceTypeCategoryValidator implements ServiceTypeValidator {

    private static final String CATEGORY_MESSAGE = "Invalid Service Category";
    private static final String CATEGORY_CODE = "PGR.INVALID_SERVICE_CATEGORY";
    private static final String CATEGORY_FIELD_NAME = "serviceType.category";

    private ServiceCategoryRepository serviceCategoryRepository;

    public ServiceTypeCategoryValidator(ServiceCategoryRepository serviceCategoryRepository) {
        this.serviceCategoryRepository = serviceCategoryRepository;
    }

    @Override
    public boolean canValidate(ServiceType serviceType) {
        return serviceType.isUpdate();
    }

    @Override
    public void validate(ServiceType serviceType) {

        List<ServiceCategory>  categoryList = serviceCategoryRepository.
                findByTenantIdAndId(serviceType.getTenantId(),serviceType.getCategory());

        if(categoryList.isEmpty())
            throw new InvalidServiceTypeException(getErrorFields());

        if(!categoryList.isEmpty()){
            ServiceCategory category = categoryList.get(0);

            if(!category.getId().equals(serviceType.getCategory()))
                throw new InvalidServiceTypeException(getErrorFields());
        }
    }

    private List<ErrorField> getErrorFields(){

        List<ErrorField> errorFields = new ArrayList<>();

        final ErrorField error = ErrorField.builder()
                .code(CATEGORY_CODE)
                .field(CATEGORY_FIELD_NAME)
                .message(CATEGORY_MESSAGE)
                .build();

        errorFields.add(error);

        return  errorFields;
    }
}
