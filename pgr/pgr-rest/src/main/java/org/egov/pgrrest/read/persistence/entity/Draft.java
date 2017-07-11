package org.egov.pgrrest.read.persistence.entity;

import lombok.*;
import org.egov.pgrrest.common.persistence.entity.AbstractAuditable;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cs_draft")
@SequenceGenerator(name = Draft.SEQ_CS_DRAFT, sequenceName = Draft.SEQ_CS_DRAFT, allocationSize = 1)
@EqualsAndHashCode
public class Draft extends AbstractAuditable<Long> {

    public static final String SEQ_CS_DRAFT = "SEQ_CS_DRAFT";

    private static final long serialVersionUID = 26943677171075535L;

    @Id
    @GeneratedValue(generator = SEQ_CS_DRAFT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "tenantid")
    private String tenantId;

    @Column(name = "servicecode")
    private String serviceCode;

    @Column(name = "userid")
    private Long userId;

    private String draft;
}
