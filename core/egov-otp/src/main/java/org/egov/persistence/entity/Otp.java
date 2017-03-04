package org.egov.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "eg_token")
public class Otp extends AbstractAuditable {

    private static final long serialVersionUID = 4020616083055647372L;
    @Id
    private String id;

    @Column(name = "tokennumber")
    private String number;

    @Column(name = "tokenidentity")
    private String identity;

    @Column(name = "ttlsecs")
    private Long timeToLiveInSeconds;
}