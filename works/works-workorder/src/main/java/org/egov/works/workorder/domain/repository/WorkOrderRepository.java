package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.RequestInfo;
import org.egov.works.workorder.web.contract.WorkOrder;
import org.egov.works.workorder.web.contract.WorkOrderSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ritesh on 27/11/17.
 */
@Repository
public class WorkOrderRepository {

    @Autowired
    private WorkOrderJdbcRepository workOrderJdbcRepository;

    public List<WorkOrder> search(final WorkOrderSearchContract workOrderSearchContract, final RequestInfo requestInfo) {
        return workOrderJdbcRepository.searchWorkOrders(workOrderSearchContract, requestInfo);
    }


}
