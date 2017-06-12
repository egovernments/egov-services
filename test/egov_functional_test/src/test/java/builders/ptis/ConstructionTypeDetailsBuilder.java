package builders.ptis;

import entities.ptis.ConstructionTypeDetails;

public class ConstructionTypeDetailsBuilder {

    ConstructionTypeDetails constructionTypeDetails = new ConstructionTypeDetails();

    public ConstructionTypeDetailsBuilder withFloorType(String floorType) {
        constructionTypeDetails.setFloorType(floorType);
        return this;
    }

    public ConstructionTypeDetailsBuilder withRoofType(String roofType) {
        constructionTypeDetails.setRoofType(roofType);
        return this;
    }

    public ConstructionTypeDetailsBuilder withWoodType(String woodType) {
        constructionTypeDetails.setWoodType(woodType);
        return this;
    }

    public ConstructionTypeDetailsBuilder withWallType(String wallType) {
        constructionTypeDetails.setWallType(wallType);
        return this;
    }

    public ConstructionTypeDetails build() {
        return constructionTypeDetails;
    }
}
