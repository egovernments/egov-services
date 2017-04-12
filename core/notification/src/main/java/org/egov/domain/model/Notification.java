package org.egov.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class Notification {
    private Long id;
    private String messageCode;
    private String messageValues;
    private String reference;
    private Long userId;
    private boolean read;
    private Date createdDate;
}
