package org.egov.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
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

    public org.egov.domain.model.Message toDomain() {
        return new org.egov.domain.model.Message(code,message,tenantId,locale);
    }
}
