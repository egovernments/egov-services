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
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locale")
    private String locale;

    @Column(name = "code")
    private String code;

    @Column(name = "message")
    private String message;

    @Column(name = "tenant_id")
    private String tenantId;
}
