package org.egov.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "eg_token")
public class Token extends AbstractAuditable {

    private static final long serialVersionUID = 4020616083055647372L;

    @Id
    private String id;

    @Column(name = "tokennumber")
    private String number;

    @Column(name = "tokenidentity")
    private String identity;

    @Column(name = "ttlsecs")
    private Long timeToLiveInSeconds;

    public Token(org.egov.domain.model.Token domainToken) {
        id = domainToken.getUuid();
        number = domainToken.getNumber();
        identity = domainToken.getIdentity();
        timeToLiveInSeconds = domainToken.getTimeToLive();
        setCreatedBy(0L);
        setCreatedDate(new Date());
    }
}