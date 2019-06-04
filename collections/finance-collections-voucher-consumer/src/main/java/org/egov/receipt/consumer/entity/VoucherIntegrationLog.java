package org.egov.receipt.consumer.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherIntegrationLog {
private String id;
private String referenceNumber;
private String status;
private String voucherNumber;
private String type;
private String requestJson;
private String description;
private String tenantId;
private Date createdDate;
}
