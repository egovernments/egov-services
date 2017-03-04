package org.egov.persistence.repository;

import org.egov.persistence.entity.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TokenJpaRepositoryTest {

    @Autowired
    private TokenJpaRepository tokenJpaRepository;

    @Test
    @Sql(scripts = {"/sql/clearTokens.sql", "/sql/createTokens.sql"})
    public void should_save_otp() {
        final Token token = new Token();
        token.setId(UUID.randomUUID().toString());
        token.setNumber("99999");
        token.setIdentity("someIdentity");
        token.setTimeToLiveInSeconds(400L);
        token.setCreatedDate(new Date());
        token.setCreatedBy(0L);
        token.setLastModifiedBy(1L);
        token.setLastModifiedDate(new Date());

        tokenJpaRepository.save(token);

        final List<Token> tokens = tokenJpaRepository.findAll();
        assertEquals(3, tokens.size());
    }

    @Test
    @Sql(scripts = {"/sql/clearTokens.sql", "/sql/createTokens.sql"})
    public void should_retrieve_otp_for_given_token_number_and_identity() {
        final Token actualToken = tokenJpaRepository.findByNumberAndIdentity("token2", "identity2");

        assertNotNull(actualToken);
        assertEquals("id2" , actualToken.getId());
        assertEquals("identity2" , actualToken.getIdentity());
        assertEquals("token2" , actualToken.getNumber());
        assertEquals(Long.valueOf(200) , actualToken.getTimeToLiveInSeconds());
        assertEquals(Long.valueOf(124) , actualToken.getCreatedBy());
        assertNull(actualToken.getLastModifiedBy());
        assertNull(actualToken.getLastModifiedDate());
        assertNull(actualToken.getVersion());
        assertNotNull(actualToken.getCreatedDate());
    }
}