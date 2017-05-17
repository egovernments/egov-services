package org.egov.pgrrest.common.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "submission_attribute")
public class SubmissionAttribute extends AbstractAuditable<SubmissionAttributeKey> {

    @Transient
    private boolean retained;

    @EmbeddedId
    private SubmissionAttributeKey id;

    public String getKey() {
        return id.getKey();
    }
}

