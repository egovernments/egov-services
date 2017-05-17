package org.egov.workflow.persistence.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "keyword_service_status")
public class KeywordStatusMapping extends AbstractAuditable<KeywordStatusMappingKey> {

    @EmbeddedId
    private KeywordStatusMappingKey id;

    public org.egov.workflow.domain.model.KeywordStatusMapping toDomain(){

        return org.egov.workflow.domain.model.KeywordStatusMapping.builder()
            .tenantId(id.getTenantId())
            .keyword(id.getKeyword())
            .code(id.getCode())
            .build();
    }
}
