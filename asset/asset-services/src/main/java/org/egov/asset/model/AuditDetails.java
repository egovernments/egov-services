package org.egov.asset.model;

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

    private String createdBy = null;

    private Long createdDate = null;

    private String lastModifiedBy = null;

    private Long lastModifiedDate = null;

}
