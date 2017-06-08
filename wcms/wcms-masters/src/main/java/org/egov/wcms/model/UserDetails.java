package org.egov.wcms.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class UserDetails {

    @NotNull
    private long id;

    @NotNull
    private String firstName;

    @NotNull
    private String middleName;

    @NotNull
    private String lastName;

    @NotNull
    private String dob;

    @NotNull
    private String altContactNumber;

    @NotNull
    private String fatherName;

    @NotNull
    private String husbandName;

    @NotNull
    private String bloodGroup;

    @NotNull
    private String pan;

    @NotNull
    private String permanentAddress;

    @NotNull
    private String permanentCity;

    @NotNull
    private String permanentPinCode;

    @NotNull
    private String correspondenceAddress;

    @NotNull
    private String correspondenceCity;

    @NotNull
    private String correspondencePinCode;

    @NotNull
    private String signature;

    @NotNull
    private String identificationMark;

    @NotNull
    private String photo;
}
