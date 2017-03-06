package org.egov.user.web.controller;

import org.egov.user.domain.service.UserService;
import org.egov.user.persistence.entity.Address;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.SecureUser;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.AddressType;
import org.egov.user.persistence.entity.enums.GuardianRelation;
import org.egov.user.web.contract.Error;
import org.egov.user.web.contract.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

@RestController
public class UserController {

    private UserService userService;
    private TokenStore tokenStore;

    public UserController(UserService userService, TokenStore tokenStore) {
        this.userService = userService;
        this.tokenStore = tokenStore;
    }

    @RequestMapping("/details")
    public ResponseEntity<?> getUser(@RequestParam(value = "access_token") String accessToken) {
        OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
        if (authentication != null)
            return new ResponseEntity<User>(((SecureUser) authentication.getPrincipal()).getUser(), HttpStatus.OK);
        else {
            ErrorResponse errRes = populateErrors();
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/_search")
    public ResponseEntity<?> get(@RequestBody GetUserByIdRequest request) {
        if(request.getId().size() > 0) {
            List<User> userEntities = userService.getUsersById(request.getId());
            List<org.egov.user.web.contract.User> userContracts = userEntities.stream()
                    .map(this::convertEntityToContract)
                    .collect(Collectors.toList());
            ResponseInfo responseInfo = ResponseInfo.builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .build();
            GetUserByIdResponse getUserByIdResponse = new GetUserByIdResponse(responseInfo, userContracts);
            return new ResponseEntity<>(getUserByIdResponse, HttpStatus.OK);
        } else {
            Error error = Error.builder().code(400).message("User ids cannot be empty").build();
            ResponseInfo responseInfo = ResponseInfo.builder().status(HttpStatus.BAD_REQUEST.toString()).build();
            ErrorResponse errorResponse = ErrorResponse.builder().error(error).responseInfo(responseInfo).build();

            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }

    }

    private org.egov.user.web.contract.User convertEntityToContract(User userEntity) {
        List<Address> addresses = userEntity.getAddress();
        Optional<Address> permanentAddress = getAddressOfType(addresses, AddressType.PERMANENT);
        Optional<Address> correspondenceAddress = getAddressOfType(addresses, AddressType.CORRESPONDENCE);

        org.egov.user.web.contract.User userContract = org.egov.user.web.contract.User.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUsername())
                .salutation(userEntity.getSalutation())
                .name(userEntity.getName())
                .gender(userEntity.getGender()!=null?userEntity.getGender().toString():"")
                .mobileNumber(userEntity.getMobileNumber())
                .emailId(userEntity.getEmailId())
                .altContactNumber(userEntity.getAltContactNumber())
                .pan(userEntity.getPan())
                .aadhaarNumber(userEntity.getAadhaarNumber())
                .active(userEntity.isActive())
                .dob(userEntity.getDob())
                .pwdExpiryDate(userEntity.getPwdExpiryDate().toDate())
                .locale(userEntity.getLocale())
                .type(userEntity.getType())
                .accountLocked(false)
                .roles(convertEntityToContract(userEntity.getRoles()))
                .signature(userEntity.getSignature())
                .bloodGroup(userEntity.getBloodGroup()!=null?userEntity.getBloodGroup().getValue():"")
                .photo(userEntity.getPhoto())
                .identificationMark(userEntity.getIdentificationMark())
                .createdBy(userEntity.getId())
                .createdDate(userEntity.getCreatedDate())
                .lastModifiedBy(userEntity.getId())
                .lastModifiedDate(userEntity.getLastModifiedDate())
                .build();

        permanentAddress.ifPresent(address -> {
            userContract.setPermanentAddress(convertEntityToContract(address));
            userContract.setPermanentCity(address.getCityTownVillage());
            userContract.setPermanentPinCode(address.getPinCode());
        });

        correspondenceAddress.ifPresent(address -> {
            userContract.setCorrespondenceAddress(convertEntityToContract(address));
            userContract.setCorrespondenceCity(address.getCityTownVillage());
            userContract.setCorrespondencePinCode(address.getPinCode());
        });

        if (userEntity.getGuardianRelation() != null && isGuardianRelationFatherOrHusband(userEntity.getGuardianRelation())) {
            userContract.setFatherOrHusbandName(userEntity.getGuardian());
        }

        return userContract;
    }

    private Set<org.egov.user.web.contract.Role> convertEntityToContract(Set<Role> roleSet) {
        return roleSet.stream().map(
                roleEntity -> {
                    org.egov.user.web.contract.Role roleContract = org.egov.user.web.contract.Role.builder()
                            .id(roleEntity.getId())
                            .name(roleEntity.getName())
                            .description(roleEntity.getDescription())
                            .build();
                    roleContract.setCreatedBy(roleEntity.getCreatedBy().getId());
                    roleContract.setCreatedDate(roleEntity.getCreatedDate());
                    roleContract.setLastModifiedBy(roleEntity.getLastModifiedBy().getId());
                    roleContract.setLastModifiedDate(roleEntity.getLastModifiedDate());
                    return roleContract;
                }
        ).collect(Collectors.toSet());
    }

    private String convertEntityToContract(Address address) {
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

    private boolean isGuardianRelationFatherOrHusband(GuardianRelation guardianRelation) {
        return (guardianRelation.equals(GuardianRelation.Father) || guardianRelation.equals(GuardianRelation.Husband));
    }

    private Optional<Address> getAddressOfType(List<Address> addressList, AddressType addressType) {
        return addressList.stream()
                .filter(address -> address.getType().equals(addressType))
                .findFirst();
    }

    private ErrorResponse populateErrors() {
        ResponseInfo responseInfo = ResponseInfo.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .apiId("")
                .build();

        Error error = Error.builder()
                .code(1)
                .description("Error while fetching user details")
                .build();

        return ErrorResponse.builder()
                .responseInfo(responseInfo)
                .error(error)
                .build();
    }
}
