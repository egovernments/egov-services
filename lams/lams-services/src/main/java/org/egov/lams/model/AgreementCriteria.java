package org.egov.lams.model;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.egov.lams.model.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class AgreementCriteria {

    @NotNull
    private String tenantId;
    private Long id;
    private Set<Long> agreementId;
    private String agreementNumber;
    private String tenderNumber;
    private String acknowledgementNumber;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fromDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date toDate;
    private Status status;
    private String tinNumber;
    private String tradelicenseNumber;
    private Long assetCategory;
    private String shoppingComplexName;
    private String assetCode;
    private Long locality;
    private Long revenueWard;
    private Long electionWard;
    private String shopNumber;
    private String allotteeName;
    private Long mobileNumber;
    private Long offSet;
    private Long size;
    private String stateId;
    private Set<Long> asset;
    private Set<Long> allottee;
    private String action;
    private String referenceNumber;
    private String oldAgreementNumber;
    private String floorNumber;
    private Long parent;

    /*
     * Below three methods use the Java Stream api and stream all specified fields and check for null all these methods return
     * true only when all the fields specified in stream are null or else it returns false
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public boolean isAgreementEmpty() {
        return Stream.of(agreementId, agreementNumber, status, fromDate, toDate, tenderNumber,
                tinNumber, tradelicenseNumber, asset, allottee, referenceNumber, oldAgreementNumber, floorNumber)
                .allMatch(Objects::isNull);
    }

    public boolean isAllotteeEmpty() {
        return Stream.of(allotteeName, mobileNumber)
                .allMatch(Objects::isNull);
    }

    public boolean isAssetEmpty() {
        return Stream.of(assetCategory, shoppingComplexName, assetCode, locality,
                revenueWard, electionWard, shopNumber)
                .allMatch(Objects::isNull);
    }

    public boolean isAssetOnlyNull() {
        return !isAgreementEmpty() && !isAllotteeEmpty() && isAssetEmpty();
    }

    public boolean isAllotteeOnlyNull() {
        return !isAgreementEmpty() && !isAssetEmpty() && isAllotteeEmpty();
    }

    public boolean isAgreementOnlyNull() {
        return isAgreementEmpty() && !isAssetEmpty() && !isAllotteeEmpty();
    }

    public boolean isAgreementAndAllotteeNull() {
        return isAgreementEmpty() && !isAssetEmpty() && isAllotteeEmpty();
    }

    public boolean isAssetAndAllotteeNull() {
        return !isAgreementEmpty() && isAssetEmpty() && isAllotteeEmpty();
    }

    public boolean isAgreementAndAssetNull() {
        return isAgreementEmpty() && isAssetEmpty() && !isAllotteeEmpty();
    }

    public boolean isAgreementAndAssetAndAllotteeNotNull() {
        return !isAgreementEmpty() && !isAssetEmpty() && !isAllotteeEmpty();
    }
}
