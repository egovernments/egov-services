package org.egov.user.persistence.entity;

import org.egov.user.domain.model.enums.*;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void test_entity_should_convert_itself_to_domain() throws Exception {
        User userEntity = getUserEntity();

        org.egov.user.domain.model.User userModel = userEntity.toDomain();

        assertThat(userModel.getId()).isEqualTo(1L);
        assertThat(userModel.getUsername()).isEqualTo(userModel.getUsername());
        assertThat(userModel.getSalutation()).isEqualTo(userModel.getSalutation());
        assertThat(userModel.getName()).isEqualTo(userModel.getName());
        assertThat(userModel.getGender()).isEqualTo(userModel.getGender());
        assertThat(userModel.getMobileNumber()).isEqualTo(userModel.getMobileNumber());
        assertThat(userModel.getEmailId()).isEqualTo(userModel.getEmailId());
        assertThat(userModel.getAltContactNumber()).isEqualTo(userModel.getAltContactNumber());
        assertThat(userModel.getPan()).isEqualTo(userModel.getPan());
        assertThat(userModel.getAadhaarNumber()).isEqualTo(userModel.getAadhaarNumber());
        assertThat(userModel.getAddress()).isEqualTo(userModel.getAddress());
        assertThat(userModel.isActive()).isEqualTo(userModel.isActive());
        assertThat(userModel.getDob()).isEqualTo(userModel.getDob());
        assertThat(userModel.getPwdExpiryDate()).isEqualTo(userModel.getPwdExpiryDate());
        assertThat(userModel.getLocale()).isEqualTo(userModel.getLocale());
        assertThat(userModel.getType()).isEqualTo(userModel.getType());
        assertThat(userModel.isAccountLocked()).isEqualTo(userModel.isAccountLocked());
        assertThat(userModel.getRoles()).isEqualTo(userModel.getRoles());
        assertThat(userModel.getGuardian()).isEqualTo(userModel.getGuardian());
        assertThat(userModel.getGuardianRelation()).isEqualTo(userModel.getGuardianRelation());
        assertThat(userModel.getSignature()).isEqualTo(userModel.getSignature());
        assertThat(userModel.getBloodGroup()).isEqualTo(userModel.getBloodGroup());
        assertThat(userModel.getPhoto()).isEqualTo(userModel.getPhoto());
        assertThat(userModel.getIdentificationMark()).isEqualTo(userModel.getIdentificationMark());
        assertThat(userModel.getRoles().size()).isEqualTo(2);
        assertThat(userModel.getAddress().size()).isEqualTo(2);
    }

    @Test
    public void test_entity_should_build_itself_from_domain() {
        org.egov.user.domain.model.User domainUser = getUserModel();
        User entityUser = new User(domainUser);

        assertThat(entityUser.getId()).isEqualTo(domainUser.getId());
        assertThat(entityUser.getName()).isEqualTo(domainUser.getName());
        assertThat(entityUser.getUsername()).isEqualTo(domainUser.getUsername());
        assertThat(entityUser.getTitle()).isEqualTo(domainUser.getTitle());
        assertThat(entityUser.getPassword()).isEqualTo(domainUser.getPassword());
        assertThat(entityUser.getSalutation()).isEqualTo(domainUser.getSalutation());
        assertThat(entityUser.getGuardian()).isEqualTo(domainUser.getGuardian());
        assertThat(entityUser.getGuardianRelation()).isEqualTo(domainUser.getGuardianRelation());
        assertThat(entityUser.getGender()).isEqualTo(domainUser.getGender());
        assertThat(entityUser.getMobileNumber()).isEqualTo(domainUser.getMobileNumber());
        assertThat(entityUser.getEmailId()).isEqualTo(domainUser.getEmailId());
        assertThat(entityUser.getAltContactNumber()).isEqualTo(domainUser.getAltContactNumber());
        assertThat(entityUser.getPan()).isEqualTo(domainUser.getPan());
        assertThat(entityUser.getAadhaarNumber()).isEqualTo(domainUser.getAadhaarNumber());
        assertThat(entityUser.isActive()).isEqualTo(domainUser.isActive());
        assertThat(entityUser.getDob()).isEqualTo(domainUser.getDob());
        assertThat(entityUser.getPwdExpiryDate()).isEqualTo(domainUser.getPwdExpiryDate());
        assertThat(entityUser.getLocale()).isEqualTo(domainUser.getLocale());
        assertThat(entityUser.getType()).isEqualTo(domainUser.getType());
        assertThat(entityUser.getBloodGroup()).isEqualTo(domainUser.getBloodGroup());
        assertThat(entityUser.getIdentificationMark()).isEqualTo(domainUser.getIdentificationMark());
        assertThat(entityUser.getSignature()).isEqualTo(domainUser.getSignature());
        assertThat(entityUser.getPhoto()).isEqualTo(domainUser.getPhoto());
        assertThat(entityUser.isAccountLocked()).isEqualTo(domainUser.isAccountLocked());
        assertThat(entityUser.getLastModifiedDate()).isEqualTo(domainUser.getLastModifiedDate());
        assertThat(entityUser.getCreatedDate()).isEqualTo(domainUser.getCreatedDate());
        assertThat(entityUser.getRoles().size()).isEqualTo(2);
    }

    private org.egov.user.persistence.entity.User getUserEntity() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
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

    private List<Address> getAddressList() {
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

    private Set<Role> getListOfRoles() {
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

        return new HashSet<Role>() {{
            add(role1);
            add(role2);
        }};
    }

    private org.egov.user.domain.model.User getUserModel() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);
        Date date = calendar.getTime();

        return org.egov.user.domain.model.User.builder()
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
                .active(true)
                .dob(date)
                .pwdExpiryDate(date)
                .locale("en_IN")
                .type(UserType.CITIZEN)
                .accountLocked(false)
                .roles(getListOfDomainRoles())
                .guardian("name of relative")
                .guardianRelation(GuardianRelation.Father)
                .signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39")
                .bloodGroup(BloodGroup.A_POSITIVE)
                .photo("3b26fb49-e43d-401b-899a-f8f0a1572de0")
                .identificationMark("identification mark")
                .build();
    }

    private Set<org.egov.user.domain.model.Role> getListOfDomainRoles() {
        org.egov.user.domain.model.Role userRole = org.egov.user.domain.model.Role.builder()
                .name("USER")
                .description("Role Description")
                .build();

        org.egov.user.domain.model.Role adminRole = org.egov.user.domain.model.Role.builder()
                .name("ADMIN")
                .description("Role Description")
                .build();

        return new HashSet<org.egov.user.domain.model.Role>() {{
            add(userRole); add(adminRole);
        }};
    }
}
