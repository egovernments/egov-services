package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.user.persistence.entity.Address;
import org.egov.user.persistence.entity.enums.AddressType;
import org.egov.user.persistence.entity.enums.GuardianRelation;
import org.egov.user.persistence.entity.enums.UserType;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public User(org.egov.user.persistence.entity.User user) {

        this.id = user.getId();
        this.userName = user.getUsername();
        this.salutation = user.getSalutation();
        this.name = user.getName();
        this.gender = user.getGender() != null ? user.getGender().toString() : null;
        this.mobileNumber = user.getMobileNumber();
        this.emailId = user.getEmailId();
        this.altContactNumber = user.getAltContactNumber();
        this.pan = user.getPan();
        this.aadhaarNumber = user.getAadhaarNumber();
        this.active = user.isActive();
        this.dob = user.getDob();
        this.pwdExpiryDate = user.getPwdExpiryDate() != null ? user.getPwdExpiryDate().toDate() : null;
        this.locale = user.getLocale();
        this.type = user.getType();
        this.accountLocked = user.isAccountLocked();
        this.signature = user.getSignature();
        this.bloodGroup = user.getBloodGroup() != null ? user.getBloodGroup().getValue() : null;
        this.photo = user.getPhoto();
        this.identificationMark = user.getIdentificationMark();
        this.createdBy = user.getCreatedBy() != null ? user.getCreatedBy().getId() : null;
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy() != null ? user.getLastModifiedBy().getId() : null;
        this.lastModifiedDate = user.getLastModifiedDate();

        this.roles = convertRoleEntitiesToContract(user.getRoles());

        if (isGuardianRelationFatherOrHusband(user.getGuardianRelation())) {
            this.fatherOrHusbandName = user.getGuardian();
        }

        convertAndSetAddressesToContract(user.getAddress());
    }

    @JsonProperty("id")
    private Long id;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("salutation")
    private String salutation;

    @JsonProperty("name")
    private String name;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("emailId")
    private String emailId;

    @JsonProperty("altContactNumber")
    private String altContactNumber;

    @JsonProperty("pan")
    private String pan;

    @JsonProperty("aadhaarNumber")
    private String aadhaarNumber;

    @JsonProperty("permanentAddress")
    private String permanentAddress;

    @JsonProperty("permanentCity")
    private String permanentCity;

    @JsonProperty("permanentPinCode")
    private String permanentPinCode;

    @JsonProperty("correspondenceAddress")
    private String correspondenceAddress;

    @JsonProperty("correspondenceCity")
    private String correspondenceCity;

    @JsonProperty("correspondencePinCode")
    private String correspondencePinCode;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("dob")
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date dob;

    @JsonProperty("pwdExpiryDate")
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date pwdExpiryDate;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("type")
    private UserType type;

    @JsonProperty("accountLocked")
    private Boolean accountLocked;

    @JsonProperty("roles")
    private List<Role> roles;

    @JsonProperty("fatherOrHusbandName")
    private String fatherOrHusbandName;

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("bloodGroup")
    private String bloodGroup;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("identificationMark")
    private String identificationMark;

    @JsonProperty("createdBy")
    private Long createdBy;

    @JsonProperty("createdDate")
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date createdDate;

    @JsonProperty("lastModifiedBy")
    private Long lastModifiedBy;

    @JsonProperty("lastModifiedDate")
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date lastModifiedDate;

    private void convertAndSetAddressesToContract(List<Address> addresses) {
        if (addresses == null) return;

        Optional<org.egov.user.persistence.entity.Address> permanentAddress =
                getAddressOfType(addresses, AddressType.PERMANENT);
        Optional<Address> correspondenceAddress = getAddressOfType(addresses, AddressType.CORRESPONDENCE);
        permanentAddress.ifPresent(address -> {
            this.permanentAddress = convertAddressEntityToString(address);
            this.permanentCity = address.getCityTownVillage();
            this.permanentPinCode = address.getPinCode();
        });

        correspondenceAddress.ifPresent(address -> {
            this.setCorrespondenceAddress(convertAddressEntityToString(address));
            this.setCorrespondenceCity(address.getCityTownVillage());
            this.setCorrespondencePinCode(address.getPinCode());
        });
    }

    private Optional<Address> getAddressOfType(List<Address> addressList, AddressType addressType) {
        return addressList.stream()
                .filter(address -> address.getType().equals(addressType))
                .findFirst();
    }

    private String convertAddressEntityToString(org.egov.user.persistence.entity.Address address) {
        final String ADDRESS_SEPARATOR = "%s, ";
        final String PIN_CODE_FORMATTER = "PIN: %s";

        Formatter formatter = new Formatter();
        if (isNotBlank(address.getHouseNoBldgApt()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getHouseNoBldgApt()));
        if (isNotBlank(address.getAreaLocalitySector()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getAreaLocalitySector()));
        if (isNotBlank(address.getStreetRoadLine()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getStreetRoadLine()));
        if (isNotBlank(address.getLandmark()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getLandmark()));
        if (isNotBlank(address.getCityTownVillage()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getCityTownVillage()));
        if (isNotBlank(address.getPostOffice()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getPostOffice()));
        if (isNotBlank(address.getSubDistrict()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getSubDistrict()));
        if (isNotBlank(address.getDistrict()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getDistrict()));
        if (isNotBlank(address.getState()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getState()));
        if (isNotBlank(address.getCountry()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getCountry()));
        if (isNotBlank(address.getPinCode()))
            formatter.format(PIN_CODE_FORMATTER, trim(address.getPinCode()));

        return formatter.toString();
    }

    private List<Role> convertRoleEntitiesToContract(Set<org.egov.user.persistence.entity.Role> roleEntities) {
        if(roleEntities == null) return new ArrayList<>();
        return roleEntities.stream().map(Role::new).collect(Collectors.toList());
    }

    private boolean isGuardianRelationFatherOrHusband(GuardianRelation guardianRelation) {
        return (guardianRelation != null &&
                (guardianRelation.equals(GuardianRelation.Father)
                        || guardianRelation.equals(GuardianRelation.Husband))
        );
    }
}
