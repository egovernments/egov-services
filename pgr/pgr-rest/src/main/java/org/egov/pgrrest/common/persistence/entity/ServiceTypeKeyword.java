package org.egov.pgrrest.common.persistence.entity;

import lombok.*;

import javax.persistence.*;

import static org.egov.pgrrest.common.persistence.entity.ServiceTypeKeyword.SEQ_SERVICE_TYPE_KEYWORD;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "servicetype_keyword")
@SequenceGenerator(name = SEQ_SERVICE_TYPE_KEYWORD, sequenceName = SEQ_SERVICE_TYPE_KEYWORD, allocationSize = 1)
public class ServiceTypeKeyword extends AbstractPersistable<Long> {

    public static final String SEQ_SERVICE_TYPE_KEYWORD = "seq_servicetype_keyword";

    @Id
    @GeneratedValue(generator = SEQ_SERVICE_TYPE_KEYWORD, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "servicecode")
    private String serviceCode;

    @Column(name = "tenantid")
    private String tenantId;

    @Column(name = "keyword")
    private String keyword;
}
