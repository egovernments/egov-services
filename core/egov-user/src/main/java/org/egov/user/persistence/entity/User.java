package org.egov.user.persistence.entity;

import lombok.*;
import org.egov.user.persistence.enums.BloodGroup;
import org.egov.user.persistence.enums.Gender;
import org.egov.user.persistence.enums.GuardianRelation;
import org.egov.user.persistence.enums.UserType;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.egov.user.persistence.entity.EnumConverter.toEnumType;

@Entity
@Table(name = "eg_user")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractAuditable<UserKey> {

    private static final long serialVersionUID = 1666623645834766468L;

    @EmbeddedId
    private UserKey userKey;

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

    @Column(name = "active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "eg_userrole", joinColumns = {
    		@JoinColumn(name = "userid"),
    		@JoinColumn(name = "tenantid"),
	},
            inverseJoinColumns = {
    		@JoinColumn(name = "roleid"),
			@JoinColumn(name = "roleidtenantid")
    }
    )
	private Set<Role> roles = new HashSet<>();

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "pwdexpirydate")
    private Date pwdExpiryDate;

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
    private Boolean accountLocked;

    public User (org.egov.user.domain.model.User user) {
        this.name = user.getName();
        this.userKey = new UserKey(user.getId(), user.getTenantId());
        this.username = user.getUsername();
        this.title = user.getTitle();
        this.password = user.getPassword();
        this.salutation = user.getSalutation();
        this.guardian = user.getGuardian();
        this.guardianRelation = toEntityGuardianRelation(user.getGuardianRelation());
        this.gender = toEntityGender(user.getGender());
        this.mobileNumber = user.getMobileNumber();
        this.emailId = user.getEmailId();
        this.altContactNumber = user.getAltContactNumber();
        this.pan = user.getPan();
        this.aadhaarNumber = user.getAadhaarNumber();
        this.active = mapNullableBooleanToPrimitive(user.getActive());
		this.accountLocked = mapNullableBooleanToPrimitive(user.getAccountLocked());
        this.dob = user.getDob();
        this.pwdExpiryDate = user.getPasswordExpiryDate();
        this.locale = user.getLocale();
        this.type = toEntityUserType(user.getType());
        this.bloodGroup = toEntityBloodGroup(user.getBloodGroup());
        this.identificationMark = user.getIdentificationMark();
        this.signature = user.getSignature();
        this.photo = user.getPhoto();
        this.roles = convertDomainRolesToEntity(user.getRoles());
    }

	private boolean mapNullableBooleanToPrimitive(Boolean nullableBoolean) {
		if (nullableBoolean == null) {
			return false;
		} else {
			return nullableBoolean;
		}
	}

	public org.egov.user.domain.model.User toDomain(org.egov.user.domain.model.Address correspondenceAddress,
													org.egov.user.domain.model.Address permanentAddress) {
        return
        org.egov.user.domain.model.User.builder()
                .id(userKey.getId())
                .username(username)
                .title(title)
                .password(password)
                .salutation(salutation)
                .guardian(guardian)
                .guardianRelation(toDomainGuardianRelation())
                .name(name)
                .gender(toDomainGender())
                .mobileNumber(mobileNumber)
                .emailId(emailId)
                .altContactNumber(altContactNumber)
                .pan(pan)
                .aadhaarNumber(aadhaarNumber)
                .active(active)
                .roles(convertEntityRoleToDomain(roles))
                .dob(dob)
                .passwordExpiryDate(pwdExpiryDate)
                .locale(locale)
                .type(toDomainUserType())
                .bloodGroup(toDomainBloodGroup())
                .identificationMark(identificationMark)
                .signature(signature)
                .photo(photo)
                .accountLocked(accountLocked)
                .lastModifiedDate(getLastModifiedDate())
                .createdDate(getCreatedDate())
                .lastModifiedBy(getLastModifiedId())
                .createdBy(getCreatedById())
                .tenantId(userKey.getTenantId())
				.correspondenceAddress(correspondenceAddress)
				.permanentAddress(permanentAddress)
				.build();
    }

	private Set<Role> convertDomainRolesToEntity(List<org.egov.user.domain.model.Role> domainRoles) {
		return domainRoles.stream().map(Role::new).collect(Collectors.toSet());
	}

    private org.egov.user.domain.model.enums.GuardianRelation toDomainGuardianRelation() {
        return toEnumType(org.egov.user.domain.model.enums.GuardianRelation.class, guardianRelation);
    }

    private GuardianRelation toEntityGuardianRelation(
            org.egov.user.domain.model.enums.GuardianRelation guardianRelation) {
        return toEnumType(GuardianRelation.class, guardianRelation);
    }

    private org.egov.user.domain.model.enums.Gender toDomainGender() {
        return toEnumType(org.egov.user.domain.model.enums.Gender.class, gender);
    }

    private Gender toEntityGender(
            org.egov.user.domain.model.enums.Gender gender) {
        return toEnumType(Gender.class, gender);
    }

    private org.egov.user.domain.model.enums.BloodGroup toDomainBloodGroup() {
        return toEnumType(org.egov.user.domain.model.enums.BloodGroup.class, bloodGroup);
    }

    private BloodGroup toEntityBloodGroup(
            org.egov.user.domain.model.enums.BloodGroup bloodGroup) {
        return toEnumType(BloodGroup.class, bloodGroup);
    }

    private org.egov.user.domain.model.enums.UserType toDomainUserType() {
        return toEnumType(org.egov.user.domain.model.enums.UserType.class, type);
    }

    private UserType toEntityUserType(org.egov.user.domain.model.enums.UserType type) {
        return toEnumType(UserType.class, type);
    }

    private Long getLastModifiedId() {
        return  getLastModifiedBy();
    }

    private Long getCreatedById() {
        return getCreatedBy();
    }

    private List<org.egov.user.domain.model.Role> convertEntityRoleToDomain(Set<Role> entityRoles) {
        return entityRoles.stream().map(Role::toDomain).collect(Collectors.toList());
    }

	@Override
	public UserKey getId() {
		return userKey;
	}

	@Override
	protected void setId(UserKey id) {
		this.userKey = id;
	}
}