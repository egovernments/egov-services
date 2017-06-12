package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.Roles;
import entities.requests.eGovEIS.employee.User;

public class UserBuilder {

    User user = new User();

    Roles[] roles = new Roles[2];
    Roles role1 = new RolesBuilder().build();
    Roles role2 = new RolesBuilder("second").build();

    public UserBuilder() {
        user.setPassword("12345");
        user.setSalutation("Mr");
        user.setName("ABCD");
        user.setGender("Male");
        user.setMobileNumber("9999999999");
        user.setEmailId("abcd@egovernments.org");
        user.setAltContactNumber(null);
        user.setPan(null);
        user.setAadhaarNumber(null);
        user.setPermanentAddress("null");
        user.setPermanentCity("null");
        user.setPermanentPincode("null");
        user.setCorrespondenceAddress(null);
        user.setCorrespondenceCity(null);
        user.setCorrespondencePincode(null);
        user.setActive(true);
        user.setDob("01/01/1990");
        user.setPwdExpiryDate("01-01-2018 00:00:00");
        user.setLocale("en_in");
        user.setType("EMPLOYEE");
        user.setAccountLocked(false);
        user.setFatherOrHusbandName(null);
        user.setBloodGroup(null);
        user.setIdentificationMark(null);
        user.setPhoto(null);
        user.setCreatedBy(1);
        user.setCreatedDate("01-01-2017 00:00:00");
        user.setLastModifiedBy(1);
        user.setLastModifiedDate("01-01-2017 00:00:00");
        roles[0] = role1;
        roles[1] = role2;
        user.setRoles(roles);
    }

    public UserBuilder withUserName(String userName) {
        user.setUserName(userName);
        return this;
    }

    public User build() {
        return user;
    }
}
