package org.egov.persistence.repository;

import org.egov.persistence.entity.Otp;
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

@RunWith(SpringRunner.class)
@DataJpaTest
public class OtpRepositoryTest {

    @Autowired
    private OtpRepository otpRepository;

    @Test
    @Sql(scripts = {"/sql/clearTokens.sql", "/sql/createTokens.sql"})
    public void should_save_otp() {
        final Otp otp = new Otp();
        otp.setId(UUID.randomUUID().toString());
        otp.setNumber("99999");
        otp.setIdentity("someIdentity");
        otp.setTimeToLiveInSeconds(400L);
        otp.setCreatedDate(new Date());
        otp.setCreatedBy(0L);

        otpRepository.save(otp);

        final List<Otp> otps = otpRepository.findAll();
        assertEquals(3, otps.size());
    }
}