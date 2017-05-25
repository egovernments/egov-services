package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.model.Position;
import org.egov.pgr.employee.enrichment.repository.contract.*;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PositionsResponseTest {

    @Test
    public void test_should_return_position() {
        final String designationId = "3";
        final DesignationResponse designationResponse = new DesignationResponse(designationId);
        final String departmentId = "4";
        final Long department =4L;
        final PositionResponse positionResponse =
            new PositionResponse(new DepartmentDesignationResponse(designationResponse, department));
        final List<PositionResponse> positions = Collections.singletonList(positionResponse);
        final PositionsResponse positionsResponse = new PositionsResponse(positions);

        final Position position = positionsResponse.toDomain();

        assertNotNull(position);
        assertEquals(departmentId, position.getDepartmentId());
        assertEquals(designationId, position.getDesignationId());
    }
}