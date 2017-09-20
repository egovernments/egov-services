/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.voucher.workflow.consumer;

import java.util.HashMap;

import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.egf.voucher.web.requests.VoucherRequest;
import org.egov.voucher.workflow.repository.MessageQueueRepository;
import org.egov.voucher.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WorkflowListener {

    @Value("${egov.services.egf-voucher.workflow.populated.topic}")
    private String workflowEnrichedTopic;

    @Value("${egov.services.egf-voucher.workflow.populated.key}")
    private String workflowEnrichedKey;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageQueueRepository messageQueueRepository;

    @Autowired
    private WorkflowService workflowService;

    @KafkaListener(id = "${egov.services.egf-voucher.validated.id}", topics = "${egov.services.egf-voucher.validated.topic}", group = "${egov.services.egf-voucher.validated.group}")
    public void process(final HashMap<String, Object> voucherRequestMap) {

        HashMap<String, Object> tlWorkflowEnrichedMap = new HashMap<>();
        VoucherRequest request;

        System.out.println("voucherRequestMap" + voucherRequestMap);

        if (voucherRequestMap.get("voucher_create") != null) {

            request = objectMapper.convertValue(voucherRequestMap.get("voucher_create"), VoucherRequest.class);

            for (final VoucherContract voucherContract : request.getVouchers()) {

                workflowService.enrichWorkflow(voucherContract, request.getRequestInfo());

            }

            tlWorkflowEnrichedMap.put("voucher_create", request);

        } else {

            request = objectMapper.convertValue(voucherRequestMap.get("voucher_update"), VoucherRequest.class);

            for (final VoucherContract voucherContract : request.getVouchers()) {

                workflowService.enrichWorkflow(voucherContract, request.getRequestInfo());

            }

            tlWorkflowEnrichedMap.put("voucher_update", request);

        }

        messageQueueRepository.save(tlWorkflowEnrichedMap);

    }

}
