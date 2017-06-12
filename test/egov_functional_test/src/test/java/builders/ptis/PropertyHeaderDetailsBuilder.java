package builders.ptis;

import entities.ptis.PropertyHeaderDetails;

public class PropertyHeaderDetailsBuilder {

    PropertyHeaderDetails propertyHeaderDetails = new PropertyHeaderDetails();

    public PropertyHeaderDetailsBuilder() {
        propertyHeaderDetails.setPropertyType("Residential");
        propertyHeaderDetails.setCategoryOfOwnership("Private");
    }

    public PropertyHeaderDetailsBuilder withPropertyType(String propertyType) {
        propertyHeaderDetails.setPropertyType(propertyType);
        return this;
    }

    public PropertyHeaderDetailsBuilder withCategoryOfOwnership(String categoryOfOwnership) {
        propertyHeaderDetails.setCategoryOfOwnership(categoryOfOwnership);
        return this;
    }

    public PropertyHeaderDetails build() {
        return propertyHeaderDetails;
    }
}
