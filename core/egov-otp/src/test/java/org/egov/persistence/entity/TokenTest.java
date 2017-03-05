package org.egov.persistence.entity;

import org.egov.domain.model.TokenRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenTest {

    @Test
    public void test_should_create_entity_from_token_request() {
        final org.egov.domain.model.Token domainToken =
                org.egov.domain.model.Token.builder()
                        .number("tokenNumber")
                        .identity("identity")
                        .uuid("uuid")
                        .build();

        final TokenRequest tokenRequest = new TokenRequest("identity", "tenantId");
        final Token entityToken = new Token(tokenRequest);

        assertNotNull(entityToken.getNumber());
        assertNotNull(entityToken.getId());
        assertEquals("identity", entityToken.getIdentity());
        assertEquals(Long.valueOf(0), entityToken.getCreatedBy());
        assertNotNull(entityToken.getCreatedDate());
        assertEquals(Long.valueOf(300), entityToken.getTimeToLiveInSeconds());
    }

}