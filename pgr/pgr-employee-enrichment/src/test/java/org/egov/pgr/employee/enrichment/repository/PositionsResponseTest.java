package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.repository.contract.DepartmentDesignationResponse;
import org.egov.pgr.employee.enrichment.repository.contract.DesignationResponse;
import org.egov.pgr.employee.enrichment.repository.contract.PositionResponse;
import org.egov.pgr.employee.enrichment.repository.contract.PositionsResponse;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PositionsResponseTest {

    @Test
    public void test_should_return_designation_id() {
        final String designationId = "3";
        final DesignationResponse designationResponse = new DesignationResponse(designationId);
        final PositionResponse positionResponse =
            new PositionResponse(new DepartmentDesignationResponse(designationResponse));
        final List<PositionResponse> positions = Collections.singletonList(positionResponse);

        final PositionsResponse positionsResponse = new PositionsResponse(positions);

        assertEquals(designationId, positionsResponse.getDesignationId());
    }
}