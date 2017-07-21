package org.egov.workflow.persistence.repository;

import org.egov.workflow.TestConfiguration;
import org.egov.workflow.persistence.entity.KeywordStatusMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfiguration.class)
public class KeywordStatusMappingJpaRepositoryTest {

    @Autowired
    KeywordStatusMappingJpaRepository keywordStatusMappingJpaRepository;

    @Test
    @Sql(scripts = {
        "/sql/clearKeywordStatusMapping.sql",
        "/sql/insertKeywordStatusMapping.sql"})
    public void test_should_return_status_mapping_based_on_keyword_and_tenantId(){

        List<KeywordStatusMapping> statusList = keywordStatusMappingJpaRepository
            .findByKeywordAndTenantId("Complaint","default");

        assertThat(statusList.size()).isEqualTo(2);
    }
}