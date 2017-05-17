package org.egov.workflow.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class KeywordStatusMappingKey implements Serializable {

    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Column(name = "tenantid", nullable = false)
    private String tenantId;

    @Column(name = "servicestatuscode", nullable = false)
    private String code;
}