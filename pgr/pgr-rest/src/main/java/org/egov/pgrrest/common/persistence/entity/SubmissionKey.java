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
public class SubmissionKey implements Serializable {
    private String crn;
    @Column(name = "tenantid")
    private String tenantId;
}
