package org.egov.user.web.contract;

import org.egov.user.domain.model.Address;
import org.egov.user.domain.model.Role;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.enums.*;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;

public class UserRequestTest {

    @Test
    public void testEntityToContractConversion() throws Exception {
        User userEntity = getUser();
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
        assertThat(userRequestContract.getActive()).isEqualTo(userEntity.getActive());
        assertThat(userRequestContract.getDob()).isEqualTo(userEntity.getDob());
        assertThat(userRequestContract.getPwdExpiryDate()).isEqualTo(userEntity.getPwdExpiryDate());
        assertThat(userRequestContract.getLocale()).isEqualTo(userEntity.getLocale());
        assertThat(userRequestContract.getType()).isEqualTo(userEntity.getType());
        assertThat(userRequestContract.getAccountLocked()).isEqualTo(userEntity.getAccountLocked());
        assertThat(userRequestContract.getFatherOrHusbandName()).isEqualTo(userEntity.getGuardian());
        assertThat(userRequestContract.getSignature()).isEqualTo(userEntity.getSignature());
        assertThat(userRequestContract.getBloodGroup()).isEqualTo(userEntity.getBloodGroup().getValue());
        assertThat(userRequestContract.getPhoto()).isEqualTo(userEntity.getPhoto());
        assertThat(userRequestContract.getIdentificationMark()).isEqualTo(userEntity.getIdentificationMark());
        assertThat(userRequestContract.getRoles().get(0).getName()).isEqualTo("name of the role 1");
        assertThat(userRequestContract.getRoles().get(1).getName()).isEqualTo("name of the role 2");
        assertThat(userRequestContract.getCreatedBy()).isEqualTo(1L);
        assertThat(userRequestContract.getCreatedDate()).isEqualTo(userEntity.getCreatedDate());
        assertThat(userRequestContract.getLastModifiedBy()).isEqualTo(2L);
        assertThat(userRequestContract.getLastModifiedDate()).isEqualTo(userEntity.getLastModifiedDate());
    }

    @Test
    public void testContractToDomainConversion() throws Exception {
        UserRequest userRequest = buildUserRequest();
        User userForCreate = userRequest.toDomain();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(2017, 01, 01, 01, 01, 01);
        String expectedDate = c.getTime().toString();

        assertEquals("Kroorveer", userForCreate.getName());
        assertEquals("yakku", userForCreate.getUsername());
        assertEquals("Dr.", userForCreate.getSalutation());
        assertEquals("8967452310", userForCreate.getMobileNumber());
        assertEquals("kroorkool@maildrop.cc", userForCreate.getEmailId());
        assertEquals("0987654321", userForCreate.getAltContactNumber());
        assertEquals("KR12345J", userForCreate.getPan());
        assertEquals("qwerty-1234567", userForCreate.getAadhaarNumber());
        assertTrue(userForCreate.getActive());
        assertEquals(expectedDate, userForCreate.getDob().toString());
        assertEquals(expectedDate, userForCreate.getPwdExpiryDate().toString());
        assertEquals("en_IN", userForCreate.getLocale());
        assertEquals(UserType.CITIZEN, userForCreate.getType());
        assertFalse(userForCreate.getAccountLocked());
        assertEquals("signature", userForCreate.getSignature());
        assertEquals("myPhoto", userForCreate.getPhoto());
        assertEquals("hole in the mole", userForCreate.getIdentificationMark());
        assertEquals(Gender.MALE, userForCreate.getGender());
        assertEquals(BloodGroup.O_POSITIVE, userForCreate.getBloodGroup());
        assertNotNull(userForCreate.getLastModifiedDate());
        assertNotNull(userForCreate.getCreatedDate());
        assertNotEquals(expectedDate, userForCreate.getLastModifiedDate().toString());
        assertNotEquals(expectedDate, userForCreate.getCreatedDate().toString());
        assertEquals("CITIZEN", userForCreate.getRoles().iterator().next().getName());
        assertEquals("ap.public", userForCreate.getTenantId());
        assertEquals("otpreference1", userForCreate.getOtpReference());
        assertEquals("!abcd1234", userForCreate.getPassword());
    }

    @Test
    public void testShouldOverrideProvidedRolesByCitizenRole() throws Exception {
        UserRequest userRequest = buildUserRequestWithRoles();
        User userForCreate = userRequest.toDomain();

        assertEquals("CITIZEN", userForCreate.getRoles().iterator().next().getName());
    }

    private UserRequest buildUserRequestWithRoles() {
        List<RoleRequest> roles = new ArrayList<>();
        roles.add(RoleRequest.builder().name("INSPECTOR").build());
        return getUserBuilder(roles).build();
    }

    private UserRequest buildUserRequest() {
        List<RoleRequest> roles = new ArrayList<>();
        roles.add(RoleRequest.builder().name("CITIZEN").build());
        return getUserBuilder(roles).build();
    }

    private UserRequest.UserRequestBuilder getUserBuilder(List<RoleRequest> roles) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(2017, 01, 01, 01, 01, 01);
        Date dateToTest = c.getTime();
        return UserRequest.builder()
                .name("Kroorveer")
                .userName("yakku")
                .salutation("Dr.")
                .mobileNumber("8967452310")
                .emailId("kroorkool@maildrop.cc")
                .altContactNumber("0987654321")
                .pan("KR12345J")
                .aadhaarNumber("qwerty-1234567")
                .active(Boolean.TRUE)
                .dob(dateToTest)
                .pwdExpiryDate(dateToTest)
                .locale("en_IN")
                .type(UserType.CITIZEN)
                .accountLocked(Boolean.FALSE)
                .signature("signature")
                .photo("myPhoto")
                .identificationMark("hole in the mole")
                .gender("Male")
                .bloodGroup("O_positive")
                .lastModifiedDate(dateToTest)
                .createdDate(dateToTest)
                .tenantId("ap.public")
                .otpReference("otpreference1")
                .password("!abcd1234")
                .roles(roles);
    }

    private User getUser() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);
        Date date = calendar.getTime();

        return User.builder()
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
                .createdBy(1L)
                .createdDate(date)
                .lastModifiedBy(2L)
                .lastModifiedDate(date)
                .build();
    }

    private List<Address> getAddressList() {
        return asList(Address.builder()
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

    private List<Role> getListOfRoles() {
        User user = User.builder().id(0L).build();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);

        Role role1 = Role.builder()
                .id(1L)
                .name("name of the role 1")
                .description("description")
                .createdBy(1L)
                .createdDate(calendar.getTime())
                .lastModifiedBy(1L)
                .lastModifiedDate(calendar.getTime())
                .build();

        Role role2 = Role.builder()
                .id(2L)
                .name("name of the role 2")
                .description("description")
                .createdBy(1L)
                .createdDate(calendar.getTime())
                .lastModifiedBy(1L)
                .lastModifiedDate(calendar.getTime())
                .build();

        return Arrays.asList(role1, role2);
    }
}