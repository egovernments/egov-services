package org.egov.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditDetails {

    private String createdBy;

    private Long createdDate;

    private String lastModifiedBy;

    private Long lastModifiedDate;

}