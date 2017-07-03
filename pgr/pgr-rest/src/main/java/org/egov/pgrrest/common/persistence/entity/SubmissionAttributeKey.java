package org.egov.pgrrest.common.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class SubmissionAttributeKey implements Serializable {
    private String code;
    private String key;
    private String crn;
    @Column(name = "tenantid")
    private String tenantId;
}

