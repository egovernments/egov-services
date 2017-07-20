package org.egov.workflow.persistence;

import org.egov.workflow.TestConfiguration;
import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.repository.StateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfiguration.class)
public class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;

    @Test
    @Sql(scripts = {"/sql/dropState.sql","/sql/createState.sql","/sql/clearState.sql","/sql/insertState.sql"})
    public void test_should_close_workflow(){
        State state = stateRepository.findOne(1L);
        state.addStateHistory(new StateHistory(state));
        state.setComments("Complaint is closed");
        state.setStatus(State.StateStatus.ENDED);
        state.setSenderName("Harry");
        state.setValue("closed");
        state.setOwnerPosition(2L);
        state.setDateInfo(new Date());
        state.setLastModifiedBy(00L);
        state.setLastModifiedDate(new Date());

        State savedState = stateRepository.save(state);
        assertEquals(State.StateStatus.ENDED,savedState.getStatus());
        assertTrue("Id for state closed",savedState.getId() == 1);
    }

}