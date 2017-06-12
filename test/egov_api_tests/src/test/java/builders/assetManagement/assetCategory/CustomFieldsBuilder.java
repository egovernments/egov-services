package builders.assetManagement.assetCategory;

import entities.requests.assetManagement.assetCategory.CustomFields;

public class CustomFieldsBuilder {

    CustomFields customFields = new CustomFields();

    public CustomFieldsBuilder() {
    }

    public CustomFieldsBuilder withName(String name) {
        customFields.setName(name);
        return this;
    }

    public CustomFieldsBuilder withType(String type) {
        customFields.setType(type);
        return this;
    }

    public CustomFieldsBuilder withIsActive(String isActive) {
        customFields.setIsActive(isActive);
        return this;
    }

    public CustomFieldsBuilder withIsMandatory(String isMandatory) {
        customFields.setIsMandatory(isMandatory);
        return this;
    }

    public CustomFieldsBuilder withValues(String values) {
        customFields.setValues(values);
        return this;
    }

    public CustomFieldsBuilder withLocalText(String localText) {
        customFields.setLocalText(localText);
        return this;
    }

    public CustomFieldsBuilder withregExFormate(String regExFormate) {
        customFields.setRegExFormate(regExFormate);
        return this;
    }

    public CustomFields build() {
        return customFields;
    }
}
