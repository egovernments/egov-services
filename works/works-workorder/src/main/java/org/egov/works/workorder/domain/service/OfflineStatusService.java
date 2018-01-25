package org.egov.works.workorder.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.web.contract.DetailedEstimateOfflineStatus;
import org.egov.works.commons.web.contract.LOAOfflineStatuses;
import org.egov.works.workorder.domain.repository.OfflineStatusRepository;
import org.egov.works.workorder.web.contract.OfflineStatusResponse;
import org.egov.works.workorder.web.contract.OfflineStatusSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.egov.works.workorder.config.Constants;

@Service
public class OfflineStatusService {

    @Autowired
    private OfflineStatusRepository offlineStatusRepository;

    public OfflineStatusResponse getOfflineStatus(final String detailedEstimate, final String tenantId,
            final RequestInfo requestInfo) {
        OfflineStatusSearchContract offlineStatusSearchContract = new OfflineStatusSearchContract();
        final List<String> detailedEstimateNumber = new ArrayList<>();
        detailedEstimateNumber.add(detailedEstimate);
        offlineStatusSearchContract.setDetailedEstimateNumbers(detailedEstimateNumber);
        List<String> statuses = new ArrayList<>();
        statuses.add(DetailedEstimateOfflineStatus.L1_TENDER_FINALIZED.toString());
        offlineStatusSearchContract.setStatuses(statuses);
        offlineStatusSearchContract.setObjectType(Constants.DETAILEDESTIMATE_OFFLINE);
        offlineStatusSearchContract.setTenantId(tenantId);

        final OfflineStatusResponse offlineStatusResponse = offlineStatusRepository
                .getOfflineStatus(offlineStatusSearchContract, tenantId, requestInfo);
        return offlineStatusResponse;
    }

    public OfflineStatusResponse getOfflineStatusForWorkOrder(final String letterOfAcceptance, final String tenantId,
            final RequestInfo requestInfo) {
        OfflineStatusSearchContract offlineStatusSearchContract = new OfflineStatusSearchContract();
        final List<String> loaNumber = new ArrayList<>();
        loaNumber.add(letterOfAcceptance);
        offlineStatusSearchContract.setLoaNumbers(loaNumber);
        List<String> statuses = new ArrayList<>();
        statuses.add(LOAOfflineStatuses.AGREEMENT_ORDER_SIGNED.toString());
        offlineStatusSearchContract.setStatuses(statuses);
        offlineStatusSearchContract.setObjectType(Constants.LETTEROFACCEPTANCE_OFFLINE);
        offlineStatusSearchContract.setTenantId(tenantId);

        final OfflineStatusResponse offlineStatusResponse = offlineStatusRepository
                .getOfflineStatusByLoaNumber(offlineStatusSearchContract, tenantId, requestInfo);
        return offlineStatusResponse;
    }
}
