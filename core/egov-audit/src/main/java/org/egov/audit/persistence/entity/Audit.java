package org.egov.audit.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import java.util.Date;

import static org.egov.audit.persistence.entity.Audit.SEQ_AUDIT;

@Getter
@Setter
@Entity
@Table(name = "audit")
@SequenceGenerator(name = SEQ_AUDIT, sequenceName = SEQ_AUDIT, allocationSize = 1)
public class Audit {
    public static final String SEQ_AUDIT = "SEQ_EGPGR_COMPLAINT";

    @Id
    @GeneratedValue(generator = SEQ_AUDIT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "channel", nullable = false)
    @Length(max = 128, min = 5)
    private String channel;

    @Column(name = "device_id")
    @Length(max = 128)
    private String deviceId;

    @Column(name = "consumer_id")
    private Long consumerId;

    @Column(name = "event_name", nullable = false)
    @Length(max = 128)
    private String eventName;

    @Column(name = "event_date")
    private Date escalationDate;

    @Column(name = "os_version", length = 32)
    private String osVersion;

    @Column(name = "ip_address", length = 32)
    private String ipAddress;
}