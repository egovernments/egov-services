package org.egov.collection.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LegacyReceiptDetails {
@NotNull
private Long id;

private String billNo;

private Long billId;

private Long billYear;

private Long taxId;
@NotNull
private Date billDate;

private String description;

private Double currDemand;

private Double arrDemand;

private Double currCollection;

private Double arrCollection;

private Double currBalance;

private Double arrBalance;

@NotNull
private Long id_receipt_header; 
@NotNull
private String tenantid;
}