package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.PositionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private SevaRequest sevaRequest;

    @Test
    public void test_should_set_department_id_to_seva_request() {
        final String designationId = "designationId";
        final String tenantId = "tenantId";
        when(sevaRequest.getTenantId()).thenReturn(tenantId);
        final long assigneeId = 2L;
        when(sevaRequest.getAssignee()).thenReturn(assigneeId);
        when(positionRepository.getDesignationIdForAssignee(tenantId, assigneeId)).thenReturn(designationId);

        departmentService.enrichRequestWithDesignation(sevaRequest);

        verify(sevaRequest).setDesignation(designationId);
    }

}