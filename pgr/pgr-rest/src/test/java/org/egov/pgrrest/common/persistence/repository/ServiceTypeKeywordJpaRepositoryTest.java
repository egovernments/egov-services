package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.persistence.entity.ServiceTypeKeyword;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceTypeKeywordJpaRepositoryTest {

    @Autowired
    private ServiceTypeKeywordJpaRepository serviceTypeKeywordJpaRepository;

    @Test
    @Sql(scripts = {
        "/sql/clearKeywords.sql",
        "/sql/insertKeywords.sql"
    })
    public void test_should_fetch_keywords_for_given_service_codes_and_tenant() {
        final List<String> serviceCodes = Arrays.asList("serviceCode1", "serviceCode2");

        final List<ServiceTypeKeyword> actualKeywords = serviceTypeKeywordJpaRepository
            .findByServiceCodeInAndTenantId(serviceCodes, "tenant1");

        assertEquals(3, actualKeywords.size());
    }
}