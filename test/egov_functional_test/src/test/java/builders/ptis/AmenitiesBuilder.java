package builders.ptis;

import entities.ptis.Amenities;

public class AmenitiesBuilder {

    Amenities amenities = new Amenities();

    public AmenitiesBuilder() {
        amenities.setLift(true);
        amenities.setToilets(true);
        amenities.setAttachedBathroom(true);
        amenities.setElectricity(true);
        amenities.setWaterHarvesting(true);
        amenities.setWaterTap(true);
        amenities.setCableConnection(true);
    }

    public AmenitiesBuilder hasLift(Boolean lift) {
        amenities.setLift(lift);
        return this;
    }

    public AmenitiesBuilder hasToilets(Boolean toilets) {
        amenities.setToilets(toilets);
        return this;
    }

    public AmenitiesBuilder has(Boolean lift) {
        amenities.setLift(lift);
        return this;
    }

    public AmenitiesBuilder hasAttachedBathroom(Boolean attachedBathroom) {
        amenities.setAttachedBathroom(attachedBathroom);
        return this;
    }

    public AmenitiesBuilder hasElectricity(Boolean electricity) {
        amenities.setElectricity(electricity);
        return this;
    }

    public AmenitiesBuilder hasWaterHarvesting(Boolean waterHarvesting) {
        amenities.setWaterHarvesting(waterHarvesting);
        return this;
    }

    public AmenitiesBuilder hasWaterTap(Boolean waterTap) {
        amenities.setWaterTap(waterTap);
        return this;
    }

    public AmenitiesBuilder hasCableConnection(Boolean cableConnection) {
        amenities.setCableConnection(cableConnection);
        return this;
    }

    public Amenities build() {
        return amenities;
    }
}
