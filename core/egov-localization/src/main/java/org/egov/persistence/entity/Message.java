package org.egov.persistence.entity;

import lombok.*;
import org.egov.domain.model.Tenant;

import javax.persistence.*;

@Entity
@Data
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = Message.SEQ_MESSAGE, sequenceName = Message.SEQ_MESSAGE, allocationSize = 1)
public class Message {

    static final String SEQ_MESSAGE = "SEQ_MESSAGE";

    @Id
    @GeneratedValue(generator = SEQ_MESSAGE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "locale")
    private String locale;

    @Column(name = "code")
    private String code;

    @Column(name = "message")
    private String message;

    @Column(name = "tenantid")
    private String tenantId;

    public Message(org.egov.domain.model.Message domainMessage) {
        this.locale = domainMessage.getLocale();
        this.tenantId = domainMessage.getTenant().getTenantId();
        this.message = domainMessage.getMessage();
        this.code = domainMessage.getCode();
    }

    public org.egov.domain.model.Message toDomain() {
        return org.egov.domain.model.Message.builder()
            .code(code)
            .message(message)
            .locale(locale)
            .tenant(new Tenant(tenantId))
            .build();
    }
}
