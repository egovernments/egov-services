package org.egov.user.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.egov.user.domain.model.enums.AddressType;

@Getter
@Builder
public class Address {

    private Long id;

    private String houseNoBldgApt;

    private String streetRoadLine;

    private String landmark;

    private String areaLocalitySector;

    private String cityTownVillage;

    private String district;

    private String subDistrict;

    private String postOffice;

    private String state;

    private String country;

    private String pinCode;

    private AddressType type;
}
