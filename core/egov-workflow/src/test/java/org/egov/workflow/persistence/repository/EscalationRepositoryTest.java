package org.egov.workflow.persistence.repository;

import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EscalationRepositoryTest {

    private static final int DEFAULT_ESCALATION_HOURS = 10;
    @Mock
    private EscalationJpaRepository escalationJpaRepository;

    private EscalationRepository escalationRepository;

    @Before
    public void before() {
        escalationRepository = new EscalationRepository(DEFAULT_ESCALATION_HOURS, escalationJpaRepository);
    }

    @Test
    public void test_should_return_default_escalation_hours_if_configured_value_not_present() {
        final long designationId = 2L;
        final long complaintTypeId = 1L;
        final String tenantId = "tenantId";
        final EscalationHoursSearchCriteria searchCriteria = EscalationHoursSearchCriteria.builder()
            .complaintTypeId(complaintTypeId)
            .designationId(designationId)
            .tenantId(tenantId)
            .build();
        when(escalationJpaRepository.findBy(designationId, complaintTypeId, tenantId))
            .thenReturn(null);

        final int actualEscalationHours = escalationRepository.getEscalationHours(searchCriteria);

        assertEquals(DEFAULT_ESCALATION_HOURS, actualEscalationHours);
    }

    @Test
    public void test_should_return_escalation_hours_when_configured_value_is_present() {
        final long designationId = 2L;
        final long complaintTypeId = 1L;
        final String tenantId = "tenantId";
        final EscalationHoursSearchCriteria searchCriteria = EscalationHoursSearchCriteria.builder()
            .complaintTypeId(complaintTypeId)
            .designationId(designationId)
            .tenantId(tenantId)
            .build();
        when(escalationJpaRepository.findBy(designationId, complaintTypeId, tenantId)).thenReturn(3);

        final int actualEscalationHours = escalationRepository.getEscalationHours(searchCriteria);

        assertEquals(3, actualEscalationHours);
    }
}