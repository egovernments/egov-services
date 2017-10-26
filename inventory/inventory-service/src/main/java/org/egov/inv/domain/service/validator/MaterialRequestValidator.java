package org.egov.inv.domain.service.validator;


import org.egov.common.contract.response.ErrorField;
import org.egov.inv.domain.MaterialException;
import org.egov.inv.domain.model.Material;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.egov.inv.constants.InvConstants.*;

@Service
public class MaterialRequestValidator {

    public static final String MATERIAL_CODE = "Material code";
    public static final String MATERIAL_NAME = "Material Name";
    public static final String MATERIAL_DESCRIPTION = "Material Description";
    public static final String OLD_CODE = "Old Code";
    public static final String MATERIAL_TYPE = "Material Type";
    public static final String BASE_UOM = "Base Uom";
    public static final String MINIMUM_QUANTITY = "Minimum Quantity";
    public static final String MAXIMUM_QUANTITY = "Maximum Quantity";
    public static final String STOCK_UOM = "Stock Uom";
    public static final String MATERIAL_CLASS = "Material Class";
    public static final String REORDER_LEVEL = "Reorder Level";
    public static final String REORDER_QUANTITY = "Reorder Quantity";
    public static final String MATERIAL_CONTROL_TYPE = "Material Control Type";
    public static final String MODEL = "Model";
    public static final String MANUFACTURE_PART_NO = "Manufacture Part No";
    public static final String TECHNICAL_SPECS = "Technical Specs";
    public static final String TERMS_OF_DELIVERY = "Terms Of Delivery";
    public static final String INVENTORY_TYPE = "Inventory Type";

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
        commonMasterValidator.validatePattern(MATERIAL_CODE, material.getCode(), PATTERN_ALPHANUMERIC, errorFields);
        commonMasterValidator.addNotNullForStringValidationErrors(MATERIAL_NAME, material.getName(), errorFields);
        commonMasterValidator.validatePattern(MATERIAL_NAME, material.getName(), PATTERN_CHARACTER, errorFields);
        commonMasterValidator.addNotNullForStringValidationErrors(MATERIAL_DESCRIPTION, material.getDescription(), errorFields);
        commonMasterValidator.addLengthValidationError(MATERIAL_DESCRIPTION, material.getDescription(), 0, 1024, errorFields);
        commonMasterValidator.validatePattern(MATERIAL_DESCRIPTION, material.getDescription(), PATTERN_ALPHANUMERIC_SPACE, errorFields);
        commonMasterValidator.addLengthValidationError(OLD_CODE, material.getOldCode(), 0, 50, errorFields);
        commonMasterValidator.validatePattern(OLD_CODE, material.getOldCode(), PATTERN_ALPHANUMERIC, errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(MATERIAL_TYPE, material.getMaterialType(), errorFields);
        commonMasterValidator.validatePattern(MATERIAL_TYPE, material.getMaterialType().getName(), PATTERN_ALPHANUMERIC, errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(BASE_UOM, material.getBaseUom(), errorFields);
        commonMasterValidator.validatePattern(BASE_UOM, material.getBaseUom().getName(), PATTERN_CHARACTER, errorFields);
        commonMasterValidator.validatePattern(BASE_UOM, material.getBaseUom().getName(), PATTERN_ALPHANUMERIC, errorFields);
        commonMasterValidator.validatePattern(INVENTORY_TYPE, material.getInventoryType().toString(), PATTERN_CHARACTER, errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(MINIMUM_QUANTITY, material.getMinQuality(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(MAXIMUM_QUANTITY, material.getMaxQuality(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(STOCK_UOM, material.getStockingUom(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(MATERIAL_CLASS, material.getMaterialClass(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(REORDER_LEVEL, material.getReorderLevel(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(REORDER_QUANTITY, material.getReorderQuantity(), errorFields);
        commonMasterValidator.addNotNullForObjectValidationErrors(MATERIAL_CONTROL_TYPE, material.getMaterialControlType(), errorFields);
        commonMasterValidator.validatePattern(MODEL, material.getModel(), PATTERN_ALPHANUMERIC, errorFields);
        commonMasterValidator.validatePattern(MANUFACTURE_PART_NO, material.getManufacturePartNo(), PATTERN_ALPHANUMERIC, errorFields);
        commonMasterValidator.validatePattern(TECHNICAL_SPECS, material.getTechnicalSpecs(), PATTERN_ALPHANUMERIC, errorFields);
        commonMasterValidator.validatePattern(TERMS_OF_DELIVERY, material.getTermsOfDelivery(), PATTERN_ALPHANUMERIC, errorFields);
    }

}
