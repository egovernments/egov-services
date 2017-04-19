package org.egov.domain.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Message {
 
    private String code;
    private String message;
    private String tenantId;
    private String locale;
}