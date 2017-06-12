package builders.assetManagement;

import entities.assetManagement.CustomFieldsDetails;

public final class CustomFieldsDetailsBuilder {

    CustomFieldsDetails customFieldsDetails = new CustomFieldsDetails();

    public CustomFieldsDetailsBuilder() {
    }

    public CustomFieldsDetailsBuilder withName(String name) {
        customFieldsDetails.setName(name);
        return this;
    }

    public CustomFieldsDetailsBuilder withDataType(String dataType) {
        customFieldsDetails.setDataType(dataType);
        return this;
    }

    public CustomFieldsDetailsBuilder withRegExFormat(String regExFormat) {
        customFieldsDetails.setRegExFormat(regExFormat);
        return this;
    }

    public CustomFieldsDetailsBuilder withValue(String value) {
        customFieldsDetails.setValue(value);
        return this;
    }

    public CustomFieldsDetailsBuilder withLocalText(String localText) {
        customFieldsDetails.setLocalText(localText);
        return this;
    }

    public CustomFieldsDetailsBuilder withIsActive(Boolean isActive) {
        customFieldsDetails.setActive(isActive);
        return this;
    }

    public CustomFieldsDetailsBuilder withMandatory(Boolean mandatory) {
        customFieldsDetails.setMandatory(mandatory);
        return this;
    }

    public CustomFieldsDetails build() {
        return customFieldsDetails;
    }
}
