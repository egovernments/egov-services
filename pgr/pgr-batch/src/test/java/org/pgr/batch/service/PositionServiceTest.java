package org.pgr.batch.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pgr.batch.repository.PositionRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.service.model.Position;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PositionServiceTest {
    @InjectMocks
    private PositionService positionService;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private ServiceRequest serviceRequest;

    @Test
    public void test_should_set_department_id_to_seva_request() {
        final String designationId = "designationId";
        final String departmentId = "departmentId";
        final String tenantId = "tenantId";
        when(serviceRequest.getTenantId()).thenReturn(tenantId);
        final long assigneeId = 2L;
        when(serviceRequest.getPositionId()).thenReturn(String.valueOf(assigneeId));
        final Position position = new Position(designationId, departmentId);
        when(positionRepository.getDesignationIdForAssignee(tenantId, assigneeId)).thenReturn(position);

        positionService.enrichRequestWithPosition(serviceRequest);

        verify(serviceRequest).update(position);
    }
}