package org.egov.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.domain.model.TokenRequest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "eg_token")
public class Token extends AbstractAuditable {

    private static final long serialVersionUID = 4020616083055647372L;
    private static final String IST = "Asia/Calcutta";
    private static final String YES = "Y";
    private static final String NO = "N";

    @Id
    private String id;

    @Column(name = "tokennumber")
    private String number;

    @Column(name = "tokenidentity")
    private String identity;

    @Column(name = "tenantid")
    private String tenantId;

    @Column(name = "ttlsecs")
    private Long timeToLiveInSeconds;

    @Column(name = "validated")
    private String validated;

    public Token(TokenRequest tokenRequest) {
        id = UUID.randomUUID().toString();
        number = tokenRequest.generateToken();
        tenantId = tokenRequest.getTenantId();
        identity = tokenRequest.getIdentity();
        timeToLiveInSeconds = tokenRequest.getTimeToLive();
        validated = NO;
        setCreatedBy(0L);
        setCreatedDate(new Date());
    }

    public boolean isValidated() {
        return YES.equalsIgnoreCase(validated);
    }

    public org.egov.domain.model.Token toDomain() {

        return org.egov.domain.model.Token.builder()
                .uuid(id)
                .identity(identity)
                .number(number)
                .tenantId(tenantId)
                .timeToLiveInSeconds(timeToLiveInSeconds)
                .expiryDateTime(getExpiryDateTime())
                .validated(isValidated())
                .build();
    }

    private LocalDateTime getExpiryDateTime() {
        return LocalDateTime.ofInstant(getCreatedDate().toInstant(), ZoneId.of(IST))
                .plusSeconds(timeToLiveInSeconds);
    }
}