package org.egov.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.domain.model.TokenRequest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

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

    @Column(name = "tenant")
    private String tenant;

    @Column(name = "ttlsecs")
    private Long timeToLiveInSeconds;

    public Token(TokenRequest tokenRequest) {
        id = UUID.randomUUID().toString();
        number = tokenRequest.generateToken();
        tenant = tokenRequest.getTenantId();
        identity = tokenRequest.getIdentity();
        timeToLiveInSeconds = tokenRequest.getTimeToLive();
        setCreatedBy(0L);
        setCreatedDate(new Date());
    }

    public org.egov.domain.model.Token toDomain() {
        return org.egov.domain.model.Token.builder()
                .uuid(id)
                .identity(identity)
                .number(number)
                .tenantId(tenant)
                .build();
    }
}