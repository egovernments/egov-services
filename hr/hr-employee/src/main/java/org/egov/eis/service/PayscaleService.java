/*
* eGov suite of products aim to improve the internal efficiency,transparency,
* accountability and the service delivery of the government  organizations.
*
*  Copyright (C) 2016  eGovernments Foundation
*
*  The updated version of eGov suite of products as by eGovernments Foundation
*  is available at http://www.egovernments.org
*
*  This program is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with this program. If not, see http://www.gnu.org/licenses/ or
*  http://www.gnu.org/licenses/gpl.html .
*
*  In addition to the terms of the GPL license to be adhered to in using this
*  program, the following additional terms are to be complied with:
*
*      1) All versions of this program, verbatim or modified must carry this
*         Legal Notice.
*
*      2) Any misrepresentation of the origin of the material is prohibited. It
*         is required that all modified versions of this material be marked in
*         reasonable ways as different from the original version.
*
*      3) This license does not grant any rights to any user of the program
*         with regards to rights under trademark law for use of the trade names
*         or trademarks of eGovernments Foundation.
*
*  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
*/

package org.egov.eis.service;


import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.model.PayscaleDetails;
import org.egov.eis.model.PayscaleHeader;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.repository.PayscaleRepository;
import org.egov.eis.service.helper.EmployeeHelper;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.PayscaleGetRequest;
import org.egov.eis.web.contract.PayscaleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class PayscaleService {

    @Autowired
    private PayscaleRepository payscaleRepository;

    public PayscaleHeader create(PayscaleRequest payscaleRequest) {
        PayscaleHeader payscaleHeader = payscaleRequest.getPayscaleHeader();
        RequestInfo requestInfo = payscaleRequest.getRequestInfo();
        Long requesterId = requestInfo.getUserInfo().getId();
        String tenantId = payscaleRequest.getPayscaleHeader().getTenantId();
        populateDataForPayscaleHeader(payscaleHeader, requesterId, tenantId);
        payscaleRepository.savePayscaleHeader(payscaleRequest.getPayscaleHeader());
        payscaleRepository.savePayscaleDetails(payscaleRequest.getPayscaleHeader());
        return payscaleHeader;
    }

    public PayscaleHeader update(PayscaleRequest payscaleRequest) {
        PayscaleHeader payscaleHeader = payscaleRequest.getPayscaleHeader();
        RequestInfo requestInfo = payscaleRequest.getRequestInfo();
        Long requesterId = requestInfo.getUserInfo().getId();
        populateDataForUpdate(payscaleHeader, requesterId);
        payscaleRepository.updatePayscaleHeader(payscaleHeader);
        updatePayscaleDetails(payscaleHeader);
        return payscaleHeader;
    }

    public List<PayscaleHeader> getPayscaleHeaders(final PayscaleGetRequest payscaleGetRequest,
                                                   final RequestInfo requestInfo) {
        return payscaleRepository.findForCriteria(payscaleGetRequest, requestInfo);
    }

    private void populateDataForPayscaleHeader(PayscaleHeader payscaleHeader, Long requesterId, String tenantId) {
        payscaleHeader.setId(payscaleRepository.generateSequence("seq_egeis_payscaleheader"));
        payscaleHeader.setTenantId(tenantId);
        payscaleHeader.setCreatedBy(requesterId);
        payscaleHeader.setLastModifiedBy(requesterId);
        payscaleHeader.getPayscaleDetails().forEach((payscaleDetails) -> {
            populateDataForPayscaleDetails(payscaleDetails, tenantId);
        });
    }

    public void populateDataForPayscaleDetails(PayscaleDetails payscaleDetails, String tenantId) {
        payscaleDetails.setId(payscaleRepository.generateSequence("seq_egeis_payscaledetails"));
        payscaleDetails.setTenantId(tenantId);
    }

    private void populateDataForUpdate(PayscaleHeader payscaleHeader, Long requesterId) {
        payscaleHeader.setLastModifiedBy(requesterId);
        payscaleHeader.setLastModifiedDate(new Date());
        payscaleHeader.getPayscaleDetails().forEach((payscaleDet) -> {
            if (isEmpty(payscaleDet.getId())) {
                payscaleDet.setId(payscaleRepository.generateSequence("seq_egeis_payscaledetails"));
                payscaleDet.setPayscaleHeaderId(payscaleHeader.getId());
            }
        });
    }

    public void updatePayscaleDetails(PayscaleHeader payscaleHeader) {
        List<PayscaleDetails> payscaleDetails = payscaleRepository.findByPayscaleHeaderId(payscaleHeader.getId(), payscaleHeader.getTenantId());
        payscaleHeader.getPayscaleDetails().forEach((payscaleDets) -> {
            if (insertPayDetails(payscaleDets, payscaleDetails)) {
                payscaleRepository.insertPayscaleDetail(payscaleDets);
            } else if (updatePayDetails(payscaleDets, payscaleDetails)) {
                payscaleDets.setTenantId(payscaleHeader.getTenantId());
                payscaleRepository.updatePayscaleDetails(payscaleDets);
            }
        });
        deleteAssignmentsInDBThatAreNotInInput(payscaleHeader.getPayscaleDetails(), payscaleDetails, payscaleHeader.getId(),
                payscaleHeader.getTenantId());
    }

    private boolean insertPayDetails(PayscaleDetails payscaleDetails, List<PayscaleDetails> payscaleDetailsList) {
        for (PayscaleDetails oldPayscale : payscaleDetailsList)
            if (payscaleDetails.getId().equals(oldPayscale.getId()))
                return false;
        return true;
    }

    private boolean updatePayDetails(PayscaleDetails payscaleDetails, List<PayscaleDetails> payscaleDetailsList) {
        for (PayscaleDetails oldPayscale : payscaleDetailsList)
            if (payscaleDetails.equals(oldPayscale))
                return false;
        return true;
    }

    private List<PayscaleDetails> getListOfPayscaleDetailsToDelete(List<PayscaleDetails> inputPayscaleDetails,
                                                                   List<PayscaleDetails> payscaleDetailsFromDb) {
        List<PayscaleDetails> payDetailsToDelete = new ArrayList<>();
        for (PayscaleDetails payDetInDB : payscaleDetailsFromDb) {
            boolean found = false;
            if (!isEmpty(inputPayscaleDetails)) {
                for (PayscaleDetails inputPayscaleDet : inputPayscaleDetails)
                    if (inputPayscaleDet.getId().equals(payDetInDB.getId())) {
                        found = true;
                        break;
                    }
            }
            if (!found)
                payDetailsToDelete.add(payDetInDB);
        }
        return payDetailsToDelete;
    }


    private void deleteAssignmentsInDBThatAreNotInInput(List<PayscaleDetails> inputPayscaleDets,
                                                        List<PayscaleDetails> payscalDetsFromDb, Long payscaleHeaderid, String tenantId) {
        List<PayscaleDetails> payDetsToDelete = getListOfPayscaleDetailsToDelete(inputPayscaleDets, payscalDetsFromDb);
        List<Long> payDetailIdsToDelete = payDetsToDelete.stream().map(PayscaleDetails::getId)
                .collect(Collectors.toList());
        if (!payDetailIdsToDelete.isEmpty()) {
            payscaleRepository.deletePayscaleDetails(payDetailIdsToDelete, payscaleHeaderid, tenantId);
        }
    }

}
