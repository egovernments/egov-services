package org.egov.persistence.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenTest {

    @Test
    public void test_should_create_entity_from_domain() {
        final org.egov.domain.model.Token domainToken =
                org.egov.domain.model.Token.builder()
                        .number("tokenNumber")
                        .identity("identity")
                        .uuid("uuid")
                        .build();

        final Token entityToken = new Token(domainToken);

        assertEquals("tokenNumber", entityToken.getNumber());
        assertEquals("uuid", entityToken.getId());
        assertEquals("identity", entityToken.getIdentity());
        assertEquals(Long.valueOf(0), entityToken.getCreatedBy());
        assertNotNull(entityToken.getCreatedDate());
        assertEquals(Long.valueOf(300), entityToken.getTimeToLiveInSeconds());
    }

}