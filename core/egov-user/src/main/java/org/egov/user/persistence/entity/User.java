/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.user.persistence.entity;

import lombok.*;
import org.egov.user.domain.model.enums.BloodGroup;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.GuardianRelation;
import org.egov.user.domain.model.enums.UserType;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.egov.user.persistence.entity.User.SEQ_COMPLAINT;


@Entity
@Table(name = "eg_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = SEQ_COMPLAINT, sequenceName = SEQ_COMPLAINT, allocationSize = 1)
public class User extends AbstractAuditable {

    public static final String SEQ_COMPLAINT = "SEQ_EG_USER";
    private static final long serialVersionUID = 1666623645834766468L;

    @Id
    @GeneratedValue(generator = SEQ_COMPLAINT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "title")
    private String title;

    @Column(name = "password")
    private String password;

    @Column(name = "salutation")
    private String salutation;

    @Column(name = "guardian")
    private String guardian;

    @Column(name = "guardianrelation")
    @Enumerated(EnumType.STRING)
    private GuardianRelation guardianRelation;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name = "mobilenumber")
    private String mobileNumber;

    @Column(name = "emailid")
    private String emailId;

    @Column(name = "altcontactnumber")
    private String altContactNumber;

    @Column(name = "pan")
    private String pan;

    @Column(name = "aadhaarnumber")
    private String aadhaarNumber;


    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    private List<Address> address = new ArrayList<>();

    @Column(name = "active")
    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "eg_userrole", joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "pwdexpirydate")
    private Date pwdExpiryDate = new Date();

    private String locale = "en_IN";

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "bloodgroup")
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Column(name = "identificationmark")
    private String identificationMark;

    @Column(name = "signature")
    private String signature;

    @Column(name = "photo")
    private String photo;

    @Column(name = "accountlocked")
    private boolean accountLocked;

    public User (org.egov.user.domain.model.User user) {
        this.name = user.getName();
        this.id = user.getId();
        this.username = user.getUsername();
        this.title = user.getTitle();
        this.password = user.getPassword();
        this.salutation = user.getSalutation();
        this.guardian = user.getGuardian();
        this.guardianRelation = user.getGuardianRelation();
        this.gender = user.getGender();
        this.mobileNumber = user.getMobileNumber();
        this.emailId = user.getEmailId();
        this.altContactNumber = user.getAltContactNumber();
        this.pan = user.getPan();
        this.aadhaarNumber = user.getAadhaarNumber();
        this.active = user.isActive();
        this.dob = user.getDob();
        this.pwdExpiryDate = user.getPwdExpiryDate();
        this.locale = user.getLocale();
        this.type = user.getType();
        this.bloodGroup = user.getBloodGroup();
        this.identificationMark = user.getIdentificationMark();
        this.signature = user.getSignature();
        this.photo = user.getPhoto();
        this.accountLocked = user.isAccountLocked();
        this.setLastModifiedDate(user.getLastModifiedDate());
        this.setCreatedDate(user.getCreatedDate());
        this.roles = convertDomainRolesToEntity(user.getRoles());
    }

    private Set<Role> convertDomainRolesToEntity(Set<org.egov.user.domain.model.Role> domainRoles) {
        return domainRoles.stream().map(Role::new).collect(Collectors.toSet());
    }

    public org.egov.user.domain.model.User toDomain() {
        return
        org.egov.user.domain.model.User.builder()
                .id(id)
                .username(username)
                .title(title)
                .password(password)
                .salutation(salutation)
                .guardian(guardian)
                .guardianRelation(guardianRelation)
                .name(name)
                .gender(gender)
                .mobileNumber(mobileNumber)
                .emailId(emailId)
                .altContactNumber(altContactNumber)
                .pan(pan)
                .aadhaarNumber(aadhaarNumber)
                .address(convertEntityAddressToDomain(address))
                .active(active)
                .roles(convertEntityRoleToDomain(roles))
                .dob(dob)
                .pwdExpiryDate(dob)
                .locale(locale)
                .type(type)
                .bloodGroup(bloodGroup)
                .identificationMark(identificationMark)
                .signature(signature)
                .photo(photo)
                .accountLocked(accountLocked)
                .lastModifiedDate(getLastModifiedDate())
                .createdDate(getCreatedDate())
                .lastModifiedBy(getLastModifiedId())
                .createdBy(getCreatedById()).build();
    }

    private Long getLastModifiedId() {
        return getLastModifiedBy() == null ? null : getLastModifiedBy().getId();
    }

    private Long getCreatedById() {
        return getCreatedBy() == null ? null : getCreatedBy().getId();
    }

    private Set<org.egov.user.domain.model.Role> convertEntityRoleToDomain(Set<Role> entityRoles) {
        return entityRoles.stream().map(Role::toDomain).collect(Collectors.toSet());
    }

    private List<org.egov.user.domain.model.Address> convertEntityAddressToDomain(List<Address> entityAddress) {
        return entityAddress.stream().map(Address::toDomain).collect(Collectors.toList());
    }
}