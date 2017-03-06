package org.egov.persistence.repository;

import org.egov.persistence.entity.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TokenJpaRepositoryTest {

    @Autowired
    private TokenJpaRepository tokenJpaRepository;

    @Test
    @Sql(scripts = {"/sql/clearTokens.sql", "/sql/createTokens.sql"})
    public void test_should_save_otp() {
        final Token token = new Token();
        token.setId(UUID.randomUUID().toString());
        token.setNumber("99999");
        token.setIdentity("someIdentity");
        token.setTimeToLiveInSeconds(400L);
        token.setCreatedDate(new Date());
        token.setCreatedBy(0L);
        token.setLastModifiedBy(1L);
        token.setLastModifiedDate(new Date());
        token.setTenant("tenant");
        token.setValidated("N");

        tokenJpaRepository.save(token);

        final List<Token> tokens = tokenJpaRepository.findAll();
        assertEquals(3, tokens.size());
    }

    @Test
    @Sql(scripts = {"/sql/clearTokens.sql", "/sql/createTokens.sql"})
    public void test_should_retrieve_otp_for_given_token_number_and_identity() {
        final List<Token> actualTokens = tokenJpaRepository
                .findByNumberAndIdentityAndTenant("token2", "identity2", "tenant2");

        assertNotNull(actualTokens);
        assertEquals(1, actualTokens.size());
        final Token firstToken = actualTokens.get(0);
        assertEquals("id2" , firstToken.getId());
        assertEquals("identity2" , firstToken.getIdentity());
        assertEquals("tenant2" , firstToken.getTenant());
        assertEquals("token2" , firstToken.getNumber());
        assertEquals(Long.valueOf(200) , firstToken.getTimeToLiveInSeconds());
        assertEquals(Long.valueOf(124) , firstToken.getCreatedBy());
        assertEquals("N", firstToken.getValidated());
        assertFalse(firstToken.isValidated());
        assertNull(firstToken.getLastModifiedBy());
        assertNull(firstToken.getLastModifiedDate());
        assertNull(firstToken.getVersion());
        assertNotNull(firstToken.getCreatedDate());
    }

    @Test
    @Sql(scripts = {"/sql/clearTokens.sql", "/sql/createTokens.sql"})
    public void test_should_fetch_token_by_id() {
        final Token token = tokenJpaRepository.findOne("id1");
        assertTrue(token.isValidated());
    }

    @Test
    @Sql(scripts = {"/sql/clearTokens.sql", "/sql/createTokens.sql"})
    @Transactional()
    public void test_should_mark_token_as_validated() {
        final Token token = tokenJpaRepository.findOne("id2");
        assertFalse(token.isValidated());
        final int count = tokenJpaRepository.markTokenAsValidated("id2");
        assertEquals(1, count);
    }
}