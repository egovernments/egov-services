package org.egov.models;

import lombok.*;
import org.egov.enums.NoticeType;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeSearchCriteria {

    private String tenantId;

    private Integer pageSize;

    private Integer pageNumber;

    private String upicNumber;

    private String applicationNo;

    private NoticeType noticeType;

    private String noticeDate;

    private Long fromDate;

    private Long toDate;
}
