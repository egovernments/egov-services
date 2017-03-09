package org.egov.user.web.contract;

import org.egov.user.persistence.entity.Address;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.enums.*;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserRequestTest {

    @Test
    public void test_entity_to_contract_conversion() throws Exception {
        org.egov.user.persistence.entity.User userEntity = getUserEntity();
        UserRequest userRequestContract = new UserRequest(userEntity);

        assertThat(userRequestContract.getId()).isEqualTo(userEntity.getId());
        assertThat(userRequestContract.getUserName()).isEqualTo(userEntity.getUsername());
        assertThat(userRequestContract.getSalutation()).isEqualTo(userEntity.getSalutation());
        assertThat(userRequestContract.getName()).isEqualTo(userEntity.getName());
        assertThat(userRequestContract.getGender()).isEqualTo(userEntity.getGender().toString());
        assertThat(userRequestContract.getMobileNumber()).isEqualTo(userEntity.getMobileNumber());
        assertThat(userRequestContract.getEmailId()).isEqualTo(userEntity.getEmailId());
        assertThat(userRequestContract.getAltContactNumber()).isEqualTo(userEntity.getAltContactNumber());
        assertThat(userRequestContract.getPan()).isEqualTo(userEntity.getPan());
        assertThat(userRequestContract.getAadhaarNumber()).isEqualTo(userEntity.getAadhaarNumber());
        assertThat(userRequestContract.getPermanentAddress()).isEqualTo("house number 1, area/locality/sector, " +
                "street/road/line, landmark, city/town/village 1, post office, sub district, " +
                "district, state, country, PIN: pincode 1");
        assertThat(userRequestContract.getPermanentCity()).isEqualTo("city/town/village 1");
        assertThat(userRequestContract.getPermanentPinCode()).isEqualTo("pincode 1");
        assertThat(userRequestContract.getCorrespondenceAddress()).isEqualTo("house number 2, area/locality/sector, " +
                "street/road/line, landmark, city/town/village 2, post office, sub district, " +
                "district, state, country, PIN: pincode 2");
        assertThat(userRequestContract.getCorrespondenceCity()).isEqualTo("city/town/village 2");
        assertThat(userRequestContract.getCorrespondencePinCode()).isEqualTo("pincode 2");
        assertThat(userRequestContract.getActive()).isEqualTo(userEntity.isActive());
        assertThat(userRequestContract.getDob()).isEqualTo(userEntity.getDob());
        assertThat(userRequestContract.getPwdExpiryDate()).isEqualTo(userEntity.getPwdExpiryDate().toDate());
        assertThat(userRequestContract.getLocale()).isEqualTo(userEntity.getLocale());
        assertThat(userRequestContract.getType()).isEqualTo(userEntity.getType());
        assertThat(userRequestContract.getAccountLocked()).isEqualTo(userEntity.isAccountLocked());
        assertThat(userRequestContract.getFatherOrHusbandName()).isEqualTo(userEntity.getGuardian());
        assertThat(userRequestContract.getSignature()).isEqualTo(userEntity.getSignature());
        assertThat(userRequestContract.getBloodGroup()).isEqualTo(userEntity.getBloodGroup().getValue());
        assertThat(userRequestContract.getPhoto()).isEqualTo(userEntity.getPhoto());
        assertThat(userRequestContract.getIdentificationMark()).isEqualTo(userEntity.getIdentificationMark());
        assertThat(userRequestContract.getRoles().get(0).getName()).isEqualTo("name of the role 1");
        assertThat(userRequestContract.getRoles().get(1).getName()).isEqualTo("name of the role 2");
        assertThat(userRequestContract.getCreatedBy()).isEqualTo(1L);
        assertThat(userRequestContract.getCreatedDate()).isEqualTo(userEntity.getCreatedDate());
        assertThat(userRequestContract.getLastModifiedBy()).isEqualTo(1L);
        assertThat(userRequestContract.getLastModifiedDate()).isEqualTo(userEntity.getLastModifiedDate());
    }

    private org.egov.user.persistence.entity.User getUserEntity() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);
        Date date = calendar.getTime();

        org.egov.user.persistence.entity.User userEntity = org.egov.user.persistence.entity.User.builder()
                .id(1L)
                .username("userName")
                .salutation("salutation")
                .name("name")
                .gender(Gender.FEMALE)
                .mobileNumber("mobileNumber1")
                .emailId("email")
                .altContactNumber("mobileNumber2")
                .pan("pan")
                .aadhaarNumber("aadhaarNumber")
                .address(getAddressList())
                .active(true)
                .dob(date)
                .pwdExpiryDate(date)
                .locale("en_IN")
                .type(UserType.CITIZEN)
                .accountLocked(false)
                .roles(getListOfRoles())
                .guardian("name of relative")
                .guardianRelation(GuardianRelation.Father)
                .signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39")
                .bloodGroup(BloodGroup.A_POSITIVE)
                .photo("3b26fb49-e43d-401b-899a-f8f0a1572de0")
                .identificationMark("identification mark")
                .build();

        userEntity.setCreatedBy(userEntity);
        userEntity.setCreatedDate(date);
        userEntity.setLastModifiedBy(userEntity);
        userEntity.setLastModifiedDate(date);

        return userEntity;
    }

    private List<org.egov.user.persistence.entity.Address> getAddressList() {
        return asList(
                org.egov.user.persistence.entity.Address.builder()
                        .id(1L)
                        .type(AddressType.PERMANENT)
                        .houseNoBldgApt("house number 1")
                        .areaLocalitySector("area/locality/sector")
                        .streetRoadLine("street/road/line")
                        .landmark("landmark")
                        .cityTownVillage("city/town/village 1")
                        .postOffice("post office")
                        .subDistrict("sub district")
                        .district("district")
                        .state("state")
                        .country("country")
                        .pinCode("pincode 1")
                        .build(),


                Address.builder()
                        .id(1L)
                        .type(AddressType.CORRESPONDENCE)
                        .houseNoBldgApt("house number 2")
                        .areaLocalitySector("area/locality/sector")
                        .streetRoadLine("street/road/line")
                        .landmark("landmark")
                        .cityTownVillage("city/town/village 2")
                        .postOffice("post office")
                        .subDistrict("sub district")
                        .district("district")
                        .state("state")
                        .country("country")
                        .pinCode("pincode 2")
                        .build()
        );
    }

    private Set<org.egov.user.persistence.entity.Role> getListOfRoles() {
        org.egov.user.persistence.entity.User user = org.egov.user.persistence.entity.User.builder().id(0L).build();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);

        org.egov.user.persistence.entity.Role role1 = org.egov.user.persistence.entity.Role.builder()
                .id(1L)
                .name("name of the role 1")
                .description("description")
                .build();
        role1.setCreatedBy(user);
        role1.setCreatedDate(calendar.getTime());
        role1.setLastModifiedBy(user);
        role1.setLastModifiedDate(calendar.getTime());

        org.egov.user.persistence.entity.Role role2 = org.egov.user.persistence.entity.Role.builder()
                .id(2L)
                .name("name of the role 2")
                .description("description")
                .build();
        role2.setCreatedBy(user);
        role2.setCreatedDate(calendar.getTime());
        role2.setLastModifiedBy(user);
        role2.setLastModifiedDate(calendar.getTime());

        return new HashSet<Role>(){{
            add(role1);
            add(role2);
        }};
    }
}