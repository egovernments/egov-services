package org.egov.models;

import lombok.*;
import org.egov.enums.NoticeType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notice {
    @Size(min = 4, max = 128)
    @NonNull
    private String tenantId;

    @Size(min = 4, max = 128)
    private String upicNumber;

    @Size(min = 4, max = 64)
    private String applicationNo;

    @NotNull
    private String noticeDate;

    @Size(min = 4, max = 128)
    private String noticeNumber;

    @NotNull
    private NoticeType noticeType;

    @Size(min = 4, max = 128)
    @NotNull
    private String fileStoreId;

    private AuditDetails auditDetails;
}