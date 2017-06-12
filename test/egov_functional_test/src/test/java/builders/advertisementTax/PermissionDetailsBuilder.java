package builders.advertisementTax;

import entities.advertisementTax.PermissionDetails;

public class PermissionDetailsBuilder {

    PermissionDetails permissionDetails = new PermissionDetails();

    public PermissionDetailsBuilder withAdParticular(String adParticular) {
        permissionDetails.setAdParticular(adParticular);
        return this;
    }

    public PermissionDetailsBuilder withOwner(String owner) {
        permissionDetails.setOwner(owner);
        return this;
    }

    public PermissionDetailsBuilder withAdvertisementDuration(String advertisementDuration) {
        permissionDetails.setAdvertisementDuration(advertisementDuration);
        return this;
    }

    public PermissionDetails build() {
        return permissionDetails;
    }
}
