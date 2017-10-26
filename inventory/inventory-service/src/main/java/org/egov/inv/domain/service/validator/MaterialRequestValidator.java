package org.egov.inv.domain.service.validator;


import org.egov.common.contract.response.ErrorField;
import org.egov.inv.domain.MaterialException;
import org.egov.inv.domain.model.Material;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialRequestValidator {

    public static final String MATERIAL_CODE = "Material code";
    public static final String MATERIAL_NAME = "Material Name";
    public static final String A_ZA_Z0_9_$ = "^[A-Za-z0-9]+$";
    public static final String MATERIAL_DESCRIPTION = "Material Description";

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
        commonMasterValidator.addLengthValidationError(MATERIAL_CODE, material.getCode(), 5, 50, errorFields);
        commonMasterValidator.addNotNullForStringValidationErrors(MATERIAL_NAME, material.getName(), errorFields);
        commonMasterValidator.validatePattern(MATERIAL_CODE, material.getCode(), A_ZA_Z0_9_$, errorFields);
        commonMasterValidator.addNotNullForStringValidationErrors(MATERIAL_DESCRIPTION, material.getDescription(), errorFields);
        commonMasterValidator.addLengthValidationError("Material Decription", material.getDescription(), 0, 1024, errorFields);
        commonMasterValidator.validatePattern(MATERIAL_DESCRIPTION, material.getDescription(), A_ZA_Z0_9_$, errorFields);
        commonMasterValidator.addLengthValidationError("Old Code", material.getOldCode(), 0, 50, errorFields);
        commonMasterValidator.validatePattern("Old Code", material.getOldCode(), A_ZA_Z0_9_$, errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Material Type", material.getMaterialType(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Base Uom", material.getBaseUom(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Minimum Quantity", material.getMinQuality(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Maximum Quantity", material.getMaxQuality(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Stock Uom", material.getStockingUom(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Material Class", material.getMaterialClass(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Reorder Level", material.getReorderLevel(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Reorder Quantity", material.getReorderQuantity(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors("Material Control Type", material.getMaterialControlType(), errorFields);
        commonMasterValidator.validatePattern("Model", material.getModel(), A_ZA_Z0_9_$, errorFields);
        commonMasterValidator.validatePattern("Manufacture Part No", material.getManufacturePartNo(), A_ZA_Z0_9_$, errorFields);
        commonMasterValidator.validatePattern("Technical Specs", material.getTechnicalSpecs(), A_ZA_Z0_9_$, errorFields);
        commonMasterValidator.validatePattern("Terms Of Delivery", material.getTermsOfDelivery(), A_ZA_Z0_9_$, errorFields);


    }

}
