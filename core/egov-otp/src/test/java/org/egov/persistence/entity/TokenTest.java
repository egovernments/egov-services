package org.egov.persistence.entity;

import org.egov.domain.model.TokenRequest;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.Assert.*;

public class TokenTest {

    private static final String IST = "Asia/Calcutta";

    @Test
    public void test_should_create_entity_from_token_request() {
        final TokenRequest tokenRequest = new TokenRequest("identity", "tenantId");
        final Token entityToken = new Token(tokenRequest);

        assertNotNull(entityToken.getNumber());
        assertNotNull(entityToken.getId());
        assertEquals("identity", entityToken.getIdentity());
        assertEquals(Long.valueOf(0), entityToken.getCreatedBy());
        assertNotNull(entityToken.getCreatedDate());
        assertEquals(Long.valueOf(300), entityToken.getTimeToLiveInSeconds());
    }

    @Test
    public void test_should_create_domain_from_entity() {
        final Token entityToken = new Token();
        entityToken.setNumber("otpNumber");
        entityToken.setTenant("tenant");
        entityToken.setId("uuid");
        entityToken.setTimeToLiveInSeconds(300L);
        entityToken.setIdentity("identity");
        entityToken.setValidated("Y");
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(2016, 1, 1, 4, 0, 0, 0, ZoneId.of(IST));
        entityToken.setCreatedDate(Date.from(zonedDateTime.toInstant()));

        final org.egov.domain.model.Token domainToken = entityToken.toDomain();

        assertEquals("otpNumber", domainToken.getNumber());
        assertEquals("identity", domainToken.getIdentity());
        assertEquals("otpNumber", domainToken.getNumber());
        assertEquals(Long.valueOf(300), domainToken.getTimeToLiveInSeconds());
        assertTrue(domainToken.isValidated());
        assertEquals(LocalDateTime.of(2016, 1, 1, 4, 5, 0, 0), domainToken.getExpiryDateTime());
    }

    @Test
    public void test_is_validated_should_return_false_when_validation_is_set_to_no() {
        final Token entityToken = new Token();
        entityToken.setNumber("otpNumber");
        entityToken.setTenant("tenant");
        entityToken.setId("uuid");
        entityToken.setTimeToLiveInSeconds(300L);
        entityToken.setIdentity("identity");
        entityToken.setValidated("N");
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(2016, 1, 1, 4, 0, 0, 0, ZoneId.of(IST));
        entityToken.setCreatedDate(Date.from(zonedDateTime.toInstant()));

        final org.egov.domain.model.Token domainToken = entityToken.toDomain();

        assertFalse(domainToken.isValidated());
    }

    @Test
    public void test_is_validated_should_return_true_when_validation_is_set_to_yes() {
        final Token entityToken = new Token();
        entityToken.setNumber("otpNumber");
        entityToken.setTenant("tenant");
        entityToken.setId("uuid");
        entityToken.setTimeToLiveInSeconds(300L);
        entityToken.setIdentity("identity");
        entityToken.setValidated("Y");
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(2016, 1, 1, 4, 0, 0, 0, ZoneId.of(IST));
        entityToken.setCreatedDate(Date.from(zonedDateTime.toInstant()));

        final org.egov.domain.model.Token domainToken = entityToken.toDomain();

        assertTrue(domainToken.isValidated());
    }

}