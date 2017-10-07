package org.egov.models;

import lombok.*;

import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notice {
    @Size(min = 4, max = 128)
    private String tenantId;

    private String upicNumber;

    private String applicationNo;

    private String noticeDate;

    private String noticeNumber;

    private String noticeType;

    private String fileStoreId;

    private AuditDetails auditDetails;
}