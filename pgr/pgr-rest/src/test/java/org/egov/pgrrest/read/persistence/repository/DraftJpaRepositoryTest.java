package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.read.persistence.entity.Draft;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DraftJpaRepositoryTest {

    @Autowired
    private DraftJpaRepository draftJpaRepository;

    @Test
    @Sql(scripts = {
        "/sql/clearDraft.sql",
        "/sql/insertDraft.sql"
    })
    public void test_should_retrieve_drafts_by_userid_servicecode_tenantId() {
        List<Draft> drafts = draftJpaRepository.findByUserIdAndServiceCodeAndTenantId(73L, "NOC", "default");
        assertNotNull(!drafts.isEmpty());
    }

    @Test
    @Sql(scripts = {
        "/sql/clearDraft.sql",
        "/sql/insertDraft.sql"
    })
    public void test_should_retrieve_drafts_by_userid_tenantId() {
        List<Draft> drafts = draftJpaRepository.findByUserIdAndTenantId(73L, "default");
        assertNotNull(!drafts.isEmpty());
    }

    @Test
    @Sql(scripts = {
        "/sql/clearDraft.sql",
        "/sql/insertDraft.sql"
    })
    public void test_should_delete_draft_by_id() {
        List<Long> draftIdList = Arrays.asList(1L);
        draftJpaRepository.deleteByIdList(draftIdList);
        Draft draftFromDb = draftJpaRepository.findOne(1L);
        assertNull(draftFromDb);
    }
}