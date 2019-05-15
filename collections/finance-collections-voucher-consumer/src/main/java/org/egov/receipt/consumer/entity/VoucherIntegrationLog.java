package org.egov.receipt.consumer.entity;

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
String id;
String referenceNumber;
String status;
String voucherNumber;
String type;
String requestJson;
String description;
String tenantId;
}
