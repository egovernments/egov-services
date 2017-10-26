package org.egov.inv.domain.service.validator;


import org.egov.common.contract.response.ErrorField;
import org.egov.inv.domain.MaterialException;
import org.egov.inv.domain.model.Material;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialRequestValidator {

    private CommonMasterValidator commonMasterValidator;

    public MaterialRequestValidator(CommonMasterValidator commonMasterValidator) {
        this.commonMasterValidator = commonMasterValidator;
    }

    public void validate(Material material) {
        List<ErrorField> errorFields = getError(material);
        if (!errorFields.isEmpty()) {
            throw new MaterialException(errorFields);
        }
    }

    private List<ErrorField> getError(Material material) {
        List<ErrorField> errorFields = new ArrayList<>();

        validation(material, errorFields);

        return errorFields;
    }

    private void validation(Material material, List<ErrorField> errorFields) {
        commonMasterValidator.addLengthValidationError("Material code", material.getCode(), 5, 50, errorFields);
        commonMasterValidator.addNameValidationErrors(material.getName(), errorFields);
        commonMasterValidator.validatePattern("Material code", material.getCode(), "^[A-Za-z0-9]+$", errorFields);
    }

}
